package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.XRayModule;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class q167ad0c23c9a1c70 {
   @Inject(method = "shouldRenderFace", at = @At("HEAD"), cancellable = true)
   private static void k(BlockState var0, BlockState var1, Direction var2, CallbackInfoReturnable<Boolean> var3) {
      if (XRayModule.q() && XRayModule.c(var0.getBlock()) && !XRayModule.c(var1.getBlock())) {
         var3.setReturnValue(true);
      }
   }
}
