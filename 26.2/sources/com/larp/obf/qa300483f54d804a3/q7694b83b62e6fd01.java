package com.larp.obf.qa300483f54d804a3;

import com.larpclient.m_;
import com.larpclient.tb;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.renderer.state.WindowRenderState;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiRenderer.class)
public abstract class q7694b83b62e6fd01 {
   @WrapOperation(
      method = "addElementToMesh",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/state/gui/GuiElementRenderState;pipeline()Lcom/mojang/blaze3d/pipeline/RenderPipeline;"
      )
   )
   private RenderPipeline A(GuiElementRenderState var1, Operation<RenderPipeline> var2) {
      RenderPipeline var3 = (RenderPipeline)var2.call(new Object[]{var1});
      if (m_.j() == null) {
         return var3;
      }

      RenderPipeline var4 = tb.n(var3);
      return var4 != null ? var4 : var3;
   }

   @WrapOperation(
      method = "addElementToMesh",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/state/gui/GuiElementRenderState;buildVertices(Lcom/mojang/blaze3d/vertex/VertexConsumer;)V"
      )
   )
   private void e(GuiElementRenderState var1, VertexConsumer var2, Operation<Void> var3) {
      m_.g var4 = m_.j();
      if (var4 == null) {
         var3.call(new Object[]{var1, var2});
      } else {
         RenderPipeline var5 = var1.pipeline();
         m_.k var6 = !tb.d(var5) ? m_.k.N : (tb.n(var5) != null ? m_.k.k : m_.k.P);
         m_.x var7 = m_.o(var2, var4, var5.getPrimitiveTopology() == PrimitiveTopology.QUADS, var1.scissorArea(), var6);

         try {
            var3.call(new Object[]{var1, var7});
         } finally {
            var7.u();
         }
      }
   }

   @WrapOperation(
      method = "addElementToMesh",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/state/gui/GuiElementRenderState;scissorArea()Lnet/minecraft/client/gui/navigation/ScreenRectangle;"
      )
   )
   private ScreenRectangle W(GuiElementRenderState var1, Operation<ScreenRectangle> var2) {
      m_.g var3 = m_.j();
      ScreenRectangle var4 = (ScreenRectangle)var2.call(new Object[]{var1});
      if (var3 != null && var4 != null) {
         WindowRenderState var5 = Minecraft.getInstance().gameRenderer.gameRenderState().windowRenderState;
         int var6 = Math.max(1, var5.guiScale);
         return var3.z(var4, var5.width / var6, var5.height / var6);
      } else {
         return var4;
      }
   }
}
