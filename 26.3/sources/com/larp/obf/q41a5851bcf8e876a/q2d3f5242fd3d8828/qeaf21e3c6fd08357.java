package com.larp.obf.q41a5851bcf8e876a.q2d3f5242fd3d8828;

import com.larpclient.q5;
import com.larpclient.wj;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public class qeaf21e3c6fd08357 {
   @Inject(method = "dropItem", at = @At("HEAD"), cancellable = true)
   private void d(LocalPlayer var1, boolean var2, CallbackInfo var3) {
      if (wj.m().Q() || q5.a().C()) {
         var3.cancel();
      }
   }
}
