package com.larpclient.util.inventory;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.IntPredicate;

final class SlotSwapFinder {
   static SlotSwapFinder.SlotSwap findSwap(List<Integer> var0, List<Integer> var1, List<Integer> var2, BiPredicate<Integer, Integer> var3, IntPredicate var4) {
      for (int var6 : var2) {
         if (var4.test(var6)) {
            for (int var8 : var0) {
               if (var8 != var6) {
                  for (int var10 : var1) {
                     if (var3.test(var8, var10)) {
                        return new SlotSwapFinder.SlotSwap(var8, var10, var6);
                     }
                  }
               }
            }
         }
      }

      return null;
   }

   record SlotSwap(int source, int destination, int returnSlot) {
   }
}
