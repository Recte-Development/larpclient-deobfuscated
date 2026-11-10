package com.larpclient;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class x {
   private x() {
   }

   public static Vec3 J(Vec3 var0, Vec3 var1, Vec3 var2, Vec3 var3, double var4) {
      if (var4 <= 0.0) {
         return var1;
      } else {
         return var4 >= 1.0
            ? var2
            : Z(var0, var1, var2, var3, var4, Math.sqrt(var0.distanceTo(var1)), Math.sqrt(var1.distanceTo(var2)), Math.sqrt(var2.distanceTo(var3)));
      }
   }

   private static Vec3 Z(Vec3 var0, Vec3 var1, Vec3 var2, Vec3 var3, double var4, double var6, double var8, double var10) {
      if (!(var6 < 1.0E-6) && !(var8 < 1.0E-6) && !(var10 < 1.0E-6)) {
         double var12 = var6 + var4 * var8;
         double var14 = var12 / var6;
         double var16 = (var12 - var6) / var8;
         double var18 = (var12 - var6 - var8) / var10;
         double var20 = var12 / (var6 + var8);
         double var22 = (var12 - var6) / (var8 + var10);
         return new Vec3(
            l(var0.x, var1.x, var2.x, var3.x, var14, var16, var18, var20, var22),
            l(var0.y, var1.y, var2.y, var3.y, var14, var16, var18, var20, var22),
            l(var0.z, var1.z, var2.z, var3.z, var14, var16, var18, var20, var22)
         );
      } else {
         return var1.lerp(var2, var4);
      }
   }

   private static double l(double var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16) {
      double var18 = Mth.lerp(var8, var0, var2);
      double var20 = Mth.lerp(var10, var2, var4);
      double var22 = Mth.lerp(var12, var4, var6);
      double var24 = Mth.lerp(var14, var18, var20);
      double var26 = Mth.lerp(var16, var20, var22);
      return Mth.lerp(var10, var24, var26);
   }

   public static final class c {
      final Vec3[] r;
      final Vec3[] k;
      final double[] x;
      final int[] W;
      int l;
      double e = Double.POSITIVE_INFINITY;

      public c(int var1) {
         this.r = new Vec3[var1];
         this.k = new Vec3[var1];
         this.x = new double[var1];
         this.W = new int[var1];
      }

      public void w() {
         this.l = 0;
      }

      public int m() {
         return this.l;
      }

      public void J(Vec3 var1, double var2, int var4) {
         if (this.l == this.r.length) {
            throw new IllegalStateException("Ribbon capacity exceeded");
         }

         this.r[this.l] = var1;
         this.x[this.l] = var2;
         this.W[this.l++] = var4;
      }

      public void v() {
         if (this.l >= 2) {
            double var1 = 0.0;

            for (int var3 = 1; var3 < this.l; var3++) {
               var1 += this.r[var3 - 1].distanceTo(this.r[var3]);
            }

            if (!(var1 < 1.0E-8)) {
               double var10 = var1 * 0.18;
               double var5 = 0.0;
               this.x[0] = 0.0;
               this.W[0] = this.W[0] & 16777215;

               for (int var7 = 1; var7 < this.l; var7++) {
                  var5 += this.r[var7 - 1].distanceTo(this.r[var7]);
                  if (var5 >= var10) {
                     break;
                  }

                  double var8 = pr.k(var5 / var10);
                  this.x[var7] = this.x[var7] * var8;
                  this.W[var7] = vv.O(this.W[var7], var8);
               }
            }
         }
      }

      public void T(Vec3 var1) {
         if (this.l >= 2) {
            Vec3 var2 = z(this.r[0], this.r[1], var1, new Vec3(1.0, 0.0, 0.0));
            this.k[0] = var2.scale(this.x[0] * 0.5);

            for (int var3 = 1; var3 < this.l; var3++) {
               Vec3 var4 = var3 + 1 < this.l ? z(this.r[var3], this.r[var3 + 1], var1, var2) : var2;
               if (var4.dot(var2) < 0.0) {
                  var4 = var4.scale(-1.0);
               }

               Vec3 var5 = var2.add(var4);
               Vec3 var6 = var5.lengthSqr() < 1.0E-12 ? var2 : var5.normalize();
               double var7 = Math.min(2.0, 1.0 / Math.max(0.5, Math.abs(var6.dot(var4))));
               this.k[var3] = var6.scale(this.x[var3] * 0.5 * var7);
               var2 = var4;
            }
         }
      }

      public Vec3 J(int var1) {
         return this.k[var1];
      }

      private static Vec3 z(Vec3 var0, Vec3 var1, Vec3 var2, Vec3 var3) {
         Vec3 var4 = var1.subtract(var0);
         Vec3 var5 = var4.cross(var2.subtract(var0));
         if (var5.lengthSqr() >= 1.0E-12) {
            return var5.normalize();
         }

         if (var4.lengthSqr() < 1.0E-12) {
            return var3;
         }

         Vec3 var6 = var4.normalize();
         Vec3 var7 = var3.subtract(var6.scale(var3.dot(var6)));
         if (var7.lengthSqr() >= 1.0E-12) {
            return var7.normalize();
         }

         Vec3 var8 = Math.abs(var6.y) < 0.9 ? new Vec3(0.0, 1.0, 0.0) : new Vec3(0.0, 0.0, 1.0);
         return var6.cross(var8).normalize();
      }
   }

   static final class l {
      private Vec3 o;
      private Vec3 y;
      private Vec3 u;
      private Vec3 m;
      private double h;
      private double T;
      private double F;

      void P(Vec3 var1, Vec3 var2, Vec3 var3, Vec3 var4) {
         this.o = var1;
         this.y = var2;
         this.u = var3;
         this.m = var4;
         this.h = Math.sqrt(var1.distanceTo(var2));
         this.T = Math.sqrt(var2.distanceTo(var3));
         this.F = Math.sqrt(var3.distanceTo(var4));
      }

      Vec3 x(double var1) {
         if (var1 <= 0.0) {
            return this.y;
         } else {
            return var1 >= 1.0 ? this.u : x.Z(this.o, this.y, this.u, this.m, var1, this.h, this.T, this.F);
         }
      }
   }
}
