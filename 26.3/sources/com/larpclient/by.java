package com.larpclient;

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

public final class by {
   private static final HttpClient U = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5L)).followRedirects(Redirect.NEVER).build();

   public static byte[] a(String var0, int var1) throws Exception {
      return y(var0, var1, "application/json");
   }

   public static byte[] y(String var0, int var1, String var2) throws Exception {
      URI var3 = URI.create(var0);
      if (!var3.getScheme().equals("https")) {
         throw new IllegalArgumentException("HTTPS required");
      } else {
         HttpRequest var4 = HttpRequest.newBuilder(var3).timeout(Duration.ofSeconds(8L)).header("Accept", var2).GET().build();
         HttpResponse var5 = U.send(var4, var1x -> new by.b(var1));
         if (var5.statusCode() != 200 && var5.statusCode() != 204) {
            throw new IOException("HTTP " + var5.statusCode());
         } else {
            return (byte[])var5.body();
         }
      }
   }

   public static JsonObject M(String var0, int var1) throws Exception {
      return JsonParser.parseString(new String(a(var0, var1), StandardCharsets.UTF_8)).getAsJsonObject();
   }

   private by() {
   }

   private static final class b implements BodySubscriber<byte[]> {
      private final int z;
      private final ByteArrayOutputStream Z = new ByteArrayOutputStream();
      private final CompletableFuture<byte[]> M = new CompletableFuture<>();
      private Subscription H;

      b(int var1) {
         this.z = var1;
      }

      @Override
      public CompletionStage<byte[]> getBody() {
         return this.M;
      }

      @Override
      public void onSubscribe(Subscription var1) {
         this.H = var1;
         var1.request(1L);
      }

      public void l(List<ByteBuffer> var1) {
         for (ByteBuffer var3 : var1) {
            if (var3.remaining() > this.z - this.Z.size()) {
               this.H.cancel();
               this.M.completeExceptionally(new IOException("Response exceeds limit"));
               return;
            }

            byte[] var4 = new byte[var3.remaining()];
            var3.get(var4);
            this.Z.writeBytes(var4);
         }

         this.H.request(1L);
      }

      @Override
      public void onError(Throwable var1) {
         this.M.completeExceptionally(var1);
      }

      @Override
      public void onComplete() {
         this.M.complete(this.Z.toByteArray());
      }
   }
}
