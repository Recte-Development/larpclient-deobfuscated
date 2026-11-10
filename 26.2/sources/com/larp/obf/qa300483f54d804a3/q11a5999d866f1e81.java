package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.PerformanceModule;
import com.mojang.blaze3d.platform.FramerateLimitTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FramerateLimitTracker.class)
public abstract class q11a5999d866f1e81 {
   @Inject(method = "getFramerateLimit()I", at = @At("RETURN"), cancellable = true)
   private void G(CallbackInfoReturnable<Integer> var1) {
      var1.setReturnValue(PerformanceModule.h(var1.getReturnValueI()));
   }
}
