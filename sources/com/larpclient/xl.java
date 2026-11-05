package com.larpclient;

import java.util.Arrays;
import java.util.Random;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class xl {
   private final f4 o = new f4();
   private final xg v = new xg();
   private final Random r = new Random();
   private final Vec3[] c = new Vec3[32];
   private final Vec3[] e = new Vec3[32];
   private final Vec3[] p = new Vec3[32];
   private final Vec3[] x = new Vec3[32];
   private final double[] t = new double[32];
   private final double[] P = new double[32];
   private final double[] l = new double[32];
   private final int[] R = new int[32];
   private final xl.q[] i = new xl.q[32];
   private int A;
   private boolean b;
   private final fk n = new fk();
   private Vec3 g;
   private xl.c T;
   private double X;
   private int m;

   public xl() {
      this.o.P = 1536;
      this.v.D = (boolean)1;
      this.n.p = 1536;
      this.n.g = 4;
      this.n.H = this.n.b = this.n.k = 0.0;
      this.n.N = yc.d.c;
      this.n.U = yc.y.a;
   }

   public void F() {
      this.o.U();
      this.v.m();
      Arrays.fill(this.c, null);
      Arrays.fill(this.e, null);
      Arrays.fill(this.p, null);
      Arrays.fill(this.x, null);
      Arrays.fill(this.t, 0.0);
      Arrays.fill(this.P, 0.0);
      Arrays.fill(this.l, 0.0);
      Arrays.fill(this.R, 0);
      Arrays.fill(this.i, null);
      this.A = 0;
      this.b = (boolean)0;
      this.X = 0.0;
      this.m = 0;
      this.g = null;
      this.T = null;
   }

   private Vec3 Z(Vec3 var1, xl.c var2) {
      return f(var1, var2.radius, var2.height, this.r);
   }

   public static Vec3 f(Vec3 var0, double var1, double var3, Random var5) {
      double var6 = var5.nextDouble() * Math.PI * 2.0;
      double var8 = Math.sqrt(0.04 + var5.nextDouble() * 0.96) * var1;
      return var0.add(Math.cos(var6) * var8, (var5.nextDouble() - 0.5) * var3, Math.sin(var6) * var8);
   }

   public void e(Vec3 var1, xl.c var2) {
      if (this.g != null
         && (
            var1.distanceToSqr(this.g) > 4096.0
               || this.T.sparkles != var2.sparkles
               || this.T.comets != var2.comets
               || this.T.radius != var2.radius
               || this.T.height != var2.height
               || this.T.cometCount != var2.cometCount
               || this.T.maximum != var2.maximum
               || this.T.verticalTravel != var2.verticalTravel
               || this.T.sidewaysTravel != var2.sidewaysTravel
               || this.T.motionPeriod != var2.motionPeriod
         )) {
         this.F();
      }

      int var3 = this.g == null ? 1 : 0;
      this.g = var1;
      this.T = var2;
      this.m++;
      int var4 = var2.comets ? Math.clamp(var2.cometCount, 0, this.c.length) : 0;
      this.o.P = Math.clamp(var2.maximum, 128, 2048) - var4;
      this.o.W(var2.speed);
      this.v.N();
      this.n.B = oh.i(var2.color, var2.opacity);
      this.n.v = oh.i(var2.tailColor, var2.opacity * 0.35);
      this.n.G = var2.bloom;
      this.n.Q = var2.radius * 2.0 + var2.height + 8.0;
      if (var2.sparkles) {
         int var5 = this.o.P - var4 * 3;
         this.n.p = 2048;
         this.X = this.X + var2.rate * 0.05;
         int var6 = (int)this.X + (var3 != 0 ? (int)Math.min(var5, var2.rate * var2.lifetime * 0.825) : 0);
         this.X = this.X - (int)this.X;
         this.n.Y = var2.lifetime * 0.65;
         this.n.d = var2.lifetime;
         this.n.V = var2.size * 0.6;
         this.n.u = var2.size;
         this.n.N = yc.d.c;
         this.n.U = yc.y.a;
         this.n.e = this.n.s = 0.0;
         this.n.K = var2.verticalTravel * 0.5;
         this.n.X = var2.sidewaysTravel * 0.5;
         this.n.T = var2.motionPeriod;
         this.n.t = 1;
         int var7 = Math.max(0, var5 - this.o.j());

         for (int var8 = 0; var8 < Math.min(var7, var6); var8++) {
            this.n.D = (this.m + var8) % 3 == 0 ? f4.a.g : f4.a.u;
            this.o.p(this.Z(var1, var2), this.n);
         }
      }

      for (int var11 = 0; var11 < this.c.length; var11++) {
         if (var11 >= var4) {
            this.c[var11] = this.p[var11] = this.x[var11] = null;
            this.v.S(var11);
         } else {
            if (this.c[var11] != null
               && !(Math.hypot(this.p[var11].x - var1.x, this.p[var11].z - var1.z) > var2.radius * 1.4)
               && !(Math.abs(this.p[var11].y - var1.y) > var2.height * 0.7)
               && !(this.t[var11] > 12.0)) {
               this.x[var11] = this.c[var11];
            } else {
               this.v.S(var11);
               this.p[var11] = this.Z(var1, var2);
               double var12 = this.r.nextDouble() * Math.PI * 2.0;
               this.e[var11] = new Vec3(Math.cos(var12), (this.r.nextDouble() - 0.5) * 0.4, Math.sin(var12));
               this.t[var11] = 0.0;
               this.R[var11] = 0;
               this.x[var11] = null;
               this.P[var11] = this.r.nextDouble() * Math.PI * 2.0;
            }

            this.R[var11]++;
            if (var2.speed > 0.0) {
               this.t[var11] = this.t[var11] + 0.05;
            }

            this.P[var11] = this.P[var11] + 0.05 * var2.speed * Math.PI * 2.0 / var2.motionPeriod;
            this.p[var11] = this.p[var11].add(this.e[var11].scale(var2.speed * 0.01));
            double var13 = this.P[var11];
            this.c[var11] = this.p[var11]
               .add(
                  Math.sin(var13 * 0.83) * var2.sidewaysTravel * 0.5,
                  Math.sin(var13) * var2.verticalTravel * 0.5,
                  Math.cos(var13 * 0.67) * var2.sidewaysTravel * 0.5
               );
            double var14 = this.l[var11] = j(this.R[var11] * 0.05, this.t[var11], this.p[var11], var1, var2);
            xg.v var10 = new xg.v(
               var2.trailLength,
               var2.trailWidth,
               oh.i(var2.color, var2.opacity * var14),
               oh.i(var2.tailColor, var2.opacity * 0.3 * var14),
               var2.bloom,
               true,
               fk.h.u,
               xg.c.N,
               yc.d.u
            );
            this.v.A(var11, this.c[var11], var10);
            this.n.D = f4.a.u;
            this.n.p = 2048;
            this.n.t = 1;
            this.n.Y = this.n.d = 0.15;
            this.n.V = this.n.u = var2.size * 1.6;
            this.n.N = yc.d.u;
            this.n.U = yc.y.J;
            this.n.B = oh.i(var2.color, var2.opacity * var14);
            this.n.v = oh.i(var2.tailColor, var2.opacity * 0.35 * var14);
            this.n.e = this.n.s = 0.0;
            this.n.K = this.n.X = 0.0;
            this.o.p(this.c[var11], this.n);
         }
      }
   }

   public void o(Vec3 var1, float var2, Frustum var3) {
      this.o.j(var1, var2, var3);
      this.v.X(var1, var2);
      this.A = 0;
      this.b = (boolean)0;
      if (this.T != null) {
         double var4 = this.T.radius * 2.0 + this.T.height + 8.0;

         for (int var6 = 0; var6 < this.c.length; var6++) {
            if (this.c[var6] != null) {
               Vec3 var7 = this.x[var6] == null ? this.c[var6] : this.x[var6].lerp(this.c[var6], var2);
               if (!(var7.distanceToSqr(var1) > var4 * var4)) {
                  double var8 = this.T.size * 0.65;
                  if (var3 == null || var3.isVisible(new AABB(var7.x - var8, var7.y - var8, var7.z - var8, var7.x + var8, var7.y + var8, var7.z + var8))) {
                     int var10 = oh.i(this.T.color, this.T.opacity * this.l[var6]);
                     var10 = oh.j(var10, var10 & 0xFF000000 | 16777215, 0.7);
                     if (var10 >>> 24 != 0) {
                        this.i[this.A++] = new xl.q(var7, var8, var10);
                     }
                  }
               }
            }
         }

         this.b = (boolean)(this.A > 0 && this.T.bloom ? 1 : 0);
      }
   }

   public boolean i() {
      return this.o.t() || this.v.Z() || this.b;
   }

   public boolean Z() {
      return this.o.C();
   }

   public int L() {
      return this.o.j() + this.e();
   }

   public int e() {
      int var1 = 0;

      for (Vec3 var5 : this.c) {
         if (var5 != null) {
            var1++;
         }
      }

      return var1;
   }

   public int f() {
      return this.v.t();
   }

   public Vec3 t(int var1) {
      return this.c[var1];
   }

   public Vec3 e(int var1) {
      return this.i[var1].position;
   }

   public int D() {
      return this.A;
   }

   public double o(int var1) {
      return this.l[var1];
   }

   public void Y(r6 var1, Vec3 var2, float var3, boolean var4) {
      this.o.Y(var1, var2, var3, var4);
      this.v.q(var1, var3, var4);
      if (!var4 || this.b) {
         var1.blend(fk.h.u);

         for (int var5 = 0; var5 < this.A; var5++) {
            xl.q var6 = this.i[var5];
            var1.j(var6.position.x, var6.position.y, var6.position.z, var6.size, 0.0, var6.color, f4.a.D, 0.0, 0.0, 0.0);
         }

         var1.blend(fk.h.z);
      }
   }

   private static double j(double var0, double var2, Vec3 var4, Vec3 var5, xl.c var6) {
      double var7 = Math.hypot(var4.x - var5.x, var4.z - var5.z);
      double var9 = Math.abs(var4.y - var5.y);
      return yc.H(var0 / 0.15)
         * yc.H((12.0 - var2) / 0.35)
         * yc.H((var6.radius * 1.4 - var7) / (var6.radius * 0.15))
         * yc.H((var6.height * 0.7 - var9) / (var6.height * 0.1));
   }

   public record c(
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

   private record q(Vec3 position, double size, int color) {
   }
}
