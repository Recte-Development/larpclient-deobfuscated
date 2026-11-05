package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.XRayModule;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class q09873a7020cab427 {
   @Inject(method = "shouldRenderFace", at = @At("HEAD"), cancellable = true)
   private static void V(BlockState var0, BlockState var1, Direction var2, CallbackInfoReturnable<Boolean> var3) {
      if (XRayModule.m() && XRayModule.z(var0.getBlock()) && !XRayModule.z(var1.getBlock())) {
         var3.setReturnValue(true);
      }
   }
}
