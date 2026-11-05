package com.larpclient.module;

import com.larpclient.r4;
import com.larpclient.yu;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Input;

public final class AutoSprintModule extends Module {
   private static AutoSprintModule F;
   private String T = "Waiting for movement";

   public AutoSprintModule() {
      super("Auto Sprint", "Sprint automatically without holding the sprint key", Category.MOVEMENT, -1);
      F = this;
   }

   public static AutoSprintModule k() {
      return F;
   }

   public static Input k(Input var0) {
      return var0.sprint() ? var0 : new Input(var0.forward(), var0.backward(), var0.left(), var0.right(), var0.jump(), var0.shift(), true);
   }

   public static Input M(LocalPlayer var0, Input var1) {
      if (F != null && F.isEnabled()) {
         AutoSprintModule var2 = F;
         if (var2 != null && var2.isRuntimeActive() && var0 != null) {
            try {
               if (var0 != getMc().player || !yu.r()) {
                  var2.T = "Movement unavailable";
                  return var1;
               } else if (com.larpclient.k.F() != null) {
                  var2.T = "Pause: AutoTunnel is steering";
                  return var1;
               } else if (AuraModule.w()) {
                  var2.T = "Pause: critical sprint release";
                  return var1;
               } else {
                  var2.T = var0.isSprinting() ? "Sprinting" : "Waiting for movement";
                  return k(var1);
               }
            } catch (RuntimeException var4) {
               r4.r("AutoSprint", "Input assistance failed; disabling and keeping native input", var4);
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
      this.T = "Waiting for movement";
      r4.g("AutoSprint", "Enabled: holding sprint input, native sprint eligibility");
   }

   @Override
   public void onDisable() {
      this.T = "Waiting for movement";
      r4.g("AutoSprint", "Disabled: sprint follows the physical key again");
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.a);
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() ? this.T : "Inactive";
   }
}
