package com.larp.obf.q5682c6a8aca9b6ec.qd12d1a2c4a911e9d;

import com.larpclient.e3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.misterpemodder.shulkerboxtooltip.impl.renderer.BasePreviewRenderer")
public abstract class q67c54874908524c9 {
   @Inject(method = "drawInnerTooltip", at = @At("HEAD"), require = 0)
   private void u(CallbackInfo var1) {
      e3.N(true);
   }

   @Inject(method = "drawInnerTooltip", at = @At("RETURN"), require = 0)
   private void H(CallbackInfo var1) {
      e3.N(false);
   }
}
