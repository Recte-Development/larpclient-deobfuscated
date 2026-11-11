package com.larpclient.module.combat;

import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.entity.PlayerInputUtil;
import com.larpclient.util.misc.InteractionArbiter;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;

public class CriticalsModule extends Module {
   private final ModeSetting mode = this.addSetting(
      new ModeSetting("Mode", "Actual jump assistance; waits for falling eligibility", "Jump", "Jump", "MiniJump")
   );
   private static CriticalsModule instance;

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.sCANNING, ScanPhase.dONE);
   }

   public CriticalsModule() {
      super("Criticals", "Help time jumps for critical hits", Category.COMBAT, -1);
      instance = this;
   }

   @Override
   public Set<ScanPhase> getCurrentNetworkImpacts() {
      return Set.of(this.mode.isMode("Jump") ? ScanPhase.sCANNING : ScanPhase.dONE);
   }

   public static boolean canAssist() {
      if (instance == null || !instance.isEnabled()) {
         return false;
      } else if (instance != null && instance.isRuntimeActive() && InteractionArbiter.canInteract() && PlayerInputUtil.canAct()) {
         LocalPlayer var0 = getMc().player;
         return !var0.isInWater()
            && !var0.isInLava()
            && !var0.onClimbable()
            && !var0.isPassenger()
            && !var0.isSprinting()
            && !var0.getAbilities().flying
            && !var0.isFallFlying()
            && !var0.isMobilityRestricted()
            && !var0.isUsingItem();
      } else {
         return false;
      }
   }

   public static boolean shouldAttackNow() {
      if (instance == null || !instance.isEnabled()) {
         return false;
      } else if (canAssist() && !isFallingCritical()) {
         LocalPlayer var0 = getMc().player;
         return !var0.onGround() && var0.getDeltaMovement().y > 0.0 && var0.getAttackStrengthScale(0.0F) >= 1.0F ? true : jumpIfReady();
      } else {
         return false;
      }
   }

   public static boolean jumpIfReady() {
      if (instance == null || !instance.isEnabled()) {
         return false;
      }

      if (!canAssist()) {
         return false;
      }

      LocalPlayer var0 = getMc().player;
      if (var0.onGround() && !(var0.getDeltaMovement().y > 0.0) && !(var0.getAttackStrengthScale(0.0F) < 1.0F)) {
         if (instance.mode.isMode("Jump")) {
            var0.jumpFromGround();
         } else {
            var0.setDeltaMovement(var0.getDeltaMovement().x, 0.1, var0.getDeltaMovement().z);
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean isFallingCritical() {
      if (instance == null || !instance.isEnabled()) {
         return false;
      }

      if (!canAssist()) {
         return false;
      }

      LocalPlayer var0 = getMc().player;
      return !var0.onGround() && var0.fallDistance > 0.0 && var0.getDeltaMovement().y < 0.0 && var0.getAttackStrengthScale(0.0F) >= 1.0F;
   }

   @Override
   public void onDisable() {
      InteractionArbiter.reset();
   }
}
