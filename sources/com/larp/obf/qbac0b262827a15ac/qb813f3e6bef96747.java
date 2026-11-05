package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.pa;
import com.larpclient.w_;
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
public abstract class qb813f3e6bef96747 {
   private static final String G = "renderToTexture(Lnet/minecraft/client/renderer/state/gui/pip/GuiSkinRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;)V";
   @Unique
   private final Matrix4f u = new Matrix4f();

   @Inject(
      method = "renderToTexture(Lnet/minecraft/client/renderer/state/gui/pip/GuiSkinRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;)V",
      at = @At("HEAD")
   )
   private void w(GuiSkinRenderState var1, PoseStack var2, SubmitNodeCollector var3, CallbackInfo var4) {
      if (var1.playerModel() instanceof w_ var5) {
         var5.n(var2);
      }

      this.u.set(var2.last().pose());
   }

   @Inject(
      method = "renderToTexture(Lnet/minecraft/client/renderer/state/gui/pip/GuiSkinRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;)V",
      at = @At("TAIL")
   )
   private void Q(GuiSkinRenderState var1, PoseStack var2, SubmitNodeCollector var3, CallbackInfo var4) {
      pa.G(var1, this.u, var2, var3);
   }
}
