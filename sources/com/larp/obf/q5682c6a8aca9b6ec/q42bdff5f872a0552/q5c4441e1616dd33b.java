package com.larp.obf.q5682c6a8aca9b6ec.q42bdff5f872a0552;

import com.larpclient.yg;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public class q5c4441e1616dd33b {
   @Inject(method = "dropItem", at = @At("HEAD"), cancellable = true)
   private void D(LocalPlayer var1, boolean var2, CallbackInfo var3) {
      if (!yg.t(var2)) {
         var3.cancel();
      } else {
         yg.T();
      }
   }
}
