package com.larpclient.mixin;

import com.larpclient.render.overlay.OverlayTextures;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.commands.RenderPass.RenderArea;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import com.mojang.renderpearl.frontend.FrontendCommandEncoder;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import org.joml.Vector4fc;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FrontendCommandEncoder.class)
public abstract class FrontendCommandEncoderMixin {
   @Unique
   private boolean X;

   @Shadow
   public abstract RenderPass createRenderPass(
      Supplier<String> var1, GpuTextureView var2, Optional<Vector4fc> var3, @Nullable GpuTextureView var4, OptionalDouble var5, RenderArea var6
   );

   @Inject(
      method = "createRenderPass(Ljava/util/function/Supplier;Lcom/mojang/renderpearl/api/textures/GpuTextureView;Ljava/util/Optional;Lcom/mojang/renderpearl/api/textures/GpuTextureView;Ljava/util/OptionalDouble;Lcom/mojang/renderpearl/api/commands/RenderPass$RenderArea;)Lcom/mojang/renderpearl/api/commands/RenderPass;",
      at = @At("HEAD"),
      cancellable = true
   )
   private void Y(
      Supplier<String> var1,
      GpuTextureView var2,
      Optional<Vector4fc> var3,
      @Nullable GpuTextureView var4,
      OptionalDouble var5,
      RenderArea var6,
      CallbackInfoReturnable<RenderPass> var7
   ) {
      if (!this.X && (OverlayTextures.colorTexture != null || OverlayTextures.depthTexture != null)) {
         RenderTarget var8 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
         boolean var9 = OverlayTextures.colorTexture != null && var2 == var8.getColorTextureView();
         boolean var10 = OverlayTextures.depthTexture != null && var4 != null && var4 == var8.getDepthTextureView();
         if (var9 || var10) {
            GpuTextureView var11 = var9 ? OverlayTextures.colorTexture : var2;
            RenderArea var12 = var6.fillsTexture(var2) ? new RenderArea(0, 0, var11.getWidth(0), var11.getHeight(0)) : var6;
            this.X = true;

            try {
               var7.setReturnValue(this.createRenderPass(var1, var11, var3, var10 ? OverlayTextures.depthTexture : var4, var5, var12));
            } finally {
               this.X = false;
            }
         }
      }
   }
}
