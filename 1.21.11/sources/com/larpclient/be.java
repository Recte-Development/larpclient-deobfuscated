package com.larpclient;

import java.util.HashMap;
import java.util.Random;
import net.minecraft.class_1921;
import net.minecraft.class_243;

public final class be {
   public static final int Q = 3200;
   public static final int L = 8192;
   public static final int f = 128;
   private final double[] E = new double[3200];
   private final double[] u = new double[3200];
   private final double[] l = new double[3200];
   private final double[] Z = new double[3200];
   private final double[] q = new double[3200];
   private final double[] p = new double[3200];
   private final double[] x = new double[3200];
   private final double[] P = new double[3200];
   private final double[] X = new double[3200];
   private final double[] G = new double[3200];
   private final double[] z = new double[3200];
   private final double[] y = new double[3200];
   private final double[] e = new double[3200];
   private final int[] Y = new int[3200];
   private final HashMap<Long, be.u> C = new HashMap<>();
   private final Random M = new Random(1397641047L);
   private class_243 N;
   private be.o O;
   private int a;
   private int F;
   private int b;
   private long U;
   private final double[] n = new double[3200];

   public void l() {
      this.N = null;
      this.O = null;
      this.a = this.F = this.b = 0;
      this.U = 0L;
      this.C.clear();
      this.M.setSeed(1397641047L);
   }

   public int f() {
      return this.a;
   }

   public int X() {
      return this.F;
   }

   public int s() {
      return this.C.size();
   }

   public static long a(int var0, int var1) {
      return (long)var0 << 32 ^ var1 & 4294967295L;
   }

   private void a(int var1, class_243 var2, boolean var3) {
      double var4 = this.M.nextDouble() * Math.PI * 2.0;
      double var6 = Math.sqrt(this.M.nextDouble()) * this.O.radius();
      this.E[var1] = var2.field_1352 + Math.cos(var4) * var6;
      this.l[var1] = var2.field_1350 + Math.sin(var4) * var6;
      this.u[var1] = var2.field_1351 + (var3 ? this.M.nextDouble() * 24.0 - 6.0 : 16.0 + this.M.nextDouble() * 2.0);
      this.x[var1] = this.M.nextDouble() * Math.PI * 2.0;
      this.P[var1] = 0.8 + this.M.nextDouble() * 1.6;
      this.X[var1] = 0.018 + Math.pow(this.M.nextDouble(), 3.0) * 0.065;
      this.Z[var1] = this.E[var1];
      this.q[var1] = this.u[var1];
      this.p[var1] = this.l[var1];
   }

   public void X(class_243 var1, be.o var2, be.h var3) {
      if (this.N == null || var1.method_1025(this.N) > var2.radius() * var2.radius() || this.O == null || this.O.radius() != var2.radius()) {
         this.l();
      }

      this.O = var2;
      this.N = var1;
      this.U++;
      this.F = 0;
      int var4 = this.O.count();
      int var5 = this.a;
      this.a = var4;

      for (int var6 = var5; var6 < this.a; var6++) {
         this.a(var6, var1, true);
      }

      double var16 = this.U * 0.05;
      double var8 = 0.65 + 0.35 * Math.sin(var16 * 0.43);

      for (int var10 = 0; var10 < this.a; var10++) {
         this.Z[var10] = this.E[var10];
         this.q[var10] = this.u[var10];
         this.p[var10] = this.l[var10];
         this.E[var10] = this.E[var10] + 0.05 * (this.O.wind() * 2.8 * var8 + Math.sin(var16 * 1.4 + this.x[var10]) * 0.22);
         this.l[var10] = this.l[var10] + 0.05 * (this.O.wind() * 0.9 * var8 + Math.cos(var16 * 0.9 + this.x[var10]) * 0.16);
         this.u[var10] = this.u[var10] - this.P[var10] * 0.05;
         be.u var11 = this.C.get(a((int)Math.floor(this.E[var10]), (int)Math.floor(this.l[var10])));
         double var12 = this.E[var10] - var1.field_1352;
         double var14 = this.l[var10] - var1.field_1350;
         if (var12 * var12 + var14 * var14 > this.O.radius() * this.O.radius()
            || this.u[var10] < var1.field_1351 - 6.0
            || this.u[var10] > var1.field_1351 + 20.0
            || var11 != null && this.u[var10] <= var11.roof.height() + 0.16) {
            this.a(var10, var1, false);
         }
      }

      if (this.C.size() >= 8192) {
         this.C.entrySet().removeIf(var1x -> this.U - var1x.getValue().tick() > 20L);
      }

      int var17 = 0;
      int var18 = 0;

      while (this.a > 0 && var18++ < this.a && var17 < 128) {
         int var19 = this.b++ % this.a;
         long var13 = a((int)Math.floor(this.E[var19]), (int)Math.floor(this.l[var19]));
         be.u var15 = this.C.get(var13);
         if ((var15 == null || this.U - var15.tick() > 20L) && this.C.size() < 8192) {
            this.C.put(var13, new be.u(var3.t((int)Math.floor(this.E[var19]), (int)Math.floor(this.l[var19])), this.U));
            var17++;
         }
      }

      if (this.U % 20L == 0L) {
         this.C.entrySet().removeIf(var1x -> this.U - var1x.getValue().tick() > 40L);
      }

      if (this.b > 320000) {
         this.b = this.b % Math.max(1, this.a);
      }
   }

   public void u(class_243 var1, float var2) {
      this.F = 0;
      if (this.N != null && this.O != null && !(var1.method_1025(this.N) > this.O.radius() * this.O.radius())) {
         double var3 = Math.clamp(var2, 0.0F, 1.0F);
         double var5 = (this.U - 1L + var3) * 0.05;

         for (int var7 = 0; var7 < this.a; var7++) {
            double var8 = this.Z[var7] + (this.E[var7] - this.Z[var7]) * var3;
            double var10 = this.q[var7] + (this.u[var7] - this.q[var7]) * var3;
            double var12 = this.p[var7] + (this.l[var7] - this.p[var7]) * var3;
            be.u var14 = this.C.get(a((int)Math.floor(var8), (int)Math.floor(var12)));
            if (var14 != null && this.U - var14.tick() <= 20L && !(var10 <= var14.roof.height() + 0.16)) {
               double var15 = Math.sqrt(
                  (var8 - var1.field_1352) * (var8 - var1.field_1352)
                     + (var10 - var1.field_1351) * (var10 - var1.field_1351)
                     + (var12 - var1.field_1350) * (var12 - var1.field_1350)
               );
               double var17 = Math.clamp((this.O.radius() - var15) / (this.O.radius() * 0.2), 0.0, 1.0) * Math.clamp((var15 - 0.35) / 0.9, 0.0, 1.0);
               var17 *= Math.clamp((var10 - var14.roof.height() - 0.16) / 0.4, 0.0, 1.0);
               int var19 = (int)Math.round(210.0 * var17);
               if (var19 > 1) {
                  double var20 = 0.22 + 0.78 * Math.clamp(var14.roof.light(), 0.0, 1.0);
                  int var22 = (int)(232.0 * var20);
                  int var23 = (int)(241.0 * var20);
                  int var24 = (int)(255.0 * var20);
                  this.Y[this.F] = var19 << 24 | var22 << 16 | var23 << 8 | var24;
                  this.G[this.F] = var8;
                  this.z[this.F] = var10;
                  this.y[this.F] = var12;
                  this.e[this.F] = this.x[var7] + var5 * (0.35 + 0.15 * this.P[var7]);
                  this.n[this.F] = this.X[var7];
                  this.F++;
               }
            }
         }
      }
   }

   public void N(f1 var1) {
      if (this.F != 0) {
         class_1921 var2 = u3.B();

         for (int var3 = 0; var3 < this.F; var3++) {
            var1.Q(var2, this.G[var3], this.z[var3], this.y[var3], this.n[var3], this.e[var3], this.Y[var3]);
         }

         c5.V("Snow flakes", this.F);
      }
   }

   public class_243 j(int var1) {
      if (var1 >= 0 && var1 < this.F) {
         return new class_243(this.G[var1], this.z[var1], this.y[var1]);
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   @FunctionalInterface
   public interface h {
      be.v t(int var1, int var2);
   }

   public record o(double radius, double density, double wind, int limit) {
      public o(double radius, double density, double wind, int limit) {
         if (Double.isFinite(radius) && Double.isFinite(density) && Double.isFinite(wind)) {
            radius = Math.clamp(radius, 12.0, 40.0);
            density = Math.clamp(density, 0.0, 1.0);
            wind = Math.clamp(wind, 0.0, 1.0);
            limit = Math.clamp(limit, 0, 3200);
            this.radius = radius;
            this.density = density;
            this.wind = wind;
            this.limit = limit;
         } else {
            throw new IllegalArgumentException("Non-finite snow options");
         }
      }

      public int count() {
         return (int)Math.round(this.limit * this.density);
      }
   }

   private record u(be.v roof, long tick) {
   }

   public record v(double height, double light) {
      public static final be.v UNAVAILABLE = new be.v(Double.POSITIVE_INFINITY, 0.0);
   }
}
