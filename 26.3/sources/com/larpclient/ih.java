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

public final class ih {
   public static final AttributeKey<ou.o> T = AttributeKey.valueOf("larp_proxy_route");
   private static volatile ih.o i = new ih.o(null, null);

   private ih() {
   }

   public static ou.o g() {
      return i.active();
   }

   public static String T() {
      return i.blocked();
   }

   public static void A(ou.o var0) {
      i = new ih.o(Objects.requireNonNull(var0), null);
      _q.z("Proxy", "Server traffic now uses {} ({})", var0.settings().label(), var0.settings().authenticated() ? "with login" : "no login");
   }

   public static void p() {
      if (i.active() != null || i.blocked() != null) {
         _q.z("Proxy", "Proxy off; server traffic connects directly");
      }

      i = new ih.o(null, null);
   }

   public static void D(String var0) {
      i = new ih.o(null, Objects.requireNonNull(var0));
      _q.R("Proxy", "Server connections paused: {}", var0);
   }

   public static ChannelFuture b(Bootstrap var0, InetAddress var1, int var2) {
      ih.o var3 = i;
      if (!N(var1)) {
         if (var3.blocked() != null) {
            throw new UncheckedIOException(new bu.j(var3.blocked()));
         }

         if (var3.active() == null) {
            return var0.connect(var1, var2);
         }

         var0.handler(new ih.a(var3.active(), var0.config().handler()));
         _q.c("Proxy", "Opening a server connection through {}", var3.active().settings().label());
         return var0.connect(var1, var2);
      } else {
         if (var3.active() != null || var3.blocked() != null) {
            _q.c("Proxy", "Local server address; connecting directly");
         }

         return var0.connect(var1, var2);
      }
   }

   public static void w(SessionService var0, Channel var1, UUID var2, String var3, String var4) throws AuthenticationException {
      ou.o var5 = var1 == null ? null : (ou.o)var1.attr(T).get();
      if (var5 != null) {
         no.y(var5, var2, var3, var4);
      } else {
         int var6 = var1 != null && var1.remoteAddress() instanceof InetSocketAddress var7 && var7.getAddress() != null && N(var7.getAddress()) ? 1 : 0;
         if (var6 == 0 && (i.active() != null || i.blocked() != null)) {
            _q.R("Proxy", "A direct server connection is logging in while the proxy is on; it was opened before the proxy changed");
         }

         var0.joinServer(var2, var3, var4);
      }
   }

   public static boolean N(InetAddress var0) {
      return var0.isLoopbackAddress()
         || var0.isAnyLocalAddress()
         || var0.isLinkLocalAddress()
         || var0.isSiteLocalAddress()
         || var0 instanceof Inet6Address && (var0.getAddress()[0] & 254) == 252;
   }

   public static long M(ou.o var0) throws IOException, InterruptedException {
      return no.L(var0);
   }

   static final class a extends ChannelInitializer<Channel> {
      private final ou.o C;
      private final ChannelHandler P;

      a(ou.o var1, ChannelHandler var2) {
         this.C = var1;
         this.P = Objects.requireNonNull(var2);
      }

      protected void initChannel(Channel var1) {
         var1.attr(ih.T).set(this.C);
         var1.pipeline().addLast(new ChannelHandler[]{this.P}).addFirst("larp_proxy", new bu(this.C));
      }
   }

   private record o(ou.o active, String blocked) {
   }
}
