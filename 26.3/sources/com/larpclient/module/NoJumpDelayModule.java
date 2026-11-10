package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.ij;
import com.larpclient.nz;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;

public final class NoJumpDelayModule extends Module {
   private static NoJumpDelayModule M;
   private final b o = this.addSetting(new b("Jump Cooldown", "Ticks before a held jump can rearm after a grounded jump", 0.0, 0.0, 10.0, 1.0));

   public NoJumpDelayModule() {
      super("No Jump Delay", "Remove the delay between grounded jumps", Category.MOVEMENT, -1);
      M = this;
   }

   public static int f(int var0) {
      return Math.clamp(var0, 0, 10);
   }

   public static int k(LivingEntity var0, int var1) {
      if (M != null && M.isEnabled()) {
         NoJumpDelayModule var2 = M;
         if (var2 != null && var2.isRuntimeActive()) {
            Minecraft var3 = getMc();
            if (var0 == var3.player
               && var3.player != null
               && nz.v()
               && ij.L() == ij.r.X
               && var3.gui.screen() == null
               && var3.isWindowActive()
               && !var0.isPassenger()
               && !var0.isInWater()
               && !var0.isInLava()
               && !var3.player.getAbilities().flying
               && !var3.player.isFallFlying()) {
               AirStuckModule var4 = AirStuckModule.Z();
               ElytraMotionModule var5 = ElytraMotionModule.n();
               FlyModule var6 = FlyModule.Q();
               return var4 != null && var4.J() || var5 != null && var5.Q() || var6 != null && var6.r() ? var1 : f(var2.o.getValue().intValue());
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
   public Set<l> getNetworkImpacts() {
      return Set.of(l.k);
   }

   @Override
   public void onEnable() {
      _q.c("NoJumpDelay", "Native grounded jump cooldown: {} ticks", this.o.getValue().intValue());
   }

   @Override
   public String getStatus() {
      int var1 = this.o.getValue().intValue();
      return var1 == 0 ? "Ready immediately" : var1 + (var1 == 1 ? " tick cooldown" : " ticks cooldown");
   }
}
