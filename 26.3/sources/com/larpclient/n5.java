package com.larpclient;

import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionfc;

public final class n5 {
   public static final int X = 6;
   public static final double J = 1.25;
   public static final double H = 4.6;
   public static final double s = 0.9375;
   public static final double[][] a = new double[][]{{0.0, -4.0, 0.0}, {0.0, 6.0, 0.0}, {-6.0, 6.0, 0.0}, {6.0, 6.0, 0.0}, {-1.9, 18.0, 0.0}, {1.9, 18.0, 0.0}};
   private final Vec3[] S = new Vec3[6];
   private final Vec3[] k = new Vec3[6];
   private final Vec3[] g = new Vec3[6];
   private final double[] G = new double[6];
   private final double[] m = new double[6];
   private final boolean[] C = new boolean[6];
   public final n5.f E;
   public final Vec3 r;
   public final double y;
   public final double A;
   public final long p;
   public final boolean Q;
   private final ap R;
   private final dd o;
   private final vn W;
   private final jl B;
   private double L;
   private boolean h;

   public n5(n5.f var1, Vec3 var2, double var3, double var5, long var7) {
      this(var1, var2, var3, var5, var7, false);
   }

   public n5(n5.f var1, Vec3 var2, double var3, double var5, long var7, boolean var9) {
      this.E = var1;
      this.r = var2;
      this.y = var3;
      this.A = var5;
      this.p = var7;
      this.Q = var9;
      this.L = var5;
      this.R = var1 != n5.f.h && var1 != n5.f.i ? null : new ap(var2);
      this.o = var1 == n5.f.h ? new dd(this, this.R) : null;
      this.W = var1 == n5.f.i ? new vn(this, this.R) : null;
      this.B = var1 == n5.f.S ? new jl(this) : null;
   }

   public ap H() {
      return this.R;
   }

   public dd O() {
      return this.o;
   }

   public vn b() {
      return this.W;
   }

   public jl R() {
      return this.B;
   }

   public double r() {
      return this.E == n5.f.Q ? 1.25 : (this.E == n5.f.i ? 1.32 : Double.POSITIVE_INFINITY);
   }

   public static double g(double var0) {
      return pr.k(var0);
   }

   public static double r(double var0, double var2, double var4) {
      return !(var0 < var2) && !(var0 >= var4) ? g((var0 - var2) / 0.09) * g((var4 - var0) / 0.22) : 0.0;
   }

   public Vec3 b(int var1) {
      double var2 = H(var1, this.Q) / 16.0 * 0.9375;
      return new Vec3(var2 * Math.cos(this.y), (24.0 - a[var1][1]) / 16.0 * 0.9375, var2 * Math.sin(this.y));
   }

   public static double H(int var0, boolean var1) {
      return var1 && (var0 == 2 || var0 == 3) ? a[var0][0] + (var0 == 2 ? 0.5 : -0.5) : a[var0][0];
   }

   public static double e(int var0) {
      return var0 == 0 ? 0.46 : (var0 == 1 ? 0.47 : 0.42);
   }

   public void V(double var1, v9.w var3) {
      double var4 = this.L - this.A;
      double var6 = var1 - this.A;
      this.L = var1;
      if (this.R != null) {
         this.R.p(var3, 48);
      }

      double var8 = this.r();
      if (Double.isFinite(var8) && !(var6 < var8) && !(var1 < this.A)) {
         if (!this.h) {
            this.h = (boolean)1;

            for (int var10 = 0; var10 < 6; var10++) {
               if (this.E == n5.f.i) {
                  this.S[var10] = this.k[var10] = vn.f(this, var10, var8).position();
                  this.g[var10] = vn.q(this, var10);
               } else {
                  this.S[var10] = this.k[var10] = this.r.add(this.b(var10)).add(0.0, 1.7, 0.0);
                  double var11 = var10 * Math.PI * 2.0 / 6.0 + this.w(var10) * 0.6;
                  this.g[var10] = new Vec3(Math.cos(var11) * (1.1 + this.w(var10 + 8)), 0.65 + this.w(var10 + 15), Math.sin(var11) * (1.1 + this.w(var10 + 8)));
               }
            }

            var4 = var8;
         }

         double var25 = Math.clamp(var6 - Math.max(var4, var8), 0.0, 0.05);

         for (int var12 = 0; var12 < 6; var12++) {
            this.k[var12] = this.S[var12];
            this.m[var12] = this.G[var12];
            if (!this.C[var12] && var25 != 0.0) {
               this.g[var12] = this.g[var12].add(0.0, -8.8 * var25, 0.0);
               Vec3 var13 = this.S[var12].add(this.g[var12].scale(var25));
               double var14 = e(var12);
               Vec3 var16 = new Vec3(0.0, -var14, 0.0);
               v9.d var17 = var3 == null ? null : var3.f(this.S[var12].add(var16), var13.add(var16));
               Vec3 var18 = var13.subtract(this.S[var12]);

               for (int var19 = 0; var19 < 2 && var3 != null; var19++) {
                  double var20 = var19 == 0 ? var18.x : var18.z;
                  if (!(Math.abs(var20) < 1.0E-8)) {
                     Vec3 var22 = var19 == 0 ? new Vec3(Math.copySign(var14, var20), 0.0, 0.0) : new Vec3(0.0, 0.0, Math.copySign(var14, var20));
                     Vec3 var23 = this.S[var12].add(var22);
                     v9.d var24 = var3.f(var23, var13.add(var22));
                     if (var24 != null && (var17 == null || var23.distanceToSqr(var24.point()) < this.S[var12].add(var16).distanceToSqr(var17.point()))) {
                        var17 = var24;
                        var16 = var22;
                     }
                  }
               }

               if (var17 != null) {
                  this.S[var12] = var17.point().subtract(var16).add(var17.normal().scale(var14 + 0.025 + var16.dot(var17.normal())));
                  if (var17.normal().y > 0.5) {
                     this.C[var12] = (boolean)1;
                     this.g[var12] = Vec3.ZERO;
                     this.S[var12] = new Vec3(this.S[var12].x, var17.point().y + (var12 == 0 ? 0.27 : 0.15), this.S[var12].z);
                  } else {
                     this.g[var12] = new Vec3(0.0, this.g[var12].y * 0.85, 0.0);
                  }
               } else {
                  this.S[var12] = var13;
               }

               if (!this.C[var12]) {
                  this.G[var12] = this.G[var12] + var25 * (2.8 + this.w(var12 + 22) * 2.0);
               }
            }
         }
      }
   }

   public n5.p E(int var1, double var2, double var4) {
      return this.t(var1, var2, var4, hg.c);
   }

   public n5.p t(int var1, double var2, double var4, hg.k var6) {
      if (this.E == n5.f.G) {
         return hg.k(this, var1, var2, var6);
      }

      if (this.E == n5.f.N) {
         return sr.V(this, var1, var2);
      }

      if (this.E == n5.f.a) {
         return ae.B(this, var1, var2);
      }

      if (this.E == n5.f.h) {
         return dd.N(this, var1, var2);
      }

      if (this.E == n5.f.S) {
         return jl.V(this, var1, var2);
      }

      Vec3 var7 = this.b(var1);
      double var8 = g((4.6 - var2) / 0.75);
      if (this.E == n5.f.i) {
         if (!(var2 < 1.32) && this.h) {
            double var23 = this.m[var1] + (this.G[var1] - this.m[var1]) * Math.clamp(var4, 0.0, 1.0);
            double var25 = g((var2 - 2.7 - this.w(var1 + 40) * 0.3) / 1.2);
            return new n5.p(
               this.k[var1].lerp(this.S[var1], Math.clamp(var4, 0.0, 1.0)),
               this.C[var1] ? Math.PI / 2 : var23 * 0.75 + vn.J(),
               this.C[var1] ? 0.0 : var23 * 0.45,
               this.C[var1] ? var23 * 0.45 : var23 + vn.h(var1),
               1.0,
               var8,
               var25,
               this.C[var1]
            );
         } else {
            return vn.f(this, var1, var2);
         }
      } else if (this.E == n5.f.Q) {
         if (!(var2 < 1.25) && this.h) {
            double var22 = this.m[var1] + (this.G[var1] - this.m[var1]) * Math.clamp(var4, 0.0, 1.0);
            return new n5.p(
               this.k[var1].lerp(this.S[var1], Math.clamp(var4, 0.0, 1.0)),
               this.C[var1] ? Math.PI / 2 : var22 * 0.75 + (var1 < 2 ? -0.07 : 0.0),
               this.C[var1] ? 0.0 : var22 * 0.45,
               this.C[var1] ? var22 * 0.45 : var22 + (var1 == 2 ? -0.32 : (var1 == 3 ? 0.32 : 0.0)),
               1.0,
               var8,
               0.0,
               this.C[var1]
            );
         } else {
            double var21 = 1.7 * g(var2 / 0.85);
            double var24 = g(var2 / 0.8);
            return new n5.p(
               this.r.add(var7).add(0.0, var21, 0.0),
               var1 < 2 ? -0.07 * var24 : 0.0,
               0.0,
               var1 == 2 ? -0.32 * var24 : (var1 == 3 ? 0.32 * var24 : 0.0),
               1.0,
               var8,
               0.0,
               false
            );
         }
      } else if (this.E == n5.f.R) {
         double var20 = g((var2 - 0.45) / 1.45);
         double var12 = var20 * 8.5;
         Vec3 var14 = var7.subtract(0.0, 1.05, 0.0).scale(1.0 - var20);
         double var15 = Math.cos(var12);
         double var17 = Math.sin(var12);
         Vec3 var19 = new Vec3(var14.x * var15 - var14.z * var17, var14.y, var14.x * var17 + var14.z * var15);
         return new n5.p(
            this.r.add(0.0, 1.05 + 0.35 * Math.sin(var20 * Math.PI), 0.0).add(var19),
            var20 * 2.0,
            var12,
            var20 * 1.2,
            Math.max(0.0, 1.0 - var20),
            1.0 - var20,
            0.0,
            false
         );
      } else {
         double var10 = g((var2 - 0.35 - (1.8 - var7.y) * 0.3) / 1.2);
         return new n5.p(
            this.r.add(var7).add(Math.sin(var2 * 2.0 + var1) * var10 * 0.04, var10 * 0.38, 0.0), var10 * 0.1, 0.0, 0.0, 1.0, 1.0 - var10 * 0.2, var10, false
         );
      }
   }

   public double S() {
      return this.E == n5.f.a ? 7.5 : (this.E == n5.f.h ? 8.0 : (this.E == n5.f.S ? 6.4 : 4.6));
   }

   public boolean R(double var1) {
      return var1 - this.A >= this.S();
   }

   public boolean V(int var1) {
      return this.C[var1];
   }

   public double w(int var1) {
      long var2 = this.p + var1 * -7046029254386353131L;
      var2 = (var2 ^ var2 >>> 30) * -4658895280553007687L;
      var2 = (var2 ^ var2 >>> 27) * -7723592293110705685L;
      return ((var2 ^ var2 >>> 31) >>> 11) * 1.110223E-16F;
   }

   public enum f {
      Q,
      R,
      f,
      G,
      N,
      a,
      h,
      i,
      S;
   }

   public record p(
      Vec3 position, double rx, double ry, double rz, double scale, double alpha, double dissolve, boolean landed, Quaternionfc orientation, Vec3 stretch
   ) {
      public p(Vec3 var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean var14) {
         this(var1, var2, var4, var6, var8, var10, var12, var14, null, null);
      }
   }
}
