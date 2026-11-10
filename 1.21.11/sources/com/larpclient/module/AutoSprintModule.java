package com.larpclient.module;

import com.larpclient.j;
import com.larpclient.q8;
import com.larpclient.sq;
import java.util.Set;
import net.minecraft.class_10185;
import net.minecraft.class_746;

public final class AutoSprintModule extends Module {
   private static AutoSprintModule w;
   private String O = "Waiting for movement";

   public AutoSprintModule() {
      super("Auto Sprint", "Sprint automatically without holding the sprint key", Category.MOVEMENT, -1);
      w = this;
   }

   public static AutoSprintModule S() {
      return w;
   }

   public static class_10185 Y(class_10185 var0) {
      return var0.comp_3165()
         ? var0
         : new class_10185(var0.comp_3159(), var0.comp_3160(), var0.comp_3161(), var0.comp_3162(), var0.comp_3163(), var0.comp_3164(), true);
   }

   public static class_10185 f(class_746 var0, class_10185 var1) {
      if (w != null && w.isEnabled()) {
         AutoSprintModule var2 = w;
         if (var2 != null && var2.isRuntimeActive() && var0 != null) {
            try {
               if (var0 != getMc().field_1724 || !j.k()) {
                  var2.O = "Movement unavailable";
                  return var1;
               } else if (sq.U() != null) {
                  var2.O = "Pause: AutoTunnel is steering";
                  return var1;
               } else if (AuraModule.N()) {
                  var2.O = "Pause: critical sprint release";
                  return var1;
               } else {
                  var2.O = var0.method_5624() ? "Sprinting" : "Waiting for movement";
                  return Y(var1);
               }
            } catch (RuntimeException var4) {
               q8.u("AutoSprint", "Input assistance failed; disabling and keeping native input", var4);
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
      this.O = "Waiting for movement";
      q8.v("AutoSprint", "Enabled: holding sprint input, native sprint eligibility");
   }

   @Override
   public void onDisable() {
      this.O = "Waiting for movement";
      q8.v("AutoSprint", "Disabled: sprint follows the physical key again");
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.Y);
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() ? this.O : "Inactive";
   }
}
