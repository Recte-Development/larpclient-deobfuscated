package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.PerformanceModule;
import com.mojang.blaze3d.platform.FramerateLimitTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FramerateLimitTracker.class)
public abstract class qcb73d0bb10407d75 {
   @Inject(method = "getFramerateLimit()I", at = @At("RETURN"), cancellable = true)
   private void R(CallbackInfoReturnable<Integer> var1) {
      var1.setReturnValue(PerformanceModule.a(var1.getReturnValueI()));
   }
}
