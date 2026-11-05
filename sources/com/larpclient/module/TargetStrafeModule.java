package com.larpclient.module;

import com.larpclient.r4;
import com.larpclient.rr;
import com.larpclient.yu;
import java.util.Set;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public final class TargetStrafeModule extends Module {
   private static TargetStrafeModule m;
   private final lc T = this.addSetting(new lc("Speed", "Horizontal orbit speed in blocks per tick", 1.0, 0.1, 1.2, 0.1));
   private final lc B = this.addSetting(new lc("Radius", "Distance from the target center, capped inside actual attack reach", 1.0, 1.0, 3.0, 0.1));
   private final lp J = this.addSetting(
      new lp("Direction", "Starting orbit direction; reverse when the path is blocked", "Clockwise", "Clockwise", "Counterclockwise")
   );
   private final ln b = this.addSetting(new ln("Auto Jump", "Jump while orbiting when grounded with clear headroom", false));
   private final ln M = this.addSetting(new ln("Avoid Edges", "Require ground below the orbit path; sneak pauses automatic movement", true));
   private LocalPlayer A;
   private LocalPlayer j;
   private ClientLevel r;
   private LivingEntity o;
   private long F;
   private boolean c;
   private int K = 1;
   private int W;
   private long R;
   private String i = "";
   private String x = "Waiting for Kill Aura";
   private Input z;
   private Input p;
   private Vec2 I;
   private Vec2 L;

   public TargetStrafeModule() {
      super("Target Strafe", "Automatically circle Kill Aura's target at an adjustable speed", Category.MOVEMENT, -1);
      m = this;
   }

   @Override
   public Module.b getTickPhase() {
      return Module.b.w;
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.a, ld.c);
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() ? this.x : "Inactive";
   }

   @Override
   public void onEnable() {
      this.O();
      this.W = 0;
      this.p("Waiting for Kill Aura");
      r4.g(
         "TargetStrafe",
         "Enabled: speed={} blocks/tick, radius={}, auto jump={}, edge checks={}",
         this.T.getValue(),
         this.B.getValue(),
         this.b.getValue(),
         this.M.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.O();
      this.W = 0;
      this.p("Inactive");
   }

   private void O() {
      this.d();
      this.A = null;
      this.r = null;
      this.o = null;
      this.c = (boolean)0;
      this.i = "";
   }

   private void p(String var1) {
      if (!this.x.equals(var1)) {
         this.v(var1);
      }

      this.x = var1;
   }

   private void v(String var1) {
      long var2 = System.nanoTime();
      if (var2 - this.R >= 1000000000L) {
         r4.g("TargetStrafe", "{}", var1);
         this.R = var2;
      }
   }

   @Override
   public void onTick() {
      this.d();
      this.c = (boolean)0;
      boolean var3 = false;
      int var2 = this.W;
      if ((-Integer.signum(var2) >>> 31 ^ 1) == 0) {
         byte var5 = 1;
         int var4 = this.W;
         this.W = var4 - var5;
         this.O();
         this.p("Recovering after position correction");
      } else {
         LivingEntity var1 = this.n();
         if (var1 == null) {
            this.O();
         } else {
            label28: {
               if (var1 == this.o) {
                  int var10000 = this.J.getValue().equals(this.i);
                  boolean var7 = false;
                  byte var6 = (byte)var10000;
                  var10000 = Integer.signum(var6);
                  if ((var10000 | -var10000) >>> 31 != 0) {
                     break label28;
                  }
               }

               int var10001 = this.J.i("Clockwise");
               boolean var9 = false;
               byte var8 = (byte)var10001;
               var10001 = Integer.signum(var8);
               this.K = ((var10001 | -var10001) >>> 31 ^ 1) == 0 ? 1 : -1;
               this.i = this.J.getValue();
               this.v("Orbit target acquired: entity=" + var1.getId());
            }

            this.A = mc.player;
            this.r = mc.level;
            this.F = mc.level.getGameTime();
            this.o = var1;
         }
      }
   }

   private LivingEntity n() {
      if (this.isInGame() && yu.r() && mc.isWindowActive() && mc.gui.screen() == null && !mc.isPaused()) {
         LocalPlayer var1 = mc.player;
         FlyModule var2 = FlyModule.s();
         AirStuckModule var3 = AirStuckModule.H();
         ElytraMotionModule var4 = ElytraMotionModule.S();
         if ((var2 == null || !var2.E())
            && (var3 == null || !var3.D())
            && (var4 == null || !var4.m())
            && com.larpclient.k.F() == null
            && rr.u() != rr.f.e
            && rr.u() != rr.f.L
            && rr.u() != rr.f.q) {
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
               this.p("Paused for player movement or item use");
               return null;
            } else if (var1.hurtTime > 0) {
               this.p("Paused for knockback");
               return null;
            } else {
               AuraModule var5 = AuraModule.G();
               LivingEntity var6 = var5 == null ? null : var5.G();
               if (var6 == null) {
                  this.p("Waiting for Kill Aura target");
                  return null;
               } else if (y(var1.getEyePosition(), var6.getBoundingBox(), this.B.getValue(), var5.X()) <= 0.0) {
                  this.p("Target is outside vertical attack reach");
                  return null;
               } else {
                  return var6;
               }
            }
         } else {
            this.p("Paused for another movement controller");
            return null;
         }
      } else {
         this.p("Waiting for gameplay");
         return null;
      }
   }

   private boolean r(LocalPlayer var1) {
      return this.isRuntimeActive()
         && this.W == 0
         && var1 != null
         && var1 == this.A
         && var1 == getMc().player
         && this.r == mc.level
         && this.F == mc.level.getGameTime()
         && this.o != null
         && this.n() == this.o;
   }

   public static boolean Y() {
      if (m != null && m.isEnabled()) {
         TargetStrafeModule var0 = m;
         if (var0 == null) {
            return false;
         }

         try {
            return var0.r(getMc().player);
         } catch (RuntimeException var2) {
            var0.U("movement ownership", var2);
            return false;
         }
      } else {
         return false;
      }
   }

   public static Input f(LocalPlayer var0, Input var1) {
      if (m != null && m.isEnabled()) {
         TargetStrafeModule var2 = m;
         if (var2 == null) {
            return var1;
         }

         try {
            if (!var2.r(var0)) {
               return var1;
            } else if (var1.shift()) {
               var2.O();
               var2.p("Paused for sneak input");
               return var1;
            } else {
               Vec3 var3 = var2.X();
               Vec2 var4 = f(var3, var0.getYRot());
               int var5 = var3.lengthSqr() > 0.0
                     && var2.b.getValue()
                     && var0.onGround()
                     && var0.getDeltaMovement().y <= 0.0
                     && !var0.getAbilities().mayfly
                     && mc.level.noCollision(var0, var0.getBoundingBox().expandTowards(0.0, 1.3, 0.0))
                  ? 1
                  : 0;
               var2.j = var0;
               var2.z = var1;
               var2.I = var0.input.moveVector;
               var2.L = var4;
               var0.input.moveVector = var4;
               var2.p = new Input(var4.y > 0.1F, var4.y < -0.1F, var4.x > 0.1F, var4.x < -0.1F, var1.jump() || var5 != 0, var1.shift(), var1.sprint());
               return var2.p;
            }
         } catch (RuntimeException var6) {
            var2.U("input", var6);
            return var1;
         }
      } else {
         return var1;
      }
   }

   public static boolean D() {
      if (m != null && m.isEnabled()) {
         TargetStrafeModule var0 = m;
         return var0 != null
            && var0.j != null
            && var0.j == getMc().player
            && var0.r == mc.level
            && var0.F == mc.level.getGameTime()
            && var0.j.input.moveVector == var0.L;
      } else {
         return false;
      }
   }

   private void d() {
      if (this.j != null && this.j == getMc().player && this.r == mc.level && this.F == mc.level.getGameTime()) {
         if (this.j.input.keyPresses == this.p) {
            this.j.input.keyPresses = this.z;
         }

         if (this.j.input.moveVector == this.L) {
            this.j.input.moveVector = this.I;
         }
      }

      this.j = null;
      this.z = this.p = null;
      this.I = this.L = null;
   }

   public static Vec3 Q(LocalPlayer var0, Vec3 var1) {
      if (m != null && m.isEnabled()) {
         TargetStrafeModule var2 = m;
         if (var2 != null && !var2.c) {
            try {
               if (!var2.r(var0)) {
                  var2.d();
                  return var1;
               } else {
                  var2.c = (boolean)1;
                  Vec3 var3 = var2.X();
                  Vec3 var4 = new Vec3(var3.x, var1.y, var3.z);
                  var0.setDeltaMovement(var4);
                  return var4;
               }
            } catch (RuntimeException var5) {
               var2.U("movement", var5);
               return var1;
            }
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private void U(String var1, RuntimeException var2) {
      r4.r("TargetStrafe", "Failed during " + var1 + "; disabling and releasing orbit input", var2);
      this.disable();
   }

   public static void H(LocalPlayer var0) {
      TargetStrafeModule var1 = m;
      if (var1 != null && var1.isRuntimeActive() && var0 == getMc().player) {
         var1.O();
         var1.W = 20;
         var1.p("Recovering after position correction");
      }
   }

   private Vec3 X() {
      LocalPlayer var1 = mc.player;
      double var2 = y(var1.getEyePosition(), this.o.getBoundingBox(), this.B.getValue(), AuraModule.G().X());
      if (var2 <= 0.0) {
         return Vec3.ZERO;
      } else {
         Vec3 var4 = this.o.getBoundingBox().getCenter();
         Vec3 var5 = h(var1.position(), var4, var2, this.T.getValue(), this.K, var1.getYRot());
         if (this.D(var5)) {
            this.p("Orbiting");
            return var5;
         } else {
            Vec3 var6 = h(var1.position(), var4, var2, this.T.getValue(), -this.K, var1.getYRot());
            if (this.D(var6)) {
               this.K = -this.K;
               this.v("Reversed orbit at blocked path or edge");
               this.p("Orbiting");
               return var6;
            } else {
               this.p("Orbit blocked by terrain or edge");
               return Vec3.ZERO;
            }
         }
      }
   }

   private boolean D(Vec3 var1) {
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

         if (this.M.getValue() && mc.level.noBlockCollision(var2, new AABB(var8 - 0.1, var3.minY - var5, var10 - 0.1, var8 + 0.1, var3.minY, var10 + 0.1))) {
            return false;
         }
      }

      return true;
   }

   public static Vec3 h(Vec3 var0, Vec3 var1, double var2, double var4, int var6, float var7) {
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

   public static double y(Vec3 var0, AABB var1, double var2, double var4) {
      double var6 = Math.max(var1.minY - var0.y, Math.max(0.0, var0.y - var1.maxY));
      double var8 = Math.max(0.0, var4 - 0.25);
      double var10 = var8 * var8 - var6 * var6;
      return Double.isFinite(var10) && !(var10 <= 0.0)
         ? Math.max(0.0, Math.min(var2, Math.sqrt(var10) + Math.min(var1.getXsize(), var1.getZsize()) / 2.0))
         : 0.0;
   }

   public static Vec2 f(Vec3 var0, float var1) {
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
