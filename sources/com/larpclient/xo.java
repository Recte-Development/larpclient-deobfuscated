package com.larpclient;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.minecraft.SessionService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.util.AttributeKey;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.UUID;

public final class xo {
   public static final AttributeKey<oe.u> O = AttributeKey.valueOf("larp_proxy_route");
   private static volatile xo.y x = new xo.y(null, null);

   private xo() {
   }

   public static oe.u y() {
      return x.active();
   }

   public static String x() {
      return x.blocked();
   }

   public static void o(oe.u var0) {
      x = new xo.y(Objects.requireNonNull(var0), null);
      mt.K("Proxy", "Server traffic now uses {} ({})", var0.settings().label(), var0.settings().authenticated() ? "with login" : "no login");
   }

   public static void j() {
      if (x.active() != null || x.blocked() != null) {
         mt.K("Proxy", "Proxy off; server traffic connects directly");
      }

      x = new xo.y(null, null);
   }

   public static void a(String var0) {
      x = new xo.y(null, Objects.requireNonNull(var0));
      mt.T("Proxy", "Server connections paused: {}", var0);
   }

   public static ChannelFuture c(Bootstrap var0, InetAddress var1, int var2) {
      xo.y var3 = x;
      if (!g(var1)) {
         if (var3.blocked() != null) {
            throw new UncheckedIOException(new u2.i(var3.blocked()));
         }

         if (var3.active() == null) {
            return var0.connect(var1, var2);
         }

         var0.handler(new xo.i(var3.active(), var0.config().handler()));
         mt.U("Proxy", "Opening a server connection through {}", var3.active().settings().label());
         return var0.connect(var1, var2);
      } else {
         if (var3.active() != null || var3.blocked() != null) {
            mt.U("Proxy", "Local server address; connecting directly");
         }

         return var0.connect(var1, var2);
      }
   }

   public static void Z(SessionService var0, Channel var1, UUID var2, String var3, String var4) throws AuthenticationException {
      oe.u var5 = var1 == null ? null : (oe.u)var1.attr(O).get();
      if (var5 != null) {
         xj.Z(var5, var2, var3, var4);
      } else {
         int var6 = var1 != null && var1.remoteAddress() instanceof InetSocketAddress var7 && var7.getAddress() != null && g(var7.getAddress()) ? 1 : 0;
         if (var6 == 0 && (x.active() != null || x.blocked() != null)) {
            mt.T("Proxy", "A direct server connection is logging in while the proxy is on; it was opened before the proxy changed");
         }

         var0.joinServer(var2, var3, var4);
      }
   }

   public static boolean g(InetAddress var0) {
      return var0.isLoopbackAddress()
         || var0.isAnyLocalAddress()
         || var0.isLinkLocalAddress()
         || var0.isSiteLocalAddress()
         || var0 instanceof Inet6Address && (var0.getAddress()[0] & 254) == 252;
   }

   public static long P(oe.u var0) throws IOException, InterruptedException {
      return xj.Y(var0);
   }

   static final class i extends ChannelInitializer<Channel> {
      private final oe.u Y;
      private final ChannelHandler B;

      i(oe.u var1, ChannelHandler var2) {
         this.Y = var1;
         this.B = Objects.requireNonNull(var2);
      }

      protected void initChannel(Channel var1) {
         var1.attr(xo.O).set(this.Y);
         var1.pipeline().addLast(new ChannelHandler[]{this.B}).addFirst("larp_proxy", new u2(this.Y));
      }
   }

   private record y(oe.u active, String blocked) {
   }
}
