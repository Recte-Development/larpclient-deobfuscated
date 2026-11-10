package com.larpclient;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class c3 {
   private static final int k = 2048;
   private final c3.h[] z = new c3.h[2048];
   private final Random E = new Random();
   private Int2IntOpenHashMap q;
   private int Y;
   private int v;
   private int D;
   private int J;
   private final c3.b[] i = new c3.b[2048];
   private final c3.b[] o = new c3.b[2048];
   private boolean C;
   private static final Comparator<c3.b> H = Comparator.<c3.b>comparingDouble(var0 -> var0.D).reversed();
   public int a = 2048;
   public double x = 64.0;

   public c3() {
      for (int var1 = 0; var1 < 2048; var1++) {
         this.z[var1] = new c3.h();
         this.i[var1] = new c3.b();
      }

      com.larpclient.h.A("Particles", "Emitter engine initialized; capacity={}", 2048);
   }

   public int G() {
      return this.Y;
   }

   public void J() {
      this.Y = 0;
      if (this.q != null) {
         this.q.clear();
      }

      this.v = 0;
      this.D = this.J = 0;
      this.C = (boolean)0;
   }

   public void t(int var1) {
      for (int var2 = this.Y - 1; var2 >= 0; var2--) {
         if (this.z[var2].J == var1) {
            this.e(var2);
         }
      }
   }

   private void e(int var1) {
      c3.h var2 = this.z[var1];
      if (this.q != null) {
         int var3 = this.q.get(var2.J) - 1;
         if (var3 == 0) {
            this.q.remove(var2.J);
         } else {
            this.q.put(var2.J, var3);
         }
      }

      if (var2.T) {
         this.v--;
      }

      this.z[var1] = this.z[--this.Y];
      this.z[this.Y] = var2;
   }

   public boolean E() {
      return this.v > 0;
   }

   private double p(double var1, double var3) {
      return var1 + this.E.nextDouble() * (var3 - var1);
   }

   public void S(Vec3 var1, gl var2) {
      int var3 = Math.clamp(this.a, 0, 2048);
      int var4 = var2.L < 2048 ? this.E(var2.F) : 0;
      int var5 = Math.clamp(var2.L, 0, 2048);
      int var6 = var2.y ? this.E(var2.F) : 0;
      double var7 = Math.cos(var2.v * 0.05);
      double var9 = Math.sin(var2.v * 0.05);

      for (int var11 = 0; var11 < Math.clamp(var2.u, 0, 2048) && this.Y < var3 && var4++ < var5; var11++) {
         c3.h var12 = this.z[this.Y++];
         var12.p = var6++;
         double var13 = this.p(0.0, Math.PI * 2);
         double var15 = Math.sqrt(this.E.nextDouble());
         double var17 = this.p(-0.5, 0.5);
         double var19 = 0.0;
         double var21 = 0.0;
         switch (var2.x) {
            case R:
               var19 = this.p(-0.5, 0.5);
               var21 = this.p(-0.5, 0.5);
               break;
            case C:
               double var23 = this.p(-1.0, 1.0);
               double var25 = Math.cbrt(this.E.nextDouble()) * 0.5;
               double var27 = Math.sqrt(1.0 - var23 * var23) * var25;
               var19 = Math.cos(var13) * var27;
               var21 = Math.sin(var13) * var27;
               var17 = var23 * var25;
               break;
            case l:
               var19 = Math.cos(var13) * var15 * 0.5;
               var21 = Math.sin(var13) * var15 * 0.5;
               break;
            case O:
               var19 = Math.cos(var13) * 0.5;
               var21 = Math.sin(var13) * 0.5;
               var17 = 0.0;
               break;
            case B:
               var17 = 0.0;
         }

         var12.l = var12._g = var1.x + var19 * var2.D;
         var12.V = var12.Z = var1.y + var17 * var2.z;
         var12.c = var12.h = var1.z + var21 * var2.P;
         var12.u = this.p(-1.0, 1.0) * var2.s * var2.V + var2.H + var2.Y;
         var12.K = this.p(-1.0, 1.0) * var2.s * var2.V + var2.g;
         var12.G = this.p(var2.y ? -1.0 : -0.4, 1.0) * var2.s + var2.C + var2.r;
         var12.v = -Math.max(0.0, var2.T);
         var12.q = Math.max(0.05, this.p(var2.O, var2.Q));
         var12.d = Math.max(0.001, this.p(var2.n, var2.f));
         if (var2.t != gl.g.z && Double.isFinite(var2.o)) {
            var12.V = var12.Z = Math.max(var12.V, var2.o + var12.d * 1.2 + 0.002);
         }

         var12.Y = var13;
         var12.D = this.p(Double.isFinite(var2.I) ? var2.I : -var2.m, var2.m);
         var12.E = var2.y;
         var12.C = var2.a;
         var12.j = var2.l;
         var12.W = this.p(0.0, Math.PI * 2);
         var12.w = var12.W;
         var12.B = var2.G;
         var12.e = var2.b;
         var12.F = (Math.PI * 2) / Math.max(0.05, var2.J);
         var12.U = var12.m = var12.o = 0.0;
         if (var12.B != 0.0 || var12.e != 0.0) {
            var12.U = Math.sin(var12.w * 0.83) * var12.e;
            var12.m = Math.sin(var12.w) * var12.B;
            var12.o = Math.cos(var12.w * 0.67) * var12.e;
            var12.l = var12._g = var12._g + var12.U;
            var12.V = var12.Z = var12.Z + var12.m;
            var12.c = var12.h = var12.h + var12.o;
         }

         var12.s = var2.W;
         var12.P = Math.exp(-Math.max(0.0, var2.K) * 0.05);
         var12.X = var2.p;
         var12.a = var2.v;
         var12.t = var7;
         var12.A = var9;
         var12.O = var2.M;
         var12.r = var2.e;
         var12.z = var2.i;
         var12.k = var2.B == c3.a.R ? var2.j : uh.q(var2.j, var2.Z, this.E.nextDouble() * 0.35);
         var12.R = var2.Z;
         var12.T = var2.R;
         if (var12.T) {
            this.v++;
         }

         var12.x = var2.B;
         var12.y = var2.t;
         var12.Q = (boolean)0;
         var12.S = var2.c.x;
         var12.N = var2.c.y;
         var12.b = var2.c.z;
         var12.f = var12.H = var12.M = var12.n = 0.0;
         if (var12.x == c3.a.R || var12.x == c3.a.T) {
            var12.f = this.p(0.0, Math.PI * 2);
            var12.H = this.p(0.0, Math.PI * 2);
            var12.M = this.p(-2.4, 2.4);
            var12.n = this.p(-1.8, 1.8);
         }

         var12.I = var2.d;
         var12.L = var2.w;
         var12.i = var2.X;
         var12.J = var2.F;
         if (this.q != null) {
            this.q.addTo(var12.J, 1);
         }

         var12.g = Double.isFinite(var2.E) ? Math.max(0.0, var2.E) : Math.max(0.0, this.x);
      }
   }

   public void t(Vec3 var1, c3.a var2, int var3, double var4, double var6, int var8, int var9, boolean var10, double var11) {
      gl var13 = new gl();
      var13.B = var2;
      var13.u = var3;
      var13.n = var13.f = var4;
      var13.O = var13.Q = var6;
      var13.j = var8;
      var13.Z = var9;
      var13.R = var10;
      var13.w = var4 * var11;
      this.S(var1, var13);
   }

   public void Z() {
      this.V(1.0);
   }

   public void V(double var1) {
      this.C(var1, null);
   }

   public void C(double var1, b5.c var3) {
      if (Double.isFinite(var1) && !(var1 < 0.0)) {
         int var4 = 0;

         while (var4 < this.Y) {
            c3.h var5 = this.z[var4];
            var5.v += 0.05;
            if (var5.v >= var5.q) {
               this.e(var4);
            } else {
               var5._g = var5.l;
               var5.Z = var5.V;
               var5.h = var5.c;
               if (var5.v >= 0.0 && var5.x != c3.a.V && !var5.Q) {
                  if (var5.E) {
                     var5.l = var5.l + var5.u * 0.05 * var1;
                     var5.V = var5.V + var5.G * 0.05 * var1;
                     var5.c = var5.c + var5.K * 0.05 * var1;
                     var5.u = var5.u * var5.P;
                     var5.K = var5.K * var5.P;
                     var5.G = (var5.G - var5.s * 0.05) * var5.P;
                     var4++;
                     continue;
                  }

                  double var6 = var5.W + var5.v * 3.0;
                  double var8 = var5.u;
                  double var10 = var5.K;
                  var5.u = (var8 * var5.t - var10 * var5.A) * var5.P;
                  var5.K = (var8 * var5.A + var10 * var5.t) * var5.P;
                  var5.G = (var5.G - var5.s * 0.05) * var5.P;
                  if (var5.X != 0.0 || var5.u != 0.0 || var5.G != 0.0 || var5.K != 0.0) {
                     var5.l = var5.l + (var5.u + Math.sin(var6) * var5.X) * 0.05 * var1;
                     var5.V = var5.V + (var5.G + Math.cos(var6 * 1.3) * var5.X * 0.4) * 0.05 * var1;
                     var5.c = var5.c + (var5.K + Math.cos(var6 * 0.83) * var5.X) * 0.05 * var1;
                  }

                  if (var5.B != 0.0 || var5.e != 0.0) {
                     d(var5, var1);
                  }

                  if (var3 != null && var5.y != gl.g.z) {
                     z(var5, var3);
                  }
               }

               var4++;
            }
         }
      } else {
         throw new IllegalArgumentException("Invalid motion speed");
      }
   }

   private static void z(c3.h var0, b5.c var1) {
      Vec3 var2 = new Vec3(var0._g, var0.Z, var0.h);
      Vec3 var3 = new Vec3(var0.l, var0.V, var0.c);
      if (!(var2.distanceToSqr(var3) < 1.0E-12)) {
         double var4 = var0.x == c3.a.T ? var0.d * 1.2 : 0.015;
         zj.K("Particle surface queries", 1L);
         b5.e var6 = var1.p(var2.add(0.0, -var4, 0.0), var3.add(0.0, -var4, 0.0));
         if (var6 != null) {
            Vec3 var7 = var6.normal();
            var0.l = var6.point().x + var7.x * 0.002;
            var0.V = var6.point().y + var4 + var7.y * 0.002;
            var0.c = var6.point().z + var7.z * 0.002;
            double var8 = var0.u * var7.x + var0.G * var7.y + var0.K * var7.z;
            if (!(var8 >= 0.0)) {
               int var10 = !(var7.y > 0.5) || var0.y != gl.g.K && !(-var8 < 0.55) ? 0 : 1;
               if (var10 != 0) {
                  var0.Q = (boolean)1;
                  var0.u = var0.G = var0.K = 0.0;
                  var0.Y = var0.Y + var0.v * var0.D;
                  var0.f = var0.f + var0.v * var0.M;
                  var0.H = var0.H + var0.v * var0.n;
                  var0.D = var0.M = var0.n = 0.0;
                  if (var0.x == c3.a.T) {
                     var0.V = var6.point().y + ke.F(var0.d, var0.f, var0.H) + 0.002;
                  }
               } else {
                  double var11 = var0.y == gl.g.e ? 0.38 : 0.08;
                  var0.u = (var0.u - (1.0 + var11) * var8 * var7.x) * 0.72;
                  var0.G = (var0.G - (1.0 + var11) * var8 * var7.y) * 0.72;
                  var0.K = (var0.K - (1.0 + var11) * var8 * var7.z) * 0.72;
               }
            }
         }
      }
   }

   private static void d(c3.h var0, double var1) {
      var0.w = var0.w + 0.05 * var1 * var0.F;
      double var3 = Math.sin(var0.w * 0.83) * var0.e;
      double var5 = Math.sin(var0.w) * var0.B;
      double var7 = Math.cos(var0.w * 0.67) * var0.e;
      var0.l = var0.l + (var3 - var0.U);
      var0.V = var0.V + (var5 - var0.m);
      var0.c = var0.c + (var7 - var0.o);
      var0.U = var3;
      var0.m = var5;
      var0.o = var7;
   }

   public int E(int var1) {
      if (this.q == null) {
         this.q();
      }

      return this.q.get(var1);
   }

   private void q() {
      this.q = new Int2IntOpenHashMap();

      for (int var1 = 0; var1 < this.Y; var1++) {
         this.q.addTo(this.z[var1].J, 1);
      }
   }

   public boolean o() {
      return this.J > 0;
   }

   public boolean L() {
      return this.C;
   }

   public int w() {
      return this.D;
   }

   public static int u(double var0, int var2) {
      long var3 = ((long)(var0 * 1000.0) / 12L + var2 * 14L) % 360L;
      return uh.n((float)var3 / 360.0F, 0.7F, 1.0F);
   }

   public void B(Vec3 var1, float var2, Frustum var3) {
      long var4 = zj.m();
      this.D = this.J = 0;
      this.C = (boolean)0;
      byte var6 = 0;

      for (int var7 = 0; var7 < this.Y; var7++) {
         c3.h var8 = this.z[var7];
         double var9 = var8.v + var2 * 0.05;
         if (!(var9 < 0.0)) {
            double var11 = var8._g + (var8.l - var8._g) * var2;
            double var13 = var8.Z + (var8.V - var8.Z) * var2;
            double var15 = var8.h + (var8.c - var8.h) * var2;
            double var17 = var11 - var1.x;
            double var19 = var13 - var1.y;
            double var21 = var15 - var1.z;
            if (!(var17 * var17 + var19 * var19 + var21 * var21 > var8.g * var8.g)) {
               double var23 = Math.clamp(var9 / var8.q, 0.0, 1.0);
               double var25 = var8.d * var8.r.q(var23);
               int var27 = var8.C ? u(cf.F(), var8.p) : uh.q(var8.k, var8.R, var23);
               double var28 = var8.j == 1.0 ? var8.O.x(var23) : Math.pow(var8.O.x(var23), var8.j);
               int var30 = var8.E ? var27 & 16777215 | Math.clamp(Math.round((var27 >>> 24) * var28), 0, 255) << 24 : uh.F(var27, var28);
               if (var30 >>> 24 != 0) {
                  double var31 = var8.x == c3.a.V ? var8.L * (1.0 - Math.pow(1.0 - var23, 3.0)) : var25;
                  double var33 = var8.x == c3.a.V ? var31 + Math.abs(var8.i) : Math.abs(var25) * 5.0;
                  if (var3 == null || var3.isVisible(new AABB(var11 - var33, var13 - var33, var15 - var33, var11 + var33, var13 + var33, var15 + var33))) {
                     c3.b var35 = this.i[this.D];
                     var35.C = var11;
                     var35.r = var13;
                     var35.P = var15;
                     var35.S = var25;
                     var35.R = var31;
                     var35.Y = var8.Y + var9 * var8.D;
                     var35.p = var8.f + var9 * var8.M;
                     var35.J = var8.H + var9 * var8.n;
                     var35.x = var8.W + var9 * 4.0;
                     var35.w = var8.u;
                     var35.I = var8.G;
                     var35.Q = var8.K;
                     var35.E = var8.i;
                     var35.a = var8.S;
                     var35.k = var8.N;
                     var35.z = var8.b;
                     var35.v = var30;
                     var35.i = var8.T;
                     var35.O = var8.x;
                     var35.d = var8.z;
                     var35.h = var8.I;
                     var35.V = (boolean)(var8.x != c3.a.C && var8.x != c3.a.Z ? 0 : 1);
                     var35.D = var17 * var17 + var19 * var19 + var21 * var21;
                     var6 |= var35.d != gl.c.E ? 1 : 0;
                     this.o[this.D++] = var35;
                     if (var35.i) {
                        this.J++;
                     }

                     this.C = this.C | var35.V;
                  }
               }
            }
         }
      }

      if (var6 != 0) {
         Arrays.sort(this.o, 0, this.D, H);
      }

      zj.K("Particles prepared", this.D);
      zj.l("Particles prepare", var4);
   }

   public void G(kj var1, Vec3 var2, float var3, boolean var4) {
      long var5 = zj.m();
      c3.b var7 = null;

      for (int var8 = 0; var8 < this.D; var8++) {
         c3.b var9 = this.o[var8];
         if (!var4 || var9.i) {
            if (var7 != null && (var9.d != gl.c.E || var7.d != gl.c.E) && !X(var7, var9)) {
               var1.r();
            }

            var1.blend(var9.d);
            var1.j(var9.V);
            if (var9.O == c3.a.V) {
               var1.Q(var9.C, var9.r, var9.P, var9.R, var9.E, var9.v, var9.a, var9.k, var9.z);
            } else if (var9.O == c3.a.R) {
               var1.r(var9.C, var9.r, var9.P, var9.S, var9.Y, var9.p, var9.J, var9.x, var9.v);
            } else if (var9.O == c3.a.T) {
               var1.b(var9.C, var9.r, var9.P, var9.S, var9.Y, var9.p, var9.J, var9.v);
            } else {
               var1.O(var9.C, var9.r, var9.P, var9.S, var9.Y, var9.v, var9.O, var9.w, var9.I, var9.Q, var9.h);
            }

            var7 = var9;
         }
      }

      var1.j(false);
      var1.blend(gl.c.K);
      zj.l("Particles", var5);
   }

   private static boolean X(c3.b var0, c3.b var1) {
      return var0.d == var1.d
         && var0.V == var1.V
         && var0.h == var1.h
         && (var0.O == c3.a.R ? 1 : 0) == (var1.O == c3.a.R ? 1 : 0)
         && (var0.O == c3.a.T ? 1 : 0) == (var1.O == c3.a.T ? 1 : 0)
         && (var0.O == c3.a.V ? 1 : 0) == (var1.O == c3.a.V ? 1 : 0);
   }

   public enum a {
      P,
      Z,
      l,
      e,
      y,
      Q,
      v,
      V,
      n,
      T,
      q,
      C,
      R;
   }

   private static final class b {
      double C;
      double r;
      double P;
      double S;
      double R;
      double E;
      double Y;
      double p;
      double J;
      double x;
      double w;
      double I;
      double Q;
      double D;
      double a;
      double k;
      double z;
      int v;
      boolean i;
      boolean V;
      c3.a O;
      gl.k h;
      gl.c d;
   }

   private static final class h {
      double l;
      double V;
      double c;
      double _g;
      double Z;
      double h;
      double u;
      double G;
      double K;
      double v;
      double q;
      double d;
      double Y;
      double D;
      double W;
      double f;
      double H;
      double M;
      double n;
      double s;
      double P;
      double X;
      double a;
      double t;
      double A;
      double L;
      double i;
      double g;
      double B;
      double e;
      double w;
      double F;
      double U;
      double m;
      double o;
      double S;
      double N;
      double b;
      gl.g y;
      boolean Q;
      int k;
      int R;
      int J;
      int p;
      boolean T;
      boolean E;
      boolean C;
      double j;
      c3.a x;
      gl.k I;
      st.z O;
      st.c r;
      gl.c z;
   }
}
