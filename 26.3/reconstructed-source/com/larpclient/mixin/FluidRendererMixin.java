package com.larpclient.mixin;

import com.larpclient.render.world.BlockRenderTags;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.FluidRenderer;
import net.minecraft.client.renderer.block.FluidRenderer.Output;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FluidRenderer.class)
public abstract class FluidRendererMixin {
   @WrapMethod(method = "tesselate")
   private void V(BlockAndTintGetter var1, BlockPos var2, Output var3, BlockState var4, FluidState var5, Operation<Void> var6) {
      if (!BlockRenderTags.prepareFluid(var1, var2, var5)) {
         var6.call(new Object[]{var1, var2, var3, var4, var5});
      } else {
         try {
            var6.call(new Object[]{var1, var2, var3, var4, var5});
         } finally {
            BlockRenderTags.clearFluid();
         }
      }
   }

   @ModifyVariable(method = "vertex", at = @At("HEAD"), argsOnly = true, ordinal = 0)
   private int j(int var1) {
      return BlockRenderTags.tagWaterColor(var1);
   }
}
