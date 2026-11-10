package com.larpclient;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.class_243;

public final class ce {
   private static final int t = 128;
   private static final int i = 32;
   private final Map<Integer, ce.m> l = new LinkedHashMap<>();
   private long Q;
   public ce.b J = ce.b.p;
   public u9.u W;
   public ti.d D;
   public double K;
   public double d;
   public boolean R;
   public boolean L;
   public boolean V;
   private boolean c;
   public int T;
   public int m;

   public ce() {
      this.W = u9.u.W;
      this.D = ti.d.G;
      this.K = 1.2;
      this.d = 0.12;
      this.R = (boolean)1;
      this.L = (boolean)1;
      this.T = ez.a;
      this.m = ez.f;
      q8.G("Trails", "Ribbon engine initialized: 32 sources, 128 points per source");
   }

   public boolean j() {
      for (ce.m var2 : this.l.values()) {
         if (var2.X == null ? this.R : var2.X.glow()) {
            return true;
         }
      }

      return false;
   }

   public boolean x() {
      return this.c;
   }

   public boolean g() {
      return this.l.isEmpty();
   }

   public int A() {
      return this.l.size();
   }

   public int w() {
      return this.l.values().stream().mapToInt(var0 -> var0.D).sum();
   }

   public void f() {
      this.l.clear();
      this.Q = 0L;
      this.c = (boolean)0;
   }

   public void T(int var1) {
      this.l.remove(var1);
   }

   public void b() {
      this.Q++;
      this.l.values().removeIf(var1 -> {
         while (var1.D > 0 && this.Q - var1.f[var1.A] > (var1.X == null ? this.K : var1.X.lifetime()) * 20.0) {
            var1.A = (var1.A + 1) % 128;
            var1.D--;
         }

         return var1.D == 0;
      });
   }

   public void W(int var1, class_243 var2) {
      ce.m var3 = this.l.get(var1);
      if (var3 == null) {
         if (this.l.size() >= 32) {
            return;
         }

         var3 = new ce.m();
         this.l.put(var1, var3);
      }

      if (var3.D > 0) {
         class_243 var4 = var3.T[(var3.A + var3.D - 1) % 128];
         double var5 = var4.method_1025(var2);
         if (var5 > 64.0) {
            var3.A = var3.D = 0;
         } else if (var5 < 4.0E-4) {
            return;
         }
      }

      if (var3.D == 128) {
         var3.A = (var3.A + 1) % 128;
         var3.D--;
      }

      int var7 = (var3.A + var3.D++) % 128;
      var3.T[var7] = var2;
      var3.f[var7] = this.Q;
   }

   public void i(int var1, class_243 var2, ce.y var3) {
      this.W(var1, var2);
      ce.m var4 = this.l.get(var1);
      if (var4 != null) {
         var4.X = var3;
      }
   }

   public void j(class_243 var1, float var2) {
      this.c = (boolean)0;
      double var3 = qo.x();

      for (ce.m var6 : this.l.values()) {
         var6.t.T();
         var6.t.C = 4096.0;
         ce.y var7 = var6.X;
         double var8 = var7 == null ? this.K : var7.lifetime();
         double var10 = var7 == null ? this.d : var7.width();
         int var12 = var7 == null ? this.T : var7.from();
         int var13 = var7 == null ? this.m : var7.to();
         boolean var14 = var7 == null ? this.L : var7.smoothing();
         ce.b var15 = var7 == null ? this.J : var7.widthCurve();
         u9.u var16 = var7 == null ? this.W : var7.alphaCurve();
         var6.n = var7 == null ? this.D : var7.blend();
         var6.k = var7 == null ? this.R : var7.glow();
         var6.i = this.V;

         for (int var17 = 1; var17 < var6.D; var17++) {
            int var18 = (var6.A + var17 - 1) % 128;
            int var19 = (var6.A + var17) % 128;
            class_243 var20 = var6.T[var18];
            class_243 var21 = var6.T[var19];
            if (var17 == var6.D - 1 && var6.f[var19] == this.Q) {
               var21 = var20.method_35590(var21, var2);
            }

            double var22 = Math.clamp(1.0 - ((float)this.Q + var2 - (float)var6.f[var18]) / (var8 * 20.0), 0.0, 1.0);
            double var24 = Math.clamp(1.0 - ((float)this.Q + var2 - (float)var6.f[var19]) / (var8 * 20.0), 0.0, 1.0);
            if (var17 == 1) {
               this.V(var6.t, var20, var22, var12, var13, var16, var15, var10, var3);
            }

            int var26 = var14 ? 4 : 1;
            if (var26 > 1) {
               class_243 var27 = var17 > 1 ? var6.T[(var6.A + var17 - 2) % 128] : var20.method_1019(var20.method_1020(var21));
               class_243 var28 = var17 < var6.D - 1 ? var6.T[(var6.A + var17 + 1) % 128] : var21.method_1019(var21.method_1020(var20));
               var6.h.V(var27, var20, var21, var28);
            }

            for (int var31 = 1; var31 <= var26; var31++) {
               double var32 = (double)var31 / var26;
               class_243 var30 = var26 == 1 ? var21 : var6.h.K(var32);
               this.V(var6.t, var30, var22 + (var24 - var22) * var32, var12, var13, var16, var15, var10, var3);
            }
         }

         if (var15 != ce.b.J) {
            var6.t.V();
         }

         var6.t.k(var1);
         var6.Q = q(var6.t, var1);
         if (var6.k && var6.Q) {
            this.c = (boolean)1;
         }
      }
   }

   private static boolean q(qd.x var0, class_243 var1) {
      for (int var2 = 1; var2 < var0.w; var2++) {
         if (var0.Q[var2 - 1].method_1025(var1) <= 4096.0
            && var0.Q[var2 - 1].method_1025(var0.Q[var2]) >= 1.0E-10
            && (var0.g[var2 - 1] > 0.0 || var0.g[var2] > 0.0)
            && (var0.M[var2 - 1] >>> 24 > 0 || var0.M[var2] >>> 24 > 0)) {
            return true;
         }
      }

      return false;
   }

   private void V(qd.x var1, class_243 var2, double var3, int var5, int var6, u9.u var7, ce.b var8, double var9, double var11) {
      int var13 = ei.R(ei.W(var6, var5, var3), var7.E(1.0 - var3));
      double var14 = var9 * this.g(var3, var8);
      if (this.V) {
         var14 *= (1.0 + 0.075 * Math.sin(var2.field_1352 * 7.0 + var2.field_1351 * 3.0 + var2.field_1350 * 5.0 - var11 * 1.7)) * (1.0 + 0.3 * var3);
         var13 = ei.R(var13, var3 * var3);
      }

      var1.x(var2, var14, var13);
   }

   public void t(f1 var1, float var2, boolean var3) {
      long var4 = c5.X();

      for (ce.m var7 : this.l.values()) {
         if (var7.Q && (!var3 || var7.k)) {
            var1.blend(var7.n);
            var1.G(var7.t, var7.i);
         }
      }

      var1.blend(ti.d.G);
      c5.w("Trails", var4);
   }

   private double g(double var1, ce.b var3) {
      return switch (var3) {
         case J -> 1.0;
         case p -> var1 * var1;
         case w -> Math.sin(Math.PI * var1);
         case P -> Math.pow(var1, 3.0) * 1.4;
      };
   }

   public enum b {
      J,
      p,
      w,
      P;
   }

   private static final class m {
      final class_243[] T = new class_243[128];
      final long[] f = new long[128];
      int A;
      int D;
      ce.y X;
      final qd.x t = new qd.x(512);
      final qd.p h = new qd.p();
      ti.d n;
      boolean k;
      boolean i;
      boolean Q;
   }

   public record y(double lifetime, double width, int from, int to, boolean glow, boolean smoothing, ti.d blend, ce.b widthCurve, u9.u alphaCurve) {
      public y(double lifetime, double width, int from, int to, boolean glow, boolean smoothing, ti.d blend, ce.b widthCurve, u9.u alphaCurve) {
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
