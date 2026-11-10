package com.larp.obf.q41a5851bcf8e876a.q2d3f5242fd3d8828;

import com.larpclient.vb;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public class q627e100e8815612d {
   @Inject(method = "dropItem", at = @At("HEAD"), cancellable = true)
   private void m(LocalPlayer var1, boolean var2, CallbackInfo var3) {
      if (vb.e(Minecraft.getInstance(), var2)) {
         var3.cancel();
      }
   }
}
