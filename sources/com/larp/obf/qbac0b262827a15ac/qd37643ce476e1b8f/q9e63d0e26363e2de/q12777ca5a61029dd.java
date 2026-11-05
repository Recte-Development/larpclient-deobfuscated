package com.larp.obf.qbac0b262827a15ac.qd37643ce476e1b8f.q9e63d0e26363e2de;

import com.larpclient.wa;
import com.larpclient.wq;
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
public abstract class q12777ca5a61029dd {
   @WrapOperation(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;isPausing()Z"))
   private boolean m(Gui var1, Operation<Boolean> var2) {
      return (Boolean)var2.call(new Object[]{var1}) && !wq.q();
   }

   @Inject(method = "pauseIfInactive", at = @At("HEAD"), cancellable = true)
   private void x(CallbackInfo var1) {
      if (wq.q()) {
         var1.cancel();
      }
   }

   @WrapOperation(method = "continueAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;stopDestroyBlock()V"))
   private void r(MultiPlayerGameMode var1, Operation<Void> var2) {
      if (!wa.B()) {
         var2.call(new Object[]{var1});
      }
   }
}
