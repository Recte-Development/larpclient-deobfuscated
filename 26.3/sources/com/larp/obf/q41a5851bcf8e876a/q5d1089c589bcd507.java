package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.dx;
import com.larpclient.w1;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.renderer.state.WindowRenderState;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiRenderer.class)
public abstract class q5d1089c589bcd507 {
   @WrapOperation(
      method = "addElementToMesh",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/state/gui/GuiElementRenderState;pipeline()Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;"
      )
   )
   private RenderPipeline W(GuiElementRenderState var1, Operation<RenderPipeline> var2) {
      RenderPipeline var3 = (RenderPipeline)var2.call(new Object[]{var1});
      if (dx.X() == null) {
         return var3;
      }

      RenderPipeline var4 = w1.q(var3);
      return var4 != null ? var4 : var3;
   }

   @WrapOperation(
      method = "addElementToMesh",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/state/gui/GuiElementRenderState;buildVertices(Lcom/mojang/blaze3d/vertex/VertexConsumer;)V"
      )
   )
   private void B(GuiElementRenderState var1, VertexConsumer var2, Operation<Void> var3) {
      dx.x var4 = dx.X();
      if (var4 == null) {
         var3.call(new Object[]{var1, var2});
      } else {
         RenderPipeline var5 = var1.pipeline();
         dx.r var6 = !w1.B(var5) ? dx.r.h : (w1.q(var5) != null ? dx.r.B : dx.r.n);
         dx.v var7 = dx.s(var2, var4, var5.getPrimitiveTopology() == PrimitiveTopology.QUADS, var1.scissorArea(), var6);

         try {
            var3.call(new Object[]{var1, var7});
         } finally {
            var7.I();
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
   private ScreenRectangle L(GuiElementRenderState var1, Operation<ScreenRectangle> var2) {
      dx.x var3 = dx.X();
      ScreenRectangle var4 = (ScreenRectangle)var2.call(new Object[]{var1});
      if (var3 != null && var4 != null) {
         WindowRenderState var5 = Minecraft.getInstance().gameRenderer.gameRenderState().windowRenderState;
         int var6 = Math.max(1, var5.guiScale);
         return var3.M(var4, var5.width / var6, var5.height / var6);
      } else {
         return var4;
      }
   }
}
