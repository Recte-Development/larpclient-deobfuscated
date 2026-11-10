package com.larp.obf.qa300483f54d804a3;

import com.larpclient.p_;
import com.larpclient.module.FreecamModule;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public abstract class qd496c43710393413 {
   @ModifyExpressionValue(method = "renderItemInHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/CameraType;isFirstPerson()Z"))
   private boolean b(boolean var1) {
      return p_.V() ? FreecamModule.F() : var1;
   }
}
