package com.larpclient;

import java.util.Arrays;

final class pe {
   static final double J = 1.15;
   static final double v = 0.85;
   static final float Uv = 0.35F;
   private static final double[][] q = new double[][]{
      {-0.25, -0.85}, {0.25, -0.85}, {-0.5, -0.33}, {0.5, -0.33}, {-0.5, 0.38}, {0.5, 0.38}, {-0.25, 1.15}, {0.25, 1.15}
   };
   static final double Z = 0.004166666666666667;
   private static final double U6 = 30.0;
   private static final double W = 0.6;
   private static final double U = 34.0;
   private static final double UC = 22.0;
   private static final double L = 4.2;
   private static final double P = 0.75;
   private static final double Y = 1.8;
   private static final double UJ = 60.0;
   private static final double D = 0.8;
   private static final double Ui = 6.0;
   private static final double w = 0.32;
   private static final double z = 0.45;
   private static final double o = 0.6;
   private static final double g = 3.0;
   private static final double F = 5.6;
   private static final double T = 20.0;
   private static final double l = 16.0;
   private static final double UN = 0.22;
   private static final double U5 = 0.5;
   private static final double O = 0.55;
   private static final double Q = 0.62;
   private static final double s = 0.7;
   private static final double UO = 2.2;
   private o5.o A;
   private double u;
   private double UI;
   private double UG;
   private double x;
   private double b;
   private double Un;
   private double d;
   private double I;
   private double c;
   private double t;
   private double UK;
   private double H;
   private double R;
   private double i;
   private double k;
   private double K;
   private double p;
   private double G;
   private double UR;
   private double j;
   private double S;
   private double X;
   private double y;
   private double h;
   private double r;
   private double C;
   private double n;
   private double Uo;
   private double M;
   private double Uf;
   private double m;
   private double e;
   private double E;
   private double V;
   private boolean U1;
   private boolean N;
   private boolean a;
   private boolean Uh;
   private float B;
   private final double[] f = new double[10];
   private final double[] Us = new double[10];

   pe(double var1, double var3, double var5) {
      this.u = var1;
      this.UI = var3;
      this.C = var5;
   }

   o5.o R() {
      return this.A;
   }

   boolean q() {
      return this.A == o5.o.A;
   }

   void d(double var1, double var3, double var5) {
      double var7 = (var1 - this.u) / var5;
      double var9 = (var3 - this.UI) / var5;
      double var11 = Math.cos(this.b);
      double var13 = Math.sin(this.b);
      this.d = var7 * var11 + var9 * var13;
      this.I = -var7 * var13 + var9 * var11;
      this.c = this.UK = var1;
      this.t = this.H = var3;
      this.R = this.i = 0.0;
      if (this.A != o5.o.x && this.A != o5.o.T) {
         this.UG = this.x = this.Un = 0.0;
      }

      if (this.A == o5.o.T) {
         this.x = this.Un = 0.0;
      }

      this.j = this.UR = 0.0;
      this.c(o5.o.A);
   }

   void i(double var1, double var3) {
      this.c = var1;
      this.t = var3;
   }

   boolean m() {
      if (this.A != o5.o.A) {
         return false;
      }

      this.c(o5.o.x);
      return true;
   }

   boolean o(double var1, double var3, double var5) {
      double var7 = (var1 - this.u) / var5;
      double var9 = (var3 - this.UI) / var5;
      double var11 = Math.cos(this.b);
      double var13 = Math.sin(this.b);
      return W(var7 * var11 + var9 * var13, -var7 * var13 + var9 * var11, Math.abs(this.C) > 45.0);
   }

   static boolean W(double var0, double var2, boolean var4) {
      double var5 = 0.06;
      if (!(var2 < -0.85 - var5) && !(var2 > 1.15 + var5)) {
         double var7 = !(var2 < -0.35) && !(var2 > 0.4) ? (var4 ? 0.3 : 0.5) : 0.27;
         return Math.abs(var0) <= var7 + var5;
      } else {
         return false;
      }
   }

   o5.z p(double var1, double var3, double var5, double var7, double var9, float var11, float var12, float var13, float var14) {
      var1 = Math.min(var1, 0.05);
      this.B = 0.0F;
      if (var1 > 0.0) {
         double var15 = -Math.expm1(-var1 / 0.035);
         this.R = this.R + ((this.c - this.UK) / var1 - this.R) * var15;
         this.i = this.i + ((this.t - this.H) / var1 - this.i) * var15;
      }

      double var33 = this.UK;
      double var17 = this.H;
      this.UK = this.c;
      this.H = this.t;
      this.k = Math.min(this.k + var1, 0.05);
      int var19 = (int)(this.k / 0.004166666666666667);
      this.k -= var19 * 0.004166666666666667;

      for (int var20 = 0; var20 < var19 && this.A != null; var20++) {
         double var21 = (var20 + 1.0) / var19;
         this.B(var3, var5, var7, var9, var33 + (this.c - var33) * var21, var17 + (this.t - var17) * var21, var11);
      }

      if (this.A == null) {
         return null;
      }

      float var34 = (float)this.C;
      float var35 = (float)this.n;
      float var22 = (float)this.Uo;
      if (this.A == o5.o.l) {
         double var23 = B(this.K / 0.48);
         double var25 = B(this.K / 0.33599999999999997);
         var34 = (float)(this.M + (var11 - this.M) * var23);
         var35 = (float)(Math.clamp(Math.toRadians(var34 - var11) + var12, -0.75, 0.75) * var25);
         var22 = (float)(var13 * var25);
      }

      double var36 = 1.0 - Math.clamp(Math.hypot(this.R, this.i) / (4.0 * var3), 0.0, 1.0);
      double var37 = this.A == o5.o.A ? 0.014 * Math.sin(this.G * 53.0) * this.r * var36 : 0.0;
      double var27 = !this.a ? 0.0 : (this.A == o5.o.j ? 1.0 : (this.A == o5.o.O ? 1.0 - B(this.K / 0.45) : 0.0));
      double var29 = (var5 - (this.UI + m(this.b) * var3)) / var3;
      o5.n var31 = new o5.n(
         (float)this.f[0],
         (float)this.f[1],
         (float)this.f[2],
         (float)this.f[3],
         (float)this.f[4],
         (float)this.f[5],
         (float)this.f[6],
         (float)this.f[7],
         (float)this.f[8],
         (float)Math.clamp(this.f[9], 0.0, 1.0),
         this.Uh
      );
      return new o5.z(
         this.A,
         this.u - var9,
         0.0,
         new o5.j(var34, var35, var22, (float)this.X, (float)this.y, var14),
         new o5.m(this.u, this.UI, (float)(this.b + var37), Math.max(0.0, var29), var31, (float)var27, this.B, this.UG / var3, this.x / var3, (float)this.Un)
      );
   }

   private void B(double var1, double var3, double var5, double var7, double var9, double var11, float var13) {
      double var14 = 0.004166666666666667;
      this.K += var14;
      this.G += var14;
      switch (this.A) {
         case A:
         case x:
            this.n(var14, var1, var3, var5, var9, var11);
            break;
         case j:
            if (this.K >= this.E) {
               this.Uf = X(this.b);
               this.m = this.u;
               this.e = this.UI;
               this.c(o5.o.O);
            }
            break;
         case O:
            this.S(var1, var3, var5);
            break;
         case h:
            this.v(var1, var3, var7);
            break;
         case T:
            this.I(var14, var1, var3, var7);
            break;
         case l:
            if (this.K >= 0.48) {
               this.A = null;
               return;
            }
      }

      this.g(var14, var1);
   }

   private void n(double var1, double var3, double var5, double var7, double var9, double var11) {
      double var13 = 30.0 * var3;
      double var15 = 0.36 * var3 * var3;
      double var17 = 0.0;
      double var19 = var13;
      double var21 = 0.0;
      if (this.A == o5.o.A) {
         double var23 = Math.cos(this.b);
         double var25 = Math.sin(this.b);
         double var27 = (this.d * var23 - this.I * var25) * var3;
         double var29 = (this.d * var25 + this.I * var23) * var3;
         double var31 = this.UG - this.Un * var29;
         double var33 = this.x + this.Un * var27;
         double var35 = 26.389378290154262;
         double var37 = var35 * var35 * (var9 - this.u - var27) + 1.5 * var35 * (this.R - var31);
         double var39 = var35 * var35 * (var11 - this.UI - var29) + 1.5 * var35 * (this.i - var33);
         double var41 = Math.hypot(var37, var39);
         double var43 = 60.0 * var13;
         if (var41 > var43) {
            var37 *= var43 / var41;
            var39 *= var43 / var41;
         }

         var17 += var37;
         var19 += var39;
         var21 += var27 * var39 - var29 * var37;
         double var45 = this.Un * Math.exp(-1.8 * var1) - this.Un;
         this.Un += var45;
         this.UG += var45 * var29;
         this.x -= var45 * var27;
         var21 += this.r * 0.8 * Math.sin(this.S * 0.5) * var15;
         if (var29 > 0.2 * var3) {
            var21 += this.r * 6.0 * (var27 > 0.0 ? -1 : 1) * var15;
         }
      }

      this.UG += var17 * var1;
      this.x += var19 * var1;
      this.Un += var21 / var15 * var1;
      this.UG = this.UG * Math.exp(-0.12 * var1);
      this.x = this.x * Math.exp(-0.12 * var1);
      this.Un = this.Un * Math.exp(-0.35 * var1);
      if (this.U1) {
         this.UG = this.UG * Math.exp(-2.5 * var1);
         this.Un = this.Un * Math.exp(-4.0 * var1);
      }

      double var48 = Math.hypot(this.UG, this.x);
      double var49 = 34.0 * var3;
      if (var48 > var49) {
         this.UG *= var49 / var48;
         this.x *= var49 / var48;
      }

      this.Un = Math.clamp(this.Un, -22.0, 22.0);
      this.u = this.u + this.UG * var1;
      this.UI = this.UI + this.x * var1;
      this.b = X(this.b + this.Un * var1);
      this.g(var3, var5, var7, var15);
      double var50 = (var5 - (this.UI + m(this.b) * var3)) / var3;
      double var51 = this.A == o5.o.x ? (this.U1 ? 0 : 1) : B((var50 - 0.04) / 0.46);
      this.r = this.r + (var51 - this.r) * -Math.expm1(-var1 * 10.0);
      if (this.A == o5.o.x) {
         int var52 = this.U1 && Math.hypot(this.UG, this.x) < 0.8 * var3 && Math.abs(this.Un) < 1.2 ? 1 : 0;
         this.UR = var52 != 0 ? this.UR + var1 : 0.0;
         if (this.UR > 0.16 || this.K > 8.0) {
            this.B();
         }
      }
   }

   private void g(double var1, double var3, double var5, double var7) {
      this.U1 = (boolean)0;
      double var9 = Math.cos(this.b);
      double var11 = Math.sin(this.b);

      for (int var13 = 0; var13 < 4; var13++) {
         if (var13 == 0 || this.A != o5.o.A) {
            double var14 = var13 == 2 ? 1.0 : (var13 == 3 ? -1.0 : 0.0);
            double var16 = var13 == 0 ? -1.0 : (var13 == 1 ? 1.0 : 0.0);
            double var18 = 0.0;

            for (double[] var23 : q) {
               var18 = Math.max(var18, this.h(var13, var23, var9, var11, var1, var3, var5));
            }

            if (!(var18 <= 0.0)) {
               double var45 = 0.0;
               double var47 = 0.0;
               int var24 = 0;

               for (double[] var28 : q) {
                  if (!(this.h(var13, var28, var9, var11, var1, var3, var5) < var18 - 0.015 * var1)) {
                     var45 += (var28[0] * var9 - var28[1] * var11) * var1;
                     var47 += (var28[0] * var11 + var28[1] * var9) * var1;
                     var24++;
                  }
               }

               var45 /= var24;
               var47 /= var24;
               if (var13 == 0) {
                  this.UI -= var18;
                  this.U1 = (boolean)1;
               }

               double var49 = this.UG - this.Un * var47;
               double var51 = this.x + this.Un * var45;
               double var29 = var49 * var14 + var51 * var16;
               if (!(var29 >= 0.0)) {
                  double var31 = var13 != 0 ? 0.45 : (-var29 > 3.0 * var1 ? 0.32 : 0.0);
                  double var33 = var45 * var16 - var47 * var14;
                  double var35 = -(1.0 + var31) * var29 / (1.0 + var33 * var33 / var7);
                  this.UG += var35 * var14;
                  this.x += var35 * var16;
                  this.Un += var33 * var35 / var7;
                  double var37 = -var16;
                  double var39 = var14;
                  var49 = this.UG - this.Un * var47;
                  var51 = this.x + this.Un * var45;
                  double var41 = var45 * var39 - var47 * var37;
                  double var43 = Math.clamp(-(var49 * var37 + var51 * var39) / (1.0 + var41 * var41 / var7), -0.6 * var35, 0.6 * var35);
                  this.UG += var43 * var37;
                  this.x += var43 * var39;
                  this.Un += var41 * var43 / var7;
                  this.B = (float)Math.max(this.B, -var29 / var1);
                  this.j = Math.max(this.j, -var29 / var1);
               }
            }
         }
      }
   }

   private double h(int var1, double[] var2, double var3, double var5, double var7, double var9, double var11) {
      double var13 = this.u + (var2[0] * var3 - var2[1] * var5) * var7;
      double var15 = this.UI + (var2[0] * var5 + var2[1] * var3) * var7;

      return switch (var1) {
         case 0 -> var15 - var9;
         case 1 -> -var15;
         case 2 -> -var13;
         default -> var13 - var11;
      };
   }

   private void B() {
      this.b = X(this.b);
      this.UG = this.x = this.Un = 0.0;
      this.N = (boolean)(Math.abs(this.b) < 0.5 ? 1 : 0);
      this.a = (boolean)(!this.N && this.j > 9.0 ? 1 : 0);
      this.Uf = this.b;
      this.m = this.u;
      this.e = this.UI;
      if (this.N) {
         this.c(o5.o.h);
      } else {
         this.E = 0.5 + Math.min(0.5, this.j / 30.0);
         this.c(o5.o.j);
      }
   }

   private void S(double var1, double var3, double var5) {
      int var7 = Math.abs(this.Uf) > 2.2 ? 1 : 0;
      double var8 = Math.min(1.0, this.K / (var7 != 0 ? 0.7 : 0.62));
      double var10 = var7 != 0 ? B(var8) : B((var8 - 0.14) / 0.86);
      this.b = this.Uf * (1.0 - var10);
      double var12 = (this.e - (var3 - m(this.Uf) * var1)) * (1.0 - var10);
      double var14 = var3 - m(this.b) * var1 + var12;
      if (var7 != 0) {
         this.u = this.m;
         this.UI = var14 - Math.sin(Math.PI * var10) * 0.9 * var1;
      } else {
         this.u = this.m - 1.15 * var1 * (Math.sin(this.Uf) - Math.sin(this.b));
         this.UI = var14;
      }

      this.u = L(this.u, this.b, var1, var5);
      if (var8 >= 1.0) {
         this.b = this.Uf = 0.0;
         this.UI = var3 - 1.15 * var1;
         this.m = this.u;
         this.c(o5.o.h);
      }
   }

   private void v(double var1, double var3, double var5) {
      double var7 = this.K;
      double var9 = this.Uf * (1.0 - B(var7 / 0.22)) + (this.N ? 0.07 : 0.1) * Math.sin(var7 * 13.0) * Math.exp(-var7 * 5.5);
      double var11 = this.m - 1.15 * var1 * Math.sin(this.Uf);
      this.b = var9;
      this.u = var11 + 1.15 * var1 * Math.sin(var9);
      this.UI = var3 - m(var9) * var1;
      this.Uh = (boolean)(this.N && this.j > 7.0 && var7 < 0.16 ? 1 : 0);
      if (!(var7 < 0.55)) {
         this.b = 0.0;
         this.u = var11;
         this.UI = var3 - 1.15 * var1;
         this.Uh = (boolean)0;
         this.M = this.C;
         this.h = 0.0;
         this.V = Math.signum(var5 - this.u);
         this.c(Math.abs(var5 - this.u) > 0.12 * var1 ? o5.o.T : o5.o.l);
      }
   }

   private void I(double var1, double var3, double var5, double var7) {
      double var9 = var7 - this.u;
      double var11 = Math.abs(var9);
      double var13 = Math.signum(var9);
      if (var13 != 0.0 && var13 != this.V) {
         this.V = var13;
         this.M = this.C;
         this.p = 0.0;
      }

      double var15 = 16.0 * var3;
      if (var11 <= this.h * this.h / (2.0 * var15)) {
         this.h = Math.max(0.5 * var3, this.h - var15 * var1);
      } else {
         this.h = Math.min(5.6 * var3, this.h + 20.0 * var3 * var1);
      }

      double var17 = Math.min(var11, this.h * var1);
      this.u += var13 * var17;
      this.UI = var5 - 1.15 * var3;
      this.b = 0.0;
      this.UG = var13 * this.h;
      this.X += var17 / var3 * 4.0;
      this.y = Math.min(1.0, this.h / var3 / 5.0);
      if (!(var11 - var17 > 1.0E-6)) {
         this.u = var7;
         this.UG = this.h = this.y = 0.0;
         this.M = this.C;
         this.c(o5.o.l);
      }
   }

   private void g(double var1, double var3) {
      double var5 = this.K;
      double var7 = Math.hypot(this.UG, this.x) / var3;
      double var9 = this.C;
      double var11 = 0.0;
      double var13 = 0.0;
      double var15 = 0.0;
      Arrays.fill(this.Us, 0, 9, 0.0);
      switch (this.A) {
         case A:
            double var29 = Math.clamp(Math.hypot(this.R, this.i) / var3 / 5.0, 0.0, 1.0);
            this.S = this.S + var1 * (9.0 + 9.0 * var29) * (0.4 + 0.6 * this.r);
            var9 = -10.0 - Math.clamp(this.UG / var3 * 4.0, -28.0, 28.0);
            var11 = this.r * (0.42 * Math.sin(this.G * 2.2) + 0.14 * Math.sin(this.G * 5.7));
            var13 = 0.2 + 0.35 * this.r;
            double var31 = -0.9 - 1.65 * this.r;
            double var33 = 0.25 + 0.35 * this.r;
            double var35 = 0.15 + 0.65 * this.r;
            this.X(
               var31 + var33 * Math.sin(this.S * 1.3),
               0.25 + 0.35 * this.r + 0.25 * Math.sin(this.S * 0.9 + 1.0),
               var31 + var33 * Math.sin(this.S * 1.3 + 2.7),
               -(0.25 + 0.35 * this.r + 0.25 * Math.sin(this.S * 0.9 + 2.4)),
               var35 * Math.sin(this.S),
               0.06 + 0.1 * this.r,
               -var35 * Math.sin(this.S + 0.3),
               -(0.06 + 0.1 * this.r)
            );
            this.Us[8] = Math.clamp(-X(this.b) * 0.45, -0.6, 0.6) * this.r;
            var15 = 1.0;
            break;
         case x:
            this.S = this.S + var1 * (this.U1 ? 8 : 18);
            var9 = -6.0 - Math.clamp(this.UG / var3 * 2.5, -35.0, 35.0);
            var11 = 0.2 * Math.sin(this.G * 3.0);
            var13 = 0.45 * this.r;
            double var28 = 0.4 + 0.6 * this.r;
            this.X(
               -1.5 + 1.25 * var28 * Math.sin(this.S),
               0.55 + 0.4 * Math.cos(this.S),
               -1.5 + 1.25 * var28 * Math.sin(this.S + Math.PI),
               -(0.55 + 0.4 * Math.cos(this.S + Math.PI)),
               0.9 * var28 * Math.sin(this.S * 0.85),
               0.12,
               -0.9 * var28 * Math.sin(this.S * 0.85),
               -0.12
            );
            var15 = 1.0;
            break;
         case j:
            var9 = 0.0;
            var11 = this.a ? 0.4 * Math.sin(this.G * 3.2) : 0.12;
            var13 = -0.1;
            double var27 = 0.25 * Math.sin(var5 * 30.0) * Math.exp(-var5 * 7.0);
            double var19 = Math.clamp(Math.sin(this.b) * 1.4, -1.0, 1.0);
            double var21 = 1.0 - Math.abs(var19);
            double var23 = Math.max(0.0, var19);
            double var25 = Math.max(0.0, -var19);
            this.X(
               0.15 + var27,
               0.65 * var21 - 1.25 * var23 + 0.1 * var25,
               0.15 - var27,
               -0.65 * var21 + 1.25 * var25 - 0.1 * var23,
               var27,
               0.25 * var21 - 0.3 * var23 + 0.08 * var25,
               -var27,
               -0.25 * var21 + 0.3 * var25 - 0.08 * var23
            );
            this.Us[8] = this.a ? 0.2 * Math.sin(this.G * 3.2 + 1.0) : 0.0;
            var15 = 1.0;
            break;
         case O:
            var9 = 0.0;
            var13 = 0.1;
            this.X(-0.5, 0.3, -0.5, -0.3, -0.2, 0.12, 0.2, -0.12);
            var15 = 1.0 - B(this.K / (Math.abs(this.Uf) > 2.2 ? 0.7 : 0.62));
            break;
         case h:
            var9 = -12.0;
            var11 = 0.55 * Math.sin(var5 * 17.0) * Math.exp(-var5 * 4.5);
            var13 = 0.1 * (1.0 - B(var5 / 0.55));
            double var17 = 1.2 + 0.35 * Math.sin(var5 * 11.0) * Math.exp(-var5 * 2.5);
            this.X(-0.1, var17, -0.1, -var17, 0.0, 0.12, 0.0, -0.12);
            var15 = 1.0 - B((var5 - 0.2) / 0.3);
            break;
         case T:
            this.p += var1;
            this.C = this.M + ((this.V < 0.0 ? -90 : 90) - this.M) * B(this.p / 0.22);
            var9 = this.C;
      }

      this.Us[9] = var15;
      if (this.A != o5.o.T && this.A != o5.o.l) {
         this.C = this.C + (var9 - this.C) * -Math.expm1(-var1 * 9.0);
      }

      double var30 = -Math.expm1(-var1 * 14.0);
      this.n = this.n + (var11 - this.n) * var30;
      this.Uo = this.Uo + (var13 - this.Uo) * var30;
      double var32 = -Math.expm1(-var1 * 22.0);

      for (int var34 = 0; var34 < this.f.length; var34++) {
         this.f[var34] = this.f[var34] + (this.Us[var34] - this.f[var34]) * var32;
      }

      if (this.A == o5.o.A || this.A == o5.o.x) {
         this.y = this.y + (Math.min(1.0, var7 / 6.0) - this.y) * var30;
         this.X += var7 * var1 * 2.0;
      } else if (this.A != o5.o.T) {
         this.y = this.y + (0.0 - this.y) * var30;
      }
   }

   private void X(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15) {
      this.Us[0] = var1;
      this.Us[1] = var3;
      this.Us[2] = var5;
      this.Us[3] = var7;
      this.Us[4] = var9;
      this.Us[5] = var11;
      this.Us[6] = var13;
      this.Us[7] = var15;
   }

   private void c(o5.o var1) {
      this.A = var1;
      this.K = this.p = 0.0;
      this.UR = 0.0;
   }

   static double m(double var0) {
      double var2 = Math.cos(var0);
      double var4 = Math.sin(var0);
      double var6 = -Double.MAX_VALUE;

      for (double[] var11 : q) {
         var6 = Math.max(var6, var11[0] * var4 + var11[1] * var2);
      }

      return var6;
   }

   private static double L(double var0, double var2, double var4, double var6) {
      double var8 = Math.cos(var2);
      double var10 = Math.sin(var2);
      double var12 = Double.MAX_VALUE;
      double var14 = -Double.MAX_VALUE;

      for (double[] var19 : q) {
         double var20 = (var19[0] * var8 - var19[1] * var10) * var4;
         var12 = Math.min(var12, var20);
         var14 = Math.max(var14, var20);
      }

      return Math.clamp(var0, -var12, Math.max(-var12, var6 - var14));
   }

   double[][] n(double var1) {
      double var3 = Math.cos(this.b);
      double var5 = Math.sin(this.b);
      double[][] var7 = new double[q.length][];

      for (int var8 = 0; var8 < q.length; var8++) {
         var7[var8] = new double[]{this.u + (q[var8][0] * var3 - q[var8][1] * var5) * var1, this.UI + (q[var8][0] * var5 + q[var8][1] * var3) * var1};
      }

      return var7;
   }

   private static double X(double var0) {
      return Math.IEEEremainder(var0, Math.PI * 2);
   }

   private static double B(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * var0 * (var0 * (var0 * 6.0 - 15.0) + 10.0);
   }
}
