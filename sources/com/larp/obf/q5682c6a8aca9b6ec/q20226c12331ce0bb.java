package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.AntiTrapModule;
import com.larpclient.module.IgnoreEntityModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public class q20226c12331ce0bb {
   @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
   private void g(LocalPlayer var1, InteractionHand var2, BlockHitResult var3, CallbackInfoReturnable<InteractionResult> var4) {
      ClientLevel var5 = Minecraft.getInstance().level;
      if (var5 != null && AntiTrapModule.V(var5.getBlockState(var3.getBlockPos())) && !var1.isShiftKeyDown()) {
         var4.setReturnValue(InteractionResult.FAIL);
      }
   }

   @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
   private void k(Player var1, Entity var2, EntityHitResult var3, InteractionHand var4, CallbackInfoReturnable<InteractionResult> var5) {
      if (AntiTrapModule.A(var2) || IgnoreEntityModule.G(var2)) {
         var5.setReturnValue(InteractionResult.FAIL);
      }
   }
}
