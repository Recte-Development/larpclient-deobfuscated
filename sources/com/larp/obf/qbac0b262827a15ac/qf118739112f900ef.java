package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.cp;
import com.larpclient.tl;
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
public abstract class qf118739112f900ef {
   @WrapOperation(
      method = "addElementToMesh",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/state/gui/GuiElementRenderState;pipeline()Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;"
      )
   )
   private RenderPipeline f(GuiElementRenderState var1, Operation<RenderPipeline> var2) {
      RenderPipeline var3 = (RenderPipeline)var2.call(new Object[]{var1});
      if (cp.U() == null) {
         return var3;
      }

      RenderPipeline var4 = tl.m(var3);
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
      cp.l var4 = cp.U();
      if (var4 == null) {
         var3.call(new Object[]{var1, var2});
      } else {
         RenderPipeline var5 = var1.pipeline();
         cp.t var6 = !tl.y(var5) ? cp.t.m : (tl.m(var5) != null ? cp.t.W : cp.t.U);
         cp.n var7 = cp.n(var2, var4, var5.getPrimitiveTopology() == PrimitiveTopology.QUADS, var1.scissorArea(), var6);

         try {
            var3.call(new Object[]{var1, var7});
         } finally {
            var7.Z();
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
   private ScreenRectangle B(GuiElementRenderState var1, Operation<ScreenRectangle> var2) {
      cp.l var3 = cp.U();
      ScreenRectangle var4 = (ScreenRectangle)var2.call(new Object[]{var1});
      if (var3 != null && var4 != null) {
         WindowRenderState var5 = Minecraft.getInstance().gameRenderer.gameRenderState().windowRenderState;
         int var6 = Math.max(1, var5.guiScale);
         return var3.k(var4, var5.width / var6, var5.height / var6);
      } else {
         return var4;
      }
   }
}
