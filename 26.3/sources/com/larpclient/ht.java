package com.larpclient;

import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

public final class ht {
   public static final String O = "larp.casino.root";

   private ht() {
   }

   public static void F(Path var0) {
      if (var0 == null) {
         System.clearProperty("larp.casino.root");
      } else {
         System.setProperty("larp.casino.root", var0.toAbsolutePath().toString());
      }
   }

   public static Path j() {
      String var0 = System.getProperty("larp.casino.root");
      return var0 != null && !var0.isBlank() ? Path.of(var0) : FabricLoader.getInstance().getConfigDir().resolve("larp-client").resolve("casino");
   }

   public static Path U(String var0) {
      return j().resolve(var0);
   }
}
