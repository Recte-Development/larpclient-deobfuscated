package com.larp.obf.q5682c6a8aca9b6ec.q42bdff5f872a0552;

import com.larpclient._s;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class qf7d8621ac2a5b361 {
   @WrapOperation(method = "sendPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getYRot()F"))
   private float k(LocalPlayer var1, Operation<Float> var2) {
      return _s.Q(var1);
   }

   @WrapOperation(method = "sendPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getXRot()F"))
   private float j(LocalPlayer var1, Operation<Float> var2) {
      return _s.R(var1);
   }

   @Inject(method = "sendPosition", at = @At("RETURN"))
   private void x(CallbackInfo var1) {
      _s.E((LocalPlayer)this);
   }
}
