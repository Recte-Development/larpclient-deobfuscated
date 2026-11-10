package com.larpclient;

import java.util.Arrays;
import java.util.Comparator;
import net.minecraft.class_243;

public final class g2 {
   public static final double gG = 0.625;
   public static final double d = 0.4;
   public static final double R = 0.81;
   public static final double I = 1.0;
   public static final double v = 3.2;
   public static final double s = 6.0;
   public static final double e = 5.0;
   public static final double y = 6.0;
   public static final double Q = 6.9;
   public static final double gM = 8.0;
   public static final double g_ = 1.45;
   public static final double W = 1.15;
   public static final double F = 2.9;
   public static final double gQ = 13.0;
   public static final double u = 16.0;
   public static final double gw = 0.24;
   public static final double V = 44.0;
   public static final int C = 220;
   public static final int Y = 280;
   public static final int r = 60;
   public static final int w = 10;
   public static final int c = 18;
   static final int J = 100;
   static final int k = 430;
   static final int gZ = 70;
   static final int Z = 72;
   private static final double gq = 0.3;
   private static final double P = 0.55;
   private static final int M = -1;
   private static final int E = -14018528;
   private final u8 x;
   private final double[] T;
   private final double[] t;
   private final double[] K;
   private final double[] A;
   private final double[] n;
   private final double[] gD;
   private final double[] N;
   private final double[] D;
   private final double[] gd;
   private final double[] z;
   private final double[] o;
   private final double[] gz;
   private final double[] j;
   private final double[] gn;
   private final double[] B;
   private final double[] G;
   private final double[] i;
   private final double[] gA;
   private final double[] U;
   private final double[] h;
   private final double[] S;
   private final double[] b;
   private final double[] gf;
   private final double[] X;
   private final double[] O;
   private final double[] m;
   private final double[] g;
   private final double[] H;
   private final double[] g3;
   private final double[] p;
   private final double[] g4;
   private final double[] a;
   private final double[] L;
   private final double[] gI;
   private final double[] q;
   private final byte[] l;
   private int gV;
   public static final double f = 2.1;

   g2(u8 var1, f7 var2) {
      this.x = var1;
      this.T = new double[220];
      this.t = new double[220];
      this.K = new double[220];
      this.A = new double[220];
      this.n = new double[220];
      this.gD = new double[220];

      for (int var3 = 0; var3 < 220; var3++) {
         double var4 = Math.sqrt(1.6 + 110.76 * var1.P(var3 + 2000));
         double var6 = var1.P(var3 + 2200) * Math.PI * 2.0;
         this.K[var3] = var4;
         this.T[var3] = Math.cos(var6) * var4;
         this.t[var3] = Math.sin(var6) * var4;
         this.A[var3] = var1.P(var3 + 2400) * Math.PI * 2.0;
         this.n[var3] = 0.28 + 0.18 * var1.P(var3 + 2600);
         this.gD[var3] = var1.P(var3 + 2800);
      }

      short var37 = 340;
      this.N = new double[var37];
      this.D = new double[var37];
      this.gd = new double[var37];
      this.z = new double[var37];
      this.o = new double[var37];
      this.gz = new double[var37];
      this.j = new double[var37];
      this.gn = new double[var37];
      this.B = new double[var37];
      this.G = new double[var37];
      this.i = new double[var37];
      this.gA = new double[var37];
      this.U = new double[var37];
      this.h = new double[var37];
      this.S = new double[var37];
      this.b = new double[var37];
      this.gf = new double[var37];
      this.X = new double[var37];
      this.O = new double[var37];
      this.m = new double[var37];
      this.g = new double[var37];
      this.H = new double[var37];
      double var38 = Math.cos(var1.y);
      double var39 = Math.sin(var1.y);

      for (int var8 = 0; var8 < var37; var8++) {
         double var9 = var1.P(var8 + 3000) * 2.0 - 1.0;
         double var11 = var1.P(var8 + 3200) * Math.PI * 2.0;
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
         this.N[var8] = var25 / var31;
         this.D[var8] = var27 / var31;
         this.gd[var8] = var29 / var31;
         this.z[var8] = var17 * this.gd[var8] - var19 * this.D[var8];
         this.o[var8] = var19 * this.N[var8] - var15 * this.gd[var8];
         this.gz[var8] = var15 * this.D[var8] - var17 * this.N[var8];
         this.j[var8] = var1.P(var8 + 3400) * Math.PI * 2.0;
         this.gn[var8] = (1.0 + 0.9 * var1.P(var8 + 3600)) * (var1.P(var8 + 3800) < 0.5 ? -1 : 1);
         this.B[var8] = 0.82 + 0.26 * var1.P(var8 + 4000);
         if (var8 < 100) {
            double var33 = 0.1 + 1.6 * var1.P(var8 + 4200);
            double var35 = (var1.P(var8 + 4400) - 0.5) * 0.3;
            this.G[var8] = 0.4 + 0.45 * var8 / 100.0;
            this.i[var8] = var38 * var33 - var39 * var35;
            this.U[var8] = var39 * var33 + var38 * var35;
            this.gA[var8] = 0.2 + 0.15 * var1.P(var8 + 4600);
         } else if (var8 < 280) {
            double var54 = 0.4 + 1.6 * var1.P(var8 + 4200);
            double var55 = var1.P(var8 + 4400) * Math.PI * 2.0;
            this.G[var8] = 0.75 + 0.75 * (var8 - 100) / 180.0;
            this.i[var8] = Math.cos(var55) * var54;
            this.U[var8] = Math.sin(var55) * var54;
            this.gA[var8] = 0.04;
         } else {
            this.G[var8] = 3.2;
         }

         this.h[var8] = 1.6 + 3.4 * var1.P(var8 + 4800);
         this.S[var8] = 0.5 + 2.4 * Math.pow(var1.P(var8 + 5000), 1.5);
         this.b[var8] = 1.3 + 1.0 * var1.P(var8 + 5200);
         this.gf[var8] = 0.2 + 0.7 * var1.P(var8 + 5400);
         this.X[var8] = 1.5 + 1.5 * var1.P(var8 + 5600);
         this.O[var8] = var1.P(var8 + 5800) * Math.PI * 2.0;
         this.m[var8] = var1.P(var8 + 6000) * Math.PI * 2.0;
         this.g[var8] = 1.4 + 2.2 * var1.P(var8 + 6200);
         this.H[var8] = var8 >= 280 ? 1.0 : var1.P(var8 + 6400) * 0.7;
      }

      short var40 = 572;
      this.g3 = new double[var40];
      this.p = new double[var40];
      this.g4 = new double[var40];
      this.a = new double[var40];
      this.L = new double[var40];
      this.gI = new double[var40];
      this.q = new double[var40];
      this.l = new byte[var40];

      for (int var41 = 0; var41 < 430; var41++) {
         double var10 = 0.965 * Math.sqrt((var41 + 0.5) / 430.0);
         double var12 = var41 * 2.399963229728653;
         this.b(
            Math.cos(var12) * var10,
            Math.sin(var12) * var10,
            U(Math.cos(var12) * var10, Math.sin(var12) * var10) ? 0 : 1,
            var41 == 0 ? 0.2 : 0.2 + 0.05 * var1.P(var41 + 7000),
            0.0,
            0.0,
            var1.P(var41 + 7200)
         );
      }

      for (int var42 = 0; var42 < 70; var42++) {
         double var45 = (var42 + 0.3 * var1.P(var42 + 7400)) * Math.PI * 2.0 / 70.0;
         double var50 = 0.975 + 0.065 * var1.P(var42 + 7600);
         this.b(Math.cos(var45) * var50, Math.sin(var45) * var50, 2, 0.17 + 0.03 * var1.P(var42 + 7800), 0.0, 0.0, var1.P(var42 + 8000));
      }

      for (int var43 = 0; var43 < 72; var43++) {
         double var46 = var1.P(var43 + 8200) * Math.PI * 2.0;
         double var51 = 1.05 + 0.45 * Math.sqrt(var1.P(var43 + 8400));
         this.b(
            Math.cos(var46) * var51,
            Math.sin(var46) * var51,
            3,
            0.2 + 0.1 * var1.P(var43 + 8600),
            0.22 * Math.pow(var1.P(var43 + 8800), 2.0),
            0.6 * var1.P(var43 + 9000),
            var1.P(var43 + 9200)
         );
      }

      Integer[] var44 = new Integer[220];

      for (int var47 = 0; var47 < 220; var47++) {
         var44[var47] = var47;
      }

      Arrays.sort(var44, Comparator.comparingDouble(var1x -> this.K[var1x]));
      var2.o(var1.z.field_1352, var1.z.field_1350);
      var2.t(var1.z.field_1352, var1.z.field_1350, 2.0);
      Integer[] var48 = var44;
      int var49 = var48.length;

      for (int var52 = 0; var52 < var49; var52++) {
         int var53 = var48[var52];
         var2.o(var1.z.field_1352 + this.T[var53], var1.z.field_1350 + this.t[var53]);
      }

      var2.t(var1.z.field_1352, var1.z.field_1350, 4.75);
   }

   public int y() {
      return this.gV;
   }

   public int N(int var1) {
      return this.l[var1];
   }

   public double g(int var1) {
      return this.K[var1];
   }

   private void b(double var1, double var3, int var5, double var6, double var8, double var10, double var12) {
      int var14 = this.gV++;
      this.g3[var14] = var1;
      this.p[var14] = var3;
      this.l[var14] = (byte)var5;
      this.a[var14] = var6;
      this.L[var14] = var8;
      this.gI[var14] = var10;
      this.g4[var14] = var12 * Math.PI * 2.0;
      this.q[var14] = this.x.P(var14 + 9400);
   }

   public static boolean U(double var0, double var2) {
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

   public static u8.s Y(u8 var0, int var1, double var2) {
      double var4 = Math.min(1.0, Math.sqrt(Math.max(0.0, var2) * 1.6));
      double var6 = var4 * Math.PI / 2.0;
      double var8 = u8.z(var1, var0.D) / 16.0 * 0.9375;
      double var10 = (24.0 - u8.E[var1][1]) / 16.0 * 0.9375;
      double var12 = var8 * Math.cos(var6) + var10 * Math.sin(var6);
      double var14 = -var8 * Math.sin(var6) + var10 * Math.cos(var6) + 0.24 * Math.sin(var6);
      double var16 = var0.z.field_1351 + (var0.x().x() - var0.z.field_1351) * u8.S(var2 / 0.45);
      int var18 = new int[]{4, 2, 0, 3, 5, 1}[var1];
      double var19 = u8.S((var2 - 0.4 - var18 * 0.025) / 0.28);
      return new u8.s(
         new class_243(var0.z.field_1352 + var12 * Math.cos(var0.y), var16 + var14, var0.z.field_1350 + var12 * Math.sin(var0.y)),
         0.0,
         0.0,
         var6,
         1.0,
         1.0,
         var19,
         var4 >= 1.0
      );
   }

   public static double n(double var0, double var2, double var4, double var6) {
      double var8 = Math.clamp(var0 / var4, 0.0, 0.999);
      return var2 + var6 * (1.0 - Math.cbrt(1.0 - var8));
   }

   private static double h(double var0, double var2, double var4) {
      double var6 = Math.clamp(var0 / var4, 0.0, 1.0);
      return var2 * (1.0 - (1.0 - var6) * (1.0 - var6) * (1.0 - var6));
   }

   public double V(double var1) {
      double var3 = 0.5 + 0.5 * u8.S((var1 - 0.45) / 1.05);
      return 1.15 * var3 * (1.0 - 0.24 * u8.S((var1 - 2.6500000000000004) / 0.55));
   }

   public class_243 Y(int var1, double var2) {
      double var4 = this.V(var2) * this.B[var1];
      double var6 = this.j[var1] + this.gn[var1] * var2;
      double var8 = Math.cos(var6);
      double var10 = Math.sin(var6);
      double var12 = (this.N[var1] * var8 + this.z[var1] * var10) * var4;
      double var14 = (this.D[var1] * var8 + this.o[var1] * var10) * var4;
      double var16 = (this.gd[var1] * var8 + this.gz[var1] * var10) * var4;
      double var18 = 0.55 * var2;
      double var20 = Math.cos(var18);
      double var22 = Math.sin(var18);
      return new class_243(var12 * var20 - var16 * var22, 1.45 + var14, var12 * var22 + var16 * var20);
   }

   public class_243 N(int var1, double var2) {
      if (var2 < this.G[var1]) {
         return null;
      }

      if (var2 < 3.2) {
         class_243 var28 = this.Y(var1, var2);
         double var5 = u8.S((var2 - this.G[var1]) / 0.55);
         if (var5 >= 1.0) {
            return var28;
         }

         double var7 = Math.sin(var5 * Math.PI);
         return new class_243(this.i[var1], this.gA[var1], this.U[var1])
            .method_35590(var28, var5)
            .method_1031(-var28.field_1350 * var7 * 0.18, var7 * 0.35, var28.field_1352 * var7 * 0.18);
      } else {
         double var4 = var2 - 3.2;
         double var6;
         double var8;
         double var10;
         if (var1 < 280) {
            class_243 var12 = this.Y(var1, 3.2);
            var6 = Math.hypot(var12.field_1352, var12.field_1350);
            var8 = Math.atan2(var12.field_1350, var12.field_1352);
            var10 = var12.field_1351;
         } else {
            var6 = 0.08;
            var8 = this.O[var1];
            var10 = 1.45 + (this.m[var1] / Math.PI - 1.0) * 0.3;
         }

         if (var4 >= this.b[var1] + 0.5) {
            return null;
         }

         double var29 = 1.0 - Math.exp(-var4 * 5.0);
         double var14 = u8.S((var4 - 0.45) / (this.b[var1] - 0.45));
         double var16 = var6 + (this.h[var1] - var6) * var29;
         double var18 = var10 + this.S[var1] * var29;
         double var20 = var16 + (this.gf[var1] - var16) * var14;
         double var22 = var18 + (0.04 - var18) * Math.pow(var14, 1.3);
         double var24 = Math.min(var4, this.b[var1]);
         double var26 = var8 + this.X[var1] * (var24 + 0.8 * var14 * var24);
         return new class_243(Math.cos(var26) * var20, var22, Math.sin(var26) * var20);
      }
   }

   public static void c(f1 var0, u8 var1, double var2, int var4, int var5, double var6, boolean var8) {
      g2 var9 = var1.H();
      if (var9 != null) {
         var9.p(var0, var2, var4, var5, var6, var8);
      }
   }

   private void p(f1 var1, double var2, int var4, int var5, double var6, boolean var8) {
      f7 var9 = this.x.x();
      class_243 var10 = new class_243(this.x.z.field_1352, var9.x(), this.x.z.field_1350);
      class_243 var11 = var10.method_1031(0.0, 1.45, 0.0);
      int var12 = ei.W(var5, -1, 0.55);
      double var13 = u8.o(var2, 0.0, 0.42) * var6;
      v(var1, this.x.z.method_1031(0.0, 0.9, 0.0), 0.45 + var2 * 1.6, ei.R(var12, var13));
      f(var1, var10, var2, var12, var6);
      double var15 = u8.S((var2 - 1.0) / 0.35);
      double var17 = 1.0 - u8.S((var2 - 5.0) / 1.0);
      double var19 = 1.0 + 0.45 * u8.o(var2, 3.2, 3.9000000000000004);
      double var21 = var15 * var17 * var6 * (0.93 + 0.07 * Math.sin(var2 * 9.0));
      if (var21 > 0.001) {
         double var23 = 44.0 * u8.S((var2 - 1.0) / 0.5);
         double var25 = (0.16 + 0.1 * var17) * var19;
         class_243 var27 = var10.method_1031(0.0, -0.1, 0.0);
         class_243 var28 = var10.method_1031(0.0, Math.min(var23, 3.5), 0.0);
         class_243 var29 = var10.method_1031(0.0, var23, 0.0);
         int var30 = ei.W(var12, var4, 0.35);
         double[] var31 = new double[]{8.0, 4.5, 2.2, 1.0};
         double[] var32 = new double[]{0.035, 0.09, 0.3, 1.0};
         int[] var33 = new int[]{var30, var12, var12, -1};

         for (int var34 = 0; var34 < var31.length; var34++) {
            double var35 = var25 * var31[var34];
            double var37 = var21 * var32[var34];
            var1.ribbon(var27, var28, var35, ei.R(var33[var34], var37), ei.R(var33[var34], var37 * 0.9));
            var1.ribbon(var28, var29, var35, ei.R(var33[var34], var37 * 0.9), ei.R(var33[var34], 0.0));
         }

         v(var1, var10.method_1031(0.0, 0.08, 0.0), 0.7 * var19, ei.R(var12, var21 * 0.55));
      }

      double var42 = u8.S((var2 - 0.6) / 0.8) * (1.0 - u8.S((var2 - 3.2) / 0.15)) * var6;
      if (var42 > 0.001) {
         double var43 = u8.S((var2 - 2.6500000000000004) / 0.55);
         v(var1, var11, this.V(var2) * 1.7, ei.R(var4, var42 * (0.2 + 0.15 * var43)));
         v(var1, var11, 0.4 + 0.25 * var43, ei.R(-1, var42 * (0.35 + 0.45 * var43)));
      }

      double var44 = u8.S((var2 - 1.3) / 0.6) * (1.0 - u8.S((var2 - 2.9000000000000004) / 0.3)) * var6;

      for (int var45 = 0; var45 < 10 && var44 > 0.001; var45++) {
         double var47 = this.x.P(var45 + 9600) * Math.PI * 2.0 + var2 * 0.15;
         double var50 = -0.15 + 0.75 * this.x.P(var45 + 9700);
         double var53 = 4.0 + 3.0 * this.x.P(var45 + 9800);
         double var56 = 0.6 + 0.4 * Math.sin(var2 * 1.7 + var45 * 2.1);
         class_243 var36 = var11.method_1031(Math.cos(var47) * Math.cos(var50) * var53, Math.sin(var50) * var53, Math.sin(var47) * Math.cos(var50) * var53);
         var1.g(
            var11.field_1352,
            var11.field_1351,
            var11.field_1350,
            var36.field_1352,
            var36.field_1351,
            var36.field_1350,
            0.05,
            0.45,
            ei.R(var12, var44 * var56 * 0.3),
            ei.R(var12, 0.0)
         );
      }

      double var46 = var2 - 3.2;
      if (var46 >= 0.0) {
         double var48 = u8.o(var46, 0.0, 0.55) * var6;
         v(var1, var11, 1.1 + var46 * 2.4, ei.R(-1, var48));
         v(var1, var10.method_1031(0.0, 0.1, 0.0), 1.6 + var46 * 3.0, ei.R(var12, var48 * 0.8));
         double var51 = u8.o(var46, 0.0, 0.85) * var6;

         for (int var54 = 0; var54 < 18 && var51 > 0.001; var54++) {
            double var57 = (var54 + 0.5 * this.x.P(var54 + 9900)) * Math.PI * 2.0 / 18.0;
            double var58 = 0.05 + 0.85 * this.x.P(var54 + 10000);
            double var38 = (5.0 + 4.0 * this.x.P(var54 + 10100)) * u8.S(var46 / 0.18);
            class_243 var40 = var10.method_1031(0.0, 0.15, 0.0);
            class_243 var41 = var40.method_1031(Math.cos(var57) * Math.cos(var58) * var38, Math.sin(var58) * var38, Math.sin(var57) * Math.cos(var58) * var38);
            var1.g(
               var40.field_1352,
               var40.field_1351,
               var40.field_1350,
               var41.field_1352,
               var41.field_1351,
               var41.field_1350,
               0.14,
               0.03,
               ei.R(-1, var51 * 0.7),
               ei.R(var12, 0.0)
            );
         }

         t(var1, var10, var46 - 0.02, 16.0, 1.3, 1.4, var12, var6);
      }

      double var49 = u8.S((var2 - 4.300000000000001) / 1.0) * (1.0 - u8.S((var2 - 5.0 - 0.4) / 0.6));
      if (var49 > 0.001) {
         L(var1, var10.method_1031(0.0, 0.05, 0.0), 0.7 + 0.35 * var49, ei.R(var12, var49 * 0.8 * var6));
      }

      double var52 = u8.S((var2 - 5.0) / 0.8) * (1.0 - u8.S((var2 - 6.9) / 1.0999999999999996));
      double var55 = this.H(var2);
      if (var52 > 0.001) {
         var1.f(var10.field_1352, var10.field_1351 + 0.05, var10.field_1350, var55, 0.3, ei.R(var12, var52 * 0.55 * var6));
         L(var1, var10.method_1031(0.0, 0.045, 0.0), var55 * 1.05, ei.R(var12, var52 * 0.14 * var6));
      }

      var1.blend(var8 ? ti.d.l : ti.d.G);
      if (!var8) {
         this.r(var1, var9, var2, var4, var5);
      }

      this.w(var1, var10, var2, var4, var5, var8 ? 0.35 * var6 : 1.0);
      this.r(var1, var9, var10, var2, var4, var5, var8 ? 0.4 * var6 : 1.0, var8);
      var1.blend(ti.d.l);
   }

   public double H(double var1) {
      double var3 = Math.clamp((var1 - 5.0) / 1.0, 0.0, 1.0);
      return 2.9 * (0.25 + 0.75 * (1.0 - (1.0 - var3) * (1.0 - var3) * (1.0 - var3)));
   }

   private void r(f1 var1, f7 var2, double var3, int var5, int var6) {
      for (int var7 = 0; var7 < 220; var7++) {
         double var8 = u8.S((var3 - n(this.K[var7], 0.06, 13.0, 1.5)) / 0.3);
         if (!(var8 <= 0.0)) {
            double var10 = u8.S((var3 - n(this.K[var7], 3.22, 16.0, 1.3)) / 0.4);
            if (!(var10 >= 1.0)) {
               double var12 = this.x.z.field_1352 + this.T[var7];
               double var14 = this.x.z.field_1350 + this.t[var7];
               double var16 = var2.H(var12, var14);
               if (!Double.isNaN(var16)) {
                  double var18 = this.T[var7] / this.K[var7];
                  double var20 = this.t[var7] / this.K[var7];
                  double var22 = (1.0 - var8) * -0.7 + var10 * 1.1;
                  double var24 = (1.0 - var8) * 0.4 + var10 * 0.6;
                  double var26 = (1.0 - var8) * 0.9 + var10 * 1.4 + 0.08 * Math.sin(this.A[var7] * 3.0);
                  int var28 = ei.W(ei.W(var5, -2065764, this.gD[var7] * 0.3), var6, Math.max(0.0, this.gD[var7] - 0.55));
                  O(
                     var1,
                     var12 + var18 * var22,
                     var16 + 0.03 + var7 * 3.0E-4 + var24,
                     var14 + var20 * var22,
                     this.A[var7] + var10 * 2.0,
                     var26,
                     this.n[var7] * Math.sqrt(var8),
                     ei.R(var28, (1.0 - var10) * 0.97)
                  );
               }
            }
         }
      }
   }

   private void w(f1 var1, class_243 var2, double var3, int var5, int var6, double var7) {
      short var9 = 340;

      for (int var10 = 0; var10 < var9; var10++) {
         class_243 var11 = this.N(var10, var3);
         if (var11 != null) {
            double var12 = Math.min(1.0, 0.55 + (var3 - this.G[var10]) * 1.6);
            double var14 = 1.0;
            if (var3 >= 3.2) {
               double var16 = 3.2 + this.b[var10];
               var14 = 1.0 - u8.S((var3 - var16) / 0.5);
               if (var10 >= 280) {
                  var12 = Math.min(1.0, (var3 - 3.2) * 6.0);
               }
            }

            double var54 = (var10 >= 280 ? 0.065 : 0.12) * var12;
            int var18 = ei.W(var5, var10 >= 280 ? -1 : var6, this.H[var10]);
            int var19 = var3 >= 3.2 + this.b[var10] ? 1 : 0;
            double var20 = var19 != 0 ? 3.2 + this.b[var10] : var3;
            double var22 = this.O[var10] + var20 * this.g[var10];
            double var24 = this.m[var10] + var20 * this.g[var10] * 0.7;
            double var26 = var20 * this.g[var10] * 1.3;
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
            var1.p(
               var2.field_1352 + var11.field_1352,
               var2.field_1351 + var11.field_1351,
               var2.field_1350 + var11.field_1350,
               var44 * var52,
               var46 * var52,
               var48 * var52,
               var28 * var50,
               var30 * var50,
               var32 * var50,
               ei.R(var18, var14 * var7),
               gd.h.e
            );
         }
      }
   }

   private void r(f1 var1, f7 var2, class_243 var3, double var4, int var6, int var7, double var8, boolean var10) {
      if (!(var4 < 5.0)) {
         double var11 = this.H(var4);
         double var13 = this.x.y + 0.3 * (var4 - 5.0);
         double var15 = Math.cos(var13);
         double var17 = Math.sin(var13);
         int var19 = ei.W(var7, -1, 0.7);

         for (int var20 = 0; var20 < this.gV; var20++) {
            byte var21 = this.l[var20];
            if (!var10 || var21 != 1 && var21 != 3) {
               double var22 = Math.hypot(this.g3[var20], this.p[var20]);
               double var24 = u8.S((var4 - 5.0 - 0.5 * Math.min(var22, 1.2) - 0.08 * this.q[var20]) / 0.35);
               if (!(var24 <= 0.0)) {
                  double var26 = u8.S((var4 - 6.9 - 0.3 * this.q[var20]) / 0.8);
                  if (!(var26 >= 1.0)) {
                     double var28 = this.g3[var20] * var11;
                     double var30 = this.p[var20] * var11;
                     double var32 = var28 * var15 - var30 * var17;
                     double var34 = var28 * var17 + var30 * var15;
                     double var36 = 1.0 + var26 * 0.5 / Math.max(0.3, var22 * var11);
                     var32 *= var36;
                     var34 *= var36;
                     double var38 = var2.H(var3.field_1352 + var32, var3.field_1350 + var34);
                     if (!Double.isNaN(var38)) {
                        double var40 = var21 == 3 ? 0.025 * Math.sin(var4 * 2.0 + this.g4[var20] * 4.0) * var24 : 0.0;
                        double var42 = var38 + 0.035 + var21 * 0.004 + var20 % 7 * 6.0E-4 + this.L[var20] * var24 + var40 + var26 * 0.35;
                        double var44 = this.a[var20] * Math.max(0.6, var11 / 2.9) * Math.sqrt(var24);
                        double var46 = (1.0 - var26) * var24;

                        int var48 = switch (var21) {
                           case 0, 2 -> ei.R(var19, var46 * var8);
                           case 1 -> ei.R(-14018528, var46 * 0.28);
                           default -> ei.R(ei.W(var6, var7, this.q[var20] * 0.5), var46 * var8);
                        };
                        O(var1, var3.field_1352 + var32, var42, var3.field_1350 + var34, this.g4[var20] + var13, this.gI[var20] + var26 * 0.8, var44, var48);
                     }
                  }
               }
            }
         }
      }
   }

   private static void O(f1 var0, double var1, double var3, double var5, double var7, double var9, double var11, int var13) {
      if (var13 >>> 24 != 0 && !(var11 <= 0.001)) {
         double var14 = Math.cos(var7);
         double var16 = Math.sin(var7);
         double var18 = Math.cos(var9);
         double var20 = Math.sin(var9);
         double var22 = var11;
         double var24 = var11 * 0.74;
         var0.p(
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
            gd.h.e
         );
      }
   }

   private static void v(f1 var0, class_243 var1, double var2, int var4) {
      if (var4 >>> 24 > 0) {
         var0.U(var1.field_1352, var1.field_1351, var1.field_1350, var2, 0.0, var4, gd.h.f, 0.0, 0.0, 0.0);
      }
   }

   public static double M(double var0) {
      if (var0 < 0.06 || var0 >= 3.2) {
         return 0.0;
      }

      if (var0 < 2.1) {
         return h(var0 - 0.06, 13.0, 1.5);
      }

      double var2 = (var0 - 2.1) / 1.1;
      return 13.0 * (1.0 - var2 * var2);
   }

   private static void f(f1 var0, class_243 var1, double var2, int var4, double var5) {
      double var7 = M(var2);
      if (!(var7 < 0.05)) {
         double var9;
         if (var2 < 1.6) {
            var9 = 1.0 - 0.55 * u8.S((var2 - 0.06) / 1.5);
         } else if (var2 < 2.1) {
            var9 = 0.45 + 0.08 * Math.sin((var2 - 1.6) * 4.0);
         } else {
            var9 = 0.45 + 0.55 * u8.S((var2 - 2.1) / 1.1);
         }

         var9 *= var5;
         var0.f(var1.field_1352, var1.field_1351 + 0.06, var1.field_1350, var7, 0.6 + var7 * 0.06, ei.R(var4, var9 * 0.85));
         N(var0, var1, var7, 0.95, ei.R(var4, var9 * 0.6));
      }
   }

   static void t(f1 var0, class_243 var1, double var2, double var4, double var6, double var8, int var10, double var11) {
      if (!(var2 < 0.0) && !(var2 > var8)) {
         double var13 = h(var2, var4, var6);
         double var15 = Math.pow(1.0 - var2 / var8, 1.5) * var11;
         if (!(var13 < 0.05) && !(var15 < 0.001)) {
            var0.f(var1.field_1352, var1.field_1351 + 0.06, var1.field_1350, var13, 0.6 + var13 * 0.06, ei.R(var10, var15 * 0.85));
            N(var0, var1, var13, 0.95 * (1.0 - var2 / var8 * 0.5), ei.R(var10, var15 * 0.6));
         }
      }
   }

   static void N(f1 var0, class_243 var1, double var2, double var4, int var6) {
      int var7 = var6 & 16777215;
      byte var8 = 72;
      double var9 = var1.field_1351 - 0.35;
      double var11 = var1.field_1351 + 0.08;
      double var13 = var1.field_1351 + var4;

      for (int var15 = 0; var15 < var8; var15++) {
         double var16 = var15 * Math.PI * 2.0 / var8;
         double var18 = (var15 + 1) * Math.PI * 2.0 / var8;
         double var20 = var1.field_1352 + Math.cos(var16) * var2;
         double var22 = var1.field_1350 + Math.sin(var16) * var2;
         double var24 = var1.field_1352 + Math.cos(var18) * var2;
         double var26 = var1.field_1350 + Math.sin(var18) * var2;
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

   static void L(f1 var0, class_243 var1, double var2, int var4) {
      if (var4 >>> 24 != 0 && !(var2 <= 0.001)) {
         int var5 = var4 & 16777215;
         byte var6 = 40;

         for (int var7 = 0; var7 < var6; var7++) {
            double var8 = var7 * Math.PI * 2.0 / var6;
            double var10 = (var7 + 1) * Math.PI * 2.0 / var6;
            var0.vertex(var1.field_1352, var1.field_1351, var1.field_1350, var4);
            var0.vertex(var1.field_1352 + Math.cos(var8) * var2, var1.field_1351, var1.field_1350 + Math.sin(var8) * var2, var5);
            var0.vertex(var1.field_1352 + Math.cos(var10) * var2, var1.field_1351, var1.field_1350 + Math.sin(var10) * var2, var5);
            var0.vertex(var1.field_1352, var1.field_1351, var1.field_1350, var4);
         }
      }
   }
}
