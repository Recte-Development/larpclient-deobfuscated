package com.larpclient;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.world.phys.Vec3;

public final class uz {
   private static final int g = 128;
   private static final int i = 32;
   private final Map<Integer, uz.s> L = new LinkedHashMap<>();
   private long n;
   public uz.i A = uz.i.I;
   public hp.y W = hp.y.B;
   public pf.x P = pf.x.S;
   public double q = 1.2;
   public double x = 0.12;
   public boolean d = (boolean)1;
   public boolean b = (boolean)1;
   public boolean G;
   private boolean J;
   public int C;
   public int R;

   public uz() {
      this.C = f.C;
      this.R = f.O;
      mt.K("Trails", "Ribbon engine initialized: 32 sources, 128 points per source");
   }

   public boolean B() {
      for (uz.s var2 : this.L.values()) {
         if (var2.a == null ? this.d : var2.a.glow()) {
            return true;
         }
      }

      return false;
   }

   public boolean f() {
      return this.J;
   }

   public boolean q() {
      return this.L.isEmpty();
   }

   public int F() {
      return this.L.size();
   }

   public int S() {
      return this.L.values().stream().mapToInt(var0 -> var0.n).sum();
   }

   public void p() {
      this.L.clear();
      this.n = 0L;
      this.J = (boolean)0;
   }

   public void u(int var1) {
      this.L.remove(var1);
   }

   public void u() {
      this.n++;
      this.L.values().removeIf(var1 -> {
         while (var1.n > 0 && this.n - var1.C[var1.d] > (var1.a == null ? this.q : var1.a.lifetime()) * 20.0) {
            var1.d = (var1.d + 1) % 128;
            var1.n--;
         }

         return var1.n == 0;
      });
   }

   public void I(int var1, Vec3 var2) {
      uz.s var3 = this.L.get(var1);
      if (var3 == null) {
         if (this.L.size() >= 32) {
            return;
         }

         var3 = new uz.s();
         this.L.put(var1, var3);
      }

      if (var3.n > 0) {
         Vec3 var4 = var3.z[(var3.d + var3.n - 1) % 128];
         double var5 = var4.distanceToSqr(var2);
         if (var5 > 64.0) {
            var3.d = var3.n = 0;
         } else if (var5 < 4.0E-4) {
            return;
         }
      }

      if (var3.n == 128) {
         var3.d = (var3.d + 1) % 128;
         var3.n--;
      }

      int var7 = (var3.d + var3.n++) % 128;
      var3.z[var7] = var2;
      var3.C[var7] = this.n;
   }

   public void B(int var1, Vec3 var2, uz.q var3) {
      this.I(var1, var2);
      uz.s var4 = this.L.get(var1);
      if (var4 != null) {
         var4.a = var3;
      }
   }

   public void P(Vec3 var1, float var2) {
      this.J = (boolean)0;
      double var3 = ty.K();

      for (uz.s var6 : this.L.values()) {
         var6.w.y();
         var6.w.t = 4096.0;
         uz.q var7 = var6.a;
         double var8 = var7 == null ? this.q : var7.lifetime();
         double var10 = var7 == null ? this.x : var7.width();
         int var12 = var7 == null ? this.C : var7.from();
         int var13 = var7 == null ? this.R : var7.to();
         boolean var14 = var7 == null ? this.b : var7.smoothing();
         uz.i var15 = var7 == null ? this.A : var7.widthCurve();
         hp.y var16 = var7 == null ? this.W : var7.alphaCurve();
         var6.Q = var7 == null ? this.P : var7.blend();
         var6.O = var7 == null ? this.d : var7.glow();
         var6.y = this.G;

         for (int var17 = 1; var17 < var6.n; var17++) {
            int var18 = (var6.d + var17 - 1) % 128;
            int var19 = (var6.d + var17) % 128;
            Vec3 var20 = var6.z[var18];
            Vec3 var21 = var6.z[var19];
            if (var17 == var6.n - 1 && var6.C[var19] == this.n) {
               var21 = var20.lerp(var21, var2);
            }

            double var22 = Math.clamp(1.0 - ((float)this.n + var2 - (float)var6.C[var18]) / (var8 * 20.0), 0.0, 1.0);
            double var24 = Math.clamp(1.0 - ((float)this.n + var2 - (float)var6.C[var19]) / (var8 * 20.0), 0.0, 1.0);
            if (var17 == 1) {
               this.N(var6.w, var20, var22, var12, var13, var16, var15, var10, var3);
            }

            int var26 = var14 ? 4 : 1;
            if (var26 > 1) {
               Vec3 var27 = var17 > 1 ? var6.z[(var6.d + var17 - 2) % 128] : var20.add(var20.subtract(var21));
               Vec3 var28 = var17 < var6.n - 1 ? var6.z[(var6.d + var17 + 1) % 128] : var21.add(var21.subtract(var20));
               var6.i.l(var27, var20, var21, var28);
            }

            for (int var31 = 1; var31 <= var26; var31++) {
               double var32 = (double)var31 / var26;
               Vec3 var30 = var26 == 1 ? var21 : var6.i.p(var32);
               this.N(var6.w, var30, var22 + (var24 - var22) * var32, var12, var13, var16, var15, var10, var3);
            }
         }

         if (var15 != uz.i.X) {
            var6.w.F();
         }

         var6.w.a(var1);
         var6.W = i(var6.w, var1);
         if (var6.O && var6.W) {
            this.J = (boolean)1;
         }
      }
   }

   private static boolean i(__.l var0, Vec3 var1) {
      for (int var2 = 1; var2 < var0.k; var2++) {
         if (var0.v[var2 - 1].distanceToSqr(var1) <= 4096.0
            && var0.v[var2 - 1].distanceToSqr(var0.v[var2]) >= 1.0E-10
            && (var0.O[var2 - 1] > 0.0 || var0.O[var2] > 0.0)
            && (var0.w[var2 - 1] >>> 24 > 0 || var0.w[var2] >>> 24 > 0)) {
            return true;
         }
      }

      return false;
   }

   private void N(__.l var1, Vec3 var2, double var3, int var5, int var6, hp.y var7, uz.i var8, double var9, double var11) {
      int var13 = _3.U(_3.D(var6, var5, var3), var7.Z(1.0 - var3));
      double var14 = var9 * this.h(var3, var8);
      if (this.G) {
         var14 *= (1.0 + 0.075 * Math.sin(var2.x * 7.0 + var2.y * 3.0 + var2.z * 5.0 - var11 * 1.7)) * (1.0 + 0.3 * var3);
         var13 = _3.U(var13, var3 * var3);
      }

      var1.y(var2, var14, var13);
   }

   public void v(ki var1, float var2, boolean var3) {
      long var4 = kx.G();

      for (uz.s var7 : this.L.values()) {
         if (var7.W && (!var3 || var7.O)) {
            var1.blend(var7.Q);
            var1.M(var7.w, var7.y);
         }
      }

      var1.blend(pf.x.S);
      kx.u("Trails", var4);
   }

   private double h(double var1, uz.i var3) {
      return switch (var3) {
         case X -> 1.0;
         case I -> var1 * var1;
         case f -> Math.sin(Math.PI * var1);
         case H -> Math.pow(var1, 3.0) * 1.4;
      };
   }

   public enum i {
      X,
      I,
      f,
      H;
   }

   public record q(double lifetime, double width, int from, int to, boolean glow, boolean smoothing, pf.x blend, uz.i widthCurve, hp.y alphaCurve) {
      public q(double lifetime, double width, int from, int to, boolean glow, boolean smoothing, pf.x blend, uz.i widthCurve, hp.y alphaCurve) {
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

   private static final class s {
      final Vec3[] z = new Vec3[128];
      final long[] C = new long[128];
      int d;
      int n;
      uz.q a;
      final __.l w = new __.l(512);
      final __.u i = new __.u();
      pf.x Q;
      boolean O;
      boolean y;
      boolean W;
   }
}
