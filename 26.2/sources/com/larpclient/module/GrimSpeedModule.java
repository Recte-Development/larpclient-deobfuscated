package com.larpclient.module;

import com.larpclient.af;
import com.larpclient.c5;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class GrimSpeedModule extends Module {
   private static GrimSpeedModule V;
   private final e h = this.addSetting(new e("Collision Boost", "Horizontal impulse per colliding entity, up to 0.08 total", 0.02, 0.01, 0.06, 0.01) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.2f", this.getValue());
      }
   });
   private final e s = this.addSetting(new e("Collision Limit", "Maximum contributing entities", 1.0, 1.0, 3.0, 1.0));
   private final s e = this.addSetting(new s("Sprint Jump", "Sprint and jump normally while moving forward, including in open space", true));
   private int B;
   private String Q = "Waiting for collision";
   private LocalPlayer E;
   private ClientLevel z;
   private int l = -1;
   private AABB N;
   private boolean a;

   public GrimSpeedModule() {
      super("Grim Speed", "Native sprint jumps with an experimental entity collision boost", Category.MOVEMENT, -1);
      V = this;
   }

   public static double Z(double var0, int var2) {
      return !Double.isFinite(var0) ? 0.0 : Math.min(0.08, Math.max(0.0, var0) * Math.max(0, var2));
   }

   public static AABB F(AABB var0, AABB var1) {
      return var0.minmax(var1).inflate(0.19);
   }

   public static Vec3 N(float var0, float var1, float var2) {
      if (Float.isFinite(var0) && Float.isFinite(var1) && Float.isFinite(var2)) {
         double var3 = Math.toRadians(var2);
         return new Vec3(Math.cos(var3) * var0 - Math.sin(var3) * var1, 0.0, Math.cos(var3) * var1 + Math.sin(var3) * var0).normalize();
      } else {
         return Vec3.ZERO;
      }
   }

   public static boolean X(Entity var0) {
      return (var0 instanceof LivingEntity || var0 instanceof AbstractBoat || var0 instanceof AbstractMinecart)
         && !(var0 instanceof ArmorStand)
         && !(var0 instanceof Bat)
         && !(var0 instanceof Parrot)
         && !var0.noPhysics
         && var0.isAlive()
         && !var0.isSpectator()
         && !var0.isPassenger()
         && var0.isPushable();
   }

   public static void a(LocalPlayer var0) {
      if (V != null && V.isRuntimeActive() && var0 == getMc().player) {
         if (V.B == 0) {
            com.larpclient.h.U("GrimSpeed", "Position correction: suspending sprint assistance and collision boost for 20 ticks");
         }

         V.B = 20;
         V.x();
         V.Q = "Recovering after correction";
      }
   }

   @Override
   public void onEnable() {
      this.onDisable();
      com.larpclient.h.U(
         "GrimSpeed",
         "Enabled: sprint jump={}, collision boost={}, limit={}, total collision cap=0.08",
         this.e.getValue(),
         this.h.getValue(),
         this.s.getValue().intValue()
      );
   }

   @Override
   public void onDisable() {
      this.B = 0;
      this.a = (boolean)0;
      this.x();
      this.Q = "Waiting for collision";
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.U);
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() ? this.Q : "Inactive";
   }

   @Override
   public void onTick() {
      this.a = (boolean)0;
      this.x();
      boolean var2 = false;
      int var1 = this.B;
      if ((-Integer.signum(var1) >>> 31 ^ 1) == 0) {
         byte var4 = 1;
         int var3 = this.B;
         int var10 = this.B = var3 - var4;
         boolean var6 = false;
         int var5 = var10;
         var10 = Integer.signum(var5);
         if ((var10 | -var10) >>> 31 == 0) {
            com.larpclient.h.U("GrimSpeed", "Correction recovery complete; checking contact next tick");
         }

         this.Q = "Recovering after correction";
      } else {
         int var10000 = this.r();
         boolean var8 = false;
         byte var7 = (byte)var10000;
         var10000 = Integer.signum(var7);
         if ((var10000 | -var10000) >>> 31 != 0) {
            this.Q = "Waiting for collision";
            this.E = mc.player;
            this.z = mc.level;
            this.l = this.E.tickCount;
            this.N = this.E.getBoundingBox();
         }
      }
   }

   private void x() {
      this.E = null;
      this.z = null;
      this.l = -1;
      this.N = null;
   }

   private boolean r() {
      if (!this.isInGame() || !c5.J()) {
         this.Q = "Movement unavailable";
         return false;
      } else if (TargetStrafeModule.l()) {
         this.Q = "Pause: Target Strafe is orbiting";
         return false;
      } else if (c5.t("Speed")) {
         this.Q = "Pause: Speed is enabled";
         return false;
      } else {
         FlyModule var1 = FlyModule.S();
         if (var1 != null && var1.f()) {
            this.Q = "Pause: Fly is enabled";
            return false;
         } else {
            AirStuckModule var2 = AirStuckModule.T();
            if (var2 != null && var2.I()) {
               this.Q = "Pause: AirStuck is enabled";
               return false;
            } else {
               ElytraMotionModule var3 = ElytraMotionModule.v();
               if (var3 != null && var3.K()) {
                  this.Q = "Pause: ElytraMotion is holding";
                  return false;
               } else {
                  LocalPlayer var4 = mc.player;
                  if (mc.gui.screen() != null
                     || var4.isInWater()
                     || var4.isInLava()
                     || var4.onClimbable()
                     || var4.isPassenger()
                     || var4.isSpectator()
                     || var4.getAbilities().flying
                     || var4.isFallFlying()
                     || var4.isCrouching()
                     || var4.isShiftKeyDown()
                     || var4.isSleeping()
                     || var4.isMobilityRestricted()
                     || var4.noPhysics
                     || var4.hurtTime > 0) {
                     this.Q = "Waiting for normal movement";
                     return false;
                  } else if (var4.isUsingItem()) {
                     this.Q = "Pause: using item";
                     return false;
                  } else {
                     return true;
                  }
               }
            }
         }
      }
   }

   public static Input M(LocalPlayer var0, Input var1) {
      if (V != null && V.isEnabled()) {
         GrimSpeedModule var2 = V;
         if (var0 != null
            && var2 != null
            && var2.isRuntimeActive()
            && var2.e.getValue()
            && var2.E == var0
            && var0 == getMc().player
            && var2.z == mc.level
            && var2.l == var0.tickCount) {
            try {
               if (!var2.r() || !var1.forward() || var1.backward() || var1.shift() || var0.getFoodData().getFoodLevel() <= 6 || af.K() != null) {
                  return var1;
               }

               if (AuraModule.G()) {
                  return var1;
               }

               int var3 = var0.isSprinting()
                     && var0.onGround()
                     && !var0.horizontalCollision
                     && !var0.getAbilities().mayfly
                     && var0.getDeltaMovement().y <= 0.0
                     && mc.level.noCollision(var0, var0.getBoundingBox().expandTowards(0.0, 1.3, 0.0))
                  ? 1
                  : 0;
               var2.a = (boolean)1;
               return new Input(var1.forward(), var1.backward(), var1.left(), var1.right(), var1.jump() || var3 != 0, var1.shift(), true);
            } catch (RuntimeException var4) {
               com.larpclient.h.m("GrimSpeed", "Input assistance failed; disabling and keeping native input", var4);
               var2.disable();
               return var1;
            }
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public static void s(LocalPlayer var0) {
      if (V != null && V.isEnabled()) {
         GrimSpeedModule var1 = V;
         if (var0 != null && var1 != null && var1.isRuntimeActive() && var1.E == var0) {
            try {
               var1.E(var0);
            } catch (RuntimeException var3) {
               com.larpclient.h.m("GrimSpeed", "Collision phase failed; disabling and clearing pending boost", var3);
               var1.disable();
            }
         }
      }
   }

   private void E(LocalPlayer var1) {
      AABB var2 = this.N;
      int var3 = var1 == mc.player && this.z == mc.level && this.l == var1.tickCount ? 1 : 0;
      this.x();
      if (var3 == 0 || var2 == null) {
         this.Q = "Movement unavailable";
      } else if (this.r()) {
         Vec3 var4 = N(var1.xxa, var1.zza, var1.getYRot());
         if (var4.lengthSqr() == 0.0) {
            this.Q = "Waiting for movement input";
         } else {
            int var5 = 0;

            for (Entity var7 : mc.level.getEntities(var1, F(var2, var1.getBoundingBox()), GrimSpeedModule::X)) {
               if (EntitySelector.pushableBy(var7).test(var1)) {
                  if (++var5 >= this.s.getValue().intValue()) {
                     break;
                  }
               }
            }

            if (var5 == 0) {
               this.Q = this.a ? "Sprint assist" : "Waiting for collision";
            } else {
               Vec3 var9 = var1.getDeltaMovement();
               double var10 = Z(this.h.getValue(), var5);
               var1.setDeltaMovement(var9.x + var4.x * var10, var9.y, var9.z + var4.z * var10);
               this.Q = String.format(Locale.ROOT, "Boosting: %d %s (+%.2f)", var5, var5 == 1 ? "entity" : "entities", var10);
            }
         }
      }
   }
}
