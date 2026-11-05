package com.larp.obf.q5682c6a8aca9b6ec.q1e1bb13db291d59c.q6e1212582deeb4b9;

import com.larpclient.xe;
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
public class q52364a409497f6b2 {
   @Inject(
      method = "renderHand(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;Lnet/minecraft/client/model/geom/ModelPart;Z)V",
      at = @At("HEAD")
   )
   private void t(PoseStack var1, SubmitNodeCollector var2, int var3, Identifier var4, ModelPart var5, boolean var6, CallbackInfo var7) {
      ((xe)((AvatarRenderer)this).getModel()).larpRestoreWalkRig();
   }
}
