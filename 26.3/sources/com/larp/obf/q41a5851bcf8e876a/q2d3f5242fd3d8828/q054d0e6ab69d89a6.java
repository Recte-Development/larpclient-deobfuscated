package com.larp.obf.q41a5851bcf8e876a.q2d3f5242fd3d8828;

import com.larpclient.w8;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class q054d0e6ab69d89a6 {
   @WrapOperation(method = "sendPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getYRot()F"))
   private float w(LocalPlayer var1, Operation<Float> var2) {
      return w8.d(var1);
   }

   @WrapOperation(method = "sendPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getXRot()F"))
   private float e(LocalPlayer var1, Operation<Float> var2) {
      return w8.t(var1);
   }

   @Inject(method = "sendPosition", at = @At("RETURN"))
   private void m(CallbackInfo var1) {
      w8.o((LocalPlayer)this);
   }
}
