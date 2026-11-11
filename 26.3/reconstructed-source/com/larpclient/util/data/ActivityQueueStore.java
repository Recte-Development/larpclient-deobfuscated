package com.larpclient.util.data;

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

public final class ActivityQueueStore implements AutoCloseable {
   private final Path path;
   private final Consumer<String> logger;
   private final ExecutorService ioExecutor = Executors.newSingleThreadExecutor(var0 -> new Thread(var0, "Larp-activity-IO"));
   private final AtomicReference<JsonObject> pendingWrite = new AtomicReference<>();
   private boolean writeScheduled;
   private boolean closed;

   public ActivityQueueStore(Path var1, Consumer<String> var2) {
      this.path = var1;
      this.logger = var2;
   }

   public CompletableFuture<JsonObject> load() {
      return CompletableFuture.supplyAsync(() -> {
         try {
            if (!Files.exists(this.path)) {
               return null;
            } else if (Files.size(this.path) > 65536L) {
               throw new IllegalArgumentException();
            } else {
               JsonObject var1 = JsonParser.parseString(Files.readString(this.path)).getAsJsonObject();
               if (var1.get("format").getAsInt() != 1) {
                  throw new IllegalArgumentException();
               } else {
                  return var1;
               }
            }
         } catch (Exception var4) {
            this.logger.accept("Activity queue unreadable; starting empty without replacing the damaged file");

            try {
               Files.move(this.path, this.path.resolveSibling(this.path.getFileName() + ".invalid-" + System.currentTimeMillis()));
            } catch (Exception var3) {
            }

            return null;
         }
      }, this.ioExecutor);
   }

   public <T> CompletableFuture<T> submit(Supplier<T> var1) {
      return CompletableFuture.supplyAsync(var1, this.ioExecutor);
   }

   public synchronized void checkpoint(JsonObject var1) {
      if (!this.closed) {
         this.pendingWrite.set(var1.deepCopy());
         if (!this.writeScheduled) {
            this.writeScheduled = (boolean)1;
            this.ioExecutor.execute(this::drainWrites);
         }
      }
   }

   private void drainWrites() {
      while (true) {
         JsonObject var1;
         synchronized (this) {
            var1 = this.pendingWrite.getAndSet(null);
            if (var1 == null) {
               this.writeScheduled = (boolean)0;
               return;
            }
         }

         try {
            writeAtomically(this.path, var1);
         } catch (Exception var4) {
            this.logger.accept("Activity checkpoint failed; retaining in-memory queue until the next checkpoint");
         }
      }
   }

   public static void writeAtomically(Path var0, JsonObject var1) throws IOException {
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
      this.closed = (boolean)1;
      this.ioExecutor.shutdown();
   }
}
