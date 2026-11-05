package com.larpclient.module;

import com.larpclient.d8;
import com.larpclient.ep;
import com.larpclient.hy;
import com.larpclient.j3;
import com.larpclient.ji;
import com.larpclient.m;
import com.larpclient.oo;
import com.larpclient.r4;
import com.larpclient.rr;
import com.larpclient.yu;
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

public final class ScaffoldModule extends Module implements m.s, hy.c {
   private static ScaffoldModule X;
   private final ln Z = this.addSetting(new ln("Auto Switch", "Use suitable hotbar blocks and restore your selected slot", true));
   private final ln H = this.addSetting(new ln("Safe Walk", "Sneak at a ledge while grounded so you stop at the edge; jumping stays manual", true));
   private final ln o = this.addSetting(new ln("Keep Height", "Build at the height captured when enabled or when this setting changes", false));
   private final lp v = this.addSetting(new lp("Rotation", "Turn the actual player while keeping mouse look free", "Instant", "Instant", "Smooth"));
   private final lc P = this.addSetting(new lc("Turn Speed", "Maximum smooth aim speed in degrees per second", 360.0, 30.0, 360.0, 15.0));
   private final lc M = this.addSetting(new lc("Look Ahead", "Extra footprint prediction in your movement direction, in blocks", 0.35, 0.0, 0.6, 0.05));
   private final lc b = this.addSetting(new lc("Range", "Placement reach capped by native block reach", 4.1, 1.0, 6.0, 0.1));
   private final lc f = this.addSetting(new lc("Delay", "Extra ticks between placement attempts; zero allows one per tick", 0.0, 0.0, 10.0, 1.0));
   private final ln m = this.addSetting(new ln("Pause Using Item", "Pause while eating or using an item", true));
   private final ln c = this.addSetting(new ln("Pause Mining", "Pause while the attack key is held", true));
   private final hy t = new hy(this, var1 -> this.addSetting(var1));
   private final ji w = new ji();
   private Object I;
   private Object y;
   private BlockPos l;
   private BlockPos h;
   private float[] T;
   private int i;
   private int L;
   private int x;
   private int z;
   private boolean g;
   private String j = "Inactive";
   private String B = "";
   private long K;
   private int C;
   private int p;
   private int n;
   private boolean J;
   private long Q;

   public ScaffoldModule() {
      super("Scaffold", "Bridge beneath your feet with visible player aim and free mouse look", Category.MOVEMENT, -1);
      X = this;
      this.P.x(() -> this.v.i("Smooth"));
      this.o.W(var1 -> {
         this.T();
         if (this.isInGame()) {
            this.L = j3.O(mc.player.getY());
         }
      });
      this.v.W(var1 -> this.T());
      this.Z.W(var1 -> this.T());
   }

   public static ScaffoldModule v() {
      return X;
   }

   @Override
   public Module.b getTickPhase() {
      return Module.b.w;
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.c);
   }

   @Override
   public hy placementHighlight() {
      return this.t;
   }

   @Override
   public String getStatus() {
      return this.j;
   }

   public List<l6<?>> D() {
      return List.of(this.Z, this.H, this.o, this.v);
   }

   public List<l6<?>> U() {
      return List.of(this.P, this.M, this.b, this.f, this.m, this.c);
   }

   @Override
   public void onEnable() {
      getMc();
      this.T();
      this.x = 0;
      this.I = mc.level;
      this.y = mc.player;
      this.L = j3.O(mc.player.getY());
      this.j = "Ready";
      this.B = "";
      this.K = 0L;
      this.C = this.p = this.n = 0;
      this.J = (boolean)0;
      this.Q = 0L;
      this.t.S();
      r4.V(
         this.getName(),
         "Enabled: real {} player aim, free camera, native placement, safe walk (sneak at ledges)={}, keep height={}; clicks wait until the look already sent reaches the support; selected slot is restored",
         this.v.getValue(),
         this.H.getValue(),
         this.o.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.T();
      this.I = this.y = null;
      this.x = 0;
      this.j = "Inactive";
      if (this.C > 0 || this.p > 0 || this.n > 0) {
         r4.V(
            this.getName(),
            "Disabled after {} placed blocks; clicks waited {} ticks for the look already sent; Safe Walk sneaked at a ledge {} times",
            this.C,
            this.p,
            this.n
         );
      }

      r4.g(this.getName(), "Released placement, independent camera and ledge protection");
   }

   private void T() {
      this.l = this.h = null;
      this.T = null;
      this.i = 0;
      this.g = (boolean)0;
      this.w.q();
      this.t.C();
      com.larpclient.m.g(this);
      rr.N(rr.f.x);
   }

   private boolean V() {
      if (this.isRuntimeActive()
         && mc != null
         && mc.player != null
         && mc.level != null
         && this.I == mc.level
         && this.y == mc.player
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
            && (!this.m.getValue() || !var1.isUsingItem())
            && (!this.c.getValue() || !mc.options.keyAttack.isDown());
      } else {
         return false;
      }
   }

   private boolean U() {
      if (!this.V() || !d8.a() || d8.V()) {
         return false;
      }

      if (!rr.j(rr.f.x)) {
         return false;
      }

      FlyModule var1 = FlyModule.s();
      AirStuckModule var2 = AirStuckModule.H();
      ElytraMotionModule var3 = ElytraMotionModule.S();
      return (var1 == null || !var1.E()) && (var2 == null || !var2.D()) && (var3 == null || !var3.m()) && !TargetStrafeModule.D();
   }

   public boolean v() {
      return !this.isEnabled() ? false : this.V() && (this.l != null || this.g && mc.player.tickCount - this.z >= 0 && mc.player.tickCount - this.z <= 3);
   }

   private float g() {
      return j3.O(yu.q(), mc.player.getYRot());
   }

   private Vec3 g() {
      return mc.player.getEyePosition().add(j3.f(mc.player.getDeltaMovement(), yu.q(), H(mc.player)));
   }

   private float[] E(BlockPos var1) {
      Vec3 var2 = this.g();
      double var3 = this.b.getValue();
      return j3.g(
         var1,
         mc.player.getEyePosition(),
         this.g(),
         mc.player.getYRot(),
         mc.player.getXRot(),
         var4 -> com.larpclient.m.c(var1, var4, var3) != null && com.larpclient.m.Y(var2, var1, var4, var3) != null,
         var3x -> com.larpclient.m.c(var1, var3x, var3) != null
      );
   }

   private float[] J(BlockPos var1, Direction var2, boolean var3) {
      BlockPos var4 = var1.relative(var2);
      Vec3 var5 = this.g();
      double var6 = this.b.getValue();
      Predicate var8 = var4x -> {
         BlockHitResult var5x = com.larpclient.m.c(var1, var4x, var6);
         return var5x != null && var5x.getDirection() == var2;
      };
      return j3.d(
         var1,
         var2,
         com.larpclient.m.S(var1, var2),
         mc.player.getEyePosition(),
         this.g(),
         mc.player.getYRot(),
         mc.player.getXRot(),
         var6x -> var8.test(var6x) && (com.larpclient.m.Y(var5, var1, var6x, var6) != null || j3.g(var4, var5, var6x, var6)),
         var8,
         var3
      );
   }

   private static double H(Player var0) {
      double var1 = var0.getAttributeValue(Attributes.MOVEMENT_SPEED);
      return var0.isSprinting() ? var1 : var1 * 1.3;
   }

   @Override
   public void aimPlacement(float var1, float var2) {
      if (this.isEnabled()) {
         if (this.U() && d8.m(this)) {
            float[] var3 = this.h != null ? this.E(this.h) : this.T;
            if (var3 != null) {
               var1 = var3[0];
               var2 = var3[1];
            }

            float[] var4 = this.v.i("Smooth")
               ? this.w.f(mc.player.tickCount, mc.player.getYRot(), mc.player.getXRot(), var1, var2, this.P.getValue())
               : new float[]{var1, var2};
            ep.t(this, ep.z.d, var4[0], var4[1], 1.0);
            if (!ep.W(ep.z.d)) {
               this.g = (boolean)0;
               rr.N(rr.f.x);
            } else {
               rr.W(rr.f.x);
               this.g = (boolean)1;
               this.z = mc.player.tickCount;
            }
         }
      }
   }

   public float O(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && rr.u() == rr.f.x) {
            mc.player.setYHeadRot(mc.player.getYRot());
            return mc.player.getYRot();
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public static boolean p(Player var0) {
      if (X != null && X.isEnabled()) {
         ScaffoldModule var1 = X;
         return var1 != null && var0 == getMc().player && var1.H.getValue() && var1.U() && var0.onGround() && !mc.options.keyShift.isDown();
      } else {
         return false;
      }
   }

   public static Input D(LocalPlayer var0, Input var1) {
      if (X != null && X.isEnabled()) {
         ScaffoldModule var2 = X;
         if (var2 == null) {
            return var1;
         } else if (!var1.shift() && !var1.jump() && p(var0)) {
            Vec2 var3 = var0.input.moveVector;
            float var4 = oo.Z() && !oo.L() ? rr.h() : var0.getYRot();
            double var5 = Math.toRadians(var4);
            Vec3 var7 = new Vec3(var3.x * Math.cos(var5) - var3.y * Math.sin(var5), 0.0, var3.y * Math.cos(var5) + var3.x * Math.sin(var5));
            Vec3 var8 = j3.f(var0.getDeltaMovement(), var7, H(var0));
            boolean var9 = j3.C(var0.getBoundingBox(), var8, var0.maxUpStep(), var1x -> var0.level().noCollision(var0, var1x));
            var2.e(var9);
            return var9 ? new Input(var1.forward(), var1.backward(), var1.left(), var1.right(), var1.jump(), true, var1.sprint()) : var1;
         } else {
            var2.J = (boolean)0;
            return var1;
         }
      } else {
         return var1;
      }
   }

   private void e(boolean var1) {
      if (var1 && !this.J) {
         this.n++;
         long var2 = System.nanoTime();
         if (var2 - this.Q >= 2000000000L) {
            this.Q = var2;
            r4.V(this.getName(), "Safe Walk sneaked at a ledge ({} times since enable); vanilla sneak keeps you on the edge until a block is placed", this.n);
         }
      }

      this.J = var1;
   }

   public static boolean b(ItemStack var0, BlockGetter var1, BlockPos var2) {
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

   private List<Item> p() {
      LinkedHashSet var1 = new LinkedHashSet();
      Inventory var2 = mc.player.getInventory();
      ItemStack var3 = mc.player.getMainHandItem();
      ItemStack var4 = mc.player.getOffhandItem();
      if (b(var3, mc.level, mc.player.blockPosition())) {
         var1.add(var3.getItem());
      }

      if (b(var4, mc.level, mc.player.blockPosition())) {
         var1.add(var4.getItem());
      }

      if (this.Z.getValue()) {
         ArrayList var5 = new ArrayList();

         for (int var6 = 0; var6 < 9; var6++) {
            ItemStack var7 = var2.getItem(var6);
            if (b(var7, mc.level, mc.player.blockPosition())) {
               var5.add(var7);
            }
         }

         var5.sort(Comparator.comparingInt(ItemStack::getCount).reversed());
         var5.forEach(var1x -> var1.add(var1x.getItem()));
      }

      return List.copyOf(var1);
   }

   private List<BlockPos> m() {
      Vec3 var1 = yu.q().scale(this.M.getValue());
      Vec3 var2 = mc.player.getDeltaMovement().multiply(1.0, 0.0, 1.0);
      return j3.L(mc.player.position(), mc.player.getBoundingBox(), var1.add(var2), this.o.getValue() ? this.L : j3.O(mc.player.getY()));
   }

   private void g(String var1) {
      this.j = var1;
      long var2 = System.nanoTime();
      if (!var1.equals(this.B) && var2 - this.K >= 2000000000L) {
         this.B = var1;
         this.K = var2;
         r4.g(this.getName(), "{}; will retry when the current placement conditions permit", var1);
      }
   }

   @Override
   public void onTick() {
      BlockPos var1 = this.l;
      com.larpclient.m.g(this);
      this.l = this.h = null;
      this.T = null;
      if (!this.U()) {
         this.T();
         this.g("Paused: screen, movement, item use or another camera owns control");
      } else {
         if (!this.v()) {
            rr.N(rr.f.x);
         }

         int var16 = this.x;
         int var15 = mc.player.tickCount;
         if ((Integer.compare(var15, var16) >>> 31 ^ 1) == 0) {
            this.t.i(null);
            this.j = "Placement delay";
         } else {
            List var2 = this.p();
            if (var2.isEmpty()) {
               this.T();
               this.g("No suitable blocks in held hands or hotbar");
            } else {
               List var3 = this.m();
               ArrayList var4 = new ArrayList();

               label83:
               for (BlockPos var6 : var3) {
                  if (com.larpclient.m.Q(var6)) {
                     for (Item var8 : var2) {
                        BlockHitResult var9 = com.larpclient.m.J(var6, var8, this.b.getValue(), true, this.Z.getValue());
                        if (var9 != null) {
                           boolean var10 = com.larpclient.m.G(var9.getBlockPos(), this.b.getValue());
                           var4.add(new ScaffoldModule.r(var6, var8, var9.getBlockPos(), var9.getDirection(), var10));
                           if (var10) {
                              break label83;
                           }
                           break;
                        }
                     }
                  }
               }

               int var19 = j3.O(var4.stream().map(ScaffoldModule.r::sentAim).toList(), var4.stream().map(ScaffoldModule.r::pos).toList().indexOf(var1));
               if (var19 >= 0) {
                  ScaffoldModule.r var20 = (ScaffoldModule.r)var4.get(var19);
                  BlockPos var22 = var20.pos();
                  float[] var10000;
                  if (var20.sentAim()) {
                     BlockPos var10001 = var20.support();
                     Direction var10002 = var20.face();
                     boolean var18 = false;
                     int var17 = this.i;
                     var10000 = this.J(var10001, var10002, (-Integer.signum(var17) >>> 31 ^ 1) == 0);
                  } else {
                     var10000 = null;
                  }

                  float[] var24 = var10000;
                  int var25 = var24 != null ? 1 : 0;
                  this.l = var22;
                  boolean var26 = com.larpclient.m.J(
                     this,
                     var22,
                     var20.item(),
                     this.b.getValue(),
                     true,
                     this.Z.getValue(),
                     () -> this.U() && var22.equals(this.l) && this.m().contains(var22),
                     () -> var25,
                     var1x -> {
                        this.x = mc.player.tickCount + this.f.getValue().intValue() + 1;
                        if (var1x) {
                           this.C++;
                        }

                        this.j = var1x ? "Bridging" : "Placement rejected; retrying";
                     }
                  );
                  if (var26) {
                     this.t.i(var22);
                     if (var25 != 0) {
                        this.T = var24;
                        this.i = 0;
                     } else {
                        byte var12 = 1;
                        int var11 = this.p;
                        this.p = var11 + var12;
                        this.h = var20.support();
                        if (var20.sentAim()) {
                           byte var14 = 1;
                           int var13 = this.i;
                           this.i = var13 + var14;
                        }
                     }

                     this.j = var25 != 0 ? "Aiming at bridge block" : "Turning before the click";
                     return;
                  }

                  this.l = null;
               }

               this.t.i(null);
               BlockPos var21 = j3.T(
                  var3, m::Q, m::S, var1x -> com.larpclient.m.c(var1x, Vec3.atCenterOf(var1x).subtract(mc.player.getEyePosition()), this.b.getValue()) != null
               );
               if (var21 != null) {
                  this.h = var21;
                  float[] var23 = ep.g(mc.player.getEyePosition(), Vec3.atCenterOf(var21));
                  this.aimPlacement(var23[0], var23[1]);
                  this.j = "Looking at the next support";
               } else {
                  this.g("Waiting for a reachable support face beneath your path");
               }
            }
         }
      }
   }

   private record r(BlockPos pos, Item item, BlockPos support, Direction face, boolean sentAim) {
   }
}
