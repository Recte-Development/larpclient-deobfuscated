package com.larpclient.module;

import com.larpclient._d;
import com.larpclient._m;
import com.larpclient.ks;
import com.larpclient.m9;
import com.larpclient.mt;
import com.larpclient.ow;
import com.larpclient.pp;
import com.larpclient.te;
import com.larpclient.tq;
import com.larpclient.wh;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public final class ScaffoldModule extends Module implements _d.s, pp.x {
   private static ScaffoldModule e;
   private final dh h = this.addSetting(new dh("Auto Switch", "Use suitable hotbar blocks and restore your selected slot", true));
   private final dh x = this.addSetting(new dh("Safe Walk", "Sneak at a ledge while grounded so you stop at the edge; jumping stays manual", true));
   private final dh A = this.addSetting(new dh("Keep Height", "Build at the height captured when enabled or when this setting changes", false));
   private final d6 H = this.addSetting(new d6("Rotation", "Turn the actual player while keeping mouse look free", "Instant", "Instant", "Smooth"));
   private final d9 n = this.addSetting(new d9("Turn Speed", "Maximum smooth aim speed in degrees per second", 360.0, 30.0, 360.0, 15.0));
   private final d9 l = this.addSetting(new d9("Look Ahead", "Extra footprint prediction in your movement direction, in blocks", 0.35, 0.0, 0.6, 0.05));
   private final d9 p = this.addSetting(new d9("Range", "Placement reach capped by native block reach", 4.1, 1.0, 6.0, 0.1));
   private final d9 Z = this.addSetting(new d9("Delay", "Extra ticks between placement attempts; zero allows one per tick", 0.0, 0.0, 10.0, 1.0));
   private final dh V = this.addSetting(new dh("Pause Using Item", "Pause while eating or using an item", true));
   private final dh U = this.addSetting(new dh("Pause Mining", "Pause while the attack key is held", true));
   private final pp Q = new pp(this, var1 -> this.addSetting(var1));
   private final ow d = new ow();
   private Object i;
   private Object w;
   private BlockPos X;
   private BlockPos S;
   private float[] c;
   private int P;
   private int D;
   private int T;
   private int J;
   private boolean Y;
   private String N = "Inactive";
   private String s = "";
   private long j;
   private int B;
   private int M;
   private int K;
   private boolean o;
   private long r;

   public ScaffoldModule() {
      super("Scaffold", "Bridge beneath your feet with visible player aim and free mouse look", Category.MOVEMENT, -1);
      e = this;
      this.n.H(() -> this.H.S("Smooth"));
      this.A.h(var1 -> {
         this.R();
         if (this.isInGame()) {
            this.D = wh.u(mc.player.getY());
         }
      });
      this.H.h(var1 -> this.R());
      this.h.h(var1 -> this.R());
   }

   public static ScaffoldModule i() {
      return e;
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.Z;
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.j);
   }

   @Override
   public pp placementHighlight() {
      return this.Q;
   }

   @Override
   public String getStatus() {
      return this.N;
   }

   public List<df<?>> p() {
      return List.of(this.h, this.x, this.A, this.H);
   }

   public List<df<?>> d() {
      return List.of(this.n, this.l, this.p, this.Z, this.V, this.U);
   }

   @Override
   public void onEnable() {
      getMc();
      this.R();
      this.T = 0;
      this.i = mc.level;
      this.w = mc.player;
      this.D = wh.u(mc.player.getY());
      this.N = "Ready";
      this.s = "";
      this.j = 0L;
      this.B = this.M = this.K = 0;
      this.o = (boolean)0;
      this.r = 0L;
      this.Q.w();
      mt.K(
         this.getName(),
         "Enabled: real {} player aim, free camera, native placement, safe walk (sneak at ledges)={}, keep height={}; clicks wait until the look already sent reaches the support; selected slot is restored",
         this.H.P(),
         this.x.P(),
         this.A.P()
      );
   }

   @Override
   public void onDisable() {
      this.R();
      this.i = this.w = null;
      this.T = 0;
      this.N = "Inactive";
      if (this.B > 0 || this.M > 0 || this.K > 0) {
         mt.K(
            this.getName(),
            "Disabled after {} placed blocks; clicks waited {} ticks for the look already sent; Safe Walk sneaked at a ledge {} times",
            this.B,
            this.M,
            this.K
         );
      }

      mt.U(this.getName(), "Released placement, independent camera and ledge protection");
   }

   private void R() {
      this.X = this.S = null;
      this.c = null;
      this.P = 0;
      this.Y = (boolean)0;
      this.d.f();
      this.Q.P();
      _d.N(this);
      tq.A(tq.y.Y);
   }

   private boolean h() {
      if (this.isRuntimeActive()
         && mc != null
         && mc.player != null
         && mc.level != null
         && this.i == mc.level
         && this.w == mc.player
         && mc.player.isAlive()
         && !mc.player.isSpectator()
         && mc.gui.screen() == null
         && mc.isWindowActive()) {
         LocalPlayer var1 = mc.player;
         return !var1.isPassenger()
            && !var1.isFallFlying()
            && !var1.getAbilities().flying
            && !var1.noPhysics
            && !var1.isSleeping()
            && !var1.isMobilityRestricted()
            && !var1.onClimbable()
            && !var1.isInWater()
            && !var1.isInLava()
            && var1.fallDistance <= 3.0
            && (!this.V.P() || !var1.isUsingItem())
            && (!this.U.P() || !mc.options.keyAttack.isDown());
      } else {
         return false;
      }
   }

   private boolean b() {
      if (!this.h() || !_m.z() || _m.l()) {
         return false;
      }

      if (!tq.y(tq.y.Y)) {
         return false;
      }

      FlyModule var1 = FlyModule.d();
      AirStuckModule var2 = AirStuckModule.X();
      ElytraMotionModule var3 = ElytraMotionModule.d();
      return (var1 == null || !var1.s()) && (var2 == null || !var2.U()) && (var3 == null || !var3.z()) && !TargetStrafeModule.Z();
   }

   public boolean n() {
      return !this.isEnabled() ? false : this.h() && (this.X != null || this.Y && mc.player.tickCount - this.J >= 0 && mc.player.tickCount - this.J <= 3);
   }

   private float L() {
      return wh.N(te.S(), mc.player.getYRot());
   }

   private Vec3 Z() {
      return mc.player.getEyePosition().add(wh.M(mc.player.getDeltaMovement(), te.S(), C(mc.player)));
   }

   private float[] H(BlockPos var1) {
      Vec3 var2 = this.Z();
      double var3 = this.p.P();
      return wh.l(
         var1,
         mc.player.getEyePosition(),
         this.L(),
         mc.player.getYRot(),
         mc.player.getXRot(),
         var4 -> _d.Y(var1, var4, var3) != null && _d.q(var2, var1, var4, var3) != null,
         var3x -> _d.Y(var1, var3x, var3) != null
      );
   }

   private float[] O(BlockPos var1, Direction var2, boolean var3) {
      BlockPos var4 = var1.relative(var2);
      Vec3 var5 = this.Z();
      double var6 = this.p.P();
      Predicate var8 = var4x -> {
         BlockHitResult var5x = _d.Y(var1, var4x, var6);
         return var5x != null && var5x.getDirection() == var2;
      };
      return wh.J(
         var1,
         var2,
         _d.k(var1, var2),
         mc.player.getEyePosition(),
         this.L(),
         mc.player.getYRot(),
         mc.player.getXRot(),
         var6x -> var8.test(var6x) && (_d.q(var5, var1, var6x, var6) != null || wh.J(var4, var5, var6x, var6)),
         var8,
         var3
      );
   }

   private static double C(Player var0) {
      double var1 = var0.getAttributeValue(Attributes.MOVEMENT_SPEED);
      return var0.isSprinting() ? var1 : var1 * 1.3;
   }

   @Override
   public void aimPlacement(float var1, float var2) {
      if (this.isEnabled()) {
         if (this.b() && _m.f(this)) {
            float[] var3 = this.S != null ? this.H(this.S) : this.c;
            if (var3 != null) {
               var1 = var3[0];
               var2 = var3[1];
            }

            float[] var4 = this.H.S("Smooth")
               ? this.d.W(mc.player.tickCount, mc.player.getYRot(), mc.player.getXRot(), var1, var2, this.n.P())
               : new float[]{var1, var2};
            m9.k(this, m9.s.p, var4[0], var4[1], 1.0);
            if (!m9.F(m9.s.p)) {
               this.Y = (boolean)0;
               tq.A(tq.y.Y);
            } else {
               tq.i(tq.y.Y);
               this.Y = (boolean)1;
               this.J = mc.player.tickCount;
            }
         }
      }
   }

   public float P(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && tq.l() == tq.y.Y) {
            mc.player.setYHeadRot(mc.player.getYRot());
            return mc.player.getYRot();
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public static boolean T(Player var0) {
      if (e != null && e.isEnabled()) {
         ScaffoldModule var1 = e;
         return var1 != null && var0 == getMc().player && var1.x.P() && var1.b() && var0.onGround() && !mc.options.keyShift.isDown();
      } else {
         return false;
      }
   }

   public static Input J(LocalPlayer var0, Input var1) {
      if (e != null && e.isEnabled()) {
         ScaffoldModule var2 = e;
         if (var2 == null) {
            return var1;
         } else if (!var1.shift() && !var1.jump() && T(var0)) {
            Vec2 var3 = var0.input.moveVector;
            float var4 = ks.N() && !ks.X() ? tq.z() : var0.getYRot();
            double var5 = Math.toRadians(var4);
            Vec3 var7 = new Vec3(var3.x * Math.cos(var5) - var3.y * Math.sin(var5), 0.0, var3.y * Math.cos(var5) + var3.x * Math.sin(var5));
            Vec3 var8 = wh.M(var0.getDeltaMovement(), var7, C(var0));
            boolean var9 = wh.v(var0.getBoundingBox(), var8, var0.maxUpStep(), var1x -> var0.level().noCollision(var0, var1x));
            var2.U(var9);
            return var9 ? new Input(var1.forward(), var1.backward(), var1.left(), var1.right(), var1.jump(), true, var1.sprint()) : var1;
         } else {
            var2.o = (boolean)0;
            return var1;
         }
      } else {
         return var1;
      }
   }

   private void U(boolean var1) {
      if (var1 && !this.o) {
         this.K++;
         long var2 = System.nanoTime();
         if (var2 - this.r >= 2000000000L) {
            this.r = var2;
            mt.K(this.getName(), "Safe Walk sneaked at a ledge ({} times since enable); vanilla sneak keeps you on the edge until a block is placed", this.K);
         }
      }

      this.o = var1;
   }

   public static boolean O(ItemStack var0, BlockGetter var1, BlockPos var2) {
      if (!var0.isEmpty() && var0.getItem() instanceof BlockItem var3) {
         Block var6 = var3.getBlock();
         BlockState var5 = var6.defaultBlockState();
         return !(var6 instanceof FallingBlock)
            && !(var6 instanceof TntBlock)
            && var6 != Blocks.MAGMA_BLOCK
            && var6 != Blocks.RESPAWN_ANCHOR
            && var6 != Blocks.CRAFTING_TABLE
            && var6 != Blocks.SMITHING_TABLE
            && var6 != Blocks.FLETCHING_TABLE
            && var6 != Blocks.NOTE_BLOCK
            && !var5.hasBlockEntity()
            && var5.isCollisionShapeFullBlock(var1, var2);
      } else {
         return false;
      }
   }

   private List<Item> l() {
      LinkedHashSet var1 = new LinkedHashSet();
      Inventory var2 = mc.player.getInventory();
      ItemStack var3 = mc.player.getMainHandItem();
      ItemStack var4 = mc.player.getOffhandItem();
      if (O(var3, mc.level, mc.player.blockPosition())) {
         var1.add(var3.getItem());
      }

      if (O(var4, mc.level, mc.player.blockPosition())) {
         var1.add(var4.getItem());
      }

      if (this.h.P()) {
         ArrayList var5 = new ArrayList();

         for (int var6 = 0; var6 < 9; var6++) {
            ItemStack var7 = var2.getItem(var6);
            if (O(var7, mc.level, mc.player.blockPosition())) {
               var5.add(var7);
            }
         }

         var5.sort(Comparator.comparingInt(ItemStack::getCount).reversed());
         var5.forEach(var1x -> var1.add(var1x.getItem()));
      }

      return List.copyOf(var1);
   }

   private List<BlockPos> t() {
      Vec3 var1 = te.S().scale(this.l.P());
      Vec3 var2 = mc.player.getDeltaMovement().multiply(1.0, 0.0, 1.0);
      return wh.D(mc.player.position(), mc.player.getBoundingBox(), var1.add(var2), this.A.P() ? this.D : wh.u(mc.player.getY()));
   }

   private void T(String var1) {
      this.N = var1;
      long var2 = System.nanoTime();
      if (!var1.equals(this.s) && var2 - this.j >= 2000000000L) {
         this.s = var1;
         this.j = var2;
         mt.U(this.getName(), "{}; will retry when the current placement conditions permit", var1);
      }
   }

   @Override
   public void onTick() {
      BlockPos var1 = this.X;
      _d.N(this);
      this.X = this.S = null;
      this.c = null;
      if (!this.b()) {
         this.R();
         this.T("Paused: screen, movement, item use or another camera owns control");
      } else {
         if (!this.n()) {
            tq.A(tq.y.Y);
         }

         int var16 = this.T;
         int var15 = mc.player.tickCount;
         if ((Integer.compare(var15, var16) >>> 31 ^ 1) == 0) {
            this.Q.m(null);
            this.N = "Placement delay";
         } else {
            List var2 = this.l();
            if (var2.isEmpty()) {
               this.R();
               this.T("No suitable blocks in held hands or hotbar");
            } else {
               List var3 = this.t();
               ArrayList var4 = new ArrayList();

               label82:
               for (BlockPos var6 : var3) {
                  if (_d.Z(var6)) {
                     for (Item var8 : var2) {
                        BlockHitResult var9 = _d.g(var6, var8, this.p.P(), true, this.h.P());
                        if (var9 != null) {
                           boolean var10 = _d.t(var9.getBlockPos(), this.p.P());
                           var4.add(new ScaffoldModule.t(var6, var8, var9.getBlockPos(), var9.getDirection(), var10));
                           if (var10) {
                              break label82;
                           }
                           break;
                        }
                     }
                  }
               }

               int var19 = wh.g(var4.stream().map(ScaffoldModule.t::sentAim).toList(), var4.stream().map(ScaffoldModule.t::pos).toList().indexOf(var1));
               if (var19 >= 0) {
                  ScaffoldModule.t var20 = (ScaffoldModule.t)var4.get(var19);
                  BlockPos var22 = var20.pos();
                  float[] var10000;
                  if (var20.sentAim()) {
                     BlockPos var10001 = var20.support();
                     Direction var10002 = var20.face();
                     boolean var18 = false;
                     int var17 = this.P;
                     var10000 = this.O(var10001, var10002, (-Integer.signum(var17) >>> 31 ^ 1) == 0);
                  } else {
                     var10000 = null;
                  }

                  float[] var24 = var10000;
                  int var25 = var24 != null ? 1 : 0;
                  this.X = var22;
                  boolean var26 = _d.F(
                     this,
                     var22,
                     var20.item(),
                     this.p.P(),
                     true,
                     this.h.P(),
                     () -> this.b() && var22.equals(this.X) && this.t().contains(var22),
                     () -> var25,
                     var1x -> {
                        this.T = mc.player.tickCount + this.Z.P().intValue() + 1;
                        if (var1x) {
                           this.B++;
                        }

                        this.N = var1x ? "Bridging" : "Placement rejected; retrying";
                     }
                  );
                  if (var26) {
                     this.Q.m(var22);
                     if (var25 != 0) {
                        this.c = var24;
                        this.P = 0;
                     } else {
                        byte var12 = 1;
                        int var11 = this.M;
                        this.M = var11 + var12;
                        this.S = var20.support();
                        if (var20.sentAim()) {
                           byte var14 = 1;
                           int var13 = this.P;
                           this.P = var13 + var14;
                        }
                     }

                     this.N = var25 != 0 ? "Aiming at bridge block" : "Turning before the click";
                     return;
                  }

                  this.X = null;
               }

               this.Q.m(null);
               BlockPos var21 = wh.v(var3, _d::Z, _d::A, var1x -> _d.Y(var1x, Vec3.atCenterOf(var1x).subtract(mc.player.getEyePosition()), this.p.P()) != null);
               if (var21 != null) {
                  this.S = var21;
                  float[] var23 = m9.Z(mc.player.getEyePosition(), Vec3.atCenterOf(var21));
                  this.aimPlacement(var23[0], var23[1]);
                  this.N = "Looking at the next support";
               } else {
                  this.T("Waiting for a reachable support face beneath your path");
               }
            }
         }
      }
   }

   private record t(BlockPos pos, Item item, BlockPos support, Direction face, boolean sentAim) {
   }
}
