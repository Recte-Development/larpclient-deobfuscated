package com.larp.obf.qbac0b262827a15ac.qd37643ce476e1b8f.q6b10a8f7e862a6d5;

import com.larpclient.k;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "net.caffeinemc.mods.sodium.client.render.chunk.DefaultChunkRenderer", remap = false)
public abstract class qee2329a397f9d808 {
   @WrapOperation(
      method = "render",
      require = 0,
      remap = false,
      at = @At(value = "INVOKE", target = "Lcom/mojang/renderpearl/api/commands/RenderPass;setPipeline(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;)V")
   )
   private void x(RenderPass var1, RenderPipeline var2, Operation<Void> var3) {
      RenderPipeline var4 = k.c(var2);
      var3.call(new Object[]{var1, var4});
      k.v(var1, var4);
   }
}
