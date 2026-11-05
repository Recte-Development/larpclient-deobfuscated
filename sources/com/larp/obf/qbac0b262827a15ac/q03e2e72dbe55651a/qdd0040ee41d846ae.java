package com.larp.obf.qbac0b262827a15ac.q03e2e72dbe55651a;

import com.larpclient.c6;
import com.larpclient.kq;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public class qdd0040ee41d846ae {
   @Inject(method = "dropItem", at = @At("HEAD"), cancellable = true)
   private void F(LocalPlayer var1, boolean var2, CallbackInfo var3) {
      if (c6.K().j() || kq.I().j()) {
         var3.cancel();
      }
   }
}
