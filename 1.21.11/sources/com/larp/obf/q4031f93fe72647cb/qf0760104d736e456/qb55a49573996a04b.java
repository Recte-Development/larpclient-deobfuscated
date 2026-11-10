package com.larp.obf.q4031f93fe72647cb.qf0760104d736e456;

import com.larpclient.gb;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com/misterpemodder/shulkerboxtooltip/impl/renderer/BasePreviewRenderer")
public abstract class qb55a49573996a04b {
   @Inject(method = "drawInnerTooltip", at = @At("HEAD"), require = 0)
   private void z(CallbackInfo var1) {
      gb.f(true);
   }

   @Inject(method = "drawInnerTooltip", at = @At("RETURN"), require = 0)
   private void p(CallbackInfo var1) {
      gb.f(false);
   }
}
