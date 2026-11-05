package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.PerformanceModule;
import com.mojang.blaze3d.platform.FramerateLimitTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FramerateLimitTracker.class)
public abstract class q8d90983bb1ad5d57 {
   @Inject(method = "getFramerateLimit()I", at = @At("RETURN"), cancellable = true)
   private void c(CallbackInfoReturnable<Integer> var1) {
      var1.setReturnValue(PerformanceModule.i(var1.getReturnValueI()));
   }
}
