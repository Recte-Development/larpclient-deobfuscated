package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.XRayModule;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class qdb083752544d4d5d {
   @Inject(method = "shouldRenderFace", at = @At("HEAD"), cancellable = true)
   private static void n(BlockState var0, BlockState var1, Direction var2, CallbackInfoReturnable<Boolean> var3) {
      if (XRayModule.z() && XRayModule.Z(var0.getBlock()) && !XRayModule.Z(var1.getBlock())) {
         var3.setReturnValue(true);
      }
   }
}
