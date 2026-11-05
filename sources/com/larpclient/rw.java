package com.larpclient;

import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.world.phys.Vec3;

public final class rw {
   static final int T = 128;
   static final int m = 16;
   static final int f = 8;
   static final int Q = 3;
   static final int j = 382;
   private final Map<Integer, rw.i> w = new LinkedHashMap<>();
   private long v;
   private boolean B;
   private fk.h H = fk.h.u;
   public rw.l u = rw.l.e;
   public xg.c t = xg.c.W;
   public yc.d L = yc.d.u;
   public double x = 1.2;
   public double Y = 0.12;
   public boolean C = (boolean)1;
   public boolean h = (boolean)1;
   public int g = rv.e;
   public int V = rv.GQ;

   public rw() {
      r4.V("Trails", "Body trail engine initialized: {} sources, {} points per source", 16, 128);
   }

   public boolean K() {
      return this.w.isEmpty();
   }

   public int k() {
      return this.w.size();
   }

   public int H() {
      return this.w.values().stream().mapToInt(var0 -> var0.P).sum();
   }

   public boolean Q() {
      return this.B;
   }

   public void U() {
      this.w.clear();
      this.v = 0L;
      this.B = (boolean)0;
   }

   public void C() {
      this.v++;
      this.w.values().removeIf(var1 -> {
         while (var1.P > 0 && this.v - var1.y[var1.G] > this.x * 20.0) {
            var1.G = (var1.G + 1) % 128;
            var1.P--;
         }

         return var1.P == 0;
      });
   }

   public void e(int var1, Vec3 var2, double var3, double var5) {
      if (Double.isFinite(var2.x) && Double.isFinite(var2.y) && Double.isFinite(var2.z) && Double.isFinite(var3) && Double.isFinite(var5)) {
         var3 = Math.clamp(var3, 0.1, 6.0);
         var5 = Math.clamp(var5, 0.1, 4.0);
         rw.i var7 = this.w.get(var1);
         if (var7 == null) {
            if (this.w.size() >= 16) {
               return;
            }

            var7 = new rw.i();
            this.w.put(var1, var7);
         }

         double var8 = var7.M;
         if (var7.P > 0) {
            int var10 = (var7.G + var7.P - 1) % 128;
            double var11 = var7.Y[var10].distanceToSqr(var2);
            if (var11 > 64.0) {
               var7.G = var7.P = 0;
            } else {
               if (var11 < 4.0E-4 && Math.abs(var7.R[var10] - var3) < 0.02) {
                  return;
               }

               var8 += Math.sqrt(var11);
            }
         }

         if (var7.P == 128) {
            var7.G = (var7.G + 1) % 128;
            var7.P--;
         }

         int var15 = (var7.G + var7.P++) % 128;
         var7.Y[var15] = var2;
         var7.R[var15] = var3;
         var7.n[var15] = var5;
         var7.g[var15] = var7.M = var8;
         var7.y[var15] = this.v;
      }
   }

   public void K(Vec3 var1, float var2) {
      this.B = (boolean)0;
      this.H = this.u == rw.l.z ? fk.h.z : fk.h.u;
      double var3 = ea.y();

      for (rw.i var6 : this.w.values()) {
         var6.Q = (boolean)0;
         var6.p = var6.C = 0;
         this.G(var6, var2);
         if (var6.S >= 2) {
            switch (this.u) {
               case z:
                  this.i(var6, var1, var3);
                  break;
               case e:
                  this.d(var6, var1, var3);
                  break;
               case L:
                  this.T(var6, var1, var3);
                  break;
               case N:
                  this.X(var6, var1, var3);
                  break;
               case n:
                  this.t(var6, var1, var3);
            }

            for (int var7 = 0; var7 < var6.p; var7++) {
               h3.b var8 = var6.z[var7];
               if (this.t != xg.c.E) {
                  var8.W();
               }

               var8.n(var1);
               var6.k[var7] = N(var8, var1);
               var6.Q = var6.Q | var6.k[var7];
            }

            if (this.C && var6.Q) {
               this.B = (boolean)1;
            }
         }
      }
   }

   public void G(r6 var1, boolean var2) {
      if (!var2 || this.C) {
         long var3 = h_.U();
         var1.blend(this.H);

         for (rw.i var6 : this.w.values()) {
            if (var6.Q) {
               for (int var7 = 0; var7 < var6.p; var7++) {
                  if (var6.k[var7]) {
                     if (var6.x[var7] >= 0.0) {
                        var1.m(var6.z[var7], var6.x[var7]);
                     } else {
                        var1.y(var6.z[var7], var6.v[var7]);
                     }
                  }
               }
            }
         }

         var1.blend(fk.h.u);

         for (rw.i var13 : this.w.values()) {
            if (var13.Q) {
               for (int var14 = 0; var14 < var13.C; var14++) {
                  Vec3 var8 = var13.K[var14];
                  int var9 = var13.e[var14];
                  double var10 = var13.r[var14];
                  var1.j(var8.x, var8.y, var8.z, var10, 0.0, oh.S(var9, 0.32), f4.a.u, 0.0, 0.0, 0.0);
                  var1.j(var8.x, var8.y, var8.z, var10 * 0.32, 0.0, oh.j(var9, var9 & 0xFF000000 | 16777215, 0.7), f4.a.u, 0.0, 0.0, 0.0);
               }
            }
         }

         var1.blend(fk.h.z);
         h_.n("Body Trails", var3);
      }
   }

   private void G(rw.i var1, float var2) {
      var1.S = 0;
      if (var1.P != 0) {
         int var3 = this.h ? 3 : 1;

         for (int var4 = 1; var4 < var1.P; var4++) {
            int var5 = (var1.G + var4 - 1) % 128;
            int var6 = (var1.G + var4) % 128;
            Vec3 var7 = var1.Y[var5];
            Vec3 var8 = var1.Y[var6];
            double var9 = var1.R[var6];
            double var11 = var1.n[var6];
            double var13 = var1.g[var6];
            if (var4 == var1.P - 1 && var1.y[var6] == this.v) {
               var8 = var7.lerp(var8, var2);
               var9 = var1.R[var5] + (var9 - var1.R[var5]) * var2;
               var11 = var1.n[var5] + (var11 - var1.n[var5]) * var2;
               var13 = var1.g[var5] + (var13 - var1.g[var5]) * var2;
            }

            double var15 = this.t(var1.y[var5], var2);
            double var17 = this.t(var1.y[var6], var2);
            if (var4 == 1) {
               this.X(var1, var7, var1.R[var5], var1.n[var5], var15, var1.g[var5]);
            }

            if (var3 > 1) {
               Vec3 var19 = var4 > 1 ? var1.Y[(var1.G + var4 - 2) % 128] : var7.add(var7.subtract(var8));
               Vec3 var20 = var4 < var1.P - 1 ? var1.Y[(var1.G + var4 + 1) % 128] : var8.add(var8.subtract(var7));
               var1.L.n(var19, var7, var8, var20);
            }

            for (int var27 = 1; var27 <= var3; var27++) {
               double var28 = (double)var27 / var3;
               this.X(
                  var1,
                  var3 == 1 ? var8 : var1.L.o(var28),
                  var1.R[var5] + (var9 - var1.R[var5]) * var28,
                  var1.n[var5] + (var11 - var1.n[var5]) * var28,
                  var15 + (var17 - var15) * var28,
                  var1.g[var5] + (var13 - var1.g[var5]) * var28
               );
            }
         }

         Vec3 var22 = null;

         for (int var23 = 0; var23 < var1.S; var23++) {
            Vec3 var24 = var1.V[Math.min(var23 + 1, var1.S - 1)].subtract(var1.V[Math.max(var23 - 1, 0)]);
            double var25 = Math.sqrt(var24.x * var24.x + var24.z * var24.z);
            Vec3 var26 = var25 < 1.0E-6 ? (var22 != null ? var22 : new Vec3(1.0, 0.0, 0.0)) : new Vec3(-var24.z / var25, 0.0, var24.x / var25);
            if (var22 != null && var26.dot(var22) < 0.0) {
               var26 = var26.scale(-1.0);
            }

            var22 = var26;
            var1.m[var23] = var26;
         }
      }
   }

   private void X(rw.i var1, Vec3 var2, double var3, double var5, double var7, double var9) {
      int var11 = var1.S++;
      var1.V[var11] = var2;
      var1.l[var11] = var3;
      var1.w[var11] = var5;
      var1.o[var11] = var7;
      var1.d[var11] = var9;
   }

   private double t(long var1, float var3) {
      return Math.clamp(1.0 - ((float)this.v + var3 - (float)var1) / (this.x * 20.0), 0.0, 1.0);
   }

   private void i(rw.i var1, Vec3 var2, double var3) {
      this.t(var1, var2, 4, 0.1, 0.0, 0.0, var3, false);
      double[] var5 = new double[]{0.16, 0.52, 0.88};

      for (int var6 = 0; var6 < var5.length; var6++) {
         h3.b var7 = this.b(var1, 0.55, false);

         for (int var8 = 0; var8 < var1.S; var8++) {
            double var9 = var1.o[var8];
            double var11 = var1.l[var8];
            double var13 = var1.d[var8];
            double var15 = 1.0 - var9;
            double var17 = var11 * var5[var6] + Math.sin(var13 * 2.4 + var6 * 2.1 - var3 * 1.6) * 0.09 * var11 * var15;
            double var19 = Math.cos(var13 * 1.8 + var6 * 1.7 - var3 * 1.2) * 0.16 * var15;
            int var21 = oh.j(this.j(var9), oh.j(this.g, this.V, 0.5), var6 * 0.18);
            this.b(var7, var1, var8, var17, var19, this.Y * 1.15 * this.C(var9), var21, 0.9 * this.L.v(1.0 - var9), var2);
         }
      }
   }

   private void d(rw.i var1, Vec3 var2, double var3) {
      this.t(var1, var2, 6, 0.24, 3.2, 5.0, var3, true);

      for (int var5 = 0; var5 < 2; var5++) {
         h3.b var6 = this.b(var1, -1.0, true);

         for (int var7 = 0; var7 < var1.S; var7++) {
            double var8 = var1.o[var7];
            double var10 = var1.l[var7];
            double var12 = Math.min(1.0, this.C(var8));
            double var14 = var5 == 0 ? var10 * 0.97 : var10 * 0.04;
            double var16 = var10 * 0.5;
            double var18 = var16 + (var14 - var16) * (0.25 + 0.75 * var12);
            this.b(var6, var1, var7, var18, 0.0, this.Y * 0.55 * this.C(var8), this.j(var8), this.L.v(1.0 - var8), var2);
         }
      }
   }

   private void T(rw.i var1, Vec3 var2, double var3) {
      double[] var5 = new double[]{-1.0, 0.0, 1.0};

      for (double var9 : var5) {
         h3.b var11 = this.b(var1, 0.0, false);

         for (int var12 = 0; var12 < var1.S; var12++) {
            double var13 = var1.o[var12];
            double var15 = var1.l[var12];
            double var17 = var13 * var13;
            this.b(var11, var1, var12, var15 * (0.55 + var9 * 0.32 * var17), 0.0, var15 * 0.42 * var17, this.j(var13), 0.13 * this.L.v(1.0 - var13), var2);
         }
      }

      for (int var19 = 0; var19 < 2; var19++) {
         h3.b var21 = this.b(var1, -1.0, true);

         for (int var24 = 0; var24 < var1.S; var24++) {
            double var26 = var1.o[var24];
            double var27 = var1.l[var24];
            double var28 = var1.d[var24];
            double var29 = 1.0 - var26;
            double var30 = var28 * 3.0 + var19 * Math.PI - var3 * 3.0;
            this.b(
               var21,
               var1,
               var24,
               var27 * 0.55 + Math.cos(var30) * 0.22 * var27 * var29,
               Math.sin(var30) * 0.3 * var29,
               this.Y * 0.4 * this.C(var26),
               oh.j(this.j(var26), this.V, 0.3),
               0.8 * this.L.v(1.0 - var26),
               var2
            );
         }
      }

      h3.b var20 = this.b(var1, -1.0, true);

      for (int var22 = 0; var22 < var1.S; var22++) {
         double var25 = var1.o[var22];
         this.b(var20, var1, var22, var1.l[var22] * 0.55, 0.0, this.Y * 2.2 * this.C(var25), this.j(var25), (0.4 + 0.6 * var25) * this.L.v(1.0 - var25), var2);
      }

      int var23 = var1.S - 1;
      this.c(var1, var1.V[var23].add(0.0, var1.l[var23] * 0.55, 0.0), this.g, var1.l[var23] * 0.3, var2);
   }

   private void X(rw.i var1, Vec3 var2, double var3) {
      h3.b var5 = this.b(var1, 0.0, false);

      for (int var6 = 0; var6 < var1.S; var6++) {
         double var7 = var1.o[var6];
         this.b(
            var5, var1, var6, var1.l[var6] * 0.5, 0.0, var1.l[var6] * 0.3 * Math.min(1.0, this.C(var7) + 0.2), this.j(var7), 0.12 * this.L.v(1.0 - var7), var2
         );
      }

      for (int var25 = 0; var25 < 2; var25++) {
         h3.b var26 = this.b(var1, -1.0, true);
         Vec3 var8 = null;

         for (int var9 = 0; var9 < var1.S; var9++) {
            double var10 = var1.o[var9];
            double var12 = var1.l[var9];
            double var14 = var1.d[var9];
            double var16 = 0.6 + 0.4 * var10;
            double var18 = var14 * 2.4 + var25 * Math.PI - var3 * 2.4;
            double var20 = 0.46 * var12 * var16;
            double var22 = Math.max(var1.w[var9] * 0.8, 0.45) * var16;
            int var24 = var25 == 0 ? this.j(var10) : oh.j(this.g, this.V, var10);
            this.b(
               var26,
               var1,
               var9,
               var12 * 0.5 + Math.cos(var18) * var20,
               Math.sin(var18) * var22,
               this.Y * 0.75 * this.C(var10),
               var24,
               this.L.v(1.0 - var10),
               var2
            );
            var8 = var26.I[var26.L - 1];
         }

         if (var8 != null) {
            this.c(var1, var8, var25 == 0 ? this.g : this.V, 0.16, var2);
         }
      }
   }

   private void t(rw.i var1, Vec3 var2, double var3) {
      this.t(var1, var2, 4, 0.07, 7.0, 3.0, var3, true);
   }

   private void t(rw.i var1, Vec3 var2, int var3, double var4, double var6, double var8, double var10, boolean var12) {
      for (int var13 = 0; var13 < var3; var13++) {
         h3.b var14 = this.b(var1, 0.0, false);
         double var15 = (var13 + 0.5) / var3;
         double var17 = Math.abs(var15 * 2.0 - 1.0);
         double var19 = var12 ? 0.55 + 0.45 * var17 * var17 : 1.0;

         for (int var21 = 0; var21 < var1.S; var21++) {
            double var22 = var1.o[var21];
            double var24 = var1.l[var21];
            double var26 = var1.d[var21];
            double var28 = 0.3 + 0.7 * Math.min(1.0, this.C(var22));
            double var30 = var24 * 0.5 + (var15 - 0.5) * var24 * var28;
            double var32 = var6 == 0.0 ? 1.0 : 0.7 + 0.3 * Math.sin(var26 * var6 - var10 * var8 + var13 * 0.9);
            this.b(var14, var1, var21, var30, 0.0, 2.0 * var24 / var3 * var28, this.j(var22), var4 * var19 * var32 * this.L.v(1.0 - var22), var2);
         }
      }
   }

   private h3.b b(rw.i var1, double var2, boolean var4) {
      int var5 = var1.p++;
      if (var1.z[var5] == null) {
         var1.z[var5] = new h3.b(382);
      }

      h3.b var6 = var1.z[var5];
      var6.H();
      var6.k = 4096.0;
      var1.x[var5] = var2;
      var1.v[var5] = var4;
      return var6;
   }

   private void b(h3.b var1, rw.i var2, int var3, double var4, double var6, double var8, int var10, double var11, Vec3 var13) {
      Vec3 var14 = var2.m[var3];
      Vec3 var15 = var2.V[var3];
      Vec3 var16 = new Vec3(var15.x + var14.x * var6, var15.y + var4, var15.z + var14.z * var6);
      var1.P(var16, Math.max(0.0, var8), oh.S(var10, Math.clamp(var11, 0.0, 1.0) * W(var16, var13)));
   }

   private void c(rw.i var1, Vec3 var2, int var3, double var4, Vec3 var6) {
      double var7 = W(var2, var6);
      if (!(var7 <= 0.0) && var1.C < var1.K.length) {
         int var9 = var1.C++;
         var1.K[var9] = var2;
         var1.e[var9] = oh.S(var3, var7);
         var1.r[var9] = var4;
      }
   }

   static double W(Vec3 var0, Vec3 var1) {
      return yc.H((var0.distanceTo(var1) - 0.6) / 1.4);
   }

   private int j(double var1) {
      return oh.j(this.V, this.g, var1);
   }

   private double C(double var1) {
      return switch (this.t) {
         case E -> 1.0;
         case W -> var1 * var1;
         case f -> Math.sin(Math.PI * var1);
         case N -> Math.pow(var1, 3.0) * 1.4;
      };
   }

   private static boolean N(h3.b var0, Vec3 var1) {
      for (int var2 = 1; var2 < var0.L; var2++) {
         if (var0.I[var2 - 1].distanceToSqr(var1) <= var0.k
            && var0.I[var2 - 1].distanceToSqr(var0.I[var2]) >= 1.0E-10
            && (var0.c[var2 - 1] > 0.0 || var0.c[var2] > 0.0)
            && (var0.Z[var2 - 1] >>> 24 > 0 || var0.Z[var2] >>> 24 > 0)) {
            return true;
         }
      }

      return false;
   }

   h3.b V(int var1, int var2) {
      rw.i var3 = this.w.get(var1);
      return var3 != null && var2 < var3.p ? var3.z[var2] : null;
   }

   int i(int var1) {
      rw.i var2 = this.w.get(var1);
      return var2 == null ? 0 : var2.p;
   }

   private static final class i {
      final Vec3[] Y = new Vec3[128];
      final double[] R = new double[128];
      final double[] n = new double[128];
      final double[] g = new double[128];
      final long[] y = new long[128];
      int G;
      int P;
      double M;
      final Vec3[] V = new Vec3[382];
      final Vec3[] m = new Vec3[382];
      final double[] l = new double[382];
      final double[] w = new double[382];
      final double[] o = new double[382];
      final double[] d = new double[382];
      int S;
      final h3.f L = new h3.f();
      final h3.b[] z = new h3.b[8];
      final double[] x = new double[8];
      final boolean[] v = new boolean[8];
      final boolean[] k = new boolean[8];
      int p;
      final Vec3[] K = new Vec3[2];
      final int[] e = new int[2];
      final double[] r = new double[2];
      int C;
      boolean Q;
   }

   public enum l {
      z,
      e,
      L,
      N,
      n;
   }
}
