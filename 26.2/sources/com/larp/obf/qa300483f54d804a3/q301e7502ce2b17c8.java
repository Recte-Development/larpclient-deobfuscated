package com.larp.obf.qa300483f54d804a3;

import com.larpclient.j4;
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
public class q301e7502ce2b17c8 {
   @Inject(
      method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/AvatarRenderState;FF)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void y(PoseStack var1, SubmitNodeCollector var2, int var3, AvatarRenderState var4, float var5, float var6, CallbackInfo var7) {
      if (!j4.X() && SelfVisualsModule.H(var4) && SelfVisualsModule.k().s()) {
         var7.cancel();
      }
   }
}
