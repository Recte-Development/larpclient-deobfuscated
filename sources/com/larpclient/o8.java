package com.larpclient;

import java.util.HashMap;
import java.util.Random;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.phys.Vec3;

public final class o8 {
   public static final int I = 3200;
   public static final int g = 8192;
   public static final int r = 128;
   private final double[] c = new double[3200];
   private final double[] V = new double[3200];
   private final double[] E = new double[3200];
   private final double[] R = new double[3200];
   private final double[] Y = new double[3200];
   private final double[] J = new double[3200];
   private final double[] a = new double[3200];
   private final double[] m = new double[3200];
   private final double[] A = new double[3200];
   private final double[] q = new double[3200];
   private final double[] s = new double[3200];
   private final double[] i = new double[3200];
   private final double[] F = new double[3200];
   private final int[] H = new int[3200];
   private final HashMap<Long, o8.p> e = new HashMap<>();
   private final Random N = new Random(1397641047L);
   private Vec3 T;
   private o8.h W;
   private int x;
   private int K;
   private int v;
   private long M;
   private final double[] l = new double[3200];

   public void m() {
      this.T = null;
      this.W = null;
      this.x = this.K = this.v = 0;
      this.M = 0L;
      this.e.clear();
      this.N.setSeed(1397641047L);
   }

   public int s() {
      return this.x;
   }

   public int i() {
      return this.K;
   }

   public int n() {
      return this.e.size();
   }

   public static long v(int var0, int var1) {
      return (long)var0 << 32 ^ var1 & 4294967295L;
   }

   private void Z(int var1, Vec3 var2, boolean var3) {
      double var4 = this.N.nextDouble() * Math.PI * 2.0;
      double var6 = Math.sqrt(this.N.nextDouble()) * this.W.radius();
      this.c[var1] = var2.x + Math.cos(var4) * var6;
      this.E[var1] = var2.z + Math.sin(var4) * var6;
      this.V[var1] = var2.y + (var3 ? this.N.nextDouble() * 24.0 - 6.0 : 16.0 + this.N.nextDouble() * 2.0);
      this.a[var1] = this.N.nextDouble() * Math.PI * 2.0;
      this.m[var1] = 0.8 + this.N.nextDouble() * 1.6;
      this.A[var1] = 0.018 + Math.pow(this.N.nextDouble(), 3.0) * 0.065;
      this.R[var1] = this.c[var1];
      this.Y[var1] = this.V[var1];
      this.J[var1] = this.E[var1];
   }

   public void Y(Vec3 var1, o8.h var2, o8.a var3) {
      if (this.T == null || var1.distanceToSqr(this.T) > var2.radius() * var2.radius() || this.W == null || this.W.radius() != var2.radius()) {
         this.m();
      }

      this.W = var2;
      this.T = var1;
      this.M++;
      this.K = 0;
      int var4 = this.W.count();
      int var5 = this.x;
      this.x = var4;

      for (int var6 = var5; var6 < this.x; var6++) {
         this.Z(var6, var1, true);
      }

      double var16 = this.M * 0.05;
      double var8 = 0.65 + 0.35 * Math.sin(var16 * 0.43);

      for (int var10 = 0; var10 < this.x; var10++) {
         this.R[var10] = this.c[var10];
         this.Y[var10] = this.V[var10];
         this.J[var10] = this.E[var10];
         this.c[var10] = this.c[var10] + 0.05 * (this.W.wind() * 2.8 * var8 + Math.sin(var16 * 1.4 + this.a[var10]) * 0.22);
         this.E[var10] = this.E[var10] + 0.05 * (this.W.wind() * 0.9 * var8 + Math.cos(var16 * 0.9 + this.a[var10]) * 0.16);
         this.V[var10] = this.V[var10] - this.m[var10] * 0.05;
         o8.p var11 = this.e.get(v((int)Math.floor(this.c[var10]), (int)Math.floor(this.E[var10])));
         double var12 = this.c[var10] - var1.x;
         double var14 = this.E[var10] - var1.z;
         if (var12 * var12 + var14 * var14 > this.W.radius() * this.W.radius()
            || this.V[var10] < var1.y - 6.0
            || this.V[var10] > var1.y + 20.0
            || var11 != null && this.V[var10] <= var11.roof.height() + 0.16) {
            this.Z(var10, var1, false);
         }
      }

      if (this.e.size() >= 8192) {
         this.e.entrySet().removeIf(var1x -> this.M - var1x.getValue().tick() > 20L);
      }

      int var17 = 0;
      int var18 = 0;

      while (this.x > 0 && var18++ < this.x && var17 < 128) {
         int var19 = this.v++ % this.x;
         long var13 = v((int)Math.floor(this.c[var19]), (int)Math.floor(this.E[var19]));
         o8.p var15 = this.e.get(var13);
         if ((var15 == null || this.M - var15.tick() > 20L) && this.e.size() < 8192) {
            this.e.put(var13, new o8.p(var3.r((int)Math.floor(this.c[var19]), (int)Math.floor(this.E[var19])), this.M));
            var17++;
         }
      }

      if (this.M % 20L == 0L) {
         this.e.entrySet().removeIf(var1x -> this.M - var1x.getValue().tick() > 40L);
      }

      if (this.v > 320000) {
         this.v = this.v % Math.max(1, this.x);
      }
   }

   public void H(Vec3 var1, float var2) {
      this.K = 0;
      if (this.T != null && this.W != null && !(var1.distanceToSqr(this.T) > this.W.radius() * this.W.radius())) {
         double var3 = Math.clamp(var2, 0.0F, 1.0F);
         double var5 = (this.M - 1L + var3) * 0.05;

         for (int var7 = 0; var7 < this.x; var7++) {
            double var8 = this.R[var7] + (this.c[var7] - this.R[var7]) * var3;
            double var10 = this.Y[var7] + (this.V[var7] - this.Y[var7]) * var3;
            double var12 = this.J[var7] + (this.E[var7] - this.J[var7]) * var3;
            o8.p var14 = this.e.get(v((int)Math.floor(var8), (int)Math.floor(var12)));
            if (var14 != null && this.M - var14.tick() <= 20L && !(var10 <= var14.roof.height() + 0.16)) {
               double var15 = Math.sqrt((var8 - var1.x) * (var8 - var1.x) + (var10 - var1.y) * (var10 - var1.y) + (var12 - var1.z) * (var12 - var1.z));
               double var17 = Math.clamp((this.W.radius() - var15) / (this.W.radius() * 0.2), 0.0, 1.0) * Math.clamp((var15 - 0.35) / 0.9, 0.0, 1.0);
               var17 *= Math.clamp((var10 - var14.roof.height() - 0.16) / 0.4, 0.0, 1.0);
               int var19 = (int)Math.round(210.0 * var17);
               if (var19 > 1) {
                  double var20 = 0.22 + 0.78 * Math.clamp(var14.roof.light(), 0.0, 1.0);
                  int var22 = (int)(232.0 * var20);
                  int var23 = (int)(241.0 * var20);
                  int var24 = (int)(255.0 * var20);
                  this.H[this.K] = var19 << 24 | var22 << 16 | var23 << 8 | var24;
                  this.q[this.K] = var8;
                  this.s[this.K] = var10;
                  this.i[this.K] = var12;
                  this.F[this.K] = this.a[var7] + var5 * (0.35 + 0.15 * this.m[var7]);
                  this.l[this.K] = this.A[var7];
                  this.K++;
               }
            }
         }
      }
   }

   public void W(ki var1) {
      if (this.K != 0) {
         RenderType var2 = zy.b();

         for (int var3 = 0; var3 < this.K; var3++) {
            var1.j(var2, this.q[var3], this.s[var3], this.i[var3], this.l[var3], this.F[var3], this.H[var3]);
         }

         kx.U("Snow flakes", this.K);
      }
   }

   public Vec3 a(int var1) {
      if (var1 >= 0 && var1 < this.K) {
         return new Vec3(this.q[var1], this.s[var1], this.i[var1]);
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   @FunctionalInterface
   public interface a {
      o8.r r(int var1, int var2);
   }

   public record h(double radius, double density, double wind, int limit) {
      public h(double radius, double density, double wind, int limit) {
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

   private record p(o8.r roof, long tick) {
   }

   public record r(double height, double light) {
      public static final o8.r UNAVAILABLE = new o8.r(Double.POSITIVE_INFINITY, 0.0);
   }
}
