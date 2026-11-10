package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.HitFXModule;
import net.minecraft.class_1297;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_746.class)
public class qbc8284ccd8969adc {
   @Inject(method = {"method_7277", "method_7304"}, at = @At("HEAD"), cancellable = true)
   private void s(class_1297 var1, CallbackInfo var2) {
      HitFXModule var3 = HitFXModule.H();
      if (var3 != null && var3.D()) {
         var2.cancel();
      }
   }
}
