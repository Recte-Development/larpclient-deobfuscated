package com.larpclient;

import java.util.Arrays;
import java.util.Random;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class e3 {
   public static final int s = 256;
   public static final int g = 32;
   public static final int l = 12;
   public static final int D = 1024;
   public static final int z = 4;
   private static final double J = 0.05;
   private final Random o;
   private final cg V = new cg();
   private final pf X = new pf();
   private final e3.h[] P = new e3.h[256];
   private final e3.z[] q = new e3.z[256];
   private int c;
   private int j;
   private int w;
   private int S;
   private int T;
   private int e;
   private int G;
   private boolean k;
   private double a;
   private double f;
   private double d;
   private Vec3 K;
   private Vec3 m = Vec3.ZERO;
   private e3.i W;
   private boolean b;
   private float N;

   public e3() {
      this(new Random());
   }

   public e3(Random var1) {
      this.o = var1;
      this.V.X = 1024;
      this.X.X = 2048;
      this.X.D = 7;

      for (int var2 = 0; var2 < this.P.length; var2++) {
         this.P[var2] = new e3.h();
         this.q[var2] = new e3.z();
      }
   }

   public void Y() {
      for (e3.h var4 : this.P) {
         Arrays.fill(var4.S, null);
         var4.j = var4.u = var4.g = null;
         var4.K = null;
      }

      for (e3.z var8 : this.q) {
         Arrays.fill(var8.u, null);
         var8.p = 0;
      }

      this.V.J();
      this.c = this.j = this.w = this.S = this.T = this.e = 0;
      this.a = this.f = this.d = 0.0;
      this.K = null;
      this.W = null;
      this.b = this.k = (boolean)0;
      this.G = 0;
   }

   public void V(Vec3 var1, e3.i var2, boolean var3, w8.z var4) {
      this.M(var1, var2, var3, var4, null);
   }

   public void M(Vec3 var1, e3.i var2, boolean var3, w8.z var4, e3.m var5) {
      if (this.W != null && (this.W.style != var2.style || this.K.distanceToSqr(var1) > 4096.0)) {
         this.Y();
      }

      if (this.W != null && (this.W.speed != var2.speed || this.W.impacts != var2.impacts)) {
         for (int var6 = 0; var6 < this.c; var6++) {
            this.P[var6].K = null;
            this.P[var6].D = 0.0;
         }
      }

      this.K = var1;
      this.W = var2;
      this.S = this.G = 0;
      this.d = this.f;
      this.f = this.f + Math.clamp((var3 ? 1 : 0) - this.f, -0.075, 0.075);
      this.V.Y(var2.speed);
      if (var2.impacts && var4 != null && var2.speed > 0.0) {
         int var14 = 0;

         while (var14++ < this.c && this.S < 4 && this.c > 0) {
            if (this.w >= this.c) {
               this.w = 0;
            }

            e3.h var7 = this.P[this.w++];
            if (var7.a && var7.K == null && !(var7.D > var7.C + 0.15)) {
               double var8 = var7.g.length() * var2.speed;
               double var10 = Math.min(var7.l, var7.C + 7.5 / Math.max(0.01, var8));
               Vec3 var12 = var7.j;
               Vec3 var13 = var7.j.add(var7.g.scale((var10 - var7.C) * var2.speed));
               this.S++;
               var7.K = var4.X(var12, var13);
               var7.D = var10;
               if (var7.K != null) {
                  var7.h = var7.C + var12.distanceTo(var7.K.point()) / var8;
               }
            }
         }
      }

      int var15 = 0;

      while (var15 < this.c) {
         e3.h var17 = this.P[var15];
         var17.u = var17.j;
         var17.C += 0.05;
         var17.j = var17.j.add(var17.g.scale(0.05 * var2.speed));
         if (var17.K != null && var17.C >= var17.h) {
            this.V(var17.K, var17.k);
            this.q(var15);
         } else {
            if (var2.style == e3.d.w && !var17.E && var17.C >= var17.l * 0.62) {
               var17.E = (boolean)1;
               if (var17.w > 0.65) {
                  this.k(var17);
                  var17.l = Math.min(var17.l, var17.C + 0.28);
               }
            }

            if (!(var17.C >= var17.l) && !(var17.j.distanceToSqr(this.K) > Math.pow(var2.distance * 1.3, 2.0))) {
               if (var2.style == e3.d.w) {
                  System.arraycopy(var17.S, 0, var17.S, 1, 11);
                  var17.S[0] = var17.u;
                  var17.W = Math.min(12, var17.W + 1);
               }

               var15++;
            } else {
               this.q(var15);
            }
         }
      }

      var15 = var2.style == e3.d.w ? Math.min(32, Math.max(1, var2.maximum * 32 / 256)) : Math.min(var2.maximum, 256);

      double var18 = var2.density * switch (var2.style) {
         case w -> 2.5;
         case p -> 42.0;
         case X -> 62.0;
         case t -> 48.0;
      };
      this.a = Math.min(8.0, this.a + var18 * 0.05 * this.f);
      int var9 = (int)this.a;
      this.a -= var9;
      int var19 = Math.min(928, Math.max(32, var2.maximum * 3));
      if (var2.style == e3.d.p) {
         for (int var11 = 0; var11 < var9 && this.V.x() < var19; var11++) {
            this.C();
         }
      } else if (var2.style == e3.d.t) {
         if (!this.k && this.f >= 1.0) {
            this.k = (boolean)1;
            var9 += (int)Math.min(var19 * 0.6, var18 * 3.0);
         }

         for (int var20 = 0; var20 < var9 && this.V.x() < var19; var20++) {
            this.h(var5);
         }
      } else {
         for (int var21 = 0; var21 < var9 && this.c < var15; var21++) {
            this.j();
         }
      }

      kx.U("Celestial active flights", this.c);
      kx.U("Celestial sprites", this.V.x());
      kx.U("Celestial terrain queries", this.S);
      if (var2.style == e3.d.t) {
         kx.U("Celestial roofed snow columns", this.G);
      }
   }

   private void q(int var1) {
      e3.h var2 = this.P[var1];
      this.P[var1] = this.P[--this.c];
      this.P[this.c] = var2;
      var2.K = null;
      Arrays.fill(var2.S, null);
      var2.W = 0;
   }

   private void j() {
      e3.h var1 = this.P[this.c++];
      int var2 = this.W.style == e3.d.w ? 1 : 0;
      var1.a = (boolean)(this.W.impacts && this.o.nextDouble() < (var2 != 0 ? 0.23 : 0.16) ? 1 : 0);
      double var3 = 0.65 + (this.o.nextDouble() - 0.5) * (var2 != 0 ? 0.17 : 0.08);
      double var5 = this.W.wind;
      var1.g = var2 != 0
         ? new Vec3(Math.cos(var3) * (18.0 + this.o.nextDouble() * 7.0), -8.0 - this.o.nextDouble() * 4.0, Math.sin(var3) * (18.0 + this.o.nextDouble() * 7.0))
            .add(var5, 0.0, var5 * 0.4)
         : new Vec3(var5 * 0.75, -10.0 - this.o.nextDouble() * 5.0, var5 * 0.3);
      Vec3 var7 = this.K;
      double var8;
      if (var2 != 0) {
         var7 = this.K.add(-var1.g.x * 0.65, var1.a ? 8.0 : Math.min(32.0, this.W.radius * 0.7), -var1.g.z * 0.65);
         var8 = var1.a ? 8.0 : 18.0;
      } else {
         var7 = this.K.add(0.0, 9.0, 0.0);
         var8 = 16.0;
      }

      var1.j = var1.u = mh.m(var7, var1.a ? Math.min(12.0, this.W.radius) : this.W.radius, var8, this.o);
      var1.C = 0.0;
      var1.l = var2 != 0 ? 1.6 + this.o.nextDouble() * 1.8 : 2.3 + this.o.nextDouble() * 1.3;
      var1.k = this.W.size * (var2 != 0 ? 0.055 : 0.014) * (0.65 + this.o.nextDouble() * 0.7);
      var1.w = this.o.nextDouble();
      var1.E = (boolean)0;
      var1.K = null;
      var1.D = 0.0;
      var1.h = Double.POSITIVE_INFINITY;
      var1.W = 0;
   }

   private void y() {
      this.X.g = 1;
      this.X.E = this.W.primary;
      this.X.N = _3.U(this.W.secondary, 0.55);
      this.X.i = this.W.bloom;
      this.X.j = this.W.distance;
      this.X.z = this.X.K = this.X.v = this.X.Z = this.X.r = 0.0;
      this.X.W = this.X.q = this.X.f = this.X.J = this.X.e = 0.0;
      this.X.G = this.X.Y = 0.0;
      this.X.S = hp.y.e;
      this.X.l = hp.u.J;
      this.X.P = cg.u.a;
   }

   private void C() {
      this.y();
      this.X.P = this.o.nextDouble() < 0.2 ? cg.u.h : cg.u.a;
      this.X.M = 3.0;
      this.X.O = 6.0;
      this.X.C = this.W.size * 0.022;
      this.X.d = this.W.size * 0.07;
      this.X.z = this.W.wind * 0.12;
      this.X.v = this.W.wind * 0.05;
      this.X.G = 0.35 + this.o.nextDouble() * 0.5;
      this.X.Y = 0.4 + this.o.nextDouble();
      this.X.m = 4.0 + this.o.nextDouble() * 4.0;
      this.X.E = _3.U(_3.D(this.W.primary, this.W.secondary, this.o.nextDouble()), 0.4 + this.o.nextDouble() * 0.5);
      int var1 = this.o.nextDouble() < 0.22 ? 1 : 0;
      this.V.i(mh.m(this.K, var1 != 0 ? Math.min(3.0, this.W.radius * 0.16) : this.W.radius, var1 != 0 ? 4.0 : Math.min(20.0, this.W.radius), this.o), this.X);
   }

   private void h(e3.m var1) {
      this.y();
      double var2 = this.o.nextDouble();
      int var4 = var2 < 0.06 ? 1 : 0;
      int var5 = var2 < 0.32 ? 1 : 0;
      double var6 = var4 != 0 ? Math.min(4.0, this.W.radius * 0.2) : (var5 != 0 ? Math.min(9.0, this.W.radius * 0.4) : this.W.radius);
      Vec3 var8 = mh.m(this.K.add(0.0, var4 != 0 ? 1.0 : (var5 != 0 ? 3.0 : 7.0), 0.0), var6, var4 != 0 ? 4.0 : (var5 != 0 ? 12.0 : 22.0), this.o);
      double var9 = 0.55 + this.o.nextDouble() * 0.55;
      double var11 = this.W.wind * 0.35;
      double var13 = this.W.wind * 0.15;
      double var15 = 6.0 + this.o.nextDouble() * 6.0;
      double var17 = var9 * this.W.speed;
      if (var1 != null) {
         double var19 = var1.P((int)Math.floor(var8.x), (int)Math.floor(var8.z));
         if (Double.isFinite(var19) && var8.y < var19 + 0.25) {
            this.G++;
            return;
         }

         if (var17 > 0.05) {
            double var21 = var8.x + var11 * this.W.speed * var15;
            double var23 = var8.z + var13 * this.W.speed * var15;
            double var25 = var1.P((int)Math.floor(var21), (int)Math.floor(var23));
            if (Double.isFinite(var25)) {
               var15 = Math.min(var15, Math.max(0.4, (var8.y - var25) / var17));
            }
         }
      }

      this.X.M = this.X.O = var15;
      this.X.z = var11;
      this.X.K = -var9;
      this.X.v = var13;
      this.X.f = 0.12;
      this.X.Y = 0.2 + this.o.nextDouble() * 0.45;
      this.X.G = 0.06;
      this.X.m = 3.0 + this.o.nextDouble() * 3.5;
      this.X.S = hp.y.H;
      int var27 = var4 == 0 && this.o.nextDouble() < 0.12 ? 1 : 0;
      this.X.P = var27 != 0 ? cg.u.h : cg.u.a;
      this.X.l = var27 != 0 ? hp.u.J : hp.u.F;
      double var20 = this.W.size * (0.02 + this.o.nextDouble() * 0.032) * (var4 != 0 ? 2.6 : (var5 != 0 ? 1.2 : 1.0));
      this.X.C = this.X.d = var20;
      int var22 = _3.D(this.W.primary, this.W.primary & 0xFF000000 | 16777215, 0.35 + this.o.nextDouble() * 0.3);
      this.X.E = _3.U(var22, var4 != 0 ? 0.28 : 0.55 + this.o.nextDouble() * 0.4);
      this.X.N = _3.U(this.W.secondary, var4 != 0 ? 0.12 : 0.35);
      this.V.i(var8, this.X);
   }

   private void k(e3.h var1) {
      this.y();
      this.X.P = cg.u.M;
      this.X.g = 3;
      this.X.M = 0.25;
      this.X.O = 0.55;
      this.X.C = var1.k * 0.65;
      this.X.d = var1.k;
      this.X.Z = 1.4;
      this.X.W = 2.0;
      this.X.q = 0.4;
      this.X.z = var1.g.x * 0.65;
      this.X.K = var1.g.y * 0.65;
      this.X.v = var1.g.z * 0.65;
      this.X.S = hp.y.B;
      this.X.l = hp.u.C;
      this.V.i(var1.j, this.X);
      this.e++;
   }

   private void V(w8.n var1, double var2) {
      this.y();
      this.X.P = cg.u.p;
      this.X.V = var1.normal();
      this.X.M = this.X.O = 0.42;
      this.X.B = Math.clamp(var2 * 7.0, 0.12, 0.45);
      this.X.L = 0.015;
      this.X.S = hp.y.H;
      this.X.l = hp.u.F;
      this.X.E = _3.U(this.W.primary, 0.5);
      this.X.N = _3.U(this.W.secondary, 0.1);
      Vec3 var4 = var1.point().add(var1.normal().scale(0.012));
      this.V.i(var4, this.X);
      this.X.P = cg.u.a;
      this.X.g = 2;
      this.X.M = 0.18;
      this.X.O = 0.32;
      this.X.C = 0.025;
      this.X.d = 0.045;
      this.X.B = 1.0;
      this.X.K = 0.2;
      this.X.Z = 0.35;
      this.X.l = hp.u.C;
      this.V.i(var4, this.X);
      this.T++;
   }

   public void r(Vec3 var1, float var2, Frustum var3) {
      long var4 = kx.G();
      this.m = var1;
      this.N = Math.clamp(var2, 0.0F, 1.0F);
      this.j = 0;
      this.b = (boolean)0;
      this.V.q(var1, this.N, var3);
      if (this.W != null) {
         double var6 = this.d + (this.f - this.d) * this.N;

         for (int var8 = 0; var8 < this.c; var8++) {
            e3.h var9 = this.P[var8];
            Vec3 var10 = var9.u.lerp(var9.j, this.N);
            double var11 = Math.max(0.0, var9.C - 0.05 + this.N * 0.05);
            double var13 = var10.distanceTo(var1);
            double var15 = W(var11, var9.l) * hp.R((this.W.distance - var13) / Math.max(2.0, this.W.distance * 0.18)) * var6;
            if (!(var15 < 0.005)) {
               int var17 = this.W.style == e3.d.w ? 1 : 0;
               Vec3 var18 = var17 != 0 && var9.W > 0 ? var9.S[var9.W - 1] : var10.subtract(var9.g.scale(0.045 + var9.w * 0.025));
               double var19 = Math.max(0.1, var9.k * 3.0);
               if (var3 == null || var3.isVisible(new AABB(var10, var18).inflate(var19))) {
                  e3.z var21 = this.q[this.j++];
                  var21.r = var9.k;
                  var21.f = (boolean)var17;
                  var21.R = _3.U(_3.D(this.W.primary, this.W.primary & 0xFF000000 | 16777215, var17 != 0 ? 0.55 : 0.18), var15 * (var17 != 0 ? 0.95 : 0.6));
                  var21.w = _3.U(this.W.secondary, var15 * (var17 != 0 ? 0.6 : 0.3));
                  if (var21.R >>> 24 == 0 && var21.w >>> 24 == 0) {
                     this.j--;
                  } else {
                     var21.u[0] = var10;
                     var21.p = 1;
                     if (var17 != 0) {
                        for (int var22 = 0; var22 < var9.W; var22++) {
                           var21.u[var21.p++] = var9.S[var22];
                        }
                     } else {
                        var21.u[var21.p++] = var18;
                     }
                  }
               }
            }
         }

         this.b = (boolean)(!this.W.bloom || this.j <= 0 && !this.V.r() ? 0 : 1);
         kx.U("Celestial prepared flights", this.j);
         kx.U("Celestial ribbon segments", this.s());
         kx.U("Celestial culled flights", this.c - this.j);
         kx.u("Celestial prepare", var4);
      }
   }

   public static double W(double var0, double var2) {
      return hp.R(var0 / 0.22) * hp.R((var2 - var0) / 0.45);
   }

   public boolean D() {
      return this.b;
   }

   public boolean S() {
      return this.V.C();
   }

   public void s(ki var1, boolean var2) {
      if (!var2 || this.b) {
         this.V.e(var1, this.m, this.N, var2);
         var1.blend(pf.x.E);

         for (int var3 = 0; var3 < this.j; var3++) {
            e3.z var4 = this.q[var3];

            for (int var5 = 1; var5 < var4.p; var5++) {
               Vec3 var6 = var4.u[var5 - 1];
               Vec3 var7 = var4.u[var5];
               double var8 = 1.0 - (var5 - 1.0) / (var4.p - 1);
               double var10 = 1.0 - (double)var5 / (var4.p - 1);
               double var12 = var4.r * (var4.f ? 0.08 + 0.92 * var8 : 1.0);
               double var14 = var4.r * (var4.f ? 0.08 + 0.92 * var10 : 0.2);
               int var16 = _3.U(_3.D(var4.w, var4.R, var8), var8);
               int var17 = _3.U(_3.D(var4.w, var4.R, var10), var10);
               var1.b(var6.x, var6.y, var6.z, var7.x, var7.y, var7.z, var12 * 3.0, var14 * 3.0, _3.U(var16, 0.15), _3.U(var17, 0.15));
               var1.b(var6.x, var6.y, var6.z, var7.x, var7.y, var7.z, var12, var14, var16, var17);
               if (var4.f) {
                  var1.b(
                     var6.x,
                     var6.y,
                     var6.z,
                     var7.x,
                     var7.y,
                     var7.z,
                     var12 * 0.26,
                     var14 * 0.26,
                     _3.D(var16, var16 & 0xFF000000 | 16777215, 0.75),
                     _3.D(var17, var17 & 0xFF000000 | 16777215, 0.75)
                  );
               }
            }

            if (var4.f) {
               Vec3 var18 = var4.u[0];
               var1.W(var18.x, var18.y, var18.z, var4.r * 1.5, 0.0, var4.R, cg.u.a, 0.0, 0.0, 0.0);
            }
         }

         var1.blend(pf.x.S);
      }
   }

   public int X() {
      return this.c;
   }

   public int d() {
      return this.V.x();
   }

   public int M() {
      int var1 = 0;

      for (int var2 = 0; var2 < this.c; var2++) {
         var1 += this.P[var2].W;
      }

      return var1;
   }

   public int j() {
      return this.j;
   }

   public int s() {
      int var1 = 0;

      for (int var2 = 0; var2 < this.j; var2++) {
         var1 += Math.max(0, this.q[var2].p - 1);
      }

      return var1;
   }

   public int c() {
      return this.S;
   }

   public int H() {
      return this.G;
   }

   public int C() {
      return this.T;
   }

   public int t() {
      return this.e;
   }

   public Vec3 N(int var1) {
      return this.P[var1].j;
   }

   public Vec3 X(int var1) {
      return this.P[var1].g;
   }

   public enum d {
      w,
      p,
      X,
      t;
   }

   private static final class h {
      Vec3 j;
      Vec3 u;
      Vec3 g;
      final Vec3[] S = new Vec3[12];
      int W;
      double C;
      double l;
      double k;
      double w;
      double D;
      double h;
      boolean a;
      boolean E;
      w8.n K;
   }

   public record i(
      e3.d style,
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

   @FunctionalInterface
   public interface m {
      double P(int var1, int var2);
   }

   private static final class z {
      final Vec3[] u = new Vec3[13];
      int p;
      int R;
      int w;
      double r;
      boolean f;
   }
}
