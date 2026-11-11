package com.larpclient.mixin;

import com.larpclient.core.ViewController;
import com.larpclient.module.render.FreecamModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin_2 {
   @ModifyExpressionValue(method = "renderItemInHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/CameraType;isFirstPerson()Z"))
   private boolean g(boolean var1) {
      return ViewController.isFreecam() ? FreecamModule.shouldShowHands() : var1;
   }
}
