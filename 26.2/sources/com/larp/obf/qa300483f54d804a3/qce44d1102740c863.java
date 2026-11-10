package com.larp.obf.qa300483f54d804a3;

import com.larpclient.p_;
import com.larpclient.ta;
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
public class qce44d1102740c863 {
   @Shadow
   private void ensureHasSentCarriedItem() {
      throw new AssertionError();
   }

   @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
   private void T(Player var1, Entity var2, CallbackInfo var3) {
      if (!ta.R(var2)) {
         var3.cancel();
      }
   }

   @Inject(method = {"useItemOn", "useItem", "interact"}, at = @At("HEAD"), cancellable = true)
   private void i(CallbackInfoReturnable<InteractionResult> var1) {
      if (AuraModule.N() || !ta.O()) {
         var1.setReturnValue(InteractionResult.PASS);
      }
   }

   @Inject(method = "piercingAttack", at = @At("HEAD"), cancellable = true)
   private void I(CallbackInfo var1) {
      if (!ta.O()) {
         var1.cancel();
      }
   }

   @Inject(method = {"startDestroyBlock", "continueDestroyBlock"}, at = @At("HEAD"), cancellable = true)
   private void j(BlockPos var1, Direction var2, CallbackInfoReturnable<Boolean> var3) {
      if (p_.V()) {
         var3.setReturnValue(false);
      } else if (AutoToolModule.C(var1)) {
         this.ensureHasSentCarriedItem();
      }
   }
}
