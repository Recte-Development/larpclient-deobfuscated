package com.larpclient;

import java.util.Arrays;
import java.util.Random;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class o8 {
   private final com.larpclient.l p = new com.larpclient.l();
   private final vw w = new vw();
   private final Random Q = new Random();
   private final Vec3[] r = new Vec3[32];
   private final Vec3[] b = new Vec3[32];
   private final Vec3[] V = new Vec3[32];
   private final Vec3[] P = new Vec3[32];
   private final double[] A = new double[32];
   private final double[] E = new double[32];
   private final double[] C = new double[32];
   private final int[] J = new int[32];
   private final o8.l[] X = new o8.l[32];
   private int K;
   private boolean O;
   private final _0 t = new _0();
   private Vec3 f;
   private o8.f M;
   private double c;
   private int S;

   public o8() {
      this.p.I = 1536;
      this.w.g = (boolean)1;
      this.t.A = 1536;
      this.t.r = 4;
      this.t.n = this.t.q = this.t.U = 0.0;
      this.t.C = pr.b.M;
      this.t.T = pr.g.A;
   }

   public void V() {
      this.p.k();
      this.w.Y();
      Arrays.fill(this.r, null);
      Arrays.fill(this.b, null);
      Arrays.fill(this.V, null);
      Arrays.fill(this.P, null);
      Arrays.fill(this.A, 0.0);
      Arrays.fill(this.E, 0.0);
      Arrays.fill(this.C, 0.0);
      Arrays.fill(this.J, 0);
      Arrays.fill(this.X, null);
      this.K = 0;
      this.O = (boolean)0;
      this.c = 0.0;
      this.S = 0;
      this.f = null;
      this.M = null;
   }

   private Vec3 P(Vec3 var1, o8.f var2) {
      return e(var1, var2.radius, var2.height, this.Q);
   }

   public static Vec3 e(Vec3 var0, double var1, double var3, Random var5) {
      double var6 = var5.nextDouble() * Math.PI * 2.0;
      double var8 = Math.sqrt(0.04 + var5.nextDouble() * 0.96) * var1;
      return var0.add(Math.cos(var6) * var8, (var5.nextDouble() - 0.5) * var3, Math.sin(var6) * var8);
   }

   public void A(Vec3 var1, o8.f var2) {
      if (this.f != null
         && (
            var1.distanceToSqr(this.f) > 4096.0
               || this.M.sparkles != var2.sparkles
               || this.M.comets != var2.comets
               || this.M.radius != var2.radius
               || this.M.height != var2.height
               || this.M.cometCount != var2.cometCount
               || this.M.maximum != var2.maximum
               || this.M.verticalTravel != var2.verticalTravel
               || this.M.sidewaysTravel != var2.sidewaysTravel
               || this.M.motionPeriod != var2.motionPeriod
         )) {
         this.V();
      }

      int var3 = this.f == null ? 1 : 0;
      this.f = var1;
      this.M = var2;
      this.S++;
      int var4 = var2.comets ? Math.clamp(var2.cometCount, 0, this.r.length) : 0;
      this.p.I = Math.clamp(var2.maximum, 128, 2048) - var4;
      this.p.A(var2.speed);
      this.w.l();
      this.t.F = vv.N(var2.color, var2.opacity);
      this.t.t = vv.N(var2.tailColor, var2.opacity * 0.35);
      this.t.M = var2.bloom;
      this.t.k = var2.radius * 2.0 + var2.height + 8.0;
      if (var2.sparkles) {
         int var5 = this.p.I - var4 * 3;
         this.t.A = 2048;
         this.c = this.c + var2.rate * 0.05;
         int var6 = (int)this.c + (var3 != 0 ? (int)Math.min(var5, var2.rate * var2.lifetime * 0.825) : 0);
         this.c = this.c - (int)this.c;
         this.t.Q = var2.lifetime * 0.65;
         this.t.W = var2.lifetime;
         this.t.i = var2.size * 0.6;
         this.t.m = var2.size;
         this.t.C = pr.b.M;
         this.t.T = pr.g.A;
         this.t.I = this.t.K = 0.0;
         this.t.y = var2.verticalTravel * 0.5;
         this.t.J = var2.sidewaysTravel * 0.5;
         this.t.d = var2.motionPeriod;
         this.t.w = 1;
         int var7 = Math.max(0, var5 - this.p.K());

         for (int var8 = 0; var8 < Math.min(var7, var6); var8++) {
            this.t.L = (this.S + var8) % 3 == 0 ? com.larpclient.l.b.Y : com.larpclient.l.b.P;
            this.p.Q(this.P(var1, var2), this.t);
         }
      }

      for (int var11 = 0; var11 < this.r.length; var11++) {
         if (var11 >= var4) {
            this.r[var11] = this.V[var11] = this.P[var11] = null;
            this.w.v(var11);
         } else {
            if (this.r[var11] != null
               && !(Math.hypot(this.V[var11].x - var1.x, this.V[var11].z - var1.z) > var2.radius * 1.4)
               && !(Math.abs(this.V[var11].y - var1.y) > var2.height * 0.7)
               && !(this.A[var11] > 12.0)) {
               this.P[var11] = this.r[var11];
            } else {
               this.w.v(var11);
               this.V[var11] = this.P(var1, var2);
               double var12 = this.Q.nextDouble() * Math.PI * 2.0;
               this.b[var11] = new Vec3(Math.cos(var12), (this.Q.nextDouble() - 0.5) * 0.4, Math.sin(var12));
               this.A[var11] = 0.0;
               this.J[var11] = 0;
               this.P[var11] = null;
               this.E[var11] = this.Q.nextDouble() * Math.PI * 2.0;
            }

            this.J[var11]++;
            if (var2.speed > 0.0) {
               this.A[var11] = this.A[var11] + 0.05;
            }

            this.E[var11] = this.E[var11] + 0.05 * var2.speed * Math.PI * 2.0 / var2.motionPeriod;
            this.V[var11] = this.V[var11].add(this.b[var11].scale(var2.speed * 0.01));
            double var13 = this.E[var11];
            this.r[var11] = this.V[var11]
               .add(
                  Math.sin(var13 * 0.83) * var2.sidewaysTravel * 0.5,
                  Math.sin(var13) * var2.verticalTravel * 0.5,
                  Math.cos(var13 * 0.67) * var2.sidewaysTravel * 0.5
               );
            double var14 = this.C[var11] = C(this.J[var11] * 0.05, this.A[var11], this.V[var11], var1, var2);
            vw.j var10 = new vw.j(
               var2.trailLength,
               var2.trailWidth,
               vv.N(var2.color, var2.opacity * var14),
               vv.N(var2.tailColor, var2.opacity * 0.3 * var14),
               var2.bloom,
               true,
               _0.v.c,
               vw.p.z,
               pr.b.A
            );
            this.w.m(var11, this.r[var11], var10);
            this.t.L = com.larpclient.l.b.P;
            this.t.A = 2048;
            this.t.w = 1;
            this.t.Q = this.t.W = 0.15;
            this.t.i = this.t.m = var2.size * 1.6;
            this.t.C = pr.b.A;
            this.t.T = pr.g.U;
            this.t.F = vv.N(var2.color, var2.opacity * var14);
            this.t.t = vv.N(var2.tailColor, var2.opacity * 0.35 * var14);
            this.t.I = this.t.K = 0.0;
            this.t.y = this.t.J = 0.0;
            this.p.Q(this.r[var11], this.t);
         }
      }
   }

   public void Q(Vec3 var1, float var2, Frustum var3) {
      this.p.p(var1, var2, var3);
      this.w.J(var1, var2);
      this.K = 0;
      this.O = (boolean)0;
      if (this.M != null) {
         double var4 = this.M.radius * 2.0 + this.M.height + 8.0;

         for (int var6 = 0; var6 < this.r.length; var6++) {
            if (this.r[var6] != null) {
               Vec3 var7 = this.P[var6] == null ? this.r[var6] : this.P[var6].lerp(this.r[var6], var2);
               if (!(var7.distanceToSqr(var1) > var4 * var4)) {
                  double var8 = this.M.size * 0.65;
                  if (var3 == null || var3.isVisible(new AABB(var7.x - var8, var7.y - var8, var7.z - var8, var7.x + var8, var7.y + var8, var7.z + var8))) {
                     int var10 = vv.N(this.M.color, this.M.opacity * this.C[var6]);
                     var10 = vv.Q(var10, var10 & 0xFF000000 | 16777215, 0.7);
                     if (var10 >>> 24 != 0) {
                        this.X[this.K++] = new o8.l(var7, var8, var10);
                     }
                  }
               }
            }
         }

         this.O = (boolean)(this.K > 0 && this.M.bloom ? 1 : 0);
      }
   }

   public boolean e() {
      return this.p.d() || this.w.S() || this.O;
   }

   public boolean F() {
      return this.p.J();
   }

   public int m() {
      return this.p.K() + this.B();
   }

   public int B() {
      int var1 = 0;

      for (Vec3 var5 : this.r) {
         if (var5 != null) {
            var1++;
         }
      }

      return var1;
   }

   public int i() {
      return this.w.K();
   }

   public Vec3 m(int var1) {
      return this.r[var1];
   }

   public Vec3 X(int var1) {
      return this.X[var1].position;
   }

   public int z() {
      return this.K;
   }

   public double V(int var1) {
      return this.C[var1];
   }

   public void w(fz var1, Vec3 var2, float var3, boolean var4) {
      this.p.z(var1, var2, var3, var4);
      this.w.Q(var1, var3, var4);
      if (!var4 || this.O) {
         var1.blend(_0.v.c);

         for (int var5 = 0; var5 < this.K; var5++) {
            o8.l var6 = this.X[var5];
            var1.a(var6.position.x, var6.position.y, var6.position.z, var6.size, 0.0, var6.color, com.larpclient.l.b.y, 0.0, 0.0, 0.0);
         }

         var1.blend(_0.v.z);
      }
   }

   private static double C(double var0, double var2, Vec3 var4, Vec3 var5, o8.f var6) {
      double var7 = Math.hypot(var4.x - var5.x, var4.z - var5.z);
      double var9 = Math.abs(var4.y - var5.y);
      return pr.k(var0 / 0.15)
         * pr.k((12.0 - var2) / 0.35)
         * pr.k((var6.radius * 1.4 - var7) / (var6.radius * 0.15))
         * pr.k((var6.height * 0.7 - var9) / (var6.height * 0.1));
   }

   public record f(
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

   private record l(Vec3 position, double size, int color) {
   }
}
