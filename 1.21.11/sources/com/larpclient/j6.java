package com.larpclient;

import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

public final class j6 {
   public static final String i = "larp.casino.root";

   private j6() {
   }

   public static void l(Path var0) {
      if (var0 == null) {
         System.clearProperty("larp.casino.root");
      } else {
         System.setProperty("larp.casino.root", var0.toAbsolutePath().toString());
      }
   }

   public static Path r() {
      String var0 = System.getProperty("larp.casino.root");
      return var0 != null && !var0.isBlank() ? Path.of(var0) : FabricLoader.getInstance().getConfigDir().resolve("larp-client").resolve("casino");
   }

   public static Path l(String var0) {
      return r().resolve(var0);
   }
}
