package com.larpclient.module;

import com.larpclient.af;
import com.larpclient.c5;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Input;

public final class AutoSprintModule extends Module {
   private static AutoSprintModule Q;
   private String Y = "Waiting for movement";

   public AutoSprintModule() {
      super("Auto Sprint", "Sprint automatically without holding the sprint key", Category.MOVEMENT, -1);
      Q = this;
   }

   public static AutoSprintModule W() {
      return Q;
   }

   public static Input p(Input var0) {
      return var0.sprint() ? var0 : new Input(var0.forward(), var0.backward(), var0.left(), var0.right(), var0.jump(), var0.shift(), true);
   }

   public static Input j(LocalPlayer var0, Input var1) {
      if (Q != null && Q.isEnabled()) {
         AutoSprintModule var2 = Q;
         if (var2 != null && var2.isRuntimeActive() && var0 != null) {
            try {
               if (var0 != getMc().player || !c5.J()) {
                  var2.Y = "Movement unavailable";
                  return var1;
               } else if (af.K() != null) {
                  var2.Y = "Pause: AutoTunnel is steering";
                  return var1;
               } else if (AuraModule.G()) {
                  var2.Y = "Pause: critical sprint release";
                  return var1;
               } else {
                  var2.Y = var0.isSprinting() ? "Sprinting" : "Waiting for movement";
                  return p(var1);
               }
            } catch (RuntimeException var4) {
               com.larpclient.h.m("AutoSprint", "Input assistance failed; disabling and keeping native input", var4);
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

   @Override
   public void onEnable() {
      this.Y = "Waiting for movement";
      com.larpclient.h.U("AutoSprint", "Enabled: holding sprint input, native sprint eligibility");
   }

   @Override
   public void onDisable() {
      this.Y = "Waiting for movement";
      com.larpclient.h.U("AutoSprint", "Disabled: sprint follows the physical key again");
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.K);
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() ? this.Y : "Inactive";
   }
}
