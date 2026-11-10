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

public final class s implements AutoCloseable {
   private final Path C;
   private final Consumer<String> G;
   private final ExecutorService Z = Executors.newSingleThreadExecutor(var0 -> new Thread(var0, "Larp-activity-IO"));
   private final AtomicReference<JsonObject> L = new AtomicReference<>();
   private boolean W;
   private boolean h;

   public s(Path var1, Consumer<String> var2) {
      this.C = var1;
      this.G = var2;
   }

   public CompletableFuture<JsonObject> H() {
      return CompletableFuture.supplyAsync(() -> {
         try {
            if (!Files.exists(this.C)) {
               return null;
            } else if (Files.size(this.C) > 65536L) {
               throw new IllegalArgumentException();
            } else {
               JsonObject var1 = JsonParser.parseString(Files.readString(this.C)).getAsJsonObject();
               if (var1.get("format").getAsInt() != 1) {
                  throw new IllegalArgumentException();
               } else {
                  return var1;
               }
            }
         } catch (Exception var4) {
            this.G.accept("Activity queue unreadable; starting empty without replacing the damaged file");

            try {
               Files.move(this.C, this.C.resolveSibling(this.C.getFileName() + ".invalid-" + System.currentTimeMillis()));
            } catch (Exception var3) {
            }

            return null;
         }
      }, this.Z);
   }

   public <T> CompletableFuture<T> A(Supplier<T> var1) {
      return CompletableFuture.supplyAsync(var1, this.Z);
   }

   public synchronized void b(JsonObject var1) {
      if (!this.h) {
         this.L.set(var1.deepCopy());
         if (!this.W) {
            this.W = (boolean)1;
            this.Z.execute(this::Z);
         }
      }
   }

   private void Z() {
      while (true) {
         JsonObject var1;
         synchronized (this) {
            var1 = this.L.getAndSet(null);
            if (var1 == null) {
               this.W = (boolean)0;
               return;
            }
         }

         try {
            L(this.C, var1);
         } catch (Exception var4) {
            this.G.accept("Activity checkpoint failed; retaining in-memory queue until the next checkpoint");
         }
      }
   }

   public static void L(Path var0, JsonObject var1) throws IOException {
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
      this.h = (boolean)1;
      this.Z.shutdown();
   }
}
