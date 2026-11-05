package com.larpclient;

import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionfc;

public final class ta {
   public static final int l = 6;
   public static final double p = 1.25;
   public static final double a = 4.6;
   public static final double V = 0.9375;
   public static final double[][] Q = new double[][]{{0.0, -4.0, 0.0}, {0.0, 6.0, 0.0}, {-6.0, 6.0, 0.0}, {6.0, 6.0, 0.0}, {-1.9, 18.0, 0.0}, {1.9, 18.0, 0.0}};
   private final Vec3[] r = new Vec3[6];
   private final Vec3[] B = new Vec3[6];
   private final Vec3[] I = new Vec3[6];
   private final double[] L = new double[6];
   private final double[] i = new double[6];
   private final boolean[] W = new boolean[6];
   public final ta.e m;
   public final Vec3 J;
   public final double v;
   public final double K;
   public final long D;
   public final boolean T;
   private final mz C;
   private final hh k;
   private final td h;
   private double M;
   private boolean c;

   public ta(ta.e var1, Vec3 var2, double var3, double var5, long var7) {
      this(var1, var2, var3, var5, var7, false);
   }

   public ta(ta.e var1, Vec3 var2, double var3, double var5, long var7, boolean var9) {
      this.m = var1;
      this.J = var2;
      this.v = var3;
      this.K = var5;
      this.D = var7;
      this.T = var9;
      this.M = var5;
      this.C = var1 != ta.e.c && var1 != ta.e.S ? null : new mz(var2);
      this.k = var1 == ta.e.c ? new hh(this, this.C) : null;
      this.h = var1 == ta.e.S ? new td(this, this.C) : null;
   }

   public mz L() {
      return this.C;
   }

   public hh E() {
      return this.k;
   }

   public td V() {
      return this.h;
   }

   public double g() {
      return this.m == ta.e.f ? 1.25 : (this.m == ta.e.S ? 1.32 : Double.POSITIVE_INFINITY);
   }

   public static double k(double var0) {
      return hp.R(var0);
   }

   public static double H(double var0, double var2, double var4) {
      return !(var0 < var2) && !(var0 >= var4) ? k((var0 - var2) / 0.09) * k((var4 - var0) / 0.22) : 0.0;
   }

   public Vec3 N(int var1) {
      double var2 = Z(var1, this.T) / 16.0 * 0.9375;
      return new Vec3(var2 * Math.cos(this.v), (24.0 - Q[var1][1]) / 16.0 * 0.9375, var2 * Math.sin(this.v));
   }

   public static double Z(int var0, boolean var1) {
      return var1 && (var0 == 2 || var0 == 3) ? Q[var0][0] + (var0 == 2 ? 0.5 : -0.5) : Q[var0][0];
   }

   public static double q(int var0) {
      return var0 == 0 ? 0.46 : (var0 == 1 ? 0.47 : 0.42);
   }

   public void H(double var1, w8.z var3) {
      double var4 = this.M - this.K;
      double var6 = var1 - this.K;
      this.M = var1;
      if (this.C != null) {
         this.C.J(var3, 48);
      }

      double var8 = this.g();
      if (Double.isFinite(var8) && !(var6 < var8) && !(var1 < this.K)) {
         if (!this.c) {
            this.c = (boolean)1;

            for (int var10 = 0; var10 < 6; var10++) {
               if (this.m == ta.e.S) {
                  this.r[var10] = this.B[var10] = td.q(this, var10, var8).position();
                  this.I[var10] = td.d(this, var10);
               } else {
                  this.r[var10] = this.B[var10] = this.J.add(this.N(var10)).add(0.0, 1.7, 0.0);
                  double var11 = var10 * Math.PI * 2.0 / 6.0 + this.W(var10) * 0.6;
                  this.I[var10] = new Vec3(Math.cos(var11) * (1.1 + this.W(var10 + 8)), 0.65 + this.W(var10 + 15), Math.sin(var11) * (1.1 + this.W(var10 + 8)));
               }
            }

            var4 = var8;
         }

         double var25 = Math.clamp(var6 - Math.max(var4, var8), 0.0, 0.05);

         for (int var12 = 0; var12 < 6; var12++) {
            this.B[var12] = this.r[var12];
            this.i[var12] = this.L[var12];
            if (!this.W[var12] && var25 != 0.0) {
               this.I[var12] = this.I[var12].add(0.0, -8.8 * var25, 0.0);
               Vec3 var13 = this.r[var12].add(this.I[var12].scale(var25));
               double var14 = q(var12);
               Vec3 var16 = new Vec3(0.0, -var14, 0.0);
               w8.n var17 = var3 == null ? null : var3.X(this.r[var12].add(var16), var13.add(var16));
               Vec3 var18 = var13.subtract(this.r[var12]);

               for (int var19 = 0; var19 < 2 && var3 != null; var19++) {
                  double var20 = var19 == 0 ? var18.x : var18.z;
                  if (!(Math.abs(var20) < 1.0E-8)) {
                     Vec3 var22 = var19 == 0 ? new Vec3(Math.copySign(var14, var20), 0.0, 0.0) : new Vec3(0.0, 0.0, Math.copySign(var14, var20));
                     Vec3 var23 = this.r[var12].add(var22);
                     w8.n var24 = var3.X(var23, var13.add(var22));
                     if (var24 != null && (var17 == null || var23.distanceToSqr(var24.point()) < this.r[var12].add(var16).distanceToSqr(var17.point()))) {
                        var17 = var24;
                        var16 = var22;
                     }
                  }
               }

               if (var17 != null) {
                  this.r[var12] = var17.point().subtract(var16).add(var17.normal().scale(var14 + 0.025 + var16.dot(var17.normal())));
                  if (var17.normal().y > 0.5) {
                     this.W[var12] = (boolean)1;
                     this.I[var12] = Vec3.ZERO;
                     this.r[var12] = new Vec3(this.r[var12].x, var17.point().y + (var12 == 0 ? 0.27 : 0.15), this.r[var12].z);
                  } else {
                     this.I[var12] = new Vec3(0.0, this.I[var12].y * 0.85, 0.0);
                  }
               } else {
                  this.r[var12] = var13;
               }

               if (!this.W[var12]) {
                  this.L[var12] = this.L[var12] + var25 * (2.8 + this.W(var12 + 22) * 2.0);
               }
            }
         }
      }
   }

   public ta.y J(int var1, double var2, double var4) {
      return this.m(var1, var2, var4, xy.y);
   }

   public ta.y m(int var1, double var2, double var4, xy.b var6) {
      if (this.m == ta.e.q) {
         return xy.v(this, var1, var2, var6);
      }

      if (this.m == ta.e.H) {
         return _7.o(this, var1, var2);
      }

      if (this.m == ta.e.d) {
         return e9.I(this, var1, var2);
      }

      if (this.m == ta.e.c) {
         return hh.v(this, var1, var2);
      }

      Vec3 var7 = this.N(var1);
      double var8 = k((4.6 - var2) / 0.75);
      if (this.m == ta.e.S) {
         if (!(var2 < 1.32) && this.c) {
            double var23 = this.i[var1] + (this.L[var1] - this.i[var1]) * Math.clamp(var4, 0.0, 1.0);
            double var25 = k((var2 - 2.7 - this.W(var1 + 40) * 0.3) / 1.2);
            return new ta.y(
               this.B[var1].lerp(this.r[var1], Math.clamp(var4, 0.0, 1.0)),
               this.W[var1] ? Math.PI / 2 : var23 * 0.75 + td.P(),
               this.W[var1] ? 0.0 : var23 * 0.45,
               this.W[var1] ? var23 * 0.45 : var23 + td.L(var1),
               1.0,
               var8,
               var25,
               this.W[var1]
            );
         } else {
            return td.q(this, var1, var2);
         }
      } else if (this.m == ta.e.f) {
         if (!(var2 < 1.25) && this.c) {
            double var22 = this.i[var1] + (this.L[var1] - this.i[var1]) * Math.clamp(var4, 0.0, 1.0);
            return new ta.y(
               this.B[var1].lerp(this.r[var1], Math.clamp(var4, 0.0, 1.0)),
               this.W[var1] ? Math.PI / 2 : var22 * 0.75 + (var1 < 2 ? -0.07 : 0.0),
               this.W[var1] ? 0.0 : var22 * 0.45,
               this.W[var1] ? var22 * 0.45 : var22 + (var1 == 2 ? -0.32 : (var1 == 3 ? 0.32 : 0.0)),
               1.0,
               var8,
               0.0,
               this.W[var1]
            );
         } else {
            double var21 = 1.7 * k(var2 / 0.85);
            double var24 = k(var2 / 0.8);
            return new ta.y(
               this.J.add(var7).add(0.0, var21, 0.0),
               var1 < 2 ? -0.07 * var24 : 0.0,
               0.0,
               var1 == 2 ? -0.32 * var24 : (var1 == 3 ? 0.32 * var24 : 0.0),
               1.0,
               var8,
               0.0,
               false
            );
         }
      } else if (this.m == ta.e.W) {
         double var20 = k((var2 - 0.45) / 1.45);
         double var12 = var20 * 8.5;
         Vec3 var14 = var7.subtract(0.0, 1.05, 0.0).scale(1.0 - var20);
         double var15 = Math.cos(var12);
         double var17 = Math.sin(var12);
         Vec3 var19 = new Vec3(var14.x * var15 - var14.z * var17, var14.y, var14.x * var17 + var14.z * var15);
         return new ta.y(
            this.J.add(0.0, 1.05 + 0.35 * Math.sin(var20 * Math.PI), 0.0).add(var19),
            var20 * 2.0,
            var12,
            var20 * 1.2,
            Math.max(0.0, 1.0 - var20),
            1.0 - var20,
            0.0,
            false
         );
      } else {
         double var10 = k((var2 - 0.35 - (1.8 - var7.y) * 0.3) / 1.2);
         return new ta.y(
            this.J.add(var7).add(Math.sin(var2 * 2.0 + var1) * var10 * 0.04, var10 * 0.38, 0.0), var10 * 0.1, 0.0, 0.0, 1.0, 1.0 - var10 * 0.2, var10, false
         );
      }
   }

   public double m() {
      return this.m == ta.e.d ? 7.5 : (this.m == ta.e.c ? 8.0 : 4.6);
   }

   public boolean l(double var1) {
      return var1 - this.K >= this.m();
   }

   public boolean G(int var1) {
      return this.W[var1];
   }

   public double W(int var1) {
      long var2 = this.D + var1 * -7046029254386353131L;
      var2 = (var2 ^ var2 >>> 30) * -4658895280553007687L;
      var2 = (var2 ^ var2 >>> 27) * -7723592293110705685L;
      return ((var2 ^ var2 >>> 31) >>> 11) * 1.110223E-16F;
   }

   public enum e {
      f,
      W,
      t,
      q,
      H,
      d,
      c,
      S;
   }

   public record y(
      Vec3 position, double rx, double ry, double rz, double scale, double alpha, double dissolve, boolean landed, Quaternionfc orientation, Vec3 stretch
   ) {
      public y(Vec3 var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean var14) {
         this(var1, var2, var4, var6, var8, var10, var12, var14, null, null);
      }
   }
}
