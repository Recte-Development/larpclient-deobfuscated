package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.module.XRayModule;
import com.mojang.blaze3d.vertex.QuadInstance;
import java.util.List;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.BlockQuadOutput;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelBlockRenderer.class)
public abstract class q85d94b13802b1fd4 {
   @Shadow
   private void tesselateFlat(
      BlockQuadOutput var1, float var2, float var3, float var4, List<BlockStateModelPart> var5, BlockAndTintGetter var6, BlockState var7, BlockPos var8
   ) {
   }

   @Inject(method = "tesselateAmbientOcclusion", at = @At("HEAD"), cancellable = true)
   private void C(
      BlockQuadOutput var1,
      float var2,
      float var3,
      float var4,
      List<BlockStateModelPart> var5,
      BlockAndTintGetter var6,
      BlockState var7,
      BlockPos var8,
      CallbackInfo var9
   ) {
      if (XRayModule.q() && XRayModule.c(var7.getBlock())) {
         this.tesselateFlat(var1, var2, var3, var4, var5, var6, var7, var8);
         var9.cancel();
      }
   }

   @ModifyArg(
      method = "tesselateFlat",
      index = 3,
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/block/BlockModelLighter;prepareQuadFlat(Lnet/minecraft/client/renderer/block/BlockAndTintGetter;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;ILnet/minecraft/client/resources/model/geometry/BakedQuad;Lcom/mojang/blaze3d/vertex/QuadInstance;)V"
      )
   )
   private int Y(BlockAndTintGetter var1, BlockState var2, BlockPos var3, int var4, BakedQuad var5, QuadInstance var6) {
      return XRayModule.q() && XRayModule.c(var2.getBlock()) ? 15728880 : var4;
   }
}
