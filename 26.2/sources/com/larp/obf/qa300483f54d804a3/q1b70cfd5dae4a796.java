package com.larp.obf.qa300483f54d804a3;

import com.larpclient.j4;
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
public class q1b70cfd5dae4a796 {
   @Inject(
      method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void F(PoseStack var1, SubmitNodeCollector var2, int var3, HumanoidRenderState var4, float var5, float var6, CallbackInfo var7) {
      if (var4 instanceof AvatarRenderState var8 && HideArmourModule.Y(var8)) {
         var7.cancel();
      } else {
         if (!j4.X() && SelfVisualsModule.H(var4) && SelfVisualsModule.k().D.getValue()) {
            var7.cancel();
         }
      }
   }
}
