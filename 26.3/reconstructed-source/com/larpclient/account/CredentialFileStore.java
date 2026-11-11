package com.larpclient.account;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

final class CredentialFileStore {
   private CredentialFileStore() {
   }

   static <T> T withLock(Path var0, CredentialFileStore.IoSupplier<T> var1) throws IOException {
      Files.createDirectories(var0.getParent());
      InstallIdentity.restrictPermissions(var0.getParent(), true);

      try (
         FileChannel var2 = FileChannel.open(var0, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
         FileLock var3 = var2.lock();
      ) {
         return (T)var1.get();
      }
   }

   static JsonObject read(Path var0) throws IOException {
      if (Files.size(var0) > 4096L) {
         throw new IOException("Invalid credential file size");
      }

      InstallIdentity.restrictPermissions(var0, false);
      return JsonParser.parseString(Files.readString(var0)).getAsJsonObject();
   }

   static void writeAtomic(Path var0, JsonObject var1) throws IOException {
      Path var2 = Files.createTempFile(var0.getParent(), "credential-", ".tmp");

      try {
         InstallIdentity.restrictPermissions(var2, false);
         Files.writeString(var2, var1.toString());

         try {
            Files.move(var2, var0, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
         } catch (AtomicMoveNotSupportedException var7) {
            Files.move(var2, var0, StandardCopyOption.REPLACE_EXISTING);
         }
      } finally {
         Files.deleteIfExists(var2);
      }
   }

   @FunctionalInterface
   interface IoSupplier<T> {
      T get() throws IOException;
   }
}
