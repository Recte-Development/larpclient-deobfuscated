package com.larpclient.module;

import com.larpclient.r4;
import com.larpclient.yu;
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
   private static GrimSpeedModule i;
   private final lc c = this.addSetting(new lc("Collision Boost", "Horizontal impulse per colliding entity, up to 0.08 total", 0.02, 0.01, 0.06, 0.01) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.2f", this.getValue());
      }
   });
   private final lc M = this.addSetting(new lc("Collision Limit", "Maximum contributing entities", 1.0, 1.0, 3.0, 1.0));
   private final ln P = this.addSetting(new ln("Sprint Jump", "Sprint and jump normally while moving forward, including in open space", true));
   private int T;
   private String U = "Waiting for collision";
   private LocalPlayer u;
   private ClientLevel t;
   private int o = -1;
   private AABB A;
   private boolean e;

   public GrimSpeedModule() {
      super("Grim Speed", "Native sprint jumps with an experimental entity collision boost", Category.MOVEMENT, -1);
      i = this;
   }

   public static double p(double var0, int var2) {
      return !Double.isFinite(var0) ? 0.0 : Math.min(0.08, Math.max(0.0, var0) * Math.max(0, var2));
   }

   public static AABB t(AABB var0, AABB var1) {
      return var0.minmax(var1).inflate(0.19);
   }

   public static Vec3 F(float var0, float var1, float var2) {
      if (Float.isFinite(var0) && Float.isFinite(var1) && Float.isFinite(var2)) {
         double var3 = Math.toRadians(var2);
         return new Vec3(Math.cos(var3) * var0 - Math.sin(var3) * var1, 0.0, Math.cos(var3) * var1 + Math.sin(var3) * var0).normalize();
      } else {
         return Vec3.ZERO;
      }
   }

   public static boolean v(Entity var0) {
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

   public static void u(LocalPlayer var0) {
      if (i != null && i.isRuntimeActive() && var0 == getMc().player) {
         if (i.T == 0) {
            r4.g("GrimSpeed", "Position correction: suspending sprint assistance and collision boost for 20 ticks");
         }

         i.T = 20;
         i.o();
         i.U = "Recovering after correction";
      }
   }

   @Override
   public void onEnable() {
      this.onDisable();
      r4.g(
         "GrimSpeed",
         "Enabled: sprint jump={}, collision boost={}, limit={}, total collision cap=0.08",
         this.P.getValue(),
         this.c.getValue(),
         this.M.getValue().intValue()
      );
   }

   @Override
   public void onDisable() {
      this.T = 0;
      this.e = (boolean)0;
      this.o();
      this.U = "Waiting for collision";
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.c);
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() ? this.U : "Inactive";
   }

   @Override
   public void onTick() {
      this.e = (boolean)0;
      this.o();
      boolean var2 = false;
      int var1 = this.T;
      if ((-Integer.signum(var1) >>> 31 ^ 1) == 0) {
         byte var4 = 1;
         int var3 = this.T;
         int var10 = this.T = var3 - var4;
         boolean var6 = false;
         int var5 = var10;
         var10 = Integer.signum(var5);
         if ((var10 | -var10) >>> 31 == 0) {
            r4.g("GrimSpeed", "Correction recovery complete; checking contact next tick");
         }

         this.U = "Recovering after correction";
      } else {
         int var10000 = this.F();
         boolean var8 = false;
         byte var7 = (byte)var10000;
         var10000 = Integer.signum(var7);
         if ((var10000 | -var10000) >>> 31 != 0) {
            this.U = "Waiting for collision";
            this.u = mc.player;
            this.t = mc.level;
            this.o = this.u.tickCount;
            this.A = this.u.getBoundingBox();
         }
      }
   }

   private void o() {
      this.u = null;
      this.t = null;
      this.o = -1;
      this.A = null;
   }

   private boolean F() {
      if (!this.isInGame() || !yu.r()) {
         this.U = "Movement unavailable";
         return false;
      } else if (TargetStrafeModule.Y()) {
         this.U = "Pause: Target Strafe is orbiting";
         return false;
      } else if (yu.G("Speed")) {
         this.U = "Pause: Speed is enabled";
         return false;
      } else {
         FlyModule var1 = FlyModule.s();
         if (var1 != null && var1.E()) {
            this.U = "Pause: Fly is enabled";
            return false;
         } else {
            AirStuckModule var2 = AirStuckModule.H();
            if (var2 != null && var2.D()) {
               this.U = "Pause: AirStuck is enabled";
               return false;
            } else {
               ElytraMotionModule var3 = ElytraMotionModule.S();
               if (var3 != null && var3.m()) {
                  this.U = "Pause: ElytraMotion is holding";
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
                     this.U = "Waiting for normal movement";
                     return false;
                  } else if (var4.isUsingItem()) {
                     this.U = "Pause: using item";
                     return false;
                  } else {
                     return true;
                  }
               }
            }
         }
      }
   }

   public static Input f(LocalPlayer var0, Input var1) {
      if (i != null && i.isEnabled()) {
         GrimSpeedModule var2 = i;
         if (var0 != null
            && var2 != null
            && var2.isRuntimeActive()
            && var2.P.getValue()
            && var2.u == var0
            && var0 == getMc().player
            && var2.t == mc.level
            && var2.o == var0.tickCount) {
            try {
               if (!var2.F() || !var1.forward() || var1.backward() || var1.shift() || var0.getFoodData().getFoodLevel() <= 6 || com.larpclient.k.F() != null) {
                  return var1;
               }

               if (AuraModule.w()) {
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
               var2.e = (boolean)1;
               return new Input(var1.forward(), var1.backward(), var1.left(), var1.right(), var1.jump() || var3 != 0, var1.shift(), true);
            } catch (RuntimeException var4) {
               r4.r("GrimSpeed", "Input assistance failed; disabling and keeping native input", var4);
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
      if (i != null && i.isEnabled()) {
         GrimSpeedModule var1 = i;
         if (var0 != null && var1 != null && var1.isRuntimeActive() && var1.u == var0) {
            try {
               var1.W(var0);
            } catch (RuntimeException var3) {
               r4.r("GrimSpeed", "Collision phase failed; disabling and clearing pending boost", var3);
               var1.disable();
            }
         }
      }
   }

   private void W(LocalPlayer var1) {
      AABB var2 = this.A;
      int var3 = var1 == mc.player && this.t == mc.level && this.o == var1.tickCount ? 1 : 0;
      this.o();
      if (var3 == 0 || var2 == null) {
         this.U = "Movement unavailable";
      } else if (this.F()) {
         Vec3 var4 = F(var1.xxa, var1.zza, var1.getYRot());
         if (var4.lengthSqr() == 0.0) {
            this.U = "Waiting for movement input";
         } else {
            int var5 = 0;

            for (Entity var7 : mc.level.getEntities(var1, t(var2, var1.getBoundingBox()), GrimSpeedModule::v)) {
               if (EntitySelector.pushableBy(var7).test(var1)) {
                  if (++var5 >= this.M.getValue().intValue()) {
                     break;
                  }
               }
            }

            if (var5 == 0) {
               this.U = this.e ? "Sprint assist" : "Waiting for collision";
            } else {
               Vec3 var9 = var1.getDeltaMovement();
               double var10 = p(this.c.getValue(), var5);
               var1.setDeltaMovement(var9.x + var4.x * var10, var9.y, var9.z + var4.z * var10);
               this.U = String.format(Locale.ROOT, "Boosting: %d %s (+%.2f)", var5, var5 == 1 ? "entity" : "entities", var10);
            }
         }
      }
   }
}
