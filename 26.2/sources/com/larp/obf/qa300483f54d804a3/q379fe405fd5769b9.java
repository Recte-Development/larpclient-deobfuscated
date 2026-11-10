package com.larp.obf.qa300483f54d804a3;

import com.larpclient.j6;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class q379fe405fd5769b9 {
   @Inject(method = "getViewYRot(F)F", at = @At("HEAD"), cancellable = true, require = 1)
   private void N(float var1, CallbackInfoReturnable<Float> var2) {
      float var3 = j6.D((LocalPlayer)this, var1);
      if (!Float.isNaN(var3)) {
         var2.setReturnValue(var3);
      }
   }

   @Inject(method = "getViewXRot(F)F", at = @At("HEAD"), cancellable = true, require = 1)
   private void A(float var1, CallbackInfoReturnable<Float> var2) {
      float var3 = j6.B((LocalPlayer)this, var1);
      if (!Float.isNaN(var3)) {
         var2.setReturnValue(var3);
      }
   }
}
