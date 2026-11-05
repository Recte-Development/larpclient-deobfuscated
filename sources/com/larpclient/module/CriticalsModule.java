package com.larpclient.module;

import com.larpclient.d8;
import com.larpclient.yu;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;

public class CriticalsModule extends Module {
   private final lp M = this.addSetting(new lp("Mode", "Actual jump assistance; waits for falling eligibility", "Jump", "Jump", "MiniJump"));
   private static CriticalsModule x;

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.a, ld.c);
   }

   public CriticalsModule() {
      super("Criticals", "Help time jumps for critical hits", Category.COMBAT, -1);
      x = this;
   }

   @Override
   public Set<ld> getCurrentNetworkImpacts() {
      return Set.of(this.M.i("Jump") ? ld.a : ld.c);
   }

   public static boolean p() {
      if (x == null || !x.isEnabled()) {
         return false;
      } else if (x != null && x.isRuntimeActive() && d8.a() && yu.X()) {
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

   public static boolean t() {
      if (x == null || !x.isEnabled()) {
         return false;
      } else if (p() && !A()) {
         LocalPlayer var0 = getMc().player;
         return !var0.onGround() && var0.getDeltaMovement().y > 0.0 && var0.getAttackStrengthScale(0.0F) >= 1.0F ? true : F();
      } else {
         return false;
      }
   }

   public static boolean F() {
      if (x == null || !x.isEnabled()) {
         return false;
      }

      if (!p()) {
         return false;
      }

      LocalPlayer var0 = getMc().player;
      if (var0.onGround() && !(var0.getDeltaMovement().y > 0.0) && !(var0.getAttackStrengthScale(0.0F) < 1.0F)) {
         if (x.M.i("Jump")) {
            var0.jumpFromGround();
         } else {
            var0.setDeltaMovement(var0.getDeltaMovement().x, 0.1, var0.getDeltaMovement().z);
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean A() {
      if (x == null || !x.isEnabled()) {
         return false;
      }

      if (!p()) {
         return false;
      }

      LocalPlayer var0 = getMc().player;
      return !var0.onGround() && var0.fallDistance > 0.0 && var0.getDeltaMovement().y < 0.0 && var0.getAttackStrengthScale(0.0F) >= 1.0F;
   }

   @Override
   public void onDisable() {
      d8.h();
   }
}
