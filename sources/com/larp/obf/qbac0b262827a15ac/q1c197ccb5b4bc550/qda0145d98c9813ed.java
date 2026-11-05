package com.larp.obf.qbac0b262827a15ac.q1c197ccb5b4bc550;

import com.larpclient.hj;
import com.larpclient.k;
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
public abstract class qda0145d98c9813ed {
   @Unique
   private RenderPipeline d;

   @Redirect(
      method = "render",
      at = @At(
         value = "INVOKE",
         target = "Lcom/mojang/blaze3d/systems/RenderSystem;getCompiledPipeline(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;)Lcom/mojang/renderpearl/api/pipeline/CompiledRenderPipeline;"
      )
   )
   private CompiledRenderPipeline S(RenderPipeline var1) {
      this.d = k.l(var1);
      return hj.h(this.d);
   }

   @Inject(
      method = "render",
      at = @At(
         value = "INVOKE",
         target = "Lcom/mojang/renderpearl/api/commands/RenderPass;setPipeline(Lcom/mojang/renderpearl/api/pipeline/CompiledRenderPipeline;)V",
         shift = Shift.AFTER
      )
   )
   private void t(CallbackInfo var1, @Local(argsOnly = true) RenderPass var2) {
      k.v(var2, this.d);
   }
}
