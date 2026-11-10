package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.h5;
import com.larpclient.he;
import com.larpclient.iy;
import com.larpclient.j5;
import com.larpclient.qc;
import com.larpclient.vv;
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
   private final m p = this.addSetting(new m("Chests", "Highlight regular and trapped chests", true));
   private final m N = this.addSetting(new m("Ender Chests", "Highlight ender chests", true));
   private final m B = this.addSetting(new m("Shulkers", "Highlight shulker boxes", true));
   private final m t = this.addSetting(new m("Barrels", "Highlight barrels", false));
   private final m X = this.addSetting(new m("Spawners", "Highlight mob spawners", true));
   private final m A = this.addSetting(new m("Hoppers", "Highlight hoppers", false));
   private final m r = this.addSetting(new m("Furnaces", "Highlight furnaces", false));
   private final m C = this.addSetting(new m("Blast Furnaces", "Highlight blast furnaces", false));
   private final m U = this.addSetting(new m("Smokers", "Highlight smokers", false));
   private final m e = this.addSetting(new m("Dispensers", "Highlight dispensers", false));
   private final m Q = this.addSetting(new m("Droppers", "Highlight droppers", false));
   private final m F = this.addSetting(new m("Crafters", "Highlight crafters", false));
   private final m f = this.addSetting(new m("Brewing Stands", "Highlight brewing stands", false));
   private final m i = this.addSetting(new m("Beehives", "Highlight beehives", false));
   private final m k = this.addSetting(new m("Decorated Pots", "Highlight decorated pots", false));
   private final m j = this.addSetting(new m("Chiseled Bookshelves", "Highlight chiseled bookshelves", false));
   private final m m = this.addSetting(new m("Jukeboxes", "Highlight jukeboxes", false));
   private final b Y = this.addSetting(new b("Range", "Loaded storage scan radius", 64.0, 16.0, 128.0, 8.0));
   private final b s = this.addSetting(new b("Max Blocks", "Nearest storage blocks drawn", 256.0, 16.0, 512.0, 16.0));
   private final o W = this.addSetting(new o("Mode", "Storage highlight style", "Filled", "Box", "Corners", "Filled"));
   private final m u = this.addSetting(new m("Labels", "Show storage names above highlighted blocks", true));
   private final b P = this.addSetting(new b("Label Range", "Maximum name-tag distance, within the storage scan range", 128.0, 16.0, 128.0, 8.0));
   private final b l = this.addSetting(new b("Max Labels", "Nearest storage name tags drawn", 64.0, 1.0, 128.0, 1.0));
   private final b D = this.addSetting(new b("Label Scale", "Name-tag size on screen, the same at every distance", 1.0, 0.5, 2.0, 0.1));
   private final m Z = this.addSetting(new m("Label Icons", "Show the block item beside each storage name", true));
   private final m h = this.addSetting(new m("Hide Overlaps", "Skip farther name tags that would cover nearer ones", true));
   private final m G = this.addSetting(new m("Boxes", "Draw storage highlight boxes", true));
   private final m q = this.addSetting(new m("Tracers", "Draw lines to selected storage blocks", false));
   private final o x = this.addSetting(new o("Tracer Origin", "Start at the camera crosshair or player feet", "Crosshair", "Crosshair", "Feet"));
   private final b a = this.addSetting(new b("Tracer Range", "Maximum line distance, within scan range", 64.0, 16.0, 128.0, 8.0));
   private final b E = this.addSetting(new b("Max Tracers", "Nearest selected storage lines drawn", 64.0, 1.0, 128.0, 1.0));
   private final j5 b;
   private final List<BlockEntity> M;
   private final Map<Block, ItemStack> d;
   private ClientLevel I;
   private int c;
   private static StorageESPModule T;
   private static boolean L;
   private static final int o = 12;
   private static final int z = 10;
   private static final int J = 160;
   private static final int S = 1073741824;
   private static final int v = -1;
   private static final int R = 14211288;

   public StorageESPModule() {
      super("StorageESP", "Highlight storage blocks", Category.RENDER, -1);
      StorageESPModule var1 = this;
      this.b = new j5(var1x -> var1.addSetting(var1x));
      this.M = new ArrayList<>();
      this.d = new HashMap<>();

      for (r var2 : List.of(this.P, this.l, this.D, this.Z, this.h)) {
         var2.a(this.u::getValue);
      }

      for (r var6 : List.of(this.x, this.a, this.E)) {
         var6.a(this.q::getValue);
      }

      this.W.a(this.G::getValue);
      this.b
         .q(
            () -> this.G.getValue() || this.q.getValue() || this.u.getValue(),
            () -> this.G.getValue() || this.q.getValue(),
            () -> this.G.getValue() || this.q.getValue() || this.u.getValue(),
            () -> this.G.getValue() && this.W.w("Filled")
         );

      for (r var7 : this.J()) {
         ((m)var7).w(var1x -> this.K());
      }

      this.Y.w(var1x -> this.K());
      this.s.w(var1x -> this.K());
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1x -> this.F());
      T = this;
      if (!L) {
         L = (boolean)1;
         HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("larp-client", "storage_esp_labels"), (var0, var1x) -> T.W(var0, var1x));
      }
   }

   @Override
   public void onEnable() {
      this.c = 0;
      this.M.clear();
      this.I = null;
      _q.c("ESP", "StorageESP enabled; received loaded storage only, tracers={}", this.q.getValue());
   }

   @Override
   public void onDisable() {
      _q.c("ESP", "StorageESP cleanup; discarded {} cached blocks", this.M.size());
      this.M.clear();
      this.d.clear();
      this.I = null;
   }

   public List<r<?>> J() {
      return List.of(this.p, this.N, this.B, this.t, this.X, this.A, this.r, this.C, this.U, this.e, this.Q, this.F, this.f, this.i, this.k, this.j, this.m);
   }

   public List<r<?>> d() {
      return List.of(this.Y, this.s);
   }

   public List<r<?>> Y() {
      return List.of(this.Y, this.G, this.W, this.u, this.q, this.x);
   }

   public List<r<?>> k() {
      return List.of(this.s, this.P, this.l, this.a, this.E);
   }

   public List<r<?>> E() {
      return List.of(this.q, this.x, this.a, this.E);
   }

   public List<r<?>> A() {
      return List.of(this.G, this.W);
   }

   public List<r<?>> B() {
      ArrayList var1 = new ArrayList<>(this.b.Q());
      var1.addAll(this.b.l());
      var1.addAll(this.b.M());
      var1.addAll(List.of(this.D, this.Z, this.h));
      return List.copyOf(var1);
   }

   private void K() {
      this.c = 0;
      this.M.clear();
   }

   public static Vec3 G(Vec3 var0, Quaternionf var1, Vec3 var2, String var3) {
      if (var3.equals("Feet")) {
         return var2.add(0.0, 0.1, 0.0);
      }

      Vector3f var4 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var1);
      return var0.add(var4.x * 0.5, var4.y * 0.5, var4.z * 0.5);
   }

   public List<r<?>> x() {
      return List.of(this.u, this.P, this.l, this.D, this.Z, this.h);
   }

   public static String B(BlockEntity var0) {
      return var0.getBlockState().getBlock().getName().getString();
   }

   public static BlockPos w(BlockEntity var0) {
      BlockState var1 = var0.getBlockState();
      return var1.getBlock() instanceof ChestBlock && var1.hasProperty(ChestBlock.TYPE) && var1.getValue(ChestBlock.TYPE) != ChestType.SINGLE
         ? ChestBlock.getConnectedBlockPos(var0.getBlockPos(), var1)
         : null;
   }

   public static String q(BlockEntity var0, boolean var1) {
      return var1 ? B(var0) + " x2" : B(var0);
   }

   public static String O(String var0, double var1) {
      String var3 = (int)var1 + "m";
      return var0 != null && !var0.isBlank() ? var0 + " · " + var3 : var3;
   }

   private boolean w(BlockEntity var1) {
      if (var1 instanceof DropperBlockEntity) {
         return this.Q.getValue();
      } else {
         return var1 instanceof DispenserBlockEntity
            ? this.e.getValue()
            : var1 instanceof ChestBlockEntity && this.p.getValue()
               || var1 instanceof EnderChestBlockEntity && this.N.getValue()
               || var1 instanceof ShulkerBoxBlockEntity && this.B.getValue()
               || var1 instanceof BarrelBlockEntity && this.t.getValue()
               || var1 instanceof SpawnerBlockEntity && this.X.getValue()
               || var1 instanceof HopperBlockEntity && this.A.getValue()
               || var1 instanceof FurnaceBlockEntity && this.r.getValue()
               || var1 instanceof BlastFurnaceBlockEntity && this.C.getValue()
               || var1 instanceof SmokerBlockEntity && this.U.getValue()
               || var1 instanceof CrafterBlockEntity && this.F.getValue()
               || var1 instanceof BrewingStandBlockEntity && this.f.getValue()
               || var1 instanceof BeehiveBlockEntity && this.i.getValue()
               || var1 instanceof DecoratedPotBlockEntity && this.k.getValue()
               || var1 instanceof ChiseledBookShelfBlockEntity && this.j.getValue()
               || var1 instanceof JukeboxBlockEntity && this.m.getValue();
      }
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.I != mc.level) {
            this.M.clear();
            this.I = mc.level;
            this.c = 0;
         }

         int var10000 = this.c;
         byte var11 = 1;
         int var10 = this.c;
         this.c = var10 + var11;
         var10000 %= 10;
         boolean var13 = false;
         int var12 = var10000;
         var10000 = Integer.signum(var12);
         if (((var10000 | -var10000) >>> 31 ^ 1) != 0) {
            this.M.clear();
            var10000 = (int)Math.ceil(this.Y.getValue() / 16.0);
            byte var9 = 1;
            int var8 = var10000;
            int var1 = var8 + var9;
            ChunkPos var2 = mc.player.chunkPosition();

            for (int var3 = var2.x() - var1; var3 <= var2.x() + var1; var3++) {
               for (int var4 = var2.z() - var1; var4 <= var2.z() + var1; var4++) {
                  LevelChunk var5 = mc.level.getChunkSource().getChunkNow(var3, var4);
                  if (var5 != null) {
                     for (BlockEntity var7 : var5.getBlockEntities().values()) {
                        if (!var7.isRemoved()
                           && this.w(var7)
                           && mc.player.distanceToSqr(Vec3.atCenterOf(var7.getBlockPos())) <= this.Y.getValue() * this.Y.getValue()) {
                           this.M.add(var7);
                        }
                     }
                  }
               }
            }

            this.M.sort(Comparator.comparingDouble(var0 -> mc.player.distanceToSqr(Vec3.atCenterOf(var0.getBlockPos()))));
            var10000 = this.M.size();
            int var15 = this.s.getValue().intValue();
            int var14 = var10000;
            if ((-Integer.compare(var14, var15) >>> 31 ^ 1) == 0) {
               this.M.subList(this.s.getValue().intValue(), this.M.size()).clear();
            }
         }
      }
   }

   private boolean A(BlockEntity var1) {
      if (!var1.isRemoved() && this.w(var1)) {
         BlockPos var2 = var1.getBlockPos();
         LevelChunk var3 = mc.level.getChunkSource().getChunkNow(var2.getX() >> 4, var2.getZ() >> 4);
         return var3 != null && var3.getBlockEntities().get(var2) == var1;
      } else {
         return false;
      }
   }

   private AABB e(BlockEntity var1) {
      BlockPos var2 = var1.getBlockPos();
      VoxelShape var3 = var1.getBlockState().getShape(mc.level, var2);
      return (var3.isEmpty() ? new AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0) : var3.bounds()).move(var2).inflate(0.003);
   }

   private int P(BlockEntity var1, double var2) {
      int var4 = var1 instanceof SpawnerBlockEntity
         ? -943189
         : (var1 instanceof EnderChestBlockEntity ? -5071873 : (var1 instanceof ShulkerBoxBlockEntity ? -1400362 : -1521772));
      return this.b.p(var4, var2, this.Y.getValue(), 1.0, var1.getBlockPos().hashCode());
   }

   private void F() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.I == mc.level) {
            try {
               int var1 = 0;
               int var2 = 0;
               Camera var3 = mc.gameRenderer.mainCamera();
               Vec3 var4 = this.q.getValue() ? G(var3.position(), var3.rotation(), mc.player.position(), this.x.getValue()) : null;

               for (BlockEntity var6 : this.M) {
                  if (this.A(var6)) {
                     double var7 = mc.player.position().distanceTo(Vec3.atCenterOf(var6.getBlockPos()));
                     if (!(var7 >= this.Y.getValue())) {
                        if (var1++ >= this.s.getValue().intValue()) {
                           break;
                        }

                        AABB var9 = this.e(var6);
                        int var10 = this.P(var6, var7);
                        if (this.G.getValue()) {
                           this.b.L(var9, this.W.getValue(), var10);
                        }

                        if (var4 != null && var7 <= this.a.getValue() && var2++ < this.E.getValue().intValue() && var10 >>> 24 != 0) {
                           this.b.q(var4, var9.getCenter(), var10);
                        }
                     }
                  }
               }
            } catch (RuntimeException var11) {
               _q.g("ESP", "Storage overlay failed", var11);
            }
         }
      }
   }

   private void W(GuiGraphicsExtractor var1, DeltaTracker var2) {
      if (this.u.getValue() && this.isRuntimeActive() && this.isInGame() && this.I == mc.level && !mc.gameRenderer.gameRenderState().guiRenderState.isHudHidden
         )
       {
         var1 = qc.h(var1, true, false);
         if (var1 != null) {
            long var3 = he.s();

            try {
               Camera var5 = mc.gameRenderer.mainCamera();
               Vec3 var6 = var5.position();
               Matrix4f var7 = var5.getViewRotationProjectionMatrix(new Matrix4f());
               Vector3f var8 = var5.rotation().transform(new Vector3f(0.0F, 1.0F, 0.0F));
               int var9 = mc.getWindow().getGuiScaledWidth();
               int var10 = mc.getWindow().getGuiScaledHeight();
               boolean var11 = RenderSystem.getDevice().getDeviceInfo().isZZeroToOne();
               float var12 = this.D.getValue().floatValue();
               int var13 = this.l.getValue().intValue();
               HashSet var14 = new HashSet();
               ArrayList var15 = new ArrayList();
               ArrayList var16 = new ArrayList();

               for (BlockEntity var18 : this.M) {
                  if (var16.size() >= var13) {
                     break;
                  }

                  if (this.A(var18)) {
                     BlockPos var19 = var18.getBlockPos();
                     double var20 = mc.player.position().distanceTo(Vec3.atCenterOf(var19));
                     if (!(var20 >= this.Y.getValue()) && !(var20 > this.P.getValue())) {
                        BlockPos var22 = w(var18);
                        AABB var23 = this.e(var18);
                        if (var22 != null) {
                           if (var14.contains(var22)) {
                              continue;
                           }

                           if (mc.level.getBlockEntity(var22) instanceof ChestBlockEntity var24 && this.A(var24)) {
                              var23 = var23.minmax(this.e(var24));
                           } else {
                              var22 = null;
                           }
                        }

                        var14.add(var19);
                        Vec3 var43 = new Vec3((var23.minX + var23.maxX) / 2.0, var23.maxY + 0.25, (var23.minZ + var23.maxZ) / 2.0);
                        iy.a var44 = iy.A(var43, var6, var7, var8, var9, var10, var11);
                        if (var44 != null) {
                           int var26 = this.P(var18, var20);
                           double var27 = (var26 >>> 24) / 255.0;
                           if (!(var27 < 0.03) && (this.b.m() || !this.P(var6, var43, var19, var22))) {
                              String var29 = var18 instanceof SpawnerBlockEntity var30 ? this.t(var30) : null;
                              MutableComponent var45 = Component.literal(this.Z(q(var18, var22 != null)) + " ")
                                 .append(Component.literal(O(var29, var20)).withColor(14211288));
                              ItemStack var31 = this.Z.getValue() ? this.Y(var18) : ItemStack.EMPTY;
                              int var32 = 1 + (var31.isEmpty() ? 0 : 12) + mc.font.width(var45) + 1;
                              float var33 = var44.x() - var32 * var12 / 2.0F;
                              float var34 = var44.y() - 12.0F * var12;
                              float[] var35 = new float[]{var33, var34, var33 + var32 * var12, var34 + 12.0F * var12};
                              if (!this.h.getValue()
                                 || !var15.stream()
                                    .anyMatch(
                                       var1x -> var35[0] < var1x[2] + 1.0F
                                          && var35[2] + 1.0F > var1x[0]
                                          && var35[1] < var1x[3] + 1.0F
                                          && var35[3] + 1.0F > var1x[1]
                                    )) {
                                 var15.add(var35);
                                 var16.add(new StorageESPModule.u(var44.x(), var44.y(), var32, var45, var31, var27));
                              }
                           }
                        }
                     }
                  }
               }

               for (int var42 = var16.size() - 1; var42 >= 0; var42--) {
                  this.I(var1, (StorageESPModule.u)var16.get(var42), var12);
               }
            } catch (RuntimeException var39) {
               _q.g("ESP", "Storage name tags failed", var39);
            } finally {
               he.T("StorageESP labels", var3);
            }
         }
      }
   }

   private void I(GuiGraphicsExtractor var1, StorageESPModule.u var2, float var3) {
      var1.pose().pushMatrix();

      try {
         var1.pose().translate(this.F(var2.x() - var2.width() * var3 / 2.0F), this.F(var2.y() - 12.0F * var3)).scale(var3);
         h5.N(var1, 0, 0, var2.width(), 12, vv.O(1073741824, var2.alpha()));
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

         var1.text(mc.font, var2.line(), var4, 2, vv.O(-1, var2.alpha()), true);
      } finally {
         var1.pose().popMatrix();
      }
   }

   private String Z(String var1) {
      return mc.font.width(var1) <= 160 ? var1 : mc.font.plainSubstrByWidth(var1, 160 - mc.font.width("...")) + "...";
   }

   private float F(float var1) {
      double var2 = mc.getWindow().getGuiScale();
      return (float)(Math.round(var1 * var2) / var2);
   }

   private boolean P(Vec3 var1, Vec3 var2, BlockPos var3, BlockPos var4) {
      BlockHitResult var5 = mc.level.clip(new ClipContext(var1, var2, net.minecraft.world.level.ClipContext.Block.COLLIDER, Fluid.NONE, mc.player));
      return var5.getType() != Type.BLOCK ? false : !var5.getBlockPos().equals(var3) && !var5.getBlockPos().equals(var4);
   }

   private ItemStack Y(BlockEntity var1) {
      return this.d.computeIfAbsent(var1.getBlockState().getBlock(), var0 -> new ItemStack(var0.asItem()));
   }

   private String t(SpawnerBlockEntity var1) {
      try {
         Entity var2 = var1.getSpawner().getOrCreateDisplayEntity(mc.level, var1.getBlockPos());
         return var2 == null ? null : var2.getType().getDescription().getString();
      } catch (RuntimeException var3) {
         _q.g("ESP", "Spawner mob name unavailable; tag shows the block name", var3);
         return null;
      }
   }

   private record u(float x, float y, int width, Component line, ItemStack icon, double alpha) {
   }
}
