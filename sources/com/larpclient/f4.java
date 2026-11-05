package com.larpclient;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class f4 {
   private static final int N = 2048;
   private final f4.b[] m = new f4.b[2048];
   private final Random z = new Random();
   private Int2IntOpenHashMap g;
   private int i;
   private int W;
   private int k;
   private int M;
   private final f4.m[] n = new f4.m[2048];
   private final f4.m[] V = new f4.m[2048];
   private boolean c;
   private static final Comparator<f4.m> X = Comparator.<f4.m>comparingDouble(var0 -> var0.t).reversed();
   public int P = 2048;
   public double O = 64.0;

   public f4() {
      for (int var1 = 0; var1 < 2048; var1++) {
         this.m[var1] = new f4.b();
         this.n[var1] = new f4.m();
      }

      r4.V("Particles", "Emitter engine initialized; capacity={}", 2048);
   }

   public int j() {
      return this.i;
   }

   public void U() {
      this.i = 0;
      if (this.g != null) {
         this.g.clear();
      }

      this.W = 0;
      this.k = this.M = 0;
      this.c = (boolean)0;
   }

   public void A(int var1) {
      for (int var2 = this.i - 1; var2 >= 0; var2--) {
         if (this.m[var2].J == var1) {
            this.k(var2);
         }
      }
   }

   private void k(int var1) {
      f4.b var2 = this.m[var1];
      if (this.g != null) {
         int var3 = this.g.get(var2.J) - 1;
         if (var3 == 0) {
            this.g.remove(var2.J);
         } else {
            this.g.put(var2.J, var3);
         }
      }

      if (var2.x) {
         this.W--;
      }

      this.m[var1] = this.m[--this.i];
      this.m[this.i] = var2;
   }

   public boolean F() {
      return this.W > 0;
   }

   private double K(double var1, double var3) {
      return var1 + this.z.nextDouble() * (var3 - var1);
   }

   public void p(Vec3 var1, fk var2) {
      int var3 = Math.clamp(this.P, 0, 2048);
      int var4 = var2.p < 2048 ? this.d(var2.g) : 0;
      int var5 = Math.clamp(var2.p, 0, 2048);
      int var6 = var2.M ? this.d(var2.g) : 0;
      double var7 = Math.cos(var2.n * 0.05);
      double var9 = Math.sin(var2.n * 0.05);

      for (int var11 = 0; var11 < Math.clamp(var2.t, 0, 2048) && this.i < var3 && var4++ < var5; var11++) {
         f4.b var12 = this.m[this.i++];
         var12.F = var6++;
         double var13 = this.K(0.0, Math.PI * 2);
         double var15 = Math.sqrt(this.z.nextDouble());
         double var17 = this.K(-0.5, 0.5);
         double var19 = 0.0;
         double var21 = 0.0;
         switch (var2.m) {
            case D:
               var19 = this.K(-0.5, 0.5);
               var21 = this.K(-0.5, 0.5);
               break;
            case u:
               double var23 = this.K(-1.0, 1.0);
               double var25 = Math.cbrt(this.z.nextDouble()) * 0.5;
               double var27 = Math.sqrt(1.0 - var23 * var23) * var25;
               var19 = Math.cos(var13) * var27;
               var21 = Math.sin(var13) * var27;
               var17 = var23 * var25;
               break;
            case f:
               var19 = Math.cos(var13) * var15 * 0.5;
               var21 = Math.sin(var13) * var15 * 0.5;
               break;
            case r:
               var19 = Math.cos(var13) * 0.5;
               var21 = Math.sin(var13) * 0.5;
               var17 = 0.0;
               break;
            case P:
               var17 = 0.0;
         }

         var12.u = var12.E = var1.x + var19 * var2.E;
         var12.s = var12.d = var1.y + var17 * var2.r;
         var12.i = var12.t = var1.z + var21 * var2.j;
         var12.B = this.K(-1.0, 1.0) * var2.e * var2.F + var2.L + var2.h;
         var12.z = this.K(-1.0, 1.0) * var2.e * var2.F + var2.W;
         var12.p = this.K(var2.M ? -1.0 : -0.4, 1.0) * var2.e + var2.k + var2.x;
         var12.c = -Math.max(0.0, var2.c);
         var12.O = Math.max(0.05, this.K(var2.Y, var2.d));
         var12.q = Math.max(0.001, this.K(var2.V, var2.u));
         if (var2.f != fk.q.k && Double.isFinite(var2.q)) {
            var12.s = var12.d = Math.max(var12.s, var2.q + var12.q * 1.2 + 0.002);
         }

         var12.y = var13;
         var12.Q = this.K(Double.isFinite(var2.w) ? var2.w : -var2.y, var2.y);
         var12.h = var2.M;
         var12.K = var2.i;
         var12.S = var2.Z;
         var12.m = this.K(0.0, Math.PI * 2);
         var12.L = var12.m;
         var12.N = var2.K;
         var12.M = var2.X;
         var12.G = (Math.PI * 2) / Math.max(0.05, var2.T);
         var12.w = var12.Y = var12.o = 0.0;
         if (var12.N != 0.0 || var12.M != 0.0) {
            var12.w = Math.sin(var12.L * 0.83) * var12.M;
            var12.Y = Math.sin(var12.L) * var12.N;
            var12.o = Math.cos(var12.L * 0.67) * var12.M;
            var12.u = var12.E = var12.E + var12.w;
            var12.s = var12.d = var12.d + var12.Y;
            var12.i = var12.t = var12.t + var12.o;
         }

         var12.A = var2.H;
         var12.a = Math.exp(-Math.max(0.0, var2.b) * 0.05);
         var12.e = var2.s;
         var12.P = var2.n;
         var12.q7 = var7;
         var12.W = var9;
         var12.g = var2.N;
         var12.n = var2.U;
         var12.b = var2.C;
         var12.l = var2.D == f4.a.h ? var2.B : oh.j(var2.B, var2.v, this.z.nextDouble() * 0.35);
         var12.D = var2.v;
         var12.x = var2.G;
         if (var12.x) {
            this.W++;
         }

         var12.Z = var2.D;
         var12.H = var2.f;
         var12.V = (boolean)0;
         var12.U = var2.S.x;
         var12.k = var2.S.y;
         var12.R = var2.S.z;
         var12.C = var12.r = var12.T = var12.X = 0.0;
         if (var12.Z == f4.a.h || var12.Z == f4.a.y) {
            var12.C = this.K(0.0, Math.PI * 2);
            var12.r = this.K(0.0, Math.PI * 2);
            var12.T = this.K(-2.4, 2.4);
            var12.X = this.K(-1.8, 1.8);
         }

         var12.v = var2.I;
         var12.j = var2.o;
         var12.I = var2.z;
         var12.J = var2.g;
         if (this.g != null) {
            this.g.addTo(var12.J, 1);
         }

         var12.f = Double.isFinite(var2.Q) ? Math.max(0.0, var2.Q) : Math.max(0.0, this.O);
      }
   }

   public void M(Vec3 var1, f4.a var2, int var3, double var4, double var6, int var8, int var9, boolean var10, double var11) {
      fk var13 = new fk();
      var13.D = var2;
      var13.t = var3;
      var13.V = var13.u = var4;
      var13.Y = var13.d = var6;
      var13.B = var8;
      var13.v = var9;
      var13.G = var10;
      var13.o = var4 * var11;
      this.p(var1, var13);
   }

   public void V() {
      this.W(1.0);
   }

   public void W(double var1) {
      this.m(var1, null);
   }

   public void m(double var1, et.l var3) {
      if (Double.isFinite(var1) && !(var1 < 0.0)) {
         int var4 = 0;

         while (var4 < this.i) {
            f4.b var5 = this.m[var4];
            var5.c += 0.05;
            if (var5.c >= var5.O) {
               this.k(var4);
            } else {
               var5.E = var5.u;
               var5.d = var5.s;
               var5.t = var5.i;
               if (var5.c >= 0.0 && var5.Z != f4.a.U && !var5.V) {
                  if (var5.h) {
                     var5.u = var5.u + var5.B * 0.05 * var1;
                     var5.s = var5.s + var5.p * 0.05 * var1;
                     var5.i = var5.i + var5.z * 0.05 * var1;
                     var5.B = var5.B * var5.a;
                     var5.z = var5.z * var5.a;
                     var5.p = (var5.p - var5.A * 0.05) * var5.a;
                     var4++;
                     continue;
                  }

                  double var6 = var5.m + var5.c * 3.0;
                  double var8 = var5.B;
                  double var10 = var5.z;
                  var5.B = (var8 * var5.q7 - var10 * var5.W) * var5.a;
                  var5.z = (var8 * var5.W + var10 * var5.q7) * var5.a;
                  var5.p = (var5.p - var5.A * 0.05) * var5.a;
                  if (var5.e != 0.0 || var5.B != 0.0 || var5.p != 0.0 || var5.z != 0.0) {
                     var5.u = var5.u + (var5.B + Math.sin(var6) * var5.e) * 0.05 * var1;
                     var5.s = var5.s + (var5.p + Math.cos(var6 * 1.3) * var5.e * 0.4) * 0.05 * var1;
                     var5.i = var5.i + (var5.z + Math.cos(var6 * 0.83) * var5.e) * 0.05 * var1;
                  }

                  if (var5.N != 0.0 || var5.M != 0.0) {
                     W(var5, var1);
                  }

                  if (var3 != null && var5.H != fk.q.k) {
                     U(var5, var3);
                  }
               }

               var4++;
            }
         }
      } else {
         throw new IllegalArgumentException("Invalid motion speed");
      }
   }

   private static void U(f4.b var0, et.l var1) {
      Vec3 var2 = new Vec3(var0.E, var0.d, var0.t);
      Vec3 var3 = new Vec3(var0.u, var0.s, var0.i);
      if (!(var2.distanceToSqr(var3) < 1.0E-12)) {
         double var4 = var0.Z == f4.a.y ? var0.q * 1.2 : 0.015;
         h_.A("Particle surface queries", 1L);
         et.d var6 = var1.J(var2.add(0.0, -var4, 0.0), var3.add(0.0, -var4, 0.0));
         if (var6 != null) {
            Vec3 var7 = var6.normal();
            var0.u = var6.point().x + var7.x * 0.002;
            var0.s = var6.point().y + var4 + var7.y * 0.002;
            var0.i = var6.point().z + var7.z * 0.002;
            double var8 = var0.B * var7.x + var0.p * var7.y + var0.z * var7.z;
            if (!(var8 >= 0.0)) {
               int var10 = !(var7.y > 0.5) || var0.H != fk.q.t && !(-var8 < 0.55) ? 0 : 1;
               if (var10 != 0) {
                  var0.V = (boolean)1;
                  var0.B = var0.p = var0.z = 0.0;
                  var0.y = var0.y + var0.c * var0.Q;
                  var0.C = var0.C + var0.c * var0.T;
                  var0.r = var0.r + var0.c * var0.X;
                  var0.Q = var0.T = var0.X = 0.0;
                  if (var0.Z == f4.a.y) {
                     var0.s = var6.point().y + x.u(var0.q, var0.C, var0.r) + 0.002;
                  }
               } else {
                  double var11 = var0.H == fk.q.j ? 0.38 : 0.08;
                  var0.B = (var0.B - (1.0 + var11) * var8 * var7.x) * 0.72;
                  var0.p = (var0.p - (1.0 + var11) * var8 * var7.y) * 0.72;
                  var0.z = (var0.z - (1.0 + var11) * var8 * var7.z) * 0.72;
               }
            }
         }
      }
   }

   private static void W(f4.b var0, double var1) {
      var0.L = var0.L + 0.05 * var1 * var0.G;
      double var3 = Math.sin(var0.L * 0.83) * var0.M;
      double var5 = Math.sin(var0.L) * var0.N;
      double var7 = Math.cos(var0.L * 0.67) * var0.M;
      var0.u = var0.u + (var3 - var0.w);
      var0.s = var0.s + (var5 - var0.Y);
      var0.i = var0.i + (var7 - var0.o);
      var0.w = var3;
      var0.Y = var5;
      var0.o = var7;
   }

   public int d(int var1) {
      if (this.g == null) {
         this.I();
      }

      return this.g.get(var1);
   }

   private void I() {
      this.g = new Int2IntOpenHashMap();

      for (int var1 = 0; var1 < this.i; var1++) {
         this.g.addTo(this.m[var1].J, 1);
      }
   }

   public boolean t() {
      return this.M > 0;
   }

   public boolean C() {
      return this.c;
   }

   public int s() {
      return this.k;
   }

   public static int S(double var0, int var2) {
      long var3 = ((long)(var0 * 1000.0) / 12L + var2 * 14L) % 360L;
      return oh.O((float)var3 / 360.0F, 0.7F, 1.0F);
   }

   public void j(Vec3 var1, float var2, Frustum var3) {
      this.I(var1, var2, var3, 1.0, 1.0);
   }

   public void I(Vec3 var1, float var2, Frustum var3, double var4, double var6) {
      long var8 = h_.U();
      this.k = this.M = 0;
      this.c = (boolean)0;
      if (var6 <= 0.0) {
         h_.n("Particles prepare", var8);
      } else {
         byte var10 = 0;

         for (int var11 = 0; var11 < this.i; var11++) {
            f4.b var12 = this.m[var11];
            double var13 = var12.c + var2 * 0.05;
            if (!(var13 < 0.0)) {
               double var15 = var12.E + (var12.u - var12.E) * var2;
               double var17 = var12.d + (var12.s - var12.d) * var2;
               double var19 = var12.t + (var12.i - var12.t) * var2;
               double var21 = var15 - var1.x;
               double var23 = var17 - var1.y;
               double var25 = var19 - var1.z;
               if (!(var21 * var21 + var23 * var23 + var25 * var25 > var12.f * var12.f)) {
                  double var27 = Math.clamp(var13 / var12.O, 0.0, 1.0);
                  double var29 = var12.q * var12.n.E(var27);
                  int var31 = var12.K ? S(ea.y(), var12.F) : oh.j(var12.l, var12.D, var27);
                  double var32 = var12.S == 1.0 ? var12.g.v(var27) : Math.pow(var12.g.v(var27), var12.S);
                  if (var6 < 1.0) {
                     var32 *= var6;
                  }

                  if (var4 < 1.0) {
                     double var34 = var12.f * var12.f;
                     var32 *= yc.H((var34 - var21 * var21 - var23 * var23 - var25 * var25) / Math.max(0.001, var34 * (1.0 - var4 * var4)));
                  }

                  int var40 = var12.h ? var31 & 16777215 | Math.clamp(Math.round((var31 >>> 24) * var32), 0, 255) << 24 : oh.S(var31, var32);
                  if (var40 >>> 24 != 0) {
                     double var35 = var12.Z == f4.a.U ? var12.j * (1.0 - Math.pow(1.0 - var27, 3.0)) : var29;
                     double var37 = var12.Z == f4.a.U ? var35 + Math.abs(var12.I) : Math.abs(var29) * 5.0;
                     if (var3 == null || var3.isVisible(new AABB(var15 - var37, var17 - var37, var19 - var37, var15 + var37, var17 + var37, var19 + var37))) {
                        f4.m var39 = this.n[this.k];
                        var39.i = var15;
                        var39.d = var17;
                        var39.G = var19;
                        var39.S = var29;
                        var39.K = var35;
                        var39.R = var12.y + var13 * var12.Q;
                        var39.p = var12.C + var13 * var12.T;
                        var39.L = var12.r + var13 * var12.X;
                        var39.z = var12.m + var13 * 4.0;
                        var39.Q = var12.B;
                        var39.Y = var12.p;
                        var39.e = var12.z;
                        var39.h = var12.I;
                        var39.X = var12.U;
                        var39.o = var12.k;
                        var39.T = var12.R;
                        var39.c = var40;
                        var39.O = var12.x;
                        var39.g = var12.Z;
                        var39.n = var12.b;
                        var39.W = var12.v;
                        var39.a = (boolean)(var12.Z != f4.a.F && var12.Z != f4.a.u ? 0 : 1);
                        var39.t = var21 * var21 + var23 * var23 + var25 * var25;
                        var10 |= var39.n != fk.h.u ? 1 : 0;
                        this.V[this.k++] = var39;
                        if (var39.O) {
                           this.M++;
                        }

                        this.c = this.c | var39.a;
                     }
                  }
               }
            }
         }

         if (var10 != 0) {
            Arrays.sort(this.V, 0, this.k, X);
         }

         h_.A("Particles prepared", this.k);
         h_.n("Particles prepare", var8);
      }
   }

   public void Y(r6 var1, Vec3 var2, float var3, boolean var4) {
      long var5 = h_.U();
      f4.m var7 = null;

      for (int var8 = 0; var8 < this.k; var8++) {
         f4.m var9 = this.V[var8];
         if (!var4 || var9.O) {
            if (var7 != null && (var9.n != fk.h.u || var7.n != fk.h.u) && !y(var7, var9)) {
               var1.m();
            }

            var1.blend(var9.n);
            var1.T(var9.a);
            if (var9.g == f4.a.U) {
               var1.e(var9.i, var9.d, var9.G, var9.K, var9.h, var9.c, var9.X, var9.o, var9.T);
            } else if (var9.g == f4.a.h) {
               var1.h(var9.i, var9.d, var9.G, var9.S, var9.R, var9.p, var9.L, var9.z, var9.c);
            } else if (var9.g == f4.a.y) {
               var1.r(var9.i, var9.d, var9.G, var9.S, var9.R, var9.p, var9.L, var9.c);
            } else {
               var1.q(var9.i, var9.d, var9.G, var9.S, var9.R, var9.c, var9.g, var9.Q, var9.Y, var9.e, var9.W);
            }

            var7 = var9;
         }
      }

      var1.T(false);
      var1.blend(fk.h.z);
      h_.n("Particles", var5);
   }

   private static boolean y(f4.m var0, f4.m var1) {
      return var0.n == var1.n
         && var0.a == var1.a
         && var0.W == var1.W
         && (var0.g == f4.a.h ? 1 : 0) == (var1.g == f4.a.h ? 1 : 0)
         && (var0.g == f4.a.y ? 1 : 0) == (var1.g == f4.a.y ? 1 : 0)
         && (var0.g == f4.a.U ? 1 : 0) == (var1.g == f4.a.U ? 1 : 0);
   }

   public enum a {
      d,
      u,
      D,
      g,
      S,
      b,
      o,
      U,
      n,
      y,
      x,
      F,
      h;
   }

   private static final class b {
      double u;
      double s;
      double i;
      double E;
      double d;
      double t;
      double B;
      double p;
      double z;
      double c;
      double O;
      double q;
      double y;
      double Q;
      double m;
      double C;
      double r;
      double T;
      double X;
      double A;
      double a;
      double e;
      double P;
      double q7;
      double W;
      double j;
      double I;
      double f;
      double N;
      double M;
      double L;
      double G;
      double w;
      double Y;
      double o;
      double U;
      double k;
      double R;
      fk.q H;
      boolean V;
      int l;
      int D;
      int J;
      int F;
      boolean x;
      boolean h;
      boolean K;
      double S;
      f4.a Z;
      fk.w v;
      yc.d g;
      yc.y n;
      fk.h b;
   }

   private static final class m {
      double i;
      double d;
      double G;
      double S;
      double K;
      double h;
      double R;
      double p;
      double L;
      double z;
      double Q;
      double Y;
      double e;
      double t;
      double X;
      double o;
      double T;
      int c;
      boolean O;
      boolean a;
      f4.a g;
      fk.w W;
      fk.h n;
   }
}
