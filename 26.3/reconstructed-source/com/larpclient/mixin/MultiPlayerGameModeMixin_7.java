package com.larpclient.mixin;

import com.larpclient.module.combat.CrystalTapModule;
import com.larpclient.module.world.AutoDrainModule;
import com.larpclient.util.entity.CombatUtil;
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
public abstract class MultiPlayerGameModeMixin_7 {
   @Inject(
      method = "useItem(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
      at = @At("HEAD")
   )
   private void k(Player var1, InteractionHand var2, CallbackInfoReturnable<InteractionResult> var3) {
      AutoDrainModule.recordWaterPlaced(var1, var2);
   }

   @Inject(
      method = "useItem(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
      at = @At("RETURN")
   )
   private void o(Player var1, InteractionHand var2, CallbackInfoReturnable<InteractionResult> var3) {
      CombatUtil.markPlayerTicked(var1);
   }

   @Inject(
      method = "useItemOn(Lnet/minecraft/client/player/LocalPlayer;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;",
      at = @At("HEAD")
   )
   private void V(LocalPlayer var1, InteractionHand var2, BlockHitResult var3, CallbackInfoReturnable<InteractionResult> var4) {
      CrystalTapModule.onUseBlock(var1, var2, var3);
   }

   @Inject(
      method = "useItemOn(Lnet/minecraft/client/player/LocalPlayer;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/BlockHitResult;)Lnet/minecraft/world/InteractionResult;",
      at = @At("RETURN")
   )
   private void U(LocalPlayer var1, InteractionHand var2, BlockHitResult var3, CallbackInfoReturnable<InteractionResult> var4) {
      CombatUtil.markPlayerTicked(var1);
      CrystalTapModule.onUseResult((InteractionResult)var4.getReturnValue());
   }

   @Inject(
      method = "interact(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/EntityHitResult;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
      at = @At("RETURN")
   )
   private void t(Player var1, Entity var2, EntityHitResult var3, InteractionHand var4, CallbackInfoReturnable<InteractionResult> var5) {
      CombatUtil.markPlayerTicked(var1);
   }

   @Inject(method = "releaseUsingItem(Lnet/minecraft/world/entity/player/Player;)V", at = @At("RETURN"))
   private void u(Player var1, CallbackInfo var2) {
      CombatUtil.markPlayerTicked(var1);
   }
}
