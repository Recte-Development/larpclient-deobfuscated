package com.larpclient.module;

import com.larpclient.mt;
import com.larpclient.te;
import com.larpclient.wq;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Input;

public final class AutoSprintModule extends Module {
   private static AutoSprintModule q;
   private String b = "Waiting for movement";

   public AutoSprintModule() {
      super("Auto Sprint", "Sprint automatically without holding the sprint key", Category.MOVEMENT, -1);
      q = this;
   }

   public static AutoSprintModule P() {
      return q;
   }

   public static Input P(Input var0) {
      return var0.sprint() ? var0 : new Input(var0.forward(), var0.backward(), var0.left(), var0.right(), var0.jump(), var0.shift(), true);
   }

   public static Input H(LocalPlayer var0, Input var1) {
      if (q != null && q.isEnabled()) {
         AutoSprintModule var2 = q;
         if (var2 != null && var2.isRuntimeActive() && var0 != null) {
            try {
               if (var0 != getMc().player || !te.i()) {
                  var2.b = "Movement unavailable";
                  return var1;
               } else if (wq.g() != null) {
                  var2.b = "Pause: AutoTunnel is steering";
                  return var1;
               } else if (AuraModule.z()) {
                  var2.b = "Pause: critical sprint release";
                  return var1;
               } else {
                  var2.b = var0.isSprinting() ? "Sprinting" : "Waiting for movement";
                  return P(var1);
               }
            } catch (RuntimeException var4) {
               mt.P("AutoSprint", "Input assistance failed; disabling and keeping native input", var4);
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
      this.b = "Waiting for movement";
      mt.U("AutoSprint", "Enabled: holding sprint input, native sprint eligibility");
   }

   @Override
   public void onDisable() {
      this.b = "Waiting for movement";
      mt.U("AutoSprint", "Disabled: sprint follows the physical key again");
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.z);
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() ? this.b : "Inactive";
   }
}
