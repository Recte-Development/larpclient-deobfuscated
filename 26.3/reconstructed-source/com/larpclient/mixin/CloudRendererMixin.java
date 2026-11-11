package com.larpclient.mixin;

import com.larpclient.core.StreamProofManager;
import com.larpclient.module.render.CustomSkyModule;
import com.larpclient.module.render.SkyShaderModule;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.minecraft.client.renderer.CloudRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CloudRenderer.class)
public class CloudRendererMixin {
   @Inject(
      method = "render(Lcom/mojang/renderpearl/api/commands/RenderPass;Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void s(RenderPass var1, RenderPipeline var2, CallbackInfo var3) {
      if (!StreamProofManager.isHidingInterface()) {
         SkyShaderModule var4 = SkyShaderModule.getInstance();
         CustomSkyModule var5 = CustomSkyModule.getInstance();
         if (CustomSkyModule.isActive() && var5 != null && !var5.isCloudsEnabled() || var4 != null && var4.getAppearanceSettings()) {
            var3.cancel();
         }
      }
   }
}
