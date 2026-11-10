package com.larp.obf.q41a5851bcf8e876a.qb12a7b6d04ee2fc9.q6780db403bf6e5d8;

import com.larpclient.at;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "net.caffeinemc.mods.sodium.client.render.chunk.DefaultChunkRenderer", remap = false)
public abstract class q70f61e95da213bbf {
   @WrapOperation(
      method = "render",
      require = 0,
      remap = false,
      at = @At(value = "INVOKE", target = "Lcom/mojang/renderpearl/api/commands/RenderPass;setPipeline(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;)V")
   )
   private void X(RenderPass var1, RenderPipeline var2, Operation<Void> var3) {
      RenderPipeline var4 = at.s(var2);
      var3.call(new Object[]{var1, var4});
      at.A(var1, var4);
   }
}
