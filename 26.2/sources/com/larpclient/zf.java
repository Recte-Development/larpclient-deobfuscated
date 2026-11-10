package com.larpclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public final class zf {
   static final double G = 2.0;
   static final double z = Math.sqrt(2.0);
   static final double U = 1.5;
   static final double v = 0.5;
   private static final int[][] M = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
   private static final int[][] o = new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

   public static zf.v B(zf.q var0, int var1, int var2, int var3, zf.f var4, zf.s var5, zf.z var6) {
      if (var4.reached(var1, var2, var3)) {
         return new zf.v(List.of(), true, 0, 0.0);
      }

      HashMap var7 = new HashMap();
      PriorityQueue var8 = new PriorityQueue();
      zf.g var9 = new zf.g(O(var1, var2, var3), var1, var2, var3);
      var9.k = var4.estimate(var1, var2, var3);
      var9.H = var9.k;
      var7.put(var9.b, var9);
      var8.add(var9);
      zf.g var10 = var9;
      int var11 = 0;
      zf.c var12 = new zf.c(var0, var4, var5, var6, var7, var8);

      while (!var8.isEmpty()) {
         zf.g var13 = (zf.g)var8.poll();
         if (!var13.t) {
            var13.t = (boolean)1;
            if (var4.reached(var13.l, var13.N, var13.O)) {
               return new zf.v(x(var13), true, var11, var13.E);
            }

            if (var13.k < var10.k || var13.k == var10.k && var13.E < var10.E) {
               var10 = var13;
            }

            if (++var11 > var6.maxNodes()) {
               break;
            }

            var12.z(var13);
         }
      }

      return var6.partial() && var10 != var9 ? new zf.v(x(var10), false, var11, var10.E) : new zf.v(null, false, var11, 0.0);
   }

   private static List<zf.h> x(zf.g var0) {
      ArrayList var1 = new ArrayList();

      for (zf.g var2 = var0; var2.w != null; var2 = var2.w) {
         var1.add(new zf.h(var2.l, var2.N, var2.O, var2.Z, var2.T));
      }

      Collections.reverse(var1);
      return var1;
   }

   static double i(int var0, int var1, int var2) {
      int var3 = Math.abs(var0);
      int var4 = Math.abs(var2);
      return Math.max(var3, var4) + (z - 1.0) * Math.min(var3, var4) + Math.abs(var1) * 0.5;
   }

   public static long O(int var0, int var1, int var2) {
      return (long)(var0 & 67108863) << 38 | (long)(var2 & 67108863) << 12 | var1 & 4095;
   }

   public static int W(long var0) {
      return (int)(var0 >> 38);
   }

   public static int d(long var0) {
      return (int)(var0 << 52 >> 52);
   }

   public static int F(long var0) {
      return (int)(var0 << 26 >> 38);
   }

   private zf() {
   }

   public record a(int x, int y, int z) {
   }

   public enum b {
      t,
      p,
      v,
      S,
      C,
      B;
   }

   private static final class c {
      final zf.q x;
      final zf.f E;
      final zf.s n;
      final zf.z P;
      final Map<Long, zf.g> j;
      final PriorityQueue<zf.g> p;

      c(zf.q var1, zf.f var2, zf.s var3, zf.z var4, Map<Long, zf.g> var5, PriorityQueue<zf.g> var6) {
         this.x = var1;
         this.E = var2;
         this.n = var3;
         this.P = var4;
         this.j = var5;
         this.p = var6;
      }

      void z(zf.g var1) {
         int var2 = var1.l;
         int var3 = var1.N;
         int var4 = var1.O;

         for (int[] var8 : zf.M) {
            int var9 = var2 + var8[0];
            int var10 = var4 + var8[1];
            this.G(var1, var9, var3, var10);
            this.P(var1, var9, var10);
            this.U(var1, var9, var10);
         }

         for (int[] var14 : zf.o) {
            this.b(var1, var2 + var14[0], var4 + var14[1], var14);
         }

         this.P(var1);
         this.r(var1);
      }

      private boolean H(int var1, int var2, int var3) {
         return this.x.floor(var1, var2 - 1, var3) || this.x.swim(var1, var2, var3);
      }

      private void r(zf.g var1) {
         int var2 = var1.l;
         int var3 = var1.N;
         int var4 = var1.O;
         if (this.x.swim(var2, var3, var4) && this.l(var2, var3 + 1, var4) && this.x.swim(var2, var3 + 1, var4) && this.x.open(var2, var3 + 2, var4)) {
            this.f(var1, var2, var3 + 1, var4, zf.b.B, 1.5, List.of());
         }
      }

      private void G(zf.g var1, int var2, int var3, int var4) {
         if (this.l(var2, var3, var4) && this.H(var2, var3, var4)) {
            ArrayList var5 = new ArrayList(2);
            double var6 = 1.0;
            double var8 = this.H(var2, var3 + 1, var4, var5);
            if (!(var8 < 0.0)) {
               double var10 = this.H(var2, var3, var4, var5);
               if (!(var10 < 0.0)) {
                  this.f(var1, var2, var3, var4, zf.b.t, var6 + var8 + var10, var5);
               }
            }
         }
      }

      private void P(zf.g var1, int var2, int var3) {
         int var4 = var1.N;
         if (this.l(var2, var4 + 1, var3) && this.l(var1.l, var4 + 2, var1.O) && this.x.floor(var2, var4, var3)) {
            ArrayList var5 = new ArrayList(3);
            double var6 = this.H(var1.l, var4 + 2, var1.O, var5);
            if (!(var6 < 0.0)) {
               double var8 = this.H(var2, var4 + 2, var3, var5);
               if (!(var8 < 0.0)) {
                  double var10 = this.H(var2, var4 + 1, var3, var5);
                  if (!(var10 < 0.0)) {
                     this.f(var1, var2, var4 + 1, var3, zf.b.v, 2.0 + var6 + var8 + var10, var5);
                  }
               }
            }
         }
      }

      private void U(zf.g var1, int var2, int var3) {
         int var4 = var1.N;
         if (this.l(var2, var4, var3) && this.x.open(var2, var4, var3) && this.x.open(var2, var4 + 1, var3) && this.x.open(var2, var4 - 1, var3)) {
            for (int var5 = 1; var5 <= this.P.maxDrop(); var5++) {
               int var6 = var4 - var5;
               if (!this.l(var2, var6, var3) || !this.x.open(var2, var6, var3)) {
                  return;
               }

               if (this.H(var2, var6, var3)) {
                  this.f(var1, var2, var6, var3, zf.b.S, 1.0 + 0.5 * var5, List.of());
                  return;
               }
            }
         }
      }

      private void b(zf.g var1, int var2, int var3, int[] var4) {
         int var5 = var1.N;
         if (this.l(var2, var5, var3) && this.H(var2, var5, var3) && this.x.open(var2, var5, var3) && this.x.open(var2, var5 + 1, var3)) {
            if (this.x.open(var1.l + var4[0], var5, var1.O)
               && this.x.open(var1.l + var4[0], var5 + 1, var1.O)
               && this.x.open(var1.l, var5, var1.O + var4[1])
               && this.x.open(var1.l, var5 + 1, var1.O + var4[1])) {
               this.f(var1, var2, var5, var3, zf.b.p, zf.z, List.of());
            }
         }
      }

      private void P(zf.g var1) {
         if (this.P.dig()) {
            int var2 = var1.l;
            int var3 = var1.N - 1;
            int var4 = var1.O;
            if (this.l(var2, var3, var4) && !this.x.open(var2, var3, var4) && this.x.floor(var2, var3 - 1, var4)) {
               double var5 = this.x.digCost(var2, var3, var4);
               if (!(var5 < 0.0)) {
                  this.f(var1, var2, var3, var4, zf.b.C, 1.5 + var5 * this.P.digWeight(), List.of(new zf.a(var2, var3, var4)));
               }
            }
         }
      }

      private double H(int var1, int var2, int var3, List<zf.a> var4) {
         if (!this.l(var1, var2, var3)) {
            return -1.0;
         }

         if (this.x.open(var1, var2, var3)) {
            return 0.0;
         }

         if (!this.P.dig()) {
            return -1.0;
         }

         double var5 = this.x.digCost(var1, var2, var3);
         if (var5 < 0.0) {
            return -1.0;
         }

         var4.add(new zf.a(var1, var2, var3));
         return var5 * this.P.digWeight();
      }

      private boolean l(int var1, int var2, int var3) {
         return this.n.contains(var1, var2, var3) && this.x.loaded(var1, var2, var3);
      }

      private void f(zf.g var1, int var2, int var3, int var4, zf.b var5, double var6, List<zf.a> var8) {
         long var9 = zf.O(var2, var3, var4);
         double var11 = var1.E + var6 + this.x.penalty(var2, var3, var4);
         zf.g var13 = this.j.get(var9);
         if (var13 == null) {
            var13 = new zf.g(var9, var2, var3, var4);
            var13.k = this.E.estimate(var2, var3, var4);
            this.j.put(var9, var13);
         } else if (var13.t || var11 >= var13.E - 1.0E-9) {
            return;
         }

         var13.E = var11;
         var13.H = var11 + var13.k;
         var13.w = var1;
         var13.Z = var5;
         var13.T = var8.isEmpty() ? List.of() : List.copyOf(var8);
         this.p.add(var13);
      }
   }

   public interface f {
      boolean reached(int var1, int var2, int var3);

      double estimate(int var1, int var2, int var3);

      static zf.f a(final int var0, final int var1, final int var2) {
         return new zf.f() {
            @Override
            public boolean reached(int var1x, int var2x, int var3) {
               return var1x == var0 && var2x == var1 && var3 == var2;
            }

            @Override
            public double estimate(int var1x, int var2x, int var3) {
               return zf.i(var1x - var0, var2x - var1, var3 - var2);
            }
         };
      }

      static zf.f j(final double var0, final double var2, final double var4, final double var6, final double var8) {
         return new zf.f() {
            @Override
            public boolean reached(int var1, int var2x, int var3) {
               return Math.hypot(var1 + 0.5 - var0, var3 + 0.5 - var4) <= var6 && Math.abs(var2x - var2) <= var8;
            }

            @Override
            public double estimate(int var1, int var2x, int var3) {
               return Math.max(0.0, Math.hypot(var1 + 0.5 - var0, var3 + 0.5 - var4) - var6) + Math.max(0.0, Math.abs(var2x - var2) - var8);
            }
         };
      }

      static zf.f N(final List<double[]> var0, final double var1) {
         return new zf.f() {
            private double m(int var1x, int var2) {
               double var3 = Double.MAX_VALUE;

               for (double[] var6 : var0) {
                  var3 = Math.min(var3, Math.hypot(var1x + 0.5 - var6[0], var2 + 0.5 - var6[1]));
               }

               return var3;
            }

            @Override
            public boolean reached(int var1x, int var2, int var3) {
               return this.m(var1x, var3) >= var1;
            }

            @Override
            public double estimate(int var1x, int var2, int var3) {
               return Math.max(0.0, var1 - this.m(var1x, var3));
            }
         };
      }

      static zf.f k(final Set<Long> var0, final int var1, final int var2, final int var3, final double var4) {
         return new zf.f() {
            @Override
            public boolean reached(int var1x, int var2x, int var3x) {
               return var0.contains(zf.O(var1x, var2x, var3x));
            }

            @Override
            public double estimate(int var1x, int var2x, int var3x) {
               return Math.max(0.0, zf.i(var1x - var1, var2x - var2, var3x - var3) - var4);
            }
         };
      }
   }

   private static final class g implements Comparable<zf.g> {
      final long b;
      final int l;
      final int N;
      final int O;
      double E;
      double H;
      double k;
      zf.g w;
      zf.b Z;
      List<zf.a> T;
      boolean t;

      g(long var1, int var3, int var4, int var5) {
         this.b = var1;
         this.l = var3;
         this.N = var4;
         this.O = var5;
      }

      public int v(zf.g var1) {
         int var2 = Double.compare(this.H, var1.H);
         return var2 != 0 ? var2 : Double.compare(this.k, var1.k);
      }
   }

   public record h(int x, int y, int z, zf.b move, List<zf.a> digs) {
      public boolean digging() {
         return !this.digs.isEmpty();
      }
   }

   public interface q {
      boolean loaded(int var1, int var2, int var3);

      boolean open(int var1, int var2, int var3);

      boolean floor(int var1, int var2, int var3);

      double digCost(int var1, int var2, int var3);

      default double penalty(int var1, int var2, int var3) {
         return 0.0;
      }

      default boolean swim(int var1, int var2, int var3) {
         return false;
      }
   }

   public record s(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
      public boolean contains(int var1, int var2, int var3) {
         return var1 >= this.minX && var1 <= this.maxX && var2 >= this.minY && var2 <= this.maxY && var3 >= this.minZ && var3 <= this.maxZ;
      }

      public static zf.s around(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
         return new zf.s(
            Math.min(var0, var3) - var6,
            Math.min(var1, var4) - var7,
            Math.min(var2, var5) - var6,
            Math.max(var0, var3) + var6,
            Math.max(var1, var4) + var8,
            Math.max(var2, var5) + var6
         );
      }
   }

   public record v(List<zf.h> steps, boolean complete, int explored, double cost) {
      public boolean found() {
         return this.steps != null;
      }

      public int digs() {
         if (this.steps == null) {
            return 0;
         }

         int var1 = 0;

         for (zf.h var3 : this.steps) {
            var1 += var3.digs().size();
         }

         return var1;
      }
   }

   public record z(int maxNodes, boolean dig, int maxDrop, boolean partial, double digWeight) {
      public static zf.z walking(int var0) {
         return new zf.z(var0, false, 3, false, 1.0);
      }

      public static zf.z digging(int var0) {
         return new zf.z(var0, true, 3, false, 1.0);
      }

      public zf.z withPartial(boolean var1) {
         return new zf.z(this.maxNodes, this.dig, this.maxDrop, var1, this.digWeight);
      }
   }
}
