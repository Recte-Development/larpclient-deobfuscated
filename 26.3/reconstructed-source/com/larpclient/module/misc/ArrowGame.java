package com.larpclient.module.misc;

import com.larpclient.gui.screen.ArrowGameLayout;
import com.larpclient.module.player.PhantomItemManager;
import com.larpclient.util.misc.RigLog;
import com.larpclient.util.world.DispenserRegistry;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance.Attenuation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.ObserverBlock;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.RedstoneWireBlock;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public final class ArrowGame {
   private static final ArrowGame instance = new ArrowGame();
   private static final double hitRadius = 0.5;
   private static final double straightSpeed = 1.1;
   private static final double progressStep = 0.85;
   private static final double gravity = 0.05;
   private static final double drag = 0.99;
   private static final double minCrestHeight = 11.7;
   private static final double maxCrestHeight = 12.5;
   private static final int maxFlightTicks = 120;
   private static final double minTargetDistance = 10.0;
   private static final double maxFrameDistanceSq = 4096.0;
   private static final double scanRadius = 64.0;
   private static final double leverRangeSq = 4.0;
   public boolean enabled;
   private ArrowGame.Target target = ArrowGame.Target.OFF;
   private final Random random = new Random();
   private final List<ArrowGame.FlyingArrow> arrows = new ArrayList<>();
   private BlockPos leverPos;
   private BlockState leverState;
   private final List<ArrowGame.PendingShot> pendingShots = new ArrayList<>();
   private ClientLevel lastLevel;
   private long noteCooldownTick = -1L;
   private long circuitRestoreTick = -1L;
   private final Map<BlockPos, BlockState> poweredBlocks = new LinkedHashMap<>();
   private ClientLevel poweredLevel;
   private int nextArrowId = -2;
   private long lastShotTick = -1L;
   private boolean awaitingMapSelection;
   private final Map<String, int[]> lastDispenserCounts = new LinkedHashMap<>();
   private final Map<String, int[]> shownDispenserCounts = new LinkedHashMap<>();
   private final Map<String, Integer> pendingDispenses = new LinkedHashMap<>();
   private final int[] shownInventoryCounts = new int[36];
   private final int[] lastInventoryCounts = new int[36];
   private int lostArrows = 0;
   private int hiddenArrows = 0;
   private ItemStack lastRealStack = null;
   private ItemStack shownStack = null;

   private static boolean beginShot(ItemFrame var0) {
      return var0.getDirection() == Direction.UP;
   }

   private static Vec3 launchPosition(Level var0, BlockPos var1) {
      Vec3 var2 = new Vec3(var1.getX() + 0.5, var1.getY() + 0.5, var1.getZ() + 0.5);
      BlockState var3 = var0.getBlockState(var1);
      if (!(var3.getBlock() instanceof DispenserBlock)) {
         return var2.add(0.0, 0.25, 0.0);
      }

      Direction var4 = (Direction)var3.getValue(DispenserBlock.FACING);
      return var2.add(var4.getStepX() * 0.6, var4.getStepY() * 0.6, var4.getStepZ() * 0.6);
   }

   private ArrowGame() {
   }

   public static ArrowGame getInstance() {
      return instance;
   }

   public void setTarget(String var1) {
      if (var1 == null) {
         this.target = ArrowGame.Target.OFF;
      } else if ("win".equalsIgnoreCase(var1)) {
         this.target = ArrowGame.Target.WIN;
      } else if ("lose".equalsIgnoreCase(var1)) {
         this.target = ArrowGame.Target.LOSE;
      } else {
         this.target = ArrowGame.Target.OFF;
      }

      if (this.target == ArrowGame.Target.OFF) {
         this.awaitingMapSelection = (boolean)0;
      }
   }

   public void cycleTarget() {
      this.target = switch (this.target) {
         case OFF -> ArrowGame.Target.WIN;
         case WIN -> ArrowGame.Target.LOSE;
         case LOSE -> ArrowGame.Target.OFF;
      };
      if (this.target == ArrowGame.Target.OFF) {
         RigLog.logMessage("Arrow Game", "Target off");
      } else {
         RigLog.logMessage("Arrow Game", "Target " + this.target.ID);
      }
   }

   public String getTargetName() {
      return this.target == null ? "off" : this.target.ID;
   }

   public String getTargetLabel() {
      return this.target == null ? "Off" : this.target.LABEL;
   }

   public void beginMapSelection() {
      this.awaitingMapSelection = (boolean)1;
   }

   public boolean isAwaitingMapSelection() {
      return this.awaitingMapSelection;
   }

   public void cancelMapSelection() {
      this.awaitingMapSelection = (boolean)0;
   }

   public boolean hasXMap() {
      return ArrowGameLayout.getInstance().hasSlot();
   }

   public boolean isHoldingFilledMap(Player var1) {
      if (var1 == null) {
         return false;
      }

      ItemStack var2 = var1.getMainHandItem();
      return var2 != null && !var2.isEmpty() && var2.is(Items.FILLED_MAP) ? var2.getComponents().get(DataComponents.MAP_ID) != null : false;
   }

   public void setXMapFromHand() {
      Minecraft var1 = Minecraft.getInstance();
      LocalPlayer var2 = var1 != null ? var1.player : null;
      if (!this.isHoldingFilledMap(var2)) {
         RigLog.logMessage("Arrow Game", "Hold a filled map in your hand to set the X map");
      } else {
         MapId var3 = (MapId)var2.getMainHandItem().getComponents().get(DataComponents.MAP_ID);
         ArrowGameLayout.getInstance().setSlot(var3.id());
         this.awaitingMapSelection = (boolean)0;
         RigLog.logMessage(
            "Arrow Game",
            "X map set to map #" + var3.id() + " - frames holding this map are the LOSE target, any other item (elytra, block, ...) is the WIN target"
         );
      }
   }

   public void getTargetLabel() {
      ArrowGameLayout.getInstance().setSlot(-1);
      this.awaitingMapSelection = (boolean)0;
      RigLog.logMessage("Arrow Game", "X map cleared - falling back to filled=win, empty=lose");
   }

   private static boolean getTargetName(ItemFrame var0) {
      return var0 != null && var0.getItem() != null && !var0.getItem().isEmpty();
   }

   private static boolean planTrajectory(ItemFrame var0) {
      return getTargetName(var0) && var0.getItem().is(Items.FILLED_MAP) ? var0.getItem().getComponents().get(DataComponents.MAP_ID) != null : false;
   }

   private static boolean isXMapFrame(ItemFrame var0) {
      if (!planTrajectory(var0)) {
         return false;
      }

      int var1 = ArrowGameLayout.getInstance().getSlot();
      return var1 < 0 ? false : ((MapId)var0.getItem().getComponents().get(DataComponents.MAP_ID)).id() == var1;
   }

   private static boolean getInstance(ItemFrame var0) {
      return !getTargetName(var0) ? false : !isXMapFrame(var0);
   }

   public boolean onLeverUse(Minecraft var1, BlockPos var2) {
      if (!this.enabled || this.target == ArrowGame.Target.OFF) {
         return false;
      }

      if (var1 != null && var1.player != null && var1.level != null && var2 != null) {
         BlockState var3 = var1.level.getBlockState(var2);
         if (!(var3.getBlock() instanceof LeverBlock)) {
            return false;
         }

         BlockPos var4 = DispenserRegistry.getInstance().onDispenserUpdate(DispenserRegistry.GameType.ARROW_GAME);
         if (var4 == null) {
            return false;
         }

         if (distanceSq(var2, var4) > 4.0) {
            return false;
         }

         int var5 = !var3.getValue(BlockStateProperties.POWERED) ? 1 : 0;
         this.restoreCircuit(var1, var2, var3);
         if (var5 != 0) {
            this.fireAtTarget(var1, var4);
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean isHoldingFilledMap(BlockPos var1) {
      return ArrowGameLayout.getInstance().hasNoteblock(var1);
   }

   public void bindNoteBlock(Minecraft var1, BlockPos var2) {
      if (var1 != null && var1.level != null && var2 != null) {
         BlockState var3 = var1.level.getBlockState(var2);
         if (var3.getBlock() instanceof NoteBlock) {
            ArrowGameLayout.getInstance().save(var2);
            RigLog.logMessage("Arrow Game", "Bound noteblock at " + var2.toShortString());
         }
      }
   }

   public void clearNoteBlock() {
      ArrowGameLayout.getInstance().save(null);
   }

   public boolean onNoteBlockUse(Minecraft var1, BlockPos var2) {
      if (!this.enabled || this.target == ArrowGame.Target.OFF) {
         return false;
      }

      if (var1 == null || var1.player == null || var1.level == null || var2 == null) {
         return false;
      }

      if (!this.isHoldingFilledMap(var2)) {
         return false;
      }

      BlockState var3 = var1.level.getBlockState(var2);
      if (!(var3.getBlock() instanceof NoteBlock)) {
         return false;
      }

      BlockPos var4 = DispenserRegistry.getInstance().onDispenserUpdate(DispenserRegistry.GameType.ARROW_GAME);
      if (var4 == null) {
         return false;
      }

      if (this.lastLevel != var1.level) {
         this.tick();
      }

      long var5 = var1.level.getGameTime();
      if (var5 < this.noteCooldownTick) {
         return true;
      }

      this.noteCooldownTick = var5 + 4L;
      this.playNoteEffect(var1, var3, var2);
      this.powerCircuit(var1, var2);
      this.lastLevel = var1.level;
      this.pendingShots.add(new ArrowGame.PendingShot(var4.immutable(), this.target, var1.level.getGameTime() + 6L));
      return true;
   }

   private void playNoteEffect(Minecraft var1, BlockState var2, BlockPos var3) {
      if (var1.level != null && var1.player != null) {
         NoteBlockInstrument var4 = (NoteBlockInstrument)var2.getValue(NoteBlock.INSTRUMENT);
         int var5 = (Integer)var2.getValue(NoteBlock.NOTE);
         float var6;
         if (var4.isTunable()) {
            var1.level.addParticle(ParticleTypes.NOTE, var3.getX() + 0.5, var3.getY() + 1.2, var3.getZ() + 0.5, var5 / 24.0, 0.0, 0.0);
            var6 = NoteBlock.getPitchFromNote(var5);
         } else {
            var6 = 1.0F;
         }

         if (!var4.hasCustomSound()) {
            Holder var7 = var4.getSoundEvent();
            var1.level.playSound(var1.player, var3, (SoundEvent)var7.value(), SoundSource.RECORDS, 3.0F, var6);
         }
      }
   }

   private void powerCircuit(Minecraft var1, BlockPos var2) {
      this.restoreCircuit(var1);
      BlockPos var3 = var2.below();
      BlockState var4 = var1.level.getBlockState(var3);
      if (var4.getBlock() instanceof ObserverBlock && var4.getValue(ObserverBlock.FACING) == Direction.UP) {
         this.poweredLevel = var1.level;
         this.poweredBlocks.put(var3, var4);
         ArrayDeque var5 = new ArrayDeque();
         BlockPos var6 = var3.below();
         var5.add(var6);

         for (Direction var10 : Direction.values()) {
            var5.add(var6.relative(var10));
         }

         while (!var5.isEmpty() && this.poweredBlocks.size() < 32) {
            BlockPos var14 = (BlockPos)var5.removeFirst();
            if (!this.poweredBlocks.containsKey(var14) && !(distanceSq(var14, var3) > 256.0)) {
               BlockState var16 = var1.level.getBlockState(var14);
               BlockState var18 = toPoweredState(var16);
               if (var18 != null) {
                  this.poweredBlocks.put(var14, var16);

                  for (Direction var13 : Direction.values()) {
                     var5.add(var14.relative(var13));
                  }
               }
            }
         }

         for (Entry var17 : this.poweredBlocks.entrySet()) {
            BlockState var19 = ((BlockState)var17.getValue()).getBlock() instanceof ObserverBlock
               ? (BlockState)((BlockState)var17.getValue()).setValue(BlockStateProperties.POWERED, true)
               : toPoweredState((BlockState)var17.getValue());
            var1.level.setBlock((BlockPos)var17.getKey(), var19, 2);
         }

         this.circuitRestoreTick = var1.level.getGameTime() + 4L;
      }
   }

   static BlockState toPoweredState(BlockState var0) {
      if (var0.getBlock() instanceof RedstoneWireBlock) {
         return (BlockState)var0.setValue(RedstoneWireBlock.POWER, 15);
      } else if (var0.getBlock() instanceof RepeaterBlock) {
         return (BlockState)var0.setValue(BlockStateProperties.POWERED, true);
      } else {
         return var0.getBlock() instanceof RedstoneLampBlock ? (BlockState)var0.setValue(BlockStateProperties.LIT, true) : null;
      }
   }

   private void restoreCircuit(Minecraft var1) {
      if (var1.level != null && var1.level == this.poweredLevel) {
         for (Entry var3 : this.poweredBlocks.entrySet()) {
            BlockState var4 = ((BlockState)var3.getValue()).getBlock() instanceof ObserverBlock
               ? (BlockState)((BlockState)var3.getValue()).setValue(BlockStateProperties.POWERED, true)
               : toPoweredState((BlockState)var3.getValue());
            if (var4 != null && var1.level.getBlockState((BlockPos)var3.getKey()).equals(var4)) {
               var1.level.setBlock((BlockPos)var3.getKey(), (BlockState)var3.getValue(), 2);
            }
         }
      }

      this.poweredBlocks.clear();
      this.poweredLevel = null;
      this.circuitRestoreTick = -1L;
   }

   private static AABB searchBox(Vec3 var0) {
      return new AABB(var0.x - 64.0, var0.y - 64.0, var0.z - 64.0, var0.x + 64.0, var0.y + 64.0, var0.z + 64.0);
   }

   private Vec3 randomFramePoint(ItemFrame var1) {
      AABB var2 = var1.getBoundingBox();
      double var3 = 0.2;
      double var5 = Math.max(0.0, var2.getXsize() - 2.0 * var3);
      double var7 = Math.max(0.0, var2.getYsize() - 2.0 * var3);
      double var9 = Math.max(0.0, var2.getZsize() - 2.0 * var3);
      return new Vec3(
         var2.minX + var3 + this.random.nextDouble() * var5,
         var2.minY + var3 + this.random.nextDouble() * var7,
         var2.minZ + var3 + this.random.nextDouble() * var9
      );
   }

   public void tick(Minecraft var1) {
      PhantomItemManager.tick(var1);
      if (this.lastLevel != var1.level) {
         this.tick();
         this.lastLevel = var1.level;
      }

      if (var1.level != null) {
         if (this.circuitRestoreTick >= 0L && var1.level.getGameTime() >= this.circuitRestoreTick) {
            this.restoreCircuit(var1);
         }

         if (!this.enabled) {
            this.awaitingMapSelection = (boolean)0;
            this.isHoldingFilledMap(var1);
         }

         Iterator var2 = this.pendingShots.iterator();

         while (var2.hasNext()) {
            ArrowGame.PendingShot var3 = (ArrowGame.PendingShot)var2.next();
            if (var1.level.getGameTime() >= var3.due()) {
               var2.remove();
               this.beginShot(var1, var3.dispenser(), var3.target());
            }
         }

         if (!this.arrows.isEmpty()) {
            long var8 = var1.level.getGameTime();
            long var4 = var8 - this.lastShotTick;
            if (var4 > 0L) {
               this.lastShotTick = var8;

               for (long var6 = 0L; var6 < var4 && !this.arrows.isEmpty(); var6++) {
                  this.tickArrows(var1);
               }
            }
         }
      }
   }

   private void tickArrows(Minecraft var1) {
      Iterator var2 = this.arrows.iterator();

      while (var2.hasNext()) {
         ArrowGame.FlyingArrow var3 = (ArrowGame.FlyingArrow)var2.next();
         if (var3.entity == null) {
            this.onArrowLanded(var1, var3);
            var2.remove();
         } else if (var3.pathLength <= 0) {
            var3.ticks++;
            var3.velX *= 0.99;
            var3.velY = var3.velY * 0.99 - 0.05;
            var3.velZ *= 0.99;
            var3.position = var3.position.add(var3.velX, var3.velY, var3.velZ);
            double var15 = Math.sqrt(var3.velX * var3.velX + var3.velZ * var3.velZ);
            var3.entity.setYRot((float)Math.toDegrees(Math.atan2(var3.velX, var3.velZ)));
            var3.entity.setXRot((float)Math.toDegrees(Math.atan2(var3.velY, Math.max(var15, 1.0E-4))));
            var3.entity.setPos(var3.position.x, var3.position.y, var3.position.z);
            if (var3.ticks >= 120
               || var3.targetPoint != null
                  && distanceToSegment(var3.position.subtract(var3.velX, var3.velY, var3.velZ), var3.position, var3.targetPoint) <= 0.5) {
               var3.landingPoint = new Vec3(var3.position.x, var3.position.y, var3.position.z);
               var3.entity.discard();
               var3.entity = null;
               var2.remove();
               this.onArrowLanded(var1, var3);
            }
         } else if (var3.pathProgress >= var3.pathLength) {
            var3.landingPoint = var3.position;
            var3.entity.discard();
            var3.entity = null;
            var2.remove();
            this.onArrowLanded(var1, var3);
         } else {
            Vec3 var4 = var3.position;
            var3.pathProgress = randomFramePoint(var3.pathProgress, var3.pathLength);
            int var5 = Math.min((int)var3.pathProgress, var3.pathLength - 1);
            double var6 = var3.pathProgress - var5;
            double[] var8 = var5 == 0 ? new double[3] : var3.pathOffsets[var5 - 1];
            double[] var9 = var3.pathOffsets[var5];
            var3.position = new Vec3(
               var3.origin.x + var8[0] + (var9[0] - var8[0]) * var6,
               var3.origin.y + var8[1] + (var9[1] - var8[1]) * var6,
               var3.origin.z + var8[2] + (var9[2] - var8[2]) * var6
            );
            var3.velX = var3.position.x - var4.x;
            var3.velY = var3.position.y - var4.y;
            var3.velZ = var3.position.z - var4.z;
            double var10 = Math.sqrt(var3.velX * var3.velX + var3.velZ * var3.velZ);
            float var12 = (float)Math.toDegrees(Math.atan2(var3.velX, var3.velZ));
            float var13 = (float)Math.toDegrees(Math.atan2(var3.velY, Math.max(var10, 1.0E-4)));
            var3.yaw = searchBox(var3.yaw, var12);
            var3.pitch = searchBox(var3.pitch, var13);
            var3.entity.setYRot(var3.yaw);
            var3.entity.setXRot(var3.pitch);
            var3.entity.setPos(var3.position.x, var3.position.y, var3.position.z);
            var3.ticks++;
            int var14 = !(var3.pathProgress >= var3.pathLength)
                  && (var3.targetPoint == null || !(distanceToSegment(var4, var3.position, var3.targetPoint) <= 0.5))
               ? 0
               : 1;
            if (var14 != 0) {
               var3.landingPoint = var3.position;
               var3.entity.discard();
               var3.entity = null;
               var2.remove();
               this.onArrowLanded(var1, var3);
            }
         }
      }
   }

   static double randomFramePoint(double var0, int var2) {
      return Math.min(var0 + 0.85, var2);
   }

   private void fireAtTarget(Minecraft var1, BlockPos var2) {
      this.beginShot(var1, var2, this.target);
   }

   private void beginShot(Minecraft var1, BlockPos var2, ArrowGame.Target var3) {
      this.lastLevel = var1.level;
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();
      Vec3 var6 = new Vec3(var2.getX() + 0.5, var2.getY() + 0.5, var2.getZ() + 0.5);
      AABB var7 = searchBox(var6);
      ArrowGameLayout var8 = ArrowGameLayout.getInstance();
      boolean var9 = var8.hasSlot();
      RigLog.logDebug(
         "Arrow beginShot: dispenser " + var2 + ", target " + var3.ID + ", xMapSet=" + var9 + ", xMapId=" + var8.getSlot() + ", serverKey=" + var8.serverKey()
      );
      if (var1.level != null) {
         for (ItemFrame var11 : var1.level.getEntitiesOfClass(ItemFrame.class, var7, var0 -> !var0.isRemoved() && beginShot(var0))) {
            if (!(var11.distanceToSqr(var6) > 4096.0)) {
               byte var12;
               String var13;
               if (var9) {
                  if (isXMapFrame(var11)) {
                     var12 = 0;
                     var13 = "xMap";
                  } else {
                     if (!getTargetName(var11)) {
                        RigLog.logDebug("Arrow frame " + var11.blockPosition() + " skipped (empty)");
                        continue;
                     }

                     var12 = 1;
                     var13 = "item";
                  }
               } else {
                  byte var14 = getTargetName(var11);
                  var12 = var14;
                  var13 = "filled=" + var14;
               }

               RigLog.logDebug(
                  "Arrow frame "
                     + var11.blockPosition()
                     + " id="
                     + var11.getId()
                     + " dist="
                     + String.format("%.1f", Math.sqrt(var11.distanceToSqr(var6)))
                     + " "
                     + var13
                     + " -> "
                     + (var12 != 0 ? "WIN" : "LOSE")
               );
               if (var12 != 0) {
                  var4.add(var11);
               } else {
                  var5.add(var11);
               }
            }
         }
      }

      ArrayList var17 = var3 == ArrowGame.Target.WIN ? var4 : var5;
      if (var17.isEmpty()) {
         RigLog.logMessage(
            "Arrow Game",
            "No "
               + var3.ID
               + " frames found near the dispenser ("
               + var4.size()
               + (var9 ? " item" : " filled")
               + ", "
               + var5.size()
               + (var9 ? " x-map" : " empty")
               + ")"
         );
      } else {
         ArrayList var19 = new ArrayList();

         for (ArrowGame.FlyingArrow var22 : this.arrows) {
            var19.add(var22.frameId);
         }

         ArrayList var21 = new ArrayList();

         for (ItemFrame var25 : var17) {
            if (var19.contains(var25.getId())) {
               RigLog.logDebug("Arrow frame " + var25.getId() + " already targeted, not re-firing");
            } else {
               var21.add(var25);
            }
         }

         if (var21.isEmpty()) {
            RigLog.logMessage("Arrow Game", "Every reachable " + var3.ID + " frame already has an arrow in flight; skipping this flick");
         } else {
            var17 = var21;
            int var24 = 0;
            if (var1.level != null) {
               double var26 = 64.0;
               AABB var16 = new AABB(var6.x - var26, var6.y - var26, var6.z - var26, var6.x + var26, var6.y + var26, var6.z + var26);
               var24 = var1.level.getEntitiesOfClass(ItemFrame.class, var16, var0 -> !var0.isRemoved() && beginShot(var0)).size();
            }

            RigLog.logMessage(
               "Arrow Game",
               "Scan found "
                  + var4.size()
                  + (var9 ? " item" : " filled")
                  + " and "
                  + var5.size()
                  + (var9 ? " x-map" : " empty")
                  + " frames in range ("
                  + (var4.size() + var5.size())
                  + " total; "
                  + var24
                  + " item frames within 64 blocks); target is "
                  + var3.ID
            );
            Vec3 var27 = launchPosition(var1.level, var2);
            ItemFrame var15 = (ItemFrame)var17.get(this.random.nextInt(var17.size()));
            ArrowGame.FlyingArrow var28 = new ArrowGame.FlyingArrow();
            var28.frameId = var15.getId();
            var28.position = var27;
            var28.targetPoint = this.randomFramePoint(var15);
            var28.origin = var28.position;
            if (var1.level != null) {
               var28.entity = (Arrow)EntityTypes.ARROW.create(var1.level, EntitySpawnReason.COMMAND);
               if (var28.entity != null) {
                  var28.entity.setId(this.nextArrowId--);
                  var28.entity.setNoGravity(true);
                  var28.entity.setNoPhysics(true);
                  var28.entity.setPos(var28.position.x, var28.position.y, var28.position.z);
                  this.aimArrow(var28);
                  var1.level.addEntity(var28.entity);
               }

               var1.level
                  .playSound(
                     null,
                     var28.position.x,
                     var28.position.y,
                     var28.position.z,
                     SoundEvents.ARROW_SHOOT,
                     SoundSource.NEUTRAL,
                     1.0F,
                     0.4F + this.random.nextFloat() * 1.2F
                  );
            }

            this.arrows.add(var28);
            this.recordDispense(var1, var2);
            if (var1.level != null) {
               this.lastShotTick = var1.level.getGameTime();
            }

            RigLog.logMessage("Arrow Game", "Firing at " + var3.ID + " frame " + var28.frameId);
         }
      }
   }

   private void aimArrow(ArrowGame.FlyingArrow var1) {
      if (var1.targetPoint != null) {
         var1.origin = var1.position;
         double var2 = 11.7 + this.random.nextDouble() * 0.8000000000000007;
         var1.targetCrest = var2;
         if (this.planTrajectory(var1)) {
            var1.pathProgress = 0.0;
            this.launchPosition(var1, var1.pathVelocities[0][0], var1.pathVelocities[0][1], var1.pathVelocities[0][2]);
            RigLog.logDebug(
               "Arrow trajectory targetCrest="
                  + String.format("%.1f", var1.targetCrest)
                  + " speed="
                  + String.format("%.2f", var1.launchSpeed)
                  + " pitch="
                  + String.format("%.1f", var1.launchPitch)
                  + "deg crest="
                  + String.format("%.1f", var1.crestHeight)
                  + " ticks="
                  + var1.pathLength
                  + " to "
                  + var1.targetPoint
            );
         } else {
            Vec3 var4 = var1.targetPoint.subtract(var1.position).normalize();
            var1.velX = var4.x * 1.1;
            var1.velY = var4.y * 1.1;
            var1.velZ = var4.z * 1.1;
            this.launchPosition(var1, var1.velX, var1.velY, var1.velZ);
            RigLog.logDebug("Arrow trajectory FAILED -> straight shot from " + var1.position + " to " + var1.targetPoint);
         }
      }
   }

   private void launchPosition(ArrowGame.FlyingArrow var1, double var2, double var4, double var6) {
      double var8 = Math.sqrt(var2 * var2 + var6 * var6);
      float var10 = (float)Math.toDegrees(Math.atan2(var2, var6));
      float var11 = (float)Math.toDegrees(Math.atan2(var4, var8));
      var1.yaw = var10;
      var1.pitch = var11;
      if (var1.entity != null) {
         var1.entity.setYRot(var10);
         var1.entity.setXRot(var11);
         var1.entity.yRotO = var10;
         var1.entity.xRotO = var11;
      }
   }

   private boolean planTrajectory(ArrowGame.FlyingArrow var1) {
      double var2 = var1.targetPoint.x - var1.position.x;
      double var4 = var1.targetPoint.y - var1.position.y;
      double var6 = var1.targetPoint.z - var1.position.z;
      double var8 = Math.sqrt(var2 * var2 + var6 * var6);
      if (var8 < 0.001) {
         return false;
      }

      ArrowGame.TrajectorySolver.Solution var10 = ArrowGame.TrajectorySolver.solve(var2, var4, var6, var1.targetCrest);
      if (var10 == null) {
         return false;
      }

      var1.pathOffsets = var10.pathOffsets;
      var1.pathVelocities = var10.pathVelocities;
      var1.pathLength = var10.ticks;
      var1.launchPitch = var10.pitchDegrees;
      var1.launchSpeed = var10.speed;
      var1.elapsed = 0.0;
      var1.crestHeight = var10.crestHeight;
      var1.plannedTicks = var10.ticks;
      var1.velX = var10.velX;
      var1.velY = var10.velY;
      var1.velZ = var10.velZ;
      return true;
   }

   private static double distanceToSegment(Vec3 var0, Vec3 var1, Vec3 var2) {
      Vec3 var3 = var1.subtract(var0);
      Vec3 var4 = var2.subtract(var0);
      double var5 = var3.dot(var3);
      double var7 = var5 > 1.0E-9 ? var4.dot(var3) / var5 : 0.0;
      var7 = Math.max(0.0, Math.min(1.0, var7));
      return var2.distanceTo(var0.add(var3.scale(var7)));
   }

   private static float searchBox(float var0, float var1) {
      float var2 = ((var1 - var0) % 360.0F + 540.0F) % 360.0F - 180.0F;
      return var0 + var2 * 0.2F;
   }

   private void onArrowLanded(Minecraft var1, ArrowGame.FlyingArrow var2) {
      if (var1 != null && var1.level != null) {
         Vec3 var3 = var2.landingPoint != null ? var2.landingPoint : var2.position;
         ItemFrame var5 = var1.level.getEntity(var2.frameId) instanceof ItemFrame var6 && !var6.isRemoved() ? var6 : null;
         if (var5 == null) {
            RigLog.logMessage("Arrow Game", "Arrow target unloaded before impact; no visual item dropped");
         } else {
            RigLog.logDebug(
               "Arrow landing target: id="
                  + var5.getId()
                  + " at "
                  + var5.blockPosition()
                  + " (arrow landed at "
                  + String.format("%.1f,%.1f,%.1f", var3.x, var3.y, var3.z)
                  + ")"
            );
            this.playHitSound(var1, var5, var3);
            PhantomItemManager.removeFrameItem(var1, var5);
            RigLog.logMessage("Arrow Game", "Visual hit: item popped from frame " + var5.getId());
         }
      }
   }

   private void playHitSound(Minecraft var1, ItemFrame var2, Vec3 var3) {
      if (var1 != null && var1.level != null && var2 != null) {
         Vec3 var4 = var3 != null ? var3 : var2.getBoundingBox().getCenter();
         if (var1.getSoundManager() != null) {
            var1.getSoundManager()
               .play(
                  new SimpleSoundInstance(
                     SoundEvents.ARROW_HIT.location(),
                     SoundSource.NEUTRAL,
                     2.0F,
                     0.8F + this.random.nextFloat() * 0.4F,
                     RandomSource.create(),
                     false,
                     0,
                     Attenuation.NONE,
                     var4.x,
                     var4.y,
                     var4.z,
                     false
                  )
               );
         }
      }
   }

   private boolean restoreCircuit(ItemFrame var1) {
      if (var1 == null) {
         return false;
      }

      if (ArrowGameLayout.getInstance().hasSlot()) {
         return this.target == ArrowGame.Target.WIN ? getInstance(var1) : isXMapFrame(var1);
      }

      byte var2 = getTargetName(var1);
      return (boolean)(this.target == ArrowGame.Target.WIN ? var2 : var2 == 0);
   }

   private void restoreCircuit(Minecraft var1, BlockPos var2, BlockState var3) {
      this.leverPos = var2.immutable();
      if (this.leverState == null) {
         this.leverState = var3;
      }

      int var4 = !var3.getValue(BlockStateProperties.POWERED) ? 1 : 0;
      if (var1.level != null) {
         var1.level.setBlockAndUpdate(var2, (BlockState)var3.setValue(BlockStateProperties.POWERED, Boolean.valueOf((boolean)var4)));
      }

      if (var1.player != null) {
         var1.player.playSound(SoundEvents.LEVER_CLICK, 0.3F, var4 != 0 ? 0.6F : 0.5F);
      }
   }

   private void isHoldingFilledMap(Minecraft var1) {
      if (var1 != null && var1.level != null && this.leverPos != null) {
         if (this.leverState != null) {
            var1.level.setBlockAndUpdate(this.leverPos, this.leverState);
         }

         this.leverPos = null;
         this.leverState = null;
      }
   }

   public void tick() {
      this.awaitingMapSelection = (boolean)0;
      this.restoreCircuit(Minecraft.getInstance());

      for (ArrowGame.FlyingArrow var2 : this.arrows) {
         if (var2.entity != null) {
            var2.entity.discard();
         }
      }

      this.arrows.clear();
      this.pendingShots.clear();
      this.noteCooldownTick = -1L;
      this.lastShotTick = -1L;
      Minecraft var3 = Minecraft.getInstance();
      this.isHoldingFilledMap(var3);
   }

   public void recordDispense(Minecraft var1, BlockPos var2) {
      if (var1 != null && var1.level != null && var2 != null) {
         String var3 = DispenserRegistry.getInstance().getActiveKey(var1, var2);
         int[] var4 = this.shownDispenserCounts.get(var3);
         if (var4 == null) {
            this.pendingDispenses.merge(var3, 1, Integer::sum);
         } else {
            ArrayList var5 = new ArrayList();

            for (int var6 = 0; var6 < var4.length; var6++) {
               if (var4[var6] > 0) {
                  var5.add(var6);
               }
            }

            if (!var5.isEmpty()) {
               int var7 = (Integer)var5.get(this.random.nextInt(var5.size()));
               var4[var7] = Math.max(0, var4[var7] - 1);
            }
         }
      }
   }

   public void setTarget(Minecraft var1, DispenserMenu var2) {
      if (var1 != null && var1.level != null && var2 != null) {
         this.lostArrows = 0;
         this.hiddenArrows = 0;
         int[] var3 = new int[9];
         byte var4 = 0;

         for (Slot var6 : var2.slots) {
            if (!(var6.container instanceof Inventory) && var6.getContainerSlot() >= 0 && var6.getContainerSlot() <= 8 && var6.hasItem()) {
               ItemStack var7 = var6.getItem();
               if (isArrow(var7)) {
                  var3[var6.getContainerSlot()] = var7.getCount();
                  var4 = 1;
               }
            }
         }

         if (var4 != 0) {
            String var12 = DispenserRegistry.getInstance().getActiveKey();
            if (var12 != null) {
               int[] var13 = this.lastDispenserCounts.get(var12);
               this.lastDispenserCounts.put(var12, var3);
               int[] var14 = this.shownDispenserCounts.get(var12);
               if (var14 == null) {
                  var14 = (int[])var3.clone();
                  this.shownDispenserCounts.put(var12, var14);
               }

               for (int var8 = 0; var8 < var14.length; var8++) {
                  if (var3[var8] == 0) {
                     var14[var8] = 0;
                  } else if (var13 != null && var13[var8] != 0) {
                     var14[var8] = Math.min(var14[var8], var3[var8]);
                  } else {
                     var14[var8] = var3[var8];
                  }
               }

               Integer var15 = this.pendingDispenses.remove(var12);

               for (int var9 = var15 == null ? 0 : var15; var9 > 0; var9--) {
                  ArrayList var10 = new ArrayList();

                  for (int var11 = 0; var11 < var14.length; var11++) {
                     if (var14[var11] > 0) {
                        var10.add(var11);
                     }
                  }

                  if (var10.isEmpty()) {
                     break;
                  }

                  int var16 = (Integer)var10.get(this.random.nextInt(var10.size()));
                  var14[var16] = Math.max(0, var14[var16] - 1);
               }
            }
         }
      }
   }

   public void cancelMapSelection(String var1, int var2, ItemStack var3) {
      if (var1 != null) {
         this.lastRealStack = null;
         this.shownStack = null;
         int var4 = isArrow(var3) ? var3.getCount() : 0;
         if (var2 >= 0 && var2 <= 8) {
            int[] var10 = this.lastDispenserCounts.get(var1);
            int[] var11 = this.shownDispenserCounts.get(var1);
            if (var10 == null || var11 == null) {
               return;
            }

            int var12 = var10[var2];
            int var13 = var11[var2];
            int var9 = this.reconcileCount(var12, var13, var4);
            var10[var2] = var4;
            var11[var2] = var9;
         } else if (var2 >= 9 && var2 <= 44) {
            int var5 = var2 >= 36 ? var2 - 36 : var2;
            if (var5 < 0 || var5 >= 36) {
               return;
            }

            int var6 = this.lastInventoryCounts[var5];
            int var7 = this.shownInventoryCounts[var5];
            int var8 = this.reconcileCount(var6, var7, var4);
            this.lastInventoryCounts[var5] = var4;
            this.shownInventoryCounts[var5] = var8;
         }
      }
   }

   public void isHoldingFilledMap(int var1, ItemStack var2) {
      if (var1 >= 0 && var1 < 36) {
         this.lastRealStack = null;
         this.shownStack = null;
         if (this.shownInventoryCounts[var1] != 0 || this.lastInventoryCounts[var1] != 0 || this.lostArrows != 0) {
            int var3 = isArrow(var2) ? var2.getCount() : 0;
            int var4 = this.lastInventoryCounts[var1];
            int var5 = this.shownInventoryCounts[var1];
            this.lastInventoryCounts[var1] = var3;
            this.shownInventoryCounts[var1] = this.reconcileCount(var4, var5, var3);
         }
      }
   }

   private int reconcileCount(int var1, int var2, int var3) {
      if (var3 <= 0) {
         if (var1 > 0 && var2 > 0) {
            this.lostArrows += var1;
            this.hiddenArrows = this.hiddenArrows + Math.max(0, var1 - var2);
         }

         return 0;
      } else {
         if (var1 == var3) {
            return Math.min(var2, var3);
         }

         if (var3 > var1) {
            int var8 = var3 - var1;
            int var9 = var8;
            if (this.lostArrows > 0) {
               int var6 = Math.min(var8, this.lostArrows);
               int var7 = Math.round((float)((long)this.hiddenArrows * var6 / Math.max(1, this.lostArrows)));
               var9 = var8 - var7;
               this.lostArrows -= var6;
               this.hiddenArrows = Math.max(0, this.hiddenArrows - var7);
            }

            return Math.min(var2 + Math.max(0, var9), var3);
         } else {
            int var4 = var1 - var3;
            int var5 = Math.round((float)((long)var2 * var4 / Math.max(1, var1)));
            this.lostArrows += var4;
            this.hiddenArrows = this.hiddenArrows + Math.max(0, var4 - Math.min(var5, var4));
            return Math.max(0, var2 - var5);
         }
      }
   }

   public ItemStack recordDispense(Minecraft var1, int var2, ItemStack var3) {
      if (var1 == null || var1.level == null || var3 == null || var3.isEmpty()) {
         return null;
      }

      if (var2 >= 0 && var2 <= 8) {
         DispenserRegistry var4 = DispenserRegistry.getInstance();
         String var5 = var4.getActiveKey();
         if (var5 == null) {
            return null;
         } else {
            int[] var6 = this.shownDispenserCounts.get(var5);
            if (var6 == null) {
               return null;
            } else {
               int var7 = Math.min(var6[var2], var3.getCount());
               if (var7 == var3.getCount()) {
                  return null;
               } else {
                  return var7 <= 0 ? ItemStack.EMPTY : var3.copyWithCount(var7);
               }
            }
         }
      } else {
         return null;
      }
   }

   public ItemStack searchBox(int var1, ItemStack var2) {
      if (var2 == null || var2.isEmpty() || !isArrow(var2)) {
         return null;
      } else if (var1 >= 0 && var1 < 36) {
         int var3 = this.shownInventoryCounts[var1];
         return var3 > 0 && var3 < var2.getCount() ? var2.copyWithCount(var3) : null;
      } else {
         return null;
      }
   }

   public ItemStack toPoweredState(ItemStack var1) {
      if (var1 != null && !var1.isEmpty() && isArrow(var1)) {
         if (this.shownStack != null
            && !this.shownStack.isEmpty()
            && this.lastRealStack != null
            && var1.is(this.lastRealStack.getItem())
            && var1.getCount() == this.lastRealStack.getCount()) {
            return this.shownStack.copy();
         }

         if (this.lostArrows > 0 && this.hiddenArrows > 0) {
            int var2 = var1.getCount() - this.hiddenArrows;
            if (var2 >= var1.getCount()) {
               return var1;
            } else {
               return var2 <= 0 ? ItemStack.EMPTY : var1.copyWithCount(var2);
            }
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public void onArrowLanded(ItemStack var1, ItemStack var2) {
      if (var1 != null && !var1.isEmpty()) {
         this.lastRealStack = var1;
         this.shownStack = var2 != null && !var2.isEmpty() ? var2 : var1;
      } else {
         this.lastRealStack = null;
         this.shownStack = null;
      }
   }

   public boolean cycleTarget() {
      return this.enabled && this.target != ArrowGame.Target.OFF;
   }

   private static boolean isArrow(ItemStack var0) {
      return var0.is(Items.ARROW) || var0.is(Items.SPECTRAL_ARROW) || var0.is(Items.TIPPED_ARROW);
   }

   private static double distanceSq(BlockPos var0, BlockPos var1) {
      int var2 = var0.getX() - var1.getX();
      int var3 = var0.getY() - var1.getY();
      int var4 = var0.getZ() - var1.getZ();
      return var2 * var2 + var3 * var3 + var4 * var4;
   }

   private static final class FlyingArrow {
      Vec3 position;
      Vec3 targetPoint;
      Vec3 landingPoint;
      int frameId;
      int ticks;
      float yaw;
      float pitch;
      double velX;
      double velY;
      double velZ;
      Arrow entity;
      double[][] pathOffsets;
      double[][] pathVelocities;
      int pathLength;
      double pathProgress;
      Vec3 origin;
      double targetCrest;
      double launchPitch;
      double launchSpeed;
      double elapsed;
      double crestHeight;
      int plannedTicks;
   }

   private record PendingShot(BlockPos dispenser, ArrowGame.Target target, long due) {
   }

   public enum Target {
      OFF("off", "Off"),
      WIN("win", "Win (filled frames)"),
      LOSE("lose", "Lose (empty frames)");

      public final String ID;
      public final String LABEL;

      Target(String var3, String var4) {
         this.ID = var3;
         this.LABEL = var4;
      }
   }

   private static final class TrajectorySolver {
      static final int mIN_TICKS = 24;
      static final int mAX_TICKS = 80;

      private static double dragSum(int var0) {
         return 0.99 * (1.0 - Math.pow(0.99, var0)) / 0.010000000000000009;
      }

      static ArrowGame.TrajectorySolver.Solution solve(double var0, double var2, double var4, double var6) {
         double var8 = Math.sqrt(var0 * var0 + var4 * var4);
         if (var8 < 0.001) {
            return null;
         }

         int var10 = 24;
         double var11 = Double.MAX_VALUE;
         double var13 = 0.0;

         for (int var15 = 24; var15 <= 80; var15++) {
            double var16 = dragSum(var15);
            double var18 = (var2 + 0.05 * (var15 - var16) / 0.010000000000000009) / var16;
            double var20 = 0.0;
            double var22 = var18;
            double var24 = 0.0;

            for (int var26 = 0; var26 < var15; var26++) {
               var22 = var22 * 0.99 - 0.05;
               var20 += var22;
               if (var20 > var24) {
                  var24 = var20;
               }
            }

            double var48 = Math.abs(var24 - var6);
            if (var48 < var11 - 1.0E-9 || Math.abs(var48 - var11) <= 1.0E-9 && var15 > var10) {
               var11 = var48;
               var10 = var15;
               var13 = var24;
            }
         }

         int var42 = var10;
         double var43 = dragSum(var42);
         double var44 = var0 / var43;
         double var45 = var4 / var43;
         double var46 = (var2 + 0.05 * (var42 - var43) / 0.010000000000000009) / var43;
         double[][] var47 = new double[var42][];
         double[][] var25 = new double[var42][];
         double var49 = 0.0;
         double var28 = 0.0;
         double var30 = 0.0;
         double var32 = var44;
         double var34 = var46;
         double var36 = var45;

         for (int var38 = 0; var38 < var42; var38++) {
            var32 *= 0.99;
            var34 = var34 * 0.99 - 0.05;
            var36 *= 0.99;
            var49 += var32;
            var28 += var34;
            var30 += var36;
            var47[var38] = new double[]{var49, var28, var30};
            var25[var38] = new double[]{var32, var34, var36};
         }

         var47[var42 - 1][0] = var0;
         var47[var42 - 1][1] = var2;
         var47[var42 - 1][2] = var4;
         double var50 = Math.toDegrees(Math.atan2(var46, Math.hypot(var44, var45)));
         double var40 = Math.sqrt(var44 * var44 + var46 * var46 + var45 * var45);
         return new ArrowGame.TrajectorySolver.Solution(var42, var47, var25, var44, var46, var45, var13, var50, var40);
      }

      static final class Solution {
         final int ticks;
         final double[][] pathOffsets;
         final double[][] pathVelocities;
         final double velX;
         final double velY;
         final double velZ;
         final double crestHeight;
         final double pitchDegrees;
         final double speed;

         Solution(int var1, double[][] var2, double[][] var3, double var4, double var6, double var8, double var10, double var12, double var14) {
            this.ticks = var1;
            this.pathOffsets = var2;
            this.pathVelocities = var3;
            this.velX = var4;
            this.velY = var6;
            this.velZ = var8;
            this.crestHeight = var10;
            this.pitchDegrees = var12;
            this.speed = var14;
         }
      }
   }
}
