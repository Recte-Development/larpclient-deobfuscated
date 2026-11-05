package com.larp.obf.qbac0b262827a15ac.qb9781bf98a2db63b;

import com.larpclient.pc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.misterpemodder.shulkerboxtooltip.impl.renderer.BasePreviewRenderer")
public abstract class q1bbd321d2217789d {
   @Inject(method = "drawInnerTooltip", at = @At("HEAD"), require = 0)
   private void L(CallbackInfo var1) {
      pc.U(true);
   }

   @Inject(method = "drawInnerTooltip", at = @At("RETURN"), require = 0)
   private void I(CallbackInfo var1) {
      pc.U(false);
   }
}
