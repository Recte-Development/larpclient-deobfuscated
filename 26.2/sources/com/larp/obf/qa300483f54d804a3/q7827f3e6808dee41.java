package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.RemovalsModule;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class q7827f3e6808dee41 {
   @Inject(method = "bobHurt", at = @At("HEAD"), cancellable = true)
   private void k(CameraRenderState var1, PoseStack var2, CallbackInfo var3) {
      if (RemovalsModule.n("hurtCam")) {
         var3.cancel();
      }
   }

   @Redirect(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;lerp(FFF)F"), require = 1, allow = 1)
   private float P(float var1, float var2, float var3) {
      return RemovalsModule.A() ? 0.0F : Mth.lerp(var1, var2, var3);
   }

   @Redirect(
      method = "renderLevel",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getEffectBlendFactor(Lnet/minecraft/core/Holder;F)F"),
      require = 1,
      allow = 1
   )
   private float V(LocalPlayer var1, Holder<MobEffect> var2, float var3) {
      return var2.equals(MobEffects.NAUSEA) && RemovalsModule.A() ? 0.0F : var1.getEffectBlendFactor(var2, var3);
   }
}
