package com.larpclient.mixin;

import com.larpclient.core.RotationManager;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {
   @Inject(method = "getViewYRot(F)F", at = @At("HEAD"), cancellable = true, require = 1)
   private void T(float var1, CallbackInfoReturnable<Float> var2) {
      float var3 = RotationManager.getInterpolatedYaw((LocalPlayer)this, var1);
      if (!Float.isNaN(var3)) {
         var2.setReturnValue(var3);
      }
   }

   public float getViewXRot(float var1) {
      LocalPlayer var2 = (LocalPlayer)this;
      float var3 = RotationManager.getInterpolatedPitch(var2, var1);
      return Float.isNaN(var3) ? var2.getXRot(var1) : var3;
   }
}
