package com.larpclient.module.movement;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.entity.PlayerInputUtil;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SpeedModule extends Module {
   private static SpeedModule instance;
   private final NumberSetting speed = this.addSetting(new NumberSetting("Speed", "Speed multiplier", 1.5, 1.0, 10.0, 0.1));
   private final ModeSetting mode = this.addSetting(
      new ModeSetting("Mode", "Speed method", "Vanilla", "Vanilla", "BHop", "Strafe", "LowHop", "Entity Collision")
   );
   private final BooleanSetting autoJump = this.addSetting(new BooleanSetting("Auto Jump", "Jump automatically", true));
   private final BooleanSetting onlyOnGround = this.addSetting(new BooleanSetting("Only On Ground", "Only boost when grounded", false));
   private final BooleanSetting damageBoost = this.addSetting(new BooleanSetting("Damage Boost", "Speed after taking damage", false));
   private final NumberSetting strafe = this.addSetting(new NumberSetting("Strafe", "Air strafe strength", 0.8, 0.1, 2.0, 0.1));
   private final BooleanSetting smoothAccel = this.addSetting(new BooleanSetting("Smooth Accel", "Gradual speed increase", true));
   private final NumberSetting collisionBoost = this.addSetting(
      new NumberSetting("Collision Boost", "Entity Collision only: horizontal boost per nearby entity", 0.02, 0.01, 0.06, 0.01)
   );
   private final NumberSetting collisionLimit = this.addSetting(
      new NumberSetting("Collision Limit", "Entity Collision only: maximum entities contributing to the boost", 1.0, 1.0, 3.0, 1.0)
   );
   private double currentSpeed;
   private String lastMode = "";
   private boolean damageBoostActive;
   private int damageBoostTicks;
   private boolean wasHurt;
   private String activeMode = "";
   private int collisionCooldown;
   private int collisionCount;

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.dONE);
   }

   public SpeedModule() {
      super("Speed", "Move faster", Category.MOVEMENT, -1);
      instance = this;
      this.collisionBoost.visibleWhen(() -> this.mode.isMode("Entity Collision"));
      this.collisionLimit.visibleWhen(() -> this.mode.isMode("Entity Collision"));
      this.speed.visibleWhen(() -> !this.mode.isMode("Entity Collision"));
      this.autoJump.visibleWhen(() -> this.mode.isMode("BHop") || this.mode.isMode("Strafe") || this.mode.isMode("LowHop"));
      this.onlyOnGround.visibleWhen(() -> !this.mode.isMode("Entity Collision"));
      this.damageBoost.visibleWhen(() -> !this.mode.isMode("Entity Collision"));
      this.strafe.visibleWhen(() -> this.mode.isMode("Strafe"));
      this.smoothAccel.visibleWhen(() -> !this.mode.isMode("Entity Collision"));
   }

   @Override
   public void onEnable() {
      this.onDisable();
      this.activeMode = this.mode.getValue();
   }

   @Override
   public void onDisable() {
      this.resetState();
      this.activeMode = "";
      this.collisionCooldown = 0;
      this.collisionCount = 0;
   }

   private void resetState() {
      this.currentSpeed = 0.0;
      this.lastMode = "";
      this.damageBoostTicks = 0;
      this.wasHurt = (boolean)0;
      this.damageBoostActive = (boolean)0;
   }

   public static void onPlayerTeleport(LocalPlayer var0) {
      if (instance != null && instance.isRuntimeActive() && var0 == getMc().player) {
         instance.collisionCooldown = 20;
         instance.collisionCount = 0;
      }
   }

   @Override
   public void onTick() {
      this.collisionCount = 0;
      int var1 = this.collisionCooldown > 0 ? 1 : 0;
      if (var1 != 0) {
         byte var15 = 1;
         int var14 = this.collisionCooldown;
         this.collisionCooldown = var14 - var15;
      }

      if (this.isInGame()) {
         if (!this.activeMode.equals(this.mode.getValue())) {
            this.resetState();
            this.activeMode = this.mode.getValue();
         }

         if (PlayerInputUtil.isInWorld()
            && !TargetStrafeModule.shouldAutoJump()
            && minecraft.gui.screen() == null
            && !minecraft.player.isInWater()
            && !minecraft.player.isInLava()
            && !minecraft.player.onClimbable()) {
            Vec3 var2 = PlayerInputUtil.getInputDirection();
            boolean var3 = minecraft.player.onGround();
            if (this.mode.isMode("Entity Collision")) {
               if (var1 == 0) {
                  this.applyCollisionBoost(var2);
               }
            } else if (var2.lengthSqr() != 0.0 && (!this.onlyOnGround.getValue() || var3)) {
               if (!this.lastMode.equals(this.mode.getValue())) {
                  this.currentSpeed = 0.0;
                  this.lastMode = this.mode.getValue();
               }

               int var4 = minecraft.player.hurtTime > 0 ? 1 : 0;
               if (this.damageBoost.getValue() && var4 != 0 && !this.wasHurt) {
                  this.damageBoostTicks = 10;
               }

               int var25;
               label120: {
                  this.wasHurt = (boolean)var4;
                  if (this.damageBoost.getValue()) {
                     var25 = this.damageBoostTicks;
                     byte var17 = 1;
                     int var16 = this.damageBoostTicks;
                     this.damageBoostTicks = var16 - var17;
                     boolean var19 = false;
                     int var18 = var25;
                     if ((-Integer.signum(var18) >>> 31 ^ 1) == 0) {
                        var25 = 1;
                        break label120;
                     }
                  }

                  var25 = 0;
               }

               this.damageBoostActive = (boolean)var25;
               if (this.damageBoostTicks < 0) {
                  this.damageBoostTicks = 0;
               }

               if (!this.mode.isMode("Vanilla") || var3) {
                  double var5 = switch ((String)this.mode.getValue()) {
                     case "BHop" -> 0.26;
                     case "Strafe" -> 0.28;
                     case "LowHop" -> 0.22;
                     default -> 0.13;
                  };
                  double var24 = var5 * this.speed.getValue();
                  boolean var21 = false;
                  byte var20 = this.damageBoostActive;
                  var25 = Integer.signum(var20);
                  double var22 = var24 * (((var25 | -var25) >>> 31 ^ 1) == 0 ? 1.15 : 1.0);
                  this.currentSpeed = this.smoothAccel.getValue() ? this.currentSpeed + (var22 - this.currentSpeed) * 0.3 : var22;
                  Vec3 var9 = minecraft.player.getDeltaMovement();
                  Vec3 var10 = var2.scale(this.currentSpeed);
                  Vec3 var11 = new Vec3(var9.x, 0.0, var9.z);
                  double var12 = (this.mode.isMode("Strafe") && !var3 ? 0.04 * this.strafe.getValue() : 0.08) * getStepScale(this.speed.getValue());
                  var11 = PlayerInputUtil.stepToward(var11, var10, var12);
                  minecraft.player.setDeltaMovement(var11.x, var9.y, var11.z);
                  if (var3 && this.autoJump.getValue() && !this.mode.isMode("Vanilla") && var9.y <= 0.0) {
                     if (this.mode.isMode("LowHop")) {
                        minecraft.player.setDeltaMovement(var11.x, 0.32, var11.z);
                     } else {
                        minecraft.player.jumpFromGround();
                     }
                  }
               }
            } else {
               this.currentSpeed = 0.0;
            }
         } else {
            this.resetState();
         }
      }
   }

   public static double getStepScale(double var0) {
      return Math.max(1.0, var0 - 4.0);
   }

   private void applyCollisionBoost(Vec3 var1) {
      LocalPlayer var2 = minecraft.player;
      if (var1.lengthSqr() != 0.0
         && !var2.isPassenger()
         && !var2.isSpectator()
         && !var2.getAbilities().flying
         && !var2.isFallFlying()
         && !var2.isCrouching()
         && var2.hurtTime <= 0) {
         AABB var3 = var2.getBoundingBox().inflate(0.19);

         for (Entity var5 : minecraft.level
            .getEntities(
               var2,
               var3,
               var0 -> var0 instanceof LivingEntity
                  && !(var0 instanceof ArmorStand)
                  && var0.isAlive()
                  && !var0.isSpectator()
                  && !var0.isPassenger()
                  && var0.isPushable()
            )) {
            if (EntitySelector.pushableBy(var5).test(var2) && ++this.collisionCount >= this.collisionLimit.getValue().intValue()) {
               break;
            }
         }

         if (this.collisionCount != 0) {
            double var7 = Math.min(0.08, this.collisionBoost.getValue() * this.collisionCount);
            Vec3 var6 = var2.getDeltaMovement();
            var2.setDeltaMovement(var6.x + var1.x * var7, var6.y, var6.z + var1.z * var7);
         }
      }
   }

   @Override
   public String getStatus() {
      if (!this.mode.isMode("Entity Collision")) {
         return "";
      } else if (!this.isRuntimeActive()) {
         return "Inactive";
      } else if (this.collisionCooldown > 0) {
         return "Collision boost recovering";
      } else {
         return this.collisionCount == 0 ? "Waiting for collision" : "Collision boost active";
      }
   }
}
