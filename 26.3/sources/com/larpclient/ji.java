package com.larpclient;

public final class ji {
   public static final int X = 6;
   public static final int W = 96;
   public static final int e = 18;
   public static final int G = 4;
   private static final double[] F = new double[]{0.0, 0.67, 0.9, 0.975, 1.0};
   private static final double[] p = new double[]{0.0, 0.08, 0.5, 1.0, 0.35};
   public static final int r = -16185079;
   private final s2 a = new s2();
   private double l;

   public void b(long var1, boolean var3, double var4) {
      double var6 = this.a.A();
      this.a.k(var1, var3 && var4 > 0.0);
      this.l = this.l + (this.a.A() - var6) * Math.clamp(var4, 0.0, 1.0);
   }

   public double G() {
      return this.l;
   }

   public static double y(int var0, int var1) {
      return Math.max(0.0, Math.min(var0 * 0.34, (var0 - qa.calculate(var0, var1).menuWidth()) / 2.0 - 18.0));
   }

   public static double K(double var0, double var2, int var4, boolean var5) {
      double var6 = var5 ? 1.8 : 0.0;
      double var8 = 0.7 + 0.15 * Math.sin(var0 * 4.5 + var2 * 0.075 + var6) + 0.08 * Math.sin(var0 * 8.2 - var2 * 0.045 + var6 * 0.6 + var4 * 0.19);
      return var8 * (1.0 - var4 * 0.133);
   }

   public static int O(double var0, int var2, boolean var3) {
      return T(var0, var2, var3, 1.0);
   }

   public static double u(int var0) {
      return F[var0];
   }

   public static int T(double var0, int var2, boolean var3, double var4) {
      double var6 = 0.5 + 0.5 * Math.cos(var0 * 3.8 + (var3 ? 1.5 : -0.6));
      double var8 = 0.0;

      for (int var10 = 1; var10 < F.length; var10++) {
         if (var4 <= F[var10]) {
            double var11 = Math.clamp((var4 - F[var10 - 1]) / (F[var10] - F[var10 - 1]), 0.0, 1.0);
            var8 = p[var10 - 1] + (p[var10] - p[var10 - 1]) * var11;
            break;
         }
      }

      double var15 = 8.0 + var2 * 0.65 + var6 * 2.0;
      double var12 = 17.0 + var2 * 2.4 + var6 * (9.0 + var2 * 0.6);
      int var14 = (int)Math.round(var15 + (var12 - var15) * var8);
      return 0xFF000000 | var14 << 16 | var14 << 8 | var14;
   }

   public ji.g A(int var1, int var2, int var3) {
      return Q(var1, var2, var3, this.l);
   }

   public static ji.g Q(int var0, int var1, int var2, double var3) {
      int var5 = var0 % 2 != 0 ? 1 : 0;
      double var6 = 0.25 + k(var0, 3) * 0.75;
      double var8 = 8.0 + var6 * 15.0;
      double var10 = y(var1, var2);
      double var12 = var8 * 0.72;
      if (var10 < var12 * 2.0 + 6.0) {
         return new ji.g(0.0, 0.0, var8, 0.0, 0.0);
      }

      double var14 = s2.S((var0 / 2 + k(var0, 7) * 0.45) / 9.0 + var3 * (0.008 + var6 * 0.009));
      double var16 = -var12 + var14 * (var2 + var12 * 2.0);
      double var18 = 0.12 + k(var0, 11) * 0.68;
      double var20 = Math.sin(var3 * 0.13 + var0 * 1.7) * var10 * 0.035;
      double var22 = Math.clamp(var10 * var18 + var20, var12 + 2.0, var10 - var12 - 2.0);
      if (var5 != 0) {
         var22 = var1 - var22;
      }

      double var24 = Math.clamp(var14 * 12.0, 0.0, 1.0) * Math.clamp((1.0 - var14) * 12.0, 0.0, 1.0);
      double var26 = (var0 % 3 == 0 ? -1 : 1) * (0.1 + k(var0, 17) * 0.12);
      return new ji.g(var22, var16, var8, k(var0, 19) * Math.PI * 2.0 + var3 * var26, (0.09 + var6 * 0.17) * var24);
   }

   private static double k(int var0, int var1) {
      long var2 = (var0 + 1L) * 1103515245L + var1 * 12345L;
      var2 = (var2 ^ var2 >>> 16) * 73244475L;
      return (var2 & 16777215L) / 1.6777216E7;
   }

   public record g(double x, double y, double size, double rotation, double alpha) {
   }
}
