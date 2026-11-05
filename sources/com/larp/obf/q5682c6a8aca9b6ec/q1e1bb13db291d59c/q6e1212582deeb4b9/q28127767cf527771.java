package com.larp.obf.q5682c6a8aca9b6ec.q1e1bb13db291d59c.q6e1212582deeb4b9;

import com.larpclient.CrystalChamsModule;
import com.larpclient.yn;
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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndCrystalRenderer.class)
public class q28127767cf527771 {
   @Redirect(
      method = "submit(Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
      at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V")
   )
   private void o(PoseStack var1, float var2, float var3, float var4) {
      CrystalChamsModule var5 = CrystalChamsModule.l();
      float var6 = var5 == null ? 1.0F : var5.N();
      var1.scale(var2 * var6, var3 * var6, var4 * var6);
   }

   @Redirect(
      method = "submit(Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/resources/Identifier;III)V"
      )
   )
   private <S> void Z(SubmitNodeCollector var1, Model<S> var2, S var3, PoseStack var4, Identifier var5, int var6, int var7, int var8) {
      CrystalChamsModule var9 = CrystalChamsModule.l();
      if (var9 == null || !var9.r(var3)) {
         if (var9 != null && var9.o()) {
            RenderType var10 = var9.D()
               ? RenderTypes.entityTranslucentEmissive(var5)
               : (var9.g() ? RenderTypes.entityTranslucent(var5) : var2.renderType(var5));
            int var11 = var3 instanceof EndCrystalRenderState var12 ? (int)(var12.x * 7.0 + var12.z * 13.0) : 0;
            var1.submitModel(var2, var3, var4, var10, var9.D() ? 15728880 : var6, var7, var9.Z(var11), null, var8);
         } else {
            var1.submitModel(var2, var3, var4, var5, var6, var7, var8);
         }
      }
   }

   @Inject(method = "getY", at = @At("RETURN"), cancellable = true)
   private static void p(float var0, CallbackInfoReturnable<Float> var1) {
      CrystalChamsModule var2 = CrystalChamsModule.l();
      if (var2 != null) {
         var1.setReturnValue(var1.getReturnValueF() * var2.G());
      }
   }

   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/boss/enderdragon/EndCrystal;Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;F)V",
      at = @At("TAIL")
   )
   private void y(EndCrystal var1, EndCrystalRenderState var2, float var3, CallbackInfo var4) {
      ((yn)var2).larpCrystalId(var1.getId());
      CrystalChamsModule var5 = CrystalChamsModule.l();
      Boolean var6 = var5 == null ? null : var5.D();
      if (var6 != null) {
         var2.showsBottom = var6;
      }
   }
}
