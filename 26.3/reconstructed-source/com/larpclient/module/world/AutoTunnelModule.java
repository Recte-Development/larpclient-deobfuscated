package com.larpclient.module.world;

import com.larpclient.core.RotationManager;
import com.larpclient.core.ViewController;
import com.larpclient.hud.NotificationManager;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.util.entity.MovementCorrection;
import com.larpclient.util.entity.PlayerInputUtil;
import com.larpclient.util.math.RotationSmoother2;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.world.BlockBreaker;
import com.larpclient.util.world.ChunkScanScheduler;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SplittableRandom;
import java.util.TreeMap;
import java.util.stream.Collectors;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public final class AutoTunnelModule extends WorldAutomationModule {
   private static AutoTunnelModule instance;
   private final BooleanSetting legit = this.addToggleSetting("Legit", "Select the best hotbar tool while smoothly aiming at each block", false);
   private final ModeSetting width = this.addSetting(
      new ModeSetting("Width", "Tunnel cross section; 1x1 is a crawl tunnel that needs a crawling start", "1x2", "1x1", "1x2", "3x3")
   );
   private final NumberSetting reach = this.addSliderSetting("Reach", "How far ahead to mine", 4.0, 2.0, 6.0, 1.0);
   private final BooleanSetting baseFinder = this.addToggleSetting(
      "Base finder", "Walk a fixed heading, avoid hazards and stop at nearby storage clusters", false
   );
   private final NumberSetting hazardScan = this.addSliderSetting("Hazard scan", "Distance ahead to inspect lava, water and drops", 6.0, 3.0, 12.0, 1.0);
   private final BooleanSetting avoidLava = this.addToggleSetting("Avoid lava", "Avoid lava beside, above and below the tunnel", true);
   private final BooleanSetting avoidWater = this.addToggleSetting("Avoid water", "Avoid water that would flood the tunnel", true);
   private final BooleanSetting avoidDrops = this.addToggleSetting("Avoid drops", "Avoid drops of two or more blocks", true);
   private final BooleanSetting avoidGravel = this.addToggleSetting("Avoid gravel", "Avoid falling sand and gravel above the tunnel", false);
   private final NumberSetting detour = this.addSliderSetting("Detour", "Maximum sideways distance for a safe return to the main heading", 4.0, 2.0, 12.0, 1.0);
   private final BooleanSetting chests = this.createStorageSetting("Chests", "Include chests and trapped chests", true);
   private final BooleanSetting barrels = this.createStorageSetting("Barrels", "Include barrels", true);
   private final BooleanSetting enderChests = this.createStorageSetting("Ender Chests", "Include ender chests", true);
   private final BooleanSetting hoppers = this.createStorageSetting("Hoppers", "Include hoppers", true);
   private final BooleanSetting spawners = this.createStorageSetting("Spawners", "Include mob spawners", true);
   private final BooleanSetting shulkerBoxes = this.createStorageSetting("Shulker Boxes", "Include uncolored and all colored shulker boxes", true);
   private final NumberSetting blocksToFlag = this.addSliderSetting(
      "Blocks to flag", "Selected storage blocks within 12 blocks of each other needed for a find", 5.0, 1.0, 64.0, 1.0
   );
   private final BooleanSetting skipDungeons = this.addToggleSetting("Skip dungeons", "Ignore spawners on cobblestone floors or near cobwebs", true);
   private final NumberSetting ignoreNearStart = this.addSliderSetting(
      "Ignore near start", "Ignore finds this close to the starting point", 48.0, 0.0, 256.0, 8.0
   );
   private final ModeSetting onFind = this.addSetting(
      new ModeSetting("On find", "Report and stop, or disconnect after reporting", "Notify and stop", "Notify and stop", "Leave") {
         @Override
         public String getModeLabel(String var1) {
            return var1.equals("Notify and stop") ? "Notify & stop" : var1;
         }

         @Override
         public String getValueAsString() {
            return this.getModeLabel(this.getValue());
         }
      }
   );
   private final BooleanSetting logFinds = this.addToggleSetting(
      "Log finds", "Append time, server, dimension and coordinates to config/larp-client/bases.txt", true
   );
   private final NumberSetting saveToolAt = this.addSliderSetting(
      "Save tool at", "Stop before the selected tool has fewer remaining uses; zero allows breaking", 30.0, 0.0, 200.0, 5.0
   );
   private final NumberSetting stopAtHealth = this.addSliderSetting(
      "Stop at health", "Stop when health reaches this value; zero disables the limit", 8.0, 0.0, 20.0, 1.0
   );
   private final ChunkScanScheduler<LevelChunk> chunkScanner = new ChunkScanScheduler<>();
   private final Map<Long, AutoTunnelModule.CachedScan> chunkCache = new HashMap<>();
   private final List<BlockPos> scanResults = new ArrayList<>();
   private final ArrayDeque<BlockPos> detourQueue = new ArrayDeque<>();
   private ClientLevel level;
   private BlockPos startPos;
   private Vec3 lastPosition;
   private Direction heading;
   private Direction laneDirection;
   private BlockPos lanePos;
   private BlockPos targetBlock;
   private final RotationSmoother2 walker = new RotationSmoother2();
   private final SplittableRandom random = new SplittableRandom();
   private Vec3 walkDirection = Vec3.ZERO;
   private boolean walking;
   private boolean menuOpen;
   private int stuckTicks;
   private BlockPos aimedBlock;
   private Vec3 aimJitter = Vec3.ZERO;
   private double aimHeightOffset = 0.6;
   private final ChunkScanScheduler.ChunkSource<LevelChunk> chunkSource = new ChunkScanScheduler.ChunkSource<LevelChunk>() {
      public LevelChunk loaded(int var1, int var2) {
         return AutoTunnelModule.this.level.getChunkSource().getChunkNow(var1, var2);
      }

      @Override
      public boolean due(int var1, int var2) {
         AutoTunnelModule.CachedScan var3 = AutoTunnelModule.this.chunkCache.get(ChunkPos.pack(var1, var2));
         return var3 == null || AutoTunnelModule.minecraft.player.tickCount - var3.tick >= 80L;
      }

      public int sectionCount(LevelChunk var1) {
         return var1.getSections().length;
      }

      public boolean scanSection(LevelChunk var1, int var2) {
         LevelChunkSection var3 = var1.getSections()[var2];
         return !var3.hasOnlyAir() && var3.maybeHas(AutoTunnelModule.this::getMovementInput);
      }

      @Override
      public void begin() {
         AutoTunnelModule.this.scanResults.clear();
      }

      public void scanSection(LevelChunk var1, int var2) {
         LevelChunkSection var3 = var1.getSections()[var2];

         for (int var4 = 0; var4 < 16; var4++) {
            for (int var5 = 0; var5 < 16; var5++) {
               for (int var6 = 0; var6 < 16; var6++) {
                  BlockState var7 = var3.getBlockState(var6, var4, var5);
                  if (AutoTunnelModule.this.getMovementInput(var7) && AutoTunnelModule.this.scanResults.size() < 256) {
                     BlockPos var8 = new BlockPos(var1.getPos().getMinBlockX() + var6, var1.getMinY() + var2 * 16 + var4, var1.getPos().getMinBlockZ() + var5);
                     if (!(
                           AutoTunnelModule.this.startPos.distSqr(var8)
                              < AutoTunnelModule.this.ignoreNearStart.getValue() * AutoTunnelModule.this.ignoreNearStart.getValue()
                        )
                        && (!AutoTunnelModule.this.skipDungeons.getValue() || !var7.is(Blocks.SPAWNER) || !AutoTunnelModule.this.isDungeonSpawner(var8))) {
                        AutoTunnelModule.this.scanResults.add(var8);
                     }
                  }
               }
            }
         }
      }

      @Override
      public void finish(int var1, int var2) {
         AutoTunnelModule.this.chunkCache
            .put(
               ChunkPos.pack(var1, var2),
               new AutoTunnelModule.CachedScan(List.copyOf(AutoTunnelModule.this.scanResults), AutoTunnelModule.minecraft.player.tickCount)
            );
      }
   };

   private BooleanSetting createStorageSetting(final String var1, String var2, boolean var3) {
      return this.addSetting(new BooleanSetting("Storage " + var1, var2, var3) {
         @Override
         public String getDisplayName() {
            return "Storage: " + var1;
         }
      });
   }

   public AutoTunnelModule() {
      super("Auto Tunnel", "Mine and walk a centered 1x1, 1x2 or 3x3 tunnel, including while menus are open");
      instance = this;

      for (Setting var2 : List.of(
         this.hazardScan,
         this.detour,
         this.chests,
         this.barrels,
         this.enderChests,
         this.hoppers,
         this.spawners,
         this.shulkerBoxes,
         this.blocksToFlag,
         this.ignoreNearStart,
         this.onFind,
         this.logFinds
      )) {
         var2.visibleWhen(this.baseFinder::getValue);
      }

      this.skipDungeons.visibleWhen(() -> this.baseFinder.getValue() && this.spawners.getValue());
      this.baseFinder.onChange(var1 -> this.reset());
      this.width.onChange(var1 -> this.reset());

      for (BooleanSetting var4 : List.of(this.chests, this.barrels, this.enderChests, this.hoppers, this.spawners, this.shulkerBoxes, this.skipDungeons)) {
         var4.onChange(var1 -> {
            this.chunkScanner.reset();
            this.chunkCache.clear();
            this.scanResults.clear();
         });
      }
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.reset();
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.reset();
   }

   @Override
   public boolean allowsScreenInteractions() {
      return true;
   }

   public static boolean isActive() {
      if (instance != null && instance.isEnabled()) {
         AutoTunnelModule var0 = instance;
         Minecraft var1 = Minecraft.getInstance();
         return var0 != null
            && var0.isRuntimeActive()
            && InteractionArbiter.canModuleInteract(var0)
            && var1.gui.overlay() == null
            && var1.player.connection.hasClientLoaded();
      } else {
         return false;
      }
   }

   @Override
   protected void aimAt(Vec3 var1, float var2) {
      if (this.isEnabled()) {
         if (!this.menuOpen) {
            double var3 = Math.min(this.reach.getValue(), minecraft.player.blockInteractionRange());
            Vec3 var5 = var1;
            if (this.targetBlock != null) {
               if (!this.targetBlock.equals(this.aimedBlock)) {
                  this.aimedBlock = this.targetBlock;
                  this.aimJitter = new Vec3(this.random.nextDouble(-0.22, 0.22), this.random.nextDouble(-0.22, 0.22), this.random.nextDouble(-0.22, 0.22));
               }

               Vec3 var6 = var1.add(this.aimJitter);
               if (BlockBreaker.hasLineOfSight(this.targetBlock, var6, var3)) {
                  var5 = var6;
               }
            }

            float[] var9 = RotationManager.calculateAngles(minecraft.player.getEyePosition(), var5);
            int var7 = this.targetBlock != null && BlockBreaker.raycastLook(this.targetBlock, var3) == null ? 0 : 1;
            float[] var8 = this.walker.rotate(minecraft.player.getYRot(), minecraft.player.getXRot(), var9[0], var9[1], (boolean)var7);
            RotationManager.requestForModule(this, RotationManager.Priority.TUNNEL, var8[0], var8[1], 1.0);
         }
      }
   }

   private void reset() {
      InteractionArbiter.findAttackBlocker(this);
      BlockBreaker.release(this);
      this.walking = this.menuOpen = (boolean)0;
      this.laneDirection = this.heading = null;
      this.startPos = null;
      this.lastPosition = null;
      this.lanePos = this.targetBlock = this.aimedBlock = null;
      this.walkDirection = Vec3.ZERO;
      this.walker.reset();
      this.aimHeightOffset = this.random.nextDouble(0.35, 1.1);
      this.detourQueue.clear();
      this.chunkScanner.reset();
      this.chunkCache.clear();
      this.scanResults.clear();
      this.level = null;
      this.stuckTicks = 0;
   }

   @Override
   public void onTick() {
      this.walking = (boolean)0;
      this.walkDirection = Vec3.ZERO;
      if (this.isReady() && !this.isManualMovement()) {
         byte var1 = this.isContainerScreenOpen();
         byte var12 = this.menuOpen;
         byte var11 = var1;
         int var10000 = Integer.compare(var11, var12);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            this.menuOpen = (boolean)var1;
            LarpLog.info(
               this.getName(),
               var1 != 0
                  ? "Inventory menu open; holding position and facing, mining only the block under the crosshair"
                  : "Inventory menu closed; resuming walking and aiming"
            );
            if (var1 == 0) {
               this.walker.reset();
            }
         }

         if (this.level != minecraft.level || this.lastPosition != null && minecraft.player.position().distanceToSqr(this.lastPosition) > 64.0) {
            this.reset();
            this.level = minecraft.level;
            this.startPos = minecraft.player.blockPosition();
            this.heading = Direction.fromYRot(minecraft.player.getYRot());
         }

         Direction var2 = this.heading;
         if (this.stopAtHealth.getValue() > 0.0 && minecraft.player.getHealth() <= this.stopAtHealth.getValue()) {
            this.stop("Low health");
         } else {
            int var19 = this.width.isMode("1x1");
            boolean var14 = false;
            byte var13 = (byte)var19;
            var19 = Integer.signum(var13);
            if (((var19 | -var19) >>> 31 ^ 1) == 0) {
               int var21 = minecraft.player.isVisuallyCrawling();
               boolean var16 = false;
               byte var15 = (byte)var21;
               var21 = Integer.signum(var15);
               if ((var21 | -var21) >>> 31 == 0) {
                  BlockBreaker.release(this);
                  this.walker.reset();
                  this.targetBlock = null;
                  this.setStatus("Waiting: 1x1 needs a crawling start, such as under a trapdoor");
                  return;
               }
            }

            int var23 = this.baseFinder.getValue();
            boolean var18 = false;
            byte var17 = (byte)var23;
            var23 = Integer.signum(var17);
            if (((var23 | -var23) >>> 31 ^ 1) == 0) {
               this.scanForBases();
               if (!this.isRuntimeActive()) {
                  return;
               }

               var2 = this.findHazard();
               if (var2 == null) {
                  return;
               }
            }

            if (this.laneDirection != var2 || this.lanePos == null) {
               this.laneDirection = var2;
               this.lanePos = minecraft.player.blockPosition().relative(var2);
               this.targetBlock = null;
               this.walker.reset();
               LarpLog.info(
                  this.getName(),
                  "Following {} through lane {}, {} at Y {} ({})",
                  var2,
                  this.lanePos.getX(),
                  this.lanePos.getZ(),
                  this.lanePos.getY(),
                  this.width.getValue()
               );
            }

            this.lanePos = advanceLane(this.lanePos, minecraft.player.position(), var2, this.isClear(this.lanePos, var2));
            String var3 = this.findHazard(this.lanePos, var2);
            if (var3 != null) {
               BlockBreaker.release(this);
               this.walker.reset();
               this.targetBlock = null;
               this.setStatus("Waiting: " + var3);
            } else {
               double var4 = Math.min(this.reach.getValue(), minecraft.player.blockInteractionRange());
               BlockPos var6 = this.findTarget(var2, var4);
               if (var6 != null) {
                  if (this.isToolNearlyBroken(var6)) {
                     this.stop("Tool nearly broken");
                     return;
                  }

                  BlockPos var7 = var6;
                  Direction var8 = var2;
                  BlockPos var9 = this.lanePos;
                  boolean var10 = BlockBreaker.breakBlockWithBestTool(
                     this,
                     var6,
                     var4,
                     true,
                     12.0F,
                     this.legit.getValue() || this.baseFinder.getValue(),
                     () -> !this.isManualMovement()
                        && this.findHazard(var9, var8) == null
                        && (!this.baseFinder.getValue() || !minecraft.level.getBlockState(var7).hasBlockEntity())
                  );
                  if (!var10) {
                     this.setStatus("Waiting for a reachable tunnel face");
                  }
               } else {
                  BlockBreaker.release(this);
               }

               this.findTarget(var2, var6);
            }
         }
      } else {
         BlockBreaker.release(this);
         this.walker.reset();
         this.targetBlock = null;
         this.setStatus(this.isManualMovement() ? "Paused for manual movement" : "Waiting for world or interaction control");
      }
   }

   private void findTarget(Direction var1, BlockPos var2) {
      Vec3 var3 = Vec3.atBottomCenterOf(this.lanePos);
      boolean var4 = this.isClear(this.lanePos, var1);
      double var5 = RotationSmoother2.forwardOffset(minecraft.player.position(), var3, var1) - (var4 ? -0.5 : 0.85);
      Vec3 var7 = minecraft.player.getDeltaMovement();
      double var8 = var7.x * var1.getStepX() + var7.z * var1.getStepZ();
      Vec3 var10 = RotationSmoother2.targetOffset(minecraft.player.position(), var3, var1);
      boolean var11 = this.walker.isLatched();
      int var12 = InteractionArbiter.ownsInteraction(this) && !this.menuOpen && !MovementCorrection.isRotationActive() && this.canStep(stepOffset(var10))
         ? 1
         : 0;
      this.walking = this.walker.updateLatch(var5, var8, (boolean)var12);
      this.walkDirection = this.walking ? var10 : Vec3.ZERO;
      if (this.walking != var11) {
         LarpLog.debug(
            this.getName(), this.walking ? "Walk keys pressed; {} clear ahead" : "Walk keys released; {} clear ahead", String.format(Locale.ROOT, "%.2f", var5)
         );
      }

      if (var2 == null && InteractionArbiter.ownsInteraction(this)) {
         Vec3 var13 = var3.add(var1.getStepX() * 6, 0.0, var1.getStepZ() * 6);
         if (minecraft.player.tickCount % 60 == 0 && this.random.nextInt(4) == 0) {
            this.aimHeightOffset = this.random.nextDouble(0.35, 1.1);
         }

         this.aimAt(new Vec3(var13.x, minecraft.player.getEyeY() - this.aimHeightOffset, var13.z), 12.0F);
      }

      if (this.lastPosition != null && minecraft.player.position().distanceToSqr(this.lastPosition) < 1.0E-4 && this.walking && var2 == null) {
         this.stuckTicks++;
      } else {
         this.stuckTicks = 0;
      }

      this.lastPosition = minecraft.player.position();
      if (var2 == null) {
         this.setStatus(
            this.menuOpen
               ? "Holding still while an inventory menu is open"
               : (
                  this.walking
                     ? (this.baseFinder.getValue() ? "Searching for bases" : "Following the cleared tunnel")
                     : "Waiting at a protected or unreachable obstruction"
               )
         );
      }

      if (this.stuckTicks > 160) {
         this.stop("Movement blocked; clear the route or choose a new heading");
      }
   }

   public static BlockPos advanceLane(BlockPos var0, Vec3 var1, Direction var2, boolean var3) {
      return var3 && RotationSmoother2.forwardOffset(var1, Vec3.atBottomCenterOf(var0), var2) < 0.12 ? var0.relative(var2) : var0;
   }

   private boolean isContainerScreenOpen() {
      return minecraft.gui.screen() instanceof AbstractContainerScreen;
   }

   private boolean isManualMovement() {
      return minecraft != null
         && minecraft.player != null
         && minecraft.isWindowActive()
         && minecraft.gui.screen() == null
         && (
            PlayerInputUtil.isKeyDown(minecraft.options.keyUp)
               || PlayerInputUtil.isKeyDown(minecraft.options.keyDown)
               || PlayerInputUtil.isKeyDown(minecraft.options.keyLeft)
               || PlayerInputUtil.isKeyDown(minecraft.options.keyRight)
               || PlayerInputUtil.isKeyDown(minecraft.options.keyJump)
               || PlayerInputUtil.isKeyDown(minecraft.options.keyShift)
         );
   }

   private List<BlockPos> findHazard(BlockPos var1, Direction var2) {
      return tunnelCells(var1, var2, this.width.getValue());
   }

   public static int widthToHeight(String var0) {
      return switch (var0) {
         case "1x1" -> 1;
         case "3x3" -> 3;
         default -> 2;
      };
   }

   public static List<BlockPos> tunnelCells(BlockPos var0, Direction var1, String var2) {
      boolean var3 = var2.equals("3x3");
      int var4 = widthToHeight(var2);
      ArrayList var5 = new ArrayList(var3 ? 9 : var4);

      for (int var6 = 0; var6 < var4; var6++) {
         var5.add(var0.above(var6));
         if (var3) {
            var5.add(var0.relative(var1.getClockWise()).above(var6));
            var5.add(var0.relative(var1.getCounterClockWise()).above(var6));
         }
      }

      return var5;
   }

   private BlockPos findTarget(Direction var1, double var2) {
      for (int var4 = 0; var4 < Math.ceil(var2); var4++) {
         BlockPos var5 = this.lanePos.relative(var1, var4);
         if (!this.detourQueue.isEmpty()
               && RotationSmoother2.forwardOffset(Vec3.atBottomCenterOf(var5), Vec3.atBottomCenterOf((Vec3i)this.detourQueue.peekFirst()), var1) < -0.01
            || this.findHazard(var5, var1) != null) {
            break;
         }

         List var6 = this.findHazard(var5, var1).stream().filter(var3 -> this.isMineable(var3, var2)).toList();
         if (!var6.isEmpty()) {
            if (this.targetBlock != null && var6.contains(this.targetBlock)) {
               return this.targetBlock;
            }

            this.targetBlock = var6.stream().min(Comparator.comparingDouble(this::rotationDistance)).orElseThrow();
            return this.targetBlock;
         }

         if (!this.isClear(var5, var1)) {
            break;
         }
      }

      this.targetBlock = null;
      return null;
   }

   private boolean isMineable(BlockPos var1, double var2) {
      return BlockBreaker.canBreak(var1)
         && minecraft.player.getEyePosition().distanceToSqr(Vec3.atCenterOf(var1)) <= var2 * var2
         && (!this.baseFinder.getValue() || !minecraft.level.getBlockState(var1).hasBlockEntity())
         && BlockBreaker.canBreak(this, var1, var2) != null;
   }

   private double rotationDistance(BlockPos var1) {
      float[] var2 = RotationManager.calculateAngles(minecraft.player.getEyePosition(), Vec3.atCenterOf(var1));
      return Math.hypot(Mth.wrapDegrees(var2[0] - minecraft.player.getYRot()), var2[1] - minecraft.player.getXRot());
   }

   private boolean isClear(BlockPos var1, Direction var2) {
      for (BlockPos var4 : this.findHazard(var1, var2)) {
         if (!minecraft.level.hasChunkAt(var4) || !minecraft.level.getBlockState(var4).getCollisionShape(minecraft.level, var4).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   private static Vec3 stepOffset(Vec3 var0) {
      return var0.normalize().scale(0.55);
   }

   private boolean canStep(Vec3 var1) {
      Vec3 var2 = var1.scale(0.65);
      AABB var3 = minecraft.player.getBoundingBox().expandTowards(var2);
      if (!minecraft.level.noCollision(minecraft.player, var3)) {
         return false;
      }

      BlockPos var4 = BlockPos.containing(minecraft.player.position().add(var2));
      return this.hasHeadroom(var4) && this.findHazard(var4, this.laneDirection) == null;
   }

   private boolean isToolNearlyBroken(BlockPos var1) {
      if (this.saveToolAt.getValue() <= 0.0) {
         return false;
      }

      BlockState var2 = minecraft.level.getBlockState(var1);
      ItemStack var3 = minecraft.player.getMainHandItem();
      float var4 = var3.getDestroySpeed(var2) + (var3.isCorrectToolForDrops(var2) ? 1000 : 0);

      for (int var5 = 0; (this.legit.getValue() || this.baseFinder.getValue()) && var5 < 9; var5++) {
         ItemStack var6 = minecraft.player.getInventory().getItem(var5);
         float var7 = var6.getDestroySpeed(var2) + (var6.isCorrectToolForDrops(var2) ? 1000 : 0);
         if (var7 > var4) {
            var3 = var6;
            var4 = var7;
         }
      }

      return var3.isDamageableItem() && var3.getMaxDamage() - var3.getDamageValue() <= this.saveToolAt.getValue();
   }

   private boolean hasHeadroom(BlockPos var1) {
      if (!minecraft.level.hasChunkAt(var1)) {
         return false;
      }

      int var2 = Math.min(widthToHeight(this.width.getValue()), 2);

      for (int var3 = 0; var3 < var2; var3++) {
         if (!minecraft.level.getBlockState(var1.above(var3)).getCollisionShape(minecraft.level, var1.above(var3)).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   private String findHazard(BlockPos var1, Direction var2) {
      if (!minecraft.level.hasChunkAt(var1)) {
         return "Waiting for loaded terrain";
      }

      for (BlockPos var4 : this.findHazard(var1, var2)) {
         for (Direction var8 : Direction.values()) {
            BlockPos var9 = var4.relative(var8);
            if (!minecraft.level.hasChunkAt(var9)) {
               return "Waiting for loaded terrain";
            }

            FluidState var10 = minecraft.level.getFluidState(var9);
            if (this.avoidLava.getValue() && var10.is(FluidTags.LAVA)) {
               return "Lava";
            }

            if (this.avoidWater.getValue() && var10.is(FluidTags.WATER)) {
               return "Water";
            }
         }

         FluidState var13 = minecraft.level.getFluidState(var4);
         if (this.avoidLava.getValue() && var13.is(FluidTags.LAVA)) {
            return "Lava";
         }

         if (this.avoidWater.getValue() && var13.is(FluidTags.WATER)) {
            return "Water";
         }
      }

      if (this.avoidDrops.getValue()
         && isDeepDrop(
            minecraft.level.getBlockState(var1.below()).getCollisionShape(minecraft.level, var1.below()).isEmpty(),
            minecraft.level.getBlockState(var1.below(2)).getCollisionShape(minecraft.level, var1.below(2)).isEmpty()
         )) {
         return "Deep drop";
      }

      if (this.avoidGravel.getValue()) {
         for (BlockPos var12 : this.findHazard(var1, var2)) {
            if (minecraft.level.getBlockState(var12.above()).getBlock() instanceof FallingBlock) {
               return "Falling sand or gravel";
            }
         }
      }

      return null;
   }

   private Direction findHazard() {
      BlockPos var1 = minecraft.player.blockPosition();

      while (
         !this.detourQueue.isEmpty()
            && minecraft.player
                  .position()
                  .multiply(1.0, 0.0, 1.0)
                  .distanceToSqr(Vec3.atBottomCenterOf((Vec3i)this.detourQueue.peekFirst()).multiply(1.0, 0.0, 1.0))
               < 0.08
      ) {
         this.detourQueue.removeFirst();
      }

      if (!this.detourQueue.isEmpty()) {
         Direction var9 = var1.equals(this.detourQueue.peekFirst()) ? this.laneDirection : directionTo(var1, this.detourQueue.peekFirst());
         String var11 = this.findHazard(var1.relative(var9), var9);
         if (var11 != null) {
            this.stop("Detour blocked by " + var11.toLowerCase(Locale.ROOT));
            return null;
         } else {
            return var9;
         }
      } else {
         String var2 = null;

         for (int var3 = 1; var3 <= this.hazardScan.getValue().intValue(); var3++) {
            var2 = this.findHazard(var1.relative(this.heading, var3), this.heading);
            if (var2 != null) {
               break;
            }
         }

         if (var2 == null) {
            return this.heading;
         }

         if (var2.equals("Waiting for loaded terrain")) {
            BlockBreaker.release(this);
            this.setStatus(var2);
            return null;
         }

         for (int var10 = 1; var10 <= this.detour.getValue().intValue(); var10++) {
            for (Direction var5 : List.of(this.heading.getClockWise(), this.heading.getCounterClockWise())) {
               int var6 = this.hazardScan.getValue().intValue() + 2;
               BlockPos var7 = var1.relative(var5, var10);
               BlockPos var8 = var7.relative(this.heading, var6);
               if (this.isLaneSafe(var1, var5, var10) && this.isLaneSafe(var7, this.heading, var6) && this.isLaneSafe(var8, var5.getOpposite(), var10)) {
                  this.detourQueue.add(var7);
                  this.detourQueue.add(var8);
                  this.detourQueue.add(var1.relative(this.heading, var6));
                  LarpLog.info(this.getName(), "Routing {} blocks around {} using {}", var10, var2, var5);
                  this.setStatus("Detouring around " + var2.toLowerCase(Locale.ROOT));
                  return var5;
               }
            }
         }

         this.stop("No safe detour around " + var2.toLowerCase(Locale.ROOT));
         return null;
      }
   }

   private boolean isLaneSafe(BlockPos var1, Direction var2, int var3) {
      for (int var4 = 1; var4 <= var3; var4++) {
         if (this.findHazard(var1.relative(var2, var4), var2) != null) {
            return false;
         }
      }

      return true;
   }

   private static Direction directionTo(BlockPos var0, BlockPos var1) {
      int var2 = var1.getX() - var0.getX();
      int var3 = var1.getZ() - var0.getZ();
      return Math.abs(var2) > Math.abs(var3) ? (var2 > 0 ? Direction.EAST : Direction.WEST) : (var3 > 0 ? Direction.SOUTH : Direction.NORTH);
   }

   public static Input getMovementInput() {
      AutoTunnelModule var0 = instance;
      if (var0 != null
         && var0.isReady()
         && var0.walking
         && !var0.menuOpen
         && var0.laneDirection != null
         && !ViewController.isFreecam()
         && !MovementCorrection.isRotationActive()
         && !var0.isManualMovement()) {
         Minecraft var1 = Minecraft.getInstance();
         if (var0.level == var1.level
            && !(var0.walkDirection.lengthSqr() < 1.0E-6)
            && var0.canStep(stepOffset(var0.walkDirection))
            && (!RotationManager.hasRequest() || RotationManager.isActive(RotationManager.Priority.TUNNEL))) {
            Input var2 = var0.walker.directionToInput(var1.player.getYRot(), var0.walkDirection);
            return var2.backward() ? null : var2;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static Vec2 isActive() {
      Input var0 = getMovementInput();
      return var0 == null ? null : RotationSmoother2.inputToVector(var0);
   }

   public static boolean isDeepDrop(boolean var0, boolean var1) {
      return var0 && var1;
   }

   public static boolean isWithinCluster(BlockPos var0, BlockPos var1) {
      return Math.abs((long)var1.getX() - var0.getX()) <= 12L
         && Math.abs((long)var1.getY() - var0.getY()) <= 12L
         && Math.abs((long)var1.getZ() - var0.getZ()) <= 12L;
   }

   public static Vec2 strafeVector(float var0, Direction var1) {
      double var2 = Math.toRadians(var0 - var1.toYRot());
      return new Vec2((float)Math.sin(var2), (float)Math.cos(var2));
   }

   private boolean getMovementInput(BlockState var1) {
      Block var2 = var1.getBlock();
      return this.chests.getValue() && (var2 == Blocks.CHEST || var2 == Blocks.TRAPPED_CHEST)
         || this.barrels.getValue() && var2 == Blocks.BARREL
         || this.enderChests.getValue() && var2 == Blocks.ENDER_CHEST
         || this.hoppers.getValue() && var2 == Blocks.HOPPER
         || this.spawners.getValue() && var2 == Blocks.SPAWNER
         || this.shulkerBoxes.getValue() && var2 instanceof ShulkerBoxBlock;
   }

   private boolean isDungeonSpawner(BlockPos var1) {
      BlockState var2 = minecraft.level.getBlockState(var1.below());
      if (!var2.is(Blocks.COBBLESTONE) && !var2.is(Blocks.MOSSY_COBBLESTONE)) {
         for (BlockPos var4 : BlockPos.betweenClosed(var1.offset(-2, -1, -2), var1.offset(2, 1, 2))) {
            if (minecraft.level.hasChunkAt(var4) && minecraft.level.getBlockState(var4).is(Blocks.COBWEB)) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   private void scanForBases() {
      ChunkPos var1 = minecraft.player.chunkPosition();
      int var2 = Math.clamp(minecraft.options.getEffectiveRenderDistance(), 1, 12);
      this.chunkCache
         .entrySet()
         .removeIf(
            var3x -> {
               ChunkPos var4 = ChunkPos.unpack(var3x.getKey());
               return Math.abs(var4.x() - var1.x()) > var2
                  || Math.abs(var4.z() - var1.z()) > var2
                  || this.level.getChunkSource().getChunkNow(var4.x(), var4.z()) == null;
            }
         );
      this.chunkScanner.tick(this.chunkSource, var1.x(), var1.z(), var2, 2);
      if (minecraft.player.tickCount % 10 == 0) {
         ArrayList var3 = new ArrayList();

         for (AutoTunnelModule.CachedScan var5 : this.chunkCache.values()) {
            for (BlockPos var7 : var5.positions) {
               if (var3.size() >= 4096) {
                  break;
               }

               if (minecraft.level.hasChunkAt(var7)
                  && this.getMovementInput(minecraft.level.getBlockState(var7))
                  && this.startPos.distSqr(var7) >= this.ignoreNearStart.getValue() * this.ignoreNearStart.getValue()
                  && (!this.skipDungeons.getValue() || !minecraft.level.getBlockState(var7).is(Blocks.SPAWNER) || !this.isDungeonSpawner(var7))) {
                  var3.add(var7);
               }
            }
         }

         HashMap var16 = new HashMap();

         for (BlockPos var19 : var3) {
            var16.computeIfAbsent(stop(var19.getX(), var19.getY(), var19.getZ()), var0 -> new ArrayList<>()).add(var19);
         }

         for (BlockPos var20 : var3) {
            ArrayList var21 = new ArrayList();
            int var8 = Math.floorDiv(var20.getX(), 12);
            int var9 = Math.floorDiv(var20.getY(), 12);
            int var10 = Math.floorDiv(var20.getZ(), 12);

            for (int var11 = var8 - 1; var11 <= var8 + 1; var11++) {
               for (int var12 = var9 - 1; var12 <= var9 + 1; var12++) {
                  for (int var13 = var10 - 1; var13 <= var10 + 1; var13++) {
                     for (BlockPos var15 : var16.getOrDefault(var11 + ":" + var12 + ":" + var13, List.of())) {
                        if (isWithinCluster(var20, var15)) {
                           var21.add(var15);
                        }
                     }
                  }
               }
            }

            if (var21.size() >= this.blocksToFlag.getValue()) {
               this.isClear(var20, var21);
               return;
            }
         }
      }
   }

   private static String stop(int var0, int var1, int var2) {
      return Math.floorDiv(var0, 12) + ":" + Math.floorDiv(var1, 12) + ":" + Math.floorDiv(var2, 12);
   }

   private void isClear(BlockPos var1, List<BlockPos> var2) {
      TreeMap var3 = new TreeMap();

      for (BlockPos var5 : var2) {
         var3.merge(BuiltInRegistries.BLOCK.getKey(minecraft.level.getBlockState(var5).getBlock()).getPath(), 1, Integer::sum);
      }

      String var11 = var1.getX() + ", " + var1.getY() + ", " + var1.getZ();
      String var12 = var11 + " (" + var3.entrySet().stream().map(var0 -> var0.getValue() + " " + (String)var0.getKey()).collect(Collectors.joining(", ")) + ")";
      minecraft.player.sendSystemMessage(Component.literal("Larp Auto Tunnel: base found at " + var12));
      NotificationManager.getInstance().notify("Base found", var12, NotificationManager.Type.SUCCESS, 10000L);
      minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.PLAYER_LEVELUP, 1.4F));
      LarpLog.info(this.getName(), "Base found at {}", var12);
      if (this.logFinds.getValue()) {
         Path var6 = FabricLoader.getInstance().getConfigDir().resolve("larp-client/bases.txt");
         ServerData var7 = minecraft.getCurrentServer();
         String var8 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            + "  "
            + (var7 == null ? "singleplayer" : var7.ip)
            + "  "
            + minecraft.level.dimension().identifier()
            + "  "
            + var12
            + System.lineSeparator();

         try {
            Files.createDirectories(var6.getParent());
            Files.writeString(var6, var8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
         } catch (IOException var10) {
            LarpLog.error(this.getName(), "Could not append base find to " + var6, var10);
         }
      }

      boolean var13 = this.onFind.isMode("Leave");
      this.disable();
      if (var13) {
         minecraft.disconnectFromWorld(Component.literal("Larp Auto Tunnel found a base at " + var12));
      }
   }

   private void stop(String var1) {
      LarpLog.info(this.getName(), "Stopped: {}", var1);
      minecraft.player.sendSystemMessage(Component.literal("Larp Auto Tunnel stopped: " + var1));
      NotificationManager.getInstance().notifyDefault("Auto Tunnel stopped", var1, NotificationManager.Type.WARNING);
      this.disable();
      this.setStatus(var1);
   }

   private record CachedScan(List<BlockPos> positions, long tick) {
   }
}
