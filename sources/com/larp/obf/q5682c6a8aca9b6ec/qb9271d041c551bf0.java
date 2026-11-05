package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.rr;
import com.larpclient.module.FreecamModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public abstract class qb9271d041c551bf0 {
   @ModifyExpressionValue(method = "renderItemInHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/CameraType;isFirstPerson()Z"))
   private boolean F(boolean var1) {
      return rr.o() ? FreecamModule.U() : var1;
   }
}
