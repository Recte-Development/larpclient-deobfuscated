package com.larpclient;

import java.util.Locale;

public final class td {
   private td() {
   }

   public static String S(Enum<?> var0) {
      String var1 = var0.name().toLowerCase(Locale.ROOT);
      return Character.toUpperCase(var1.charAt(0)) + var1.substring(1);
   }

   public static String K(double var0) {
      return var0 < 0.25 ? "Morning" : (var0 < 0.5 ? "Midday" : (var0 < 0.75 ? "Sunset" : "Night"));
   }

   public enum a {
      x,
      E,
      w,
      X;
   }

   public enum r {
      g(160),
      f(100),
      s(48),
      y(0);

      public final int L;

      r(int var3) {
         this.L = var3;
      }
   }
}
