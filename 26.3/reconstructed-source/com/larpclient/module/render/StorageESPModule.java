package com.larpclient.module.render;

import com.larpclient.core.StreamProofManager;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.entity.EspRenderer;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.DrawUtil;
import com.larpclient.util.render.ScreenProjection;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeGizmos;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.DropperBlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.entity.SmokerBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class StorageESPModule extends Module {
   private final BooleanSetting chests = this.addSetting(new BooleanSetting("Chests", "Highlight regular and trapped chests", true));
   private final BooleanSetting enderChests = this.addSetting(new BooleanSetting("Ender Chests", "Highlight ender chests", true));
   private final BooleanSetting shulkers = this.addSetting(new BooleanSetting("Shulkers", "Highlight shulker boxes", true));
   private final BooleanSetting barrels = this.addSetting(new BooleanSetting("Barrels", "Highlight barrels", false));
   private final BooleanSetting spawners = this.addSetting(new BooleanSetting("Spawners", "Highlight mob spawners", true));
   private final BooleanSetting hoppers = this.addSetting(new BooleanSetting("Hoppers", "Highlight hoppers", false));
   private final BooleanSetting furnaces = this.addSetting(new BooleanSetting("Furnaces", "Highlight furnaces", false));
   private final BooleanSetting blastFurnaces = this.addSetting(new BooleanSetting("Blast Furnaces", "Highlight blast furnaces", false));
   private final BooleanSetting smokers = this.addSetting(new BooleanSetting("Smokers", "Highlight smokers", false));
   private final BooleanSetting dispensers = this.addSetting(new BooleanSetting("Dispensers", "Highlight dispensers", false));
   private final BooleanSetting droppers = this.addSetting(new BooleanSetting("Droppers", "Highlight droppers", false));
   private final BooleanSetting crafters = this.addSetting(new BooleanSetting("Crafters", "Highlight crafters", false));
   private final BooleanSetting brewingStands = this.addSetting(new BooleanSetting("Brewing Stands", "Highlight brewing stands", false));
   private final BooleanSetting beehives = this.addSetting(new BooleanSetting("Beehives", "Highlight beehives", false));
   private final BooleanSetting decoratedPots = this.addSetting(new BooleanSetting("Decorated Pots", "Highlight decorated pots", false));
   private final BooleanSetting chiseledBookshelves = this.addSetting(new BooleanSetting("Chiseled Bookshelves", "Highlight chiseled bookshelves", false));
   private final BooleanSetting jukeboxes = this.addSetting(new BooleanSetting("Jukeboxes", "Highlight jukeboxes", false));
   private final NumberSetting range = this.addSetting(new NumberSetting("Range", "Loaded storage scan radius", 64.0, 16.0, 128.0, 8.0));
   private final NumberSetting maxBlocks = this.addSetting(new NumberSetting("Max Blocks", "Nearest storage blocks drawn", 256.0, 16.0, 512.0, 16.0));
   private final ModeSetting mode = this.addSetting(new ModeSetting("Mode", "Storage highlight style", "Filled", "Box", "Corners", "Filled"));
   private final BooleanSetting labels = this.addSetting(new BooleanSetting("Labels", "Show storage names above highlighted blocks", true));
   private final NumberSetting labelRange = this.addSetting(
      new NumberSetting("Label Range", "Maximum name-tag distance, within the storage scan range", 128.0, 16.0, 128.0, 8.0)
   );
   private final NumberSetting maxLabels = this.addSetting(new NumberSetting("Max Labels", "Nearest storage name tags drawn", 64.0, 1.0, 128.0, 1.0));
   private final NumberSetting labelScale = this.addSetting(
      new NumberSetting("Label Scale", "Name-tag size on screen, the same at every distance", 1.0, 0.5, 2.0, 0.1)
   );
   private final BooleanSetting labelIcons = this.addSetting(new BooleanSetting("Label Icons", "Show the block item beside each storage name", true));
   private final BooleanSetting hideOverlaps = this.addSetting(new BooleanSetting("Hide Overlaps", "Skip farther name tags that would cover nearer ones", true));
   private final BooleanSetting boxes = this.addSetting(new BooleanSetting("Boxes", "Draw storage highlight boxes", true));
   private final BooleanSetting tracers = this.addSetting(new BooleanSetting("Tracers", "Draw lines to selected storage blocks", false));
   private final ModeSetting tracerOrigin = this.addSetting(
      new ModeSetting("Tracer Origin", "Start at the camera crosshair or player feet", "Crosshair", "Crosshair", "Feet")
   );
   private final NumberSetting tracerRange = this.addSetting(
      new NumberSetting("Tracer Range", "Maximum line distance, within scan range", 64.0, 16.0, 128.0, 8.0)
   );
   private final NumberSetting maxTracers = this.addSetting(new NumberSetting("Max Tracers", "Nearest selected storage lines drawn", 64.0, 1.0, 128.0, 1.0));
   private final EspRenderer espRenderer;
   private final List<BlockEntity> cachedBlocks;
   private final Map<Block, ItemStack> iconCache;
   private ClientLevel lastLevel;
   private int tickCounter;
   private static StorageESPModule instance;
   private static boolean labelsRegistered;
   private static final int labelHeight = 12;
   private static final int scanInterval = 10;
   private static final int maxLabelWidth = 160;
   private static final int labelBackground = 1073741824;
   private static final int white = -1;
   private static final int distanceColor = 14211288;

   public StorageESPModule() {
      super("StorageESP", "Highlight storage blocks", Category.RENDER, -1);
      StorageESPModule var1 = this;
      this.espRenderer = new EspRenderer(var1x -> var1.addSetting(var1x));
      this.cachedBlocks = new ArrayList<>();
      this.iconCache = new HashMap<>();

      for (Setting var2 : List.of(this.labelRange, this.maxLabels, this.labelScale, this.labelIcons, this.hideOverlaps)) {
         var2.visibleWhen(this.labels::getValue);
      }

      for (Setting var6 : List.of(this.tracerOrigin, this.tracerRange, this.maxTracers)) {
         var6.visibleWhen(this.tracers::getValue);
      }

      this.mode.visibleWhen(this.boxes::getValue);
      this.espRenderer
         .drawLine(
            () -> this.boxes.getValue() || this.tracers.getValue() || this.labels.getValue(),
            () -> this.boxes.getValue() || this.tracers.getValue(),
            () -> this.boxes.getValue() || this.tracers.getValue() || this.labels.getValue(),
            () -> this.boxes.getValue() && this.mode.isMode("Filled")
         );

      for (Setting var7 : this.getTypeSettings()) {
         ((BooleanSetting)var7).onChange(var1x -> this.invalidateCache());
      }

      this.range.onChange(var1x -> this.invalidateCache());
      this.maxBlocks.onChange(var1x -> this.invalidateCache());
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1x -> this.renderOverlay());
      instance = this;
      if (!labelsRegistered) {
         labelsRegistered = (boolean)1;
         HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("larp-client", "storage_esp_labels"), (var0, var1x) -> instance.renderLabels(var0, var1x));
      }
   }

   @Override
   public void onEnable() {
      this.tickCounter = 0;
      this.cachedBlocks.clear();
      this.lastLevel = null;
      LarpLog.debug("ESP", "StorageESP enabled; received loaded storage only, tracers={}", this.tracers.getValue());
   }

   @Override
   public void onDisable() {
      LarpLog.debug("ESP", "StorageESP cleanup; discarded {} cached blocks", this.cachedBlocks.size());
      this.cachedBlocks.clear();
      this.iconCache.clear();
      this.lastLevel = null;
   }

   public List<Setting<?>> getTypeSettings() {
      return List.of(
         this.chests,
         this.enderChests,
         this.shulkers,
         this.barrels,
         this.spawners,
         this.hoppers,
         this.furnaces,
         this.blastFurnaces,
         this.smokers,
         this.dispensers,
         this.droppers,
         this.crafters,
         this.brewingStands,
         this.beehives,
         this.decoratedPots,
         this.chiseledBookshelves,
         this.jukeboxes
      );
   }

   public List<Setting<?>> getScanSettings() {
      return List.of(this.range, this.maxBlocks);
   }

   public List<Setting<?>> getIcon() {
      return List.of(this.range, this.boxes, this.mode, this.labels, this.tracers, this.tracerOrigin);
   }

   public List<Setting<?>> getLimitSettings() {
      return List.of(this.maxBlocks, this.labelRange, this.maxLabels, this.tracerRange, this.maxTracers);
   }

   public List<Setting<?>> getTracerSettings() {
      return List.of(this.tracers, this.tracerOrigin, this.tracerRange, this.maxTracers);
   }

   public List<Setting<?>> isStillLoaded() {
      return List.of(this.boxes, this.mode);
   }

   public List<Setting<?>> getBlockName() {
      ArrayList var1 = new ArrayList<>(this.espRenderer.colorSettings());
      var1.addAll(this.espRenderer.outlineSettings());
      var1.addAll(this.espRenderer.fillSettings());
      var1.addAll(List.of(this.labelScale, this.labelIcons, this.hideOverlaps));
      return List.copyOf(var1);
   }

   private void invalidateCache() {
      this.tickCounter = 0;
      this.cachedBlocks.clear();
   }

   public static Vec3 getTracerOrigin(Vec3 var0, Quaternionf var1, Vec3 var2, String var3) {
      if (var3.equals("Feet")) {
         return var2.add(0.0, 0.1, 0.0);
      }

      Vector3f var4 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var1);
      return var0.add(var4.x * 0.5, var4.y * 0.5, var4.z * 0.5);
   }

   public List<Setting<?>> getLabelSettings() {
      return List.of(this.labels, this.labelRange, this.maxLabels, this.labelScale, this.labelIcons, this.hideOverlaps);
   }

   public static String getBlockName(BlockEntity var0) {
      return var0.getBlockState().getBlock().getName().getString();
   }

   public static BlockPos getConnectedChestPos(BlockEntity var0) {
      BlockState var1 = var0.getBlockState();
      return var1.getBlock() instanceof ChestBlock && var1.hasProperty(ChestBlock.TYPE) && var1.getValue(ChestBlock.TYPE) != ChestType.SINGLE
         ? ChestBlock.getConnectedBlockPos(var0.getBlockPos(), var1)
         : null;
   }

   public static String formatName(BlockEntity var0, boolean var1) {
      return var1 ? getBlockName(var0) + " x2" : getBlockName(var0);
   }

   public static String formatLabelSuffix(String var0, double var1) {
      String var3 = (int)var1 + "m";
      return var0 != null && !var0.isBlank() ? var0 + " · " + var3 : var3;
   }

   private boolean getConnectedChestPos(BlockEntity var1) {
      if (var1 instanceof DropperBlockEntity) {
         return this.droppers.getValue();
      } else {
         return var1 instanceof DispenserBlockEntity
            ? this.dispensers.getValue()
            : var1 instanceof ChestBlockEntity && this.chests.getValue()
               || var1 instanceof EnderChestBlockEntity && this.enderChests.getValue()
               || var1 instanceof ShulkerBoxBlockEntity && this.shulkers.getValue()
               || var1 instanceof BarrelBlockEntity && this.barrels.getValue()
               || var1 instanceof SpawnerBlockEntity && this.spawners.getValue()
               || var1 instanceof HopperBlockEntity && this.hoppers.getValue()
               || var1 instanceof FurnaceBlockEntity && this.furnaces.getValue()
               || var1 instanceof BlastFurnaceBlockEntity && this.blastFurnaces.getValue()
               || var1 instanceof SmokerBlockEntity && this.smokers.getValue()
               || var1 instanceof CrafterBlockEntity && this.crafters.getValue()
               || var1 instanceof BrewingStandBlockEntity && this.brewingStands.getValue()
               || var1 instanceof BeehiveBlockEntity && this.beehives.getValue()
               || var1 instanceof DecoratedPotBlockEntity && this.decoratedPots.getValue()
               || var1 instanceof ChiseledBookShelfBlockEntity && this.chiseledBookshelves.getValue()
               || var1 instanceof JukeboxBlockEntity && this.jukeboxes.getValue();
      }
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.lastLevel != minecraft.level) {
            this.cachedBlocks.clear();
            this.lastLevel = minecraft.level;
            this.tickCounter = 0;
         }

         int var10000 = this.tickCounter;
         byte var11 = 1;
         int var10 = this.tickCounter;
         this.tickCounter = var10 + var11;
         var10000 %= 10;
         boolean var13 = false;
         int var12 = var10000;
         var10000 = Integer.signum(var12);
         if (((var10000 | -var10000) >>> 31 ^ 1) != 0) {
            this.cachedBlocks.clear();
            var10000 = (int)Math.ceil(this.range.getValue() / 16.0);
            byte var9 = 1;
            int var8 = var10000;
            int var1 = var8 + var9;
            ChunkPos var2 = minecraft.player.chunkPosition();

            for (int var3 = var2.x() - var1; var3 <= var2.x() + var1; var3++) {
               for (int var4 = var2.z() - var1; var4 <= var2.z() + var1; var4++) {
                  LevelChunk var5 = minecraft.level.getChunkSource().getChunkNow(var3, var4);
                  if (var5 != null) {
                     for (BlockEntity var7 : var5.getBlockEntities().values()) {
                        if (!var7.isRemoved()
                           && this.getConnectedChestPos(var7)
                           && minecraft.player.distanceToSqr(Vec3.atCenterOf(var7.getBlockPos())) <= this.range.getValue() * this.range.getValue()) {
                           this.cachedBlocks.add(var7);
                        }
                     }
                  }
               }
            }

            this.cachedBlocks.sort(Comparator.comparingDouble(var0 -> minecraft.player.distanceToSqr(Vec3.atCenterOf(var0.getBlockPos()))));
            var10000 = this.cachedBlocks.size();
            int var15 = this.maxBlocks.getValue().intValue();
            int var14 = var10000;
            if ((-Integer.compare(var14, var15) >>> 31 ^ 1) == 0) {
               this.cachedBlocks.subList(this.maxBlocks.getValue().intValue(), this.cachedBlocks.size()).clear();
            }
         }
      }
   }

   private boolean isStillLoaded(BlockEntity var1) {
      if (!var1.isRemoved() && this.getConnectedChestPos(var1)) {
         BlockPos var2 = var1.getBlockPos();
         LevelChunk var3 = minecraft.level.getChunkSource().getChunkNow(var2.getX() >> 4, var2.getZ() >> 4);
         return var3 != null && var3.getBlockEntities().get(var2) == var1;
      } else {
         return false;
      }
   }

   private AABB getBounds(BlockEntity var1) {
      BlockPos var2 = var1.getBlockPos();
      VoxelShape var3 = var1.getBlockState().getShape(minecraft.level, var2);
      return (var3.isEmpty() ? new AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0) : var3.bounds()).move(var2).inflate(0.003);
   }

   private int getColor(BlockEntity var1, double var2) {
      int var4 = var1 instanceof SpawnerBlockEntity
         ? -943189
         : (var1 instanceof EnderChestBlockEntity ? -5071873 : (var1 instanceof ShulkerBoxBlockEntity ? -1400362 : -1521772));
      return this.espRenderer.resolveColor(var4, var2, this.range.getValue(), 1.0, var1.getBlockPos().hashCode());
   }

   private void renderOverlay() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.lastLevel == minecraft.level) {
            try {
               int var1 = 0;
               int var2 = 0;
               Camera var3 = minecraft.gameRenderer.mainCamera();
               Vec3 var4 = this.tracers.getValue()
                  ? getTracerOrigin(var3.position(), var3.rotation(), minecraft.player.position(), this.tracerOrigin.getValue())
                  : null;

               for (BlockEntity var6 : this.cachedBlocks) {
                  if (this.isStillLoaded(var6)) {
                     double var7 = minecraft.player.position().distanceTo(Vec3.atCenterOf(var6.getBlockPos()));
                     if (!(var7 >= this.range.getValue())) {
                        if (var1++ >= this.maxBlocks.getValue().intValue()) {
                           break;
                        }

                        AABB var9 = this.getBounds(var6);
                        int var10 = this.getColor(var6, var7);
                        if (this.boxes.getValue()) {
                           this.espRenderer.drawBox(var9, this.mode.getValue(), var10);
                        }

                        if (var4 != null && var7 <= this.tracerRange.getValue() && var2++ < this.maxTracers.getValue().intValue() && var10 >>> 24 != 0) {
                           this.espRenderer.drawLine(var4, var9.getCenter(), var10);
                        }
                     }
                  }
               }
            } catch (RuntimeException var11) {
               LarpLog.errorOnce("ESP", "Storage overlay failed", var11);
            }
         }
      }
   }

   private void renderLabels(GuiGraphicsExtractor var1, DeltaTracker var2) {
      if (this.labels.getValue()
         && this.isRuntimeActive()
         && this.isInGame()
         && this.lastLevel == minecraft.level
         && !minecraft.gameRenderer.gameRenderState().guiRenderState.isHudHidden) {
         var1 = StreamProofManager.selectGraphics(var1, true, false);
         if (var1 != null) {
            long var3 = PerfProfiler.startTimer();

            try {
               Camera var5 = minecraft.gameRenderer.mainCamera();
               Vec3 var6 = var5.position();
               Matrix4f var7 = var5.getViewRotationProjectionMatrix(new Matrix4f());
               Vector3f var8 = var5.rotation().transform(new Vector3f(0.0F, 1.0F, 0.0F));
               int var9 = minecraft.getWindow().getGuiScaledWidth();
               int var10 = minecraft.getWindow().getGuiScaledHeight();
               boolean var11 = RenderSystem.getDevice().getDeviceInfo().isZZeroToOne();
               float var12 = this.labelScale.getValue().floatValue();
               int var13 = this.maxLabels.getValue().intValue();
               HashSet var14 = new HashSet();
               ArrayList var15 = new ArrayList();
               ArrayList var16 = new ArrayList();

               for (BlockEntity var18 : this.cachedBlocks) {
                  if (var16.size() >= var13) {
                     break;
                  }

                  if (this.isStillLoaded(var18)) {
                     BlockPos var19 = var18.getBlockPos();
                     double var20 = minecraft.player.position().distanceTo(Vec3.atCenterOf(var19));
                     if (!(var20 >= this.range.getValue()) && !(var20 > this.labelRange.getValue())) {
                        BlockPos var22 = getConnectedChestPos(var18);
                        AABB var23 = this.getBounds(var18);
                        if (var22 != null) {
                           if (var14.contains(var22)) {
                              continue;
                           }

                           if (minecraft.level.getBlockEntity(var22) instanceof ChestBlockEntity var24 && this.isStillLoaded(var24)) {
                              var23 = var23.minmax(this.getBounds(var24));
                           } else {
                              var22 = null;
                           }
                        }

                        var14.add(var19);
                        Vec3 var43 = new Vec3((var23.minX + var23.maxX) / 2.0, var23.maxY + 0.25, (var23.minZ + var23.maxZ) / 2.0);
                        ScreenProjection.ProjectedPoint var44 = ScreenProjection.projectPoint(var43, var6, var7, var8, var9, var10, var11);
                        if (var44 != null) {
                           int var26 = this.getColor(var18, var20);
                           double var27 = (var26 >>> 24) / 255.0;
                           if (!(var27 < 0.03) && (this.espRenderer.isThroughWalls() || !this.getColor(var6, var43, var19, var22))) {
                              String var29 = var18 instanceof SpawnerBlockEntity var30 ? this.getSpawnerMobName(var30) : null;
                              MutableComponent var45 = Component.literal(this.truncateLabel(formatName(var18, var22 != null)) + " ")
                                 .append(Component.literal(formatLabelSuffix(var29, var20)).withColor(14211288));
                              ItemStack var31 = this.labelIcons.getValue() ? this.getIcon(var18) : ItemStack.EMPTY;
                              int var32 = 1 + (var31.isEmpty() ? 0 : 12) + minecraft.font.width(var45) + 1;
                              float var33 = var44.getX() - var32 * var12 / 2.0F;
                              float var34 = var44.getY() - 12.0F * var12;
                              float[] var35 = new float[]{var33, var34, var33 + var32 * var12, var34 + 12.0F * var12};
                              if (!this.hideOverlaps.getValue()
                                 || !var15.stream()
                                    .anyMatch(
                                       var1x -> var35[0] < var1x[2] + 1.0F
                                          && var35[2] + 1.0F > var1x[0]
                                          && var35[1] < var1x[3] + 1.0F
                                          && var35[3] + 1.0F > var1x[1]
                                    )) {
                                 var15.add(var35);
                                 var16.add(new StorageESPModule.LabelTag(var44.getX(), var44.getY(), var32, var45, var31, var27));
                              }
                           }
                        }
                     }
                  }
               }

               for (int var42 = var16.size() - 1; var42 >= 0; var42--) {
                  this.renderLabel(var1, (StorageESPModule.LabelTag)var16.get(var42), var12);
               }
            } catch (RuntimeException var39) {
               LarpLog.errorOnce("ESP", "Storage name tags failed", var39);
            } finally {
               PerfProfiler.endTimer("StorageESP labels", var3);
            }
         }
      }
   }

   private void renderLabel(GuiGraphicsExtractor var1, StorageESPModule.LabelTag var2, float var3) {
      var1.pose().pushMatrix();

      try {
         var1.pose().translate(this.renderOverlay(var2.screenX() - var2.width() * var3 / 2.0F), this.renderOverlay(var2.screenY() - 12.0F * var3)).scale(var3);
         DrawUtil.fillRect(var1, 0, 0, var2.width(), 12, ColorUtil.multiplyAlpha(1073741824, var2.alpha()));
         byte var4 = 1;
         if (!var2.icon().isEmpty()) {
            if (var2.alpha() > 0.5) {
               var1.pose().pushMatrix();
               var1.pose().translate(var4, 1.0F).scale(0.625F);
               var1.item(var2.icon(), 0, 0);
               var1.pose().popMatrix();
            }

            var4 += 12;
         }

         var1.text(minecraft.font, var2.line(), var4, 2, ColorUtil.multiplyAlpha(-1, var2.alpha()), true);
      } finally {
         var1.pose().popMatrix();
      }
   }

   private String truncateLabel(String var1) {
      return minecraft.font.width(var1) <= 160 ? var1 : minecraft.font.plainSubstrByWidth(var1, 160 - minecraft.font.width("...")) + "...";
   }

   private float renderOverlay(float var1) {
      double var2 = minecraft.getWindow().getGuiScale();
      return (float)(Math.round(var1 * var2) / var2);
   }

   private boolean getColor(Vec3 var1, Vec3 var2, BlockPos var3, BlockPos var4) {
      BlockHitResult var5 = minecraft.level
         .clip(new ClipContext(var1, var2, net.minecraft.world.level.ClipContext.Block.COLLIDER, Fluid.NONE, minecraft.player));
      return var5.getType() != Type.BLOCK ? false : !var5.getBlockPos().equals(var3) && !var5.getBlockPos().equals(var4);
   }

   private ItemStack getIcon(BlockEntity var1) {
      return this.iconCache.computeIfAbsent(var1.getBlockState().getBlock(), var0 -> new ItemStack(var0.asItem()));
   }

   private String getSpawnerMobName(SpawnerBlockEntity var1) {
      try {
         Entity var2 = var1.getSpawner().getOrCreateDisplayEntity(minecraft.level, var1.getBlockPos());
         return var2 == null ? null : var2.getType().getDescription().getString();
      } catch (RuntimeException var3) {
         LarpLog.errorOnce("ESP", "Spawner mob name unavailable; tag shows the block name", var3);
         return null;
      }
   }

   private record LabelTag(float x, float y, int width, Component line, ItemStack icon, double alpha) {
      private final float screenX;
      private final float screenY;

      private LabelTag(float x, float y, int width, Component line, ItemStack icon, double alpha) {
         this.screenX = x;
         this.screenY = y;
         this.width = width;
         this.line = line;
         this.icon = icon;
         this.alpha = alpha;
      }
   }
}
