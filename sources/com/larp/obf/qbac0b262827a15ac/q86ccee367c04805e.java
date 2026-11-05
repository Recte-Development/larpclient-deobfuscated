package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.PerformanceModule;
import com.mojang.blaze3d.platform.FramerateLimitTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FramerateLimitTracker.class)
public abstract class q86ccee367c04805e {
   @Inject(method = "getFramerateLimit()I", at = @At("RETURN"), cancellable = true)
   private void H(CallbackInfoReturnable<Integer> var1) {
      var1.setReturnValue(PerformanceModule.G(var1.getReturnValueI()));
   }
}
