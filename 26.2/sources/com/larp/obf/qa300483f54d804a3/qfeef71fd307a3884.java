package com.larp.obf.qa300483f54d804a3;

import com.larpclient.ta;
import com.larpclient.module.AutoClickerModule;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class qfeef71fd307a3884 {
   @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
   private void q(CallbackInfoReturnable<Boolean> var1) {
      AutoClickerModule var2 = AutoClickerModule.N();
      if (var2 != null && var2.H() && !var2.i() && !ta.d()) {
         var1.setReturnValue(false);
      }
   }

   @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
   private void t(CallbackInfo var1) {
      AutoClickerModule var2 = AutoClickerModule.N();
      if (var2 != null && var2.h() && !var2.i() && !ta.d()) {
         var1.cancel();
      }
   }

   @WrapMethod(method = "continueAttack")
   private void k(boolean var1, Operation<Void> var2) {
      AutoClickerModule var3 = AutoClickerModule.N();
      if (var3 == null) {
         var2.call(new Object[]{var1});
      } else {
         var3.M(var1x -> var2.call(new Object[]{var1x}), var1);
      }
   }

   @WrapOperation(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;isDown()Z"))
   private boolean y(KeyMapping var1, Operation<Boolean> var2) {
      AutoClickerModule var3 = AutoClickerModule.N();
      return (Boolean)var2.call(new Object[]{var1}) || var1 == Minecraft.getInstance().options.keyUse && var3 != null && var3.k();
   }
}
