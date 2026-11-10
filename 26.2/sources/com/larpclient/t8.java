package com.larpclient;

import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.world.phys.Vec3;

public final class t8 {
   static final int D = 128;
   static final int M = 16;
   static final int S = 8;
   static final int g = 3;
   static final int G = 382;
   private final Map<Integer, t8.e> K = new LinkedHashMap<>();
   private long L;
   private boolean m;
   private gl.c R = gl.c.E;
   public t8.y W = t8.y.t;
   public ub.f i = ub.f.O;
   public st.z c = st.z.f;
   public double p = 1.2;
   public double w = 0.12;
   public boolean b = (boolean)1;
   public boolean z = (boolean)1;
   public int l = ho.J;
   public int X = ho.ds;

   public t8() {
      h.A("Trails", "Body trail engine initialized: {} sources, {} points per source", 16, 128);
   }

   public boolean D() {
      return this.K.isEmpty();
   }

   public int Q() {
      return this.K.size();
   }

   public int i() {
      return this.K.values().stream().mapToInt(var0 -> var0.J).sum();
   }

   public boolean u() {
      return this.m;
   }

   public void k() {
      this.K.clear();
      this.L = 0L;
      this.m = (boolean)0;
   }

   public void G() {
      this.L++;
      this.K.values().removeIf(var1 -> {
         while (var1.J > 0 && this.L - var1.U[var1.L] > this.p * 20.0) {
            var1.L = (var1.L + 1) % 128;
            var1.J--;
         }

         return var1.J == 0;
      });
   }

   public void U(int var1, Vec3 var2, double var3, double var5) {
      if (Double.isFinite(var2.x) && Double.isFinite(var2.y) && Double.isFinite(var2.z) && Double.isFinite(var3) && Double.isFinite(var5)) {
         var3 = Math.clamp(var3, 0.1, 6.0);
         var5 = Math.clamp(var5, 0.1, 4.0);
         t8.e var7 = this.K.get(var1);
         if (var7 == null) {
            if (this.K.size() >= 16) {
               return;
            }

            var7 = new t8.e();
            this.K.put(var1, var7);
         }

         double var8 = var7.B;
         if (var7.J > 0) {
            int var10 = (var7.L + var7.J - 1) % 128;
            double var11 = var7.p[var10].distanceToSqr(var2);
            if (var11 > 64.0) {
               var7.L = var7.J = 0;
            } else {
               if (var11 < 4.0E-4 && Math.abs(var7.y[var10] - var3) < 0.02) {
                  return;
               }

               var8 += Math.sqrt(var11);
            }
         }

         if (var7.J == 128) {
            var7.L = (var7.L + 1) % 128;
            var7.J--;
         }

         int var15 = (var7.L + var7.J++) % 128;
         var7.p[var15] = var2;
         var7.y[var15] = var3;
         var7.o[var15] = var5;
         var7.I[var15] = var7.B = var8;
         var7.U[var15] = this.L;
      }
   }

   public void O(Vec3 var1, float var2) {
      this.m = (boolean)0;
      this.R = this.W == t8.y.B ? gl.c.K : gl.c.E;
      double var3 = cf.F();

      for (t8.e var6 : this.K.values()) {
         var6.S = (boolean)0;
         var6.M = var6.e = 0;
         this.n(var6, var2);
         if (var6.z >= 2) {
            switch (this.W) {
               case B:
                  this.f(var6, var1, var3);
                  break;
               case t:
                  this.A(var6, var1, var3);
                  break;
               case E:
                  this.L(var6, var1, var3);
                  break;
               case F:
                  this.D(var6, var1, var3);
                  break;
               case I:
                  this.w(var6, var1, var3);
            }

            for (int var7 = 0; var7 < var6.M; var7++) {
               u_.p var8 = var6.s[var7];
               if (this.i != ub.f.S) {
                  var8.O();
               }

               var8.x(var1);
               var6.w[var7] = S(var8, var1);
               var6.S = var6.S | var6.w[var7];
            }

            if (this.b && var6.S) {
               this.m = (boolean)1;
            }
         }
      }
   }

   public void B(kj var1, boolean var2) {
      if (!var2 || this.b) {
         long var3 = zj.m();
         var1.blend(this.R);

         for (t8.e var6 : this.K.values()) {
            if (var6.S) {
               for (int var7 = 0; var7 < var6.M; var7++) {
                  if (var6.w[var7]) {
                     if (var6.b[var7] >= 0.0) {
                        var1.s(var6.s[var7], var6.b[var7]);
                     } else {
                        var1.b(var6.s[var7], var6.Y[var7]);
                     }
                  }
               }
            }
         }

         var1.blend(gl.c.E);

         for (t8.e var13 : this.K.values()) {
            if (var13.S) {
               for (int var14 = 0; var14 < var13.e; var14++) {
                  Vec3 var8 = var13.n[var14];
                  int var9 = var13.t[var14];
                  double var10 = var13.E[var14];
                  var1.I(var8.x, var8.y, var8.z, var10, 0.0, uh.F(var9, 0.32), c3.a.Z, 0.0, 0.0, 0.0);
                  var1.I(var8.x, var8.y, var8.z, var10 * 0.32, 0.0, uh.q(var9, var9 & 0xFF000000 | 16777215, 0.7), c3.a.Z, 0.0, 0.0, 0.0);
               }
            }
         }

         var1.blend(gl.c.K);
         zj.l("Body Trails", var3);
      }
   }

   private void n(t8.e var1, float var2) {
      var1.z = 0;
      if (var1.J != 0) {
         int var3 = this.z ? 3 : 1;

         for (int var4 = 1; var4 < var1.J; var4++) {
            int var5 = (var1.L + var4 - 1) % 128;
            int var6 = (var1.L + var4) % 128;
            Vec3 var7 = var1.p[var5];
            Vec3 var8 = var1.p[var6];
            double var9 = var1.y[var6];
            double var11 = var1.o[var6];
            double var13 = var1.I[var6];
            if (var4 == var1.J - 1 && var1.U[var6] == this.L) {
               var8 = var7.lerp(var8, var2);
               var9 = var1.y[var5] + (var9 - var1.y[var5]) * var2;
               var11 = var1.o[var5] + (var11 - var1.o[var5]) * var2;
               var13 = var1.I[var5] + (var13 - var1.I[var5]) * var2;
            }

            double var15 = this.V(var1.U[var5], var2);
            double var17 = this.V(var1.U[var6], var2);
            if (var4 == 1) {
               this.w(var1, var7, var1.y[var5], var1.o[var5], var15, var1.I[var5]);
            }

            if (var3 > 1) {
               Vec3 var19 = var4 > 1 ? var1.p[(var1.L + var4 - 2) % 128] : var7.add(var7.subtract(var8));
               Vec3 var20 = var4 < var1.J - 1 ? var1.p[(var1.L + var4 + 1) % 128] : var8.add(var8.subtract(var7));
               var1.i.U(var19, var7, var8, var20);
            }

            for (int var27 = 1; var27 <= var3; var27++) {
               double var28 = (double)var27 / var3;
               this.w(
                  var1,
                  var3 == 1 ? var8 : var1.i.L(var28),
                  var1.y[var5] + (var9 - var1.y[var5]) * var28,
                  var1.o[var5] + (var11 - var1.o[var5]) * var28,
                  var15 + (var17 - var15) * var28,
                  var1.I[var5] + (var13 - var1.I[var5]) * var28
               );
            }
         }

         Vec3 var22 = null;

         for (int var23 = 0; var23 < var1.z; var23++) {
            Vec3 var24 = var1.X[Math.min(var23 + 1, var1.z - 1)].subtract(var1.X[Math.max(var23 - 1, 0)]);
            double var25 = Math.sqrt(var24.x * var24.x + var24.z * var24.z);
            Vec3 var26 = var25 < 1.0E-6 ? (var22 != null ? var22 : new Vec3(1.0, 0.0, 0.0)) : new Vec3(-var24.z / var25, 0.0, var24.x / var25);
            if (var22 != null && var26.dot(var22) < 0.0) {
               var26 = var26.scale(-1.0);
            }

            var22 = var26;
            var1.u[var23] = var26;
         }
      }
   }

   private void w(t8.e var1, Vec3 var2, double var3, double var5, double var7, double var9) {
      int var11 = var1.z++;
      var1.X[var11] = var2;
      var1.h[var11] = var3;
      var1.k[var11] = var5;
      var1.j[var11] = var7;
      var1.Z[var11] = var9;
   }

   private double V(long var1, float var3) {
      return Math.clamp(1.0 - ((float)this.L + var3 - (float)var1) / (this.p * 20.0), 0.0, 1.0);
   }

   private void f(t8.e var1, Vec3 var2, double var3) {
      this.H(var1, var2, 4, 0.1, 0.0, 0.0, var3, false);
      double[] var5 = new double[]{0.16, 0.52, 0.88};

      for (int var6 = 0; var6 < var5.length; var6++) {
         u_.p var7 = this.Y(var1, 0.55, false);

         for (int var8 = 0; var8 < var1.z; var8++) {
            double var9 = var1.j[var8];
            double var11 = var1.h[var8];
            double var13 = var1.Z[var8];
            double var15 = 1.0 - var9;
            double var17 = var11 * var5[var6] + Math.sin(var13 * 2.4 + var6 * 2.1 - var3 * 1.6) * 0.09 * var11 * var15;
            double var19 = Math.cos(var13 * 1.8 + var6 * 1.7 - var3 * 1.2) * 0.16 * var15;
            int var21 = uh.q(this.V(var9), uh.q(this.l, this.X, 0.5), var6 * 0.18);
            this.T(var7, var1, var8, var17, var19, this.w * 1.15 * this.g(var9), var21, 0.9 * this.c.x(1.0 - var9), var2);
         }
      }
   }

   private void A(t8.e var1, Vec3 var2, double var3) {
      this.H(var1, var2, 6, 0.24, 3.2, 5.0, var3, true);

      for (int var5 = 0; var5 < 2; var5++) {
         u_.p var6 = this.Y(var1, -1.0, true);

         for (int var7 = 0; var7 < var1.z; var7++) {
            double var8 = var1.j[var7];
            double var10 = var1.h[var7];
            double var12 = Math.min(1.0, this.g(var8));
            double var14 = var5 == 0 ? var10 * 0.97 : var10 * 0.04;
            double var16 = var10 * 0.5;
            double var18 = var16 + (var14 - var16) * (0.25 + 0.75 * var12);
            this.T(var6, var1, var7, var18, 0.0, this.w * 0.55 * this.g(var8), this.V(var8), this.c.x(1.0 - var8), var2);
         }
      }
   }

   private void L(t8.e var1, Vec3 var2, double var3) {
      double[] var5 = new double[]{-1.0, 0.0, 1.0};

      for (double var9 : var5) {
         u_.p var11 = this.Y(var1, 0.0, false);

         for (int var12 = 0; var12 < var1.z; var12++) {
            double var13 = var1.j[var12];
            double var15 = var1.h[var12];
            double var17 = var13 * var13;
            this.T(var11, var1, var12, var15 * (0.55 + var9 * 0.32 * var17), 0.0, var15 * 0.42 * var17, this.V(var13), 0.13 * this.c.x(1.0 - var13), var2);
         }
      }

      for (int var19 = 0; var19 < 2; var19++) {
         u_.p var21 = this.Y(var1, -1.0, true);

         for (int var24 = 0; var24 < var1.z; var24++) {
            double var26 = var1.j[var24];
            double var27 = var1.h[var24];
            double var28 = var1.Z[var24];
            double var29 = 1.0 - var26;
            double var30 = var28 * 3.0 + var19 * Math.PI - var3 * 3.0;
            this.T(
               var21,
               var1,
               var24,
               var27 * 0.55 + Math.cos(var30) * 0.22 * var27 * var29,
               Math.sin(var30) * 0.3 * var29,
               this.w * 0.4 * this.g(var26),
               uh.q(this.V(var26), this.X, 0.3),
               0.8 * this.c.x(1.0 - var26),
               var2
            );
         }
      }

      u_.p var20 = this.Y(var1, -1.0, true);

      for (int var22 = 0; var22 < var1.z; var22++) {
         double var25 = var1.j[var22];
         this.T(var20, var1, var22, var1.h[var22] * 0.55, 0.0, this.w * 2.2 * this.g(var25), this.V(var25), (0.4 + 0.6 * var25) * this.c.x(1.0 - var25), var2);
      }

      int var23 = var1.z - 1;
      this.c(var1, var1.X[var23].add(0.0, var1.h[var23] * 0.55, 0.0), this.l, var1.h[var23] * 0.3, var2);
   }

   private void D(t8.e var1, Vec3 var2, double var3) {
      u_.p var5 = this.Y(var1, 0.0, false);

      for (int var6 = 0; var6 < var1.z; var6++) {
         double var7 = var1.j[var6];
         this.T(
            var5, var1, var6, var1.h[var6] * 0.5, 0.0, var1.h[var6] * 0.3 * Math.min(1.0, this.g(var7) + 0.2), this.V(var7), 0.12 * this.c.x(1.0 - var7), var2
         );
      }

      for (int var25 = 0; var25 < 2; var25++) {
         u_.p var26 = this.Y(var1, -1.0, true);
         Vec3 var8 = null;

         for (int var9 = 0; var9 < var1.z; var9++) {
            double var10 = var1.j[var9];
            double var12 = var1.h[var9];
            double var14 = var1.Z[var9];
            double var16 = 0.6 + 0.4 * var10;
            double var18 = var14 * 2.4 + var25 * Math.PI - var3 * 2.4;
            double var20 = 0.46 * var12 * var16;
            double var22 = Math.max(var1.k[var9] * 0.8, 0.45) * var16;
            int var24 = var25 == 0 ? this.V(var10) : uh.q(this.l, this.X, var10);
            this.T(
               var26,
               var1,
               var9,
               var12 * 0.5 + Math.cos(var18) * var20,
               Math.sin(var18) * var22,
               this.w * 0.75 * this.g(var10),
               var24,
               this.c.x(1.0 - var10),
               var2
            );
            var8 = var26.M[var26.p - 1];
         }

         if (var8 != null) {
            this.c(var1, var8, var25 == 0 ? this.l : this.X, 0.16, var2);
         }
      }
   }

   private void w(t8.e var1, Vec3 var2, double var3) {
      this.H(var1, var2, 4, 0.07, 7.0, 3.0, var3, true);
   }

   private void H(t8.e var1, Vec3 var2, int var3, double var4, double var6, double var8, double var10, boolean var12) {
      for (int var13 = 0; var13 < var3; var13++) {
         u_.p var14 = this.Y(var1, 0.0, false);
         double var15 = (var13 + 0.5) / var3;
         double var17 = Math.abs(var15 * 2.0 - 1.0);
         double var19 = var12 ? 0.55 + 0.45 * var17 * var17 : 1.0;

         for (int var21 = 0; var21 < var1.z; var21++) {
            double var22 = var1.j[var21];
            double var24 = var1.h[var21];
            double var26 = var1.Z[var21];
            double var28 = 0.3 + 0.7 * Math.min(1.0, this.g(var22));
            double var30 = var24 * 0.5 + (var15 - 0.5) * var24 * var28;
            double var32 = var6 == 0.0 ? 1.0 : 0.7 + 0.3 * Math.sin(var26 * var6 - var10 * var8 + var13 * 0.9);
            this.T(var14, var1, var21, var30, 0.0, 2.0 * var24 / var3 * var28, this.V(var22), var4 * var19 * var32 * this.c.x(1.0 - var22), var2);
         }
      }
   }

   private u_.p Y(t8.e var1, double var2, boolean var4) {
      int var5 = var1.M++;
      if (var1.s[var5] == null) {
         var1.s[var5] = new u_.p(382);
      }

      u_.p var6 = var1.s[var5];
      var6.q();
      var6.C = 4096.0;
      var1.b[var5] = var2;
      var1.Y[var5] = var4;
      return var6;
   }

   private void T(u_.p var1, t8.e var2, int var3, double var4, double var6, double var8, int var10, double var11, Vec3 var13) {
      Vec3 var14 = var2.u[var3];
      Vec3 var15 = var2.X[var3];
      Vec3 var16 = new Vec3(var15.x + var14.x * var6, var15.y + var4, var15.z + var14.z * var6);
      var1.n(var16, Math.max(0.0, var8), uh.F(var10, Math.clamp(var11, 0.0, 1.0) * V(var16, var13)));
   }

   private void c(t8.e var1, Vec3 var2, int var3, double var4, Vec3 var6) {
      double var7 = V(var2, var6);
      if (!(var7 <= 0.0) && var1.e < var1.n.length) {
         int var9 = var1.e++;
         var1.n[var9] = var2;
         var1.t[var9] = uh.F(var3, var7);
         var1.E[var9] = var4;
      }
   }

   static double V(Vec3 var0, Vec3 var1) {
      return st.h((var0.distanceTo(var1) - 0.6) / 1.4);
   }

   private int V(double var1) {
      return uh.q(this.X, this.l, var1);
   }

   private double g(double var1) {
      return switch (this.i) {
         case S -> 1.0;
         case O -> var1 * var1;
         case M -> Math.sin(Math.PI * var1);
         case k -> Math.pow(var1, 3.0) * 1.4;
      };
   }

   private static boolean S(u_.p var0, Vec3 var1) {
      for (int var2 = 1; var2 < var0.p; var2++) {
         if (var0.M[var2 - 1].distanceToSqr(var1) <= var0.C
            && var0.M[var2 - 1].distanceToSqr(var0.M[var2]) >= 1.0E-10
            && (var0.A[var2 - 1] > 0.0 || var0.A[var2] > 0.0)
            && (var0.I[var2 - 1] >>> 24 > 0 || var0.I[var2] >>> 24 > 0)) {
            return true;
         }
      }

      return false;
   }

   u_.p h(int var1, int var2) {
      t8.e var3 = this.K.get(var1);
      return var3 != null && var2 < var3.M ? var3.s[var2] : null;
   }

   int z(int var1) {
      t8.e var2 = this.K.get(var1);
      return var2 == null ? 0 : var2.M;
   }

   private static final class e {
      final Vec3[] p = new Vec3[128];
      final double[] y = new double[128];
      final double[] o = new double[128];
      final double[] I = new double[128];
      final long[] U = new long[128];
      int L;
      int J;
      double B;
      final Vec3[] X = new Vec3[382];
      final Vec3[] u = new Vec3[382];
      final double[] h = new double[382];
      final double[] k = new double[382];
      final double[] j = new double[382];
      final double[] Z = new double[382];
      int z;
      final u_.u i = new u_.u();
      final u_.p[] s = new u_.p[8];
      final double[] b = new double[8];
      final boolean[] Y = new boolean[8];
      final boolean[] w = new boolean[8];
      int M;
      final Vec3[] n = new Vec3[2];
      final int[] t = new int[2];
      final double[] E = new double[2];
      int e;
      boolean S;
   }

   public enum y {
      B,
      t,
      E,
      F,
      I;
   }
}
