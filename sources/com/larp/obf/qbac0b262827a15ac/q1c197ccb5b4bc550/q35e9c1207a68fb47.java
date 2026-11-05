package com.larp.obf.qbac0b262827a15ac.q1c197ccb5b4bc550;

import com.larpclient.ui;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.renderpearl.frontend.FrontendCommandEncoder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FrontendCommandEncoder.class)
public abstract class q35e9c1207a68fb47 {
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
   private boolean Y(boolean var1) {
      return var1 && !ui.B();
   }
}
