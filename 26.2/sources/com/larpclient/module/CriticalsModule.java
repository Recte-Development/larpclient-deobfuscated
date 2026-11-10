package com.larpclient.module;

import com.larpclient.c5;
import com.larpclient.ta;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;

public class CriticalsModule extends Module {
   private final l U = this.addSetting(new l("Mode", "Actual jump assistance; waits for falling eligibility", "Jump", "Jump", "MiniJump"));
   private static CriticalsModule G;

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.K, m.U);
   }

   public CriticalsModule() {
      super("Criticals", "Help time jumps for critical hits", Category.COMBAT, -1);
      G = this;
   }

   @Override
   public Set<m> getCurrentNetworkImpacts() {
      return Set.of(this.U.O("Jump") ? m.K : m.U);
   }

   public static boolean e() {
      if (G == null || !G.isEnabled()) {
         return false;
      } else if (G != null && G.isRuntimeActive() && ta.Y() && c5.w()) {
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

   public static boolean c() {
      if (G == null || !G.isEnabled()) {
         return false;
      } else if (e() && !g()) {
         LocalPlayer var0 = getMc().player;
         return !var0.onGround() && var0.getDeltaMovement().y > 0.0 && var0.getAttackStrengthScale(0.0F) >= 1.0F ? true : B();
      } else {
         return false;
      }
   }

   public static boolean B() {
      if (G == null || !G.isEnabled()) {
         return false;
      }

      if (!e()) {
         return false;
      }

      LocalPlayer var0 = getMc().player;
      if (var0.onGround() && !(var0.getDeltaMovement().y > 0.0) && !(var0.getAttackStrengthScale(0.0F) < 1.0F)) {
         if (G.U.O("Jump")) {
            var0.jumpFromGround();
         } else {
            var0.setDeltaMovement(var0.getDeltaMovement().x, 0.1, var0.getDeltaMovement().z);
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean g() {
      if (G == null || !G.isEnabled()) {
         return false;
      }

      if (!e()) {
         return false;
      }

      LocalPlayer var0 = getMc().player;
      return !var0.onGround() && var0.fallDistance > 0.0 && var0.getDeltaMovement().y < 0.0 && var0.getAttackStrengthScale(0.0F) >= 1.0F;
   }

   @Override
   public void onDisable() {
      ta.B();
   }
}
