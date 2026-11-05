package com.larp.obf.qbac0b262827a15ac.qd37643ce476e1b8f.q60d951b25740ad52;

import com.larpclient._b;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class q50a6bf021e0bcb76 {
   @Inject(
      method = "renderHand(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;Lnet/minecraft/client/model/geom/ModelPart;Z)V",
      at = @At("HEAD")
   )
   private void v(PoseStack var1, SubmitNodeCollector var2, int var3, Identifier var4, ModelPart var5, boolean var6, CallbackInfo var7) {
      ((_b)((AvatarRenderer)this).getModel()).larpRestoreWalkRig();
   }
}
