package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient._v;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import java.net.InetAddress;
import net.minecraft.client.multiplayer.ServerStatusPinger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerStatusPinger.class)
public abstract class q9bf61f3619a1af51 {
   @Redirect(
      method = "pingLegacyServer",
      at = @At(value = "INVOKE", target = "Lio/netty/bootstrap/Bootstrap;connect(Ljava/net/InetAddress;I)Lio/netty/channel/ChannelFuture;")
   )
   private ChannelFuture f(Bootstrap var1, InetAddress var2, int var3) {
      return _v.b(var1, var2, var3);
   }
}
