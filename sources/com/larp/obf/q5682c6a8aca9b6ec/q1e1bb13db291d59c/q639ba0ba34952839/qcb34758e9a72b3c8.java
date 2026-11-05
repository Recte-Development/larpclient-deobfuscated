package com.larp.obf.q5682c6a8aca9b6ec.q1e1bb13db291d59c.q639ba0ba34952839;

import com.larpclient.er;
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
public abstract class qcb34758e9a72b3c8 {
   @WrapMethod(method = "tesselate")
   private void w(BlockAndTintGetter var1, BlockPos var2, Output var3, BlockState var4, FluidState var5, Operation<Void> var6) {
      if (!er.f(var1, var2, var5)) {
         var6.call(new Object[]{var1, var2, var3, var4, var5});
      } else {
         try {
            var6.call(new Object[]{var1, var2, var3, var4, var5});
         } finally {
            er.j();
         }
      }
   }

   @ModifyVariable(method = "vertex", at = @At("HEAD"), argsOnly = true, ordinal = 0)
   private int n(int var1) {
      return er.x(var1);
   }
}
