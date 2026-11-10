package com.larp.obf.q41a5851bcf8e876a.q040cb7981516518c;

import com.larpclient.ob;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.ShaderSource;
import com.mojang.renderpearl.api.pipeline.CompiledRenderPipeline.Pending;
import com.mojang.renderpearl.frontend.FrontendGpuDevice;
import com.mojang.renderpearl.frontend.shaders.PipelineBuilder;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FrontendGpuDevice.class)
public abstract class q91a8c354c63e319f {
   @WrapOperation(
      method = "compilePipeline",
      at = @At(
         value = "INVOKE",
         target = "Lcom/mojang/renderpearl/frontend/shaders/PipelineBuilder;compilePipeline(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;Lcom/mojang/renderpearl/api/pipeline/ShaderSource;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"
      )
   )
   private CompletableFuture<Pending> Y(PipelineBuilder var1, RenderPipeline var2, ShaderSource var3, Executor var4, Operation<CompletableFuture<Pending>> var5) {
      return (CompletableFuture<Pending>)var5.call(new Object[]{var1, var2, ob.e(var2, var3), var4});
   }
}
