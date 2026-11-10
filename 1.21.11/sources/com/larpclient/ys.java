package com.larpclient;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class ys {
   static final long v = 12000L;
   static final long E = 15000L;
   static final long g = 3000L;
   static final long N = 60000L;
   static final int K = 4;
   static final int T = 5;
   static final long[] D = new long[]{10000L, 20000L, 40000L, 60000L};
   static final long b = 10000L;
   static final long j = 1500L;
   static final long l = 3000L;
   static final long e = 600L;
   private static final long y = -2305843009213693952L;
   private final ArrayDeque<Long> u = new ArrayDeque<>();
   private boolean Q;
   private long d = -2305843009213693952L;
   private long P = -2305843009213693952L;
   private long H = -2305843009213693952L;
   private long p = -2305843009213693952L;
   private long Z = -1L;
   private int B;
   private int Y;
   public static final List<String> q = List.of("Random", "Asia", "East", "EU Central", "EU West", "Oceania", "West");
   static final List<String> C = List.of("asia", "east", "eu central", "eu west", "oceania", "west");
   static final long i = 15000L;
   public static final double t = 5.0;
   public static final float F = 8.0F;
   public static final float f = 10.0F;

   public void P() {
      this.u.clear();
      this.Q = (boolean)0;
      this.d = this.P = this.H = this.p = -2305843009213693952L;
      this.Z = -1L;
      this.B = this.Y = 0;
   }

   public boolean c() {
      return this.Q;
   }

   public long w(long var1) {
      this.O(var1);
      long var3 = Math.max(this.H, this.P + 3000L);
      if (this.u.size() >= 4) {
         var3 = Math.max(var3, this.u.peekFirst() + 60000L);
      }

      return var3;
   }

   public boolean D(long var1) {
      return !this.Q && var1 >= this.w(var1);
   }

   public void M(long var1) {
      this.O(var1);
      this.u.addLast(var1);
      this.P = var1;
      this.Q = (boolean)1;
      this.d = var1 + 12000L;
   }

   public long n(long var1) {
      return this.Q ? Math.max(0L, this.d - var1) : Math.max(0L, this.w(var1) - var1);
   }

   public boolean B(long var1) {
      return this.Q && var1 >= this.d;
   }

   public void D(long var1) {
      if (this.Q) {
         this.d = Math.max(this.d, var1 + 15000L);
      }

      this.Y = 0;
   }

   public boolean r(long var1) {
      this.Q = (boolean)0;
      this.Y++;
      this.H = Math.max(this.H, var1 + this.o());
      return this.Y >= 5;
   }

   public void j(long var1, long var3) {
      this.Q = (boolean)0;
      this.Y = 0;
      if (var3 < 0L) {
         this.H = Math.max(this.H, var1 + this.o());
      } else {
         this.B = 0;
         this.H = Math.max(var1 + 1L, var1 + var3 + 600L);
         if (this.p != -2305843009213693952L) {
            this.Z = Math.clamp(var1 - this.p + var3, 5000, 600000);
         }
      }
   }

   public void m(long var1, long var3) {
      this.Q = (boolean)0;
      this.H = Math.max(this.H, var1 + var3);
   }

   public void U(long var1) {
      this.Q = (boolean)0;
      this.Y = this.B = 0;
      this.p = var1;
      this.H = this.Z > 0L ? var1 + this.Z + 600L : -2305843009213693952L;
   }

   public long t() {
      return this.Z;
   }

   private long o() {
      return D[Math.min(this.B++, D.length - 1)];
   }

   private void O(long var1) {
      while (!this.u.isEmpty() && var1 - this.u.peekFirst() >= 60000L) {
         this.u.removeFirst();
      }
   }

   public static ys.k S(boolean var0, boolean var1, boolean var2, boolean var3, double var4, long var6, boolean var8, long var9) {
      if (!var0) {
         return ys.k.A;
      }

      if (var2) {
         return var3 && var4 >= 12.0 ? ys.k.x : ys.k.I;
      }

      if (var1) {
         return ys.k.P;
      }

      int var11 = var9 >= 0L && var9 < 15000L ? 1 : 0;
      return var8 && var4 <= 5.0 && var6 > 3000L && var11 == 0 ? ys.k.y : ys.k.I;
   }

   public static boolean y(boolean var0, boolean var1, double var2, double var4, float var6) {
      return !var0 && !var1 && var6 > 8.0F && var2 <= var4;
   }

   public static boolean F(boolean var0, boolean var1, float var2) {
      return !var0 && !var1 && var2 > 8.0F;
   }

   public static boolean t(double var0, boolean var2, boolean var3, float var4, boolean var5) {
      int var6 = var4 <= 10.0F ? 1 : 0;
      if (var5) {
         return var0 <= (var2 ? 16 : (var6 != 0 ? 12 : 10));
      } else if (!var3 && var0 > 3.0) {
         return false;
      } else {
         return var2 ? var0 <= 7.0 : var0 <= 5.0 || var6 != 0 && var0 <= 8.0;
      }
   }

   public static double[] q(double var0, double var2, List<double[]> var4, double var5) {
      double var7 = 0.0;
      double var9 = 0.0;

      for (double[] var12 : var4) {
         double var13 = var0 - var12[0];
         double var15 = var2 - var12[1];
         double var17 = Math.hypot(var13, var15);
         if (var17 < 0.001) {
            var13 = 1.0;
            var15 = 0.0;
            var17 = 1.0;
         }

         var7 += var13 / var17 / Math.max(1.0, var17);
         var9 += var15 / var17 / Math.max(1.0, var17);
      }

      double var19 = Math.hypot(var7, var9);
      return !var4.isEmpty() && !(var19 < 1.0E-9) ? new double[]{var0 + var7 / var19 * var5, var2 + var9 / var19 * var5} : null;
   }

   public enum k {
      A,
      P,
      I,
      x,
      y;
   }

   public static final class v {
      private final Map<String, Integer> y = new HashMap<>();
      private int m;

      public void g() {
         this.y.clear();
         this.m = 0;
      }

      public String n(String var1) {
         String var2 = var1 == null ? "" : var1.trim().toLowerCase(Locale.ROOT);
         if (ys.C.contains(var2)) {
            return var2;
         }

         for (int var3 = 0; var3 < ys.C.size(); var3++) {
            String var4 = ys.C.get(Math.floorMod(this.m + var3, ys.C.size()));
            if (this.y.getOrDefault(var4, 0) < 2) {
               this.m = Math.floorMod(this.m + var3 + 1, ys.C.size());
               return var4;
            }
         }

         this.y.clear();
         String var5 = ys.C.get(Math.floorMod(this.m, ys.C.size()));
         this.m = Math.floorMod(this.m + 1, ys.C.size());
         return var5;
      }

      public void D(String var1) {
         if (var1 != null && ys.C.contains(var1)) {
            this.y.merge(var1, 1, Integer::sum);
         }
      }

      public void R(String var1) {
         if (var1 != null) {
            this.y.remove(var1);
         }
      }
   }
}
