package com.larpclient;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.IntPredicate;

final class f1 {
   static int d(int var0, int[] var1) {
      return i(var0, var1, var0x -> false);
   }

   static int i(int var0, int[] var1, IntPredicate var2) {
      for (int var3 = 0; var3 < 36; var3++) {
         int var4 = (var3 + 9) % 36;
         if (var4 != var0 && var1[var4] > 0 && var2.test(var4)) {
            return var4;
         }
      }

      if (var1[var0] == 1) {
         return var0;
      }

      for (int var5 = 0; var5 < 36; var5++) {
         if (var1[var5] == 0) {
            return var5;
         }
      }

      return -1;
   }

   static <T> Map<Integer, T> N(int var0, int var1, int var2, T var3, T var4, BiFunction<T, Integer, T> var5) {
      return l(var0, var1, var2, 0, (T)var3, (T)var4, var5);
   }

   static <T> Map<Integer, T> l(int var0, int var1, int var2, int var3, T var4, T var5, BiFunction<T, Integer, T> var6) {
      if (var1 < 0) {
         return Map.of();
      } else {
         return var0 == var1 ? Map.of(var0, (T)var6.apply(var5, 1)) : Map.of(var0, (T)var6.apply(var4, var2 - 1), var1, (T)var6.apply(var5, var3 + 1));
      }
   }
}
