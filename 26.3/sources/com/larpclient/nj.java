package com.larpclient;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class nj implements AutoCloseable {
   private final Path d;
   private final Consumer<String> M;
   private final ExecutorService j = Executors.newSingleThreadExecutor(var0 -> new Thread(var0, "Larp-activity-IO"));
   private final AtomicReference<JsonObject> F = new AtomicReference<>();
   private boolean i;
   private boolean L;

   public nj(Path var1, Consumer<String> var2) {
      this.d = var1;
      this.M = var2;
   }

   public CompletableFuture<JsonObject> K() {
      return CompletableFuture.supplyAsync(() -> {
         try {
            if (!Files.exists(this.d)) {
               return null;
            } else if (Files.size(this.d) > 65536L) {
               throw new IllegalArgumentException();
            } else {
               JsonObject var1 = JsonParser.parseString(Files.readString(this.d)).getAsJsonObject();
               if (var1.get("format").getAsInt() != 1) {
                  throw new IllegalArgumentException();
               } else {
                  return var1;
               }
            }
         } catch (Exception var4) {
            this.M.accept("Activity queue unreadable; starting empty without replacing the damaged file");

            try {
               Files.move(this.d, this.d.resolveSibling(this.d.getFileName() + ".invalid-" + System.currentTimeMillis()));
            } catch (Exception var3) {
            }

            return null;
         }
      }, this.j);
   }

   public <T> CompletableFuture<T> Z(Supplier<T> var1) {
      return CompletableFuture.supplyAsync(var1, this.j);
   }

   public synchronized void j(JsonObject var1) {
      if (!this.L) {
         this.F.set(var1.deepCopy());
         if (!this.i) {
            this.i = (boolean)1;
            this.j.execute(this::l);
         }
      }
   }

   private void l() {
      while (true) {
         JsonObject var1;
         synchronized (this) {
            var1 = this.F.getAndSet(null);
            if (var1 == null) {
               this.i = (boolean)0;
               return;
            }
         }

         try {
            o(this.d, var1);
         } catch (Exception var4) {
            this.M.accept("Activity checkpoint failed; retaining in-memory queue until the next checkpoint");
         }
      }
   }

   public static void o(Path var0, JsonObject var1) throws IOException {
      byte[] var2 = var1.toString().getBytes(StandardCharsets.UTF_8);
      if (var2.length > 65536) {
         throw new IOException("Activity queue too large");
      }

      Files.createDirectories(var0.getParent());
      Path var3 = Files.createTempFile(var0.getParent(), ".activity-", ".tmp");

      try {
         try {
            Files.setPosixFilePermissions(var3, PosixFilePermissions.fromString("rw-------"));
         } catch (UnsupportedOperationException var13) {
         }

         try (FileChannel var4 = FileChannel.open(var3, StandardOpenOption.WRITE)) {
            ByteBuffer var5 = ByteBuffer.wrap(var2);

            while (var5.hasRemaining()) {
               var4.write(var5);
            }

            var4.force(true);
         }

         Files.move(var3, var0, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
      } finally {
         Files.deleteIfExists(var3);
      }
   }

   @Override
   public synchronized void close() {
      this.L = (boolean)1;
      this.j.shutdown();
   }
}
