package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.NoBlockRotationModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockStateBase.class)
public abstract class qddc5cefb5f309a82 {
   @Inject(method = "getSeed(Lnet/minecraft/core/BlockPos;)J", at = @At("HEAD"), cancellable = true)
   private void h(BlockPos var1, CallbackInfoReturnable<Long> var2) {
      if (NoBlockRotationModule.v()) {
         var2.setReturnValue(42L);
      }
   }
}
