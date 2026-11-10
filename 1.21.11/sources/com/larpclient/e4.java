package com.larpclient;

import java.util.ArrayList;
import java.util.List;

public final class e4 {
   public static boolean I(int var0, int var1) {
      return var0 > 0 && var0 > var1;
   }

   public static int F(int var0, int var1) {
      float var2 = Math.clamp(var0 / 15.0F, 0.0F, 1.0F);
      return Math.clamp(var1, 0, 255) << 24 | Math.round(50.0F + 150.0F * var2) << 16 | Math.round(130.0F * var2) << 8 | Math.round(70.0F + 185.0F * var2);
   }

   public static List<e4.n> o(byte[] var0, int var1, int var2, int var3) {
      if (var0.length % 256 != 0) {
         throw new IllegalArgumentException("Incomplete light layers");
      }

      ArrayList var4 = new ArrayList();
      int var5 = 0;

      while (var5 < var0.length) {
         int var6 = var0[var5] & 15;
         int var7 = var5;
         if (var6 == 0) {
            var5++;
         } else {
            do {
               var5++;
            } while ((var5 & 15) != 0 && var5 < var0.length && var0[var5] == var6);

            var4.add(new e4.n(var1 * 16 + (var7 & 15), var3 + (var7 >> 8), var2 * 16 + (var7 >> 4 & 15), var5 - var7, var6));
         }
      }

      return List.copyOf(var4);
   }

   private e4() {
   }

   public record n(int x, int y, int z, int length, int light) {
   }
}
