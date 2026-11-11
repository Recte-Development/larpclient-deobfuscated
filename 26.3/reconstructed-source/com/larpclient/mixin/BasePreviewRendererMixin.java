package com.larpclient.mixin;

import com.larpclient.util.data.GlobalFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.misterpemodder.shulkerboxtooltip.impl.renderer.BasePreviewRenderer")
public abstract class BasePreviewRendererMixin {
   @Inject(method = "drawInnerTooltip", at = @At("HEAD"), require = 0)
   private void w(CallbackInfo var1) {
      GlobalFlag.setActive(true);
   }

   @Inject(method = "drawInnerTooltip", at = @At("RETURN"), require = 0)
   private void u(CallbackInfo var1) {
      GlobalFlag.setActive(false);
   }
}
