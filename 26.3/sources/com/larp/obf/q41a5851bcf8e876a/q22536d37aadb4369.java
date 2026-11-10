package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.module.RemovalsModule;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.PlayerRenderState.WaterOverlay;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class q22536d37aadb4369 {
   @Inject(method = "submitFire", at = @At("HEAD"), cancellable = true)
   private static void N(PoseStack var0, SubmitNodeCollector var1, TextureAtlasSprite var2, CallbackInfo var3) {
      if (RemovalsModule.G("fire")) {
         var3.cancel();
      }
   }

   @Inject(method = "submitWater", at = @At("HEAD"), cancellable = true)
   private static void l(WaterOverlay var0, PoseStack var1, SubmitNodeCollector var2, CallbackInfo var3) {
      if (RemovalsModule.G("water")) {
         var3.cancel();
      }
   }

   @Inject(method = "submitBlockSprite", at = @At("HEAD"), cancellable = true)
   private static void Y(Identifier var0, float var1, float var2, float var3, float var4, PoseStack var5, SubmitNodeCollector var6, int var7, CallbackInfo var8) {
      if (RemovalsModule.G("clip")) {
         var8.cancel();
      }
   }
}
