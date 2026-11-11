package com.larpclient.mixin;

import com.larpclient.core.StreamProofManager;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiRenderer.class)
public abstract class GuiRendererMixin {
   @WrapOperation(
      method = "draw",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;mainRenderTarget()Lcom/mojang/blaze3d/pipeline/RenderTarget;")
   )
   private RenderTarget e(GameRenderer var1, Operation<RenderTarget> var2) {
      RenderTarget var3 = StreamProofManager.getRenderTarget(this);
      return var3 != null ? var3 : (RenderTarget)var2.call(new Object[]{var1});
   }

   @WrapOperation(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;processBlurEffect()V"))
   private void O(GameRenderer var1, Operation<Void> var2) {
      if (!StreamProofManager.destroyOverlay(this)) {
         var2.call(new Object[]{var1});
      }
   }
}
