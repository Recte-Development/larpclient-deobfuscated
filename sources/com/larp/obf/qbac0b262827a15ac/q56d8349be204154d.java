package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.NoBlockRotationModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockStateBase.class)
public abstract class q56d8349be204154d {
   @Inject(method = "getSeed(Lnet/minecraft/core/BlockPos;)J", at = @At("HEAD"), cancellable = true)
   private void w(BlockPos var1, CallbackInfoReturnable<Long> var2) {
      if (NoBlockRotationModule.D()) {
         var2.setReturnValue(42L);
      }
   }
}
