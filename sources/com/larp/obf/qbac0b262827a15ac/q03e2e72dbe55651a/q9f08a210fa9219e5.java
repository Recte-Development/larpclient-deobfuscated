package com.larp.obf.qbac0b262827a15ac.q03e2e72dbe55651a;

import com.larpclient.w5;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public class q9f08a210fa9219e5 {
   @Inject(method = "dropItem", at = @At("HEAD"), cancellable = true)
   private void H(LocalPlayer var1, boolean var2, CallbackInfo var3) {
      if (!w5.k(var2)) {
         var3.cancel();
      } else {
         w5.Z();
      }
   }
}
