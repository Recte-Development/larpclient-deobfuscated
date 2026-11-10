package com.larp.obf.qa300483f54d804a3.q5e09f7d3f142fb91.q82cf814772134aab;

import com.larpclient.af;
import com.larpclient.l0;
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
public abstract class q1e5164448585d91d {
   @WrapOperation(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;isPausing()Z"))
   private boolean k(Gui var1, Operation<Boolean> var2) {
      return (Boolean)var2.call(new Object[]{var1}) && !af.B();
   }

   @Inject(method = "pauseIfInactive", at = @At("HEAD"), cancellable = true)
   private void F(CallbackInfo var1) {
      if (af.B()) {
         var1.cancel();
      }
   }

   @WrapOperation(method = "continueAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;stopDestroyBlock()V"))
   private void Z(MultiPlayerGameMode var1, Operation<Void> var2) {
      if (!l0.g()) {
         l0.I();
         var2.call(new Object[]{var1});
      }
   }
}
