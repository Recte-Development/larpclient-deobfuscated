package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.OrderDropperModule;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class qba21c12ed9a1392a {
   @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
   private void i(CallbackInfo var1) {
      if (OrderDropperModule.O()) {
         var1.cancel();
      }
   }

   @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
   private void u(CallbackInfoReturnable<Boolean> var1) {
      if (OrderDropperModule.O()) {
         var1.setReturnValue(false);
      }
   }

   @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
   private void N(boolean var1, CallbackInfo var2) {
      if (OrderDropperModule.O()) {
         var2.cancel();
      }
   }
}
