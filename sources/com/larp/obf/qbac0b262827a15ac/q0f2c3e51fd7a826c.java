package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.PlayerModelModule;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.HumanoidArm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemInHandLayer.class)
public class q0f2c3e51fd7a826c {
   @Redirect(
      method = "submitArmWithItem",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/model/ArmedModel;translateToHand(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lnet/minecraft/world/entity/HumanoidArm;Lcom/mojang/blaze3d/vertex/PoseStack;)V"
      )
   )
   private void f(ArmedModel<EntityRenderState> var1, EntityRenderState var2, HumanoidArm var3, PoseStack var4) {
      PlayerModelModule var5 = PlayerModelModule.Y();
      if (var5 == null || !var5.g(var2, var3, var4)) {
         var1.translateToHand(var2, var3, var4);
      }
   }
}
