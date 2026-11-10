package com.larpclient.module;

import com.larpclient._p;
import com.larpclient.nz;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;

public class CriticalsModule extends Module {
   private final o X = this.addSetting(new o("Mode", "Actual jump assistance; waits for falling eligibility", "Jump", "Jump", "MiniJump"));
   private static CriticalsModule o;

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.F, l.k);
   }

   public CriticalsModule() {
      super("Criticals", "Help time jumps for critical hits", Category.COMBAT, -1);
      o = this;
   }

   @Override
   public Set<l> getCurrentNetworkImpacts() {
      return Set.of(this.X.w("Jump") ? l.F : l.k);
   }

   public static boolean B() {
      if (o == null || !o.isEnabled()) {
         return false;
      } else if (o != null && o.isRuntimeActive() && _p.g() && nz.R()) {
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

   public static boolean Z() {
      if (o == null || !o.isEnabled()) {
         return false;
      } else if (B() && !p()) {
         LocalPlayer var0 = getMc().player;
         return !var0.onGround() && var0.getDeltaMovement().y > 0.0 && var0.getAttackStrengthScale(0.0F) >= 1.0F ? true : o();
      } else {
         return false;
      }
   }

   public static boolean o() {
      if (o == null || !o.isEnabled()) {
         return false;
      }

      if (!B()) {
         return false;
      }

      LocalPlayer var0 = getMc().player;
      if (var0.onGround() && !(var0.getDeltaMovement().y > 0.0) && !(var0.getAttackStrengthScale(0.0F) < 1.0F)) {
         if (o.X.w("Jump")) {
            var0.jumpFromGround();
         } else {
            var0.setDeltaMovement(var0.getDeltaMovement().x, 0.1, var0.getDeltaMovement().z);
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean p() {
      if (o == null || !o.isEnabled()) {
         return false;
      }

      if (!B()) {
         return false;
      }

      LocalPlayer var0 = getMc().player;
      return !var0.onGround() && var0.fallDistance > 0.0 && var0.getDeltaMovement().y < 0.0 && var0.getAttackStrengthScale(0.0F) >= 1.0F;
   }

   @Override
   public void onDisable() {
      _p.X();
   }
}
