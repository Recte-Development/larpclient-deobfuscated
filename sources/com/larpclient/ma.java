package com.larpclient;

import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

public final class ma {
   public static final String l = "larp.casino.root";

   private ma() {
   }

   public static void Q(Path var0) {
      if (var0 == null) {
         System.clearProperty("larp.casino.root");
      } else {
         System.setProperty("larp.casino.root", var0.toAbsolutePath().toString());
      }
   }

   public static Path x() {
      String var0 = System.getProperty("larp.casino.root");
      return var0 != null && !var0.isBlank() ? Path.of(var0) : FabricLoader.getInstance().getConfigDir().resolve("larp-client").resolve("casino");
   }

   public static Path h(String var0) {
      return x().resolve(var0);
   }
}
