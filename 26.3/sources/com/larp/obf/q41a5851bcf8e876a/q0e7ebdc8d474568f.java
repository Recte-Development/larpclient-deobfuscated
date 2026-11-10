package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient._p;
import com.larpclient.ij;
import com.larpclient.module.AuraModule;
import com.larpclient.module.AutoToolModule;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class q0e7ebdc8d474568f {
   @Shadow
   private void ensureHasSentCarriedItem() {
      throw new AssertionError();
   }

   @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
   private void A(Player var1, Entity var2, CallbackInfo var3) {
      if (!_p.a(var2)) {
         var3.cancel();
      }
   }

   @Inject(method = {"useItemOn", "useItem", "interact"}, at = @At("HEAD"), cancellable = true)
   private void m(CallbackInfoReturnable<InteractionResult> var1) {
      if (AuraModule.K() || !_p.y()) {
         var1.setReturnValue(InteractionResult.PASS);
      }
   }

   @Inject(method = "piercingAttack", at = @At("HEAD"), cancellable = true)
   private void m(CallbackInfo var1) {
      if (!_p.y()) {
         var1.cancel();
      }
   }

   @Inject(method = {"startDestroyBlock", "continueDestroyBlock"}, at = @At("HEAD"), cancellable = true)
   private void W(BlockPos var1, Direction var2, CallbackInfoReturnable<Boolean> var3) {
      if (ij.y()) {
         var3.setReturnValue(false);
      } else if (AutoToolModule.h(var1)) {
         this.ensureHasSentCarriedItem();
      }
   }
}
