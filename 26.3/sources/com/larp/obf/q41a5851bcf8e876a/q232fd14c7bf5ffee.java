package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.ij;
import net.minecraft.client.player.ClientInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientInput.class)
public class q232fd14c7bf5ffee {
   @Inject(method = "makeJump", at = @At("HEAD"), cancellable = true)
   private void f(CallbackInfo var1) {
      if (ij.y()) {
         var1.cancel();
      }
   }
}
