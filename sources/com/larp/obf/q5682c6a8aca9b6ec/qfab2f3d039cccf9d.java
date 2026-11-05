package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.e8;
import com.larpclient.mn;
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
public abstract class qfab2f3d039cccf9d {
   private static final String I = "renderToTexture(Lnet/minecraft/client/renderer/state/gui/pip/GuiSkinRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;)V";
   @Unique
   private final Matrix4f N = new Matrix4f();

   @Inject(
      method = "renderToTexture(Lnet/minecraft/client/renderer/state/gui/pip/GuiSkinRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;)V",
      at = @At("HEAD")
   )
   private void e(GuiSkinRenderState var1, PoseStack var2, SubmitNodeCollector var3, CallbackInfo var4) {
      if (var1.playerModel() instanceof mn var5) {
         var5.s(var2);
      }

      this.N.set(var2.last().pose());
   }

   @Inject(
      method = "renderToTexture(Lnet/minecraft/client/renderer/state/gui/pip/GuiSkinRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;)V",
      at = @At("TAIL")
   )
   private void Q(GuiSkinRenderState var1, PoseStack var2, SubmitNodeCollector var3, CallbackInfo var4) {
      e8.d(var1, this.N, var2, var3);
   }
}
