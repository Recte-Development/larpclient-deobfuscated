package com.larp.obf.qa300483f54d804a3;

import com.larpclient.p6;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import java.net.InetAddress;
import net.minecraft.client.multiplayer.ServerStatusPinger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerStatusPinger.class)
public abstract class qb79c82dcf363f0a3 {
   @Redirect(
      method = "pingLegacyServer",
      at = @At(value = "INVOKE", target = "Lio/netty/bootstrap/Bootstrap;connect(Ljava/net/InetAddress;I)Lio/netty/channel/ChannelFuture;")
   )
   private ChannelFuture c(Bootstrap var1, InetAddress var2, int var3) {
      return p6.M(var1, var2, var3);
   }
}
