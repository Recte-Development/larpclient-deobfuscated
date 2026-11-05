package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.SnowGroundModule;
import com.larpclient.module.NoWeatherModule;
import com.larpclient.module.RemovalsModule;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WeatherEffectRenderer.class)
public class qc4f2f8e029ac719b {
   @Inject(method = "render", at = @At("HEAD"), cancellable = true)
   private void A(CallbackInfo var1) {
      if (SnowGroundModule.f() || NoWeatherModule.b() || RemovalsModule.s("weather")) {
         var1.cancel();
      }
   }
}
