package com.larpclient.network;

import com.larpclient.account.MojangJoinProxy;
import com.larpclient.config.ProxySettings;
import com.larpclient.util.misc.LarpLog;
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

public final class ProxyRouter {
   public static final AttributeKey<ProxySettings.ResolvedProxy> routeKey = AttributeKey.valueOf("larp_proxy_route");
   private static volatile ProxyRouter.ProxyState state = new ProxyRouter.ProxyState(null, null);

   private ProxyRouter() {
   }

   public static ProxySettings.ResolvedProxy getActiveProxy() {
      return state.active();
   }

   public static String getBlockedReason() {
      return state.blocked();
   }

   public static void enableProxy(ProxySettings.ResolvedProxy var0) {
      state = new ProxyRouter.ProxyState(Objects.requireNonNull(var0), null);
      LarpLog.info("Proxy", "Server traffic now uses {} ({})", var0.settings().label(), var0.settings().authenticated() ? "with login" : "no login");
   }

   public static void disableProxy() {
      if (state.active() != null || state.blocked() != null) {
         LarpLog.info("Proxy", "Proxy off; server traffic connects directly");
      }

      state = new ProxyRouter.ProxyState(null, null);
   }

   public static void blockConnections(String var0) {
      state = new ProxyRouter.ProxyState(null, Objects.requireNonNull(var0));
      LarpLog.warn("Proxy", "Server connections paused: {}", var0);
   }

   public static ChannelFuture connect(Bootstrap var0, InetAddress var1, int var2) {
      ProxyRouter.ProxyState var3 = state;
      if (!isLocalAddress(var1)) {
         if (var3.blocked() != null) {
            throw new UncheckedIOException(new ProxyHandler.ProxyException(var3.blocked()));
         }

         if (var3.active() == null) {
            return var0.connect(var1, var2);
         }

         var0.handler(new ProxyRouter.ProxyChannelInitializer(var3.active(), var0.config().handler()));
         LarpLog.debug("Proxy", "Opening a server connection through {}", var3.active().settings().label());
         return var0.connect(var1, var2);
      } else {
         if (var3.active() != null || var3.blocked() != null) {
            LarpLog.debug("Proxy", "Local server address; connecting directly");
         }

         return var0.connect(var1, var2);
      }
   }

   public static void joinServer(SessionService var0, Channel var1, UUID var2, String var3, String var4) throws AuthenticationException {
      ProxySettings.ResolvedProxy var5 = var1 == null ? null : (ProxySettings.ResolvedProxy)var1.attr(routeKey).get();
      if (var5 != null) {
         MojangJoinProxy.joinServer(var5, var2, var3, var4);
      } else {
         int var6 = var1 != null && var1.remoteAddress() instanceof InetSocketAddress var7 && var7.getAddress() != null && isLocalAddress(var7.getAddress())
            ? 1
            : 0;
         if (var6 == 0 && (state.active() != null || state.blocked() != null)) {
            LarpLog.warn("Proxy", "A direct server connection is logging in while the proxy is on; it was opened before the proxy changed");
         }

         var0.joinServer(var2, var3, var4);
      }
   }

   public static boolean isLocalAddress(InetAddress var0) {
      return var0.isLoopbackAddress()
         || var0.isAnyLocalAddress()
         || var0.isLinkLocalAddress()
         || var0.isSiteLocalAddress()
         || var0 instanceof Inet6Address && (var0.getAddress()[0] & 254) == 252;
   }

   public static long measureLatency(ProxySettings.ResolvedProxy var0) throws IOException, InterruptedException {
      return MojangJoinProxy.measureLatency(var0);
   }

   static final class ProxyChannelInitializer extends ChannelInitializer<Channel> {
      private final ProxySettings.ResolvedProxy route;
      private final ChannelHandler delegate;

      ProxyChannelInitializer(ProxySettings.ResolvedProxy var1, ChannelHandler var2) {
         this.route = var1;
         this.delegate = Objects.requireNonNull(var2);
      }

      protected void initChannel(Channel var1) {
         var1.attr(ProxyRouter.routeKey).set(this.route);
         var1.pipeline().addLast(new ChannelHandler[]{this.delegate}).addFirst("larp_proxy", new ProxyHandler(this.route));
      }
   }

   private record ProxyState(ProxySettings.ResolvedProxy active, String blocked) {
   }
}
