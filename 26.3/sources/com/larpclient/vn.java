package com.larpclient;

import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class vn {
   public static final double u = 0.16;
   public static final double F = 0.95;
   public static final double c = 1.32;
   public static final double L = 2.7;
   public static final double U = 4.6;
   public static final int X = 48;
   public static final int t = 14;
   public static final int x = 40;
   public static final int C = 8;
   public static final int T = 40;
   public static final int l = 102;
   static final double B = 0.12;
   static final double P = 9.8;
   private static final double[][] s = new double[][]{{4.0, 4.0, 4.0}, {4.0, 6.0, 2.0}, {2.0, 6.0, 2.0}, {2.0, 6.0, 2.0}, {2.0, 6.0, 2.0}, {2.0, 6.0, 2.0}};
   private static final int[] n = new int[]{6, 9, 5, 5, 5, 5};
   private static final int[][] d = new int[][]{{1, 1}, {1, -1}, {4, 1}, {5, 1}, {2, 1}, {3, -1}, {0, 1}, {5, -1}};
   private final n5 H;
   private final int[] o = new int[102];
   private final double[] V = new double[102];
   private final double[] g = new double[102];
   private final double[] i = new double[102];
   private final double[] a = new double[102];
   private final double[] W = new double[102];
   private final double[] Q = new double[102];
   private final double[] S = new double[102];
   private final double[] v = new double[102];
   private final double[] h = new double[102];
   private final double[] f = new double[102];
   private final double[] k = new double[102];
   private final double[] G = new double[102];
   private final double[] R = new double[102];
   private final double[] N = new double[102];
   private final double[] w = new double[102];
   private final Quaternionf K = new Quaternionf();
   private final Vector3f J = new Vector3f();

   vn(n5 var1, ap var2) {
      this.H = var1;
      int var3 = 0;

      for (int var7 : n) {
         var3 += var7;
      }

      for (int var17 = 0; var17 < 102; var17++) {
         this.S[var17] = 0.09 + 0.11 * var1.w(var17 + 11000);
         this.k[var17] = (var1.w(var17 + 11200) - 0.5) * 9.0;
         this.G[var17] = (var1.w(var17 + 11400) - 0.5) * 11.0;
         this.R[var17] = (var1.w(var17 + 11600) - 0.5) * 7.0;
         this.h[var17] = 1.2 + 1.4 * var1.w(var17 + 11800);
         this.f[var17] = 1.0 + 2.0 * var1.w(var17 + 12000);
         this.N[var17] = var1.w(var17 + 12200);
         this.w[var17] = var1.w(var17 + 12400);
         if (var17 >= 48) {
            if (var17 < 62) {
               int var19 = var17 - 48;
               double var22 = (var19 + 0.5 * var1.w(var17 + 13400)) * Math.PI * 2.0 / 14.0;
               double var25 = 0.45 + 0.6 * var1.w(var17 + 13600);
               this.o[var17] = -1;
               this.V[var17] = Math.cos(var22) * var25;
               this.i[var17] = Math.sin(var22) * var25;
               this.a[var17] = Math.cos(var22) * 0.55;
               this.W[var17] = 1.0;
               this.Q[var17] = Math.sin(var22) * 0.55;
               this.S[var17] = 0.12 + 0.18 * var1.w(var17 + 13800);
               this.h[var17] = this.h[var17] * 0.6;
               this.f[var17] = this.f[var17] * 0.55;
            } else {
               this.o[var17] = -2;
               double var20 = var1.w(var17 + 13400) * Math.PI * 2.0;
               double var24 = 0.25 * Math.sqrt(var1.w(var17 + 13600));
               this.V[var17] = Math.cos(var20) * var24;
               this.i[var17] = Math.sin(var20) * var24;
               this.g[var17] = 0.1 + 1.7 * var1.w(var17 + 13800);
               this.S[var17] = 0.05 + 0.07 * var1.w(var17 + 11000);
            }
         } else {
            int var18 = (int)(var1.w(var17 + 12600) * var3);
            int var21 = 0;

            while (var18 >= n[var21]) {
               var18 -= n[var21++];
            }

            this.o[var17] = var21;
            double[] var23 = s[var21];
            double var8 = var21 >= 2 && var21 <= 3 && var1.Q ? 1.5 : var23[0];
            int var10 = (int)(var1.w(var17 + 12800) * (var21 == 0 ? 5 : 4));
            double var11 = var1.w(var17 + 13000) * 2.0 - 1.0;
            double var13 = var1.w(var17 + 13200) * 2.0 - 1.0;
            double var15 = 0.5;
            switch (var10) {
               case 0:
                  this.V[var17] = var8 + var15;
                  this.g[var17] = var13 * var23[1];
                  this.i[var17] = var11 * var23[2];
                  this.a[var17] = 1.0;
                  break;
               case 1:
                  this.V[var17] = -var8 - var15;
                  this.g[var17] = var13 * var23[1];
                  this.i[var17] = var11 * var23[2];
                  this.a[var17] = -1.0;
                  break;
               case 2:
                  this.i[var17] = var23[2] + var15;
                  this.g[var17] = var13 * var23[1];
                  this.V[var17] = var11 * var8;
                  this.Q[var17] = 1.0;
                  break;
               case 3:
                  this.i[var17] = -var23[2] - var15;
                  this.g[var17] = var13 * var23[1];
                  this.V[var17] = var11 * var8;
                  this.Q[var17] = -1.0;
                  break;
               default:
                  this.g[var17] = -var23[1] - var15;
                  this.V[var17] = var11 * var8;
                  this.i[var17] = var13 * var23[2];
                  this.W[var17] = -1.0;
            }
         }

         this.v[var17] = this.o[var17] == -1
            ? 0.05 + 0.45 * Math.hypot(this.V[var17], this.i[var17]) / 1.05
            : 0.06 + 0.7 * Math.clamp(this.L(var17) / 1.9, 0.0, 1.0) + 0.08 * this.N[var17];
      }

      var2.N(var1.r.x, var1.r.z);
      var2.B(var1.r.x, var1.r.z, 4.0);
   }

   private double L(int var1) {
      return this.o[var1] < 0 ? this.g[var1] : (24.0 - n5.a[this.o[var1]][1] - this.g[var1]) / 16.0 * 0.9375;
   }

   private static double K(double var0) {
      return 0.12 * n5.g(var0 / 0.16);
   }

   private static double T(int var0, double var1) {
      double var3 = n5.g(var1 / 0.16) * 0.22;
      return var0 == 2 ? -var3 : (var0 == 3 ? var3 : 0.0);
   }

   private static double d(double var0) {
      return 0.016 * Math.sin(var0 * 70.0) * n5.g((var0 - 0.95) / 0.25) * (1.0 - n5.g((var0 - 1.32 + 0.07) / 0.07));
   }

   public static n5.p f(n5 var0, int var1, double var2) {
      var2 = Math.min(var2, 1.32);
      double var4 = K(var2);
      double var6 = n5.H(var1, var0.Q) / 16.0 * 0.9375;
      double var8 = (24.0 - n5.a[var1][1]) / 16.0 * 0.9375;
      double var10 = Math.cos(var0.y);
      double var12 = Math.sin(var0.y);
      double var14 = var8 * Math.sin(var4);
      Vec3 var16 = var0.r.add(var6 * var10 + var14 * var12, var8 * Math.cos(var4), var6 * var12 - var14 * var10);
      return new n5.p(var16, var4, d(var2), T(var1, var2), 1.0, 1.0, 0.0, false);
   }

   static double J() {
      return 0.12;
   }

   static double h(int var0) {
      return var0 == 2 ? -0.22 : (var0 == 3 ? 0.22 : 0.0);
   }

   static Vec3 q(n5 var0, int var1) {
      double var2 = var1 * Math.PI * 2.0 / 6.0 + var0.w(var1 + 14000) * 0.8;
      double var4 = 0.9 + 0.7 * var0.w(var1 + 14200);
      return new Vec3(Math.cos(var2) * var4, 0.5 + 0.8 * var0.w(var1 + 14400), Math.sin(var2) * var4);
   }

   private Vec3 Q(int var1, double var2, Vector3f var4) {
      if (this.o[var1] == -1) {
         var4.set(this.a[var1], this.W[var1], this.Q[var1]).normalize();
         return new Vec3(this.H.r.x + this.V[var1], this.H.H().v() + 0.02, this.H.r.z + this.i[var1]);
      } else if (this.o[var1] == -2) {
         var4.set(0.0F, 1.0F, 0.0F);
         return this.H.r.add(this.V[var1], this.g[var1], this.i[var1]);
      } else {
         n5.p var5 = f(this.H, this.o[var1], var2);
         this.K.identity().rotateY((float)(Math.PI - this.H.y)).rotateXYZ((float)var5.rx(), (float)var5.ry(), (float)var5.rz());
         double var6 = 0.05859375;
         this.K.transform(this.J.set((float)(-this.V[var1] * var6), (float)(-this.g[var1] * var6), (float)(this.i[var1] * var6)));
         this.K.transform(var4.set((float)(-this.a[var1]), (float)(-this.W[var1]), (float)this.Q[var1]));
         return var5.position().add(this.J.x, this.J.y, this.J.z);
      }
   }

   private static double S(Vector3f var0) {
      return Math.acos(Math.clamp(var0.y, -1.0F, 1.0F));
   }

   private static double C(Vector3f var0) {
      return Math.atan2(var0.x, var0.z);
   }

   public vn.b z(int var1, double var2) {
      Vector3f var4 = new Vector3f();
      double var5 = 1.0 - n5.g((var2 - 3.45 - 0.45 * this.w[var1]) / 0.6);
      if (var2 < 1.32) {
         double var59 = this.o[var1] == -2 ? 0.0 : n5.g((var2 - this.v[var1]) / 0.28);
         if (var59 <= 0.0) {
            return null;
         }

         Vec3 var9 = this.Q(var1, var2, var4);
         double var60 = C(var4) + (this.N[var1] - 0.5) * 0.7;
         double var61 = S(var4) + (this.w[var1] - 0.5) * 0.6;
         double var62 = this.S[var1] * var59;
         return new vn.b(
            var9.add(var4.x * var62 * 0.35, var4.y * var62 * 0.35, var4.z * var62 * 0.35), var60, var61, (this.N[var1] - 0.5) * 0.4, var62, 1.0, false
         );
      } else {
         Vec3 var7 = this.Q(var1, 1.32, var4);
         if (this.o[var1] != -2) {
            var7 = var7.add(var4.x * this.S[var1] * 0.35, var4.y * this.S[var1] * 0.35, var4.z * this.S[var1] * 0.35);
         }

         double var8 = C(var4) + (this.N[var1] - 0.5) * 0.7;
         double var10 = S(var4) + (this.w[var1] - 0.5) * 0.6;
         double var12 = (this.N[var1] - 0.5) * 0.4;
         double var14 = var7.x - this.H.r.x;
         double var16 = var7.z - this.H.r.z;
         double var18 = Math.hypot(var14, var16);
         if (var18 < 1.0E-4) {
            double var20 = this.N[var1] * Math.PI * 2.0;
            var14 = Math.cos(var20);
            var16 = Math.sin(var20);
            var18 = 1.0;
         }

         double var63 = (this.w[var1] - 0.5) * 1.2;
         double var22 = var14 / var18 * this.h[var1] - var16 / var18 * var63;
         double var24 = var16 / var18 * this.h[var1] + var14 / var18 * var63;
         double var26 = this.f[var1];
         double var28 = this.o[var1] == -2 ? this.S[var1] * Math.min(1.0, (var2 - 1.32) * 8.0) : this.S[var1];
         double var30 = Math.PI / 2;
         double var32 = po.R(var28, var30, 0.0);
         double var34 = this.H.H().v();
         double var36 = K(var7.y, var26, var34 + var32);

         for (int var38 = 0; var38 < 2; var38++) {
            double var39 = this.H.H().y(var7.x + var22 * var36, var7.z + var24 * var36);
            var34 = Double.isNaN(var39) ? this.H.H().v() - 4.5 : var39;
            var36 = K(var7.y, var26, var34 + var32);
         }

         double var64 = var2 - 1.32;
         double var40 = 0.28 * Math.abs(var26 - 9.8 * var36);
         double var42 = 2.0 * var40 / 9.8;
         double var45 = Math.min(var64, var36);
         double var47 = 0.0;
         Vec3 var44;
         if (var64 < var36) {
            var44 = var7.add(var22 * var64, var26 * var64 - 4.9 * var64 * var64, var24 * var64);
         } else if (var64 < var36 + var42) {
            double var49 = var64 - var36;
            var44 = new Vec3(
               var7.x + var22 * (var36 + 0.45 * var49), var34 + var32 + var40 * var49 - 4.9 * var49 * var49, var7.z + var24 * (var36 + 0.45 * var49)
            );
            var47 = n5.g(var49 / var42);
         } else {
            var44 = new Vec3(var7.x + var22 * (var36 + 0.45 * var42), var34 + var32, var7.z + var24 * (var36 + 0.45 * var42));
            var47 = 1.0;
         }

         double var65 = var8 + this.k[var1] * var45;
         double var51 = var10 + this.G[var1] * var45;
         double var53 = var12 + this.R[var1] * var45;
         double var55 = Math.round((var51 - var30) / Math.PI) * Math.PI + var30;
         double var57 = Math.round(var53 / Math.PI) * Math.PI;
         var51 += (var55 - var51) * var47;
         var53 += (var57 - var53) * var47;
         if (var47 > 0.0) {
            var44 = var44.add(0.0, (po.R(var28, var55, var57) - var32) * var47, 0.0);
         }

         return new vn.b(var44, var65, var51, var53, var28, var5, var47 >= 1.0);
      }
   }

   static double K(double var0, double var2, double var4) {
      double var6 = Math.max(0.0, var0 - var4);
      return (var2 + Math.sqrt(var2 * var2 + 19.6 * var6)) / 9.8;
   }

   public static void u(fz var0, n5 var1, double var2, int var4, int var5, double var6, boolean var8) {
      vn var9 = var1.b();
      if (var9 != null) {
         var9.c(var0, var2, var4, var5, var6, var8);
      }
   }

   private void c(fz var1, double var2, int var4, int var5, double var6, boolean var8) {
      Vec3 var9 = new Vec3(this.H.r.x, this.H.H().v(), this.H.r.z);
      int var10 = vv.Q(var4, var5, 0.45);
      int var11 = vv.Q(var5, -1, 0.5);
      double var12 = n5.r(var2, 0.0, 0.35) * var6;
      d(var1, this.H.r.add(0.0, 1.15, 0.0), 0.35 + var2 * 0.9, vv.O(var11, var12 * 0.8));
      double var14 = n5.g((var2 - 0.05) / 0.7);
      double var16 = 1.0 - n5.g((var2 - 1.32 - 0.4) / 2.4);
      double var18 = var14 * var16 * var6;
      if (var18 > 0.001) {
         dd.l(var1, var9.add(0.0, 0.04, 0.0), 0.3 + 1.25 * var14, vv.O(var4, var18 * 0.32));
         var1.l(var9.x, var9.y + 0.045, var9.z, 0.3 + 1.25 * var14, 0.12, vv.O(var11, var18 * 0.6));
      }

      double var20 = n5.g((var2 - 0.95) / 0.29000000000000004);
      double var22 = n5.r(var2, 0.95, 1.37) * var6;
      if (var22 > 0.001) {
         for (int var24 = 0; var24 < 8; var24++) {
            this.Z(var1, var24, Math.min(var2, 1.32), var20, vv.O(var11, var22));
         }
      }

      double var41 = n5.r(var2, 0.9700000000000001, 1.3800000000000001) * var6;
      d(var1, this.H.r.add(0.0, 1.05, 0.0), 0.25 + 0.5 * n5.g((var2 - 1.32 + 0.35) / 0.35), vv.O(var11, var41 * 0.7));
      double var26 = var2 - 1.32;
      if (var26 >= 0.0) {
         double var28 = n5.r(var26, 0.0, 0.5) * var6;
         d(var1, this.H.r.add(0.0, 1.0, 0.0), 1.0 + var26 * 2.2, vv.O(var11, var28));
         if (var26 < 0.7) {
            var1.l(
               var9.x, var9.y + 0.06, var9.z, 0.3 + 3.4 * (1.0 - Math.pow(1.0 - var26 / 0.7, 3.0)), 0.3, vv.O(var11, Math.pow(1.0 - var26 / 0.7, 1.4) * var6)
            );
         }

         if (var26 < 1.3) {
            dd.c(var1, var9, 0.3 + 2.6 * (1.0 - Math.pow(1.0 - var26 / 1.3, 3.0)), 0.5, vv.O(var4, (1.0 - var26 / 1.3) * 0.35 * var6));
         }

         double var30 = n5.r(var26, 0.0, 2.3) * var6;

         for (int var32 = 0; var32 < 40 && var30 > 0.001; var32++) {
            double var33 = this.H.w(var32 + 15000) * Math.PI * 2.0;
            double var35 = 0.2 + 1.6 * this.H.w(var32 + 15200) * (1.0 - Math.exp(-var26 * 2.5));
            double var37 = 0.2 + 1.7 * this.H.w(var32 + 15400) + var26 * (0.5 - 0.45 * var26) * (1.0 + this.H.w(var32 + 15600));
            double var39 = 0.45 + 0.55 * Math.pow(Math.max(0.0, Math.sin(var2 * 6.0 + var32 * 1.9)), 4.0);
            var1.K(
               this.H.r.x + Math.cos(var33) * var35,
               this.H.r.y + Math.max(0.05, var37),
               this.H.r.z + Math.sin(var33) * var35,
               0.022 + 0.02 * this.H.w(var32 + 15800),
               var33 + var2,
               vv.O(var11, var30 * var39)
            );
         }
      }

      var1.blend(var8 ? _0.v.c : _0.v.z);

      for (int var42 = 0; var42 < 102; var42++) {
         vn.b var29 = this.z(var42, var2);
         if (var29 != null && !(var29.alpha() <= 0.001) && !(var29.size() <= 0.001)) {
            double var45 = var29.alpha() * (var8 ? 0.3 * var6 : 0.82);
            var1.c(
               var29.position().x,
               var29.position().y,
               var29.position().z,
               var29.size(),
               var29.yaw(),
               var29.pitch(),
               var29.roll(),
               vv.O(var8 ? var11 : var10, var45)
            );
         }
      }

      var1.blend(_0.v.c);
      if (var26 > 0.3) {
         for (byte var43 = 0; var43 < 102; var43 += 3) {
            vn.b var44 = this.z(var43, var2);
            if (var44 != null && var44.rest()) {
               double var46 = Math.pow(Math.max(0.0, Math.sin(var2 * 4.5 + var43 * 1.7)), 12.0) * var44.alpha() * var6;
               if (var46 > 0.02) {
                  var1.K(var44.position().x, var44.position().y + 0.04, var44.position().z, 0.07, var2, vv.O(-1, var46));
               }
            }
         }
      }
   }

   private void Z(fz var1, int var2, double var3, double var5, int var7) {
      int var8 = d[var2][0];
      int var9 = d[var2][1];
      n5.p var10 = f(this.H, var8, var3);
      this.K.identity().rotateY((float)(Math.PI - this.H.y)).rotateXYZ((float)var10.rx(), (float)var10.ry(), (float)var10.rz());
      double[] var11 = s[var8];
      double var12 = 0.05859375;
      double var14 = (var11[2] + 0.7) * var9;
      double var16 = (this.H.w(var2 + 16000) - 0.5) * var11[0];
      double var18 = (this.H.w(var2 + 16200) - 0.5) * var11[1] * 0.6;
      byte var20 = 5;
      Vec3 var21 = null;

      for (int var22 = 0; var22 <= var20; var22++) {
         double var23 = (double)var22 / var20;
         if (var23 > var5 + 1.0E-9) {
            break;
         }

         double var25 = var16 + (this.H.w(var2 * 8 + var22 + 16400) - 0.5) * var11[0] * 0.9;
         double var27 = var18 + (var23 - 0.5) * var11[1] * 1.6 * (this.H.w(var2 + 16600) < 0.5 ? 1 : -1);
         this.K
            .transform(
               this.J
                  .set(
                     (float)(-Math.clamp(var25, -var11[0], var11[0]) * var12), (float)(-Math.clamp(var27, -var11[1], var11[1]) * var12), (float)(var14 * var12)
                  )
            );
         Vec3 var29 = var10.position().add(this.J.x, this.J.y, this.J.z);
         if (var21 != null) {
            var1.ribbon(var21, var29, 0.028, var7, var7);
         }

         var21 = var29;
      }
   }

   private static void d(fz var0, Vec3 var1, double var2, int var4) {
      if (var4 >>> 24 > 0) {
         var0.a(var1.x, var1.y, var1.z, var2, 0.0, var4, l.b.P, 0.0, 0.0, 0.0);
      }
   }

   public record b(Vec3 position, double yaw, double pitch, double roll, double size, double alpha, boolean rest) {
   }
}
