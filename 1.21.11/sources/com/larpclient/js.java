package com.larpclient;

import java.util.Arrays;
import java.util.Random;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_4604;

public final class js {
   public static final int V = 256;
   public static final int H = 32;
   public static final int U = 12;
   public static final int M = 1024;
   public static final int l = 4;
   private static final double L = 0.05;
   private final Random b;
   private final gd G = new gd();
   private final ti N = new ti();
   private final js.b[] F = new js.b[256];
   private final js.t[] S = new js.t[256];
   private int k;
   private int z;
   private int Z;
   private int Y;
   private int w;
   private int I;
   private int t;
   private boolean W;
   private double n;
   private double s;
   private double C;
   private class_243 v;
   private class_243 q = class_243.field_1353;
   private js.a Q;
   private boolean e;
   private float r;

   public js() {
      this(new Random());
   }

   public js(Random var1) {
      this.b = var1;
      this.G.l = 1024;
      this.N.f = 2048;
      this.N.t = 7;

      for (int var2 = 0; var2 < this.F.length; var2++) {
         this.F[var2] = new js.b();
         this.S[var2] = new js.t();
      }
   }

   public void a() {
      for (js.b var4 : this.F) {
         Arrays.fill(var4.e, null);
         var4.R = var4.s = var4.d = null;
         var4.X = null;
      }

      for (js.t var8 : this.S) {
         Arrays.fill(var8.k, null);
         var8.l = 0;
      }

      this.G.q();
      this.k = this.z = this.Z = this.Y = this.w = this.I = 0;
      this.n = this.s = this.C = 0.0;
      this.v = null;
      this.Q = null;
      this.e = this.W = (boolean)0;
      this.t = 0;
   }

   public void R(class_243 var1, js.a var2, boolean var3, i3.k var4) {
      this.v(var1, var2, var3, var4, null);
   }

   public void v(class_243 var1, js.a var2, boolean var3, i3.k var4, js.q var5) {
      if (this.Q != null && (this.Q.style != var2.style || this.v.method_1025(var1) > 4096.0)) {
         this.a();
      }

      if (this.Q != null && (this.Q.speed != var2.speed || this.Q.impacts != var2.impacts)) {
         for (int var6 = 0; var6 < this.k; var6++) {
            this.F[var6].X = null;
            this.F[var6].O = 0.0;
         }
      }

      this.v = var1;
      this.Q = var2;
      this.Y = this.t = 0;
      this.C = this.s;
      this.s = this.s + Math.clamp((var3 ? 1 : 0) - this.s, -0.075, 0.075);
      this.G.O(var2.speed);
      if (var2.impacts && var4 != null && var2.speed > 0.0) {
         int var14 = 0;

         while (var14++ < this.k && this.Y < 4 && this.k > 0) {
            if (this.Z >= this.k) {
               this.Z = 0;
            }

            js.b var7 = this.F[this.Z++];
            if (var7.m && var7.X == null && !(var7.O > var7.Y + 0.15)) {
               double var8 = var7.d.method_1033() * var2.speed;
               double var10 = Math.min(var7.I, var7.Y + 7.5 / Math.max(0.01, var8));
               class_243 var12 = var7.R;
               class_243 var13 = var7.R.method_1019(var7.d.method_1021((var10 - var7.Y) * var2.speed));
               this.Y++;
               var7.X = var4.U(var12, var13);
               var7.O = var10;
               if (var7.X != null) {
                  var7.j = var7.Y + var12.method_1022(var7.X.point()) / var8;
               }
            }
         }
      }

      int var15 = 0;

      while (var15 < this.k) {
         js.b var17 = this.F[var15];
         var17.s = var17.R;
         var17.Y += 0.05;
         var17.R = var17.R.method_1019(var17.d.method_1021(0.05 * var2.speed));
         if (var17.X != null && var17.Y >= var17.j) {
            this.R(var17.X, var17.M);
            this.H(var15);
         } else {
            if (var2.style == js.s.q && !var17.S && var17.Y >= var17.I * 0.62) {
               var17.S = (boolean)1;
               if (var17.Z > 0.65) {
                  this.m(var17);
                  var17.I = Math.min(var17.I, var17.Y + 0.28);
               }
            }

            if (!(var17.Y >= var17.I) && !(var17.R.method_1025(this.v) > Math.pow(var2.distance * 1.3, 2.0))) {
               if (var2.style == js.s.q) {
                  System.arraycopy(var17.e, 0, var17.e, 1, 11);
                  var17.e[0] = var17.s;
                  var17.a = Math.min(12, var17.a + 1);
               }

               var15++;
            } else {
               this.H(var15);
            }
         }
      }

      var15 = var2.style == js.s.q ? Math.min(32, Math.max(1, var2.maximum * 32 / 256)) : Math.min(var2.maximum, 256);

      double var18 = var2.density * switch (var2.style) {
         case q -> 2.5;
         case K -> 42.0;
         case i -> 62.0;
         case F -> 48.0;
      };
      this.n = Math.min(8.0, this.n + var18 * 0.05 * this.s);
      int var9 = (int)this.n;
      this.n -= var9;
      int var19 = Math.min(928, Math.max(32, var2.maximum * 3));
      if (var2.style == js.s.K) {
         for (int var11 = 0; var11 < var9 && this.G.I() < var19; var11++) {
            this.O();
         }
      } else if (var2.style == js.s.F) {
         if (!this.W && this.s >= 1.0) {
            this.W = (boolean)1;
            var9 += (int)Math.min(var19 * 0.6, var18 * 3.0);
         }

         for (int var20 = 0; var20 < var9 && this.G.I() < var19; var20++) {
            this.C(var5);
         }
      } else {
         for (int var21 = 0; var21 < var9 && this.k < var15; var21++) {
            this.x();
         }
      }

      c5.V("Celestial active flights", this.k);
      c5.V("Celestial sprites", this.G.I());
      c5.V("Celestial terrain queries", this.Y);
      if (var2.style == js.s.F) {
         c5.V("Celestial roofed snow columns", this.t);
      }
   }

   private void H(int var1) {
      js.b var2 = this.F[var1];
      this.F[var1] = this.F[--this.k];
      this.F[this.k] = var2;
      var2.X = null;
      Arrays.fill(var2.e, null);
      var2.a = 0;
   }

   private void x() {
      js.b var1 = this.F[this.k++];
      int var2 = this.Q.style == js.s.q ? 1 : 0;
      var1.m = (boolean)(this.Q.impacts && this.b.nextDouble() < (var2 != 0 ? 0.23 : 0.16) ? 1 : 0);
      double var3 = 0.65 + (this.b.nextDouble() - 0.5) * (var2 != 0 ? 0.17 : 0.08);
      double var5 = this.Q.wind;
      var1.d = var2 != 0
         ? new class_243(
               Math.cos(var3) * (18.0 + this.b.nextDouble() * 7.0), -8.0 - this.b.nextDouble() * 4.0, Math.sin(var3) * (18.0 + this.b.nextDouble() * 7.0)
            )
            .method_1031(var5, 0.0, var5 * 0.4)
         : new class_243(var5 * 0.75, -10.0 - this.b.nextDouble() * 5.0, var5 * 0.3);
      class_243 var7 = this.v;
      double var8;
      if (var2 != 0) {
         var7 = this.v.method_1031(-var1.d.field_1352 * 0.65, var1.m ? 8.0 : Math.min(32.0, this.Q.radius * 0.7), -var1.d.field_1350 * 0.65);
         var8 = var1.m ? 8.0 : 18.0;
      } else {
         var7 = this.v.method_1031(0.0, 9.0, 0.0);
         var8 = 16.0;
      }

      var1.R = var1.s = wx.c(var7, var1.m ? Math.min(12.0, this.Q.radius) : this.Q.radius, var8, this.b);
      var1.Y = 0.0;
      var1.I = var2 != 0 ? 1.6 + this.b.nextDouble() * 1.8 : 2.3 + this.b.nextDouble() * 1.3;
      var1.M = this.Q.size * (var2 != 0 ? 0.055 : 0.014) * (0.65 + this.b.nextDouble() * 0.7);
      var1.Z = this.b.nextDouble();
      var1.S = (boolean)0;
      var1.X = null;
      var1.O = 0.0;
      var1.j = Double.POSITIVE_INFINITY;
      var1.a = 0;
   }

   private void v() {
      this.N.G = 1;
      this.N.l = this.Q.primary;
      this.N.b = ei.R(this.Q.secondary, 0.55);
      this.N.a = this.Q.bloom;
      this.N.k = this.Q.distance;
      this.N.o = this.N.g = this.N.c = this.N.O = this.N.d = 0.0;
      this.N.R = this.N.j = this.N.v = this.N.X = this.N.w = 0.0;
      this.N.C = this.N.q = 0.0;
      this.N.D = u9.u.M;
      this.N.W = u9.o.G;
      this.N.Q = gd.h.f;
   }

   private void O() {
      this.v();
      this.N.Q = this.b.nextDouble() < 0.2 ? gd.h.L : gd.h.f;
      this.N.i = 3.0;
      this.N.B = 6.0;
      this.N.M = this.Q.size * 0.022;
      this.N.F = this.Q.size * 0.07;
      this.N.o = this.Q.wind * 0.12;
      this.N.c = this.Q.wind * 0.05;
      this.N.C = 0.35 + this.b.nextDouble() * 0.5;
      this.N.q = 0.4 + this.b.nextDouble();
      this.N.e = 4.0 + this.b.nextDouble() * 4.0;
      this.N.l = ei.R(ei.W(this.Q.primary, this.Q.secondary, this.b.nextDouble()), 0.4 + this.b.nextDouble() * 0.5);
      int var1 = this.b.nextDouble() < 0.22 ? 1 : 0;
      this.G.V(wx.c(this.v, var1 != 0 ? Math.min(3.0, this.Q.radius * 0.16) : this.Q.radius, var1 != 0 ? 4.0 : Math.min(20.0, this.Q.radius), this.b), this.N);
   }

   private void C(js.q var1) {
      this.v();
      double var2 = this.b.nextDouble();
      int var4 = var2 < 0.06 ? 1 : 0;
      int var5 = var2 < 0.32 ? 1 : 0;
      double var6 = var4 != 0 ? Math.min(4.0, this.Q.radius * 0.2) : (var5 != 0 ? Math.min(9.0, this.Q.radius * 0.4) : this.Q.radius);
      class_243 var8 = wx.c(this.v.method_1031(0.0, var4 != 0 ? 1.0 : (var5 != 0 ? 3.0 : 7.0), 0.0), var6, var4 != 0 ? 4.0 : (var5 != 0 ? 12.0 : 22.0), this.b);
      double var9 = 0.55 + this.b.nextDouble() * 0.55;
      double var11 = this.Q.wind * 0.35;
      double var13 = this.Q.wind * 0.15;
      double var15 = 6.0 + this.b.nextDouble() * 6.0;
      double var17 = var9 * this.Q.speed;
      if (var1 != null) {
         double var19 = var1.t((int)Math.floor(var8.field_1352), (int)Math.floor(var8.field_1350));
         if (Double.isFinite(var19) && var8.field_1351 < var19 + 0.25) {
            this.t++;
            return;
         }

         if (var17 > 0.05) {
            double var21 = var8.field_1352 + var11 * this.Q.speed * var15;
            double var23 = var8.field_1350 + var13 * this.Q.speed * var15;
            double var25 = var1.t((int)Math.floor(var21), (int)Math.floor(var23));
            if (Double.isFinite(var25)) {
               var15 = Math.min(var15, Math.max(0.4, (var8.field_1351 - var25) / var17));
            }
         }
      }

      this.N.i = this.N.B = var15;
      this.N.o = var11;
      this.N.g = -var9;
      this.N.c = var13;
      this.N.v = 0.12;
      this.N.q = 0.2 + this.b.nextDouble() * 0.45;
      this.N.C = 0.06;
      this.N.e = 3.0 + this.b.nextDouble() * 3.5;
      this.N.D = u9.u.b;
      int var27 = var4 == 0 && this.b.nextDouble() < 0.12 ? 1 : 0;
      this.N.Q = var27 != 0 ? gd.h.L : gd.h.f;
      this.N.W = var27 != 0 ? u9.o.G : u9.o.d;
      double var20 = this.Q.size * (0.02 + this.b.nextDouble() * 0.032) * (var4 != 0 ? 2.6 : (var5 != 0 ? 1.2 : 1.0));
      this.N.M = this.N.F = var20;
      int var22 = ei.W(this.Q.primary, this.Q.primary & 0xFF000000 | 16777215, 0.35 + this.b.nextDouble() * 0.3);
      this.N.l = ei.R(var22, var4 != 0 ? 0.28 : 0.55 + this.b.nextDouble() * 0.4);
      this.N.b = ei.R(this.Q.secondary, var4 != 0 ? 0.12 : 0.35);
      this.G.V(var8, this.N);
   }

   private void m(js.b var1) {
      this.v();
      this.N.Q = gd.h.P;
      this.N.G = 3;
      this.N.i = 0.25;
      this.N.B = 0.55;
      this.N.M = var1.M * 0.65;
      this.N.F = var1.M;
      this.N.O = 1.4;
      this.N.R = 2.0;
      this.N.j = 0.4;
      this.N.o = var1.d.field_1352 * 0.65;
      this.N.g = var1.d.field_1351 * 0.65;
      this.N.c = var1.d.field_1350 * 0.65;
      this.N.D = u9.u.W;
      this.N.W = u9.o.D;
      this.G.V(var1.R, this.N);
      this.I++;
   }

   private void R(i3.j var1, double var2) {
      this.v();
      this.N.Q = gd.h.d;
      this.N.m = var1.normal();
      this.N.i = this.N.B = 0.42;
      this.N.Z = Math.clamp(var2 * 7.0, 0.12, 0.45);
      this.N.I = 0.015;
      this.N.D = u9.u.b;
      this.N.W = u9.o.d;
      this.N.l = ei.R(this.Q.primary, 0.5);
      this.N.b = ei.R(this.Q.secondary, 0.1);
      class_243 var4 = var1.point().method_1019(var1.normal().method_1021(0.012));
      this.G.V(var4, this.N);
      this.N.Q = gd.h.f;
      this.N.G = 2;
      this.N.i = 0.18;
      this.N.B = 0.32;
      this.N.M = 0.025;
      this.N.F = 0.045;
      this.N.Z = 1.0;
      this.N.g = 0.2;
      this.N.O = 0.35;
      this.N.W = u9.o.D;
      this.G.V(var4, this.N);
      this.w++;
   }

   public void J(class_243 var1, float var2, class_4604 var3) {
      long var4 = c5.X();
      this.q = var1;
      this.r = Math.clamp(var2, 0.0F, 1.0F);
      this.z = 0;
      this.e = (boolean)0;
      this.G.c(var1, this.r, var3);
      if (this.Q != null) {
         double var6 = this.C + (this.s - this.C) * this.r;

         for (int var8 = 0; var8 < this.k; var8++) {
            js.b var9 = this.F[var8];
            class_243 var10 = var9.s.method_35590(var9.R, this.r);
            double var11 = Math.max(0.0, var9.Y - 0.05 + this.r * 0.05);
            double var13 = var10.method_1022(var1);
            double var15 = m(var11, var9.I) * u9.L((this.Q.distance - var13) / Math.max(2.0, this.Q.distance * 0.18)) * var6;
            if (!(var15 < 0.005)) {
               int var17 = this.Q.style == js.s.q ? 1 : 0;
               class_243 var18 = var17 != 0 && var9.a > 0 ? var9.e[var9.a - 1] : var10.method_1020(var9.d.method_1021(0.045 + var9.Z * 0.025));
               double var19 = Math.max(0.1, var9.M * 3.0);
               if (var3 == null || var3.method_23093(new class_238(var10, var18).method_1014(var19))) {
                  js.t var21 = this.S[this.z++];
                  var21.V = var9.M;
                  var21.L = (boolean)var17;
                  var21.g = ei.R(ei.W(this.Q.primary, this.Q.primary & 0xFF000000 | 16777215, var17 != 0 ? 0.55 : 0.18), var15 * (var17 != 0 ? 0.95 : 0.6));
                  var21.R = ei.R(this.Q.secondary, var15 * (var17 != 0 ? 0.6 : 0.3));
                  if (var21.g >>> 24 == 0 && var21.R >>> 24 == 0) {
                     this.z--;
                  } else {
                     var21.k[0] = var10;
                     var21.l = 1;
                     if (var17 != 0) {
                        for (int var22 = 0; var22 < var9.a; var22++) {
                           var21.k[var21.l++] = var9.e[var22];
                        }
                     } else {
                        var21.k[var21.l++] = var18;
                     }
                  }
               }
            }
         }

         this.e = (boolean)(!this.Q.bloom || this.z <= 0 && !this.G.o() ? 0 : 1);
         c5.V("Celestial prepared flights", this.z);
         c5.V("Celestial ribbon segments", this.A());
         c5.V("Celestial culled flights", this.k - this.z);
         c5.w("Celestial prepare", var4);
      }
   }

   public static double m(double var0, double var2) {
      return u9.L(var0 / 0.22) * u9.L((var2 - var0) / 0.45);
   }

   public boolean y() {
      return this.e;
   }

   public boolean f() {
      return this.G.A();
   }

   public void q(f1 var1, boolean var2) {
      if (!var2 || this.e) {
         this.G.v(var1, this.q, this.r, var2);
         var1.blend(ti.d.l);

         for (int var3 = 0; var3 < this.z; var3++) {
            js.t var4 = this.S[var3];

            for (int var5 = 1; var5 < var4.l; var5++) {
               class_243 var6 = var4.k[var5 - 1];
               class_243 var7 = var4.k[var5];
               double var8 = 1.0 - (var5 - 1.0) / (var4.l - 1);
               double var10 = 1.0 - (double)var5 / (var4.l - 1);
               double var12 = var4.V * (var4.L ? 0.08 + 0.92 * var8 : 1.0);
               double var14 = var4.V * (var4.L ? 0.08 + 0.92 * var10 : 0.2);
               int var16 = ei.R(ei.W(var4.R, var4.g, var8), var8);
               int var17 = ei.R(ei.W(var4.R, var4.g, var10), var10);
               var1.g(
                  var6.field_1352,
                  var6.field_1351,
                  var6.field_1350,
                  var7.field_1352,
                  var7.field_1351,
                  var7.field_1350,
                  var12 * 3.0,
                  var14 * 3.0,
                  ei.R(var16, 0.15),
                  ei.R(var17, 0.15)
               );
               var1.g(var6.field_1352, var6.field_1351, var6.field_1350, var7.field_1352, var7.field_1351, var7.field_1350, var12, var14, var16, var17);
               if (var4.L) {
                  var1.g(
                     var6.field_1352,
                     var6.field_1351,
                     var6.field_1350,
                     var7.field_1352,
                     var7.field_1351,
                     var7.field_1350,
                     var12 * 0.26,
                     var14 * 0.26,
                     ei.W(var16, var16 & 0xFF000000 | 16777215, 0.75),
                     ei.W(var17, var17 & 0xFF000000 | 16777215, 0.75)
                  );
               }
            }

            if (var4.L) {
               class_243 var18 = var4.k[0];
               var1.U(var18.field_1352, var18.field_1351, var18.field_1350, var4.V * 1.5, 0.0, var4.g, gd.h.f, 0.0, 0.0, 0.0);
            }
         }

         var1.blend(ti.d.G);
      }
   }

   public int M() {
      return this.k;
   }

   public int B() {
      return this.G.I();
   }

   public int R() {
      int var1 = 0;

      for (int var2 = 0; var2 < this.k; var2++) {
         var1 += this.F[var2].a;
      }

      return var1;
   }

   public int C() {
      return this.z;
   }

   public int A() {
      int var1 = 0;

      for (int var2 = 0; var2 < this.z; var2++) {
         var1 += Math.max(0, this.S[var2].l - 1);
      }

      return var1;
   }

   public int X() {
      return this.Y;
   }

   public int Z() {
      return this.t;
   }

   public int q() {
      return this.w;
   }

   public int y() {
      return this.I;
   }

   public class_243 e(int var1) {
      return this.F[var1].R;
   }

   public class_243 S(int var1) {
      return this.F[var1].d;
   }

   public record a(
      js.s style,
      double density,
      double radius,
      double speed,
      double size,
      double wind,
      double distance,
      int primary,
      int secondary,
      boolean bloom,
      boolean impacts,
      int maximum
   ) {
   }

   private static final class b {
      class_243 R;
      class_243 s;
      class_243 d;
      final class_243[] e = new class_243[12];
      int a;
      double Y;
      double I;
      double M;
      double Z;
      double O;
      double j;
      boolean m;
      boolean S;
      i3.j X;
   }

   @FunctionalInterface
   public interface q {
      double t(int var1, int var2);
   }

   public enum s {
      q,
      K,
      i,
      F;
   }

   private static final class t {
      final class_243[] k = new class_243[13];
      int l;
      int g;
      int R;
      double V;
      boolean L;
   }
}
