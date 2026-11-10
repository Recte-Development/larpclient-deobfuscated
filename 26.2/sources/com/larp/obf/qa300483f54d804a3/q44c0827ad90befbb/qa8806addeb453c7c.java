package com.larp.obf.qa300483f54d804a3.q44c0827ad90befbb;

import com.larpclient.tj;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class qa8806addeb453c7c {
   @WrapOperation(method = "sendPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getYRot()F"))
   private float v(LocalPlayer var1, Operation<Float> var2) {
      return tj.x(var1);
   }

   @WrapOperation(method = "sendPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getXRot()F"))
   private float o(LocalPlayer var1, Operation<Float> var2) {
      return tj.H(var1);
   }

   @Inject(method = "sendPosition", at = @At("RETURN"))
   private void S(CallbackInfo var1) {
      tj.q((LocalPlayer)this);
   }
}
