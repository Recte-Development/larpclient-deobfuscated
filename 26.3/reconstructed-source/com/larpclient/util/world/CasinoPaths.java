package com.larpclient.util.world;

import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

public final class CasinoPaths {
   public static final String rOOT_PROPERTY = "larp.casino.root";

   private CasinoPaths() {
   }

   public static void setRoot(Path var0) {
      if (var0 == null) {
         System.clearProperty("larp.casino.root");
      } else {
         System.setProperty("larp.casino.root", var0.toAbsolutePath().toString());
      }
   }

   public static Path getRoot() {
      String var0 = System.getProperty("larp.casino.root");
      return var0 != null && !var0.isBlank() ? Path.of(var0) : FabricLoader.getInstance().getConfigDir().resolve("larp-client").resolve("casino");
   }

   public static Path resolve(String var0) {
      return getRoot().resolve(var0);
   }
}
