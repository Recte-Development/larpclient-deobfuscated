package com.larp.obf.q41a5851bcf8e876a.q1342f79d4278119d;

import com.larpclient.hc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.misterpemodder.shulkerboxtooltip.impl.renderer.BasePreviewRenderer")
public abstract class q9d7fcb40459e7c63 {
   @Inject(method = "drawInnerTooltip", at = @At("HEAD"), require = 0)
   private void w(CallbackInfo var1) {
      hc.P(true);
   }

   @Inject(method = "drawInnerTooltip", at = @At("RETURN"), require = 0)
   private void u(CallbackInfo var1) {
      hc.P(false);
   }
}
