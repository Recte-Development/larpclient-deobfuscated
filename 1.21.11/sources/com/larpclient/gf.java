package com.larpclient;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
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

public final class gf {
   public static final AttributeKey<id.u> d = AttributeKey.valueOf("larp_proxy_route");
   private static volatile gf.o U = new gf.o(null, null);

   private gf() {
   }

   public static id.u Q() {
      return U.active();
   }

   public static String D() {
      return U.blocked();
   }

   public static void W(id.u var0) {
      U = new gf.o(Objects.requireNonNull(var0), null);
      q8.G("Proxy", "Server traffic now uses {} ({})", var0.settings().label(), var0.settings().authenticated() ? "with login" : "no login");
   }

   public static void A() {
      if (U.active() != null || U.blocked() != null) {
         q8.G("Proxy", "Proxy off; server traffic connects directly");
      }

      U = new gf.o(null, null);
   }

   public static void W(String var0) {
      U = new gf.o(null, Objects.requireNonNull(var0));
      q8.L("Proxy", "Server connections paused: {}", var0);
   }

   public static ChannelFuture I(Bootstrap var0, InetAddress var1, int var2) {
      gf.o var3 = U;
      if (!E(var1)) {
         if (var3.blocked() != null) {
            throw new UncheckedIOException(new w9.h(var3.blocked()));
         }

         if (var3.active() == null) {
            return var0.connect(var1, var2);
         }

         var0.handler(new gf.f(var3.active(), var0.config().handler()));
         q8.v("Proxy", "Opening a server connection through {}", var3.active().settings().label());
         return var0.connect(var1, var2);
      } else {
         if (var3.active() != null || var3.blocked() != null) {
            q8.v("Proxy", "Local server address; connecting directly");
         }

         return var0.connect(var1, var2);
      }
   }

   public static void I(MinecraftSessionService var0, Channel var1, UUID var2, String var3, String var4) throws AuthenticationException {
      id.u var5 = var1 == null ? null : (id.u)var1.attr(d).get();
      if (var5 != null) {
         g5.E(var5, var2, var3, var4);
      } else {
         int var6 = var1 != null && var1.remoteAddress() instanceof InetSocketAddress var7 && var7.getAddress() != null && E(var7.getAddress()) ? 1 : 0;
         if (var6 == 0 && (U.active() != null || U.blocked() != null)) {
            q8.L("Proxy", "A direct server connection is logging in while the proxy is on; it was opened before the proxy changed");
         }

         var0.joinServer(var2, var3, var4);
      }
   }

   public static boolean E(InetAddress var0) {
      return var0.isLoopbackAddress()
         || var0.isAnyLocalAddress()
         || var0.isLinkLocalAddress()
         || var0.isSiteLocalAddress()
         || var0 instanceof Inet6Address && (var0.getAddress()[0] & 254) == 252;
   }

   public static long N(id.u var0) throws IOException, InterruptedException {
      return g5.r(var0);
   }

   static final class f extends ChannelInitializer<Channel> {
      private final id.u p;
      private final ChannelHandler y;

      f(id.u var1, ChannelHandler var2) {
         this.p = var1;
         this.y = Objects.requireNonNull(var2);
      }

      protected void initChannel(Channel var1) {
         var1.attr(gf.d).set(this.p);
         var1.pipeline().addLast(new ChannelHandler[]{this.y}).addFirst("larp_proxy", new w9(this.p));
      }
   }

   private record o(id.u active, String blocked) {
   }
}
