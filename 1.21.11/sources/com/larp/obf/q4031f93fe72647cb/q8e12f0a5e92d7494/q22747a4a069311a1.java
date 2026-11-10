package com.larp.obf.q4031f93fe72647cb.q8e12f0a5e92d7494;

import com.larpclient.sh;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(class_310.class)
public abstract class q22747a4a069311a1 {
   @Inject(method = "method_1536", at = @At("HEAD"), cancellable = true)
   private void r(CallbackInfoReturnable<Boolean> var1) {
      if (sh.A()) {
         sh.e(0);
         var1.setReturnValue(false);
      }
   }

   @Inject(method = "method_1583", at = @At("HEAD"), cancellable = true)
   private void E(CallbackInfo var1) {
      if (sh.A()) {
         sh.e(1);
         var1.cancel();
      }
   }

   @Inject(method = "method_1590", at = @At("HEAD"), cancellable = true)
   private void Q(boolean var1, CallbackInfo var2) {
      if (sh.A()) {
         var2.cancel();
      }
   }
}
