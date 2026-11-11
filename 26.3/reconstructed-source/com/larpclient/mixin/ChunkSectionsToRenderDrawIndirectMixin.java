package com.larpclient.mixin;

import com.larpclient.render.misc.RenderPipelineCompat;
import com.larpclient.render.world.TerrainMaterials;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.CompiledRenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.minecraft.client.renderer.chunk.ChunkSectionsToRender.DrawIndirect;
import net.minecraft.client.renderer.chunk.ChunkSectionsToRender.DrawSeparate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({DrawIndirect.class, DrawSeparate.class})
public abstract class ChunkSectionsToRenderDrawIndirectMixin {
   @Unique
   private RenderPipeline k;

   @Redirect(
      method = "render",
      at = @At(
         value = "INVOKE",
         target = "Lcom/mojang/blaze3d/systems/RenderSystem;getCompiledPipeline(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;)Lcom/mojang/renderpearl/api/pipeline/CompiledRenderPipeline;"
      )
   )
   private CompiledRenderPipeline N(RenderPipeline var1) {
      this.k = TerrainMaterials.remapTerrainPipeline(var1);
      return RenderPipelineCompat.getCompiledPipeline(this.k);
   }

   @Inject(
      method = "render",
      at = @At(
         value = "INVOKE",
         target = "Lcom/mojang/renderpearl/api/commands/RenderPass;setPipeline(Lcom/mojang/renderpearl/api/pipeline/CompiledRenderPipeline;)V",
         shift = Shift.AFTER
      )
   )
   private void s(CallbackInfo var1, @Local(argsOnly = true) RenderPass var2) {
      TerrainMaterials.bindMaterial(var2, this.k);
   }
}
