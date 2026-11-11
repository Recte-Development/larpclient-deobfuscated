package com.larpclient.mixin;

import com.larpclient.module.world.AutoAreaMiner;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin5 {
   @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
   private void t(CallbackInfoReturnable<Boolean> var1) {
      if (AutoAreaMiner.isPickingCorners()) {
         AutoAreaMiner.onCornerClick(0);
         var1.setReturnValue(false);
      }
   }

   @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
   private void F(CallbackInfo var1) {
      if (AutoAreaMiner.isPickingCorners()) {
         AutoAreaMiner.onCornerClick(1);
         var1.cancel();
      }
   }

   @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
   private void e(boolean var1, CallbackInfo var2) {
      if (AutoAreaMiner.isPickingCorners()) {
         var2.cancel();
      }
   }
}
