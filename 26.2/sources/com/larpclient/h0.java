package com.larpclient;

public final class h0 {
   public static final double V = 0.05;
   public static final double m = 0.35;
   private static final double n = 720.0;

   public static double R(double var0) {
      return var0 - Math.floor((var0 + 180.0) / 360.0) * 360.0;
   }

   public static double r(double var0, double var2, boolean var4) {
      return Math.hypot(R(var0), var4 ? var2 : 0.0);
   }

   public static boolean d(double var0, double var2) {
      return Double.isFinite(var0) && var0 <= Math.clamp(var2, 0.0, 180.0) / 2.0;
   }

   public static boolean l(double var0, int var2, double var3, int var5, boolean var6) {
      double var7 = var6 ? Math.max(2.0, var3 * 0.15) : 0.0;
      return var0 < var3 - var7 - 1.0E-7 || !var6 && Math.abs(var0 - var3) <= 1.0E-7 && var2 < var5;
   }

   public static boolean y(double var0, double var2, double var4, double var6, boolean var8) {
      return var0 * var4 < -1.0E-5 || var8 && var2 * var6 < -1.0E-5;
   }

   private h0() {
   }

   public static final class e {
      private double t;
      private double K;

      public double r(double var1, double var3, double var5) {
         if (Double.isFinite(var1) && Double.isFinite(var3) && Double.isFinite(var5) && !(var3 <= 0.0) && !(var5 <= 0.0)) {
            double var7 = Math.min(var3, 0.05);
            if (Math.abs(var1) <= 0.35) {
               this.Z();
               return 0.0;
            }

            double var9 = Math.clamp(var1 * 8.0, -var5, var5);
            if (this.t * var1 < 0.0) {
               this.t = 0.0;
            }

            this.t = Math.clamp(this.t, -var5, var5);
            double var11 = this.t;
            this.t = this.t + Math.clamp(var9 - this.t, -720.0 * var7, 720.0 * var7);
            double var13 = (var11 + this.t) * 0.5 * var7;
            return Math.copySign(Math.min(Math.min(Math.abs(var13), var5 * var7), Math.max(0.0, Math.abs(var1) - 0.35)), var1);
         } else {
            this.Z();
            return 0.0;
         }
      }

      public float P(float var1, double var2, boolean var4) {
         double var5 = var1 + var2 + this.K;
         if (var4) {
            var5 = Math.clamp(var5, -90.0, 90.0);
         }

         float var7 = (float)var5;
         this.K = var5 - var7;
         return var7;
      }

      public void Z() {
         this.t = this.K = 0.0;
      }

      public double V() {
         return this.t;
      }
   }

   public static final class p {
      private long b;
      private boolean z;

      public double o(long var1) {
         long var3 = this.b == 0L ? 0L : var1 - this.b;
         this.z = (boolean)(this.b == 0L || var3 > 0L && var3 <= 250000000L ? 0 : 1);
         this.b = var1;
         return var3 <= 0L ? 0.0 : Math.min(var3 / 1.0E9, 0.05);
      }

      public boolean j() {
         return this.z;
      }

      public void C() {
         this.b = 0L;
         this.z = (boolean)0;
      }
   }
}
