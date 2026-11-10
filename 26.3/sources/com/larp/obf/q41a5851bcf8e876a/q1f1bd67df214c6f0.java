package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.fw;
import com.larpclient.m;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.render.pip.GuiSkinRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.gui.pip.GuiSkinRenderState;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiSkinRenderer.class)
public abstract class q1f1bd67df214c6f0 {
   private static final String k = "renderToTexture(Lnet/minecraft/client/renderer/state/gui/pip/GuiSkinRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;)V";
   @Unique
   private final Matrix4f X = new Matrix4f();

   @Inject(
      method = "renderToTexture(Lnet/minecraft/client/renderer/state/gui/pip/GuiSkinRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;)V",
      at = @At("HEAD")
   )
   private void j(GuiSkinRenderState var1, PoseStack var2, SubmitNodeCollector var3, CallbackInfo var4) {
      if (var1.playerModel() instanceof fw var5) {
         var5.q(var2);
      }

      this.X.set(var2.last().pose());
   }

   @Inject(
      method = "renderToTexture(Lnet/minecraft/client/renderer/state/gui/pip/GuiSkinRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;)V",
      at = @At("TAIL")
   )
   private void P(GuiSkinRenderState var1, PoseStack var2, SubmitNodeCollector var3, CallbackInfo var4) {
      m.e(var1, this.X, var2, var3);
   }
}
