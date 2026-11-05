package com.larpclient;

import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.world.phys.Vec3;

public final class _2 {
   static final int D = 128;
   static final int H = 16;
   static final int W = 8;
   static final int e = 3;
   static final int q = 382;
   private final Map<Integer, _2.l> Q = new LinkedHashMap<>();
   private long u;
   private boolean N;
   private pf.x I = pf.x.E;
   public _2.o T = _2.o.Q;
   public uz.i K = uz.i.I;
   public hp.y f = hp.y.B;
   public double L = 1.2;
   public double S = 0.12;
   public boolean J = (boolean)1;
   public boolean l = (boolean)1;
   public int o = com.larpclient.f.C;
   public int P = com.larpclient.f.O;

   public _2() {
      mt.K("Trails", "Body trail engine initialized: {} sources, {} points per source", 16, 128);
   }

   public boolean l() {
      return this.Q.isEmpty();
   }

   public int L() {
      return this.Q.size();
   }

   public int k() {
      return this.Q.values().stream().mapToInt(var0 -> var0.w).sum();
   }

   public boolean E() {
      return this.N;
   }

   public void o() {
      this.Q.clear();
      this.u = 0L;
      this.N = (boolean)0;
   }

   public void q() {
      this.u++;
      this.Q.values().removeIf(var1 -> {
         while (var1.w > 0 && this.u - var1.C[var1.h] > this.L * 20.0) {
            var1.h = (var1.h + 1) % 128;
            var1.w--;
         }

         return var1.w == 0;
      });
   }

   public void H(int var1, Vec3 var2, double var3, double var5) {
      if (Double.isFinite(var2.x) && Double.isFinite(var2.y) && Double.isFinite(var2.z) && Double.isFinite(var3) && Double.isFinite(var5)) {
         var3 = Math.clamp(var3, 0.1, 6.0);
         var5 = Math.clamp(var5, 0.1, 4.0);
         _2.l var7 = this.Q.get(var1);
         if (var7 == null) {
            if (this.Q.size() >= 16) {
               return;
            }

            var7 = new _2.l();
            this.Q.put(var1, var7);
         }

         double var8 = var7.W;
         if (var7.w > 0) {
            int var10 = (var7.h + var7.w - 1) % 128;
            double var11 = var7.M[var10].distanceToSqr(var2);
            if (var11 > 64.0) {
               var7.h = var7.w = 0;
            } else {
               if (var11 < 4.0E-4 && Math.abs(var7.F[var10] - var3) < 0.02) {
                  return;
               }

               var8 += Math.sqrt(var11);
            }
         }

         if (var7.w == 128) {
            var7.h = (var7.h + 1) % 128;
            var7.w--;
         }

         int var15 = (var7.h + var7.w++) % 128;
         var7.M[var15] = var2;
         var7.F[var15] = var3;
         var7.z[var15] = var5;
         var7.p[var15] = var7.W = var8;
         var7.C[var15] = this.u;
      }
   }

   public void U(Vec3 var1, float var2) {
      this.N = (boolean)0;
      this.I = this.T == _2.o.E ? pf.x.S : pf.x.E;
      double var3 = ty.K();

      for (_2.l var6 : this.Q.values()) {
         var6.t = (boolean)0;
         var6.R = var6.q = 0;
         this.s(var6, var2);
         if (var6.x >= 2) {
            switch (this.T) {
               case E:
                  this.I(var6, var1, var3);
                  break;
               case Q:
                  this.S(var6, var1, var3);
                  break;
               case Z:
                  this.P(var6, var1, var3);
                  break;
               case X:
                  this.E(var6, var1, var3);
                  break;
               case q:
                  this.Z(var6, var1, var3);
            }

            for (int var7 = 0; var7 < var6.R; var7++) {
               __.l var8 = var6.c[var7];
               if (this.K != uz.i.X) {
                  var8.F();
               }

               var8.a(var1);
               var6.u[var7] = m(var8, var1);
               var6.t = var6.t | var6.u[var7];
            }

            if (this.J && var6.t) {
               this.N = (boolean)1;
            }
         }
      }
   }

   public void K(ki var1, boolean var2) {
      if (!var2 || this.J) {
         long var3 = kx.G();
         var1.blend(this.I);

         for (_2.l var6 : this.Q.values()) {
            if (var6.t) {
               for (int var7 = 0; var7 < var6.R; var7++) {
                  if (var6.u[var7]) {
                     if (var6.l[var7] >= 0.0) {
                        var1.f(var6.c[var7], var6.l[var7]);
                     } else {
                        var1.M(var6.c[var7], var6.n[var7]);
                     }
                  }
               }
            }
         }

         var1.blend(pf.x.E);

         for (_2.l var13 : this.Q.values()) {
            if (var13.t) {
               for (int var14 = 0; var14 < var13.q; var14++) {
                  Vec3 var8 = var13.k[var14];
                  int var9 = var13.K[var14];
                  double var10 = var13.d[var14];
                  var1.W(var8.x, var8.y, var8.z, var10, 0.0, _3.U(var9, 0.32), cg.u.a, 0.0, 0.0, 0.0);
                  var1.W(var8.x, var8.y, var8.z, var10 * 0.32, 0.0, _3.D(var9, var9 & 0xFF000000 | 16777215, 0.7), cg.u.a, 0.0, 0.0, 0.0);
               }
            }
         }

         var1.blend(pf.x.S);
         kx.u("Body Trails", var3);
      }
   }

   private void s(_2.l var1, float var2) {
      var1.x = 0;
      if (var1.w != 0) {
         int var3 = this.l ? 3 : 1;

         for (int var4 = 1; var4 < var1.w; var4++) {
            int var5 = (var1.h + var4 - 1) % 128;
            int var6 = (var1.h + var4) % 128;
            Vec3 var7 = var1.M[var5];
            Vec3 var8 = var1.M[var6];
            double var9 = var1.F[var6];
            double var11 = var1.z[var6];
            double var13 = var1.p[var6];
            if (var4 == var1.w - 1 && var1.C[var6] == this.u) {
               var8 = var7.lerp(var8, var2);
               var9 = var1.F[var5] + (var9 - var1.F[var5]) * var2;
               var11 = var1.z[var5] + (var11 - var1.z[var5]) * var2;
               var13 = var1.p[var5] + (var13 - var1.p[var5]) * var2;
            }

            double var15 = this.G(var1.C[var5], var2);
            double var17 = this.G(var1.C[var6], var2);
            if (var4 == 1) {
               this.Q(var1, var7, var1.F[var5], var1.z[var5], var15, var1.p[var5]);
            }

            if (var3 > 1) {
               Vec3 var19 = var4 > 1 ? var1.M[(var1.h + var4 - 2) % 128] : var7.add(var7.subtract(var8));
               Vec3 var20 = var4 < var1.w - 1 ? var1.M[(var1.h + var4 + 1) % 128] : var8.add(var8.subtract(var7));
               var1.A.l(var19, var7, var8, var20);
            }

            for (int var27 = 1; var27 <= var3; var27++) {
               double var28 = (double)var27 / var3;
               this.Q(
                  var1,
                  var3 == 1 ? var8 : var1.A.p(var28),
                  var1.F[var5] + (var9 - var1.F[var5]) * var28,
                  var1.z[var5] + (var11 - var1.z[var5]) * var28,
                  var15 + (var17 - var15) * var28,
                  var1.p[var5] + (var13 - var1.p[var5]) * var28
               );
            }
         }

         Vec3 var22 = null;

         for (int var23 = 0; var23 < var1.x; var23++) {
            Vec3 var24 = var1.S[Math.min(var23 + 1, var1.x - 1)].subtract(var1.S[Math.max(var23 - 1, 0)]);
            double var25 = Math.sqrt(var24.x * var24.x + var24.z * var24.z);
            Vec3 var26 = var25 < 1.0E-6 ? (var22 != null ? var22 : new Vec3(1.0, 0.0, 0.0)) : new Vec3(-var24.z / var25, 0.0, var24.x / var25);
            if (var22 != null && var26.dot(var22) < 0.0) {
               var26 = var26.scale(-1.0);
            }

            var22 = var26;
            var1.L[var23] = var26;
         }
      }
   }

   private void Q(_2.l var1, Vec3 var2, double var3, double var5, double var7, double var9) {
      int var11 = var1.x++;
      var1.S[var11] = var2;
      var1.D[var11] = var3;
      var1.O[var11] = var5;
      var1.a[var11] = var7;
      var1.B[var11] = var9;
   }

   private double G(long var1, float var3) {
      return Math.clamp(1.0 - ((float)this.u + var3 - (float)var1) / (this.L * 20.0), 0.0, 1.0);
   }

   private void I(_2.l var1, Vec3 var2, double var3) {
      this.b(var1, var2, 4, 0.1, 0.0, 0.0, var3, false);
      double[] var5 = new double[]{0.16, 0.52, 0.88};

      for (int var6 = 0; var6 < var5.length; var6++) {
         __.l var7 = this.t(var1, 0.55, false);

         for (int var8 = 0; var8 < var1.x; var8++) {
            double var9 = var1.a[var8];
            double var11 = var1.D[var8];
            double var13 = var1.B[var8];
            double var15 = 1.0 - var9;
            double var17 = var11 * var5[var6] + Math.sin(var13 * 2.4 + var6 * 2.1 - var3 * 1.6) * 0.09 * var11 * var15;
            double var19 = Math.cos(var13 * 1.8 + var6 * 1.7 - var3 * 1.2) * 0.16 * var15;
            int var21 = _3.D(this.m(var9), _3.D(this.o, this.P, 0.5), var6 * 0.18);
            this.P(var7, var1, var8, var17, var19, this.S * 1.15 * this.H(var9), var21, 0.9 * this.f.Z(1.0 - var9), var2);
         }
      }
   }

   private void S(_2.l var1, Vec3 var2, double var3) {
      this.b(var1, var2, 6, 0.24, 3.2, 5.0, var3, true);

      for (int var5 = 0; var5 < 2; var5++) {
         __.l var6 = this.t(var1, -1.0, true);

         for (int var7 = 0; var7 < var1.x; var7++) {
            double var8 = var1.a[var7];
            double var10 = var1.D[var7];
            double var12 = Math.min(1.0, this.H(var8));
            double var14 = var5 == 0 ? var10 * 0.97 : var10 * 0.04;
            double var16 = var10 * 0.5;
            double var18 = var16 + (var14 - var16) * (0.25 + 0.75 * var12);
            this.P(var6, var1, var7, var18, 0.0, this.S * 0.55 * this.H(var8), this.m(var8), this.f.Z(1.0 - var8), var2);
         }
      }
   }

   private void P(_2.l var1, Vec3 var2, double var3) {
      double[] var5 = new double[]{-1.0, 0.0, 1.0};

      for (double var9 : var5) {
         __.l var11 = this.t(var1, 0.0, false);

         for (int var12 = 0; var12 < var1.x; var12++) {
            double var13 = var1.a[var12];
            double var15 = var1.D[var12];
            double var17 = var13 * var13;
            this.P(var11, var1, var12, var15 * (0.55 + var9 * 0.32 * var17), 0.0, var15 * 0.42 * var17, this.m(var13), 0.13 * this.f.Z(1.0 - var13), var2);
         }
      }

      for (int var19 = 0; var19 < 2; var19++) {
         __.l var21 = this.t(var1, -1.0, true);

         for (int var24 = 0; var24 < var1.x; var24++) {
            double var26 = var1.a[var24];
            double var27 = var1.D[var24];
            double var28 = var1.B[var24];
            double var29 = 1.0 - var26;
            double var30 = var28 * 3.0 + var19 * Math.PI - var3 * 3.0;
            this.P(
               var21,
               var1,
               var24,
               var27 * 0.55 + Math.cos(var30) * 0.22 * var27 * var29,
               Math.sin(var30) * 0.3 * var29,
               this.S * 0.4 * this.H(var26),
               _3.D(this.m(var26), this.P, 0.3),
               0.8 * this.f.Z(1.0 - var26),
               var2
            );
         }
      }

      __.l var20 = this.t(var1, -1.0, true);

      for (int var22 = 0; var22 < var1.x; var22++) {
         double var25 = var1.a[var22];
         this.P(var20, var1, var22, var1.D[var22] * 0.55, 0.0, this.S * 2.2 * this.H(var25), this.m(var25), (0.4 + 0.6 * var25) * this.f.Z(1.0 - var25), var2);
      }

      int var23 = var1.x - 1;
      this.n(var1, var1.S[var23].add(0.0, var1.D[var23] * 0.55, 0.0), this.o, var1.D[var23] * 0.3, var2);
   }

   private void E(_2.l var1, Vec3 var2, double var3) {
      __.l var5 = this.t(var1, 0.0, false);

      for (int var6 = 0; var6 < var1.x; var6++) {
         double var7 = var1.a[var6];
         this.P(
            var5, var1, var6, var1.D[var6] * 0.5, 0.0, var1.D[var6] * 0.3 * Math.min(1.0, this.H(var7) + 0.2), this.m(var7), 0.12 * this.f.Z(1.0 - var7), var2
         );
      }

      for (int var25 = 0; var25 < 2; var25++) {
         __.l var26 = this.t(var1, -1.0, true);
         Vec3 var8 = null;

         for (int var9 = 0; var9 < var1.x; var9++) {
            double var10 = var1.a[var9];
            double var12 = var1.D[var9];
            double var14 = var1.B[var9];
            double var16 = 0.6 + 0.4 * var10;
            double var18 = var14 * 2.4 + var25 * Math.PI - var3 * 2.4;
            double var20 = 0.46 * var12 * var16;
            double var22 = Math.max(var1.O[var9] * 0.8, 0.45) * var16;
            int var24 = var25 == 0 ? this.m(var10) : _3.D(this.o, this.P, var10);
            this.P(
               var26,
               var1,
               var9,
               var12 * 0.5 + Math.cos(var18) * var20,
               Math.sin(var18) * var22,
               this.S * 0.75 * this.H(var10),
               var24,
               this.f.Z(1.0 - var10),
               var2
            );
            var8 = var26.v[var26.k - 1];
         }

         if (var8 != null) {
            this.n(var1, var8, var25 == 0 ? this.o : this.P, 0.16, var2);
         }
      }
   }

   private void Z(_2.l var1, Vec3 var2, double var3) {
      this.b(var1, var2, 4, 0.07, 7.0, 3.0, var3, true);
   }

   private void b(_2.l var1, Vec3 var2, int var3, double var4, double var6, double var8, double var10, boolean var12) {
      for (int var13 = 0; var13 < var3; var13++) {
         __.l var14 = this.t(var1, 0.0, false);
         double var15 = (var13 + 0.5) / var3;
         double var17 = Math.abs(var15 * 2.0 - 1.0);
         double var19 = var12 ? 0.55 + 0.45 * var17 * var17 : 1.0;

         for (int var21 = 0; var21 < var1.x; var21++) {
            double var22 = var1.a[var21];
            double var24 = var1.D[var21];
            double var26 = var1.B[var21];
            double var28 = 0.3 + 0.7 * Math.min(1.0, this.H(var22));
            double var30 = var24 * 0.5 + (var15 - 0.5) * var24 * var28;
            double var32 = var6 == 0.0 ? 1.0 : 0.7 + 0.3 * Math.sin(var26 * var6 - var10 * var8 + var13 * 0.9);
            this.P(var14, var1, var21, var30, 0.0, 2.0 * var24 / var3 * var28, this.m(var22), var4 * var19 * var32 * this.f.Z(1.0 - var22), var2);
         }
      }
   }

   private __.l t(_2.l var1, double var2, boolean var4) {
      int var5 = var1.R++;
      if (var1.c[var5] == null) {
         var1.c[var5] = new __.l(382);
      }

      __.l var6 = var1.c[var5];
      var6.y();
      var6.t = 4096.0;
      var1.l[var5] = var2;
      var1.n[var5] = var4;
      return var6;
   }

   private void P(__.l var1, _2.l var2, int var3, double var4, double var6, double var8, int var10, double var11, Vec3 var13) {
      Vec3 var14 = var2.L[var3];
      Vec3 var15 = var2.S[var3];
      Vec3 var16 = new Vec3(var15.x + var14.x * var6, var15.y + var4, var15.z + var14.z * var6);
      var1.y(var16, Math.max(0.0, var8), _3.U(var10, Math.clamp(var11, 0.0, 1.0) * i(var16, var13)));
   }

   private void n(_2.l var1, Vec3 var2, int var3, double var4, Vec3 var6) {
      double var7 = i(var2, var6);
      if (!(var7 <= 0.0) && var1.q < var1.k.length) {
         int var9 = var1.q++;
         var1.k[var9] = var2;
         var1.K[var9] = _3.U(var3, var7);
         var1.d[var9] = var4;
      }
   }

   static double i(Vec3 var0, Vec3 var1) {
      return hp.R((var0.distanceTo(var1) - 0.6) / 1.4);
   }

   private int m(double var1) {
      return _3.D(this.P, this.o, var1);
   }

   private double H(double var1) {
      return switch (this.K) {
         case X -> 1.0;
         case I -> var1 * var1;
         case f -> Math.sin(Math.PI * var1);
         case H -> Math.pow(var1, 3.0) * 1.4;
      };
   }

   private static boolean m(__.l var0, Vec3 var1) {
      for (int var2 = 1; var2 < var0.k; var2++) {
         if (var0.v[var2 - 1].distanceToSqr(var1) <= var0.t
            && var0.v[var2 - 1].distanceToSqr(var0.v[var2]) >= 1.0E-10
            && (var0.O[var2 - 1] > 0.0 || var0.O[var2] > 0.0)
            && (var0.w[var2 - 1] >>> 24 > 0 || var0.w[var2] >>> 24 > 0)) {
            return true;
         }
      }

      return false;
   }

   __.l u(int var1, int var2) {
      _2.l var3 = this.Q.get(var1);
      return var3 != null && var2 < var3.R ? var3.c[var2] : null;
   }

   int e(int var1) {
      _2.l var2 = this.Q.get(var1);
      return var2 == null ? 0 : var2.R;
   }

   private static final class l {
      final Vec3[] M = new Vec3[128];
      final double[] F = new double[128];
      final double[] z = new double[128];
      final double[] p = new double[128];
      final long[] C = new long[128];
      int h;
      int w;
      double W;
      final Vec3[] S = new Vec3[382];
      final Vec3[] L = new Vec3[382];
      final double[] D = new double[382];
      final double[] O = new double[382];
      final double[] a = new double[382];
      final double[] B = new double[382];
      int x;
      final __.u A = new __.u();
      final __.l[] c = new __.l[8];
      final double[] l = new double[8];
      final boolean[] n = new boolean[8];
      final boolean[] u = new boolean[8];
      int R;
      final Vec3[] k = new Vec3[2];
      final int[] K = new int[2];
      final double[] d = new double[2];
      int q;
      boolean t;
   }

   public enum o {
      E,
      Q,
      Z,
      X,
      q;
   }
}
