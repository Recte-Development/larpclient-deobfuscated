package com.larp.obf.q41a5851bcf8e876a.q040cb7981516518c;

import com.larpclient.at;
import com.larpclient.p7;
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
public abstract class q7359f1c834f004e4 {
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
      this.k = at.j(var1);
      return p7.g(this.k);
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
      at.A(var2, this.k);
   }
}
