package com.larpclient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodySubscriber;
import java.net.http.HttpResponse.BodySubscribers;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Flow.Subscription;

public final class hf implements AutoCloseable {
   private static final int I = 524288;
   private final HttpClient O = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(4L)).build();
   private final URI b;

   public hf() {
      this(URI.create("https://lrclib.net/api/get"));
   }

   hf(URI var1) {
      this.b = var1;
   }

   public static URI v(p6 var0) {
      String var1 = "track_name=" + A(var0.title()) + "&artist_name=" + A(var0.artist());
      if (!var0.album().isBlank()) {
         var1 = var1 + "&album_name=" + A(var0.album());
      }

      if (var0.duration() > 0.0) {
         var1 = var1 + "&duration=" + Math.round(var0.duration());
      }

      return URI.create("https://lrclib.net/api/get?" + var1);
   }

   private static String A(String var0) {
      return URLEncoder.encode(var0, StandardCharsets.UTF_8);
   }

   public CompletableFuture<jx> l(p6 var1) {
      return this.T(URI.create(this.b.toString() + "?" + v(var1).getRawQuery()))
         .thenCompose(
            var2 -> {
               jx var3 = n((HttpResponse<byte[]>)var2);
               if (var2.statusCode() == 404 || var2.statusCode() == 200 && var3.timed() == null && !var3.status().equals("Instrumental track")) {
                  URI var4 = this.b.resolve("search?track_name=" + A(var1.title()) + "&artist_name=" + A(var1.artist()));
                  return CompletableFuture.<URI>supplyAsync(() -> var4, CompletableFuture.delayedExecutor(300L, TimeUnit.MILLISECONDS))
                     .thenCompose(this::T)
                     .thenApply(var1xx -> var1xx.statusCode() == 200 ? x(var1xx.body(), var1) : n((HttpResponse<byte[]>)var1xx))
                     .exceptionally(var0 -> jx.unavailable("Lyrics lookup failed; retrying shortly", 30L))
                     .thenApply(
                        var1xx -> {
                           if (var1xx.timed() == null && !var3.plain().isEmpty()) {
                              int var2x = !var1xx.status().equals("Lyrics lookup rate limited")
                                    && !var1xx.status().equals("Lyrics provider busy; retrying shortly")
                                 ? 0
                                 : 1;
                              return new jx(
                                 null, var3.plain(), var2x != 0 ? var1xx.status() + "; showing untimed lyrics" : var3.status(), var1xx.retryAfterSeconds()
                              );
                           } else {
                              return (jx)var1xx;
                           }
                        }
                     );
               } else {
                  return CompletableFuture.completedFuture(var3);
               }
            }
         )
         .exceptionally(var0 -> jx.unavailable("Lyrics lookup failed; retrying shortly", 30L));
   }

   private CompletableFuture<HttpResponse<byte[]>> T(URI var1) {
      HttpRequest var2 = HttpRequest.newBuilder(var1)
         .timeout(Duration.ofSeconds(10L))
         .header("User-Agent", "LarpClient/" + a0.r + " (https://larpclient.com; WorldLyrics)")
         .GET()
         .build();
      return this.O.<byte[]>sendAsync(var2, var0 -> new hf.o()).orTimeout(12L, TimeUnit.SECONDS);
   }

   private static jx n(HttpResponse<byte[]> var0) {
      if (var0.statusCode() == 429) {
         return jx.unavailable("Lyrics lookup rate limited", o(var0.headers().firstValue("Retry-After").orElse("60")));
      } else if (var0.statusCode() == 404) {
         return jx.unavailable("No lyrics found for this song", 600L);
      } else if (var0.statusCode() == 503) {
         return jx.unavailable("Lyrics provider busy; retrying shortly", o(var0.headers().firstValue("Retry-After").orElse("2")));
      } else {
         return var0.statusCode() != 200
            ? jx.unavailable("Lyrics provider unavailable (HTTP " + var0.statusCode() + ")", 30L)
            : q(new String((byte[])var0.body(), StandardCharsets.UTF_8));
      }
   }

   public static jx x(byte[] var0, p6 var1) {
      JsonArray var2 = JsonParser.parseString(new String(var0, StandardCharsets.UTF_8)).getAsJsonArray();
      if (var2.size() > 100) {
         return jx.unavailable("Lyrics provider returned too many matches", 30L);
      }

      jx var3 = null;
      double var4 = Double.MAX_VALUE;

      for (JsonElement var7 : var2) {
         if (var7.isJsonObject()) {
            JsonObject var8 = var7.getAsJsonObject();

            try {
               if (j(var8, "trackName", var1.title()) && j(var8, "artistName", var1.artist())) {
                  double var9 = var8.has("duration") && !var8.get("duration").isJsonNull() ? var8.get("duration").getAsDouble() : 0.0;
                  if (Double.isFinite(var9) && !(var9 <= 0.0)) {
                     int var11 = !var1.album().isBlank() && j(var8, "albumName", var1.album()) ? 1 : 0;
                     double var12 = var1.duration() > 0.0 ? Math.abs(var9 - var1.duration()) : 0.0;
                     if (var1.duration() > 0.0 ? !(var12 > 2.0) : var11 != 0) {
                        jx var14 = q(var8.toString());
                        if (var14.timed() != null || !var14.plain().isEmpty()) {
                           double var15 = var12 + (var11 != 0 ? 0 : 10) + (var14.timed() == null ? 100 : 0);
                           if (var15 < var4) {
                              var4 = var15;
                              var3 = var14;
                           }
                        }
                     }
                  }
               }
            } catch (RuntimeException var17) {
            }
         }
      }

      return var3 == null ? jx.unavailable("No lyrics found for this song", 600L) : var3;
   }

   private static boolean j(JsonObject var0, String var1, String var2) {
      return var0.has(var1) && !var0.get(var1).isJsonNull() && c(var0.get(var1).getAsString()).equals(c(var2));
   }

   private static String c(String var0) {
      return Normalizer.normalize(var0, Form.NFKC)
         .toLowerCase(Locale.ROOT)
         .replace('’', '\'')
         .replace('‘', '\'')
         .replace('–', '-')
         .replace('—', '-')
         .replaceAll("(?U)\\s+", " ")
         .strip();
   }

   public static jx q(String var0) {
      JsonObject var1 = JsonParser.parseString(var0).getAsJsonObject();
      if (var1.has("instrumental") && var1.get("instrumental").getAsBoolean()) {
         return jx.unavailable("Instrumental track", 600L);
      }

      if (var1.has("syncedLyrics") && !var1.get("syncedLyrics").isJsonNull()) {
         j var2 = j.P(var1.get("syncedLyrics").getAsString());
         if (!var2.t().isEmpty()) {
            return new jx(var2, List.of(), "Synced lyrics", 0L);
         }
      }

      if (var1.has("plainLyrics") && !var1.get("plainLyrics").isJsonNull()) {
         List var3 = var1.get("plainLyrics")
            .getAsString()
            .lines()
            .map(String::strip)
            .filter(var0x -> !var0x.isBlank())
            .limit(512L)
            .map(var0x -> var0x.replaceAll("[\\p{Cntrl}§]", ""))
            .map(var0x -> var0x.codePointCount(0, var0x.length()) > 96 ? var0x.substring(0, var0x.offsetByCodePoints(0, 96)) : var0x)
            .toList();
         if (!var3.isEmpty()) {
            return new jx(null, var3, "Lyrics available without timing", 0L);
         }
      }

      return jx.unavailable("No lyrics found for this song", 600L);
   }

   public static long o(String var0) {
      try {
         return Math.max(1L, Long.parseLong(var0));
      } catch (NumberFormatException var4) {
         try {
            return Math.max(1L, Duration.between(Instant.now(), ZonedDateTime.parse(var0, DateTimeFormatter.RFC_1123_DATE_TIME).toInstant()).getSeconds());
         } catch (RuntimeException var3) {
            return 60L;
         }
      }
   }

   @Override
   public void close() {
      this.O.shutdownNow();
   }

   private static final class o implements BodySubscriber<byte[]> {
      private final BodySubscriber<byte[]> k = BodySubscribers.ofByteArray();
      private Subscription f;
      private int S;

      @Override
      public CompletionStage<byte[]> getBody() {
         return this.k.getBody();
      }

      @Override
      public void onSubscribe(Subscription var1) {
         this.f = var1;
         this.k.onSubscribe(var1);
      }

      public void R(List<ByteBuffer> var1) {
         for (ByteBuffer var3 : var1) {
            this.S = this.S + var3.remaining();
         }

         if (this.S > 524288) {
            this.f.cancel();
            this.k.onError(new IOException("Lyrics response too large"));
         } else {
            this.k.onNext(var1);
         }
      }

      @Override
      public void onError(Throwable var1) {
         this.k.onError(var1);
      }

      @Override
      public void onComplete() {
         this.k.onComplete();
      }
   }
}
