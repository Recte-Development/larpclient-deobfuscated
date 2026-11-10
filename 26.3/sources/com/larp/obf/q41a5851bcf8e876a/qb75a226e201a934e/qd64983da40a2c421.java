package com.larp.obf.q41a5851bcf8e876a.qb75a226e201a934e;

import com.larpclient._6;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class qd64983da40a2c421 {
   @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
   private void t(CallbackInfoReturnable<Boolean> var1) {
      if (_6.A()) {
         _6.W(0);
         var1.setReturnValue(false);
      }
   }

   @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
   private void F(CallbackInfo var1) {
      if (_6.A()) {
         _6.W(1);
         var1.cancel();
      }
   }

   @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
   private void e(boolean var1, CallbackInfo var2) {
      if (_6.A()) {
         var2.cancel();
      }
   }
}
