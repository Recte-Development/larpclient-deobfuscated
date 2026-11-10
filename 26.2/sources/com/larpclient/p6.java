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

public final class p6 {
   public static final AttributeKey<p8.v> c = AttributeKey.valueOf("larp_proxy_route");
   private static volatile p6.l F = new p6.l(null, null);

   private p6() {
   }

   public static p8.v l() {
      return F.active();
   }

   public static String N() {
      return F.blocked();
   }

   public static void b(p8.v var0) {
      F = new p6.l(Objects.requireNonNull(var0), null);
      h.A("Proxy", "Server traffic now uses {} ({})", var0.settings().label(), var0.settings().authenticated() ? "with login" : "no login");
   }

   public static void P() {
      if (F.active() != null || F.blocked() != null) {
         h.A("Proxy", "Proxy off; server traffic connects directly");
      }

      F = new p6.l(null, null);
   }

   public static void y(String var0) {
      F = new p6.l(null, Objects.requireNonNull(var0));
      h.g("Proxy", "Server connections paused: {}", var0);
   }

   public static ChannelFuture M(Bootstrap var0, InetAddress var1, int var2) {
      p6.l var3 = F;
      if (!F(var1)) {
         if (var3.blocked() != null) {
            throw new UncheckedIOException(new ti.r(var3.blocked()));
         }

         if (var3.active() == null) {
            return var0.connect(var1, var2);
         }

         var0.handler(new p6.s(var3.active(), var0.config().handler()));
         h.U("Proxy", "Opening a server connection through {}", var3.active().settings().label());
         return var0.connect(var1, var2);
      } else {
         if (var3.active() != null || var3.blocked() != null) {
            h.U("Proxy", "Local server address; connecting directly");
         }

         return var0.connect(var1, var2);
      }
   }

   public static void I(MinecraftSessionService var0, Channel var1, UUID var2, String var3, String var4) throws AuthenticationException {
      p8.v var5 = var1 == null ? null : (p8.v)var1.attr(c).get();
      if (var5 != null) {
         aq.C(var5, var2, var3, var4);
      } else {
         int var6 = var1 != null && var1.remoteAddress() instanceof InetSocketAddress var7 && var7.getAddress() != null && F(var7.getAddress()) ? 1 : 0;
         if (var6 == 0 && (F.active() != null || F.blocked() != null)) {
            h.g("Proxy", "A direct server connection is logging in while the proxy is on; it was opened before the proxy changed");
         }

         var0.joinServer(var2, var3, var4);
      }
   }

   public static boolean F(InetAddress var0) {
      return var0.isLoopbackAddress()
         || var0.isAnyLocalAddress()
         || var0.isLinkLocalAddress()
         || var0.isSiteLocalAddress()
         || var0 instanceof Inet6Address && (var0.getAddress()[0] & 254) == 252;
   }

   public static long i(p8.v var0) throws IOException, InterruptedException {
      return aq.f(var0);
   }

   private record l(p8.v active, String blocked) {
   }

   static final class s extends ChannelInitializer<Channel> {
      private final p8.v Q;
      private final ChannelHandler n;

      s(p8.v var1, ChannelHandler var2) {
         this.Q = var1;
         this.n = Objects.requireNonNull(var2);
      }

      protected void initChannel(Channel var1) {
         var1.attr(p6.c).set(this.Q);
         var1.pipeline().addLast(new ChannelHandler[]{this.n}).addFirst("larp_proxy", new ti(this.Q));
      }
   }
}
