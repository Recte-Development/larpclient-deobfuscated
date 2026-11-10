package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.e_;
import com.larpclient.module.AimAssistModule;
import com.larpclient.module.GrimSpeedModule;
import com.larpclient.module.SpeedModule;
import com.larpclient.module.TargetStrafeModule;
import com.larpclient.module.WTapModule;
import net.minecraft.class_1297;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_1297.class)
public class q9cd26b18a2be4ed3 {
   @Inject(
      method = {"method_30634(DDD)V", "method_5808(DDDFF)V", "method_74862(Lnet/minecraft/class_10182;Lnet/minecraft/class_10182;Ljava/util/Set;)V"},
      at = @At("RETURN")
   )
   private void b(CallbackInfo var1) {
      if (this instanceof class_746 var2) {
         SpeedModule.c(var2);
         TargetStrafeModule.r(var2);
         GrimSpeedModule.u(var2);
         AimAssistModule.n(var2);
         WTapModule.i(var2);
         e_.Y();
      }
   }
}
