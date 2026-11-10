package com.larp.obf.qa300483f54d804a3;

import com.larpclient.kq;
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
public class q50f973251e840c66 {
   @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;Lio/netty/channel/ChannelFutureListener;Z)V", at = @At("HEAD"), cancellable = true)
   private void q(Packet<?> var1, ChannelFutureListener var2, boolean var3, CallbackInfo var4) {
      if (!BacktrackModule.z()) {
         if (FakePayModule.j(var1)) {
            var4.cancel();
         } else if (BacktrackModule.E() && BacktrackModule.g((Connection)this, var1, var2, var3)) {
            var4.cancel();
         } else if (FakeLagModule.M() && FakeLagModule.C((Connection)this, var1, var2, var3)) {
            var4.cancel();
         } else {
            kq.X(var1);
         }
      }
   }

   @Inject(method = "genericsFtw", at = @At("HEAD"))
   private static void Y(Packet<?> var0, PacketListener var1, CallbackInfo var2) {
      BacktrackModule.k(var0, var1);
   }
}
