package com.larpclient.mixin;

import com.larpclient.module.render.ItemPhysicModule;
import com.larpclient.render.model.KnifeDropPose;
import com.larpclient.util.entity.GroundedFloatingState;
import com.larpclient.util.render.PoseStackUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntityRenderer.class)
public abstract class ItemEntityRendererMixin {
   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;F)V",
      at = @At("TAIL")
   )
   private void b(ItemEntity var1, ItemEntityRenderState var2, float var3, CallbackInfo var4) {
      ((GroundedFloatingState)var2).larp$setGrounded(var1.onGround());
      ((GroundedFloatingState)var2).larp$setFloating(!var1.onGround() && var1.isInWater());
   }

   @Inject(
      method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/entity/ItemEntityRenderer;submitMultipleFromCount(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/ItemClusterRenderState;Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/phys/AABB;)V"
      )
   )
   private void D(ItemEntityRenderState var1, PoseStack var2, SubmitNodeCollector var3, CameraRenderState var4, CallbackInfo var5) {
      ItemPhysicModule var6 = ItemPhysicModule.getInstance();
      if (var6 != null && var6.isActive()) {
         AABB var7 = var1.item.getModelBoundingBox();
         float var8 = (float)Math.sin(var1.ageInTicks / 10.0F + var1.bobOffset) * 0.1F + 0.1F;
         float var9 = -((float)var7.minY) + 0.0625F;
         float var10 = ItemEntity.getSpin(var1.ageInTicks, var1.bobOffset);
         PoseStackUtil.rotate(var2, Axis.YP.rotation(-var10));
         var2.translate(0.0F, -var8 - var9, 0.0F);
         GroundedFloatingState var11 = (GroundedFloatingState)var1;
         KnifeDropPose.Pose var12 = KnifeDropPose.compute(
            var11.larp$isGrounded(), var11.larp$isFloating(), var6.isFlatMode(), var1.ageInTicks, var1.bobOffset, var7.minY, var7.minZ, var7.maxZ
         );
         var2.translate(0.0, var12.height(), 0.0);
         if (var12.billboard()) {
            PoseStackUtil.rotate(var2, var4.orientation);
            var2.scale(1.1F, 1.1F, 1.0F);
         } else {
            PoseStackUtil.rotate(var2, Axis.YP.rotation((float)var12.yaw()));
            PoseStackUtil.rotate(var2, Axis.XP.rotation((float)var12.pitch()));
            PoseStackUtil.rotate(var2, Axis.ZP.rotation((float)var12.roll()));
         }
      }
   }
}
