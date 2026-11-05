package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.NoBlockRotationModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockStateBase.class)
public abstract class qf10892c00cad3a6e {
   @Inject(method = "getSeed(Lnet/minecraft/core/BlockPos;)J", at = @At("HEAD"), cancellable = true)
   private void W(BlockPos var1, CallbackInfoReturnable<Long> var2) {
      if (NoBlockRotationModule.F()) {
         var2.setReturnValue(42L);
      }
   }
}
