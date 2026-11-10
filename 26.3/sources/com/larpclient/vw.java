package com.larpclient;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.world.phys.Vec3;

public final class vw {
   private static final int p = 128;
   private static final int X = 32;
   private final Map<Integer, vw.w> T = new LinkedHashMap<>();
   private long h;
   public vw.p v = vw.p.V;
   public pr.b W = pr.b.A;
   public _0.v V = _0.v.z;
   public double d = 1.2;
   public double P = 0.12;
   public boolean j = (boolean)1;
   public boolean B = (boolean)1;
   public boolean g;
   private boolean I;
   public int k = c.y;
   public int a = c.t;

   public vw() {
      _q.z("Trails", "Ribbon engine initialized: 32 sources, 128 points per source");
   }

   public boolean r() {
      for (vw.w var2 : this.T.values()) {
         if (var2.g == null ? this.j : var2.g.glow()) {
            return true;
         }
      }

      return false;
   }

   public boolean S() {
      return this.I;
   }

   public boolean w() {
      return this.T.isEmpty();
   }

   public int Y() {
      return this.T.size();
   }

   public int K() {
      return this.T.values().stream().mapToInt(var0 -> var0.i).sum();
   }

   public void Y() {
      this.T.clear();
      this.h = 0L;
      this.I = (boolean)0;
   }

   public void v(int var1) {
      this.T.remove(var1);
   }

   public void l() {
      this.h++;
      this.T.values().removeIf(var1 -> {
         while (var1.i > 0 && this.h - var1.D[var1.o] > (var1.g == null ? this.d : var1.g.lifetime()) * 20.0) {
            var1.o = (var1.o + 1) % 128;
            var1.i--;
         }

         return var1.i == 0;
      });
   }

   public void Z(int var1, Vec3 var2) {
      vw.w var3 = this.T.get(var1);
      if (var3 == null) {
         if (this.T.size() >= 32) {
            return;
         }

         var3 = new vw.w();
         this.T.put(var1, var3);
      }

      if (var3.i > 0) {
         Vec3 var4 = var3.L[(var3.o + var3.i - 1) % 128];
         double var5 = var4.distanceToSqr(var2);
         if (var5 > 64.0) {
            var3.o = var3.i = 0;
         } else if (var5 < 4.0E-4) {
            return;
         }
      }

      if (var3.i == 128) {
         var3.o = (var3.o + 1) % 128;
         var3.i--;
      }

      int var7 = (var3.o + var3.i++) % 128;
      var3.L[var7] = var2;
      var3.D[var7] = this.h;
   }

   public void m(int var1, Vec3 var2, vw.j var3) {
      this.Z(var1, var2);
      vw.w var4 = this.T.get(var1);
      if (var4 != null) {
         var4.g = var3;
      }
   }

   public void J(Vec3 var1, float var2) {
      this.I = (boolean)0;
      double var3 = hz.Q();

      for (vw.w var6 : this.T.values()) {
         var6.b.w();
         var6.b.e = 4096.0;
         vw.j var7 = var6.g;
         double var8 = var7 == null ? this.d : var7.lifetime();
         double var10 = var7 == null ? this.P : var7.width();
         int var12 = var7 == null ? this.k : var7.from();
         int var13 = var7 == null ? this.a : var7.to();
         boolean var14 = var7 == null ? this.B : var7.smoothing();
         vw.p var15 = var7 == null ? this.v : var7.widthCurve();
         pr.b var16 = var7 == null ? this.W : var7.alphaCurve();
         var6.n = var7 == null ? this.V : var7.blend();
         var6.F = var7 == null ? this.j : var7.glow();
         var6.c = this.g;

         for (int var17 = 1; var17 < var6.i; var17++) {
            int var18 = (var6.o + var17 - 1) % 128;
            int var19 = (var6.o + var17) % 128;
            Vec3 var20 = var6.L[var18];
            Vec3 var21 = var6.L[var19];
            if (var17 == var6.i - 1 && var6.D[var19] == this.h) {
               var21 = var20.lerp(var21, var2);
            }

            double var22 = Math.clamp(1.0 - ((float)this.h + var2 - (float)var6.D[var18]) / (var8 * 20.0), 0.0, 1.0);
            double var24 = Math.clamp(1.0 - ((float)this.h + var2 - (float)var6.D[var19]) / (var8 * 20.0), 0.0, 1.0);
            if (var17 == 1) {
               this.K(var6.b, var20, var22, var12, var13, var16, var15, var10, var3);
            }

            int var26 = var14 ? 4 : 1;
            if (var26 > 1) {
               Vec3 var27 = var17 > 1 ? var6.L[(var6.o + var17 - 2) % 128] : var20.add(var20.subtract(var21));
               Vec3 var28 = var17 < var6.i - 1 ? var6.L[(var6.o + var17 + 1) % 128] : var21.add(var21.subtract(var20));
               var6.A.P(var27, var20, var21, var28);
            }

            for (int var31 = 1; var31 <= var26; var31++) {
               double var32 = (double)var31 / var26;
               Vec3 var30 = var26 == 1 ? var21 : var6.A.x(var32);
               this.K(var6.b, var30, var22 + (var24 - var22) * var32, var12, var13, var16, var15, var10, var3);
            }
         }

         if (var15 != vw.p.o) {
            var6.b.v();
         }

         var6.b.T(var1);
         var6.P = R(var6.b, var1);
         if (var6.F && var6.P) {
            this.I = (boolean)1;
         }
      }
   }

   private static boolean R(x.c var0, Vec3 var1) {
      for (int var2 = 1; var2 < var0.l; var2++) {
         if (var0.r[var2 - 1].distanceToSqr(var1) <= 4096.0
            && var0.r[var2 - 1].distanceToSqr(var0.r[var2]) >= 1.0E-10
            && (var0.x[var2 - 1] > 0.0 || var0.x[var2] > 0.0)
            && (var0.W[var2 - 1] >>> 24 > 0 || var0.W[var2] >>> 24 > 0)) {
            return true;
         }
      }

      return false;
   }

   private void K(x.c var1, Vec3 var2, double var3, int var5, int var6, pr.b var7, vw.p var8, double var9, double var11) {
      int var13 = vv.O(vv.Q(var6, var5, var3), var7.p(1.0 - var3));
      double var14 = var9 * this.w(var3, var8);
      if (this.g) {
         var14 *= (1.0 + 0.075 * Math.sin(var2.x * 7.0 + var2.y * 3.0 + var2.z * 5.0 - var11 * 1.7)) * (1.0 + 0.3 * var3);
         var13 = vv.O(var13, var3 * var3);
      }

      var1.J(var2, var14, var13);
   }

   public void Q(fz var1, float var2, boolean var3) {
      long var4 = he.s();

      for (vw.w var7 : this.T.values()) {
         if (var7.P && (!var3 || var7.F)) {
            var1.blend(var7.n);
            var1.n(var7.b, var7.c);
         }
      }

      var1.blend(_0.v.z);
      he.T("Trails", var4);
   }

   private double w(double var1, vw.p var3) {
      return switch (var3) {
         case o -> 1.0;
         case V -> var1 * var1;
         case n -> Math.sin(Math.PI * var1);
         case z -> Math.pow(var1, 3.0) * 1.4;
      };
   }

   public record j(double lifetime, double width, int from, int to, boolean glow, boolean smoothing, _0.v blend, vw.p widthCurve, pr.b alphaCurve) {
      public j(double lifetime, double width, int from, int to, boolean glow, boolean smoothing, _0.v blend, vw.p widthCurve, pr.b alphaCurve) {
         if (Double.isFinite(lifetime) && !(lifetime <= 0.0) && Double.isFinite(width) && !(width <= 0.0)) {
            Objects.requireNonNull(blend);
            Objects.requireNonNull(widthCurve);
            Objects.requireNonNull(alphaCurve);
            this.lifetime = lifetime;
            this.width = width;
            this.from = from;
            this.to = to;
            this.glow = glow;
            this.smoothing = smoothing;
            this.blend = blend;
            this.widthCurve = widthCurve;
            this.alphaCurve = alphaCurve;
         } else {
            throw new IllegalArgumentException("Trail lifetime and width must be finite and positive");
         }
      }
   }

   public enum p {
      o,
      V,
      n,
      z;
   }

   private static final class w {
      final Vec3[] L = new Vec3[128];
      final long[] D = new long[128];
      int o;
      int i;
      vw.j g;
      final x.c b = new x.c(512);
      final x.l A = new x.l();
      _0.v n;
      boolean F;
      boolean c;
      boolean P;
   }
}
