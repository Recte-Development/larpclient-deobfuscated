package com.larpclient;

public final class y6 {
   private y6() {
   }

   public static boolean J(int var0, int var1, boolean var2) {
      return var2 || var1 >= 0 && var1 != var0;
   }

   public static int b(int var0, int var1, int var2, boolean var3) {
      if (var3 && var1 >= 0 && var2 >= 0 && var1 != var2) {
         return var0 == var1 ? var2 : (var0 == var2 ? var1 : var0);
      } else {
         return var0;
      }
   }
}
