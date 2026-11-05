package com.larp.obf.q5682c6a8aca9b6ec.q1e1bb13db291d59c.q3bbad90b3114bdda;

import com.larpclient.k;
import com.larpclient.yx;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class qf64b1a47237d9f12 {
   @WrapOperation(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;isPausing()Z"))
   private boolean J(Gui var1, Operation<Boolean> var2) {
      return (Boolean)var2.call(new Object[]{var1}) && !k.V();
   }

   @Inject(method = "pauseIfInactive", at = @At("HEAD"), cancellable = true)
   private void T(CallbackInfo var1) {
      if (k.V()) {
         var1.cancel();
      }
   }

   @WrapOperation(method = "continueAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;stopDestroyBlock()V"))
   private void A(MultiPlayerGameMode var1, Operation<Void> var2) {
      if (!yx.C()) {
         var2.call(new Object[]{var1});
      }
   }
}
