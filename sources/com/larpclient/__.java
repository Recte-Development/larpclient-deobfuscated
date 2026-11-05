package com.larpclient;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class __ {
   private __() {
   }

   public static Vec3 m(Vec3 var0, Vec3 var1, Vec3 var2, Vec3 var3, double var4) {
      if (var4 <= 0.0) {
         return var1;
      } else {
         return var4 >= 1.0
            ? var2
            : Q(var0, var1, var2, var3, var4, Math.sqrt(var0.distanceTo(var1)), Math.sqrt(var1.distanceTo(var2)), Math.sqrt(var2.distanceTo(var3)));
      }
   }

   private static Vec3 Q(Vec3 var0, Vec3 var1, Vec3 var2, Vec3 var3, double var4, double var6, double var8, double var10) {
      if (!(var6 < 1.0E-6) && !(var8 < 1.0E-6) && !(var10 < 1.0E-6)) {
         double var12 = var6 + var4 * var8;
         double var14 = var12 / var6;
         double var16 = (var12 - var6) / var8;
         double var18 = (var12 - var6 - var8) / var10;
         double var20 = var12 / (var6 + var8);
         double var22 = (var12 - var6) / (var8 + var10);
         return new Vec3(
            G(var0.x, var1.x, var2.x, var3.x, var14, var16, var18, var20, var22),
            G(var0.y, var1.y, var2.y, var3.y, var14, var16, var18, var20, var22),
            G(var0.z, var1.z, var2.z, var3.z, var14, var16, var18, var20, var22)
         );
      } else {
         return var1.lerp(var2, var4);
      }
   }

   private static double G(double var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16) {
      double var18 = Mth.lerp(var8, var0, var2);
      double var20 = Mth.lerp(var10, var2, var4);
      double var22 = Mth.lerp(var12, var4, var6);
      double var24 = Mth.lerp(var14, var18, var20);
      double var26 = Mth.lerp(var16, var20, var22);
      return Mth.lerp(var10, var24, var26);
   }

   public static final class l {
      final Vec3[] v;
      final Vec3[] i;
      final double[] O;
      final int[] w;
      int k;
      double t = Double.POSITIVE_INFINITY;

      public l(int var1) {
         this.v = new Vec3[var1];
         this.i = new Vec3[var1];
         this.O = new double[var1];
         this.w = new int[var1];
      }

      public void y() {
         this.k = 0;
      }

      public int h() {
         return this.k;
      }

      public void y(Vec3 var1, double var2, int var4) {
         if (this.k == this.v.length) {
            throw new IllegalStateException("Ribbon capacity exceeded");
         }

         this.v[this.k] = var1;
         this.O[this.k] = var2;
         this.w[this.k++] = var4;
      }

      public void F() {
         if (this.k >= 2) {
            double var1 = 0.0;

            for (int var3 = 1; var3 < this.k; var3++) {
               var1 += this.v[var3 - 1].distanceTo(this.v[var3]);
            }

            if (!(var1 < 1.0E-8)) {
               double var10 = var1 * 0.18;
               double var5 = 0.0;
               this.O[0] = 0.0;
               this.w[0] = this.w[0] & 16777215;

               for (int var7 = 1; var7 < this.k; var7++) {
                  var5 += this.v[var7 - 1].distanceTo(this.v[var7]);
                  if (var5 >= var10) {
                     break;
                  }

                  double var8 = hp.R(var5 / var10);
                  this.O[var7] = this.O[var7] * var8;
                  this.w[var7] = _3.U(this.w[var7], var8);
               }
            }
         }
      }

      public void a(Vec3 var1) {
         if (this.k >= 2) {
            Vec3 var2 = H(this.v[0], this.v[1], var1, new Vec3(1.0, 0.0, 0.0));
            this.i[0] = var2.scale(this.O[0] * 0.5);

            for (int var3 = 1; var3 < this.k; var3++) {
               Vec3 var4 = var3 + 1 < this.k ? H(this.v[var3], this.v[var3 + 1], var1, var2) : var2;
               if (var4.dot(var2) < 0.0) {
                  var4 = var4.scale(-1.0);
               }

               Vec3 var5 = var2.add(var4);
               Vec3 var6 = var5.lengthSqr() < 1.0E-12 ? var2 : var5.normalize();
               double var7 = Math.min(2.0, 1.0 / Math.max(0.5, Math.abs(var6.dot(var4))));
               this.i[var3] = var6.scale(this.O[var3] * 0.5 * var7);
               var2 = var4;
            }
         }
      }

      public Vec3 v(int var1) {
         return this.i[var1];
      }

      private static Vec3 H(Vec3 var0, Vec3 var1, Vec3 var2, Vec3 var3) {
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

   static final class u {
      private Vec3 F;
      private Vec3 G;
      private Vec3 V;
      private Vec3 z;
      private double k;
      private double T;
      private double h;

      void l(Vec3 var1, Vec3 var2, Vec3 var3, Vec3 var4) {
         this.F = var1;
         this.G = var2;
         this.V = var3;
         this.z = var4;
         this.k = Math.sqrt(var1.distanceTo(var2));
         this.T = Math.sqrt(var2.distanceTo(var3));
         this.h = Math.sqrt(var3.distanceTo(var4));
      }

      Vec3 p(double var1) {
         if (var1 <= 0.0) {
            return this.G;
         } else {
            return var1 >= 1.0 ? this.V : __.Q(this.F, this.G, this.V, this.z, var1, this.k, this.T, this.h);
         }
      }
   }
}
