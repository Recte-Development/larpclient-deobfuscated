package com.larpclient;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.world.phys.Vec3;

public final class ub {
   private static final int w = 128;
   private static final int v = 32;
   private final Map<Integer, ub.b> u = new LinkedHashMap<>();
   private long d;
   public ub.f r = ub.f.O;
   public st.z n = st.z.f;
   public gl.c X = gl.c.K;
   public double D = 1.2;
   public double g = 0.12;
   public boolean a = (boolean)1;
   public boolean G = (boolean)1;
   public boolean l;
   private boolean x;
   public int A = ho.J;
   public int m = ho.ds;

   public ub() {
      h.A("Trails", "Ribbon engine initialized: 32 sources, 128 points per source");
   }

   public boolean j() {
      for (ub.b var2 : this.u.values()) {
         if (var2.C == null ? this.a : var2.C.glow()) {
            return true;
         }
      }

      return false;
   }

   public boolean P() {
      return this.x;
   }

   public boolean m() {
      return this.u.isEmpty();
   }

   public int u() {
      return this.u.size();
   }

   public int J() {
      return this.u.values().stream().mapToInt(var0 -> var0.u).sum();
   }

   public void V() {
      this.u.clear();
      this.d = 0L;
      this.x = (boolean)0;
   }

   public void L(int var1) {
      this.u.remove(var1);
   }

   public void w() {
      this.d++;
      this.u.values().removeIf(var1 -> {
         while (var1.u > 0 && this.d - var1.P[var1.p] > (var1.C == null ? this.D : var1.C.lifetime()) * 20.0) {
            var1.p = (var1.p + 1) % 128;
            var1.u--;
         }

         return var1.u == 0;
      });
   }

   public void a(int var1, Vec3 var2) {
      ub.b var3 = this.u.get(var1);
      if (var3 == null) {
         if (this.u.size() >= 32) {
            return;
         }

         var3 = new ub.b();
         this.u.put(var1, var3);
      }

      if (var3.u > 0) {
         Vec3 var4 = var3.E[(var3.p + var3.u - 1) % 128];
         double var5 = var4.distanceToSqr(var2);
         if (var5 > 64.0) {
            var3.p = var3.u = 0;
         } else if (var5 < 4.0E-4) {
            return;
         }
      }

      if (var3.u == 128) {
         var3.p = (var3.p + 1) % 128;
         var3.u--;
      }

      int var7 = (var3.p + var3.u++) % 128;
      var3.E[var7] = var2;
      var3.P[var7] = this.d;
   }

   public void d(int var1, Vec3 var2, ub.o var3) {
      this.a(var1, var2);
      ub.b var4 = this.u.get(var1);
      if (var4 != null) {
         var4.C = var3;
      }
   }

   public void e(Vec3 var1, float var2) {
      this.x = (boolean)0;
      double var3 = cf.F();

      for (ub.b var6 : this.u.values()) {
         var6.S.q();
         var6.S.C = 4096.0;
         ub.o var7 = var6.C;
         double var8 = var7 == null ? this.D : var7.lifetime();
         double var10 = var7 == null ? this.g : var7.width();
         int var12 = var7 == null ? this.A : var7.from();
         int var13 = var7 == null ? this.m : var7.to();
         boolean var14 = var7 == null ? this.G : var7.smoothing();
         ub.f var15 = var7 == null ? this.r : var7.widthCurve();
         st.z var16 = var7 == null ? this.n : var7.alphaCurve();
         var6.k = var7 == null ? this.X : var7.blend();
         var6.O = var7 == null ? this.a : var7.glow();
         var6.s = this.l;

         for (int var17 = 1; var17 < var6.u; var17++) {
            int var18 = (var6.p + var17 - 1) % 128;
            int var19 = (var6.p + var17) % 128;
            Vec3 var20 = var6.E[var18];
            Vec3 var21 = var6.E[var19];
            if (var17 == var6.u - 1 && var6.P[var19] == this.d) {
               var21 = var20.lerp(var21, var2);
            }

            double var22 = Math.clamp(1.0 - ((float)this.d + var2 - (float)var6.P[var18]) / (var8 * 20.0), 0.0, 1.0);
            double var24 = Math.clamp(1.0 - ((float)this.d + var2 - (float)var6.P[var19]) / (var8 * 20.0), 0.0, 1.0);
            if (var17 == 1) {
               this.J(var6.S, var20, var22, var12, var13, var16, var15, var10, var3);
            }

            int var26 = var14 ? 4 : 1;
            if (var26 > 1) {
               Vec3 var27 = var17 > 1 ? var6.E[(var6.p + var17 - 2) % 128] : var20.add(var20.subtract(var21));
               Vec3 var28 = var17 < var6.u - 1 ? var6.E[(var6.p + var17 + 1) % 128] : var21.add(var21.subtract(var20));
               var6.v.U(var27, var20, var21, var28);
            }

            for (int var31 = 1; var31 <= var26; var31++) {
               double var32 = (double)var31 / var26;
               Vec3 var30 = var26 == 1 ? var21 : var6.v.L(var32);
               this.J(var6.S, var30, var22 + (var24 - var22) * var32, var12, var13, var16, var15, var10, var3);
            }
         }

         if (var15 != ub.f.S) {
            var6.S.O();
         }

         var6.S.x(var1);
         var6.V = m(var6.S, var1);
         if (var6.O && var6.V) {
            this.x = (boolean)1;
         }
      }
   }

   private static boolean m(u_.p var0, Vec3 var1) {
      for (int var2 = 1; var2 < var0.p; var2++) {
         if (var0.M[var2 - 1].distanceToSqr(var1) <= 4096.0
            && var0.M[var2 - 1].distanceToSqr(var0.M[var2]) >= 1.0E-10
            && (var0.A[var2 - 1] > 0.0 || var0.A[var2] > 0.0)
            && (var0.I[var2 - 1] >>> 24 > 0 || var0.I[var2] >>> 24 > 0)) {
            return true;
         }
      }

      return false;
   }

   private void J(u_.p var1, Vec3 var2, double var3, int var5, int var6, st.z var7, ub.f var8, double var9, double var11) {
      int var13 = uh.F(uh.q(var6, var5, var3), var7.x(1.0 - var3));
      double var14 = var9 * this.Q(var3, var8);
      if (this.l) {
         var14 *= (1.0 + 0.075 * Math.sin(var2.x * 7.0 + var2.y * 3.0 + var2.z * 5.0 - var11 * 1.7)) * (1.0 + 0.3 * var3);
         var13 = uh.F(var13, var3 * var3);
      }

      var1.n(var2, var14, var13);
   }

   public void j(kj var1, float var2, boolean var3) {
      long var4 = zj.m();

      for (ub.b var7 : this.u.values()) {
         if (var7.V && (!var3 || var7.O)) {
            var1.blend(var7.k);
            var1.b(var7.S, var7.s);
         }
      }

      var1.blend(gl.c.K);
      zj.l("Trails", var4);
   }

   private double Q(double var1, ub.f var3) {
      return switch (var3) {
         case S -> 1.0;
         case O -> var1 * var1;
         case M -> Math.sin(Math.PI * var1);
         case k -> Math.pow(var1, 3.0) * 1.4;
      };
   }

   private static final class b {
      final Vec3[] E = new Vec3[128];
      final long[] P = new long[128];
      int p;
      int u;
      ub.o C;
      final u_.p S = new u_.p(512);
      final u_.u v = new u_.u();
      gl.c k;
      boolean O;
      boolean s;
      boolean V;
   }

   public enum f {
      S,
      O,
      M,
      k;
   }

   public record o(double lifetime, double width, int from, int to, boolean glow, boolean smoothing, gl.c blend, ub.f widthCurve, st.z alphaCurve) {
      public o(double lifetime, double width, int from, int to, boolean glow, boolean smoothing, gl.c blend, ub.f widthCurve, st.z alphaCurve) {
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
