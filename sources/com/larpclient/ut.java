package com.larpclient;

public final class ut {
   public static final int z = 5;
   public static final int Z = 20;
   public static final int U = 18;
   public static final double S = 0.421875;
   private static final int[][] x = new int[18][4];

   private ut() {
   }

   public static int F(int var0, int var1) {
      return x[var0][var1];
   }

   public static boolean W(int var0) {
      return var0 >= 16 || var0 % 4 >= 2;
   }

   public static double Y(int var0, int var1) {
      if (W(var0)) {
         return 0.0014204545454545455;
      }

      int var2 = F(var0, var1) / 4;
      return var0 % 4 == 0 ? (8.5 + var2 * 511.0 / 4.0) / 1056.0 : (536.5 + (4 - var2) * 511.0 / 4.0) / 1056.0;
   }

   public static double N(int var0, int var1) {
      return W(var0) ? 0.005859375 : (F(var0, var1) % 2 == 1 ? 20.5 : 235.5) / 256.0;
   }

   public static int g(double[] var0, int var1, int var2) {
      int var3 = F(var1, 0) * 3;
      int var4 = F(var1, 1) * 3;
      int var5 = F(var1, 2) * 3;
      double var6 = var0[var4] - var0[var3];
      double var8 = var0[var4 + 1] - var0[var3 + 1];
      double var10 = var0[var4 + 2] - var0[var3 + 2];
      double var12 = var0[var5] - var0[var3];
      double var14 = var0[var5 + 1] - var0[var3 + 1];
      double var16 = var0[var5 + 2] - var0[var3 + 2];
      double var18 = var8 * var16 - var10 * var14;
      double var20 = var10 * var12 - var6 * var16;
      double var22 = var6 * var14 - var8 * var12;
      double var24 = Math.sqrt(var18 * var18 + var20 * var20 + var22 * var22);
      double var26 = Math.max(0.0, (-var18 * 0.3 + var20 * 0.8 + var22 * 0.5) / (var24 * Math.sqrt(0.98)));
      int var28 = oh.j(var2, var2 & 0xFF000000, 1.0 - (W(var1) ? 0.6 + 0.2 * var26 : 0.72 + 0.28 * var26));
      return var2 & 0xFF000000 | var28 & 16777215;
   }

   public static void D(double[] var0, double var1, double[] var3) {
      for (int var4 = 0; var4 < 5; var4++) {
         double var5 = (var4 * 0.5 - 1.0) * var1;

         for (int var7 = 0; var7 < 4; var7++) {
            int var8 = (var4 * 4 + var7) * 3;
            var0[var8] = var5;
            var0[var8 + 1] = (var7 % 2 == 0 ? -1 : 1) * var1 * 0.421875;
            var0[var8 + 2] = var3[var4] + (var7 < 2 ? 1 : -1) * var1 * 0.004;
         }
      }
   }

   public static void w(double[] var0, double var1, double var3, double var5, double var7, double var9) {
      double var11 = Math.cos(var3);
      double var13 = Math.sin(var3);
      double var15 = Math.cos(var5);
      double var17 = Math.sin(var5);
      double var19 = Math.cos(var7);
      double var21 = Math.sin(var7);

      for (int var23 = 0; var23 < 5; var23++) {
         double var24 = (var23 * 0.5 - 1.0) * var1;
         double var26 = Math.sin(var23 * Math.PI / 4.0 + var9) * var1 * 0.1;

         for (int var28 = 0; var28 < 4; var28++) {
            double var29 = (var28 % 2 == 0 ? -1 : 1) * var1 * 0.421875;
            double var31 = var26 + (var28 < 2 ? 1 : -1) * var1 * 0.004;
            double var33 = var19 * var24 - var21 * var29;
            double var35 = var21 * var24 + var19 * var29;
            double var37 = var15 * var35 - var17 * var31;
            double var39 = var17 * var35 + var15 * var31;
            int var41 = (var23 * 4 + var28) * 3;
            var0[var41] = var11 * var33 + var13 * var39;
            var0[var41 + 1] = var37;
            var0[var41 + 2] = -var13 * var33 + var11 * var39;
         }
      }
   }

   static {
      for (int var0 = 0; var0 < 4; var0++) {
         int var1 = var0 * 4;
         int var2 = var1 + 4;
         x[var0 * 4] = new int[]{var1, var2, var2 + 1, var1 + 1};
         x[var0 * 4 + 1] = new int[]{var1 + 3, var2 + 3, var2 + 2, var1 + 2};
         x[var0 * 4 + 2] = new int[]{var1 + 1, var2 + 1, var2 + 3, var1 + 3};
         x[var0 * 4 + 3] = new int[]{var1 + 2, var2 + 2, var2, var1};
      }

      x[16] = new int[]{0, 1, 3, 2};
      x[17] = new int[]{18, 19, 17, 16};
   }
}
