package com.larp.obf.qa300483f54d804a3.q5e09f7d3f142fb91.q4ad210cb713c6dfc;

import com.larpclient.CrystalChamsModule;
import com.larpclient.c;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EndCrystalRenderer;
import net.minecraft.client.renderer.entity.state.EndCrystalRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer.CrumblingOverlay;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndCrystalRenderer.class)
public class q839bc234489d699a {
   @WrapOperation(
      method = "submit(Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V")
   )
   private void m(PoseStack var1, float var2, float var3, float var4, Operation<Void> var5) {
      CrystalChamsModule var6 = CrystalChamsModule.Y();
      float var7 = var6 == null ? 1.0F : var6.a();
      var5.call(new Object[]{var1, var2 * var7, var3 * var7, var4 * var7});
   }

   @WrapOperation(
      method = "submit(Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/resources/Identifier;IIILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"
      )
   )
   private <S> void q(
      SubmitNodeCollector var1,
      Model<S> var2,
      S var3,
      PoseStack var4,
      Identifier var5,
      int var6,
      int var7,
      int var8,
      CrumblingOverlay var9,
      Operation<Void> var10
   ) {
      CrystalChamsModule var11 = CrystalChamsModule.Y();
      if (var11 == null || !var11.B(var3)) {
         if (var11 != null && var11.i()) {
            RenderType var12 = var11.y()
               ? RenderTypes.entityTranslucentEmissive(var5)
               : (var11.H() ? RenderTypes.entityTranslucent(var5) : var2.renderType(var5));
            int var13 = var3 instanceof EndCrystalRenderState var14 ? (int)(var14.x * 7.0 + var14.z * 13.0) : 0;
            var1.submitModel(var2, var3, var4, var12, var11.y() ? 15728880 : var6, var7, var11.m(var13), null, var8, var9);
         } else {
            var10.call(new Object[]{var1, var2, var3, var4, var5, var6, var7, var8, var9});
         }
      }
   }

   @Inject(method = "getY", at = @At("RETURN"), cancellable = true)
   private static void j(float var0, CallbackInfoReturnable<Float> var1) {
      CrystalChamsModule var2 = CrystalChamsModule.Y();
      if (var2 != null) {
         var1.setReturnValue(var1.getReturnValueF() * var2.M());
      }
   }

   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/boss/enderdragon/EndCrystal;Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;F)V",
      at = @At("TAIL")
   )
   private void U(EndCrystal var1, EndCrystalRenderState var2, float var3, CallbackInfo var4) {
      ((c)var2).larpCrystalId(var1.getId());
      CrystalChamsModule var5 = CrystalChamsModule.Y();
      Boolean var6 = var5 == null ? null : var5.h();
      if (var6 != null) {
         var2.showsBottom = var6;
      }
   }
}
