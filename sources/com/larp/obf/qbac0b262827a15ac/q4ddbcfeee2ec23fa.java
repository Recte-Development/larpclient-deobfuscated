package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.tq;
import com.larpclient.module.FreecamModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public abstract class q4ddbcfeee2ec23fa {
   @ModifyExpressionValue(method = "renderItemInHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/CameraType;isFirstPerson()Z"))
   private boolean g(boolean var1) {
      return tq.f() ? FreecamModule.L() : var1;
   }
}
