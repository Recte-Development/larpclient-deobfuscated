package com.larpclient;

import java.util.Arrays;
import java.util.Random;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class wa {
   public static final int K = 256;
   public static final int w = 32;
   public static final int W = 12;
   public static final int M = 1024;
   public static final int q = 4;
   private static final double z = 0.05;
   private final Random G;
   private final com.larpclient.l k = new com.larpclient.l();
   private final _0 t = new _0();
   private final wa.s[] h = new wa.s[256];
   private final wa.q[] E = new wa.q[256];
   private int c;
   private int J;
   private int H;
   private int O;
   private int V;
   private int n;
   private int s;
   private boolean u;
   private double U;
   private double x;
   private double r;
   private Vec3 a;
   private Vec3 N = Vec3.ZERO;
   private wa.l I;
   private boolean m;
   private float S;

   public wa() {
      this(new Random());
   }

   public wa(Random var1) {
      this.G = var1;
      this.k.I = 1024;
      this.t.A = 2048;
      this.t.r = 7;

      for (int var2 = 0; var2 < this.h.length; var2++) {
         this.h[var2] = new wa.s();
         this.E[var2] = new wa.q();
      }
   }

   public void g() {
      for (wa.s var4 : this.h) {
         Arrays.fill(var4.i, null);
         var4.n = var4.G = var4.Y = null;
         var4.q = null;
      }

      for (wa.q var8 : this.E) {
         Arrays.fill(var8.R, null);
         var8.X = 0;
      }

      this.k.k();
      this.c = this.J = this.H = this.O = this.V = this.n = 0;
      this.U = this.x = this.r = 0.0;
      this.a = null;
      this.I = null;
      this.m = this.u = (boolean)0;
      this.s = 0;
   }

   public void m(Vec3 var1, wa.l var2, boolean var3, v9.w var4) {
      this.i(var1, var2, var3, var4, null);
   }

   public void i(Vec3 var1, wa.l var2, boolean var3, v9.w var4, wa.u var5) {
      if (this.I != null && (this.I.style != var2.style || this.a.distanceToSqr(var1) > 4096.0)) {
         this.g();
      }

      if (this.I != null && (this.I.speed != var2.speed || this.I.impacts != var2.impacts)) {
         for (int var6 = 0; var6 < this.c; var6++) {
            this.h[var6].q = null;
            this.h[var6].b = 0.0;
         }
      }

      this.a = var1;
      this.I = var2;
      this.O = this.s = 0;
      this.r = this.x;
      this.x = this.x + Math.clamp((var3 ? 1 : 0) - this.x, -0.075, 0.075);
      this.k.A(var2.speed);
      if (var2.impacts && var4 != null && var2.speed > 0.0) {
         int var14 = 0;

         while (var14++ < this.c && this.O < 4 && this.c > 0) {
            if (this.H >= this.c) {
               this.H = 0;
            }

            wa.s var7 = this.h[this.H++];
            if (var7.K && var7.q == null && !(var7.b > var7.V + 0.15)) {
               double var8 = var7.Y.length() * var2.speed;
               double var10 = Math.min(var7.t, var7.V + 7.5 / Math.max(0.01, var8));
               Vec3 var12 = var7.n;
               Vec3 var13 = var7.n.add(var7.Y.scale((var10 - var7.V) * var2.speed));
               this.O++;
               var7.q = var4.f(var12, var13);
               var7.b = var10;
               if (var7.q != null) {
                  var7.d = var7.V + var12.distanceTo(var7.q.point()) / var8;
               }
            }
         }
      }

      int var15 = 0;

      while (var15 < this.c) {
         wa.s var17 = this.h[var15];
         var17.G = var17.n;
         var17.V += 0.05;
         var17.n = var17.n.add(var17.Y.scale(0.05 * var2.speed));
         if (var17.q != null && var17.V >= var17.d) {
            this.H(var17.q, var17.N);
            this.E(var15);
         } else {
            if (var2.style == wa.h.S && !var17.u && var17.V >= var17.t * 0.62) {
               var17.u = (boolean)1;
               if (var17.g > 0.65) {
                  this.J(var17);
                  var17.t = Math.min(var17.t, var17.V + 0.28);
               }
            }

            if (!(var17.V >= var17.t) && !(var17.n.distanceToSqr(this.a) > Math.pow(var2.distance * 1.3, 2.0))) {
               if (var2.style == wa.h.S) {
                  System.arraycopy(var17.i, 0, var17.i, 1, 11);
                  var17.i[0] = var17.G;
                  var17.m = Math.min(12, var17.m + 1);
               }

               var15++;
            } else {
               this.E(var15);
            }
         }
      }

      var15 = var2.style == wa.h.S ? Math.min(32, Math.max(1, var2.maximum * 32 / 256)) : Math.min(var2.maximum, 256);

      double var18 = var2.density * switch (var2.style) {
         case S -> 2.5;
         case d -> 42.0;
         case W -> 62.0;
         case x -> 48.0;
      };
      this.U = Math.min(8.0, this.U + var18 * 0.05 * this.x);
      int var9 = (int)this.U;
      this.U -= var9;
      int var19 = Math.min(928, Math.max(32, var2.maximum * 3));
      if (var2.style == wa.h.d) {
         for (int var11 = 0; var11 < var9 && this.k.K() < var19; var11++) {
            this.F();
         }
      } else if (var2.style == wa.h.x) {
         if (!this.u && this.x >= 1.0) {
            this.u = (boolean)1;
            var9 += (int)Math.min(var19 * 0.6, var18 * 3.0);
         }

         for (int var20 = 0; var20 < var9 && this.k.K() < var19; var20++) {
            this.E(var5);
         }
      } else {
         for (int var21 = 0; var21 < var9 && this.c < var15; var21++) {
            this.q();
         }
      }

      he.Z("Celestial active flights", this.c);
      he.Z("Celestial sprites", this.k.K());
      he.Z("Celestial terrain queries", this.O);
      if (var2.style == wa.h.x) {
         he.Z("Celestial roofed snow columns", this.s);
      }
   }

   private void E(int var1) {
      wa.s var2 = this.h[var1];
      this.h[var1] = this.h[--this.c];
      this.h[this.c] = var2;
      var2.q = null;
      Arrays.fill(var2.i, null);
      var2.m = 0;
   }

   private void q() {
      wa.s var1 = this.h[this.c++];
      int var2 = this.I.style == wa.h.S ? 1 : 0;
      var1.K = (boolean)(this.I.impacts && this.G.nextDouble() < (var2 != 0 ? 0.23 : 0.16) ? 1 : 0);
      double var3 = 0.65 + (this.G.nextDouble() - 0.5) * (var2 != 0 ? 0.17 : 0.08);
      double var5 = this.I.wind;
      var1.Y = var2 != 0
         ? new Vec3(Math.cos(var3) * (18.0 + this.G.nextDouble() * 7.0), -8.0 - this.G.nextDouble() * 4.0, Math.sin(var3) * (18.0 + this.G.nextDouble() * 7.0))
            .add(var5, 0.0, var5 * 0.4)
         : new Vec3(var5 * 0.75, -10.0 - this.G.nextDouble() * 5.0, var5 * 0.3);
      Vec3 var7 = this.a;
      double var8;
      if (var2 != 0) {
         var7 = this.a.add(-var1.Y.x * 0.65, var1.K ? 8.0 : Math.min(32.0, this.I.radius * 0.7), -var1.Y.z * 0.65);
         var8 = var1.K ? 8.0 : 18.0;
      } else {
         var7 = this.a.add(0.0, 9.0, 0.0);
         var8 = 16.0;
      }

      var1.n = var1.G = o8.e(var7, var1.K ? Math.min(12.0, this.I.radius) : this.I.radius, var8, this.G);
      var1.V = 0.0;
      var1.t = var2 != 0 ? 1.6 + this.G.nextDouble() * 1.8 : 2.3 + this.G.nextDouble() * 1.3;
      var1.N = this.I.size * (var2 != 0 ? 0.055 : 0.014) * (0.65 + this.G.nextDouble() * 0.7);
      var1.g = this.G.nextDouble();
      var1.u = (boolean)0;
      var1.q = null;
      var1.b = 0.0;
      var1.d = Double.POSITIVE_INFINITY;
      var1.m = 0;
   }

   private void A() {
      this.t.w = 1;
      this.t.F = this.I.primary;
      this.t.t = vv.O(this.I.secondary, 0.55);
      this.t.M = this.I.bloom;
      this.t.k = this.I.distance;
      this.t.o = this.t.P = this.t.S = this.t.I = this.t.U = 0.0;
      this.t.n = this.t.q = this.t.K = this.t.s = this.t.u = 0.0;
      this.t.y = this.t.J = 0.0;
      this.t.C = pr.b.M;
      this.t.T = pr.g.A;
      this.t.L = com.larpclient.l.b.P;
   }

   private void F() {
      this.A();
      this.t.L = this.G.nextDouble() < 0.2 ? com.larpclient.l.b.Y : com.larpclient.l.b.P;
      this.t.Q = 3.0;
      this.t.W = 6.0;
      this.t.i = this.I.size * 0.022;
      this.t.m = this.I.size * 0.07;
      this.t.o = this.I.wind * 0.12;
      this.t.S = this.I.wind * 0.05;
      this.t.y = 0.35 + this.G.nextDouble() * 0.5;
      this.t.J = 0.4 + this.G.nextDouble();
      this.t.d = 4.0 + this.G.nextDouble() * 4.0;
      this.t.F = vv.O(vv.Q(this.I.primary, this.I.secondary, this.G.nextDouble()), 0.4 + this.G.nextDouble() * 0.5);
      int var1 = this.G.nextDouble() < 0.22 ? 1 : 0;
      this.k.Q(o8.e(this.a, var1 != 0 ? Math.min(3.0, this.I.radius * 0.16) : this.I.radius, var1 != 0 ? 4.0 : Math.min(20.0, this.I.radius), this.G), this.t);
   }

   private void E(wa.u var1) {
      this.A();
      double var2 = this.G.nextDouble();
      int var4 = var2 < 0.06 ? 1 : 0;
      int var5 = var2 < 0.32 ? 1 : 0;
      double var6 = var4 != 0 ? Math.min(4.0, this.I.radius * 0.2) : (var5 != 0 ? Math.min(9.0, this.I.radius * 0.4) : this.I.radius);
      Vec3 var8 = o8.e(this.a.add(0.0, var4 != 0 ? 1.0 : (var5 != 0 ? 3.0 : 7.0), 0.0), var6, var4 != 0 ? 4.0 : (var5 != 0 ? 12.0 : 22.0), this.G);
      double var9 = 0.55 + this.G.nextDouble() * 0.55;
      double var11 = this.I.wind * 0.35;
      double var13 = this.I.wind * 0.15;
      double var15 = 6.0 + this.G.nextDouble() * 6.0;
      double var17 = var9 * this.I.speed;
      if (var1 != null) {
         double var19 = var1.a((int)Math.floor(var8.x), (int)Math.floor(var8.z));
         if (Double.isFinite(var19) && var8.y < var19 + 0.25) {
            this.s++;
            return;
         }

         if (var17 > 0.05) {
            double var21 = var8.x + var11 * this.I.speed * var15;
            double var23 = var8.z + var13 * this.I.speed * var15;
            double var25 = var1.a((int)Math.floor(var21), (int)Math.floor(var23));
            if (Double.isFinite(var25)) {
               var15 = Math.min(var15, Math.max(0.4, (var8.y - var25) / var17));
            }
         }
      }

      this.t.Q = this.t.W = var15;
      this.t.o = var11;
      this.t.P = -var9;
      this.t.S = var13;
      this.t.K = 0.12;
      this.t.J = 0.2 + this.G.nextDouble() * 0.45;
      this.t.y = 0.06;
      this.t.d = 3.0 + this.G.nextDouble() * 3.5;
      this.t.C = pr.b.Y;
      int var27 = var4 == 0 && this.G.nextDouble() < 0.12 ? 1 : 0;
      this.t.L = var27 != 0 ? com.larpclient.l.b.Y : com.larpclient.l.b.P;
      this.t.T = var27 != 0 ? pr.g.A : pr.g.Z;
      double var20 = this.I.size * (0.02 + this.G.nextDouble() * 0.032) * (var4 != 0 ? 2.6 : (var5 != 0 ? 1.2 : 1.0));
      this.t.i = this.t.m = var20;
      int var22 = vv.Q(this.I.primary, this.I.primary & 0xFF000000 | 16777215, 0.35 + this.G.nextDouble() * 0.3);
      this.t.F = vv.O(var22, var4 != 0 ? 0.28 : 0.55 + this.G.nextDouble() * 0.4);
      this.t.t = vv.O(this.I.secondary, var4 != 0 ? 0.12 : 0.35);
      this.k.Q(var8, this.t);
   }

   private void J(wa.s var1) {
      this.A();
      this.t.L = com.larpclient.l.b.i;
      this.t.w = 3;
      this.t.Q = 0.25;
      this.t.W = 0.55;
      this.t.i = var1.N * 0.65;
      this.t.m = var1.N;
      this.t.I = 1.4;
      this.t.n = 2.0;
      this.t.q = 0.4;
      this.t.o = var1.Y.x * 0.65;
      this.t.P = var1.Y.y * 0.65;
      this.t.S = var1.Y.z * 0.65;
      this.t.C = pr.b.A;
      this.t.T = pr.g.U;
      this.k.Q(var1.n, this.t);
      this.n++;
   }

   private void H(v9.d var1, double var2) {
      this.A();
      this.t.L = com.larpclient.l.b.T;
      this.t.Y = var1.normal();
      this.t.Q = this.t.W = 0.42;
      this.t.h = Math.clamp(var2 * 7.0, 0.12, 0.45);
      this.t.z = 0.015;
      this.t.C = pr.b.Y;
      this.t.T = pr.g.Z;
      this.t.F = vv.O(this.I.primary, 0.5);
      this.t.t = vv.O(this.I.secondary, 0.1);
      Vec3 var4 = var1.point().add(var1.normal().scale(0.012));
      this.k.Q(var4, this.t);
      this.t.L = com.larpclient.l.b.P;
      this.t.w = 2;
      this.t.Q = 0.18;
      this.t.W = 0.32;
      this.t.i = 0.025;
      this.t.m = 0.045;
      this.t.h = 1.0;
      this.t.P = 0.2;
      this.t.I = 0.35;
      this.t.T = pr.g.U;
      this.k.Q(var4, this.t);
      this.V++;
   }

   public void z(Vec3 var1, float var2, Frustum var3) {
      long var4 = he.s();
      this.N = var1;
      this.S = Math.clamp(var2, 0.0F, 1.0F);
      this.J = 0;
      this.m = (boolean)0;
      this.k.p(var1, this.S, var3);
      if (this.I != null) {
         double var6 = this.r + (this.x - this.r) * this.S;

         for (int var8 = 0; var8 < this.c; var8++) {
            wa.s var9 = this.h[var8];
            Vec3 var10 = var9.G.lerp(var9.n, this.S);
            double var11 = Math.max(0.0, var9.V - 0.05 + this.S * 0.05);
            double var13 = var10.distanceTo(var1);
            double var15 = D(var11, var9.t) * pr.k((this.I.distance - var13) / Math.max(2.0, this.I.distance * 0.18)) * var6;
            if (!(var15 < 0.005)) {
               int var17 = this.I.style == wa.h.S ? 1 : 0;
               Vec3 var18 = var17 != 0 && var9.m > 0 ? var9.i[var9.m - 1] : var10.subtract(var9.Y.scale(0.045 + var9.g * 0.025));
               double var19 = Math.max(0.1, var9.N * 3.0);
               if (var3 == null || var3.isVisible(new AABB(var10, var18).inflate(var19))) {
                  wa.q var21 = this.E[this.J++];
                  var21.z = var9.N;
                  var21.J = (boolean)var17;
                  var21.B = vv.O(vv.Q(this.I.primary, this.I.primary & 0xFF000000 | 16777215, var17 != 0 ? 0.55 : 0.18), var15 * (var17 != 0 ? 0.95 : 0.6));
                  var21.p = vv.O(this.I.secondary, var15 * (var17 != 0 ? 0.6 : 0.3));
                  if (var21.B >>> 24 == 0 && var21.p >>> 24 == 0) {
                     this.J--;
                  } else {
                     var21.R[0] = var10;
                     var21.X = 1;
                     if (var17 != 0) {
                        for (int var22 = 0; var22 < var9.m; var22++) {
                           var21.R[var21.X++] = var9.i[var22];
                        }
                     } else {
                        var21.R[var21.X++] = var18;
                     }
                  }
               }
            }
         }

         this.m = (boolean)(!this.I.bloom || this.J <= 0 && !this.k.d() ? 0 : 1);
         he.Z("Celestial prepared flights", this.J);
         he.Z("Celestial ribbon segments", this.r());
         he.Z("Celestial culled flights", this.c - this.J);
         he.T("Celestial prepare", var4);
      }
   }

   public static double D(double var0, double var2) {
      return pr.k(var0 / 0.22) * pr.k((var2 - var0) / 0.45);
   }

   public boolean F() {
      return this.m;
   }

   public boolean j() {
      return this.k.J();
   }

   public void k(fz var1, boolean var2) {
      if (!var2 || this.m) {
         this.k.z(var1, this.N, this.S, var2);
         var1.blend(_0.v.c);

         for (int var3 = 0; var3 < this.J; var3++) {
            wa.q var4 = this.E[var3];

            for (int var5 = 1; var5 < var4.X; var5++) {
               Vec3 var6 = var4.R[var5 - 1];
               Vec3 var7 = var4.R[var5];
               double var8 = 1.0 - (var5 - 1.0) / (var4.X - 1);
               double var10 = 1.0 - (double)var5 / (var4.X - 1);
               double var12 = var4.z * (var4.J ? 0.08 + 0.92 * var8 : 1.0);
               double var14 = var4.z * (var4.J ? 0.08 + 0.92 * var10 : 0.2);
               int var16 = vv.O(vv.Q(var4.p, var4.B, var8), var8);
               int var17 = vv.O(vv.Q(var4.p, var4.B, var10), var10);
               var1.E(var6.x, var6.y, var6.z, var7.x, var7.y, var7.z, var12 * 3.0, var14 * 3.0, vv.O(var16, 0.15), vv.O(var17, 0.15));
               var1.E(var6.x, var6.y, var6.z, var7.x, var7.y, var7.z, var12, var14, var16, var17);
               if (var4.J) {
                  var1.E(
                     var6.x,
                     var6.y,
                     var6.z,
                     var7.x,
                     var7.y,
                     var7.z,
                     var12 * 0.26,
                     var14 * 0.26,
                     vv.Q(var16, var16 & 0xFF000000 | 16777215, 0.75),
                     vv.Q(var17, var17 & 0xFF000000 | 16777215, 0.75)
                  );
               }
            }

            if (var4.J) {
               Vec3 var18 = var4.R[0];
               var1.a(var18.x, var18.y, var18.z, var4.z * 1.5, 0.0, var4.B, com.larpclient.l.b.P, 0.0, 0.0, 0.0);
            }
         }

         var1.blend(_0.v.z);
      }
   }

   public int R() {
      return this.c;
   }

   public int h() {
      return this.k.K();
   }

   public int G() {
      int var1 = 0;

      for (int var2 = 0; var2 < this.c; var2++) {
         var1 += this.h[var2].m;
      }

      return var1;
   }

   public int J() {
      return this.J;
   }

   public int r() {
      int var1 = 0;

      for (int var2 = 0; var2 < this.J; var2++) {
         var1 += Math.max(0, this.E[var2].X - 1);
      }

      return var1;
   }

   public int y() {
      return this.O;
   }

   public int C() {
      return this.s;
   }

   public int E() {
      return this.V;
   }

   public int B() {
      return this.n;
   }

   public Vec3 n(int var1) {
      return this.h[var1].n;
   }

   public Vec3 g(int var1) {
      return this.h[var1].Y;
   }

   public enum h {
      S,
      d,
      W,
      x;
   }

   public record l(
      wa.h style,
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

   private static final class q {
      final Vec3[] R = new Vec3[13];
      int X;
      int B;
      int p;
      double z;
      boolean J;
   }

   private static final class s {
      Vec3 n;
      Vec3 G;
      Vec3 Y;
      final Vec3[] i = new Vec3[12];
      int m;
      double V;
      double t;
      double N;
      double g;
      double b;
      double d;
      boolean K;
      boolean u;
      v9.d q;
   }

   @FunctionalInterface
   public interface u {
      double a(int var1, int var2);
   }
}
