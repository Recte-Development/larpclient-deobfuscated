package com.larpclient;

import java.util.Locale;

public final class ng {
   private ng() {
   }

   public static String X(Enum<?> var0) {
      String var1 = var0.name().toLowerCase(Locale.ROOT);
      return Character.toUpperCase(var1.charAt(0)) + var1.substring(1);
   }

   public static String p(double var0) {
      return var0 < 0.25 ? "Morning" : (var0 < 0.5 ? "Midday" : (var0 < 0.75 ? "Sunset" : "Night"));
   }

   public enum c {
      f(160),
      r(100),
      M(48),
      T(0);

      public final int g;

      c(int var3) {
         this.g = var3;
      }
   }

   public enum i {
      c,
      j,
      u,
      I;
   }
}
