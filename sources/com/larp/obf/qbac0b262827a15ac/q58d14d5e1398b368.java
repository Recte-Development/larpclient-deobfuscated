package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.SnowGroundModule;
import com.larpclient.module.NoWeatherModule;
import com.larpclient.module.RemovalsModule;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WeatherEffectRenderer.class)
public class q58d14d5e1398b368 {
   @Inject(method = "render", at = @At("HEAD"), cancellable = true)
   private void t(CallbackInfo var1) {
      if (SnowGroundModule.K() || NoWeatherModule.F() || RemovalsModule.r("weather")) {
         var1.cancel();
      }
   }
}
