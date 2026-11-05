package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.module.RemovalsModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class q8bf4d76949611889 {
   @Inject(method = "bobHurt", at = @At("HEAD"), cancellable = true)
   private void z(CameraRenderState var1, PoseStack var2, CallbackInfo var3) {
      if (RemovalsModule.r("hurtCam")) {
         var3.cancel();
      }
   }

   @ModifyExpressionValue(
      method = "renderLevel",
      at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/state/level/PlayerRenderState;portalEffectIntensity:F"),
      require = 1,
      allow = 1
   )
   private float n(float var1) {
      return RemovalsModule.D() ? 0.0F : var1;
   }

   @ModifyExpressionValue(
      method = "renderLevel",
      at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/state/level/PlayerRenderState;nauseaEffectIntensity:F"),
      require = 1,
      allow = 1
   )
   private float l(float var1) {
      return RemovalsModule.D() ? 0.0F : var1;
   }
}
