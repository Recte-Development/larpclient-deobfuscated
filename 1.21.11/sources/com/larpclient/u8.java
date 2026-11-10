package com.larpclient;

import net.minecraft.class_243;
import org.joml.Quaternionfc;

public final class u8 {
   public static final int t = 6;
   public static final double I = 1.25;
   public static final double g = 4.6;
   public static final double e = 0.9375;
   public static final double[][] E = new double[][]{{0.0, -4.0, 0.0}, {0.0, 6.0, 0.0}, {-6.0, 6.0, 0.0}, {6.0, 6.0, 0.0}, {-1.9, 18.0, 0.0}, {1.9, 18.0, 0.0}};
   private final class_243[] k = new class_243[6];
   private final class_243[] H = new class_243[6];
   private final class_243[] Z = new class_243[6];
   private final double[] N = new double[6];
   private final double[] C = new double[6];
   private final boolean[] F = new boolean[6];
   public final u8.w q;
   public final class_243 z;
   public final double y;
   public final double l;
   public final long u;
   public final boolean D;
   private final f7 a;
   private final g2 v;
   private final g0 p;
   private final e8 K;
   private double x;
   private boolean j;

   public u8(u8.w var1, class_243 var2, double var3, double var5, long var7) {
      this(var1, var2, var3, var5, var7, false);
   }

   public u8(u8.w var1, class_243 var2, double var3, double var5, long var7, boolean var9) {
      this.q = var1;
      this.z = var2;
      this.y = var3;
      this.l = var5;
      this.u = var7;
      this.D = var9;
      this.x = var5;
      this.a = var1 != u8.w.y && var1 != u8.w.v ? null : new f7(var2);
      this.v = var1 == u8.w.y ? new g2(this, this.a) : null;
      this.p = var1 == u8.w.v ? new g0(this, this.a) : null;
      this.K = var1 == u8.w.G ? new e8(this) : null;
   }

   public f7 x() {
      return this.a;
   }

   public g2 H() {
      return this.v;
   }

   public g0 j() {
      return this.p;
   }

   public e8 E() {
      return this.K;
   }

   public double f() {
      return this.q == u8.w.H ? 1.25 : (this.q == u8.w.v ? 1.32 : Double.POSITIVE_INFINITY);
   }

   public static double S(double var0) {
      return u9.L(var0);
   }

   public static double o(double var0, double var2, double var4) {
      return !(var0 < var2) && !(var0 >= var4) ? S((var0 - var2) / 0.09) * S((var4 - var0) / 0.22) : 0.0;
   }

   public class_243 h(int var1) {
      double var2 = z(var1, this.D) / 16.0 * 0.9375;
      return new class_243(var2 * Math.cos(this.y), (24.0 - E[var1][1]) / 16.0 * 0.9375, var2 * Math.sin(this.y));
   }

   public static double z(int var0, boolean var1) {
      return var1 && (var0 == 2 || var0 == 3) ? E[var0][0] + (var0 == 2 ? 0.5 : -0.5) : E[var0][0];
   }

   public static double k(int var0) {
      return var0 == 0 ? 0.46 : (var0 == 1 ? 0.47 : 0.42);
   }

   public void t(double var1, i3.k var3) {
      double var4 = this.x - this.l;
      double var6 = var1 - this.l;
      this.x = var1;
      if (this.a != null) {
         this.a.u(var3, 48);
      }

      double var8 = this.f();
      if (Double.isFinite(var8) && !(var6 < var8) && !(var1 < this.l)) {
         if (!this.j) {
            this.j = (boolean)1;

            for (int var10 = 0; var10 < 6; var10++) {
               if (this.q == u8.w.v) {
                  this.k[var10] = this.H[var10] = g0.y(this, var10, var8).position();
                  this.Z[var10] = g0.v(this, var10);
               } else {
                  this.k[var10] = this.H[var10] = this.z.method_1019(this.h(var10)).method_1031(0.0, 1.7, 0.0);
                  double var11 = var10 * Math.PI * 2.0 / 6.0 + this.P(var10) * 0.6;
                  this.Z[var10] = new class_243(
                     Math.cos(var11) * (1.1 + this.P(var10 + 8)), 0.65 + this.P(var10 + 15), Math.sin(var11) * (1.1 + this.P(var10 + 8))
                  );
               }
            }

            var4 = var8;
         }

         double var25 = Math.clamp(var6 - Math.max(var4, var8), 0.0, 0.05);

         for (int var12 = 0; var12 < 6; var12++) {
            this.H[var12] = this.k[var12];
            this.C[var12] = this.N[var12];
            if (!this.F[var12] && var25 != 0.0) {
               this.Z[var12] = this.Z[var12].method_1031(0.0, -8.8 * var25, 0.0);
               class_243 var13 = this.k[var12].method_1019(this.Z[var12].method_1021(var25));
               double var14 = k(var12);
               class_243 var16 = new class_243(0.0, -var14, 0.0);
               i3.j var17 = var3 == null ? null : var3.U(this.k[var12].method_1019(var16), var13.method_1019(var16));
               class_243 var18 = var13.method_1020(this.k[var12]);

               for (int var19 = 0; var19 < 2 && var3 != null; var19++) {
                  double var20 = var19 == 0 ? var18.field_1352 : var18.field_1350;
                  if (!(Math.abs(var20) < 1.0E-8)) {
                     class_243 var22 = var19 == 0 ? new class_243(Math.copySign(var14, var20), 0.0, 0.0) : new class_243(0.0, 0.0, Math.copySign(var14, var20));
                     class_243 var23 = this.k[var12].method_1019(var22);
                     i3.j var24 = var3.U(var23, var13.method_1019(var22));
                     if (var24 != null && (var17 == null || var23.method_1025(var24.point()) < this.k[var12].method_1019(var16).method_1025(var17.point()))) {
                        var17 = var24;
                        var16 = var22;
                     }
                  }
               }

               if (var17 != null) {
                  this.k[var12] = var17.point().method_1020(var16).method_1019(var17.normal().method_1021(var14 + 0.025 + var16.method_1026(var17.normal())));
                  if (var17.normal().field_1351 > 0.5) {
                     this.F[var12] = (boolean)1;
                     this.Z[var12] = class_243.field_1353;
                     this.k[var12] = new class_243(this.k[var12].field_1352, var17.point().field_1351 + (var12 == 0 ? 0.27 : 0.15), this.k[var12].field_1350);
                  } else {
                     this.Z[var12] = new class_243(0.0, this.Z[var12].field_1351 * 0.85, 0.0);
                  }
               } else {
                  this.k[var12] = var13;
               }

               if (!this.F[var12]) {
                  this.N[var12] = this.N[var12] + var25 * (2.8 + this.P(var12 + 22) * 2.0);
               }
            }
         }
      }
   }

   public u8.s e(int var1, double var2, double var4) {
      return this.S(var1, var2, var4, yt.G);
   }

   public u8.s S(int var1, double var2, double var4, yt.l var6) {
      if (this.q == u8.w.Y) {
         return yt.i(this, var1, var2, var6);
      }

      if (this.q == u8.w.e) {
         return jo.f(this, var1, var2);
      }

      if (this.q == u8.w.K) {
         return g4.a(this, var1, var2);
      }

      if (this.q == u8.w.y) {
         return g2.Y(this, var1, var2);
      }

      if (this.q == u8.w.G) {
         return e8.f(this, var1, var2);
      }

      class_243 var7 = this.h(var1);
      double var8 = S((4.6 - var2) / 0.75);
      if (this.q == u8.w.v) {
         if (!(var2 < 1.32) && this.j) {
            double var23 = this.C[var1] + (this.N[var1] - this.C[var1]) * Math.clamp(var4, 0.0, 1.0);
            double var25 = S((var2 - 2.7 - this.P(var1 + 40) * 0.3) / 1.2);
            return new u8.s(
               this.H[var1].method_35590(this.k[var1], Math.clamp(var4, 0.0, 1.0)),
               this.F[var1] ? Math.PI / 2 : var23 * 0.75 + g0.f(),
               this.F[var1] ? 0.0 : var23 * 0.45,
               this.F[var1] ? var23 * 0.45 : var23 + g0.s(var1),
               1.0,
               var8,
               var25,
               this.F[var1]
            );
         } else {
            return g0.y(this, var1, var2);
         }
      } else if (this.q == u8.w.H) {
         if (!(var2 < 1.25) && this.j) {
            double var22 = this.C[var1] + (this.N[var1] - this.C[var1]) * Math.clamp(var4, 0.0, 1.0);
            return new u8.s(
               this.H[var1].method_35590(this.k[var1], Math.clamp(var4, 0.0, 1.0)),
               this.F[var1] ? Math.PI / 2 : var22 * 0.75 + (var1 < 2 ? -0.07 : 0.0),
               this.F[var1] ? 0.0 : var22 * 0.45,
               this.F[var1] ? var22 * 0.45 : var22 + (var1 == 2 ? -0.32 : (var1 == 3 ? 0.32 : 0.0)),
               1.0,
               var8,
               0.0,
               this.F[var1]
            );
         } else {
            double var21 = 1.7 * S(var2 / 0.85);
            double var24 = S(var2 / 0.8);
            return new u8.s(
               this.z.method_1019(var7).method_1031(0.0, var21, 0.0),
               var1 < 2 ? -0.07 * var24 : 0.0,
               0.0,
               var1 == 2 ? -0.32 * var24 : (var1 == 3 ? 0.32 * var24 : 0.0),
               1.0,
               var8,
               0.0,
               false
            );
         }
      } else if (this.q == u8.w.R) {
         double var20 = S((var2 - 0.45) / 1.45);
         double var12 = var20 * 8.5;
         class_243 var14 = var7.method_1023(0.0, 1.05, 0.0).method_1021(1.0 - var20);
         double var15 = Math.cos(var12);
         double var17 = Math.sin(var12);
         class_243 var19 = new class_243(
            var14.field_1352 * var15 - var14.field_1350 * var17, var14.field_1351, var14.field_1352 * var17 + var14.field_1350 * var15
         );
         return new u8.s(
            this.z.method_1031(0.0, 1.05 + 0.35 * Math.sin(var20 * Math.PI), 0.0).method_1019(var19),
            var20 * 2.0,
            var12,
            var20 * 1.2,
            Math.max(0.0, 1.0 - var20),
            1.0 - var20,
            0.0,
            false
         );
      } else {
         double var10 = S((var2 - 0.35 - (1.8 - var7.field_1351) * 0.3) / 1.2);
         return new u8.s(
            this.z.method_1019(var7).method_1031(Math.sin(var2 * 2.0 + var1) * var10 * 0.04, var10 * 0.38, 0.0),
            var10 * 0.1,
            0.0,
            0.0,
            1.0,
            1.0 - var10 * 0.2,
            var10,
            false
         );
      }
   }

   public double p() {
      return this.q == u8.w.K ? 7.5 : (this.q == u8.w.y ? 8.0 : (this.q == u8.w.G ? 6.4 : 4.6));
   }

   public boolean X(double var1) {
      return var1 - this.l >= this.p();
   }

   public boolean O(int var1) {
      return this.F[var1];
   }

   public double P(int var1) {
      long var2 = this.u + var1 * -7046029254386353131L;
      var2 = (var2 ^ var2 >>> 30) * -4658895280553007687L;
      var2 = (var2 ^ var2 >>> 27) * -7723592293110705685L;
      return ((var2 ^ var2 >>> 31) >>> 11) * 1.110223E-16F;
   }

   public record s(
      class_243 position,
      double rx,
      double ry,
      double rz,
      double scale,
      double alpha,
      double dissolve,
      boolean landed,
      Quaternionfc orientation,
      class_243 stretch
   ) {
      public s(class_243 var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean var14) {
         this(var1, var2, var4, var6, var8, var10, var12, var14, null, null);
      }
   }

   public enum w {
      H,
      R,
      I,
      Y,
      e,
      K,
      y,
      v,
      G;
   }
}
