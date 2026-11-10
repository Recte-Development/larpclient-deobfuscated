package com.larpclient;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.MinecraftClientHttpException;
import com.mojang.authlib.yggdrasil.response.ErrorResponse;
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
import net.minecraft.class_12239;

final class g5 {
   static final URI c = URI.create("https://sessionserver.mojang.com/session/minecraft/join");
   static final int D = 10000;
   static final int q = 15;
   static final int K = 65536;

   private g5() {
   }

   static void E(id.u var0, UUID var1, String var2, String var3) throws AuthenticationException {
      JsonObject var4 = new JsonObject();
      var4.addProperty("accessToken", var2);
      var4.addProperty("selectedProfile", UndashedUuid.toString(var1));
      var4.addProperty("serverId", var3);

      g5.x var5;
      try {
         var5 = O(var0, c, var4.toString());
      } catch (IOException var7) {
         q8.L(
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
         q8.G("Proxy", "Mojang join check passed through {}", var0.settings().label());
      } else {
         q8.L("Proxy", "Mojang join check through {} returned HTTP {}", var0.settings().label(), var5.status());
         throw D(var5.status(), var5.body());
      }
   }

   static AuthenticationException D(int var0, String var1) {
      ErrorResponse var2 = H(var1);
      return (var2 == null ? new MinecraftClientHttpException(var0) : new MinecraftClientHttpException(var0, var2)).toAuthenticationException();
   }

   static long r(id.u var0) throws IOException, InterruptedException {
      long var1 = System.nanoTime();
      CompletableFuture var3 = new CompletableFuture();
      ChannelFuture var4 = E(var0, new g5.k(var0, var3)).connect(q(var0, c.getHost()));
      var4.addListener(var1x -> {
         if (!var1x.isSuccess()) {
            var3.completeExceptionally(var1x.cause());
         }
      });

      try {
         L(var0, var3);
         return (System.nanoTime() - var1) / 1000000L;
      } finally {
         var4.channel().close();
      }
   }

   static g5.x O(id.u var0, URI var1, String var2) throws IOException, InterruptedException {
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
            var6 = E(var0, new g5.j(var0, var1.getHost(), var4, var5)).connect(q(var0, var1.getHost()));
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
            return L(var0, var5);
         } finally {
            var6.channel().close();
         }
      } else {
         throw new IllegalArgumentException("Proxy requests use default HTTPS endpoints");
      }
   }

   private static Bootstrap E(id.u var0, ChannelHandler var1) {
      class_12239 var2 = class_12239.method_75867(false);
      return (Bootstrap)((Bootstrap)((Bootstrap)((Bootstrap)new Bootstrap().group(var2.method_75869())).channel(var2.method_75870()))
            .resolver(NoopAddressResolverGroup.INSTANCE)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000))
         .handler(var1);
   }

   private static InetSocketAddress q(id.u var0, String var1) throws IOException {
      return var0.settings().type() == id.f.h ? new InetSocketAddress(InetAddress.getByName(var1), 443) : InetSocketAddress.createUnresolved(var1, 443);
   }

   private static <T> T L(id.u var0, CompletableFuture<T> var1) throws IOException, InterruptedException {
      try {
         return (T)var1.get(15L, TimeUnit.SECONDS);
      } catch (TimeoutException var5) {
         var1.cancel(false);
         throw new w9.h("Proxy " + var0.settings().label() + ": the request did not finish in time");
      } catch (ExecutionException var6) {
         Throwable var3 = var6.getCause();
         if (var3 instanceof w9.h var4) {
            throw var4;
         } else if (var3 instanceof SSLException) {
            throw new w9.h("Proxy " + var0.settings().label() + ": the secure connection through the proxy failed", var3);
         } else {
            throw new w9.h("Proxy " + var0.settings().label() + ": the request failed", var3);
         }
      }
   }

   private static ErrorResponse H(String var0) {
      try {
         JsonObject var1 = JsonParser.parseString(var0).getAsJsonObject();
         return new ErrorResponse(T(var1, "path"), T(var1, "error"), T(var1, "errorMessage"), null);
      } catch (RuntimeException var2) {
         return null;
      }
   }

   private static String T(JsonObject var0, String var1) {
      return var0.has(var1) && var0.get(var1).isJsonPrimitive() ? var0.get(var1).getAsString() : null;
   }

   private static SslHandler C(String var0) throws NoSuchAlgorithmException {
      SSLEngine var1 = SSLContext.getDefault().createSSLEngine(var0, 443);
      var1.setUseClientMode(true);
      SSLParameters var2 = var1.getSSLParameters();
      var2.setEndpointIdentificationAlgorithm("HTTPS");
      var2.setServerNames(List.of(new SNIHostName(var0)));
      var1.setSSLParameters(var2);
      return new SslHandler(var1);
   }

   static final class j extends ChannelInitializer<Channel> {
      private final id.u j;
      private final String n;
      private final FullHttpRequest D;
      private final CompletableFuture<g5.x> p;

      j(id.u var1, String var2, FullHttpRequest var3, CompletableFuture<g5.x> var4) {
         this.j = var1;
         this.n = var2;
         this.D = var3;
         this.p = var4;
      }

      protected void initChannel(Channel var1) throws Exception {
         var1.pipeline()
            .addLast("larp_proxy", new w9(this.j))
            .addLast("tls", g5.C(this.n))
            .addLast("http", new HttpClientCodec())
            .addLast("aggregate", new HttpObjectAggregator(65536))
            .addLast("larp_response", new g5.q(this.D, this.p));
      }
   }

   static final class k extends ChannelInitializer<Channel> {
      private final id.u l;
      private final CompletableFuture<Void> e;

      k(id.u var1, CompletableFuture<Void> var2) {
         this.l = var1;
         this.e = var2;
      }

      protected void initChannel(Channel var1) {
         var1.pipeline().addLast("larp_proxy", new w9(this.l)).addLast("larp_probe", new g5.w(this.e));
      }
   }

   static final class q extends SimpleChannelInboundHandler<FullHttpResponse> {
      private final FullHttpRequest p;
      private final CompletableFuture<g5.x> N;

      q(FullHttpRequest var1, CompletableFuture<g5.x> var2) {
         this.p = var1;
         this.N = var2;
      }

      public void channelActive(ChannelHandlerContext var1) {
         var1.writeAndFlush(this.p.retain());
      }

      protected void y(ChannelHandlerContext var1, FullHttpResponse var2) {
         this.N.complete(new g5.x(var2.status().code(), var2.content().toString(StandardCharsets.UTF_8)));
         var1.close();
      }

      public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) {
         this.N.completeExceptionally(var2);
         var1.close();
      }

      public void channelInactive(ChannelHandlerContext var1) {
         this.N.completeExceptionally(new IOException("The connection closed before a response"));
      }

      public void handlerRemoved(ChannelHandlerContext var1) {
         if (this.p.refCnt() > 0) {
            this.p.release();
         }
      }
   }

   static final class w extends ChannelInboundHandlerAdapter {
      private final CompletableFuture<Void> G;

      w(CompletableFuture<Void> var1) {
         this.G = var1;
      }

      public void channelActive(ChannelHandlerContext var1) {
         this.G.complete(null);
         var1.close();
      }
   }

   record x(int status, String body) {
   }
}
