package com.larpclient.module;

import com.larpclient.mt;
import com.larpclient.te;
import com.larpclient.tq;
import com.larpclient.wq;
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
   private static TargetStrafeModule V;
   private final d9 R = this.addSetting(new d9("Speed", "Horizontal orbit speed in blocks per tick", 1.0, 0.1, 1.2, 0.1));
   private final d9 b = this.addSetting(new d9("Radius", "Distance from the target center, capped inside actual attack reach", 1.0, 1.0, 3.0, 0.1));
   private final d6 E = this.addSetting(
      new d6("Direction", "Starting orbit direction; reverse when the path is blocked", "Clockwise", "Clockwise", "Counterclockwise")
   );
   private final dh k = this.addSetting(new dh("Auto Jump", "Jump while orbiting when grounded with clear headroom", false));
   private final dh d = this.addSetting(new dh("Avoid Edges", "Require ground below the orbit path; sneak pauses automatic movement", true));
   private LocalPlayer p;
   private LocalPlayer o;
   private ClientLevel h;
   private LivingEntity u;
   private long K;
   private boolean T;
   private int x = 1;
   private int W;
   private long U;
   private String s = "";
   private String M = "Waiting for Kill Aura";
   private Input r;
   private Input J;
   private Vec2 v;
   private Vec2 f;

   public TargetStrafeModule() {
      super("Target Strafe", "Automatically circle Kill Aura's target at an adjustable speed", Category.MOVEMENT, -1);
      V = this;
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.Z;
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.z, dm.j);
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() ? this.M : "Inactive";
   }

   @Override
   public void onEnable() {
      this.v();
      this.W = 0;
      this.N("Waiting for Kill Aura");
      mt.U("TargetStrafe", "Enabled: speed={} blocks/tick, radius={}, auto jump={}, edge checks={}", this.R.P(), this.b.P(), this.k.P(), this.d.P());
   }

   @Override
   public void onDisable() {
      this.v();
      this.W = 0;
      this.N("Inactive");
   }

   private void v() {
      this.M();
      this.p = null;
      this.h = null;
      this.u = null;
      this.T = (boolean)0;
      this.s = "";
   }

   private void N(String var1) {
      if (!this.M.equals(var1)) {
         this.M(var1);
      }

      this.M = var1;
   }

   private void M(String var1) {
      long var2 = System.nanoTime();
      if (var2 - this.U >= 1000000000L) {
         mt.U("TargetStrafe", "{}", var1);
         this.U = var2;
      }
   }

   @Override
   public void onTick() {
      this.M();
      this.T = (boolean)0;
      boolean var3 = false;
      int var2 = this.W;
      if ((-Integer.signum(var2) >>> 31 ^ 1) == 0) {
         byte var5 = 1;
         int var4 = this.W;
         this.W = var4 - var5;
         this.v();
         this.N("Recovering after position correction");
      } else {
         LivingEntity var1 = this.G();
         if (var1 == null) {
            this.v();
         } else {
            label28: {
               if (var1 == this.u) {
                  int var10000 = this.E.P().equals(this.s);
                  boolean var7 = false;
                  byte var6 = (byte)var10000;
                  var10000 = Integer.signum(var6);
                  if ((var10000 | -var10000) >>> 31 != 0) {
                     break label28;
                  }
               }

               int var10001 = this.E.S("Clockwise");
               boolean var9 = false;
               byte var8 = (byte)var10001;
               var10001 = Integer.signum(var8);
               this.x = ((var10001 | -var10001) >>> 31 ^ 1) == 0 ? 1 : -1;
               this.s = this.E.P();
               this.M("Orbit target acquired: entity=" + var1.getId());
            }

            this.p = mc.player;
            this.h = mc.level;
            this.K = mc.level.getGameTime();
            this.u = var1;
         }
      }
   }

   private LivingEntity G() {
      if (this.isInGame() && te.i() && mc.isWindowActive() && mc.gui.screen() == null && !mc.isPaused()) {
         LocalPlayer var1 = mc.player;
         FlyModule var2 = FlyModule.d();
         AirStuckModule var3 = AirStuckModule.X();
         ElytraMotionModule var4 = ElytraMotionModule.d();
         if ((var2 == null || !var2.s())
            && (var3 == null || !var3.U())
            && (var4 == null || !var4.z())
            && wq.g() == null
            && tq.l() != tq.y.J
            && tq.l() != tq.y.a
            && tq.l() != tq.y.g) {
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
               this.N("Paused for player movement or item use");
               return null;
            } else if (var1.hurtTime > 0) {
               this.N("Paused for knockback");
               return null;
            } else {
               AuraModule var5 = AuraModule.S();
               LivingEntity var6 = var5 == null ? null : var5.l();
               if (var6 == null) {
                  this.N("Waiting for Kill Aura target");
                  return null;
               } else if (q(var1.getEyePosition(), var6.getBoundingBox(), this.b.P(), var5.l()) <= 0.0) {
                  this.N("Target is outside vertical attack reach");
                  return null;
               } else {
                  return var6;
               }
            }
         } else {
            this.N("Paused for another movement controller");
            return null;
         }
      } else {
         this.N("Waiting for gameplay");
         return null;
      }
   }

   private boolean V(LocalPlayer var1) {
      return this.isRuntimeActive()
         && this.W == 0
         && var1 != null
         && var1 == this.p
         && var1 == getMc().player
         && this.h == mc.level
         && this.K == mc.level.getGameTime()
         && this.u != null
         && this.G() == this.u;
   }

   public static boolean h() {
      if (V != null && V.isEnabled()) {
         TargetStrafeModule var0 = V;
         if (var0 == null) {
            return false;
         }

         try {
            return var0.V(getMc().player);
         } catch (RuntimeException var2) {
            var0.T("movement ownership", var2);
            return false;
         }
      } else {
         return false;
      }
   }

   public static Input U(LocalPlayer var0, Input var1) {
      if (V != null && V.isEnabled()) {
         TargetStrafeModule var2 = V;
         if (var2 == null) {
            return var1;
         }

         try {
            if (!var2.V(var0)) {
               return var1;
            } else if (var1.shift()) {
               var2.v();
               var2.N("Paused for sneak input");
               return var1;
            } else {
               Vec3 var3 = var2.E();
               Vec2 var4 = o(var3, var0.getYRot());
               int var5 = var3.lengthSqr() > 0.0
                     && var2.k.P()
                     && var0.onGround()
                     && var0.getDeltaMovement().y <= 0.0
                     && !var0.getAbilities().mayfly
                     && mc.level.noCollision(var0, var0.getBoundingBox().expandTowards(0.0, 1.3, 0.0))
                  ? 1
                  : 0;
               var2.o = var0;
               var2.r = var1;
               var2.v = var0.input.moveVector;
               var2.f = var4;
               var0.input.moveVector = var4;
               var2.J = new Input(var4.y > 0.1F, var4.y < -0.1F, var4.x > 0.1F, var4.x < -0.1F, var1.jump() || var5 != 0, var1.shift(), var1.sprint());
               return var2.J;
            }
         } catch (RuntimeException var6) {
            var2.T("input", var6);
            return var1;
         }
      } else {
         return var1;
      }
   }

   public static boolean Z() {
      if (V != null && V.isEnabled()) {
         TargetStrafeModule var0 = V;
         return var0 != null
            && var0.o != null
            && var0.o == getMc().player
            && var0.h == mc.level
            && var0.K == mc.level.getGameTime()
            && var0.o.input.moveVector == var0.f;
      } else {
         return false;
      }
   }

   private void M() {
      if (this.o != null && this.o == getMc().player && this.h == mc.level && this.K == mc.level.getGameTime()) {
         if (this.o.input.keyPresses == this.J) {
            this.o.input.keyPresses = this.r;
         }

         if (this.o.input.moveVector == this.f) {
            this.o.input.moveVector = this.v;
         }
      }

      this.o = null;
      this.r = this.J = null;
      this.v = this.f = null;
   }

   public static Vec3 N(LocalPlayer var0, Vec3 var1) {
      if (V != null && V.isEnabled()) {
         TargetStrafeModule var2 = V;
         if (var2 != null && !var2.T) {
            try {
               if (!var2.V(var0)) {
                  var2.M();
                  return var1;
               } else {
                  var2.T = (boolean)1;
                  Vec3 var3 = var2.E();
                  Vec3 var4 = new Vec3(var3.x, var1.y, var3.z);
                  var0.setDeltaMovement(var4);
                  return var4;
               }
            } catch (RuntimeException var5) {
               var2.T("movement", var5);
               return var1;
            }
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private void T(String var1, RuntimeException var2) {
      mt.P("TargetStrafe", "Failed during " + var1 + "; disabling and releasing orbit input", var2);
      this.disable();
   }

   public static void W(LocalPlayer var0) {
      TargetStrafeModule var1 = V;
      if (var1 != null && var1.isRuntimeActive() && var0 == getMc().player) {
         var1.v();
         var1.W = 20;
         var1.N("Recovering after position correction");
      }
   }

   private Vec3 E() {
      LocalPlayer var1 = mc.player;
      double var2 = q(var1.getEyePosition(), this.u.getBoundingBox(), this.b.P(), AuraModule.S().l());
      if (var2 <= 0.0) {
         return Vec3.ZERO;
      } else {
         Vec3 var4 = this.u.getBoundingBox().getCenter();
         Vec3 var5 = F(var1.position(), var4, var2, this.R.P(), this.x, var1.getYRot());
         if (this.m(var5)) {
            this.N("Orbiting");
            return var5;
         } else {
            Vec3 var6 = F(var1.position(), var4, var2, this.R.P(), -this.x, var1.getYRot());
            if (this.m(var6)) {
               this.x = -this.x;
               this.M("Reversed orbit at blocked path or edge");
               this.N("Orbiting");
               return var6;
            } else {
               this.N("Orbit blocked by terrain or edge");
               return Vec3.ZERO;
            }
         }
      }
   }

   private boolean m(Vec3 var1) {
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

         if (this.d.P() && mc.level.noBlockCollision(var2, new AABB(var8 - 0.1, var3.minY - var5, var10 - 0.1, var8 + 0.1, var3.minY, var10 + 0.1))) {
            return false;
         }
      }

      return true;
   }

   public static Vec3 F(Vec3 var0, Vec3 var1, double var2, double var4, int var6, float var7) {
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

   public static double q(Vec3 var0, AABB var1, double var2, double var4) {
      double var6 = Math.max(var1.minY - var0.y, Math.max(0.0, var0.y - var1.maxY));
      double var8 = Math.max(0.0, var4 - 0.25);
      double var10 = var8 * var8 - var6 * var6;
      return Double.isFinite(var10) && !(var10 <= 0.0)
         ? Math.max(0.0, Math.min(var2, Math.sqrt(var10) + Math.min(var1.getXsize(), var1.getZsize()) / 2.0))
         : 0.0;
   }

   public static Vec2 o(Vec3 var0, float var1) {
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
