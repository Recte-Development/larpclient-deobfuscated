package com.larpclient;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public final class l7 {
   private l7() {
   }

   public static JsonObject k(Path var0) throws IOException {
      if (!Files.exists(var0)) {
         return new JsonObject();
      }

      try {
         return JsonParser.parseString(Files.readString(var0)).getAsJsonObject();
      } catch (RuntimeException var3) {
         Path var2 = var0.resolveSibling(var0.getFileName() + ".invalid-" + System.currentTimeMillis());
         Files.copy(var0, var2);
         h.m("FakeSpawner", "Malformed JSON preserved at " + var2 + "; recovering empty state", var3);
         return new JsonObject();
      }
   }

   public static void O(Path var0, String var1) throws IOException {
      Files.createDirectories(var0.getParent());
      Path var2 = Files.createTempFile(var0.getParent(), "spawner-", ".tmp");

      try {
         Files.writeString(var2, var1);

         try {
            Files.move(var2, var0, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
         } catch (AtomicMoveNotSupportedException var7) {
            Files.move(var2, var0, StandardCopyOption.REPLACE_EXISTING);
         }
      } finally {
         Files.deleteIfExists(var2);
      }
   }
}
