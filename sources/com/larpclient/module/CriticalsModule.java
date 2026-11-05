package com.larpclient.module;

import com.larpclient._m;
import com.larpclient.te;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;

public class CriticalsModule extends Module {
   private final d6 x = this.addSetting(new d6("Mode", "Actual jump assistance; waits for falling eligibility", "Jump", "Jump", "MiniJump"));
   private static CriticalsModule c;

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.z, dm.j);
   }

   public CriticalsModule() {
      super("Criticals", "Help time jumps for critical hits", Category.COMBAT, -1);
      c = this;
   }

   @Override
   public Set<dm> getCurrentNetworkImpacts() {
      return Set.of(this.x.S("Jump") ? dm.z : dm.j);
   }

   public static boolean d() {
      if (c == null || !c.isEnabled()) {
         return false;
      } else if (c != null && c.isRuntimeActive() && _m.z() && te.h()) {
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

   public static boolean D() {
      if (c == null || !c.isEnabled()) {
         return false;
      } else if (d() && !o()) {
         LocalPlayer var0 = getMc().player;
         return !var0.onGround() && var0.getDeltaMovement().y > 0.0 && var0.getAttackStrengthScale(0.0F) >= 1.0F ? true : n();
      } else {
         return false;
      }
   }

   public static boolean n() {
      if (c == null || !c.isEnabled()) {
         return false;
      }

      if (!d()) {
         return false;
      }

      LocalPlayer var0 = getMc().player;
      if (var0.onGround() && !(var0.getDeltaMovement().y > 0.0) && !(var0.getAttackStrengthScale(0.0F) < 1.0F)) {
         if (c.x.S("Jump")) {
            var0.jumpFromGround();
         } else {
            var0.setDeltaMovement(var0.getDeltaMovement().x, 0.1, var0.getDeltaMovement().z);
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean o() {
      if (c == null || !c.isEnabled()) {
         return false;
      }

      if (!d()) {
         return false;
      }

      LocalPlayer var0 = getMc().player;
      return !var0.onGround() && var0.fallDistance > 0.0 && var0.getDeltaMovement().y < 0.0 && var0.getAttackStrengthScale(0.0F) >= 1.0F;
   }

   @Override
   public void onDisable() {
      _m.U();
   }
}
