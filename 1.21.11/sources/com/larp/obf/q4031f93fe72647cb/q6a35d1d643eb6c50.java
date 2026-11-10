package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.RemovalsModule;
import net.minecraft.class_1058;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_4603;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_4603.class)
public class q6a35d1d643eb6c50 {
   @Inject(method = "method_23070", at = @At("HEAD"), cancellable = true)
   private static void G(class_4587 var0, class_4597 var1, class_1058 var2, CallbackInfo var3) {
      if (RemovalsModule.o("fire")) {
         var3.cancel();
      }
   }

   @Inject(method = "method_23069", at = @At("HEAD"), cancellable = true)
   private static void n(class_310 var0, class_4587 var1, class_4597 var2, CallbackInfo var3) {
      if (RemovalsModule.o("water")) {
         var3.cancel();
      }
   }

   @Inject(method = "method_23068", at = @At("HEAD"), cancellable = true)
   private static void I(class_1058 var0, class_4587 var1, class_4597 var2, CallbackInfo var3) {
      if (RemovalsModule.o("clip")) {
         var3.cancel();
      }
   }
}
