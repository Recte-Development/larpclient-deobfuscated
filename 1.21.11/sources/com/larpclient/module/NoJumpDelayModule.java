package com.larpclient.module;

import com.larpclient.j;
import com.larpclient.q8;
import com.larpclient.wp;
import java.util.Set;
import net.minecraft.class_1309;
import net.minecraft.class_310;

public final class NoJumpDelayModule extends Module {
   private static NoJumpDelayModule W;
   private final lo L = this.addSetting(new lo("Jump Cooldown", "Ticks before a held jump can rearm after a grounded jump", 0.0, 0.0, 10.0, 1.0));

   public NoJumpDelayModule() {
      super("No Jump Delay", "Remove the delay between grounded jumps", Category.MOVEMENT, -1);
      W = this;
   }

   public static int l(int var0) {
      return Math.clamp(var0, 0, 10);
   }

   public static int o(class_1309 var0, int var1) {
      if (W != null && W.isEnabled()) {
         NoJumpDelayModule var2 = W;
         if (var2 != null && var2.isRuntimeActive()) {
            class_310 var3 = getMc();
            if (var0 == var3.field_1724
               && var3.field_1724 != null
               && j.k()
               && wp.o() == wp.z.D
               && var3.field_1755 == null
               && var3.method_1569()
               && !var0.method_5765()
               && !var0.method_5799()
               && !var0.method_5771()
               && !var3.field_1724.method_31549().field_7479
               && !var3.field_1724.method_6128()) {
               AirStuckModule var4 = AirStuckModule.U();
               ElytraMotionModule var5 = ElytraMotionModule.N();
               FlyModule var6 = FlyModule.W();
               return (var4 == null || !var4.E()) && (var5 == null || !var5.k()) && (var6 == null || !var6.P()) ? l(var2.L.getValue().intValue()) : var1;
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
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.F);
   }

   @Override
   public void onEnable() {
      q8.v("NoJumpDelay", "Native grounded jump cooldown: {} ticks", this.L.getValue().intValue());
   }

   @Override
   public String getStatus() {
      int var1 = this.L.getValue().intValue();
      return var1 == 0 ? "Ready immediately" : var1 + (var1 == 1 ? " tick cooldown" : " ticks cooldown");
   }
}
