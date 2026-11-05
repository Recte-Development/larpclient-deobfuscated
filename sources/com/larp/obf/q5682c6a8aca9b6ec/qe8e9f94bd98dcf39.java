package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.d8;
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
public abstract class qe8e9f94bd98dcf39 {
   @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
   private void Z(CallbackInfoReturnable<Boolean> var1) {
      AutoClickerModule var2 = AutoClickerModule.p();
      if (var2 != null && var2.x() && !var2.l() && !d8.N()) {
         var1.setReturnValue(false);
      }
   }

   @Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
   private void z(CallbackInfo var1) {
      AutoClickerModule var2 = AutoClickerModule.p();
      if (var2 != null && var2.N() && !var2.l() && !d8.N()) {
         var1.cancel();
      }
   }

   @WrapMethod(method = "continueAttack")
   private void R(boolean var1, Operation<Void> var2) {
      AutoClickerModule var3 = AutoClickerModule.p();
      if (var3 == null) {
         var2.call(new Object[]{var1});
      } else {
         var3.P(var1x -> var2.call(new Object[]{var1x}), var1);
      }
   }

   @WrapOperation(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;isDown()Z"))
   private boolean e(KeyMapping var1, Operation<Boolean> var2) {
      AutoClickerModule var3 = AutoClickerModule.p();
      return (Boolean)var2.call(new Object[]{var1}) || var1 == Minecraft.getInstance().options.keyUse && var3 != null && var3.i();
   }
}
