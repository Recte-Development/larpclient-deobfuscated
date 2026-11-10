package com.larpclient;

public final class wo {
   public static final wo K = new wo();
   public static final int P = 900;
   public static final int E = 750;
   public static final int W = 75;
   public static final int M = 750 + "Larp Client".length() * 75 + 425;
   public static final int H = 400;
   public static final int G = 500;
   private long z = -1L;
   private long L = -1L;

   public void n(long var1) {
      if (this.z < 0L) {
         this.z = var1;
      }
   }

   public void a(long var1) {
      if (this.L < 0L) {
         this.L = var1;
      }
   }

   public boolean j() {
      return this.L >= 0L;
   }

   public double R(long var1) {
      return this.z < 0L ? 0.0 : Math.max(0L, var1 - this.z) / 1000000.0;
   }

   public double Q(long var1) {
      return this.z >= 0L && this.L >= 0L ? Math.max(0L, var1 - Math.max(this.L, this.z + M * 1000000L)) / 1000000.0 : 0.0;
   }

   public double i(long var1, boolean var3) {
      return var3 ? C(this.R(var1) / 450.0) : 1.0;
   }

   public double q(long var1, boolean var3) {
      return var3 ? 0.82 + 0.18 * a(this.R(var1) / 900.0) : 1.0;
   }

   public double S(long var1, boolean var3) {
      return var3 ? (-Math.PI * 2) * (1.0 - a(this.R(var1) / 900.0)) : 0.0;
   }

   public int c(long var1, boolean var3) {
      return var3 ? Math.clamp((int)((this.R(var1) - 750.0) / 75.0), 0, "Larp Client".length()) : "Larp Client".length();
   }

   public double P(long var1, boolean var3) {
      return this.L < 0L ? 1.0 : (var3 ? 1.0 - C(this.Q(var1) / 400.0) : 0.0);
   }

   public double z(long var1, boolean var3) {
      if (this.L < 0L) {
         return 0.0;
      } else {
         return var3 ? C((this.Q(var1) - 400.0) / 500.0) : 1.0;
      }
   }

   public boolean q(long var1, boolean var3) {
      return this.z(var1, var3) >= 1.0;
   }

   public wo.e L(long var1, boolean var3) {
      if (this.z < 0L) {
         return wo.e.B;
      }

      if (this.L >= 0L && (!var3 || !(this.R(var1) < M))) {
         if (this.P(var1, var3) > 0.0) {
            return wo.e.x;
         } else {
            return this.q(var1, var3) ? wo.e.a : wo.e.m;
         }
      } else {
         return wo.e.T;
      }
   }

   public static double C(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   private static double a(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return 1.0 - Math.pow(1.0 - var0, 3.0);
   }

   public enum e {
      B,
      T,
      x,
      m,
      a;
   }
}
