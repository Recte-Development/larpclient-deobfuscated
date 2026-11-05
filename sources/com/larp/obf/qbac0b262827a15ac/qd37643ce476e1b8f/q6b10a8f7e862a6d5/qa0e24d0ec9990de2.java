package com.larp.obf.qbac0b262827a15ac.qd37643ce476e1b8f.q6b10a8f7e862a6d5;

import com.larpclient.em;
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
public abstract class qa0e24d0ec9990de2 {
   @WrapMethod(method = "tesselate")
   private void b(BlockAndTintGetter var1, BlockPos var2, Output var3, BlockState var4, FluidState var5, Operation<Void> var6) {
      if (!em.U(var1, var2, var5)) {
         var6.call(new Object[]{var1, var2, var3, var4, var5});
      } else {
         try {
            var6.call(new Object[]{var1, var2, var3, var4, var5});
         } finally {
            em.k();
         }
      }
   }

   @ModifyVariable(method = "vertex", at = @At("HEAD"), argsOnly = true, ordinal = 0)
   private int f(int var1) {
      return em.o(var1);
   }
}
