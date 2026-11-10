package com.larp.obf.qa300483f54d804a3;

import com.larpclient.SnowGroundModule;
import com.larpclient.module.NoWeatherModule;
import com.larpclient.module.RemovalsModule;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WeatherEffectRenderer.class)
public class q896e5abf2433f9b1 {
   @Inject(method = "render", at = @At("HEAD"), cancellable = true)
   private void W(CallbackInfo var1) {
      if (SnowGroundModule.e() || NoWeatherModule.R() || RemovalsModule.n("weather")) {
         var1.cancel();
      }
   }
}
