package com.larpclient;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class u_ {
   private u_() {
   }

   public static Vec3 B(Vec3 var0, Vec3 var1, Vec3 var2, Vec3 var3, double var4) {
      if (var4 <= 0.0) {
         return var1;
      } else {
         return var4 >= 1.0
            ? var2
            : B(var0, var1, var2, var3, var4, Math.sqrt(var0.distanceTo(var1)), Math.sqrt(var1.distanceTo(var2)), Math.sqrt(var2.distanceTo(var3)));
      }
   }

   private static Vec3 B(Vec3 var0, Vec3 var1, Vec3 var2, Vec3 var3, double var4, double var6, double var8, double var10) {
      if (!(var6 < 1.0E-6) && !(var8 < 1.0E-6) && !(var10 < 1.0E-6)) {
         double var12 = var6 + var4 * var8;
         double var14 = var12 / var6;
         double var16 = (var12 - var6) / var8;
         double var18 = (var12 - var6 - var8) / var10;
         double var20 = var12 / (var6 + var8);
         double var22 = (var12 - var6) / (var8 + var10);
         return new Vec3(
            f(var0.x, var1.x, var2.x, var3.x, var14, var16, var18, var20, var22),
            f(var0.y, var1.y, var2.y, var3.y, var14, var16, var18, var20, var22),
            f(var0.z, var1.z, var2.z, var3.z, var14, var16, var18, var20, var22)
         );
      } else {
         return var1.lerp(var2, var4);
      }
   }

   private static double f(double var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16) {
      double var18 = Mth.lerp(var8, var0, var2);
      double var20 = Mth.lerp(var10, var2, var4);
      double var22 = Mth.lerp(var12, var4, var6);
      double var24 = Mth.lerp(var14, var18, var20);
      double var26 = Mth.lerp(var16, var20, var22);
      return Mth.lerp(var10, var24, var26);
   }

   public static final class p {
      final Vec3[] M;
      final Vec3[] D;
      final double[] A;
      final int[] I;
      int p;
      double C = Double.POSITIVE_INFINITY;

      public p(int var1) {
         this.M = new Vec3[var1];
         this.D = new Vec3[var1];
         this.A = new double[var1];
         this.I = new int[var1];
      }

      public void q() {
         this.p = 0;
      }

      public int F() {
         return this.p;
      }

      public void n(Vec3 var1, double var2, int var4) {
         if (this.p == this.M.length) {
            throw new IllegalStateException("Ribbon capacity exceeded");
         }

         this.M[this.p] = var1;
         this.A[this.p] = var2;
         this.I[this.p++] = var4;
      }

      public void O() {
         if (this.p >= 2) {
            double var1 = 0.0;

            for (int var3 = 1; var3 < this.p; var3++) {
               var1 += this.M[var3 - 1].distanceTo(this.M[var3]);
            }

            if (!(var1 < 1.0E-8)) {
               double var10 = var1 * 0.18;
               double var5 = 0.0;
               this.A[0] = 0.0;
               this.I[0] = this.I[0] & 16777215;

               for (int var7 = 1; var7 < this.p; var7++) {
                  var5 += this.M[var7 - 1].distanceTo(this.M[var7]);
                  if (var5 >= var10) {
                     break;
                  }

                  double var8 = st.h(var5 / var10);
                  this.A[var7] = this.A[var7] * var8;
                  this.I[var7] = uh.F(this.I[var7], var8);
               }
            }
         }
      }

      public void x(Vec3 var1) {
         if (this.p >= 2) {
            Vec3 var2 = G(this.M[0], this.M[1], var1, new Vec3(1.0, 0.0, 0.0));
            this.D[0] = var2.scale(this.A[0] * 0.5);

            for (int var3 = 1; var3 < this.p; var3++) {
               Vec3 var4 = var3 + 1 < this.p ? G(this.M[var3], this.M[var3 + 1], var1, var2) : var2;
               if (var4.dot(var2) < 0.0) {
                  var4 = var4.scale(-1.0);
               }

               Vec3 var5 = var2.add(var4);
               Vec3 var6 = var5.lengthSqr() < 1.0E-12 ? var2 : var5.normalize();
               double var7 = Math.min(2.0, 1.0 / Math.max(0.5, Math.abs(var6.dot(var4))));
               this.D[var3] = var6.scale(this.A[var3] * 0.5 * var7);
               var2 = var4;
            }
         }
      }

      public Vec3 j(int var1) {
         return this.D[var1];
      }

      private static Vec3 G(Vec3 var0, Vec3 var1, Vec3 var2, Vec3 var3) {
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
      private Vec3 e;
      private Vec3 t;
      private Vec3 m;
      private Vec3 o;
      private double X;
      private double f;
      private double x;

      void U(Vec3 var1, Vec3 var2, Vec3 var3, Vec3 var4) {
         this.e = var1;
         this.t = var2;
         this.m = var3;
         this.o = var4;
         this.X = Math.sqrt(var1.distanceTo(var2));
         this.f = Math.sqrt(var2.distanceTo(var3));
         this.x = Math.sqrt(var3.distanceTo(var4));
      }

      Vec3 L(double var1) {
         if (var1 <= 0.0) {
            return this.t;
         } else {
            return var1 >= 1.0 ? this.m : u_.B(this.e, this.t, this.m, this.o, var1, this.X, this.f, this.x);
         }
      }
   }
}
