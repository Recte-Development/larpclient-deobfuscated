package com.larp.obf.q41a5851bcf8e876a.q040cb7981516518c;

import com.larpclient.ij;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.player.FirstPersonHandsAndItems;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FirstPersonHandsAndItems.class)
public abstract class qfc332a0c76e451b9 {
   @WrapOperation(
      method = "extractRenderState",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getViewYRot(F)F"),
      require = 1,
      allow = 1
   )
   private float z(LocalPlayer var1, float var2, Operation<Float> var3) {
      float var4 = ij.G(Mth.lerp(var2, var1.yBobO, var1.yBob));
      return Float.isNaN(var4) ? (Float)var3.call(new Object[]{var1, var2}) : var4;
   }

   @WrapOperation(
      method = "extractRenderState",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getViewXRot(F)F"),
      require = 1,
      allow = 1
   )
   private float w(LocalPlayer var1, float var2, Operation<Float> var3) {
      float var4 = ij.M();
      return Float.isNaN(var4) ? (Float)var3.call(new Object[]{var1, var2}) : var4;
   }
}
