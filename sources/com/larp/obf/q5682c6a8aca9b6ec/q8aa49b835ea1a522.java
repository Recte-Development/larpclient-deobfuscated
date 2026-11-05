package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.y8;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiRenderer.class)
public abstract class q8aa49b835ea1a522 {
   @WrapOperation(
      method = "draw",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;mainRenderTarget()Lcom/mojang/blaze3d/pipeline/RenderTarget;")
   )
   private RenderTarget u(GameRenderer var1, Operation<RenderTarget> var2) {
      RenderTarget var3 = y8.U(this);
      return var3 != null ? var3 : (RenderTarget)var2.call(new Object[]{var1});
   }

   @WrapOperation(method = "draw", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GameRenderer;processBlurEffect()V"))
   private void N(GameRenderer var1, Operation<Void> var2) {
      if (!y8.x(this)) {
         var2.call(new Object[]{var1});
      }
   }
}
