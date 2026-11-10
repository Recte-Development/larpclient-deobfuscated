package com.larpclient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class je {
   public static final int S = 45;
   public static final List<String> g = List.of("Cheap", "Normal", "High");
   public static final List<String> d = List.of("$500K", "$1M", "$2M", "$5M", "$10M", "No Limit");
   private static final Pattern n = Pattern.compile("^auction \\(page (\\d+)\\)$");
   private static final Pattern i = Pattern.compile("^\\$\\s*([0-9][0-9,]*(?:\\.[0-9]+)?)\\s*([kmbt]?)$");
   private static final Pattern p = Pattern.compile("\\$\\s*([0-9][0-9,]*(?:\\.[0-9]+)?)\\s*([kmbt]?)(?![a-z0-9])");

   private je() {
   }

   public static String R(String var0) {
      return "ah " + w(var0);
   }

   public static String q(je.w var0) {
      return var0 == je.w.W ? "ah firework rocket" : "ah bottle o' enchanting";
   }

   public static int w(String var0) {
      Matcher var1 = n.matcher(w(var0));
      if (!var1.matches()) {
         return -1;
      }

      try {
         return Integer.parseInt(var1.group(1));
      } catch (NumberFormatException var3) {
         return -1;
      }
   }

   public static long M(List<String> var0) {
      if (var0 == null) {
         return -1L;
      }

      long var1 = -1L;

      for (String var4 : var0) {
         Matcher var5 = i.matcher(w(var4));
         if (var5.matches()) {
            if (var1 >= 0L) {
               return -1L;
            }

            var1 = U(var5.group(1), var5.group(2));
            if (var1 < 0L) {
               return -1L;
            }
         }
      }

      return var1;
   }

   public static long A(String var0) {
      Matcher var1 = p.matcher(w(var0));
      return var1.find() ? U(var1.group(1), var1.group(2)) : -1L;
   }

   static long U(String var0, String var1) {
      try {
         BigDecimal var2 = new BigDecimal(var0.replace(",", ""));

         byte var3 = switch (var1) {
            case "k" -> 3;
            case "m" -> 6;
            case "b" -> 9;
            case "t" -> 12;
            default -> 0;
         };
         var2 = var2.scaleByPowerOfTen(var3);
         return var2.signum() >= 0 && var2.compareTo(BigDecimal.valueOf(4611686018427387903L)) <= 0
            ? var2.setScale(0, RoundingMode.HALF_UP).longValueExact()
            : -1L;
      } catch (NumberFormatException | ArithmeticException var6) {
         return -1L;
      }
   }

   public static String z(long var0) {
      if (var0 < 0L) {
         return "no limit";
      }

      String[] var2 = new String[]{"", "K", "M", "B", "T"};
      double var3 = var0;

      int var5;
      for (var5 = 0; var3 >= 1000.0 && var5 < var2.length - 1; var5++) {
         var3 /= 1000.0;
      }

      String var6 = var5 == 0 ? Long.toString(var0) : String.format(Locale.ROOT, var3 >= 100.0 ? "%.0f" : "%.1f", var3).replaceAll("\\.0$", "");
      return "$" + var6 + var2[var5];
   }

   public static je.v d(String var0, String var1) {
      if (!"minecraft:arrow".equals(var0)) {
         return je.v.S;
      } else {
         String var2 = w(var1);
         if (var2.equals("next page")) {
            return je.v.Q;
         } else {
            return var2.equals("previous page") ? je.v.o : je.v.S;
         }
      }
   }

   public static je.k Y(String var0, String var1) {
      String var2 = w(var1);
      if (var2.contains("sell")) {
         return je.k.V;
      } else {
         int var3 = !var2.contains("cancel")
               && !var2.contains("deny")
               && !var2.contains("decline")
               && !var2.contains("back")
               && !var2.equals("no")
               && !var2.startsWith("no ")
            ? 0
            : 1;
         if (var3 != 0) {
            return je.k.h;
         } else if (var2.contains("confirm")
            || var2.equals("yes")
            || var2.startsWith("yes ")
            || var2.contains("accept")
            || var2.contains("buy")
            || var2.contains("purchase")) {
            return je.k.z;
         } else if (var0 == null) {
            return je.k.V;
         } else if (var0.equals("minecraft:lime_stained_glass_pane")
            || var0.equals("minecraft:green_stained_glass_pane")
            || var0.equals("minecraft:lime_wool")
            || var0.equals("minecraft:green_wool")
            || var0.equals("minecraft:lime_concrete")
            || var0.equals("minecraft:green_concrete")
            || var0.equals("minecraft:lime_dye")
            || var0.equals("minecraft:emerald_block")) {
            return je.k.z;
         } else {
            return !var0.equals("minecraft:red_stained_glass_pane")
                  && !var0.equals("minecraft:red_wool")
                  && !var0.equals("minecraft:red_concrete")
                  && !var0.equals("minecraft:barrier")
                  && !var0.equals("minecraft:red_dye")
               ? je.k.V
               : je.k.h;
         }
      }
   }

   public static boolean q(String var0) {
      String var1 = w(var0);
      return var1.contains("sure") && var1.contains("buy") && !var1.contains("sell");
   }

   public static boolean m(String var0) {
      String var1 = w(var0);
      return var1.equals("yes") || var1.equals("confirm") || var1.equals("buy");
   }

   public static boolean a(String var0) {
      String var1 = w(var0);
      return var1.equals("no") || var1.equals("cancel");
   }

   public static je.d I(String var0) {
      String var1 = w(var0);
      if (var1.isEmpty() || var1.contains("whispers") || var1.startsWith("<") || var1.contains(" -> ")) {
         return je.d.d;
      } else if (var1.contains("already bought")
         || var1.contains("already been bought")
         || var1.contains("no longer available")
         || var1.contains("no longer exists")
         || var1.contains("already sold")) {
         return je.d.K;
      } else if (var1.contains("not enough money")
         || var1.contains("don't have enough money")
         || var1.contains("don’t have enough money")
         || var1.contains("cannot afford")
         || var1.contains("can't afford")
         || var1.contains("insufficient")) {
         return je.d.Y;
      } else if (!var1.contains("inventory is full") && !var1.contains("inventory full") && !var1.contains("no space")) {
         return (var1.contains("bought") || var1.contains("purchased")) && var1.contains("$") ? je.d.I : je.d.d;
      } else {
         return je.d.z;
      }
   }

   public static long U(String var0, je.w var1) {
      int var2 = var1 == je.w.W ? 1 : 0;

      return switch (var0) {
         case "Cheap" -> var2 != 0 ? 35000L : 320000L;
         case "High" -> var2 != 0 ? 150000L : 700000L;
         default -> var2 != 0 ? 60000L : 450000L;
      };
   }

   public static long p(String var0) {
      return switch (var0) {
         case "$500K" -> 500000L;
         case "$1M" -> 1000000L;
         case "$5M" -> 5000000L;
         case "$10M" -> 10000000L;
         case "No Limit" -> -1L;
         default -> 2000000L;
      };
   }

   public static boolean g(long var0, int var2, long var3) {
      return var0 >= 0L && var2 > 0 && var2 <= 64
         ? BigDecimal.valueOf(var0).multiply(BigDecimal.valueOf(64L)).compareTo(BigDecimal.valueOf(var3).multiply(BigDecimal.valueOf(var2))) <= 0
         : false;
   }

   public static String w(String var0) {
      return var0 == null ? "" : var0.replaceAll("§.", "").replace(' ', ' ').trim().toLowerCase(Locale.ROOT);
   }

   public enum d {
      d,
      K,
      Y,
      z,
      I;
   }

   public enum k {
      z,
      h,
      V;
   }

   public enum v {
      Q,
      o,
      S;
   }

   public enum w {
      W,
      A;
   }
}
