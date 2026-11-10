package com.larpclient;

public final class sn {
   public static final sn p = new sn();
   public static final int E = 900;
   public static final int a = 750;
   public static final int u = 75;
   public static final int I = 750 + "Larp Client".length() * 75 + 425;
   public static final int n = 400;
   public static final int o = 500;
   private long X = -1L;
   private long w = -1L;

   public void E(long var1) {
      if (this.X < 0L) {
         this.X = var1;
      }
   }

   public void s(long var1) {
      if (this.w < 0L) {
         this.w = var1;
      }
   }

   public boolean b() {
      return this.w >= 0L;
   }

   public double L(long var1) {
      return this.X < 0L ? 0.0 : Math.max(0L, var1 - this.X) / 1000000.0;
   }

   public double E(long var1) {
      return this.X >= 0L && this.w >= 0L ? Math.max(0L, var1 - Math.max(this.w, this.X + I * 1000000L)) / 1000000.0 : 0.0;
   }

   public double E(long var1, boolean var3) {
      return var3 ? q(this.L(var1) / 450.0) : 1.0;
   }

   public double N(long var1, boolean var3) {
      return var3 ? 0.82 + 0.18 * R(this.L(var1) / 900.0) : 1.0;
   }

   public double h(long var1, boolean var3) {
      return var3 ? (-Math.PI * 2) * (1.0 - R(this.L(var1) / 900.0)) : 0.0;
   }

   public int i(long var1, boolean var3) {
      return var3 ? Math.clamp((int)((this.L(var1) - 750.0) / 75.0), 0, "Larp Client".length()) : "Larp Client".length();
   }

   public double a(long var1, boolean var3) {
      return this.w < 0L ? 1.0 : (var3 ? 1.0 - q(this.E(var1) / 400.0) : 0.0);
   }

   public double p(long var1, boolean var3) {
      if (this.w < 0L) {
         return 0.0;
      } else {
         return var3 ? q((this.E(var1) - 400.0) / 500.0) : 1.0;
      }
   }

   public boolean R(long var1, boolean var3) {
      return this.p(var1, var3) >= 1.0;
   }

   public sn.j m(long var1, boolean var3) {
      if (this.X < 0L) {
         return sn.j.O;
      }

      if (this.w >= 0L && (!var3 || !(this.L(var1) < I))) {
         if (this.a(var1, var3) > 0.0) {
            return sn.j.T;
         } else {
            return this.R(var1, var3) ? sn.j.g : sn.j.D;
         }
      } else {
         return sn.j.z;
      }
   }

   public static double q(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return var0 * var0 * (3.0 - 2.0 * var0);
   }

   private static double R(double var0) {
      var0 = Math.clamp(var0, 0.0, 1.0);
      return 1.0 - Math.pow(1.0 - var0, 3.0);
   }

   public enum j {
      O,
      z,
      T,
      D,
      g;
   }
}
