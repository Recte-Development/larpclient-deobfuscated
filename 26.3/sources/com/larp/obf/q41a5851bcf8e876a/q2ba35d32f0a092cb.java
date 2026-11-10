package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.SnowGroundModule;
import com.larpclient.module.NoWeatherModule;
import com.larpclient.module.RemovalsModule;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WeatherEffectRenderer.class)
public class q2ba35d32f0a092cb {
   @Inject(method = "render", at = @At("HEAD"), cancellable = true)
   private void B(CallbackInfo var1) {
      if (SnowGroundModule.l() || NoWeatherModule.U() || RemovalsModule.G("weather")) {
         var1.cancel();
      }
   }
}
