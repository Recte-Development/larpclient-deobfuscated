package com.larpclient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class pc {
   public static final int W = 45;
   public static final List<String> x = List.of("Cheap", "Normal", "High");
   public static final List<String> d = List.of("$500K", "$1M", "$2M", "$5M", "$10M", "No Limit");
   private static final Pattern b = Pattern.compile("^auction \\(page (\\d+)\\)$");
   private static final Pattern R = Pattern.compile("^\\$\\s*([0-9][0-9,]*(?:\\.[0-9]+)?)\\s*([kmbt]?)$");
   private static final Pattern c = Pattern.compile("\\$\\s*([0-9][0-9,]*(?:\\.[0-9]+)?)\\s*([kmbt]?)(?![a-z0-9])");

   private pc() {
   }

   public static String J(String var0) {
      return "ah " + D(var0);
   }

   public static String a(pc.c var0) {
      return var0 == pc.c.x ? "ah firework rocket" : "ah bottle o' enchanting";
   }

   public static int q(String var0) {
      Matcher var1 = b.matcher(D(var0));
      if (!var1.matches()) {
         return -1;
      }

      try {
         return Integer.parseInt(var1.group(1));
      } catch (NumberFormatException var3) {
         return -1;
      }
   }

   public static long Y(List<String> var0) {
      if (var0 == null) {
         return -1L;
      }

      long var1 = -1L;

      for (String var4 : var0) {
         Matcher var5 = R.matcher(D(var4));
         if (var5.matches()) {
            if (var1 >= 0L) {
               return -1L;
            }

            var1 = i(var5.group(1), var5.group(2));
            if (var1 < 0L) {
               return -1L;
            }
         }
      }

      return var1;
   }

   public static long S(String var0) {
      Matcher var1 = c.matcher(D(var0));
      return var1.find() ? i(var1.group(1), var1.group(2)) : -1L;
   }

   static long i(String var0, String var1) {
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

   public static String b(long var0) {
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

   public static pc.i z(String var0, String var1) {
      if (!"minecraft:arrow".equals(var0)) {
         return pc.i.A;
      } else {
         String var2 = D(var1);
         if (var2.equals("next page")) {
            return pc.i.F;
         } else {
            return var2.equals("previous page") ? pc.i.w : pc.i.A;
         }
      }
   }

   public static pc.m r(String var0, String var1) {
      String var2 = D(var1);
      if (var2.contains("sell")) {
         return pc.m.o;
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
            return pc.m.R;
         } else if (var2.contains("confirm")
            || var2.equals("yes")
            || var2.startsWith("yes ")
            || var2.contains("accept")
            || var2.contains("buy")
            || var2.contains("purchase")) {
            return pc.m.A;
         } else if (var0 == null) {
            return pc.m.o;
         } else if (var0.equals("minecraft:lime_stained_glass_pane")
            || var0.equals("minecraft:green_stained_glass_pane")
            || var0.equals("minecraft:lime_wool")
            || var0.equals("minecraft:green_wool")
            || var0.equals("minecraft:lime_concrete")
            || var0.equals("minecraft:green_concrete")
            || var0.equals("minecraft:lime_dye")
            || var0.equals("minecraft:emerald_block")) {
            return pc.m.A;
         } else {
            return !var0.equals("minecraft:red_stained_glass_pane")
                  && !var0.equals("minecraft:red_wool")
                  && !var0.equals("minecraft:red_concrete")
                  && !var0.equals("minecraft:barrier")
                  && !var0.equals("minecraft:red_dye")
               ? pc.m.o
               : pc.m.R;
         }
      }
   }

   public static boolean e(String var0) {
      String var1 = D(var0);
      return var1.contains("sure") && var1.contains("buy") && !var1.contains("sell");
   }

   public static boolean N(String var0) {
      String var1 = D(var0);
      return var1.equals("yes") || var1.equals("confirm") || var1.equals("buy");
   }

   public static boolean J(String var0) {
      String var1 = D(var0);
      return var1.equals("no") || var1.equals("cancel");
   }

   public static pc.j p(String var0) {
      String var1 = D(var0);
      if (var1.isEmpty() || var1.contains("whispers") || var1.startsWith("<") || var1.contains(" -> ")) {
         return pc.j.R;
      } else if (var1.contains("already bought")
         || var1.contains("already been bought")
         || var1.contains("no longer available")
         || var1.contains("no longer exists")
         || var1.contains("already sold")) {
         return pc.j.C;
      } else if (var1.contains("not enough money")
         || var1.contains("don't have enough money")
         || var1.contains("don’t have enough money")
         || var1.contains("cannot afford")
         || var1.contains("can't afford")
         || var1.contains("insufficient")) {
         return pc.j.h;
      } else if (!var1.contains("inventory is full") && !var1.contains("inventory full") && !var1.contains("no space")) {
         return (var1.contains("bought") || var1.contains("purchased")) && var1.contains("$") ? pc.j.E : pc.j.R;
      } else {
         return pc.j.a;
      }
   }

   public static long H(String var0, pc.c var1) {
      int var2 = var1 == pc.c.x ? 1 : 0;

      return switch (var0) {
         case "Cheap" -> var2 != 0 ? 35000L : 320000L;
         case "High" -> var2 != 0 ? 150000L : 700000L;
         default -> var2 != 0 ? 60000L : 450000L;
      };
   }

   public static long V(String var0) {
      return switch (var0) {
         case "$500K" -> 500000L;
         case "$1M" -> 1000000L;
         case "$5M" -> 5000000L;
         case "$10M" -> 10000000L;
         case "No Limit" -> -1L;
         default -> 2000000L;
      };
   }

   public static boolean u(long var0, int var2, long var3) {
      return var0 >= 0L && var2 > 0 && var2 <= 64
         ? BigDecimal.valueOf(var0).multiply(BigDecimal.valueOf(64L)).compareTo(BigDecimal.valueOf(var3).multiply(BigDecimal.valueOf(var2))) <= 0
         : false;
   }

   public static String D(String var0) {
      return var0 == null ? "" : var0.replaceAll("§.", "").replace(' ', ' ').trim().toLowerCase(Locale.ROOT);
   }

   public enum c {
      x,
      T;
   }

   public enum i {
      F,
      w,
      A;
   }

   public enum j {
      R,
      C,
      h,
      a,
      E;
   }

   public enum m {
      A,
      R,
      o;
   }
}
