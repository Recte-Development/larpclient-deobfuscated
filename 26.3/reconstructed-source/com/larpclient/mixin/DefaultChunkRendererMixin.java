package com.larpclient.mixin;

import com.larpclient.render.world.TerrainMaterials;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "net.caffeinemc.mods.sodium.client.render.chunk.DefaultChunkRenderer", remap = false)
public abstract class DefaultChunkRendererMixin {
   @WrapOperation(
      method = "render",
      require = 0,
      remap = false,
      at = @At(value = "INVOKE", target = "Lcom/mojang/renderpearl/api/commands/RenderPass;setPipeline(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;)V")
   )
   private void X(RenderPass var1, RenderPipeline var2, Operation<Void> var3) {
      RenderPipeline var4 = TerrainMaterials.remapSodiumPipeline(var2);
      var3.call(new Object[]{var1, var4});
      TerrainMaterials.bindMaterial(var1, var4);
   }
}
