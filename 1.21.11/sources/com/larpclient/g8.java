package com.larpclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public final class g8 {
   static final double h = 2.0;
   static final double g = Math.sqrt(2.0);
   static final double o = 1.5;
   static final double Y = 0.5;
   private static final int[][] O = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
   private static final int[][] V = new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

   public static g8.t O(g8.s var0, int var1, int var2, int var3, g8.i var4, g8.l var5, g8.b var6) {
      if (var4.reached(var1, var2, var3)) {
         return new g8.t(List.of(), true, 0, 0.0);
      }

      HashMap var7 = new HashMap();
      PriorityQueue var8 = new PriorityQueue();
      g8.k var9 = new g8.k(M(var1, var2, var3), var1, var2, var3);
      var9.w = var4.estimate(var1, var2, var3);
      var9.K = var9.w;
      var7.put(var9.f, var9);
      var8.add(var9);
      g8.k var10 = var9;
      int var11 = 0;
      g8.a var12 = new g8.a(var0, var4, var5, var6, var7, var8);

      while (!var8.isEmpty()) {
         g8.k var13 = (g8.k)var8.poll();
         if (!var13.a) {
            var13.a = (boolean)1;
            if (var4.reached(var13.O, var13.i, var13.u)) {
               return new g8.t(P(var13), true, var11, var13.v);
            }

            if (var13.w < var10.w || var13.w == var10.w && var13.v < var10.v) {
               var10 = var13;
            }

            if (++var11 > var6.maxNodes()) {
               break;
            }

            var12.G(var13);
         }
      }

      return var6.partial() && var10 != var9 ? new g8.t(P(var10), false, var11, var10.v) : new g8.t(null, false, var11, 0.0);
   }

   private static List<g8.p> P(g8.k var0) {
      ArrayList var1 = new ArrayList();

      for (g8.k var2 = var0; var2.q != null; var2 = var2.q) {
         var1.add(new g8.p(var2.O, var2.i, var2.u, var2.x, var2.l));
      }

      Collections.reverse(var1);
      return var1;
   }

   static double D(int var0, int var1, int var2) {
      int var3 = Math.abs(var0);
      int var4 = Math.abs(var2);
      return Math.max(var3, var4) + (g - 1.0) * Math.min(var3, var4) + Math.abs(var1) * 0.5;
   }

   public static long M(int var0, int var1, int var2) {
      return (long)(var0 & 67108863) << 38 | (long)(var2 & 67108863) << 12 | var1 & 4095;
   }

   public static int x(long var0) {
      return (int)(var0 >> 38);
   }

   public static int T(long var0) {
      return (int)(var0 << 52 >> 52);
   }

   public static int G(long var0) {
      return (int)(var0 << 26 >> 38);
   }

   private g8() {
   }

   private static final class a {
      final g8.s q;
      final g8.i O;
      final g8.l y;
      final g8.b E;
      final Map<Long, g8.k> l;
      final PriorityQueue<g8.k> h;

      a(g8.s var1, g8.i var2, g8.l var3, g8.b var4, Map<Long, g8.k> var5, PriorityQueue<g8.k> var6) {
         this.q = var1;
         this.O = var2;
         this.y = var3;
         this.E = var4;
         this.l = var5;
         this.h = var6;
      }

      void G(g8.k var1) {
         int var2 = var1.O;
         int var3 = var1.i;
         int var4 = var1.u;

         for (int[] var8 : g8.O) {
            int var9 = var2 + var8[0];
            int var10 = var4 + var8[1];
            this.k(var1, var9, var3, var10);
            this.S(var1, var9, var10);
            this.B(var1, var9, var10);
         }

         for (int[] var14 : g8.V) {
            this.X(var1, var2 + var14[0], var4 + var14[1], var14);
         }

         this.m(var1);
         this.H(var1);
      }

      private boolean U(int var1, int var2, int var3) {
         return this.q.floor(var1, var2 - 1, var3) || this.q.swim(var1, var2, var3);
      }

      private void H(g8.k var1) {
         int var2 = var1.O;
         int var3 = var1.i;
         int var4 = var1.u;
         if (this.q.swim(var2, var3, var4) && this.P(var2, var3 + 1, var4) && this.q.swim(var2, var3 + 1, var4) && this.q.open(var2, var3 + 2, var4)) {
            this.s(var1, var2, var3 + 1, var4, g8.c.J, 1.5, List.of());
         }
      }

      private void k(g8.k var1, int var2, int var3, int var4) {
         if (this.P(var2, var3, var4) && this.U(var2, var3, var4)) {
            ArrayList var5 = new ArrayList(2);
            double var6 = 1.0;
            double var8 = this.A(var2, var3 + 1, var4, var5);
            if (!(var8 < 0.0)) {
               double var10 = this.A(var2, var3, var4, var5);
               if (!(var10 < 0.0)) {
                  this.s(var1, var2, var3, var4, g8.c.S, var6 + var8 + var10, var5);
               }
            }
         }
      }

      private void S(g8.k var1, int var2, int var3) {
         int var4 = var1.i;
         if (this.P(var2, var4 + 1, var3) && this.P(var1.O, var4 + 2, var1.u) && this.q.floor(var2, var4, var3)) {
            ArrayList var5 = new ArrayList(3);
            double var6 = this.A(var1.O, var4 + 2, var1.u, var5);
            if (!(var6 < 0.0)) {
               double var8 = this.A(var2, var4 + 2, var3, var5);
               if (!(var8 < 0.0)) {
                  double var10 = this.A(var2, var4 + 1, var3, var5);
                  if (!(var10 < 0.0)) {
                     this.s(var1, var2, var4 + 1, var3, g8.c.b, 2.0 + var6 + var8 + var10, var5);
                  }
               }
            }
         }
      }

      private void B(g8.k var1, int var2, int var3) {
         int var4 = var1.i;
         if (this.P(var2, var4, var3) && this.q.open(var2, var4, var3) && this.q.open(var2, var4 + 1, var3) && this.q.open(var2, var4 - 1, var3)) {
            for (int var5 = 1; var5 <= this.E.maxDrop(); var5++) {
               int var6 = var4 - var5;
               if (!this.P(var2, var6, var3) || !this.q.open(var2, var6, var3)) {
                  return;
               }

               if (this.U(var2, var6, var3)) {
                  this.s(var1, var2, var6, var3, g8.c.C, 1.0 + 0.5 * var5, List.of());
                  return;
               }
            }
         }
      }

      private void X(g8.k var1, int var2, int var3, int[] var4) {
         int var5 = var1.i;
         if (this.P(var2, var5, var3) && this.U(var2, var5, var3) && this.q.open(var2, var5, var3) && this.q.open(var2, var5 + 1, var3)) {
            if (this.q.open(var1.O + var4[0], var5, var1.u)
               && this.q.open(var1.O + var4[0], var5 + 1, var1.u)
               && this.q.open(var1.O, var5, var1.u + var4[1])
               && this.q.open(var1.O, var5 + 1, var1.u + var4[1])) {
               this.s(var1, var2, var5, var3, g8.c.Q, g8.g, List.of());
            }
         }
      }

      private void m(g8.k var1) {
         if (this.E.dig()) {
            int var2 = var1.O;
            int var3 = var1.i - 1;
            int var4 = var1.u;
            if (this.P(var2, var3, var4) && !this.q.open(var2, var3, var4) && this.q.floor(var2, var3 - 1, var4)) {
               double var5 = this.q.digCost(var2, var3, var4);
               if (!(var5 < 0.0)) {
                  this.s(var1, var2, var3, var4, g8.c.d, 1.5 + var5 * this.E.digWeight(), List.of(new g8.v(var2, var3, var4)));
               }
            }
         }
      }

      private double A(int var1, int var2, int var3, List<g8.v> var4) {
         if (!this.P(var1, var2, var3)) {
            return -1.0;
         }

         if (this.q.open(var1, var2, var3)) {
            return 0.0;
         }

         if (!this.E.dig()) {
            return -1.0;
         }

         double var5 = this.q.digCost(var1, var2, var3);
         if (var5 < 0.0) {
            return -1.0;
         }

         var4.add(new g8.v(var1, var2, var3));
         return var5 * this.E.digWeight();
      }

      private boolean P(int var1, int var2, int var3) {
         return this.y.contains(var1, var2, var3) && this.q.loaded(var1, var2, var3);
      }

      private void s(g8.k var1, int var2, int var3, int var4, g8.c var5, double var6, List<g8.v> var8) {
         long var9 = g8.M(var2, var3, var4);
         double var11 = var1.v + var6 + this.q.penalty(var2, var3, var4);
         g8.k var13 = this.l.get(var9);
         if (var13 == null) {
            var13 = new g8.k(var9, var2, var3, var4);
            var13.w = this.O.estimate(var2, var3, var4);
            this.l.put(var9, var13);
         } else if (var13.a || var11 >= var13.v - 1.0E-9) {
            return;
         }

         var13.v = var11;
         var13.K = var11 + var13.w;
         var13.q = var1;
         var13.x = var5;
         var13.l = var8.isEmpty() ? List.of() : List.copyOf(var8);
         this.h.add(var13);
      }
   }

   public record b(int maxNodes, boolean dig, int maxDrop, boolean partial, double digWeight) {
      public static g8.b walking(int var0) {
         return new g8.b(var0, false, 3, false, 1.0);
      }

      public static g8.b digging(int var0) {
         return new g8.b(var0, true, 3, false, 1.0);
      }

      public g8.b withPartial(boolean var1) {
         return new g8.b(this.maxNodes, this.dig, this.maxDrop, var1, this.digWeight);
      }
   }

   public enum c {
      S,
      Q,
      b,
      C,
      d,
      J;
   }

   public interface i {
      boolean reached(int var1, int var2, int var3);

      double estimate(int var1, int var2, int var3);

      static g8.i e(final int var0, final int var1, final int var2) {
         return new g8.i() {
            @Override
            public boolean reached(int var1x, int var2x, int var3) {
               return var1x == var0 && var2x == var1 && var3 == var2;
            }

            @Override
            public double estimate(int var1x, int var2x, int var3) {
               return g8.D(var1x - var0, var2x - var1, var3 - var2);
            }
         };
      }

      static g8.i T(final double var0, final double var2, final double var4, final double var6, final double var8) {
         return new g8.i() {
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

      static g8.i g(final List<double[]> var0, final double var1) {
         return new g8.i() {
            private double g(int var1x, int var2) {
               double var3 = Double.MAX_VALUE;

               for (double[] var6 : var0) {
                  var3 = Math.min(var3, Math.hypot(var1x + 0.5 - var6[0], var2 + 0.5 - var6[1]));
               }

               return var3;
            }

            @Override
            public boolean reached(int var1x, int var2, int var3) {
               return this.g(var1x, var3) >= var1;
            }

            @Override
            public double estimate(int var1x, int var2, int var3) {
               return Math.max(0.0, var1 - this.g(var1x, var3));
            }
         };
      }

      static g8.i S(final Set<Long> var0, final int var1, final int var2, final int var3, final double var4) {
         return new g8.i() {
            @Override
            public boolean reached(int var1x, int var2x, int var3x) {
               return var0.contains(g8.M(var1x, var2x, var3x));
            }

            @Override
            public double estimate(int var1x, int var2x, int var3x) {
               return Math.max(0.0, g8.D(var1x - var1, var2x - var2, var3x - var3) - var4);
            }
         };
      }
   }

   private static final class k implements Comparable<g8.k> {
      final long f;
      final int O;
      final int i;
      final int u;
      double v;
      double K;
      double w;
      g8.k q;
      g8.c x;
      List<g8.v> l;
      boolean a;

      k(long var1, int var3, int var4, int var5) {
         this.f = var1;
         this.O = var3;
         this.i = var4;
         this.u = var5;
      }

      public int z(g8.k var1) {
         int var2 = Double.compare(this.K, var1.K);
         return var2 != 0 ? var2 : Double.compare(this.w, var1.w);
      }
   }

   public record l(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
      public boolean contains(int var1, int var2, int var3) {
         return var1 >= this.minX && var1 <= this.maxX && var2 >= this.minY && var2 <= this.maxY && var3 >= this.minZ && var3 <= this.maxZ;
      }

      public static g8.l around(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
         return new g8.l(
            Math.min(var0, var3) - var6,
            Math.min(var1, var4) - var7,
            Math.min(var2, var5) - var6,
            Math.max(var0, var3) + var6,
            Math.max(var1, var4) + var8,
            Math.max(var2, var5) + var6
         );
      }
   }

   public record p(int x, int y, int z, g8.c move, List<g8.v> digs) {
      public boolean digging() {
         return !this.digs.isEmpty();
      }
   }

   public interface s {
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

   public record t(List<g8.p> steps, boolean complete, int explored, double cost) {
      public boolean found() {
         return this.steps != null;
      }

      public int digs() {
         if (this.steps == null) {
            return 0;
         }

         int var1 = 0;

         for (g8.p var3 : this.steps) {
            var1 += var3.digs().size();
         }

         return var1;
      }
   }

   public record v(int x, int y, int z) {
   }
}
