package com.larpclient.module;

import com.larpclient.r4;
import com.larpclient.rr;
import com.larpclient.yu;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;

public final class NoJumpDelayModule extends Module {
   private static NoJumpDelayModule D;
   private final lc W = this.addSetting(new lc("Jump Cooldown", "Ticks before a held jump can rearm after a grounded jump", 0.0, 0.0, 10.0, 1.0));

   public NoJumpDelayModule() {
      super("No Jump Delay", "Remove the delay between grounded jumps", Category.MOVEMENT, -1);
      D = this;
   }

   public static int j(int var0) {
      return Math.clamp(var0, 0, 10);
   }

   public static int o(LivingEntity var0, int var1) {
      if (D != null && D.isEnabled()) {
         NoJumpDelayModule var2 = D;
         if (var2 != null && var2.isRuntimeActive()) {
            Minecraft var3 = getMc();
            if (var0 == var3.player
               && var3.player != null
               && yu.r()
               && rr.u() == rr.f.i
               && var3.gui.screen() == null
               && var3.isWindowActive()
               && !var0.isPassenger()
               && !var0.isInWater()
               && !var0.isInLava()
               && !var3.player.getAbilities().flying
               && !var3.player.isFallFlying()) {
               AirStuckModule var4 = AirStuckModule.H();
               ElytraMotionModule var5 = ElytraMotionModule.S();
               FlyModule var6 = FlyModule.s();
               return var4 != null && var4.D() || var5 != null && var5.m() || var6 != null && var6.E() ? var1 : j(var2.W.getValue().intValue());
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
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.c);
   }

   @Override
   public void onEnable() {
      r4.g("NoJumpDelay", "Native grounded jump cooldown: {} ticks", this.W.getValue().intValue());
   }

   @Override
   public String getStatus() {
      int var1 = this.W.getValue().intValue();
      return var1 == 0 ? "Ready immediately" : var1 + (var1 == 1 ? " tick cooldown" : " ticks cooldown");
   }
}
