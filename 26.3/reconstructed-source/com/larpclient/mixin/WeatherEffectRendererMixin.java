package com.larpclient.mixin;

import com.larpclient.module.render.NoWeatherModule;
import com.larpclient.module.render.RemovalsModule;
import com.larpclient.module.render.SnowGroundModule;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WeatherEffectRenderer.class)
public class WeatherEffectRendererMixin {
   @Inject(method = "render", at = @At("HEAD"), cancellable = true)
   private void B(CallbackInfo var1) {
      if (SnowGroundModule.hasVisibleSnow() || NoWeatherModule.isActive() || RemovalsModule.isRemovalEnabled("weather")) {
         var1.cancel();
      }
   }
}
