package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.NoWeatherModule;
import net.minecraft.class_1937;
import net.minecraft.class_638;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_1937.class)
public class qb6d1dd46a10cfe59 {
   @Inject(method = {"method_8430", "method_8478"}, at = @At("HEAD"), cancellable = true)
   private void y(float var1, CallbackInfoReturnable<Float> var2) {
      if (this instanceof class_638 && NoWeatherModule.b()) {
         var2.setReturnValue(0.0F);
      }
   }
}
