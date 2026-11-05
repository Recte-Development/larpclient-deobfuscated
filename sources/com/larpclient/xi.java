package com.larpclient;

import java.util.Arrays;
import java.util.Random;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class xi {
   public static final int O = 256;
   public static final int r = 32;
   public static final int a = 12;
   public static final int y = 1024;
   public static final int P = 4;
   private static final double S = 0.05;
   private final Random M;
   private final f4 E = new f4();
   private final fk k = new fk();
   private final xi.g[] A = new xi.g[256];
   private final xi.s[] G = new xi.s[256];
   private int N;
   private int H;
   private int d;
   private int B;
   private int J;
   private int L;
   private int s;
   private boolean j;
   private double i;
   private double n;
   private double R;
   private Vec3 F;
   private Vec3 m = Vec3.ZERO;
   private xi.j z;
   private boolean Q;
   private float g;

   public xi() {
      this(new Random());
   }

   public xi(Random var1) {
      this.M = var1;
      this.E.P = 1024;
      this.k.p = 2048;
      this.k.g = 7;

      for (int var2 = 0; var2 < this.A.length; var2++) {
         this.A[var2] = new xi.g();
         this.G[var2] = new xi.s();
      }
   }

   public void J() {
      for (xi.g var4 : this.A) {
         Arrays.fill(var4.r, null);
         var4.I = var4.y = var4.b = null;
         var4.O = null;
      }

      for (xi.s var8 : this.G) {
         Arrays.fill(var8.B, null);
         var8.H = 0;
      }

      this.E.U();
      this.N = this.H = this.d = this.B = this.J = this.L = 0;
      this.i = this.n = this.R = 0.0;
      this.F = null;
      this.z = null;
      this.Q = this.j = (boolean)0;
      this.s = 0;
   }

   public void H(Vec3 var1, xi.j var2, boolean var3, et.l var4) {
      this.Y(var1, var2, var3, var4, null);
   }

   public void Y(Vec3 var1, xi.j var2, boolean var3, et.l var4, xi.t var5) {
      if (this.z != null && (this.z.style != var2.style || this.F.distanceToSqr(var1) > 4096.0)) {
         this.J();
      }

      if (this.z != null && (this.z.speed != var2.speed || this.z.impacts != var2.impacts)) {
         for (int var6 = 0; var6 < this.N; var6++) {
            this.A[var6].O = null;
            this.A[var6].c = 0.0;
         }
      }

      this.F = var1;
      this.z = var2;
      this.B = this.s = 0;
      this.R = this.n;
      this.n = this.n + Math.clamp((var3 ? 1 : 0) - this.n, -0.075, 0.075);
      this.E.W(var2.speed);
      if (var2.impacts && var4 != null && var2.speed > 0.0) {
         int var14 = 0;

         while (var14++ < this.N && this.B < 4 && this.N > 0) {
            if (this.d >= this.N) {
               this.d = 0;
            }

            xi.g var7 = this.A[this.d++];
            if (var7.v && var7.O == null && !(var7.c > var7.i + 0.15)) {
               double var8 = var7.b.length() * var2.speed;
               double var10 = Math.min(var7.W, var7.i + 7.5 / Math.max(0.01, var8));
               Vec3 var12 = var7.I;
               Vec3 var13 = var7.I.add(var7.b.scale((var10 - var7.i) * var2.speed));
               this.B++;
               var7.O = var4.J(var12, var13);
               var7.c = var10;
               if (var7.O != null) {
                  var7.p = var7.i + var12.distanceTo(var7.O.point()) / var8;
               }
            }
         }
      }

      int var15 = 0;

      while (var15 < this.N) {
         xi.g var17 = this.A[var15];
         var17.y = var17.I;
         var17.i += 0.05;
         var17.I = var17.I.add(var17.b.scale(0.05 * var2.speed));
         if (var17.O != null && var17.i >= var17.p) {
            this.i(var17.O, var17.U);
            this.i(var15);
         } else {
            if (var2.style == xi.v.m && !var17.V && var17.i >= var17.W * 0.62) {
               var17.V = (boolean)1;
               if (var17.F > 0.65) {
                  this.P(var17);
                  var17.W = Math.min(var17.W, var17.i + 0.28);
               }
            }

            if (!(var17.i >= var17.W) && !(var17.I.distanceToSqr(this.F) > Math.pow(var2.distance * 1.3, 2.0))) {
               if (var2.style == xi.v.m) {
                  System.arraycopy(var17.r, 0, var17.r, 1, 11);
                  var17.r[0] = var17.y;
                  var17.h = Math.min(12, var17.h + 1);
               }

               var15++;
            } else {
               this.i(var15);
            }
         }
      }

      var15 = var2.style == xi.v.m ? Math.min(32, Math.max(1, var2.maximum * 32 / 256)) : Math.min(var2.maximum, 256);

      double var18 = var2.density * switch (var2.style) {
         case m -> 2.5;
         case A -> 42.0;
         case l -> 62.0;
         case z -> 48.0;
      };
      this.i = Math.min(8.0, this.i + var18 * 0.05 * this.n);
      int var9 = (int)this.i;
      this.i -= var9;
      int var19 = Math.min(928, Math.max(32, var2.maximum * 3));
      if (var2.style == xi.v.A) {
         for (int var11 = 0; var11 < var9 && this.E.j() < var19; var11++) {
            this.n();
         }
      } else if (var2.style == xi.v.z) {
         if (!this.j && this.n >= 1.0) {
            this.j = (boolean)1;
            var9 += (int)Math.min(var19 * 0.6, var18 * 3.0);
         }

         for (int var20 = 0; var20 < var9 && this.E.j() < var19; var20++) {
            this.Q(var5);
         }
      } else {
         for (int var21 = 0; var21 < var9 && this.N < var15; var21++) {
            this.W();
         }
      }

      h_.A("Celestial active flights", this.N);
      h_.A("Celestial sprites", this.E.j());
      h_.A("Celestial terrain queries", this.B);
      if (var2.style == xi.v.z) {
         h_.A("Celestial roofed snow columns", this.s);
      }
   }

   private void i(int var1) {
      xi.g var2 = this.A[var1];
      this.A[var1] = this.A[--this.N];
      this.A[this.N] = var2;
      var2.O = null;
      Arrays.fill(var2.r, null);
      var2.h = 0;
   }

   private void W() {
      xi.g var1 = this.A[this.N++];
      int var2 = this.z.style == xi.v.m ? 1 : 0;
      var1.v = (boolean)(this.z.impacts && this.M.nextDouble() < (var2 != 0 ? 0.23 : 0.16) ? 1 : 0);
      double var3 = 0.65 + (this.M.nextDouble() - 0.5) * (var2 != 0 ? 0.17 : 0.08);
      double var5 = this.z.wind;
      var1.b = var2 != 0
         ? new Vec3(Math.cos(var3) * (18.0 + this.M.nextDouble() * 7.0), -8.0 - this.M.nextDouble() * 4.0, Math.sin(var3) * (18.0 + this.M.nextDouble() * 7.0))
            .add(var5, 0.0, var5 * 0.4)
         : new Vec3(var5 * 0.75, -10.0 - this.M.nextDouble() * 5.0, var5 * 0.3);
      Vec3 var7 = this.F;
      double var8;
      if (var2 != 0) {
         var7 = this.F.add(-var1.b.x * 0.65, var1.v ? 8.0 : Math.min(32.0, this.z.radius * 0.7), -var1.b.z * 0.65);
         var8 = var1.v ? 8.0 : 18.0;
      } else {
         var7 = this.F.add(0.0, 9.0, 0.0);
         var8 = 16.0;
      }

      var1.I = var1.y = xl.f(var7, var1.v ? Math.min(12.0, this.z.radius) : this.z.radius, var8, this.M);
      var1.i = 0.0;
      var1.W = var2 != 0 ? 1.6 + this.M.nextDouble() * 1.8 : 2.3 + this.M.nextDouble() * 1.3;
      var1.U = this.z.size * (var2 != 0 ? 0.055 : 0.014) * (0.65 + this.M.nextDouble() * 0.7);
      var1.F = this.M.nextDouble();
      var1.V = (boolean)0;
      var1.O = null;
      var1.c = 0.0;
      var1.p = Double.POSITIVE_INFINITY;
      var1.h = 0;
   }

   private void m() {
      this.k.t = 1;
      this.k.B = this.z.primary;
      this.k.v = oh.S(this.z.secondary, 0.55);
      this.k.G = this.z.bloom;
      this.k.Q = this.z.distance;
      this.k.h = this.k.x = this.k.W = this.k.e = this.k.k = 0.0;
      this.k.H = this.k.b = this.k.s = this.k.n = this.k.y = 0.0;
      this.k.K = this.k.X = 0.0;
      this.k.N = yc.d.c;
      this.k.U = yc.y.a;
      this.k.D = f4.a.u;
   }

   private void n() {
      this.m();
      this.k.D = this.M.nextDouble() < 0.2 ? f4.a.g : f4.a.u;
      this.k.Y = 3.0;
      this.k.d = 6.0;
      this.k.V = this.z.size * 0.022;
      this.k.u = this.z.size * 0.07;
      this.k.h = this.z.wind * 0.12;
      this.k.W = this.z.wind * 0.05;
      this.k.K = 0.35 + this.M.nextDouble() * 0.5;
      this.k.X = 0.4 + this.M.nextDouble();
      this.k.T = 4.0 + this.M.nextDouble() * 4.0;
      this.k.B = oh.S(oh.j(this.z.primary, this.z.secondary, this.M.nextDouble()), 0.4 + this.M.nextDouble() * 0.5);
      int var1 = this.M.nextDouble() < 0.22 ? 1 : 0;
      this.E.p(xl.f(this.F, var1 != 0 ? Math.min(3.0, this.z.radius * 0.16) : this.z.radius, var1 != 0 ? 4.0 : Math.min(20.0, this.z.radius), this.M), this.k);
   }

   private void Q(xi.t var1) {
      this.m();
      double var2 = this.M.nextDouble();
      int var4 = var2 < 0.06 ? 1 : 0;
      int var5 = var2 < 0.32 ? 1 : 0;
      double var6 = var4 != 0 ? Math.min(4.0, this.z.radius * 0.2) : (var5 != 0 ? Math.min(9.0, this.z.radius * 0.4) : this.z.radius);
      Vec3 var8 = xl.f(this.F.add(0.0, var4 != 0 ? 1.0 : (var5 != 0 ? 3.0 : 7.0), 0.0), var6, var4 != 0 ? 4.0 : (var5 != 0 ? 12.0 : 22.0), this.M);
      double var9 = 0.55 + this.M.nextDouble() * 0.55;
      double var11 = this.z.wind * 0.35;
      double var13 = this.z.wind * 0.15;
      double var15 = 6.0 + this.M.nextDouble() * 6.0;
      double var17 = var9 * this.z.speed;
      if (var1 != null) {
         double var19 = var1.J((int)Math.floor(var8.x), (int)Math.floor(var8.z));
         if (Double.isFinite(var19) && var8.y < var19 + 0.25) {
            this.s++;
            return;
         }

         if (var17 > 0.05) {
            double var21 = var8.x + var11 * this.z.speed * var15;
            double var23 = var8.z + var13 * this.z.speed * var15;
            double var25 = var1.J((int)Math.floor(var21), (int)Math.floor(var23));
            if (Double.isFinite(var25)) {
               var15 = Math.min(var15, Math.max(0.4, (var8.y - var25) / var17));
            }
         }
      }

      this.k.Y = this.k.d = var15;
      this.k.h = var11;
      this.k.x = -var9;
      this.k.W = var13;
      this.k.s = 0.12;
      this.k.X = 0.2 + this.M.nextDouble() * 0.45;
      this.k.K = 0.06;
      this.k.T = 3.0 + this.M.nextDouble() * 3.5;
      this.k.N = yc.d.R;
      int var27 = var4 == 0 && this.M.nextDouble() < 0.12 ? 1 : 0;
      this.k.D = var27 != 0 ? f4.a.g : f4.a.u;
      this.k.U = var27 != 0 ? yc.y.a : yc.y.W;
      double var20 = this.z.size * (0.02 + this.M.nextDouble() * 0.032) * (var4 != 0 ? 2.6 : (var5 != 0 ? 1.2 : 1.0));
      this.k.V = this.k.u = var20;
      int var22 = oh.j(this.z.primary, this.z.primary & 0xFF000000 | 16777215, 0.35 + this.M.nextDouble() * 0.3);
      this.k.B = oh.S(var22, var4 != 0 ? 0.28 : 0.55 + this.M.nextDouble() * 0.4);
      this.k.v = oh.S(this.z.secondary, var4 != 0 ? 0.12 : 0.35);
      this.E.p(var8, this.k);
   }

   private void P(xi.g var1) {
      this.m();
      this.k.D = f4.a.d;
      this.k.t = 3;
      this.k.Y = 0.25;
      this.k.d = 0.55;
      this.k.V = var1.U * 0.65;
      this.k.u = var1.U;
      this.k.e = 1.4;
      this.k.H = 2.0;
      this.k.b = 0.4;
      this.k.h = var1.b.x * 0.65;
      this.k.x = var1.b.y * 0.65;
      this.k.W = var1.b.z * 0.65;
      this.k.N = yc.d.u;
      this.k.U = yc.y.J;
      this.E.p(var1.I, this.k);
      this.L++;
   }

   private void i(et.d var1, double var2) {
      this.m();
      this.k.D = f4.a.U;
      this.k.S = var1.normal();
      this.k.Y = this.k.d = 0.42;
      this.k.o = Math.clamp(var2 * 7.0, 0.12, 0.45);
      this.k.z = 0.015;
      this.k.N = yc.d.R;
      this.k.U = yc.y.W;
      this.k.B = oh.S(this.z.primary, 0.5);
      this.k.v = oh.S(this.z.secondary, 0.1);
      Vec3 var4 = var1.point().add(var1.normal().scale(0.012));
      this.E.p(var4, this.k);
      this.k.D = f4.a.u;
      this.k.t = 2;
      this.k.Y = 0.18;
      this.k.d = 0.32;
      this.k.V = 0.025;
      this.k.u = 0.045;
      this.k.o = 1.0;
      this.k.x = 0.2;
      this.k.e = 0.35;
      this.k.U = yc.y.J;
      this.E.p(var4, this.k);
      this.J++;
   }

   public void k(Vec3 var1, float var2, Frustum var3) {
      long var4 = h_.U();
      this.m = var1;
      this.g = Math.clamp(var2, 0.0F, 1.0F);
      this.H = 0;
      this.Q = (boolean)0;
      this.E.j(var1, this.g, var3);
      if (this.z != null) {
         double var6 = this.R + (this.n - this.R) * this.g;

         for (int var8 = 0; var8 < this.N; var8++) {
            xi.g var9 = this.A[var8];
            Vec3 var10 = var9.y.lerp(var9.I, this.g);
            double var11 = Math.max(0.0, var9.i - 0.05 + this.g * 0.05);
            double var13 = var10.distanceTo(var1);
            double var15 = E(var11, var9.W) * yc.H((this.z.distance - var13) / Math.max(2.0, this.z.distance * 0.18)) * var6;
            if (!(var15 < 0.005)) {
               int var17 = this.z.style == xi.v.m ? 1 : 0;
               Vec3 var18 = var17 != 0 && var9.h > 0 ? var9.r[var9.h - 1] : var10.subtract(var9.b.scale(0.045 + var9.F * 0.025));
               double var19 = Math.max(0.1, var9.U * 3.0);
               if (var3 == null || var3.isVisible(new AABB(var10, var18).inflate(var19))) {
                  xi.s var21 = this.G[this.H++];
                  var21.z = var9.U;
                  var21.g = (boolean)var17;
                  var21.i = oh.S(oh.j(this.z.primary, this.z.primary & 0xFF000000 | 16777215, var17 != 0 ? 0.55 : 0.18), var15 * (var17 != 0 ? 0.95 : 0.6));
                  var21.V = oh.S(this.z.secondary, var15 * (var17 != 0 ? 0.6 : 0.3));
                  if (var21.i >>> 24 == 0 && var21.V >>> 24 == 0) {
                     this.H--;
                  } else {
                     var21.B[0] = var10;
                     var21.H = 1;
                     if (var17 != 0) {
                        for (int var22 = 0; var22 < var9.h; var22++) {
                           var21.B[var21.H++] = var9.r[var22];
                        }
                     } else {
                        var21.B[var21.H++] = var18;
                     }
                  }
               }
            }
         }

         this.Q = (boolean)(!this.z.bloom || this.H <= 0 && !this.E.t() ? 0 : 1);
         h_.A("Celestial prepared flights", this.H);
         h_.A("Celestial ribbon segments", this.Q());
         h_.A("Celestial culled flights", this.N - this.H);
         h_.n("Celestial prepare", var4);
      }
   }

   public static double E(double var0, double var2) {
      return yc.H(var0 / 0.22) * yc.H((var2 - var0) / 0.45);
   }

   public boolean W() {
      return this.Q;
   }

   public boolean x() {
      return this.E.C();
   }

   public void y(r6 var1, boolean var2) {
      if (!var2 || this.Q) {
         this.E.Y(var1, this.m, this.g, var2);
         var1.blend(fk.h.u);

         for (int var3 = 0; var3 < this.H; var3++) {
            xi.s var4 = this.G[var3];

            for (int var5 = 1; var5 < var4.H; var5++) {
               Vec3 var6 = var4.B[var5 - 1];
               Vec3 var7 = var4.B[var5];
               double var8 = 1.0 - (var5 - 1.0) / (var4.H - 1);
               double var10 = 1.0 - (double)var5 / (var4.H - 1);
               double var12 = var4.z * (var4.g ? 0.08 + 0.92 * var8 : 1.0);
               double var14 = var4.z * (var4.g ? 0.08 + 0.92 * var10 : 0.2);
               int var16 = oh.S(oh.j(var4.V, var4.i, var8), var8);
               int var17 = oh.S(oh.j(var4.V, var4.i, var10), var10);
               var1.p(var6.x, var6.y, var6.z, var7.x, var7.y, var7.z, var12 * 3.0, var14 * 3.0, oh.S(var16, 0.15), oh.S(var17, 0.15));
               var1.p(var6.x, var6.y, var6.z, var7.x, var7.y, var7.z, var12, var14, var16, var17);
               if (var4.g) {
                  var1.p(
                     var6.x,
                     var6.y,
                     var6.z,
                     var7.x,
                     var7.y,
                     var7.z,
                     var12 * 0.26,
                     var14 * 0.26,
                     oh.j(var16, var16 & 0xFF000000 | 16777215, 0.75),
                     oh.j(var17, var17 & 0xFF000000 | 16777215, 0.75)
                  );
               }
            }

            if (var4.g) {
               Vec3 var18 = var4.B[0];
               var1.j(var18.x, var18.y, var18.z, var4.z * 1.5, 0.0, var4.i, f4.a.u, 0.0, 0.0, 0.0);
            }
         }

         var1.blend(fk.h.z);
      }
   }

   public int M() {
      return this.N;
   }

   public int G() {
      return this.E.j();
   }

   public int S() {
      int var1 = 0;

      for (int var2 = 0; var2 < this.N; var2++) {
         var1 += this.A[var2].h;
      }

      return var1;
   }

   public int j() {
      return this.H;
   }

   public int Q() {
      int var1 = 0;

      for (int var2 = 0; var2 < this.H; var2++) {
         var1 += Math.max(0, this.G[var2].H - 1);
      }

      return var1;
   }

   public int X() {
      return this.B;
   }

   public int P() {
      return this.s;
   }

   public int U() {
      return this.J;
   }

   public int a() {
      return this.L;
   }

   public Vec3 G(int var1) {
      return this.A[var1].I;
   }

   public Vec3 C(int var1) {
      return this.A[var1].b;
   }

   private static final class g {
      Vec3 I;
      Vec3 y;
      Vec3 b;
      final Vec3[] r = new Vec3[12];
      int h;
      double i;
      double W;
      double U;
      double F;
      double c;
      double p;
      boolean v;
      boolean V;
      et.d O;
   }

   public record j(
      xi.v style,
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

   private static final class s {
      final Vec3[] B = new Vec3[13];
      int H;
      int i;
      int V;
      double z;
      boolean g;
   }

   @FunctionalInterface
   public interface t {
      double J(int var1, int var2);
   }

   public enum v {
      m,
      A,
      l,
      z;
   }
}
