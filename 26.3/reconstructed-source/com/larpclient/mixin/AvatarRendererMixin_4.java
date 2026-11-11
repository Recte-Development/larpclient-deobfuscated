package com.larpclient.mixin;

import com.larpclient.module.render.SelfVisualsModule;
import com.larpclient.render.model.ModelVisualRenderer;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.StreamProofState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.UvMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin_4 {
   @Redirect(
      method = "renderHand",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModelPart(Lnet/minecraft/client/model/geom/ModelPart;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IILnet/minecraft/client/renderer/texture/UvMapping;)V"
      )
   )
   private void E(SubmitNodeCollector var1, ModelPart var2, PoseStack var3, RenderType var4, int var5, int var6, UvMapping var7) {
      try {
         if (!StreamProofState.isActive() && ModelVisualRenderer.renderFirstPersonArm(var2, var3, var1, var4, var5, var6, var7)) {
            return;
         }
      } catch (RuntimeException var10) {
         LarpLog.errorOnce("SelfVisuals", "First-person arm stage=material shader=model/material failed; restoring vanilla arms", var10);
         SelfVisualsModule var9 = SelfVisualsModule.getPrimaryColor();
         if (var9 != null) {
            var9.disable();
         }
      }

      var1.submitModelPart(var2, var3, var4, var5, var6, var7);
   }
}
