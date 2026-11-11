package com.larpclient.mixin;

import com.larpclient.util.entity.AimOverride;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin2 {
   @WrapOperation(method = "sendPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getYRot()F"))
   private float w(LocalPlayer var1, Operation<Float> var2) {
      return AimOverride.getYaw(var1);
   }

   @WrapOperation(method = "sendPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getXRot()F"))
   private float e(LocalPlayer var1, Operation<Float> var2) {
      return AimOverride.getPitch(var1);
   }

   @Inject(method = "sendPosition", at = @At("RETURN"))
   private void m(CallbackInfo var1) {
      AimOverride.tick((LocalPlayer)this);
   }
}
