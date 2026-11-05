package com.larp.obf.qbac0b262827a15ac;

import com.larpclient._m;
import com.larpclient.tq;
import com.larpclient.module.AuraModule;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class qb4db0f196f8596c7 {
   @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
   private void f(Player var1, Entity var2, CallbackInfo var3) {
      if (!_m.m(var2)) {
         var3.cancel();
      }
   }

   @Inject(method = {"useItemOn", "useItem", "interact"}, at = @At("HEAD"), cancellable = true)
   private void g(CallbackInfoReturnable<InteractionResult> var1) {
      if (AuraModule.i() || !_m.f()) {
         var1.setReturnValue(InteractionResult.PASS);
      }
   }

   @Inject(method = "piercingAttack", at = @At("HEAD"), cancellable = true)
   private void M(CallbackInfo var1) {
      if (!_m.f()) {
         var1.cancel();
      }
   }

   @Inject(method = {"startDestroyBlock", "continueDestroyBlock"}, at = @At("HEAD"), cancellable = true)
   private void o(CallbackInfoReturnable<Boolean> var1) {
      if (tq.f()) {
         var1.setReturnValue(false);
      }
   }
}
