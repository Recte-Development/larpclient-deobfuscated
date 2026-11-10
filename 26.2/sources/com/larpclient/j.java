package com.larpclient;

import java.util.Locale;

public final class j {
   private j() {
   }

   public static String Y(Enum<?> var0) {
      String var1 = var0.name().toLowerCase(Locale.ROOT);
      return Character.toUpperCase(var1.charAt(0)) + var1.substring(1);
   }

   public static String A(double var0) {
      return var0 < 0.25 ? "Morning" : (var0 < 0.5 ? "Midday" : (var0 < 0.75 ? "Sunset" : "Night"));
   }

   public enum r {
      H,
      C,
      L,
      M;
   }

   public enum v {
      s(160),
      H(100),
      v(48),
      A(0);

      public final int P;

      v(int var3) {
         this.P = var3;
      }
   }
}
