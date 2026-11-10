package com.larp.obf.q41a5851bcf8e876a.qb75a226e201a934e;

import com.larpclient._z;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class q142db25b4ea2d12b {
   @Inject(method = "setTitleText", at = @At("HEAD"))
   private void N(ClientboundSetTitleTextPacket var1, CallbackInfo var2) {
      if (var1.text() != null) {
         _z.f(var1.text().getString());
      }
   }

   @Inject(method = "setSubtitleText", at = @At("HEAD"))
   private void R(ClientboundSetSubtitleTextPacket var1, CallbackInfo var2) {
      if (var1.text() != null) {
         _z.f(var1.text().getString());
      }
   }
}
