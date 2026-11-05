package com.larpclient;

import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class x3 {
   public static final double v = 0.16;
   public static final double a = 0.95;
   public static final double x = 1.32;
   public static final double V = 2.7;
   public static final double I = 4.6;
   public static final int c = 48;
   public static final int R = 14;
   public static final int w = 40;
   public static final int y = 8;
   public static final int J = 40;
   public static final int g = 102;
   static final double C = 0.12;
   static final double X = 9.8;
   private static final double[][] L = new double[][]{{4.0, 4.0, 4.0}, {4.0, 6.0, 2.0}, {2.0, 6.0, 2.0}, {2.0, 6.0, 2.0}, {2.0, 6.0, 2.0}, {2.0, 6.0, 2.0}};
   private static final int[] H = new int[]{6, 9, 5, 5, 5, 5};
   private static final int[][] Q = new int[][]{{1, 1}, {1, -1}, {4, 1}, {5, 1}, {2, 1}, {3, -1}, {0, 1}, {5, -1}};
   private final ez S;
   private final int[] s = new int[102];
   private final double[] j = new double[102];
   private final double[] U = new double[102];
   private final double[] b = new double[102];
   private final double[] f = new double[102];
   private final double[] u = new double[102];
   private final double[] O = new double[102];
   private final double[] E = new double[102];
   private final double[] Y = new double[102];
   private final double[] W = new double[102];
   private final double[] n = new double[102];
   private final double[] k = new double[102];
   private final double[] r = new double[102];
   private final double[] B = new double[102];
   private final double[] o = new double[102];
   private final double[] Z = new double[102];
   private final Quaternionf N = new Quaternionf();
   private final Vector3f d = new Vector3f();

   x3(ez var1, ur var2) {
      this.S = var1;
      int var3 = 0;

      for (int var7 : H) {
         var3 += var7;
      }

      for (int var17 = 0; var17 < 102; var17++) {
         this.E[var17] = 0.09 + 0.11 * var1.L(var17 + 11000);
         this.k[var17] = (var1.L(var17 + 11200) - 0.5) * 9.0;
         this.r[var17] = (var1.L(var17 + 11400) - 0.5) * 11.0;
         this.B[var17] = (var1.L(var17 + 11600) - 0.5) * 7.0;
         this.W[var17] = 1.2 + 1.4 * var1.L(var17 + 11800);
         this.n[var17] = 1.0 + 2.0 * var1.L(var17 + 12000);
         this.o[var17] = var1.L(var17 + 12200);
         this.Z[var17] = var1.L(var17 + 12400);
         if (var17 >= 48) {
            if (var17 < 62) {
               int var19 = var17 - 48;
               double var22 = (var19 + 0.5 * var1.L(var17 + 13400)) * Math.PI * 2.0 / 14.0;
               double var25 = 0.45 + 0.6 * var1.L(var17 + 13600);
               this.s[var17] = -1;
               this.j[var17] = Math.cos(var22) * var25;
               this.b[var17] = Math.sin(var22) * var25;
               this.f[var17] = Math.cos(var22) * 0.55;
               this.u[var17] = 1.0;
               this.O[var17] = Math.sin(var22) * 0.55;
               this.E[var17] = 0.12 + 0.18 * var1.L(var17 + 13800);
               this.W[var17] = this.W[var17] * 0.6;
               this.n[var17] = this.n[var17] * 0.55;
            } else {
               this.s[var17] = -2;
               double var20 = var1.L(var17 + 13400) * Math.PI * 2.0;
               double var24 = 0.25 * Math.sqrt(var1.L(var17 + 13600));
               this.j[var17] = Math.cos(var20) * var24;
               this.b[var17] = Math.sin(var20) * var24;
               this.U[var17] = 0.1 + 1.7 * var1.L(var17 + 13800);
               this.E[var17] = 0.05 + 0.07 * var1.L(var17 + 11000);
            }
         } else {
            int var18 = (int)(var1.L(var17 + 12600) * var3);
            int var21 = 0;

            while (var18 >= H[var21]) {
               var18 -= H[var21++];
            }

            this.s[var17] = var21;
            double[] var23 = L[var21];
            double var8 = var21 >= 2 && var21 <= 3 && var1.d ? 1.5 : var23[0];
            int var10 = (int)(var1.L(var17 + 12800) * (var21 == 0 ? 5 : 4));
            double var11 = var1.L(var17 + 13000) * 2.0 - 1.0;
            double var13 = var1.L(var17 + 13200) * 2.0 - 1.0;
            double var15 = 0.5;
            switch (var10) {
               case 0:
                  this.j[var17] = var8 + var15;
                  this.U[var17] = var13 * var23[1];
                  this.b[var17] = var11 * var23[2];
                  this.f[var17] = 1.0;
                  break;
               case 1:
                  this.j[var17] = -var8 - var15;
                  this.U[var17] = var13 * var23[1];
                  this.b[var17] = var11 * var23[2];
                  this.f[var17] = -1.0;
                  break;
               case 2:
                  this.b[var17] = var23[2] + var15;
                  this.U[var17] = var13 * var23[1];
                  this.j[var17] = var11 * var8;
                  this.O[var17] = 1.0;
                  break;
               case 3:
                  this.b[var17] = -var23[2] - var15;
                  this.U[var17] = var13 * var23[1];
                  this.j[var17] = var11 * var8;
                  this.O[var17] = -1.0;
                  break;
               default:
                  this.U[var17] = -var23[1] - var15;
                  this.j[var17] = var11 * var8;
                  this.b[var17] = var13 * var23[2];
                  this.u[var17] = -1.0;
            }
         }

         this.Y[var17] = this.s[var17] == -1
            ? 0.05 + 0.45 * Math.hypot(this.j[var17], this.b[var17]) / 1.05
            : 0.06 + 0.7 * Math.clamp(this.n(var17) / 1.9, 0.0, 1.0) + 0.08 * this.o[var17];
      }

      var2.I(var1.D.x, var1.D.z);
      var2.U(var1.D.x, var1.D.z, 4.0);
   }

   private double n(int var1) {
      return this.s[var1] < 0 ? this.U[var1] : (24.0 - ez.X[this.s[var1]][1] - this.U[var1]) / 16.0 * 0.9375;
   }

   private static double L(double var0) {
      return 0.12 * ez.N(var0 / 0.16);
   }

   private static double B(int var0, double var1) {
      double var3 = ez.N(var1 / 0.16) * 0.22;
      return var0 == 2 ? -var3 : (var0 == 3 ? var3 : 0.0);
   }

   private static double U(double var0) {
      return 0.016 * Math.sin(var0 * 70.0) * ez.N((var0 - 0.95) / 0.25) * (1.0 - ez.N((var0 - 1.32 + 0.07) / 0.07));
   }

   public static ez.q w(ez var0, int var1, double var2) {
      var2 = Math.min(var2, 1.32);
      double var4 = L(var2);
      double var6 = ez.k(var1, var0.d) / 16.0 * 0.9375;
      double var8 = (24.0 - ez.X[var1][1]) / 16.0 * 0.9375;
      double var10 = Math.cos(var0.A);
      double var12 = Math.sin(var0.A);
      double var14 = var8 * Math.sin(var4);
      Vec3 var16 = var0.D.add(var6 * var10 + var14 * var12, var8 * Math.cos(var4), var6 * var12 - var14 * var10);
      return new ez.q(var16, var4, U(var2), B(var1, var2), 1.0, 1.0, 0.0, false);
   }

   static double p() {
      return 0.12;
   }

   static double J(int var0) {
      return var0 == 2 ? -0.22 : (var0 == 3 ? 0.22 : 0.0);
   }

   static Vec3 e(ez var0, int var1) {
      double var2 = var1 * Math.PI * 2.0 / 6.0 + var0.L(var1 + 14000) * 0.8;
      double var4 = 0.9 + 0.7 * var0.L(var1 + 14200);
      return new Vec3(Math.cos(var2) * var4, 0.5 + 0.8 * var0.L(var1 + 14400), Math.sin(var2) * var4);
   }

   private Vec3 i(int var1, double var2, Vector3f var4) {
      if (this.s[var1] == -1) {
         var4.set(this.f[var1], this.u[var1], this.O[var1]).normalize();
         return new Vec3(this.S.D.x + this.j[var1], this.S.g().X() + 0.02, this.S.D.z + this.b[var1]);
      } else if (this.s[var1] == -2) {
         var4.set(0.0F, 1.0F, 0.0F);
         return this.S.D.add(this.j[var1], this.U[var1], this.b[var1]);
      } else {
         ez.q var5 = w(this.S, this.s[var1], var2);
         this.N.identity().rotateY((float)(Math.PI - this.S.A)).rotateXYZ((float)var5.rx(), (float)var5.ry(), (float)var5.rz());
         double var6 = 0.05859375;
         this.N.transform(this.d.set((float)(-this.j[var1] * var6), (float)(-this.U[var1] * var6), (float)(this.b[var1] * var6)));
         this.N.transform(var4.set((float)(-this.f[var1]), (float)(-this.u[var1]), (float)this.O[var1]));
         return var5.position().add(this.d.x, this.d.y, this.d.z);
      }
   }

   private static double B(Vector3f var0) {
      return Math.acos(Math.clamp(var0.y, -1.0F, 1.0F));
   }

   private static double R(Vector3f var0) {
      return Math.atan2(var0.x, var0.z);
   }

   public x3.a r(int var1, double var2) {
      Vector3f var4 = new Vector3f();
      double var5 = 1.0 - ez.N((var2 - 3.45 - 0.45 * this.Z[var1]) / 0.6);
      if (var2 < 1.32) {
         double var59 = this.s[var1] == -2 ? 0.0 : ez.N((var2 - this.Y[var1]) / 0.28);
         if (var59 <= 0.0) {
            return null;
         }

         Vec3 var9 = this.i(var1, var2, var4);
         double var60 = R(var4) + (this.o[var1] - 0.5) * 0.7;
         double var61 = B(var4) + (this.Z[var1] - 0.5) * 0.6;
         double var62 = this.E[var1] * var59;
         return new x3.a(
            var9.add(var4.x * var62 * 0.35, var4.y * var62 * 0.35, var4.z * var62 * 0.35), var60, var61, (this.o[var1] - 0.5) * 0.4, var62, 1.0, false
         );
      } else {
         Vec3 var7 = this.i(var1, 1.32, var4);
         if (this.s[var1] != -2) {
            var7 = var7.add(var4.x * this.E[var1] * 0.35, var4.y * this.E[var1] * 0.35, var4.z * this.E[var1] * 0.35);
         }

         double var8 = R(var4) + (this.o[var1] - 0.5) * 0.7;
         double var10 = B(var4) + (this.Z[var1] - 0.5) * 0.6;
         double var12 = (this.o[var1] - 0.5) * 0.4;
         double var14 = var7.x - this.S.D.x;
         double var16 = var7.z - this.S.D.z;
         double var18 = Math.hypot(var14, var16);
         if (var18 < 1.0E-4) {
            double var20 = this.o[var1] * Math.PI * 2.0;
            var14 = Math.cos(var20);
            var16 = Math.sin(var20);
            var18 = 1.0;
         }

         double var63 = (this.Z[var1] - 0.5) * 1.2;
         double var22 = var14 / var18 * this.W[var1] - var16 / var18 * var63;
         double var24 = var16 / var18 * this.W[var1] + var14 / var18 * var63;
         double var26 = this.n[var1];
         double var28 = this.s[var1] == -2 ? this.E[var1] * Math.min(1.0, (var2 - 1.32) * 8.0) : this.E[var1];
         double var30 = Math.PI / 2;
         double var32 = com.larpclient.x.u(var28, var30, 0.0);
         double var34 = this.S.g().X();
         double var36 = J(var7.y, var26, var34 + var32);

         for (int var38 = 0; var38 < 2; var38++) {
            double var39 = this.S.g().L(var7.x + var22 * var36, var7.z + var24 * var36);
            var34 = Double.isNaN(var39) ? this.S.g().X() - 4.5 : var39;
            var36 = J(var7.y, var26, var34 + var32);
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
            var47 = ez.N(var49 / var42);
         } else {
            var44 = new Vec3(var7.x + var22 * (var36 + 0.45 * var42), var34 + var32, var7.z + var24 * (var36 + 0.45 * var42));
            var47 = 1.0;
         }

         double var65 = var8 + this.k[var1] * var45;
         double var51 = var10 + this.r[var1] * var45;
         double var53 = var12 + this.B[var1] * var45;
         double var55 = Math.round((var51 - var30) / Math.PI) * Math.PI + var30;
         double var57 = Math.round(var53 / Math.PI) * Math.PI;
         var51 += (var55 - var51) * var47;
         var53 += (var57 - var53) * var47;
         if (var47 > 0.0) {
            var44 = var44.add(0.0, (com.larpclient.x.u(var28, var55, var57) - var32) * var47, 0.0);
         }

         return new x3.a(var44, var65, var51, var53, var28, var5, var47 >= 1.0);
      }
   }

   static double J(double var0, double var2, double var4) {
      double var6 = Math.max(0.0, var0 - var4);
      return (var2 + Math.sqrt(var2 * var2 + 19.6 * var6)) / 9.8;
   }

   public static void O(r6 var0, ez var1, double var2, int var4, int var5, double var6, boolean var8) {
      x3 var9 = var1.e();
      if (var9 != null) {
         var9.p(var0, var2, var4, var5, var6, var8);
      }
   }

   private void p(r6 var1, double var2, int var4, int var5, double var6, boolean var8) {
      Vec3 var9 = new Vec3(this.S.D.x, this.S.g().X(), this.S.D.z);
      int var10 = oh.j(var4, var5, 0.45);
      int var11 = oh.j(var5, -1, 0.5);
      double var12 = ez.n(var2, 0.0, 0.35) * var6;
      W(var1, this.S.D.add(0.0, 1.15, 0.0), 0.35 + var2 * 0.9, oh.S(var11, var12 * 0.8));
      double var14 = ez.N((var2 - 0.05) / 0.7);
      double var16 = 1.0 - ez.N((var2 - 1.32 - 0.4) / 2.4);
      double var18 = var14 * var16 * var6;
      if (var18 > 0.001) {
         rl.y(var1, var9.add(0.0, 0.04, 0.0), 0.3 + 1.25 * var14, oh.S(var4, var18 * 0.32));
         var1.C(var9.x, var9.y + 0.045, var9.z, 0.3 + 1.25 * var14, 0.12, oh.S(var11, var18 * 0.6));
      }

      double var20 = ez.N((var2 - 0.95) / 0.29000000000000004);
      double var22 = ez.n(var2, 0.95, 1.37) * var6;
      if (var22 > 0.001) {
         for (int var24 = 0; var24 < 8; var24++) {
            this.h(var1, var24, Math.min(var2, 1.32), var20, oh.S(var11, var22));
         }
      }

      double var41 = ez.n(var2, 0.9700000000000001, 1.3800000000000001) * var6;
      W(var1, this.S.D.add(0.0, 1.05, 0.0), 0.25 + 0.5 * ez.N((var2 - 1.32 + 0.35) / 0.35), oh.S(var11, var41 * 0.7));
      double var26 = var2 - 1.32;
      if (var26 >= 0.0) {
         double var28 = ez.n(var26, 0.0, 0.5) * var6;
         W(var1, this.S.D.add(0.0, 1.0, 0.0), 1.0 + var26 * 2.2, oh.S(var11, var28));
         if (var26 < 0.7) {
            var1.C(
               var9.x, var9.y + 0.06, var9.z, 0.3 + 3.4 * (1.0 - Math.pow(1.0 - var26 / 0.7, 3.0)), 0.3, oh.S(var11, Math.pow(1.0 - var26 / 0.7, 1.4) * var6)
            );
         }

         if (var26 < 1.3) {
            rl.A(var1, var9, 0.3 + 2.6 * (1.0 - Math.pow(1.0 - var26 / 1.3, 3.0)), 0.5, oh.S(var4, (1.0 - var26 / 1.3) * 0.35 * var6));
         }

         double var30 = ez.n(var26, 0.0, 2.3) * var6;

         for (int var32 = 0; var32 < 40 && var30 > 0.001; var32++) {
            double var33 = this.S.L(var32 + 15000) * Math.PI * 2.0;
            double var35 = 0.2 + 1.6 * this.S.L(var32 + 15200) * (1.0 - Math.exp(-var26 * 2.5));
            double var37 = 0.2 + 1.7 * this.S.L(var32 + 15400) + var26 * (0.5 - 0.45 * var26) * (1.0 + this.S.L(var32 + 15600));
            double var39 = 0.45 + 0.55 * Math.pow(Math.max(0.0, Math.sin(var2 * 6.0 + var32 * 1.9)), 4.0);
            var1.m(
               this.S.D.x + Math.cos(var33) * var35,
               this.S.D.y + Math.max(0.05, var37),
               this.S.D.z + Math.sin(var33) * var35,
               0.022 + 0.02 * this.S.L(var32 + 15800),
               var33 + var2,
               oh.S(var11, var30 * var39)
            );
         }
      }

      var1.blend(var8 ? fk.h.u : fk.h.z);

      for (int var42 = 0; var42 < 102; var42++) {
         x3.a var29 = this.r(var42, var2);
         if (var29 != null && !(var29.alpha() <= 0.001) && !(var29.size() <= 0.001)) {
            double var45 = var29.alpha() * (var8 ? 0.3 * var6 : 0.82);
            var1.r(
               var29.position().x,
               var29.position().y,
               var29.position().z,
               var29.size(),
               var29.yaw(),
               var29.pitch(),
               var29.roll(),
               oh.S(var8 ? var11 : var10, var45)
            );
         }
      }

      var1.blend(fk.h.u);
      if (var26 > 0.3) {
         for (byte var43 = 0; var43 < 102; var43 += 3) {
            x3.a var44 = this.r(var43, var2);
            if (var44 != null && var44.rest()) {
               double var46 = Math.pow(Math.max(0.0, Math.sin(var2 * 4.5 + var43 * 1.7)), 12.0) * var44.alpha() * var6;
               if (var46 > 0.02) {
                  var1.m(var44.position().x, var44.position().y + 0.04, var44.position().z, 0.07, var2, oh.S(-1, var46));
               }
            }
         }
      }
   }

   private void h(r6 var1, int var2, double var3, double var5, int var7) {
      int var8 = Q[var2][0];
      int var9 = Q[var2][1];
      ez.q var10 = w(this.S, var8, var3);
      this.N.identity().rotateY((float)(Math.PI - this.S.A)).rotateXYZ((float)var10.rx(), (float)var10.ry(), (float)var10.rz());
      double[] var11 = L[var8];
      double var12 = 0.05859375;
      double var14 = (var11[2] + 0.7) * var9;
      double var16 = (this.S.L(var2 + 16000) - 0.5) * var11[0];
      double var18 = (this.S.L(var2 + 16200) - 0.5) * var11[1] * 0.6;
      byte var20 = 5;
      Vec3 var21 = null;

      for (int var22 = 0; var22 <= var20; var22++) {
         double var23 = (double)var22 / var20;
         if (var23 > var5 + 1.0E-9) {
            break;
         }

         double var25 = var16 + (this.S.L(var2 * 8 + var22 + 16400) - 0.5) * var11[0] * 0.9;
         double var27 = var18 + (var23 - 0.5) * var11[1] * 1.6 * (this.S.L(var2 + 16600) < 0.5 ? 1 : -1);
         this.N
            .transform(
               this.d
                  .set(
                     (float)(-Math.clamp(var25, -var11[0], var11[0]) * var12), (float)(-Math.clamp(var27, -var11[1], var11[1]) * var12), (float)(var14 * var12)
                  )
            );
         Vec3 var29 = var10.position().add(this.d.x, this.d.y, this.d.z);
         if (var21 != null) {
            var1.ribbon(var21, var29, 0.028, var7, var7);
         }

         var21 = var29;
      }
   }

   private static void W(r6 var0, Vec3 var1, double var2, int var4) {
      if (var4 >>> 24 > 0) {
         var0.j(var1.x, var1.y, var1.z, var2, 0.0, var4, f4.a.u, 0.0, 0.0, 0.0);
      }
   }

   public record a(Vec3 position, double yaw, double pitch, double roll, double size, double alpha, boolean rest) {
   }
}
