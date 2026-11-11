package com.larpclient.module.movement;

import com.larpclient.module.combat.AuraModule;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.world.AutoTunnelModule;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.entity.PlayerInputUtil;
import com.larpclient.util.misc.LarpLog;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Input;

public final class AutoSprintModule extends Module {
   private static AutoSprintModule instance;
   private String status = "Waiting for movement";

   public AutoSprintModule() {
      super("Auto Sprint", "Sprint automatically without holding the sprint key", Category.MOVEMENT, -1);
      instance = this;
   }

   public static AutoSprintModule getInstance() {
      return instance;
   }

   public static Input forceSprint(Input var0) {
      return var0.sprint() ? var0 : new Input(var0.forward(), var0.backward(), var0.left(), var0.right(), var0.jump(), var0.shift(), true);
   }

   public static Input applyInput(LocalPlayer var0, Input var1) {
      if (instance != null && instance.isEnabled()) {
         AutoSprintModule var2 = instance;
         if (var2 != null && var2.isRuntimeActive() && var0 != null) {
            try {
               if (var0 != getMc().player || !PlayerInputUtil.isInWorld()) {
                  var2.status = "Movement unavailable";
                  return var1;
               } else if (AutoTunnelModule.isActive() != null) {
                  var2.status = "Pause: AutoTunnel is steering";
                  return var1;
               } else if (AuraModule.isSprintResetActive()) {
                  var2.status = "Pause: critical sprint release";
                  return var1;
               } else {
                  var2.status = var0.isSprinting() ? "Sprinting" : "Waiting for movement";
                  return forceSprint(var1);
               }
            } catch (RuntimeException var4) {
               LarpLog.error("AutoSprint", "Input assistance failed; disabling and keeping native input", var4);
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
      this.status = "Waiting for movement";
      LarpLog.debug("AutoSprint", "Enabled: holding sprint input, native sprint eligibility");
   }

   @Override
   public void onDisable() {
      this.status = "Waiting for movement";
      LarpLog.debug("AutoSprint", "Disabled: sprint follows the physical key again");
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.sCANNING);
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() ? this.status : "Inactive";
   }
}
