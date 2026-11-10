package com.larp.obf.qa300483f54d804a3.q44c0827ad90befbb;

import com.larpclient.sz;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class q4eb328ebe1fc37c6 {
   @Inject(method = "closeContainer", at = @At("HEAD"))
   private void Y(CallbackInfo var1) {
      sz.f(Minecraft.getInstance());
   }
}
