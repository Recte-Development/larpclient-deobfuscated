package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.Feature3DUIModule;
import net.minecraft.class_329;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_329.class)
public abstract class qbece2c44320cad4e {
   @Inject(method = "method_1753", at = @At("HEAD"), cancellable = true)
   private void e(CallbackInfo var1) {
      if (Feature3DUIModule.e()) {
         var1.cancel();
      }
   }
}
