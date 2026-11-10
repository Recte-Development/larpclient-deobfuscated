package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.w7;
import com.larpclient.module.SelfVisualsModule;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CapeLayer.class)
public class q7c61ae487383f132 {
   @Inject(
      method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/AvatarRenderState;FF)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void N(PoseStack var1, SubmitNodeCollector var2, int var3, AvatarRenderState var4, float var5, float var6, CallbackInfo var7) {
      if (!w7.x() && SelfVisualsModule.u(var4) && SelfVisualsModule.v().E()) {
         var7.cancel();
      }
   }
}
