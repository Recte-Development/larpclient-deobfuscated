package com.larpclient.module.movement;

import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.entity.PlayerInputUtil;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.phys.Vec3;

public class FlyModule extends Module {
   private final NumberSetting speed = this.addSetting(new NumberSetting("Speed", "Flight speed", 2.0, 0.5, 10.0, 0.1));
   private final ModeSetting mode = this.addSetting(new ModeSetting("Mode", "Vanilla flight or gradual glide descent", "Vanilla", "Vanilla", "Glide"));
   private final NumberSetting glideSpeed = this.addSetting(new NumberSetting("Glide Speed", "Target descent speed", 0.02, 0.0, 0.1, 0.005));
   private static FlyModule instance;
   private LocalPlayer trackedPlayer;
   private boolean originalFlying;
   private boolean appliedFlying;
   private boolean originalMayFly;
   private float originalFlySpeed;
   private float appliedFlySpeed;

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.dONE);
   }

   public FlyModule() {
      super("Fly", "Fly or glide slowly", Category.MOVEMENT, -1);
      instance = this;
      this.glideSpeed.visibleWhen(() -> this.mode.isMode("Glide"));
   }

   public static FlyModule getInstance() {
      return instance;
   }

   public boolean isFlightActive() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && getMc().player != null && getMc().player.isAlive();
   }

   @Override
   public String getStatus() {
      if (!this.isEnabled()) {
         return "Vanilla flight or gradual glide descent";
      } else {
         return this.isFlightActive() ? "Flight active" : "Waiting for player";
      }
   }

   @Override
   public void onTick() {
      if (this.isInGame() && this.isFlightActive() && PlayerInputUtil.isInWorld()) {
         if (this.mode.isMode("Vanilla")) {
            Abilities var1 = minecraft.player.getAbilities();
            if (this.trackedPlayer != minecraft.player) {
               this.restoreAbilities();
               this.trackedPlayer = minecraft.player;
               this.originalMayFly = var1.mayfly;
               this.originalFlying = var1.flying;
               this.originalFlySpeed = var1.flyingSpeed;
            }

            byte var7 = this.appliedFlying;
            byte var6 = var1.flying;
            int var10000 = Integer.compare(var6, var7);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               this.originalFlying = var1.flying;
            }

            if (var1.flyingSpeed != this.appliedFlySpeed) {
               this.originalFlySpeed = var1.flyingSpeed;
            }

            boolean var9 = false;
            byte var8 = var1.mayfly;
            var10000 = Integer.signum(var8);
            if ((var10000 | -var10000) >>> 31 == 0) {
               this.originalMayFly = (boolean)0;
            }

            boolean var11 = false;
            byte var10 = var1.flying;
            var10000 = Integer.signum(var10);
            int var2 = (var10000 | -var10000) >>> 31 == 0 ? 1 : 0;
            var1.mayfly = (boolean)1;
            var1.flying = this.appliedFlying = (boolean)1;
            var1.flyingSpeed = this.appliedFlySpeed = this.speed.getValue().floatValue() * 0.05F;
            boolean var5 = false;
            int var4 = var2;
            var10000 = Integer.signum(var4);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               minecraft.player.onUpdateAbilities();
            }
         } else {
            this.restoreAbilities();
            Vec3 var12 = minecraft.player.getDeltaMovement();
            if (!minecraft.player.onGround() && var12.y < 0.0 && !minecraft.player.isInWater() && !minecraft.player.isInLava()) {
               Vec3 var13 = PlayerInputUtil.getInputDirection().scale(this.speed.getValue() * 0.15);
               Vec3 var3 = PlayerInputUtil.stepToward(new Vec3(var12.x, 0.0, var12.z), var13, 0.04);
               minecraft.player.setDeltaMovement(var3.x, Math.min(0.0, var12.y + Math.min(0.08, -this.glideSpeed.getValue() - var12.y)), var3.z);
            }
         }
      } else {
         this.restoreAbilities();
      }
   }

   private void restoreAbilities() {
      if (this.trackedPlayer != null) {
         Abilities var1 = this.trackedPlayer.getAbilities();
         boolean var2 = var1.flying;
         if (var1.mayfly) {
            var1.mayfly = this.originalMayFly;
         }

         if (var1.flying == this.appliedFlying) {
            var1.flying = (boolean)(var1.mayfly && this.originalFlying ? 1 : 0);
         }

         if (var1.flyingSpeed == this.appliedFlySpeed) {
            var1.flyingSpeed = this.originalFlySpeed;
         }

         if (var2 != var1.flying && this.trackedPlayer == getMc().player && this.trackedPlayer.isAlive()) {
            this.trackedPlayer.onUpdateAbilities();
         }

         this.trackedPlayer = null;
      }
   }

   public void forgetPlayer() {
      this.trackedPlayer = null;
   }

   @Override
   public void onDisable() {
      this.restoreAbilities();
   }
}
