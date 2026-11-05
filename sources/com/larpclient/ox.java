package com.larpclient;

public final class ox {
   public static final ox t = new ox();
   public static final int F = 900;
   public static final int Z = 750;
   public static final int u = 75;
   public static final int o = 750 + "Larp Client".length() * 75 + 425;
   public static final int G = 400;
   public static final int i = 500;
   private long a = -1L;
   private long g = -1L;

   public void j(long var1) {
      if (this.a < 0L) {
         this.a = var1;
      }
   }

   public void O(long var1) {
      if (this.g < 0L) {
         this.g = var1;
      }
   }

   public boolean g() {
      return this.g >= 0L;
   }

   public double m(long var1) {
      return this.a < 0L ? 0.0 : Math.max(0L, var1 - this.a) / 1000000.0;
   }

   public double r(long var1) {
      return this.a >= 0L && this.g >= 0L ? Math.max(0L, var1 - Math.max(this.g, this.a + o * 1000000L)) / 1000000.0 : 0.0;
   }

   public double a(long var1, boolean var3) {
      return var3 ? R(this.m(var1) / 450.0) : 1.0;
   }

   public double C(long var1, boolean var3) {
      return var3 ? 0.82 + 0.18 * S(this.m(var1) / 900.0) : 1.0;
   }

   public double A(long var1, boolean var3) {
      return var3 ? (-Math.PI * 2) * (1.0 - S(this.m(var1) / 900.0)) : 0.0;
   }

   public int L(long var1, boolean var3) {
      return var3 ? Math.clamp((int)((this.m(var1) - 750.0) / 75.0), 0, "Larp Client".length()) : "Larp Client".length();
   }

   public double x(long var1, boolean var3) {
      return this.g < 0L ? 1.0 : (var3 ? 1.0 - R(this.r(var1) / 400.0) : 0.0);
   }

   public double B(long var1, boolean var3) {
      if (this.g < 0L) {
         return 0.0;
      } else {
         return var3 ? R((this.r(var1) - 400.0) / 500.0) : 1.0;
      }
   }

   public boolean n(long var1, boolean var3) {
      return this.B(var1, var3) >= 1.0;
   }

   public ox.l m(long var1, boolean var3) {
      if (this.a < 0L) {
         return ox.l.k;
      }

      if (this.g >= 0L && (!var3 || !(this.m(var1) < o))) {
         if (this.x(var1, var3) > 0.0) {
            return ox.l.m;
         } else {
            return this.n(var1, var3) ? ox.l.A : ox.l.P;
         }
      } else {
         return ox.l.n;
      }
   }

   public static double R(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   private static double S(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return 1.0 - Math.pow(1.0 - var0, 3.0);
   }

   public enum l {
      k,
      n,
      m,
      P,
      A;
   }
}
