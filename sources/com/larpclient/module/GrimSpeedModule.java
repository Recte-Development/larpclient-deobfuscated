package com.larpclient.module;

import com.larpclient.mt;
import com.larpclient.te;
import com.larpclient.wq;
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
   private static GrimSpeedModule Y;
   private final d9 j = this.addSetting(new d9("Collision Boost", "Horizontal impulse per colliding entity, up to 0.08 total", 0.02, 0.01, 0.06, 0.01) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.2f", this.P());
      }
   });
   private final d9 P = this.addSetting(new d9("Collision Limit", "Maximum contributing entities", 1.0, 1.0, 3.0, 1.0));
   private final dh f = this.addSetting(new dh("Sprint Jump", "Sprint and jump normally while moving forward, including in open space", true));
   private int G;
   private String d = "Waiting for collision";
   private LocalPlayer B;
   private ClientLevel q;
   private int T = -1;
   private AABB O;
   private boolean H;

   public GrimSpeedModule() {
      super("Grim Speed", "Native sprint jumps with an experimental entity collision boost", Category.MOVEMENT, -1);
      Y = this;
   }

   public static double e(double var0, int var2) {
      return !Double.isFinite(var0) ? 0.0 : Math.min(0.08, Math.max(0.0, var0) * Math.max(0, var2));
   }

   public static AABB P(AABB var0, AABB var1) {
      return var0.minmax(var1).inflate(0.19);
   }

   public static Vec3 t(float var0, float var1, float var2) {
      if (Float.isFinite(var0) && Float.isFinite(var1) && Float.isFinite(var2)) {
         double var3 = Math.toRadians(var2);
         return new Vec3(Math.cos(var3) * var0 - Math.sin(var3) * var1, 0.0, Math.cos(var3) * var1 + Math.sin(var3) * var0).normalize();
      } else {
         return Vec3.ZERO;
      }
   }

   public static boolean u(Entity var0) {
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

   public static void K(LocalPlayer var0) {
      if (Y != null && Y.isRuntimeActive() && var0 == getMc().player) {
         if (Y.G == 0) {
            mt.U("GrimSpeed", "Position correction: suspending sprint assistance and collision boost for 20 ticks");
         }

         Y.G = 20;
         Y.L();
         Y.d = "Recovering after correction";
      }
   }

   @Override
   public void onEnable() {
      this.onDisable();
      mt.U("GrimSpeed", "Enabled: sprint jump={}, collision boost={}, limit={}, total collision cap=0.08", this.f.P(), this.j.P(), this.P.P().intValue());
   }

   @Override
   public void onDisable() {
      this.G = 0;
      this.H = (boolean)0;
      this.L();
      this.d = "Waiting for collision";
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.j);
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() ? this.d : "Inactive";
   }

   @Override
   public void onTick() {
      this.H = (boolean)0;
      this.L();
      boolean var2 = false;
      int var1 = this.G;
      if ((-Integer.signum(var1) >>> 31 ^ 1) == 0) {
         byte var4 = 1;
         int var3 = this.G;
         int var10 = this.G = var3 - var4;
         boolean var6 = false;
         int var5 = var10;
         var10 = Integer.signum(var5);
         if ((var10 | -var10) >>> 31 == 0) {
            mt.U("GrimSpeed", "Correction recovery complete; checking contact next tick");
         }

         this.d = "Recovering after correction";
      } else {
         int var10000 = this.y();
         boolean var8 = false;
         byte var7 = (byte)var10000;
         var10000 = Integer.signum(var7);
         if ((var10000 | -var10000) >>> 31 != 0) {
            this.d = "Waiting for collision";
            this.B = mc.player;
            this.q = mc.level;
            this.T = this.B.tickCount;
            this.O = this.B.getBoundingBox();
         }
      }
   }

   private void L() {
      this.B = null;
      this.q = null;
      this.T = -1;
      this.O = null;
   }

   private boolean y() {
      if (!this.isInGame() || !te.i()) {
         this.d = "Movement unavailable";
         return false;
      } else if (TargetStrafeModule.h()) {
         this.d = "Pause: Target Strafe is orbiting";
         return false;
      } else if (te.i("Speed")) {
         this.d = "Pause: Speed is enabled";
         return false;
      } else {
         FlyModule var1 = FlyModule.d();
         if (var1 != null && var1.s()) {
            this.d = "Pause: Fly is enabled";
            return false;
         } else {
            AirStuckModule var2 = AirStuckModule.X();
            if (var2 != null && var2.U()) {
               this.d = "Pause: AirStuck is enabled";
               return false;
            } else {
               ElytraMotionModule var3 = ElytraMotionModule.d();
               if (var3 != null && var3.z()) {
                  this.d = "Pause: ElytraMotion is holding";
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
                     this.d = "Waiting for normal movement";
                     return false;
                  } else if (var4.isUsingItem()) {
                     this.d = "Pause: using item";
                     return false;
                  } else {
                     return true;
                  }
               }
            }
         }
      }
   }

   public static Input p(LocalPlayer var0, Input var1) {
      if (Y != null && Y.isEnabled()) {
         GrimSpeedModule var2 = Y;
         if (var0 != null
            && var2 != null
            && var2.isRuntimeActive()
            && var2.f.P()
            && var2.B == var0
            && var0 == getMc().player
            && var2.q == mc.level
            && var2.T == var0.tickCount) {
            try {
               if (!var2.y() || !var1.forward() || var1.backward() || var1.shift() || var0.getFoodData().getFoodLevel() <= 6 || wq.g() != null) {
                  return var1;
               }

               if (AuraModule.z()) {
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
               var2.H = (boolean)1;
               return new Input(var1.forward(), var1.backward(), var1.left(), var1.right(), var1.jump() || var3 != 0, var1.shift(), true);
            } catch (RuntimeException var4) {
               mt.P("GrimSpeed", "Input assistance failed; disabling and keeping native input", var4);
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

   public static void v(LocalPlayer var0) {
      if (Y != null && Y.isEnabled()) {
         GrimSpeedModule var1 = Y;
         if (var0 != null && var1 != null && var1.isRuntimeActive() && var1.B == var0) {
            try {
               var1.M(var0);
            } catch (RuntimeException var3) {
               mt.P("GrimSpeed", "Collision phase failed; disabling and clearing pending boost", var3);
               var1.disable();
            }
         }
      }
   }

   private void M(LocalPlayer var1) {
      AABB var2 = this.O;
      int var3 = var1 == mc.player && this.q == mc.level && this.T == var1.tickCount ? 1 : 0;
      this.L();
      if (var3 == 0 || var2 == null) {
         this.d = "Movement unavailable";
      } else if (this.y()) {
         Vec3 var4 = t(var1.xxa, var1.zza, var1.getYRot());
         if (var4.lengthSqr() == 0.0) {
            this.d = "Waiting for movement input";
         } else {
            int var5 = 0;

            for (Entity var7 : mc.level.getEntities(var1, P(var2, var1.getBoundingBox()), GrimSpeedModule::u)) {
               if (EntitySelector.pushableBy(var7).test(var1)) {
                  if (++var5 >= this.P.P().intValue()) {
                     break;
                  }
               }
            }

            if (var5 == 0) {
               this.d = this.H ? "Sprint assist" : "Waiting for collision";
            } else {
               Vec3 var9 = var1.getDeltaMovement();
               double var10 = e(this.j.P(), var5);
               var1.setDeltaMovement(var9.x + var4.x * var10, var9.y, var9.z + var4.z * var10);
               this.d = String.format(Locale.ROOT, "Boosting: %d %s (+%.2f)", var5, var5 == 1 ? "entity" : "entities", var10);
            }
         }
      }
   }
}
