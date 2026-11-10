package com.larpclient;

import java.util.Arrays;
import java.util.Comparator;
import net.minecraft.world.phys.Vec3;

public final class uu {
   public static final double T = 0.625;
   public static final double M = 0.4;
   public static final double V = 0.81;
   public static final double v = 1.0;
   public static final double w = 3.2;
   public static final double N = 6.0;
   public static final double D = 5.0;
   public static final double O = 6.0;
   public static final double g = 6.9;
   public static final double cq = 8.0;
   public static final double l = 1.45;
   public static final double cU = 1.15;
   public static final double k = 2.9;
   public static final double h = 13.0;
   public static final double C = 16.0;
   public static final double c = 0.24;
   public static final double cQ = 44.0;
   public static final int c_ = 220;
   public static final int z = 280;
   public static final int Z = 60;
   public static final int Y = 10;
   public static final int I = 18;
   static final int cz = 100;
   static final int L = 430;
   static final int c9 = 70;
   static final int P = 72;
   private static final double s = 0.3;
   private static final double cO = 0.55;
   private static final int cc = -1;
   private static final int K = -14018528;
   private final pj J;
   private final double[] B;
   private final double[] o;
   private final double[] y;
   private final double[] G;
   private final double[] d;
   private final double[] cX;
   private final double[] cf;
   private final double[] r;
   private final double[] e;
   private final double[] n;
   private final double[] f;
   private final double[] Q;
   private final double[] ce;
   private final double[] u;
   private final double[] t;
   private final double[] cw;
   private final double[] F;
   private final double[] x;
   private final double[] cV;
   private final double[] j;
   private final double[] H;
   private final double[] W;
   private final double[] A;
   private final double[] i;
   private final double[] X;
   private final double[] S;
   private final double[] cP;
   private final double[] m;
   private final double[] ca;
   private final double[] b;
   private final double[] c0;
   private final double[] U;
   private final double[] E;
   private final double[] c7;
   private final double[] cb;
   private final byte[] p;
   private int q;
   public static final double R = 2.1;

   uu(pj var1, f var2) {
      this.J = var1;
      this.B = new double[220];
      this.o = new double[220];
      this.y = new double[220];
      this.G = new double[220];
      this.d = new double[220];
      this.cX = new double[220];

      for (int var3 = 0; var3 < 220; var3++) {
         double var4 = Math.sqrt(1.6 + 110.76 * var1.F(var3 + 2000));
         double var6 = var1.F(var3 + 2200) * Math.PI * 2.0;
         this.y[var3] = var4;
         this.B[var3] = Math.cos(var6) * var4;
         this.o[var3] = Math.sin(var6) * var4;
         this.G[var3] = var1.F(var3 + 2400) * Math.PI * 2.0;
         this.d[var3] = 0.28 + 0.18 * var1.F(var3 + 2600);
         this.cX[var3] = var1.F(var3 + 2800);
      }

      short var37 = 340;
      this.cf = new double[var37];
      this.r = new double[var37];
      this.e = new double[var37];
      this.n = new double[var37];
      this.f = new double[var37];
      this.Q = new double[var37];
      this.ce = new double[var37];
      this.u = new double[var37];
      this.t = new double[var37];
      this.cw = new double[var37];
      this.F = new double[var37];
      this.x = new double[var37];
      this.cV = new double[var37];
      this.j = new double[var37];
      this.H = new double[var37];
      this.W = new double[var37];
      this.A = new double[var37];
      this.i = new double[var37];
      this.X = new double[var37];
      this.S = new double[var37];
      this.cP = new double[var37];
      this.m = new double[var37];
      double var38 = Math.cos(var1.z);
      double var39 = Math.sin(var1.z);

      for (int var8 = 0; var8 < var37; var8++) {
         double var9 = var1.F(var8 + 3000) * 2.0 - 1.0;
         double var11 = var1.F(var8 + 3200) * Math.PI * 2.0;
         double var13 = Math.sqrt(1.0 - var9 * var9);
         double var15 = var13 * Math.cos(var11);
         double var17 = var9;
         double var19 = var13 * Math.sin(var11);
         double var21 = Math.abs(var17) > 0.9 ? 1.0 : 0.0;
         double var23 = Math.abs(var17) > 0.9 ? 0.0 : 1.0;
         double var25 = var23 * var19;
         double var27 = -var21 * var19;
         double var29 = var21 * var17 - var23 * var15;
         double var31 = Math.sqrt(var25 * var25 + var27 * var27 + var29 * var29);
         this.cf[var8] = var25 / var31;
         this.r[var8] = var27 / var31;
         this.e[var8] = var29 / var31;
         this.n[var8] = var17 * this.e[var8] - var19 * this.r[var8];
         this.f[var8] = var19 * this.cf[var8] - var15 * this.e[var8];
         this.Q[var8] = var15 * this.r[var8] - var17 * this.cf[var8];
         this.ce[var8] = var1.F(var8 + 3400) * Math.PI * 2.0;
         this.u[var8] = (1.0 + 0.9 * var1.F(var8 + 3600)) * (var1.F(var8 + 3800) < 0.5 ? -1 : 1);
         this.t[var8] = 0.82 + 0.26 * var1.F(var8 + 4000);
         if (var8 < 100) {
            double var33 = 0.1 + 1.6 * var1.F(var8 + 4200);
            double var35 = (var1.F(var8 + 4400) - 0.5) * 0.3;
            this.cw[var8] = 0.4 + 0.45 * var8 / 100.0;
            this.F[var8] = var38 * var33 - var39 * var35;
            this.cV[var8] = var39 * var33 + var38 * var35;
            this.x[var8] = 0.2 + 0.15 * var1.F(var8 + 4600);
         } else if (var8 < 280) {
            double var54 = 0.4 + 1.6 * var1.F(var8 + 4200);
            double var55 = var1.F(var8 + 4400) * Math.PI * 2.0;
            this.cw[var8] = 0.75 + 0.75 * (var8 - 100) / 180.0;
            this.F[var8] = Math.cos(var55) * var54;
            this.cV[var8] = Math.sin(var55) * var54;
            this.x[var8] = 0.04;
         } else {
            this.cw[var8] = 3.2;
         }

         this.j[var8] = 1.6 + 3.4 * var1.F(var8 + 4800);
         this.H[var8] = 0.5 + 2.4 * Math.pow(var1.F(var8 + 5000), 1.5);
         this.W[var8] = 1.3 + 1.0 * var1.F(var8 + 5200);
         this.A[var8] = 0.2 + 0.7 * var1.F(var8 + 5400);
         this.i[var8] = 1.5 + 1.5 * var1.F(var8 + 5600);
         this.X[var8] = var1.F(var8 + 5800) * Math.PI * 2.0;
         this.S[var8] = var1.F(var8 + 6000) * Math.PI * 2.0;
         this.cP[var8] = 1.4 + 2.2 * var1.F(var8 + 6200);
         this.m[var8] = var8 >= 280 ? 1.0 : var1.F(var8 + 6400) * 0.7;
      }

      short var40 = 572;
      this.ca = new double[var40];
      this.b = new double[var40];
      this.c0 = new double[var40];
      this.U = new double[var40];
      this.E = new double[var40];
      this.c7 = new double[var40];
      this.cb = new double[var40];
      this.p = new byte[var40];

      for (int var41 = 0; var41 < 430; var41++) {
         double var10 = 0.965 * Math.sqrt((var41 + 0.5) / 430.0);
         double var12 = var41 * 2.399963229728653;
         this.n(
            Math.cos(var12) * var10,
            Math.sin(var12) * var10,
            y(Math.cos(var12) * var10, Math.sin(var12) * var10) ? 0 : 1,
            var41 == 0 ? 0.2 : 0.2 + 0.05 * var1.F(var41 + 7000),
            0.0,
            0.0,
            var1.F(var41 + 7200)
         );
      }

      for (int var42 = 0; var42 < 70; var42++) {
         double var45 = (var42 + 0.3 * var1.F(var42 + 7400)) * Math.PI * 2.0 / 70.0;
         double var50 = 0.975 + 0.065 * var1.F(var42 + 7600);
         this.n(Math.cos(var45) * var50, Math.sin(var45) * var50, 2, 0.17 + 0.03 * var1.F(var42 + 7800), 0.0, 0.0, var1.F(var42 + 8000));
      }

      for (int var43 = 0; var43 < 72; var43++) {
         double var46 = var1.F(var43 + 8200) * Math.PI * 2.0;
         double var51 = 1.05 + 0.45 * Math.sqrt(var1.F(var43 + 8400));
         this.n(
            Math.cos(var46) * var51,
            Math.sin(var46) * var51,
            3,
            0.2 + 0.1 * var1.F(var43 + 8600),
            0.22 * Math.pow(var1.F(var43 + 8800), 2.0),
            0.6 * var1.F(var43 + 9000),
            var1.F(var43 + 9200)
         );
      }

      Integer[] var44 = new Integer[220];

      for (int var47 = 0; var47 < 220; var47++) {
         var44[var47] = var47;
      }

      Arrays.sort(var44, Comparator.comparingDouble(var1x -> this.y[var1x]));
      var2.o(var1.v.x, var1.v.z);
      var2.e(var1.v.x, var1.v.z, 2.0);
      Integer[] var48 = var44;
      int var49 = var48.length;

      for (int var52 = 0; var52 < var49; var52++) {
         int var53 = var48[var52];
         var2.o(var1.v.x + this.B[var53], var1.v.z + this.o[var53]);
      }

      var2.e(var1.v.x, var1.v.z, 4.75);
   }

   public int s() {
      return this.q;
   }

   public int e(int var1) {
      return this.p[var1];
   }

   public double X(int var1) {
      return this.y[var1];
   }

   private void n(double var1, double var3, int var5, double var6, double var8, double var10, double var12) {
      int var14 = this.q++;
      this.ca[var14] = var1;
      this.b[var14] = var3;
      this.p[var14] = (byte)var5;
      this.U[var14] = var6;
      this.E[var14] = var8;
      this.c7[var14] = var10;
      this.c0[var14] = var12 * Math.PI * 2.0;
      this.cb[var14] = this.J.F(var14 + 9400);
   }

   public static boolean y(double var0, double var2) {
      double var4 = var0 * var0 + (var2 - 0.5) * (var2 - 0.5);
      double var6 = var0 * var0 + (var2 + 0.5) * (var2 + 0.5);
      if (var4 <= 0.019600000000000003) {
         return true;
      } else if (var6 <= 0.019600000000000003) {
         return false;
      } else if (var4 <= 0.25) {
         return false;
      } else {
         return var6 <= 0.25 ? true : var0 > 0.0;
      }
   }

   public static pj.n y(pj var0, int var1, double var2) {
      double var4 = Math.min(1.0, Math.sqrt(Math.max(0.0, var2) * 1.6));
      double var6 = var4 * Math.PI / 2.0;
      double var8 = pj.R(var1, var0.l) / 16.0 * 0.9375;
      double var10 = (24.0 - pj.S[var1][1]) / 16.0 * 0.9375;
      double var12 = var8 * Math.cos(var6) + var10 * Math.sin(var6);
      double var14 = -var8 * Math.sin(var6) + var10 * Math.cos(var6) + 0.24 * Math.sin(var6);
      double var16 = var0.v.y + (var0.a().C() - var0.v.y) * pj.Z(var2 / 0.45);
      int var18 = new int[]{4, 2, 0, 3, 5, 1}[var1];
      double var19 = pj.Z((var2 - 0.4 - var18 * 0.025) / 0.28);
      return new pj.n(
         new Vec3(var0.v.x + var12 * Math.cos(var0.z), var16 + var14, var0.v.z + var12 * Math.sin(var0.z)), 0.0, 0.0, var6, 1.0, 1.0, var19, var4 >= 1.0
      );
   }

   public static double R(double var0, double var2, double var4, double var6) {
      double var8 = Math.clamp(var0 / var4, 0.0, 0.999);
      return var2 + var6 * (1.0 - Math.cbrt(1.0 - var8));
   }

   private static double b(double var0, double var2, double var4) {
      double var6 = Math.clamp(var0 / var4, 0.0, 1.0);
      return var2 * (1.0 - (1.0 - var6) * (1.0 - var6) * (1.0 - var6));
   }

   public double C(double var1) {
      double var3 = 0.5 + 0.5 * pj.Z((var1 - 0.45) / 1.05);
      return 1.15 * var3 * (1.0 - 0.24 * pj.Z((var1 - 2.6500000000000004) / 0.55));
   }

   public Vec3 C(int var1, double var2) {
      double var4 = this.C(var2) * this.t[var1];
      double var6 = this.ce[var1] + this.u[var1] * var2;
      double var8 = Math.cos(var6);
      double var10 = Math.sin(var6);
      double var12 = (this.cf[var1] * var8 + this.n[var1] * var10) * var4;
      double var14 = (this.r[var1] * var8 + this.f[var1] * var10) * var4;
      double var16 = (this.e[var1] * var8 + this.Q[var1] * var10) * var4;
      double var18 = 0.55 * var2;
      double var20 = Math.cos(var18);
      double var22 = Math.sin(var18);
      return new Vec3(var12 * var20 - var16 * var22, 1.45 + var14, var12 * var22 + var16 * var20);
   }

   public Vec3 Q(int var1, double var2) {
      if (var2 < this.cw[var1]) {
         return null;
      }

      if (var2 < 3.2) {
         Vec3 var28 = this.C(var1, var2);
         double var5 = pj.Z((var2 - this.cw[var1]) / 0.55);
         if (var5 >= 1.0) {
            return var28;
         }

         double var7 = Math.sin(var5 * Math.PI);
         return new Vec3(this.F[var1], this.x[var1], this.cV[var1]).lerp(var28, var5).add(-var28.z * var7 * 0.18, var7 * 0.35, var28.x * var7 * 0.18);
      } else {
         double var4 = var2 - 3.2;
         double var6;
         double var8;
         double var10;
         if (var1 < 280) {
            Vec3 var12 = this.C(var1, 3.2);
            var6 = Math.hypot(var12.x, var12.z);
            var8 = Math.atan2(var12.z, var12.x);
            var10 = var12.y;
         } else {
            var6 = 0.08;
            var8 = this.X[var1];
            var10 = 1.45 + (this.S[var1] / Math.PI - 1.0) * 0.3;
         }

         if (var4 >= this.W[var1] + 0.5) {
            return null;
         }

         double var29 = 1.0 - Math.exp(-var4 * 5.0);
         double var14 = pj.Z((var4 - 0.45) / (this.W[var1] - 0.45));
         double var16 = var6 + (this.j[var1] - var6) * var29;
         double var18 = var10 + this.H[var1] * var29;
         double var20 = var16 + (this.A[var1] - var16) * var14;
         double var22 = var18 + (0.04 - var18) * Math.pow(var14, 1.3);
         double var24 = Math.min(var4, this.W[var1]);
         double var26 = var8 + this.i[var1] * (var24 + 0.8 * var14 * var24);
         return new Vec3(Math.cos(var26) * var20, var22, Math.sin(var26) * var20);
      }
   }

   public static void h(kj var0, pj var1, double var2, int var4, int var5, double var6, boolean var8) {
      uu var9 = var1.U();
      if (var9 != null) {
         var9.h(var0, var2, var4, var5, var6, var8);
      }
   }

   private void h(kj var1, double var2, int var4, int var5, double var6, boolean var8) {
      f var9 = this.J.a();
      Vec3 var10 = new Vec3(this.J.v.x, var9.C(), this.J.v.z);
      Vec3 var11 = var10.add(0.0, 1.45, 0.0);
      int var12 = uh.q(var5, -1, 0.55);
      double var13 = pj.T(var2, 0.0, 0.42) * var6;
      y(var1, this.J.v.add(0.0, 0.9, 0.0), 0.45 + var2 * 1.6, uh.F(var12, var13));
      p(var1, var10, var2, var12, var6);
      double var15 = pj.Z((var2 - 1.0) / 0.35);
      double var17 = 1.0 - pj.Z((var2 - 5.0) / 1.0);
      double var19 = 1.0 + 0.45 * pj.T(var2, 3.2, 3.9000000000000004);
      double var21 = var15 * var17 * var6 * (0.93 + 0.07 * Math.sin(var2 * 9.0));
      if (var21 > 0.001) {
         double var23 = 44.0 * pj.Z((var2 - 1.0) / 0.5);
         double var25 = (0.16 + 0.1 * var17) * var19;
         Vec3 var27 = var10.add(0.0, -0.1, 0.0);
         Vec3 var28 = var10.add(0.0, Math.min(var23, 3.5), 0.0);
         Vec3 var29 = var10.add(0.0, var23, 0.0);
         int var30 = uh.q(var12, var4, 0.35);
         double[] var31 = new double[]{8.0, 4.5, 2.2, 1.0};
         double[] var32 = new double[]{0.035, 0.09, 0.3, 1.0};
         int[] var33 = new int[]{var30, var12, var12, -1};

         for (int var34 = 0; var34 < var31.length; var34++) {
            double var35 = var25 * var31[var34];
            double var37 = var21 * var32[var34];
            var1.ribbon(var27, var28, var35, uh.F(var33[var34], var37), uh.F(var33[var34], var37 * 0.9));
            var1.ribbon(var28, var29, var35, uh.F(var33[var34], var37 * 0.9), uh.F(var33[var34], 0.0));
         }

         y(var1, var10.add(0.0, 0.08, 0.0), 0.7 * var19, uh.F(var12, var21 * 0.55));
      }

      double var42 = pj.Z((var2 - 0.6) / 0.8) * (1.0 - pj.Z((var2 - 3.2) / 0.15)) * var6;
      if (var42 > 0.001) {
         double var43 = pj.Z((var2 - 2.6500000000000004) / 0.55);
         y(var1, var11, this.C(var2) * 1.7, uh.F(var4, var42 * (0.2 + 0.15 * var43)));
         y(var1, var11, 0.4 + 0.25 * var43, uh.F(-1, var42 * (0.35 + 0.45 * var43)));
      }

      double var44 = pj.Z((var2 - 1.3) / 0.6) * (1.0 - pj.Z((var2 - 2.9000000000000004) / 0.3)) * var6;

      for (int var45 = 0; var45 < 10 && var44 > 0.001; var45++) {
         double var47 = this.J.F(var45 + 9600) * Math.PI * 2.0 + var2 * 0.15;
         double var50 = -0.15 + 0.75 * this.J.F(var45 + 9700);
         double var53 = 4.0 + 3.0 * this.J.F(var45 + 9800);
         double var56 = 0.6 + 0.4 * Math.sin(var2 * 1.7 + var45 * 2.1);
         Vec3 var36 = var11.add(Math.cos(var47) * Math.cos(var50) * var53, Math.sin(var50) * var53, Math.sin(var47) * Math.cos(var50) * var53);
         var1.k(var11.x, var11.y, var11.z, var36.x, var36.y, var36.z, 0.05, 0.45, uh.F(var12, var44 * var56 * 0.3), uh.F(var12, 0.0));
      }

      double var46 = var2 - 3.2;
      if (var46 >= 0.0) {
         double var48 = pj.T(var46, 0.0, 0.55) * var6;
         y(var1, var11, 1.1 + var46 * 2.4, uh.F(-1, var48));
         y(var1, var10.add(0.0, 0.1, 0.0), 1.6 + var46 * 3.0, uh.F(var12, var48 * 0.8));
         double var51 = pj.T(var46, 0.0, 0.85) * var6;

         for (int var54 = 0; var54 < 18 && var51 > 0.001; var54++) {
            double var57 = (var54 + 0.5 * this.J.F(var54 + 9900)) * Math.PI * 2.0 / 18.0;
            double var58 = 0.05 + 0.85 * this.J.F(var54 + 10000);
            double var38 = (5.0 + 4.0 * this.J.F(var54 + 10100)) * pj.Z(var46 / 0.18);
            Vec3 var40 = var10.add(0.0, 0.15, 0.0);
            Vec3 var41 = var40.add(Math.cos(var57) * Math.cos(var58) * var38, Math.sin(var58) * var38, Math.sin(var57) * Math.cos(var58) * var38);
            var1.k(var40.x, var40.y, var40.z, var41.x, var41.y, var41.z, 0.14, 0.03, uh.F(-1, var51 * 0.7), uh.F(var12, 0.0));
         }

         Z(var1, var10, var46 - 0.02, 16.0, 1.3, 1.4, var12, var6);
      }

      double var49 = pj.Z((var2 - 4.300000000000001) / 1.0) * (1.0 - pj.Z((var2 - 5.0 - 0.4) / 0.6));
      if (var49 > 0.001) {
         l(var1, var10.add(0.0, 0.05, 0.0), 0.7 + 0.35 * var49, uh.F(var12, var49 * 0.8 * var6));
      }

      double var52 = pj.Z((var2 - 5.0) / 0.8) * (1.0 - pj.Z((var2 - 6.9) / 1.0999999999999996));
      double var55 = this.x(var2);
      if (var52 > 0.001) {
         var1.F(var10.x, var10.y + 0.05, var10.z, var55, 0.3, uh.F(var12, var52 * 0.55 * var6));
         l(var1, var10.add(0.0, 0.045, 0.0), var55 * 1.05, uh.F(var12, var52 * 0.14 * var6));
      }

      var1.blend(var8 ? gl.c.E : gl.c.K);
      if (!var8) {
         this.F(var1, var9, var2, var4, var5);
      }

      this.h(var1, var10, var2, var4, var5, var8 ? 0.35 * var6 : 1.0);
      this.k(var1, var9, var10, var2, var4, var5, var8 ? 0.4 * var6 : 1.0, var8);
      var1.blend(gl.c.E);
   }

   public double x(double var1) {
      double var3 = Math.clamp((var1 - 5.0) / 1.0, 0.0, 1.0);
      return 2.9 * (0.25 + 0.75 * (1.0 - (1.0 - var3) * (1.0 - var3) * (1.0 - var3)));
   }

   private void F(kj var1, f var2, double var3, int var5, int var6) {
      for (int var7 = 0; var7 < 220; var7++) {
         double var8 = pj.Z((var3 - R(this.y[var7], 0.06, 13.0, 1.5)) / 0.3);
         if (!(var8 <= 0.0)) {
            double var10 = pj.Z((var3 - R(this.y[var7], 3.22, 16.0, 1.3)) / 0.4);
            if (!(var10 >= 1.0)) {
               double var12 = this.J.v.x + this.B[var7];
               double var14 = this.J.v.z + this.o[var7];
               double var16 = var2.R(var12, var14);
               if (!Double.isNaN(var16)) {
                  double var18 = this.B[var7] / this.y[var7];
                  double var20 = this.o[var7] / this.y[var7];
                  double var22 = (1.0 - var8) * -0.7 + var10 * 1.1;
                  double var24 = (1.0 - var8) * 0.4 + var10 * 0.6;
                  double var26 = (1.0 - var8) * 0.9 + var10 * 1.4 + 0.08 * Math.sin(this.G[var7] * 3.0);
                  int var28 = uh.q(uh.q(var5, -2065764, this.cX[var7] * 0.3), var6, Math.max(0.0, this.cX[var7] - 0.55));
                  E(
                     var1,
                     var12 + var18 * var22,
                     var16 + 0.03 + var7 * 3.0E-4 + var24,
                     var14 + var20 * var22,
                     this.G[var7] + var10 * 2.0,
                     var26,
                     this.d[var7] * Math.sqrt(var8),
                     uh.F(var28, (1.0 - var10) * 0.97)
                  );
               }
            }
         }
      }
   }

   private void h(kj var1, Vec3 var2, double var3, int var5, int var6, double var7) {
      short var9 = 340;

      for (int var10 = 0; var10 < var9; var10++) {
         Vec3 var11 = this.Q(var10, var3);
         if (var11 != null) {
            double var12 = Math.min(1.0, 0.55 + (var3 - this.cw[var10]) * 1.6);
            double var14 = 1.0;
            if (var3 >= 3.2) {
               double var16 = 3.2 + this.W[var10];
               var14 = 1.0 - pj.Z((var3 - var16) / 0.5);
               if (var10 >= 280) {
                  var12 = Math.min(1.0, (var3 - 3.2) * 6.0);
               }
            }

            double var54 = (var10 >= 280 ? 0.065 : 0.12) * var12;
            int var18 = uh.q(var5, var10 >= 280 ? -1 : var6, this.m[var10]);
            int var19 = var3 >= 3.2 + this.W[var10] ? 1 : 0;
            double var20 = var19 != 0 ? 3.2 + this.W[var10] : var3;
            double var22 = this.X[var10] + var20 * this.cP[var10];
            double var24 = this.S[var10] + var20 * this.cP[var10] * 0.7;
            double var26 = var20 * this.cP[var10] * 1.3;
            if (var19 != 0) {
               var24 = 0.0;
               var26 = 0.0;
            }

            double var28 = Math.cos(var24) * Math.cos(var22);
            double var30 = Math.sin(var24);
            double var32 = Math.cos(var24) * Math.sin(var22);
            double var34 = -Math.sin(var22);
            double var36 = Math.cos(var22);
            double var38 = var30 * var36;
            double var40 = var32 * var34 - var28 * var36;
            double var42 = -var30 * var34;
            double var44 = var34 * Math.cos(var26) + var38 * Math.sin(var26);
            double var46 = var40 * Math.sin(var26);
            double var48 = var36 * Math.cos(var26) + var42 * Math.sin(var26);
            if (var19 != 0) {
               var28 = Math.cos(var22);
               var30 = 0.0;
               var32 = Math.sin(var22);
               var44 = -Math.sin(var22);
               var46 = 0.0;
               var48 = Math.cos(var22);
            }

            double var50 = var54 * 1.15;
            double var52 = var54 * 0.85;
            var1.m(
               var2.x + var11.x,
               var2.y + var11.y,
               var2.z + var11.z,
               var44 * var52,
               var46 * var52,
               var48 * var52,
               var28 * var50,
               var30 * var50,
               var32 * var50,
               uh.F(var18, var14 * var7),
               c3.a.q
            );
         }
      }
   }

   private void k(kj var1, f var2, Vec3 var3, double var4, int var6, int var7, double var8, boolean var10) {
      if (!(var4 < 5.0)) {
         double var11 = this.x(var4);
         double var13 = this.J.z + 0.3 * (var4 - 5.0);
         double var15 = Math.cos(var13);
         double var17 = Math.sin(var13);
         int var19 = uh.q(var7, -1, 0.7);

         for (int var20 = 0; var20 < this.q; var20++) {
            byte var21 = this.p[var20];
            if (!var10 || var21 != 1 && var21 != 3) {
               double var22 = Math.hypot(this.ca[var20], this.b[var20]);
               double var24 = pj.Z((var4 - 5.0 - 0.5 * Math.min(var22, 1.2) - 0.08 * this.cb[var20]) / 0.35);
               if (!(var24 <= 0.0)) {
                  double var26 = pj.Z((var4 - 6.9 - 0.3 * this.cb[var20]) / 0.8);
                  if (!(var26 >= 1.0)) {
                     double var28 = this.ca[var20] * var11;
                     double var30 = this.b[var20] * var11;
                     double var32 = var28 * var15 - var30 * var17;
                     double var34 = var28 * var17 + var30 * var15;
                     double var36 = 1.0 + var26 * 0.5 / Math.max(0.3, var22 * var11);
                     var32 *= var36;
                     var34 *= var36;
                     double var38 = var2.R(var3.x + var32, var3.z + var34);
                     if (!Double.isNaN(var38)) {
                        double var40 = var21 == 3 ? 0.025 * Math.sin(var4 * 2.0 + this.c0[var20] * 4.0) * var24 : 0.0;
                        double var42 = var38 + 0.035 + var21 * 0.004 + var20 % 7 * 6.0E-4 + this.E[var20] * var24 + var40 + var26 * 0.35;
                        double var44 = this.U[var20] * Math.max(0.6, var11 / 2.9) * Math.sqrt(var24);
                        double var46 = (1.0 - var26) * var24;

                        int var48 = switch (var21) {
                           case 0, 2 -> uh.F(var19, var46 * var8);
                           case 1 -> uh.F(-14018528, var46 * 0.28);
                           default -> uh.F(uh.q(var6, var7, this.cb[var20] * 0.5), var46 * var8);
                        };
                        E(var1, var3.x + var32, var42, var3.z + var34, this.c0[var20] + var13, this.c7[var20] + var26 * 0.8, var44, var48);
                     }
                  }
               }
            }
         }
      }
   }

   private static void E(kj var0, double var1, double var3, double var5, double var7, double var9, double var11, int var13) {
      if (var13 >>> 24 != 0 && !(var11 <= 0.001)) {
         double var14 = Math.cos(var7);
         double var16 = Math.sin(var7);
         double var18 = Math.cos(var9);
         double var20 = Math.sin(var9);
         double var22 = var11;
         double var24 = var11 * 0.74;
         var0.m(
            var1,
            var3 + var20 * var22 * 0.5,
            var5,
            -var16 * var24,
            0.0,
            var14 * var24,
            var14 * var18 * var22,
            var20 * var22,
            var16 * var18 * var22,
            var13,
            c3.a.q
         );
      }
   }

   private static void y(kj var0, Vec3 var1, double var2, int var4) {
      if (var4 >>> 24 > 0) {
         var0.I(var1.x, var1.y, var1.z, var2, 0.0, var4, c3.a.Z, 0.0, 0.0, 0.0);
      }
   }

   public static double U(double var0) {
      if (var0 < 0.06 || var0 >= 3.2) {
         return 0.0;
      }

      if (var0 < 2.1) {
         return b(var0 - 0.06, 13.0, 1.5);
      }

      double var2 = (var0 - 2.1) / 1.1;
      return 13.0 * (1.0 - var2 * var2);
   }

   private static void p(kj var0, Vec3 var1, double var2, int var4, double var5) {
      double var7 = U(var2);
      if (!(var7 < 0.05)) {
         double var9;
         if (var2 < 1.6) {
            var9 = 1.0 - 0.55 * pj.Z((var2 - 0.06) / 1.5);
         } else if (var2 < 2.1) {
            var9 = 0.45 + 0.08 * Math.sin((var2 - 1.6) * 4.0);
         } else {
            var9 = 0.45 + 0.55 * pj.Z((var2 - 2.1) / 1.1);
         }

         var9 *= var5;
         var0.F(var1.x, var1.y + 0.06, var1.z, var7, 0.6 + var7 * 0.06, uh.F(var4, var9 * 0.85));
         l(var0, var1, var7, 0.95, uh.F(var4, var9 * 0.6));
      }
   }

   static void Z(kj var0, Vec3 var1, double var2, double var4, double var6, double var8, int var10, double var11) {
      if (!(var2 < 0.0) && !(var2 > var8)) {
         double var13 = b(var2, var4, var6);
         double var15 = Math.pow(1.0 - var2 / var8, 1.5) * var11;
         if (!(var13 < 0.05) && !(var15 < 0.001)) {
            var0.F(var1.x, var1.y + 0.06, var1.z, var13, 0.6 + var13 * 0.06, uh.F(var10, var15 * 0.85));
            l(var0, var1, var13, 0.95 * (1.0 - var2 / var8 * 0.5), uh.F(var10, var15 * 0.6));
         }
      }
   }

   static void l(kj var0, Vec3 var1, double var2, double var4, int var6) {
      int var7 = var6 & 16777215;
      byte var8 = 72;
      double var9 = var1.y - 0.35;
      double var11 = var1.y + 0.08;
      double var13 = var1.y + var4;

      for (int var15 = 0; var15 < var8; var15++) {
         double var16 = var15 * Math.PI * 2.0 / var8;
         double var18 = (var15 + 1) * Math.PI * 2.0 / var8;
         double var20 = var1.x + Math.cos(var16) * var2;
         double var22 = var1.z + Math.sin(var16) * var2;
         double var24 = var1.x + Math.cos(var18) * var2;
         double var26 = var1.z + Math.sin(var18) * var2;
         var0.vertex(var20, var9, var22, var7);
         var0.vertex(var24, var9, var26, var7);
         var0.vertex(var24, var11, var26, var6);
         var0.vertex(var20, var11, var22, var6);
         var0.vertex(var20, var11, var22, var6);
         var0.vertex(var24, var11, var26, var6);
         var0.vertex(var24, var13, var26, var7);
         var0.vertex(var20, var13, var22, var7);
      }
   }

   static void l(kj var0, Vec3 var1, double var2, int var4) {
      if (var4 >>> 24 != 0 && !(var2 <= 0.001)) {
         int var5 = var4 & 16777215;
         byte var6 = 40;

         for (int var7 = 0; var7 < var6; var7++) {
            double var8 = var7 * Math.PI * 2.0 / var6;
            double var10 = (var7 + 1) * Math.PI * 2.0 / var6;
            var0.vertex(var1.x, var1.y, var1.z, var4);
            var0.vertex(var1.x + Math.cos(var8) * var2, var1.y, var1.z + Math.sin(var8) * var2, var5);
            var0.vertex(var1.x + Math.cos(var10) * var2, var1.y, var1.z + Math.sin(var10) * var2, var5);
            var0.vertex(var1.x, var1.y, var1.z, var4);
         }
      }
   }
}
