package com.larpclient;

import java.util.Locale;

public final class xb {
   private xb() {
   }

   public static String c(Enum<?> var0) {
      String var1 = var0.name().toLowerCase(Locale.ROOT);
      return Character.toUpperCase(var1.charAt(0)) + var1.substring(1);
   }

   public static String v(double var0) {
      return var0 < 0.25 ? "Morning" : (var0 < 0.5 ? "Midday" : (var0 < 0.75 ? "Sunset" : "Night"));
   }

   public enum s {
      O(160),
      s(100),
      W(48),
      C(0);

      public final int g;

      s(int var3) {
         this.g = var3;
      }
   }

   public enum x {
      D,
      r,
      G,
      B;
   }
}
