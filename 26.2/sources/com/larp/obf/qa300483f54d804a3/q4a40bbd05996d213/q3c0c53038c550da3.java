package com.larp.obf.qa300483f54d804a3.q4a40bbd05996d213;

import com.larpclient.a4;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class q3c0c53038c550da3 {
   @Inject(method = "setTitleText", at = @At("HEAD"))
   private void b(ClientboundSetTitleTextPacket var1, CallbackInfo var2) {
      if (var1.text() != null) {
         a4.F(var1.text().getString());
      }
   }

   @Inject(method = "setSubtitleText", at = @At("HEAD"))
   private void C(ClientboundSetSubtitleTextPacket var1, CallbackInfo var2) {
      if (var1.text() != null) {
         a4.F(var1.text().getString());
      }
   }
}
