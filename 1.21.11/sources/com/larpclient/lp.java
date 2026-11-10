package com.larpclient;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public final class lp {
   private lp() {
   }

   public static int M(int[] var0, int var1, int var2) {
      int var3 = 0;

      for (int var7 : var0) {
         if (var7 == var1) {
            var3++;
         }
      }

      if (var3 == 0) {
         return -1;
      }

      int var8 = Math.floorMod(var2, var3);

      for (int var9 = 0; var9 < var0.length; var9++) {
         if (var0[var9] == var1 && var8-- == 0) {
            return var9;
         }
      }

      return -1;
   }

   public static int S(int var0, int var1, int var2) {
      return var0 != 0 && (var0 != var1 || var2 == var1) ? var0 : var2;
   }

   public static int[] C(int[] var0) {
      if (var0.length != 9) {
         throw new IllegalArgumentException("Russian Roulette requires nine occupied slots.");
      }

      LinkedHashMap var1 = new LinkedHashMap();

      for (int var5 : var0) {
         var1.merge(var5, 1, Integer::sum);
      }

      if (var1.size() == 2 && var1.containsValue(8) && var1.containsValue(1)) {
         int[] var6 = new int[9];

         for (int var7 = 0; var7 < 9; var7++) {
            var6[var7] = var1.get(var0[var7]) == 8 ? 1 : 2;
         }

         return var6;
      } else {
         throw new IllegalArgumentException("Russian Roulette needs eight matching safe items and one different lose item.");
      }
   }

   public static int[] Q(int[] var0) {
      if (var0.length != 9) {
         throw new IllegalArgumentException("45/45/10 requires nine slots.");
      }

      LinkedHashMap var1 = new LinkedHashMap();

      for (int var5 : var0) {
         var1.merge(var5, 1, Integer::sum);
      }

      if (var1.size() == 3 && var1.values().stream().filter(var0x -> var0x == 4).count() == 2L && var1.containsValue(1)) {
         LinkedHashMap var6 = new LinkedHashMap();
         int var7 = 1;

         for (Entry var10 : var1.entrySet()) {
            var6.put((Integer)var10.getKey(), var10.getValue() == 1 ? 3 : var7++);
         }

         int[] var9 = new int[9];

         for (int var11 = 0; var11 < 9; var11++) {
            var9[var11] = (Integer)var6.get(var0[var11]);
         }

         return var9;
      } else {
         throw new IllegalArgumentException("45/45/10 needs four slots of each main item and one slot of the third item.");
      }
   }
}
