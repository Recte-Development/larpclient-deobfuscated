package com.larp.obf.qa300483f54d804a3.q44c0827ad90befbb;

import com.larpclient.sz;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class qf8da0ef850aa038b {
   @Inject(method = "drop", at = @At("HEAD"), cancellable = true)
   private void Y(boolean var1, CallbackInfoReturnable<Boolean> var2) {
      if (sz.D(Minecraft.getInstance(), var1)) {
         var2.setReturnValue(true);
      }
   }
}
