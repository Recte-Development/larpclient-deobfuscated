package com.larpclient;

import net.minecraft.util.Mth;

public final class dn {
   public static final double D = 45.0;
   public static final double n = 14.0;
   public static final double E = 0.095;
   static final int V = 60;
   static final int F = 90;
   static final float u = 12.0F;
   static final float d = 9.0F;
   private static final float[] y = new float[]{0.0F, -12.0F, 12.0F};
   private static final float[] S = new float[]{-30.0F, 30.0F, -55.0F, 55.0F, -90.0F, 90.0F, -140.0F, 140.0F};
   private static final float[] c = new float[]{-62.0F, -46.0F, -33.0F, -23.0F, -15.0F, -9.0F, -4.0F, 1.0F, 6.0F, 12.0F, 19.0F, 27.0F, 36.0F};
   private static final float[] C = new float[]{0.0F, -18.0F, 18.0F, -36.0F, 36.0F, -60.0F, 60.0F, -90.0F, 90.0F};
   private static final float[] B = new float[]{-28.0F, -18.0F, -10.0F, -4.0F, 0.0F, 4.0F, 8.0F, 12.0F, 16.0F, 21.0F, 27.0F, 34.0F};
   private static final float[] k = new float[]{-4.0F, 0.0F, 4.0F, 8.0F, 12.0F, 16.0F};
   private static final float[] U = new float[]{-28.0F, -18.0F};
   private static final int[] g = new int[]{10, 25, 45};
   private final vu.x b = new vu.x();
   private double z = Double.NEGATIVE_INFINITY;
   private int K;

   public double Z() {
      return this.z;
   }

   public int X() {
      return this.K;
   }

   public dn.g B(dn.b var1, vu.x var2, int var3, boolean var4, dn.o var5) {
      this.K = 0;
      float var6 = vu.v(var2.K, var2.R, var5.q, var5.P);
      float var7 = var2.A + Mth.wrapDegrees(var6 - var2.A);
      this.z = this.g(var1, var2.K, var2.R, var6, var5) + var5.g + 4.0;
      dn.g var8 = this.P(var1, var2, var3, var4, var5, var7, y, false, null);
      if (k(var8, var5, var2)) {
         var8 = this.P(var1, var2, var3, var4, var5, var7, S, true, var8);
      }

      return var8;
   }

   private dn.g P(dn.b var1, vu.x var2, int var3, boolean var4, dn.o var5, float var6, float[] var7, boolean var8, dn.g var9) {
      int var10 = var4 && var5.T && var3 <= 0 ? 1 : 0;

      for (float var14 : var7) {
         for (float var18 : c) {
            for (int var19 = 0; var19 < (var10 != 0 ? 2 : 1); var19++) {
               dn.g var20 = this.L(var1, var2, var3, var19 == 1, var5, var6 + var14, var18, var8);
               double var21 = var20.cost() + Math.abs(var14) * 0.08;
               if (var9 == null || var21 < var9.cost()) {
                  var9 = new dn.g(
                     var20.yaw(), var20.pitch(), var20.rocket(), var21, var20.hitsGround(), var20.minClearance(), var20.endY(), var20.endSpeed(), var8
                  );
               }
            }
         }
      }

      return var9;
   }

   static boolean k(dn.g var0, dn.o var1, vu.x var2) {
      double var3 = var1.b(var1.n(var2.K, var2.R));
      return var0.hitsGround() || var0.minClearance() < var3 * 0.6 || var0.cost() > 400.0 || var0.endSpeed() < 0.45 && var0.minClearance() < 40.0;
   }

   dn.g L(dn.b var1, vu.x var2, int var3, boolean var4, dn.o var5, float var6, float var7, boolean var8) {
      vu.x var9 = this.b.i(var2);
      int var10 = var4 ? vu.r(var5.N) : Math.max(0, var3);
      double var11 = var5.n(var9.K, var9.R);
      double var13 = Double.MAX_VALUE;
      double var15 = 0.0;
      double var17 = 0.0;
      double var19 = 0.0;
      double var21 = 0.0;
      double var23 = 0.0;
      double var25 = 0.0;
      int var27 = -1;

      for (int var28 = 0; var28 < 60; var28++) {
         var9.A = vu.p(var9.A, var6, 12.0F);
         var9.G = Mth.clamp(vu.p(var9.G, var7, 9.0F), -90.0F, 90.0F);
         vu.n(var9, var5.V);
         vu.a(var9);
         if (var10 > 0) {
            vu.M(var9);
            var10--;
         }

         double var29 = var5.n(var9.K, var9.R);
         double var31 = var9.J - this.h(var1, var9.K, var9.R, var5);
         if (var31 < 0.05) {
            var27 = var28;
            break;
         }

         if (var31 < 24.0 && var28 > 0 && var9.J - var9.g * 0.5 - this.h(var1, var9.K - var9.S * 0.5, var9.R - var9.m * 0.5, var5) < 0.05) {
            var27 = var28;
            break;
         }

         var13 = Math.min(var13, var31);
         double var33 = Math.max(Math.max(var5.b(var29), var9.W() * 5.0), -var9.g * 25.0);
         if (var31 < var33) {
            double var35 = var33 - var31;
            var15 += var35 * var35 * 0.5 + var35 * 2.0;
         }

         if (var9.g < -0.5) {
            var21 += (-var9.g - 0.5) * 25.0;
         }

         if (var31 < 8.0 && var9.g < -0.5) {
            var23 += (-var9.g - 0.5) * 30.0;
         }

         if (var9.J < var5.e) {
            var25 += (var5.e - var9.J) * 4.0;
         }

         double var43 = var28 < 20 ? 0.4 : 1.0;
         var17 += Math.abs(this.I(var5, var9.J, var29)) * var43;
         var19 += var43;
      }

      double var42 = var5.n(var9.K, var9.R);
      double var30 = var9.W();
      double var32 = var11 - var42;
      double var34;
      if (var27 >= 0) {
         var34 = 1000000 + (60 - var27) * 1000;
      } else {
         double var36 = this.I(var5, var9.J, var42);
         var34 = (var36 < 0.0 ? -var36 * (var5.p() ? 4.0 : 2.2) : var36 * (var5.p() ? 1.6 : 0.6))
            + var17 / Math.max(1.0E-6, var19) * 0.4
            - var32 * (var5.p() ? 0.35 : 1.0)
            - (var5.p() ? 0.0 : (this.E(var5, var9.J, var42) - this.E(var5, var2.J, var11) + V(var30) - V(var2.W())) * 9.0)
            + var15 / 60.0 * 10.0
            + var25 / 60.0
            + var23
            + var21 / 60.0 * 10.0
            + (var4 ? var5.O : 0.0);
         if (var30 < 0.55) {
            var34 += (0.55 - var30) * 60.0;
         }

         if (var5.p() && var42 < 120.0 && var30 > var5.w) {
            var34 += (var30 - var5.w) * 40.0;
         }

         double var38 = Math.atan2(-var9.S, var9.m);
         double var40 = Math.atan2(-(var5.q - var9.K), var5.P - var9.R);
         if (var42 > 8.0) {
            var34 += Math.abs(Mth.wrapDegrees(Math.toDegrees(var38 - var40))) * 0.06;
         }
      }

      return new dn.g(var6, var7, var4, var34, var27 >= 0, var27 >= 0 ? -1.0 : var13, var9.J, var30, var8);
   }

   double Y(dn.o var1, double var2) {
      double var4 = var1.O(var2);
      double var6 = var4 - (var1.p() ? 6 : 45);
      if (!var1.p() || var2 >= 67.5) {
         var6 = Math.max(var6, Math.min(this.z, var4 + 400.0));
      }

      return var6;
   }

   double a(dn.o var1, double var2) {
      return Math.max(var1.O(var2) + (var1.p() ? 6 : 15), this.Y(var1, var2) + 10.0);
   }

   double I(dn.o var1, double var2, double var4) {
      double var6 = this.Y(var1, var4);
      double var8 = this.a(var1, var4);
      return var2 < var6 ? var2 - var6 : (var2 > var8 ? var2 - var8 : 0.0);
   }

   private double E(dn.o var1, double var2, double var4) {
      return Math.min(var2, this.a(var1, var4));
   }

   static double V(double var0) {
      return var0 * var0 / 0.16 * 0.8;
   }

   int h(dn.b var1, double var2, double var4, dn.o var6) {
      int var7 = Integer.MIN_VALUE;
      byte var8 = 0;

      for (int var9 = Mth.floor(var2 - 0.3); var9 <= Mth.floor(var2 + 0.3); var9++) {
         for (int var10 = Mth.floor(var4 - 0.3); var10 <= Mth.floor(var4 + 0.3); var10++) {
            int var11 = var1.w(var9, var10);
            this.K++;
            if (var11 == Integer.MIN_VALUE) {
               var8 = 1;
            } else {
               var7 = Math.max(var7, var11);
            }
         }
      }

      if (var8 != 0) {
         var7 = Math.max(var7, (int)Math.ceil(var6.X - var6.g));
      }

      return var7 == Integer.MIN_VALUE ? -100000 : var7;
   }

   double g(dn.b var1, double var2, double var4, float var6, dn.o var7) {
      double var8 = var7.n(var2, var4);
      double var10 = Math.min(320.0, var7.p() ? Math.max(0.0, var8 - 27.0) : var8 + 24.0);
      double var12 = vu.y(var6, 0.0F);
      double var14 = vu.Z(var6, 0.0F);
      double var16 = Double.NEGATIVE_INFINITY;

      for (double var18 = 0.0; var18 <= var10; var18 += 4.0) {
         for (byte var20 = -8; var20 <= 8; var20 += 8) {
            int var21 = var1.w(Mth.floor(var2 + var12 * var18 - var14 * var20), Mth.floor(var4 + var14 * var18 + var12 * var20));
            this.K++;
            if (var21 == Integer.MIN_VALUE) {
               if (var18 <= 200.0) {
                  var16 = Math.max(var16, var7.X - var7.g);
               }
            } else {
               var16 = Math.max(var16, var21);
            }
         }
      }

      return var16 == Double.NEGATIVE_INFINITY ? -100000.0 : var16;
   }

   public dn.l w(dn.b var1, dn.u var2, vu.x var3, double var4, double var6, double var8, int var10, double var11) {
      double var13 = Math.hypot(var4 - var3.K, var8 - var3.R);
      float var15 = var13 > 2.5 ? var3.A + Mth.wrapDegrees(vu.v(var3.K, var3.R, var4, var8) - var3.A) : var3.A;
      double var16 = Double.MAX_VALUE;
      float var18 = var15;
      float var19 = 0.0F;
      byte var20 = 0;

      for (float var24 : C) {
         for (float var28 : B) {
            double[] var29 = this.X(var1, var2, var3, var15 + var24, var28, -1, var28, var4, var6, var8, var10, var11);
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
         for (float var43 : y) {
            for (float var47 : k) {
               for (int var32 : g) {
                  for (float var36 : U) {
                     double[] var37 = this.X(var1, var2, var3, var15 + var43 * 1.5F, var47, var32, var36, var4, var6, var8, var10, var11);
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

      return new dn.l(var18, var19, var16, (boolean)var20);
   }

   double[] X(dn.b var1, dn.u var2, vu.x var3, float var4, float var5, int var6, float var7, double var8, double var10, double var12, int var14, double var15) {
      vu.x var17 = this.b.i(var3);
      int var18 = Math.max(0, var14);
      double var19 = 0.0;
      double var21 = var8 - var17.K;
      double var23 = var12 - var17.R;
      double var25 = Math.hypot(var21, var23);
      double var27 = var25 > 0.001 ? var21 / var25 : 0.0;
      double var29 = var25 > 0.001 ? var23 / var25 : 0.0;

      for (int var31 = 0; var31 < 90; var31++) {
         var17.A = vu.p(var17.A, var4, 20.0F);
         var17.G = Mth.clamp(vu.p(var17.G, var6 >= 0 && var31 >= var6 ? var7 : var5, 13.0F), -90.0F, 90.0F);
         double var32 = var17.W();
         vu.n(var17, var15);
         double var34 = var17.K + var17.S;
         double var36 = var17.R + var17.m;
         double var38 = var17.J + var17.g;
         int var40 = this.S(var1, var34, var36);
         if (var40 != Integer.MIN_VALUE && var40 > var17.J + 0.6) {
            double var41 = var32 * 10.0 - 3.0;
            if (var41 > 0.0) {
               return new double[]{1000.0 + var41 * 60.0, 0.0};
            }

            var19 += 12.0;
            var17.S = var17.m = 0.0;
            var34 = var17.K;
            var36 = var17.R;
         }

         int var54 = this.S(var1, var34, var36);
         if (var54 != Integer.MIN_VALUE && var38 <= var54) {
            double var42 = var17.g;
            var17.K = var34;
            var17.R = var36;
            var17.J = var54;
            if (var42 > 0.0) {
               return new double[]{600 + var31, 0.0};
            }

            int var44 = Mth.floor(var17.K);
            int var45 = Mth.floor(var17.R);
            double var46 = Math.hypot(var17.K - var8, var17.R - var12);
            if (var2 != null && !var2.B(var44, var54, var45)) {
               return new double[]{850.0 + var46, 0.0};
            }

            int var48 = Math.abs(var17.J - var10) < 1.2 && var46 < 7.0 ? 1 : 0;
            double var49 = this.l(var1, var17.K, var17.J, var17.R, var17.S, var17.m);
            double var51 = (var48 != 0 ? 0 : 150) + var49 + var46 * 0.6 + var17.W() * 7.0 + Math.max(0.0, -var42 - 0.5) * 45.0 + var19;
            return new double[]{var51, var48 != 0 && var49 == 0.0 ? 1.0 : 0.0};
         }

         var17.K = var34;
         var17.J = var38;
         var17.R = var36;
         if (var18 > 0) {
            vu.M(var17);
            var18--;
         }

         if (var17.J < var10 - 6.0) {
            return new double[]{800.0, 0.0};
         }
      }

      double var53 = Math.hypot(var17.K - var8, var17.R - var12);
      double var33 = var17.J - var10;
      double var35 = Math.max(0.0, -((var8 - var17.K) * var27 + (var12 - var17.R) * var29));
      return new double[]{
         Math.min(280.0, 120.0 + Math.abs(var33 - var53 * 0.22) * 4.0 + var53 * 0.4 + Math.max(0.0, var17.W() - 1.15) * 25.0 + var35 * 1.5 + var19), 0.0
      };
   }

   private int S(dn.b var1, double var2, double var4) {
      this.K++;
      return var1.w(Mth.floor(var2), Mth.floor(var4));
   }

   double l(dn.b var1, double var2, double var4, double var6, double var8, double var10) {
      for (int var12 = 0; var12 < 14; var12++) {
         var2 += var8;
         var6 += var10;
         int var13 = this.S(var1, var2, var6);
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

   public interface b {
      int u = Integer.MIN_VALUE;

      int w(int var1, int var2);
   }

   public record g(float yaw, float pitch, boolean rocket, double cost, boolean hitsGround, double minClearance, double endY, double endSpeed, boolean wide) {
   }

   public record l(float yaw, float pitch, double cost, boolean onTarget) {
   }

   public static final class o {
      public double q;
      public double P;
      public double y = 290.0;
      public double u = Double.NaN;
      public double g = 14.0;
      public double X = 262.0;
      public double e = Double.NEGATIVE_INFINITY;
      public double V = 0.08;
      public boolean T = (boolean)1;
      public double O = 70.0;
      public int N = 3;
      public double w = 1.45;

      public boolean p() {
         return !Double.isNaN(this.u);
      }

      public double O(double var1) {
         return !this.p() ? this.y : Math.min(this.y, this.u + 14.0 + Math.max(0.0, var1 - 45.0) * 0.095);
      }

      public double b(double var1) {
         return !this.p() ? this.g : Math.max(3.0, Math.min(this.g, (var1 - 45.0) * 0.25 + 4.0));
      }

      public double n(double var1, double var3) {
         return Math.hypot(this.q - var1, this.P - var3);
      }
   }

   public interface u {
      boolean B(int var1, int var2, int var3);
   }
}
