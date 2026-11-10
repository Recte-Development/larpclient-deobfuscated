package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.ih;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import java.net.InetAddress;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Connection.class)
public abstract class qd423dff7c9e0cedb {
   @Redirect(
      method = "connect(Ljava/net/InetSocketAddress;Lnet/minecraft/server/network/EventLoopGroupHolder;Lnet/minecraft/network/Connection;)Lio/netty/channel/ChannelFuture;",
      at = @At(value = "INVOKE", target = "Lio/netty/bootstrap/Bootstrap;connect(Ljava/net/InetAddress;I)Lio/netty/channel/ChannelFuture;")
   )
   private static ChannelFuture T(Bootstrap var0, InetAddress var1, int var2) {
      return ih.b(var0, var1, var2);
   }
}
