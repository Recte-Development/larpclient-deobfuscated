package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.OrderDropperModule;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class qc38d20edb61fc7b7 {
   @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
   private void Q(CallbackInfo var1) {
      if (OrderDropperModule.z()) {
         var1.cancel();
      }
   }

   @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
   private void m(CallbackInfoReturnable<Boolean> var1) {
      if (OrderDropperModule.z()) {
         var1.setReturnValue(false);
      }
   }

   @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
   private void F(boolean var1, CallbackInfo var2) {
      if (OrderDropperModule.z()) {
         var2.cancel();
      }
   }
}
