package com.larpclient;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class e {
   static final long J = 12000L;
   static final long h = 15000L;
   static final long a = 3000L;
   static final long I = 60000L;
   static final int C = 4;
   static final int i = 5;
   static final long[] K = new long[]{10000L, 20000L, 40000L, 60000L};
   static final long Y = 10000L;
   static final long d = 1500L;
   static final long l = 3000L;
   static final long S = 600L;
   private static final long m = -2305843009213693952L;
   private final ArrayDeque<Long> z = new ArrayDeque<>();
   private boolean y;
   private long k = -2305843009213693952L;
   private long j = -2305843009213693952L;
   private long N = -2305843009213693952L;
   private long Q = -2305843009213693952L;
   private long o = -1L;
   private int H;
   private int s;
   public static final List<String> O = List.of("Random", "Asia", "East", "EU Central", "EU West", "Oceania", "West");
   static final List<String> t = List.of("asia", "east", "eu central", "eu west", "oceania", "west");
   static final long B = 15000L;
   public static final double x = 5.0;
   public static final float p = 8.0F;
   public static final float e = 10.0F;

   public void v() {
      this.z.clear();
      this.y = (boolean)0;
      this.k = this.j = this.N = this.Q = -2305843009213693952L;
      this.o = -1L;
      this.H = this.s = 0;
   }

   public boolean V() {
      return this.y;
   }

   public long i(long var1) {
      this.p(var1);
      long var3 = Math.max(this.N, this.j + 3000L);
      if (this.z.size() >= 4) {
         var3 = Math.max(var3, this.z.peekFirst() + 60000L);
      }

      return var3;
   }

   public boolean z(long var1) {
      return !this.y && var1 >= this.i(var1);
   }

   public void V(long var1) {
      this.p(var1);
      this.z.addLast(var1);
      this.j = var1;
      this.y = (boolean)1;
      this.k = var1 + 12000L;
   }

   public long N(long var1) {
      return this.y ? Math.max(0L, this.k - var1) : Math.max(0L, this.i(var1) - var1);
   }

   public boolean X(long var1) {
      return this.y && var1 >= this.k;
   }

   public void M(long var1) {
      if (this.y) {
         this.k = Math.max(this.k, var1 + 15000L);
      }

      this.s = 0;
   }

   public boolean d(long var1) {
      this.y = (boolean)0;
      this.s++;
      this.N = Math.max(this.N, var1 + this.N());
      return this.s >= 5;
   }

   public void o(long var1, long var3) {
      this.y = (boolean)0;
      this.s = 0;
      if (var3 < 0L) {
         this.N = Math.max(this.N, var1 + this.N());
      } else {
         this.H = 0;
         this.N = Math.max(var1 + 1L, var1 + var3 + 600L);
         if (this.Q != -2305843009213693952L) {
            this.o = Math.clamp(var1 - this.Q + var3, 5000, 600000);
         }
      }
   }

   public void M(long var1, long var3) {
      this.y = (boolean)0;
      this.N = Math.max(this.N, var1 + var3);
   }

   public void x(long var1) {
      this.y = (boolean)0;
      this.s = this.H = 0;
      this.Q = var1;
      this.N = this.o > 0L ? var1 + this.o + 600L : -2305843009213693952L;
   }

   public long H() {
      return this.o;
   }

   private long N() {
      return K[Math.min(this.H++, K.length - 1)];
   }

   private void p(long var1) {
      while (!this.z.isEmpty() && var1 - this.z.peekFirst() >= 60000L) {
         this.z.removeFirst();
      }
   }

   public static e.y O(boolean var0, boolean var1, boolean var2, boolean var3, double var4, long var6, boolean var8, long var9) {
      if (!var0) {
         return e.y.t;
      }

      if (var2) {
         return var3 && var4 >= 12.0 ? e.y.N : e.y.y;
      }

      if (var1) {
         return e.y.U;
      }

      int var11 = var9 >= 0L && var9 < 15000L ? 1 : 0;
      return var8 && var4 <= 5.0 && var6 > 3000L && var11 == 0 ? e.y.r : e.y.y;
   }

   public static boolean G(boolean var0, boolean var1, double var2, double var4, float var6) {
      return !var0 && !var1 && var6 > 8.0F && var2 <= var4;
   }

   public static boolean y(boolean var0, boolean var1, float var2) {
      return !var0 && !var1 && var2 > 8.0F;
   }

   public static boolean H(double var0, boolean var2, boolean var3, float var4, boolean var5) {
      int var6 = var4 <= 10.0F ? 1 : 0;
      if (var5) {
         return var0 <= (var2 ? 16 : (var6 != 0 ? 12 : 10));
      } else if (!var3 && var0 > 3.0) {
         return false;
      } else {
         return var2 ? var0 <= 7.0 : var0 <= 5.0 || var6 != 0 && var0 <= 8.0;
      }
   }

   public static double[] L(double var0, double var2, List<double[]> var4, double var5) {
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

   public static final class a {
      private final Map<String, Integer> R = new HashMap<>();
      private int a;

      public void g() {
         this.R.clear();
         this.a = 0;
      }

      public String z(String var1) {
         String var2 = var1 == null ? "" : var1.trim().toLowerCase(Locale.ROOT);
         if (com.larpclient.e.t.contains(var2)) {
            return var2;
         }

         for (int var3 = 0; var3 < com.larpclient.e.t.size(); var3++) {
            String var4 = com.larpclient.e.t.get(Math.floorMod(this.a + var3, com.larpclient.e.t.size()));
            if (this.R.getOrDefault(var4, 0) < 2) {
               this.a = Math.floorMod(this.a + var3 + 1, com.larpclient.e.t.size());
               return var4;
            }
         }

         this.R.clear();
         String var5 = com.larpclient.e.t.get(Math.floorMod(this.a, com.larpclient.e.t.size()));
         this.a = Math.floorMod(this.a + 1, com.larpclient.e.t.size());
         return var5;
      }

      public void v(String var1) {
         if (var1 != null && com.larpclient.e.t.contains(var1)) {
            this.R.merge(var1, 1, Integer::sum);
         }
      }

      public void i(String var1) {
         if (var1 != null) {
            this.R.remove(var1);
         }
      }
   }

   public enum y {
      t,
      U,
      y,
      N,
      r;
   }
}
