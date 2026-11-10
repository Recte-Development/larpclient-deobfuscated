package com.larpclient;

public final class gk {
   public static final int A = 6;
   public static final int B = 96;
   public static final int z = 18;
   public static final int o = 4;
   private static final double[] H = new double[]{0.0, 0.67, 0.9, 0.975, 1.0};
   private static final double[] G = new double[]{0.0, 0.08, 0.5, 1.0, 0.35};
   public static final int E = -16185079;
   private final lr a = new lr();
   private double P;

   public void E(long var1, boolean var3, double var4) {
      double var6 = this.a.x();
      this.a.H(var1, var3 && var4 > 0.0);
      this.P = this.P + (this.a.x() - var6) * Math.clamp(var4, 0.0, 1.0);
   }

   public double x() {
      return this.P;
   }

   public static double n(int var0, int var1) {
      return Math.max(0.0, Math.min(var0 * 0.34, (var0 - uz.calculate(var0, var1).menuWidth()) / 2.0 - 18.0));
   }

   public static double W(double var0, double var2, int var4, boolean var5) {
      double var6 = var5 ? 1.8 : 0.0;
      double var8 = 0.7 + 0.15 * Math.sin(var0 * 4.5 + var2 * 0.075 + var6) + 0.08 * Math.sin(var0 * 8.2 - var2 * 0.045 + var6 * 0.6 + var4 * 0.19);
      return var8 * (1.0 - var4 * 0.133);
   }

   public static int J(double var0, int var2, boolean var3) {
      return g(var0, var2, var3, 1.0);
   }

   public static double w(int var0) {
      return H[var0];
   }

   public static int g(double var0, int var2, boolean var3, double var4) {
      double var6 = 0.5 + 0.5 * Math.cos(var0 * 3.8 + (var3 ? 1.5 : -0.6));
      double var8 = 0.0;

      for (int var10 = 1; var10 < H.length; var10++) {
         if (var4 <= H[var10]) {
            double var11 = Math.clamp((var4 - H[var10 - 1]) / (H[var10] - H[var10 - 1]), 0.0, 1.0);
            var8 = G[var10 - 1] + (G[var10] - G[var10 - 1]) * var11;
            break;
         }
      }

      double var15 = 8.0 + var2 * 0.65 + var6 * 2.0;
      double var12 = 17.0 + var2 * 2.4 + var6 * (9.0 + var2 * 0.6);
      int var14 = (int)Math.round(var15 + (var12 - var15) * var8);
      return 0xFF000000 | var14 << 16 | var14 << 8 | var14;
   }

   public gk.x g(int var1, int var2, int var3) {
      return s(var1, var2, var3, this.P);
   }

   public static gk.x s(int var0, int var1, int var2, double var3) {
      int var5 = var0 % 2 != 0 ? 1 : 0;
      double var6 = 0.25 + O(var0, 3) * 0.75;
      double var8 = 8.0 + var6 * 15.0;
      double var10 = n(var1, var2);
      double var12 = var8 * 0.72;
      if (var10 < var12 * 2.0 + 6.0) {
         return new gk.x(0.0, 0.0, var8, 0.0, 0.0);
      }

      double var14 = lr.G((var0 / 2 + O(var0, 7) * 0.45) / 9.0 + var3 * (0.008 + var6 * 0.009));
      double var16 = -var12 + var14 * (var2 + var12 * 2.0);
      double var18 = 0.12 + O(var0, 11) * 0.68;
      double var20 = Math.sin(var3 * 0.13 + var0 * 1.7) * var10 * 0.035;
      double var22 = Math.clamp(var10 * var18 + var20, var12 + 2.0, var10 - var12 - 2.0);
      if (var5 != 0) {
         var22 = var1 - var22;
      }

      double var24 = Math.clamp(var14 * 12.0, 0.0, 1.0) * Math.clamp((1.0 - var14) * 12.0, 0.0, 1.0);
      double var26 = (var0 % 3 == 0 ? -1 : 1) * (0.1 + O(var0, 17) * 0.12);
      return new gk.x(var22, var16, var8, O(var0, 19) * Math.PI * 2.0 + var3 * var26, (0.09 + var6 * 0.17) * var24);
   }

   private static double O(int var0, int var1) {
      long var2 = (var0 + 1L) * 1103515245L + var1 * 12345L;
      var2 = (var2 ^ var2 >>> 16) * 73244475L;
      return (var2 & 16777215L) / 1.6777216E7;
   }

   public record x(double x, double y, double size, double rotation, double alpha) {
   }
}
