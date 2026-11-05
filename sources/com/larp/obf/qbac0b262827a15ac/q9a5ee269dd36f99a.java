package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.tq;
import net.minecraft.client.player.ClientInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientInput.class)
public class q9a5ee269dd36f99a {
   @Inject(method = "makeJump", at = @At("HEAD"), cancellable = true)
   private void E(CallbackInfo var1) {
      if (tq.f()) {
         var1.cancel();
      }
   }
}
