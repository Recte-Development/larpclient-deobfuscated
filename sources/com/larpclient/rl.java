package com.larpclient;

import java.util.Arrays;
import java.util.Comparator;
import net.minecraft.world.phys.Vec3;

public final class rl {
   public static final double jo = 0.625;
   public static final double N = 0.4;
   public static final double jh = 0.81;
   public static final double F = 1.0;
   public static final double P = 3.2;
   public static final double i = 6.0;
   public static final double S = 5.0;
   public static final double t = 6.0;
   public static final double w = 6.9;
   public static final double E = 8.0;
   public static final double jx = 1.45;
   public static final double jz = 1.15;
   public static final double f = 2.9;
   public static final double jK = 13.0;
   public static final double p = 16.0;
   public static final double Q = 0.24;
   public static final double O = 44.0;
   public static final int j_ = 220;
   public static final int jV = 280;
   public static final int B = 60;
   public static final int jr = 10;
   public static final int Z = 18;
   static final int G = 100;
   static final int V = 430;
   static final int n = 70;
   static final int Y = 72;
   private static final double H = 0.3;
   private static final double a = 0.55;
   private static final int jj = -1;
   private static final int q = -14018528;
   private final ez j4;
   private final double[] X;
   private final double[] R;
   private final double[] ja;
   private final double[] x;
   private final double[] b;
   private final double[] C;
   private final double[] g;
   private final double[] T;
   private final double[] I;
   private final double[] jH;
   private final double[] ji;
   private final double[] K;
   private final double[] k;
   private final double[] jO;
   private final double[] m;
   private final double[] d;
   private final double[] e;
   private final double[] s;
   private final double[] J;
   private final double[] u;
   private final double[] r;
   private final double[] M;
   private final double[] A;
   private final double[] z;
   private final double[] W;
   private final double[] L;
   private final double[] o;
   private final double[] h;
   private final double[] y;
   private final double[] U;
   private final double[] jw;
   private final double[] D;
   private final double[] l;
   private final double[] c;
   private final double[] v;
   private final byte[] j;
   private int jm;
   public static final double jM = 2.1;

   rl(ez var1, ur var2) {
      this.j4 = var1;
      this.X = new double[220];
      this.R = new double[220];
      this.ja = new double[220];
      this.x = new double[220];
      this.b = new double[220];
      this.C = new double[220];

      for (int var3 = 0; var3 < 220; var3++) {
         double var4 = Math.sqrt(1.6 + 110.76 * var1.L(var3 + 2000));
         double var6 = var1.L(var3 + 2200) * Math.PI * 2.0;
         this.ja[var3] = var4;
         this.X[var3] = Math.cos(var6) * var4;
         this.R[var3] = Math.sin(var6) * var4;
         this.x[var3] = var1.L(var3 + 2400) * Math.PI * 2.0;
         this.b[var3] = 0.28 + 0.18 * var1.L(var3 + 2600);
         this.C[var3] = var1.L(var3 + 2800);
      }

      short var37 = 340;
      this.g = new double[var37];
      this.T = new double[var37];
      this.I = new double[var37];
      this.jH = new double[var37];
      this.ji = new double[var37];
      this.K = new double[var37];
      this.k = new double[var37];
      this.jO = new double[var37];
      this.m = new double[var37];
      this.d = new double[var37];
      this.e = new double[var37];
      this.s = new double[var37];
      this.J = new double[var37];
      this.u = new double[var37];
      this.r = new double[var37];
      this.M = new double[var37];
      this.A = new double[var37];
      this.z = new double[var37];
      this.W = new double[var37];
      this.L = new double[var37];
      this.o = new double[var37];
      this.h = new double[var37];
      double var38 = Math.cos(var1.A);
      double var39 = Math.sin(var1.A);

      for (int var8 = 0; var8 < var37; var8++) {
         double var9 = var1.L(var8 + 3000) * 2.0 - 1.0;
         double var11 = var1.L(var8 + 3200) * Math.PI * 2.0;
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
         this.g[var8] = var25 / var31;
         this.T[var8] = var27 / var31;
         this.I[var8] = var29 / var31;
         this.jH[var8] = var17 * this.I[var8] - var19 * this.T[var8];
         this.ji[var8] = var19 * this.g[var8] - var15 * this.I[var8];
         this.K[var8] = var15 * this.T[var8] - var17 * this.g[var8];
         this.k[var8] = var1.L(var8 + 3400) * Math.PI * 2.0;
         this.jO[var8] = (1.0 + 0.9 * var1.L(var8 + 3600)) * (var1.L(var8 + 3800) < 0.5 ? -1 : 1);
         this.m[var8] = 0.82 + 0.26 * var1.L(var8 + 4000);
         if (var8 < 100) {
            double var33 = 0.1 + 1.6 * var1.L(var8 + 4200);
            double var35 = (var1.L(var8 + 4400) - 0.5) * 0.3;
            this.d[var8] = 0.4 + 0.45 * var8 / 100.0;
            this.e[var8] = var38 * var33 - var39 * var35;
            this.J[var8] = var39 * var33 + var38 * var35;
            this.s[var8] = 0.2 + 0.15 * var1.L(var8 + 4600);
         } else if (var8 < 280) {
            double var54 = 0.4 + 1.6 * var1.L(var8 + 4200);
            double var55 = var1.L(var8 + 4400) * Math.PI * 2.0;
            this.d[var8] = 0.75 + 0.75 * (var8 - 100) / 180.0;
            this.e[var8] = Math.cos(var55) * var54;
            this.J[var8] = Math.sin(var55) * var54;
            this.s[var8] = 0.04;
         } else {
            this.d[var8] = 3.2;
         }

         this.u[var8] = 1.6 + 3.4 * var1.L(var8 + 4800);
         this.r[var8] = 0.5 + 2.4 * Math.pow(var1.L(var8 + 5000), 1.5);
         this.M[var8] = 1.3 + 1.0 * var1.L(var8 + 5200);
         this.A[var8] = 0.2 + 0.7 * var1.L(var8 + 5400);
         this.z[var8] = 1.5 + 1.5 * var1.L(var8 + 5600);
         this.W[var8] = var1.L(var8 + 5800) * Math.PI * 2.0;
         this.L[var8] = var1.L(var8 + 6000) * Math.PI * 2.0;
         this.o[var8] = 1.4 + 2.2 * var1.L(var8 + 6200);
         this.h[var8] = var8 >= 280 ? 1.0 : var1.L(var8 + 6400) * 0.7;
      }

      short var40 = 572;
      this.y = new double[var40];
      this.U = new double[var40];
      this.jw = new double[var40];
      this.D = new double[var40];
      this.l = new double[var40];
      this.c = new double[var40];
      this.v = new double[var40];
      this.j = new byte[var40];

      for (int var41 = 0; var41 < 430; var41++) {
         double var10 = 0.965 * Math.sqrt((var41 + 0.5) / 430.0);
         double var12 = var41 * 2.399963229728653;
         this.O(
            Math.cos(var12) * var10,
            Math.sin(var12) * var10,
            N(Math.cos(var12) * var10, Math.sin(var12) * var10) ? 0 : 1,
            var41 == 0 ? 0.2 : 0.2 + 0.05 * var1.L(var41 + 7000),
            0.0,
            0.0,
            var1.L(var41 + 7200)
         );
      }

      for (int var42 = 0; var42 < 70; var42++) {
         double var45 = (var42 + 0.3 * var1.L(var42 + 7400)) * Math.PI * 2.0 / 70.0;
         double var50 = 0.975 + 0.065 * var1.L(var42 + 7600);
         this.O(Math.cos(var45) * var50, Math.sin(var45) * var50, 2, 0.17 + 0.03 * var1.L(var42 + 7800), 0.0, 0.0, var1.L(var42 + 8000));
      }

      for (int var43 = 0; var43 < 72; var43++) {
         double var46 = var1.L(var43 + 8200) * Math.PI * 2.0;
         double var51 = 1.05 + 0.45 * Math.sqrt(var1.L(var43 + 8400));
         this.O(
            Math.cos(var46) * var51,
            Math.sin(var46) * var51,
            3,
            0.2 + 0.1 * var1.L(var43 + 8600),
            0.22 * Math.pow(var1.L(var43 + 8800), 2.0),
            0.6 * var1.L(var43 + 9000),
            var1.L(var43 + 9200)
         );
      }

      Integer[] var44 = new Integer[220];

      for (int var47 = 0; var47 < 220; var47++) {
         var44[var47] = var47;
      }

      Arrays.sort(var44, Comparator.comparingDouble(var1x -> this.ja[var1x]));
      var2.I(var1.D.x, var1.D.z);
      var2.U(var1.D.x, var1.D.z, 2.0);
      Integer[] var48 = var44;
      int var49 = var48.length;

      for (int var52 = 0; var52 < var49; var52++) {
         int var53 = var48[var52];
         var2.I(var1.D.x + this.X[var53], var1.D.z + this.R[var53]);
      }

      var2.U(var1.D.x, var1.D.z, 4.75);
   }

   public int t() {
      return this.jm;
   }

   public int s(int var1) {
      return this.j[var1];
   }

   public double p(int var1) {
      return this.ja[var1];
   }

   private void O(double var1, double var3, int var5, double var6, double var8, double var10, double var12) {
      int var14 = this.jm++;
      this.y[var14] = var1;
      this.U[var14] = var3;
      this.j[var14] = (byte)var5;
      this.D[var14] = var6;
      this.l[var14] = var8;
      this.c[var14] = var10;
      this.jw[var14] = var12 * Math.PI * 2.0;
      this.v[var14] = this.j4.L(var14 + 9400);
   }

   public static boolean N(double var0, double var2) {
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

   public static ez.q y(ez var0, int var1, double var2) {
      double var4 = Math.min(1.0, Math.sqrt(Math.max(0.0, var2) * 1.6));
      double var6 = var4 * Math.PI / 2.0;
      double var8 = ez.k(var1, var0.d) / 16.0 * 0.9375;
      double var10 = (24.0 - ez.X[var1][1]) / 16.0 * 0.9375;
      double var12 = var8 * Math.cos(var6) + var10 * Math.sin(var6);
      double var14 = -var8 * Math.sin(var6) + var10 * Math.cos(var6) + 0.24 * Math.sin(var6);
      double var16 = var0.D.y + (var0.g().X() - var0.D.y) * ez.N(var2 / 0.45);
      int var18 = new int[]{4, 2, 0, 3, 5, 1}[var1];
      double var19 = ez.N((var2 - 0.4 - var18 * 0.025) / 0.28);
      return new ez.q(
         new Vec3(var0.D.x + var12 * Math.cos(var0.A), var16 + var14, var0.D.z + var12 * Math.sin(var0.A)), 0.0, 0.0, var6, 1.0, 1.0, var19, var4 >= 1.0
      );
   }

   public static double r(double var0, double var2, double var4, double var6) {
      double var8 = Math.clamp(var0 / var4, 0.0, 0.999);
      return var2 + var6 * (1.0 - Math.cbrt(1.0 - var8));
   }

   private static double o(double var0, double var2, double var4) {
      double var6 = Math.clamp(var0 / var4, 0.0, 1.0);
      return var2 * (1.0 - (1.0 - var6) * (1.0 - var6) * (1.0 - var6));
   }

   public double E(double var1) {
      double var3 = 0.5 + 0.5 * ez.N((var1 - 0.45) / 1.05);
      return 1.15 * var3 * (1.0 - 0.24 * ez.N((var1 - 2.6500000000000004) / 0.55));
   }

   public Vec3 i(int var1, double var2) {
      double var4 = this.E(var2) * this.m[var1];
      double var6 = this.k[var1] + this.jO[var1] * var2;
      double var8 = Math.cos(var6);
      double var10 = Math.sin(var6);
      double var12 = (this.g[var1] * var8 + this.jH[var1] * var10) * var4;
      double var14 = (this.T[var1] * var8 + this.ji[var1] * var10) * var4;
      double var16 = (this.I[var1] * var8 + this.K[var1] * var10) * var4;
      double var18 = 0.55 * var2;
      double var20 = Math.cos(var18);
      double var22 = Math.sin(var18);
      return new Vec3(var12 * var20 - var16 * var22, 1.45 + var14, var12 * var22 + var16 * var20);
   }

   public Vec3 z(int var1, double var2) {
      if (var2 < this.d[var1]) {
         return null;
      }

      if (var2 < 3.2) {
         Vec3 var28 = this.i(var1, var2);
         double var5 = ez.N((var2 - this.d[var1]) / 0.55);
         if (var5 >= 1.0) {
            return var28;
         }

         double var7 = Math.sin(var5 * Math.PI);
         return new Vec3(this.e[var1], this.s[var1], this.J[var1]).lerp(var28, var5).add(-var28.z * var7 * 0.18, var7 * 0.35, var28.x * var7 * 0.18);
      } else {
         double var4 = var2 - 3.2;
         double var6;
         double var8;
         double var10;
         if (var1 < 280) {
            Vec3 var12 = this.i(var1, 3.2);
            var6 = Math.hypot(var12.x, var12.z);
            var8 = Math.atan2(var12.z, var12.x);
            var10 = var12.y;
         } else {
            var6 = 0.08;
            var8 = this.W[var1];
            var10 = 1.45 + (this.L[var1] / Math.PI - 1.0) * 0.3;
         }

         if (var4 >= this.M[var1] + 0.5) {
            return null;
         }

         double var29 = 1.0 - Math.exp(-var4 * 5.0);
         double var14 = ez.N((var4 - 0.45) / (this.M[var1] - 0.45));
         double var16 = var6 + (this.u[var1] - var6) * var29;
         double var18 = var10 + this.r[var1] * var29;
         double var20 = var16 + (this.A[var1] - var16) * var14;
         double var22 = var18 + (0.04 - var18) * Math.pow(var14, 1.3);
         double var24 = Math.min(var4, this.M[var1]);
         double var26 = var8 + this.z[var1] * (var24 + 0.8 * var14 * var24);
         return new Vec3(Math.cos(var26) * var20, var22, Math.sin(var26) * var20);
      }
   }

   public static void L(r6 var0, ez var1, double var2, int var4, int var5, double var6, boolean var8) {
      rl var9 = var1.u();
      if (var9 != null) {
         var9.J(var0, var2, var4, var5, var6, var8);
      }
   }

   private void J(r6 var1, double var2, int var4, int var5, double var6, boolean var8) {
      ur var9 = this.j4.g();
      Vec3 var10 = new Vec3(this.j4.D.x, var9.X(), this.j4.D.z);
      Vec3 var11 = var10.add(0.0, 1.45, 0.0);
      int var12 = oh.j(var5, -1, 0.55);
      double var13 = ez.n(var2, 0.0, 0.42) * var6;
      o(var1, this.j4.D.add(0.0, 0.9, 0.0), 0.45 + var2 * 1.6, oh.S(var12, var13));
      m(var1, var10, var2, var12, var6);
      double var15 = ez.N((var2 - 1.0) / 0.35);
      double var17 = 1.0 - ez.N((var2 - 5.0) / 1.0);
      double var19 = 1.0 + 0.45 * ez.n(var2, 3.2, 3.9000000000000004);
      double var21 = var15 * var17 * var6 * (0.93 + 0.07 * Math.sin(var2 * 9.0));
      if (var21 > 0.001) {
         double var23 = 44.0 * ez.N((var2 - 1.0) / 0.5);
         double var25 = (0.16 + 0.1 * var17) * var19;
         Vec3 var27 = var10.add(0.0, -0.1, 0.0);
         Vec3 var28 = var10.add(0.0, Math.min(var23, 3.5), 0.0);
         Vec3 var29 = var10.add(0.0, var23, 0.0);
         int var30 = oh.j(var12, var4, 0.35);
         double[] var31 = new double[]{8.0, 4.5, 2.2, 1.0};
         double[] var32 = new double[]{0.035, 0.09, 0.3, 1.0};
         int[] var33 = new int[]{var30, var12, var12, -1};

         for (int var34 = 0; var34 < var31.length; var34++) {
            double var35 = var25 * var31[var34];
            double var37 = var21 * var32[var34];
            var1.ribbon(var27, var28, var35, oh.S(var33[var34], var37), oh.S(var33[var34], var37 * 0.9));
            var1.ribbon(var28, var29, var35, oh.S(var33[var34], var37 * 0.9), oh.S(var33[var34], 0.0));
         }

         o(var1, var10.add(0.0, 0.08, 0.0), 0.7 * var19, oh.S(var12, var21 * 0.55));
      }

      double var42 = ez.N((var2 - 0.6) / 0.8) * (1.0 - ez.N((var2 - 3.2) / 0.15)) * var6;
      if (var42 > 0.001) {
         double var43 = ez.N((var2 - 2.6500000000000004) / 0.55);
         o(var1, var11, this.E(var2) * 1.7, oh.S(var4, var42 * (0.2 + 0.15 * var43)));
         o(var1, var11, 0.4 + 0.25 * var43, oh.S(-1, var42 * (0.35 + 0.45 * var43)));
      }

      double var44 = ez.N((var2 - 1.3) / 0.6) * (1.0 - ez.N((var2 - 2.9000000000000004) / 0.3)) * var6;

      for (int var45 = 0; var45 < 10 && var44 > 0.001; var45++) {
         double var47 = this.j4.L(var45 + 9600) * Math.PI * 2.0 + var2 * 0.15;
         double var50 = -0.15 + 0.75 * this.j4.L(var45 + 9700);
         double var53 = 4.0 + 3.0 * this.j4.L(var45 + 9800);
         double var56 = 0.6 + 0.4 * Math.sin(var2 * 1.7 + var45 * 2.1);
         Vec3 var36 = var11.add(Math.cos(var47) * Math.cos(var50) * var53, Math.sin(var50) * var53, Math.sin(var47) * Math.cos(var50) * var53);
         var1.p(var11.x, var11.y, var11.z, var36.x, var36.y, var36.z, 0.05, 0.45, oh.S(var12, var44 * var56 * 0.3), oh.S(var12, 0.0));
      }

      double var46 = var2 - 3.2;
      if (var46 >= 0.0) {
         double var48 = ez.n(var46, 0.0, 0.55) * var6;
         o(var1, var11, 1.1 + var46 * 2.4, oh.S(-1, var48));
         o(var1, var10.add(0.0, 0.1, 0.0), 1.6 + var46 * 3.0, oh.S(var12, var48 * 0.8));
         double var51 = ez.n(var46, 0.0, 0.85) * var6;

         for (int var54 = 0; var54 < 18 && var51 > 0.001; var54++) {
            double var57 = (var54 + 0.5 * this.j4.L(var54 + 9900)) * Math.PI * 2.0 / 18.0;
            double var58 = 0.05 + 0.85 * this.j4.L(var54 + 10000);
            double var38 = (5.0 + 4.0 * this.j4.L(var54 + 10100)) * ez.N(var46 / 0.18);
            Vec3 var40 = var10.add(0.0, 0.15, 0.0);
            Vec3 var41 = var40.add(Math.cos(var57) * Math.cos(var58) * var38, Math.sin(var58) * var38, Math.sin(var57) * Math.cos(var58) * var38);
            var1.p(var40.x, var40.y, var40.z, var41.x, var41.y, var41.z, 0.14, 0.03, oh.S(-1, var51 * 0.7), oh.S(var12, 0.0));
         }

         y(var1, var10, var46 - 0.02, 16.0, 1.3, 1.4, var12, var6);
      }

      double var49 = ez.N((var2 - 4.300000000000001) / 1.0) * (1.0 - ez.N((var2 - 5.0 - 0.4) / 0.6));
      if (var49 > 0.001) {
         y(var1, var10.add(0.0, 0.05, 0.0), 0.7 + 0.35 * var49, oh.S(var12, var49 * 0.8 * var6));
      }

      double var52 = ez.N((var2 - 5.0) / 0.8) * (1.0 - ez.N((var2 - 6.9) / 1.0999999999999996));
      double var55 = this.n(var2);
      if (var52 > 0.001) {
         var1.C(var10.x, var10.y + 0.05, var10.z, var55, 0.3, oh.S(var12, var52 * 0.55 * var6));
         y(var1, var10.add(0.0, 0.045, 0.0), var55 * 1.05, oh.S(var12, var52 * 0.14 * var6));
      }

      var1.blend(var8 ? fk.h.u : fk.h.z);
      if (!var8) {
         this.L(var1, var9, var2, var4, var5);
      }

      this.B(var1, var10, var2, var4, var5, var8 ? 0.35 * var6 : 1.0);
      this.f(var1, var9, var10, var2, var4, var5, var8 ? 0.4 * var6 : 1.0, var8);
      var1.blend(fk.h.u);
   }

   public double n(double var1) {
      double var3 = Math.clamp((var1 - 5.0) / 1.0, 0.0, 1.0);
      return 2.9 * (0.25 + 0.75 * (1.0 - (1.0 - var3) * (1.0 - var3) * (1.0 - var3)));
   }

   private void L(r6 var1, ur var2, double var3, int var5, int var6) {
      for (int var7 = 0; var7 < 220; var7++) {
         double var8 = ez.N((var3 - r(this.ja[var7], 0.06, 13.0, 1.5)) / 0.3);
         if (!(var8 <= 0.0)) {
            double var10 = ez.N((var3 - r(this.ja[var7], 3.22, 16.0, 1.3)) / 0.4);
            if (!(var10 >= 1.0)) {
               double var12 = this.j4.D.x + this.X[var7];
               double var14 = this.j4.D.z + this.R[var7];
               double var16 = var2.L(var12, var14);
               if (!Double.isNaN(var16)) {
                  double var18 = this.X[var7] / this.ja[var7];
                  double var20 = this.R[var7] / this.ja[var7];
                  double var22 = (1.0 - var8) * -0.7 + var10 * 1.1;
                  double var24 = (1.0 - var8) * 0.4 + var10 * 0.6;
                  double var26 = (1.0 - var8) * 0.9 + var10 * 1.4 + 0.08 * Math.sin(this.x[var7] * 3.0);
                  int var28 = oh.j(oh.j(var5, -2065764, this.C[var7] * 0.3), var6, Math.max(0.0, this.C[var7] - 0.55));
                  k(
                     var1,
                     var12 + var18 * var22,
                     var16 + 0.03 + var7 * 3.0E-4 + var24,
                     var14 + var20 * var22,
                     this.x[var7] + var10 * 2.0,
                     var26,
                     this.b[var7] * Math.sqrt(var8),
                     oh.S(var28, (1.0 - var10) * 0.97)
                  );
               }
            }
         }
      }
   }

   private void B(r6 var1, Vec3 var2, double var3, int var5, int var6, double var7) {
      short var9 = 340;

      for (int var10 = 0; var10 < var9; var10++) {
         Vec3 var11 = this.z(var10, var3);
         if (var11 != null) {
            double var12 = Math.min(1.0, 0.55 + (var3 - this.d[var10]) * 1.6);
            double var14 = 1.0;
            if (var3 >= 3.2) {
               double var16 = 3.2 + this.M[var10];
               var14 = 1.0 - ez.N((var3 - var16) / 0.5);
               if (var10 >= 280) {
                  var12 = Math.min(1.0, (var3 - 3.2) * 6.0);
               }
            }

            double var54 = (var10 >= 280 ? 0.065 : 0.12) * var12;
            int var18 = oh.j(var5, var10 >= 280 ? -1 : var6, this.h[var10]);
            int var19 = var3 >= 3.2 + this.M[var10] ? 1 : 0;
            double var20 = var19 != 0 ? 3.2 + this.M[var10] : var3;
            double var22 = this.W[var10] + var20 * this.o[var10];
            double var24 = this.L[var10] + var20 * this.o[var10] * 0.7;
            double var26 = var20 * this.o[var10] * 1.3;
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
            var1.s(
               var2.x + var11.x,
               var2.y + var11.y,
               var2.z + var11.z,
               var44 * var52,
               var46 * var52,
               var48 * var52,
               var28 * var50,
               var30 * var50,
               var32 * var50,
               oh.S(var18, var14 * var7),
               f4.a.x
            );
         }
      }
   }

   private void f(r6 var1, ur var2, Vec3 var3, double var4, int var6, int var7, double var8, boolean var10) {
      if (!(var4 < 5.0)) {
         double var11 = this.n(var4);
         double var13 = this.j4.A + 0.3 * (var4 - 5.0);
         double var15 = Math.cos(var13);
         double var17 = Math.sin(var13);
         int var19 = oh.j(var7, -1, 0.7);

         for (int var20 = 0; var20 < this.jm; var20++) {
            byte var21 = this.j[var20];
            if (!var10 || var21 != 1 && var21 != 3) {
               double var22 = Math.hypot(this.y[var20], this.U[var20]);
               double var24 = ez.N((var4 - 5.0 - 0.5 * Math.min(var22, 1.2) - 0.08 * this.v[var20]) / 0.35);
               if (!(var24 <= 0.0)) {
                  double var26 = ez.N((var4 - 6.9 - 0.3 * this.v[var20]) / 0.8);
                  if (!(var26 >= 1.0)) {
                     double var28 = this.y[var20] * var11;
                     double var30 = this.U[var20] * var11;
                     double var32 = var28 * var15 - var30 * var17;
                     double var34 = var28 * var17 + var30 * var15;
                     double var36 = 1.0 + var26 * 0.5 / Math.max(0.3, var22 * var11);
                     var32 *= var36;
                     var34 *= var36;
                     double var38 = var2.L(var3.x + var32, var3.z + var34);
                     if (!Double.isNaN(var38)) {
                        double var40 = var21 == 3 ? 0.025 * Math.sin(var4 * 2.0 + this.jw[var20] * 4.0) * var24 : 0.0;
                        double var42 = var38 + 0.035 + var21 * 0.004 + var20 % 7 * 6.0E-4 + this.l[var20] * var24 + var40 + var26 * 0.35;
                        double var44 = this.D[var20] * Math.max(0.6, var11 / 2.9) * Math.sqrt(var24);
                        double var46 = (1.0 - var26) * var24;

                        int var48 = switch (var21) {
                           case 0, 2 -> oh.S(var19, var46 * var8);
                           case 1 -> oh.S(-14018528, var46 * 0.28);
                           default -> oh.S(oh.j(var6, var7, this.v[var20] * 0.5), var46 * var8);
                        };
                        k(var1, var3.x + var32, var42, var3.z + var34, this.jw[var20] + var13, this.c[var20] + var26 * 0.8, var44, var48);
                     }
                  }
               }
            }
         }
      }
   }

   private static void k(r6 var0, double var1, double var3, double var5, double var7, double var9, double var11, int var13) {
      if (var13 >>> 24 != 0 && !(var11 <= 0.001)) {
         double var14 = Math.cos(var7);
         double var16 = Math.sin(var7);
         double var18 = Math.cos(var9);
         double var20 = Math.sin(var9);
         double var22 = var11;
         double var24 = var11 * 0.74;
         var0.s(
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
            f4.a.x
         );
      }
   }

   private static void o(r6 var0, Vec3 var1, double var2, int var4) {
      if (var4 >>> 24 > 0) {
         var0.j(var1.x, var1.y, var1.z, var2, 0.0, var4, f4.a.u, 0.0, 0.0, 0.0);
      }
   }

   public static double Z(double var0) {
      if (var0 < 0.06 || var0 >= 3.2) {
         return 0.0;
      }

      if (var0 < 2.1) {
         return o(var0 - 0.06, 13.0, 1.5);
      }

      double var2 = (var0 - 2.1) / 1.1;
      return 13.0 * (1.0 - var2 * var2);
   }

   private static void m(r6 var0, Vec3 var1, double var2, int var4, double var5) {
      double var7 = Z(var2);
      if (!(var7 < 0.05)) {
         double var9;
         if (var2 < 1.6) {
            var9 = 1.0 - 0.55 * ez.N((var2 - 0.06) / 1.5);
         } else if (var2 < 2.1) {
            var9 = 0.45 + 0.08 * Math.sin((var2 - 1.6) * 4.0);
         } else {
            var9 = 0.45 + 0.55 * ez.N((var2 - 2.1) / 1.1);
         }

         var9 *= var5;
         var0.C(var1.x, var1.y + 0.06, var1.z, var7, 0.6 + var7 * 0.06, oh.S(var4, var9 * 0.85));
         A(var0, var1, var7, 0.95, oh.S(var4, var9 * 0.6));
      }
   }

   static void y(r6 var0, Vec3 var1, double var2, double var4, double var6, double var8, int var10, double var11) {
      if (!(var2 < 0.0) && !(var2 > var8)) {
         double var13 = o(var2, var4, var6);
         double var15 = Math.pow(1.0 - var2 / var8, 1.5) * var11;
         if (!(var13 < 0.05) && !(var15 < 0.001)) {
            var0.C(var1.x, var1.y + 0.06, var1.z, var13, 0.6 + var13 * 0.06, oh.S(var10, var15 * 0.85));
            A(var0, var1, var13, 0.95 * (1.0 - var2 / var8 * 0.5), oh.S(var10, var15 * 0.6));
         }
      }
   }

   static void A(r6 var0, Vec3 var1, double var2, double var4, int var6) {
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

   static void y(r6 var0, Vec3 var1, double var2, int var4) {
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
