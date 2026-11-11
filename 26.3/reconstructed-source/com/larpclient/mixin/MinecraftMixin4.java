package com.larpclient.mixin;

import com.larpclient.module.world.AutoTunnelModule;
import com.larpclient.util.world.BlockBreaker;
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
public abstract class MinecraftMixin4 {
   @WrapOperation(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;isPausing()Z"))
   private boolean D(Gui var1, Operation<Boolean> var2) {
      return (Boolean)var2.call(new Object[]{var1}) && !AutoTunnelModule.isActive();
   }

   @Inject(method = "pauseIfInactive", at = @At("HEAD"), cancellable = true)
   private void e(CallbackInfo var1) {
      if (AutoTunnelModule.isActive()) {
         var1.cancel();
      }
   }

   @WrapOperation(method = "continueAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;stopDestroyBlock()V"))
   private void z(MultiPlayerGameMode var1, Operation<Void> var2) {
      if (!BlockBreaker.isBreaking()) {
         BlockBreaker.traceVanillaAbort();
         var2.call(new Object[]{var1});
      }
   }
}
