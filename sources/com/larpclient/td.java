package com.larpclient;

import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class td {
   public static final double V = 0.16;
   public static final double K = 0.95;
   public static final double T = 1.32;
   public static final double z = 2.7;
   public static final double P = 4.6;
   public static final int E = 48;
   public static final int h = 14;
   public static final int i = 40;
   public static final int N = 8;
   public static final int D = 40;
   public static final int L = 102;
   static final double B = 0.12;
   static final double q = 9.8;
   private static final double[][] u = new double[][]{{4.0, 4.0, 4.0}, {4.0, 6.0, 2.0}, {2.0, 6.0, 2.0}, {2.0, 6.0, 2.0}, {2.0, 6.0, 2.0}, {2.0, 6.0, 2.0}};
   private static final int[] G = new int[]{6, 9, 5, 5, 5, 5};
   private static final int[][] k = new int[][]{{1, 1}, {1, -1}, {4, 1}, {5, 1}, {2, 1}, {3, -1}, {0, 1}, {5, -1}};
   private final ta x;
   private final int[] r = new int[102];
   private final double[] J = new double[102];
   private final double[] n = new double[102];
   private final double[] a = new double[102];
   private final double[] H = new double[102];
   private final double[] g = new double[102];
   private final double[] U = new double[102];
   private final double[] Z = new double[102];
   private final double[] F = new double[102];
   private final double[] f = new double[102];
   private final double[] I = new double[102];
   private final double[] o = new double[102];
   private final double[] d = new double[102];
   private final double[] j = new double[102];
   private final double[] s = new double[102];
   private final double[] e = new double[102];
   private final Quaternionf t = new Quaternionf();
   private final Vector3f v = new Vector3f();

   td(ta var1, mz var2) {
      this.x = var1;
      int var3 = 0;

      for (int var7 : G) {
         var3 += var7;
      }

      for (int var17 = 0; var17 < 102; var17++) {
         this.Z[var17] = 0.09 + 0.11 * var1.W(var17 + 11000);
         this.o[var17] = (var1.W(var17 + 11200) - 0.5) * 9.0;
         this.d[var17] = (var1.W(var17 + 11400) - 0.5) * 11.0;
         this.j[var17] = (var1.W(var17 + 11600) - 0.5) * 7.0;
         this.f[var17] = 1.2 + 1.4 * var1.W(var17 + 11800);
         this.I[var17] = 1.0 + 2.0 * var1.W(var17 + 12000);
         this.s[var17] = var1.W(var17 + 12200);
         this.e[var17] = var1.W(var17 + 12400);
         if (var17 >= 48) {
            if (var17 < 62) {
               int var19 = var17 - 48;
               double var22 = (var19 + 0.5 * var1.W(var17 + 13400)) * Math.PI * 2.0 / 14.0;
               double var25 = 0.45 + 0.6 * var1.W(var17 + 13600);
               this.r[var17] = -1;
               this.J[var17] = Math.cos(var22) * var25;
               this.a[var17] = Math.sin(var22) * var25;
               this.H[var17] = Math.cos(var22) * 0.55;
               this.g[var17] = 1.0;
               this.U[var17] = Math.sin(var22) * 0.55;
               this.Z[var17] = 0.12 + 0.18 * var1.W(var17 + 13800);
               this.f[var17] = this.f[var17] * 0.6;
               this.I[var17] = this.I[var17] * 0.55;
            } else {
               this.r[var17] = -2;
               double var20 = var1.W(var17 + 13400) * Math.PI * 2.0;
               double var24 = 0.25 * Math.sqrt(var1.W(var17 + 13600));
               this.J[var17] = Math.cos(var20) * var24;
               this.a[var17] = Math.sin(var20) * var24;
               this.n[var17] = 0.1 + 1.7 * var1.W(var17 + 13800);
               this.Z[var17] = 0.05 + 0.07 * var1.W(var17 + 11000);
            }
         } else {
            int var18 = (int)(var1.W(var17 + 12600) * var3);
            int var21 = 0;

            while (var18 >= G[var21]) {
               var18 -= G[var21++];
            }

            this.r[var17] = var21;
            double[] var23 = u[var21];
            double var8 = var21 >= 2 && var21 <= 3 && var1.T ? 1.5 : var23[0];
            int var10 = (int)(var1.W(var17 + 12800) * (var21 == 0 ? 5 : 4));
            double var11 = var1.W(var17 + 13000) * 2.0 - 1.0;
            double var13 = var1.W(var17 + 13200) * 2.0 - 1.0;
            double var15 = 0.5;
            switch (var10) {
               case 0:
                  this.J[var17] = var8 + var15;
                  this.n[var17] = var13 * var23[1];
                  this.a[var17] = var11 * var23[2];
                  this.H[var17] = 1.0;
                  break;
               case 1:
                  this.J[var17] = -var8 - var15;
                  this.n[var17] = var13 * var23[1];
                  this.a[var17] = var11 * var23[2];
                  this.H[var17] = -1.0;
                  break;
               case 2:
                  this.a[var17] = var23[2] + var15;
                  this.n[var17] = var13 * var23[1];
                  this.J[var17] = var11 * var8;
                  this.U[var17] = 1.0;
                  break;
               case 3:
                  this.a[var17] = -var23[2] - var15;
                  this.n[var17] = var13 * var23[1];
                  this.J[var17] = var11 * var8;
                  this.U[var17] = -1.0;
                  break;
               default:
                  this.n[var17] = -var23[1] - var15;
                  this.J[var17] = var11 * var8;
                  this.a[var17] = var13 * var23[2];
                  this.g[var17] = -1.0;
            }
         }

         this.F[var17] = this.r[var17] == -1
            ? 0.05 + 0.45 * Math.hypot(this.J[var17], this.a[var17]) / 1.05
            : 0.06 + 0.7 * Math.clamp(this.O(var17) / 1.9, 0.0, 1.0) + 0.08 * this.s[var17];
      }

      var2.H(var1.J.x, var1.J.z);
      var2.M(var1.J.x, var1.J.z, 4.0);
   }

   private double O(int var1) {
      return this.r[var1] < 0 ? this.n[var1] : (24.0 - ta.Q[this.r[var1]][1] - this.n[var1]) / 16.0 * 0.9375;
   }

   private static double B(double var0) {
      return 0.12 * ta.k(var0 / 0.16);
   }

   private static double E(int var0, double var1) {
      double var3 = ta.k(var1 / 0.16) * 0.22;
      return var0 == 2 ? -var3 : (var0 == 3 ? var3 : 0.0);
   }

   private static double g(double var0) {
      return 0.016 * Math.sin(var0 * 70.0) * ta.k((var0 - 0.95) / 0.25) * (1.0 - ta.k((var0 - 1.32 + 0.07) / 0.07));
   }

   public static ta.y q(ta var0, int var1, double var2) {
      var2 = Math.min(var2, 1.32);
      double var4 = B(var2);
      double var6 = ta.Z(var1, var0.T) / 16.0 * 0.9375;
      double var8 = (24.0 - ta.Q[var1][1]) / 16.0 * 0.9375;
      double var10 = Math.cos(var0.v);
      double var12 = Math.sin(var0.v);
      double var14 = var8 * Math.sin(var4);
      Vec3 var16 = var0.J.add(var6 * var10 + var14 * var12, var8 * Math.cos(var4), var6 * var12 - var14 * var10);
      return new ta.y(var16, var4, g(var2), E(var1, var2), 1.0, 1.0, 0.0, false);
   }

   static double P() {
      return 0.12;
   }

   static double L(int var0) {
      return var0 == 2 ? -0.22 : (var0 == 3 ? 0.22 : 0.0);
   }

   static Vec3 d(ta var0, int var1) {
      double var2 = var1 * Math.PI * 2.0 / 6.0 + var0.W(var1 + 14000) * 0.8;
      double var4 = 0.9 + 0.7 * var0.W(var1 + 14200);
      return new Vec3(Math.cos(var2) * var4, 0.5 + 0.8 * var0.W(var1 + 14400), Math.sin(var2) * var4);
   }

   private Vec3 r(int var1, double var2, Vector3f var4) {
      if (this.r[var1] == -1) {
         var4.set(this.H[var1], this.g[var1], this.U[var1]).normalize();
         return new Vec3(this.x.J.x + this.J[var1], this.x.L().X() + 0.02, this.x.J.z + this.a[var1]);
      } else if (this.r[var1] == -2) {
         var4.set(0.0F, 1.0F, 0.0F);
         return this.x.J.add(this.J[var1], this.n[var1], this.a[var1]);
      } else {
         ta.y var5 = q(this.x, this.r[var1], var2);
         this.t.identity().rotateY((float)(Math.PI - this.x.v)).rotateXYZ((float)var5.rx(), (float)var5.ry(), (float)var5.rz());
         double var6 = 0.05859375;
         this.t.transform(this.v.set((float)(-this.J[var1] * var6), (float)(-this.n[var1] * var6), (float)(this.a[var1] * var6)));
         this.t.transform(var4.set((float)(-this.H[var1]), (float)(-this.g[var1]), (float)this.U[var1]));
         return var5.position().add(this.v.x, this.v.y, this.v.z);
      }
   }

   private static double s(Vector3f var0) {
      return Math.acos(Math.clamp(var0.y, -1.0F, 1.0F));
   }

   private static double A(Vector3f var0) {
      return Math.atan2(var0.x, var0.z);
   }

   public td.p i(int var1, double var2) {
      Vector3f var4 = new Vector3f();
      double var5 = 1.0 - ta.k((var2 - 3.45 - 0.45 * this.e[var1]) / 0.6);
      if (var2 < 1.32) {
         double var59 = this.r[var1] == -2 ? 0.0 : ta.k((var2 - this.F[var1]) / 0.28);
         if (var59 <= 0.0) {
            return null;
         }

         Vec3 var9 = this.r(var1, var2, var4);
         double var60 = A(var4) + (this.s[var1] - 0.5) * 0.7;
         double var61 = s(var4) + (this.e[var1] - 0.5) * 0.6;
         double var62 = this.Z[var1] * var59;
         return new td.p(
            var9.add(var4.x * var62 * 0.35, var4.y * var62 * 0.35, var4.z * var62 * 0.35), var60, var61, (this.s[var1] - 0.5) * 0.4, var62, 1.0, false
         );
      } else {
         Vec3 var7 = this.r(var1, 1.32, var4);
         if (this.r[var1] != -2) {
            var7 = var7.add(var4.x * this.Z[var1] * 0.35, var4.y * this.Z[var1] * 0.35, var4.z * this.Z[var1] * 0.35);
         }

         double var8 = A(var4) + (this.s[var1] - 0.5) * 0.7;
         double var10 = s(var4) + (this.e[var1] - 0.5) * 0.6;
         double var12 = (this.s[var1] - 0.5) * 0.4;
         double var14 = var7.x - this.x.J.x;
         double var16 = var7.z - this.x.J.z;
         double var18 = Math.hypot(var14, var16);
         if (var18 < 1.0E-4) {
            double var20 = this.s[var1] * Math.PI * 2.0;
            var14 = Math.cos(var20);
            var16 = Math.sin(var20);
            var18 = 1.0;
         }

         double var63 = (this.e[var1] - 0.5) * 1.2;
         double var22 = var14 / var18 * this.f[var1] - var16 / var18 * var63;
         double var24 = var16 / var18 * this.f[var1] + var14 / var18 * var63;
         double var26 = this.I[var1];
         double var28 = this.r[var1] == -2 ? this.Z[var1] * Math.min(1.0, (var2 - 1.32) * 8.0) : this.Z[var1];
         double var30 = Math.PI / 2;
         double var32 = co.l(var28, var30, 0.0);
         double var34 = this.x.L().X();
         double var36 = g(var7.y, var26, var34 + var32);

         for (int var38 = 0; var38 < 2; var38++) {
            double var39 = this.x.L().H(var7.x + var22 * var36, var7.z + var24 * var36);
            var34 = Double.isNaN(var39) ? this.x.L().X() - 4.5 : var39;
            var36 = g(var7.y, var26, var34 + var32);
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
            var47 = ta.k(var49 / var42);
         } else {
            var44 = new Vec3(var7.x + var22 * (var36 + 0.45 * var42), var34 + var32, var7.z + var24 * (var36 + 0.45 * var42));
            var47 = 1.0;
         }

         double var65 = var8 + this.o[var1] * var45;
         double var51 = var10 + this.d[var1] * var45;
         double var53 = var12 + this.j[var1] * var45;
         double var55 = Math.round((var51 - var30) / Math.PI) * Math.PI + var30;
         double var57 = Math.round(var53 / Math.PI) * Math.PI;
         var51 += (var55 - var51) * var47;
         var53 += (var57 - var53) * var47;
         if (var47 > 0.0) {
            var44 = var44.add(0.0, (co.l(var28, var55, var57) - var32) * var47, 0.0);
         }

         return new td.p(var44, var65, var51, var53, var28, var5, var47 >= 1.0);
      }
   }

   static double g(double var0, double var2, double var4) {
      double var6 = Math.max(0.0, var0 - var4);
      return (var2 + Math.sqrt(var2 * var2 + 19.6 * var6)) / 9.8;
   }

   public static void x(ki var0, ta var1, double var2, int var4, int var5, double var6, boolean var8) {
      td var9 = var1.V();
      if (var9 != null) {
         var9.W(var0, var2, var4, var5, var6, var8);
      }
   }

   private void W(ki var1, double var2, int var4, int var5, double var6, boolean var8) {
      Vec3 var9 = new Vec3(this.x.J.x, this.x.L().X(), this.x.J.z);
      int var10 = _3.D(var4, var5, 0.45);
      int var11 = _3.D(var5, -1, 0.5);
      double var12 = ta.H(var2, 0.0, 0.35) * var6;
      i(var1, this.x.J.add(0.0, 1.15, 0.0), 0.35 + var2 * 0.9, _3.U(var11, var12 * 0.8));
      double var14 = ta.k((var2 - 0.05) / 0.7);
      double var16 = 1.0 - ta.k((var2 - 1.32 - 0.4) / 2.4);
      double var18 = var14 * var16 * var6;
      if (var18 > 0.001) {
         hh.F(var1, var9.add(0.0, 0.04, 0.0), 0.3 + 1.25 * var14, _3.U(var4, var18 * 0.32));
         var1.H(var9.x, var9.y + 0.045, var9.z, 0.3 + 1.25 * var14, 0.12, _3.U(var11, var18 * 0.6));
      }

      double var20 = ta.k((var2 - 0.95) / 0.29000000000000004);
      double var22 = ta.H(var2, 0.95, 1.37) * var6;
      if (var22 > 0.001) {
         for (int var24 = 0; var24 < 8; var24++) {
            this.X(var1, var24, Math.min(var2, 1.32), var20, _3.U(var11, var22));
         }
      }

      double var41 = ta.H(var2, 0.9700000000000001, 1.3800000000000001) * var6;
      i(var1, this.x.J.add(0.0, 1.05, 0.0), 0.25 + 0.5 * ta.k((var2 - 1.32 + 0.35) / 0.35), _3.U(var11, var41 * 0.7));
      double var26 = var2 - 1.32;
      if (var26 >= 0.0) {
         double var28 = ta.H(var26, 0.0, 0.5) * var6;
         i(var1, this.x.J.add(0.0, 1.0, 0.0), 1.0 + var26 * 2.2, _3.U(var11, var28));
         if (var26 < 0.7) {
            var1.H(
               var9.x, var9.y + 0.06, var9.z, 0.3 + 3.4 * (1.0 - Math.pow(1.0 - var26 / 0.7, 3.0)), 0.3, _3.U(var11, Math.pow(1.0 - var26 / 0.7, 1.4) * var6)
            );
         }

         if (var26 < 1.3) {
            hh.J(var1, var9, 0.3 + 2.6 * (1.0 - Math.pow(1.0 - var26 / 1.3, 3.0)), 0.5, _3.U(var4, (1.0 - var26 / 1.3) * 0.35 * var6));
         }

         double var30 = ta.H(var26, 0.0, 2.3) * var6;

         for (int var32 = 0; var32 < 40 && var30 > 0.001; var32++) {
            double var33 = this.x.W(var32 + 15000) * Math.PI * 2.0;
            double var35 = 0.2 + 1.6 * this.x.W(var32 + 15200) * (1.0 - Math.exp(-var26 * 2.5));
            double var37 = 0.2 + 1.7 * this.x.W(var32 + 15400) + var26 * (0.5 - 0.45 * var26) * (1.0 + this.x.W(var32 + 15600));
            double var39 = 0.45 + 0.55 * Math.pow(Math.max(0.0, Math.sin(var2 * 6.0 + var32 * 1.9)), 4.0);
            var1.t(
               this.x.J.x + Math.cos(var33) * var35,
               this.x.J.y + Math.max(0.05, var37),
               this.x.J.z + Math.sin(var33) * var35,
               0.022 + 0.02 * this.x.W(var32 + 15800),
               var33 + var2,
               _3.U(var11, var30 * var39)
            );
         }
      }

      var1.blend(var8 ? pf.x.E : pf.x.S);

      for (int var42 = 0; var42 < 102; var42++) {
         td.p var29 = this.i(var42, var2);
         if (var29 != null && !(var29.alpha() <= 0.001) && !(var29.size() <= 0.001)) {
            double var45 = var29.alpha() * (var8 ? 0.3 * var6 : 0.82);
            var1.k(
               var29.position().x,
               var29.position().y,
               var29.position().z,
               var29.size(),
               var29.yaw(),
               var29.pitch(),
               var29.roll(),
               _3.U(var8 ? var11 : var10, var45)
            );
         }
      }

      var1.blend(pf.x.E);
      if (var26 > 0.3) {
         for (byte var43 = 0; var43 < 102; var43 += 3) {
            td.p var44 = this.i(var43, var2);
            if (var44 != null && var44.rest()) {
               double var46 = Math.pow(Math.max(0.0, Math.sin(var2 * 4.5 + var43 * 1.7)), 12.0) * var44.alpha() * var6;
               if (var46 > 0.02) {
                  var1.t(var44.position().x, var44.position().y + 0.04, var44.position().z, 0.07, var2, _3.U(-1, var46));
               }
            }
         }
      }
   }

   private void X(ki var1, int var2, double var3, double var5, int var7) {
      int var8 = k[var2][0];
      int var9 = k[var2][1];
      ta.y var10 = q(this.x, var8, var3);
      this.t.identity().rotateY((float)(Math.PI - this.x.v)).rotateXYZ((float)var10.rx(), (float)var10.ry(), (float)var10.rz());
      double[] var11 = u[var8];
      double var12 = 0.05859375;
      double var14 = (var11[2] + 0.7) * var9;
      double var16 = (this.x.W(var2 + 16000) - 0.5) * var11[0];
      double var18 = (this.x.W(var2 + 16200) - 0.5) * var11[1] * 0.6;
      byte var20 = 5;
      Vec3 var21 = null;

      for (int var22 = 0; var22 <= var20; var22++) {
         double var23 = (double)var22 / var20;
         if (var23 > var5 + 1.0E-9) {
            break;
         }

         double var25 = var16 + (this.x.W(var2 * 8 + var22 + 16400) - 0.5) * var11[0] * 0.9;
         double var27 = var18 + (var23 - 0.5) * var11[1] * 1.6 * (this.x.W(var2 + 16600) < 0.5 ? 1 : -1);
         this.t
            .transform(
               this.v
                  .set(
                     (float)(-Math.clamp(var25, -var11[0], var11[0]) * var12), (float)(-Math.clamp(var27, -var11[1], var11[1]) * var12), (float)(var14 * var12)
                  )
            );
         Vec3 var29 = var10.position().add(this.v.x, this.v.y, this.v.z);
         if (var21 != null) {
            var1.ribbon(var21, var29, 0.028, var7, var7);
         }

         var21 = var29;
      }
   }

   private static void i(ki var0, Vec3 var1, double var2, int var4) {
      if (var4 >>> 24 > 0) {
         var0.W(var1.x, var1.y, var1.z, var2, 0.0, var4, cg.u.a, 0.0, 0.0, 0.0);
      }
   }

   public record p(Vec3 position, double yaw, double pitch, double roll, double size, double alpha, boolean rest) {
   }
}
