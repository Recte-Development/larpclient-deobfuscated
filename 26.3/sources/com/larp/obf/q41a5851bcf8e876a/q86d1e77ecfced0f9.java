package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.XRayModule;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class q86d1e77ecfced0f9 {
   @Inject(method = "shouldRenderFace", at = @At("HEAD"), cancellable = true)
   private static void c(BlockState var0, BlockState var1, Direction var2, CallbackInfoReturnable<Boolean> var3) {
      if (XRayModule.S() && XRayModule.g(var0.getBlock()) && !XRayModule.g(var1.getBlock())) {
         var3.setReturnValue(true);
      }
   }
}
