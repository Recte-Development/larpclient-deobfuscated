package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.uv;
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
public class qdf30a6a9d45f819f {
   @Inject(
      method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/AvatarRenderState;FF)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void k(PoseStack var1, SubmitNodeCollector var2, int var3, AvatarRenderState var4, float var5, float var6, CallbackInfo var7) {
      if (!uv.G() && SelfVisualsModule.B(var4) && SelfVisualsModule.f().I()) {
         var7.cancel();
      }
   }
}
