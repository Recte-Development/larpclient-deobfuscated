package com.larp.obf.qa300483f54d804a3;

import com.larpclient.c4;
import com.larpclient.g3;
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
public abstract class qf9dd3881d61b2cf4 {
   private static final String Y = "renderToTexture(Lnet/minecraft/client/renderer/state/gui/pip/GuiSkinRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;)V";
   @Unique
   private final Matrix4f F = new Matrix4f();

   @Inject(
      method = "renderToTexture(Lnet/minecraft/client/renderer/state/gui/pip/GuiSkinRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;)V",
      at = @At("HEAD")
   )
   private void J(GuiSkinRenderState var1, PoseStack var2, SubmitNodeCollector var3, CallbackInfo var4) {
      if (var1.playerModel() instanceof g3 var5) {
         var5.s(var2);
      }

      this.F.set(var2.last().pose());
   }

   @Inject(
      method = "renderToTexture(Lnet/minecraft/client/renderer/state/gui/pip/GuiSkinRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;)V",
      at = @At("TAIL")
   )
   private void A(GuiSkinRenderState var1, PoseStack var2, SubmitNodeCollector var3, CallbackInfo var4) {
      c4.r(var1, this.F, var2, var3);
   }
}
