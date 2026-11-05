package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.xq;
import com.larpclient.module.BacktrackModule;
import com.larpclient.module.FakeLagModule;
import com.larpclient.module.FakePayModule;
import io.netty.channel.ChannelFutureListener;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public class q7e4e0779e2fb9e25 {
   @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;Lio/netty/channel/ChannelFutureListener;Z)V", at = @At("HEAD"), cancellable = true)
   private void F(Packet<?> var1, ChannelFutureListener var2, boolean var3, CallbackInfo var4) {
      if (!BacktrackModule.u()) {
         if (FakePayModule.v(var1)) {
            var4.cancel();
         } else if (BacktrackModule.h() && BacktrackModule.P((Connection)this, var1, var2, var3)) {
            var4.cancel();
         } else if (FakeLagModule.z() && FakeLagModule.Y((Connection)this, var1, var2, var3)) {
            var4.cancel();
         } else {
            xq.V(var1);
         }
      }
   }

   @Inject(method = "genericsFtw", at = @At("HEAD"))
   private static void H(Packet<?> var0, PacketListener var1, CallbackInfo var2) {
      BacktrackModule.C(var0, var1);
   }
}
