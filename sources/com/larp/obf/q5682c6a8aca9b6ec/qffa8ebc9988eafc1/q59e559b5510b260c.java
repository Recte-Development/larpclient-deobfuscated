package com.larp.obf.q5682c6a8aca9b6ec.qffa8ebc9988eafc1;

import com.larpclient.of;
import com.larpclient.y3;
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
public abstract class q59e559b5510b260c {
   @Unique
   private RenderPipeline H;

   @Redirect(
      method = "render",
      at = @At(
         value = "INVOKE",
         target = "Lcom/mojang/blaze3d/systems/RenderSystem;getCompiledPipeline(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;)Lcom/mojang/renderpearl/api/pipeline/CompiledRenderPipeline;"
      )
   )
   private CompiledRenderPipeline w(RenderPipeline var1) {
      this.H = y3.a(var1);
      return of.O(this.H);
   }

   @Inject(
      method = "render",
      at = @At(
         value = "INVOKE",
         target = "Lcom/mojang/renderpearl/api/commands/RenderPass;setPipeline(Lcom/mojang/renderpearl/api/pipeline/CompiledRenderPipeline;)V",
         shift = Shift.AFTER
      )
   )
   private void A(CallbackInfo var1, @Local(argsOnly = true) RenderPass var2) {
      y3.g(var2, this.H);
   }
}
