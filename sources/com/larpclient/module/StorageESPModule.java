package com.larpclient.module;

import com.larpclient._3;
import com.larpclient.c7;
import com.larpclient.dq;
import com.larpclient.k6;
import com.larpclient.kx;
import com.larpclient.mt;
import com.larpclient.zo;
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
   private final dh q = this.addSetting(new dh("Chests", "Highlight regular and trapped chests", true));
   private final dh f = this.addSetting(new dh("Ender Chests", "Highlight ender chests", true));
   private final dh K = this.addSetting(new dh("Shulkers", "Highlight shulker boxes", true));
   private final dh m = this.addSetting(new dh("Barrels", "Highlight barrels", false));
   private final dh X = this.addSetting(new dh("Spawners", "Highlight mob spawners", true));
   private final dh T = this.addSetting(new dh("Hoppers", "Highlight hoppers", false));
   private final dh M = this.addSetting(new dh("Furnaces", "Highlight furnaces", false));
   private final dh D = this.addSetting(new dh("Blast Furnaces", "Highlight blast furnaces", false));
   private final dh d = this.addSetting(new dh("Smokers", "Highlight smokers", false));
   private final dh Q = this.addSetting(new dh("Dispensers", "Highlight dispensers", false));
   private final dh Z = this.addSetting(new dh("Droppers", "Highlight droppers", false));
   private final dh c = this.addSetting(new dh("Crafters", "Highlight crafters", false));
   private final dh O = this.addSetting(new dh("Brewing Stands", "Highlight brewing stands", false));
   private final dh H = this.addSetting(new dh("Beehives", "Highlight beehives", false));
   private final dh P = this.addSetting(new dh("Decorated Pots", "Highlight decorated pots", false));
   private final dh U = this.addSetting(new dh("Chiseled Bookshelves", "Highlight chiseled bookshelves", false));
   private final dh l = this.addSetting(new dh("Jukeboxes", "Highlight jukeboxes", false));
   private final d9 J = this.addSetting(new d9("Range", "Loaded storage scan radius", 64.0, 16.0, 128.0, 8.0));
   private final d9 k = this.addSetting(new d9("Max Blocks", "Nearest storage blocks drawn", 256.0, 16.0, 512.0, 16.0));
   private final d6 W = this.addSetting(new d6("Mode", "Storage highlight style", "Filled", "Box", "Corners", "Filled"));
   private final dh b = this.addSetting(new dh("Labels", "Show storage names above highlighted blocks", true));
   private final d9 B = this.addSetting(new d9("Label Range", "Maximum name-tag distance, within the storage scan range", 128.0, 16.0, 128.0, 8.0));
   private final d9 x = this.addSetting(new d9("Max Labels", "Nearest storage name tags drawn", 64.0, 1.0, 128.0, 1.0));
   private final d9 a = this.addSetting(new d9("Label Scale", "Name-tag size on screen, the same at every distance", 1.0, 0.5, 2.0, 0.1));
   private final dh v = this.addSetting(new dh("Label Icons", "Show the block item beside each storage name", true));
   private final dh g = this.addSetting(new dh("Hide Overlaps", "Skip farther name tags that would cover nearer ones", true));
   private final dh I = this.addSetting(new dh("Boxes", "Draw storage highlight boxes", true));
   private final dh n = this.addSetting(new dh("Tracers", "Draw lines to selected storage blocks", false));
   private final d6 o = this.addSetting(new d6("Tracer Origin", "Start at the camera crosshair or player feet", "Crosshair", "Crosshair", "Feet"));
   private final d9 L = this.addSetting(new d9("Tracer Range", "Maximum line distance, within scan range", 64.0, 16.0, 128.0, 8.0));
   private final d9 y = this.addSetting(new d9("Max Tracers", "Nearest selected storage lines drawn", 64.0, 1.0, 128.0, 1.0));
   private final dq r;
   private final List<BlockEntity> V;
   private final Map<Block, ItemStack> S;
   private ClientLevel E;
   private int s;
   private static StorageESPModule Y;
   private static boolean A;
   private static final int h = 16;
   private static final int z = 12;

   public StorageESPModule() {
      super("StorageESP", "Highlight storage blocks", Category.RENDER, -1);
      StorageESPModule var1 = this;
      this.r = new dq(var1x -> var1.addSetting(var1x));
      this.V = new ArrayList<>();
      this.S = new HashMap<>();

      for (df var2 : List.of(this.B, this.x, this.a, this.v, this.g)) {
         var2.H(this.b::P);
      }

      for (df var6 : List.of(this.o, this.L, this.y)) {
         var6.H(this.n::P);
      }

      this.W.H(this.I::P);
      this.r
         .o(
            () -> this.I.P() || this.n.P() || this.b.P(),
            () -> this.I.P() || this.n.P(),
            () -> this.I.P() || this.n.P() || this.b.P(),
            () -> this.I.P() && this.W.S("Filled")
         );

      for (df var7 : this.i()) {
         ((dh)var7).h(var1x -> this.x());
      }

      this.J.h(var1x -> this.x());
      this.k.h(var1x -> this.x());
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1x -> this.O());
      Y = this;
      if (!A) {
         A = (boolean)1;
         HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("larp-client", "storage_esp_labels"), (var0, var1x) -> Y.U(var0, var1x));
      }
   }

   @Override
   public void onEnable() {
      this.s = 0;
      this.V.clear();
      this.E = null;
      mt.U("ESP", "StorageESP enabled; received loaded storage only, tracers={}", this.n.P());
   }

   @Override
   public void onDisable() {
      mt.U("ESP", "StorageESP cleanup; discarded {} cached blocks", this.V.size());
      this.V.clear();
      this.S.clear();
      this.E = null;
   }

   public List<df<?>> i() {
      return List.of(this.q, this.f, this.K, this.m, this.X, this.T, this.M, this.D, this.d, this.Q, this.Z, this.c, this.O, this.H, this.P, this.U, this.l);
   }

   public List<df<?>> N() {
      return List.of(this.J, this.k);
   }

   public List<df<?>> R() {
      return List.of(this.J, this.I, this.W, this.b, this.n, this.o);
   }

   public List<df<?>> U() {
      return List.of(this.k, this.B, this.x, this.L, this.y);
   }

   public List<df<?>> x() {
      return List.of(this.n, this.o, this.L, this.y);
   }

   public List<df<?>> g() {
      return List.of(this.I, this.W);
   }

   public List<df<?>> m() {
      ArrayList var1 = new ArrayList<>(this.r.q());
      var1.addAll(this.r.g());
      var1.addAll(this.r.L());
      var1.addAll(List.of(this.a, this.v, this.g));
      return List.copyOf(var1);
   }

   private void x() {
      this.s = 0;
      this.V.clear();
   }

   public static Vec3 H(Vec3 var0, Quaternionf var1, Vec3 var2, String var3) {
      if (var3.equals("Feet")) {
         return var2.add(0.0, 0.1, 0.0);
      }

      Vector3f var4 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var1);
      return var0.add(var4.x * 0.5, var4.y * 0.5, var4.z * 0.5);
   }

   public List<df<?>> A() {
      return List.of(this.b, this.B, this.x, this.a, this.v, this.g);
   }

   public static String d(BlockEntity var0) {
      return var0.getBlockState().getBlock().getName().getString();
   }

   public static BlockPos t(BlockEntity var0) {
      BlockState var1 = var0.getBlockState();
      return var1.getBlock() instanceof ChestBlock && var1.hasProperty(ChestBlock.TYPE) && var1.getValue(ChestBlock.TYPE) != ChestType.SINGLE
         ? ChestBlock.getConnectedBlockPos(var0.getBlockPos(), var1)
         : null;
   }

   public static String Q(BlockEntity var0, boolean var1) {
      return var1 ? d(var0) + " x2" : d(var0);
   }

   public static String F(String var0, double var1) {
      String var3 = (int)var1 + "m";
      return var0 != null && !var0.isBlank() ? var0 + " · " + var3 : var3;
   }

   private boolean Y(BlockEntity var1) {
      if (var1 instanceof DropperBlockEntity) {
         return this.Z.P();
      } else {
         return var1 instanceof DispenserBlockEntity
            ? this.Q.P()
            : var1 instanceof ChestBlockEntity && this.q.P()
               || var1 instanceof EnderChestBlockEntity && this.f.P()
               || var1 instanceof ShulkerBoxBlockEntity && this.K.P()
               || var1 instanceof BarrelBlockEntity && this.m.P()
               || var1 instanceof SpawnerBlockEntity && this.X.P()
               || var1 instanceof HopperBlockEntity && this.T.P()
               || var1 instanceof FurnaceBlockEntity && this.M.P()
               || var1 instanceof BlastFurnaceBlockEntity && this.D.P()
               || var1 instanceof SmokerBlockEntity && this.d.P()
               || var1 instanceof CrafterBlockEntity && this.c.P()
               || var1 instanceof BrewingStandBlockEntity && this.O.P()
               || var1 instanceof BeehiveBlockEntity && this.H.P()
               || var1 instanceof DecoratedPotBlockEntity && this.P.P()
               || var1 instanceof ChiseledBookShelfBlockEntity && this.U.P()
               || var1 instanceof JukeboxBlockEntity && this.l.P();
      }
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.E != mc.level) {
            this.V.clear();
            this.E = mc.level;
            this.s = 0;
         }

         int var10000 = this.s;
         byte var11 = 1;
         int var10 = this.s;
         this.s = var10 + var11;
         var10000 %= 10;
         boolean var13 = false;
         int var12 = var10000;
         var10000 = Integer.signum(var12);
         if (((var10000 | -var10000) >>> 31 ^ 1) != 0) {
            this.V.clear();
            int var1 = (int)Math.ceil(this.J.P() / 16.0) + 1;
            ChunkPos var2 = mc.player.chunkPosition();
            var10000 = var2.x();
            int var9 = var1;
            int var8 = var10000;

            for (int var3 = var8 - var9; var3 <= var2.x() + var1; var3++) {
               for (int var4 = var2.z() - var1; var4 <= var2.z() + var1; var4++) {
                  LevelChunk var5 = mc.level.getChunkSource().getChunkNow(var3, var4);
                  if (var5 != null) {
                     for (BlockEntity var7 : var5.getBlockEntities().values()) {
                        if (!var7.isRemoved() && this.Y(var7) && mc.player.distanceToSqr(Vec3.atCenterOf(var7.getBlockPos())) <= this.J.P() * this.J.P()) {
                           this.V.add(var7);
                        }
                     }
                  }
               }
            }

            this.V.sort(Comparator.comparingDouble(var0 -> mc.player.distanceToSqr(Vec3.atCenterOf(var0.getBlockPos()))));
            var10000 = this.V.size();
            int var15 = this.k.P().intValue();
            int var14 = var10000;
            if ((-Integer.compare(var14, var15) >>> 31 ^ 1) == 0) {
               this.V.subList(this.k.P().intValue(), this.V.size()).clear();
            }
         }
      }
   }

   private boolean u(BlockEntity var1) {
      if (!var1.isRemoved() && this.Y(var1)) {
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

   private int U(BlockEntity var1, double var2) {
      int var4 = var1 instanceof SpawnerBlockEntity
         ? -943189
         : (var1 instanceof EnderChestBlockEntity ? -5071873 : (var1 instanceof ShulkerBoxBlockEntity ? -1400362 : -1521772));
      return this.r.w(var4, var2, this.J.P(), 1.0, var1.getBlockPos().hashCode());
   }

   private void O() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.E == mc.level) {
            try {
               int var1 = 0;
               int var2 = 0;
               Camera var3 = mc.gameRenderer.mainCamera();
               Vec3 var4 = this.n.P() ? H(var3.position(), var3.rotation(), mc.player.position(), this.o.P()) : null;

               for (BlockEntity var6 : this.V) {
                  if (this.u(var6)) {
                     double var7 = mc.player.position().distanceTo(Vec3.atCenterOf(var6.getBlockPos()));
                     if (!(var7 >= this.J.P())) {
                        if (var1++ >= this.k.P().intValue()) {
                           break;
                        }

                        AABB var9 = this.L(var6);
                        int var10 = this.U(var6, var7);
                        if (this.I.P()) {
                           this.r.O(var9, this.W.P(), var10);
                        }

                        if (var4 != null && var7 <= this.L.P() && var2++ < this.y.P().intValue() && var10 >>> 24 != 0) {
                           this.r.n(var4, var9.getCenter(), var10);
                        }
                     }
                  }
               }
            } catch (RuntimeException var11) {
               mt.B("ESP", "Storage overlay failed", var11);
            }
         }
      }
   }

   private void U(GuiGraphicsExtractor var1, DeltaTracker var2) {
      if (this.b.P() && this.isRuntimeActive() && this.isInGame() && this.E == mc.level && !mc.gameRenderer.gameRenderState().guiRenderState.isHudHidden) {
         var1 = zo.R(var1, true, false);
         if (var1 != null) {
            long var3 = kx.G();

            try {
               Camera var5 = mc.gameRenderer.mainCamera();
               Vec3 var6 = var5.position();
               Matrix4f var7 = var5.getViewRotationProjectionMatrix(new Matrix4f());
               Vector3f var8 = var5.rotation().transform(new Vector3f(0.0F, 1.0F, 0.0F));
               int var9 = mc.getWindow().getGuiScaledWidth();
               int var10 = mc.getWindow().getGuiScaledHeight();
               boolean var11 = RenderSystem.getDevice().getDeviceInfo().isZZeroToOne();
               float var12 = this.a.P().floatValue();
               int var13 = this.x.P().intValue();
               HashSet var14 = new HashSet();
               ArrayList var15 = new ArrayList();
               ArrayList var16 = new ArrayList();

               for (BlockEntity var18 : this.V) {
                  if (var16.size() >= var13) {
                     break;
                  }

                  if (this.u(var18)) {
                     BlockPos var19 = var18.getBlockPos();
                     double var20 = mc.player.position().distanceTo(Vec3.atCenterOf(var19));
                     if (!(var20 >= this.J.P()) && !(var20 > this.B.P())) {
                        BlockPos var22 = t(var18);
                        AABB var23 = this.L(var18);
                        if (var22 != null) {
                           if (var14.contains(var22)) {
                              continue;
                           }

                           if (mc.level.getBlockEntity(var22) instanceof ChestBlockEntity var24 && this.u(var24)) {
                              var23 = var23.minmax(this.L(var24));
                           } else {
                              var22 = null;
                           }
                        }

                        var14.add(var19);
                        Vec3 var44 = new Vec3((var23.minX + var23.maxX) / 2.0, var23.maxY + 0.25, (var23.minZ + var23.maxZ) / 2.0);
                        com.larpclient.di.a var45 = com.larpclient.di.k(var44, var6, var7, var8, var9, var10, var11);
                        if (var45 != null) {
                           int var26 = this.U(var18, var20);
                           double var27 = (var26 >>> 24) / 255.0;
                           if (!(var27 < 0.03) && (this.r.R() || !this.s(var6, var44, var19, var22))) {
                              String var29 = var18 instanceof SpawnerBlockEntity var30 ? this.h(var30) : null;
                              String var46 = k6.Q(Q(var18, var22 != null), 160, k6.v.B);
                              String var31 = F(var29, var20);
                              ItemStack var32 = this.v.P() ? this.C(var18) : ItemStack.EMPTY;
                              int var33 = 7 + (var32.isEmpty() ? 0 : 16) + k6.w(var46, k6.v.B) + 6 + k6.x(var31) + 6;
                              float var34 = var45.x() - var33 * var12 / 2.0F;
                              float var35 = var45.y() - 16.0F * var12;
                              float[] var36 = new float[]{var34, var35, var34 + var33 * var12, var35 + 16.0F * var12};
                              if (!this.g.P()
                                 || !var15.stream()
                                    .anyMatch(
                                       var1x -> var36[0] < var1x[2] + 1.0F
                                          && var36[2] + 1.0F > var1x[0]
                                          && var36[1] < var1x[3] + 1.0F
                                          && var36[3] + 1.0F > var1x[1]
                                    )) {
                                 var15.add(var36);
                                 var16.add(new StorageESPModule.x(var45.x(), var45.y(), var33, var46, var31, var32, var26, var27));
                              }
                           }
                        }
                     }
                  }
               }

               for (int var43 = var16.size() - 1; var43 >= 0; var43--) {
                  this.m(var1, (StorageESPModule.x)var16.get(var43), var12);
               }
            } catch (RuntimeException var40) {
               mt.B("ESP", "Storage name tags failed", var40);
            } finally {
               kx.u("StorageESP labels", var3);
            }
         }
      }
   }

   private void m(GuiGraphicsExtractor var1, StorageESPModule.x var2, float var3) {
      var1.pose().pushMatrix();

      try {
         var1.pose().translate(var2.x(), var2.y()).scale(var3).translate(-var2.width() / 2.0F, -16.0F);
         c7.s(var1, 0, 0, var2.width(), 16, _3.U(-435154666, var2.alpha()));
         c7.s(var1, 0, 0, 2, 16, var2.color());
         int var4 = 7;
         if (!var2.icon().isEmpty()) {
            if (var2.alpha() > 0.5) {
               var1.pose().pushMatrix();
               var1.pose().translate(var4, 2.0F).scale(0.75F);
               var1.item(var2.icon(), 0, 0);
               var1.pose().popMatrix();
            }

            var4 += 16;
         }

         k6.r(var1, var2.name(), var4, 4, _3.U(-854792, var2.alpha()), k6.v.B);
         var4 += k6.w(var2.name(), k6.v.B) + 6;
         k6.G(var1, var2.detail(), var4, 4, _3.U(-6511697, var2.alpha()));
      } finally {
         var1.pose().popMatrix();
      }
   }

   private boolean s(Vec3 var1, Vec3 var2, BlockPos var3, BlockPos var4) {
      BlockHitResult var5 = mc.level.clip(new ClipContext(var1, var2, net.minecraft.world.level.ClipContext.Block.COLLIDER, Fluid.NONE, mc.player));
      return var5.getType() != Type.BLOCK ? false : !var5.getBlockPos().equals(var3) && !var5.getBlockPos().equals(var4);
   }

   private ItemStack C(BlockEntity var1) {
      return this.S.computeIfAbsent(var1.getBlockState().getBlock(), var0 -> new ItemStack(var0.asItem()));
   }

   private String h(SpawnerBlockEntity var1) {
      try {
         Entity var2 = var1.getSpawner().getOrCreateDisplayEntity(mc.level, var1.getBlockPos());
         return var2 == null ? null : var2.getType().getDescription().getString();
      } catch (RuntimeException var3) {
         mt.B("ESP", "Spawner mob name unavailable; tag shows the block name", var3);
         return null;
      }
   }

   private record x(float x, float y, int width, String name, String detail, ItemStack icon, int color, double alpha) {
   }
}
