package com.larpclient;

import java.util.HashMap;
import java.util.Random;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.phys.Vec3;

public final class on {
   public static final int T = 3200;
   public static final int n = 8192;
   public static final int b = 128;
   private final double[] w = new double[3200];
   private final double[] H = new double[3200];
   private final double[] q = new double[3200];
   private final double[] t = new double[3200];
   private final double[] O = new double[3200];
   private final double[] W = new double[3200];
   private final double[] z = new double[3200];
   private final double[] B = new double[3200];
   private final double[] M = new double[3200];
   private final double[] Y = new double[3200];
   private final double[] m = new double[3200];
   private final double[] Z = new double[3200];
   private final double[] L = new double[3200];
   private final int[] y = new int[3200];
   private final HashMap<Long, on.h> X = new HashMap<>();
   private final Random K = new Random(1397641047L);
   private Vec3 s;
   private on.o N;
   private int r;
   private int u;
   private int a;
   private long x;
   private final double[] J = new double[3200];

   public void k() {
      this.s = null;
      this.N = null;
      this.r = this.u = this.a = 0;
      this.x = 0L;
      this.X.clear();
      this.K.setSeed(1397641047L);
   }

   public int I() {
      return this.r;
   }

   public int k() {
      return this.u;
   }

   public int A() {
      return this.X.size();
   }

   public static long L(int var0, int var1) {
      return (long)var0 << 32 ^ var1 & 4294967295L;
   }

   private void T(int var1, Vec3 var2, boolean var3) {
      double var4 = this.K.nextDouble() * Math.PI * 2.0;
      double var6 = Math.sqrt(this.K.nextDouble()) * this.N.radius();
      this.w[var1] = var2.x + Math.cos(var4) * var6;
      this.q[var1] = var2.z + Math.sin(var4) * var6;
      this.H[var1] = var2.y + (var3 ? this.K.nextDouble() * 24.0 - 6.0 : 16.0 + this.K.nextDouble() * 2.0);
      this.z[var1] = this.K.nextDouble() * Math.PI * 2.0;
      this.B[var1] = 0.8 + this.K.nextDouble() * 1.6;
      this.M[var1] = 0.018 + Math.pow(this.K.nextDouble(), 3.0) * 0.065;
      this.t[var1] = this.w[var1];
      this.O[var1] = this.H[var1];
      this.W[var1] = this.q[var1];
   }

   public void I(Vec3 var1, on.o var2, on.c var3) {
      if (this.s == null || var1.distanceToSqr(this.s) > var2.radius() * var2.radius() || this.N == null || this.N.radius() != var2.radius()) {
         this.k();
      }

      this.N = var2;
      this.s = var1;
      this.x++;
      this.u = 0;
      int var4 = this.N.count();
      int var5 = this.r;
      this.r = var4;

      for (int var6 = var5; var6 < this.r; var6++) {
         this.T(var6, var1, true);
      }

      double var16 = this.x * 0.05;
      double var8 = 0.65 + 0.35 * Math.sin(var16 * 0.43);

      for (int var10 = 0; var10 < this.r; var10++) {
         this.t[var10] = this.w[var10];
         this.O[var10] = this.H[var10];
         this.W[var10] = this.q[var10];
         this.w[var10] = this.w[var10] + 0.05 * (this.N.wind() * 2.8 * var8 + Math.sin(var16 * 1.4 + this.z[var10]) * 0.22);
         this.q[var10] = this.q[var10] + 0.05 * (this.N.wind() * 0.9 * var8 + Math.cos(var16 * 0.9 + this.z[var10]) * 0.16);
         this.H[var10] = this.H[var10] - this.B[var10] * 0.05;
         on.h var11 = this.X.get(L((int)Math.floor(this.w[var10]), (int)Math.floor(this.q[var10])));
         double var12 = this.w[var10] - var1.x;
         double var14 = this.q[var10] - var1.z;
         if (var12 * var12 + var14 * var14 > this.N.radius() * this.N.radius()
            || this.H[var10] < var1.y - 6.0
            || this.H[var10] > var1.y + 20.0
            || var11 != null && this.H[var10] <= var11.roof.height() + 0.16) {
            this.T(var10, var1, false);
         }
      }

      if (this.X.size() >= 8192) {
         this.X.entrySet().removeIf(var1x -> this.x - var1x.getValue().tick() > 20L);
      }

      int var17 = 0;
      int var18 = 0;

      while (this.r > 0 && var18++ < this.r && var17 < 128) {
         int var19 = this.a++ % this.r;
         long var13 = L((int)Math.floor(this.w[var19]), (int)Math.floor(this.q[var19]));
         on.h var15 = this.X.get(var13);
         if ((var15 == null || this.x - var15.tick() > 20L) && this.X.size() < 8192) {
            this.X.put(var13, new on.h(var3.j((int)Math.floor(this.w[var19]), (int)Math.floor(this.q[var19])), this.x));
            var17++;
         }
      }

      if (this.x % 20L == 0L) {
         this.X.entrySet().removeIf(var1x -> this.x - var1x.getValue().tick() > 40L);
      }

      if (this.a > 320000) {
         this.a = this.a % Math.max(1, this.r);
      }
   }

   public void i(Vec3 var1, float var2) {
      this.u = 0;
      if (this.s != null && this.N != null && !(var1.distanceToSqr(this.s) > this.N.radius() * this.N.radius())) {
         double var3 = Math.clamp(var2, 0.0F, 1.0F);
         double var5 = (this.x - 1L + var3) * 0.05;

         for (int var7 = 0; var7 < this.r; var7++) {
            double var8 = this.t[var7] + (this.w[var7] - this.t[var7]) * var3;
            double var10 = this.O[var7] + (this.H[var7] - this.O[var7]) * var3;
            double var12 = this.W[var7] + (this.q[var7] - this.W[var7]) * var3;
            on.h var14 = this.X.get(L((int)Math.floor(var8), (int)Math.floor(var12)));
            if (var14 != null && this.x - var14.tick() <= 20L && !(var10 <= var14.roof.height() + 0.16)) {
               double var15 = Math.sqrt((var8 - var1.x) * (var8 - var1.x) + (var10 - var1.y) * (var10 - var1.y) + (var12 - var1.z) * (var12 - var1.z));
               double var17 = Math.clamp((this.N.radius() - var15) / (this.N.radius() * 0.2), 0.0, 1.0) * Math.clamp((var15 - 0.35) / 0.9, 0.0, 1.0);
               var17 *= Math.clamp((var10 - var14.roof.height() - 0.16) / 0.4, 0.0, 1.0);
               int var19 = (int)Math.round(210.0 * var17);
               if (var19 > 1) {
                  double var20 = 0.22 + 0.78 * Math.clamp(var14.roof.light(), 0.0, 1.0);
                  int var22 = (int)(232.0 * var20);
                  int var23 = (int)(241.0 * var20);
                  int var24 = (int)(255.0 * var20);
                  this.y[this.u] = var19 << 24 | var22 << 16 | var23 << 8 | var24;
                  this.Y[this.u] = var8;
                  this.m[this.u] = var10;
                  this.Z[this.u] = var12;
                  this.L[this.u] = this.z[var7] + var5 * (0.35 + 0.15 * this.B[var7]);
                  this.J[this.u] = this.M[var7];
                  this.u++;
               }
            }
         }
      }
   }

   public void S(r6 var1) {
      if (this.u != 0) {
         RenderType var2 = uf.S();

         for (int var3 = 0; var3 < this.u; var3++) {
            var1.k(var2, this.Y[var3], this.m[var3], this.Z[var3], this.J[var3], this.L[var3], this.y[var3]);
         }

         h_.A("Snow flakes", this.u);
      }
   }

   public Vec3 A(int var1) {
      if (var1 >= 0 && var1 < this.u) {
         return new Vec3(this.Y[var1], this.m[var1], this.Z[var1]);
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   @FunctionalInterface
   public interface c {
      on.u j(int var1, int var2);
   }

   private record h(on.u roof, long tick) {
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

   public record u(double height, double light) {
      public static final on.u UNAVAILABLE = new on.u(Double.POSITIVE_INFINITY, 0.0);
   }
}
