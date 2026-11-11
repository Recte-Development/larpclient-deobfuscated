package com.larpclient.mixin;

import com.larpclient.module.player.OrderDropperModule;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin_2 {
   @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
   private void Q(CallbackInfo var1) {
      if (OrderDropperModule.skipShulker()) {
         var1.cancel();
      }
   }

   @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
   private void m(CallbackInfoReturnable<Boolean> var1) {
      if (OrderDropperModule.skipShulker()) {
         var1.setReturnValue(false);
      }
   }

   @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
   private void F(boolean var1, CallbackInfo var2) {
      if (OrderDropperModule.skipShulker()) {
         var2.cancel();
      }
   }
}
