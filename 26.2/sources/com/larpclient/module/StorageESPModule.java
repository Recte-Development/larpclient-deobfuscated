package com.larpclient.module;

import com.larpclient.k7;
import com.larpclient.kv;
import com.larpclient.p2;
import com.larpclient.uh;
import com.larpclient.z4;
import com.larpclient.zj;
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
   private final s g = this.addSetting(new s("Chests", "Highlight regular and trapped chests", true));
   private final s U = this.addSetting(new s("Ender Chests", "Highlight ender chests", true));
   private final s p = this.addSetting(new s("Shulkers", "Highlight shulker boxes", true));
   private final s v = this.addSetting(new s("Barrels", "Highlight barrels", false));
   private final s w = this.addSetting(new s("Spawners", "Highlight mob spawners", true));
   private final s s = this.addSetting(new s("Hoppers", "Highlight hoppers", false));
   private final s d = this.addSetting(new s("Furnaces", "Highlight furnaces", false));
   private final s o = this.addSetting(new s("Blast Furnaces", "Highlight blast furnaces", false));
   private final s V = this.addSetting(new s("Smokers", "Highlight smokers", false));
   private final s x = this.addSetting(new s("Dispensers", "Highlight dispensers", false));
   private final s H = this.addSetting(new s("Droppers", "Highlight droppers", false));
   private final s D = this.addSetting(new s("Crafters", "Highlight crafters", false));
   private final s L = this.addSetting(new s("Brewing Stands", "Highlight brewing stands", false));
   private final s A = this.addSetting(new s("Beehives", "Highlight beehives", false));
   private final s Q = this.addSetting(new s("Decorated Pots", "Highlight decorated pots", false));
   private final s R = this.addSetting(new s("Chiseled Bookshelves", "Highlight chiseled bookshelves", false));
   private final s O = this.addSetting(new s("Jukeboxes", "Highlight jukeboxes", false));
   private final e M = this.addSetting(new e("Range", "Loaded storage scan radius", 64.0, 16.0, 128.0, 8.0));
   private final e n = this.addSetting(new e("Max Blocks", "Nearest storage blocks drawn", 256.0, 16.0, 512.0, 16.0));
   private final l c = this.addSetting(new l("Mode", "Storage highlight style", "Filled", "Box", "Corners", "Filled"));
   private final s b = this.addSetting(new s("Labels", "Show storage names above highlighted blocks", true));
   private final e J = this.addSetting(new e("Label Range", "Maximum name-tag distance, within the storage scan range", 128.0, 16.0, 128.0, 8.0));
   private final e h = this.addSetting(new e("Max Labels", "Nearest storage name tags drawn", 64.0, 1.0, 128.0, 1.0));
   private final e X = this.addSetting(new e("Label Scale", "Name-tag size on screen, the same at every distance", 1.0, 0.5, 2.0, 0.1));
   private final s W = this.addSetting(new s("Label Icons", "Show the block item beside each storage name", true));
   private final s T = this.addSetting(new s("Hide Overlaps", "Skip farther name tags that would cover nearer ones", true));
   private final s K = this.addSetting(new s("Boxes", "Draw storage highlight boxes", true));
   private final s N = this.addSetting(new s("Tracers", "Draw lines to selected storage blocks", false));
   private final l j = this.addSetting(new l("Tracer Origin", "Start at the camera crosshair or player feet", "Crosshair", "Crosshair", "Feet"));
   private final e k = this.addSetting(new e("Tracer Range", "Maximum line distance, within scan range", 64.0, 16.0, 128.0, 8.0));
   private final e f = this.addSetting(new e("Max Tracers", "Nearest selected storage lines drawn", 64.0, 1.0, 128.0, 1.0));
   private final z4 Y;
   private final List<BlockEntity> i;
   private final Map<Block, ItemStack> S;
   private ClientLevel F;
   private int q;
   private static StorageESPModule Z;
   private static boolean P;
   private static final int y = 12;
   private static final int t = 10;
   private static final int C = 160;
   private static final int z = 1073741824;
   private static final int u = -1;
   private static final int r = 14211288;

   public StorageESPModule() {
      super("StorageESP", "Highlight storage blocks", Category.RENDER, -1);
      StorageESPModule var1 = this;
      this.Y = new z4(var1x -> var1.addSetting(var1x));
      this.i = new ArrayList<>();
      this.S = new HashMap<>();

      for (q var2 : List.of(this.J, this.h, this.X, this.W, this.T)) {
         var2.P(this.b::getValue);
      }

      for (q var6 : List.of(this.j, this.k, this.f)) {
         var6.P(this.N::getValue);
      }

      this.c.P(this.K::getValue);
      this.Y
         .s(
            () -> this.K.getValue() || this.N.getValue() || this.b.getValue(),
            () -> this.K.getValue() || this.N.getValue(),
            () -> this.K.getValue() || this.N.getValue() || this.b.getValue(),
            () -> this.K.getValue() && this.c.O("Filled")
         );

      for (q var7 : this.D()) {
         ((s)var7).o(var1x -> this.V());
      }

      this.M.o(var1x -> this.V());
      this.n.o(var1x -> this.V());
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1x -> this.s());
      Z = this;
      if (!P) {
         P = (boolean)1;
         HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("larp-client", "storage_esp_labels"), (var0, var1x) -> Z.G(var0, var1x));
      }
   }

   @Override
   public void onEnable() {
      this.q = 0;
      this.i.clear();
      this.F = null;
      com.larpclient.h.U("ESP", "StorageESP enabled; received loaded storage only, tracers={}", this.N.getValue());
   }

   @Override
   public void onDisable() {
      com.larpclient.h.U("ESP", "StorageESP cleanup; discarded {} cached blocks", this.i.size());
      this.i.clear();
      this.S.clear();
      this.F = null;
   }

   public List<q<?>> D() {
      return List.of(this.g, this.U, this.p, this.v, this.w, this.s, this.d, this.o, this.V, this.x, this.H, this.D, this.L, this.A, this.Q, this.R, this.O);
   }

   public List<q<?>> Y() {
      return List.of(this.M, this.n);
   }

   public List<q<?>> U() {
      return List.of(this.M, this.K, this.c, this.b, this.N, this.j);
   }

   public List<q<?>> o() {
      return List.of(this.n, this.J, this.h, this.k, this.f);
   }

   public List<q<?>> s() {
      return List.of(this.N, this.j, this.k, this.f);
   }

   public List<q<?>> E() {
      return List.of(this.K, this.c);
   }

   public List<q<?>> d() {
      ArrayList var1 = new ArrayList<>(this.Y.f());
      var1.addAll(this.Y.O());
      var1.addAll(this.Y.p());
      var1.addAll(List.of(this.X, this.W, this.T));
      return List.copyOf(var1);
   }

   private void V() {
      this.q = 0;
      this.i.clear();
   }

   public static Vec3 O(Vec3 var0, Quaternionf var1, Vec3 var2, String var3) {
      if (var3.equals("Feet")) {
         return var2.add(0.0, 0.1, 0.0);
      }

      Vector3f var4 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var1);
      return var0.add(var4.x * 0.5, var4.y * 0.5, var4.z * 0.5);
   }

   public List<q<?>> t() {
      return List.of(this.b, this.J, this.h, this.X, this.W, this.T);
   }

   public static String y(BlockEntity var0) {
      return var0.getBlockState().getBlock().getName().getString();
   }

   public static BlockPos q(BlockEntity var0) {
      BlockState var1 = var0.getBlockState();
      return var1.getBlock() instanceof ChestBlock && var1.hasProperty(ChestBlock.TYPE) && var1.getValue(ChestBlock.TYPE) != ChestType.SINGLE
         ? ChestBlock.getConnectedBlockPos(var0.getBlockPos(), var1)
         : null;
   }

   public static String m(BlockEntity var0, boolean var1) {
      return var1 ? y(var0) + " x2" : y(var0);
   }

   public static String Y(String var0, double var1) {
      String var3 = (int)var1 + "m";
      return var0 != null && !var0.isBlank() ? var0 + " · " + var3 : var3;
   }

   private boolean V(BlockEntity var1) {
      if (var1 instanceof DropperBlockEntity) {
         return this.H.getValue();
      } else {
         return var1 instanceof DispenserBlockEntity
            ? this.x.getValue()
            : var1 instanceof ChestBlockEntity && this.g.getValue()
               || var1 instanceof EnderChestBlockEntity && this.U.getValue()
               || var1 instanceof ShulkerBoxBlockEntity && this.p.getValue()
               || var1 instanceof BarrelBlockEntity && this.v.getValue()
               || var1 instanceof SpawnerBlockEntity && this.w.getValue()
               || var1 instanceof HopperBlockEntity && this.s.getValue()
               || var1 instanceof FurnaceBlockEntity && this.d.getValue()
               || var1 instanceof BlastFurnaceBlockEntity && this.o.getValue()
               || var1 instanceof SmokerBlockEntity && this.V.getValue()
               || var1 instanceof CrafterBlockEntity && this.D.getValue()
               || var1 instanceof BrewingStandBlockEntity && this.L.getValue()
               || var1 instanceof BeehiveBlockEntity && this.A.getValue()
               || var1 instanceof DecoratedPotBlockEntity && this.Q.getValue()
               || var1 instanceof ChiseledBookShelfBlockEntity && this.R.getValue()
               || var1 instanceof JukeboxBlockEntity && this.O.getValue();
      }
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.F != mc.level) {
            this.i.clear();
            this.F = mc.level;
            this.q = 0;
         }

         int var10000 = this.q;
         byte var11 = 1;
         int var10 = this.q;
         this.q = var10 + var11;
         var10000 %= 10;
         boolean var13 = false;
         int var12 = var10000;
         var10000 = Integer.signum(var12);
         if (((var10000 | -var10000) >>> 31 ^ 1) != 0) {
            this.i.clear();
            int var1 = (int)Math.ceil(this.M.getValue() / 16.0) + 1;
            ChunkPos var2 = mc.player.chunkPosition();
            var10000 = var2.x();
            int var15 = var1;
            int var14 = var10000;

            for (int var3 = var14 - var15; var3 <= var2.x() + var1; var3++) {
               for (int var4 = var2.z() - var1; var4 <= var2.z() + var1; var4++) {
                  LevelChunk var5 = mc.level.getChunkSource().getChunkNow(var3, var4);
                  if (var5 != null) {
                     for (BlockEntity var7 : var5.getBlockEntities().values()) {
                        if (!var7.isRemoved()
                           && this.V(var7)
                           && mc.player.distanceToSqr(Vec3.atCenterOf(var7.getBlockPos())) <= this.M.getValue() * this.M.getValue()) {
                           this.i.add(var7);
                        }
                     }
                  }
               }
            }

            this.i.sort(Comparator.comparingDouble(var0 -> mc.player.distanceToSqr(Vec3.atCenterOf(var0.getBlockPos()))));
            var10000 = this.i.size();
            int var9 = this.n.getValue().intValue();
            int var8 = var10000;
            if ((-Integer.compare(var8, var9) >>> 31 ^ 1) == 0) {
               this.i.subList(this.n.getValue().intValue(), this.i.size()).clear();
            }
         }
      }
   }

   private boolean Z(BlockEntity var1) {
      if (!var1.isRemoved() && this.V(var1)) {
         BlockPos var2 = var1.getBlockPos();
         LevelChunk var3 = mc.level.getChunkSource().getChunkNow(var2.getX() >> 4, var2.getZ() >> 4);
         return var3 != null && var3.getBlockEntities().get(var2) == var1;
      } else {
         return false;
      }
   }

   private AABB L(BlockEntity var1) {
      BlockPos var2 = var1.getBlockPos();
      VoxelShape var3 = var1.getBlockState().getShape(mc.level, var2);
      return (var3.isEmpty() ? new AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0) : var3.bounds()).move(var2).inflate(0.003);
   }

   private int j(BlockEntity var1, double var2) {
      int var4 = var1 instanceof SpawnerBlockEntity
         ? -943189
         : (var1 instanceof EnderChestBlockEntity ? -5071873 : (var1 instanceof ShulkerBoxBlockEntity ? -1400362 : -1521772));
      return this.Y.c(var4, var2, this.M.getValue(), 1.0, var1.getBlockPos().hashCode());
   }

   private void s() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.F == mc.level) {
            try {
               int var1 = 0;
               int var2 = 0;
               Camera var3 = mc.gameRenderer.mainCamera();
               Vec3 var4 = this.N.getValue() ? O(var3.position(), var3.rotation(), mc.player.position(), this.j.getValue()) : null;

               for (BlockEntity var6 : this.i) {
                  if (this.Z(var6)) {
                     double var7 = mc.player.position().distanceTo(Vec3.atCenterOf(var6.getBlockPos()));
                     if (!(var7 >= this.M.getValue())) {
                        if (var1++ >= this.n.getValue().intValue()) {
                           break;
                        }

                        AABB var9 = this.L(var6);
                        int var10 = this.j(var6, var7);
                        if (this.K.getValue()) {
                           this.Y.R(var9, this.c.getValue(), var10);
                        }

                        if (var4 != null && var7 <= this.k.getValue() && var2++ < this.f.getValue().intValue() && var10 >>> 24 != 0) {
                           this.Y.S(var4, var9.getCenter(), var10);
                        }
                     }
                  }
               }
            } catch (RuntimeException var11) {
               com.larpclient.h.w("ESP", "Storage overlay failed", var11);
            }
         }
      }
   }

   private void G(GuiGraphicsExtractor var1, DeltaTracker var2) {
      if (this.b.getValue() && this.isRuntimeActive() && this.isInGame() && this.F == mc.level && !mc.gameRenderer.gameRenderState().guiRenderState.isHudHidden
         )
       {
         var1 = p2.x(var1, true, false);
         if (var1 != null) {
            long var3 = zj.m();

            try {
               Camera var5 = mc.gameRenderer.mainCamera();
               Vec3 var6 = var5.position();
               Matrix4f var7 = var5.getViewRotationProjectionMatrix(new Matrix4f());
               Vector3f var8 = var5.rotation().transform(new Vector3f(0.0F, 1.0F, 0.0F));
               int var9 = mc.getWindow().getGuiScaledWidth();
               int var10 = mc.getWindow().getGuiScaledHeight();
               boolean var11 = RenderSystem.getDevice().getDeviceInfo().isZZeroToOne();
               float var12 = this.X.getValue().floatValue();
               int var13 = this.h.getValue().intValue();
               HashSet var14 = new HashSet();
               ArrayList var15 = new ArrayList();
               ArrayList var16 = new ArrayList();

               for (BlockEntity var18 : this.i) {
                  if (var16.size() >= var13) {
                     break;
                  }

                  if (this.Z(var18)) {
                     BlockPos var19 = var18.getBlockPos();
                     double var20 = mc.player.position().distanceTo(Vec3.atCenterOf(var19));
                     if (!(var20 >= this.M.getValue()) && !(var20 > this.J.getValue())) {
                        BlockPos var22 = q(var18);
                        AABB var23 = this.L(var18);
                        if (var22 != null) {
                           if (var14.contains(var22)) {
                              continue;
                           }

                           if (mc.level.getBlockEntity(var22) instanceof ChestBlockEntity var24 && this.Z(var24)) {
                              var23 = var23.minmax(this.L(var24));
                           } else {
                              var22 = null;
                           }
                        }

                        var14.add(var19);
                        Vec3 var43 = new Vec3((var23.minX + var23.maxX) / 2.0, var23.maxY + 0.25, (var23.minZ + var23.maxZ) / 2.0);
                        k7.o var44 = k7.I(var43, var6, var7, var8, var9, var10, var11);
                        if (var44 != null) {
                           int var26 = this.j(var18, var20);
                           double var27 = (var26 >>> 24) / 255.0;
                           if (!(var27 < 0.03) && (this.Y.t() || !this.p(var6, var43, var19, var22))) {
                              String var29 = var18 instanceof SpawnerBlockEntity var30 ? this.e(var30) : null;
                              MutableComponent var45 = Component.literal(this.e(m(var18, var22 != null)) + " ")
                                 .append(Component.literal(Y(var29, var20)).withColor(14211288));
                              ItemStack var31 = this.W.getValue() ? this.W(var18) : ItemStack.EMPTY;
                              int var32 = 1 + (var31.isEmpty() ? 0 : 12) + mc.font.width(var45) + 1;
                              float var33 = var44.x() - var32 * var12 / 2.0F;
                              float var34 = var44.y() - 12.0F * var12;
                              float[] var35 = new float[]{var33, var34, var33 + var32 * var12, var34 + 12.0F * var12};
                              if (!this.T.getValue()
                                 || !var15.stream()
                                    .anyMatch(
                                       var1x -> var35[0] < var1x[2] + 1.0F
                                          && var35[2] + 1.0F > var1x[0]
                                          && var35[1] < var1x[3] + 1.0F
                                          && var35[3] + 1.0F > var1x[1]
                                    )) {
                                 var15.add(var35);
                                 var16.add(new StorageESPModule.n(var44.x(), var44.y(), var32, var45, var31, var27));
                              }
                           }
                        }
                     }
                  }
               }

               for (int var42 = var16.size() - 1; var42 >= 0; var42--) {
                  this.S(var1, (StorageESPModule.n)var16.get(var42), var12);
               }
            } catch (RuntimeException var39) {
               com.larpclient.h.w("ESP", "Storage name tags failed", var39);
            } finally {
               zj.l("StorageESP labels", var3);
            }
         }
      }
   }

   private void S(GuiGraphicsExtractor var1, StorageESPModule.n var2, float var3) {
      var1.pose().pushMatrix();

      try {
         var1.pose().translate(this.z(var2.x() - var2.width() * var3 / 2.0F), this.z(var2.y() - 12.0F * var3)).scale(var3);
         kv.d(var1, 0, 0, var2.width(), 12, uh.F(1073741824, var2.alpha()));
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

         var1.text(mc.font, var2.line(), var4, 2, uh.F(-1, var2.alpha()), true);
      } finally {
         var1.pose().popMatrix();
      }
   }

   private String e(String var1) {
      return mc.font.width(var1) <= 160 ? var1 : mc.font.plainSubstrByWidth(var1, 160 - mc.font.width("...")) + "...";
   }

   private float z(float var1) {
      double var2 = mc.getWindow().getGuiScale();
      return (float)(Math.round(var1 * var2) / var2);
   }

   private boolean p(Vec3 var1, Vec3 var2, BlockPos var3, BlockPos var4) {
      BlockHitResult var5 = mc.level.clip(new ClipContext(var1, var2, net.minecraft.world.level.ClipContext.Block.COLLIDER, Fluid.NONE, mc.player));
      return var5.getType() != Type.BLOCK ? false : !var5.getBlockPos().equals(var3) && !var5.getBlockPos().equals(var4);
   }

   private ItemStack W(BlockEntity var1) {
      return this.S.computeIfAbsent(var1.getBlockState().getBlock(), var0 -> new ItemStack(var0.asItem()));
   }

   private String e(SpawnerBlockEntity var1) {
      try {
         Entity var2 = var1.getSpawner().getOrCreateDisplayEntity(mc.level, var1.getBlockPos());
         return var2 == null ? null : var2.getType().getDescription().getString();
      } catch (RuntimeException var3) {
         com.larpclient.h.w("ESP", "Spawner mob name unavailable; tag shows the block name", var3);
         return null;
      }
   }

   private record n(float x, float y, int width, Component line, ItemStack icon, double alpha) {
   }
}
