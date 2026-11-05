package com.larp.obf.qbac0b262827a15ac.q03e2e72dbe55651a;

import com.larpclient._a;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public class q6ba411a94e4e9fb7 {
   @Inject(method = "dropItem", at = @At("HEAD"), cancellable = true)
   private void u(LocalPlayer var1, boolean var2, CallbackInfo var3) {
      if (_a.F(Minecraft.getInstance(), var2)) {
         var3.cancel();
      }
   }
}
