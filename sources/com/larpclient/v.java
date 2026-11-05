package com.larpclient;

public final class v {
   public static final v s = new v();
   public static final int c = 900;
   public static final int N = 750;
   public static final int e = 75;
   public static final int Y = 750 + "Larp Client".length() * 75 + 425;
   public static final int l = 400;
   public static final int q = 500;
   private long k = -1L;
   private long K = -1L;

   public void W(long var1) {
      if (this.k < 0L) {
         this.k = var1;
      }
   }

   public void b(long var1) {
      if (this.K < 0L) {
         this.K = var1;
      }
   }

   public boolean Y() {
      return this.K >= 0L;
   }

   public double s(long var1) {
      return this.k < 0L ? 0.0 : Math.max(0L, var1 - this.k) / 1000000.0;
   }

   public double f(long var1) {
      return this.k >= 0L && this.K >= 0L ? Math.max(0L, var1 - Math.max(this.K, this.k + Y * 1000000L)) / 1000000.0 : 0.0;
   }

   public double K(long var1, boolean var3) {
      return var3 ? T(this.s(var1) / 450.0) : 1.0;
   }

   public double e(long var1, boolean var3) {
      return var3 ? 0.82 + 0.18 * c(this.s(var1) / 900.0) : 1.0;
   }

   public double c(long var1, boolean var3) {
      return var3 ? (-Math.PI * 2) * (1.0 - c(this.s(var1) / 900.0)) : 0.0;
   }

   public int f(long var1, boolean var3) {
      return var3 ? Math.clamp((int)((this.s(var1) - 750.0) / 75.0), 0, "Larp Client".length()) : "Larp Client".length();
   }

   public double H(long var1, boolean var3) {
      return this.K < 0L ? 1.0 : (var3 ? 1.0 - T(this.f(var1) / 400.0) : 0.0);
   }

   public double M(long var1, boolean var3) {
      if (this.K < 0L) {
         return 0.0;
      } else {
         return var3 ? T((this.f(var1) - 400.0) / 500.0) : 1.0;
      }
   }

   public boolean x(long var1, boolean var3) {
      return this.M(var1, var3) >= 1.0;
   }

   public v.o L(long var1, boolean var3) {
      if (this.k < 0L) {
         return v.o.H;
      }

      if (this.K >= 0L && (!var3 || !(this.s(var1) < Y))) {
         if (this.H(var1, var3) > 0.0) {
            return v.o.R;
         } else {
            return this.x(var1, var3) ? v.o.e : v.o.l;
         }
      } else {
         return v.o.y;
      }
   }

   public static double T(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   private static double c(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return 1.0 - Math.pow(1.0 - var0, 3.0);
   }

   public enum o {
      H,
      y,
      R,
      l,
      e;
   }
}
