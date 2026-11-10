package com.larpclient;

import java.util.Arrays;

final class ps {
   static final double y4 = 1.15;
   static final double N = 0.85;
   static final float T = 0.35F;
   private static final double[][] y1 = new double[][]{
      {-0.25, -0.85}, {0.25, -0.85}, {-0.5, -0.33}, {0.5, -0.33}, {-0.5, 0.38}, {0.5, 0.38}, {-0.25, 1.15}, {0.25, 1.15}
   };
   static final double J = 0.004166666666666667;
   private static final double e = 30.0;
   private static final double I = 0.6;
   private static final double O = 34.0;
   private static final double y7 = 22.0;
   private static final double u = 4.2;
   private static final double A = 0.75;
   private static final double yH = 1.8;
   private static final double n = 60.0;
   private static final double l = 0.8;
   private static final double V = 6.0;
   private static final double t = 0.32;
   private static final double r = 0.45;
   private static final double i = 0.6;
   private static final double R = 3.0;
   private static final double j = 5.6;
   private static final double S = 20.0;
   private static final double B = 16.0;
   private static final double y = 0.22;
   private static final double F = 0.5;
   private static final double f = 0.55;
   private static final double W = 0.62;
   private static final double y0 = 0.7;
   private static final double x = 2.2;
   private tx.k E;
   private double a;
   private double X;
   private double yn;
   private double yZ;
   private double y_;
   private double m;
   private double yP;
   private double h;
   private double C;
   private double w;
   private double y6;
   private double s;
   private double d;
   private double Z;
   private double U;
   private double P;
   private double Q;
   private double z;
   private double K;
   private double H;
   private double Y;
   private double o;
   private double D;
   private double q;
   private double y5;
   private double yY;
   private double b;
   private double v;
   private double c;
   private double yf;
   private double y2;
   private double yK;
   private double p;
   private double yF;
   private boolean L;
   private boolean yu;
   private boolean k;
   private boolean G;
   private float yR;
   private final double[] M = new double[10];
   private final double[] g = new double[10];

   ps(double var1, double var3, double var5) {
      this.a = var1;
      this.X = var3;
      this.yY = var5;
   }

   tx.k u() {
      return this.E;
   }

   boolean B() {
      return this.E == tx.k.P;
   }

   void C(double var1, double var3, double var5) {
      double var7 = (var1 - this.a) / var5;
      double var9 = (var3 - this.X) / var5;
      double var11 = Math.cos(this.y_);
      double var13 = Math.sin(this.y_);
      this.yP = var7 * var11 + var9 * var13;
      this.h = -var7 * var13 + var9 * var11;
      this.C = this.y6 = var1;
      this.w = this.s = var3;
      this.d = this.Z = 0.0;
      if (this.E != tx.k.Y && this.E != tx.k.Q) {
         this.yn = this.yZ = this.m = 0.0;
      }

      if (this.E == tx.k.Q) {
         this.yZ = this.m = 0.0;
      }

      this.H = this.K = 0.0;
      this.L(tx.k.P);
   }

   void k(double var1, double var3) {
      this.C = var1;
      this.w = var3;
   }

   boolean d() {
      if (this.E != tx.k.P) {
         return false;
      }

      this.L(tx.k.Y);
      return true;
   }

   boolean w(double var1, double var3, double var5) {
      double var7 = (var1 - this.a) / var5;
      double var9 = (var3 - this.X) / var5;
      double var11 = Math.cos(this.y_);
      double var13 = Math.sin(this.y_);
      return n(var7 * var11 + var9 * var13, -var7 * var13 + var9 * var11, Math.abs(this.yY) > 45.0);
   }

   static boolean n(double var0, double var2, boolean var4) {
      double var5 = 0.06;
      if (!(var2 < -0.85 - var5) && !(var2 > 1.15 + var5)) {
         double var7 = !(var2 < -0.35) && !(var2 > 0.4) ? (var4 ? 0.3 : 0.5) : 0.27;
         return Math.abs(var0) <= var7 + var5;
      } else {
         return false;
      }
   }

   tx.a T(double var1, double var3, double var5, double var7, double var9, float var11, float var12, float var13, float var14) {
      var1 = Math.min(var1, 0.05);
      this.yR = 0.0F;
      if (var1 > 0.0) {
         double var15 = -Math.expm1(-var1 / 0.035);
         this.d = this.d + ((this.C - this.y6) / var1 - this.d) * var15;
         this.Z = this.Z + ((this.w - this.s) / var1 - this.Z) * var15;
      }

      double var33 = this.y6;
      double var17 = this.s;
      this.y6 = this.C;
      this.s = this.w;
      this.U = Math.min(this.U + var1, 0.05);
      int var19 = (int)(this.U / 0.004166666666666667);
      this.U -= var19 * 0.004166666666666667;

      for (int var20 = 0; var20 < var19 && this.E != null; var20++) {
         double var21 = (var20 + 1.0) / var19;
         this.e(var3, var5, var7, var9, var33 + (this.C - var33) * var21, var17 + (this.w - var17) * var21, var11);
      }

      if (this.E == null) {
         return null;
      }

      float var34 = (float)this.yY;
      float var35 = (float)this.b;
      float var22 = (float)this.v;
      if (this.E == tx.k.F) {
         double var23 = A(this.P / 0.48);
         double var25 = A(this.P / 0.33599999999999997);
         var34 = (float)(this.c + (var11 - this.c) * var23);
         var35 = (float)(Math.clamp(Math.toRadians(var34 - var11) + var12, -0.75, 0.75) * var25);
         var22 = (float)(var13 * var25);
      }

      double var36 = 1.0 - Math.clamp(Math.hypot(this.d, this.Z) / (4.0 * var3), 0.0, 1.0);
      double var37 = this.E == tx.k.P ? 0.014 * Math.sin(this.z * 53.0) * this.y5 * var36 : 0.0;
      double var27 = !this.k ? 0.0 : (this.E == tx.k.K ? 1.0 : (this.E == tx.k.Z ? 1.0 - A(this.P / 0.45) : 0.0));
      double var29 = (var5 - (this.X + h(this.y_) * var3)) / var3;
      tx.g var31 = new tx.g(
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
         this.G
      );
      return new tx.a(
         this.E,
         this.a - var9,
         0.0,
         new tx.u(var34, var35, var22, (float)this.o, (float)this.D, var14),
         new tx.e(this.a, this.X, (float)(this.y_ + var37), Math.max(0.0, var29), var31, (float)var27, this.yR, this.yn / var3, this.yZ / var3, (float)this.m)
      );
   }

   private void e(double var1, double var3, double var5, double var7, double var9, double var11, float var13) {
      double var14 = 0.004166666666666667;
      this.P += var14;
      this.z += var14;
      switch (this.E) {
         case P:
         case Y:
            this.y(var14, var1, var3, var5, var9, var11);
            break;
         case K:
            if (this.P >= this.p) {
               this.yf = U(this.y_);
               this.y2 = this.a;
               this.yK = this.X;
               this.L(tx.k.Z);
            }
            break;
         case Z:
            this.m(var1, var3, var5);
            break;
         case R:
            this.W(var1, var3, var7);
            break;
         case Q:
            this.I(var14, var1, var3, var7);
            break;
         case F:
            if (this.P >= 0.48) {
               this.E = null;
               return;
            }
      }

      this.I(var14, var1);
   }

   private void y(double var1, double var3, double var5, double var7, double var9, double var11) {
      double var13 = 30.0 * var3;
      double var15 = 0.36 * var3 * var3;
      double var17 = 0.0;
      double var19 = var13;
      double var21 = 0.0;
      if (this.E == tx.k.P) {
         double var23 = Math.cos(this.y_);
         double var25 = Math.sin(this.y_);
         double var27 = (this.yP * var23 - this.h * var25) * var3;
         double var29 = (this.yP * var25 + this.h * var23) * var3;
         double var31 = this.yn - this.m * var29;
         double var33 = this.yZ + this.m * var27;
         double var35 = 26.389378290154262;
         double var37 = var35 * var35 * (var9 - this.a - var27) + 1.5 * var35 * (this.d - var31);
         double var39 = var35 * var35 * (var11 - this.X - var29) + 1.5 * var35 * (this.Z - var33);
         double var41 = Math.hypot(var37, var39);
         double var43 = 60.0 * var13;
         if (var41 > var43) {
            var37 *= var43 / var41;
            var39 *= var43 / var41;
         }

         var17 += var37;
         var19 += var39;
         var21 += var27 * var39 - var29 * var37;
         double var45 = this.m * Math.exp(-1.8 * var1) - this.m;
         this.m += var45;
         this.yn += var45 * var29;
         this.yZ -= var45 * var27;
         var21 += this.y5 * 0.8 * Math.sin(this.Y * 0.5) * var15;
         if (var29 > 0.2 * var3) {
            var21 += this.y5 * 6.0 * (var27 > 0.0 ? -1 : 1) * var15;
         }
      }

      this.yn += var17 * var1;
      this.yZ += var19 * var1;
      this.m += var21 / var15 * var1;
      this.yn = this.yn * Math.exp(-0.12 * var1);
      this.yZ = this.yZ * Math.exp(-0.12 * var1);
      this.m = this.m * Math.exp(-0.35 * var1);
      if (this.L) {
         this.yn = this.yn * Math.exp(-2.5 * var1);
         this.m = this.m * Math.exp(-4.0 * var1);
      }

      double var48 = Math.hypot(this.yn, this.yZ);
      double var49 = 34.0 * var3;
      if (var48 > var49) {
         this.yn *= var49 / var48;
         this.yZ *= var49 / var48;
      }

      this.m = Math.clamp(this.m, -22.0, 22.0);
      this.a = this.a + this.yn * var1;
      this.X = this.X + this.yZ * var1;
      this.y_ = U(this.y_ + this.m * var1);
      this.F(var3, var5, var7, var15);
      double var50 = (var5 - (this.X + h(this.y_) * var3)) / var3;
      double var51 = this.E == tx.k.Y ? (this.L ? 0 : 1) : A((var50 - 0.04) / 0.46);
      this.y5 = this.y5 + (var51 - this.y5) * -Math.expm1(-var1 * 10.0);
      if (this.E == tx.k.Y) {
         int var52 = this.L && Math.hypot(this.yn, this.yZ) < 0.8 * var3 && Math.abs(this.m) < 1.2 ? 1 : 0;
         this.K = var52 != 0 ? this.K + var1 : 0.0;
         if (this.K > 0.16 || this.P > 8.0) {
            this.Y();
         }
      }
   }

   private void F(double var1, double var3, double var5, double var7) {
      this.L = (boolean)0;
      double var9 = Math.cos(this.y_);
      double var11 = Math.sin(this.y_);

      for (int var13 = 0; var13 < 4; var13++) {
         if (var13 == 0 || this.E != tx.k.P) {
            double var14 = var13 == 2 ? 1.0 : (var13 == 3 ? -1.0 : 0.0);
            double var16 = var13 == 0 ? -1.0 : (var13 == 1 ? 1.0 : 0.0);
            double var18 = 0.0;

            for (double[] var23 : y1) {
               var18 = Math.max(var18, this.b(var13, var23, var9, var11, var1, var3, var5));
            }

            if (!(var18 <= 0.0)) {
               double var45 = 0.0;
               double var47 = 0.0;
               int var24 = 0;

               for (double[] var28 : y1) {
                  if (!(this.b(var13, var28, var9, var11, var1, var3, var5) < var18 - 0.015 * var1)) {
                     var45 += (var28[0] * var9 - var28[1] * var11) * var1;
                     var47 += (var28[0] * var11 + var28[1] * var9) * var1;
                     var24++;
                  }
               }

               var45 /= var24;
               var47 /= var24;
               if (var13 == 0) {
                  this.X -= var18;
                  this.L = (boolean)1;
               }

               double var49 = this.yn - this.m * var47;
               double var51 = this.yZ + this.m * var45;
               double var29 = var49 * var14 + var51 * var16;
               if (!(var29 >= 0.0)) {
                  double var31 = var13 != 0 ? 0.45 : (-var29 > 3.0 * var1 ? 0.32 : 0.0);
                  double var33 = var45 * var16 - var47 * var14;
                  double var35 = -(1.0 + var31) * var29 / (1.0 + var33 * var33 / var7);
                  this.yn += var35 * var14;
                  this.yZ += var35 * var16;
                  this.m += var33 * var35 / var7;
                  double var37 = -var16;
                  double var39 = var14;
                  var49 = this.yn - this.m * var47;
                  var51 = this.yZ + this.m * var45;
                  double var41 = var45 * var39 - var47 * var37;
                  double var43 = Math.clamp(-(var49 * var37 + var51 * var39) / (1.0 + var41 * var41 / var7), -0.6 * var35, 0.6 * var35);
                  this.yn += var43 * var37;
                  this.yZ += var43 * var39;
                  this.m += var41 * var43 / var7;
                  this.yR = (float)Math.max(this.yR, -var29 / var1);
                  this.H = Math.max(this.H, -var29 / var1);
               }
            }
         }
      }
   }

   private double b(int var1, double[] var2, double var3, double var5, double var7, double var9, double var11) {
      double var13 = this.a + (var2[0] * var3 - var2[1] * var5) * var7;
      double var15 = this.X + (var2[0] * var5 + var2[1] * var3) * var7;

      return switch (var1) {
         case 0 -> var15 - var9;
         case 1 -> -var15;
         case 2 -> -var13;
         default -> var13 - var11;
      };
   }

   private void Y() {
      this.y_ = U(this.y_);
      this.yn = this.yZ = this.m = 0.0;
      this.yu = (boolean)(Math.abs(this.y_) < 0.5 ? 1 : 0);
      this.k = (boolean)(!this.yu && this.H > 9.0 ? 1 : 0);
      this.yf = this.y_;
      this.y2 = this.a;
      this.yK = this.X;
      if (this.yu) {
         this.L(tx.k.R);
      } else {
         this.p = 0.5 + Math.min(0.5, this.H / 30.0);
         this.L(tx.k.K);
      }
   }

   private void m(double var1, double var3, double var5) {
      int var7 = Math.abs(this.yf) > 2.2 ? 1 : 0;
      double var8 = Math.min(1.0, this.P / (var7 != 0 ? 0.7 : 0.62));
      double var10 = var7 != 0 ? A(var8) : A((var8 - 0.14) / 0.86);
      this.y_ = this.yf * (1.0 - var10);
      double var12 = (this.yK - (var3 - h(this.yf) * var1)) * (1.0 - var10);
      double var14 = var3 - h(this.y_) * var1 + var12;
      if (var7 != 0) {
         this.a = this.y2;
         this.X = var14 - Math.sin(Math.PI * var10) * 0.9 * var1;
      } else {
         this.a = this.y2 - 1.15 * var1 * (Math.sin(this.yf) - Math.sin(this.y_));
         this.X = var14;
      }

      this.a = z(this.a, this.y_, var1, var5);
      if (var8 >= 1.0) {
         this.y_ = this.yf = 0.0;
         this.X = var3 - 1.15 * var1;
         this.y2 = this.a;
         this.L(tx.k.R);
      }
   }

   private void W(double var1, double var3, double var5) {
      double var7 = this.P;
      double var9 = this.yf * (1.0 - A(var7 / 0.22)) + (this.yu ? 0.07 : 0.1) * Math.sin(var7 * 13.0) * Math.exp(-var7 * 5.5);
      double var11 = this.y2 - 1.15 * var1 * Math.sin(this.yf);
      this.y_ = var9;
      this.a = var11 + 1.15 * var1 * Math.sin(var9);
      this.X = var3 - h(var9) * var1;
      this.G = (boolean)(this.yu && this.H > 7.0 && var7 < 0.16 ? 1 : 0);
      if (!(var7 < 0.55)) {
         this.y_ = 0.0;
         this.a = var11;
         this.X = var3 - 1.15 * var1;
         this.G = (boolean)0;
         this.c = this.yY;
         this.q = 0.0;
         this.yF = Math.signum(var5 - this.a);
         this.L(Math.abs(var5 - this.a) > 0.12 * var1 ? tx.k.Q : tx.k.F);
      }
   }

   private void I(double var1, double var3, double var5, double var7) {
      double var9 = var7 - this.a;
      double var11 = Math.abs(var9);
      double var13 = Math.signum(var9);
      if (var13 != 0.0 && var13 != this.yF) {
         this.yF = var13;
         this.c = this.yY;
         this.Q = 0.0;
      }

      double var15 = 16.0 * var3;
      if (var11 <= this.q * this.q / (2.0 * var15)) {
         this.q = Math.max(0.5 * var3, this.q - var15 * var1);
      } else {
         this.q = Math.min(5.6 * var3, this.q + 20.0 * var3 * var1);
      }

      double var17 = Math.min(var11, this.q * var1);
      this.a += var13 * var17;
      this.X = var5 - 1.15 * var3;
      this.y_ = 0.0;
      this.yn = var13 * this.q;
      this.o += var17 / var3 * 4.0;
      this.D = Math.min(1.0, this.q / var3 / 5.0);
      if (!(var11 - var17 > 1.0E-6)) {
         this.a = var7;
         this.yn = this.q = this.D = 0.0;
         this.c = this.yY;
         this.L(tx.k.F);
      }
   }

   private void I(double var1, double var3) {
      double var5 = this.P;
      double var7 = Math.hypot(this.yn, this.yZ) / var3;
      double var9 = this.yY;
      double var11 = 0.0;
      double var13 = 0.0;
      double var15 = 0.0;
      Arrays.fill(this.g, 0, 9, 0.0);
      switch (this.E) {
         case P:
            double var29 = Math.clamp(Math.hypot(this.d, this.Z) / var3 / 5.0, 0.0, 1.0);
            this.Y = this.Y + var1 * (9.0 + 9.0 * var29) * (0.4 + 0.6 * this.y5);
            var9 = -10.0 - Math.clamp(this.yn / var3 * 4.0, -28.0, 28.0);
            var11 = this.y5 * (0.42 * Math.sin(this.z * 2.2) + 0.14 * Math.sin(this.z * 5.7));
            var13 = 0.2 + 0.35 * this.y5;
            double var31 = -0.9 - 1.65 * this.y5;
            double var33 = 0.25 + 0.35 * this.y5;
            double var35 = 0.15 + 0.65 * this.y5;
            this.q(
               var31 + var33 * Math.sin(this.Y * 1.3),
               0.25 + 0.35 * this.y5 + 0.25 * Math.sin(this.Y * 0.9 + 1.0),
               var31 + var33 * Math.sin(this.Y * 1.3 + 2.7),
               -(0.25 + 0.35 * this.y5 + 0.25 * Math.sin(this.Y * 0.9 + 2.4)),
               var35 * Math.sin(this.Y),
               0.06 + 0.1 * this.y5,
               -var35 * Math.sin(this.Y + 0.3),
               -(0.06 + 0.1 * this.y5)
            );
            this.g[8] = Math.clamp(-U(this.y_) * 0.45, -0.6, 0.6) * this.y5;
            var15 = 1.0;
            break;
         case Y:
            this.Y = this.Y + var1 * (this.L ? 8 : 18);
            var9 = -6.0 - Math.clamp(this.yn / var3 * 2.5, -35.0, 35.0);
            var11 = 0.2 * Math.sin(this.z * 3.0);
            var13 = 0.45 * this.y5;
            double var28 = 0.4 + 0.6 * this.y5;
            this.q(
               -1.5 + 1.25 * var28 * Math.sin(this.Y),
               0.55 + 0.4 * Math.cos(this.Y),
               -1.5 + 1.25 * var28 * Math.sin(this.Y + Math.PI),
               -(0.55 + 0.4 * Math.cos(this.Y + Math.PI)),
               0.9 * var28 * Math.sin(this.Y * 0.85),
               0.12,
               -0.9 * var28 * Math.sin(this.Y * 0.85),
               -0.12
            );
            var15 = 1.0;
            break;
         case K:
            var9 = 0.0;
            var11 = this.k ? 0.4 * Math.sin(this.z * 3.2) : 0.12;
            var13 = -0.1;
            double var27 = 0.25 * Math.sin(var5 * 30.0) * Math.exp(-var5 * 7.0);
            double var19 = Math.clamp(Math.sin(this.y_) * 1.4, -1.0, 1.0);
            double var21 = 1.0 - Math.abs(var19);
            double var23 = Math.max(0.0, var19);
            double var25 = Math.max(0.0, -var19);
            this.q(
               0.15 + var27,
               0.65 * var21 - 1.25 * var23 + 0.1 * var25,
               0.15 - var27,
               -0.65 * var21 + 1.25 * var25 - 0.1 * var23,
               var27,
               0.25 * var21 - 0.3 * var23 + 0.08 * var25,
               -var27,
               -0.25 * var21 + 0.3 * var25 - 0.08 * var23
            );
            this.g[8] = this.k ? 0.2 * Math.sin(this.z * 3.2 + 1.0) : 0.0;
            var15 = 1.0;
            break;
         case Z:
            var9 = 0.0;
            var13 = 0.1;
            this.q(-0.5, 0.3, -0.5, -0.3, -0.2, 0.12, 0.2, -0.12);
            var15 = 1.0 - A(this.P / (Math.abs(this.yf) > 2.2 ? 0.7 : 0.62));
            break;
         case R:
            var9 = -12.0;
            var11 = 0.55 * Math.sin(var5 * 17.0) * Math.exp(-var5 * 4.5);
            var13 = 0.1 * (1.0 - A(var5 / 0.55));
            double var17 = 1.2 + 0.35 * Math.sin(var5 * 11.0) * Math.exp(-var5 * 2.5);
            this.q(-0.1, var17, -0.1, -var17, 0.0, 0.12, 0.0, -0.12);
            var15 = 1.0 - A((var5 - 0.2) / 0.3);
            break;
         case Q:
            this.Q += var1;
            this.yY = this.c + ((this.yF < 0.0 ? -90 : 90) - this.c) * A(this.Q / 0.22);
            var9 = this.yY;
      }

      this.g[9] = var15;
      if (this.E != tx.k.Q && this.E != tx.k.F) {
         this.yY = this.yY + (var9 - this.yY) * -Math.expm1(-var1 * 9.0);
      }

      double var30 = -Math.expm1(-var1 * 14.0);
      this.b = this.b + (var11 - this.b) * var30;
      this.v = this.v + (var13 - this.v) * var30;
      double var32 = -Math.expm1(-var1 * 22.0);

      for (int var34 = 0; var34 < this.M.length; var34++) {
         this.M[var34] = this.M[var34] + (this.g[var34] - this.M[var34]) * var32;
      }

      if (this.E == tx.k.P || this.E == tx.k.Y) {
         this.D = this.D + (Math.min(1.0, var7 / 6.0) - this.D) * var30;
         this.o += var7 * var1 * 2.0;
      } else if (this.E != tx.k.Q) {
         this.D = this.D + (0.0 - this.D) * var30;
      }
   }

   private void q(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15) {
      this.g[0] = var1;
      this.g[1] = var3;
      this.g[2] = var5;
      this.g[3] = var7;
      this.g[4] = var9;
      this.g[5] = var11;
      this.g[6] = var13;
      this.g[7] = var15;
   }

   private void L(tx.k var1) {
      this.E = var1;
      this.P = this.Q = 0.0;
      this.K = 0.0;
   }

   static double h(double var0) {
      double var2 = Math.cos(var0);
      double var4 = Math.sin(var0);
      double var6 = -Double.MAX_VALUE;

      for (double[] var11 : y1) {
         var6 = Math.max(var6, var11[0] * var4 + var11[1] * var2);
      }

      return var6;
   }

   private static double z(double var0, double var2, double var4, double var6) {
      double var8 = Math.cos(var2);
      double var10 = Math.sin(var2);
      double var12 = Double.MAX_VALUE;
      double var14 = -Double.MAX_VALUE;

      for (double[] var19 : y1) {
         double var20 = (var19[0] * var8 - var19[1] * var10) * var4;
         var12 = Math.min(var12, var20);
         var14 = Math.max(var14, var20);
      }

      return Math.clamp(var0, -var12, Math.max(-var12, var6 - var14));
   }

   double[][] F(double var1) {
      double var3 = Math.cos(this.y_);
      double var5 = Math.sin(this.y_);
      double[][] var7 = new double[y1.length][];

      for (int var8 = 0; var8 < y1.length; var8++) {
         var7[var8] = new double[]{this.a + (y1[var8][0] * var3 - y1[var8][1] * var5) * var1, this.X + (y1[var8][0] * var5 + y1[var8][1] * var3) * var1};
      }

      return var7;
   }

   private static double U(double var0) {
      return Math.IEEEremainder(var0, Math.PI * 2);
   }

   private static double A(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * var0 * (var0 * (var0 * 6.0 - 15.0) + 10.0);
   }
}
