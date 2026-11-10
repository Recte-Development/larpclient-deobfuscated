package com.larpclient;

import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.class_243;

public final class s9 {
   static final int f = 128;
   static final int I = 16;
   static final int N = 8;
   static final int Q = 3;
   static final int i = 382;
   private final Map<Integer, s9.g> x = new LinkedHashMap<>();
   private long S;
   private boolean Y;
   private ti.d v = ti.d.l;
   public s9.s E = s9.s.h;
   public ce.b g = ce.b.p;
   public u9.u b = u9.u.W;
   public double z = 1.2;
   public double o = 0.12;
   public boolean q = (boolean)1;
   public boolean a = (boolean)1;
   public int k = ez.a;
   public int l;

   public s9() {
      this.l = ez.f;
      q8.G("Trails", "Body trail engine initialized: {} sources, {} points per source", 16, 128);
   }

   public boolean N() {
      return this.x.isEmpty();
   }

   public int d() {
      return this.x.size();
   }

   public int w() {
      return this.x.values().stream().mapToInt(var0 -> var0.k).sum();
   }

   public boolean U() {
      return this.Y;
   }

   public void d() {
      this.x.clear();
      this.S = 0L;
      this.Y = (boolean)0;
   }

   public void n() {
      this.S++;
      this.x.values().removeIf(var1 -> {
         while (var1.k > 0 && this.S - var1.j[var1.O] > this.z * 20.0) {
            var1.O = (var1.O + 1) % 128;
            var1.k--;
         }

         return var1.k == 0;
      });
   }

   public void F(int var1, class_243 var2, double var3, double var5) {
      if (Double.isFinite(var2.field_1352)
         && Double.isFinite(var2.field_1351)
         && Double.isFinite(var2.field_1350)
         && Double.isFinite(var3)
         && Double.isFinite(var5)) {
         var3 = Math.clamp(var3, 0.1, 6.0);
         var5 = Math.clamp(var5, 0.1, 4.0);
         s9.g var7 = this.x.get(var1);
         if (var7 == null) {
            if (this.x.size() >= 16) {
               return;
            }

            var7 = new s9.g();
            this.x.put(var1, var7);
         }

         double var8 = var7.i;
         if (var7.k > 0) {
            int var10 = (var7.O + var7.k - 1) % 128;
            double var11 = var7.m[var10].method_1025(var2);
            if (var11 > 64.0) {
               var7.O = var7.k = 0;
            } else {
               if (var11 < 4.0E-4 && Math.abs(var7.M[var10] - var3) < 0.02) {
                  return;
               }

               var8 += Math.sqrt(var11);
            }
         }

         if (var7.k == 128) {
            var7.O = (var7.O + 1) % 128;
            var7.k--;
         }

         int var15 = (var7.O + var7.k++) % 128;
         var7.m[var15] = var2;
         var7.M[var15] = var3;
         var7.D[var15] = var5;
         var7.y[var15] = var7.i = var8;
         var7.j[var15] = this.S;
      }
   }

   public void n(class_243 var1, float var2) {
      this.Y = (boolean)0;
      this.v = this.E == s9.s.T ? ti.d.G : ti.d.l;
      double var3 = qo.x();

      for (s9.g var6 : this.x.values()) {
         var6.L = (boolean)0;
         var6.Q = var6.A = 0;
         this.Y(var6, var2);
         if (var6.W >= 2) {
            switch (this.E) {
               case T:
                  this.Q(var6, var1, var3);
                  break;
               case h:
                  this.B(var6, var1, var3);
                  break;
               case e:
                  this.D(var6, var1, var3);
                  break;
               case V:
                  this.Z(var6, var1, var3);
                  break;
               case Z:
                  this.L(var6, var1, var3);
            }

            for (int var7 = 0; var7 < var6.Q; var7++) {
               qd.x var8 = var6.F[var7];
               if (this.g != ce.b.J) {
                  var8.V();
               }

               var8.k(var1);
               var6.s[var7] = F(var8, var1);
               var6.L = var6.L | var6.s[var7];
            }

            if (this.q && var6.L) {
               this.Y = (boolean)1;
            }
         }
      }
   }

   public void l(f1 var1, boolean var2) {
      if (!var2 || this.q) {
         long var3 = c5.X();
         var1.blend(this.v);

         for (s9.g var6 : this.x.values()) {
            if (var6.L) {
               for (int var7 = 0; var7 < var6.Q; var7++) {
                  if (var6.s[var7]) {
                     if (var6.z[var7] >= 0.0) {
                        var1.d(var6.F[var7], var6.z[var7]);
                     } else {
                        var1.G(var6.F[var7], var6.X[var7]);
                     }
                  }
               }
            }
         }

         var1.blend(ti.d.l);

         for (s9.g var13 : this.x.values()) {
            if (var13.L) {
               for (int var14 = 0; var14 < var13.A; var14++) {
                  class_243 var8 = var13.E[var14];
                  int var9 = var13.U[var14];
                  double var10 = var13.S[var14];
                  var1.U(var8.field_1352, var8.field_1351, var8.field_1350, var10, 0.0, ei.R(var9, 0.32), gd.h.f, 0.0, 0.0, 0.0);
                  var1.U(
                     var8.field_1352, var8.field_1351, var8.field_1350, var10 * 0.32, 0.0, ei.W(var9, var9 & 0xFF000000 | 16777215, 0.7), gd.h.f, 0.0, 0.0, 0.0
                  );
               }
            }
         }

         var1.blend(ti.d.G);
         c5.w("Body Trails", var3);
      }
   }

   private void Y(s9.g var1, float var2) {
      var1.W = 0;
      if (var1.k != 0) {
         int var3 = this.a ? 3 : 1;

         for (int var4 = 1; var4 < var1.k; var4++) {
            int var5 = (var1.O + var4 - 1) % 128;
            int var6 = (var1.O + var4) % 128;
            class_243 var7 = var1.m[var5];
            class_243 var8 = var1.m[var6];
            double var9 = var1.M[var6];
            double var11 = var1.D[var6];
            double var13 = var1.y[var6];
            if (var4 == var1.k - 1 && var1.j[var6] == this.S) {
               var8 = var7.method_35590(var8, var2);
               var9 = var1.M[var5] + (var9 - var1.M[var5]) * var2;
               var11 = var1.D[var5] + (var11 - var1.D[var5]) * var2;
               var13 = var1.y[var5] + (var13 - var1.y[var5]) * var2;
            }

            double var15 = this.M(var1.j[var5], var2);
            double var17 = this.M(var1.j[var6], var2);
            if (var4 == 1) {
               this.x(var1, var7, var1.M[var5], var1.D[var5], var15, var1.y[var5]);
            }

            if (var3 > 1) {
               class_243 var19 = var4 > 1 ? var1.m[(var1.O + var4 - 2) % 128] : var7.method_1019(var7.method_1020(var8));
               class_243 var20 = var4 < var1.k - 1 ? var1.m[(var1.O + var4 + 1) % 128] : var8.method_1019(var8.method_1020(var7));
               var1.f.V(var19, var7, var8, var20);
            }

            for (int var27 = 1; var27 <= var3; var27++) {
               double var28 = (double)var27 / var3;
               this.x(
                  var1,
                  var3 == 1 ? var8 : var1.f.K(var28),
                  var1.M[var5] + (var9 - var1.M[var5]) * var28,
                  var1.D[var5] + (var11 - var1.D[var5]) * var28,
                  var15 + (var17 - var15) * var28,
                  var1.y[var5] + (var13 - var1.y[var5]) * var28
               );
            }
         }

         class_243 var22 = null;

         for (int var23 = 0; var23 < var1.W; var23++) {
            class_243 var24 = var1.J[Math.min(var23 + 1, var1.W - 1)].method_1020(var1.J[Math.max(var23 - 1, 0)]);
            double var25 = Math.sqrt(var24.field_1352 * var24.field_1352 + var24.field_1350 * var24.field_1350);
            class_243 var26 = var25 < 1.0E-6
               ? (var22 != null ? var22 : new class_243(1.0, 0.0, 0.0))
               : new class_243(-var24.field_1350 / var25, 0.0, var24.field_1352 / var25);
            if (var22 != null && var26.method_1026(var22) < 0.0) {
               var26 = var26.method_1021(-1.0);
            }

            var22 = var26;
            var1.h[var23] = var26;
         }
      }
   }

   private void x(s9.g var1, class_243 var2, double var3, double var5, double var7, double var9) {
      int var11 = var1.W++;
      var1.J[var11] = var2;
      var1.R[var11] = var3;
      var1.a[var11] = var5;
      var1.G[var11] = var7;
      var1.p[var11] = var9;
   }

   private double M(long var1, float var3) {
      return Math.clamp(1.0 - ((float)this.S + var3 - (float)var1) / (this.z * 20.0), 0.0, 1.0);
   }

   private void Q(s9.g var1, class_243 var2, double var3) {
      this.H(var1, var2, 4, 0.1, 0.0, 0.0, var3, false);
      double[] var5 = new double[]{0.16, 0.52, 0.88};

      for (int var6 = 0; var6 < var5.length; var6++) {
         qd.x var7 = this.E(var1, 0.55, false);

         for (int var8 = 0; var8 < var1.W; var8++) {
            double var9 = var1.G[var8];
            double var11 = var1.R[var8];
            double var13 = var1.p[var8];
            double var15 = 1.0 - var9;
            double var17 = var11 * var5[var6] + Math.sin(var13 * 2.4 + var6 * 2.1 - var3 * 1.6) * 0.09 * var11 * var15;
            double var19 = Math.cos(var13 * 1.8 + var6 * 1.7 - var3 * 1.2) * 0.16 * var15;
            int var21 = ei.W(this.L(var9), ei.W(this.k, this.l, 0.5), var6 * 0.18);
            this.A(var7, var1, var8, var17, var19, this.o * 1.15 * this.D(var9), var21, 0.9 * this.b.E(1.0 - var9), var2);
         }
      }
   }

   private void B(s9.g var1, class_243 var2, double var3) {
      this.H(var1, var2, 6, 0.24, 3.2, 5.0, var3, true);

      for (int var5 = 0; var5 < 2; var5++) {
         qd.x var6 = this.E(var1, -1.0, true);

         for (int var7 = 0; var7 < var1.W; var7++) {
            double var8 = var1.G[var7];
            double var10 = var1.R[var7];
            double var12 = Math.min(1.0, this.D(var8));
            double var14 = var5 == 0 ? var10 * 0.97 : var10 * 0.04;
            double var16 = var10 * 0.5;
            double var18 = var16 + (var14 - var16) * (0.25 + 0.75 * var12);
            this.A(var6, var1, var7, var18, 0.0, this.o * 0.55 * this.D(var8), this.L(var8), this.b.E(1.0 - var8), var2);
         }
      }
   }

   private void D(s9.g var1, class_243 var2, double var3) {
      double[] var5 = new double[]{-1.0, 0.0, 1.0};

      for (double var9 : var5) {
         qd.x var11 = this.E(var1, 0.0, false);

         for (int var12 = 0; var12 < var1.W; var12++) {
            double var13 = var1.G[var12];
            double var15 = var1.R[var12];
            double var17 = var13 * var13;
            this.A(var11, var1, var12, var15 * (0.55 + var9 * 0.32 * var17), 0.0, var15 * 0.42 * var17, this.L(var13), 0.13 * this.b.E(1.0 - var13), var2);
         }
      }

      for (int var19 = 0; var19 < 2; var19++) {
         qd.x var21 = this.E(var1, -1.0, true);

         for (int var24 = 0; var24 < var1.W; var24++) {
            double var26 = var1.G[var24];
            double var27 = var1.R[var24];
            double var28 = var1.p[var24];
            double var29 = 1.0 - var26;
            double var30 = var28 * 3.0 + var19 * Math.PI - var3 * 3.0;
            this.A(
               var21,
               var1,
               var24,
               var27 * 0.55 + Math.cos(var30) * 0.22 * var27 * var29,
               Math.sin(var30) * 0.3 * var29,
               this.o * 0.4 * this.D(var26),
               ei.W(this.L(var26), this.l, 0.3),
               0.8 * this.b.E(1.0 - var26),
               var2
            );
         }
      }

      qd.x var20 = this.E(var1, -1.0, true);

      for (int var22 = 0; var22 < var1.W; var22++) {
         double var25 = var1.G[var22];
         this.A(var20, var1, var22, var1.R[var22] * 0.55, 0.0, this.o * 2.2 * this.D(var25), this.L(var25), (0.4 + 0.6 * var25) * this.b.E(1.0 - var25), var2);
      }

      int var23 = var1.W - 1;
      this.h(var1, var1.J[var23].method_1031(0.0, var1.R[var23] * 0.55, 0.0), this.k, var1.R[var23] * 0.3, var2);
   }

   private void Z(s9.g var1, class_243 var2, double var3) {
      qd.x var5 = this.E(var1, 0.0, false);

      for (int var6 = 0; var6 < var1.W; var6++) {
         double var7 = var1.G[var6];
         this.A(
            var5, var1, var6, var1.R[var6] * 0.5, 0.0, var1.R[var6] * 0.3 * Math.min(1.0, this.D(var7) + 0.2), this.L(var7), 0.12 * this.b.E(1.0 - var7), var2
         );
      }

      for (int var25 = 0; var25 < 2; var25++) {
         qd.x var26 = this.E(var1, -1.0, true);
         class_243 var8 = null;

         for (int var9 = 0; var9 < var1.W; var9++) {
            double var10 = var1.G[var9];
            double var12 = var1.R[var9];
            double var14 = var1.p[var9];
            double var16 = 0.6 + 0.4 * var10;
            double var18 = var14 * 2.4 + var25 * Math.PI - var3 * 2.4;
            double var20 = 0.46 * var12 * var16;
            double var22 = Math.max(var1.a[var9] * 0.8, 0.45) * var16;
            int var24 = var25 == 0 ? this.L(var10) : ei.W(this.k, this.l, var10);
            this.A(
               var26,
               var1,
               var9,
               var12 * 0.5 + Math.cos(var18) * var20,
               Math.sin(var18) * var22,
               this.o * 0.75 * this.D(var10),
               var24,
               this.b.E(1.0 - var10),
               var2
            );
            var8 = var26.Q[var26.w - 1];
         }

         if (var8 != null) {
            this.h(var1, var8, var25 == 0 ? this.k : this.l, 0.16, var2);
         }
      }
   }

   private void L(s9.g var1, class_243 var2, double var3) {
      this.H(var1, var2, 4, 0.07, 7.0, 3.0, var3, true);
   }

   private void H(s9.g var1, class_243 var2, int var3, double var4, double var6, double var8, double var10, boolean var12) {
      for (int var13 = 0; var13 < var3; var13++) {
         qd.x var14 = this.E(var1, 0.0, false);
         double var15 = (var13 + 0.5) / var3;
         double var17 = Math.abs(var15 * 2.0 - 1.0);
         double var19 = var12 ? 0.55 + 0.45 * var17 * var17 : 1.0;

         for (int var21 = 0; var21 < var1.W; var21++) {
            double var22 = var1.G[var21];
            double var24 = var1.R[var21];
            double var26 = var1.p[var21];
            double var28 = 0.3 + 0.7 * Math.min(1.0, this.D(var22));
            double var30 = var24 * 0.5 + (var15 - 0.5) * var24 * var28;
            double var32 = var6 == 0.0 ? 1.0 : 0.7 + 0.3 * Math.sin(var26 * var6 - var10 * var8 + var13 * 0.9);
            this.A(var14, var1, var21, var30, 0.0, 2.0 * var24 / var3 * var28, this.L(var22), var4 * var19 * var32 * this.b.E(1.0 - var22), var2);
         }
      }
   }

   private qd.x E(s9.g var1, double var2, boolean var4) {
      int var5 = var1.Q++;
      if (var1.F[var5] == null) {
         var1.F[var5] = new qd.x(382);
      }

      qd.x var6 = var1.F[var5];
      var6.T();
      var6.C = 4096.0;
      var1.z[var5] = var2;
      var1.X[var5] = var4;
      return var6;
   }

   private void A(qd.x var1, s9.g var2, int var3, double var4, double var6, double var8, int var10, double var11, class_243 var13) {
      class_243 var14 = var2.h[var3];
      class_243 var15 = var2.J[var3];
      class_243 var16 = new class_243(var15.field_1352 + var14.field_1352 * var6, var15.field_1351 + var4, var15.field_1350 + var14.field_1350 * var6);
      var1.x(var16, Math.max(0.0, var8), ei.R(var10, Math.clamp(var11, 0.0, 1.0) * n(var16, var13)));
   }

   private void h(s9.g var1, class_243 var2, int var3, double var4, class_243 var6) {
      double var7 = n(var2, var6);
      if (!(var7 <= 0.0) && var1.A < var1.E.length) {
         int var9 = var1.A++;
         var1.E[var9] = var2;
         var1.U[var9] = ei.R(var3, var7);
         var1.S[var9] = var4;
      }
   }

   static double n(class_243 var0, class_243 var1) {
      return u9.L((var0.method_1022(var1) - 0.6) / 1.4);
   }

   private int L(double var1) {
      return ei.W(this.l, this.k, var1);
   }

   private double D(double var1) {
      return switch (this.g) {
         case J -> 1.0;
         case p -> var1 * var1;
         case w -> Math.sin(Math.PI * var1);
         case P -> Math.pow(var1, 3.0) * 1.4;
      };
   }

   private static boolean F(qd.x var0, class_243 var1) {
      for (int var2 = 1; var2 < var0.w; var2++) {
         if (var0.Q[var2 - 1].method_1025(var1) <= var0.C
            && var0.Q[var2 - 1].method_1025(var0.Q[var2]) >= 1.0E-10
            && (var0.g[var2 - 1] > 0.0 || var0.g[var2] > 0.0)
            && (var0.M[var2 - 1] >>> 24 > 0 || var0.M[var2] >>> 24 > 0)) {
            return true;
         }
      }

      return false;
   }

   qd.x N(int var1, int var2) {
      s9.g var3 = this.x.get(var1);
      return var3 != null && var2 < var3.Q ? var3.F[var2] : null;
   }

   int a(int var1) {
      s9.g var2 = this.x.get(var1);
      return var2 == null ? 0 : var2.Q;
   }

   private static final class g {
      final class_243[] m = new class_243[128];
      final double[] M = new double[128];
      final double[] D = new double[128];
      final double[] y = new double[128];
      final long[] j = new long[128];
      int O;
      int k;
      double i;
      final class_243[] J = new class_243[382];
      final class_243[] h = new class_243[382];
      final double[] R = new double[382];
      final double[] a = new double[382];
      final double[] G = new double[382];
      final double[] p = new double[382];
      int W;
      final qd.p f = new qd.p();
      final qd.x[] F = new qd.x[8];
      final double[] z = new double[8];
      final boolean[] X = new boolean[8];
      final boolean[] s = new boolean[8];
      int Q;
      final class_243[] E = new class_243[2];
      final int[] U = new int[2];
      final double[] S = new double[2];
      int A;
      boolean L;
   }

   public enum s {
      T,
      h,
      e,
      V,
      Z;
   }
}
