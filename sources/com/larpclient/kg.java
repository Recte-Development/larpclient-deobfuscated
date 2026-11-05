package com.larpclient;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpTimeoutException;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodySubscriber;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public final class kg {
   private static final ExecutorService M = new ThreadPoolExecutor(2, 4, 30L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(64), var0 -> {
      Thread var1 = new Thread(var0, "Larp-platform");
      var1.setDaemon(true);
      return var1;
   }, new AbortPolicy());
   private final URI D;
   private final Supplier<PublicApiAccess> v;
   private final Duration m;
   private final HttpClient L;
   private final AtomicInteger p = new AtomicInteger();

   public kg(URI var1, Supplier<PublicApiAccess> var2) {
      this(var1, var2, Duration.ofSeconds(15L));
   }

   public kg(URI var1, Supplier<PublicApiAccess> var2, Duration var3) {
      this.D = p(var1);
      this.v = var2;
      this.m = var3;
      this.L = HttpClient.newBuilder().executor(M).connectTimeout(Duration.ofSeconds(8L)).followRedirects(Redirect.NEVER).build();
   }

   public static URI c() {
      return URI.create("https://larpclient.com");
   }

   public static URI p(URI var0) {
      boolean var1 = List.of("localhost", "127.0.0.1", "[::1]").contains(var0.getHost());
      if (var0.getHost() != null
         && var0.getUserInfo() == null
         && var0.getQuery() == null
         && var0.getFragment() == null
         && (var0.getPath().isEmpty() || var0.getPath().equals("/"))
         && ("https".equals(var0.getScheme()) || var1 && "http".equals(var0.getScheme()))) {
         return var0;
      } else {
         throw new IllegalArgumentException("API origin requires HTTPS (HTTP is allowed only on loopback).");
      }
   }

   public CompletableFuture<JsonObject> b(String var1, String var2, JsonObject var3, boolean var4, String var5) {
      return this.D(var1, var2, var3, var4, var5, null);
   }

   public CompletableFuture<JsonObject> D(String var1, String var2, JsonObject var3, boolean var4, String var5, PublicApiAccess var6) {
      if (this.p.incrementAndGet() > 8) {
         this.p.decrementAndGet();
         return CompletableFuture.failedFuture(new pe(429, "RATE_LIMITED"));
      }

      try {
         if (var2.startsWith("/api/") && !var2.contains("..") && !var2.contains("#")) {
            byte[] var7 = var3 == null ? new byte[0] : var3.toString().getBytes(StandardCharsets.UTF_8);
            if (var7.length > (var2.equals("/api/reports") ? 32768 : 131072)) {
               throw new pe(413, "PAYLOAD_TOO_LARGE");
            }

            Builder var8 = HttpRequest.newBuilder(this.D.resolve(var2)).timeout(this.m).header("Accept", "application/json");
            if (var4) {
               (var6 == null ? this.v.get() : var6).Y(var8, var1, this.D.resolve(var2), var7);
            }

            if (var5 != null) {
               var8.header("If-Match", var5);
            }

            if (var3 != null) {
               var8.header("Content-Type", "application/json");
            }

            var8.method(var1, var3 == null ? BodyPublishers.noBody() : BodyPublishers.ofByteArray(var7));
            CompletableFuture var9 = this.L.sendAsync(var8.build(), var0 -> new kg.x(8388608));
            CompletableFuture var10 = new CompletableFuture();
            CompletableFuture.delayedExecutor(this.m.toMillis(), TimeUnit.MILLISECONDS).execute(() -> {
               if (var10.completeExceptionally(new pe(0, "TIMEOUT"))) {
                  var9.cancel(true);
               }
            });
            var9.whenComplete((var1x, var2x) -> {
               if (var2x != null) {
                  var10.completeExceptionally(f(var2x));
               } else {
                  try {
                     var10.complete(I(var1x.statusCode(), (String)var1x.body()));
                  } catch (RuntimeException var4x) {
                     var10.completeExceptionally(var4x);
                  }
               }
            });
            var10.whenComplete((var3x, var4x) -> {
               this.p.decrementAndGet();
               if (var10.isCancelled()) {
                  var9.cancel(true);
               }
            });
            return var10;
         } else {
            throw pe.e();
         }
      } catch (RuntimeException var11) {
         this.p.decrementAndGet();
         return CompletableFuture.failedFuture(var11 instanceof pe ? var11 : pe.e());
      }
   }

   public CompletableFuture<JsonObject> F(String var1, boolean var2) {
      return this.b("GET", var1, null, var2, null);
   }

   public static pe f(Throwable var0) {
      while (var0.getCause() != null && var0 instanceof CompletionException) {
         var0 = var0.getCause();
      }

      return var0 instanceof pe var1 ? var1 : new pe(0, var0 instanceof HttpTimeoutException ? "TIMEOUT" : "NETWORK");
   }

   public static JsonObject I(int var0, String var1) {
      JsonObject var2;
      try {
         var2 = JsonParser.parseString(var1).getAsJsonObject();
      } catch (RuntimeException var6) {
         throw var0 >= 200 && var0 < 300 ? pe.e() : new pe(var0, "HTTP_ERROR");
      }

      if (var0 < 200 || var0 >= 300) {
         String var3 = "HTTP_ERROR";

         try {
            String var4 = var2.getAsJsonObject("error").get("code").getAsString();
            if (List.of("REVISION_CONFLICT", "QUOTA_EXCEEDED", "INVALID_INPUT", "UNAUTHENTICATED", "FORBIDDEN", "RATE_LIMITED", "DISCORD_LIMIT").contains(var4)
               )
             {
               var3 = var4;
            }
         } catch (RuntimeException var5) {
         }

         throw new pe(var0, var3);
      } else if (var2.has("ok")
         && var2.get("ok").isJsonPrimitive()
         && var2.getAsJsonPrimitive("ok").isBoolean()
         && var2.get("ok").getAsBoolean()
         && var2.has("data")
         && var2.get("data").isJsonObject()) {
         return var2.getAsJsonObject("data");
      } else {
         throw pe.e();
      }
   }

   private static final class x implements BodySubscriber<String> {
      private final CompletableFuture<String> c = new CompletableFuture<>();
      private final ByteArrayOutputStream n = new ByteArrayOutputStream();
      private final int P;
      private Subscription f;

      x(int var1) {
         this.P = var1;
      }

      @Override
      public CompletionStage<String> getBody() {
         return this.c;
      }

      @Override
      public void onSubscribe(Subscription var1) {
         this.f = var1;
         var1.request(1L);
      }

      public void V(List<ByteBuffer> var1) {
         for (ByteBuffer var3 : var1) {
            if (this.n.size() + var3.remaining() > this.P) {
               this.f.cancel();
               this.c.completeExceptionally(pe.e());
               return;
            }

            byte[] var4 = new byte[var3.remaining()];
            var3.get(var4);
            this.n.writeBytes(var4);
         }

         this.f.request(1L);
      }

      @Override
      public void onError(Throwable var1) {
         this.c.completeExceptionally(var1);
      }

      @Override
      public void onComplete() {
         this.c.complete(this.n.toString(StandardCharsets.UTF_8));
      }
   }
}
