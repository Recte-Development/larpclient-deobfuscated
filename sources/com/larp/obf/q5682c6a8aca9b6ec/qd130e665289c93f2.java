package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.y8;
import com.larpclient.yv;
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
public class qd130e665289c93f2 {
   @Inject(
      method = "render(Lcom/mojang/renderpearl/api/commands/RenderPass;Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void z(RenderPass var1, RenderPipeline var2, CallbackInfo var3) {
      if (!y8.J()) {
         SkyShaderModule var4 = SkyShaderModule.B();
         CustomSkyModule var5 = CustomSkyModule.Y();
         if (CustomSkyModule.i() && var5 != null && !var5.d() || yv.K() || var4 != null && var4.Y()) {
            var3.cancel();
         }
      }
   }
}
