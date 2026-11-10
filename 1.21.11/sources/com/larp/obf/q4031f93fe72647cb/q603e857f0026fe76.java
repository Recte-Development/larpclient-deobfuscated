package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.wp;
import com.larpclient.module.CustomCrosshairModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.class_329;
import net.minecraft.class_332;
import net.minecraft.class_9779;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_329.class)
public class q603e857f0026fe76 {
   @ModifyExpressionValue(method = "method_1736", at = @At(value = "INVOKE", target = "Lnet/minecraft/class_5498;method_31034()Z"))
   private boolean P(boolean var1) {
      return wp.d() || var1;
   }

   @Inject(method = "method_1736", at = @At("HEAD"), cancellable = true)
   private void n(class_332 var1, class_9779 var2, CallbackInfo var3) {
      if (CustomCrosshairModule.y()) {
         var3.cancel();
      }
   }
}
