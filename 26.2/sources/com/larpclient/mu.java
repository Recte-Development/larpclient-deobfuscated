package com.larpclient;

import java.util.HashMap;
import java.util.Random;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.phys.Vec3;

public final class mu {
   public static final int x = 3200;
   public static final int z = 8192;
   public static final int p = 128;
   private final double[] B = new double[3200];
   private final double[] m = new double[3200];
   private final double[] S = new double[3200];
   private final double[] N = new double[3200];
   private final double[] O = new double[3200];
   private final double[] o = new double[3200];
   private final double[] Q = new double[3200];
   private final double[] c = new double[3200];
   private final double[] q = new double[3200];
   private final double[] J = new double[3200];
   private final double[] X = new double[3200];
   private final double[] T = new double[3200];
   private final double[] d = new double[3200];
   private final int[] P = new int[3200];
   private final HashMap<Long, mu.o> i = new HashMap<>();
   private final Random F = new Random(1397641047L);
   private Vec3 V;
   private mu.j R;
   private int M;
   private int a;
   private int E;
   private long l;
   private final double[] k = new double[3200];

   public void T() {
      this.V = null;
      this.R = null;
      this.M = this.a = this.E = 0;
      this.l = 0L;
      this.i.clear();
      this.F.setSeed(1397641047L);
   }

   public int b() {
      return this.M;
   }

   public int s() {
      return this.a;
   }

   public int Z() {
      return this.i.size();
   }

   public static long s(int var0, int var1) {
      return (long)var0 << 32 ^ var1 & 4294967295L;
   }

   private void U(int var1, Vec3 var2, boolean var3) {
      double var4 = this.F.nextDouble() * Math.PI * 2.0;
      double var6 = Math.sqrt(this.F.nextDouble()) * this.R.radius();
      this.B[var1] = var2.x + Math.cos(var4) * var6;
      this.S[var1] = var2.z + Math.sin(var4) * var6;
      this.m[var1] = var2.y + (var3 ? this.F.nextDouble() * 24.0 - 6.0 : 16.0 + this.F.nextDouble() * 2.0);
      this.Q[var1] = this.F.nextDouble() * Math.PI * 2.0;
      this.c[var1] = 0.8 + this.F.nextDouble() * 1.6;
      this.q[var1] = 0.018 + Math.pow(this.F.nextDouble(), 3.0) * 0.065;
      this.N[var1] = this.B[var1];
      this.O[var1] = this.m[var1];
      this.o[var1] = this.S[var1];
   }

   public void M(Vec3 var1, mu.j var2, mu.a var3) {
      if (this.V == null || var1.distanceToSqr(this.V) > var2.radius() * var2.radius() || this.R == null || this.R.radius() != var2.radius()) {
         this.T();
      }

      this.R = var2;
      this.V = var1;
      this.l++;
      this.a = 0;
      int var4 = this.R.count();
      int var5 = this.M;
      this.M = var4;

      for (int var6 = var5; var6 < this.M; var6++) {
         this.U(var6, var1, true);
      }

      double var16 = this.l * 0.05;
      double var8 = 0.65 + 0.35 * Math.sin(var16 * 0.43);

      for (int var10 = 0; var10 < this.M; var10++) {
         this.N[var10] = this.B[var10];
         this.O[var10] = this.m[var10];
         this.o[var10] = this.S[var10];
         this.B[var10] = this.B[var10] + 0.05 * (this.R.wind() * 2.8 * var8 + Math.sin(var16 * 1.4 + this.Q[var10]) * 0.22);
         this.S[var10] = this.S[var10] + 0.05 * (this.R.wind() * 0.9 * var8 + Math.cos(var16 * 0.9 + this.Q[var10]) * 0.16);
         this.m[var10] = this.m[var10] - this.c[var10] * 0.05;
         mu.o var11 = this.i.get(s((int)Math.floor(this.B[var10]), (int)Math.floor(this.S[var10])));
         double var12 = this.B[var10] - var1.x;
         double var14 = this.S[var10] - var1.z;
         if (var12 * var12 + var14 * var14 > this.R.radius() * this.R.radius()
            || this.m[var10] < var1.y - 6.0
            || this.m[var10] > var1.y + 20.0
            || var11 != null && this.m[var10] <= var11.roof.height() + 0.16) {
            this.U(var10, var1, false);
         }
      }

      if (this.i.size() >= 8192) {
         this.i.entrySet().removeIf(var1x -> this.l - var1x.getValue().tick() > 20L);
      }

      int var17 = 0;
      int var18 = 0;

      while (this.M > 0 && var18++ < this.M && var17 < 128) {
         int var19 = this.E++ % this.M;
         long var13 = s((int)Math.floor(this.B[var19]), (int)Math.floor(this.S[var19]));
         mu.o var15 = this.i.get(var13);
         if ((var15 == null || this.l - var15.tick() > 20L) && this.i.size() < 8192) {
            this.i.put(var13, new mu.o(var3.N((int)Math.floor(this.B[var19]), (int)Math.floor(this.S[var19])), this.l));
            var17++;
         }
      }

      if (this.l % 20L == 0L) {
         this.i.entrySet().removeIf(var1x -> this.l - var1x.getValue().tick() > 40L);
      }

      if (this.E > 320000) {
         this.E = this.E % Math.max(1, this.M);
      }
   }

   public void b(Vec3 var1, float var2) {
      this.a = 0;
      if (this.V != null && this.R != null && !(var1.distanceToSqr(this.V) > this.R.radius() * this.R.radius())) {
         double var3 = Math.clamp(var2, 0.0F, 1.0F);
         double var5 = (this.l - 1L + var3) * 0.05;

         for (int var7 = 0; var7 < this.M; var7++) {
            double var8 = this.N[var7] + (this.B[var7] - this.N[var7]) * var3;
            double var10 = this.O[var7] + (this.m[var7] - this.O[var7]) * var3;
            double var12 = this.o[var7] + (this.S[var7] - this.o[var7]) * var3;
            mu.o var14 = this.i.get(s((int)Math.floor(var8), (int)Math.floor(var12)));
            if (var14 != null && this.l - var14.tick() <= 20L && !(var10 <= var14.roof.height() + 0.16)) {
               double var15 = Math.sqrt((var8 - var1.x) * (var8 - var1.x) + (var10 - var1.y) * (var10 - var1.y) + (var12 - var1.z) * (var12 - var1.z));
               double var17 = Math.clamp((this.R.radius() - var15) / (this.R.radius() * 0.2), 0.0, 1.0) * Math.clamp((var15 - 0.35) / 0.9, 0.0, 1.0);
               var17 *= Math.clamp((var10 - var14.roof.height() - 0.16) / 0.4, 0.0, 1.0);
               int var19 = (int)Math.round(210.0 * var17);
               if (var19 > 1) {
                  double var20 = 0.22 + 0.78 * Math.clamp(var14.roof.light(), 0.0, 1.0);
                  int var22 = (int)(232.0 * var20);
                  int var23 = (int)(241.0 * var20);
                  int var24 = (int)(255.0 * var20);
                  this.P[this.a] = var19 << 24 | var22 << 16 | var23 << 8 | var24;
                  this.J[this.a] = var8;
                  this.X[this.a] = var10;
                  this.T[this.a] = var12;
                  this.d[this.a] = this.Q[var7] + var5 * (0.35 + 0.15 * this.c[var7]);
                  this.k[this.a] = this.q[var7];
                  this.a++;
               }
            }
         }
      }
   }

   public void J(kj var1) {
      if (this.a != 0) {
         RenderType var2 = wx.N();

         for (int var3 = 0; var3 < this.a; var3++) {
            var1.M(var2, this.J[var3], this.X[var3], this.T[var3], this.k[var3], this.d[var3], this.P[var3]);
         }

         zj.K("Snow flakes", this.a);
      }
   }

   public Vec3 a(int var1) {
      if (var1 >= 0 && var1 < this.a) {
         return new Vec3(this.J[var1], this.X[var1], this.T[var1]);
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   @FunctionalInterface
   public interface a {
      mu.h N(int var1, int var2);
   }

   public record h(double height, double light) {
      public static final mu.h UNAVAILABLE = new mu.h(Double.POSITIVE_INFINITY, 0.0);
   }

   public record j(double radius, double density, double wind, int limit) {
      public j(double radius, double density, double wind, int limit) {
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

   private record o(mu.h roof, long tick) {
   }
}
