package com.larpclient.mixin;

import com.larpclient.module.render.RealisticWaterModule;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public abstract class ScreenEffectRendererMixin2 {
   @Inject(method = "submitWater", at = @At("HEAD"), cancellable = true)
   private static void Q(CallbackInfo var0) {
      if (RealisticWaterModule.isUnderwaterActive()) {
         var0.cancel();
      }
   }
}
