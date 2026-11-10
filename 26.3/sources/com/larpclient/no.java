package com.larpclient;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.MinecraftClientHttpException;
import com.mojang.authlib.services.response.ErrorResponse;
import com.mojang.util.UndashedUuid;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.ssl.SslHandler;
import io.netty.resolver.NoopAddressResolverGroup;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLParameters;
import net.minecraft.server.network.EventLoopGroupHolder;

final class no {
   static final URI A = URI.create("https://sessionserver.mojang.com/session/minecraft/join");
   static final int W = 10000;
   static final int S = 15;
   static final int L = 65536;

   private no() {
   }

   static void y(ou.o var0, UUID var1, String var2, String var3) throws AuthenticationException {
      JsonObject var4 = new JsonObject();
      var4.addProperty("accessToken", var2);
      var4.addProperty("selectedProfile", UndashedUuid.toString(var1));
      var4.addProperty("serverId", var3);

      no.s var5;
      try {
         var5 = K(var0, A, var4.toString());
      } catch (IOException var7) {
         _q.R(
            "Proxy",
            "Mojang join check through {} failed ({}); the server login stops instead of going direct",
            var0.settings().label(),
            var7.getClass().getSimpleName()
         );
         throw new AuthenticationException(var7.getMessage());
      } catch (InterruptedException var8) {
         Thread.currentThread().interrupt();
         throw new AuthenticationException("Proxy " + var0.settings().label() + ": the join check was interrupted");
      }

      if (var5.status() / 100 == 2) {
         _q.z("Proxy", "Mojang join check passed through {}", var0.settings().label());
      } else {
         _q.R("Proxy", "Mojang join check through {} returned HTTP {}", var0.settings().label(), var5.status());
         throw Z(var5.status(), var5.body());
      }
   }

   static AuthenticationException Z(int var0, String var1) {
      ErrorResponse var2 = T(var1);
      return (var2 == null ? new MinecraftClientHttpException(var0) : new MinecraftClientHttpException(var0, var2)).toAuthenticationException();
   }

   static long L(ou.o var0) throws IOException, InterruptedException {
      long var1 = System.nanoTime();
      CompletableFuture var3 = new CompletableFuture();
      ChannelFuture var4 = S(var0, new no.p(var0, var3)).connect(M(var0, A.getHost()));
      var4.addListener(var1x -> {
         if (!var1x.isSuccess()) {
            var3.completeExceptionally(var1x.cause());
         }
      });

      try {
         Y(var0, var3);
         return (System.nanoTime() - var1) / 1000000L;
      } finally {
         var4.channel().close();
      }
   }

   static no.s K(ou.o var0, URI var1, String var2) throws IOException, InterruptedException {
      if ("https".equals(var1.getScheme()) && var1.getPort() == -1) {
         byte[] var3 = var2.getBytes(StandardCharsets.UTF_8);
         DefaultFullHttpRequest var4 = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, var1.getRawPath(), Unpooled.wrappedBuffer(var3));
         var4.headers()
            .set(HttpHeaderNames.HOST, var1.getHost())
            .set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=utf-8")
            .set(HttpHeaderNames.CONTENT_LENGTH, var3.length)
            .set(HttpHeaderNames.ACCEPT, "application/json")
            .set(HttpHeaderNames.USER_AGENT, "Java/" + System.getProperty("java.version"))
            .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
         CompletableFuture var5 = new CompletableFuture();

         ChannelFuture var6;
         try {
            var6 = S(var0, new no.g(var0, var1.getHost(), var4, var5)).connect(M(var0, var1.getHost()));
         } catch (RuntimeException var12) {
            var4.release();
            throw var12;
         }

         var6.addListener(var1x -> {
            if (!var1x.isSuccess()) {
               var5.completeExceptionally(var1x.cause());
            }
         });

         try {
            return Y(var0, var5);
         } finally {
            var6.channel().close();
         }
      } else {
         throw new IllegalArgumentException("Proxy requests use default HTTPS endpoints");
      }
   }

   private static Bootstrap S(ou.o var0, ChannelHandler var1) {
      EventLoopGroupHolder var2 = EventLoopGroupHolder.remote(false);
      return (Bootstrap)((Bootstrap)((Bootstrap)((Bootstrap)new Bootstrap().group(var2.eventLoopGroup())).channel(var2.channelCls()))
            .resolver(NoopAddressResolverGroup.INSTANCE)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000))
         .handler(var1);
   }

   private static InetSocketAddress M(ou.o var0, String var1) throws IOException {
      return var0.settings().type() == ou.u.C ? new InetSocketAddress(InetAddress.getByName(var1), 443) : InetSocketAddress.createUnresolved(var1, 443);
   }

   private static <T> T Y(ou.o var0, CompletableFuture<T> var1) throws IOException, InterruptedException {
      try {
         return (T)var1.get(15L, TimeUnit.SECONDS);
      } catch (TimeoutException var5) {
         var1.cancel(false);
         throw new bu.j("Proxy " + var0.settings().label() + ": the request did not finish in time");
      } catch (ExecutionException var6) {
         Throwable var3 = var6.getCause();
         if (var3 instanceof bu.j var4) {
            throw var4;
         } else if (var3 instanceof SSLException) {
            throw new bu.j("Proxy " + var0.settings().label() + ": the secure connection through the proxy failed", var3);
         } else {
            throw new bu.j("Proxy " + var0.settings().label() + ": the request failed", var3);
         }
      }
   }

   private static ErrorResponse T(String var0) {
      try {
         JsonObject var1 = JsonParser.parseString(var0).getAsJsonObject();
         return new ErrorResponse(k(var1, "path"), k(var1, "error"), k(var1, "errorMessage"), null);
      } catch (RuntimeException var2) {
         return null;
      }
   }

   private static String k(JsonObject var0, String var1) {
      return var0.has(var1) && var0.get(var1).isJsonPrimitive() ? var0.get(var1).getAsString() : null;
   }

   private static SslHandler B(String var0) throws NoSuchAlgorithmException {
      SSLEngine var1 = SSLContext.getDefault().createSSLEngine(var0, 443);
      var1.setUseClientMode(true);
      SSLParameters var2 = var1.getSSLParameters();
      var2.setEndpointIdentificationAlgorithm("HTTPS");
      var2.setServerNames(List.of(new SNIHostName(var0)));
      var1.setSSLParameters(var2);
      return new SslHandler(var1);
   }

   static final class g extends ChannelInitializer<Channel> {
      private final ou.o w;
      private final String y;
      private final FullHttpRequest Z;
      private final CompletableFuture<no.s> l;

      g(ou.o var1, String var2, FullHttpRequest var3, CompletableFuture<no.s> var4) {
         this.w = var1;
         this.y = var2;
         this.Z = var3;
         this.l = var4;
      }

      protected void initChannel(Channel var1) throws Exception {
         var1.pipeline()
            .addLast("larp_proxy", new bu(this.w))
            .addLast("tls", no.B(this.y))
            .addLast("http", new HttpClientCodec())
            .addLast("aggregate", new HttpObjectAggregator(65536))
            .addLast("larp_response", new no.z(this.Z, this.l));
      }
   }

   static final class p extends ChannelInitializer<Channel> {
      private final ou.o x;
      private final CompletableFuture<Void> I;

      p(ou.o var1, CompletableFuture<Void> var2) {
         this.x = var1;
         this.I = var2;
      }

      protected void initChannel(Channel var1) {
         var1.pipeline().addLast("larp_proxy", new bu(this.x)).addLast("larp_probe", new no.x(this.I));
      }
   }

   record s(int status, String body) {
   }

   static final class x extends ChannelInboundHandlerAdapter {
      private final CompletableFuture<Void> P;

      x(CompletableFuture<Void> var1) {
         this.P = var1;
      }

      public void channelActive(ChannelHandlerContext var1) {
         this.P.complete(null);
         var1.close();
      }
   }

   static final class z extends SimpleChannelInboundHandler<FullHttpResponse> {
      private final FullHttpRequest n;
      private final CompletableFuture<no.s> S;

      z(FullHttpRequest var1, CompletableFuture<no.s> var2) {
         this.n = var1;
         this.S = var2;
      }

      public void channelActive(ChannelHandlerContext var1) {
         var1.writeAndFlush(this.n.retain());
      }

      protected void z(ChannelHandlerContext var1, FullHttpResponse var2) {
         this.S.complete(new no.s(var2.status().code(), var2.content().toString(StandardCharsets.UTF_8)));
         var1.close();
      }

      public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) {
         this.S.completeExceptionally(var2);
         var1.close();
      }

      public void channelInactive(ChannelHandlerContext var1) {
         this.S.completeExceptionally(new IOException("The connection closed before a response"));
      }

      public void handlerRemoved(ChannelHandlerContext var1) {
         if (this.n.refCnt() > 0) {
            this.n.release();
         }
      }
   }
}
