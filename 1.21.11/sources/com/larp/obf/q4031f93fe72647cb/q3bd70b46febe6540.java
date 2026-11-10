package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.gf;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import java.net.InetAddress;
import net.minecraft.class_2535;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(class_2535.class)
public abstract class q3bd70b46febe6540 {
   @Redirect(
      method = "method_52271(Ljava/net/InetSocketAddress;Lnet/minecraft/class_12239;Lnet/minecraft/class_2535;)Lio/netty/channel/ChannelFuture;",
      at = @At(value = "INVOKE", target = "Lio/netty/bootstrap/Bootstrap;connect(Ljava/net/InetAddress;I)Lio/netty/channel/ChannelFuture;")
   )
   private static ChannelFuture e(Bootstrap var0, InetAddress var1, int var2) {
      return gf.I(var0, var1, var2);
   }
}
