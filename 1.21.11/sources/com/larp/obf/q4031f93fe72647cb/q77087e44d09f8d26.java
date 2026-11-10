package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.module.OrderDropperModule;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_310.class)
public abstract class q77087e44d09f8d26 {
   @Inject(method = "method_1583", at = @At("HEAD"), cancellable = true)
   private void F(CallbackInfo var1) {
      if (OrderDropperModule.r()) {
         var1.cancel();
      }
   }

   @Inject(method = "method_1536", at = @At("HEAD"), cancellable = true)
   private void U(CallbackInfoReturnable<Boolean> var1) {
      if (OrderDropperModule.r()) {
         var1.setReturnValue(false);
      }
   }

   @Inject(method = "method_1590", at = @At("HEAD"), cancellable = true)
   private void e(boolean var1, CallbackInfo var2) {
      if (OrderDropperModule.r()) {
         var2.cancel();
      }
   }
}
