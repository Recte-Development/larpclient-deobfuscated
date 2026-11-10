package com.larp.obf.qa300483f54d804a3.q5e09f7d3f142fb91.q4ad210cb713c6dfc;

import com.larpclient.lf;
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
public class q866527a2025ebe83 {
   @Inject(
      method = "renderHand(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;Lnet/minecraft/client/model/geom/ModelPart;Z)V",
      at = @At("HEAD")
   )
   private void e(PoseStack var1, SubmitNodeCollector var2, int var3, Identifier var4, ModelPart var5, boolean var6, CallbackInfo var7) {
      ((lf)((AvatarRenderer)this).getModel()).larpRestoreWalkRig();
   }
}
