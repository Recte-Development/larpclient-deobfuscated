package com.larpclient.network;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpResponse.BodySubscriber;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Flow.Subscription;

public final class HttpsFetcher {
   private static final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5L)).followRedirects(Redirect.NEVER).build();

   public static byte[] fetchJsonBytes(String var0, int var1) throws Exception {
      return fetchBytes(var0, var1, "application/json");
   }

   public static byte[] fetchBytes(String var0, int var1, String var2) throws Exception {
      URI var3 = URI.create(var0);
      if (!var3.getScheme().equals("https")) {
         throw new IllegalArgumentException("HTTPS required");
      } else {
         HttpRequest var4 = HttpRequest.newBuilder(var3).timeout(Duration.ofSeconds(8L)).header("Accept", var2).GET().build();
         HttpResponse var5 = httpClient.send(var4, var1x -> new HttpsFetcher.LimitedBodySubscriber(var1));
         if (var5.statusCode() != 200 && var5.statusCode() != 204) {
            throw new IOException("HTTP " + var5.statusCode());
         } else {
            return (byte[])var5.body();
         }
      }
   }

   public static JsonObject fetchJson(String var0, int var1) throws Exception {
      return JsonParser.parseString(new String(fetchJsonBytes(var0, var1), StandardCharsets.UTF_8)).getAsJsonObject();
   }

   private HttpsFetcher() {
   }

   private static final class LimitedBodySubscriber implements BodySubscriber<byte[]> {
      private final int maxBytes;
      private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      private final CompletableFuture<byte[]> bodyFuture = new CompletableFuture<>();
      private Subscription subscription;

      LimitedBodySubscriber(int var1) {
         this.maxBytes = var1;
      }

      @Override
      public CompletionStage<byte[]> getBody() {
         return this.bodyFuture;
      }

      @Override
      public void onSubscribe(Subscription var1) {
         this.subscription = var1;
         var1.request(1L);
      }

      public void onNext(List<ByteBuffer> var1) {
         for (ByteBuffer var3 : var1) {
            if (var3.remaining() > this.maxBytes - this.buffer.size()) {
               this.subscription.cancel();
               this.bodyFuture.completeExceptionally(new IOException("Response exceeds limit"));
               return;
            }

            byte[] var4 = new byte[var3.remaining()];
            var3.get(var4);
            this.buffer.writeBytes(var4);
         }

         this.subscription.request(1L);
      }

      @Override
      public void onError(Throwable var1) {
         this.bodyFuture.completeExceptionally(var1);
      }

      @Override
      public void onComplete() {
         this.bodyFuture.complete(this.buffer.toByteArray());
      }
   }
}
