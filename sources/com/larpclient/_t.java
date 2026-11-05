package com.larpclient;

import java.util.Arrays;

final class _t {
   static final double d = 1.15;
   static final double q = 0.85;
   static final float G = 0.35F;
   private static final double[][] n = new double[][]{
      {-0.25, -0.85}, {0.25, -0.85}, {-0.5, -0.33}, {0.5, -0.33}, {-0.5, 0.38}, {0.5, 0.38}, {-0.25, 1.15}, {0.25, 1.15}
   };
   static final double B = 0.004166666666666667;
   private static final double m = 30.0;
   private static final double g = 0.6;
   private static final double YJ = 34.0;
   private static final double N = 22.0;
   private static final double YH = 4.2;
   private static final double L = 0.75;
   private static final double a = 1.8;
   private static final double YU = 60.0;
   private static final double Yu = 0.8;
   private static final double l = 6.0;
   private static final double I = 0.32;
   private static final double R = 0.45;
   private static final double A = 0.6;
   private static final double O = 3.0;
   private static final double x = 5.6;
   private static final double E = 20.0;
   private static final double S = 16.0;
   private static final double V = 0.22;
   private static final double s = 0.5;
   private static final double v = 0.55;
   private static final double F = 0.62;
   private static final double Y2 = 0.7;
   private static final double YZ = 2.2;
   private og.e o;
   private double U;
   private double C;
   private double YE;
   private double b;
   private double Yk;
   private double YL;
   private double YX;
   private double u;
   private double Y0;
   private double Q;
   private double T;
   private double W;
   private double w;
   private double h;
   private double c;
   private double Yj;
   private double y;
   private double YW;
   private double p;
   private double r;
   private double Yy;
   private double Yw;
   private double K;
   private double D;
   private double f;
   private double H;
   private double j;
   private double k;
   private double z;
   private double t;
   private double Y;
   private double Yn;
   private double i;
   private double Y8;
   private boolean Z;
   private boolean P;
   private boolean J;
   private boolean Yt;
   private float e;
   private final double[] M = new double[10];
   private final double[] X = new double[10];

   _t(double var1, double var3, double var5) {
      this.U = var1;
      this.C = var3;
      this.H = var5;
   }

   og.e m() {
      return this.o;
   }

   boolean p() {
      return this.o == og.e.d;
   }

   void t(double var1, double var3, double var5) {
      double var7 = (var1 - this.U) / var5;
      double var9 = (var3 - this.C) / var5;
      double var11 = Math.cos(this.Yk);
      double var13 = Math.sin(this.Yk);
      this.YX = var7 * var11 + var9 * var13;
      this.u = -var7 * var13 + var9 * var11;
      this.Y0 = this.T = var1;
      this.Q = this.W = var3;
      this.w = this.h = 0.0;
      if (this.o != og.e.a && this.o != og.e.V) {
         this.YE = this.b = this.YL = 0.0;
      }

      if (this.o == og.e.V) {
         this.b = this.YL = 0.0;
      }

      this.r = this.p = 0.0;
      this.N(og.e.d);
   }

   void K(double var1, double var3) {
      this.Y0 = var1;
      this.Q = var3;
   }

   boolean l() {
      if (this.o != og.e.d) {
         return false;
      }

      this.N(og.e.a);
      return true;
   }

   boolean E(double var1, double var3, double var5) {
      double var7 = (var1 - this.U) / var5;
      double var9 = (var3 - this.C) / var5;
      double var11 = Math.cos(this.Yk);
      double var13 = Math.sin(this.Yk);
      return l(var7 * var11 + var9 * var13, -var7 * var13 + var9 * var11, Math.abs(this.H) > 45.0);
   }

   static boolean l(double var0, double var2, boolean var4) {
      double var5 = 0.06;
      if (!(var2 < -0.85 - var5) && !(var2 > 1.15 + var5)) {
         double var7 = !(var2 < -0.35) && !(var2 > 0.4) ? (var4 ? 0.3 : 0.5) : 0.27;
         return Math.abs(var0) <= var7 + var5;
      } else {
         return false;
      }
   }

   og.s u(double var1, double var3, double var5, double var7, double var9, float var11, float var12, float var13, float var14) {
      var1 = Math.min(var1, 0.05);
      this.e = 0.0F;
      if (var1 > 0.0) {
         double var15 = -Math.expm1(-var1 / 0.035);
         this.w = this.w + ((this.Y0 - this.T) / var1 - this.w) * var15;
         this.h = this.h + ((this.Q - this.W) / var1 - this.h) * var15;
      }

      double var33 = this.T;
      double var17 = this.W;
      this.T = this.Y0;
      this.W = this.Q;
      this.c = Math.min(this.c + var1, 0.05);
      int var19 = (int)(this.c / 0.004166666666666667);
      this.c -= var19 * 0.004166666666666667;

      for (int var20 = 0; var20 < var19 && this.o != null; var20++) {
         double var21 = (var20 + 1.0) / var19;
         this.q(var3, var5, var7, var9, var33 + (this.Y0 - var33) * var21, var17 + (this.Q - var17) * var21, var11);
      }

      if (this.o == null) {
         return null;
      }

      float var34 = (float)this.H;
      float var35 = (float)this.j;
      float var22 = (float)this.k;
      if (this.o == og.e.E) {
         double var23 = e(this.Yj / 0.48);
         double var25 = e(this.Yj / 0.33599999999999997);
         var34 = (float)(this.z + (var11 - this.z) * var23);
         var35 = (float)(Math.clamp(Math.toRadians(var34 - var11) + var12, -0.75, 0.75) * var25);
         var22 = (float)(var13 * var25);
      }

      double var36 = 1.0 - Math.clamp(Math.hypot(this.w, this.h) / (4.0 * var3), 0.0, 1.0);
      double var37 = this.o == og.e.d ? 0.014 * Math.sin(this.YW * 53.0) * this.f * var36 : 0.0;
      double var27 = !this.J ? 0.0 : (this.o == og.e.I ? 1.0 : (this.o == og.e.b ? 1.0 - e(this.Yj / 0.45) : 0.0));
      double var29 = (var5 - (this.C + L(this.Yk) * var3)) / var3;
      og.a var31 = new og.a(
         (float)this.M[0],
         (float)this.M[1],
         (float)this.M[2],
         (float)this.M[3],
         (float)this.M[4],
         (float)this.M[5],
         (float)this.M[6],
         (float)this.M[7],
         (float)this.M[8],
         (float)Math.clamp(this.M[9], 0.0, 1.0),
         this.Yt
      );
      return new og.s(
         this.o,
         this.U - var9,
         0.0,
         new og.i(var34, var35, var22, (float)this.Yw, (float)this.K, var14),
         new og.q(this.U, this.C, (float)(this.Yk + var37), Math.max(0.0, var29), var31, (float)var27, this.e, this.YE / var3, this.b / var3, (float)this.YL)
      );
   }

   private void q(double var1, double var3, double var5, double var7, double var9, double var11, float var13) {
      double var14 = 0.004166666666666667;
      this.Yj += var14;
      this.YW += var14;
      switch (this.o) {
         case d:
         case a:
            this.x(var14, var1, var3, var5, var9, var11);
            break;
         case I:
            if (this.Yj >= this.i) {
               this.t = v(this.Yk);
               this.Y = this.U;
               this.Yn = this.C;
               this.N(og.e.b);
            }
            break;
         case b:
            this.n(var1, var3, var5);
            break;
         case B:
            this.y(var1, var3, var7);
            break;
         case V:
            this.c(var14, var1, var3, var7);
            break;
         case E:
            if (this.Yj >= 0.48) {
               this.o = null;
               return;
            }
      }

      this.f(var14, var1);
   }

   private void x(double var1, double var3, double var5, double var7, double var9, double var11) {
      double var13 = 30.0 * var3;
      double var15 = 0.36 * var3 * var3;
      double var17 = 0.0;
      double var19 = var13;
      double var21 = 0.0;
      if (this.o == og.e.d) {
         double var23 = Math.cos(this.Yk);
         double var25 = Math.sin(this.Yk);
         double var27 = (this.YX * var23 - this.u * var25) * var3;
         double var29 = (this.YX * var25 + this.u * var23) * var3;
         double var31 = this.YE - this.YL * var29;
         double var33 = this.b + this.YL * var27;
         double var35 = 26.389378290154262;
         double var37 = var35 * var35 * (var9 - this.U - var27) + 1.5 * var35 * (this.w - var31);
         double var39 = var35 * var35 * (var11 - this.C - var29) + 1.5 * var35 * (this.h - var33);
         double var41 = Math.hypot(var37, var39);
         double var43 = 60.0 * var13;
         if (var41 > var43) {
            var37 *= var43 / var41;
            var39 *= var43 / var41;
         }

         var17 += var37;
         var19 += var39;
         var21 += var27 * var39 - var29 * var37;
         double var45 = this.YL * Math.exp(-1.8 * var1) - this.YL;
         this.YL += var45;
         this.YE += var45 * var29;
         this.b -= var45 * var27;
         var21 += this.f * 0.8 * Math.sin(this.Yy * 0.5) * var15;
         if (var29 > 0.2 * var3) {
            var21 += this.f * 6.0 * (var27 > 0.0 ? -1 : 1) * var15;
         }
      }

      this.YE += var17 * var1;
      this.b += var19 * var1;
      this.YL += var21 / var15 * var1;
      this.YE = this.YE * Math.exp(-0.12 * var1);
      this.b = this.b * Math.exp(-0.12 * var1);
      this.YL = this.YL * Math.exp(-0.35 * var1);
      if (this.Z) {
         this.YE = this.YE * Math.exp(-2.5 * var1);
         this.YL = this.YL * Math.exp(-4.0 * var1);
      }

      double var48 = Math.hypot(this.YE, this.b);
      double var49 = 34.0 * var3;
      if (var48 > var49) {
         this.YE *= var49 / var48;
         this.b *= var49 / var48;
      }

      this.YL = Math.clamp(this.YL, -22.0, 22.0);
      this.U = this.U + this.YE * var1;
      this.C = this.C + this.b * var1;
      this.Yk = v(this.Yk + this.YL * var1);
      this.X(var3, var5, var7, var15);
      double var50 = (var5 - (this.C + L(this.Yk) * var3)) / var3;
      double var51 = this.o == og.e.a ? (this.Z ? 0 : 1) : e((var50 - 0.04) / 0.46);
      this.f = this.f + (var51 - this.f) * -Math.expm1(-var1 * 10.0);
      if (this.o == og.e.a) {
         int var52 = this.Z && Math.hypot(this.YE, this.b) < 0.8 * var3 && Math.abs(this.YL) < 1.2 ? 1 : 0;
         this.p = var52 != 0 ? this.p + var1 : 0.0;
         if (this.p > 0.16 || this.Yj > 8.0) {
            this.X();
         }
      }
   }

   private void X(double var1, double var3, double var5, double var7) {
      this.Z = (boolean)0;
      double var9 = Math.cos(this.Yk);
      double var11 = Math.sin(this.Yk);

      for (int var13 = 0; var13 < 4; var13++) {
         if (var13 == 0 || this.o != og.e.d) {
            double var14 = var13 == 2 ? 1.0 : (var13 == 3 ? -1.0 : 0.0);
            double var16 = var13 == 0 ? -1.0 : (var13 == 1 ? 1.0 : 0.0);
            double var18 = 0.0;

            for (double[] var23 : n) {
               var18 = Math.max(var18, this.J(var13, var23, var9, var11, var1, var3, var5));
            }

            if (!(var18 <= 0.0)) {
               double var45 = 0.0;
               double var47 = 0.0;
               int var24 = 0;

               for (double[] var28 : n) {
                  if (!(this.J(var13, var28, var9, var11, var1, var3, var5) < var18 - 0.015 * var1)) {
                     var45 += (var28[0] * var9 - var28[1] * var11) * var1;
                     var47 += (var28[0] * var11 + var28[1] * var9) * var1;
                     var24++;
                  }
               }

               var45 /= var24;
               var47 /= var24;
               if (var13 == 0) {
                  this.C -= var18;
                  this.Z = (boolean)1;
               }

               double var49 = this.YE - this.YL * var47;
               double var51 = this.b + this.YL * var45;
               double var29 = var49 * var14 + var51 * var16;
               if (!(var29 >= 0.0)) {
                  double var31 = var13 != 0 ? 0.45 : (-var29 > 3.0 * var1 ? 0.32 : 0.0);
                  double var33 = var45 * var16 - var47 * var14;
                  double var35 = -(1.0 + var31) * var29 / (1.0 + var33 * var33 / var7);
                  this.YE += var35 * var14;
                  this.b += var35 * var16;
                  this.YL += var33 * var35 / var7;
                  double var37 = -var16;
                  double var39 = var14;
                  var49 = this.YE - this.YL * var47;
                  var51 = this.b + this.YL * var45;
                  double var41 = var45 * var39 - var47 * var37;
                  double var43 = Math.clamp(-(var49 * var37 + var51 * var39) / (1.0 + var41 * var41 / var7), -0.6 * var35, 0.6 * var35);
                  this.YE += var43 * var37;
                  this.b += var43 * var39;
                  this.YL += var41 * var43 / var7;
                  this.e = (float)Math.max(this.e, -var29 / var1);
                  this.r = Math.max(this.r, -var29 / var1);
               }
            }
         }
      }
   }

   private double J(int var1, double[] var2, double var3, double var5, double var7, double var9, double var11) {
      double var13 = this.U + (var2[0] * var3 - var2[1] * var5) * var7;
      double var15 = this.C + (var2[0] * var5 + var2[1] * var3) * var7;

      return switch (var1) {
         case 0 -> var15 - var9;
         case 1 -> -var15;
         case 2 -> -var13;
         default -> var13 - var11;
      };
   }

   private void X() {
      this.Yk = v(this.Yk);
      this.YE = this.b = this.YL = 0.0;
      this.P = (boolean)(Math.abs(this.Yk) < 0.5 ? 1 : 0);
      this.J = (boolean)(!this.P && this.r > 9.0 ? 1 : 0);
      this.t = this.Yk;
      this.Y = this.U;
      this.Yn = this.C;
      if (this.P) {
         this.N(og.e.B);
      } else {
         this.i = 0.5 + Math.min(0.5, this.r / 30.0);
         this.N(og.e.I);
      }
   }

   private void n(double var1, double var3, double var5) {
      int var7 = Math.abs(this.t) > 2.2 ? 1 : 0;
      double var8 = Math.min(1.0, this.Yj / (var7 != 0 ? 0.7 : 0.62));
      double var10 = var7 != 0 ? e(var8) : e((var8 - 0.14) / 0.86);
      this.Yk = this.t * (1.0 - var10);
      double var12 = (this.Yn - (var3 - L(this.t) * var1)) * (1.0 - var10);
      double var14 = var3 - L(this.Yk) * var1 + var12;
      if (var7 != 0) {
         this.U = this.Y;
         this.C = var14 - Math.sin(Math.PI * var10) * 0.9 * var1;
      } else {
         this.U = this.Y - 1.15 * var1 * (Math.sin(this.t) - Math.sin(this.Yk));
         this.C = var14;
      }

      this.U = v(this.U, this.Yk, var1, var5);
      if (var8 >= 1.0) {
         this.Yk = this.t = 0.0;
         this.C = var3 - 1.15 * var1;
         this.Y = this.U;
         this.N(og.e.B);
      }
   }

   private void y(double var1, double var3, double var5) {
      double var7 = this.Yj;
      double var9 = this.t * (1.0 - e(var7 / 0.22)) + (this.P ? 0.07 : 0.1) * Math.sin(var7 * 13.0) * Math.exp(-var7 * 5.5);
      double var11 = this.Y - 1.15 * var1 * Math.sin(this.t);
      this.Yk = var9;
      this.U = var11 + 1.15 * var1 * Math.sin(var9);
      this.C = var3 - L(var9) * var1;
      this.Yt = (boolean)(this.P && this.r > 7.0 && var7 < 0.16 ? 1 : 0);
      if (!(var7 < 0.55)) {
         this.Yk = 0.0;
         this.U = var11;
         this.C = var3 - 1.15 * var1;
         this.Yt = (boolean)0;
         this.z = this.H;
         this.D = 0.0;
         this.Y8 = Math.signum(var5 - this.U);
         this.N(Math.abs(var5 - this.U) > 0.12 * var1 ? og.e.V : og.e.E);
      }
   }

   private void c(double var1, double var3, double var5, double var7) {
      double var9 = var7 - this.U;
      double var11 = Math.abs(var9);
      double var13 = Math.signum(var9);
      if (var13 != 0.0 && var13 != this.Y8) {
         this.Y8 = var13;
         this.z = this.H;
         this.y = 0.0;
      }

      double var15 = 16.0 * var3;
      if (var11 <= this.D * this.D / (2.0 * var15)) {
         this.D = Math.max(0.5 * var3, this.D - var15 * var1);
      } else {
         this.D = Math.min(5.6 * var3, this.D + 20.0 * var3 * var1);
      }

      double var17 = Math.min(var11, this.D * var1);
      this.U += var13 * var17;
      this.C = var5 - 1.15 * var3;
      this.Yk = 0.0;
      this.YE = var13 * this.D;
      this.Yw += var17 / var3 * 4.0;
      this.K = Math.min(1.0, this.D / var3 / 5.0);
      if (!(var11 - var17 > 1.0E-6)) {
         this.U = var7;
         this.YE = this.D = this.K = 0.0;
         this.z = this.H;
         this.N(og.e.E);
      }
   }

   private void f(double var1, double var3) {
      double var5 = this.Yj;
      double var7 = Math.hypot(this.YE, this.b) / var3;
      double var9 = this.H;
      double var11 = 0.0;
      double var13 = 0.0;
      double var15 = 0.0;
      Arrays.fill(this.X, 0, 9, 0.0);
      switch (this.o) {
         case d:
            double var29 = Math.clamp(Math.hypot(this.w, this.h) / var3 / 5.0, 0.0, 1.0);
            this.Yy = this.Yy + var1 * (9.0 + 9.0 * var29) * (0.4 + 0.6 * this.f);
            var9 = -10.0 - Math.clamp(this.YE / var3 * 4.0, -28.0, 28.0);
            var11 = this.f * (0.42 * Math.sin(this.YW * 2.2) + 0.14 * Math.sin(this.YW * 5.7));
            var13 = 0.2 + 0.35 * this.f;
            double var31 = -0.9 - 1.65 * this.f;
            double var33 = 0.25 + 0.35 * this.f;
            double var35 = 0.15 + 0.65 * this.f;
            this.M(
               var31 + var33 * Math.sin(this.Yy * 1.3),
               0.25 + 0.35 * this.f + 0.25 * Math.sin(this.Yy * 0.9 + 1.0),
               var31 + var33 * Math.sin(this.Yy * 1.3 + 2.7),
               -(0.25 + 0.35 * this.f + 0.25 * Math.sin(this.Yy * 0.9 + 2.4)),
               var35 * Math.sin(this.Yy),
               0.06 + 0.1 * this.f,
               -var35 * Math.sin(this.Yy + 0.3),
               -(0.06 + 0.1 * this.f)
            );
            this.X[8] = Math.clamp(-v(this.Yk) * 0.45, -0.6, 0.6) * this.f;
            var15 = 1.0;
            break;
         case a:
            this.Yy = this.Yy + var1 * (this.Z ? 8 : 18);
            var9 = -6.0 - Math.clamp(this.YE / var3 * 2.5, -35.0, 35.0);
            var11 = 0.2 * Math.sin(this.YW * 3.0);
            var13 = 0.45 * this.f;
            double var28 = 0.4 + 0.6 * this.f;
            this.M(
               -1.5 + 1.25 * var28 * Math.sin(this.Yy),
               0.55 + 0.4 * Math.cos(this.Yy),
               -1.5 + 1.25 * var28 * Math.sin(this.Yy + Math.PI),
               -(0.55 + 0.4 * Math.cos(this.Yy + Math.PI)),
               0.9 * var28 * Math.sin(this.Yy * 0.85),
               0.12,
               -0.9 * var28 * Math.sin(this.Yy * 0.85),
               -0.12
            );
            var15 = 1.0;
            break;
         case I:
            var9 = 0.0;
            var11 = this.J ? 0.4 * Math.sin(this.YW * 3.2) : 0.12;
            var13 = -0.1;
            double var27 = 0.25 * Math.sin(var5 * 30.0) * Math.exp(-var5 * 7.0);
            double var19 = Math.clamp(Math.sin(this.Yk) * 1.4, -1.0, 1.0);
            double var21 = 1.0 - Math.abs(var19);
            double var23 = Math.max(0.0, var19);
            double var25 = Math.max(0.0, -var19);
            this.M(
               0.15 + var27,
               0.65 * var21 - 1.25 * var23 + 0.1 * var25,
               0.15 - var27,
               -0.65 * var21 + 1.25 * var25 - 0.1 * var23,
               var27,
               0.25 * var21 - 0.3 * var23 + 0.08 * var25,
               -var27,
               -0.25 * var21 + 0.3 * var25 - 0.08 * var23
            );
            this.X[8] = this.J ? 0.2 * Math.sin(this.YW * 3.2 + 1.0) : 0.0;
            var15 = 1.0;
            break;
         case b:
            var9 = 0.0;
            var13 = 0.1;
            this.M(-0.5, 0.3, -0.5, -0.3, -0.2, 0.12, 0.2, -0.12);
            var15 = 1.0 - e(this.Yj / (Math.abs(this.t) > 2.2 ? 0.7 : 0.62));
            break;
         case B:
            var9 = -12.0;
            var11 = 0.55 * Math.sin(var5 * 17.0) * Math.exp(-var5 * 4.5);
            var13 = 0.1 * (1.0 - e(var5 / 0.55));
            double var17 = 1.2 + 0.35 * Math.sin(var5 * 11.0) * Math.exp(-var5 * 2.5);
            this.M(-0.1, var17, -0.1, -var17, 0.0, 0.12, 0.0, -0.12);
            var15 = 1.0 - e((var5 - 0.2) / 0.3);
            break;
         case V:
            this.y += var1;
            this.H = this.z + ((this.Y8 < 0.0 ? -90 : 90) - this.z) * e(this.y / 0.22);
            var9 = this.H;
      }

      this.X[9] = var15;
      if (this.o != og.e.V && this.o != og.e.E) {
         this.H = this.H + (var9 - this.H) * -Math.expm1(-var1 * 9.0);
      }

      double var30 = -Math.expm1(-var1 * 14.0);
      this.j = this.j + (var11 - this.j) * var30;
      this.k = this.k + (var13 - this.k) * var30;
      double var32 = -Math.expm1(-var1 * 22.0);

      for (int var34 = 0; var34 < this.M.length; var34++) {
         this.M[var34] = this.M[var34] + (this.X[var34] - this.M[var34]) * var32;
      }

      if (this.o == og.e.d || this.o == og.e.a) {
         this.K = this.K + (Math.min(1.0, var7 / 6.0) - this.K) * var30;
         this.Yw += var7 * var1 * 2.0;
      } else if (this.o != og.e.V) {
         this.K = this.K + (0.0 - this.K) * var30;
      }
   }

   private void M(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15) {
      this.X[0] = var1;
      this.X[1] = var3;
      this.X[2] = var5;
      this.X[3] = var7;
      this.X[4] = var9;
      this.X[5] = var11;
      this.X[6] = var13;
      this.X[7] = var15;
   }

   private void N(og.e var1) {
      this.o = var1;
      this.Yj = this.y = 0.0;
      this.p = 0.0;
   }

   static double L(double var0) {
      double var2 = Math.cos(var0);
      double var4 = Math.sin(var0);
      double var6 = -Double.MAX_VALUE;

      for (double[] var11 : n) {
         var6 = Math.max(var6, var11[0] * var4 + var11[1] * var2);
      }

      return var6;
   }

   private static double v(double var0, double var2, double var4, double var6) {
      double var8 = Math.cos(var2);
      double var10 = Math.sin(var2);
      double var12 = Double.MAX_VALUE;
      double var14 = -Double.MAX_VALUE;

      for (double[] var19 : n) {
         double var20 = (var19[0] * var8 - var19[1] * var10) * var4;
         var12 = Math.min(var12, var20);
         var14 = Math.max(var14, var20);
      }

      return Math.clamp(var0, -var12, Math.max(-var12, var6 - var14));
   }

   double[][] Y(double var1) {
      double var3 = Math.cos(this.Yk);
      double var5 = Math.sin(this.Yk);
      double[][] var7 = new double[n.length][];

      for (int var8 = 0; var8 < n.length; var8++) {
         var7[var8] = new double[]{this.U + (n[var8][0] * var3 - n[var8][1] * var5) * var1, this.C + (n[var8][0] * var5 + n[var8][1] * var3) * var1};
      }

      return var7;
   }

   private static double v(double var0) {
      return Math.IEEEremainder(var0, Math.PI * 2);
   }

   private static double e(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * var0 * (var0 * (var0 * 6.0 - 15.0) + 10.0);
   }
}
