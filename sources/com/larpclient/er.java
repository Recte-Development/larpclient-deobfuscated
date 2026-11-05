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

public final class er implements AutoCloseable {
   private static final int H = 524288;
   private final HttpClient x = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(4L)).build();
   private final URI W;

   public er() {
      this(URI.create("https://lrclib.net/api/get"));
   }

   er(URI var1) {
      this.W = var1;
   }

   public static URI z(ha var0) {
      String var1 = "track_name=" + a(var0.title()) + "&artist_name=" + a(var0.artist());
      if (!var0.album().isBlank()) {
         var1 = var1 + "&album_name=" + a(var0.album());
      }

      if (var0.duration() > 0.0) {
         var1 = var1 + "&duration=" + Math.round(var0.duration());
      }

      return URI.create("https://lrclib.net/api/get?" + var1);
   }

   private static String a(String var0) {
      return URLEncoder.encode(var0, StandardCharsets.UTF_8);
   }

   public CompletableFuture<r> m(ha var1) {
      return this.m(URI.create(this.W.toString() + "?" + z(var1).getRawQuery()))
         .thenCompose(
            var2 -> {
               r var3 = Q((HttpResponse<byte[]>)var2);
               if (var2.statusCode() == 404 || var2.statusCode() == 200 && var3.timed() == null && !var3.status().equals("Instrumental track")) {
                  URI var4 = this.W.resolve("search?track_name=" + a(var1.title()) + "&artist_name=" + a(var1.artist()));
                  return CompletableFuture.<URI>supplyAsync(() -> var4, CompletableFuture.delayedExecutor(300L, TimeUnit.MILLISECONDS))
                     .thenCompose(this::m)
                     .thenApply(var1xx -> var1xx.statusCode() == 200 ? t(var1xx.body(), var1) : Q((HttpResponse<byte[]>)var1xx))
                     .exceptionally(var0 -> r.unavailable("Lyrics lookup failed; retrying shortly", 30L))
                     .thenApply(
                        var1xx -> {
                           if (var1xx.timed() == null && !var3.plain().isEmpty()) {
                              int var2x = !var1xx.status().equals("Lyrics lookup rate limited")
                                    && !var1xx.status().equals("Lyrics provider busy; retrying shortly")
                                 ? 0
                                 : 1;
                              return new r(
                                 null, var3.plain(), var2x != 0 ? var1xx.status() + "; showing untimed lyrics" : var3.status(), var1xx.retryAfterSeconds()
                              );
                           } else {
                              return (r)var1xx;
                           }
                        }
                     );
               } else {
                  return CompletableFuture.completedFuture(var3);
               }
            }
         )
         .exceptionally(var0 -> r.unavailable("Lyrics lookup failed; retrying shortly", 30L));
   }

   private CompletableFuture<HttpResponse<byte[]>> m(URI var1) {
      HttpRequest var2 = HttpRequest.newBuilder(var1)
         .timeout(Duration.ofSeconds(10L))
         .header("User-Agent", "LarpClient/" + zq.x + " (https://larpclient.com; WorldLyrics)")
         .GET()
         .build();
      return this.x.<byte[]>sendAsync(var2, var0 -> new er.i()).orTimeout(12L, TimeUnit.SECONDS);
   }

   private static r Q(HttpResponse<byte[]> var0) {
      if (var0.statusCode() == 429) {
         return r.unavailable("Lyrics lookup rate limited", F(var0.headers().firstValue("Retry-After").orElse("60")));
      } else if (var0.statusCode() == 404) {
         return r.unavailable("No lyrics found for this song", 600L);
      } else if (var0.statusCode() == 503) {
         return r.unavailable("Lyrics provider busy; retrying shortly", F(var0.headers().firstValue("Retry-After").orElse("2")));
      } else {
         return var0.statusCode() != 200
            ? r.unavailable("Lyrics provider unavailable (HTTP " + var0.statusCode() + ")", 30L)
            : z(new String((byte[])var0.body(), StandardCharsets.UTF_8));
      }
   }

   public static r t(byte[] var0, ha var1) {
      JsonArray var2 = JsonParser.parseString(new String(var0, StandardCharsets.UTF_8)).getAsJsonArray();
      if (var2.size() > 100) {
         return r.unavailable("Lyrics provider returned too many matches", 30L);
      }

      r var3 = null;
      double var4 = Double.MAX_VALUE;

      for (JsonElement var7 : var2) {
         if (var7.isJsonObject()) {
            JsonObject var8 = var7.getAsJsonObject();

            try {
               if (Q(var8, "trackName", var1.title()) && Q(var8, "artistName", var1.artist())) {
                  double var9 = var8.has("duration") && !var8.get("duration").isJsonNull() ? var8.get("duration").getAsDouble() : 0.0;
                  if (Double.isFinite(var9) && !(var9 <= 0.0)) {
                     int var11 = !var1.album().isBlank() && Q(var8, "albumName", var1.album()) ? 1 : 0;
                     double var12 = var1.duration() > 0.0 ? Math.abs(var9 - var1.duration()) : 0.0;
                     if (var1.duration() > 0.0 ? !(var12 > 2.0) : var11 != 0) {
                        r var14 = z(var8.toString());
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

      return var3 == null ? r.unavailable("No lyrics found for this song", 600L) : var3;
   }

   private static boolean Q(JsonObject var0, String var1, String var2) {
      return var0.has(var1) && !var0.get(var1).isJsonNull() && X(var0.get(var1).getAsString()).equals(X(var2));
   }

   private static String X(String var0) {
      return Normalizer.normalize(var0, Form.NFKC)
         .toLowerCase(Locale.ROOT)
         .replace('’', '\'')
         .replace('‘', '\'')
         .replace('–', '-')
         .replace('—', '-')
         .replaceAll("(?U)\\s+", " ")
         .strip();
   }

   public static r z(String var0) {
      JsonObject var1 = JsonParser.parseString(var0).getAsJsonObject();
      if (var1.has("instrumental") && var1.get("instrumental").getAsBoolean()) {
         return r.unavailable("Instrumental track", 600L);
      }

      if (var1.has("syncedLyrics") && !var1.get("syncedLyrics").isJsonNull()) {
         e6 var2 = e6.L(var1.get("syncedLyrics").getAsString());
         if (!var2.K().isEmpty()) {
            return new r(var2, List.of(), "Synced lyrics", 0L);
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
            return new r(null, var3, "Lyrics available without timing", 0L);
         }
      }

      return r.unavailable("No lyrics found for this song", 600L);
   }

   public static long F(String var0) {
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
      this.x.shutdownNow();
   }

   private static final class i implements BodySubscriber<byte[]> {
      private final BodySubscriber<byte[]> K = BodySubscribers.ofByteArray();
      private Subscription W;
      private int c;

      @Override
      public CompletionStage<byte[]> getBody() {
         return this.K.getBody();
      }

      @Override
      public void onSubscribe(Subscription var1) {
         this.W = var1;
         this.K.onSubscribe(var1);
      }

      public void j(List<ByteBuffer> var1) {
         for (ByteBuffer var3 : var1) {
            this.c = this.c + var3.remaining();
         }

         if (this.c > 524288) {
            this.W.cancel();
            this.K.onError(new IOException("Lyrics response too large"));
         } else {
            this.K.onNext(var1);
         }
      }

      @Override
      public void onError(Throwable var1) {
         this.K.onError(var1);
      }

      @Override
      public void onComplete() {
         this.K.onComplete();
      }
   }
}
