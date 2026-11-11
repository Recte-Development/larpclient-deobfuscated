package com.larpclient.mixin;

import com.larpclient.module.render.SelfVisualsModule;
import com.larpclient.render.model.ModelVisualRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CapeLayer.class)
public class CapeLayerMixin {
   @Inject(
      method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/AvatarRenderState;FF)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void N(PoseStack var1, SubmitNodeCollector var2, int var3, AvatarRenderState var4, float var5, float var6, CallbackInfo var7) {
      if (!ModelVisualRenderer.isProbing() && SelfVisualsModule.shouldStyleRenderState(var4) && SelfVisualsModule.getPrimaryColor().getMaterial()) {
         var7.cancel();
      }
   }
}
