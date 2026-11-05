package com.larpclient.module;

import com.larpclient.mt;
import com.larpclient.te;
import com.larpclient.tq;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;

public final class NoJumpDelayModule extends Module {
   private static NoJumpDelayModule R;
   private final d9 S = this.addSetting(new d9("Jump Cooldown", "Ticks before a held jump can rearm after a grounded jump", 0.0, 0.0, 10.0, 1.0));

   public NoJumpDelayModule() {
      super("No Jump Delay", "Remove the delay between grounded jumps", Category.MOVEMENT, -1);
      R = this;
   }

   public static int C(int var0) {
      return Math.clamp(var0, 0, 10);
   }

   public static int T(LivingEntity var0, int var1) {
      if (R != null && R.isEnabled()) {
         NoJumpDelayModule var2 = R;
         if (var2 != null && var2.isRuntimeActive()) {
            Minecraft var3 = getMc();
            if (var0 == var3.player
               && var3.player != null
               && te.i()
               && tq.l() == tq.y.A
               && var3.gui.screen() == null
               && var3.isWindowActive()
               && !var0.isPassenger()
               && !var0.isInWater()
               && !var0.isInLava()
               && !var3.player.getAbilities().flying
               && !var3.player.isFallFlying()) {
               AirStuckModule var4 = AirStuckModule.X();
               ElytraMotionModule var5 = ElytraMotionModule.d();
               FlyModule var6 = FlyModule.d();
               return var4 != null && var4.U() || var5 != null && var5.z() || var6 != null && var6.s() ? var1 : C(var2.S.P().intValue());
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
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.j);
   }

   @Override
   public void onEnable() {
      mt.U("NoJumpDelay", "Native grounded jump cooldown: {} ticks", this.S.P().intValue());
   }

   @Override
   public String getStatus() {
      int var1 = this.S.P().intValue();
      return var1 == 0 ? "Ready immediately" : var1 + (var1 == 1 ? " tick cooldown" : " ticks cooldown");
   }
}
