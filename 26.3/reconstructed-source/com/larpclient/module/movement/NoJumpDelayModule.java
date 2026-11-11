package com.larpclient.module.movement;

import com.larpclient.core.ViewController;
import com.larpclient.module.exploit.AirStuckModule;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.entity.PlayerInputUtil;
import com.larpclient.util.misc.LarpLog;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;

public final class NoJumpDelayModule extends Module {
   private static NoJumpDelayModule instance;
   private final NumberSetting jumpCooldown = this.addSetting(
      new NumberSetting("Jump Cooldown", "Ticks before a held jump can rearm after a grounded jump", 0.0, 0.0, 10.0, 1.0)
   );

   public NoJumpDelayModule() {
      super("No Jump Delay", "Remove the delay between grounded jumps", Category.MOVEMENT, -1);
      instance = this;
   }

   public static int clampCooldown(int var0) {
      return Math.clamp(var0, 0, 10);
   }

   public static int getJumpCooldown(LivingEntity var0, int var1) {
      if (instance != null && instance.isEnabled()) {
         NoJumpDelayModule var2 = instance;
         if (var2 != null && var2.isRuntimeActive()) {
            Minecraft var3 = getMc();
            if (var0 == var3.player
               && var3.player != null
               && PlayerInputUtil.isInWorld()
               && ViewController.update() == ViewController.Owner.NONE
               && var3.gui.screen() == null
               && var3.isWindowActive()
               && !var0.isPassenger()
               && !var0.isInWater()
               && !var0.isInLava()
               && !var3.player.getAbilities().flying
               && !var3.player.isFallFlying()) {
               AirStuckModule var4 = AirStuckModule.getInstance();
               ElytraMotionModule var5 = ElytraMotionModule.getInstance();
               FlyModule var6 = FlyModule.getInstance();
               return var4 != null && var4.isHolding() || var5 != null && var5.isHolding() || var6 != null && var6.isFlightActive()
                  ? var1
                  : clampCooldown(var2.jumpCooldown.getValue().intValue());
            } else {
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
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.dONE);
   }

   @Override
   public void onEnable() {
      LarpLog.debug("NoJumpDelay", "Native grounded jump cooldown: {} ticks", this.jumpCooldown.getValue().intValue());
   }

   @Override
   public String getStatus() {
      int var1 = this.jumpCooldown.getValue().intValue();
      return var1 == 0 ? "Ready immediately" : var1 + (var1 == 1 ? " tick cooldown" : " ticks cooldown");
   }
}
