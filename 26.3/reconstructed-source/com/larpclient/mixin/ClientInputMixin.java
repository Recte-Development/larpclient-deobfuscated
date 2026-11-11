package com.larpclient.mixin;

import com.larpclient.core.ViewController;
import net.minecraft.client.player.ClientInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientInput.class)
public class ClientInputMixin {
   @Inject(method = "makeJump", at = @At("HEAD"), cancellable = true)
   private void f(CallbackInfo var1) {
      if (ViewController.isFreecam()) {
         var1.cancel();
      }
   }
}
