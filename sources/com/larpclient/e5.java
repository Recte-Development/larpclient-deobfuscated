package com.larpclient;

import java.util.ArrayList;
import java.util.List;

public final class e5 {
   public static final int o = 24;
   public static final int n = 6;

   public static e5.v e(int var0, List<Integer> var1) {
      int var2 = Math.max(1, var0);
      int var3 = 0;
      byte var4 = 0;
      ArrayList var5 = new ArrayList();

      for (int var7 : var1) {
         int var8 = Math.clamp(var7, 1, var2);
         if (var3 > 0 && var3 + var8 > var2) {
            var3 = 0;
            var4 += 30;
         }

         var5.add(new e5.c(var3, var4, var8));
         var3 += var8 + 6;
      }

      return new e5.v(List.copyOf(var5), var5.isEmpty() ? 0 : var4 + 24);
   }

   private e5() {
   }

   public record c(int x, int y, int width) {
   }

   public record v(List<e5.c> cells, int height) {
   }
}
