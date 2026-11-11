package com.larpclient.mixin;

import com.larpclient.network.ProxyRouter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import java.net.InetAddress;
import net.minecraft.client.multiplayer.ServerStatusPinger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerStatusPinger.class)
public abstract class ServerStatusPingerMixin {
   @Redirect(
      method = "pingLegacyServer",
      at = @At(value = "INVOKE", target = "Lio/netty/bootstrap/Bootstrap;connect(Ljava/net/InetAddress;I)Lio/netty/channel/ChannelFuture;")
   )
   private ChannelFuture X(Bootstrap var1, InetAddress var2, int var3) {
      return ProxyRouter.connect(var1, var2, var3);
   }
}
