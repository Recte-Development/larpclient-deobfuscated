package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.d8;
import com.larpclient.rr;
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
public class q464e2faa97fb2b8e {
   @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
   private void F(Player var1, Entity var2, CallbackInfo var3) {
      if (!d8.F(var2)) {
         var3.cancel();
      }
   }

   @Inject(method = {"useItemOn", "useItem", "interact"}, at = @At("HEAD"), cancellable = true)
   private void l(CallbackInfoReturnable<InteractionResult> var1) {
      if (AuraModule.W() || !d8.O()) {
         var1.setReturnValue(InteractionResult.PASS);
      }
   }

   @Inject(method = "piercingAttack", at = @At("HEAD"), cancellable = true)
   private void f(CallbackInfo var1) {
      if (!d8.O()) {
         var1.cancel();
      }
   }

   @Inject(method = {"startDestroyBlock", "continueDestroyBlock"}, at = @At("HEAD"), cancellable = true)
   private void R(CallbackInfoReturnable<Boolean> var1) {
      if (rr.o()) {
         var1.setReturnValue(false);
      }
   }
}
