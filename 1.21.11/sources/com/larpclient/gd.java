package com.larpclient;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_4604;

public final class gd {
   private static final int T = 2048;
   private final gd.m[] K = new gd.m[2048];
   private final Random t = new Random();
   private Int2IntOpenHashMap W;
   private int F;
   private int p;
   private int d;
   private int h;
   private final gd.z[] V = new gd.z[2048];
   private final gd.z[] M = new gd.z[2048];
   private boolean U;
   private static final Comparator<gd.z> I = Comparator.<gd.z>comparingDouble(var0 -> var0.w).reversed();
   public int l = 2048;
   public double i = 64.0;

   public gd() {
      for (int var1 = 0; var1 < 2048; var1++) {
         this.K[var1] = new gd.m();
         this.V[var1] = new gd.z();
      }

      q8.G("Particles", "Emitter engine initialized; capacity={}", 2048);
   }

   public int I() {
      return this.F;
   }

   public void q() {
      this.F = 0;
      if (this.W != null) {
         this.W.clear();
      }

      this.p = 0;
      this.d = this.h = 0;
      this.U = (boolean)0;
   }

   public void S(int var1) {
      for (int var2 = this.F - 1; var2 >= 0; var2--) {
         if (this.K[var2].o == var1) {
            this.T(var2);
         }
      }
   }

   private void T(int var1) {
      gd.m var2 = this.K[var1];
      if (this.W != null) {
         int var3 = this.W.get(var2.o) - 1;
         if (var3 == 0) {
            this.W.remove(var2.o);
         } else {
            this.W.put(var2.o, var3);
         }
      }

      if (var2.r) {
         this.p--;
      }

      this.K[var1] = this.K[--this.F];
      this.K[this.F] = var2;
   }

   public boolean K() {
      return this.p > 0;
   }

   private double L(double var1, double var3) {
      return var1 + this.t.nextDouble() * (var3 - var1);
   }

   public void V(class_243 var1, ti var2) {
      int var3 = Math.clamp(this.l, 0, 2048);
      int var4 = var2.f < 2048 ? this.i(var2.t) : 0;
      int var5 = Math.clamp(var2.f, 0, 2048);
      int var6 = var2.T ? this.i(var2.t) : 0;
      double var7 = Math.cos(var2.X * 0.05);
      double var9 = Math.sin(var2.X * 0.05);

      for (int var11 = 0; var11 < Math.clamp(var2.G, 0, 2048) && this.F < var3 && var4++ < var5; var11++) {
         gd.m var12 = this.K[this.F++];
         var12.R = var6++;
         double var13 = this.L(0.0, Math.PI * 2);
         double var15 = Math.sqrt(this.t.nextDouble());
         double var17 = this.L(-0.5, 0.5);
         double var19 = 0.0;
         double var21 = 0.0;
         switch (var2.z) {
            case g:
               var19 = this.L(-0.5, 0.5);
               var21 = this.L(-0.5, 0.5);
               break;
            case L:
               double var23 = this.L(-1.0, 1.0);
               double var25 = Math.cbrt(this.t.nextDouble()) * 0.5;
               double var27 = Math.sqrt(1.0 - var23 * var23) * var25;
               var19 = Math.cos(var13) * var27;
               var21 = Math.sin(var13) * var27;
               var17 = var23 * var25;
               break;
            case O:
               var19 = Math.cos(var13) * var15 * 0.5;
               var21 = Math.sin(var13) * var15 * 0.5;
               break;
            case t:
               var19 = Math.cos(var13) * 0.5;
               var21 = Math.sin(var13) * 0.5;
               var17 = 0.0;
               break;
            case G:
               var17 = 0.0;
         }

         var12.d = var12.m = var1.field_1352 + var19 * var2.E;
         var12.e = var12.M = var1.field_1351 + var17 * var2.A;
         var12.s = var12.Y = var1.field_1350 + var21 * var2.h;
         var12.L = this.L(-1.0, 1.0) * var2.O * var2.r + var2.y + var2.o;
         var12.f = this.L(-1.0, 1.0) * var2.O * var2.r + var2.c;
         var12.Z = this.L(var2.T ? -1.0 : -0.4, 1.0) * var2.O + var2.d + var2.g;
         var12.O = -Math.max(0.0, var2.J);
         var12.J = Math.max(0.05, this.L(var2.i, var2.B));
         var12.D = Math.max(0.001, this.L(var2.M, var2.F));
         if (var2.K != ti.y.w && Double.isFinite(var2.S)) {
            var12.e = var12.M = Math.max(var12.e, var2.S + var12.D * 1.2 + 0.002);
         }

         var12.c = var13;
         var12.I = this.L(Double.isFinite(var2.P) ? var2.P : -var2.w, var2.w);
         var12.G = var2.T;
         var12.A = var2.V;
         var12.j = var2.H;
         var12.X = this.L(0.0, Math.PI * 2);
         var12.EU = var12.X;
         var12.x = var2.C;
         var12.k = var2.q;
         var12.H = (Math.PI * 2) / Math.max(0.05, var2.e);
         var12.Q = var12.N = var12.n = 0.0;
         if (var12.x != 0.0 || var12.k != 0.0) {
            var12.Q = Math.sin(var12.EU * 0.83) * var12.k;
            var12.N = Math.sin(var12.EU) * var12.x;
            var12.n = Math.cos(var12.EU * 0.67) * var12.k;
            var12.d = var12.m = var12.m + var12.Q;
            var12.e = var12.M = var12.M + var12.N;
            var12.s = var12.Y = var12.Y + var12.n;
         }

         var12.B = var2.R;
         var12.l = Math.exp(-Math.max(0.0, var2.j) * 0.05);
         var12.v = var2.v;
         var12.a = var2.X;
         var12.y = var7;
         var12.w = var9;
         var12.C = var2.D;
         var12.p = var2.W;
         var12.K = var2.x;
         var12.u = var2.Q == gd.h.m ? var2.l : ei.W(var2.l, var2.b, this.t.nextDouble() * 0.35);
         var12.V = var2.b;
         var12.r = var2.a;
         if (var12.r) {
            this.p++;
         }

         var12.U = var2.Q;
         var12.F = var2.K;
         var12.S = (boolean)0;
         var12.i = var2.m.field_1352;
         var12.g = var2.m.field_1351;
         var12.P = var2.m.field_1350;
         var12.t = var12.b = var12.T = var12.E = 0.0;
         if (var12.U == gd.h.m || var12.U == gd.h.n) {
            var12.t = this.L(0.0, Math.PI * 2);
            var12.b = this.L(0.0, Math.PI * 2);
            var12.T = this.L(-2.4, 2.4);
            var12.E = this.L(-1.8, 1.8);
         }

         var12.q = var2.p;
         var12.h = var2.Z;
         var12.W = var2.I;
         var12.o = var2.t;
         if (this.W != null) {
            this.W.addTo(var12.o, 1);
         }

         var12.z = Double.isFinite(var2.k) ? Math.max(0.0, var2.k) : Math.max(0.0, this.i);
      }
   }

   public void u(class_243 var1, gd.h var2, int var3, double var4, double var6, int var8, int var9, boolean var10, double var11) {
      ti var13 = new ti();
      var13.Q = var2;
      var13.G = var3;
      var13.M = var13.F = var4;
      var13.i = var13.B = var6;
      var13.l = var8;
      var13.b = var9;
      var13.a = var10;
      var13.Z = var4 * var11;
      this.V(var1, var13);
   }

   public void d() {
      this.O(1.0);
   }

   public void O(double var1) {
      this.o(var1, null);
   }

   public void o(double var1, i3.k var3) {
      if (Double.isFinite(var1) && !(var1 < 0.0)) {
         int var4 = 0;

         while (var4 < this.F) {
            gd.m var5 = this.K[var4];
            var5.O += 0.05;
            if (var5.O >= var5.J) {
               this.T(var4);
            } else {
               var5.m = var5.d;
               var5.M = var5.e;
               var5.Y = var5.s;
               if (var5.O >= 0.0 && var5.U != gd.h.d && !var5.S) {
                  if (var5.G) {
                     var5.d = var5.d + var5.L * 0.05 * var1;
                     var5.e = var5.e + var5.Z * 0.05 * var1;
                     var5.s = var5.s + var5.f * 0.05 * var1;
                     var5.L = var5.L * var5.l;
                     var5.f = var5.f * var5.l;
                     var5.Z = (var5.Z - var5.B * 0.05) * var5.l;
                     var4++;
                     continue;
                  }

                  double var6 = var5.X + var5.O * 3.0;
                  double var8 = var5.L;
                  double var10 = var5.f;
                  var5.L = (var8 * var5.y - var10 * var5.w) * var5.l;
                  var5.f = (var8 * var5.w + var10 * var5.y) * var5.l;
                  var5.Z = (var5.Z - var5.B * 0.05) * var5.l;
                  if (var5.v != 0.0 || var5.L != 0.0 || var5.Z != 0.0 || var5.f != 0.0) {
                     var5.d = var5.d + (var5.L + Math.sin(var6) * var5.v) * 0.05 * var1;
                     var5.e = var5.e + (var5.Z + Math.cos(var6 * 1.3) * var5.v * 0.4) * 0.05 * var1;
                     var5.s = var5.s + (var5.f + Math.cos(var6 * 0.83) * var5.v) * 0.05 * var1;
                  }

                  if (var5.x != 0.0 || var5.k != 0.0) {
                     i(var5, var1);
                  }

                  if (var3 != null && var5.F != ti.y.w) {
                     Q(var5, var3);
                  }
               }

               var4++;
            }
         }
      } else {
         throw new IllegalArgumentException("Invalid motion speed");
      }
   }

   private static void Q(gd.m var0, i3.k var1) {
      class_243 var2 = new class_243(var0.m, var0.M, var0.Y);
      class_243 var3 = new class_243(var0.d, var0.e, var0.s);
      if (!(var2.method_1025(var3) < 1.0E-12)) {
         double var4 = var0.U == gd.h.n ? var0.D * 1.2 : 0.015;
         c5.V("Particle surface queries", 1L);
         i3.j var6 = var1.U(var2.method_1031(0.0, -var4, 0.0), var3.method_1031(0.0, -var4, 0.0));
         if (var6 != null) {
            class_243 var7 = var6.normal();
            var0.d = var6.point().field_1352 + var7.field_1352 * 0.002;
            var0.e = var6.point().field_1351 + var4 + var7.field_1351 * 0.002;
            var0.s = var6.point().field_1350 + var7.field_1350 * 0.002;
            double var8 = var0.L * var7.field_1352 + var0.Z * var7.field_1351 + var0.f * var7.field_1350;
            if (!(var8 >= 0.0)) {
               int var10 = !(var7.field_1351 > 0.5) || var0.F != ti.y.o && !(-var8 < 0.55) ? 0 : 1;
               if (var10 != 0) {
                  var0.S = (boolean)1;
                  var0.L = var0.Z = var0.f = 0.0;
                  var0.c = var0.c + var0.O * var0.I;
                  var0.t = var0.t + var0.O * var0.T;
                  var0.b = var0.b + var0.O * var0.E;
                  var0.I = var0.T = var0.E = 0.0;
                  if (var0.U == gd.h.n) {
                     var0.e = var6.point().field_1351 + e.k(var0.D, var0.t, var0.b) + 0.002;
                  }
               } else {
                  double var11 = var0.F == ti.y.u ? 0.38 : 0.08;
                  var0.L = (var0.L - (1.0 + var11) * var8 * var7.field_1352) * 0.72;
                  var0.Z = (var0.Z - (1.0 + var11) * var8 * var7.field_1351) * 0.72;
                  var0.f = (var0.f - (1.0 + var11) * var8 * var7.field_1350) * 0.72;
               }
            }
         }
      }
   }

   private static void i(gd.m var0, double var1) {
      var0.EU = var0.EU + 0.05 * var1 * var0.H;
      double var3 = Math.sin(var0.EU * 0.83) * var0.k;
      double var5 = Math.sin(var0.EU) * var0.x;
      double var7 = Math.cos(var0.EU * 0.67) * var0.k;
      var0.d = var0.d + (var3 - var0.Q);
      var0.e = var0.e + (var5 - var0.N);
      var0.s = var0.s + (var7 - var0.n);
      var0.Q = var3;
      var0.N = var5;
      var0.n = var7;
   }

   public int i(int var1) {
      if (this.W == null) {
         this.f();
      }

      return this.W.get(var1);
   }

   private void f() {
      this.W = new Int2IntOpenHashMap();

      for (int var1 = 0; var1 < this.F; var1++) {
         this.W.addTo(this.K[var1].o, 1);
      }
   }

   public boolean o() {
      return this.h > 0;
   }

   public boolean A() {
      return this.U;
   }

   public int J() {
      return this.d;
   }

   public static int R(double var0, int var2) {
      long var3 = ((long)(var0 * 1000.0) / 12L + var2 * 14L) % 360L;
      return ei.X((float)var3 / 360.0F, 0.7F, 1.0F);
   }

   public void c(class_243 var1, float var2, class_4604 var3) {
      long var4 = c5.X();
      this.d = this.h = 0;
      this.U = (boolean)0;
      byte var6 = 0;

      for (int var7 = 0; var7 < this.F; var7++) {
         gd.m var8 = this.K[var7];
         double var9 = var8.O + var2 * 0.05;
         if (!(var9 < 0.0)) {
            double var11 = var8.m + (var8.d - var8.m) * var2;
            double var13 = var8.M + (var8.e - var8.M) * var2;
            double var15 = var8.Y + (var8.s - var8.Y) * var2;
            double var17 = var11 - var1.field_1352;
            double var19 = var13 - var1.field_1351;
            double var21 = var15 - var1.field_1350;
            if (!(var17 * var17 + var19 * var19 + var21 * var21 > var8.z * var8.z)) {
               double var23 = Math.clamp(var9 / var8.J, 0.0, 1.0);
               double var25 = var8.D * var8.p.j(var23);
               int var27 = var8.A ? R(qo.x(), var8.R) : ei.W(var8.u, var8.V, var23);
               double var28 = var8.j == 1.0 ? var8.C.E(var23) : Math.pow(var8.C.E(var23), var8.j);
               int var30 = var8.G ? var27 & 16777215 | Math.clamp(Math.round((var27 >>> 24) * var28), 0, 255) << 24 : ei.R(var27, var28);
               if (var30 >>> 24 != 0) {
                  double var31 = var8.U == gd.h.d ? var8.h * (1.0 - Math.pow(1.0 - var23, 3.0)) : var25;
                  double var33 = var8.U == gd.h.d ? var31 + Math.abs(var8.W) : Math.abs(var25) * 5.0;
                  if (var3 == null
                     || var3.method_23093(new class_238(var11 - var33, var13 - var33, var15 - var33, var11 + var33, var13 + var33, var15 + var33))) {
                     gd.z var35 = this.V[this.d];
                     var35.c = var11;
                     var35.k = var13;
                     var35.B = var15;
                     var35.d = var25;
                     var35.Q = var31;
                     var35.W = var8.c + var9 * var8.I;
                     var35.i = var8.t + var9 * var8.T;
                     var35.z = var8.b + var9 * var8.E;
                     var35.y = var8.X + var9 * 4.0;
                     var35.q = var8.L;
                     var35.T = var8.Z;
                     var35.Z = var8.f;
                     var35.F = var8.W;
                     var35.l = var8.i;
                     var35.L = var8.g;
                     var35.A = var8.P;
                     var35.j = var30;
                     var35.m = var8.r;
                     var35.t = var8.U;
                     var35.u = var8.K;
                     var35.O = var8.q;
                     var35.V = (boolean)(var8.U != gd.h.g && var8.U != gd.h.f ? 0 : 1);
                     var35.w = var17 * var17 + var19 * var19 + var21 * var21;
                     var6 |= var35.u != ti.d.l ? 1 : 0;
                     this.M[this.d++] = var35;
                     if (var35.m) {
                        this.h++;
                     }

                     this.U = this.U | var35.V;
                  }
               }
            }
         }
      }

      if (var6 != 0) {
         Arrays.sort(this.M, 0, this.d, I);
      }

      c5.V("Particles prepared", this.d);
      c5.w("Particles prepare", var4);
   }

   public void v(f1 var1, class_243 var2, float var3, boolean var4) {
      long var5 = c5.X();
      gd.z var7 = null;

      for (int var8 = 0; var8 < this.d; var8++) {
         gd.z var9 = this.M[var8];
         if (!var4 || var9.m) {
            if (var7 != null && (var9.u != ti.d.l || var7.u != ti.d.l) && !Q(var7, var9)) {
               var1.s();
            }

            var1.blend(var9.u);
            var1.I(var9.V);
            if (var9.t == gd.h.d) {
               var1.E(var9.c, var9.k, var9.B, var9.Q, var9.F, var9.j, var9.l, var9.L, var9.A);
            } else if (var9.t == gd.h.m) {
               var1.X(var9.c, var9.k, var9.B, var9.d, var9.W, var9.i, var9.z, var9.y, var9.j);
            } else if (var9.t == gd.h.n) {
               var1.X(var9.c, var9.k, var9.B, var9.d, var9.W, var9.i, var9.z, var9.j);
            } else {
               var1.v(var9.c, var9.k, var9.B, var9.d, var9.W, var9.j, var9.t, var9.q, var9.T, var9.Z, var9.O);
            }

            var7 = var9;
         }
      }

      var1.I(false);
      var1.blend(ti.d.G);
      c5.w("Particles", var5);
   }

   private static boolean Q(gd.z var0, gd.z var1) {
      return var0.u == var1.u
         && var0.V == var1.V
         && var0.O == var1.O
         && (var0.t == gd.h.m ? 1 : 0) == (var1.t == gd.h.m ? 1 : 0)
         && (var0.t == gd.h.n ? 1 : 0) == (var1.t == gd.h.n ? 1 : 0)
         && (var0.t == gd.h.d ? 1 : 0) == (var1.t == gd.h.d ? 1 : 0);
   }

   public enum h {
      P,
      f,
      x,
      L,
      S,
      E,
      Y,
      d,
      t,
      n,
      e,
      g,
      m;
   }

   private static final class m {
      double d;
      double e;
      double s;
      double m;
      double M;
      double Y;
      double L;
      double Z;
      double f;
      double O;
      double J;
      double D;
      double c;
      double I;
      double X;
      double t;
      double b;
      double T;
      double E;
      double B;
      double l;
      double v;
      double a;
      double y;
      double w;
      double h;
      double W;
      double z;
      double x;
      double k;
      double EU;
      double H;
      double Q;
      double N;
      double n;
      double i;
      double g;
      double P;
      ti.y F;
      boolean S;
      int u;
      int V;
      int o;
      int R;
      boolean r;
      boolean G;
      boolean A;
      double j;
      gd.h U;
      ti.i q;
      u9.u C;
      u9.o p;
      ti.d K;
   }

   private static final class z {
      double c;
      double k;
      double B;
      double d;
      double Q;
      double F;
      double W;
      double i;
      double z;
      double y;
      double q;
      double T;
      double Z;
      double w;
      double l;
      double L;
      double A;
      int j;
      boolean m;
      boolean V;
      gd.h t;
      ti.i O;
      ti.d u;
   }
}
