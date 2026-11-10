package com.larp.obf.qa300483f54d804a3.q44c0827ad90befbb;

import com.larpclient.mg;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class q123d74580a87ff22 {
   @Inject(method = "drop", at = @At("HEAD"), cancellable = true)
   private void X(boolean var1, CallbackInfoReturnable<Boolean> var2) {
      if (!mg.C(var1)) {
         var2.setReturnValue(false);
      } else {
         mg.V();
      }
   }
}
