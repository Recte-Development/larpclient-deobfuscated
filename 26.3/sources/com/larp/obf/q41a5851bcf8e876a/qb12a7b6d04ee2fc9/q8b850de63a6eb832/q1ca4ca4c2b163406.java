package com.larp.obf.q41a5851bcf8e876a.qb12a7b6d04ee2fc9.q8b850de63a6eb832;

import com.larpclient._n;
import com.larpclient.vs;
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
public abstract class q1ca4ca4c2b163406 {
   @WrapOperation(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;isPausing()Z"))
   private boolean D(Gui var1, Operation<Boolean> var2) {
      return (Boolean)var2.call(new Object[]{var1}) && !_n.P();
   }

   @Inject(method = "pauseIfInactive", at = @At("HEAD"), cancellable = true)
   private void e(CallbackInfo var1) {
      if (_n.P()) {
         var1.cancel();
      }
   }

   @WrapOperation(method = "continueAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;stopDestroyBlock()V"))
   private void z(MultiPlayerGameMode var1, Operation<Void> var2) {
      if (!vs.Y()) {
         vs.D();
         var2.call(new Object[]{var1});
      }
   }
}
