package com.larpclient.module;

import com.larpclient._n;
import com.larpclient._q;
import com.larpclient.ij;
import com.larpclient.nz;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public final class TargetStrafeModule extends Module {
   private static final int H = 3;
   private static final int V = 10;
   private static final float g = 25.0F;
   private static final double M = Math.PI / 3;
   private static final double B = 0.7;
   private static final double J = 1.0;
   private static final float[] a = new float[]{0.0F, 0.0F};
   private static TargetStrafeModule w;
   private final o d = this.addSetting(
      new o(
         "Mode",
         "Native presses real movement keys at normal speed for anti-cheat servers; Fast overrides your speed and will flag on them",
         "Native",
         "Native",
         "Fast"
      )
   );
   private final b N = this.addSetting(new b("Distance", "Distance to hold from the target center, capped inside actual attack reach", 2.4, 1.5, 3.0, 0.1));
   private final b P = this.addSetting(new b("Speed", "Horizontal orbit speed in blocks per tick", 1.0, 0.1, 1.2, 0.1));
   private final b b = this.addSetting(new b("Radius", "Distance from the target center, capped inside actual attack reach", 1.0, 1.0, 3.0, 0.1));
   private final o i = this.addSetting(
      new o("Direction", "Starting orbit direction; reverse when the path is blocked", "Clockwise", "Clockwise", "Counterclockwise")
   );
   private final m K = this.addSetting(new m("Auto Jump", "Jump while orbiting when grounded with clear headroom", false));
   private final m r = this.addSetting(new m("Avoid Edges", "Require ground below the orbit path; sneak pauses automatic movement", true));
   private LocalPlayer s;
   private LocalPlayer W;
   private ClientLevel f;
   private LivingEntity p;
   private long I;
   private boolean T;
   private int y = 1;
   private int G;
   private int D;
   private int z;
   private float[] l;
   private float o;
   private long m;
   private String U = "";
   private String c = "Waiting for Kill Aura";
   private Input k;
   private Input h;
   private Vec2 X;
   private Vec2 O;

   public TargetStrafeModule() {
      super("Target Strafe", "Automatically circle Kill Aura's target", Category.MOVEMENT, -1);
      w = this;
      this.N.a(() -> !this.l());
      this.P.a(this::l);
      this.b.a(this::l);
   }

   private boolean l() {
      return this.d.w("Fast");
   }

   @Override
   public Module.a getTickPhase() {
      return Module.a.T;
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(com.larpclient.module.l.F, com.larpclient.module.l.k);
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() ? this.c : "Inactive";
   }

   @Override
   public void onEnable() {
      this.V();
      this.G = 0;
      this.W("Waiting for Kill Aura");
      _q.c(
         "TargetStrafe",
         "Enabled: mode={}, distance={}, fast speed={} blocks/tick, fast radius={}, auto jump={}, edge checks={}",
         this.d.getValue(),
         this.N.getValue(),
         this.P.getValue(),
         this.b.getValue(),
         this.K.getValue(),
         this.r.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.V();
      this.G = 0;
      this.W("Inactive");
   }

   private void V() {
      this.t();
      this.s = null;
      this.f = null;
      this.p = null;
      this.T = (boolean)0;
      this.U = "";
      this.l = null;
      this.D = this.z = 0;
   }

   private void W(String var1) {
      if (!this.c.equals(var1)) {
         this.b(var1);
      }

      this.c = var1;
   }

   private void b(String var1) {
      long var2 = System.nanoTime();
      if (var2 - this.m >= 1000000000L) {
         _q.c("TargetStrafe", "{}", var1);
         this.m = var2;
      }
   }

   @Override
   public void onTick() {
      this.t();
      this.T = (boolean)0;
      boolean var3 = false;
      int var2 = this.G;
      if ((-Integer.signum(var2) >>> 31 ^ 1) == 0) {
         byte var5 = 1;
         int var4 = this.G;
         this.G = var4 - var5;
         this.V();
         this.W("Recovering after position correction");
      } else {
         LivingEntity var1 = this.g();
         if (var1 == null) {
            this.V();
         } else {
            label28: {
               if (var1 == this.p) {
                  int var10000 = this.i.getValue().equals(this.U);
                  boolean var7 = false;
                  byte var6 = (byte)var10000;
                  var10000 = Integer.signum(var6);
                  if ((var10000 | -var10000) >>> 31 != 0) {
                     break label28;
                  }
               }

               int var10001 = this.i.w("Clockwise");
               boolean var9 = false;
               byte var8 = (byte)var10001;
               var10001 = Integer.signum(var8);
               this.y = ((var10001 | -var10001) >>> 31 ^ 1) == 0 ? 1 : -1;
               this.U = this.i.getValue();
               this.l = null;
               this.z = 0;
               this.b("Orbit target acquired: entity=" + var1.getId());
            }

            this.s = mc.player;
            this.f = mc.level;
            this.I = mc.level.getGameTime();
            this.p = var1;
         }
      }
   }

   private LivingEntity g() {
      if (this.isInGame() && nz.v() && mc.isWindowActive() && mc.gui.screen() == null && !mc.isPaused()) {
         LocalPlayer var1 = mc.player;
         FlyModule var2 = FlyModule.Q();
         AirStuckModule var3 = AirStuckModule.Z();
         ElytraMotionModule var4 = ElytraMotionModule.n();
         if ((var2 == null || !var2.r())
            && (var3 == null || !var3.J())
            && (var4 == null || !var4.Q())
            && _n.P() == null
            && ij.L() != ij.r.D
            && ij.L() != ij.r.s
            && ij.L() != ij.r.I) {
            if (var1.isPassenger()
               || var1.isSpectator()
               || var1.getAbilities().flying
               || var1.isFallFlying()
               || var1.isInWater()
               || var1.isInLava()
               || var1.onClimbable()
               || var1.isSleeping()
               || var1.isMobilityRestricted()
               || var1.noPhysics
               || var1.isUsingItem()
               || var1.isCrouching()
               || var1.isShiftKeyDown()
               || mc.options.keyShift.isDown()) {
               this.W("Paused for player movement or item use");
               return null;
            } else if (this.l() && var1.hurtTime > 0) {
               this.W("Paused for knockback");
               return null;
            } else {
               AuraModule var5 = AuraModule.t();
               LivingEntity var6 = var5 == null ? null : var5.R();
               if (var6 == null) {
                  this.W("Waiting for Kill Aura target");
                  return null;
               } else if (T(var1.getEyePosition(), var6.getBoundingBox(), this.N(), var5.v()) <= 0.0) {
                  this.W("Target is outside vertical attack reach");
                  return null;
               } else {
                  return var6;
               }
            }
         } else {
            this.W("Paused for another movement controller");
            return null;
         }
      } else {
         this.W("Waiting for gameplay");
         return null;
      }
   }

   private boolean O(LocalPlayer var1) {
      return this.isRuntimeActive()
         && this.G == 0
         && var1 != null
         && var1 == this.s
         && var1 == getMc().player
         && this.f == mc.level
         && this.I == mc.level.getGameTime()
         && this.p != null
         && this.g() == this.p;
   }

   private double N() {
      return this.l() ? this.b.getValue() : this.N.getValue();
   }

   public static boolean v() {
      if (w != null && w.isEnabled()) {
         TargetStrafeModule var0 = w;
         if (var0 == null) {
            return false;
         }

         try {
            return var0.O(getMc().player);
         } catch (RuntimeException var2) {
            var0.B("movement ownership", var2);
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean g() {
      if (w != null && w.isEnabled()) {
         TargetStrafeModule var0 = w;
         return var0 != null && var0.l() && v();
      } else {
         return false;
      }
   }

   public static Input f(LocalPlayer var0, Input var1) {
      if (w != null && w.isEnabled()) {
         TargetStrafeModule var2 = w;
         if (var2 == null) {
            return var1;
         }

         try {
            if (!var2.O(var0)) {
               return var1;
            }

            if (var1.shift()) {
               var2.V();
               var2.W("Paused for sneak input");
               return var1;
            }

            if (!var2.l()) {
               return var2.E(var0, var1);
            }

            Vec3 var3 = var2.P();
            Vec2 var4 = m(var3, var0.getYRot());
            return var2.A(
               var0,
               var1,
               var4,
               new Input(
                  var4.y > 0.1F,
                  var4.y < -0.1F,
                  var4.x > 0.1F,
                  var4.x < -0.1F,
                  var1.jump() || var2.v(var0, var3.lengthSqr() > 0.0),
                  var1.shift(),
                  var1.sprint()
               )
            );
         } catch (RuntimeException var5) {
            var2.B("input", var5);
            return var1;
         }
      } else {
         return var1;
      }
   }

   private Input E(LocalPlayer var1, Input var2) {
      float[] var3 = this.Y();
      int var4 = var3[0] > 0.0F ? 1 : 0;
      int var5 = var3[0] == 0.0F && var3[1] == 0.0F ? 0 : 1;
      return this.A(
         var1,
         var2,
         new Vec2(var3[1], var3[0]).normalized(),
         new Input(
            (boolean)var4,
            var3[0] < 0.0F,
            var3[1] > 0.0F,
            var3[1] < 0.0F,
            var2.jump() || this.v(var1, (boolean)var5),
            var2.shift(),
            var4 != 0 && !var1.horizontalCollision
         )
      );
   }

   private boolean v(LocalPlayer var1, boolean var2) {
      return var2
         && this.K.getValue()
         && var1.onGround()
         && var1.getDeltaMovement().y <= 0.0
         && !var1.getAbilities().mayfly
         && mc.level.noCollision(var1, var1.getBoundingBox().expandTowards(0.0, 1.3, 0.0));
   }

   private Input A(LocalPlayer var1, Input var2, Vec2 var3, Input var4) {
      this.W = var1;
      this.k = var2;
      this.X = var1.input.moveVector;
      this.O = var3;
      this.h = var4;
      var1.input.moveVector = var3;
      return var4;
   }

   public static boolean t() {
      if (w != null && w.isEnabled()) {
         TargetStrafeModule var0 = w;
         return var0 != null
            && var0.W != null
            && var0.W == getMc().player
            && var0.f == mc.level
            && var0.I == mc.level.getGameTime()
            && var0.W.input.moveVector == var0.O;
      } else {
         return false;
      }
   }

   private void t() {
      if (this.W != null && this.W == getMc().player && this.f == mc.level && this.I == mc.level.getGameTime()) {
         if (this.W.input.keyPresses == this.h) {
            this.W.input.keyPresses = this.k;
         }

         if (this.W.input.moveVector == this.O) {
            this.W.input.moveVector = this.X;
         }
      }

      this.W = null;
      this.k = this.h = null;
      this.X = this.O = null;
   }

   public static Vec3 c(LocalPlayer var0, Vec3 var1) {
      if (w != null && w.isEnabled()) {
         TargetStrafeModule var2 = w;
         if (var2 != null && !var2.T) {
            try {
               if (!var2.O(var0)) {
                  var2.t();
                  return var1;
               }

               var2.T = (boolean)1;
               if (!var2.l()) {
                  return var1;
               }

               Vec3 var3 = var2.P();
               Vec3 var4 = new Vec3(var3.x, var1.y, var3.z);
               var0.setDeltaMovement(var4);
               return var4;
            } catch (RuntimeException var5) {
               var2.B("movement", var5);
               return var1;
            }
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private void B(String var1, RuntimeException var2) {
      _q.o("TargetStrafe", "Failed during " + var1 + "; disabling and releasing orbit input", var2);
      this.disable();
   }

   public static void m(LocalPlayer var0) {
      TargetStrafeModule var1 = w;
      if (var1 != null && var1.isRuntimeActive() && var0 == getMc().player) {
         if (var1.G == 0) {
            _q.z("TargetStrafe", "Position correction while active: mode=" + var1.d.getValue() + ", status=" + var1.c + "; pausing for 20 ticks");
         }

         var1.V();
         var1.G = 20;
         var1.W("Recovering after position correction");
      }
   }

   private float[] Y() {
      LocalPlayer var1 = mc.player;
      float var2 = var1.getYRot();
      if (this.z > 0) {
         this.z--;
      }

      double var3 = T(var1.getEyePosition(), this.p.getBoundingBox(), this.N.getValue(), AuraModule.t().v());
      if (var3 <= 0.0) {
         this.l = null;
         return a;
      }

      if (this.l != null && this.D-- > 0 && Math.abs(Mth.wrapDegrees(var2 - this.o)) <= 25.0F && this.Z(this.l, var2)) {
         return this.l;
      }

      Vec3 var5 = this.p.getBoundingBox().getCenter();
      float[] var6 = h(var1.position(), var5, var3, this.y, var2);
      if (!this.Z(var6, var2)) {
         float[] var7 = this.z == 0 ? h(var1.position(), var5, var3, -this.y, var2) : null;
         if (var7 == null || !this.Z(var7, var2)) {
            this.l = null;
            this.W("Orbit blocked by terrain or edge");
            return a;
         }

         this.y = -this.y;
         this.z = 10;
         var6 = var7;
         this.b("Reversed orbit at blocked path or edge");
      }

      if (!Arrays.equals(var6, this.l)) {
         this.b(
            String.format(
               Locale.ROOT,
               "Keys forward=%.0f strafe=%.0f distance=%.2f ring=%.2f",
               var6[0],
               var6[1],
               var1.position().subtract(var5).horizontalDistance(),
               var3
            )
         );
      }

      this.l = var6;
      this.D = 2;
      this.o = var2;
      this.W("Orbiting");
      return var6;
   }

   private boolean Z(float[] var1, float var2) {
      Vec3 var3 = GrimSpeedModule.Q(var1[1], var1[0], var2);
      return this.a(var3.scale(mc.player.onGround() ? 0.7 : 1.0));
   }

   private Vec3 P() {
      LocalPlayer var1 = mc.player;
      double var2 = T(var1.getEyePosition(), this.p.getBoundingBox(), this.b.getValue(), AuraModule.t().v());
      if (var2 <= 0.0) {
         return Vec3.ZERO;
      } else {
         Vec3 var4 = this.p.getBoundingBox().getCenter();
         Vec3 var5 = E(var1.position(), var4, var2, this.P.getValue(), this.y, var1.getYRot());
         if (this.a(var5)) {
            this.W("Orbiting");
            return var5;
         } else {
            Vec3 var6 = E(var1.position(), var4, var2, this.P.getValue(), -this.y, var1.getYRot());
            if (this.a(var6)) {
               this.y = -this.y;
               this.b("Reversed orbit at blocked path or edge");
               this.W("Orbiting");
               return var6;
            } else {
               this.W("Orbit blocked by terrain or edge");
               return Vec3.ZERO;
            }
         }
      }
   }

   private boolean a(Vec3 var1) {
      LocalPlayer var2 = mc.player;
      AABB var3 = var2.getBoundingBox().deflate(0.001);
      if (!mc.level.noCollision(var2, var3.expandTowards(var1.x, Math.max(0.0, var2.getDeltaMovement().y), var1.z))) {
         return false;
      }

      int var4 = Math.max(1, (int)Math.ceil(var1.horizontalDistance() / 0.25));
      double var5 = var2.onGround() ? 0.6 : 2.0;

      for (int var7 = 1; var7 <= var4; var7++) {
         double var8 = var2.getX() + var1.x * var7 / var4;
         double var10 = var2.getZ() + var1.z * var7 / var4;
         if (!mc.level.hasChunkAt(BlockPos.containing(var8, var2.getY(), var10))) {
            return false;
         }

         if (this.r.getValue() && mc.level.noBlockCollision(var2, new AABB(var8 - 0.1, var3.minY - var5, var10 - 0.1, var8 + 0.1, var3.minY, var10 + 0.1))) {
            return false;
         }
      }

      return true;
   }

   public static float[] h(Vec3 var0, Vec3 var1, double var2, int var4, float var5) {
      return Double.isFinite(var2)
            && !(var2 <= 0.0)
            && Float.isFinite(var5)
            && Double.isFinite(var0.x)
            && Double.isFinite(var0.z)
            && Double.isFinite(var1.x)
            && Double.isFinite(var1.z)
         ? AuraModule.a(v(var0, var1, var2, var4, var5), var5)
         : a;
   }

   public static double v(Vec3 var0, Vec3 var1, double var2, int var4, float var5) {
      double var6 = var0.x - var1.x;
      double var8 = var0.z - var1.z;
      double var10 = Math.hypot(var6, var8);
      if (var10 < 1.0E-4) {
         return var5;
      }

      double var12 = var6 / var10;
      double var14 = var8 / var10;
      double var16 = var4 >= 0 ? 1.0 : -1.0;
      double var18 = Math.clamp((var10 - var2) * (Math.PI / 3), -Math.PI / 2, Math.PI / 2);
      double var20 = -var14 * var16 * Math.cos(var18) - var12 * Math.sin(var18);
      double var22 = var12 * var16 * Math.cos(var18) - var14 * Math.sin(var18);
      return Mth.wrapDegrees(Math.toDegrees(Math.atan2(var22, var20)) - 90.0);
   }

   public static Vec3 E(Vec3 var0, Vec3 var1, double var2, double var4, int var6, float var7) {
      if (Double.isFinite(var2)
         && Double.isFinite(var4)
         && !(var2 <= 0.0)
         && !(var4 <= 0.0)
         && Double.isFinite(var0.x)
         && Double.isFinite(var0.z)
         && Double.isFinite(var1.x)
         && Double.isFinite(var1.z)
         && Float.isFinite(var7)) {
         double var8 = var0.x - var1.x;
         double var10 = var0.z - var1.z;
         double var12 = var8 * var8 + var10 * var10 < 1.0E-8 ? Math.toRadians(var7) : Math.atan2(var10, var8);
         var12 += (var6 >= 0 ? 1 : -1) * 2 * Math.asin(Math.min(1.0, var4 / (2.0 * var2)));
         Vec3 var14 = new Vec3(var1.x + Math.cos(var12) * var2 - var0.x, 0.0, var1.z + Math.sin(var12) * var2 - var0.z);
         double var15 = var14.length();
         return var15 > var4 ? var14.scale(var4 / var15) : var14;
      } else {
         return Vec3.ZERO;
      }
   }

   public static double T(Vec3 var0, AABB var1, double var2, double var4) {
      double var6 = Math.max(var1.minY - var0.y, Math.max(0.0, var0.y - var1.maxY));
      double var8 = Math.max(0.0, var4 - 0.25);
      double var10 = var8 * var8 - var6 * var6;
      return Double.isFinite(var10) && !(var10 <= 0.0)
         ? Math.max(0.0, Math.min(var2, Math.sqrt(var10) + Math.min(var1.getXsize(), var1.getZsize()) / 2.0))
         : 0.0;
   }

   public static Vec2 m(Vec3 var0, float var1) {
      if (var0.horizontalDistanceSqr() < 1.0E-8) {
         return Vec2.ZERO;
      }

      Vec3 var2 = var0.normalize();
      double var3 = Math.toRadians(var1);
      double var5 = Math.cos(var3);
      double var7 = Math.sin(var3);
      return new Vec2((float)(var5 * var2.x + var7 * var2.z), (float)(-var7 * var2.x + var5 * var2.z));
   }
}
