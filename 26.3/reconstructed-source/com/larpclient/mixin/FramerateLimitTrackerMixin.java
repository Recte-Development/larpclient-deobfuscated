package com.larpclient.mixin;

import com.larpclient.module.misc.PerformanceModule;
import com.mojang.blaze3d.platform.FramerateLimitTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FramerateLimitTracker.class)
public abstract class FramerateLimitTrackerMixin {
   @Inject(method = "getFramerateLimit()I", at = @At("RETURN"), cancellable = true)
   private void R(CallbackInfoReturnable<Integer> var1) {
      var1.setReturnValue(PerformanceModule.limitFps(var1.getReturnValueI()));
   }
}
