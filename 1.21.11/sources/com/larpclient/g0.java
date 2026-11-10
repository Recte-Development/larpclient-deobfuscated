package com.larpclient;

import net.minecraft.class_243;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class g0 {
   public static final double j = 0.16;
   public static final double Z = 0.95;
   public static final double M = 1.32;
   public static final double Q = 2.7;
   public static final double U = 4.6;
   public static final int k = 48;
   public static final int y = 14;
   public static final int T = 40;
   public static final int H = 8;
   public static final int h = 40;
   public static final int D = 102;
   static final double l = 0.12;
   static final double L = 9.8;
   private static final double[][] F = new double[][]{{4.0, 4.0, 4.0}, {4.0, 6.0, 2.0}, {2.0, 6.0, 2.0}, {2.0, 6.0, 2.0}, {2.0, 6.0, 2.0}, {2.0, 6.0, 2.0}};
   private static final int[] r = new int[]{6, 9, 5, 5, 5, 5};
   private static final int[][] g = new int[][]{{1, 1}, {1, -1}, {4, 1}, {5, 1}, {2, 1}, {3, -1}, {0, 1}, {5, -1}};
   private final u8 b;
   private final int[] K = new int[102];
   private final double[] O = new double[102];
   private final double[] a = new double[102];
   private final double[] n = new double[102];
   private final double[] J = new double[102];
   private final double[] e = new double[102];
   private final double[] x = new double[102];
   private final double[] d = new double[102];
   private final double[] R = new double[102];
   private final double[] P = new double[102];
   private final double[] A = new double[102];
   private final double[] S = new double[102];
   private final double[] f = new double[102];
   private final double[] X = new double[102];
   private final double[] m = new double[102];
   private final double[] N = new double[102];
   private final Quaternionf q = new Quaternionf();
   private final Vector3f o = new Vector3f();

   g0(u8 var1, f7 var2) {
      this.b = var1;
      int var3 = 0;

      for (int var7 : r) {
         var3 += var7;
      }

      for (int var17 = 0; var17 < 102; var17++) {
         this.d[var17] = 0.09 + 0.11 * var1.P(var17 + 11000);
         this.S[var17] = (var1.P(var17 + 11200) - 0.5) * 9.0;
         this.f[var17] = (var1.P(var17 + 11400) - 0.5) * 11.0;
         this.X[var17] = (var1.P(var17 + 11600) - 0.5) * 7.0;
         this.P[var17] = 1.2 + 1.4 * var1.P(var17 + 11800);
         this.A[var17] = 1.0 + 2.0 * var1.P(var17 + 12000);
         this.m[var17] = var1.P(var17 + 12200);
         this.N[var17] = var1.P(var17 + 12400);
         if (var17 >= 48) {
            if (var17 < 62) {
               int var19 = var17 - 48;
               double var22 = (var19 + 0.5 * var1.P(var17 + 13400)) * Math.PI * 2.0 / 14.0;
               double var25 = 0.45 + 0.6 * var1.P(var17 + 13600);
               this.K[var17] = -1;
               this.O[var17] = Math.cos(var22) * var25;
               this.n[var17] = Math.sin(var22) * var25;
               this.J[var17] = Math.cos(var22) * 0.55;
               this.e[var17] = 1.0;
               this.x[var17] = Math.sin(var22) * 0.55;
               this.d[var17] = 0.12 + 0.18 * var1.P(var17 + 13800);
               this.P[var17] = this.P[var17] * 0.6;
               this.A[var17] = this.A[var17] * 0.55;
            } else {
               this.K[var17] = -2;
               double var20 = var1.P(var17 + 13400) * Math.PI * 2.0;
               double var24 = 0.25 * Math.sqrt(var1.P(var17 + 13600));
               this.O[var17] = Math.cos(var20) * var24;
               this.n[var17] = Math.sin(var20) * var24;
               this.a[var17] = 0.1 + 1.7 * var1.P(var17 + 13800);
               this.d[var17] = 0.05 + 0.07 * var1.P(var17 + 11000);
            }
         } else {
            int var18 = (int)(var1.P(var17 + 12600) * var3);
            int var21 = 0;

            while (var18 >= r[var21]) {
               var18 -= r[var21++];
            }

            this.K[var17] = var21;
            double[] var23 = F[var21];
            double var8 = var21 >= 2 && var21 <= 3 && var1.D ? 1.5 : var23[0];
            int var10 = (int)(var1.P(var17 + 12800) * (var21 == 0 ? 5 : 4));
            double var11 = var1.P(var17 + 13000) * 2.0 - 1.0;
            double var13 = var1.P(var17 + 13200) * 2.0 - 1.0;
            double var15 = 0.5;
            switch (var10) {
               case 0:
                  this.O[var17] = var8 + var15;
                  this.a[var17] = var13 * var23[1];
                  this.n[var17] = var11 * var23[2];
                  this.J[var17] = 1.0;
                  break;
               case 1:
                  this.O[var17] = -var8 - var15;
                  this.a[var17] = var13 * var23[1];
                  this.n[var17] = var11 * var23[2];
                  this.J[var17] = -1.0;
                  break;
               case 2:
                  this.n[var17] = var23[2] + var15;
                  this.a[var17] = var13 * var23[1];
                  this.O[var17] = var11 * var8;
                  this.x[var17] = 1.0;
                  break;
               case 3:
                  this.n[var17] = -var23[2] - var15;
                  this.a[var17] = var13 * var23[1];
                  this.O[var17] = var11 * var8;
                  this.x[var17] = -1.0;
                  break;
               default:
                  this.a[var17] = -var23[1] - var15;
                  this.O[var17] = var11 * var8;
                  this.n[var17] = var13 * var23[2];
                  this.e[var17] = -1.0;
            }
         }

         this.R[var17] = this.K[var17] == -1
            ? 0.05 + 0.45 * Math.hypot(this.O[var17], this.n[var17]) / 1.05
            : 0.06 + 0.7 * Math.clamp(this.U(var17) / 1.9, 0.0, 1.0) + 0.08 * this.m[var17];
      }

      var2.o(var1.z.field_1352, var1.z.field_1350);
      var2.t(var1.z.field_1352, var1.z.field_1350, 4.0);
   }

   private double U(int var1) {
      return this.K[var1] < 0 ? this.a[var1] : (24.0 - u8.E[this.K[var1]][1] - this.a[var1]) / 16.0 * 0.9375;
   }

   private static double d(double var0) {
      return 0.12 * u8.S(var0 / 0.16);
   }

   private static double q(int var0, double var1) {
      double var3 = u8.S(var1 / 0.16) * 0.22;
      return var0 == 2 ? -var3 : (var0 == 3 ? var3 : 0.0);
   }

   private static double I(double var0) {
      return 0.016 * Math.sin(var0 * 70.0) * u8.S((var0 - 0.95) / 0.25) * (1.0 - u8.S((var0 - 1.32 + 0.07) / 0.07));
   }

   public static u8.s y(u8 var0, int var1, double var2) {
      var2 = Math.min(var2, 1.32);
      double var4 = d(var2);
      double var6 = u8.z(var1, var0.D) / 16.0 * 0.9375;
      double var8 = (24.0 - u8.E[var1][1]) / 16.0 * 0.9375;
      double var10 = Math.cos(var0.y);
      double var12 = Math.sin(var0.y);
      double var14 = var8 * Math.sin(var4);
      class_243 var16 = var0.z.method_1031(var6 * var10 + var14 * var12, var8 * Math.cos(var4), var6 * var12 - var14 * var10);
      return new u8.s(var16, var4, I(var2), q(var1, var2), 1.0, 1.0, 0.0, false);
   }

   static double f() {
      return 0.12;
   }

   static double s(int var0) {
      return var0 == 2 ? -0.22 : (var0 == 3 ? 0.22 : 0.0);
   }

   static class_243 v(u8 var0, int var1) {
      double var2 = var1 * Math.PI * 2.0 / 6.0 + var0.P(var1 + 14000) * 0.8;
      double var4 = 0.9 + 0.7 * var0.P(var1 + 14200);
      return new class_243(Math.cos(var2) * var4, 0.5 + 0.8 * var0.P(var1 + 14400), Math.sin(var2) * var4);
   }

   private class_243 S(int var1, double var2, Vector3f var4) {
      if (this.K[var1] == -1) {
         var4.set(this.J[var1], this.e[var1], this.x[var1]).normalize();
         return new class_243(this.b.z.field_1352 + this.O[var1], this.b.x().x() + 0.02, this.b.z.field_1350 + this.n[var1]);
      } else if (this.K[var1] == -2) {
         var4.set(0.0F, 1.0F, 0.0F);
         return this.b.z.method_1031(this.O[var1], this.a[var1], this.n[var1]);
      } else {
         u8.s var5 = y(this.b, this.K[var1], var2);
         this.q.identity().rotateY((float)(Math.PI - this.b.y)).rotateXYZ((float)var5.rx(), (float)var5.ry(), (float)var5.rz());
         double var6 = 0.05859375;
         this.q.transform(this.o.set((float)(-this.O[var1] * var6), (float)(-this.a[var1] * var6), (float)(this.n[var1] * var6)));
         this.q.transform(var4.set((float)(-this.J[var1]), (float)(-this.e[var1]), (float)this.x[var1]));
         return var5.position().method_1031(this.o.x, this.o.y, this.o.z);
      }
   }

   private static double h(Vector3f var0) {
      return Math.acos(Math.clamp(var0.y, -1.0F, 1.0F));
   }

   private static double f(Vector3f var0) {
      return Math.atan2(var0.x, var0.z);
   }

   public g0.k Q(int var1, double var2) {
      Vector3f var4 = new Vector3f();
      double var5 = 1.0 - u8.S((var2 - 3.45 - 0.45 * this.N[var1]) / 0.6);
      if (var2 < 1.32) {
         double var59 = this.K[var1] == -2 ? 0.0 : u8.S((var2 - this.R[var1]) / 0.28);
         if (var59 <= 0.0) {
            return null;
         }

         class_243 var9 = this.S(var1, var2, var4);
         double var60 = f(var4) + (this.m[var1] - 0.5) * 0.7;
         double var61 = h(var4) + (this.N[var1] - 0.5) * 0.6;
         double var62 = this.d[var1] * var59;
         return new g0.k(
            var9.method_1031(var4.x * var62 * 0.35, var4.y * var62 * 0.35, var4.z * var62 * 0.35), var60, var61, (this.m[var1] - 0.5) * 0.4, var62, 1.0, false
         );
      } else {
         class_243 var7 = this.S(var1, 1.32, var4);
         if (this.K[var1] != -2) {
            var7 = var7.method_1031(var4.x * this.d[var1] * 0.35, var4.y * this.d[var1] * 0.35, var4.z * this.d[var1] * 0.35);
         }

         double var8 = f(var4) + (this.m[var1] - 0.5) * 0.7;
         double var10 = h(var4) + (this.N[var1] - 0.5) * 0.6;
         double var12 = (this.m[var1] - 0.5) * 0.4;
         double var14 = var7.field_1352 - this.b.z.field_1352;
         double var16 = var7.field_1350 - this.b.z.field_1350;
         double var18 = Math.hypot(var14, var16);
         if (var18 < 1.0E-4) {
            double var20 = this.m[var1] * Math.PI * 2.0;
            var14 = Math.cos(var20);
            var16 = Math.sin(var20);
            var18 = 1.0;
         }

         double var63 = (this.N[var1] - 0.5) * 1.2;
         double var22 = var14 / var18 * this.P[var1] - var16 / var18 * var63;
         double var24 = var16 / var18 * this.P[var1] + var14 / var18 * var63;
         double var26 = this.A[var1];
         double var28 = this.K[var1] == -2 ? this.d[var1] * Math.min(1.0, (var2 - 1.32) * 8.0) : this.d[var1];
         double var30 = Math.PI / 2;
         double var32 = com.larpclient.e.k(var28, var30, 0.0);
         double var34 = this.b.x().x();
         double var36 = I(var7.field_1351, var26, var34 + var32);

         for (int var38 = 0; var38 < 2; var38++) {
            double var39 = this.b.x().H(var7.field_1352 + var22 * var36, var7.field_1350 + var24 * var36);
            var34 = Double.isNaN(var39) ? this.b.x().x() - 4.5 : var39;
            var36 = I(var7.field_1351, var26, var34 + var32);
         }

         double var64 = var2 - 1.32;
         double var40 = 0.28 * Math.abs(var26 - 9.8 * var36);
         double var42 = 2.0 * var40 / 9.8;
         double var45 = Math.min(var64, var36);
         double var47 = 0.0;
         class_243 var44;
         if (var64 < var36) {
            var44 = var7.method_1031(var22 * var64, var26 * var64 - 4.9 * var64 * var64, var24 * var64);
         } else if (var64 < var36 + var42) {
            double var49 = var64 - var36;
            var44 = new class_243(
               var7.field_1352 + var22 * (var36 + 0.45 * var49),
               var34 + var32 + var40 * var49 - 4.9 * var49 * var49,
               var7.field_1350 + var24 * (var36 + 0.45 * var49)
            );
            var47 = u8.S(var49 / var42);
         } else {
            var44 = new class_243(var7.field_1352 + var22 * (var36 + 0.45 * var42), var34 + var32, var7.field_1350 + var24 * (var36 + 0.45 * var42));
            var47 = 1.0;
         }

         double var65 = var8 + this.S[var1] * var45;
         double var51 = var10 + this.f[var1] * var45;
         double var53 = var12 + this.X[var1] * var45;
         double var55 = Math.round((var51 - var30) / Math.PI) * Math.PI + var30;
         double var57 = Math.round(var53 / Math.PI) * Math.PI;
         var51 += (var55 - var51) * var47;
         var53 += (var57 - var53) * var47;
         if (var47 > 0.0) {
            var44 = var44.method_1031(0.0, (com.larpclient.e.k(var28, var55, var57) - var32) * var47, 0.0);
         }

         return new g0.k(var44, var65, var51, var53, var28, var5, var47 >= 1.0);
      }
   }

   static double I(double var0, double var2, double var4) {
      double var6 = Math.max(0.0, var0 - var4);
      return (var2 + Math.sqrt(var2 * var2 + 19.6 * var6)) / 9.8;
   }

   public static void B(f1 var0, u8 var1, double var2, int var4, int var5, double var6, boolean var8) {
      g0 var9 = var1.j();
      if (var9 != null) {
         var9.L(var0, var2, var4, var5, var6, var8);
      }
   }

   private void L(f1 var1, double var2, int var4, int var5, double var6, boolean var8) {
      class_243 var9 = new class_243(this.b.z.field_1352, this.b.x().x(), this.b.z.field_1350);
      int var10 = ei.W(var4, var5, 0.45);
      int var11 = ei.W(var5, -1, 0.5);
      double var12 = u8.o(var2, 0.0, 0.35) * var6;
      U(var1, this.b.z.method_1031(0.0, 1.15, 0.0), 0.35 + var2 * 0.9, ei.R(var11, var12 * 0.8));
      double var14 = u8.S((var2 - 0.05) / 0.7);
      double var16 = 1.0 - u8.S((var2 - 1.32 - 0.4) / 2.4);
      double var18 = var14 * var16 * var6;
      if (var18 > 0.001) {
         g2.L(var1, var9.method_1031(0.0, 0.04, 0.0), 0.3 + 1.25 * var14, ei.R(var4, var18 * 0.32));
         var1.f(var9.field_1352, var9.field_1351 + 0.045, var9.field_1350, 0.3 + 1.25 * var14, 0.12, ei.R(var11, var18 * 0.6));
      }

      double var20 = u8.S((var2 - 0.95) / 0.29000000000000004);
      double var22 = u8.o(var2, 0.95, 1.37) * var6;
      if (var22 > 0.001) {
         for (int var24 = 0; var24 < 8; var24++) {
            this.k(var1, var24, Math.min(var2, 1.32), var20, ei.R(var11, var22));
         }
      }

      double var41 = u8.o(var2, 0.9700000000000001, 1.3800000000000001) * var6;
      U(var1, this.b.z.method_1031(0.0, 1.05, 0.0), 0.25 + 0.5 * u8.S((var2 - 1.32 + 0.35) / 0.35), ei.R(var11, var41 * 0.7));
      double var26 = var2 - 1.32;
      if (var26 >= 0.0) {
         double var28 = u8.o(var26, 0.0, 0.5) * var6;
         U(var1, this.b.z.method_1031(0.0, 1.0, 0.0), 1.0 + var26 * 2.2, ei.R(var11, var28));
         if (var26 < 0.7) {
            var1.f(
               var9.field_1352,
               var9.field_1351 + 0.06,
               var9.field_1350,
               0.3 + 3.4 * (1.0 - Math.pow(1.0 - var26 / 0.7, 3.0)),
               0.3,
               ei.R(var11, Math.pow(1.0 - var26 / 0.7, 1.4) * var6)
            );
         }

         if (var26 < 1.3) {
            g2.N(var1, var9, 0.3 + 2.6 * (1.0 - Math.pow(1.0 - var26 / 1.3, 3.0)), 0.5, ei.R(var4, (1.0 - var26 / 1.3) * 0.35 * var6));
         }

         double var30 = u8.o(var26, 0.0, 2.3) * var6;

         for (int var32 = 0; var32 < 40 && var30 > 0.001; var32++) {
            double var33 = this.b.P(var32 + 15000) * Math.PI * 2.0;
            double var35 = 0.2 + 1.6 * this.b.P(var32 + 15200) * (1.0 - Math.exp(-var26 * 2.5));
            double var37 = 0.2 + 1.7 * this.b.P(var32 + 15400) + var26 * (0.5 - 0.45 * var26) * (1.0 + this.b.P(var32 + 15600));
            double var39 = 0.45 + 0.55 * Math.pow(Math.max(0.0, Math.sin(var2 * 6.0 + var32 * 1.9)), 4.0);
            var1.V(
               this.b.z.field_1352 + Math.cos(var33) * var35,
               this.b.z.field_1351 + Math.max(0.05, var37),
               this.b.z.field_1350 + Math.sin(var33) * var35,
               0.022 + 0.02 * this.b.P(var32 + 15800),
               var33 + var2,
               ei.R(var11, var30 * var39)
            );
         }
      }

      var1.blend(var8 ? ti.d.l : ti.d.G);

      for (int var42 = 0; var42 < 102; var42++) {
         g0.k var29 = this.Q(var42, var2);
         if (var29 != null && !(var29.alpha() <= 0.001) && !(var29.size() <= 0.001)) {
            double var45 = var29.alpha() * (var8 ? 0.3 * var6 : 0.82);
            var1.X(
               var29.position().field_1352,
               var29.position().field_1351,
               var29.position().field_1350,
               var29.size(),
               var29.yaw(),
               var29.pitch(),
               var29.roll(),
               ei.R(var8 ? var11 : var10, var45)
            );
         }
      }

      var1.blend(ti.d.l);
      if (var26 > 0.3) {
         for (byte var43 = 0; var43 < 102; var43 += 3) {
            g0.k var44 = this.Q(var43, var2);
            if (var44 != null && var44.rest()) {
               double var46 = Math.pow(Math.max(0.0, Math.sin(var2 * 4.5 + var43 * 1.7)), 12.0) * var44.alpha() * var6;
               if (var46 > 0.02) {
                  var1.V(var44.position().field_1352, var44.position().field_1351 + 0.04, var44.position().field_1350, 0.07, var2, ei.R(-1, var46));
               }
            }
         }
      }
   }

   private void k(f1 var1, int var2, double var3, double var5, int var7) {
      int var8 = g[var2][0];
      int var9 = g[var2][1];
      u8.s var10 = y(this.b, var8, var3);
      this.q.identity().rotateY((float)(Math.PI - this.b.y)).rotateXYZ((float)var10.rx(), (float)var10.ry(), (float)var10.rz());
      double[] var11 = F[var8];
      double var12 = 0.05859375;
      double var14 = (var11[2] + 0.7) * var9;
      double var16 = (this.b.P(var2 + 16000) - 0.5) * var11[0];
      double var18 = (this.b.P(var2 + 16200) - 0.5) * var11[1] * 0.6;
      byte var20 = 5;
      class_243 var21 = null;

      for (int var22 = 0; var22 <= var20; var22++) {
         double var23 = (double)var22 / var20;
         if (var23 > var5 + 1.0E-9) {
            break;
         }

         double var25 = var16 + (this.b.P(var2 * 8 + var22 + 16400) - 0.5) * var11[0] * 0.9;
         double var27 = var18 + (var23 - 0.5) * var11[1] * 1.6 * (this.b.P(var2 + 16600) < 0.5 ? 1 : -1);
         this.q
            .transform(
               this.o
                  .set(
                     (float)(-Math.clamp(var25, -var11[0], var11[0]) * var12), (float)(-Math.clamp(var27, -var11[1], var11[1]) * var12), (float)(var14 * var12)
                  )
            );
         class_243 var29 = var10.position().method_1031(this.o.x, this.o.y, this.o.z);
         if (var21 != null) {
            var1.ribbon(var21, var29, 0.028, var7, var7);
         }

         var21 = var29;
      }
   }

   private static void U(f1 var0, class_243 var1, double var2, int var4) {
      if (var4 >>> 24 > 0) {
         var0.U(var1.field_1352, var1.field_1351, var1.field_1350, var2, 0.0, var4, gd.h.f, 0.0, 0.0, 0.0);
      }
   }

   public record k(class_243 position, double yaw, double pitch, double roll, double size, double alpha, boolean rest) {
   }
}
