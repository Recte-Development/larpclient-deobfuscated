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

public final class ya {
   private static final ExecutorService O = new ThreadPoolExecutor(2, 4, 30L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(64), var0 -> {
      Thread var1 = new Thread(var0, "Larp-platform");
      var1.setDaemon(true);
      return var1;
   }, new AbortPolicy());
   private final URI m;
   private final Supplier<PublicApiAccess> E;
   private final Duration q;
   private final HttpClient c;
   private final AtomicInteger V = new AtomicInteger();

   public ya(URI var1, Supplier<PublicApiAccess> var2) {
      this(var1, var2, Duration.ofSeconds(15L));
   }

   public ya(URI var1, Supplier<PublicApiAccess> var2, Duration var3) {
      this.m = b(var1);
      this.E = var2;
      this.q = var3;
      this.c = HttpClient.newBuilder().executor(O).connectTimeout(Duration.ofSeconds(8L)).followRedirects(Redirect.NEVER).build();
   }

   public static URI N() {
      return URI.create("https://larpclient.com");
   }

   public static URI b(URI var0) {
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

   public CompletableFuture<JsonObject> E(String var1, String var2, JsonObject var3, boolean var4, String var5) {
      return this.u(var1, var2, var3, var4, var5, null);
   }

   public CompletableFuture<JsonObject> u(String var1, String var2, JsonObject var3, boolean var4, String var5, PublicApiAccess var6) {
      if (this.V.incrementAndGet() > 8) {
         this.V.decrementAndGet();
         return CompletableFuture.failedFuture(new u6(429, "RATE_LIMITED"));
      }

      try {
         if (var2.startsWith("/api/") && !var2.contains("..") && !var2.contains("#")) {
            byte[] var7 = var3 == null ? new byte[0] : var3.toString().getBytes(StandardCharsets.UTF_8);
            if (var7.length > (var2.equals("/api/reports") ? 32768 : 131072)) {
               throw new u6(413, "PAYLOAD_TOO_LARGE");
            }

            Builder var8 = HttpRequest.newBuilder(this.m.resolve(var2)).timeout(this.q).header("Accept", "application/json");
            if (var4) {
               (var6 == null ? this.E.get() : var6).K(var8, var1, this.m.resolve(var2), var7);
            }

            if (var5 != null) {
               var8.header("If-Match", var5);
            }

            if (var3 != null) {
               var8.header("Content-Type", "application/json");
            }

            var8.method(var1, var3 == null ? BodyPublishers.noBody() : BodyPublishers.ofByteArray(var7));
            CompletableFuture var9 = this.c.sendAsync(var8.build(), var0 -> new ya.q(8388608));
            CompletableFuture var10 = new CompletableFuture();
            CompletableFuture.delayedExecutor(this.q.toMillis(), TimeUnit.MILLISECONDS).execute(() -> {
               if (var10.completeExceptionally(new u6(0, "TIMEOUT"))) {
                  var9.cancel(true);
               }
            });
            var9.whenComplete((var1x, var2x) -> {
               if (var2x != null) {
                  var10.completeExceptionally(B(var2x));
               } else {
                  try {
                     var10.complete(X(var1x.statusCode(), (String)var1x.body()));
                  } catch (RuntimeException var4x) {
                     var10.completeExceptionally(var4x);
                  }
               }
            });
            var10.whenComplete((var3x, var4x) -> {
               this.V.decrementAndGet();
               if (var10.isCancelled()) {
                  var9.cancel(true);
               }
            });
            return var10;
         } else {
            throw u6.V();
         }
      } catch (RuntimeException var11) {
         this.V.decrementAndGet();
         return CompletableFuture.failedFuture(var11 instanceof u6 ? var11 : u6.V());
      }
   }

   public CompletableFuture<JsonObject> V(String var1, boolean var2) {
      return this.E("GET", var1, null, var2, null);
   }

   public static u6 B(Throwable var0) {
      while (var0.getCause() != null && var0 instanceof CompletionException) {
         var0 = var0.getCause();
      }

      return var0 instanceof u6 var1 ? var1 : new u6(0, var0 instanceof HttpTimeoutException ? "TIMEOUT" : "NETWORK");
   }

   public static JsonObject X(int var0, String var1) {
      JsonObject var2;
      try {
         var2 = JsonParser.parseString(var1).getAsJsonObject();
      } catch (RuntimeException var6) {
         throw var0 >= 200 && var0 < 300 ? u6.V() : new u6(var0, "HTTP_ERROR");
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

         throw new u6(var0, var3);
      } else if (var2.has("ok")
         && var2.get("ok").isJsonPrimitive()
         && var2.getAsJsonPrimitive("ok").isBoolean()
         && var2.get("ok").getAsBoolean()
         && var2.has("data")
         && var2.get("data").isJsonObject()) {
         return var2.getAsJsonObject("data");
      } else {
         throw u6.V();
      }
   }

   private static final class q implements BodySubscriber<String> {
      private final CompletableFuture<String> k = new CompletableFuture<>();
      private final ByteArrayOutputStream M = new ByteArrayOutputStream();
      private final int F;
      private Subscription H;

      q(int var1) {
         this.F = var1;
      }

      @Override
      public CompletionStage<String> getBody() {
         return this.k;
      }

      @Override
      public void onSubscribe(Subscription var1) {
         this.H = var1;
         var1.request(1L);
      }

      public void r(List<ByteBuffer> var1) {
         for (ByteBuffer var3 : var1) {
            if (this.M.size() + var3.remaining() > this.F) {
               this.H.cancel();
               this.k.completeExceptionally(u6.V());
               return;
            }

            byte[] var4 = new byte[var3.remaining()];
            var3.get(var4);
            this.M.writeBytes(var4);
         }

         this.H.request(1L);
      }

      @Override
      public void onError(Throwable var1) {
         this.k.completeExceptionally(var1);
      }

      @Override
      public void onComplete() {
         this.k.complete(this.M.toString(StandardCharsets.UTF_8));
      }
   }
}
