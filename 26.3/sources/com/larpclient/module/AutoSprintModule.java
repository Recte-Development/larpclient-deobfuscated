package com.larpclient.module;

import com.larpclient._n;
import com.larpclient._q;
import com.larpclient.nz;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Input;

public final class AutoSprintModule extends Module {
   private static AutoSprintModule p;
   private String H = "Waiting for movement";

   public AutoSprintModule() {
      super("Auto Sprint", "Sprint automatically without holding the sprint key", Category.MOVEMENT, -1);
      p = this;
   }

   public static AutoSprintModule q() {
      return p;
   }

   public static Input T(Input var0) {
      return var0.sprint() ? var0 : new Input(var0.forward(), var0.backward(), var0.left(), var0.right(), var0.jump(), var0.shift(), true);
   }

   public static Input l(LocalPlayer var0, Input var1) {
      if (p != null && p.isEnabled()) {
         AutoSprintModule var2 = p;
         if (var2 != null && var2.isRuntimeActive() && var0 != null) {
            try {
               if (var0 != getMc().player || !nz.v()) {
                  var2.H = "Movement unavailable";
                  return var1;
               } else if (_n.P() != null) {
                  var2.H = "Pause: AutoTunnel is steering";
                  return var1;
               } else if (AuraModule.M()) {
                  var2.H = "Pause: critical sprint release";
                  return var1;
               } else {
                  var2.H = var0.isSprinting() ? "Sprinting" : "Waiting for movement";
                  return T(var1);
               }
            } catch (RuntimeException var4) {
               _q.o("AutoSprint", "Input assistance failed; disabling and keeping native input", var4);
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
      this.H = "Waiting for movement";
      _q.c("AutoSprint", "Enabled: holding sprint input, native sprint eligibility");
   }

   @Override
   public void onDisable() {
      this.H = "Waiting for movement";
      _q.c("AutoSprint", "Disabled: sprint follows the physical key again");
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.F);
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() ? this.H : "Inactive";
   }
}
