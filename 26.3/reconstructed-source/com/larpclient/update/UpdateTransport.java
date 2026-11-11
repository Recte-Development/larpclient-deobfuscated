package com.larpclient.update;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.LongConsumer;

final class UpdateTransport implements AutoCloseable {
   static final URI ORIGIN = URI.create("https://larpclient.com");
   private final URI origin;
   private final UpdateTransport.Signer signer;
   private final ExecutorService network = Executors.newFixedThreadPool(2, UpdateService.threads("larp-update-http"));
   private final ScheduledExecutorService deadlines = Executors.newSingleThreadScheduledExecutor(UpdateService.threads("larp-update-deadline"));
   private final HttpClient http;

   UpdateTransport(UpdateTransport.Signer var1) {
      this(ORIGIN, var1, false);
   }

   UpdateTransport(URI var1, UpdateTransport.Signer var2, boolean var3) {
      if (var1.equals(ORIGIN)
         || var3
            && "http".equals(var1.getScheme())
            && "127.0.0.1".equals(var1.getHost())
            && var1.getPort() > 0
            && var1.getPath().isEmpty()
            && var1.getRawQuery() == null
            && var1.getUserInfo() == null
            && var1.getFragment() == null) {
         this.origin = var1;
         this.signer = var2;
         this.http = HttpClient.newBuilder().executor(this.network).connectTimeout(Duration.ofSeconds(8L)).followRedirects(Redirect.NEVER).build();
      } else {
         throw new IllegalArgumentException("Update origin is fixed");
      }
   }

   byte[] metadata(Identity var1, UpdateTransport.Cancellation var2) throws IOException {
      String var3 = "/api/updates/latest?edition="
         + encode(var1.edition())
         + "&minecraftVersion="
         + encode(var1.minecraft())
         + "&channel="
         + encode(var1.channel())
         + "&flavor="
         + encode(var1.flavor())
         + "&loader="
         + encode(var1.loader());
      HttpRequest var4 = HttpRequest.newBuilder(this.origin.resolve(var3)).GET().header("Accept", "application/json").timeout(Duration.ofSeconds(20L)).build();
      return this.exchange(var4, 131072L, 20, var2, (var0, var1x) -> {
         if (var1x > 131072L) {
            throw new IOException("Update metadata exceeds limit");
         } else {
            byte[] var3x = var0.readNBytes(131073);
            if (var3x.length != 0 && var3x.length <= 131072 && (var1x < 0L || var3x.length == var1x)) {
               return var3x;
            } else {
               throw new IOException("Invalid update metadata length");
            }
         }
      });
   }

   void download(UpdateManifest.Release var1, Path var2, UpdateTransport.Cancellation var3, LongConsumer var4) throws IOException {
      URI var5 = this.origin.resolve("/api/updates/artifacts/" + var1.artifactId());
      Builder var6 = HttpRequest.newBuilder(var5).GET().header("Accept", "application/java-archive").timeout(Duration.ofSeconds(180L));
      this.signer.sign(var6, var5);
      this.exchange(var6.build(), var1.size(), 180, var3, (var4x, var5x) -> {
         if (var5x >= 0L && var5x != var1.size()) {
            throw new IOException("Artifact content length does not match signed length");
         }

         try (FileChannel var7 = FileChannel.open(var2, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE, LinkOption.NOFOLLOW_LINKS)) {
            byte[] var8 = new byte[65536];
            long var9 = 0L;
            long var11 = 0L;

            int var13;
            while ((var13 = var4x.read(var8)) != -1) {
               var3.check();
               var9 += var13;
               if (var9 > var1.size()) {
                  throw new IOException("Artifact exceeds signed size");
               }

               ByteBuffer var14 = ByteBuffer.wrap(var8, 0, var13);

               while (var14.hasRemaining()) {
                  var7.write(var14);
               }

               long var15 = System.nanoTime();
               if (var15 - var11 > 100000000L || var9 == var1.size()) {
                  var4.accept(var9);
                  var11 = var15;
               }
            }

            var3.check();
            if (var9 != var1.size()) {
               throw new IOException("Artifact is incomplete");
            }

            var7.force(true);
         }

         return null;
      });
   }

   private <T> T exchange(HttpRequest var1, long var2, int var4, UpdateTransport.Cancellation var5, UpdateTransport.Body<T> var6) throws IOException {
      var5.check();
      CompletableFuture var7 = this.http.sendAsync(var1, BodyHandlers.ofInputStream());
      var5.request(var7);
      ScheduledFuture var8 = this.deadlines.schedule(var5::cancel, var4, TimeUnit.SECONDS);

      try {
         HttpResponse var9 = (HttpResponse)var7.get(var4, TimeUnit.SECONDS);

         try (InputStream var10 = (InputStream)var9.body()) {
            var5.stream(var10);
            var5.check();
            int var11 = var9.statusCode();
            if (var11 != 200) {
               throw new IOException(
                  var11 != 401 && var11 != 403
                     ? (var11 == 429 ? "Update service is busy. Try again later" : "Update server returned HTTP " + var11)
                     : "This download requires server access. Use the official download page."
               );
            } else if (!var9.uri().equals(var1.uri())) {
               throw new IOException("Unexpected update redirect");
            } else {
               String var12 = var9.headers().firstValue("Content-Encoding").orElse("identity");
               if (!var12.equalsIgnoreCase("identity")) {
                  throw new IOException("Encoded update responses are not supported");
               } else {
                  long var13 = var9.headers().firstValueAsLong("Content-Length").orElse(-1L);
                  if (var13 > var2 || var13 == 0L) {
                     throw new IOException("Update response exceeds size limit");
                  } else {
                     return (T)var6.read(var10, var13);
                  }
               }
            }
         }
      } catch (InterruptedException var24) {
         Thread.currentThread().interrupt();
         var5.cancel();
         throw new IOException("Update interrupted", var24);
      } catch (ExecutionException | TimeoutException | CancellationException | IllegalArgumentException var25) {
         var5.cancel();
         throw new IOException("Update connection failed or timed out", var25);
      } finally {
         var8.cancel(false);
      }
   }

   private static String encode(String var0) {
      return URLEncoder.encode(var0, StandardCharsets.UTF_8);
   }

   @Override
   public void close() {
      this.http.close();
      this.network.shutdownNow();
      this.deadlines.shutdownNow();
   }

   @FunctionalInterface
   private interface Body<T> {
      T read(InputStream var1, long var2) throws IOException;
   }

   static final class Cancellation {
      private volatile boolean cancelled;
      private volatile CompletableFuture<?> request;
      private volatile InputStream stream;

      void check() throws IOException {
         if (this.cancelled || Thread.currentThread().isInterrupted()) {
            throw new IOException("Update operation cancelled");
         }
      }

      synchronized void request(CompletableFuture<?> var1) throws IOException {
         this.request = var1;
         if (this.cancelled) {
            var1.cancel(true);
            this.check();
         }
      }

      synchronized void stream(InputStream var1) throws IOException {
         this.stream = var1;
         if (this.cancelled) {
            var1.close();
            this.check();
         }
      }

      synchronized void cancel() {
         this.cancelled = true;
         if (this.request != null) {
            this.request.cancel(true);
         }

         if (this.stream != null) {
            try {
               this.stream.close();
            } catch (IOException var2) {
            }
         }
      }
   }

   @FunctionalInterface
   interface Signer {
      void sign(Builder var1, URI var2) throws IOException;
   }
}
