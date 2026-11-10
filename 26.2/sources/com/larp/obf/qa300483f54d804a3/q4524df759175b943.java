package com.larp.obf.qa300483f54d804a3;

import com.larpclient.module.RemovalsModule;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class q4524df759175b943 {
   @Inject(method = "submitFire", at = @At("HEAD"), cancellable = true)
   private static void l(PoseStack var0, SubmitNodeCollector var1, TextureAtlasSprite var2, CallbackInfo var3) {
      if (RemovalsModule.n("fire")) {
         var3.cancel();
      }
   }

   @Inject(method = "submitWater", at = @At("HEAD"), cancellable = true)
   private static void q(Minecraft var0, PoseStack var1, SubmitNodeCollector var2, CallbackInfo var3) {
      if (RemovalsModule.n("water")) {
         var3.cancel();
      }
   }

   @Inject(method = "submitBlockSprite", at = @At("HEAD"), cancellable = true)
   private static void s(TextureAtlasSprite var0, PoseStack var1, SubmitNodeCollector var2, int var3, CallbackInfo var4) {
      if (RemovalsModule.n("clip")) {
         var4.cancel();
      }
   }
}
