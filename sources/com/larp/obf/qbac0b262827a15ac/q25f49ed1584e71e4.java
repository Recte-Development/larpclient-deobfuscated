package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.xo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import java.net.InetAddress;
import net.minecraft.client.multiplayer.ServerStatusPinger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerStatusPinger.class)
public abstract class q25f49ed1584e71e4 {
   @Redirect(
      method = "pingLegacyServer",
      at = @At(value = "INVOKE", target = "Lio/netty/bootstrap/Bootstrap;connect(Ljava/net/InetAddress;I)Lio/netty/channel/ChannelFuture;")
   )
   private ChannelFuture k(Bootstrap var1, InetAddress var2, int var3) {
      return xo.c(var1, var2, var3);
   }
}
