package com.larpclient;

import java.util.Locale;

public final class tf {
   private tf() {
   }

   public static String o(Enum<?> var0) {
      String var1 = var0.name().toLowerCase(Locale.ROOT);
      return Character.toUpperCase(var1.charAt(0)) + var1.substring(1);
   }

   public static String R(double var0) {
      return var0 < 0.25 ? "Morning" : (var0 < 0.5 ? "Midday" : (var0 < 0.75 ? "Sunset" : "Night"));
   }

   public enum f {
      f(160),
      r(100),
      m(48),
      E(0);

      public final int y;

      f(int var3) {
         this.y = var3;
      }
   }

   public enum g {
      A,
      u,
      v,
      e;
   }
}
