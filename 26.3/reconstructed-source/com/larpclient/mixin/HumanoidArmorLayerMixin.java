package com.larpclient.mixin;

import com.larpclient.module.render.HideArmourModule;
import com.larpclient.module.render.SelfVisualsModule;
import com.larpclient.render.model.ModelVisualRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin {
   @Inject(
      method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void C(PoseStack var1, SubmitNodeCollector var2, int var3, HumanoidRenderState var4, float var5, float var6, CallbackInfo var7) {
      if (var4 instanceof AvatarRenderState var8 && HideArmourModule.shouldHideArmour(var8)) {
         var7.cancel();
      } else {
         if (!ModelVisualRenderer.isProbing() && SelfVisualsModule.shouldStyleRenderState(var4) && SelfVisualsModule.getPrimaryColor().affectArmor.getValue()) {
            var7.cancel();
         }
      }
   }
}
