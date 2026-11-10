package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.XRayModule;
import net.minecraft.class_2248;
import net.minecraft.class_2350;
import net.minecraft.class_2680;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_2248.class)
public class q91963ae435e2a8c6 {
   @Inject(method = "method_9607", at = @At("HEAD"), cancellable = true)
   private static void n(class_2680 var0, class_2680 var1, class_2350 var2, CallbackInfoReturnable<Boolean> var3) {
      if (XRayModule.m() && XRayModule.r(var0.method_26204()) && !XRayModule.r(var1.method_26204())) {
         var3.setReturnValue(true);
      }
   }
}
