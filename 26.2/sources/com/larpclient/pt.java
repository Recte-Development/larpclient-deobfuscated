package com.larpclient;

import java.util.Arrays;
import java.util.Random;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class pt {
   public static final int p = 256;
   public static final int E = 32;
   public static final int X = 12;
   public static final int t = 1024;
   public static final int j = 4;
   private static final double N = 0.05;
   private final Random y;
   private final c3 L = new c3();
   private final gl S = new gl();
   private final pt.f[] w = new pt.f[256];
   private final pt.w[] F = new pt.w[256];
   private int H;
   private int D;
   private int e;
   private int J;
   private int x;
   private int o;
   private int a;
   private boolean h;
   private double c;
   private double O;
   private double Z;
   private Vec3 W;
   private Vec3 s = Vec3.ZERO;
   private pt.s M;
   private boolean l;
   private float K;

   public pt() {
      this(new Random());
   }

   public pt(Random var1) {
      this.y = var1;
      this.L.a = 1024;
      this.S.L = 2048;
      this.S.F = 7;

      for (int var2 = 0; var2 < this.w.length; var2++) {
         this.w[var2] = new pt.f();
         this.F[var2] = new pt.w();
      }
   }

   public void y() {
      for (pt.f var4 : this.w) {
         Arrays.fill(var4.B, null);
         var4.I = var4.k = var4.h = null;
         var4.y = null;
      }

      for (pt.w var8 : this.F) {
         Arrays.fill(var8.z, null);
         var8.G = 0;
      }

      this.L.J();
      this.H = this.D = this.e = this.J = this.x = this.o = 0;
      this.c = this.O = this.Z = 0.0;
      this.W = null;
      this.M = null;
      this.l = this.h = (boolean)0;
      this.a = 0;
   }

   public void J(Vec3 var1, pt.s var2, boolean var3, b5.c var4) {
      this.K(var1, var2, var3, var4, null);
   }

   public void K(Vec3 var1, pt.s var2, boolean var3, b5.c var4, pt.b var5) {
      if (this.M != null && (this.M.style != var2.style || this.W.distanceToSqr(var1) > 4096.0)) {
         this.y();
      }

      if (this.M != null && (this.M.speed != var2.speed || this.M.impacts != var2.impacts)) {
         for (int var6 = 0; var6 < this.H; var6++) {
            this.w[var6].y = null;
            this.w[var6].a = 0.0;
         }
      }

      this.W = var1;
      this.M = var2;
      this.J = this.a = 0;
      this.Z = this.O;
      this.O = this.O + Math.clamp((var3 ? 1 : 0) - this.O, -0.075, 0.075);
      this.L.V(var2.speed);
      if (var2.impacts && var4 != null && var2.speed > 0.0) {
         int var14 = 0;

         while (var14++ < this.H && this.J < 4 && this.H > 0) {
            if (this.e >= this.H) {
               this.e = 0;
            }

            pt.f var7 = this.w[this.e++];
            if (var7.d && var7.y == null && !(var7.a > var7.c + 0.15)) {
               double var8 = var7.h.length() * var2.speed;
               double var10 = Math.min(var7.V, var7.c + 7.5 / Math.max(0.01, var8));
               Vec3 var12 = var7.I;
               Vec3 var13 = var7.I.add(var7.h.scale((var10 - var7.c) * var2.speed));
               this.J++;
               var7.y = var4.p(var12, var13);
               var7.a = var10;
               if (var7.y != null) {
                  var7.n = var7.c + var12.distanceTo(var7.y.point()) / var8;
               }
            }
         }
      }

      int var15 = 0;

      while (var15 < this.H) {
         pt.f var17 = this.w[var15];
         var17.k = var17.I;
         var17.c += 0.05;
         var17.I = var17.I.add(var17.h.scale(0.05 * var2.speed));
         if (var17.y != null && var17.c >= var17.n) {
            this.E(var17.y, var17.p);
            this.G(var15);
         } else {
            if (var2.style == pt.l.Q && !var17.i && var17.c >= var17.V * 0.62) {
               var17.i = (boolean)1;
               if (var17.C > 0.65) {
                  this.b(var17);
                  var17.V = Math.min(var17.V, var17.c + 0.28);
               }
            }

            if (!(var17.c >= var17.V) && !(var17.I.distanceToSqr(this.W) > Math.pow(var2.distance * 1.3, 2.0))) {
               if (var2.style == pt.l.Q) {
                  System.arraycopy(var17.B, 0, var17.B, 1, 11);
                  var17.B[0] = var17.k;
                  var17.T = Math.min(12, var17.T + 1);
               }

               var15++;
            } else {
               this.G(var15);
            }
         }
      }

      var15 = var2.style == pt.l.Q ? Math.min(32, Math.max(1, var2.maximum * 32 / 256)) : Math.min(var2.maximum, 256);

      double var18 = var2.density * switch (var2.style) {
         case Q -> 2.5;
         case u -> 42.0;
         case R -> 62.0;
         case a -> 48.0;
      };
      this.c = Math.min(8.0, this.c + var18 * 0.05 * this.O);
      int var9 = (int)this.c;
      this.c -= var9;
      int var19 = Math.min(928, Math.max(32, var2.maximum * 3));
      if (var2.style == pt.l.u) {
         for (int var11 = 0; var11 < var9 && this.L.G() < var19; var11++) {
            this.w();
         }
      } else if (var2.style == pt.l.a) {
         if (!this.h && this.O >= 1.0) {
            this.h = (boolean)1;
            var9 += (int)Math.min(var19 * 0.6, var18 * 3.0);
         }

         for (int var20 = 0; var20 < var9 && this.L.G() < var19; var20++) {
            this.P(var5);
         }
      } else {
         for (int var21 = 0; var21 < var9 && this.H < var15; var21++) {
            this.E();
         }
      }

      zj.K("Celestial active flights", this.H);
      zj.K("Celestial sprites", this.L.G());
      zj.K("Celestial terrain queries", this.J);
      if (var2.style == pt.l.a) {
         zj.K("Celestial roofed snow columns", this.a);
      }
   }

   private void G(int var1) {
      pt.f var2 = this.w[var1];
      this.w[var1] = this.w[--this.H];
      this.w[this.H] = var2;
      var2.y = null;
      Arrays.fill(var2.B, null);
      var2.T = 0;
   }

   private void E() {
      pt.f var1 = this.w[this.H++];
      int var2 = this.M.style == pt.l.Q ? 1 : 0;
      var1.d = (boolean)(this.M.impacts && this.y.nextDouble() < (var2 != 0 ? 0.23 : 0.16) ? 1 : 0);
      double var3 = 0.65 + (this.y.nextDouble() - 0.5) * (var2 != 0 ? 0.17 : 0.08);
      double var5 = this.M.wind;
      var1.h = var2 != 0
         ? new Vec3(Math.cos(var3) * (18.0 + this.y.nextDouble() * 7.0), -8.0 - this.y.nextDouble() * 4.0, Math.sin(var3) * (18.0 + this.y.nextDouble() * 7.0))
            .add(var5, 0.0, var5 * 0.4)
         : new Vec3(var5 * 0.75, -10.0 - this.y.nextDouble() * 5.0, var5 * 0.3);
      Vec3 var7 = this.W;
      double var8;
      if (var2 != 0) {
         var7 = this.W.add(-var1.h.x * 0.65, var1.d ? 8.0 : Math.min(32.0, this.M.radius * 0.7), -var1.h.z * 0.65);
         var8 = var1.d ? 8.0 : 18.0;
      } else {
         var7 = this.W.add(0.0, 9.0, 0.0);
         var8 = 16.0;
      }

      var1.I = var1.k = th.U(var7, var1.d ? Math.min(12.0, this.M.radius) : this.M.radius, var8, this.y);
      var1.c = 0.0;
      var1.V = var2 != 0 ? 1.6 + this.y.nextDouble() * 1.8 : 2.3 + this.y.nextDouble() * 1.3;
      var1.p = this.M.size * (var2 != 0 ? 0.055 : 0.014) * (0.65 + this.y.nextDouble() * 0.7);
      var1.C = this.y.nextDouble();
      var1.i = (boolean)0;
      var1.y = null;
      var1.a = 0.0;
      var1.n = Double.POSITIVE_INFINITY;
      var1.T = 0;
   }

   private void U() {
      this.S.u = 1;
      this.S.j = this.M.primary;
      this.S.Z = uh.F(this.M.secondary, 0.55);
      this.S.R = this.M.bloom;
      this.S.E = this.M.distance;
      this.S.Y = this.S.r = this.S.g = this.S.s = this.S.C = 0.0;
      this.S.W = this.S.K = this.S.p = this.S.v = this.S.m = 0.0;
      this.S.G = this.S.b = 0.0;
      this.S.M = st.z.C;
      this.S.e = st.c.T;
      this.S.B = c3.a.Z;
   }

   private void w() {
      this.U();
      this.S.B = this.y.nextDouble() < 0.2 ? c3.a.e : c3.a.Z;
      this.S.O = 3.0;
      this.S.Q = 6.0;
      this.S.n = this.M.size * 0.022;
      this.S.f = this.M.size * 0.07;
      this.S.Y = this.M.wind * 0.12;
      this.S.g = this.M.wind * 0.05;
      this.S.G = 0.35 + this.y.nextDouble() * 0.5;
      this.S.b = 0.4 + this.y.nextDouble();
      this.S.J = 4.0 + this.y.nextDouble() * 4.0;
      this.S.j = uh.F(uh.q(this.M.primary, this.M.secondary, this.y.nextDouble()), 0.4 + this.y.nextDouble() * 0.5);
      int var1 = this.y.nextDouble() < 0.22 ? 1 : 0;
      this.L.S(th.U(this.W, var1 != 0 ? Math.min(3.0, this.M.radius * 0.16) : this.M.radius, var1 != 0 ? 4.0 : Math.min(20.0, this.M.radius), this.y), this.S);
   }

   private void P(pt.b var1) {
      this.U();
      double var2 = this.y.nextDouble();
      int var4 = var2 < 0.06 ? 1 : 0;
      int var5 = var2 < 0.32 ? 1 : 0;
      double var6 = var4 != 0 ? Math.min(4.0, this.M.radius * 0.2) : (var5 != 0 ? Math.min(9.0, this.M.radius * 0.4) : this.M.radius);
      Vec3 var8 = th.U(this.W.add(0.0, var4 != 0 ? 1.0 : (var5 != 0 ? 3.0 : 7.0), 0.0), var6, var4 != 0 ? 4.0 : (var5 != 0 ? 12.0 : 22.0), this.y);
      double var9 = 0.55 + this.y.nextDouble() * 0.55;
      double var11 = this.M.wind * 0.35;
      double var13 = this.M.wind * 0.15;
      double var15 = 6.0 + this.y.nextDouble() * 6.0;
      double var17 = var9 * this.M.speed;
      if (var1 != null) {
         double var19 = var1.l((int)Math.floor(var8.x), (int)Math.floor(var8.z));
         if (Double.isFinite(var19) && var8.y < var19 + 0.25) {
            this.a++;
            return;
         }

         if (var17 > 0.05) {
            double var21 = var8.x + var11 * this.M.speed * var15;
            double var23 = var8.z + var13 * this.M.speed * var15;
            double var25 = var1.l((int)Math.floor(var21), (int)Math.floor(var23));
            if (Double.isFinite(var25)) {
               var15 = Math.min(var15, Math.max(0.4, (var8.y - var25) / var17));
            }
         }
      }

      this.S.O = this.S.Q = var15;
      this.S.Y = var11;
      this.S.r = -var9;
      this.S.g = var13;
      this.S.p = 0.12;
      this.S.b = 0.2 + this.y.nextDouble() * 0.45;
      this.S.G = 0.06;
      this.S.J = 3.0 + this.y.nextDouble() * 3.5;
      this.S.M = st.z.J;
      int var27 = var4 == 0 && this.y.nextDouble() < 0.12 ? 1 : 0;
      this.S.B = var27 != 0 ? c3.a.e : c3.a.Z;
      this.S.e = var27 != 0 ? st.c.T : st.c.X;
      double var20 = this.M.size * (0.02 + this.y.nextDouble() * 0.032) * (var4 != 0 ? 2.6 : (var5 != 0 ? 1.2 : 1.0));
      this.S.n = this.S.f = var20;
      int var22 = uh.q(this.M.primary, this.M.primary & 0xFF000000 | 16777215, 0.35 + this.y.nextDouble() * 0.3);
      this.S.j = uh.F(var22, var4 != 0 ? 0.28 : 0.55 + this.y.nextDouble() * 0.4);
      this.S.Z = uh.F(this.M.secondary, var4 != 0 ? 0.12 : 0.35);
      this.L.S(var8, this.S);
   }

   private void b(pt.f var1) {
      this.U();
      this.S.B = c3.a.P;
      this.S.u = 3;
      this.S.O = 0.25;
      this.S.Q = 0.55;
      this.S.n = var1.p * 0.65;
      this.S.f = var1.p;
      this.S.s = 1.4;
      this.S.W = 2.0;
      this.S.K = 0.4;
      this.S.Y = var1.h.x * 0.65;
      this.S.r = var1.h.y * 0.65;
      this.S.g = var1.h.z * 0.65;
      this.S.M = st.z.f;
      this.S.e = st.c.Q;
      this.L.S(var1.I, this.S);
      this.o++;
   }

   private void E(b5.e var1, double var2) {
      this.U();
      this.S.B = c3.a.V;
      this.S.c = var1.normal();
      this.S.O = this.S.Q = 0.42;
      this.S.w = Math.clamp(var2 * 7.0, 0.12, 0.45);
      this.S.X = 0.015;
      this.S.M = st.z.J;
      this.S.e = st.c.X;
      this.S.j = uh.F(this.M.primary, 0.5);
      this.S.Z = uh.F(this.M.secondary, 0.1);
      Vec3 var4 = var1.point().add(var1.normal().scale(0.012));
      this.L.S(var4, this.S);
      this.S.B = c3.a.Z;
      this.S.u = 2;
      this.S.O = 0.18;
      this.S.Q = 0.32;
      this.S.n = 0.025;
      this.S.f = 0.045;
      this.S.w = 1.0;
      this.S.r = 0.2;
      this.S.s = 0.35;
      this.S.e = st.c.Q;
      this.L.S(var4, this.S);
      this.x++;
   }

   public void c(Vec3 var1, float var2, Frustum var3) {
      long var4 = zj.m();
      this.s = var1;
      this.K = Math.clamp(var2, 0.0F, 1.0F);
      this.D = 0;
      this.l = (boolean)0;
      this.L.B(var1, this.K, var3);
      if (this.M != null) {
         double var6 = this.Z + (this.O - this.Z) * this.K;

         for (int var8 = 0; var8 < this.H; var8++) {
            pt.f var9 = this.w[var8];
            Vec3 var10 = var9.k.lerp(var9.I, this.K);
            double var11 = Math.max(0.0, var9.c - 0.05 + this.K * 0.05);
            double var13 = var10.distanceTo(var1);
            double var15 = H(var11, var9.V) * st.h((this.M.distance - var13) / Math.max(2.0, this.M.distance * 0.18)) * var6;
            if (!(var15 < 0.005)) {
               int var17 = this.M.style == pt.l.Q ? 1 : 0;
               Vec3 var18 = var17 != 0 && var9.T > 0 ? var9.B[var9.T - 1] : var10.subtract(var9.h.scale(0.045 + var9.C * 0.025));
               double var19 = Math.max(0.1, var9.p * 3.0);
               if (var3 == null || var3.isVisible(new AABB(var10, var18).inflate(var19))) {
                  pt.w var21 = this.F[this.D++];
                  var21.f = var9.p;
                  var21.n = (boolean)var17;
                  var21.T = uh.F(uh.q(this.M.primary, this.M.primary & 0xFF000000 | 16777215, var17 != 0 ? 0.55 : 0.18), var15 * (var17 != 0 ? 0.95 : 0.6));
                  var21.q = uh.F(this.M.secondary, var15 * (var17 != 0 ? 0.6 : 0.3));
                  if (var21.T >>> 24 == 0 && var21.q >>> 24 == 0) {
                     this.D--;
                  } else {
                     var21.z[0] = var10;
                     var21.G = 1;
                     if (var17 != 0) {
                        for (int var22 = 0; var22 < var9.T; var22++) {
                           var21.z[var21.G++] = var9.B[var22];
                        }
                     } else {
                        var21.z[var21.G++] = var18;
                     }
                  }
               }
            }
         }

         this.l = (boolean)(!this.M.bloom || this.D <= 0 && !this.L.o() ? 0 : 1);
         zj.K("Celestial prepared flights", this.D);
         zj.K("Celestial ribbon segments", this.F());
         zj.K("Celestial culled flights", this.H - this.D);
         zj.l("Celestial prepare", var4);
      }
   }

   public static double H(double var0, double var2) {
      return st.h(var0 / 0.22) * st.h((var2 - var0) / 0.45);
   }

   public boolean d() {
      return this.l;
   }

   public boolean b() {
      return this.L.L();
   }

   public void C(kj var1, boolean var2) {
      if (!var2 || this.l) {
         this.L.G(var1, this.s, this.K, var2);
         var1.blend(gl.c.E);

         for (int var3 = 0; var3 < this.D; var3++) {
            pt.w var4 = this.F[var3];

            for (int var5 = 1; var5 < var4.G; var5++) {
               Vec3 var6 = var4.z[var5 - 1];
               Vec3 var7 = var4.z[var5];
               double var8 = 1.0 - (var5 - 1.0) / (var4.G - 1);
               double var10 = 1.0 - (double)var5 / (var4.G - 1);
               double var12 = var4.f * (var4.n ? 0.08 + 0.92 * var8 : 1.0);
               double var14 = var4.f * (var4.n ? 0.08 + 0.92 * var10 : 0.2);
               int var16 = uh.F(uh.q(var4.q, var4.T, var8), var8);
               int var17 = uh.F(uh.q(var4.q, var4.T, var10), var10);
               var1.k(var6.x, var6.y, var6.z, var7.x, var7.y, var7.z, var12 * 3.0, var14 * 3.0, uh.F(var16, 0.15), uh.F(var17, 0.15));
               var1.k(var6.x, var6.y, var6.z, var7.x, var7.y, var7.z, var12, var14, var16, var17);
               if (var4.n) {
                  var1.k(
                     var6.x,
                     var6.y,
                     var6.z,
                     var7.x,
                     var7.y,
                     var7.z,
                     var12 * 0.26,
                     var14 * 0.26,
                     uh.q(var16, var16 & 0xFF000000 | 16777215, 0.75),
                     uh.q(var17, var17 & 0xFF000000 | 16777215, 0.75)
                  );
               }
            }

            if (var4.n) {
               Vec3 var18 = var4.z[0];
               var1.I(var18.x, var18.y, var18.z, var4.f * 1.5, 0.0, var4.T, c3.a.Z, 0.0, 0.0, 0.0);
            }
         }

         var1.blend(gl.c.K);
      }
   }

   public int D() {
      return this.H;
   }

   public int O() {
      return this.L.G();
   }

   public int P() {
      int var1 = 0;

      for (int var2 = 0; var2 < this.H; var2++) {
         var1 += this.w[var2].T;
      }

      return var1;
   }

   public int c() {
      return this.D;
   }

   public int F() {
      int var1 = 0;

      for (int var2 = 0; var2 < this.D; var2++) {
         var1 += Math.max(0, this.F[var2].G - 1);
      }

      return var1;
   }

   public int T() {
      return this.J;
   }

   public int m() {
      return this.a;
   }

   public int b() {
      return this.x;
   }

   public int d() {
      return this.o;
   }

   public Vec3 v(int var1) {
      return this.w[var1].I;
   }

   public Vec3 z(int var1) {
      return this.w[var1].h;
   }

   @FunctionalInterface
   public interface b {
      double l(int var1, int var2);
   }

   private static final class f {
      Vec3 I;
      Vec3 k;
      Vec3 h;
      final Vec3[] B = new Vec3[12];
      int T;
      double c;
      double V;
      double p;
      double C;
      double a;
      double n;
      boolean d;
      boolean i;
      b5.e y;
   }

   public enum l {
      Q,
      u,
      R,
      a;
   }

   public record s(
      pt.l style,
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

   private static final class w {
      final Vec3[] z = new Vec3[13];
      int G;
      int T;
      int q;
      double f;
      boolean n;
   }
}
