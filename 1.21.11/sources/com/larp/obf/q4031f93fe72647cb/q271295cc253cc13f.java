package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.gf;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import java.net.InetAddress;
import net.minecraft.class_644;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(class_644.class)
public abstract class q271295cc253cc13f {
   @Redirect(
      method = "method_3001",
      at = @At(value = "INVOKE", target = "Lio/netty/bootstrap/Bootstrap;connect(Ljava/net/InetAddress;I)Lio/netty/channel/ChannelFuture;")
   )
   private ChannelFuture t(Bootstrap var1, InetAddress var2, int var3) {
      return gf.I(var1, var2, var3);
   }
}
