package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.rr;
import net.minecraft.client.player.ClientInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientInput.class)
public class q8704afdbb780e033 {
   @Inject(method = "makeJump", at = @At("HEAD"), cancellable = true)
   private void b(CallbackInfo var1) {
      if (rr.o()) {
         var1.cancel();
      }
   }
}
