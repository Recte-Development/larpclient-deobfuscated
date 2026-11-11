package com.larpclient.util.inventory;

public final class SlotSwapUtil {
   private SlotSwapUtil() {
   }

   public static boolean canSwapSlot(int var0, int var1, boolean var2) {
      return var2 || var1 >= 0 && var1 != var0;
   }

   public static int remapSlot(int var0, int var1, int var2, boolean var3) {
      if (var3 && var1 >= 0 && var2 >= 0 && var1 != var2) {
         return var0 == var1 ? var2 : (var0 == var2 ? var1 : var0);
      } else {
         return var0;
      }
   }
}
