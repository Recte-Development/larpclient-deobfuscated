package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.wy;
import com.larpclient.zo;
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
public class q03c5277d766822e5 {
   @Inject(
      method = "render(Lcom/mojang/renderpearl/api/commands/RenderPass;Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void H(RenderPass var1, RenderPipeline var2, CallbackInfo var3) {
      if (!zo.l()) {
         SkyShaderModule var4 = SkyShaderModule.U();
         CustomSkyModule var5 = CustomSkyModule.Y();
         if (CustomSkyModule.a() && var5 != null && !var5.Q() || wy.a() || var4 != null && var4.d()) {
            var3.cancel();
         }
      }
   }
}
