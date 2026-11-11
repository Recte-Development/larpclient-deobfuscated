package com.larpclient.mixin;

import com.larpclient.module.render.XRayModule;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockMixin {
   @Inject(method = "shouldRenderFace", at = @At("HEAD"), cancellable = true)
   private static void c(BlockState var0, BlockState var1, Direction var2, CallbackInfoReturnable<Boolean> var3) {
      if (XRayModule.isTerrainSeeThrough() && XRayModule.isVisibleBlock(var0.getBlock()) && !XRayModule.isVisibleBlock(var1.getBlock())) {
         var3.setReturnValue(true);
      }
   }
}
