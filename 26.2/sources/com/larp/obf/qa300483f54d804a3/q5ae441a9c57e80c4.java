package com.larp.obf.qa300483f54d804a3;

import com.larpclient.p_;
import net.minecraft.client.player.ClientInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientInput.class)
public class q5ae441a9c57e80c4 {
   @Inject(method = "makeJump", at = @At("HEAD"), cancellable = true)
   private void w(CallbackInfo var1) {
      if (p_.V()) {
         var1.cancel();
      }
   }
}
