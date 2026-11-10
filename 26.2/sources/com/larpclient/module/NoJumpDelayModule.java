package com.larpclient.module;

import com.larpclient.c5;
import com.larpclient.p_;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;

public final class NoJumpDelayModule extends Module {
   private static NoJumpDelayModule s;
   private final e P = this.addSetting(new e("Jump Cooldown", "Ticks before a held jump can rearm after a grounded jump", 0.0, 0.0, 10.0, 1.0));

   public NoJumpDelayModule() {
      super("No Jump Delay", "Remove the delay between grounded jumps", Category.MOVEMENT, -1);
      s = this;
   }

   public static int W(int var0) {
      return Math.clamp(var0, 0, 10);
   }

   public static int b(LivingEntity var0, int var1) {
      if (s != null && s.isEnabled()) {
         NoJumpDelayModule var2 = s;
         if (var2 != null && var2.isRuntimeActive()) {
            Minecraft var3 = getMc();
            if (var0 == var3.player
               && var3.player != null
               && c5.J()
               && p_.A() == p_.o.T
               && var3.gui.screen() == null
               && var3.isWindowActive()
               && !var0.isPassenger()
               && !var0.isInWater()
               && !var0.isInLava()
               && !var3.player.getAbilities().flying
               && !var3.player.isFallFlying()) {
               AirStuckModule var4 = AirStuckModule.T();
               ElytraMotionModule var5 = ElytraMotionModule.v();
               FlyModule var6 = FlyModule.S();
               return var4 != null && var4.I() || var5 != null && var5.K() || var6 != null && var6.f() ? var1 : W(var2.P.getValue().intValue());
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
   public Set<m> getNetworkImpacts() {
      return Set.of(m.U);
   }

   @Override
   public void onEnable() {
      com.larpclient.h.U("NoJumpDelay", "Native grounded jump cooldown: {} ticks", this.P.getValue().intValue());
   }

   @Override
   public String getStatus() {
      int var1 = this.P.getValue().intValue();
      return var1 == 0 ? "Ready immediately" : var1 + (var1 == 1 ? " tick cooldown" : " ticks cooldown");
   }
}
