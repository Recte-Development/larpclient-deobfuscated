package com.larp.obf.q41a5851bcf8e876a.q2d3f5242fd3d8828;

import com.larpclient.qm;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public class q4fd7aeb84691e89d {
   @Inject(method = "dropItem", at = @At("HEAD"), cancellable = true)
   private void F(LocalPlayer var1, boolean var2, CallbackInfo var3) {
      if (!qm.Q(var2)) {
         var3.cancel();
      } else {
         qm.p();
      }
   }
}
