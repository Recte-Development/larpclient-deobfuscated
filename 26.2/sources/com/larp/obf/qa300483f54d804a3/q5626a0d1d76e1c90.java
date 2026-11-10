package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.OrderDropperModule;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class q5626a0d1d76e1c90 {
   @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
   private void Q(CallbackInfo var1) {
      if (OrderDropperModule.C()) {
         var1.cancel();
      }
   }

   @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
   private void C(CallbackInfoReturnable<Boolean> var1) {
      if (OrderDropperModule.C()) {
         var1.setReturnValue(false);
      }
   }

   @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
   private void Y(boolean var1, CallbackInfo var2) {
      if (OrderDropperModule.C()) {
         var2.cancel();
      }
   }
}
