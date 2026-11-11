package com.larpclient.mixin;

import com.larpclient.module.exploit.NoBlockRotationModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockStateBase.class)
public abstract class BlockBehaviourBlockStateBaseMixin_2 {
   @Inject(method = "getSeed(Lnet/minecraft/core/BlockPos;)J", at = @At("HEAD"), cancellable = true)
   private void h(BlockPos var1, CallbackInfoReturnable<Long> var2) {
      if (NoBlockRotationModule.isSeedAdmitted()) {
         var2.setReturnValue(42L);
      }
   }
}
