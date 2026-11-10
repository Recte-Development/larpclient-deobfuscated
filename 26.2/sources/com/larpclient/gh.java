package com.larpclient;

import net.minecraft.util.Mth;

public final class gh {
   public static final double S = 45.0;
   public static final double Q = 14.0;
   public static final double x = 0.095;
   static final int O = 60;
   static final int C = 90;
   static final float Z = 12.0F;
   static final float F = 9.0F;
   private static final float[] X = new float[]{0.0F, -12.0F, 12.0F};
   private static final float[] z = new float[]{-30.0F, 30.0F, -55.0F, 55.0F, -90.0F, 90.0F, -140.0F, 140.0F};
   private static final float[] K = new float[]{-62.0F, -46.0F, -33.0F, -23.0F, -15.0F, -9.0F, -4.0F, 1.0F, 6.0F, 12.0F, 19.0F, 27.0F, 36.0F};
   private static final float[] P = new float[]{0.0F, -18.0F, 18.0F, -36.0F, 36.0F, -60.0F, 60.0F, -90.0F, 90.0F};
   private static final float[] w = new float[]{-28.0F, -18.0F, -10.0F, -4.0F, 0.0F, 4.0F, 8.0F, 12.0F, 16.0F, 21.0F, 27.0F, 34.0F};
   private static final float[] r = new float[]{-4.0F, 0.0F, 4.0F, 8.0F, 12.0F, 16.0F};
   private static final float[] l = new float[]{-28.0F, -18.0F};
   private static final int[] R = new int[]{10, 25, 45};
   private final x.w I = new x.w();
   private double g = Double.NEGATIVE_INFINITY;
   private int E;

   public double i() {
      return this.g;
   }

   public int B() {
      return this.E;
   }

   public gh.s o(gh.d var1, x.w var2, int var3, boolean var4, gh.r var5) {
      this.E = 0;
      float var6 = com.larpclient.x.N(var2.w, var2.M, var5.a, var5.O);
      float var7 = var2.A + Mth.wrapDegrees(var6 - var2.A);
      this.g = this.g(var1, var2.w, var2.M, var6, var5) + var5.E + 4.0;
      gh.s var8 = this.A(var1, var2, var3, var4, var5, var7, X, false, null);
      if (I(var8, var5, var2)) {
         var8 = this.A(var1, var2, var3, var4, var5, var7, z, true, var8);
      }

      return var8;
   }

   private gh.s A(gh.d var1, x.w var2, int var3, boolean var4, gh.r var5, float var6, float[] var7, boolean var8, gh.s var9) {
      int var10 = var4 && var5.U && var3 <= 0 ? 1 : 0;

      for (float var14 : var7) {
         for (float var18 : K) {
            for (int var19 = 0; var19 < (var10 != 0 ? 2 : 1); var19++) {
               gh.s var20 = this.D(var1, var2, var3, var19 == 1, var5, var6 + var14, var18, var8);
               double var21 = var20.cost() + Math.abs(var14) * 0.08;
               if (var9 == null || var21 < var9.cost()) {
                  var9 = new gh.s(
                     var20.yaw(), var20.pitch(), var20.rocket(), var21, var20.hitsGround(), var20.minClearance(), var20.endY(), var20.endSpeed(), var8
                  );
               }
            }
         }
      }

      return var9;
   }

   static boolean I(gh.s var0, gh.r var1, x.w var2) {
      double var3 = var1.y(var1.e(var2.w, var2.M));
      return var0.hitsGround() || var0.minClearance() < var3 * 0.6 || var0.cost() > 400.0 || var0.endSpeed() < 0.45 && var0.minClearance() < 40.0;
   }

   gh.s D(gh.d var1, x.w var2, int var3, boolean var4, gh.r var5, float var6, float var7, boolean var8) {
      x.w var9 = this.I.y(var2);
      int var10 = var4 ? com.larpclient.x.i(var5.w) : Math.max(0, var3);
      double var11 = var5.e(var9.w, var9.M);
      double var13 = Double.MAX_VALUE;
      double var15 = 0.0;
      double var17 = 0.0;
      double var19 = 0.0;
      double var21 = 0.0;
      double var23 = 0.0;
      double var25 = 0.0;
      int var27 = -1;

      for (int var28 = 0; var28 < 60; var28++) {
         var9.A = com.larpclient.x.t(var9.A, var6, 12.0F);
         var9.z = Mth.clamp(com.larpclient.x.t(var9.z, var7, 9.0F), -90.0F, 90.0F);
         com.larpclient.x.P(var9, var5.N);
         com.larpclient.x.s(var9);
         if (var10 > 0) {
            com.larpclient.x.C(var9);
            var10--;
         }

         double var29 = var5.e(var9.w, var9.M);
         double var31 = var9.L - this.d(var1, var9.w, var9.M, var5);
         if (var31 < 0.05) {
            var27 = var28;
            break;
         }

         if (var31 < 24.0 && var28 > 0 && var9.L - var9.p * 0.5 - this.d(var1, var9.w - var9.K * 0.5, var9.M - var9.S * 0.5, var5) < 0.05) {
            var27 = var28;
            break;
         }

         var13 = Math.min(var13, var31);
         double var33 = Math.max(Math.max(var5.y(var29), var9.H() * 5.0), -var9.p * 25.0);
         if (var31 < var33) {
            double var35 = var33 - var31;
            var15 += var35 * var35 * 0.5 + var35 * 2.0;
         }

         if (var9.p < -0.5) {
            var21 += (-var9.p - 0.5) * 25.0;
         }

         if (var31 < 8.0 && var9.p < -0.5) {
            var23 += (-var9.p - 0.5) * 30.0;
         }

         if (var9.L < var5.i) {
            var25 += (var5.i - var9.L) * 4.0;
         }

         double var43 = var28 < 20 ? 0.4 : 1.0;
         var17 += Math.abs(this.k(var5, var9.L, var29)) * var43;
         var19 += var43;
      }

      double var42 = var5.e(var9.w, var9.M);
      double var30 = var9.H();
      double var32 = var11 - var42;
      double var34;
      if (var27 >= 0) {
         var34 = 1000000 + (60 - var27) * 1000;
      } else {
         double var36 = this.k(var5, var9.L, var42);
         var34 = (var36 < 0.0 ? -var36 * (var5.W() ? 4.0 : 2.2) : var36 * (var5.W() ? 1.6 : 0.6))
            + var17 / Math.max(1.0E-6, var19) * 0.4
            - var32 * (var5.W() ? 0.35 : 1.0)
            - (var5.W() ? 0.0 : (this.m(var5, var9.L, var42) - this.m(var5, var2.L, var11) + O(var30) - O(var2.H())) * 9.0)
            + var15 / 60.0 * 10.0
            + var25 / 60.0
            + var23
            + var21 / 60.0 * 10.0
            + (var4 ? var5.Q : 0.0);
         if (var30 < 0.55) {
            var34 += (0.55 - var30) * 60.0;
         }

         if (var5.W() && var42 < 120.0 && var30 > var5.b) {
            var34 += (var30 - var5.b) * 40.0;
         }

         double var38 = Math.atan2(-var9.K, var9.S);
         double var40 = Math.atan2(-(var5.a - var9.w), var5.O - var9.M);
         if (var42 > 8.0) {
            var34 += Math.abs(Mth.wrapDegrees(Math.toDegrees(var38 - var40))) * 0.06;
         }
      }

      return new gh.s(var6, var7, var4, var34, var27 >= 0, var27 >= 0 ? -1.0 : var13, var9.L, var30, var8);
   }

   double n(gh.r var1, double var2) {
      double var4 = var1.X(var2);
      double var6 = var4 - (var1.W() ? 6 : 45);
      if (!var1.W() || var2 >= 67.5) {
         var6 = Math.max(var6, Math.min(this.g, var4 + 400.0));
      }

      return var6;
   }

   double J(gh.r var1, double var2) {
      return Math.max(var1.X(var2) + (var1.W() ? 6 : 15), this.n(var1, var2) + 10.0);
   }

   double k(gh.r var1, double var2, double var4) {
      double var6 = this.n(var1, var4);
      double var8 = this.J(var1, var4);
      return var2 < var6 ? var2 - var6 : (var2 > var8 ? var2 - var8 : 0.0);
   }

   private double m(gh.r var1, double var2, double var4) {
      return Math.min(var2, this.J(var1, var4));
   }

   static double O(double var0) {
      return var0 * var0 / 0.16 * 0.8;
   }

   int d(gh.d var1, double var2, double var4, gh.r var6) {
      int var7 = Integer.MIN_VALUE;
      byte var8 = 0;

      for (int var9 = Mth.floor(var2 - 0.3); var9 <= Mth.floor(var2 + 0.3); var9++) {
         for (int var10 = Mth.floor(var4 - 0.3); var10 <= Mth.floor(var4 + 0.3); var10++) {
            int var11 = var1.Q(var9, var10);
            this.E++;
            if (var11 == Integer.MIN_VALUE) {
               var8 = 1;
            } else {
               var7 = Math.max(var7, var11);
            }
         }
      }

      if (var8 != 0) {
         var7 = Math.max(var7, (int)Math.ceil(var6.Y - var6.E));
      }

      return var7 == Integer.MIN_VALUE ? -100000 : var7;
   }

   double g(gh.d var1, double var2, double var4, float var6, gh.r var7) {
      double var8 = var7.e(var2, var4);
      double var10 = Math.min(320.0, var7.W() ? Math.max(0.0, var8 - 27.0) : var8 + 24.0);
      double var12 = com.larpclient.x.g(var6, 0.0F);
      double var14 = com.larpclient.x.L(var6, 0.0F);
      double var16 = Double.NEGATIVE_INFINITY;

      for (double var18 = 0.0; var18 <= var10; var18 += 4.0) {
         for (byte var20 = -8; var20 <= 8; var20 += 8) {
            int var21 = var1.Q(Mth.floor(var2 + var12 * var18 - var14 * var20), Mth.floor(var4 + var14 * var18 + var12 * var20));
            this.E++;
            if (var21 == Integer.MIN_VALUE) {
               if (var18 <= 200.0) {
                  var16 = Math.max(var16, var7.Y - var7.E);
               }
            } else {
               var16 = Math.max(var16, var21);
            }
         }
      }

      return var16 == Double.NEGATIVE_INFINITY ? -100000.0 : var16;
   }

   public gh.y Z(gh.d var1, gh.n var2, x.w var3, double var4, double var6, double var8, int var10, double var11) {
      double var13 = Math.hypot(var4 - var3.w, var8 - var3.M);
      float var15 = var13 > 2.5 ? var3.A + Mth.wrapDegrees(com.larpclient.x.N(var3.w, var3.M, var4, var8) - var3.A) : var3.A;
      double var16 = Double.MAX_VALUE;
      float var18 = var15;
      float var19 = 0.0F;
      byte var20 = 0;

      for (float var24 : P) {
         for (float var28 : w) {
            double[] var29 = this.d(var1, var2, var3, var15 + var24, var28, -1, var28, var4, var6, var8, var10, var11);
            double var30 = var29[0] + Math.abs(var24) * 0.15;
            if (var30 < var16) {
               var16 = var30;
               var18 = var15 + var24;
               var19 = var28;
               var20 = (byte)(var29[1] > 0.0 ? 1 : 0);
            }
         }
      }

      if (var13 > 8.0 && (var20 == 0 || var16 >= 40.0)) {
         for (float var43 : X) {
            for (float var47 : r) {
               for (int var32 : R) {
                  for (float var36 : l) {
                     double[] var37 = this.d(var1, var2, var3, var15 + var43 * 1.5F, var47, var32, var36, var4, var6, var8, var10, var11);
                     double var38 = var37[0] + Math.abs(var43 * 1.5F) * 0.15 + 2.0;
                     if (var38 < var16) {
                        var16 = var38;
                        var18 = var15 + var43 * 1.5F;
                        var19 = var47;
                        var20 = (byte)(var37[1] > 0.0 ? 1 : 0);
                     }
                  }
               }
            }
         }
      }

      return new gh.y(var18, var19, var16, (boolean)var20);
   }

   double[] d(gh.d var1, gh.n var2, x.w var3, float var4, float var5, int var6, float var7, double var8, double var10, double var12, int var14, double var15) {
      x.w var17 = this.I.y(var3);
      int var18 = Math.max(0, var14);
      double var19 = 0.0;
      double var21 = var8 - var17.w;
      double var23 = var12 - var17.M;
      double var25 = Math.hypot(var21, var23);
      double var27 = var25 > 0.001 ? var21 / var25 : 0.0;
      double var29 = var25 > 0.001 ? var23 / var25 : 0.0;

      for (int var31 = 0; var31 < 90; var31++) {
         var17.A = com.larpclient.x.t(var17.A, var4, 20.0F);
         var17.z = Mth.clamp(com.larpclient.x.t(var17.z, var6 >= 0 && var31 >= var6 ? var7 : var5, 13.0F), -90.0F, 90.0F);
         double var32 = var17.H();
         com.larpclient.x.P(var17, var15);
         double var34 = var17.w + var17.K;
         double var36 = var17.M + var17.S;
         double var38 = var17.L + var17.p;
         int var40 = this.o(var1, var34, var36);
         if (var40 != Integer.MIN_VALUE && var40 > var17.L + 0.6) {
            double var41 = var32 * 10.0 - 3.0;
            if (var41 > 0.0) {
               return new double[]{1000.0 + var41 * 60.0, 0.0};
            }

            var19 += 12.0;
            var17.K = var17.S = 0.0;
            var34 = var17.w;
            var36 = var17.M;
         }

         int var54 = this.o(var1, var34, var36);
         if (var54 != Integer.MIN_VALUE && var38 <= var54) {
            double var42 = var17.p;
            var17.w = var34;
            var17.M = var36;
            var17.L = var54;
            if (var42 > 0.0) {
               return new double[]{600 + var31, 0.0};
            }

            int var44 = Mth.floor(var17.w);
            int var45 = Mth.floor(var17.M);
            double var46 = Math.hypot(var17.w - var8, var17.M - var12);
            if (var2 != null && !var2.Y(var44, var54, var45)) {
               return new double[]{850.0 + var46, 0.0};
            }

            int var48 = Math.abs(var17.L - var10) < 1.2 && var46 < 7.0 ? 1 : 0;
            double var49 = this.N(var1, var17.w, var17.L, var17.M, var17.K, var17.S);
            double var51 = (var48 != 0 ? 0 : 150) + var49 + var46 * 0.6 + var17.H() * 7.0 + Math.max(0.0, -var42 - 0.5) * 45.0 + var19;
            return new double[]{var51, var48 != 0 && var49 == 0.0 ? 1.0 : 0.0};
         }

         var17.w = var34;
         var17.L = var38;
         var17.M = var36;
         if (var18 > 0) {
            com.larpclient.x.C(var17);
            var18--;
         }

         if (var17.L < var10 - 6.0) {
            return new double[]{800.0, 0.0};
         }
      }

      double var53 = Math.hypot(var17.w - var8, var17.M - var12);
      double var33 = var17.L - var10;
      double var35 = Math.max(0.0, -((var8 - var17.w) * var27 + (var12 - var17.M) * var29));
      return new double[]{
         Math.min(280.0, 120.0 + Math.abs(var33 - var53 * 0.22) * 4.0 + var53 * 0.4 + Math.max(0.0, var17.H() - 1.15) * 25.0 + var35 * 1.5 + var19), 0.0
      };
   }

   private int o(gh.d var1, double var2, double var4) {
      this.E++;
      return var1.Q(Mth.floor(var2), Mth.floor(var4));
   }

   double N(gh.d var1, double var2, double var4, double var6, double var8, double var10) {
      for (int var12 = 0; var12 < 14; var12++) {
         var2 += var8;
         var6 += var10;
         int var13 = this.o(var1, var2, var6);
         if (var13 == Integer.MIN_VALUE || var4 - var13 > 1.2) {
            return 400.0;
         }

         if (var13 > var4 + 0.6) {
            return 0.0;
         }

         var8 *= 0.546;
         var10 *= 0.546;
         if (Math.hypot(var8, var10) < 0.02) {
            return 0.0;
         }
      }

      return 0.0;
   }

   public interface d {
      int X = Integer.MIN_VALUE;

      int Q(int var1, int var2);
   }

   public interface n {
      boolean Y(int var1, int var2, int var3);
   }

   public static final class r {
      public double a;
      public double O;
      public double j = 290.0;
      public double q = Double.NaN;
      public double E = 14.0;
      public double Y = 262.0;
      public double i = Double.NEGATIVE_INFINITY;
      public double N = 0.08;
      public boolean U = (boolean)1;
      public double Q = 70.0;
      public int w = 3;
      public double b = 1.45;

      public boolean W() {
         return !Double.isNaN(this.q);
      }

      public double X(double var1) {
         return !this.W() ? this.j : Math.min(this.j, this.q + 14.0 + Math.max(0.0, var1 - 45.0) * 0.095);
      }

      public double y(double var1) {
         return !this.W() ? this.E : Math.max(3.0, Math.min(this.E, (var1 - 45.0) * 0.25 + 4.0));
      }

      public double e(double var1, double var3) {
         return Math.hypot(this.a - var1, this.O - var3);
      }
   }

   public record s(float yaw, float pitch, boolean rocket, double cost, boolean hitsGround, double minClearance, double endY, double endSpeed, boolean wide) {
   }

   public record y(float yaw, float pitch, double cost, boolean onTarget) {
   }
}
