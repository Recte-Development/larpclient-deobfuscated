package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.qg;
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
public abstract class qa1d01ae7c45166db {
   @Inject(
      method = "useItem(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
      at = @At("HEAD")
   )
   private void k(Player var1, InteractionHand var2, CallbackInfoReturnable<InteractionResult> var3) {
      AutoDrainModule.V(var1, var2);
   }

   @Inject(
      method = "useItem(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
      at = @At("RETURN")
   )
   private void o(Player var1, InteractionHand var2, CallbackInfoReturnable<InteractionResult> var3) {
      qg.f(var1);
   }

   @Inject(
      method = "useItemOn(Lnet/minecraft/client/player/LocalPlayer;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;",
      at = @At("HEAD")
   )
   private void V(LocalPlayer var1, InteractionHand var2, BlockHitResult var3, CallbackInfoReturnable<InteractionResult> var4) {
      CrystalTapModule.X(var1, var2, var3);
   }

   @Inject(
      method = "useItemOn(Lnet/minecraft/client/player/LocalPlayer;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;",
      at = @At("RETURN")
   )
   private void U(LocalPlayer var1, InteractionHand var2, BlockHitResult var3, CallbackInfoReturnable<InteractionResult> var4) {
      qg.f(var1);
      CrystalTapModule.j((InteractionResult)var4.getReturnValue());
   }

   @Inject(
      method = "interact(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/EntityHitResult;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
      at = @At("RETURN")
   )
   private void t(Player var1, Entity var2, EntityHitResult var3, InteractionHand var4, CallbackInfoReturnable<InteractionResult> var5) {
      qg.f(var1);
   }

   @Inject(method = "releaseUsingItem(Lnet/minecraft/world/entity/player/Player;)V", at = @At("RETURN"))
   private void u(Player var1, CallbackInfo var2) {
      qg.f(var1);
   }
}
