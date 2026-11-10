package com.larpclient;

public final class a3 {
   private a3() {
   }

   public static int[] F(int[] var0, int var1, int var2, boolean var3) {
      int[] var4 = (int[])var0.clone();
      if (var3 && var1 != var2 && var1 >= 0 && var2 >= 0 && var1 < var0.length && var2 < var0.length && var0[var2] >= 1) {
         var4[var1]++;
         var4[var2]--;
         return var4;
      } else {
         return var4;
      }
   }
}
