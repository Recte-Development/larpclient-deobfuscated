package com.larpclient;

import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

public final class h5 {
   public static final String w = "larp.casino.root";

   private h5() {
   }

   public static void o(Path var0) {
      if (var0 == null) {
         System.clearProperty("larp.casino.root");
      } else {
         System.setProperty("larp.casino.root", var0.toAbsolutePath().toString());
      }
   }

   public static Path H() {
      String var0 = System.getProperty("larp.casino.root");
      return var0 != null && !var0.isBlank() ? Path.of(var0) : FabricLoader.getInstance().getConfigDir().resolve("larp-client").resolve("casino");
   }

   public static Path G(String var0) {
      return H().resolve(var0);
   }
}
