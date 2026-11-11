package com.larpclient.module.movement;

import com.larpclient.module.combat.AuraModule;
import com.larpclient.module.exploit.AirStuckModule;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.world.AutoTunnelModule;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.entity.PlayerInputUtil;
import com.larpclient.util.misc.LarpLog;
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
   private static GrimSpeedModule instance;
   private final NumberSetting collisionBoost = this.addSetting(
      new NumberSetting("Collision Boost", "Horizontal impulse per colliding entity, up to 0.08 total", 0.02, 0.01, 0.06, 0.01) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, "%.2f", this.getValue());
         }
      }
   );
   private final NumberSetting collisionLimit = this.addSetting(new NumberSetting("Collision Limit", "Maximum contributing entities", 1.0, 1.0, 3.0, 1.0));
   private final BooleanSetting sprintJump = this.addSetting(
      new BooleanSetting("Sprint Jump", "Sprint and jump normally while moving forward, including in open space", true)
   );
   private int correctionTicks;
   private String status = "Waiting for collision";
   private LocalPlayer pendingPlayer;
   private ClientLevel pendingLevel;
   private int pendingTickCount = -1;
   private AABB pendingBoundingBox;
   private boolean sprintAssisted;

   public GrimSpeedModule() {
      super("Grim Speed", "Native sprint jumps with an experimental entity collision boost", Category.MOVEMENT, -1);
      instance = this;
   }

   public static double computeBoost(double var0, int var2) {
      return !Double.isFinite(var0) ? 0.0 : Math.min(0.08, Math.max(0.0, var0) * Math.max(0, var2));
   }

   public static AABB getCollisionBox(AABB var0, AABB var1) {
      return var0.minmax(var1).inflate(0.19);
   }

   public static Vec3 getInputDirection(float var0, float var1, float var2) {
      if (Float.isFinite(var0) && Float.isFinite(var1) && Float.isFinite(var2)) {
         double var3 = Math.toRadians(var2);
         return new Vec3(Math.cos(var3) * var0 - Math.sin(var3) * var1, 0.0, Math.cos(var3) * var1 + Math.sin(var3) * var0).normalize();
      } else {
         return Vec3.ZERO;
      }
   }

   public static boolean isBoostableEntity(Entity var0) {
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

   public static void onPositionCorrection(LocalPlayer var0) {
      if (instance != null && instance.isRuntimeActive() && var0 == getMc().player) {
         if (instance.correctionTicks == 0) {
            LarpLog.debug("GrimSpeed", "Position correction: suspending sprint assistance and collision boost for 20 ticks");
         }

         instance.correctionTicks = 20;
         instance.clearPending();
         instance.status = "Recovering after correction";
      }
   }

   @Override
   public void onEnable() {
      this.onDisable();
      LarpLog.debug(
         "GrimSpeed",
         "Enabled: sprint jump={}, collision boost={}, limit={}, total collision cap=0.08",
         this.sprintJump.getValue(),
         this.collisionBoost.getValue(),
         this.collisionLimit.getValue().intValue()
      );
   }

   @Override
   public void onDisable() {
      this.correctionTicks = 0;
      this.sprintAssisted = (boolean)0;
      this.clearPending();
      this.status = "Waiting for collision";
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.dONE);
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() ? this.status : "Inactive";
   }

   @Override
   public void onTick() {
      this.sprintAssisted = (boolean)0;
      this.clearPending();
      boolean var2 = false;
      int var1 = this.correctionTicks;
      if ((-Integer.signum(var1) >>> 31 ^ 1) == 0) {
         byte var4 = 1;
         int var3 = this.correctionTicks;
         int var10 = this.correctionTicks = var3 - var4;
         boolean var6 = false;
         int var5 = var10;
         var10 = Integer.signum(var5);
         if ((var10 | -var10) >>> 31 == 0) {
            LarpLog.debug("GrimSpeed", "Correction recovery complete; checking contact next tick");
         }

         this.status = "Recovering after correction";
      } else {
         int var10000 = this.canAssistMovement();
         boolean var8 = false;
         byte var7 = (byte)var10000;
         var10000 = Integer.signum(var7);
         if ((var10000 | -var10000) >>> 31 != 0) {
            this.status = "Waiting for collision";
            this.pendingPlayer = minecraft.player;
            this.pendingLevel = minecraft.level;
            this.pendingTickCount = this.pendingPlayer.tickCount;
            this.pendingBoundingBox = this.pendingPlayer.getBoundingBox();
         }
      }
   }

   private void clearPending() {
      this.pendingPlayer = null;
      this.pendingLevel = null;
      this.pendingTickCount = -1;
      this.pendingBoundingBox = null;
   }

   private boolean canAssistMovement() {
      if (!this.isInGame() || !PlayerInputUtil.isInWorld()) {
         this.status = "Movement unavailable";
         return false;
      } else if (TargetStrafeModule.findTarget()) {
         this.status = "Pause: Target Strafe is orbiting";
         return false;
      } else if (PlayerInputUtil.isModuleActive("Speed")) {
         this.status = "Pause: Speed is enabled";
         return false;
      } else {
         FlyModule var1 = FlyModule.getInstance();
         if (var1 != null && var1.isFlightActive()) {
            this.status = "Pause: Fly is enabled";
            return false;
         } else {
            AirStuckModule var2 = AirStuckModule.getInstance();
            if (var2 != null && var2.isHolding()) {
               this.status = "Pause: AirStuck is enabled";
               return false;
            } else {
               ElytraMotionModule var3 = ElytraMotionModule.getInstance();
               if (var3 != null && var3.isHolding()) {
                  this.status = "Pause: ElytraMotion is holding";
                  return false;
               } else {
                  LocalPlayer var4 = minecraft.player;
                  if (minecraft.gui.screen() != null
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
                     this.status = "Waiting for normal movement";
                     return false;
                  } else if (var4.isUsingItem()) {
                     this.status = "Pause: using item";
                     return false;
                  } else {
                     return true;
                  }
               }
            }
         }
      }
   }

   public static Input modifyInput(LocalPlayer var0, Input var1) {
      if (instance != null && instance.isEnabled()) {
         GrimSpeedModule var2 = instance;
         if (var0 != null
            && var2 != null
            && var2.isRuntimeActive()
            && var2.sprintJump.getValue()
            && var2.pendingPlayer == var0
            && var0 == getMc().player
            && var2.pendingLevel == minecraft.level
            && var2.pendingTickCount == var0.tickCount) {
            try {
               if (!var2.canAssistMovement()
                  || !var1.forward()
                  || var1.backward()
                  || var1.shift()
                  || var0.getFoodData().getFoodLevel() <= 6
                  || AutoTunnelModule.isActive() != null) {
                  return var1;
               }

               if (AuraModule.isSprintResetActive()) {
                  return var1;
               }

               int var3 = var0.isSprinting()
                     && var0.onGround()
                     && !var0.horizontalCollision
                     && !var0.getAbilities().mayfly
                     && var0.getDeltaMovement().y <= 0.0
                     && minecraft.level.noCollision(var0, var0.getBoundingBox().expandTowards(0.0, 1.3, 0.0))
                  ? 1
                  : 0;
               var2.sprintAssisted = (boolean)1;
               return new Input(var1.forward(), var1.backward(), var1.left(), var1.right(), var1.jump() || var3 != 0, var1.shift(), true);
            } catch (RuntimeException var4) {
               LarpLog.error("GrimSpeed", "Input assistance failed; disabling and keeping native input", var4);
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

   public static void onCollisionPhase(LocalPlayer var0) {
      if (instance != null && instance.isEnabled()) {
         GrimSpeedModule var1 = instance;
         if (var0 != null && var1 != null && var1.isRuntimeActive() && var1.pendingPlayer == var0) {
            try {
               var1.applyCollisionBoost(var0);
            } catch (RuntimeException var3) {
               LarpLog.error("GrimSpeed", "Collision phase failed; disabling and clearing pending boost", var3);
               var1.disable();
            }
         }
      }
   }

   private void applyCollisionBoost(LocalPlayer var1) {
      AABB var2 = this.pendingBoundingBox;
      int var3 = var1 == minecraft.player && this.pendingLevel == minecraft.level && this.pendingTickCount == var1.tickCount ? 1 : 0;
      this.clearPending();
      if (var3 == 0 || var2 == null) {
         this.status = "Movement unavailable";
      } else if (this.canAssistMovement()) {
         Vec3 var4 = getInputDirection(var1.xxa, var1.zza, var1.getYRot());
         if (var4.lengthSqr() == 0.0) {
            this.status = "Waiting for movement input";
         } else {
            int var5 = 0;

            for (Entity var7 : minecraft.level.getEntities(var1, getCollisionBox(var2, var1.getBoundingBox()), GrimSpeedModule::isBoostableEntity)) {
               if (EntitySelector.pushableBy(var7).test(var1)) {
                  if (++var5 >= this.collisionLimit.getValue().intValue()) {
                     break;
                  }
               }
            }

            if (var5 == 0) {
               this.status = this.sprintAssisted ? "Sprint assist" : "Waiting for collision";
            } else {
               Vec3 var9 = var1.getDeltaMovement();
               double var10 = computeBoost(this.collisionBoost.getValue(), var5);
               var1.setDeltaMovement(var9.x + var4.x * var10, var9.y, var9.z + var4.z * var10);
               this.status = String.format(Locale.ROOT, "Boosting: %d %s (+%.2f)", var5, var5 == 1 ? "entity" : "entities", var10);
            }
         }
      }
   }
}
