package com.larpclient;

import com.larpclient.module.Module;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public final class d3 {
   private d3() {
   }

   private static String k(String var0) {
      StringBuilder var1 = new StringBuilder();
      var0.toLowerCase(Locale.ROOT).codePoints().filter(Character::isLetterOrDigit).forEach(var1::appendCodePoint);
      return var1.toString();
   }

   private static String x(String var0) {
      StringBuilder var1 = new StringBuilder();
      byte var2 = 1;

      for (int var6 : var0.codePoints().toArray()) {
         if (!Character.isLetterOrDigit(var6)) {
            var2 = 1;
         } else {
            if (var2 != 0 || Character.isUpperCase(var6)) {
               var1.appendCodePoint(Character.toLowerCase(var6));
            }

            var2 = 0;
         }
      }

      return var1.toString();
   }

   public static int i(Module var0, String var1) {
      if (var0 instanceof ChooseASkyEffectAndAdjustItsAppearanceModule) {
         return Math.min(Y("Sky Shader", var1), Math.min(Y("CustomSky", var1), Y("Sky", var1)));
      }

      int var2 = Math.min(Y(var0.getDisplayName(), var1), Y(var0.getName(), var1));

      for (String var4 : var0.getSearchAliases()) {
         var2 = Math.min(var2, Y(var4, var1));
      }

      return var2;
   }

   private static int Y(String var0, String var1) {
      String var2 = k(var1);
      String var3 = k(var0);
      if (var2.isEmpty()) {
         return Integer.MAX_VALUE;
      }

      if (var3.startsWith(var2)) {
         return 0;
      }

      if (var2.length() >= 2 && x(var0).equals(var2)) {
         return 2;
      }

      if (var3.contains(var2)) {
         return 3;
      }

      if (var2.length() >= 3 && var2.length() <= 64) {
         int var4 = var2.length() >= 7 ? 2 : 1;
         int var5 = var4 + 1;

         for (int var6 = Math.max(1, var2.length() - var4); var6 <= Math.min(var3.length(), var2.length() + var4); var6++) {
            var5 = Math.min(var5, g(var2, var3.substring(0, var6)));
         }

         return var5 <= var4 ? 10 + var5 : Integer.MAX_VALUE;
      } else {
         return Integer.MAX_VALUE;
      }
   }

   private static int g(String var0, String var1) {
      int[][] var2 = new int[var0.length() + 1][var1.length() + 1];
      int var3 = 0;

      while (var3 <= var0.length()) {
         var2[var3][0] = var3++;
      }

      var3 = 0;

      while (var3 <= var1.length()) {
         var2[0][var3] = var3++;
      }

      for (int var6 = 1; var6 <= var0.length(); var6++) {
         for (int var4 = 1; var4 <= var1.length(); var4++) {
            var2[var6][var4] = Math.min(
               Math.min(var2[var6 - 1][var4] + 1, var2[var6][var4 - 1] + 1),
               var2[var6 - 1][var4 - 1] + (var0.charAt(var6 - 1) == var1.charAt(var4 - 1) ? 0 : 1)
            );
            if (var6 > 1 && var4 > 1 && var0.charAt(var6 - 1) == var1.charAt(var4 - 2) && var0.charAt(var6 - 2) == var1.charAt(var4 - 1)) {
               var2[var6][var4] = Math.min(var2[var6][var4], var2[var6 - 2][var4 - 2] + 1);
            }
         }
      }

      return var2[var0.length()][var1.length()];
   }

   public static List<String> E(List<Module> var0, String var1) {
      record 1t(Module module, int rank) {
      }

      return !var1.isBlank() && !var0.stream().anyMatch(var1x -> var1x.getDisplayName().equalsIgnoreCase(var1.trim()))
         ? var0.stream()
            .map(var1x -> new 1t(var1x, i(var1x, var1)))
            .filter(var0x -> var0x.rank() != Integer.MAX_VALUE)
            .sorted(
               Comparator.comparingInt(1t::rank)
                  .thenComparing(var0x -> var0x.module().getDisplayName(), String.CASE_INSENSITIVE_ORDER)
                  .thenComparing(var0x -> var0x.module().getName())
            )
            .map(var0x -> var0x.module().getDisplayName())
            .distinct()
            .limit(6L)
            .toList()
         : List.of();
   }
}
