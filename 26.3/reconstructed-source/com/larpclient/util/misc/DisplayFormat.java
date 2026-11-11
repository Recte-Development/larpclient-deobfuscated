package com.larpclient.util.misc;

import java.util.Locale;

public final class DisplayFormat {
   private DisplayFormat() {
   }

   public static String formatEnumName(Enum<?> var0) {
      String var1 = var0.name().toLowerCase(Locale.ROOT);
      return Character.toUpperCase(var1.charAt(0)) + var1.substring(1);
   }

   public static String timeOfDayName(double var0) {
      return var0 < 0.25 ? "Morning" : (var0 < 0.5 ? "Midday" : (var0 < 0.75 ? "Sunset" : "Night"));
   }

   public enum MenuQuality {
      HIGH(160),
      MEDIUM(100),
      LOW(48),
      OFF(0);

      public final int PARTICLECOUNT;

      MenuQuality(int var3) {
         this.PARTICLECOUNT = var3;
      }
   }

   public enum Season {
      SPRING,
      SUMMER,
      AUTUMN,
      WINTER;
   }
}
