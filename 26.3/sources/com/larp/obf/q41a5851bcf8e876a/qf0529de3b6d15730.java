package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.qd;
import com.larpclient.w6;
import com.larpclient.wi;
import com.larpclient.module.ItemPhysicModule;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntityRenderer.class)
public abstract class qf0529de3b6d15730 {
   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;F)V",
      at = @At("TAIL")
   )
   private void b(ItemEntity var1, ItemEntityRenderState var2, float var3, CallbackInfo var4) {
      ((wi)var2).larp$setGrounded(var1.onGround());
      ((wi)var2).larp$setFloating(!var1.onGround() && var1.isInWater());
   }

   @Inject(
      method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/entity/ItemEntityRenderer;submitMultipleFromCount(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/ItemClusterRenderState;Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/phys/AABB;)V"
      )
   )
   private void D(ItemEntityRenderState var1, PoseStack var2, SubmitNodeCollector var3, CameraRenderState var4, CallbackInfo var5) {
      ItemPhysicModule var6 = ItemPhysicModule.f();
      if (var6 != null && var6.M()) {
         AABB var7 = var1.item.getModelBoundingBox();
         float var8 = (float)Math.sin(var1.ageInTicks / 10.0F + var1.bobOffset) * 0.1F + 0.1F;
         float var9 = -((float)var7.minY) + 0.0625F;
         float var10 = ItemEntity.getSpin(var1.ageInTicks, var1.bobOffset);
         qd.i(var2, Axis.YP.rotation(-var10));
         var2.translate(0.0F, -var8 - var9, 0.0F);
         wi var11 = (wi)var1;
         w6.o var12 = w6.A(var11.larp$isGrounded(), var11.larp$isFloating(), var6.G(), var1.ageInTicks, var1.bobOffset, var7.minY, var7.minZ, var7.maxZ);
         var2.translate(0.0, var12.height(), 0.0);
         if (var12.billboard()) {
            qd.i(var2, var4.orientation);
            var2.scale(1.1F, 1.1F, 1.0F);
         } else {
            qd.i(var2, Axis.YP.rotation((float)var12.yaw()));
            qd.i(var2, Axis.XP.rotation((float)var12.pitch()));
            qd.i(var2, Axis.ZP.rotation((float)var12.roll()));
         }
      }
   }

   @Mixin(ItemEntityRenderState.class)
   public abstract static class q65e3bd9f241ae13f implements wi {
      @Unique
      private boolean U;
      @Unique
      private boolean A;

      @Override
      public void larp$setGrounded(boolean var1) {
         this.U = var1;
      }

      @Override
      public boolean larp$isGrounded() {
         return this.U;
      }

      @Override
      public void larp$setFloating(boolean var1) {
         this.A = var1;
      }

      @Override
      public boolean larp$isFloating() {
         return this.A;
      }
   }
}
