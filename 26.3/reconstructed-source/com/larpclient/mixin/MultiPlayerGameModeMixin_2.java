package com.larpclient.mixin;

import com.larpclient.core.ViewController;
import com.larpclient.module.combat.AuraModule;
import com.larpclient.module.player.AutoToolModule;
import com.larpclient.util.misc.InteractionArbiter;
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
public class MultiPlayerGameModeMixin_2 {
   @Shadow
   private void ensureHasSentCarriedItem() {
      throw new AssertionError();
   }

   @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
   private void A(Player var1, Entity var2, CallbackInfo var3) {
      if (!InteractionArbiter.tryManualAttack(var2)) {
         var3.cancel();
      }
   }

   @Inject(method = {"useItemOn", "useItem", "interact"}, at = @At("HEAD"), cancellable = true)
   private void m(CallbackInfoReturnable<InteractionResult> var1) {
      if (AuraModule.isShieldUnpressActive() || !InteractionArbiter.claimManualInteraction()) {
         var1.setReturnValue(InteractionResult.PASS);
      }
   }

   @Inject(method = "piercingAttack", at = @At("HEAD"), cancellable = true)
   private void m(CallbackInfo var1) {
      if (!InteractionArbiter.claimManualInteraction()) {
         var1.cancel();
      }
   }

   @Inject(method = {"startDestroyBlock", "continueDestroyBlock"}, at = @At("HEAD"), cancellable = true)
   private void W(BlockPos var1, Direction var2, CallbackInfoReturnable<Boolean> var3) {
      if (ViewController.isFreecam()) {
         var3.setReturnValue(false);
      } else if (AutoToolModule.restoreSlot(var1)) {
         this.ensureHasSentCarriedItem();
      }
   }
}
