package com.larp.obf.q41a5851bcf8e876a.qb12a7b6d04ee2fc9.q6780db403bf6e5d8;

import com.larpclient.hj;
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
public abstract class qf0f86d9e062e6541 {
   @WrapMethod(method = "tesselate")
   private void V(BlockAndTintGetter var1, BlockPos var2, Output var3, BlockState var4, FluidState var5, Operation<Void> var6) {
      if (!hj.O(var1, var2, var5)) {
         var6.call(new Object[]{var1, var2, var3, var4, var5});
      } else {
         try {
            var6.call(new Object[]{var1, var2, var3, var4, var5});
         } finally {
            hj.V();
         }
      }
   }

   @ModifyVariable(method = "vertex", at = @At("HEAD"), argsOnly = true, ordinal = 0)
   private int j(int var1) {
      return hj.e(var1);
   }
}
