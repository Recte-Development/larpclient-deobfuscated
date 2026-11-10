package com.larpclient;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class va {
   static final long b = 12000L;
   static final long e = 15000L;
   static final long D = 3000L;
   static final long B = 60000L;
   static final int N = 4;
   static final int I = 5;
   static final long[] A = new long[]{10000L, 20000L, 40000L, 60000L};
   static final long S = 10000L;
   static final long M = 1500L;
   static final long Y = 3000L;
   static final long k = 600L;
   private static final long n = -2305843009213693952L;
   private final ArrayDeque<Long> X = new ArrayDeque<>();
   private boolean R;
   private long h = -2305843009213693952L;
   private long P = -2305843009213693952L;
   private long Z = -2305843009213693952L;
   private long c = -2305843009213693952L;
   private long r = -1L;
   private int C;
   private int G;
   public static final List<String> o = List.of("Random", "Asia", "East", "EU Central", "EU West", "Oceania", "West");
   static final List<String> j = List.of("asia", "east", "eu central", "eu west", "oceania", "west");
   static final long p = 15000L;
   public static final double v = 5.0;
   public static final float L = 8.0F;
   public static final float U = 10.0F;

   public void x() {
      this.X.clear();
      this.R = (boolean)0;
      this.h = this.P = this.Z = this.c = -2305843009213693952L;
      this.r = -1L;
      this.C = this.G = 0;
   }

   public boolean O() {
      return this.R;
   }

   public long T(long var1) {
      this.S(var1);
      long var3 = Math.max(this.Z, this.P + 3000L);
      if (this.X.size() >= 4) {
         var3 = Math.max(var3, this.X.peekFirst() + 60000L);
      }

      return var3;
   }

   public boolean h(long var1) {
      return !this.R && var1 >= this.T(var1);
   }

   public void o(long var1) {
      this.S(var1);
      this.X.addLast(var1);
      this.P = var1;
      this.R = (boolean)1;
      this.h = var1 + 12000L;
   }

   public long K(long var1) {
      return this.R ? Math.max(0L, this.h - var1) : Math.max(0L, this.T(var1) - var1);
   }

   public boolean s(long var1) {
      return this.R && var1 >= this.h;
   }

   public void Q(long var1) {
      if (this.R) {
         this.h = Math.max(this.h, var1 + 15000L);
      }

      this.G = 0;
   }

   public boolean Z(long var1) {
      this.R = (boolean)0;
      this.G++;
      this.Z = Math.max(this.Z, var1 + this.Q());
      return this.G >= 5;
   }

   public void q(long var1, long var3) {
      this.R = (boolean)0;
      this.G = 0;
      if (var3 < 0L) {
         this.Z = Math.max(this.Z, var1 + this.Q());
      } else {
         this.C = 0;
         this.Z = Math.max(var1 + 1L, var1 + var3 + 600L);
         if (this.c != -2305843009213693952L) {
            this.r = Math.clamp(var1 - this.c + var3, 5000, 600000);
         }
      }
   }

   public void N(long var1, long var3) {
      this.R = (boolean)0;
      this.Z = Math.max(this.Z, var1 + var3);
   }

   public void y(long var1) {
      this.R = (boolean)0;
      this.G = this.C = 0;
      this.c = var1;
      this.Z = this.r > 0L ? var1 + this.r + 600L : -2305843009213693952L;
   }

   public long k() {
      return this.r;
   }

   private long Q() {
      return A[Math.min(this.C++, A.length - 1)];
   }

   private void S(long var1) {
      while (!this.X.isEmpty() && var1 - this.X.peekFirst() >= 60000L) {
         this.X.removeFirst();
      }
   }

   public static va.v n(boolean var0, boolean var1, boolean var2, boolean var3, double var4, long var6, boolean var8, long var9) {
      if (!var0) {
         return va.v.o;
      }

      if (var2) {
         return var3 && var4 >= 12.0 ? va.v.D : va.v.f;
      }

      if (var1) {
         return va.v.v;
      }

      int var11 = var9 >= 0L && var9 < 15000L ? 1 : 0;
      return var8 && var4 <= 5.0 && var6 > 3000L && var11 == 0 ? va.v.Z : va.v.f;
   }

   public static boolean K(boolean var0, boolean var1, double var2, double var4, float var6) {
      return !var0 && !var1 && var6 > 8.0F && var2 <= var4;
   }

   public static boolean J(boolean var0, boolean var1, float var2) {
      return !var0 && !var1 && var2 > 8.0F;
   }

   public static boolean Y(double var0, boolean var2, boolean var3, float var4, boolean var5) {
      int var6 = var4 <= 10.0F ? 1 : 0;
      if (var5) {
         return var0 <= (var2 ? 16 : (var6 != 0 ? 12 : 10));
      } else if (!var3 && var0 > 3.0) {
         return false;
      } else {
         return var2 ? var0 <= 7.0 : var0 <= 5.0 || var6 != 0 && var0 <= 8.0;
      }
   }

   public static double[] g(double var0, double var2, List<double[]> var4, double var5) {
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

   public static final class i {
      private final Map<String, Integer> P = new HashMap<>();
      private int M;

      public void X() {
         this.P.clear();
         this.M = 0;
      }

      public String K(String var1) {
         String var2 = var1 == null ? "" : var1.trim().toLowerCase(Locale.ROOT);
         if (va.j.contains(var2)) {
            return var2;
         }

         for (int var3 = 0; var3 < va.j.size(); var3++) {
            String var4 = va.j.get(Math.floorMod(this.M + var3, va.j.size()));
            if (this.P.getOrDefault(var4, 0) < 2) {
               this.M = Math.floorMod(this.M + var3 + 1, va.j.size());
               return var4;
            }
         }

         this.P.clear();
         String var5 = va.j.get(Math.floorMod(this.M, va.j.size()));
         this.M = Math.floorMod(this.M + 1, va.j.size());
         return var5;
      }

      public void g(String var1) {
         if (var1 != null && va.j.contains(var1)) {
            this.P.merge(var1, 1, Integer::sum);
         }
      }

      public void G(String var1) {
         if (var1 != null) {
            this.P.remove(var1);
         }
      }
   }

   public enum v {
      o,
      v,
      f,
      D,
      Z;
   }
}
