package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.ij;
import com.larpclient.module.FreecamModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public abstract class q9fcb97d7a89a6ef4 {
   @ModifyExpressionValue(method = "renderItemInHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/CameraType;isFirstPerson()Z"))
   private boolean g(boolean var1) {
      return ij.y() ? FreecamModule.K() : var1;
   }
}
