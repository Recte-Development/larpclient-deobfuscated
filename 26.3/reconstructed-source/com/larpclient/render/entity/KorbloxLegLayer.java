package com.larpclient.render.entity;

import com.larpclient.social.cosmetic.SkinOverrideAccess;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;

public final class KorbloxLegLayer extends RenderLayer<AvatarRenderState, PlayerModel> {
   public KorbloxLegLayer(RenderLayerParent<AvatarRenderState, PlayerModel> var1) {
      super(var1);
   }

   public void submit(PoseStack var1, SubmitNodeCollector var2, int var3, AvatarRenderState var4, float var5, float var6) {
      LegTrail var7 = ((SkinOverrideAccess)var4).larp$korblox();
      if (var7 != null && !var4.isInvisible && !var4.isSpectator) {
         int var8 = LivingEntityRenderer.getOverlayCoords(var4, 0.0F);
         PlayerModel var9 = (PlayerModel)this.getParentModel();
         if (var7.right()) {
            KorbloxLegRenderer.renderLeg(var1, var2, var9.rightLeg, var7, var3, var8, false);
         }

         if (var7.left()) {
            KorbloxLegRenderer.renderLeg(var1, var2, var9.leftLeg, var7, var3, var8, true);
         }
      }
   }
}
