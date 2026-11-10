package com.larpclient;

import java.util.Arrays;
import java.util.Random;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_4604;

public final class wx {
   private final gd e = new gd();
   private final ce I = new ce();
   private final Random M = new Random();
   private final class_243[] N = new class_243[32];
   private final class_243[] z = new class_243[32];
   private final class_243[] k = new class_243[32];
   private final class_243[] A = new class_243[32];
   private final double[] J = new double[32];
   private final double[] d = new double[32];
   private final double[] q = new double[32];
   private final int[] r = new int[32];
   private final wx.w[] Q = new wx.w[32];
   private int b;
   private boolean n;
   private final ti m = new ti();
   private class_243 W;
   private wx.v C;
   private double O;
   private int K;

   public wx() {
      this.e.l = 1536;
      this.I.V = (boolean)1;
      this.m.f = 1536;
      this.m.t = 4;
      this.m.R = this.m.j = this.m.d = 0.0;
      this.m.D = u9.u.M;
      this.m.W = u9.o.G;
   }

   public void V() {
      this.e.q();
      this.I.f();
      Arrays.fill(this.N, null);
      Arrays.fill(this.z, null);
      Arrays.fill(this.k, null);
      Arrays.fill(this.A, null);
      Arrays.fill(this.J, 0.0);
      Arrays.fill(this.d, 0.0);
      Arrays.fill(this.q, 0.0);
      Arrays.fill(this.r, 0);
      Arrays.fill(this.Q, null);
      this.b = 0;
      this.n = (boolean)0;
      this.O = 0.0;
      this.K = 0;
      this.W = null;
      this.C = null;
   }

   private class_243 K(class_243 var1, wx.v var2) {
      return c(var1, var2.radius, var2.height, this.M);
   }

   public static class_243 c(class_243 var0, double var1, double var3, Random var5) {
      double var6 = var5.nextDouble() * Math.PI * 2.0;
      double var8 = Math.sqrt(0.04 + var5.nextDouble() * 0.96) * var1;
      return var0.method_1031(Math.cos(var6) * var8, (var5.nextDouble() - 0.5) * var3, Math.sin(var6) * var8);
   }

   public void w(class_243 var1, wx.v var2) {
      if (this.W != null
         && (
            var1.method_1025(this.W) > 4096.0
               || this.C.sparkles != var2.sparkles
               || this.C.comets != var2.comets
               || this.C.radius != var2.radius
               || this.C.height != var2.height
               || this.C.cometCount != var2.cometCount
               || this.C.maximum != var2.maximum
               || this.C.verticalTravel != var2.verticalTravel
               || this.C.sidewaysTravel != var2.sidewaysTravel
               || this.C.motionPeriod != var2.motionPeriod
         )) {
         this.V();
      }

      int var3 = this.W == null ? 1 : 0;
      this.W = var1;
      this.C = var2;
      this.K++;
      int var4 = var2.comets ? Math.clamp(var2.cometCount, 0, this.N.length) : 0;
      this.e.l = Math.clamp(var2.maximum, 128, 2048) - var4;
      this.e.O(var2.speed);
      this.I.b();
      this.m.l = ei.D(var2.color, var2.opacity);
      this.m.b = ei.D(var2.tailColor, var2.opacity * 0.35);
      this.m.a = var2.bloom;
      this.m.k = var2.radius * 2.0 + var2.height + 8.0;
      if (var2.sparkles) {
         int var5 = this.e.l - var4 * 3;
         this.m.f = 2048;
         this.O = this.O + var2.rate * 0.05;
         int var6 = (int)this.O + (var3 != 0 ? (int)Math.min(var5, var2.rate * var2.lifetime * 0.825) : 0);
         this.O = this.O - (int)this.O;
         this.m.i = var2.lifetime * 0.65;
         this.m.B = var2.lifetime;
         this.m.M = var2.size * 0.6;
         this.m.F = var2.size;
         this.m.D = u9.u.M;
         this.m.W = u9.o.G;
         this.m.O = this.m.v = 0.0;
         this.m.C = var2.verticalTravel * 0.5;
         this.m.q = var2.sidewaysTravel * 0.5;
         this.m.e = var2.motionPeriod;
         this.m.G = 1;
         int var7 = Math.max(0, var5 - this.e.I());

         for (int var8 = 0; var8 < Math.min(var7, var6); var8++) {
            this.m.Q = (this.K + var8) % 3 == 0 ? gd.h.L : gd.h.f;
            this.e.V(this.K(var1, var2), this.m);
         }
      }

      for (int var11 = 0; var11 < this.N.length; var11++) {
         if (var11 >= var4) {
            this.N[var11] = this.k[var11] = this.A[var11] = null;
            this.I.T(var11);
         } else {
            if (this.N[var11] != null
               && !(Math.hypot(this.k[var11].field_1352 - var1.field_1352, this.k[var11].field_1350 - var1.field_1350) > var2.radius * 1.4)
               && !(Math.abs(this.k[var11].field_1351 - var1.field_1351) > var2.height * 0.7)
               && !(this.J[var11] > 12.0)) {
               this.A[var11] = this.N[var11];
            } else {
               this.I.T(var11);
               this.k[var11] = this.K(var1, var2);
               double var12 = this.M.nextDouble() * Math.PI * 2.0;
               this.z[var11] = new class_243(Math.cos(var12), (this.M.nextDouble() - 0.5) * 0.4, Math.sin(var12));
               this.J[var11] = 0.0;
               this.r[var11] = 0;
               this.A[var11] = null;
               this.d[var11] = this.M.nextDouble() * Math.PI * 2.0;
            }

            this.r[var11]++;
            if (var2.speed > 0.0) {
               this.J[var11] = this.J[var11] + 0.05;
            }

            this.d[var11] = this.d[var11] + 0.05 * var2.speed * Math.PI * 2.0 / var2.motionPeriod;
            this.k[var11] = this.k[var11].method_1019(this.z[var11].method_1021(var2.speed * 0.01));
            double var13 = this.d[var11];
            this.N[var11] = this.k[var11]
               .method_1031(
                  Math.sin(var13 * 0.83) * var2.sidewaysTravel * 0.5,
                  Math.sin(var13) * var2.verticalTravel * 0.5,
                  Math.cos(var13 * 0.67) * var2.sidewaysTravel * 0.5
               );
            double var14 = this.q[var11] = b(this.r[var11] * 0.05, this.J[var11], this.k[var11], var1, var2);
            ce.y var10 = new ce.y(
               var2.trailLength,
               var2.trailWidth,
               ei.D(var2.color, var2.opacity * var14),
               ei.D(var2.tailColor, var2.opacity * 0.3 * var14),
               var2.bloom,
               true,
               ti.d.l,
               ce.b.P,
               u9.u.W
            );
            this.I.i(var11, this.N[var11], var10);
            this.m.Q = gd.h.f;
            this.m.f = 2048;
            this.m.G = 1;
            this.m.i = this.m.B = 0.15;
            this.m.M = this.m.F = var2.size * 1.6;
            this.m.D = u9.u.W;
            this.m.W = u9.o.D;
            this.m.l = ei.D(var2.color, var2.opacity * var14);
            this.m.b = ei.D(var2.tailColor, var2.opacity * 0.35 * var14);
            this.m.O = this.m.v = 0.0;
            this.m.C = this.m.q = 0.0;
            this.e.V(this.N[var11], this.m);
         }
      }
   }

   public void G(class_243 var1, float var2, class_4604 var3) {
      this.e.c(var1, var2, var3);
      this.I.j(var1, var2);
      this.b = 0;
      this.n = (boolean)0;
      if (this.C != null) {
         double var4 = this.C.radius * 2.0 + this.C.height + 8.0;

         for (int var6 = 0; var6 < this.N.length; var6++) {
            if (this.N[var6] != null) {
               class_243 var7 = this.A[var6] == null ? this.N[var6] : this.A[var6].method_35590(this.N[var6], var2);
               if (!(var7.method_1025(var1) > var4 * var4)) {
                  double var8 = this.C.size * 0.65;
                  if (var3 == null
                     || var3.method_23093(
                        new class_238(
                           var7.field_1352 - var8,
                           var7.field_1351 - var8,
                           var7.field_1350 - var8,
                           var7.field_1352 + var8,
                           var7.field_1351 + var8,
                           var7.field_1350 + var8
                        )
                     )) {
                     int var10 = ei.D(this.C.color, this.C.opacity * this.q[var6]);
                     var10 = ei.W(var10, var10 & 0xFF000000 | 16777215, 0.7);
                     if (var10 >>> 24 != 0) {
                        this.Q[this.b++] = new wx.w(var7, var8, var10);
                     }
                  }
               }
            }
         }

         this.n = (boolean)(this.b > 0 && this.C.bloom ? 1 : 0);
      }
   }

   public boolean f() {
      return this.e.o() || this.I.x() || this.n;
   }

   public boolean y() {
      return this.e.A();
   }

   public int i() {
      return this.e.I() + this.q();
   }

   public int q() {
      int var1 = 0;

      for (class_243 var5 : this.N) {
         if (var5 != null) {
            var1++;
         }
      }

      return var1;
   }

   public int r() {
      return this.I.w();
   }

   public class_243 p(int var1) {
      return this.N[var1];
   }

   public class_243 Z(int var1) {
      return this.Q[var1].position;
   }

   public int W() {
      return this.b;
   }

   public double h(int var1) {
      return this.q[var1];
   }

   public void E(f1 var1, class_243 var2, float var3, boolean var4) {
      this.e.v(var1, var2, var3, var4);
      this.I.t(var1, var3, var4);
      if (!var4 || this.n) {
         var1.blend(ti.d.l);

         for (int var5 = 0; var5 < this.b; var5++) {
            wx.w var6 = this.Q[var5];
            var1.U(var6.position.field_1352, var6.position.field_1351, var6.position.field_1350, var6.size, 0.0, var6.color, gd.h.x, 0.0, 0.0, 0.0);
         }

         var1.blend(ti.d.G);
      }
   }

   private static double b(double var0, double var2, class_243 var4, class_243 var5, wx.v var6) {
      double var7 = Math.hypot(var4.field_1352 - var5.field_1352, var4.field_1350 - var5.field_1350);
      double var9 = Math.abs(var4.field_1351 - var5.field_1351);
      return u9.L(var0 / 0.15)
         * u9.L((12.0 - var2) / 0.35)
         * u9.L((var6.radius * 1.4 - var7) / (var6.radius * 0.15))
         * u9.L((var6.height * 0.7 - var9) / (var6.height * 0.1));
   }

   public record v(
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

   private record w(class_243 position, double size, int color) {
   }
}
