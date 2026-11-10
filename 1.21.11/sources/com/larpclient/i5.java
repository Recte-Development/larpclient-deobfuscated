package com.larpclient;

public final class i5 {
   public static final int v = 4;

   private static double c(double var0, double var2) {
      return var0 != 0.0 && var2 != 0.0 && Math.signum(var0) == Math.signum(var2) ? 2.0 * var0 * var2 / (var0 + var2) : 0.0;
   }

   public static double x(double var0, double var2, double var4, double var6, double var8) {
      var8 = Math.clamp(var8, 0.0, 1.0);
      double var10 = var4 - var2;
      double var12 = c(var2 - var0, var10);
      double var14 = c(var10, var6 - var4);
      double var16 = var8 * var8;
      double var18 = var16 * var8;
      double var20 = (2.0 * var18 - 3.0 * var16 + 1.0) * var2
         + (var18 - 2.0 * var16 + var8) * var12
         + (-2.0 * var18 + 3.0 * var16) * var4
         + (var18 - var16) * var14;
      return Math.clamp(var20, Math.min(var2, var4), Math.max(var2, var4));
   }

   private i5() {
   }
}
