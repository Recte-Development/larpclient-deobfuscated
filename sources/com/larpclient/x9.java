package com.larpclient;

public final class x9 {
   public static final int O = 6;
   public static final int V = 96;
   public static final int K = 18;
   public static final int B = 4;
   private static final double[] Y = new double[]{0.0, 0.67, 0.9, 0.975, 1.0};
   private static final double[] E = new double[]{0.0, 0.08, 0.5, 1.0, 0.35};
   public static final int s = -16185079;
   private final x8 a = new x8();
   private double j;

   public void p(long var1, boolean var3, double var4) {
      double var6 = this.a.J();
      this.a.Q(var1, var3 && var4 > 0.0);
      this.j = this.j + (this.a.J() - var6) * Math.clamp(var4, 0.0, 1.0);
   }

   public double i() {
      return this.j;
   }

   public static double K(int var0, int var1) {
      return Math.max(0.0, Math.min(var0 * 0.34, (var0 - _q.calculate(var0, var1).menuWidth()) / 2.0 - 18.0));
   }

   public static double x(double var0, double var2, int var4, boolean var5) {
      double var6 = var5 ? 1.8 : 0.0;
      double var8 = 0.7 + 0.15 * Math.sin(var0 * 4.5 + var2 * 0.075 + var6) + 0.08 * Math.sin(var0 * 8.2 - var2 * 0.045 + var6 * 0.6 + var4 * 0.19);
      return var8 * (1.0 - var4 * 0.133);
   }

   public static int j(double var0, int var2, boolean var3) {
      return J(var0, var2, var3, 1.0);
   }

   public static double G(int var0) {
      return Y[var0];
   }

   public static int J(double var0, int var2, boolean var3, double var4) {
      double var6 = 0.5 + 0.5 * Math.cos(var0 * 3.8 + (var3 ? 1.5 : -0.6));
      double var8 = 0.0;

      for (int var10 = 1; var10 < Y.length; var10++) {
         if (var4 <= Y[var10]) {
            double var11 = Math.clamp((var4 - Y[var10 - 1]) / (Y[var10] - Y[var10 - 1]), 0.0, 1.0);
            var8 = E[var10 - 1] + (E[var10] - E[var10 - 1]) * var11;
            break;
         }
      }

      double var15 = 8.0 + var2 * 0.65 + var6 * 2.0;
      double var12 = 17.0 + var2 * 2.4 + var6 * (9.0 + var2 * 0.6);
      int var14 = (int)Math.round(var15 + (var12 - var15) * var8);
      return 0xFF000000 | var14 << 16 | var14 << 8 | var14;
   }

   public x9.h Q(int var1, int var2, int var3) {
      return X(var1, var2, var3, this.j);
   }

   public static x9.h X(int var0, int var1, int var2, double var3) {
      int var5 = var0 % 2 != 0 ? 1 : 0;
      double var6 = 0.25 + o(var0, 3) * 0.75;
      double var8 = 8.0 + var6 * 15.0;
      double var10 = K(var1, var2);
      double var12 = var8 * 0.72;
      if (var10 < var12 * 2.0 + 6.0) {
         return new x9.h(0.0, 0.0, var8, 0.0, 0.0);
      }

      double var14 = x8.Q((var0 / 2 + o(var0, 7) * 0.45) / 9.0 + var3 * (0.008 + var6 * 0.009));
      double var16 = -var12 + var14 * (var2 + var12 * 2.0);
      double var18 = 0.12 + o(var0, 11) * 0.68;
      double var20 = Math.sin(var3 * 0.13 + var0 * 1.7) * var10 * 0.035;
      double var22 = Math.clamp(var10 * var18 + var20, var12 + 2.0, var10 - var12 - 2.0);
      if (var5 != 0) {
         var22 = var1 - var22;
      }

      double var24 = Math.clamp(var14 * 12.0, 0.0, 1.0) * Math.clamp((1.0 - var14) * 12.0, 0.0, 1.0);
      double var26 = (var0 % 3 == 0 ? -1 : 1) * (0.1 + o(var0, 17) * 0.12);
      return new x9.h(var22, var16, var8, o(var0, 19) * Math.PI * 2.0 + var3 * var26, (0.09 + var6 * 0.17) * var24);
   }

   private static double o(int var0, int var1) {
      long var2 = (var0 + 1L) * 1103515245L + var1 * 12345L;
      var2 = (var2 ^ var2 >>> 16) * 73244475L;
      return (var2 & 16777215L) / 1.6777216E7;
   }

   public record h(double x, double y, double size, double rotation, double alpha) {
   }
}
