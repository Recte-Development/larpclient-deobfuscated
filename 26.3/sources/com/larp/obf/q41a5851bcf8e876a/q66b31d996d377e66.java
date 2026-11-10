package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.w7;
import com.larpclient.module.HideArmourModule;
import com.larpclient.module.SelfVisualsModule;
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
public class q66b31d996d377e66 {
   @Inject(
      method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void C(PoseStack var1, SubmitNodeCollector var2, int var3, HumanoidRenderState var4, float var5, float var6, CallbackInfo var7) {
      if (var4 instanceof AvatarRenderState var8 && HideArmourModule.m(var8)) {
         var7.cancel();
      } else {
         if (!w7.x() && SelfVisualsModule.u(var4) && SelfVisualsModule.v().v.getValue()) {
            var7.cancel();
         }
      }
   }
}
