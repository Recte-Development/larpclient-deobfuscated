package com.larpclient;

import java.util.HashMap;
import java.util.Random;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.phys.Vec3;

public final class aw {
   public static final int m = 3200;
   public static final int N = 8192;
   public static final int R = 128;
   private final double[] Z = new double[3200];
   private final double[] H = new double[3200];
   private final double[] e = new double[3200];
   private final double[] K = new double[3200];
   private final double[] M = new double[3200];
   private final double[] o = new double[3200];
   private final double[] I = new double[3200];
   private final double[] f = new double[3200];
   private final double[] l = new double[3200];
   private final double[] i = new double[3200];
   private final double[] Y = new double[3200];
   private final double[] x = new double[3200];
   private final double[] q = new double[3200];
   private final int[] A = new int[3200];
   private final HashMap<Long, aw.c> S = new HashMap<>();
   private final Random c = new Random(1397641047L);
   private Vec3 t;
   private aw.j n;
   private int s;
   private int J;
   private int P;
   private long u;
   private final double[] b = new double[3200];

   public void R() {
      this.t = null;
      this.n = null;
      this.s = this.J = this.P = 0;
      this.u = 0L;
      this.S.clear();
      this.c.setSeed(1397641047L);
   }

   public int R() {
      return this.s;
   }

   public int y() {
      return this.J;
   }

   public int c() {
      return this.S.size();
   }

   public static long g(int var0, int var1) {
      return (long)var0 << 32 ^ var1 & 4294967295L;
   }

   private void a(int var1, Vec3 var2, boolean var3) {
      double var4 = this.c.nextDouble() * Math.PI * 2.0;
      double var6 = Math.sqrt(this.c.nextDouble()) * this.n.radius();
      this.Z[var1] = var2.x + Math.cos(var4) * var6;
      this.e[var1] = var2.z + Math.sin(var4) * var6;
      this.H[var1] = var2.y + (var3 ? this.c.nextDouble() * 24.0 - 6.0 : 16.0 + this.c.nextDouble() * 2.0);
      this.I[var1] = this.c.nextDouble() * Math.PI * 2.0;
      this.f[var1] = 0.8 + this.c.nextDouble() * 1.6;
      this.l[var1] = 0.018 + Math.pow(this.c.nextDouble(), 3.0) * 0.065;
      this.K[var1] = this.Z[var1];
      this.M[var1] = this.H[var1];
      this.o[var1] = this.e[var1];
   }

   public void K(Vec3 var1, aw.j var2, aw.x var3) {
      if (this.t == null || var1.distanceToSqr(this.t) > var2.radius() * var2.radius() || this.n == null || this.n.radius() != var2.radius()) {
         this.R();
      }

      this.n = var2;
      this.t = var1;
      this.u++;
      this.J = 0;
      int var4 = this.n.count();
      int var5 = this.s;
      this.s = var4;

      for (int var6 = var5; var6 < this.s; var6++) {
         this.a(var6, var1, true);
      }

      double var16 = this.u * 0.05;
      double var8 = 0.65 + 0.35 * Math.sin(var16 * 0.43);

      for (int var10 = 0; var10 < this.s; var10++) {
         this.K[var10] = this.Z[var10];
         this.M[var10] = this.H[var10];
         this.o[var10] = this.e[var10];
         this.Z[var10] = this.Z[var10] + 0.05 * (this.n.wind() * 2.8 * var8 + Math.sin(var16 * 1.4 + this.I[var10]) * 0.22);
         this.e[var10] = this.e[var10] + 0.05 * (this.n.wind() * 0.9 * var8 + Math.cos(var16 * 0.9 + this.I[var10]) * 0.16);
         this.H[var10] = this.H[var10] - this.f[var10] * 0.05;
         aw.c var11 = this.S.get(g((int)Math.floor(this.Z[var10]), (int)Math.floor(this.e[var10])));
         double var12 = this.Z[var10] - var1.x;
         double var14 = this.e[var10] - var1.z;
         if (var12 * var12 + var14 * var14 > this.n.radius() * this.n.radius()
            || this.H[var10] < var1.y - 6.0
            || this.H[var10] > var1.y + 20.0
            || var11 != null && this.H[var10] <= var11.roof.height() + 0.16) {
            this.a(var10, var1, false);
         }
      }

      if (this.S.size() >= 8192) {
         this.S.entrySet().removeIf(var1x -> this.u - var1x.getValue().tick() > 20L);
      }

      int var17 = 0;
      int var18 = 0;

      while (this.s > 0 && var18++ < this.s && var17 < 128) {
         int var19 = this.P++ % this.s;
         long var13 = g((int)Math.floor(this.Z[var19]), (int)Math.floor(this.e[var19]));
         aw.c var15 = this.S.get(var13);
         if ((var15 == null || this.u - var15.tick() > 20L) && this.S.size() < 8192) {
            this.S.put(var13, new aw.c(var3.W((int)Math.floor(this.Z[var19]), (int)Math.floor(this.e[var19])), this.u));
            var17++;
         }
      }

      if (this.u % 20L == 0L) {
         this.S.entrySet().removeIf(var1x -> this.u - var1x.getValue().tick() > 40L);
      }

      if (this.P > 320000) {
         this.P = this.P % Math.max(1, this.s);
      }
   }

   public void v(Vec3 var1, float var2) {
      this.J = 0;
      if (this.t != null && this.n != null && !(var1.distanceToSqr(this.t) > this.n.radius() * this.n.radius())) {
         double var3 = Math.clamp(var2, 0.0F, 1.0F);
         double var5 = (this.u - 1L + var3) * 0.05;

         for (int var7 = 0; var7 < this.s; var7++) {
            double var8 = this.K[var7] + (this.Z[var7] - this.K[var7]) * var3;
            double var10 = this.M[var7] + (this.H[var7] - this.M[var7]) * var3;
            double var12 = this.o[var7] + (this.e[var7] - this.o[var7]) * var3;
            aw.c var14 = this.S.get(g((int)Math.floor(var8), (int)Math.floor(var12)));
            if (var14 != null && this.u - var14.tick() <= 20L && !(var10 <= var14.roof.height() + 0.16)) {
               double var15 = Math.sqrt((var8 - var1.x) * (var8 - var1.x) + (var10 - var1.y) * (var10 - var1.y) + (var12 - var1.z) * (var12 - var1.z));
               double var17 = Math.clamp((this.n.radius() - var15) / (this.n.radius() * 0.2), 0.0, 1.0) * Math.clamp((var15 - 0.35) / 0.9, 0.0, 1.0);
               var17 *= Math.clamp((var10 - var14.roof.height() - 0.16) / 0.4, 0.0, 1.0);
               int var19 = (int)Math.round(210.0 * var17);
               if (var19 > 1) {
                  double var20 = 0.22 + 0.78 * Math.clamp(var14.roof.light(), 0.0, 1.0);
                  int var22 = (int)(232.0 * var20);
                  int var23 = (int)(241.0 * var20);
                  int var24 = (int)(255.0 * var20);
                  this.A[this.J] = var19 << 24 | var22 << 16 | var23 << 8 | var24;
                  this.i[this.J] = var8;
                  this.Y[this.J] = var10;
                  this.x[this.J] = var12;
                  this.q[this.J] = this.I[var7] + var5 * (0.35 + 0.15 * this.f[var7]);
                  this.b[this.J] = this.l[var7];
                  this.J++;
               }
            }
         }
      }
   }

   public void j(fz var1) {
      if (this.J != 0) {
         RenderType var2 = d_.S();

         for (int var3 = 0; var3 < this.J; var3++) {
            var1.r(var2, this.i[var3], this.Y[var3], this.x[var3], this.b[var3], this.q[var3], this.A[var3]);
         }

         he.Z("Snow flakes", this.J);
      }
   }

   public Vec3 K(int var1) {
      if (var1 >= 0 && var1 < this.J) {
         return new Vec3(this.i[var1], this.Y[var1], this.x[var1]);
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   private record c(aw.i roof, long tick) {
   }

   public record i(double height, double light) {
      public static final aw.i UNAVAILABLE = new aw.i(Double.POSITIVE_INFINITY, 0.0);
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

   @FunctionalInterface
   public interface x {
      aw.i W(int var1, int var2);
   }
}
