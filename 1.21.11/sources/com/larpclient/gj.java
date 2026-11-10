package com.larpclient;

import net.minecraft.class_3532;

public final class gj {
   public static final double P = 45.0;
   public static final double v = 14.0;
   public static final double f = 0.095;
   static final int J = 60;
   static final int h = 90;
   static final float m = 12.0F;
   static final float G = 9.0F;
   private static final float[] u = new float[]{0.0F, -12.0F, 12.0F};
   private static final float[] U = new float[]{-30.0F, 30.0F, -55.0F, 55.0F, -90.0F, 90.0F, -140.0F, 140.0F};
   private static final float[] V = new float[]{-62.0F, -46.0F, -33.0F, -23.0F, -15.0F, -9.0F, -4.0F, 1.0F, 6.0F, 12.0F, 19.0F, 27.0F, 36.0F};
   private static final float[] E = new float[]{0.0F, -18.0F, 18.0F, -36.0F, 36.0F, -60.0F, 60.0F, -90.0F, 90.0F};
   private static final float[] o = new float[]{-28.0F, -18.0F, -10.0F, -4.0F, 0.0F, 4.0F, 8.0F, 12.0F, 16.0F, 21.0F, 27.0F, 34.0F};
   private static final float[] Z = new float[]{-4.0F, 0.0F, 4.0F, 8.0F, 12.0F, 16.0F};
   private static final float[] D = new float[]{-28.0F, -18.0F};
   private static final int[] z = new int[]{10, 25, 45};
   private final wf.w l = new wf.w();
   private double t = Double.NEGATIVE_INFINITY;
   private int a;

   public double U() {
      return this.t;
   }

   public int d() {
      return this.a;
   }

   public gj.f S(gj.a var1, wf.w var2, int var3, boolean var4, gj.d var5) {
      this.a = 0;
      float var6 = wf.F(var2.P, var2.b, var5.h, var5.A);
      float var7 = var2.j + class_3532.method_15393(var6 - var2.j);
      this.t = this.N(var1, var2.P, var2.b, var6, var5) + var5.j + 4.0;
      gj.f var8 = this.x(var1, var2, var3, var4, var5, var7, u, false, null);
      if (q(var8, var5, var2)) {
         var8 = this.x(var1, var2, var3, var4, var5, var7, U, true, var8);
      }

      return var8;
   }

   private gj.f x(gj.a var1, wf.w var2, int var3, boolean var4, gj.d var5, float var6, float[] var7, boolean var8, gj.f var9) {
      int var10 = var4 && var5.X && var3 <= 0 ? 1 : 0;

      for (float var14 : var7) {
         for (float var18 : V) {
            for (int var19 = 0; var19 < (var10 != 0 ? 2 : 1); var19++) {
               gj.f var20 = this.H(var1, var2, var3, var19 == 1, var5, var6 + var14, var18, var8);
               double var21 = var20.cost() + Math.abs(var14) * 0.08;
               if (var9 == null || var21 < var9.cost()) {
                  var9 = new gj.f(
                     var20.yaw(), var20.pitch(), var20.rocket(), var21, var20.hitsGround(), var20.minClearance(), var20.endY(), var20.endSpeed(), var8
                  );
               }
            }
         }
      }

      return var9;
   }

   static boolean q(gj.f var0, gj.d var1, wf.w var2) {
      double var3 = var1.A(var1.u(var2.P, var2.b));
      return var0.hitsGround() || var0.minClearance() < var3 * 0.6 || var0.cost() > 400.0 || var0.endSpeed() < 0.45 && var0.minClearance() < 40.0;
   }

   gj.f H(gj.a var1, wf.w var2, int var3, boolean var4, gj.d var5, float var6, float var7, boolean var8) {
      wf.w var9 = this.l.P(var2);
      int var10 = var4 ? wf.A(var5.C) : Math.max(0, var3);
      double var11 = var5.u(var9.P, var9.b);
      double var13 = Double.MAX_VALUE;
      double var15 = 0.0;
      double var17 = 0.0;
      double var19 = 0.0;
      double var21 = 0.0;
      double var23 = 0.0;
      double var25 = 0.0;
      int var27 = -1;

      for (int var28 = 0; var28 < 60; var28++) {
         var9.j = wf.a(var9.j, var6, 12.0F);
         var9.i = class_3532.method_15363(wf.a(var9.i, var7, 9.0F), -90.0F, 90.0F);
         wf.L(var9, var5.W);
         wf.s(var9);
         if (var10 > 0) {
            wf.d(var9);
            var10--;
         }

         double var29 = var5.u(var9.P, var9.b);
         double var31 = var9.G - this.e(var1, var9.P, var9.b, var5);
         if (var31 < 0.05) {
            var27 = var28;
            break;
         }

         if (var31 < 24.0 && var28 > 0 && var9.G - var9.A * 0.5 - this.e(var1, var9.P - var9.o * 0.5, var9.b - var9.M * 0.5, var5) < 0.05) {
            var27 = var28;
            break;
         }

         var13 = Math.min(var13, var31);
         double var33 = Math.max(Math.max(var5.A(var29), var9.W() * 5.0), -var9.A * 25.0);
         if (var31 < var33) {
            double var35 = var33 - var31;
            var15 += var35 * var35 * 0.5 + var35 * 2.0;
         }

         if (var9.A < -0.5) {
            var21 += (-var9.A - 0.5) * 25.0;
         }

         if (var31 < 8.0 && var9.A < -0.5) {
            var23 += (-var9.A - 0.5) * 30.0;
         }

         if (var9.G < var5.Y) {
            var25 += (var5.Y - var9.G) * 4.0;
         }

         double var43 = var28 < 20 ? 0.4 : 1.0;
         var17 += Math.abs(this.e(var5, var9.G, var29)) * var43;
         var19 += var43;
      }

      double var42 = var5.u(var9.P, var9.b);
      double var30 = var9.W();
      double var32 = var11 - var42;
      double var34;
      if (var27 >= 0) {
         var34 = 1000000 + (60 - var27) * 1000;
      } else {
         double var36 = this.e(var5, var9.G, var42);
         var34 = (var36 < 0.0 ? -var36 * (var5.O() ? 4.0 : 2.2) : var36 * (var5.O() ? 1.6 : 0.6))
            + var17 / Math.max(1.0E-6, var19) * 0.4
            - var32 * (var5.O() ? 0.35 : 1.0)
            - (var5.O() ? 0.0 : (this.A(var5, var9.G, var42) - this.A(var5, var2.G, var11) + v(var30) - v(var2.W())) * 9.0)
            + var15 / 60.0 * 10.0
            + var25 / 60.0
            + var23
            + var21 / 60.0 * 10.0
            + (var4 ? var5.u : 0.0);
         if (var30 < 0.55) {
            var34 += (0.55 - var30) * 60.0;
         }

         if (var5.O() && var42 < 120.0 && var30 > var5.P) {
            var34 += (var30 - var5.P) * 40.0;
         }

         double var38 = Math.atan2(-var9.o, var9.M);
         double var40 = Math.atan2(-(var5.h - var9.P), var5.A - var9.b);
         if (var42 > 8.0) {
            var34 += Math.abs(class_3532.method_15338(Math.toDegrees(var38 - var40))) * 0.06;
         }
      }

      return new gj.f(var6, var7, var4, var34, var27 >= 0, var27 >= 0 ? -1.0 : var13, var9.G, var30, var8);
   }

   double j(gj.d var1, double var2) {
      double var4 = var1.U(var2);
      double var6 = var4 - (var1.O() ? 6 : 45);
      if (!var1.O() || var2 >= 67.5) {
         var6 = Math.max(var6, Math.min(this.t, var4 + 400.0));
      }

      return var6;
   }

   double G(gj.d var1, double var2) {
      return Math.max(var1.U(var2) + (var1.O() ? 6 : 15), this.j(var1, var2) + 10.0);
   }

   double e(gj.d var1, double var2, double var4) {
      double var6 = this.j(var1, var4);
      double var8 = this.G(var1, var4);
      return var2 < var6 ? var2 - var6 : (var2 > var8 ? var2 - var8 : 0.0);
   }

   private double A(gj.d var1, double var2, double var4) {
      return Math.min(var2, this.G(var1, var4));
   }

   static double v(double var0) {
      return var0 * var0 / 0.16 * 0.8;
   }

   int e(gj.a var1, double var2, double var4, gj.d var6) {
      int var7 = Integer.MIN_VALUE;
      byte var8 = 0;

      for (int var9 = class_3532.method_15357(var2 - 0.3); var9 <= class_3532.method_15357(var2 + 0.3); var9++) {
         for (int var10 = class_3532.method_15357(var4 - 0.3); var10 <= class_3532.method_15357(var4 + 0.3); var10++) {
            int var11 = var1.E(var9, var10);
            this.a++;
            if (var11 == Integer.MIN_VALUE) {
               var8 = 1;
            } else {
               var7 = Math.max(var7, var11);
            }
         }
      }

      if (var8 != 0) {
         var7 = Math.max(var7, (int)Math.ceil(var6.S - var6.j));
      }

      return var7 == Integer.MIN_VALUE ? -100000 : var7;
   }

   double N(gj.a var1, double var2, double var4, float var6, gj.d var7) {
      double var8 = var7.u(var2, var4);
      double var10 = Math.min(320.0, var7.O() ? Math.max(0.0, var8 - 27.0) : var8 + 24.0);
      double var12 = wf.x(var6, 0.0F);
      double var14 = wf.Q(var6, 0.0F);
      double var16 = Double.NEGATIVE_INFINITY;

      for (double var18 = 0.0; var18 <= var10; var18 += 4.0) {
         for (byte var20 = -8; var20 <= 8; var20 += 8) {
            int var21 = var1.E(class_3532.method_15357(var2 + var12 * var18 - var14 * var20), class_3532.method_15357(var4 + var14 * var18 + var12 * var20));
            this.a++;
            if (var21 == Integer.MIN_VALUE) {
               if (var18 <= 200.0) {
                  var16 = Math.max(var16, var7.S - var7.j);
               }
            } else {
               var16 = Math.max(var16, var21);
            }
         }
      }

      return var16 == Double.NEGATIVE_INFINITY ? -100000.0 : var16;
   }

   public gj.o Q(gj.a var1, gj.k var2, wf.w var3, double var4, double var6, double var8, int var10, double var11) {
      double var13 = Math.hypot(var4 - var3.P, var8 - var3.b);
      float var15 = var13 > 2.5 ? var3.j + class_3532.method_15393(wf.F(var3.P, var3.b, var4, var8) - var3.j) : var3.j;
      double var16 = Double.MAX_VALUE;
      float var18 = var15;
      float var19 = 0.0F;
      byte var20 = 0;

      for (float var24 : E) {
         for (float var28 : o) {
            double[] var29 = this.x(var1, var2, var3, var15 + var24, var28, -1, var28, var4, var6, var8, var10, var11);
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
         for (float var43 : u) {
            for (float var47 : Z) {
               for (int var32 : z) {
                  for (float var36 : D) {
                     double[] var37 = this.x(var1, var2, var3, var15 + var43 * 1.5F, var47, var32, var36, var4, var6, var8, var10, var11);
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

      return new gj.o(var18, var19, var16, (boolean)var20);
   }

   double[] x(gj.a var1, gj.k var2, wf.w var3, float var4, float var5, int var6, float var7, double var8, double var10, double var12, int var14, double var15) {
      wf.w var17 = this.l.P(var3);
      int var18 = Math.max(0, var14);
      double var19 = 0.0;
      double var21 = var8 - var17.P;
      double var23 = var12 - var17.b;
      double var25 = Math.hypot(var21, var23);
      double var27 = var25 > 0.001 ? var21 / var25 : 0.0;
      double var29 = var25 > 0.001 ? var23 / var25 : 0.0;

      for (int var31 = 0; var31 < 90; var31++) {
         var17.j = wf.a(var17.j, var4, 20.0F);
         var17.i = class_3532.method_15363(wf.a(var17.i, var6 >= 0 && var31 >= var6 ? var7 : var5, 13.0F), -90.0F, 90.0F);
         double var32 = var17.W();
         wf.L(var17, var15);
         double var34 = var17.P + var17.o;
         double var36 = var17.b + var17.M;
         double var38 = var17.G + var17.A;
         int var40 = this.q(var1, var34, var36);
         if (var40 != Integer.MIN_VALUE && var40 > var17.G + 0.6) {
            double var41 = var32 * 10.0 - 3.0;
            if (var41 > 0.0) {
               return new double[]{1000.0 + var41 * 60.0, 0.0};
            }

            var19 += 12.0;
            var17.o = var17.M = 0.0;
            var34 = var17.P;
            var36 = var17.b;
         }

         int var54 = this.q(var1, var34, var36);
         if (var54 != Integer.MIN_VALUE && var38 <= var54) {
            double var42 = var17.A;
            var17.P = var34;
            var17.b = var36;
            var17.G = var54;
            if (var42 > 0.0) {
               return new double[]{600 + var31, 0.0};
            }

            int var44 = class_3532.method_15357(var17.P);
            int var45 = class_3532.method_15357(var17.b);
            double var46 = Math.hypot(var17.P - var8, var17.b - var12);
            if (var2 != null && !var2.t(var44, var54, var45)) {
               return new double[]{850.0 + var46, 0.0};
            }

            int var48 = Math.abs(var17.G - var10) < 1.2 && var46 < 7.0 ? 1 : 0;
            double var49 = this.x(var1, var17.P, var17.G, var17.b, var17.o, var17.M);
            double var51 = (var48 != 0 ? 0 : 150) + var49 + var46 * 0.6 + var17.W() * 7.0 + Math.max(0.0, -var42 - 0.5) * 45.0 + var19;
            return new double[]{var51, var48 != 0 && var49 == 0.0 ? 1.0 : 0.0};
         }

         var17.P = var34;
         var17.G = var38;
         var17.b = var36;
         if (var18 > 0) {
            wf.d(var17);
            var18--;
         }

         if (var17.G < var10 - 6.0) {
            return new double[]{800.0, 0.0};
         }
      }

      double var53 = Math.hypot(var17.P - var8, var17.b - var12);
      double var33 = var17.G - var10;
      double var35 = Math.max(0.0, -((var8 - var17.P) * var27 + (var12 - var17.b) * var29));
      return new double[]{
         Math.min(280.0, 120.0 + Math.abs(var33 - var53 * 0.22) * 4.0 + var53 * 0.4 + Math.max(0.0, var17.W() - 1.15) * 25.0 + var35 * 1.5 + var19), 0.0
      };
   }

   private int q(gj.a var1, double var2, double var4) {
      this.a++;
      return var1.E(class_3532.method_15357(var2), class_3532.method_15357(var4));
   }

   double x(gj.a var1, double var2, double var4, double var6, double var8, double var10) {
      for (int var12 = 0; var12 < 14; var12++) {
         var2 += var8;
         var6 += var10;
         int var13 = this.q(var1, var2, var6);
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

   public interface a {
      int Z = Integer.MIN_VALUE;

      int E(int var1, int var2);
   }

   public static final class d {
      public double h;
      public double A;
      public double V = 290.0;
      public double N = Double.NaN;
      public double j = 14.0;
      public double S = 262.0;
      public double Y = Double.NEGATIVE_INFINITY;
      public double W = 0.08;
      public boolean X = (boolean)1;
      public double u = 70.0;
      public int C = 3;
      public double P = 1.45;

      public boolean O() {
         return !Double.isNaN(this.N);
      }

      public double U(double var1) {
         return !this.O() ? this.V : Math.min(this.V, this.N + 14.0 + Math.max(0.0, var1 - 45.0) * 0.095);
      }

      public double A(double var1) {
         return !this.O() ? this.j : Math.max(3.0, Math.min(this.j, (var1 - 45.0) * 0.25 + 4.0));
      }

      public double u(double var1, double var3) {
         return Math.hypot(this.h - var1, this.A - var3);
      }
   }

   public record f(float yaw, float pitch, boolean rocket, double cost, boolean hitsGround, double minClearance, double endY, double endSpeed, boolean wide) {
   }

   public interface k {
      boolean t(int var1, int var2, int var3);
   }

   public record o(float yaw, float pitch, double cost, boolean onTarget) {
   }
}
