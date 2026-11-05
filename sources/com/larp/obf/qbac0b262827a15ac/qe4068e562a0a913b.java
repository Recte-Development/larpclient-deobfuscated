package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.pl;
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
public class qe4068e562a0a913b {
   @Inject(method = "send(Lnet/minecraft/network/protocol/Packet;Lio/netty/channel/ChannelFutureListener;Z)V", at = @At("HEAD"), cancellable = true)
   private void K(Packet<?> var1, ChannelFutureListener var2, boolean var3, CallbackInfo var4) {
      if (!BacktrackModule.C()) {
         if (FakePayModule.V(var1)) {
            var4.cancel();
         } else if (BacktrackModule.Z() && BacktrackModule.o((Connection)this, var1, var2, var3)) {
            var4.cancel();
         } else if (FakeLagModule.V() && FakeLagModule.L((Connection)this, var1, var2, var3)) {
            var4.cancel();
         } else {
            pl.l(var1);
         }
      }
   }

   @Inject(method = "genericsFtw", at = @At("HEAD"))
   private static void f(Packet<?> var0, PacketListener var1, CallbackInfo var2) {
      BacktrackModule.I(var0, var1);
   }
}
