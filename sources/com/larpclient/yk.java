package com.larpclient;

import java.util.function.IntPredicate;

record yk(int slotCount, int cursorCount) {
   static void completeDrawBaseline(int[] var0, int[] var1, int var2, int var3, int var4) {
      if (var1[var2] >= 1 && (var3 == var2 || var1[var3] == 0 || var0[var3] == var4) && (var3 != var2 || var1[var2] == 1)) {
         if (--var1[var2] == 0) {
            var0[var2] = -1;
         }

         var0[var3] = var4;
         var1[var3]++;
      } else {
         throw new IllegalArgumentException("draw baseline cannot preserve original destination items");
      }
   }

   static int dealtSlot(int var0, int[] var1, int[] var2, int[] var3, int[] var4, int var5) {
      int var6 = -1;

      for (int var7 = 0; var7 < var3.length; var7++) {
         int var8 = var1[var7] == var0 ? var2[var7] : 0;
         if (var3[var7] == var0 && var4[var7] == var8 + 1) {
            if (var7 == var5) {
               return var7;
            }

            if (var6 >= 0) {
               var6 = -2;
            } else if (var6 != -2) {
               var6 = var7;
            }
         }
      }

      return var6 < 0 ? -1 : var6;
   }

   static yk.e batch(int var0, int var1, int var2, int var3, boolean var4, int var5, int var6) {
      if (var0 >= 0 && var1 >= 0 && var0 != var1 && var2 <= var5) {
         int var7 = var2 == 1 ? 0 : 1;
         yk var8 = plan(var2, 0, var7, true, var5);
         if (var8 == null) {
            return null;
         }

         yk var9 = plan(var3, var8.cursorCount(), 1, var4, var6);
         if (var9 == null) {
            return null;
         }

         yk var10 = var9.cursorCount() == 0 ? new yk(var8.slotCount(), 0) : plan(var8.slotCount(), var9.cursorCount(), 0, true, var5);
         return var10 == null ? null : new yk.e(var8, var9, var10, var7);
      } else {
         return null;
      }
   }

   static int pickupCount(int var0) {
      return var0 / 2 + var0 % 2;
   }

   static int skipEmptyReturn(int var0, boolean var1, int var2, int var3) {
      if (!var1) {
         return var0;
      } else if (var0 == 2 && pickupCount(var2 + 1) == 1) {
         return 3;
      } else {
         return var0 == 5 && pickupCount(var3) == 1 ? 6 : var0;
      }
   }

   static yk plan(int var0, int var1, int var2, boolean var3, int var4) {
      if (var0 >= 0 && var1 >= 0 && (var2 == 0 || var2 == 1)) {
         if (var1 != 0) {
            int var6 = var2 == 1 ? 1 : var1;
            return var3 && var0 + var6 <= var4 ? new yk(var0 + var6, var1 - var6) : null;
         }

         if (var0 == 0) {
            return null;
         }

         int var5 = var2 == 0 ? var0 : pickupCount(var0);
         return new yk(var0 - var5, var5);
      } else {
         return null;
      }
   }

   record e(yk pickup, yk placement, yk returned, int pickupButton) {
      int clicks() {
         return this.placement.cursorCount() == 0 ? 2 : 3;
      }

      boolean execute(IntPredicate var1) {
         for (int var2 = 0; var2 < this.clicks(); var2++) {
            if (!var1.test(var2)) {
               return false;
            }
         }

         return true;
      }
   }
}
