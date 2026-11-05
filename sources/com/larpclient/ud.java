package com.larpclient;

import java.util.Arrays;

final class ud {
   static final double j1 = 1.15;
   static final double jy = 0.85;
   static final float jW = 0.35F;
   private static final double[][] jr = new double[][]{
      {-0.25, -0.85}, {0.25, -0.85}, {-0.5, -0.33}, {0.5, -0.33}, {-0.5, 0.38}, {0.5, 0.38}, {-0.25, 1.15}, {0.25, 1.15}
   };
   static final double A = 0.004166666666666667;
   private static final double o = 30.0;
   private static final double H = 0.6;
   private static final double w = 34.0;
   private static final double jv = 22.0;
   private static final double D = 4.2;
   private static final double Q = 0.75;
   private static final double K = 1.8;
   private static final double h = 60.0;
   private static final double N = 0.8;
   private static final double U = 6.0;
   private static final double b = 0.32;
   private static final double s = 0.45;
   private static final double q = 0.6;
   private static final double jm = 3.0;
   private static final double M = 5.6;
   private static final double B = 20.0;
   private static final double j_ = 16.0;
   private static final double jq = 0.22;
   private static final double F = 0.5;
   private static final double V = 0.55;
   private static final double r = 0.62;
   private static final double n = 0.7;
   private static final double jJ = 2.2;
   private zt.e G;
   private double J;
   private double jD;
   private double jV;
   private double m;
   private double P;
   private double i;
   private double S;
   private double f;
   private double Y;
   private double d;
   private double jG;
   private double k;
   private double p;
   private double x;
   private double l;
   private double T;
   private double jM;
   private double ji;
   private double e;
   private double jQ;
   private double z;
   private double y;
   private double t;
   private double O;
   private double E;
   private double a;
   private double v;
   private double W;
   private double c;
   private double Z;
   private double jg;
   private double jH;
   private double R;
   private double u;
   private boolean g;
   private boolean I;
   private boolean X;
   private boolean C;
   private float L;
   private final double[] j = new double[10];
   private final double[] jw = new double[10];

   ud(double var1, double var3, double var5) {
      this.J = var1;
      this.jD = var3;
      this.a = var5;
   }

   zt.e M() {
      return this.G;
   }

   boolean M() {
      return this.G == zt.e.y;
   }

   void v(double var1, double var3, double var5) {
      double var7 = (var1 - this.J) / var5;
      double var9 = (var3 - this.jD) / var5;
      double var11 = Math.cos(this.P);
      double var13 = Math.sin(this.P);
      this.S = var7 * var11 + var9 * var13;
      this.f = -var7 * var13 + var9 * var11;
      this.Y = this.jG = var1;
      this.d = this.k = var3;
      this.p = this.x = 0.0;
      if (this.G != zt.e.j && this.G != zt.e.u) {
         this.jV = this.m = this.i = 0.0;
      }

      if (this.G == zt.e.u) {
         this.m = this.i = 0.0;
      }

      this.jQ = this.e = 0.0;
      this.I(zt.e.y);
   }

   void r(double var1, double var3) {
      this.Y = var1;
      this.d = var3;
   }

   boolean Q() {
      if (this.G != zt.e.y) {
         return false;
      }

      this.I(zt.e.j);
      return true;
   }

   boolean n(double var1, double var3, double var5) {
      double var7 = (var1 - this.J) / var5;
      double var9 = (var3 - this.jD) / var5;
      double var11 = Math.cos(this.P);
      double var13 = Math.sin(this.P);
      return f(var7 * var11 + var9 * var13, -var7 * var13 + var9 * var11, Math.abs(this.a) > 45.0);
   }

   static boolean f(double var0, double var2, boolean var4) {
      double var5 = 0.06;
      if (!(var2 < -0.85 - var5) && !(var2 > 1.15 + var5)) {
         double var7 = !(var2 < -0.35) && !(var2 > 0.4) ? (var4 ? 0.3 : 0.5) : 0.27;
         return Math.abs(var0) <= var7 + var5;
      } else {
         return false;
      }
   }

   zt.l V(double var1, double var3, double var5, double var7, double var9, float var11, float var12, float var13, float var14) {
      var1 = Math.min(var1, 0.05);
      this.L = 0.0F;
      if (var1 > 0.0) {
         double var15 = -Math.expm1(-var1 / 0.035);
         this.p = this.p + ((this.Y - this.jG) / var1 - this.p) * var15;
         this.x = this.x + ((this.d - this.k) / var1 - this.x) * var15;
      }

      double var33 = this.jG;
      double var17 = this.k;
      this.jG = this.Y;
      this.k = this.d;
      this.l = Math.min(this.l + var1, 0.05);
      int var19 = (int)(this.l / 0.004166666666666667);
      this.l -= var19 * 0.004166666666666667;

      for (int var20 = 0; var20 < var19 && this.G != null; var20++) {
         double var21 = (var20 + 1.0) / var19;
         this.h(var3, var5, var7, var9, var33 + (this.Y - var33) * var21, var17 + (this.d - var17) * var21, var11);
      }

      if (this.G == null) {
         return null;
      }

      float var34 = (float)this.a;
      float var35 = (float)this.v;
      float var22 = (float)this.W;
      if (this.G == zt.e.p) {
         double var23 = w(this.T / 0.48);
         double var25 = w(this.T / 0.33599999999999997);
         var34 = (float)(this.c + (var11 - this.c) * var23);
         var35 = (float)(Math.clamp(Math.toRadians(var34 - var11) + var12, -0.75, 0.75) * var25);
         var22 = (float)(var13 * var25);
      }

      double var36 = 1.0 - Math.clamp(Math.hypot(this.p, this.x) / (4.0 * var3), 0.0, 1.0);
      double var37 = this.G == zt.e.y ? 0.014 * Math.sin(this.ji * 53.0) * this.E * var36 : 0.0;
      double var27 = !this.X ? 0.0 : (this.G == zt.e.a ? 1.0 : (this.G == zt.e.s ? 1.0 - w(this.T / 0.45) : 0.0));
      double var29 = (var5 - (this.jD + u(this.P) * var3)) / var3;
      zt.p var31 = new zt.p(
         (float)this.j[0],
         (float)this.j[1],
         (float)this.j[2],
         (float)this.j[3],
         (float)this.j[4],
         (float)this.j[5],
         (float)this.j[6],
         (float)this.j[7],
         (float)this.j[8],
         (float)Math.clamp(this.j[9], 0.0, 1.0),
         this.C
      );
      return new zt.l(
         this.G,
         this.J - var9,
         0.0,
         new zt.n(var34, var35, var22, (float)this.y, (float)this.t, var14),
         new zt.t(this.J, this.jD, (float)(this.P + var37), Math.max(0.0, var29), var31, (float)var27, this.L, this.jV / var3, this.m / var3, (float)this.i)
      );
   }

   private void h(double var1, double var3, double var5, double var7, double var9, double var11, float var13) {
      double var14 = 0.004166666666666667;
      this.T += var14;
      this.ji += var14;
      switch (this.G) {
         case y:
         case j:
            this.m(var14, var1, var3, var5, var9, var11);
            break;
         case a:
            if (this.T >= this.R) {
               this.Z = i(this.P);
               this.jg = this.J;
               this.jH = this.jD;
               this.I(zt.e.s);
            }
            break;
         case s:
            this.k(var1, var3, var5);
            break;
         case r:
            this.y(var1, var3, var7);
            break;
         case u:
            this.B(var14, var1, var3, var7);
            break;
         case p:
            if (this.T >= 0.48) {
               this.G = null;
               return;
            }
      }

      this.c(var14, var1);
   }

   private void m(double var1, double var3, double var5, double var7, double var9, double var11) {
      double var13 = 30.0 * var3;
      double var15 = 0.36 * var3 * var3;
      double var17 = 0.0;
      double var19 = var13;
      double var21 = 0.0;
      if (this.G == zt.e.y) {
         double var23 = Math.cos(this.P);
         double var25 = Math.sin(this.P);
         double var27 = (this.S * var23 - this.f * var25) * var3;
         double var29 = (this.S * var25 + this.f * var23) * var3;
         double var31 = this.jV - this.i * var29;
         double var33 = this.m + this.i * var27;
         double var35 = 26.389378290154262;
         double var37 = var35 * var35 * (var9 - this.J - var27) + 1.5 * var35 * (this.p - var31);
         double var39 = var35 * var35 * (var11 - this.jD - var29) + 1.5 * var35 * (this.x - var33);
         double var41 = Math.hypot(var37, var39);
         double var43 = 60.0 * var13;
         if (var41 > var43) {
            var37 *= var43 / var41;
            var39 *= var43 / var41;
         }

         var17 += var37;
         var19 += var39;
         var21 += var27 * var39 - var29 * var37;
         double var45 = this.i * Math.exp(-1.8 * var1) - this.i;
         this.i += var45;
         this.jV += var45 * var29;
         this.m -= var45 * var27;
         var21 += this.E * 0.8 * Math.sin(this.z * 0.5) * var15;
         if (var29 > 0.2 * var3) {
            var21 += this.E * 6.0 * (var27 > 0.0 ? -1 : 1) * var15;
         }
      }

      this.jV += var17 * var1;
      this.m += var19 * var1;
      this.i += var21 / var15 * var1;
      this.jV = this.jV * Math.exp(-0.12 * var1);
      this.m = this.m * Math.exp(-0.12 * var1);
      this.i = this.i * Math.exp(-0.35 * var1);
      if (this.g) {
         this.jV = this.jV * Math.exp(-2.5 * var1);
         this.i = this.i * Math.exp(-4.0 * var1);
      }

      double var48 = Math.hypot(this.jV, this.m);
      double var49 = 34.0 * var3;
      if (var48 > var49) {
         this.jV *= var49 / var48;
         this.m *= var49 / var48;
      }

      this.i = Math.clamp(this.i, -22.0, 22.0);
      this.J = this.J + this.jV * var1;
      this.jD = this.jD + this.m * var1;
      this.P = i(this.P + this.i * var1);
      this.g(var3, var5, var7, var15);
      double var50 = (var5 - (this.jD + u(this.P) * var3)) / var3;
      double var51 = this.G == zt.e.j ? (this.g ? 0 : 1) : w((var50 - 0.04) / 0.46);
      this.E = this.E + (var51 - this.E) * -Math.expm1(-var1 * 10.0);
      if (this.G == zt.e.j) {
         int var52 = this.g && Math.hypot(this.jV, this.m) < 0.8 * var3 && Math.abs(this.i) < 1.2 ? 1 : 0;
         this.e = var52 != 0 ? this.e + var1 : 0.0;
         if (this.e > 0.16 || this.T > 8.0) {
            this.J();
         }
      }
   }

   private void g(double var1, double var3, double var5, double var7) {
      this.g = (boolean)0;
      double var9 = Math.cos(this.P);
      double var11 = Math.sin(this.P);

      for (int var13 = 0; var13 < 4; var13++) {
         if (var13 == 0 || this.G != zt.e.y) {
            double var14 = var13 == 2 ? 1.0 : (var13 == 3 ? -1.0 : 0.0);
            double var16 = var13 == 0 ? -1.0 : (var13 == 1 ? 1.0 : 0.0);
            double var18 = 0.0;

            for (double[] var23 : jr) {
               var18 = Math.max(var18, this.L(var13, var23, var9, var11, var1, var3, var5));
            }

            if (!(var18 <= 0.0)) {
               double var45 = 0.0;
               double var47 = 0.0;
               int var24 = 0;

               for (double[] var28 : jr) {
                  if (!(this.L(var13, var28, var9, var11, var1, var3, var5) < var18 - 0.015 * var1)) {
                     var45 += (var28[0] * var9 - var28[1] * var11) * var1;
                     var47 += (var28[0] * var11 + var28[1] * var9) * var1;
                     var24++;
                  }
               }

               var45 /= var24;
               var47 /= var24;
               if (var13 == 0) {
                  this.jD -= var18;
                  this.g = (boolean)1;
               }

               double var49 = this.jV - this.i * var47;
               double var51 = this.m + this.i * var45;
               double var29 = var49 * var14 + var51 * var16;
               if (!(var29 >= 0.0)) {
                  double var31 = var13 != 0 ? 0.45 : (-var29 > 3.0 * var1 ? 0.32 : 0.0);
                  double var33 = var45 * var16 - var47 * var14;
                  double var35 = -(1.0 + var31) * var29 / (1.0 + var33 * var33 / var7);
                  this.jV += var35 * var14;
                  this.m += var35 * var16;
                  this.i += var33 * var35 / var7;
                  double var37 = -var16;
                  double var39 = var14;
                  var49 = this.jV - this.i * var47;
                  var51 = this.m + this.i * var45;
                  double var41 = var45 * var39 - var47 * var37;
                  double var43 = Math.clamp(-(var49 * var37 + var51 * var39) / (1.0 + var41 * var41 / var7), -0.6 * var35, 0.6 * var35);
                  this.jV += var43 * var37;
                  this.m += var43 * var39;
                  this.i += var41 * var43 / var7;
                  this.L = (float)Math.max(this.L, -var29 / var1);
                  this.jQ = Math.max(this.jQ, -var29 / var1);
               }
            }
         }
      }
   }

   private double L(int var1, double[] var2, double var3, double var5, double var7, double var9, double var11) {
      double var13 = this.J + (var2[0] * var3 - var2[1] * var5) * var7;
      double var15 = this.jD + (var2[0] * var5 + var2[1] * var3) * var7;

      return switch (var1) {
         case 0 -> var15 - var9;
         case 1 -> -var15;
         case 2 -> -var13;
         default -> var13 - var11;
      };
   }

   private void J() {
      this.P = i(this.P);
      this.jV = this.m = this.i = 0.0;
      this.I = (boolean)(Math.abs(this.P) < 0.5 ? 1 : 0);
      this.X = (boolean)(!this.I && this.jQ > 9.0 ? 1 : 0);
      this.Z = this.P;
      this.jg = this.J;
      this.jH = this.jD;
      if (this.I) {
         this.I(zt.e.r);
      } else {
         this.R = 0.5 + Math.min(0.5, this.jQ / 30.0);
         this.I(zt.e.a);
      }
   }

   private void k(double var1, double var3, double var5) {
      int var7 = Math.abs(this.Z) > 2.2 ? 1 : 0;
      double var8 = Math.min(1.0, this.T / (var7 != 0 ? 0.7 : 0.62));
      double var10 = var7 != 0 ? w(var8) : w((var8 - 0.14) / 0.86);
      this.P = this.Z * (1.0 - var10);
      double var12 = (this.jH - (var3 - u(this.Z) * var1)) * (1.0 - var10);
      double var14 = var3 - u(this.P) * var1 + var12;
      if (var7 != 0) {
         this.J = this.jg;
         this.jD = var14 - Math.sin(Math.PI * var10) * 0.9 * var1;
      } else {
         this.J = this.jg - 1.15 * var1 * (Math.sin(this.Z) - Math.sin(this.P));
         this.jD = var14;
      }

      this.J = e(this.J, this.P, var1, var5);
      if (var8 >= 1.0) {
         this.P = this.Z = 0.0;
         this.jD = var3 - 1.15 * var1;
         this.jg = this.J;
         this.I(zt.e.r);
      }
   }

   private void y(double var1, double var3, double var5) {
      double var7 = this.T;
      double var9 = this.Z * (1.0 - w(var7 / 0.22)) + (this.I ? 0.07 : 0.1) * Math.sin(var7 * 13.0) * Math.exp(-var7 * 5.5);
      double var11 = this.jg - 1.15 * var1 * Math.sin(this.Z);
      this.P = var9;
      this.J = var11 + 1.15 * var1 * Math.sin(var9);
      this.jD = var3 - u(var9) * var1;
      this.C = (boolean)(this.I && this.jQ > 7.0 && var7 < 0.16 ? 1 : 0);
      if (!(var7 < 0.55)) {
         this.P = 0.0;
         this.J = var11;
         this.jD = var3 - 1.15 * var1;
         this.C = (boolean)0;
         this.c = this.a;
         this.O = 0.0;
         this.u = Math.signum(var5 - this.J);
         this.I(Math.abs(var5 - this.J) > 0.12 * var1 ? zt.e.u : zt.e.p);
      }
   }

   private void B(double var1, double var3, double var5, double var7) {
      double var9 = var7 - this.J;
      double var11 = Math.abs(var9);
      double var13 = Math.signum(var9);
      if (var13 != 0.0 && var13 != this.u) {
         this.u = var13;
         this.c = this.a;
         this.jM = 0.0;
      }

      double var15 = 16.0 * var3;
      if (var11 <= this.O * this.O / (2.0 * var15)) {
         this.O = Math.max(0.5 * var3, this.O - var15 * var1);
      } else {
         this.O = Math.min(5.6 * var3, this.O + 20.0 * var3 * var1);
      }

      double var17 = Math.min(var11, this.O * var1);
      this.J += var13 * var17;
      this.jD = var5 - 1.15 * var3;
      this.P = 0.0;
      this.jV = var13 * this.O;
      this.y += var17 / var3 * 4.0;
      this.t = Math.min(1.0, this.O / var3 / 5.0);
      if (!(var11 - var17 > 1.0E-6)) {
         this.J = var7;
         this.jV = this.O = this.t = 0.0;
         this.c = this.a;
         this.I(zt.e.p);
      }
   }

   private void c(double var1, double var3) {
      double var5 = this.T;
      double var7 = Math.hypot(this.jV, this.m) / var3;
      double var9 = this.a;
      double var11 = 0.0;
      double var13 = 0.0;
      double var15 = 0.0;
      Arrays.fill(this.jw, 0, 9, 0.0);
      switch (this.G) {
         case y:
            double var29 = Math.clamp(Math.hypot(this.p, this.x) / var3 / 5.0, 0.0, 1.0);
            this.z = this.z + var1 * (9.0 + 9.0 * var29) * (0.4 + 0.6 * this.E);
            var9 = -10.0 - Math.clamp(this.jV / var3 * 4.0, -28.0, 28.0);
            var11 = this.E * (0.42 * Math.sin(this.ji * 2.2) + 0.14 * Math.sin(this.ji * 5.7));
            var13 = 0.2 + 0.35 * this.E;
            double var31 = -0.9 - 1.65 * this.E;
            double var33 = 0.25 + 0.35 * this.E;
            double var35 = 0.15 + 0.65 * this.E;
            this.G(
               var31 + var33 * Math.sin(this.z * 1.3),
               0.25 + 0.35 * this.E + 0.25 * Math.sin(this.z * 0.9 + 1.0),
               var31 + var33 * Math.sin(this.z * 1.3 + 2.7),
               -(0.25 + 0.35 * this.E + 0.25 * Math.sin(this.z * 0.9 + 2.4)),
               var35 * Math.sin(this.z),
               0.06 + 0.1 * this.E,
               -var35 * Math.sin(this.z + 0.3),
               -(0.06 + 0.1 * this.E)
            );
            this.jw[8] = Math.clamp(-i(this.P) * 0.45, -0.6, 0.6) * this.E;
            var15 = 1.0;
            break;
         case j:
            this.z = this.z + var1 * (this.g ? 8 : 18);
            var9 = -6.0 - Math.clamp(this.jV / var3 * 2.5, -35.0, 35.0);
            var11 = 0.2 * Math.sin(this.ji * 3.0);
            var13 = 0.45 * this.E;
            double var28 = 0.4 + 0.6 * this.E;
            this.G(
               -1.5 + 1.25 * var28 * Math.sin(this.z),
               0.55 + 0.4 * Math.cos(this.z),
               -1.5 + 1.25 * var28 * Math.sin(this.z + Math.PI),
               -(0.55 + 0.4 * Math.cos(this.z + Math.PI)),
               0.9 * var28 * Math.sin(this.z * 0.85),
               0.12,
               -0.9 * var28 * Math.sin(this.z * 0.85),
               -0.12
            );
            var15 = 1.0;
            break;
         case a:
            var9 = 0.0;
            var11 = this.X ? 0.4 * Math.sin(this.ji * 3.2) : 0.12;
            var13 = -0.1;
            double var27 = 0.25 * Math.sin(var5 * 30.0) * Math.exp(-var5 * 7.0);
            double var19 = Math.clamp(Math.sin(this.P) * 1.4, -1.0, 1.0);
            double var21 = 1.0 - Math.abs(var19);
            double var23 = Math.max(0.0, var19);
            double var25 = Math.max(0.0, -var19);
            this.G(
               0.15 + var27,
               0.65 * var21 - 1.25 * var23 + 0.1 * var25,
               0.15 - var27,
               -0.65 * var21 + 1.25 * var25 - 0.1 * var23,
               var27,
               0.25 * var21 - 0.3 * var23 + 0.08 * var25,
               -var27,
               -0.25 * var21 + 0.3 * var25 - 0.08 * var23
            );
            this.jw[8] = this.X ? 0.2 * Math.sin(this.ji * 3.2 + 1.0) : 0.0;
            var15 = 1.0;
            break;
         case s:
            var9 = 0.0;
            var13 = 0.1;
            this.G(-0.5, 0.3, -0.5, -0.3, -0.2, 0.12, 0.2, -0.12);
            var15 = 1.0 - w(this.T / (Math.abs(this.Z) > 2.2 ? 0.7 : 0.62));
            break;
         case r:
            var9 = -12.0;
            var11 = 0.55 * Math.sin(var5 * 17.0) * Math.exp(-var5 * 4.5);
            var13 = 0.1 * (1.0 - w(var5 / 0.55));
            double var17 = 1.2 + 0.35 * Math.sin(var5 * 11.0) * Math.exp(-var5 * 2.5);
            this.G(-0.1, var17, -0.1, -var17, 0.0, 0.12, 0.0, -0.12);
            var15 = 1.0 - w((var5 - 0.2) / 0.3);
            break;
         case u:
            this.jM += var1;
            this.a = this.c + ((this.u < 0.0 ? -90 : 90) - this.c) * w(this.jM / 0.22);
            var9 = this.a;
      }

      this.jw[9] = var15;
      if (this.G != zt.e.u && this.G != zt.e.p) {
         this.a = this.a + (var9 - this.a) * -Math.expm1(-var1 * 9.0);
      }

      double var30 = -Math.expm1(-var1 * 14.0);
      this.v = this.v + (var11 - this.v) * var30;
      this.W = this.W + (var13 - this.W) * var30;
      double var32 = -Math.expm1(-var1 * 22.0);

      for (int var34 = 0; var34 < this.j.length; var34++) {
         this.j[var34] = this.j[var34] + (this.jw[var34] - this.j[var34]) * var32;
      }

      if (this.G == zt.e.y || this.G == zt.e.j) {
         this.t = this.t + (Math.min(1.0, var7 / 6.0) - this.t) * var30;
         this.y += var7 * var1 * 2.0;
      } else if (this.G != zt.e.u) {
         this.t = this.t + (0.0 - this.t) * var30;
      }
   }

   private void G(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15) {
      this.jw[0] = var1;
      this.jw[1] = var3;
      this.jw[2] = var5;
      this.jw[3] = var7;
      this.jw[4] = var9;
      this.jw[5] = var11;
      this.jw[6] = var13;
      this.jw[7] = var15;
   }

   private void I(zt.e var1) {
      this.G = var1;
      this.T = this.jM = 0.0;
      this.e = 0.0;
   }

   static double u(double var0) {
      double var2 = Math.cos(var0);
      double var4 = Math.sin(var0);
      double var6 = -Double.MAX_VALUE;

      for (double[] var11 : jr) {
         var6 = Math.max(var6, var11[0] * var4 + var11[1] * var2);
      }

      return var6;
   }

   private static double e(double var0, double var2, double var4, double var6) {
      double var8 = Math.cos(var2);
      double var10 = Math.sin(var2);
      double var12 = Double.MAX_VALUE;
      double var14 = -Double.MAX_VALUE;

      for (double[] var19 : jr) {
         double var20 = (var19[0] * var8 - var19[1] * var10) * var4;
         var12 = Math.min(var12, var20);
         var14 = Math.max(var14, var20);
      }

      return Math.clamp(var0, -var12, Math.max(-var12, var6 - var14));
   }

   double[][] F(double var1) {
      double var3 = Math.cos(this.P);
      double var5 = Math.sin(this.P);
      double[][] var7 = new double[jr.length][];

      for (int var8 = 0; var8 < jr.length; var8++) {
         var7[var8] = new double[]{this.J + (jr[var8][0] * var3 - jr[var8][1] * var5) * var1, this.jD + (jr[var8][0] * var5 + jr[var8][1] * var3) * var1};
      }

      return var7;
   }

   private static double i(double var0) {
      return Math.IEEEremainder(var0, Math.PI * 2);
   }

   private static double w(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * var0 * (var0 * (var0 * 6.0 - 15.0) + 10.0);
   }
}
