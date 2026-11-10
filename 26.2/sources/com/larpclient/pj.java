package com.larpclient;

import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionfc;

public final class pj {
   public static final int o = 6;
   public static final double p = 1.25;
   public static final double w = 4.6;
   public static final double W = 0.9375;
   public static final double[][] S = new double[][]{{0.0, -4.0, 0.0}, {0.0, 6.0, 0.0}, {-6.0, 6.0, 0.0}, {6.0, 6.0, 0.0}, {-1.9, 18.0, 0.0}, {1.9, 18.0, 0.0}};
   private final Vec3[] E = new Vec3[6];
   private final Vec3[] r = new Vec3[6];
   private final Vec3[] g = new Vec3[6];
   private final double[] D = new double[6];
   private final double[] Y = new double[6];
   private final boolean[] R = new boolean[6];
   public final pj.f h;
   public final Vec3 v;
   public final double z;
   public final double U;
   public final long j;
   public final boolean l;
   private final com.larpclient.f G;
   private final uu t;
   private final se x;
   private final b3 s;
   private double c;
   private boolean y;

   public pj(pj.f var1, Vec3 var2, double var3, double var5, long var7) {
      this(var1, var2, var3, var5, var7, false);
   }

   public pj(pj.f var1, Vec3 var2, double var3, double var5, long var7, boolean var9) {
      this.h = var1;
      this.v = var2;
      this.z = var3;
      this.U = var5;
      this.j = var7;
      this.l = var9;
      this.c = var5;
      this.G = var1 != pj.f.m && var1 != pj.f.C ? null : new com.larpclient.f(var2);
      this.t = var1 == pj.f.m ? new uu(this, this.G) : null;
      this.x = var1 == pj.f.C ? new se(this, this.G) : null;
      this.s = var1 == pj.f.L ? new b3(this) : null;
   }

   public com.larpclient.f a() {
      return this.G;
   }

   public uu U() {
      return this.t;
   }

   public se V() {
      return this.x;
   }

   public b3 s() {
      return this.s;
   }

   public double e() {
      return this.h == pj.f.e ? 1.25 : (this.h == pj.f.C ? 1.32 : Double.POSITIVE_INFINITY);
   }

   public static double Z(double var0) {
      return st.h(var0);
   }

   public static double T(double var0, double var2, double var4) {
      return !(var0 < var2) && !(var0 >= var4) ? Z((var0 - var2) / 0.09) * Z((var4 - var0) / 0.22) : 0.0;
   }

   public Vec3 o(int var1) {
      double var2 = R(var1, this.l) / 16.0 * 0.9375;
      return new Vec3(var2 * Math.cos(this.z), (24.0 - S[var1][1]) / 16.0 * 0.9375, var2 * Math.sin(this.z));
   }

   public static double R(int var0, boolean var1) {
      return var1 && (var0 == 2 || var0 == 3) ? S[var0][0] + (var0 == 2 ? 0.5 : -0.5) : S[var0][0];
   }

   public static double e(int var0) {
      return var0 == 0 ? 0.46 : (var0 == 1 ? 0.47 : 0.42);
   }

   public void O(double var1, b5.c var3) {
      double var4 = this.c - this.U;
      double var6 = var1 - this.U;
      this.c = var1;
      if (this.G != null) {
         this.G.R(var3, 48);
      }

      double var8 = this.e();
      if (Double.isFinite(var8) && !(var6 < var8) && !(var1 < this.U)) {
         if (!this.y) {
            this.y = (boolean)1;

            for (int var10 = 0; var10 < 6; var10++) {
               if (this.h == pj.f.C) {
                  this.E[var10] = this.r[var10] = se.v(this, var10, var8).position();
                  this.g[var10] = se.a(this, var10);
               } else {
                  this.E[var10] = this.r[var10] = this.v.add(this.o(var10)).add(0.0, 1.7, 0.0);
                  double var11 = var10 * Math.PI * 2.0 / 6.0 + this.F(var10) * 0.6;
                  this.g[var10] = new Vec3(Math.cos(var11) * (1.1 + this.F(var10 + 8)), 0.65 + this.F(var10 + 15), Math.sin(var11) * (1.1 + this.F(var10 + 8)));
               }
            }

            var4 = var8;
         }

         double var25 = Math.clamp(var6 - Math.max(var4, var8), 0.0, 0.05);

         for (int var12 = 0; var12 < 6; var12++) {
            this.r[var12] = this.E[var12];
            this.Y[var12] = this.D[var12];
            if (!this.R[var12] && var25 != 0.0) {
               this.g[var12] = this.g[var12].add(0.0, -8.8 * var25, 0.0);
               Vec3 var13 = this.E[var12].add(this.g[var12].scale(var25));
               double var14 = e(var12);
               Vec3 var16 = new Vec3(0.0, -var14, 0.0);
               b5.e var17 = var3 == null ? null : var3.p(this.E[var12].add(var16), var13.add(var16));
               Vec3 var18 = var13.subtract(this.E[var12]);

               for (int var19 = 0; var19 < 2 && var3 != null; var19++) {
                  double var20 = var19 == 0 ? var18.x : var18.z;
                  if (!(Math.abs(var20) < 1.0E-8)) {
                     Vec3 var22 = var19 == 0 ? new Vec3(Math.copySign(var14, var20), 0.0, 0.0) : new Vec3(0.0, 0.0, Math.copySign(var14, var20));
                     Vec3 var23 = this.E[var12].add(var22);
                     b5.e var24 = var3.p(var23, var13.add(var22));
                     if (var24 != null && (var17 == null || var23.distanceToSqr(var24.point()) < this.E[var12].add(var16).distanceToSqr(var17.point()))) {
                        var17 = var24;
                        var16 = var22;
                     }
                  }
               }

               if (var17 != null) {
                  this.E[var12] = var17.point().subtract(var16).add(var17.normal().scale(var14 + 0.025 + var16.dot(var17.normal())));
                  if (var17.normal().y > 0.5) {
                     this.R[var12] = (boolean)1;
                     this.g[var12] = Vec3.ZERO;
                     this.E[var12] = new Vec3(this.E[var12].x, var17.point().y + (var12 == 0 ? 0.27 : 0.15), this.E[var12].z);
                  } else {
                     this.g[var12] = new Vec3(0.0, this.g[var12].y * 0.85, 0.0);
                  }
               } else {
                  this.E[var12] = var13;
               }

               if (!this.R[var12]) {
                  this.D[var12] = this.D[var12] + var25 * (2.8 + this.F(var12 + 22) * 2.0);
               }
            }
         }
      }
   }

   public pj.n r(int var1, double var2, double var4) {
      return this.i(var1, var2, var4, uy.V);
   }

   public pj.n i(int var1, double var2, double var4, uy.b var6) {
      if (this.h == pj.f.P) {
         return uy.c(this, var1, var2, var6);
      }

      if (this.h == pj.f.Q) {
         return ln.a(this, var1, var2);
      }

      if (this.h == pj.f.E) {
         return ja.k(this, var1, var2);
      }

      if (this.h == pj.f.m) {
         return uu.y(this, var1, var2);
      }

      if (this.h == pj.f.L) {
         return b3.k(this, var1, var2);
      }

      Vec3 var7 = this.o(var1);
      double var8 = Z((4.6 - var2) / 0.75);
      if (this.h == pj.f.C) {
         if (!(var2 < 1.32) && this.y) {
            double var23 = this.Y[var1] + (this.D[var1] - this.Y[var1]) * Math.clamp(var4, 0.0, 1.0);
            double var25 = Z((var2 - 2.7 - this.F(var1 + 40) * 0.3) / 1.2);
            return new pj.n(
               this.r[var1].lerp(this.E[var1], Math.clamp(var4, 0.0, 1.0)),
               this.R[var1] ? Math.PI / 2 : var23 * 0.75 + se.l(),
               this.R[var1] ? 0.0 : var23 * 0.45,
               this.R[var1] ? var23 * 0.45 : var23 + se.I(var1),
               1.0,
               var8,
               var25,
               this.R[var1]
            );
         } else {
            return se.v(this, var1, var2);
         }
      } else if (this.h == pj.f.e) {
         if (!(var2 < 1.25) && this.y) {
            double var22 = this.Y[var1] + (this.D[var1] - this.Y[var1]) * Math.clamp(var4, 0.0, 1.0);
            return new pj.n(
               this.r[var1].lerp(this.E[var1], Math.clamp(var4, 0.0, 1.0)),
               this.R[var1] ? Math.PI / 2 : var22 * 0.75 + (var1 < 2 ? -0.07 : 0.0),
               this.R[var1] ? 0.0 : var22 * 0.45,
               this.R[var1] ? var22 * 0.45 : var22 + (var1 == 2 ? -0.32 : (var1 == 3 ? 0.32 : 0.0)),
               1.0,
               var8,
               0.0,
               this.R[var1]
            );
         } else {
            double var21 = 1.7 * Z(var2 / 0.85);
            double var24 = Z(var2 / 0.8);
            return new pj.n(
               this.v.add(var7).add(0.0, var21, 0.0),
               var1 < 2 ? -0.07 * var24 : 0.0,
               0.0,
               var1 == 2 ? -0.32 * var24 : (var1 == 3 ? 0.32 * var24 : 0.0),
               1.0,
               var8,
               0.0,
               false
            );
         }
      } else if (this.h == pj.f.X) {
         double var20 = Z((var2 - 0.45) / 1.45);
         double var12 = var20 * 8.5;
         Vec3 var14 = var7.subtract(0.0, 1.05, 0.0).scale(1.0 - var20);
         double var15 = Math.cos(var12);
         double var17 = Math.sin(var12);
         Vec3 var19 = new Vec3(var14.x * var15 - var14.z * var17, var14.y, var14.x * var17 + var14.z * var15);
         return new pj.n(
            this.v.add(0.0, 1.05 + 0.35 * Math.sin(var20 * Math.PI), 0.0).add(var19),
            var20 * 2.0,
            var12,
            var20 * 1.2,
            Math.max(0.0, 1.0 - var20),
            1.0 - var20,
            0.0,
            false
         );
      } else {
         double var10 = Z((var2 - 0.35 - (1.8 - var7.y) * 0.3) / 1.2);
         return new pj.n(
            this.v.add(var7).add(Math.sin(var2 * 2.0 + var1) * var10 * 0.04, var10 * 0.38, 0.0), var10 * 0.1, 0.0, 0.0, 1.0, 1.0 - var10 * 0.2, var10, false
         );
      }
   }

   public double Z() {
      return this.h == pj.f.E ? 7.5 : (this.h == pj.f.m ? 8.0 : (this.h == pj.f.L ? 6.4 : 4.6));
   }

   public boolean n(double var1) {
      return var1 - this.U >= this.Z();
   }

   public boolean W(int var1) {
      return this.R[var1];
   }

   public double F(int var1) {
      long var2 = this.j + var1 * -7046029254386353131L;
      var2 = (var2 ^ var2 >>> 30) * -4658895280553007687L;
      var2 = (var2 ^ var2 >>> 27) * -7723592293110705685L;
      return ((var2 ^ var2 >>> 31) >>> 11) * 1.110223E-16F;
   }

   public enum f {
      e,
      X,
      h,
      P,
      Q,
      E,
      m,
      C,
      L;
   }

   public record n(
      Vec3 position, double rx, double ry, double rz, double scale, double alpha, double dissolve, boolean landed, Quaternionfc orientation, Vec3 stretch
   ) {
      public n(Vec3 var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean var14) {
         this(var1, var2, var4, var6, var8, var10, var12, var14, null, null);
      }
   }
}
