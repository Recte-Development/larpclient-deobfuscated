package com.larpclient.module;

import com.larpclient._n;
import com.larpclient._q;
import com.larpclient.nz;
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
   private static GrimSpeedModule s;
   private final b F = this.addSetting(new b("Collision Boost", "Horizontal impulse per colliding entity, up to 0.08 total", 0.02, 0.01, 0.06, 0.01) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.2f", this.getValue());
      }
   });
   private final b E = this.addSetting(new b("Collision Limit", "Maximum contributing entities", 1.0, 1.0, 3.0, 1.0));
   private final m g = this.addSetting(new m("Sprint Jump", "Sprint and jump normally while moving forward, including in open space", true));
   private int I;
   private String b = "Waiting for collision";
   private LocalPlayer N;
   private ClientLevel r;
   private int h = -1;
   private AABB Y;
   private boolean v;

   public GrimSpeedModule() {
      super("Grim Speed", "Native sprint jumps with an experimental entity collision boost", Category.MOVEMENT, -1);
      s = this;
   }

   public static double w(double var0, int var2) {
      return !Double.isFinite(var0) ? 0.0 : Math.min(0.08, Math.max(0.0, var0) * Math.max(0, var2));
   }

   public static AABB j(AABB var0, AABB var1) {
      return var0.minmax(var1).inflate(0.19);
   }

   public static Vec3 Q(float var0, float var1, float var2) {
      if (Float.isFinite(var0) && Float.isFinite(var1) && Float.isFinite(var2)) {
         double var3 = Math.toRadians(var2);
         return new Vec3(Math.cos(var3) * var0 - Math.sin(var3) * var1, 0.0, Math.cos(var3) * var1 + Math.sin(var3) * var0).normalize();
      } else {
         return Vec3.ZERO;
      }
   }

   public static boolean I(Entity var0) {
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

   public static void U(LocalPlayer var0) {
      if (s != null && s.isRuntimeActive() && var0 == getMc().player) {
         if (s.I == 0) {
            _q.c("GrimSpeed", "Position correction: suspending sprint assistance and collision boost for 20 ticks");
         }

         s.I = 20;
         s.W();
         s.b = "Recovering after correction";
      }
   }

   @Override
   public void onEnable() {
      this.onDisable();
      _q.c(
         "GrimSpeed",
         "Enabled: sprint jump={}, collision boost={}, limit={}, total collision cap=0.08",
         this.g.getValue(),
         this.F.getValue(),
         this.E.getValue().intValue()
      );
   }

   @Override
   public void onDisable() {
      this.I = 0;
      this.v = (boolean)0;
      this.W();
      this.b = "Waiting for collision";
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.k);
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() ? this.b : "Inactive";
   }

   @Override
   public void onTick() {
      this.v = (boolean)0;
      this.W();
      boolean var2 = false;
      int var1 = this.I;
      if ((-Integer.signum(var1) >>> 31 ^ 1) == 0) {
         byte var4 = 1;
         int var3 = this.I;
         int var10 = this.I = var3 - var4;
         boolean var6 = false;
         int var5 = var10;
         var10 = Integer.signum(var5);
         if ((var10 | -var10) >>> 31 == 0) {
            _q.c("GrimSpeed", "Correction recovery complete; checking contact next tick");
         }

         this.b = "Recovering after correction";
      } else {
         int var10000 = this.P();
         boolean var8 = false;
         byte var7 = (byte)var10000;
         var10000 = Integer.signum(var7);
         if ((var10000 | -var10000) >>> 31 != 0) {
            this.b = "Waiting for collision";
            this.N = mc.player;
            this.r = mc.level;
            this.h = this.N.tickCount;
            this.Y = this.N.getBoundingBox();
         }
      }
   }

   private void W() {
      this.N = null;
      this.r = null;
      this.h = -1;
      this.Y = null;
   }

   private boolean P() {
      if (!this.isInGame() || !nz.v()) {
         this.b = "Movement unavailable";
         return false;
      } else if (TargetStrafeModule.g()) {
         this.b = "Pause: Target Strafe is orbiting";
         return false;
      } else if (nz.X("Speed")) {
         this.b = "Pause: Speed is enabled";
         return false;
      } else {
         FlyModule var1 = FlyModule.Q();
         if (var1 != null && var1.r()) {
            this.b = "Pause: Fly is enabled";
            return false;
         } else {
            AirStuckModule var2 = AirStuckModule.Z();
            if (var2 != null && var2.J()) {
               this.b = "Pause: AirStuck is enabled";
               return false;
            } else {
               ElytraMotionModule var3 = ElytraMotionModule.n();
               if (var3 != null && var3.Q()) {
                  this.b = "Pause: ElytraMotion is holding";
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
                     this.b = "Waiting for normal movement";
                     return false;
                  } else if (var4.isUsingItem()) {
                     this.b = "Pause: using item";
                     return false;
                  } else {
                     return true;
                  }
               }
            }
         }
      }
   }

   public static Input n(LocalPlayer var0, Input var1) {
      if (s != null && s.isEnabled()) {
         GrimSpeedModule var2 = s;
         if (var0 != null
            && var2 != null
            && var2.isRuntimeActive()
            && var2.g.getValue()
            && var2.N == var0
            && var0 == getMc().player
            && var2.r == mc.level
            && var2.h == var0.tickCount) {
            try {
               if (!var2.P() || !var1.forward() || var1.backward() || var1.shift() || var0.getFoodData().getFoodLevel() <= 6 || _n.P() != null) {
                  return var1;
               }

               if (AuraModule.M()) {
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
               var2.v = (boolean)1;
               return new Input(var1.forward(), var1.backward(), var1.left(), var1.right(), var1.jump() || var3 != 0, var1.shift(), true);
            } catch (RuntimeException var4) {
               _q.o("GrimSpeed", "Input assistance failed; disabling and keeping native input", var4);
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

   public static void B(LocalPlayer var0) {
      if (s != null && s.isEnabled()) {
         GrimSpeedModule var1 = s;
         if (var0 != null && var1 != null && var1.isRuntimeActive() && var1.N == var0) {
            try {
               var1.F(var0);
            } catch (RuntimeException var3) {
               _q.o("GrimSpeed", "Collision phase failed; disabling and clearing pending boost", var3);
               var1.disable();
            }
         }
      }
   }

   private void F(LocalPlayer var1) {
      AABB var2 = this.Y;
      int var3 = var1 == mc.player && this.r == mc.level && this.h == var1.tickCount ? 1 : 0;
      this.W();
      if (var3 == 0 || var2 == null) {
         this.b = "Movement unavailable";
      } else if (this.P()) {
         Vec3 var4 = Q(var1.xxa, var1.zza, var1.getYRot());
         if (var4.lengthSqr() == 0.0) {
            this.b = "Waiting for movement input";
         } else {
            int var5 = 0;

            for (Entity var7 : mc.level.getEntities(var1, j(var2, var1.getBoundingBox()), GrimSpeedModule::I)) {
               if (EntitySelector.pushableBy(var7).test(var1)) {
                  if (++var5 >= this.E.getValue().intValue()) {
                     break;
                  }
               }
            }

            if (var5 == 0) {
               this.b = this.v ? "Sprint assist" : "Waiting for collision";
            } else {
               Vec3 var9 = var1.getDeltaMovement();
               double var10 = w(this.F.getValue(), var5);
               var1.setDeltaMovement(var9.x + var4.x * var10, var9.y, var9.z + var4.z * var10);
               this.b = String.format(Locale.ROOT, "Boosting: %d %s (+%.2f)", var5, var5 == 1 ? "entity" : "entities", var10);
            }
         }
      }
   }
}
