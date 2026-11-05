package com.larpclient;

public final class ph {
   public static final double x = 0.05;
   public static final double I = 0.35;
   private static final double u = 720.0;

   public static double N(double var0) {
      return var0 - Math.floor((var0 + 180.0) / 360.0) * 360.0;
   }

   public static double U(double var0, double var2, boolean var4) {
      return Math.hypot(N(var0), var4 ? var2 : 0.0);
   }

   public static boolean T(double var0, double var2) {
      return Double.isFinite(var0) && var0 <= Math.clamp(var2, 0.0, 180.0) / 2.0;
   }

   public static boolean k(double var0, int var2, double var3, int var5, boolean var6) {
      double var7 = var6 ? Math.max(2.0, var3 * 0.15) : 0.0;
      return var0 < var3 - var7 - 1.0E-7 || !var6 && Math.abs(var0 - var3) <= 1.0E-7 && var2 < var5;
   }

   public static boolean u(double var0, double var2, double var4, double var6, boolean var8) {
      return var0 * var4 < -1.0E-5 || var8 && var2 * var6 < -1.0E-5;
   }

   private ph() {
   }

   public static final class j {
      private double R;
      private double u;

      public double e(double var1, double var3, double var5) {
         if (Double.isFinite(var1) && Double.isFinite(var3) && Double.isFinite(var5) && !(var3 <= 0.0) && !(var5 <= 0.0)) {
            double var7 = Math.min(var3, 0.05);
            if (Math.abs(var1) <= 0.35) {
               this.D();
               return 0.0;
            }

            double var9 = Math.clamp(var1 * 8.0, -var5, var5);
            if (this.R * var1 < 0.0) {
               this.R = 0.0;
            }

            this.R = Math.clamp(this.R, -var5, var5);
            double var11 = this.R;
            this.R = this.R + Math.clamp(var9 - this.R, -720.0 * var7, 720.0 * var7);
            double var13 = (var11 + this.R) * 0.5 * var7;
            return Math.copySign(Math.min(Math.min(Math.abs(var13), var5 * var7), Math.max(0.0, Math.abs(var1) - 0.35)), var1);
         } else {
            this.D();
            return 0.0;
         }
      }

      public float H(float var1, double var2, boolean var4) {
         double var5 = var1 + var2 + this.u;
         if (var4) {
            var5 = Math.clamp(var5, -90.0, 90.0);
         }

         float var7 = (float)var5;
         this.u = var5 - var7;
         return var7;
      }

      public void D() {
         this.R = this.u = 0.0;
      }

      public double B() {
         return this.R;
      }
   }

   public static final class t {
      private long j;
      private boolean t;

      public double Q(long var1) {
         long var3 = this.j == 0L ? 0L : var1 - this.j;
         this.t = (boolean)(this.j == 0L || var3 > 0L && var3 <= 250000000L ? 0 : 1);
         this.j = var1;
         return var3 <= 0L ? 0.0 : Math.min(var3 / 1.0E9, 0.05);
      }

      public boolean J() {
         return this.t;
      }

      public void Z() {
         this.j = 0L;
         this.t = (boolean)0;
      }
   }
}
