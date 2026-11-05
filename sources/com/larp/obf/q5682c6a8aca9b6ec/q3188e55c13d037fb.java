package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.ow;
import com.larpclient.rk;
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
public abstract class q3188e55c13d037fb {
   @WrapOperation(
      method = "addElementToMesh",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/state/gui/GuiElementRenderState;pipeline()Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;"
      )
   )
   private RenderPipeline x(GuiElementRenderState var1, Operation<RenderPipeline> var2) {
      RenderPipeline var3 = (RenderPipeline)var2.call(new Object[]{var1});
      if (rk.w() == null) {
         return var3;
      }

      RenderPipeline var4 = ow.p(var3);
      return var4 != null ? var4 : var3;
   }

   @WrapOperation(
      method = "addElementToMesh",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/state/gui/GuiElementRenderState;buildVertices(Lcom/mojang/blaze3d/vertex/VertexConsumer;)V"
      )
   )
   private void P(GuiElementRenderState var1, VertexConsumer var2, Operation<Void> var3) {
      rk.f var4 = rk.w();
      if (var4 == null) {
         var3.call(new Object[]{var1, var2});
      } else {
         RenderPipeline var5 = var1.pipeline();
         rk.u var6 = !ow.x(var5) ? rk.u.G : (ow.p(var5) != null ? rk.u.w : rk.u.b);
         rk.h var7 = rk.E(var2, var4, var5.getPrimitiveTopology() == PrimitiveTopology.QUADS, var1.scissorArea(), var6);

         try {
            var3.call(new Object[]{var1, var7});
         } finally {
            var7.k();
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
   private ScreenRectangle C(GuiElementRenderState var1, Operation<ScreenRectangle> var2) {
      rk.f var3 = rk.w();
      ScreenRectangle var4 = (ScreenRectangle)var2.call(new Object[]{var1});
      if (var3 != null && var4 != null) {
         WindowRenderState var5 = Minecraft.getInstance().gameRenderer.gameRenderState().windowRenderState;
         int var6 = Math.max(1, var5.guiScale);
         return var3.p(var4, var5.width / var6, var5.height / var6);
      } else {
         return var4;
      }
   }
}
