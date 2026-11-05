package com.larp.obf.qbac0b262827a15ac.q03e2e72dbe55651a;

import com.larpclient.pb;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class q532e232363c4407d {
   @WrapOperation(method = "sendPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getYRot()F"))
   private float q(LocalPlayer var1, Operation<Float> var2) {
      return pb.T(var1);
   }

   @WrapOperation(method = "sendPosition", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getXRot()F"))
   private float m(LocalPlayer var1, Operation<Float> var2) {
      return pb.U(var1);
   }

   @Inject(method = "sendPosition", at = @At("RETURN"))
   private void M(CallbackInfo var1) {
      pb.W((LocalPlayer)this);
   }
}
