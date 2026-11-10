package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.q1;
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
public class qcf3bf12943ac1810 {
   @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;Lio/netty/channel/ChannelFutureListener;Z)V", at = @At("HEAD"), cancellable = true)
   private void C(Packet<?> var1, ChannelFutureListener var2, boolean var3, CallbackInfo var4) {
      if (!BacktrackModule.Z()) {
         if (FakePayModule.O(var1)) {
            var4.cancel();
         } else if (BacktrackModule.F() && BacktrackModule.H((Connection)this, var1, var2, var3)) {
            var4.cancel();
         } else if (FakeLagModule.O() && FakeLagModule.M((Connection)this, var1, var2, var3)) {
            var4.cancel();
         } else {
            q1.S(var1);
         }
      }
   }

   @Inject(method = "genericsFtw", at = @At("HEAD"))
   private static void j(Packet<?> var0, PacketListener var1, CallbackInfo var2) {
      BacktrackModule.a(var0, var1);
   }
}
