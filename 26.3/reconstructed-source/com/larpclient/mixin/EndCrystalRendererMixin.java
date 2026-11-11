package com.larpclient.mixin;

import com.larpclient.module.render.CrystalChamsModule;
import com.larpclient.util.entity.CrystalIdAccess;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EndCrystalRenderer;
import net.minecraft.client.renderer.entity.state.EndCrystalRenderState;
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
public class EndCrystalRendererMixin {
   @WrapOperation(
      method = "submit(Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V")
   )
   private void H(PoseStack var1, float var2, float var3, float var4, Operation<Void> var5) {
      CrystalChamsModule var6 = CrystalChamsModule.getInstance();
      float var7 = var6 == null ? 1.0F : var6.getScale();
      var5.call(new Object[]{var1, var2 * var7, var3 * var7, var4 * var7});
   }

   @WrapOperation(
      method = "submit(Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/resources/Identifier;III)V"
      )
   )
   private <S> void m(SubmitNodeCollector var1, Model<S> var2, S var3, PoseStack var4, Identifier var5, int var6, int var7, int var8, Operation<Void> var9) {
      CrystalChamsModule var10 = CrystalChamsModule.getInstance();
      if (var10 == null || !var10.isCustomized(var3)) {
         if (var10 != null && var10.getScale()) {
            RenderType var11 = var10.isGlowEnabled()
               ? RenderTypes.entityTranslucentEmissive(var5)
               : (var10.getAppearanceSettings() ? RenderTypes.entityTranslucent(var5) : var2.renderType(var5));
            int var12 = var3 instanceof EndCrystalRenderState var13 ? (int)(var13.x * 7.0 + var13.z * 13.0) : 0;
            var1.submitModel(var2, var3, var4, var11, var10.isGlowEnabled() ? 15728880 : var6, var7, var10.getTint(var12), null, var8);
         } else {
            var9.call(new Object[]{var1, var2, var3, var4, var5, var6, var7, var8});
         }
      }
   }

   @Inject(method = "getY", at = @At("RETURN"), cancellable = true)
   private static void s(float var0, CallbackInfoReturnable<Float> var1) {
      CrystalChamsModule var2 = CrystalChamsModule.getInstance();
      if (var2 != null) {
         var1.setReturnValue(var1.getReturnValueF() * var2.getBounceScale());
      }
   }

   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/boss/enderdragon/EndCrystal;Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;F)V",
      at = @At("TAIL")
   )
   private void z(EndCrystal var1, EndCrystalRenderState var2, float var3, CallbackInfo var4) {
      ((CrystalIdAccess)var2).larpCrystalId(var1.getId());
      CrystalChamsModule var5 = CrystalChamsModule.getInstance();
      Boolean var6 = var5 == null ? null : var5.getBaseOverride();
      if (var6 != null) {
         var2.showsBottom = var6;
      }
   }
}
