package com.larpclient.module;

import com.larpclient._o;
import com.larpclient.fr;
import com.larpclient.h_;
import com.larpclient.j_;
import com.larpclient.mi;
import com.larpclient.oh;
import com.larpclient.r4;
import com.larpclient.y8;
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
   private final ln f = this.addSetting(new ln("Chests", "Highlight regular and trapped chests", true));
   private final ln W = this.addSetting(new ln("Ender Chests", "Highlight ender chests", true));
   private final ln H = this.addSetting(new ln("Shulkers", "Highlight shulker boxes", true));
   private final ln V = this.addSetting(new ln("Barrels", "Highlight barrels", false));
   private final ln n = this.addSetting(new ln("Spawners", "Highlight mob spawners", true));
   private final ln Y = this.addSetting(new ln("Hoppers", "Highlight hoppers", false));
   private final ln J = this.addSetting(new ln("Furnaces", "Highlight furnaces", false));
   private final ln B = this.addSetting(new ln("Blast Furnaces", "Highlight blast furnaces", false));
   private final ln E = this.addSetting(new ln("Smokers", "Highlight smokers", false));
   private final ln T = this.addSetting(new ln("Dispensers", "Highlight dispensers", false));
   private final ln w = this.addSetting(new ln("Droppers", "Highlight droppers", false));
   private final ln c = this.addSetting(new ln("Crafters", "Highlight crafters", false));
   private final ln X = this.addSetting(new ln("Brewing Stands", "Highlight brewing stands", false));
   private final ln y = this.addSetting(new ln("Beehives", "Highlight beehives", false));
   private final ln r = this.addSetting(new ln("Decorated Pots", "Highlight decorated pots", false));
   private final ln L = this.addSetting(new ln("Chiseled Bookshelves", "Highlight chiseled bookshelves", false));
   private final ln l = this.addSetting(new ln("Jukeboxes", "Highlight jukeboxes", false));
   private final lc K = this.addSetting(new lc("Range", "Loaded storage scan radius", 64.0, 16.0, 128.0, 8.0));
   private final lc g = this.addSetting(new lc("Max Blocks", "Nearest storage blocks drawn", 256.0, 16.0, 512.0, 16.0));
   private final lp i = this.addSetting(new lp("Mode", "Storage highlight style", "Filled", "Box", "Corners", "Filled"));
   private final ln A = this.addSetting(new ln("Labels", "Show storage names above highlighted blocks", true));
   private final lc p = this.addSetting(new lc("Label Range", "Maximum name-tag distance, within the storage scan range", 128.0, 16.0, 128.0, 8.0));
   private final lc u = this.addSetting(new lc("Max Labels", "Nearest storage name tags drawn", 64.0, 1.0, 128.0, 1.0));
   private final lc z = this.addSetting(new lc("Label Scale", "Name-tag size on screen, the same at every distance", 1.0, 0.5, 2.0, 0.1));
   private final ln Q = this.addSetting(new ln("Label Icons", "Show the block item beside each storage name", true));
   private final ln o = this.addSetting(new ln("Hide Overlaps", "Skip farther name tags that would cover nearer ones", true));
   private final ln a = this.addSetting(new ln("Boxes", "Draw storage highlight boxes", true));
   private final ln x = this.addSetting(new ln("Tracers", "Draw lines to selected storage blocks", false));
   private final lp q = this.addSetting(new lp("Tracer Origin", "Start at the camera crosshair or player feet", "Crosshair", "Crosshair", "Feet"));
   private final lc e = this.addSetting(new lc("Tracer Range", "Maximum line distance, within scan range", 64.0, 16.0, 128.0, 8.0));
   private final lc Z = this.addSetting(new lc("Max Tracers", "Nearest selected storage lines drawn", 64.0, 1.0, 128.0, 1.0));
   private final mi S;
   private final List<BlockEntity> N;
   private final Map<Block, ItemStack> I;
   private ClientLevel D;
   private int s;
   private static StorageESPModule t;
   private static boolean C;
   private static final int k = 16;
   private static final int h = 12;

   public StorageESPModule() {
      super("StorageESP", "Highlight storage blocks", Category.RENDER, -1);
      StorageESPModule var1 = this;
      this.S = new mi(var1x -> var1.addSetting(var1x));
      this.N = new ArrayList<>();
      this.I = new HashMap<>();

      for (l6 var2 : List.of(this.p, this.u, this.z, this.Q, this.o)) {
         var2.x(this.A::getValue);
      }

      for (l6 var6 : List.of(this.q, this.e, this.Z)) {
         var6.x(this.x::getValue);
      }

      this.i.x(this.a::getValue);
      this.S
         .J(
            () -> this.a.getValue() || this.x.getValue() || this.A.getValue(),
            () -> this.a.getValue() || this.x.getValue(),
            () -> this.a.getValue() || this.x.getValue() || this.A.getValue(),
            () -> this.a.getValue() && this.i.i("Filled")
         );

      for (l6 var7 : this.J()) {
         ((ln)var7).W(var1x -> this.x());
      }

      this.K.W(var1x -> this.x());
      this.g.W(var1x -> this.x());
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1x -> this.M());
      t = this;
      if (!C) {
         C = (boolean)1;
         HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("larp-client", "storage_esp_labels"), (var0, var1x) -> t.W(var0, var1x));
      }
   }

   @Override
   public void onEnable() {
      this.s = 0;
      this.N.clear();
      this.D = null;
      r4.g("ESP", "StorageESP enabled; received loaded storage only, tracers={}", this.x.getValue());
   }

   @Override
   public void onDisable() {
      r4.g("ESP", "StorageESP cleanup; discarded {} cached blocks", this.N.size());
      this.N.clear();
      this.I.clear();
      this.D = null;
   }

   public List<l6<?>> J() {
      return List.of(this.f, this.W, this.H, this.V, this.n, this.Y, this.J, this.B, this.E, this.T, this.w, this.c, this.X, this.y, this.r, this.L, this.l);
   }

   public List<l6<?>> q() {
      return List.of(this.K, this.g);
   }

   public List<l6<?>> Y() {
      return List.of(this.K, this.a, this.i, this.A, this.x, this.q);
   }

   public List<l6<?>> U() {
      return List.of(this.g, this.p, this.u, this.e, this.Z);
   }

   public List<l6<?>> N() {
      return List.of(this.x, this.q, this.e, this.Z);
   }

   public List<l6<?>> T() {
      return List.of(this.a, this.i);
   }

   public List<l6<?>> y() {
      ArrayList var1 = new ArrayList<>(this.S.t());
      var1.addAll(this.S.J());
      var1.addAll(this.S.Q());
      var1.addAll(List.of(this.z, this.Q, this.o));
      return List.copyOf(var1);
   }

   private void x() {
      this.s = 0;
      this.N.clear();
   }

   public static Vec3 q(Vec3 var0, Quaternionf var1, Vec3 var2, String var3) {
      if (var3.equals("Feet")) {
         return var2.add(0.0, 0.1, 0.0);
      }

      Vector3f var4 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var1);
      return var0.add(var4.x * 0.5, var4.y * 0.5, var4.z * 0.5);
   }

   public List<l6<?>> A() {
      return List.of(this.A, this.p, this.u, this.z, this.Q, this.o);
   }

   public static String z(BlockEntity var0) {
      return var0.getBlockState().getBlock().getName().getString();
   }

   public static BlockPos I(BlockEntity var0) {
      BlockState var1 = var0.getBlockState();
      return var1.getBlock() instanceof ChestBlock && var1.hasProperty(ChestBlock.TYPE) && var1.getValue(ChestBlock.TYPE) != ChestType.SINGLE
         ? ChestBlock.getConnectedBlockPos(var0.getBlockPos(), var1)
         : null;
   }

   public static String I(BlockEntity var0, boolean var1) {
      return var1 ? z(var0) + " x2" : z(var0);
   }

   public static String u(String var0, double var1) {
      String var3 = (int)var1 + "m";
      return var0 != null && !var0.isBlank() ? var0 + " · " + var3 : var3;
   }

   private boolean A(BlockEntity var1) {
      if (var1 instanceof DropperBlockEntity) {
         return this.w.getValue();
      } else {
         return var1 instanceof DispenserBlockEntity
            ? this.T.getValue()
            : var1 instanceof ChestBlockEntity && this.f.getValue()
               || var1 instanceof EnderChestBlockEntity && this.W.getValue()
               || var1 instanceof ShulkerBoxBlockEntity && this.H.getValue()
               || var1 instanceof BarrelBlockEntity && this.V.getValue()
               || var1 instanceof SpawnerBlockEntity && this.n.getValue()
               || var1 instanceof HopperBlockEntity && this.Y.getValue()
               || var1 instanceof FurnaceBlockEntity && this.J.getValue()
               || var1 instanceof BlastFurnaceBlockEntity && this.B.getValue()
               || var1 instanceof SmokerBlockEntity && this.E.getValue()
               || var1 instanceof CrafterBlockEntity && this.c.getValue()
               || var1 instanceof BrewingStandBlockEntity && this.X.getValue()
               || var1 instanceof BeehiveBlockEntity && this.y.getValue()
               || var1 instanceof DecoratedPotBlockEntity && this.r.getValue()
               || var1 instanceof ChiseledBookShelfBlockEntity && this.L.getValue()
               || var1 instanceof JukeboxBlockEntity && this.l.getValue();
      }
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.D != mc.level) {
            this.N.clear();
            this.D = mc.level;
            this.s = 0;
         }

         int var10000 = this.s;
         byte var13 = 1;
         int var12 = this.s;
         this.s = var12 + var13;
         var10000 %= 10;
         boolean var15 = false;
         int var14 = var10000;
         var10000 = Integer.signum(var14);
         if (((var10000 | -var10000) >>> 31 ^ 1) != 0) {
            this.N.clear();
            var10000 = (int)Math.ceil(this.K.getValue() / 16.0);
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
                           && this.A(var7)
                           && mc.player.distanceToSqr(Vec3.atCenterOf(var7.getBlockPos())) <= this.K.getValue() * this.K.getValue()) {
                           this.N.add(var7);
                        }
                     }
                  }
               }
            }

            this.N.sort(Comparator.comparingDouble(var0 -> mc.player.distanceToSqr(Vec3.atCenterOf(var0.getBlockPos()))));
            var10000 = this.N.size();
            int var11 = this.g.getValue().intValue();
            int var10 = var10000;
            if ((-Integer.compare(var10, var11) >>> 31 ^ 1) == 0) {
               this.N.subList(this.g.getValue().intValue(), this.N.size()).clear();
            }
         }
      }
   }

   private boolean m(BlockEntity var1) {
      if (!var1.isRemoved() && this.A(var1)) {
         BlockPos var2 = var1.getBlockPos();
         LevelChunk var3 = mc.level.getChunkSource().getChunkNow(var2.getX() >> 4, var2.getZ() >> 4);
         return var3 != null && var3.getBlockEntities().get(var2) == var1;
      } else {
         return false;
      }
   }

   private AABB M(BlockEntity var1) {
      BlockPos var2 = var1.getBlockPos();
      VoxelShape var3 = var1.getBlockState().getShape(mc.level, var2);
      return (var3.isEmpty() ? new AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0) : var3.bounds()).move(var2).inflate(0.003);
   }

   private int T(BlockEntity var1, double var2) {
      int var4 = var1 instanceof SpawnerBlockEntity
         ? -943189
         : (var1 instanceof EnderChestBlockEntity ? -5071873 : (var1 instanceof ShulkerBoxBlockEntity ? -1400362 : -1521772));
      return this.S.k(var4, var2, this.K.getValue(), 1.0, var1.getBlockPos().hashCode());
   }

   private void M() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.D == mc.level) {
            try {
               int var1 = 0;
               int var2 = 0;
               Camera var3 = mc.gameRenderer.mainCamera();
               Vec3 var4 = this.x.getValue() ? q(var3.position(), var3.rotation(), mc.player.position(), this.q.getValue()) : null;

               for (BlockEntity var6 : this.N) {
                  if (this.m(var6)) {
                     double var7 = mc.player.position().distanceTo(Vec3.atCenterOf(var6.getBlockPos()));
                     if (!(var7 >= this.K.getValue())) {
                        if (var1++ >= this.g.getValue().intValue()) {
                           break;
                        }

                        AABB var9 = this.M(var6);
                        int var10 = this.T(var6, var7);
                        if (this.a.getValue()) {
                           this.S.I(var9, this.i.getValue(), var10);
                        }

                        if (var4 != null && var7 <= this.e.getValue() && var2++ < this.Z.getValue().intValue() && var10 >>> 24 != 0) {
                           this.S.D(var4, var9.getCenter(), var10);
                        }
                     }
                  }
               }
            } catch (RuntimeException var11) {
               r4.T("ESP", "Storage overlay failed", var11);
            }
         }
      }
   }

   private void W(GuiGraphicsExtractor var1, DeltaTracker var2) {
      if (this.A.getValue() && this.isRuntimeActive() && this.isInGame() && this.D == mc.level && !mc.gameRenderer.gameRenderState().guiRenderState.isHudHidden
         )
       {
         var1 = y8.U(var1, true, false);
         if (var1 != null) {
            long var3 = h_.U();

            try {
               Camera var5 = mc.gameRenderer.mainCamera();
               Vec3 var6 = var5.position();
               Matrix4f var7 = var5.getViewRotationProjectionMatrix(new Matrix4f());
               Vector3f var8 = var5.rotation().transform(new Vector3f(0.0F, 1.0F, 0.0F));
               int var9 = mc.getWindow().getGuiScaledWidth();
               int var10 = mc.getWindow().getGuiScaledHeight();
               boolean var11 = RenderSystem.getDevice().getDeviceInfo().isZZeroToOne();
               float var12 = this.z.getValue().floatValue();
               int var13 = this.u.getValue().intValue();
               HashSet var14 = new HashSet();
               ArrayList var15 = new ArrayList();
               ArrayList var16 = new ArrayList();

               for (BlockEntity var18 : this.N) {
                  if (var16.size() >= var13) {
                     break;
                  }

                  if (this.m(var18)) {
                     BlockPos var19 = var18.getBlockPos();
                     double var20 = mc.player.position().distanceTo(Vec3.atCenterOf(var19));
                     if (!(var20 >= this.K.getValue()) && !(var20 > this.p.getValue())) {
                        BlockPos var22 = I(var18);
                        AABB var23 = this.M(var18);
                        if (var22 != null) {
                           if (var14.contains(var22)) {
                              continue;
                           }

                           if (mc.level.getBlockEntity(var22) instanceof ChestBlockEntity var24 && this.m(var24)) {
                              var23 = var23.minmax(this.M(var24));
                           } else {
                              var22 = null;
                           }
                        }

                        var14.add(var19);
                        Vec3 var44 = new Vec3((var23.minX + var23.maxX) / 2.0, var23.maxY + 0.25, (var23.minZ + var23.maxZ) / 2.0);
                        _o.t var45 = _o.p(var44, var6, var7, var8, var9, var10, var11);
                        if (var45 != null) {
                           int var26 = this.T(var18, var20);
                           double var27 = (var26 >>> 24) / 255.0;
                           if (!(var27 < 0.03) && (this.S.K() || !this.Q(var6, var44, var19, var22))) {
                              String var29 = var18 instanceof SpawnerBlockEntity var30 ? this.T(var30) : null;
                              String var46 = j_.Y(I(var18, var22 != null), 160, j_.w.Q);
                              String var31 = u(var29, var20);
                              ItemStack var32 = this.Q.getValue() ? this.q(var18) : ItemStack.EMPTY;
                              int var33 = 7 + (var32.isEmpty() ? 0 : 16) + j_.E(var46, j_.w.Q) + 6 + j_.L(var31) + 6;
                              float var34 = var45.x() - var33 * var12 / 2.0F;
                              float var35 = var45.y() - 16.0F * var12;
                              float[] var36 = new float[]{var34, var35, var34 + var33 * var12, var35 + 16.0F * var12};
                              if (!this.o.getValue()
                                 || !var15.stream()
                                    .anyMatch(
                                       var1x -> var36[0] < var1x[2] + 1.0F
                                          && var36[2] + 1.0F > var1x[0]
                                          && var36[1] < var1x[3] + 1.0F
                                          && var36[3] + 1.0F > var1x[1]
                                    )) {
                                 var15.add(var36);
                                 var16.add(new StorageESPModule.d(var45.x(), var45.y(), var33, var46, var31, var32, var26, var27));
                              }
                           }
                        }
                     }
                  }
               }

               for (int var43 = var16.size() - 1; var43 >= 0; var43--) {
                  this.d(var1, (StorageESPModule.d)var16.get(var43), var12);
               }
            } catch (RuntimeException var40) {
               r4.T("ESP", "Storage name tags failed", var40);
            } finally {
               h_.n("StorageESP labels", var3);
            }
         }
      }
   }

   private void d(GuiGraphicsExtractor var1, StorageESPModule.d var2, float var3) {
      var1.pose().pushMatrix();

      try {
         var1.pose().translate(var2.x(), var2.y()).scale(var3).translate(-var2.width() / 2.0F, -16.0F);
         fr.k(var1, 0, 0, var2.width(), 16, oh.S(-435154666, var2.alpha()));
         fr.k(var1, 0, 0, 2, 16, var2.color());
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

         j_.y(var1, var2.name(), var4, 4, oh.S(-854792, var2.alpha()), j_.w.Q);
         var4 += j_.E(var2.name(), j_.w.Q) + 6;
         j_.c(var1, var2.detail(), var4, 4, oh.S(-6511697, var2.alpha()));
      } finally {
         var1.pose().popMatrix();
      }
   }

   private boolean Q(Vec3 var1, Vec3 var2, BlockPos var3, BlockPos var4) {
      BlockHitResult var5 = mc.level.clip(new ClipContext(var1, var2, net.minecraft.world.level.ClipContext.Block.COLLIDER, Fluid.NONE, mc.player));
      return var5.getType() != Type.BLOCK ? false : !var5.getBlockPos().equals(var3) && !var5.getBlockPos().equals(var4);
   }

   private ItemStack q(BlockEntity var1) {
      return this.I.computeIfAbsent(var1.getBlockState().getBlock(), var0 -> new ItemStack(var0.asItem()));
   }

   private String T(SpawnerBlockEntity var1) {
      try {
         Entity var2 = var1.getSpawner().getOrCreateDisplayEntity(mc.level, var1.getBlockPos());
         return var2 == null ? null : var2.getType().getDescription().getString();
      } catch (RuntimeException var3) {
         r4.T("ESP", "Spawner mob name unavailable; tag shows the block name", var3);
         return null;
      }
   }

   private record d(float x, float y, int width, String name, String detail, ItemStack icon, int color, double alpha) {
   }
}
