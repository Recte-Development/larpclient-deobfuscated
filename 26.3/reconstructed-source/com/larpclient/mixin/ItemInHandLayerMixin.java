package com.larpclient.mixin;

import com.larpclient.module.render.PlayerModelModule;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemInHandLayer.class)
public class ItemInHandLayerMixin {
   @Redirect(
      method = "submitArmWithItem",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/model/ArmedModel;translateToHand(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lnet/minecraft/world/entity/HumanoidArm;Lcom/mojang/blaze3d/vertex/PoseStack;)V"
      )
   )
   private void v(ArmedModel<EntityRenderState> var1, EntityRenderState var2, HumanoidArm var3, PoseStack var4) {
      PlayerModelModule var5 = PlayerModelModule.getInstance();
      if (var5 == null || !var5.translateToHand(var2, var3, var4)) {
         var1.translateToHand(var2, var3, var4);
      }
   }
}
