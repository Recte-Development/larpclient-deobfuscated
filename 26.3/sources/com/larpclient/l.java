package com.larpclient;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class l {
   private static final int a = 2048;
   private final l.z[] h = new l.z[2048];
   private final Random f = new Random();
   private Int2IntOpenHashMap J;
   private int C;
   private int x;
   private int y;
   private int V;
   private final l.s[] X = new l.s[2048];
   private final l.s[] u = new l.s[2048];
   private boolean S;
   private static final Comparator<l.s> t = Comparator.<l.s>comparingDouble(var0 -> var0.S).reversed();
   public int I = 2048;
   public double Q = 64.0;

   public l() {
      for (int var1 = 0; var1 < 2048; var1++) {
         this.h[var1] = new l.z();
         this.X[var1] = new l.s();
      }

      _q.z("Particles", "Emitter engine initialized; capacity={}", 2048);
   }

   public int K() {
      return this.C;
   }

   public void k() {
      this.C = 0;
      if (this.J != null) {
         this.J.clear();
      }

      this.x = 0;
      this.y = this.V = 0;
      this.S = (boolean)0;
   }

   public void c(int var1) {
      for (int var2 = this.C - 1; var2 >= 0; var2--) {
         if (this.h[var2].N == var1) {
            this.B(var2);
         }
      }
   }

   private void B(int var1) {
      l.z var2 = this.h[var1];
      if (this.J != null) {
         int var3 = this.J.get(var2.N) - 1;
         if (var3 == 0) {
            this.J.remove(var2.N);
         } else {
            this.J.put(var2.N, var3);
         }
      }

      if (var2.m) {
         this.x--;
      }

      this.h[var1] = this.h[--this.C];
      this.h[this.C] = var2;
   }

   public boolean r() {
      return this.x > 0;
   }

   private double S(double var1, double var3) {
      return var1 + this.f.nextDouble() * (var3 - var1);
   }

   public void Q(Vec3 var1, _0 var2) {
      int var3 = Math.clamp(this.I, 0, 2048);
      int var4 = var2.A < 2048 ? this.W(var2.r) : 0;
      int var5 = Math.clamp(var2.A, 0, 2048);
      int var6 = var2.G ? this.W(var2.r) : 0;
      double var7 = Math.cos(var2.s * 0.05);
      double var9 = Math.sin(var2.s * 0.05);

      for (int var11 = 0; var11 < Math.clamp(var2.w, 0, 2048) && this.C < var3 && var4++ < var5; var11++) {
         l.z var12 = this.h[this.C++];
         var12.U = var6++;
         double var13 = this.S(0.0, Math.PI * 2);
         double var15 = Math.sqrt(this.f.nextDouble());
         double var17 = this.S(-0.5, 0.5);
         double var19 = 0.0;
         double var21 = 0.0;
         switch (var2.D) {
            case M:
               var19 = this.S(-0.5, 0.5);
               var21 = this.S(-0.5, 0.5);
               break;
            case x:
               double var23 = this.S(-1.0, 1.0);
               double var25 = Math.cbrt(this.f.nextDouble()) * 0.5;
               double var27 = Math.sqrt(1.0 - var23 * var23) * var25;
               var19 = Math.cos(var13) * var27;
               var21 = Math.sin(var13) * var27;
               var17 = var23 * var25;
               break;
            case y:
               var19 = Math.cos(var13) * var15 * 0.5;
               var21 = Math.sin(var13) * var15 * 0.5;
               break;
            case F:
               var19 = Math.cos(var13) * 0.5;
               var21 = Math.sin(var13) * 0.5;
               var17 = 0.0;
               break;
            case G:
               var17 = 0.0;
         }

         var12.Q = var12.T = var1.x + var19 * var2.l;
         var12.f = var12.g = var1.y + var17 * var2.x;
         var12.X = var12.b = var1.z + var21 * var2.e;
         var12.p = this.S(-1.0, 1.0) * var2.I * var2.O + var2.R + var2.o;
         var12.Z = this.S(-1.0, 1.0) * var2.I * var2.O + var2.S;
         var12.j = this.S(var2.G ? -1.0 : -0.4, 1.0) * var2.I + var2.U + var2.P;
         var12.c = -Math.max(0.0, var2.V);
         var12.P = Math.max(0.05, this.S(var2.Q, var2.W));
         var12.E = Math.max(0.001, this.S(var2.i, var2.m));
         if (var2.p != _0.e.g && Double.isFinite(var2.g)) {
            var12.f = var12.g = Math.max(var12.f, var2.g + var12.E * 1.2 + 0.002);
         }

         var12.UP = var13;
         var12.u = this.S(Double.isFinite(var2.c) ? var2.c : -var2.u, var2.u);
         var12.o = var2.G;
         var12.M = var2.v;
         var12.s = var2.H;
         var12.i = this.S(0.0, Math.PI * 2);
         var12.k = var12.i;
         var12.Y = var2.y;
         var12.R = var2.J;
         var12.v = (Math.PI * 2) / Math.max(0.05, var2.d);
         var12.O = var12.h = var12.C = 0.0;
         if (var12.Y != 0.0 || var12.R != 0.0) {
            var12.O = Math.sin(var12.k * 0.83) * var12.R;
            var12.h = Math.sin(var12.k) * var12.Y;
            var12.C = Math.cos(var12.k * 0.67) * var12.R;
            var12.Q = var12.T = var12.T + var12.O;
            var12.f = var12.g = var12.g + var12.h;
            var12.X = var12.b = var12.b + var12.C;
         }

         var12.a = var2.n;
         var12.I = Math.exp(-Math.max(0.0, var2.q) * 0.05);
         var12.S = var2.K;
         var12.q = var2.s;
         var12.t = var7;
         var12.D = var9;
         var12.L = var2.C;
         var12.y = var2.T;
         var12.l = var2.j;
         var12.F = var2.L == l.b.O ? var2.F : vv.Q(var2.F, var2.t, this.f.nextDouble() * 0.35);
         var12.B = var2.t;
         var12.m = var2.M;
         if (var12.m) {
            this.x++;
         }

         var12.G = var2.L;
         var12.n = var2.p;
         var12.V = (boolean)0;
         var12.e = var2.Y.x;
         var12.w = var2.Y.y;
         var12.J = var2.Y.z;
         var12.H = var12.A = var12.x = var12.z = 0.0;
         if (var12.G == l.b.O || var12.G == l.b.u) {
            var12.H = this.S(0.0, Math.PI * 2);
            var12.A = this.S(0.0, Math.PI * 2);
            var12.x = this.S(-2.4, 2.4);
            var12.z = this.S(-1.8, 1.8);
         }

         var12.W = var2.Z;
         var12.r = var2.h;
         var12.d = var2.z;
         var12.N = var2.r;
         if (this.J != null) {
            this.J.addTo(var12.N, 1);
         }

         var12.K = Double.isFinite(var2.k) ? Math.max(0.0, var2.k) : Math.max(0.0, this.Q);
      }
   }

   public void w(Vec3 var1, l.b var2, int var3, double var4, double var6, int var8, int var9, boolean var10, double var11) {
      _0 var13 = new _0();
      var13.L = var2;
      var13.w = var3;
      var13.i = var13.m = var4;
      var13.Q = var13.W = var6;
      var13.F = var8;
      var13.t = var9;
      var13.M = var10;
      var13.h = var4 * var11;
      this.Q(var1, var13);
   }

   public void Q() {
      this.A(1.0);
   }

   public void A(double var1) {
      this.G(var1, null);
   }

   public void G(double var1, v9.w var3) {
      if (Double.isFinite(var1) && !(var1 < 0.0)) {
         int var4 = 0;

         while (var4 < this.C) {
            l.z var5 = this.h[var4];
            var5.c += 0.05;
            if (var5.c >= var5.P) {
               this.B(var4);
            } else {
               var5.T = var5.Q;
               var5.g = var5.f;
               var5.b = var5.X;
               if (var5.c >= 0.0 && var5.G != l.b.T && !var5.V) {
                  if (var5.o) {
                     var5.Q = var5.Q + var5.p * 0.05 * var1;
                     var5.f = var5.f + var5.j * 0.05 * var1;
                     var5.X = var5.X + var5.Z * 0.05 * var1;
                     var5.p = var5.p * var5.I;
                     var5.Z = var5.Z * var5.I;
                     var5.j = (var5.j - var5.a * 0.05) * var5.I;
                     var4++;
                     continue;
                  }

                  double var6 = var5.i + var5.c * 3.0;
                  double var8 = var5.p;
                  double var10 = var5.Z;
                  var5.p = (var8 * var5.t - var10 * var5.D) * var5.I;
                  var5.Z = (var8 * var5.D + var10 * var5.t) * var5.I;
                  var5.j = (var5.j - var5.a * 0.05) * var5.I;
                  if (var5.S != 0.0 || var5.p != 0.0 || var5.j != 0.0 || var5.Z != 0.0) {
                     var5.Q = var5.Q + (var5.p + Math.sin(var6) * var5.S) * 0.05 * var1;
                     var5.f = var5.f + (var5.j + Math.cos(var6 * 1.3) * var5.S * 0.4) * 0.05 * var1;
                     var5.X = var5.X + (var5.Z + Math.cos(var6 * 0.83) * var5.S) * 0.05 * var1;
                  }

                  if (var5.Y != 0.0 || var5.R != 0.0) {
                     u(var5, var1);
                  }

                  if (var3 != null && var5.n != _0.e.g) {
                     N(var5, var3);
                  }
               }

               var4++;
            }
         }
      } else {
         throw new IllegalArgumentException("Invalid motion speed");
      }
   }

   private static void N(l.z var0, v9.w var1) {
      Vec3 var2 = new Vec3(var0.T, var0.g, var0.b);
      Vec3 var3 = new Vec3(var0.Q, var0.f, var0.X);
      if (!(var2.distanceToSqr(var3) < 1.0E-12)) {
         double var4 = var0.G == l.b.u ? var0.E * 1.2 : 0.015;
         he.Z("Particle surface queries", 1L);
         v9.d var6 = var1.f(var2.add(0.0, -var4, 0.0), var3.add(0.0, -var4, 0.0));
         if (var6 != null) {
            Vec3 var7 = var6.normal();
            var0.Q = var6.point().x + var7.x * 0.002;
            var0.f = var6.point().y + var4 + var7.y * 0.002;
            var0.X = var6.point().z + var7.z * 0.002;
            double var8 = var0.p * var7.x + var0.j * var7.y + var0.Z * var7.z;
            if (!(var8 >= 0.0)) {
               int var10 = !(var7.y > 0.5) || var0.n != _0.e.j && !(-var8 < 0.55) ? 0 : 1;
               if (var10 != 0) {
                  var0.V = (boolean)1;
                  var0.p = var0.j = var0.Z = 0.0;
                  var0.UP = var0.UP + var0.c * var0.u;
                  var0.H = var0.H + var0.c * var0.x;
                  var0.A = var0.A + var0.c * var0.z;
                  var0.u = var0.x = var0.z = 0.0;
                  if (var0.G == l.b.u) {
                     var0.f = var6.point().y + po.R(var0.E, var0.H, var0.A) + 0.002;
                  }
               } else {
                  double var11 = var0.n == _0.e.K ? 0.38 : 0.08;
                  var0.p = (var0.p - (1.0 + var11) * var8 * var7.x) * 0.72;
                  var0.j = (var0.j - (1.0 + var11) * var8 * var7.y) * 0.72;
                  var0.Z = (var0.Z - (1.0 + var11) * var8 * var7.z) * 0.72;
               }
            }
         }
      }
   }

   private static void u(l.z var0, double var1) {
      var0.k = var0.k + 0.05 * var1 * var0.v;
      double var3 = Math.sin(var0.k * 0.83) * var0.R;
      double var5 = Math.sin(var0.k) * var0.Y;
      double var7 = Math.cos(var0.k * 0.67) * var0.R;
      var0.Q = var0.Q + (var3 - var0.O);
      var0.f = var0.f + (var5 - var0.h);
      var0.X = var0.X + (var7 - var0.C);
      var0.O = var3;
      var0.h = var5;
      var0.C = var7;
   }

   public int W(int var1) {
      if (this.J == null) {
         this.L();
      }

      return this.J.get(var1);
   }

   private void L() {
      this.J = new Int2IntOpenHashMap();

      for (int var1 = 0; var1 < this.C; var1++) {
         this.J.addTo(this.h[var1].N, 1);
      }
   }

   public boolean d() {
      return this.V > 0;
   }

   public boolean J() {
      return this.S;
   }

   public int d() {
      return this.y;
   }

   public static int c(double var0, int var2) {
      long var3 = ((long)(var0 * 1000.0) / 12L + var2 * 14L) % 360L;
      return vv.b((float)var3 / 360.0F, 0.7F, 1.0F);
   }

   public void p(Vec3 var1, float var2, Frustum var3) {
      long var4 = he.s();
      this.y = this.V = 0;
      this.S = (boolean)0;
      byte var6 = 0;

      for (int var7 = 0; var7 < this.C; var7++) {
         l.z var8 = this.h[var7];
         double var9 = var8.c + var2 * 0.05;
         if (!(var9 < 0.0)) {
            double var11 = var8.T + (var8.Q - var8.T) * var2;
            double var13 = var8.g + (var8.f - var8.g) * var2;
            double var15 = var8.b + (var8.X - var8.b) * var2;
            double var17 = var11 - var1.x;
            double var19 = var13 - var1.y;
            double var21 = var15 - var1.z;
            if (!(var17 * var17 + var19 * var19 + var21 * var21 > var8.K * var8.K)) {
               double var23 = Math.clamp(var9 / var8.P, 0.0, 1.0);
               double var25 = var8.E * var8.y.a(var23);
               int var27 = var8.M ? c(hz.Q(), var8.U) : vv.Q(var8.F, var8.B, var23);
               double var28 = var8.s == 1.0 ? var8.L.p(var23) : Math.pow(var8.L.p(var23), var8.s);
               int var30 = var8.o ? var27 & 16777215 | Math.clamp(Math.round((var27 >>> 24) * var28), 0, 255) << 24 : vv.O(var27, var28);
               if (var30 >>> 24 != 0) {
                  double var31 = var8.G == l.b.T ? var8.r * (1.0 - Math.pow(1.0 - var23, 3.0)) : var25;
                  double var33 = var8.G == l.b.T ? var31 + Math.abs(var8.d) : Math.abs(var25) * 5.0;
                  if (var3 == null || var3.isVisible(new AABB(var11 - var33, var13 - var33, var15 - var33, var11 + var33, var13 + var33, var15 + var33))) {
                     l.s var35 = this.X[this.y];
                     var35.a = var11;
                     var35.C = var13;
                     var35.b = var15;
                     var35.z = var25;
                     var35.H = var31;
                     var35.v = var8.UP + var9 * var8.u;
                     var35.i = var8.H + var9 * var8.x;
                     var35.n = var8.A + var9 * var8.z;
                     var35.g = var8.i + var9 * 4.0;
                     var35.y = var8.p;
                     var35.m = var8.j;
                     var35.X = var8.Z;
                     var35.s = var8.d;
                     var35.e = var8.e;
                     var35.L = var8.w;
                     var35.K = var8.J;
                     var35.B = var30;
                     var35.R = var8.m;
                     var35.J = var8.G;
                     var35.f = var8.l;
                     var35.h = var8.W;
                     var35.T = (boolean)(var8.G != l.b.j && var8.G != l.b.P ? 0 : 1);
                     var35.S = var17 * var17 + var19 * var19 + var21 * var21;
                     var6 |= var35.f != _0.v.c ? 1 : 0;
                     this.u[this.y++] = var35;
                     if (var35.R) {
                        this.V++;
                     }

                     this.S = this.S | var35.T;
                  }
               }
            }
         }
      }

      if (var6 != 0) {
         Arrays.sort(this.u, 0, this.y, t);
      }

      he.Z("Particles prepared", this.y);
      he.T("Particles prepare", var4);
   }

   public void z(fz var1, Vec3 var2, float var3, boolean var4) {
      long var5 = he.s();
      l.s var7 = null;

      for (int var8 = 0; var8 < this.y; var8++) {
         l.s var9 = this.u[var8];
         if (!var4 || var9.R) {
            if (var7 != null && (var9.f != _0.v.c || var7.f != _0.v.c) && !b(var7, var9)) {
               var1.I();
            }

            var1.blend(var9.f);
            var1.x(var9.T);
            if (var9.J == l.b.T) {
               var1.O(var9.a, var9.C, var9.b, var9.H, var9.s, var9.B, var9.e, var9.L, var9.K);
            } else if (var9.J == l.b.O) {
               var1.x(var9.a, var9.C, var9.b, var9.z, var9.v, var9.i, var9.n, var9.g, var9.B);
            } else if (var9.J == l.b.u) {
               var1.c(var9.a, var9.C, var9.b, var9.z, var9.v, var9.i, var9.n, var9.B);
            } else {
               var1.i(var9.a, var9.C, var9.b, var9.z, var9.v, var9.B, var9.J, var9.y, var9.m, var9.X, var9.h);
            }

            var7 = var9;
         }
      }

      var1.x(false);
      var1.blend(_0.v.z);
      he.T("Particles", var5);
   }

   private static boolean b(l.s var0, l.s var1) {
      return var0.f == var1.f
         && var0.T == var1.T
         && var0.h == var1.h
         && (var0.J == l.b.O ? 1 : 0) == (var1.J == l.b.O ? 1 : 0)
         && (var0.J == l.b.u ? 1 : 0) == (var1.J == l.b.u ? 1 : 0)
         && (var0.J == l.b.T ? 1 : 0) == (var1.J == l.b.T ? 1 : 0);
   }

   public enum b {
      i,
      P,
      y,
      Y,
      J,
      L,
      K,
      T,
      x,
      u,
      r,
      j,
      O;
   }

   private static final class s {
      double a;
      double C;
      double b;
      double z;
      double H;
      double s;
      double v;
      double i;
      double n;
      double g;
      double y;
      double m;
      double X;
      double S;
      double e;
      double L;
      double K;
      int B;
      boolean R;
      boolean T;
      l.b J;
      _0.j h;
      _0.v f;
   }

   private static final class z {
      double Q;
      double f;
      double X;
      double T;
      double g;
      double b;
      double p;
      double j;
      double Z;
      double c;
      double P;
      double E;
      double UP;
      double u;
      double i;
      double H;
      double A;
      double x;
      double z;
      double a;
      double I;
      double S;
      double q;
      double t;
      double D;
      double r;
      double d;
      double K;
      double Y;
      double R;
      double k;
      double v;
      double O;
      double h;
      double C;
      double e;
      double w;
      double J;
      _0.e n;
      boolean V;
      int F;
      int B;
      int N;
      int U;
      boolean m;
      boolean o;
      boolean M;
      double s;
      l.b G;
      _0.j W;
      pr.b L;
      pr.g y;
      _0.v l;
   }
}
