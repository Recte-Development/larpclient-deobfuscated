package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.SnowGroundModule;
import com.larpclient.module.NoWeatherModule;
import com.larpclient.module.RemovalsModule;
import net.minecraft.class_9976;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_9976.class)
public class q7be8a5bf6bcad01f {
   @Inject(method = "method_62320", at = @At("HEAD"), cancellable = true)
   private void y(CallbackInfo var1) {
      if (SnowGroundModule.e() || NoWeatherModule.b() || RemovalsModule.o("weather")) {
         var1.cancel();
      }
   }
}
