package com.larp.obf.qa300483f54d804a3.q5e09f7d3f142fb91.qe1de85825286c614;

import com.larpclient.w6;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Pseudo
@Mixin(targets = "net.caffeinemc.mods.sodium.client.render.chunk.DefaultChunkRenderer", remap = false)
public abstract class q03fa5080b20b2a9e {
   @WrapOperation(
      method = "render",
      require = 0,
      remap = false,
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;setPipeline(Lcom/mojang/blaze3d/pipeline/RenderPipeline;)V")
   )
   private void r(RenderPass var1, RenderPipeline var2, Operation<Void> var3) {
      RenderPipeline var4 = w6.M(var2);
      var3.call(new Object[]{var1, var4});
      w6.C(var1, var4);
   }
}
