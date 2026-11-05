package com.larpclient;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.world.phys.Vec3;

public final class xg {
   private static final int k = 128;
   private static final int U = 32;
   private final Map<Integer, xg.k> N = new LinkedHashMap<>();
   private long h;
   public xg.c q = xg.c.W;
   public yc.d a = yc.d.u;
   public fk.h I = fk.h.z;
   public double v = 1.2;
   public double Q = 0.12;
   public boolean s = (boolean)1;
   public boolean e = (boolean)1;
   public boolean D;
   private boolean z;
   public int T = rv.e;
   public int O = rv.GQ;

   public xg() {
      r4.V("Trails", "Ribbon engine initialized: 32 sources, 128 points per source");
   }

   public boolean J() {
      for (xg.k var2 : this.N.values()) {
         if (var2.Q == null ? this.s : var2.Q.glow()) {
            return true;
         }
      }

      return false;
   }

   public boolean Z() {
      return this.z;
   }

   public boolean T() {
      return this.N.isEmpty();
   }

   public int S() {
      return this.N.size();
   }

   public int t() {
      return this.N.values().stream().mapToInt(var0 -> var0.c).sum();
   }

   public void m() {
      this.N.clear();
      this.h = 0L;
      this.z = (boolean)0;
   }

   public void S(int var1) {
      this.N.remove(var1);
   }

   public void N() {
      this.h++;
      this.N.values().removeIf(var1 -> {
         while (var1.c > 0 && this.h - var1.t[var1.z] > (var1.Q == null ? this.v : var1.Q.lifetime()) * 20.0) {
            var1.z = (var1.z + 1) % 128;
            var1.c--;
         }

         return var1.c == 0;
      });
   }

   public void w(int var1, Vec3 var2) {
      xg.k var3 = this.N.get(var1);
      if (var3 == null) {
         if (this.N.size() >= 32) {
            return;
         }

         var3 = new xg.k();
         this.N.put(var1, var3);
      }

      if (var3.c > 0) {
         Vec3 var4 = var3.u[(var3.z + var3.c - 1) % 128];
         double var5 = var4.distanceToSqr(var2);
         if (var5 > 64.0) {
            var3.z = var3.c = 0;
         } else if (var5 < 4.0E-4) {
            return;
         }
      }

      if (var3.c == 128) {
         var3.z = (var3.z + 1) % 128;
         var3.c--;
      }

      int var7 = (var3.z + var3.c++) % 128;
      var3.u[var7] = var2;
      var3.t[var7] = this.h;
   }

   public void A(int var1, Vec3 var2, xg.v var3) {
      this.w(var1, var2);
      xg.k var4 = this.N.get(var1);
      if (var4 != null) {
         var4.Q = var3;
      }
   }

   public void X(Vec3 var1, float var2) {
      this.z = (boolean)0;
      double var3 = ea.y();

      for (xg.k var6 : this.N.values()) {
         var6.f.H();
         var6.f.k = 4096.0;
         xg.v var7 = var6.Q;
         double var8 = var7 == null ? this.v : var7.lifetime();
         double var10 = var7 == null ? this.Q : var7.width();
         int var12 = var7 == null ? this.T : var7.from();
         int var13 = var7 == null ? this.O : var7.to();
         boolean var14 = var7 == null ? this.e : var7.smoothing();
         xg.c var15 = var7 == null ? this.q : var7.widthCurve();
         yc.d var16 = var7 == null ? this.a : var7.alphaCurve();
         var6.w = var7 == null ? this.I : var7.blend();
         var6.J = var7 == null ? this.s : var7.glow();
         var6.g = this.D;

         for (int var17 = 1; var17 < var6.c; var17++) {
            int var18 = (var6.z + var17 - 1) % 128;
            int var19 = (var6.z + var17) % 128;
            Vec3 var20 = var6.u[var18];
            Vec3 var21 = var6.u[var19];
            if (var17 == var6.c - 1 && var6.t[var19] == this.h) {
               var21 = var20.lerp(var21, var2);
            }

            double var22 = Math.clamp(1.0 - ((float)this.h + var2 - (float)var6.t[var18]) / (var8 * 20.0), 0.0, 1.0);
            double var24 = Math.clamp(1.0 - ((float)this.h + var2 - (float)var6.t[var19]) / (var8 * 20.0), 0.0, 1.0);
            if (var17 == 1) {
               this.N(var6.f, var20, var22, var12, var13, var16, var15, var10, var3);
            }

            int var26 = var14 ? 4 : 1;
            if (var26 > 1) {
               Vec3 var27 = var17 > 1 ? var6.u[(var6.z + var17 - 2) % 128] : var20.add(var20.subtract(var21));
               Vec3 var28 = var17 < var6.c - 1 ? var6.u[(var6.z + var17 + 1) % 128] : var21.add(var21.subtract(var20));
               var6.v.n(var27, var20, var21, var28);
            }

            for (int var31 = 1; var31 <= var26; var31++) {
               double var32 = (double)var31 / var26;
               Vec3 var30 = var26 == 1 ? var21 : var6.v.o(var32);
               this.N(var6.f, var30, var22 + (var24 - var22) * var32, var12, var13, var16, var15, var10, var3);
            }
         }

         if (var15 != xg.c.E) {
            var6.f.W();
         }

         var6.f.n(var1);
         var6.G = R(var6.f, var1);
         if (var6.J && var6.G) {
            this.z = (boolean)1;
         }
      }
   }

   private static boolean R(h3.b var0, Vec3 var1) {
      for (int var2 = 1; var2 < var0.L; var2++) {
         if (var0.I[var2 - 1].distanceToSqr(var1) <= 4096.0
            && var0.I[var2 - 1].distanceToSqr(var0.I[var2]) >= 1.0E-10
            && (var0.c[var2 - 1] > 0.0 || var0.c[var2] > 0.0)
            && (var0.Z[var2 - 1] >>> 24 > 0 || var0.Z[var2] >>> 24 > 0)) {
            return true;
         }
      }

      return false;
   }

   private void N(h3.b var1, Vec3 var2, double var3, int var5, int var6, yc.d var7, xg.c var8, double var9, double var11) {
      int var13 = oh.S(oh.j(var6, var5, var3), var7.v(1.0 - var3));
      double var14 = var9 * this.L(var3, var8);
      if (this.D) {
         var14 *= (1.0 + 0.075 * Math.sin(var2.x * 7.0 + var2.y * 3.0 + var2.z * 5.0 - var11 * 1.7)) * (1.0 + 0.3 * var3);
         var13 = oh.S(var13, var3 * var3);
      }

      var1.P(var2, var14, var13);
   }

   public void q(r6 var1, float var2, boolean var3) {
      long var4 = h_.U();

      for (xg.k var7 : this.N.values()) {
         if (var7.G && (!var3 || var7.J)) {
            var1.blend(var7.w);
            var1.y(var7.f, var7.g);
         }
      }

      var1.blend(fk.h.z);
      h_.n("Trails", var4);
   }

   private double L(double var1, xg.c var3) {
      return switch (var3) {
         case E -> 1.0;
         case W -> var1 * var1;
         case f -> Math.sin(Math.PI * var1);
         case N -> Math.pow(var1, 3.0) * 1.4;
      };
   }

   public enum c {
      E,
      W,
      f,
      N;
   }

   private static final class k {
      final Vec3[] u = new Vec3[128];
      final long[] t = new long[128];
      int z;
      int c;
      xg.v Q;
      final h3.b f = new h3.b(512);
      final h3.f v = new h3.f();
      fk.h w;
      boolean J;
      boolean g;
      boolean G;
   }

   public record v(double lifetime, double width, int from, int to, boolean glow, boolean smoothing, fk.h blend, xg.c widthCurve, yc.d alphaCurve) {
      public v(double lifetime, double width, int from, int to, boolean glow, boolean smoothing, fk.h blend, xg.c widthCurve, yc.d alphaCurve) {
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
}
