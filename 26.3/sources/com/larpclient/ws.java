package com.larpclient;

import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.world.phys.Vec3;

public final class ws {
   static final int c = 128;
   static final int V = 16;
   static final int d = 8;
   static final int Z = 3;
   static final int C = 382;
   private final Map<Integer, ws.v> i = new LinkedHashMap<>();
   private long T;
   private boolean l;
   private _0.v u = _0.v.c;
   public ws.m W = ws.m.p;
   public vw.p a = vw.p.V;
   public pr.b n = pr.b.A;
   public double f = 1.2;
   public double X = 0.12;
   public boolean b = (boolean)1;
   public boolean Q = (boolean)1;
   public int v = com.larpclient.c.y;
   public int E = com.larpclient.c.t;

   public ws() {
      _q.z("Trails", "Body trail engine initialized: {} sources, {} points per source", 16, 128);
   }

   public boolean s() {
      return this.i.isEmpty();
   }

   public int e() {
      return this.i.size();
   }

   public int x() {
      return this.i.values().stream().mapToInt(var0 -> var0.w).sum();
   }

   public boolean M() {
      return this.l;
   }

   public void v() {
      this.i.clear();
      this.T = 0L;
      this.l = (boolean)0;
   }

   public void o() {
      this.T++;
      this.i.values().removeIf(var1 -> {
         while (var1.w > 0 && this.T - var1.b[var1.e] > this.f * 20.0) {
            var1.e = (var1.e + 1) % 128;
            var1.w--;
         }

         return var1.w == 0;
      });
   }

   public void W(int var1, Vec3 var2, double var3, double var5) {
      if (Double.isFinite(var2.x) && Double.isFinite(var2.y) && Double.isFinite(var2.z) && Double.isFinite(var3) && Double.isFinite(var5)) {
         var3 = Math.clamp(var3, 0.1, 6.0);
         var5 = Math.clamp(var5, 0.1, 4.0);
         ws.v var7 = this.i.get(var1);
         if (var7 == null) {
            if (this.i.size() >= 16) {
               return;
            }

            var7 = new ws.v();
            this.i.put(var1, var7);
         }

         double var8 = var7.D;
         if (var7.w > 0) {
            int var10 = (var7.e + var7.w - 1) % 128;
            double var11 = var7.z[var10].distanceToSqr(var2);
            if (var11 > 64.0) {
               var7.e = var7.w = 0;
            } else {
               if (var11 < 4.0E-4 && Math.abs(var7.B[var10] - var3) < 0.02) {
                  return;
               }

               var8 += Math.sqrt(var11);
            }
         }

         if (var7.w == 128) {
            var7.e = (var7.e + 1) % 128;
            var7.w--;
         }

         int var15 = (var7.e + var7.w++) % 128;
         var7.z[var15] = var2;
         var7.B[var15] = var3;
         var7.N[var15] = var5;
         var7.h[var15] = var7.D = var8;
         var7.b[var15] = this.T;
      }
   }

   public void J(Vec3 var1, float var2) {
      this.l = (boolean)0;
      this.u = this.W == ws.m.b ? _0.v.z : _0.v.c;
      double var3 = hz.Q();

      for (ws.v var6 : this.i.values()) {
         var6.P = (boolean)0;
         var6.F = var6.c = 0;
         this.H(var6, var2);
         if (var6.u >= 2) {
            switch (this.W) {
               case b:
                  this.e(var6, var1, var3);
                  break;
               case p:
                  this.A(var6, var1, var3);
                  break;
               case R:
                  this.S(var6, var1, var3);
                  break;
               case q:
                  this.s(var6, var1, var3);
                  break;
               case r:
                  this.Q(var6, var1, var3);
            }

            for (int var7 = 0; var7 < var6.F; var7++) {
               x.c var8 = var6.A[var7];
               if (this.a != vw.p.o) {
                  var8.v();
               }

               var8.T(var1);
               var6.k[var7] = L(var8, var1);
               var6.P = var6.P | var6.k[var7];
            }

            if (this.b && var6.P) {
               this.l = (boolean)1;
            }
         }
      }
   }

   public void A(fz var1, boolean var2) {
      if (!var2 || this.b) {
         long var3 = he.s();
         var1.blend(this.u);

         for (ws.v var6 : this.i.values()) {
            if (var6.P) {
               for (int var7 = 0; var7 < var6.F; var7++) {
                  if (var6.k[var7]) {
                     if (var6.K[var7] >= 0.0) {
                        var1.Y(var6.A[var7], var6.K[var7]);
                     } else {
                        var1.n(var6.A[var7], var6.f[var7]);
                     }
                  }
               }
            }
         }

         var1.blend(_0.v.c);

         for (ws.v var13 : this.i.values()) {
            if (var13.P) {
               for (int var14 = 0; var14 < var13.c; var14++) {
                  Vec3 var8 = var13.I[var14];
                  int var9 = var13.Q[var14];
                  double var10 = var13.T[var14];
                  var1.a(var8.x, var8.y, var8.z, var10, 0.0, vv.O(var9, 0.32), l.b.P, 0.0, 0.0, 0.0);
                  var1.a(var8.x, var8.y, var8.z, var10 * 0.32, 0.0, vv.Q(var9, var9 & 0xFF000000 | 16777215, 0.7), l.b.P, 0.0, 0.0, 0.0);
               }
            }
         }

         var1.blend(_0.v.z);
         he.T("Body Trails", var3);
      }
   }

   private void H(ws.v var1, float var2) {
      var1.u = 0;
      if (var1.w != 0) {
         int var3 = this.Q ? 3 : 1;

         for (int var4 = 1; var4 < var1.w; var4++) {
            int var5 = (var1.e + var4 - 1) % 128;
            int var6 = (var1.e + var4) % 128;
            Vec3 var7 = var1.z[var5];
            Vec3 var8 = var1.z[var6];
            double var9 = var1.B[var6];
            double var11 = var1.N[var6];
            double var13 = var1.h[var6];
            if (var4 == var1.w - 1 && var1.b[var6] == this.T) {
               var8 = var7.lerp(var8, var2);
               var9 = var1.B[var5] + (var9 - var1.B[var5]) * var2;
               var11 = var1.N[var5] + (var11 - var1.N[var5]) * var2;
               var13 = var1.h[var5] + (var13 - var1.h[var5]) * var2;
            }

            double var15 = this.z(var1.b[var5], var2);
            double var17 = this.z(var1.b[var6], var2);
            if (var4 == 1) {
               this.D(var1, var7, var1.B[var5], var1.N[var5], var15, var1.h[var5]);
            }

            if (var3 > 1) {
               Vec3 var19 = var4 > 1 ? var1.z[(var1.e + var4 - 2) % 128] : var7.add(var7.subtract(var8));
               Vec3 var20 = var4 < var1.w - 1 ? var1.z[(var1.e + var4 + 1) % 128] : var8.add(var8.subtract(var7));
               var1.X.P(var19, var7, var8, var20);
            }

            for (int var27 = 1; var27 <= var3; var27++) {
               double var28 = (double)var27 / var3;
               this.D(
                  var1,
                  var3 == 1 ? var8 : var1.X.x(var28),
                  var1.B[var5] + (var9 - var1.B[var5]) * var28,
                  var1.N[var5] + (var11 - var1.N[var5]) * var28,
                  var15 + (var17 - var15) * var28,
                  var1.h[var5] + (var13 - var1.h[var5]) * var28
               );
            }
         }

         Vec3 var22 = null;

         for (int var23 = 0; var23 < var1.u; var23++) {
            Vec3 var24 = var1.p[Math.min(var23 + 1, var1.u - 1)].subtract(var1.p[Math.max(var23 - 1, 0)]);
            double var25 = Math.sqrt(var24.x * var24.x + var24.z * var24.z);
            Vec3 var26 = var25 < 1.0E-6 ? (var22 != null ? var22 : new Vec3(1.0, 0.0, 0.0)) : new Vec3(-var24.z / var25, 0.0, var24.x / var25);
            if (var22 != null && var26.dot(var22) < 0.0) {
               var26 = var26.scale(-1.0);
            }

            var22 = var26;
            var1.q[var23] = var26;
         }
      }
   }

   private void D(ws.v var1, Vec3 var2, double var3, double var5, double var7, double var9) {
      int var11 = var1.u++;
      var1.p[var11] = var2;
      var1.L[var11] = var3;
      var1.l[var11] = var5;
      var1.V[var11] = var7;
      var1.J[var11] = var9;
   }

   private double z(long var1, float var3) {
      return Math.clamp(1.0 - ((float)this.T + var3 - (float)var1) / (this.f * 20.0), 0.0, 1.0);
   }

   private void e(ws.v var1, Vec3 var2, double var3) {
      this.Y(var1, var2, 4, 0.1, 0.0, 0.0, var3, false);
      double[] var5 = new double[]{0.16, 0.52, 0.88};

      for (int var6 = 0; var6 < var5.length; var6++) {
         x.c var7 = this.L(var1, 0.55, false);

         for (int var8 = 0; var8 < var1.u; var8++) {
            double var9 = var1.V[var8];
            double var11 = var1.L[var8];
            double var13 = var1.J[var8];
            double var15 = 1.0 - var9;
            double var17 = var11 * var5[var6] + Math.sin(var13 * 2.4 + var6 * 2.1 - var3 * 1.6) * 0.09 * var11 * var15;
            double var19 = Math.cos(var13 * 1.8 + var6 * 1.7 - var3 * 1.2) * 0.16 * var15;
            int var21 = vv.Q(this.Y(var9), vv.Q(this.v, this.E, 0.5), var6 * 0.18);
            this.Z(var7, var1, var8, var17, var19, this.X * 1.15 * this.N(var9), var21, 0.9 * this.n.p(1.0 - var9), var2);
         }
      }
   }

   private void A(ws.v var1, Vec3 var2, double var3) {
      this.Y(var1, var2, 6, 0.24, 3.2, 5.0, var3, true);

      for (int var5 = 0; var5 < 2; var5++) {
         x.c var6 = this.L(var1, -1.0, true);

         for (int var7 = 0; var7 < var1.u; var7++) {
            double var8 = var1.V[var7];
            double var10 = var1.L[var7];
            double var12 = Math.min(1.0, this.N(var8));
            double var14 = var5 == 0 ? var10 * 0.97 : var10 * 0.04;
            double var16 = var10 * 0.5;
            double var18 = var16 + (var14 - var16) * (0.25 + 0.75 * var12);
            this.Z(var6, var1, var7, var18, 0.0, this.X * 0.55 * this.N(var8), this.Y(var8), this.n.p(1.0 - var8), var2);
         }
      }
   }

   private void S(ws.v var1, Vec3 var2, double var3) {
      double[] var5 = new double[]{-1.0, 0.0, 1.0};

      for (double var9 : var5) {
         x.c var11 = this.L(var1, 0.0, false);

         for (int var12 = 0; var12 < var1.u; var12++) {
            double var13 = var1.V[var12];
            double var15 = var1.L[var12];
            double var17 = var13 * var13;
            this.Z(var11, var1, var12, var15 * (0.55 + var9 * 0.32 * var17), 0.0, var15 * 0.42 * var17, this.Y(var13), 0.13 * this.n.p(1.0 - var13), var2);
         }
      }

      for (int var19 = 0; var19 < 2; var19++) {
         x.c var21 = this.L(var1, -1.0, true);

         for (int var24 = 0; var24 < var1.u; var24++) {
            double var26 = var1.V[var24];
            double var27 = var1.L[var24];
            double var28 = var1.J[var24];
            double var29 = 1.0 - var26;
            double var30 = var28 * 3.0 + var19 * Math.PI - var3 * 3.0;
            this.Z(
               var21,
               var1,
               var24,
               var27 * 0.55 + Math.cos(var30) * 0.22 * var27 * var29,
               Math.sin(var30) * 0.3 * var29,
               this.X * 0.4 * this.N(var26),
               vv.Q(this.Y(var26), this.E, 0.3),
               0.8 * this.n.p(1.0 - var26),
               var2
            );
         }
      }

      x.c var20 = this.L(var1, -1.0, true);

      for (int var22 = 0; var22 < var1.u; var22++) {
         double var25 = var1.V[var22];
         this.Z(var20, var1, var22, var1.L[var22] * 0.55, 0.0, this.X * 2.2 * this.N(var25), this.Y(var25), (0.4 + 0.6 * var25) * this.n.p(1.0 - var25), var2);
      }

      int var23 = var1.u - 1;
      this.s(var1, var1.p[var23].add(0.0, var1.L[var23] * 0.55, 0.0), this.v, var1.L[var23] * 0.3, var2);
   }

   private void s(ws.v var1, Vec3 var2, double var3) {
      x.c var5 = this.L(var1, 0.0, false);

      for (int var6 = 0; var6 < var1.u; var6++) {
         double var7 = var1.V[var6];
         this.Z(
            var5, var1, var6, var1.L[var6] * 0.5, 0.0, var1.L[var6] * 0.3 * Math.min(1.0, this.N(var7) + 0.2), this.Y(var7), 0.12 * this.n.p(1.0 - var7), var2
         );
      }

      for (int var25 = 0; var25 < 2; var25++) {
         x.c var26 = this.L(var1, -1.0, true);
         Vec3 var8 = null;

         for (int var9 = 0; var9 < var1.u; var9++) {
            double var10 = var1.V[var9];
            double var12 = var1.L[var9];
            double var14 = var1.J[var9];
            double var16 = 0.6 + 0.4 * var10;
            double var18 = var14 * 2.4 + var25 * Math.PI - var3 * 2.4;
            double var20 = 0.46 * var12 * var16;
            double var22 = Math.max(var1.l[var9] * 0.8, 0.45) * var16;
            int var24 = var25 == 0 ? this.Y(var10) : vv.Q(this.v, this.E, var10);
            this.Z(
               var26,
               var1,
               var9,
               var12 * 0.5 + Math.cos(var18) * var20,
               Math.sin(var18) * var22,
               this.X * 0.75 * this.N(var10),
               var24,
               this.n.p(1.0 - var10),
               var2
            );
            var8 = var26.r[var26.l - 1];
         }

         if (var8 != null) {
            this.s(var1, var8, var25 == 0 ? this.v : this.E, 0.16, var2);
         }
      }
   }

   private void Q(ws.v var1, Vec3 var2, double var3) {
      this.Y(var1, var2, 4, 0.07, 7.0, 3.0, var3, true);
   }

   private void Y(ws.v var1, Vec3 var2, int var3, double var4, double var6, double var8, double var10, boolean var12) {
      for (int var13 = 0; var13 < var3; var13++) {
         x.c var14 = this.L(var1, 0.0, false);
         double var15 = (var13 + 0.5) / var3;
         double var17 = Math.abs(var15 * 2.0 - 1.0);
         double var19 = var12 ? 0.55 + 0.45 * var17 * var17 : 1.0;

         for (int var21 = 0; var21 < var1.u; var21++) {
            double var22 = var1.V[var21];
            double var24 = var1.L[var21];
            double var26 = var1.J[var21];
            double var28 = 0.3 + 0.7 * Math.min(1.0, this.N(var22));
            double var30 = var24 * 0.5 + (var15 - 0.5) * var24 * var28;
            double var32 = var6 == 0.0 ? 1.0 : 0.7 + 0.3 * Math.sin(var26 * var6 - var10 * var8 + var13 * 0.9);
            this.Z(var14, var1, var21, var30, 0.0, 2.0 * var24 / var3 * var28, this.Y(var22), var4 * var19 * var32 * this.n.p(1.0 - var22), var2);
         }
      }
   }

   private x.c L(ws.v var1, double var2, boolean var4) {
      int var5 = var1.F++;
      if (var1.A[var5] == null) {
         var1.A[var5] = new x.c(382);
      }

      x.c var6 = var1.A[var5];
      var6.w();
      var6.e = 4096.0;
      var1.K[var5] = var2;
      var1.f[var5] = var4;
      return var6;
   }

   private void Z(x.c var1, ws.v var2, int var3, double var4, double var6, double var8, int var10, double var11, Vec3 var13) {
      Vec3 var14 = var2.q[var3];
      Vec3 var15 = var2.p[var3];
      Vec3 var16 = new Vec3(var15.x + var14.x * var6, var15.y + var4, var15.z + var14.z * var6);
      var1.J(var16, Math.max(0.0, var8), vv.O(var10, Math.clamp(var11, 0.0, 1.0) * r(var16, var13)));
   }

   private void s(ws.v var1, Vec3 var2, int var3, double var4, Vec3 var6) {
      double var7 = r(var2, var6);
      if (!(var7 <= 0.0) && var1.c < var1.I.length) {
         int var9 = var1.c++;
         var1.I[var9] = var2;
         var1.Q[var9] = vv.O(var3, var7);
         var1.T[var9] = var4;
      }
   }

   static double r(Vec3 var0, Vec3 var1) {
      return pr.k((var0.distanceTo(var1) - 0.6) / 1.4);
   }

   private int Y(double var1) {
      return vv.Q(this.E, this.v, var1);
   }

   private double N(double var1) {
      return switch (this.a) {
         case o -> 1.0;
         case V -> var1 * var1;
         case n -> Math.sin(Math.PI * var1);
         case z -> Math.pow(var1, 3.0) * 1.4;
      };
   }

   private static boolean L(x.c var0, Vec3 var1) {
      for (int var2 = 1; var2 < var0.l; var2++) {
         if (var0.r[var2 - 1].distanceToSqr(var1) <= var0.e
            && var0.r[var2 - 1].distanceToSqr(var0.r[var2]) >= 1.0E-10
            && (var0.x[var2 - 1] > 0.0 || var0.x[var2] > 0.0)
            && (var0.W[var2 - 1] >>> 24 > 0 || var0.W[var2] >>> 24 > 0)) {
            return true;
         }
      }

      return false;
   }

   x.c E(int var1, int var2) {
      ws.v var3 = this.i.get(var1);
      return var3 != null && var2 < var3.F ? var3.A[var2] : null;
   }

   int w(int var1) {
      ws.v var2 = this.i.get(var1);
      return var2 == null ? 0 : var2.F;
   }

   public enum m {
      b,
      p,
      R,
      q,
      r;
   }

   private static final class v {
      final Vec3[] z = new Vec3[128];
      final double[] B = new double[128];
      final double[] N = new double[128];
      final double[] h = new double[128];
      final long[] b = new long[128];
      int e;
      int w;
      double D;
      final Vec3[] p = new Vec3[382];
      final Vec3[] q = new Vec3[382];
      final double[] L = new double[382];
      final double[] l = new double[382];
      final double[] V = new double[382];
      final double[] J = new double[382];
      int u;
      final x.l X = new x.l();
      final x.c[] A = new x.c[8];
      final double[] K = new double[8];
      final boolean[] f = new boolean[8];
      final boolean[] k = new boolean[8];
      int F;
      final Vec3[] I = new Vec3[2];
      final int[] Q = new int[2];
      final double[] T = new double[2];
      int c;
      boolean P;
   }
}
