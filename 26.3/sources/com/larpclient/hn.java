package com.larpclient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public final class hn {
   static final double J = 2.0;
   static final double V = Math.sqrt(2.0);
   static final double a = 1.5;
   static final double U = 0.5;
   private static final int[][] s = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
   private static final int[][] i = new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

   public static hn.l a(hn.s var0, int var1, int var2, int var3, hn.c var4, hn.j var5, hn.m var6) {
      if (var4.reached(var1, var2, var3)) {
         return new hn.l(List.of(), true, 0, 0.0);
      }

      HashMap var7 = new HashMap();
      PriorityQueue var8 = new PriorityQueue();
      hn.q var9 = new hn.q(f(var1, var2, var3), var1, var2, var3);
      var9.d = var4.estimate(var1, var2, var3);
      var9.b = var9.d;
      var7.put(var9.S, var9);
      var8.add(var9);
      hn.q var10 = var9;
      int var11 = 0;
      hn.h var12 = new hn.h(var0, var4, var5, var6, var7, var8);

      while (!var8.isEmpty()) {
         hn.q var13 = (hn.q)var8.poll();
         if (!var13.C) {
            var13.C = (boolean)1;
            if (var4.reached(var13.X, var13.A, var13.G)) {
               return new hn.l(L(var13), true, var11, var13.e);
            }

            if (var13.d < var10.d || var13.d == var10.d && var13.e < var10.e) {
               var10 = var13;
            }

            if (++var11 > var6.maxNodes()) {
               break;
            }

            var12.N(var13);
         }
      }

      return var6.partial() && var10 != var9 ? new hn.l(L(var10), false, var11, var10.e) : new hn.l(null, false, var11, 0.0);
   }

   private static List<hn.g> L(hn.q var0) {
      ArrayList var1 = new ArrayList();

      for (hn.q var2 = var0; var2.u != null; var2 = var2.u) {
         var1.add(new hn.g(var2.X, var2.A, var2.G, var2.M, var2.D));
      }

      Collections.reverse(var1);
      return var1;
   }

   static double R(int var0, int var1, int var2) {
      int var3 = Math.abs(var0);
      int var4 = Math.abs(var2);
      return Math.max(var3, var4) + (V - 1.0) * Math.min(var3, var4) + Math.abs(var1) * 0.5;
   }

   public static long f(int var0, int var1, int var2) {
      return (long)(var0 & 67108863) << 38 | (long)(var2 & 67108863) << 12 | var1 & 4095;
   }

   public static int k(long var0) {
      return (int)(var0 >> 38);
   }

   public static int V(long var0) {
      return (int)(var0 << 52 >> 52);
   }

   public static int m(long var0) {
      return (int)(var0 << 26 >> 38);
   }

   private hn() {
   }

   public interface c {
      boolean reached(int var1, int var2, int var3);

      double estimate(int var1, int var2, int var3);

      static hn.c f(final int var0, final int var1, final int var2) {
         return new hn.c() {
            @Override
            public boolean reached(int var1x, int var2x, int var3) {
               return var1x == var0 && var2x == var1 && var3 == var2;
            }

            @Override
            public double estimate(int var1x, int var2x, int var3) {
               return hn.R(var1x - var0, var2x - var1, var3 - var2);
            }
         };
      }

      static hn.c a(final double var0, final double var2, final double var4, final double var6, final double var8) {
         return new hn.c() {
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

      static hn.c Z(final List<double[]> var0, final double var1) {
         return new hn.c() {
            private double f(int var1x, int var2) {
               double var3 = Double.MAX_VALUE;

               for (double[] var6 : var0) {
                  var3 = Math.min(var3, Math.hypot(var1x + 0.5 - var6[0], var2 + 0.5 - var6[1]));
               }

               return var3;
            }

            @Override
            public boolean reached(int var1x, int var2, int var3) {
               return this.f(var1x, var3) >= var1;
            }

            @Override
            public double estimate(int var1x, int var2, int var3) {
               return Math.max(0.0, var1 - this.f(var1x, var3));
            }
         };
      }

      static hn.c Q(final Set<Long> var0, final int var1, final int var2, final int var3, final double var4) {
         return new hn.c() {
            @Override
            public boolean reached(int var1x, int var2x, int var3x) {
               return var0.contains(hn.f(var1x, var2x, var3x));
            }

            @Override
            public double estimate(int var1x, int var2x, int var3x) {
               return Math.max(0.0, hn.R(var1x - var1, var2x - var2, var3x - var3) - var4);
            }
         };
      }
   }

   public record g(int x, int y, int z, hn.y move, List<hn.n> digs) {
      public boolean digging() {
         return !this.digs.isEmpty();
      }
   }

   private static final class h {
      final hn.s S;
      final hn.c T;
      final hn.j E;
      final hn.m P;
      final Map<Long, hn.q> a;
      final PriorityQueue<hn.q> X;

      h(hn.s var1, hn.c var2, hn.j var3, hn.m var4, Map<Long, hn.q> var5, PriorityQueue<hn.q> var6) {
         this.S = var1;
         this.T = var2;
         this.E = var3;
         this.P = var4;
         this.a = var5;
         this.X = var6;
      }

      void N(hn.q var1) {
         int var2 = var1.X;
         int var3 = var1.A;
         int var4 = var1.G;

         for (int[] var8 : hn.s) {
            int var9 = var2 + var8[0];
            int var10 = var4 + var8[1];
            this.b(var1, var9, var3, var10);
            this.C(var1, var9, var10);
            this.y(var1, var9, var10);
         }

         for (int[] var14 : hn.i) {
            this.h(var1, var2 + var14[0], var4 + var14[1], var14);
         }

         this.W(var1);
         this.d(var1);
      }

      private boolean E(int var1, int var2, int var3) {
         return this.S.floor(var1, var2 - 1, var3) || this.S.swim(var1, var2, var3);
      }

      private void d(hn.q var1) {
         int var2 = var1.X;
         int var3 = var1.A;
         int var4 = var1.G;
         if (this.S.swim(var2, var3, var4) && this.k(var2, var3 + 1, var4) && this.S.swim(var2, var3 + 1, var4) && this.S.open(var2, var3 + 2, var4)) {
            this.W(var1, var2, var3 + 1, var4, hn.y.s, 1.5, List.of());
         }
      }

      private void b(hn.q var1, int var2, int var3, int var4) {
         if (this.k(var2, var3, var4) && this.E(var2, var3, var4)) {
            ArrayList var5 = new ArrayList(2);
            double var6 = 1.0;
            double var8 = this.d(var2, var3 + 1, var4, var5);
            if (!(var8 < 0.0)) {
               double var10 = this.d(var2, var3, var4, var5);
               if (!(var10 < 0.0)) {
                  this.W(var1, var2, var3, var4, hn.y.R, var6 + var8 + var10, var5);
               }
            }
         }
      }

      private void C(hn.q var1, int var2, int var3) {
         int var4 = var1.A;
         if (this.k(var2, var4 + 1, var3) && this.k(var1.X, var4 + 2, var1.G) && this.S.floor(var2, var4, var3)) {
            ArrayList var5 = new ArrayList(3);
            double var6 = this.d(var1.X, var4 + 2, var1.G, var5);
            if (!(var6 < 0.0)) {
               double var8 = this.d(var2, var4 + 2, var3, var5);
               if (!(var8 < 0.0)) {
                  double var10 = this.d(var2, var4 + 1, var3, var5);
                  if (!(var10 < 0.0)) {
                     this.W(var1, var2, var4 + 1, var3, hn.y.a, 2.0 + var6 + var8 + var10, var5);
                  }
               }
            }
         }
      }

      private void y(hn.q var1, int var2, int var3) {
         int var4 = var1.A;
         if (this.k(var2, var4, var3) && this.S.open(var2, var4, var3) && this.S.open(var2, var4 + 1, var3) && this.S.open(var2, var4 - 1, var3)) {
            for (int var5 = 1; var5 <= this.P.maxDrop(); var5++) {
               int var6 = var4 - var5;
               if (!this.k(var2, var6, var3) || !this.S.open(var2, var6, var3)) {
                  return;
               }

               if (this.E(var2, var6, var3)) {
                  this.W(var1, var2, var6, var3, hn.y.h, 1.0 + 0.5 * var5, List.of());
                  return;
               }
            }
         }
      }

      private void h(hn.q var1, int var2, int var3, int[] var4) {
         int var5 = var1.A;
         if (this.k(var2, var5, var3) && this.E(var2, var5, var3) && this.S.open(var2, var5, var3) && this.S.open(var2, var5 + 1, var3)) {
            if (this.S.open(var1.X + var4[0], var5, var1.G)
               && this.S.open(var1.X + var4[0], var5 + 1, var1.G)
               && this.S.open(var1.X, var5, var1.G + var4[1])
               && this.S.open(var1.X, var5 + 1, var1.G + var4[1])) {
               this.W(var1, var2, var5, var3, hn.y.L, hn.V, List.of());
            }
         }
      }

      private void W(hn.q var1) {
         if (this.P.dig()) {
            int var2 = var1.X;
            int var3 = var1.A - 1;
            int var4 = var1.G;
            if (this.k(var2, var3, var4) && !this.S.open(var2, var3, var4) && this.S.floor(var2, var3 - 1, var4)) {
               double var5 = this.S.digCost(var2, var3, var4);
               if (!(var5 < 0.0)) {
                  this.W(var1, var2, var3, var4, hn.y.v, 1.5 + var5 * this.P.digWeight(), List.of(new hn.n(var2, var3, var4)));
               }
            }
         }
      }

      private double d(int var1, int var2, int var3, List<hn.n> var4) {
         if (!this.k(var1, var2, var3)) {
            return -1.0;
         }

         if (this.S.open(var1, var2, var3)) {
            return 0.0;
         }

         if (!this.P.dig()) {
            return -1.0;
         }

         double var5 = this.S.digCost(var1, var2, var3);
         if (var5 < 0.0) {
            return -1.0;
         }

         var4.add(new hn.n(var1, var2, var3));
         return var5 * this.P.digWeight();
      }

      private boolean k(int var1, int var2, int var3) {
         return this.E.contains(var1, var2, var3) && this.S.loaded(var1, var2, var3);
      }

      private void W(hn.q var1, int var2, int var3, int var4, hn.y var5, double var6, List<hn.n> var8) {
         long var9 = hn.f(var2, var3, var4);
         double var11 = var1.e + var6 + this.S.penalty(var2, var3, var4);
         hn.q var13 = this.a.get(var9);
         if (var13 == null) {
            var13 = new hn.q(var9, var2, var3, var4);
            var13.d = this.T.estimate(var2, var3, var4);
            this.a.put(var9, var13);
         } else if (var13.C || var11 >= var13.e - 1.0E-9) {
            return;
         }

         var13.e = var11;
         var13.b = var11 + var13.d;
         var13.u = var1;
         var13.M = var5;
         var13.D = var8.isEmpty() ? List.of() : List.copyOf(var8);
         this.X.add(var13);
      }
   }

   public record j(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
      public boolean contains(int var1, int var2, int var3) {
         return var1 >= this.minX && var1 <= this.maxX && var2 >= this.minY && var2 <= this.maxY && var3 >= this.minZ && var3 <= this.maxZ;
      }

      public static hn.j around(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
         return new hn.j(
            Math.min(var0, var3) - var6,
            Math.min(var1, var4) - var7,
            Math.min(var2, var5) - var6,
            Math.max(var0, var3) + var6,
            Math.max(var1, var4) + var8,
            Math.max(var2, var5) + var6
         );
      }
   }

   public record l(List<hn.g> steps, boolean complete, int explored, double cost) {
      public boolean found() {
         return this.steps != null;
      }

      public int digs() {
         if (this.steps == null) {
            return 0;
         }

         int var1 = 0;

         for (hn.g var3 : this.steps) {
            var1 += var3.digs().size();
         }

         return var1;
      }
   }

   public record m(int maxNodes, boolean dig, int maxDrop, boolean partial, double digWeight) {
      public static hn.m walking(int var0) {
         return new hn.m(var0, false, 3, false, 1.0);
      }

      public static hn.m digging(int var0) {
         return new hn.m(var0, true, 3, false, 1.0);
      }

      public hn.m withPartial(boolean var1) {
         return new hn.m(this.maxNodes, this.dig, this.maxDrop, var1, this.digWeight);
      }
   }

   public record n(int x, int y, int z) {
   }

   private static final class q implements Comparable<hn.q> {
      final long S;
      final int X;
      final int A;
      final int G;
      double e;
      double b;
      double d;
      hn.q u;
      hn.y M;
      List<hn.n> D;
      boolean C;

      q(long var1, int var3, int var4, int var5) {
         this.S = var1;
         this.X = var3;
         this.A = var4;
         this.G = var5;
      }

      public int Q(hn.q var1) {
         int var2 = Double.compare(this.b, var1.b);
         return var2 != 0 ? var2 : Double.compare(this.d, var1.d);
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

   public enum y {
      R,
      L,
      a,
      h,
      v,
      s;
   }
}
