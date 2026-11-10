package com.larp.obf.qa300483f54d804a3.qcee37ce2501e1d45;

import com.larpclient.ji;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.misterpemodder.shulkerboxtooltip.impl.renderer.BasePreviewRenderer")
public abstract class qf40e0a74ded3bd75 {
   @Inject(method = "drawInnerTooltip", at = @At("HEAD"), require = 0)
   private void V(CallbackInfo var1) {
      ji.v(true);
   }

   @Inject(method = "drawInnerTooltip", at = @At("RETURN"), require = 0)
   private void p(CallbackInfo var1) {
      ji.v(false);
   }
}
