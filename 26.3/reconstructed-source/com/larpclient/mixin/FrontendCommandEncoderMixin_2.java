package com.larpclient.mixin;

import com.larpclient.render.overlay.MainPassSplitter;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.renderpearl.frontend.FrontendCommandEncoder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FrontendCommandEncoder.class)
public abstract class FrontendCommandEncoderMixin_2 {
   @ModifyExpressionValue(
      method = {
            "createRenderPass(Lcom/mojang/renderpearl/api/commands/RenderPassDescriptor;)Lcom/mojang/renderpearl/api/commands/RenderPass;",
            "createFence",
            "clearColorAndDepthTextures",
            "clearColorTexture",
            "clearDepthTexture",
            "copyBufferToTexture",
            "copyTextureToBuffer",
            "copyTextureToTexture",
            "copyToBuffer",
            "writeToBuffer",
            "writeToTexture(Lcom/mojang/renderpearl/api/textures/GpuTexture;Lcom/mojang/blaze3d/platform/NativeImage;IIII)V",
            "writeToTexture(Lcom/mojang/renderpearl/api/textures/GpuTexture;Ljava/nio/ByteBuffer;IIIIII)V"
      },
      at = @At(value = "FIELD", target = "Lcom/mojang/renderpearl/frontend/FrontendCommandEncoder;isInRenderPass:Z", opcode = 180)
   )
   private boolean g(boolean var1) {
      return var1 && !MainPassSplitter.suspendMainPass();
   }
}
