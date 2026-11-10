package com.larpclient.module;

import com.larpclient.af;
import com.larpclient.c5;
import com.larpclient.p_;
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
   private static final int Y = 3;
   private static final int R = 10;
   private static final float F = 25.0F;
   private static final double i = Math.PI / 3;
   private static final double B = 0.7;
   private static final double V = 1.0;
   private static final float[] n = new float[]{0.0F, 0.0F};
   private static TargetStrafeModule u;
   private final l z = this.addSetting(
      new l(
         "Mode",
         "Native presses real movement keys at normal speed for anti-cheat servers; Fast overrides your speed and will flag on them",
         "Native",
         "Native",
         "Fast"
      )
   );
   private final e q = this.addSetting(new e("Distance", "Distance to hold from the target center, capped inside actual attack reach", 2.4, 1.5, 3.0, 0.1));
   private final e X = this.addSetting(new e("Speed", "Horizontal orbit speed in blocks per tick", 1.0, 0.1, 1.2, 0.1));
   private final e I = this.addSetting(new e("Radius", "Distance from the target center, capped inside actual attack reach", 1.0, 1.0, 3.0, 0.1));
   private final l a = this.addSetting(
      new l("Direction", "Starting orbit direction; reverse when the path is blocked", "Clockwise", "Clockwise", "Counterclockwise")
   );
   private final s C = this.addSetting(new s("Auto Jump", "Jump while orbiting when grounded with clear headroom", false));
   private final s J = this.addSetting(new s("Avoid Edges", "Require ground below the orbit path; sneak pauses automatic movement", true));
   private LocalPlayer U;
   private LocalPlayer j;
   private ClientLevel S;
   private LivingEntity E;
   private long Q;
   private boolean O;
   private int Z = 1;
   private int v;
   private int G;
   private int T;
   private float[] b;
   private float h;
   private long l;
   private String W = "";
   private String r = "Waiting for Kill Aura";
   private Input M;
   private Input e;
   private Vec2 s;
   private Vec2 f;

   public TargetStrafeModule() {
      super("Target Strafe", "Automatically circle Kill Aura's target", Category.MOVEMENT, -1);
      u = this;
      this.q.P(() -> !this.f());
      this.X.P(this::f);
      this.I.P(this::f);
   }

   private boolean f() {
      return this.z.O("Fast");
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.u;
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.K, m.U);
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() ? this.r : "Inactive";
   }

   @Override
   public void onEnable() {
      this.W();
      this.v = 0;
      this.j("Waiting for Kill Aura");
      com.larpclient.h.U(
         "TargetStrafe",
         "Enabled: mode={}, distance={}, fast speed={} blocks/tick, fast radius={}, auto jump={}, edge checks={}",
         this.z.getValue(),
         this.q.getValue(),
         this.X.getValue(),
         this.I.getValue(),
         this.C.getValue(),
         this.J.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.W();
      this.v = 0;
      this.j("Inactive");
   }

   private void W() {
      this.t();
      this.U = null;
      this.S = null;
      this.E = null;
      this.O = (boolean)0;
      this.W = "";
      this.b = null;
      this.G = this.T = 0;
   }

   private void j(String var1) {
      if (!this.r.equals(var1)) {
         this.V(var1);
      }

      this.r = var1;
   }

   private void V(String var1) {
      long var2 = System.nanoTime();
      if (var2 - this.l >= 1000000000L) {
         com.larpclient.h.U("TargetStrafe", "{}", var1);
         this.l = var2;
      }
   }

   @Override
   public void onTick() {
      this.t();
      this.O = (boolean)0;
      boolean var3 = false;
      int var2 = this.v;
      if ((-Integer.signum(var2) >>> 31 ^ 1) == 0) {
         byte var5 = 1;
         int var4 = this.v;
         this.v = var4 - var5;
         this.W();
         this.j("Recovering after position correction");
      } else {
         LivingEntity var1 = this.f();
         if (var1 == null) {
            this.W();
         } else {
            label28: {
               if (var1 == this.E) {
                  int var10000 = this.a.getValue().equals(this.W);
                  boolean var7 = false;
                  byte var6 = (byte)var10000;
                  var10000 = Integer.signum(var6);
                  if ((var10000 | -var10000) >>> 31 != 0) {
                     break label28;
                  }
               }

               int var10001 = this.a.O("Clockwise");
               boolean var9 = false;
               byte var8 = (byte)var10001;
               var10001 = Integer.signum(var8);
               this.Z = ((var10001 | -var10001) >>> 31 ^ 1) == 0 ? 1 : -1;
               this.W = this.a.getValue();
               this.b = null;
               this.T = 0;
               this.V("Orbit target acquired: entity=" + var1.getId());
            }

            this.U = mc.player;
            this.S = mc.level;
            this.Q = mc.level.getGameTime();
            this.E = var1;
         }
      }
   }

   private LivingEntity f() {
      if (this.isInGame() && c5.J() && mc.isWindowActive() && mc.gui.screen() == null && !mc.isPaused()) {
         LocalPlayer var1 = mc.player;
         FlyModule var2 = FlyModule.S();
         AirStuckModule var3 = AirStuckModule.T();
         ElytraMotionModule var4 = ElytraMotionModule.v();
         if ((var2 == null || !var2.f())
            && (var3 == null || !var3.I())
            && (var4 == null || !var4.K())
            && af.K() == null
            && p_.A() != p_.o.G
            && p_.A() != p_.o.h
            && p_.A() != p_.o.d) {
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
               this.j("Paused for player movement or item use");
               return null;
            } else if (this.f() && var1.hurtTime > 0) {
               this.j("Paused for knockback");
               return null;
            } else {
               AuraModule var5 = AuraModule.V();
               LivingEntity var6 = var5 == null ? null : var5.u();
               if (var6 == null) {
                  this.j("Waiting for Kill Aura target");
                  return null;
               } else if (r(var1.getEyePosition(), var6.getBoundingBox(), this.z(), var5.d()) <= 0.0) {
                  this.j("Target is outside vertical attack reach");
                  return null;
               } else {
                  return var6;
               }
            }
         } else {
            this.j("Paused for another movement controller");
            return null;
         }
      } else {
         this.j("Waiting for gameplay");
         return null;
      }
   }

   private boolean N(LocalPlayer var1) {
      return this.isRuntimeActive()
         && this.v == 0
         && var1 != null
         && var1 == this.U
         && var1 == getMc().player
         && this.S == mc.level
         && this.Q == mc.level.getGameTime()
         && this.E != null
         && this.f() == this.E;
   }

   private double z() {
      return this.f() ? this.I.getValue() : this.q.getValue();
   }

   public static boolean Z() {
      if (u != null && u.isEnabled()) {
         TargetStrafeModule var0 = u;
         if (var0 == null) {
            return false;
         }

         try {
            return var0.N(getMc().player);
         } catch (RuntimeException var2) {
            var0.m("movement ownership", var2);
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean l() {
      if (u != null && u.isEnabled()) {
         TargetStrafeModule var0 = u;
         return var0 != null && var0.f() && Z();
      } else {
         return false;
      }
   }

   public static Input v(LocalPlayer var0, Input var1) {
      if (u != null && u.isEnabled()) {
         TargetStrafeModule var2 = u;
         if (var2 == null) {
            return var1;
         }

         try {
            if (!var2.N(var0)) {
               return var1;
            }

            if (var1.shift()) {
               var2.W();
               var2.j("Paused for sneak input");
               return var1;
            }

            if (!var2.f()) {
               return var2.l(var0, var1);
            }

            Vec3 var3 = var2.c();
            Vec2 var4 = Y(var3, var0.getYRot());
            return var2.b(
               var0,
               var1,
               var4,
               new Input(
                  var4.y > 0.1F,
                  var4.y < -0.1F,
                  var4.x > 0.1F,
                  var4.x < -0.1F,
                  var1.jump() || var2.s(var0, var3.lengthSqr() > 0.0),
                  var1.shift(),
                  var1.sprint()
               )
            );
         } catch (RuntimeException var5) {
            var2.m("input", var5);
            return var1;
         }
      } else {
         return var1;
      }
   }

   private Input l(LocalPlayer var1, Input var2) {
      float[] var3 = this.a();
      int var4 = var3[0] > 0.0F ? 1 : 0;
      int var5 = var3[0] == 0.0F && var3[1] == 0.0F ? 0 : 1;
      return this.b(
         var1,
         var2,
         new Vec2(var3[1], var3[0]).normalized(),
         new Input(
            (boolean)var4,
            var3[0] < 0.0F,
            var3[1] > 0.0F,
            var3[1] < 0.0F,
            var2.jump() || this.s(var1, (boolean)var5),
            var2.shift(),
            var4 != 0 && !var1.horizontalCollision
         )
      );
   }

   private boolean s(LocalPlayer var1, boolean var2) {
      return var2
         && this.C.getValue()
         && var1.onGround()
         && var1.getDeltaMovement().y <= 0.0
         && !var1.getAbilities().mayfly
         && mc.level.noCollision(var1, var1.getBoundingBox().expandTowards(0.0, 1.3, 0.0));
   }

   private Input b(LocalPlayer var1, Input var2, Vec2 var3, Input var4) {
      this.j = var1;
      this.M = var2;
      this.s = var1.input.moveVector;
      this.f = var3;
      this.e = var4;
      var1.input.moveVector = var3;
      return var4;
   }

   public static boolean q() {
      if (u != null && u.isEnabled()) {
         TargetStrafeModule var0 = u;
         return var0 != null
            && var0.j != null
            && var0.j == getMc().player
            && var0.S == mc.level
            && var0.Q == mc.level.getGameTime()
            && var0.j.input.moveVector == var0.f;
      } else {
         return false;
      }
   }

   private void t() {
      if (this.j != null && this.j == getMc().player && this.S == mc.level && this.Q == mc.level.getGameTime()) {
         if (this.j.input.keyPresses == this.e) {
            this.j.input.keyPresses = this.M;
         }

         if (this.j.input.moveVector == this.f) {
            this.j.input.moveVector = this.s;
         }
      }

      this.j = null;
      this.M = this.e = null;
      this.s = this.f = null;
   }

   public static Vec3 u(LocalPlayer var0, Vec3 var1) {
      if (u != null && u.isEnabled()) {
         TargetStrafeModule var2 = u;
         if (var2 != null && !var2.O) {
            try {
               if (!var2.N(var0)) {
                  var2.t();
                  return var1;
               }

               var2.O = (boolean)1;
               if (!var2.f()) {
                  return var1;
               }

               Vec3 var3 = var2.c();
               Vec3 var4 = new Vec3(var3.x, var1.y, var3.z);
               var0.setDeltaMovement(var4);
               return var4;
            } catch (RuntimeException var5) {
               var2.m("movement", var5);
               return var1;
            }
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private void m(String var1, RuntimeException var2) {
      com.larpclient.h.m("TargetStrafe", "Failed during " + var1 + "; disabling and releasing orbit input", var2);
      this.disable();
   }

   public static void X(LocalPlayer var0) {
      TargetStrafeModule var1 = u;
      if (var1 != null && var1.isRuntimeActive() && var0 == getMc().player) {
         if (var1.v == 0) {
            com.larpclient.h.A("TargetStrafe", "Position correction while active: mode=" + var1.z.getValue() + ", status=" + var1.r + "; pausing for 20 ticks");
         }

         var1.W();
         var1.v = 20;
         var1.j("Recovering after position correction");
      }
   }

   private float[] a() {
      LocalPlayer var1 = mc.player;
      float var2 = var1.getYRot();
      if (this.T > 0) {
         this.T--;
      }

      double var3 = r(var1.getEyePosition(), this.E.getBoundingBox(), this.q.getValue(), AuraModule.V().d());
      if (var3 <= 0.0) {
         this.b = null;
         return n;
      }

      if (this.b != null && this.G-- > 0 && Math.abs(Mth.wrapDegrees(var2 - this.h)) <= 25.0F && this.a(this.b, var2)) {
         return this.b;
      }

      Vec3 var5 = this.E.getBoundingBox().getCenter();
      float[] var6 = h(var1.position(), var5, var3, this.Z, var2);
      if (!this.a(var6, var2)) {
         float[] var7 = this.T == 0 ? h(var1.position(), var5, var3, -this.Z, var2) : null;
         if (var7 == null || !this.a(var7, var2)) {
            this.b = null;
            this.j("Orbit blocked by terrain or edge");
            return n;
         }

         this.Z = -this.Z;
         this.T = 10;
         var6 = var7;
         this.V("Reversed orbit at blocked path or edge");
      }

      if (!Arrays.equals(var6, this.b)) {
         this.V(
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

      this.b = var6;
      this.G = 2;
      this.h = var2;
      this.j("Orbiting");
      return var6;
   }

   private boolean a(float[] var1, float var2) {
      Vec3 var3 = GrimSpeedModule.N(var1[1], var1[0], var2);
      return this.C(var3.scale(mc.player.onGround() ? 0.7 : 1.0));
   }

   private Vec3 c() {
      LocalPlayer var1 = mc.player;
      double var2 = r(var1.getEyePosition(), this.E.getBoundingBox(), this.I.getValue(), AuraModule.V().d());
      if (var2 <= 0.0) {
         return Vec3.ZERO;
      } else {
         Vec3 var4 = this.E.getBoundingBox().getCenter();
         Vec3 var5 = T(var1.position(), var4, var2, this.X.getValue(), this.Z, var1.getYRot());
         if (this.C(var5)) {
            this.j("Orbiting");
            return var5;
         } else {
            Vec3 var6 = T(var1.position(), var4, var2, this.X.getValue(), -this.Z, var1.getYRot());
            if (this.C(var6)) {
               this.Z = -this.Z;
               this.V("Reversed orbit at blocked path or edge");
               this.j("Orbiting");
               return var6;
            } else {
               this.j("Orbit blocked by terrain or edge");
               return Vec3.ZERO;
            }
         }
      }
   }

   private boolean C(Vec3 var1) {
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

         if (this.J.getValue() && mc.level.noBlockCollision(var2, new AABB(var8 - 0.1, var3.minY - var5, var10 - 0.1, var8 + 0.1, var3.minY, var10 + 0.1))) {
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
         ? AuraModule.i(t(var0, var1, var2, var4, var5), var5)
         : n;
   }

   public static double t(Vec3 var0, Vec3 var1, double var2, int var4, float var5) {
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

   public static Vec3 T(Vec3 var0, Vec3 var1, double var2, double var4, int var6, float var7) {
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

   public static double r(Vec3 var0, AABB var1, double var2, double var4) {
      double var6 = Math.max(var1.minY - var0.y, Math.max(0.0, var0.y - var1.maxY));
      double var8 = Math.max(0.0, var4 - 0.25);
      double var10 = var8 * var8 - var6 * var6;
      return Double.isFinite(var10) && !(var10 <= 0.0)
         ? Math.max(0.0, Math.min(var2, Math.sqrt(var10) + Math.min(var1.getXsize(), var1.getZsize()) / 2.0))
         : 0.0;
   }

   public static Vec2 Y(Vec3 var0, float var1) {
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
