package com.larpclient;

import java.util.Arrays;

final class qg {
   static final double H = 1.15;
   static final double f = 0.85;
   static final float l = 0.35F;
   private static final double[][] O = new double[][]{
      {-0.25, -0.85}, {0.25, -0.85}, {-0.5, -0.33}, {0.5, -0.33}, {-0.5, 0.38}, {0.5, 0.38}, {-0.25, 1.15}, {0.25, 1.15}
   };
   static final double gt = 0.004166666666666667;
   private static final double g_ = 30.0;
   private static final double a = 0.6;
   private static final double N = 34.0;
   private static final double gX = 22.0;
   private static final double F = 4.2;
   private static final double n = 0.75;
   private static final double h = 1.8;
   private static final double G = 60.0;
   private static final double s = 0.8;
   private static final double q = 6.0;
   private static final double K = 0.32;
   private static final double m = 0.45;
   private static final double w = 0.6;
   private static final double L = 3.0;
   private static final double v = 5.6;
   private static final double u = 20.0;
   private static final double gh = 16.0;
   private static final double r = 0.22;
   private static final double y = 0.5;
   private static final double gf = 0.55;
   private static final double W = 0.62;
   private static final double gC = 0.7;
   private static final double d = 2.2;
   private yx.s z;
   private double C;
   private double x;
   private double Z;
   private double t;
   private double g4;
   private double e;
   private double ge;
   private double gR;
   private double R;
   private double p;
   private double g8;
   private double Y;
   private double gU;
   private double T;
   private double gT;
   private double I;
   private double b;
   private double c;
   private double g5;
   private double gS;
   private double gN;
   private double gr;
   private double P;
   private double X;
   private double D;
   private double g;
   private double gj;
   private double J;
   private double A;
   private double V;
   private double k;
   private double i;
   private double j;
   private double Q;
   private boolean U;
   private boolean B;
   private boolean gu;
   private boolean E;
   private float S;
   private final double[] M = new double[10];
   private final double[] o = new double[10];

   qg(double var1, double var3, double var5) {
      this.C = var1;
      this.x = var3;
      this.g = var5;
   }

   yx.s v() {
      return this.z;
   }

   boolean D() {
      return this.z == yx.s.X;
   }

   void z(double var1, double var3, double var5) {
      double var7 = (var1 - this.C) / var5;
      double var9 = (var3 - this.x) / var5;
      double var11 = Math.cos(this.g4);
      double var13 = Math.sin(this.g4);
      this.ge = var7 * var11 + var9 * var13;
      this.gR = -var7 * var13 + var9 * var11;
      this.R = this.g8 = var1;
      this.p = this.Y = var3;
      this.gU = this.T = 0.0;
      if (this.z != yx.s.e && this.z != yx.s.h) {
         this.Z = this.t = this.e = 0.0;
      }

      if (this.z == yx.s.h) {
         this.t = this.e = 0.0;
      }

      this.gS = this.g5 = 0.0;
      this.Q(yx.s.X);
   }

   void W(double var1, double var3) {
      this.R = var1;
      this.p = var3;
   }

   boolean p() {
      if (this.z != yx.s.X) {
         return false;
      }

      this.Q(yx.s.e);
      return true;
   }

   boolean s(double var1, double var3, double var5) {
      double var7 = (var1 - this.C) / var5;
      double var9 = (var3 - this.x) / var5;
      double var11 = Math.cos(this.g4);
      double var13 = Math.sin(this.g4);
      return G(var7 * var11 + var9 * var13, -var7 * var13 + var9 * var11, Math.abs(this.g) > 45.0);
   }

   static boolean G(double var0, double var2, boolean var4) {
      double var5 = 0.06;
      if (!(var2 < -0.85 - var5) && !(var2 > 1.15 + var5)) {
         double var7 = !(var2 < -0.35) && !(var2 > 0.4) ? (var4 ? 0.3 : 0.5) : 0.27;
         return Math.abs(var0) <= var7 + var5;
      } else {
         return false;
      }
   }

   yx.o V(double var1, double var3, double var5, double var7, double var9, float var11, float var12, float var13, float var14) {
      var1 = Math.min(var1, 0.05);
      this.S = 0.0F;
      if (var1 > 0.0) {
         double var15 = -Math.expm1(-var1 / 0.035);
         this.gU = this.gU + ((this.R - this.g8) / var1 - this.gU) * var15;
         this.T = this.T + ((this.p - this.Y) / var1 - this.T) * var15;
      }

      double var33 = this.g8;
      double var17 = this.Y;
      this.g8 = this.R;
      this.Y = this.p;
      this.gT = Math.min(this.gT + var1, 0.05);
      int var19 = (int)(this.gT / 0.004166666666666667);
      this.gT -= var19 * 0.004166666666666667;

      for (int var20 = 0; var20 < var19 && this.z != null; var20++) {
         double var21 = (var20 + 1.0) / var19;
         this.g(var3, var5, var7, var9, var33 + (this.R - var33) * var21, var17 + (this.p - var17) * var21, var11);
      }

      if (this.z == null) {
         return null;
      }

      float var34 = (float)this.g;
      float var35 = (float)this.gj;
      float var22 = (float)this.J;
      if (this.z == yx.s.b) {
         double var23 = I(this.I / 0.48);
         double var25 = I(this.I / 0.33599999999999997);
         var34 = (float)(this.A + (var11 - this.A) * var23);
         var35 = (float)(Math.clamp(Math.toRadians(var34 - var11) + var12, -0.75, 0.75) * var25);
         var22 = (float)(var13 * var25);
      }

      double var36 = 1.0 - Math.clamp(Math.hypot(this.gU, this.T) / (4.0 * var3), 0.0, 1.0);
      double var37 = this.z == yx.s.X ? 0.014 * Math.sin(this.c * 53.0) * this.D * var36 : 0.0;
      double var27 = !this.gu ? 0.0 : (this.z == yx.s.E ? 1.0 : (this.z == yx.s.T ? 1.0 - I(this.I / 0.45) : 0.0));
      double var29 = (var5 - (this.x + u(this.g4) * var3)) / var3;
      yx.h var31 = new yx.h(
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
         this.E
      );
      return new yx.o(
         this.z,
         this.C - var9,
         0.0,
         new yx.k(var34, var35, var22, (float)this.gr, (float)this.P, var14),
         new yx.g(this.C, this.x, (float)(this.g4 + var37), Math.max(0.0, var29), var31, (float)var27, this.S, this.Z / var3, this.t / var3, (float)this.e)
      );
   }

   private void g(double var1, double var3, double var5, double var7, double var9, double var11, float var13) {
      double var14 = 0.004166666666666667;
      this.I += var14;
      this.c += var14;
      switch (this.z) {
         case X:
         case e:
            this.P(var14, var1, var3, var5, var9, var11);
            break;
         case E:
            if (this.I >= this.j) {
               this.V = X(this.g4);
               this.k = this.C;
               this.i = this.x;
               this.Q(yx.s.T);
            }
            break;
         case T:
            this.I(var1, var3, var5);
            break;
         case N:
            this.T(var1, var3, var7);
            break;
         case h:
            this.U(var14, var1, var3, var7);
            break;
         case b:
            if (this.I >= 0.48) {
               this.z = null;
               return;
            }
      }

      this.J(var14, var1);
   }

   private void P(double var1, double var3, double var5, double var7, double var9, double var11) {
      double var13 = 30.0 * var3;
      double var15 = 0.36 * var3 * var3;
      double var17 = 0.0;
      double var19 = var13;
      double var21 = 0.0;
      if (this.z == yx.s.X) {
         double var23 = Math.cos(this.g4);
         double var25 = Math.sin(this.g4);
         double var27 = (this.ge * var23 - this.gR * var25) * var3;
         double var29 = (this.ge * var25 + this.gR * var23) * var3;
         double var31 = this.Z - this.e * var29;
         double var33 = this.t + this.e * var27;
         double var35 = 26.389378290154262;
         double var37 = var35 * var35 * (var9 - this.C - var27) + 1.5 * var35 * (this.gU - var31);
         double var39 = var35 * var35 * (var11 - this.x - var29) + 1.5 * var35 * (this.T - var33);
         double var41 = Math.hypot(var37, var39);
         double var43 = 60.0 * var13;
         if (var41 > var43) {
            var37 *= var43 / var41;
            var39 *= var43 / var41;
         }

         var17 += var37;
         var19 += var39;
         var21 += var27 * var39 - var29 * var37;
         double var45 = this.e * Math.exp(-1.8 * var1) - this.e;
         this.e += var45;
         this.Z += var45 * var29;
         this.t -= var45 * var27;
         var21 += this.D * 0.8 * Math.sin(this.gN * 0.5) * var15;
         if (var29 > 0.2 * var3) {
            var21 += this.D * 6.0 * (var27 > 0.0 ? -1 : 1) * var15;
         }
      }

      this.Z += var17 * var1;
      this.t += var19 * var1;
      this.e += var21 / var15 * var1;
      this.Z = this.Z * Math.exp(-0.12 * var1);
      this.t = this.t * Math.exp(-0.12 * var1);
      this.e = this.e * Math.exp(-0.35 * var1);
      if (this.U) {
         this.Z = this.Z * Math.exp(-2.5 * var1);
         this.e = this.e * Math.exp(-4.0 * var1);
      }

      double var48 = Math.hypot(this.Z, this.t);
      double var49 = 34.0 * var3;
      if (var48 > var49) {
         this.Z *= var49 / var48;
         this.t *= var49 / var48;
      }

      this.e = Math.clamp(this.e, -22.0, 22.0);
      this.C = this.C + this.Z * var1;
      this.x = this.x + this.t * var1;
      this.g4 = X(this.g4 + this.e * var1);
      this.r(var3, var5, var7, var15);
      double var50 = (var5 - (this.x + u(this.g4) * var3)) / var3;
      double var51 = this.z == yx.s.e ? (this.U ? 0 : 1) : I((var50 - 0.04) / 0.46);
      this.D = this.D + (var51 - this.D) * -Math.expm1(-var1 * 10.0);
      if (this.z == yx.s.e) {
         int var52 = this.U && Math.hypot(this.Z, this.t) < 0.8 * var3 && Math.abs(this.e) < 1.2 ? 1 : 0;
         this.g5 = var52 != 0 ? this.g5 + var1 : 0.0;
         if (this.g5 > 0.16 || this.I > 8.0) {
            this.Y();
         }
      }
   }

   private void r(double var1, double var3, double var5, double var7) {
      this.U = (boolean)0;
      double var9 = Math.cos(this.g4);
      double var11 = Math.sin(this.g4);

      for (int var13 = 0; var13 < 4; var13++) {
         if (var13 == 0 || this.z != yx.s.X) {
            double var14 = var13 == 2 ? 1.0 : (var13 == 3 ? -1.0 : 0.0);
            double var16 = var13 == 0 ? -1.0 : (var13 == 1 ? 1.0 : 0.0);
            double var18 = 0.0;

            for (double[] var23 : O) {
               var18 = Math.max(var18, this.y(var13, var23, var9, var11, var1, var3, var5));
            }

            if (!(var18 <= 0.0)) {
               double var45 = 0.0;
               double var47 = 0.0;
               int var24 = 0;

               for (double[] var28 : O) {
                  if (!(this.y(var13, var28, var9, var11, var1, var3, var5) < var18 - 0.015 * var1)) {
                     var45 += (var28[0] * var9 - var28[1] * var11) * var1;
                     var47 += (var28[0] * var11 + var28[1] * var9) * var1;
                     var24++;
                  }
               }

               var45 /= var24;
               var47 /= var24;
               if (var13 == 0) {
                  this.x -= var18;
                  this.U = (boolean)1;
               }

               double var49 = this.Z - this.e * var47;
               double var51 = this.t + this.e * var45;
               double var29 = var49 * var14 + var51 * var16;
               if (!(var29 >= 0.0)) {
                  double var31 = var13 != 0 ? 0.45 : (-var29 > 3.0 * var1 ? 0.32 : 0.0);
                  double var33 = var45 * var16 - var47 * var14;
                  double var35 = -(1.0 + var31) * var29 / (1.0 + var33 * var33 / var7);
                  this.Z += var35 * var14;
                  this.t += var35 * var16;
                  this.e += var33 * var35 / var7;
                  double var37 = -var16;
                  double var39 = var14;
                  var49 = this.Z - this.e * var47;
                  var51 = this.t + this.e * var45;
                  double var41 = var45 * var39 - var47 * var37;
                  double var43 = Math.clamp(-(var49 * var37 + var51 * var39) / (1.0 + var41 * var41 / var7), -0.6 * var35, 0.6 * var35);
                  this.Z += var43 * var37;
                  this.t += var43 * var39;
                  this.e += var41 * var43 / var7;
                  this.S = (float)Math.max(this.S, -var29 / var1);
                  this.gS = Math.max(this.gS, -var29 / var1);
               }
            }
         }
      }
   }

   private double y(int var1, double[] var2, double var3, double var5, double var7, double var9, double var11) {
      double var13 = this.C + (var2[0] * var3 - var2[1] * var5) * var7;
      double var15 = this.x + (var2[0] * var5 + var2[1] * var3) * var7;

      return switch (var1) {
         case 0 -> var15 - var9;
         case 1 -> -var15;
         case 2 -> -var13;
         default -> var13 - var11;
      };
   }

   private void Y() {
      this.g4 = X(this.g4);
      this.Z = this.t = this.e = 0.0;
      this.B = (boolean)(Math.abs(this.g4) < 0.5 ? 1 : 0);
      this.gu = (boolean)(!this.B && this.gS > 9.0 ? 1 : 0);
      this.V = this.g4;
      this.k = this.C;
      this.i = this.x;
      if (this.B) {
         this.Q(yx.s.N);
      } else {
         this.j = 0.5 + Math.min(0.5, this.gS / 30.0);
         this.Q(yx.s.E);
      }
   }

   private void I(double var1, double var3, double var5) {
      int var7 = Math.abs(this.V) > 2.2 ? 1 : 0;
      double var8 = Math.min(1.0, this.I / (var7 != 0 ? 0.7 : 0.62));
      double var10 = var7 != 0 ? I(var8) : I((var8 - 0.14) / 0.86);
      this.g4 = this.V * (1.0 - var10);
      double var12 = (this.i - (var3 - u(this.V) * var1)) * (1.0 - var10);
      double var14 = var3 - u(this.g4) * var1 + var12;
      if (var7 != 0) {
         this.C = this.k;
         this.x = var14 - Math.sin(Math.PI * var10) * 0.9 * var1;
      } else {
         this.C = this.k - 1.15 * var1 * (Math.sin(this.V) - Math.sin(this.g4));
         this.x = var14;
      }

      this.C = B(this.C, this.g4, var1, var5);
      if (var8 >= 1.0) {
         this.g4 = this.V = 0.0;
         this.x = var3 - 1.15 * var1;
         this.k = this.C;
         this.Q(yx.s.N);
      }
   }

   private void T(double var1, double var3, double var5) {
      double var7 = this.I;
      double var9 = this.V * (1.0 - I(var7 / 0.22)) + (this.B ? 0.07 : 0.1) * Math.sin(var7 * 13.0) * Math.exp(-var7 * 5.5);
      double var11 = this.k - 1.15 * var1 * Math.sin(this.V);
      this.g4 = var9;
      this.C = var11 + 1.15 * var1 * Math.sin(var9);
      this.x = var3 - u(var9) * var1;
      this.E = (boolean)(this.B && this.gS > 7.0 && var7 < 0.16 ? 1 : 0);
      if (!(var7 < 0.55)) {
         this.g4 = 0.0;
         this.C = var11;
         this.x = var3 - 1.15 * var1;
         this.E = (boolean)0;
         this.A = this.g;
         this.X = 0.0;
         this.Q = Math.signum(var5 - this.C);
         this.Q(Math.abs(var5 - this.C) > 0.12 * var1 ? yx.s.h : yx.s.b);
      }
   }

   private void U(double var1, double var3, double var5, double var7) {
      double var9 = var7 - this.C;
      double var11 = Math.abs(var9);
      double var13 = Math.signum(var9);
      if (var13 != 0.0 && var13 != this.Q) {
         this.Q = var13;
         this.A = this.g;
         this.b = 0.0;
      }

      double var15 = 16.0 * var3;
      if (var11 <= this.X * this.X / (2.0 * var15)) {
         this.X = Math.max(0.5 * var3, this.X - var15 * var1);
      } else {
         this.X = Math.min(5.6 * var3, this.X + 20.0 * var3 * var1);
      }

      double var17 = Math.min(var11, this.X * var1);
      this.C += var13 * var17;
      this.x = var5 - 1.15 * var3;
      this.g4 = 0.0;
      this.Z = var13 * this.X;
      this.gr += var17 / var3 * 4.0;
      this.P = Math.min(1.0, this.X / var3 / 5.0);
      if (!(var11 - var17 > 1.0E-6)) {
         this.C = var7;
         this.Z = this.X = this.P = 0.0;
         this.A = this.g;
         this.Q(yx.s.b);
      }
   }

   private void J(double var1, double var3) {
      double var5 = this.I;
      double var7 = Math.hypot(this.Z, this.t) / var3;
      double var9 = this.g;
      double var11 = 0.0;
      double var13 = 0.0;
      double var15 = 0.0;
      Arrays.fill(this.o, 0, 9, 0.0);
      switch (this.z) {
         case X:
            double var29 = Math.clamp(Math.hypot(this.gU, this.T) / var3 / 5.0, 0.0, 1.0);
            this.gN = this.gN + var1 * (9.0 + 9.0 * var29) * (0.4 + 0.6 * this.D);
            var9 = -10.0 - Math.clamp(this.Z / var3 * 4.0, -28.0, 28.0);
            var11 = this.D * (0.42 * Math.sin(this.c * 2.2) + 0.14 * Math.sin(this.c * 5.7));
            var13 = 0.2 + 0.35 * this.D;
            double var31 = -0.9 - 1.65 * this.D;
            double var33 = 0.25 + 0.35 * this.D;
            double var35 = 0.15 + 0.65 * this.D;
            this.i(
               var31 + var33 * Math.sin(this.gN * 1.3),
               0.25 + 0.35 * this.D + 0.25 * Math.sin(this.gN * 0.9 + 1.0),
               var31 + var33 * Math.sin(this.gN * 1.3 + 2.7),
               -(0.25 + 0.35 * this.D + 0.25 * Math.sin(this.gN * 0.9 + 2.4)),
               var35 * Math.sin(this.gN),
               0.06 + 0.1 * this.D,
               -var35 * Math.sin(this.gN + 0.3),
               -(0.06 + 0.1 * this.D)
            );
            this.o[8] = Math.clamp(-X(this.g4) * 0.45, -0.6, 0.6) * this.D;
            var15 = 1.0;
            break;
         case e:
            this.gN = this.gN + var1 * (this.U ? 8 : 18);
            var9 = -6.0 - Math.clamp(this.Z / var3 * 2.5, -35.0, 35.0);
            var11 = 0.2 * Math.sin(this.c * 3.0);
            var13 = 0.45 * this.D;
            double var28 = 0.4 + 0.6 * this.D;
            this.i(
               -1.5 + 1.25 * var28 * Math.sin(this.gN),
               0.55 + 0.4 * Math.cos(this.gN),
               -1.5 + 1.25 * var28 * Math.sin(this.gN + Math.PI),
               -(0.55 + 0.4 * Math.cos(this.gN + Math.PI)),
               0.9 * var28 * Math.sin(this.gN * 0.85),
               0.12,
               -0.9 * var28 * Math.sin(this.gN * 0.85),
               -0.12
            );
            var15 = 1.0;
            break;
         case E:
            var9 = 0.0;
            var11 = this.gu ? 0.4 * Math.sin(this.c * 3.2) : 0.12;
            var13 = -0.1;
            double var27 = 0.25 * Math.sin(var5 * 30.0) * Math.exp(-var5 * 7.0);
            double var19 = Math.clamp(Math.sin(this.g4) * 1.4, -1.0, 1.0);
            double var21 = 1.0 - Math.abs(var19);
            double var23 = Math.max(0.0, var19);
            double var25 = Math.max(0.0, -var19);
            this.i(
               0.15 + var27,
               0.65 * var21 - 1.25 * var23 + 0.1 * var25,
               0.15 - var27,
               -0.65 * var21 + 1.25 * var25 - 0.1 * var23,
               var27,
               0.25 * var21 - 0.3 * var23 + 0.08 * var25,
               -var27,
               -0.25 * var21 + 0.3 * var25 - 0.08 * var23
            );
            this.o[8] = this.gu ? 0.2 * Math.sin(this.c * 3.2 + 1.0) : 0.0;
            var15 = 1.0;
            break;
         case T:
            var9 = 0.0;
            var13 = 0.1;
            this.i(-0.5, 0.3, -0.5, -0.3, -0.2, 0.12, 0.2, -0.12);
            var15 = 1.0 - I(this.I / (Math.abs(this.V) > 2.2 ? 0.7 : 0.62));
            break;
         case N:
            var9 = -12.0;
            var11 = 0.55 * Math.sin(var5 * 17.0) * Math.exp(-var5 * 4.5);
            var13 = 0.1 * (1.0 - I(var5 / 0.55));
            double var17 = 1.2 + 0.35 * Math.sin(var5 * 11.0) * Math.exp(-var5 * 2.5);
            this.i(-0.1, var17, -0.1, -var17, 0.0, 0.12, 0.0, -0.12);
            var15 = 1.0 - I((var5 - 0.2) / 0.3);
            break;
         case h:
            this.b += var1;
            this.g = this.A + ((this.Q < 0.0 ? -90 : 90) - this.A) * I(this.b / 0.22);
            var9 = this.g;
      }

      this.o[9] = var15;
      if (this.z != yx.s.h && this.z != yx.s.b) {
         this.g = this.g + (var9 - this.g) * -Math.expm1(-var1 * 9.0);
      }

      double var30 = -Math.expm1(-var1 * 14.0);
      this.gj = this.gj + (var11 - this.gj) * var30;
      this.J = this.J + (var13 - this.J) * var30;
      double var32 = -Math.expm1(-var1 * 22.0);

      for (int var34 = 0; var34 < this.M.length; var34++) {
         this.M[var34] = this.M[var34] + (this.o[var34] - this.M[var34]) * var32;
      }

      if (this.z == yx.s.X || this.z == yx.s.e) {
         this.P = this.P + (Math.min(1.0, var7 / 6.0) - this.P) * var30;
         this.gr += var7 * var1 * 2.0;
      } else if (this.z != yx.s.h) {
         this.P = this.P + (0.0 - this.P) * var30;
      }
   }

   private void i(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15) {
      this.o[0] = var1;
      this.o[1] = var3;
      this.o[2] = var5;
      this.o[3] = var7;
      this.o[4] = var9;
      this.o[5] = var11;
      this.o[6] = var13;
      this.o[7] = var15;
   }

   private void Q(yx.s var1) {
      this.z = var1;
      this.I = this.b = 0.0;
      this.g5 = 0.0;
   }

   static double u(double var0) {
      double var2 = Math.cos(var0);
      double var4 = Math.sin(var0);
      double var6 = -Double.MAX_VALUE;

      for (double[] var11 : O) {
         var6 = Math.max(var6, var11[0] * var4 + var11[1] * var2);
      }

      return var6;
   }

   private static double B(double var0, double var2, double var4, double var6) {
      double var8 = Math.cos(var2);
      double var10 = Math.sin(var2);
      double var12 = Double.MAX_VALUE;
      double var14 = -Double.MAX_VALUE;

      for (double[] var19 : O) {
         double var20 = (var19[0] * var8 - var19[1] * var10) * var4;
         var12 = Math.min(var12, var20);
         var14 = Math.max(var14, var20);
      }

      return Math.clamp(var0, -var12, Math.max(-var12, var6 - var14));
   }

   double[][] X(double var1) {
      double var3 = Math.cos(this.g4);
      double var5 = Math.sin(this.g4);
      double[][] var7 = new double[O.length][];

      for (int var8 = 0; var8 < O.length; var8++) {
         var7[var8] = new double[]{this.C + (O[var8][0] * var3 - O[var8][1] * var5) * var1, this.x + (O[var8][0] * var5 + O[var8][1] * var3) * var1};
      }

      return var7;
   }

   private static double X(double var0) {
      return Math.IEEEremainder(var0, Math.PI * 2);
   }

   private static double I(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * var0 * (var0 * (var0 * 6.0 - 15.0) + 10.0);
   }
}
