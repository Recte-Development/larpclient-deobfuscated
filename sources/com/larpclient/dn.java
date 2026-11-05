package com.larpclient;

import java.util.ArrayList;
import java.util.List;

public final class dn {
   public static final int u = 24;
   public static final int G = 6;

   public static dn.i U(int var0, List<Integer> var1) {
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

         var5.add(new dn.v(var3, var4, var8));
         var3 += var8 + 6;
      }

      return new dn.i(List.copyOf(var5), var5.isEmpty() ? 0 : var4 + 24);
   }

   private dn() {
   }

   public record i(List<dn.v> cells, int height) {
   }

   public record v(int x, int y, int width) {
   }
}
