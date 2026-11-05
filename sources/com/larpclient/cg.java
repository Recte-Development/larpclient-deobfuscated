package com.larpclient;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class cg {
   private static final int S = 2048;
   private final cg.r[] M = new cg.r[2048];
   private final Random F = new Random();
   private Int2IntOpenHashMap x;
   private int P;
   private int u;
   private int C;
   private int D;
   private final cg.c[] d = new cg.c[2048];
   private final cg.c[] q = new cg.c[2048];
   private boolean Y;
   private static final Comparator<cg.c> s = Comparator.<cg.c>comparingDouble(var0 -> var0.S).reversed();
   public int X = 2048;
   public double H = 64.0;

   public cg() {
      for (int var1 = 0; var1 < 2048; var1++) {
         this.M[var1] = new cg.r();
         this.d[var1] = new cg.c();
      }

      mt.K("Particles", "Emitter engine initialized; capacity={}", 2048);
   }

   public int x() {
      return this.P;
   }

   public void J() {
      this.P = 0;
      if (this.x != null) {
         this.x.clear();
      }

      this.u = 0;
      this.C = this.D = 0;
      this.Y = (boolean)0;
   }

   public void b(int var1) {
      for (int var2 = this.P - 1; var2 >= 0; var2--) {
         if (this.M[var2].P == var1) {
            this.v(var2);
         }
      }
   }

   private void v(int var1) {
      cg.r var2 = this.M[var1];
      if (this.x != null) {
         int var3 = this.x.get(var2.P) - 1;
         if (var3 == 0) {
            this.x.remove(var2.P);
         } else {
            this.x.put(var2.P, var3);
         }
      }

      if (var2.O) {
         this.u--;
      }

      this.M[var1] = this.M[--this.P];
      this.M[this.P] = var2;
   }

   public boolean L() {
      return this.u > 0;
   }

   private double o(double var1, double var3) {
      return var1 + this.F.nextDouble() * (var3 - var1);
   }

   public void i(Vec3 var1, pf var2) {
      int var3 = Math.clamp(this.X, 0, 2048);
      int var4 = var2.X < 2048 ? this.t(var2.D) : 0;
      int var5 = Math.clamp(var2.X, 0, 2048);
      int var6 = var2.w ? this.t(var2.D) : 0;
      double var7 = Math.cos(var2.J * 0.05);
      double var9 = Math.sin(var2.J * 0.05);

      for (int var11 = 0; var11 < Math.clamp(var2.g, 0, 2048) && this.P < var3 && var4++ < var5; var11++) {
         cg.r var12 = this.M[this.P++];
         var12.I = var6++;
         double var13 = this.o(0.0, Math.PI * 2);
         double var15 = Math.sqrt(this.F.nextDouble());
         double var17 = this.o(-0.5, 0.5);
         double var19 = 0.0;
         double var21 = 0.0;
         switch (var2.A) {
            case Y:
               var19 = this.o(-0.5, 0.5);
               var21 = this.o(-0.5, 0.5);
               break;
            case T:
               double var23 = this.o(-1.0, 1.0);
               double var25 = Math.cbrt(this.F.nextDouble()) * 0.5;
               double var27 = Math.sqrt(1.0 - var23 * var23) * var25;
               var19 = Math.cos(var13) * var27;
               var21 = Math.sin(var13) * var27;
               var17 = var23 * var25;
               break;
            case e:
               var19 = Math.cos(var13) * var15 * 0.5;
               var21 = Math.sin(var13) * var15 * 0.5;
               break;
            case r:
               var19 = Math.cos(var13) * 0.5;
               var21 = Math.sin(var13) * 0.5;
               var17 = 0.0;
               break;
            case l:
               var17 = 0.0;
         }

         var12.K = var12.T = var1.x + var19 * var2.F;
         var12.k = var12.w = var1.y + var17 * var2.I;
         var12.u = var12.z = var1.z + var21 * var2.U;
         var12.N = this.o(-1.0, 1.0) * var2.Z * var2.b + var2.x + var2.z;
         var12.o = this.o(-1.0, 1.0) * var2.Z * var2.b + var2.v;
         var12.B = this.o(var2.w ? -1.0 : -0.4, 1.0) * var2.Z + var2.r + var2.K;
         var12.m = -Math.max(0.0, var2.t);
         var12.s = Math.max(0.05, this.o(var2.M, var2.O));
         var12.q = Math.max(0.001, this.o(var2.C, var2.d));
         if (var2.c != pf.l.e && Double.isFinite(var2.n)) {
            var12.k = var12.w = Math.max(var12.k, var2.n + var12.q * 1.2 + 0.002);
         }

         var12.A = var13;
         var12.j = this.o(Double.isFinite(var2.h) ? var2.h : -var2.e, var2.e);
         var12.p = var2.w;
         var12.b = var2.Q;
         var12.F = var2.H;
         var12.Y = this.o(0.0, Math.PI * 2);
         var12.L = var12.Y;
         var12.v = var2.G;
         var12.r = var2.Y;
         var12.f = (Math.PI * 2) / Math.max(0.05, var2.m);
         var12.p4 = var12.c = var12.Z = 0.0;
         if (var12.v != 0.0 || var12.r != 0.0) {
            var12.p4 = Math.sin(var12.L * 0.83) * var12.r;
            var12.c = Math.sin(var12.L) * var12.v;
            var12.Z = Math.cos(var12.L * 0.67) * var12.r;
            var12.K = var12.T = var12.T + var12.p4;
            var12.k = var12.w = var12.w + var12.c;
            var12.u = var12.z = var12.z + var12.Z;
         }

         var12.a = var2.W;
         var12.n = Math.exp(-Math.max(0.0, var2.q) * 0.05);
         var12.h = var2.f;
         var12.y = var2.J;
         var12.i = var7;
         var12.e = var9;
         var12.S = var2.S;
         var12.U = var2.l;
         var12.J = var2.p;
         var12.d = var2.P == cg.u.f ? var2.E : _3.D(var2.E, var2.N, this.F.nextDouble() * 0.35);
         var12.l = var2.N;
         var12.O = var2.i;
         if (var12.O) {
            this.u++;
         }

         var12.R = var2.P;
         var12.x = var2.c;
         var12.W = (boolean)0;
         var12.Q = var2.V.x;
         var12.G = var2.V.y;
         var12.H = var2.V.z;
         var12.g = var12.C = var12.M = var12.X = 0.0;
         if (var12.R == cg.u.f || var12.R == cg.u.z) {
            var12.g = this.o(0.0, Math.PI * 2);
            var12.C = this.o(0.0, Math.PI * 2);
            var12.M = this.o(-2.4, 2.4);
            var12.X = this.o(-1.8, 1.8);
         }

         var12.E = var2.R;
         var12.t = var2.B;
         var12.D = var2.L;
         var12.P = var2.D;
         if (this.x != null) {
            this.x.addTo(var12.P, 1);
         }

         var12.V = Double.isFinite(var2.j) ? Math.max(0.0, var2.j) : Math.max(0.0, this.H);
      }
   }

   public void q(Vec3 var1, cg.u var2, int var3, double var4, double var6, int var8, int var9, boolean var10, double var11) {
      pf var13 = new pf();
      var13.P = var2;
      var13.g = var3;
      var13.C = var13.d = var4;
      var13.M = var13.O = var6;
      var13.E = var8;
      var13.N = var9;
      var13.i = var10;
      var13.B = var4 * var11;
      this.i(var1, var13);
   }

   public void b() {
      this.Y(1.0);
   }

   public void Y(double var1) {
      this.O(var1, null);
   }

   public void O(double var1, w8.z var3) {
      if (Double.isFinite(var1) && !(var1 < 0.0)) {
         int var4 = 0;

         while (var4 < this.P) {
            cg.r var5 = this.M[var4];
            var5.m += 0.05;
            if (var5.m >= var5.s) {
               this.v(var4);
            } else {
               var5.T = var5.K;
               var5.w = var5.k;
               var5.z = var5.u;
               if (var5.m >= 0.0 && var5.R != cg.u.p && !var5.W) {
                  if (var5.p) {
                     var5.K = var5.K + var5.N * 0.05 * var1;
                     var5.k = var5.k + var5.B * 0.05 * var1;
                     var5.u = var5.u + var5.o * 0.05 * var1;
                     var5.N = var5.N * var5.n;
                     var5.o = var5.o * var5.n;
                     var5.B = (var5.B - var5.a * 0.05) * var5.n;
                     var4++;
                     continue;
                  }

                  double var6 = var5.Y + var5.m * 3.0;
                  double var8 = var5.N;
                  double var10 = var5.o;
                  var5.N = (var8 * var5.i - var10 * var5.e) * var5.n;
                  var5.o = (var8 * var5.e + var10 * var5.i) * var5.n;
                  var5.B = (var5.B - var5.a * 0.05) * var5.n;
                  if (var5.h != 0.0 || var5.N != 0.0 || var5.B != 0.0 || var5.o != 0.0) {
                     var5.K = var5.K + (var5.N + Math.sin(var6) * var5.h) * 0.05 * var1;
                     var5.k = var5.k + (var5.B + Math.cos(var6 * 1.3) * var5.h * 0.4) * 0.05 * var1;
                     var5.u = var5.u + (var5.o + Math.cos(var6 * 0.83) * var5.h) * 0.05 * var1;
                  }

                  if (var5.v != 0.0 || var5.r != 0.0) {
                     K(var5, var1);
                  }

                  if (var3 != null && var5.x != pf.l.e) {
                     y(var5, var3);
                  }
               }

               var4++;
            }
         }
      } else {
         throw new IllegalArgumentException("Invalid motion speed");
      }
   }

   private static void y(cg.r var0, w8.z var1) {
      Vec3 var2 = new Vec3(var0.T, var0.w, var0.z);
      Vec3 var3 = new Vec3(var0.K, var0.k, var0.u);
      if (!(var2.distanceToSqr(var3) < 1.0E-12)) {
         double var4 = var0.R == cg.u.z ? var0.q * 1.2 : 0.015;
         kx.U("Particle surface queries", 1L);
         w8.n var6 = var1.X(var2.add(0.0, -var4, 0.0), var3.add(0.0, -var4, 0.0));
         if (var6 != null) {
            Vec3 var7 = var6.normal();
            var0.K = var6.point().x + var7.x * 0.002;
            var0.k = var6.point().y + var4 + var7.y * 0.002;
            var0.u = var6.point().z + var7.z * 0.002;
            double var8 = var0.N * var7.x + var0.B * var7.y + var0.o * var7.z;
            if (!(var8 >= 0.0)) {
               int var10 = !(var7.y > 0.5) || var0.x != pf.l.l && !(-var8 < 0.55) ? 0 : 1;
               if (var10 != 0) {
                  var0.W = (boolean)1;
                  var0.N = var0.B = var0.o = 0.0;
                  var0.A = var0.A + var0.m * var0.j;
                  var0.g = var0.g + var0.m * var0.M;
                  var0.C = var0.C + var0.m * var0.X;
                  var0.j = var0.M = var0.X = 0.0;
                  if (var0.R == cg.u.z) {
                     var0.k = var6.point().y + co.l(var0.q, var0.g, var0.C) + 0.002;
                  }
               } else {
                  double var11 = var0.x == pf.l.I ? 0.38 : 0.08;
                  var0.N = (var0.N - (1.0 + var11) * var8 * var7.x) * 0.72;
                  var0.B = (var0.B - (1.0 + var11) * var8 * var7.y) * 0.72;
                  var0.o = (var0.o - (1.0 + var11) * var8 * var7.z) * 0.72;
               }
            }
         }
      }
   }

   private static void K(cg.r var0, double var1) {
      var0.L = var0.L + 0.05 * var1 * var0.f;
      double var3 = Math.sin(var0.L * 0.83) * var0.r;
      double var5 = Math.sin(var0.L) * var0.v;
      double var7 = Math.cos(var0.L * 0.67) * var0.r;
      var0.K = var0.K + (var3 - var0.p4);
      var0.k = var0.k + (var5 - var0.c);
      var0.u = var0.u + (var7 - var0.Z);
      var0.p4 = var3;
      var0.c = var5;
      var0.Z = var7;
   }

   public int t(int var1) {
      if (this.x == null) {
         this.y();
      }

      return this.x.get(var1);
   }

   private void y() {
      this.x = new Int2IntOpenHashMap();

      for (int var1 = 0; var1 < this.P; var1++) {
         this.x.addTo(this.M[var1].P, 1);
      }
   }

   public boolean r() {
      return this.D > 0;
   }

   public boolean C() {
      return this.Y;
   }

   public int D() {
      return this.C;
   }

   public static int c(double var0, int var2) {
      long var3 = ((long)(var0 * 1000.0) / 12L + var2 * 14L) % 360L;
      return _3.Z((float)var3 / 360.0F, 0.7F, 1.0F);
   }

   public void q(Vec3 var1, float var2, Frustum var3) {
      this.D(var1, var2, var3, 1.0, 1.0);
   }

   public void D(Vec3 var1, float var2, Frustum var3, double var4, double var6) {
      long var8 = kx.G();
      this.C = this.D = 0;
      this.Y = (boolean)0;
      if (var6 <= 0.0) {
         kx.u("Particles prepare", var8);
      } else {
         byte var10 = 0;

         for (int var11 = 0; var11 < this.P; var11++) {
            cg.r var12 = this.M[var11];
            double var13 = var12.m + var2 * 0.05;
            if (!(var13 < 0.0)) {
               double var15 = var12.T + (var12.K - var12.T) * var2;
               double var17 = var12.w + (var12.k - var12.w) * var2;
               double var19 = var12.z + (var12.u - var12.z) * var2;
               double var21 = var15 - var1.x;
               double var23 = var17 - var1.y;
               double var25 = var19 - var1.z;
               if (!(var21 * var21 + var23 * var23 + var25 * var25 > var12.V * var12.V)) {
                  double var27 = Math.clamp(var13 / var12.s, 0.0, 1.0);
                  double var29 = var12.q * var12.U.T(var27);
                  int var31 = var12.b ? c(ty.K(), var12.I) : _3.D(var12.d, var12.l, var27);
                  double var32 = var12.F == 1.0 ? var12.S.Z(var27) : Math.pow(var12.S.Z(var27), var12.F);
                  if (var6 < 1.0) {
                     var32 *= var6;
                  }

                  if (var4 < 1.0) {
                     double var34 = var12.V * var12.V;
                     var32 *= hp.R((var34 - var21 * var21 - var23 * var23 - var25 * var25) / Math.max(0.001, var34 * (1.0 - var4 * var4)));
                  }

                  int var40 = var12.p ? var31 & 16777215 | Math.clamp(Math.round((var31 >>> 24) * var32), 0, 255) << 24 : _3.U(var31, var32);
                  if (var40 >>> 24 != 0) {
                     double var35 = var12.R == cg.u.p ? var12.t * (1.0 - Math.pow(1.0 - var27, 3.0)) : var29;
                     double var37 = var12.R == cg.u.p ? var35 + Math.abs(var12.D) : Math.abs(var29) * 5.0;
                     if (var3 == null || var3.isVisible(new AABB(var15 - var37, var17 - var37, var19 - var37, var15 + var37, var17 + var37, var19 + var37))) {
                        cg.c var39 = this.d[this.C];
                        var39.d = var15;
                        var39.V = var17;
                        var39.a = var19;
                        var39.O = var29;
                        var39.u = var35;
                        var39.w = var12.A + var13 * var12.j;
                        var39.W = var12.g + var13 * var12.M;
                        var39.i = var12.C + var13 * var12.X;
                        var39.Q = var12.Y + var13 * 4.0;
                        var39.E = var12.N;
                        var39.p = var12.B;
                        var39.H = var12.o;
                        var39.G = var12.D;
                        var39.L = var12.Q;
                        var39.s = var12.G;
                        var39.c = var12.H;
                        var39.I = var40;
                        var39.J = var12.O;
                        var39.b = var12.R;
                        var39.e = var12.J;
                        var39.o = var12.E;
                        var39.k = (boolean)(var12.R != cg.u.T && var12.R != cg.u.a ? 0 : 1);
                        var39.S = var21 * var21 + var23 * var23 + var25 * var25;
                        var10 |= var39.e != pf.x.E ? 1 : 0;
                        this.q[this.C++] = var39;
                        if (var39.J) {
                           this.D++;
                        }

                        this.Y = this.Y | var39.k;
                     }
                  }
               }
            }
         }

         if (var10 != 0) {
            Arrays.sort(this.q, 0, this.C, s);
         }

         kx.U("Particles prepared", this.C);
         kx.u("Particles prepare", var8);
      }
   }

   public void e(ki var1, Vec3 var2, float var3, boolean var4) {
      long var5 = kx.G();
      cg.c var7 = null;

      for (int var8 = 0; var8 < this.C; var8++) {
         cg.c var9 = this.q[var8];
         if (!var4 || var9.J) {
            if (var7 != null && (var9.e != pf.x.E || var7.e != pf.x.E) && !D(var7, var9)) {
               var1.Z();
            }

            var1.blend(var9.e);
            var1.v(var9.k);
            if (var9.b == cg.u.p) {
               var1.H(var9.d, var9.V, var9.a, var9.u, var9.G, var9.I, var9.L, var9.s, var9.c);
            } else if (var9.b == cg.u.f) {
               var1.g(var9.d, var9.V, var9.a, var9.O, var9.w, var9.W, var9.i, var9.Q, var9.I);
            } else if (var9.b == cg.u.z) {
               var1.k(var9.d, var9.V, var9.a, var9.O, var9.w, var9.W, var9.i, var9.I);
            } else {
               var1.L(var9.d, var9.V, var9.a, var9.O, var9.w, var9.I, var9.b, var9.E, var9.p, var9.H, var9.o);
            }

            var7 = var9;
         }
      }

      var1.v(false);
      var1.blend(pf.x.S);
      kx.u("Particles", var5);
   }

   private static boolean D(cg.c var0, cg.c var1) {
      return var0.e == var1.e
         && var0.k == var1.k
         && var0.o == var1.o
         && (var0.b == cg.u.f ? 1 : 0) == (var1.b == cg.u.f ? 1 : 0)
         && (var0.b == cg.u.z ? 1 : 0) == (var1.b == cg.u.z ? 1 : 0)
         && (var0.b == cg.u.p ? 1 : 0) == (var1.b == cg.u.p ? 1 : 0);
   }

   private static final class c {
      double d;
      double V;
      double a;
      double O;
      double u;
      double G;
      double w;
      double W;
      double i;
      double Q;
      double E;
      double p;
      double H;
      double S;
      double L;
      double s;
      double c;
      int I;
      boolean J;
      boolean k;
      cg.u b;
      pf.t o;
      pf.x e;
   }

   private static final class r {
      double K;
      double k;
      double u;
      double T;
      double w;
      double z;
      double N;
      double B;
      double o;
      double m;
      double s;
      double q;
      double A;
      double j;
      double Y;
      double g;
      double C;
      double M;
      double X;
      double a;
      double n;
      double h;
      double y;
      double i;
      double e;
      double t;
      double D;
      double V;
      double v;
      double r;
      double L;
      double f;
      double p4;
      double c;
      double Z;
      double Q;
      double G;
      double H;
      pf.l x;
      boolean W;
      int d;
      int l;
      int P;
      int I;
      boolean O;
      boolean p;
      boolean b;
      double F;
      cg.u R;
      pf.t E;
      hp.y S;
      hp.u U;
      pf.x J;
   }

   public enum u {
      M,
      a,
      n,
      h,
      w,
      B,
      Z,
      p,
      u,
      z,
      x,
      T,
      f;
   }
}
