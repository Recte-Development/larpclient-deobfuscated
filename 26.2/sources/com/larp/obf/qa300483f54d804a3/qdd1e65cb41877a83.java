package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.NoBlockRotationModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockStateBase.class)
public abstract class qdd1e65cb41877a83 {
   @Inject(method = "getSeed(Lnet/minecraft/core/BlockPos;)J", at = @At("HEAD"), cancellable = true)
   private void T(BlockPos var1, CallbackInfoReturnable<Long> var2) {
      if (NoBlockRotationModule.R()) {
         var2.setReturnValue(42L);
      }
   }
}
