package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.ih;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import java.net.InetAddress;
import net.minecraft.client.multiplayer.ServerStatusPinger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerStatusPinger.class)
public abstract class qca8864b9b3bb4d26 {
   @Redirect(
      method = "pingLegacyServer",
      at = @At(value = "INVOKE", target = "Lio/netty/bootstrap/Bootstrap;connect(Ljava/net/InetAddress;I)Lio/netty/channel/ChannelFuture;")
   )
   private ChannelFuture X(Bootstrap var1, InetAddress var2, int var3) {
      return ih.b(var1, var2, var3);
   }
}
