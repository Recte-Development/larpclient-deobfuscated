package com.larp.obf.qbac0b262827a15ac;

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
public class q3944a9825b94232c {
   @Inject(method = "submitFire", at = @At("HEAD"), cancellable = true)
   private static void z(PoseStack var0, SubmitNodeCollector var1, TextureAtlasSprite var2, CallbackInfo var3) {
      if (RemovalsModule.r("fire")) {
         var3.cancel();
      }
   }

   @Inject(method = "submitWater", at = @At("HEAD"), cancellable = true)
   private static void K(WaterOverlay var0, PoseStack var1, SubmitNodeCollector var2, CallbackInfo var3) {
      if (RemovalsModule.r("water")) {
         var3.cancel();
      }
   }

   @Inject(method = "submitBlockSprite", at = @At("HEAD"), cancellable = true)
   private static void i(Identifier var0, float var1, float var2, float var3, float var4, PoseStack var5, SubmitNodeCollector var6, int var7, CallbackInfo var8) {
      if (RemovalsModule.r("clip")) {
         var8.cancel();
      }
   }
}
