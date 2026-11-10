package com.larpclient;

public final class h2 {
   public static final h2 j = new h2();
   public static final int d = 900;
   public static final int Y = 750;
   public static final int J = 75;
   public static final int e = 750 + "Larp Client".length() * 75 + 425;
   public static final int U = 400;
   public static final int i = 500;
   private long q = -1L;
   private long R = -1L;

   public void W(long var1) {
      if (this.q < 0L) {
         this.q = var1;
      }
   }

   public void b(long var1) {
      if (this.R < 0L) {
         this.R = var1;
      }
   }

   public boolean G() {
      return this.R >= 0L;
   }

   public double i(long var1) {
      return this.q < 0L ? 0.0 : Math.max(0L, var1 - this.q) / 1000000.0;
   }

   public double s(long var1) {
      return this.q >= 0L && this.R >= 0L ? Math.max(0L, var1 - Math.max(this.R, this.q + e * 1000000L)) / 1000000.0 : 0.0;
   }

   public double x(long var1, boolean var3) {
      return var3 ? C(this.i(var1) / 450.0) : 1.0;
   }

   public double B(long var1, boolean var3) {
      return var3 ? 0.82 + 0.18 * B(this.i(var1) / 900.0) : 1.0;
   }

   public double c(long var1, boolean var3) {
      return var3 ? (-Math.PI * 2) * (1.0 - B(this.i(var1) / 900.0)) : 0.0;
   }

   public int g(long var1, boolean var3) {
      return var3 ? Math.clamp((int)((this.i(var1) - 750.0) / 75.0), 0, "Larp Client".length()) : "Larp Client".length();
   }

   public double d(long var1, boolean var3) {
      return this.R < 0L ? 1.0 : (var3 ? 1.0 - C(this.s(var1) / 400.0) : 0.0);
   }

   public double M(long var1, boolean var3) {
      if (this.R < 0L) {
         return 0.0;
      } else {
         return var3 ? C((this.s(var1) - 400.0) / 500.0) : 1.0;
      }
   }

   public boolean H(long var1, boolean var3) {
      return this.M(var1, var3) >= 1.0;
   }

   public h2.e O(long var1, boolean var3) {
      if (this.q < 0L) {
         return h2.e.N;
      }

      if (this.R >= 0L && (!var3 || !(this.i(var1) < e))) {
         if (this.d(var1, var3) > 0.0) {
            return h2.e.j;
         } else {
            return this.H(var1, var3) ? h2.e.O : h2.e.J;
         }
      } else {
         return h2.e.b;
      }
   }

   public static double C(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   private static double B(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return 1.0 - Math.pow(1.0 - var0, 3.0);
   }

   public enum e {
      N,
      b,
      j,
      J,
      O;
   }
}
