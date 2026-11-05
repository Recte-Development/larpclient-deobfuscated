package com.larpclient;

import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionfc;

public final class ez {
   public static final int Q = 6;
   public static final double E = 1.25;
   public static final double s = 4.6;
   public static final double a = 0.9375;
   public static final double[][] X = new double[][]{{0.0, -4.0, 0.0}, {0.0, 6.0, 0.0}, {-6.0, 6.0, 0.0}, {6.0, 6.0, 0.0}, {-1.9, 18.0, 0.0}, {1.9, 18.0, 0.0}};
   private final Vec3[] J = new Vec3[6];
   private final Vec3[] e = new Vec3[6];
   private final Vec3[] r = new Vec3[6];
   private final double[] m = new double[6];
   private final double[] N = new double[6];
   private final boolean[] u = new boolean[6];
   public final ez.k j;
   public final Vec3 D;
   public final double A;
   public final double G;
   public final long q;
   public final boolean d;
   private final ur c;
   private final rl M;
   private final x3 F;
   private double Y;
   private boolean z;

   public ez(ez.k var1, Vec3 var2, double var3, double var5, long var7) {
      this(var1, var2, var3, var5, var7, false);
   }

   public ez(ez.k var1, Vec3 var2, double var3, double var5, long var7, boolean var9) {
      this.j = var1;
      this.D = var2;
      this.A = var3;
      this.G = var5;
      this.q = var7;
      this.d = var9;
      this.Y = var5;
      this.c = var1 != ez.k.A && var1 != ez.k.T ? null : new ur(var2);
      this.M = var1 == ez.k.A ? new rl(this, this.c) : null;
      this.F = var1 == ez.k.T ? new x3(this, this.c) : null;
   }

   public ur g() {
      return this.c;
   }

   public rl u() {
      return this.M;
   }

   public x3 e() {
      return this.F;
   }

   public double Q() {
      return this.j == ez.k.V ? 1.25 : (this.j == ez.k.T ? 1.32 : Double.POSITIVE_INFINITY);
   }

   public static double N(double var0) {
      return yc.H(var0);
   }

   public static double n(double var0, double var2, double var4) {
      return !(var0 < var2) && !(var0 >= var4) ? N((var0 - var2) / 0.09) * N((var4 - var0) / 0.22) : 0.0;
   }

   public Vec3 k(int var1) {
      double var2 = k(var1, this.d) / 16.0 * 0.9375;
      return new Vec3(var2 * Math.cos(this.A), (24.0 - X[var1][1]) / 16.0 * 0.9375, var2 * Math.sin(this.A));
   }

   public static double k(int var0, boolean var1) {
      return var1 && (var0 == 2 || var0 == 3) ? X[var0][0] + (var0 == 2 ? 0.5 : -0.5) : X[var0][0];
   }

   public static double p(int var0) {
      return var0 == 0 ? 0.46 : (var0 == 1 ? 0.47 : 0.42);
   }

   public void w(double var1, et.l var3) {
      double var4 = this.Y - this.G;
      double var6 = var1 - this.G;
      this.Y = var1;
      if (this.c != null) {
         this.c.j(var3, 48);
      }

      double var8 = this.Q();
      if (Double.isFinite(var8) && !(var6 < var8) && !(var1 < this.G)) {
         if (!this.z) {
            this.z = (boolean)1;

            for (int var10 = 0; var10 < 6; var10++) {
               if (this.j == ez.k.T) {
                  this.J[var10] = this.e[var10] = x3.w(this, var10, var8).position();
                  this.r[var10] = x3.e(this, var10);
               } else {
                  this.J[var10] = this.e[var10] = this.D.add(this.k(var10)).add(0.0, 1.7, 0.0);
                  double var11 = var10 * Math.PI * 2.0 / 6.0 + this.L(var10) * 0.6;
                  this.r[var10] = new Vec3(Math.cos(var11) * (1.1 + this.L(var10 + 8)), 0.65 + this.L(var10 + 15), Math.sin(var11) * (1.1 + this.L(var10 + 8)));
               }
            }

            var4 = var8;
         }

         double var25 = Math.clamp(var6 - Math.max(var4, var8), 0.0, 0.05);

         for (int var12 = 0; var12 < 6; var12++) {
            this.e[var12] = this.J[var12];
            this.N[var12] = this.m[var12];
            if (!this.u[var12] && var25 != 0.0) {
               this.r[var12] = this.r[var12].add(0.0, -8.8 * var25, 0.0);
               Vec3 var13 = this.J[var12].add(this.r[var12].scale(var25));
               double var14 = p(var12);
               Vec3 var16 = new Vec3(0.0, -var14, 0.0);
               et.d var17 = var3 == null ? null : var3.J(this.J[var12].add(var16), var13.add(var16));
               Vec3 var18 = var13.subtract(this.J[var12]);

               for (int var19 = 0; var19 < 2 && var3 != null; var19++) {
                  double var20 = var19 == 0 ? var18.x : var18.z;
                  if (!(Math.abs(var20) < 1.0E-8)) {
                     Vec3 var22 = var19 == 0 ? new Vec3(Math.copySign(var14, var20), 0.0, 0.0) : new Vec3(0.0, 0.0, Math.copySign(var14, var20));
                     Vec3 var23 = this.J[var12].add(var22);
                     et.d var24 = var3.J(var23, var13.add(var22));
                     if (var24 != null && (var17 == null || var23.distanceToSqr(var24.point()) < this.J[var12].add(var16).distanceToSqr(var17.point()))) {
                        var17 = var24;
                        var16 = var22;
                     }
                  }
               }

               if (var17 != null) {
                  this.J[var12] = var17.point().subtract(var16).add(var17.normal().scale(var14 + 0.025 + var16.dot(var17.normal())));
                  if (var17.normal().y > 0.5) {
                     this.u[var12] = (boolean)1;
                     this.r[var12] = Vec3.ZERO;
                     this.J[var12] = new Vec3(this.J[var12].x, var17.point().y + (var12 == 0 ? 0.27 : 0.15), this.J[var12].z);
                  } else {
                     this.r[var12] = new Vec3(0.0, this.r[var12].y * 0.85, 0.0);
                  }
               } else {
                  this.J[var12] = var13;
               }

               if (!this.u[var12]) {
                  this.m[var12] = this.m[var12] + var25 * (2.8 + this.L(var12 + 22) * 2.0);
               }
            }
         }
      }
   }

   public ez.q X(int var1, double var2, double var4) {
      return this.T(var1, var2, var4, u4.x);
   }

   public ez.q T(int var1, double var2, double var4, u4.v var6) {
      if (this.j == ez.k.G) {
         return u4.k(this, var1, var2, var6);
      }

      if (this.j == ez.k.k) {
         return l9.Y(this, var1, var2);
      }

      if (this.j == ez.k.d) {
         return y9.u(this, var1, var2);
      }

      if (this.j == ez.k.A) {
         return rl.y(this, var1, var2);
      }

      Vec3 var7 = this.k(var1);
      double var8 = N((4.6 - var2) / 0.75);
      if (this.j == ez.k.T) {
         if (!(var2 < 1.32) && this.z) {
            double var23 = this.N[var1] + (this.m[var1] - this.N[var1]) * Math.clamp(var4, 0.0, 1.0);
            double var25 = N((var2 - 2.7 - this.L(var1 + 40) * 0.3) / 1.2);
            return new ez.q(
               this.e[var1].lerp(this.J[var1], Math.clamp(var4, 0.0, 1.0)),
               this.u[var1] ? Math.PI / 2 : var23 * 0.75 + x3.p(),
               this.u[var1] ? 0.0 : var23 * 0.45,
               this.u[var1] ? var23 * 0.45 : var23 + x3.J(var1),
               1.0,
               var8,
               var25,
               this.u[var1]
            );
         } else {
            return x3.w(this, var1, var2);
         }
      } else if (this.j == ez.k.V) {
         if (!(var2 < 1.25) && this.z) {
            double var22 = this.N[var1] + (this.m[var1] - this.N[var1]) * Math.clamp(var4, 0.0, 1.0);
            return new ez.q(
               this.e[var1].lerp(this.J[var1], Math.clamp(var4, 0.0, 1.0)),
               this.u[var1] ? Math.PI / 2 : var22 * 0.75 + (var1 < 2 ? -0.07 : 0.0),
               this.u[var1] ? 0.0 : var22 * 0.45,
               this.u[var1] ? var22 * 0.45 : var22 + (var1 == 2 ? -0.32 : (var1 == 3 ? 0.32 : 0.0)),
               1.0,
               var8,
               0.0,
               this.u[var1]
            );
         } else {
            double var21 = 1.7 * N(var2 / 0.85);
            double var24 = N(var2 / 0.8);
            return new ez.q(
               this.D.add(var7).add(0.0, var21, 0.0),
               var1 < 2 ? -0.07 * var24 : 0.0,
               0.0,
               var1 == 2 ? -0.32 * var24 : (var1 == 3 ? 0.32 * var24 : 0.0),
               1.0,
               var8,
               0.0,
               false
            );
         }
      } else if (this.j == ez.k.r) {
         double var20 = N((var2 - 0.45) / 1.45);
         double var12 = var20 * 8.5;
         Vec3 var14 = var7.subtract(0.0, 1.05, 0.0).scale(1.0 - var20);
         double var15 = Math.cos(var12);
         double var17 = Math.sin(var12);
         Vec3 var19 = new Vec3(var14.x * var15 - var14.z * var17, var14.y, var14.x * var17 + var14.z * var15);
         return new ez.q(
            this.D.add(0.0, 1.05 + 0.35 * Math.sin(var20 * Math.PI), 0.0).add(var19),
            var20 * 2.0,
            var12,
            var20 * 1.2,
            Math.max(0.0, 1.0 - var20),
            1.0 - var20,
            0.0,
            false
         );
      } else {
         double var10 = N((var2 - 0.35 - (1.8 - var7.y) * 0.3) / 1.2);
         return new ez.q(
            this.D.add(var7).add(Math.sin(var2 * 2.0 + var1) * var10 * 0.04, var10 * 0.38, 0.0), var10 * 0.1, 0.0, 0.0, 1.0, 1.0 - var10 * 0.2, var10, false
         );
      }
   }

   public double d() {
      return this.j == ez.k.d ? 7.5 : (this.j == ez.k.A ? 8.0 : 4.6);
   }

   public boolean F(double var1) {
      return var1 - this.G >= this.d();
   }

   public boolean Z(int var1) {
      return this.u[var1];
   }

   public double L(int var1) {
      long var2 = this.q + var1 * -7046029254386353131L;
      var2 = (var2 ^ var2 >>> 30) * -4658895280553007687L;
      var2 = (var2 ^ var2 >>> 27) * -7723592293110705685L;
      return ((var2 ^ var2 >>> 31) >>> 11) * 1.110223E-16F;
   }

   public enum k {
      V,
      r,
      K,
      G,
      k,
      d,
      A,
      T;
   }

   public record q(
      Vec3 position, double rx, double ry, double rz, double scale, double alpha, double dissolve, boolean landed, Quaternionfc orientation, Vec3 stretch
   ) {
      public q(Vec3 var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean var14) {
         this(var1, var2, var4, var6, var8, var10, var12, var14, null, null);
      }
   }
}
