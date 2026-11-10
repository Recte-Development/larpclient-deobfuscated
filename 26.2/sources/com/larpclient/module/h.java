package com.larpclient.module;

import java.util.Random;

public final class h {
   private final int[] v = new int[512];

   public h() {
      this(System.currentTimeMillis());
   }

   public h(long var1) {
      Random var3 = new Random(var1);
      int[] var4 = new int[256];
      int var5 = 0;

      while (var5 < 256) {
         var4[var5] = var5++;
      }

      for (int var8 = 0; var8 < 256; var8++) {
         int var6 = var3.nextInt(256 - var8) + var8;
         int var7 = var4[var8];
         var4[var8] = var4[var6];
         var4[var6] = var7;
      }

      for (int var9 = 0; var9 < 256; var9++) {
         this.v[var9] = this.v[var9 + 256] = var4[var9];
      }
   }

   public double i(double var1, double var3, double var5) {
      int var7 = (int)Math.floor(var1) & 0xFF;
      int var8 = (int)Math.floor(var3) & 0xFF;
      int var9 = (int)Math.floor(var5) & 0xFF;
      var1 -= Math.floor(var1);
      var3 -= Math.floor(var3);
      var5 -= Math.floor(var5);
      double var10 = r(var1);
      double var12 = r(var3);
      double var14 = r(var5);
      int var16 = this.v[var7] + var8;
      int var17 = this.v[var16] + var9;
      int var18 = this.v[var16 + 1] + var9;
      int var19 = this.v[var7 + 1] + var8;
      int var20 = this.v[var19] + var9;
      int var21 = this.v[var19 + 1] + var9;
      return f(
         var14,
         f(
            var12,
            f(var10, F(this.v[var17], var1, var3, var5), F(this.v[var20], var1 - 1.0, var3, var5)),
            f(var10, F(this.v[var18], var1, var3 - 1.0, var5), F(this.v[var21], var1 - 1.0, var3 - 1.0, var5))
         ),
         f(
            var12,
            f(var10, F(this.v[var17 + 1], var1, var3, var5 - 1.0), F(this.v[var20 + 1], var1 - 1.0, var3, var5 - 1.0)),
            f(var10, F(this.v[var18 + 1], var1, var3 - 1.0, var5 - 1.0), F(this.v[var21 + 1], var1 - 1.0, var3 - 1.0, var5 - 1.0))
         )
      );
   }

   private static double r(double var0) {
      return var0 * var0 * var0 * (var0 * (var0 * 6.0 - 15.0) + 10.0);
   }

   private static double f(double var0, double var2, double var4) {
      return var2 + var0 * (var4 - var2);
   }

   private static double F(int var0, double var1, double var3, double var5) {
      int var7 = var0 & 15;
      double var8 = var7 < 8 ? var1 : var3;
      double var10 = var7 < 4 ? var3 : (var7 != 12 && var7 != 14 ? var5 : var1);
      return ((var7 & 1) == 0 ? var8 : -var8) + ((var7 & 2) == 0 ? var10 : -var10);
   }
}
