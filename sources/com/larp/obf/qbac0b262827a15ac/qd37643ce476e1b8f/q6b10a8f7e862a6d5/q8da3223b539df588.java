package com.larp.obf.qbac0b262827a15ac.qd37643ce476e1b8f.q6b10a8f7e862a6d5;

import com.larpclient.em;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.BlockQuadOutput;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SectionCompiler.class)
public abstract class q8da3223b539df588 {
   @WrapOperation(
      method = "compile",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/block/ModelBlockRenderer;tesselateBlock(Lnet/minecraft/client/renderer/block/BlockQuadOutput;FFFLnet/minecraft/client/renderer/block/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/client/renderer/block/dispatch/BlockStateModel;J)V"
      )
   )
   private void J(
      ModelBlockRenderer var1,
      BlockQuadOutput var2,
      float var3,
      float var4,
      float var5,
      BlockAndTintGetter var6,
      BlockPos var7,
      BlockState var8,
      BlockStateModel var9,
      long var10,
      Operation<Void> var12
   ) {
      if (!em.x(var8, var3, var4, var5, var6, var7)) {
         var12.call(new Object[]{var1, var2, var3, var4, var5, var6, var7, var8, var9, var10});
      } else {
         try {
            var12.call(new Object[]{var1, var2, var3, var4, var5, var6, var7, var8, var9, var10});
         } finally {
            em.s();
         }
      }
   }
}
