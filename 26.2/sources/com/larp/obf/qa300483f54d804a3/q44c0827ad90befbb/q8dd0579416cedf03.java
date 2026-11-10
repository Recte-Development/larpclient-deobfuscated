package com.larp.obf.qa300483f54d804a3.q44c0827ad90befbb;

import com.larpclient.lh;
import com.larpclient.pw;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class q8dd0579416cedf03 {
   @Inject(method = "drop", at = @At("HEAD"), cancellable = true)
   private void s(boolean var1, CallbackInfoReturnable<Boolean> var2) {
      if (pw.S().C() || lh.I().A()) {
         var2.setReturnValue(false);
      }
   }
}
