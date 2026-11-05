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

public final class _v {
   public static final AttributeKey<h4.o> o = AttributeKey.valueOf("larp_proxy_route");
   private static volatile _v.t e = new _v.t(null, null);

   private _v() {
   }

   public static h4.o B() {
      return e.active();
   }

   public static String u() {
      return e.blocked();
   }

   public static void G(h4.o var0) {
      e = new _v.t(Objects.requireNonNull(var0), null);
      r4.V("Proxy", "Server traffic now uses {} ({})", var0.settings().label(), var0.settings().authenticated() ? "with login" : "no login");
   }

   public static void U() {
      if (e.active() != null || e.blocked() != null) {
         r4.V("Proxy", "Proxy off; server traffic connects directly");
      }

      e = new _v.t(null, null);
   }

   public static void q(String var0) {
      e = new _v.t(null, Objects.requireNonNull(var0));
      r4.m("Proxy", "Server connections paused: {}", var0);
   }

   public static ChannelFuture b(Bootstrap var0, InetAddress var1, int var2) {
      _v.t var3 = e;
      if (!x(var1)) {
         if (var3.blocked() != null) {
            throw new UncheckedIOException(new xj.s(var3.blocked()));
         }

         if (var3.active() == null) {
            return var0.connect(var1, var2);
         }

         var0.handler(new _v.i(var3.active(), var0.config().handler()));
         r4.g("Proxy", "Opening a server connection through {}", var3.active().settings().label());
         return var0.connect(var1, var2);
      } else {
         if (var3.active() != null || var3.blocked() != null) {
            r4.g("Proxy", "Local server address; connecting directly");
         }

         return var0.connect(var1, var2);
      }
   }

   public static void o(SessionService var0, Channel var1, UUID var2, String var3, String var4) throws AuthenticationException {
      h4.o var5 = var1 == null ? null : (h4.o)var1.attr(o).get();
      if (var5 != null) {
         f0.C(var5, var2, var3, var4);
      } else {
         int var6 = var1 != null && var1.remoteAddress() instanceof InetSocketAddress var7 && var7.getAddress() != null && x(var7.getAddress()) ? 1 : 0;
         if (var6 == 0 && (e.active() != null || e.blocked() != null)) {
            r4.m("Proxy", "A direct server connection is logging in while the proxy is on; it was opened before the proxy changed");
         }

         var0.joinServer(var2, var3, var4);
      }
   }

   public static boolean x(InetAddress var0) {
      return var0.isLoopbackAddress()
         || var0.isAnyLocalAddress()
         || var0.isLinkLocalAddress()
         || var0.isSiteLocalAddress()
         || var0 instanceof Inet6Address && (var0.getAddress()[0] & 254) == 252;
   }

   public static long h(h4.o var0) throws IOException, InterruptedException {
      return f0.r(var0);
   }

   static final class i extends ChannelInitializer<Channel> {
      private final h4.o N;
      private final ChannelHandler c;

      i(h4.o var1, ChannelHandler var2) {
         this.N = var1;
         this.c = Objects.requireNonNull(var2);
      }

      protected void initChannel(Channel var1) {
         var1.attr(_v.o).set(this.N);
         var1.pipeline().addLast(new ChannelHandler[]{this.c}).addFirst("larp_proxy", new xj(this.N));
      }
   }

   private record t(h4.o active, String blocked) {
   }
}
