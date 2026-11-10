package com.larp.obf.qa300483f54d804a3.q4a40bbd05996d213;

import com.larpclient.ae;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class q32cee00a2cbfbe4e {
   @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
   private void t(CallbackInfoReturnable<Boolean> var1) {
      if (ae.d()) {
         ae.T(0);
         var1.setReturnValue(false);
      }
   }

   @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
   private void B(CallbackInfo var1) {
      if (ae.d()) {
         ae.T(1);
         var1.cancel();
      }
   }

   @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
   private void H(boolean var1, CallbackInfo var2) {
      if (ae.d()) {
         var2.cancel();
      }
   }
}
