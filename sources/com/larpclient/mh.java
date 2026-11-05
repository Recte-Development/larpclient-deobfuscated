package com.larpclient;

import java.util.Arrays;
import java.util.Random;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class mh {
   private final cg h = new cg();
   private final uz c = new uz();
   private final Random T = new Random();
   private final Vec3[] g = new Vec3[32];
   private final Vec3[] X = new Vec3[32];
   private final Vec3[] U = new Vec3[32];
   private final Vec3[] k = new Vec3[32];
   private final double[] w = new double[32];
   private final double[] A = new double[32];
   private final double[] V = new double[32];
   private final int[] R = new int[32];
   private final mh.a[] b = new mh.a[32];
   private int y;
   private boolean r;
   private final pf o = new pf();
   private Vec3 G;
   private mh.r B;
   private double K;
   private int Y;

   public mh() {
      this.h.X = 1536;
      this.c.G = (boolean)1;
      this.o.X = 1536;
      this.o.D = 4;
      this.o.W = this.o.q = this.o.r = 0.0;
      this.o.S = hp.y.e;
      this.o.l = hp.u.J;
   }

   public void w() {
      this.h.J();
      this.c.p();
      Arrays.fill(this.g, null);
      Arrays.fill(this.X, null);
      Arrays.fill(this.U, null);
      Arrays.fill(this.k, null);
      Arrays.fill(this.w, 0.0);
      Arrays.fill(this.A, 0.0);
      Arrays.fill(this.V, 0.0);
      Arrays.fill(this.R, 0);
      Arrays.fill(this.b, null);
      this.y = 0;
      this.r = (boolean)0;
      this.K = 0.0;
      this.Y = 0;
      this.G = null;
      this.B = null;
   }

   private Vec3 Y(Vec3 var1, mh.r var2) {
      return m(var1, var2.radius, var2.height, this.T);
   }

   public static Vec3 m(Vec3 var0, double var1, double var3, Random var5) {
      double var6 = var5.nextDouble() * Math.PI * 2.0;
      double var8 = Math.sqrt(0.04 + var5.nextDouble() * 0.96) * var1;
      return var0.add(Math.cos(var6) * var8, (var5.nextDouble() - 0.5) * var3, Math.sin(var6) * var8);
   }

   public void U(Vec3 var1, mh.r var2) {
      if (this.G != null
         && (
            var1.distanceToSqr(this.G) > 4096.0
               || this.B.sparkles != var2.sparkles
               || this.B.comets != var2.comets
               || this.B.radius != var2.radius
               || this.B.height != var2.height
               || this.B.cometCount != var2.cometCount
               || this.B.maximum != var2.maximum
               || this.B.verticalTravel != var2.verticalTravel
               || this.B.sidewaysTravel != var2.sidewaysTravel
               || this.B.motionPeriod != var2.motionPeriod
         )) {
         this.w();
      }

      int var3 = this.G == null ? 1 : 0;
      this.G = var1;
      this.B = var2;
      this.Y++;
      int var4 = var2.comets ? Math.clamp(var2.cometCount, 0, this.g.length) : 0;
      this.h.X = Math.clamp(var2.maximum, 128, 2048) - var4;
      this.h.Y(var2.speed);
      this.c.u();
      this.o.E = _3.A(var2.color, var2.opacity);
      this.o.N = _3.A(var2.tailColor, var2.opacity * 0.35);
      this.o.i = var2.bloom;
      this.o.j = var2.radius * 2.0 + var2.height + 8.0;
      if (var2.sparkles) {
         int var5 = this.h.X - var4 * 3;
         this.o.X = 2048;
         this.K = this.K + var2.rate * 0.05;
         int var6 = (int)this.K + (var3 != 0 ? (int)Math.min(var5, var2.rate * var2.lifetime * 0.825) : 0);
         this.K = this.K - (int)this.K;
         this.o.M = var2.lifetime * 0.65;
         this.o.O = var2.lifetime;
         this.o.C = var2.size * 0.6;
         this.o.d = var2.size;
         this.o.S = hp.y.e;
         this.o.l = hp.u.J;
         this.o.Z = this.o.f = 0.0;
         this.o.G = var2.verticalTravel * 0.5;
         this.o.Y = var2.sidewaysTravel * 0.5;
         this.o.m = var2.motionPeriod;
         this.o.g = 1;
         int var7 = Math.max(0, var5 - this.h.x());

         for (int var8 = 0; var8 < Math.min(var7, var6); var8++) {
            this.o.P = (this.Y + var8) % 3 == 0 ? cg.u.h : cg.u.a;
            this.h.i(this.Y(var1, var2), this.o);
         }
      }

      for (int var11 = 0; var11 < this.g.length; var11++) {
         if (var11 >= var4) {
            this.g[var11] = this.U[var11] = this.k[var11] = null;
            this.c.u(var11);
         } else {
            if (this.g[var11] != null
               && !(Math.hypot(this.U[var11].x - var1.x, this.U[var11].z - var1.z) > var2.radius * 1.4)
               && !(Math.abs(this.U[var11].y - var1.y) > var2.height * 0.7)
               && !(this.w[var11] > 12.0)) {
               this.k[var11] = this.g[var11];
            } else {
               this.c.u(var11);
               this.U[var11] = this.Y(var1, var2);
               double var12 = this.T.nextDouble() * Math.PI * 2.0;
               this.X[var11] = new Vec3(Math.cos(var12), (this.T.nextDouble() - 0.5) * 0.4, Math.sin(var12));
               this.w[var11] = 0.0;
               this.R[var11] = 0;
               this.k[var11] = null;
               this.A[var11] = this.T.nextDouble() * Math.PI * 2.0;
            }

            this.R[var11]++;
            if (var2.speed > 0.0) {
               this.w[var11] = this.w[var11] + 0.05;
            }

            this.A[var11] = this.A[var11] + 0.05 * var2.speed * Math.PI * 2.0 / var2.motionPeriod;
            this.U[var11] = this.U[var11].add(this.X[var11].scale(var2.speed * 0.01));
            double var13 = this.A[var11];
            this.g[var11] = this.U[var11]
               .add(
                  Math.sin(var13 * 0.83) * var2.sidewaysTravel * 0.5,
                  Math.sin(var13) * var2.verticalTravel * 0.5,
                  Math.cos(var13 * 0.67) * var2.sidewaysTravel * 0.5
               );
            double var14 = this.V[var11] = L(this.R[var11] * 0.05, this.w[var11], this.U[var11], var1, var2);
            uz.q var10 = new uz.q(
               var2.trailLength,
               var2.trailWidth,
               _3.A(var2.color, var2.opacity * var14),
               _3.A(var2.tailColor, var2.opacity * 0.3 * var14),
               var2.bloom,
               true,
               pf.x.E,
               uz.i.H,
               hp.y.B
            );
            this.c.B(var11, this.g[var11], var10);
            this.o.P = cg.u.a;
            this.o.X = 2048;
            this.o.g = 1;
            this.o.M = this.o.O = 0.15;
            this.o.C = this.o.d = var2.size * 1.6;
            this.o.S = hp.y.B;
            this.o.l = hp.u.C;
            this.o.E = _3.A(var2.color, var2.opacity * var14);
            this.o.N = _3.A(var2.tailColor, var2.opacity * 0.35 * var14);
            this.o.Z = this.o.f = 0.0;
            this.o.G = this.o.Y = 0.0;
            this.h.i(this.g[var11], this.o);
         }
      }
   }

   public void G(Vec3 var1, float var2, Frustum var3) {
      this.h.q(var1, var2, var3);
      this.c.P(var1, var2);
      this.y = 0;
      this.r = (boolean)0;
      if (this.B != null) {
         double var4 = this.B.radius * 2.0 + this.B.height + 8.0;

         for (int var6 = 0; var6 < this.g.length; var6++) {
            if (this.g[var6] != null) {
               Vec3 var7 = this.k[var6] == null ? this.g[var6] : this.k[var6].lerp(this.g[var6], var2);
               if (!(var7.distanceToSqr(var1) > var4 * var4)) {
                  double var8 = this.B.size * 0.65;
                  if (var3 == null || var3.isVisible(new AABB(var7.x - var8, var7.y - var8, var7.z - var8, var7.x + var8, var7.y + var8, var7.z + var8))) {
                     int var10 = _3.A(this.B.color, this.B.opacity * this.V[var6]);
                     var10 = _3.D(var10, var10 & 0xFF000000 | 16777215, 0.7);
                     if (var10 >>> 24 != 0) {
                        this.b[this.y++] = new mh.a(var7, var8, var10);
                     }
                  }
               }
            }
         }

         this.r = (boolean)(this.y > 0 && this.B.bloom ? 1 : 0);
      }
   }

   public boolean N() {
      return this.h.r() || this.c.f() || this.r;
   }

   public boolean P() {
      return this.h.C();
   }

   public int b() {
      return this.h.x() + this.S();
   }

   public int S() {
      int var1 = 0;

      for (Vec3 var5 : this.g) {
         if (var5 != null) {
            var1++;
         }
      }

      return var1;
   }

   public int i() {
      return this.c.S();
   }

   public Vec3 l(int var1) {
      return this.g[var1];
   }

   public Vec3 E(int var1) {
      return this.b[var1].position;
   }

   public int d() {
      return this.y;
   }

   public double O(int var1) {
      return this.V[var1];
   }

   public void n(ki var1, Vec3 var2, float var3, boolean var4) {
      this.h.e(var1, var2, var3, var4);
      this.c.v(var1, var3, var4);
      if (!var4 || this.r) {
         var1.blend(pf.x.E);

         for (int var5 = 0; var5 < this.y; var5++) {
            mh.a var6 = this.b[var5];
            var1.W(var6.position.x, var6.position.y, var6.position.z, var6.size, 0.0, var6.color, cg.u.n, 0.0, 0.0, 0.0);
         }

         var1.blend(pf.x.S);
      }
   }

   private static double L(double var0, double var2, Vec3 var4, Vec3 var5, mh.r var6) {
      double var7 = Math.hypot(var4.x - var5.x, var4.z - var5.z);
      double var9 = Math.abs(var4.y - var5.y);
      return hp.R(var0 / 0.15)
         * hp.R((12.0 - var2) / 0.35)
         * hp.R((var6.radius * 1.4 - var7) / (var6.radius * 0.15))
         * hp.R((var6.height * 0.7 - var9) / (var6.height * 0.1));
   }

   private record a(Vec3 position, double size, int color) {
   }

   public record r(
      boolean sparkles,
      boolean comets,
      double radius,
      double height,
      double rate,
      double lifetime,
      double speed,
      double size,
      int cometCount,
      double trailLength,
      double trailWidth,
      int color,
      int tailColor,
      double opacity,
      boolean bloom,
      double verticalTravel,
      double sidewaysTravel,
      double motionPeriod,
      int maximum
   ) {
   }
}
