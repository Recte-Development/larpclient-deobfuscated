package com.larpclient;

public final class x {
   public static final int Y = 6;
   private static final double[] R = new double[]{0.0, 1.15, 0.0, 0.0, -0.8, 0.0, -0.5, 0.0, 0.0, 0.0, 0.0, -0.32, 0.6, 0.1, 0.0, 0.0, 0.0, 0.4};
   private static final int[][] I = new int[][]{{0, 3, 2}, {0, 4, 3}, {0, 5, 4}, {0, 2, 5}, {1, 2, 3}, {1, 3, 4}, {1, 4, 5}, {1, 5, 2}};

   public static int b() {
      return I.length;
   }

   public static int s(int var0, int var1) {
      return I[var0][var1 == 3 ? 2 : var1];
   }

   public static double u(double var0, double var2, double var4) {
      double var6 = Math.cos(var2);
      double var8 = Math.sin(var2);
      double var10 = Math.cos(var4);
      double var12 = Math.sin(var4);
      double var14 = 0.0;

      for (byte var16 = 0; var16 < R.length; var16 += 3) {
         var14 = Math.min(var14, (R[var16] * var12 + R[var16 + 1] * var10) * var6 - R[var16 + 2] * var8);
      }

      return -var14 * var0;
   }

   public static void L(double[] var0, double var1, double var3, double var5, double var7) {
      double var9 = Math.cos(var3);
      double var11 = Math.sin(var3);
      double var13 = Math.cos(var5);
      double var15 = Math.sin(var5);
      double var17 = Math.cos(var7);
      double var19 = Math.sin(var7);

      for (byte var21 = 0; var21 < R.length; var21 += 3) {
         double var22 = R[var21] * var17 - R[var21 + 1] * var19;
         double var24 = R[var21] * var19 + R[var21 + 1] * var17;
         double var26 = var24 * var15 + R[var21 + 2] * var13;
         var24 = var24 * var13 - R[var21 + 2] * var15;
         var0[var21] = (var22 * var9 + var26 * var11) * var1;
         var0[var21 + 1] = var24 * var1;
         var0[var21 + 2] = (-var22 * var11 + var26 * var9) * var1;
      }
   }

   public static int s(double[] var0, int var1, int var2) {
      int var3 = I[var1][0] * 3;
      int var4 = I[var1][1] * 3;
      int var5 = I[var1][2] * 3;
      double var6 = var0[var4] - var0[var3];
      double var8 = var0[var4 + 1] - var0[var3 + 1];
      double var10 = var0[var4 + 2] - var0[var3 + 2];
      double var12 = var0[var5] - var0[var3];
      double var14 = var0[var5 + 1] - var0[var3 + 1];
      double var16 = var0[var5 + 2] - var0[var3 + 2];
      double var18 = var8 * var16 - var10 * var14;
      double var20 = var10 * var12 - var6 * var16;
      double var22 = var6 * var14 - var8 * var12;
      double var24 = 0.68
         + 0.32 * Math.max(0.0, (var18 * 0.3 + var20 * 0.8 + var22 * 0.52) / Math.max(1.0E-12, Math.sqrt(var18 * var18 + var20 * var20 + var22 * var22)));
      int var26 = var2 & 0xFF000000;

      for (byte var27 = 0; var27 <= 16; var27 += 8) {
         var26 |= (int)((var2 >>> var27 & 0xFF) * var24) << var27;
      }

      return var26;
   }

   private x() {
   }
}
