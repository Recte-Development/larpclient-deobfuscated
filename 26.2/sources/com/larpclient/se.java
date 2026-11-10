package com.larpclient;

import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class se {
   public static final double l = 0.16;
   public static final double I = 0.95;
   public static final double R = 1.32;
   public static final double J = 2.7;
   public static final double E = 4.6;
   public static final int f = 48;
   public static final int B = 14;
   public static final int W = 40;
   public static final int O = 8;
   public static final int m = 40;
   public static final int a = 102;
   static final double L = 0.12;
   static final double K = 9.8;
   private static final double[][] y = new double[][]{{4.0, 4.0, 4.0}, {4.0, 6.0, 2.0}, {2.0, 6.0, 2.0}, {2.0, 6.0, 2.0}, {2.0, 6.0, 2.0}, {2.0, 6.0, 2.0}};
   private static final int[] j = new int[]{6, 9, 5, 5, 5, 5};
   private static final int[][] N = new int[][]{{1, 1}, {1, -1}, {4, 1}, {5, 1}, {2, 1}, {3, -1}, {0, 1}, {5, -1}};
   private final pj x;
   private final int[] S = new int[102];
   private final double[] T = new double[102];
   private final double[] n = new double[102];
   private final double[] Y = new double[102];
   private final double[] F = new double[102];
   private final double[] u = new double[102];
   private final double[] p = new double[102];
   private final double[] d = new double[102];
   private final double[] k = new double[102];
   private final double[] h = new double[102];
   private final double[] t = new double[102];
   private final double[] q = new double[102];
   private final double[] V = new double[102];
   private final double[] r = new double[102];
   private final double[] Q = new double[102];
   private final double[] D = new double[102];
   private final Quaternionf U = new Quaternionf();
   private final Vector3f G = new Vector3f();

   se(pj var1, f var2) {
      this.x = var1;
      int var3 = 0;

      for (int var7 : j) {
         var3 += var7;
      }

      for (int var17 = 0; var17 < 102; var17++) {
         this.d[var17] = 0.09 + 0.11 * var1.F(var17 + 11000);
         this.q[var17] = (var1.F(var17 + 11200) - 0.5) * 9.0;
         this.V[var17] = (var1.F(var17 + 11400) - 0.5) * 11.0;
         this.r[var17] = (var1.F(var17 + 11600) - 0.5) * 7.0;
         this.h[var17] = 1.2 + 1.4 * var1.F(var17 + 11800);
         this.t[var17] = 1.0 + 2.0 * var1.F(var17 + 12000);
         this.Q[var17] = var1.F(var17 + 12200);
         this.D[var17] = var1.F(var17 + 12400);
         if (var17 >= 48) {
            if (var17 < 62) {
               int var19 = var17 - 48;
               double var22 = (var19 + 0.5 * var1.F(var17 + 13400)) * Math.PI * 2.0 / 14.0;
               double var25 = 0.45 + 0.6 * var1.F(var17 + 13600);
               this.S[var17] = -1;
               this.T[var17] = Math.cos(var22) * var25;
               this.Y[var17] = Math.sin(var22) * var25;
               this.F[var17] = Math.cos(var22) * 0.55;
               this.u[var17] = 1.0;
               this.p[var17] = Math.sin(var22) * 0.55;
               this.d[var17] = 0.12 + 0.18 * var1.F(var17 + 13800);
               this.h[var17] = this.h[var17] * 0.6;
               this.t[var17] = this.t[var17] * 0.55;
            } else {
               this.S[var17] = -2;
               double var20 = var1.F(var17 + 13400) * Math.PI * 2.0;
               double var24 = 0.25 * Math.sqrt(var1.F(var17 + 13600));
               this.T[var17] = Math.cos(var20) * var24;
               this.Y[var17] = Math.sin(var20) * var24;
               this.n[var17] = 0.1 + 1.7 * var1.F(var17 + 13800);
               this.d[var17] = 0.05 + 0.07 * var1.F(var17 + 11000);
            }
         } else {
            int var18 = (int)(var1.F(var17 + 12600) * var3);
            int var21 = 0;

            while (var18 >= j[var21]) {
               var18 -= j[var21++];
            }

            this.S[var17] = var21;
            double[] var23 = y[var21];
            double var8 = var21 >= 2 && var21 <= 3 && var1.l ? 1.5 : var23[0];
            int var10 = (int)(var1.F(var17 + 12800) * (var21 == 0 ? 5 : 4));
            double var11 = var1.F(var17 + 13000) * 2.0 - 1.0;
            double var13 = var1.F(var17 + 13200) * 2.0 - 1.0;
            double var15 = 0.5;
            switch (var10) {
               case 0:
                  this.T[var17] = var8 + var15;
                  this.n[var17] = var13 * var23[1];
                  this.Y[var17] = var11 * var23[2];
                  this.F[var17] = 1.0;
                  break;
               case 1:
                  this.T[var17] = -var8 - var15;
                  this.n[var17] = var13 * var23[1];
                  this.Y[var17] = var11 * var23[2];
                  this.F[var17] = -1.0;
                  break;
               case 2:
                  this.Y[var17] = var23[2] + var15;
                  this.n[var17] = var13 * var23[1];
                  this.T[var17] = var11 * var8;
                  this.p[var17] = 1.0;
                  break;
               case 3:
                  this.Y[var17] = -var23[2] - var15;
                  this.n[var17] = var13 * var23[1];
                  this.T[var17] = var11 * var8;
                  this.p[var17] = -1.0;
                  break;
               default:
                  this.n[var17] = -var23[1] - var15;
                  this.T[var17] = var11 * var8;
                  this.Y[var17] = var13 * var23[2];
                  this.u[var17] = -1.0;
            }
         }

         this.k[var17] = this.S[var17] == -1
            ? 0.05 + 0.45 * Math.hypot(this.T[var17], this.Y[var17]) / 1.05
            : 0.06 + 0.7 * Math.clamp(this.f(var17) / 1.9, 0.0, 1.0) + 0.08 * this.Q[var17];
      }

      var2.o(var1.v.x, var1.v.z);
      var2.e(var1.v.x, var1.v.z, 4.0);
   }

   private double f(int var1) {
      return this.S[var1] < 0 ? this.n[var1] : (24.0 - pj.S[this.S[var1]][1] - this.n[var1]) / 16.0 * 0.9375;
   }

   private static double q(double var0) {
      return 0.12 * pj.Z(var0 / 0.16);
   }

   private static double m(int var0, double var1) {
      double var3 = pj.Z(var1 / 0.16) * 0.22;
      return var0 == 2 ? -var3 : (var0 == 3 ? var3 : 0.0);
   }

   private static double f(double var0) {
      return 0.016 * Math.sin(var0 * 70.0) * pj.Z((var0 - 0.95) / 0.25) * (1.0 - pj.Z((var0 - 1.32 + 0.07) / 0.07));
   }

   public static pj.n v(pj var0, int var1, double var2) {
      var2 = Math.min(var2, 1.32);
      double var4 = q(var2);
      double var6 = pj.R(var1, var0.l) / 16.0 * 0.9375;
      double var8 = (24.0 - pj.S[var1][1]) / 16.0 * 0.9375;
      double var10 = Math.cos(var0.z);
      double var12 = Math.sin(var0.z);
      double var14 = var8 * Math.sin(var4);
      Vec3 var16 = var0.v.add(var6 * var10 + var14 * var12, var8 * Math.cos(var4), var6 * var12 - var14 * var10);
      return new pj.n(var16, var4, f(var2), m(var1, var2), 1.0, 1.0, 0.0, false);
   }

   static double l() {
      return 0.12;
   }

   static double I(int var0) {
      return var0 == 2 ? -0.22 : (var0 == 3 ? 0.22 : 0.0);
   }

   static Vec3 a(pj var0, int var1) {
      double var2 = var1 * Math.PI * 2.0 / 6.0 + var0.F(var1 + 14000) * 0.8;
      double var4 = 0.9 + 0.7 * var0.F(var1 + 14200);
      return new Vec3(Math.cos(var2) * var4, 0.5 + 0.8 * var0.F(var1 + 14400), Math.sin(var2) * var4);
   }

   private Vec3 i(int var1, double var2, Vector3f var4) {
      if (this.S[var1] == -1) {
         var4.set(this.F[var1], this.u[var1], this.p[var1]).normalize();
         return new Vec3(this.x.v.x + this.T[var1], this.x.a().C() + 0.02, this.x.v.z + this.Y[var1]);
      } else if (this.S[var1] == -2) {
         var4.set(0.0F, 1.0F, 0.0F);
         return this.x.v.add(this.T[var1], this.n[var1], this.Y[var1]);
      } else {
         pj.n var5 = v(this.x, this.S[var1], var2);
         this.U.identity().rotateY((float)(Math.PI - this.x.z)).rotateXYZ((float)var5.rx(), (float)var5.ry(), (float)var5.rz());
         double var6 = 0.05859375;
         this.U.transform(this.G.set((float)(-this.T[var1] * var6), (float)(-this.n[var1] * var6), (float)(this.Y[var1] * var6)));
         this.U.transform(var4.set((float)(-this.F[var1]), (float)(-this.u[var1]), (float)this.p[var1]));
         return var5.position().add(this.G.x, this.G.y, this.G.z);
      }
   }

   private static double U(Vector3f var0) {
      return Math.acos(Math.clamp(var0.y, -1.0F, 1.0F));
   }

   private static double u(Vector3f var0) {
      return Math.atan2(var0.x, var0.z);
   }

   public se.t L(int var1, double var2) {
      Vector3f var4 = new Vector3f();
      double var5 = 1.0 - pj.Z((var2 - 3.45 - 0.45 * this.D[var1]) / 0.6);
      if (var2 < 1.32) {
         double var59 = this.S[var1] == -2 ? 0.0 : pj.Z((var2 - this.k[var1]) / 0.28);
         if (var59 <= 0.0) {
            return null;
         }

         Vec3 var9 = this.i(var1, var2, var4);
         double var60 = u(var4) + (this.Q[var1] - 0.5) * 0.7;
         double var61 = U(var4) + (this.D[var1] - 0.5) * 0.6;
         double var62 = this.d[var1] * var59;
         return new se.t(
            var9.add(var4.x * var62 * 0.35, var4.y * var62 * 0.35, var4.z * var62 * 0.35), var60, var61, (this.Q[var1] - 0.5) * 0.4, var62, 1.0, false
         );
      } else {
         Vec3 var7 = this.i(var1, 1.32, var4);
         if (this.S[var1] != -2) {
            var7 = var7.add(var4.x * this.d[var1] * 0.35, var4.y * this.d[var1] * 0.35, var4.z * this.d[var1] * 0.35);
         }

         double var8 = u(var4) + (this.Q[var1] - 0.5) * 0.7;
         double var10 = U(var4) + (this.D[var1] - 0.5) * 0.6;
         double var12 = (this.Q[var1] - 0.5) * 0.4;
         double var14 = var7.x - this.x.v.x;
         double var16 = var7.z - this.x.v.z;
         double var18 = Math.hypot(var14, var16);
         if (var18 < 1.0E-4) {
            double var20 = this.Q[var1] * Math.PI * 2.0;
            var14 = Math.cos(var20);
            var16 = Math.sin(var20);
            var18 = 1.0;
         }

         double var63 = (this.D[var1] - 0.5) * 1.2;
         double var22 = var14 / var18 * this.h[var1] - var16 / var18 * var63;
         double var24 = var16 / var18 * this.h[var1] + var14 / var18 * var63;
         double var26 = this.t[var1];
         double var28 = this.S[var1] == -2 ? this.d[var1] * Math.min(1.0, (var2 - 1.32) * 8.0) : this.d[var1];
         double var30 = Math.PI / 2;
         double var32 = ke.F(var28, var30, 0.0);
         double var34 = this.x.a().C();
         double var36 = l(var7.y, var26, var34 + var32);

         for (int var38 = 0; var38 < 2; var38++) {
            double var39 = this.x.a().R(var7.x + var22 * var36, var7.z + var24 * var36);
            var34 = Double.isNaN(var39) ? this.x.a().C() - 4.5 : var39;
            var36 = l(var7.y, var26, var34 + var32);
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
            var47 = pj.Z(var49 / var42);
         } else {
            var44 = new Vec3(var7.x + var22 * (var36 + 0.45 * var42), var34 + var32, var7.z + var24 * (var36 + 0.45 * var42));
            var47 = 1.0;
         }

         double var65 = var8 + this.q[var1] * var45;
         double var51 = var10 + this.V[var1] * var45;
         double var53 = var12 + this.r[var1] * var45;
         double var55 = Math.round((var51 - var30) / Math.PI) * Math.PI + var30;
         double var57 = Math.round(var53 / Math.PI) * Math.PI;
         var51 += (var55 - var51) * var47;
         var53 += (var57 - var53) * var47;
         if (var47 > 0.0) {
            var44 = var44.add(0.0, (ke.F(var28, var55, var57) - var32) * var47, 0.0);
         }

         return new se.t(var44, var65, var51, var53, var28, var5, var47 >= 1.0);
      }
   }

   static double l(double var0, double var2, double var4) {
      double var6 = Math.max(0.0, var0 - var4);
      return (var2 + Math.sqrt(var2 * var2 + 19.6 * var6)) / 9.8;
   }

   public static void K(kj var0, pj var1, double var2, int var4, int var5, double var6, boolean var8) {
      se var9 = var1.V();
      if (var9 != null) {
         var9.G(var0, var2, var4, var5, var6, var8);
      }
   }

   private void G(kj var1, double var2, int var4, int var5, double var6, boolean var8) {
      Vec3 var9 = new Vec3(this.x.v.x, this.x.a().C(), this.x.v.z);
      int var10 = uh.q(var4, var5, 0.45);
      int var11 = uh.q(var5, -1, 0.5);
      double var12 = pj.T(var2, 0.0, 0.35) * var6;
      c(var1, this.x.v.add(0.0, 1.15, 0.0), 0.35 + var2 * 0.9, uh.F(var11, var12 * 0.8));
      double var14 = pj.Z((var2 - 0.05) / 0.7);
      double var16 = 1.0 - pj.Z((var2 - 1.32 - 0.4) / 2.4);
      double var18 = var14 * var16 * var6;
      if (var18 > 0.001) {
         uu.l(var1, var9.add(0.0, 0.04, 0.0), 0.3 + 1.25 * var14, uh.F(var4, var18 * 0.32));
         var1.F(var9.x, var9.y + 0.045, var9.z, 0.3 + 1.25 * var14, 0.12, uh.F(var11, var18 * 0.6));
      }

      double var20 = pj.Z((var2 - 0.95) / 0.29000000000000004);
      double var22 = pj.T(var2, 0.95, 1.37) * var6;
      if (var22 > 0.001) {
         for (int var24 = 0; var24 < 8; var24++) {
            this.R(var1, var24, Math.min(var2, 1.32), var20, uh.F(var11, var22));
         }
      }

      double var41 = pj.T(var2, 0.9700000000000001, 1.3800000000000001) * var6;
      c(var1, this.x.v.add(0.0, 1.05, 0.0), 0.25 + 0.5 * pj.Z((var2 - 1.32 + 0.35) / 0.35), uh.F(var11, var41 * 0.7));
      double var26 = var2 - 1.32;
      if (var26 >= 0.0) {
         double var28 = pj.T(var26, 0.0, 0.5) * var6;
         c(var1, this.x.v.add(0.0, 1.0, 0.0), 1.0 + var26 * 2.2, uh.F(var11, var28));
         if (var26 < 0.7) {
            var1.F(
               var9.x, var9.y + 0.06, var9.z, 0.3 + 3.4 * (1.0 - Math.pow(1.0 - var26 / 0.7, 3.0)), 0.3, uh.F(var11, Math.pow(1.0 - var26 / 0.7, 1.4) * var6)
            );
         }

         if (var26 < 1.3) {
            uu.l(var1, var9, 0.3 + 2.6 * (1.0 - Math.pow(1.0 - var26 / 1.3, 3.0)), 0.5, uh.F(var4, (1.0 - var26 / 1.3) * 0.35 * var6));
         }

         double var30 = pj.T(var26, 0.0, 2.3) * var6;

         for (int var32 = 0; var32 < 40 && var30 > 0.001; var32++) {
            double var33 = this.x.F(var32 + 15000) * Math.PI * 2.0;
            double var35 = 0.2 + 1.6 * this.x.F(var32 + 15200) * (1.0 - Math.exp(-var26 * 2.5));
            double var37 = 0.2 + 1.7 * this.x.F(var32 + 15400) + var26 * (0.5 - 0.45 * var26) * (1.0 + this.x.F(var32 + 15600));
            double var39 = 0.45 + 0.55 * Math.pow(Math.max(0.0, Math.sin(var2 * 6.0 + var32 * 1.9)), 4.0);
            var1.P(
               this.x.v.x + Math.cos(var33) * var35,
               this.x.v.y + Math.max(0.05, var37),
               this.x.v.z + Math.sin(var33) * var35,
               0.022 + 0.02 * this.x.F(var32 + 15800),
               var33 + var2,
               uh.F(var11, var30 * var39)
            );
         }
      }

      var1.blend(var8 ? gl.c.E : gl.c.K);

      for (int var42 = 0; var42 < 102; var42++) {
         se.t var29 = this.L(var42, var2);
         if (var29 != null && !(var29.alpha() <= 0.001) && !(var29.size() <= 0.001)) {
            double var45 = var29.alpha() * (var8 ? 0.3 * var6 : 0.82);
            var1.b(
               var29.position().x,
               var29.position().y,
               var29.position().z,
               var29.size(),
               var29.yaw(),
               var29.pitch(),
               var29.roll(),
               uh.F(var8 ? var11 : var10, var45)
            );
         }
      }

      var1.blend(gl.c.E);
      if (var26 > 0.3) {
         for (byte var43 = 0; var43 < 102; var43 += 3) {
            se.t var44 = this.L(var43, var2);
            if (var44 != null && var44.rest()) {
               double var46 = Math.pow(Math.max(0.0, Math.sin(var2 * 4.5 + var43 * 1.7)), 12.0) * var44.alpha() * var6;
               if (var46 > 0.02) {
                  var1.P(var44.position().x, var44.position().y + 0.04, var44.position().z, 0.07, var2, uh.F(-1, var46));
               }
            }
         }
      }
   }

   private void R(kj var1, int var2, double var3, double var5, int var7) {
      int var8 = N[var2][0];
      int var9 = N[var2][1];
      pj.n var10 = v(this.x, var8, var3);
      this.U.identity().rotateY((float)(Math.PI - this.x.z)).rotateXYZ((float)var10.rx(), (float)var10.ry(), (float)var10.rz());
      double[] var11 = y[var8];
      double var12 = 0.05859375;
      double var14 = (var11[2] + 0.7) * var9;
      double var16 = (this.x.F(var2 + 16000) - 0.5) * var11[0];
      double var18 = (this.x.F(var2 + 16200) - 0.5) * var11[1] * 0.6;
      byte var20 = 5;
      Vec3 var21 = null;

      for (int var22 = 0; var22 <= var20; var22++) {
         double var23 = (double)var22 / var20;
         if (var23 > var5 + 1.0E-9) {
            break;
         }

         double var25 = var16 + (this.x.F(var2 * 8 + var22 + 16400) - 0.5) * var11[0] * 0.9;
         double var27 = var18 + (var23 - 0.5) * var11[1] * 1.6 * (this.x.F(var2 + 16600) < 0.5 ? 1 : -1);
         this.U
            .transform(
               this.G
                  .set(
                     (float)(-Math.clamp(var25, -var11[0], var11[0]) * var12), (float)(-Math.clamp(var27, -var11[1], var11[1]) * var12), (float)(var14 * var12)
                  )
            );
         Vec3 var29 = var10.position().add(this.G.x, this.G.y, this.G.z);
         if (var21 != null) {
            var1.ribbon(var21, var29, 0.028, var7, var7);
         }

         var21 = var29;
      }
   }

   private static void c(kj var0, Vec3 var1, double var2, int var4) {
      if (var4 >>> 24 > 0) {
         var0.I(var1.x, var1.y, var1.z, var2, 0.0, var4, c3.a.Z, 0.0, 0.0, 0.0);
      }
   }

   public record t(Vec3 position, double yaw, double pitch, double roll, double size, double alpha, boolean rest) {
   }
}
