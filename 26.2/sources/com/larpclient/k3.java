package com.larpclient;

public final class k3 {
   private k3() {
   }

   public static boolean z(int var0, int var1, boolean var2) {
      return var2 || var1 >= 0 && var1 != var0;
   }

   public static int f(int var0, int var1, int var2, boolean var3) {
      if (var3 && var1 >= 0 && var2 >= 0 && var1 != var2) {
         return var0 == var1 ? var2 : (var0 == var2 ? var1 : var0);
      } else {
         return var0;
      }
   }
}
