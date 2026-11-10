package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.qc;
import com.larpclient.module.CustomSkyModule;
import com.larpclient.module.SkyShaderModule;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.minecraft.client.renderer.CloudRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CloudRenderer.class)
public class qe1f7e4cd90cf9d8d {
   @Inject(
      method = "render(Lcom/mojang/renderpearl/api/commands/RenderPass;Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void s(RenderPass var1, RenderPipeline var2, CallbackInfo var3) {
      if (!qc.t()) {
         SkyShaderModule var4 = SkyShaderModule.R();
         CustomSkyModule var5 = CustomSkyModule.p();
         if (CustomSkyModule.C() && var5 != null && !var5.r() || var4 != null && var4.A()) {
            var3.cancel();
         }
      }
   }
}
