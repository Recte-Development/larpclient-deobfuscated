package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.PerformanceModule;
import net.minecraft.class_9919;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_9919.class)
public abstract class q33010c708c3474f9 {
   @Inject(method = "method_61937()I", at = @At("RETURN"), cancellable = true)
   private void f(CallbackInfoReturnable<Integer> var1) {
      var1.setReturnValue(PerformanceModule.B(var1.getReturnValueI()));
   }
}
