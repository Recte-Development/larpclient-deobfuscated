package com.larp.obf.qa300483f54d804a3;

import com.larpclient.si;
import com.larpclient.module.AutoDrainModule;
import com.larpclient.module.CrystalTapModule;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public abstract class q8e8a837375752853 {
   @Inject(
      method = "useItem(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
      at = @At("HEAD")
   )
   private void D(Player var1, InteractionHand var2, CallbackInfoReturnable<InteractionResult> var3) {
      AutoDrainModule.k(var1, var2);
   }

   @Inject(
      method = "useItem(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
      at = @At("RETURN")
   )
   private void A(Player var1, InteractionHand var2, CallbackInfoReturnable<InteractionResult> var3) {
      si.o(var1);
   }

   @Inject(
      method = "useItemOn(Lnet/minecraft/client/player/LocalPlayer;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;",
      at = @At("HEAD")
   )
   private void O(LocalPlayer var1, InteractionHand var2, BlockHitResult var3, CallbackInfoReturnable<InteractionResult> var4) {
      CrystalTapModule.N(var1, var2, var3);
   }

   @Inject(
      method = "useItemOn(Lnet/minecraft/client/player/LocalPlayer;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;",
      at = @At("RETURN")
   )
   private void D(LocalPlayer var1, InteractionHand var2, BlockHitResult var3, CallbackInfoReturnable<InteractionResult> var4) {
      si.o(var1);
      CrystalTapModule.O((InteractionResult)var4.getReturnValue());
   }

   @Inject(
      method = "interact(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/EntityHitResult;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
      at = @At("RETURN")
   )
   private void P(Player var1, Entity var2, EntityHitResult var3, InteractionHand var4, CallbackInfoReturnable<InteractionResult> var5) {
      si.o(var1);
   }

   @Inject(method = "releaseUsingItem(Lnet/minecraft/world/entity/player/Player;)V", at = @At("RETURN"))
   private void O(Player var1, CallbackInfo var2) {
      si.o(var1);
   }
}
