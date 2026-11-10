package com.larpclient;

import java.util.Arrays;
import java.util.Random;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class th {
   private final c3 k = new c3();
   private final ub O = new ub();
   private final Random Y = new Random();
   private final Vec3[] D = new Vec3[32];
   private final Vec3[] j = new Vec3[32];
   private final Vec3[] z = new Vec3[32];
   private final Vec3[] G = new Vec3[32];
   private final double[] r = new double[32];
   private final double[] f = new double[32];
   private final double[] d = new double[32];
   private final int[] L = new int[32];
   private final th.g[] X = new th.g[32];
   private int y;
   private boolean e;
   private final gl h = new gl();
   private Vec3 U;
   private th.n F;
   private double w;
   private int q;

   public th() {
      this.k.a = 1536;
      this.O.l = (boolean)1;
      this.h.L = 1536;
      this.h.F = 4;
      this.h.W = this.h.K = this.h.C = 0.0;
      this.h.M = st.z.C;
      this.h.e = st.c.T;
   }

   public void b() {
      this.k.J();
      this.O.V();
      Arrays.fill(this.D, null);
      Arrays.fill(this.j, null);
      Arrays.fill(this.z, null);
      Arrays.fill(this.G, null);
      Arrays.fill(this.r, 0.0);
      Arrays.fill(this.f, 0.0);
      Arrays.fill(this.d, 0.0);
      Arrays.fill(this.L, 0);
      Arrays.fill(this.X, null);
      this.y = 0;
      this.e = (boolean)0;
      this.w = 0.0;
      this.q = 0;
      this.U = null;
      this.F = null;
   }

   private Vec3 F(Vec3 var1, th.n var2) {
      return U(var1, var2.radius, var2.height, this.Y);
   }

   public static Vec3 U(Vec3 var0, double var1, double var3, Random var5) {
      double var6 = var5.nextDouble() * Math.PI * 2.0;
      double var8 = Math.sqrt(0.04 + var5.nextDouble() * 0.96) * var1;
      return var0.add(Math.cos(var6) * var8, (var5.nextDouble() - 0.5) * var3, Math.sin(var6) * var8);
   }

   public void d(Vec3 var1, th.n var2) {
      if (this.U != null
         && (
            var1.distanceToSqr(this.U) > 4096.0
               || this.F.sparkles != var2.sparkles
               || this.F.comets != var2.comets
               || this.F.radius != var2.radius
               || this.F.height != var2.height
               || this.F.cometCount != var2.cometCount
               || this.F.maximum != var2.maximum
               || this.F.verticalTravel != var2.verticalTravel
               || this.F.sidewaysTravel != var2.sidewaysTravel
               || this.F.motionPeriod != var2.motionPeriod
         )) {
         this.b();
      }

      int var3 = this.U == null ? 1 : 0;
      this.U = var1;
      this.F = var2;
      this.q++;
      int var4 = var2.comets ? Math.clamp(var2.cometCount, 0, this.D.length) : 0;
      this.k.a = Math.clamp(var2.maximum, 128, 2048) - var4;
      this.k.V(var2.speed);
      this.O.w();
      this.h.j = uh.H(var2.color, var2.opacity);
      this.h.Z = uh.H(var2.tailColor, var2.opacity * 0.35);
      this.h.R = var2.bloom;
      this.h.E = var2.radius * 2.0 + var2.height + 8.0;
      if (var2.sparkles) {
         int var5 = this.k.a - var4 * 3;
         this.h.L = 2048;
         this.w = this.w + var2.rate * 0.05;
         int var6 = (int)this.w + (var3 != 0 ? (int)Math.min(var5, var2.rate * var2.lifetime * 0.825) : 0);
         this.w = this.w - (int)this.w;
         this.h.O = var2.lifetime * 0.65;
         this.h.Q = var2.lifetime;
         this.h.n = var2.size * 0.6;
         this.h.f = var2.size;
         this.h.M = st.z.C;
         this.h.e = st.c.T;
         this.h.s = this.h.p = 0.0;
         this.h.G = var2.verticalTravel * 0.5;
         this.h.b = var2.sidewaysTravel * 0.5;
         this.h.J = var2.motionPeriod;
         this.h.u = 1;
         int var7 = Math.max(0, var5 - this.k.G());

         for (int var8 = 0; var8 < Math.min(var7, var6); var8++) {
            this.h.B = (this.q + var8) % 3 == 0 ? c3.a.e : c3.a.Z;
            this.k.S(this.F(var1, var2), this.h);
         }
      }

      for (int var11 = 0; var11 < this.D.length; var11++) {
         if (var11 >= var4) {
            this.D[var11] = this.z[var11] = this.G[var11] = null;
            this.O.L(var11);
         } else {
            if (this.D[var11] != null
               && !(Math.hypot(this.z[var11].x - var1.x, this.z[var11].z - var1.z) > var2.radius * 1.4)
               && !(Math.abs(this.z[var11].y - var1.y) > var2.height * 0.7)
               && !(this.r[var11] > 12.0)) {
               this.G[var11] = this.D[var11];
            } else {
               this.O.L(var11);
               this.z[var11] = this.F(var1, var2);
               double var12 = this.Y.nextDouble() * Math.PI * 2.0;
               this.j[var11] = new Vec3(Math.cos(var12), (this.Y.nextDouble() - 0.5) * 0.4, Math.sin(var12));
               this.r[var11] = 0.0;
               this.L[var11] = 0;
               this.G[var11] = null;
               this.f[var11] = this.Y.nextDouble() * Math.PI * 2.0;
            }

            this.L[var11]++;
            if (var2.speed > 0.0) {
               this.r[var11] = this.r[var11] + 0.05;
            }

            this.f[var11] = this.f[var11] + 0.05 * var2.speed * Math.PI * 2.0 / var2.motionPeriod;
            this.z[var11] = this.z[var11].add(this.j[var11].scale(var2.speed * 0.01));
            double var13 = this.f[var11];
            this.D[var11] = this.z[var11]
               .add(
                  Math.sin(var13 * 0.83) * var2.sidewaysTravel * 0.5,
                  Math.sin(var13) * var2.verticalTravel * 0.5,
                  Math.cos(var13 * 0.67) * var2.sidewaysTravel * 0.5
               );
            double var14 = this.d[var11] = k(this.L[var11] * 0.05, this.r[var11], this.z[var11], var1, var2);
            ub.o var10 = new ub.o(
               var2.trailLength,
               var2.trailWidth,
               uh.H(var2.color, var2.opacity * var14),
               uh.H(var2.tailColor, var2.opacity * 0.3 * var14),
               var2.bloom,
               true,
               gl.c.E,
               ub.f.k,
               st.z.f
            );
            this.O.d(var11, this.D[var11], var10);
            this.h.B = c3.a.Z;
            this.h.L = 2048;
            this.h.u = 1;
            this.h.O = this.h.Q = 0.15;
            this.h.n = this.h.f = var2.size * 1.6;
            this.h.M = st.z.f;
            this.h.e = st.c.Q;
            this.h.j = uh.H(var2.color, var2.opacity * var14);
            this.h.Z = uh.H(var2.tailColor, var2.opacity * 0.35 * var14);
            this.h.s = this.h.p = 0.0;
            this.h.G = this.h.b = 0.0;
            this.k.S(this.D[var11], this.h);
         }
      }
   }

   public void Q(Vec3 var1, float var2, Frustum var3) {
      this.k.B(var1, var2, var3);
      this.O.e(var1, var2);
      this.y = 0;
      this.e = (boolean)0;
      if (this.F != null) {
         double var4 = this.F.radius * 2.0 + this.F.height + 8.0;

         for (int var6 = 0; var6 < this.D.length; var6++) {
            if (this.D[var6] != null) {
               Vec3 var7 = this.G[var6] == null ? this.D[var6] : this.G[var6].lerp(this.D[var6], var2);
               if (!(var7.distanceToSqr(var1) > var4 * var4)) {
                  double var8 = this.F.size * 0.65;
                  if (var3 == null || var3.isVisible(new AABB(var7.x - var8, var7.y - var8, var7.z - var8, var7.x + var8, var7.y + var8, var7.z + var8))) {
                     int var10 = uh.H(this.F.color, this.F.opacity * this.d[var6]);
                     var10 = uh.q(var10, var10 & 0xFF000000 | 16777215, 0.7);
                     if (var10 >>> 24 != 0) {
                        this.X[this.y++] = new th.g(var7, var8, var10);
                     }
                  }
               }
            }
         }

         this.e = (boolean)(this.y > 0 && this.F.bloom ? 1 : 0);
      }
   }

   public boolean g() {
      return this.k.o() || this.O.P() || this.e;
   }

   public boolean p() {
      return this.k.L();
   }

   public int Y() {
      return this.k.G() + this.E();
   }

   public int E() {
      int var1 = 0;

      for (Vec3 var5 : this.D) {
         if (var5 != null) {
            var1++;
         }
      }

      return var1;
   }

   public int p() {
      return this.O.J();
   }

   public Vec3 U(int var1) {
      return this.D[var1];
   }

   public Vec3 B(int var1) {
      return this.X[var1].position;
   }

   public int W() {
      return this.y;
   }

   public double t(int var1) {
      return this.d[var1];
   }

   public void V(kj var1, Vec3 var2, float var3, boolean var4) {
      this.k.G(var1, var2, var3, var4);
      this.O.j(var1, var3, var4);
      if (!var4 || this.e) {
         var1.blend(gl.c.E);

         for (int var5 = 0; var5 < this.y; var5++) {
            th.g var6 = this.X[var5];
            var1.I(var6.position.x, var6.position.y, var6.position.z, var6.size, 0.0, var6.color, c3.a.l, 0.0, 0.0, 0.0);
         }

         var1.blend(gl.c.K);
      }
   }

   private static double k(double var0, double var2, Vec3 var4, Vec3 var5, th.n var6) {
      double var7 = Math.hypot(var4.x - var5.x, var4.z - var5.z);
      double var9 = Math.abs(var4.y - var5.y);
      return st.h(var0 / 0.15)
         * st.h((12.0 - var2) / 0.35)
         * st.h((var6.radius * 1.4 - var7) / (var6.radius * 0.15))
         * st.h((var6.height * 0.7 - var9) / (var6.height * 0.1));
   }

   private record g(Vec3 position, double size, int color) {
   }

   public record n(
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
