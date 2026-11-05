package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.OrderDropperModule;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class q7b3626be01fa962b {
   @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
   private void a(CallbackInfo var1) {
      if (OrderDropperModule.M()) {
         var1.cancel();
      }
   }

   @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
   private void k(CallbackInfoReturnable<Boolean> var1) {
      if (OrderDropperModule.M()) {
         var1.setReturnValue(false);
      }
   }

   @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
   private void M(boolean var1, CallbackInfo var2) {
      if (OrderDropperModule.M()) {
         var2.cancel();
      }
   }
}
