package com.larp.obf.qbac0b262827a15ac.q03e2e72dbe55651a;

import com.larpclient._a;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class q885fb3022fc1dfb5 {
   @Inject(method = "closeContainer", at = @At("HEAD"))
   private void G(CallbackInfo var1) {
      _a.X(Minecraft.getInstance());
   }
}
