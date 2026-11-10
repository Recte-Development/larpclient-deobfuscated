package com.larpclient;

public final class uh {
   private double T;
   private double J;
   private long o;

   public void t(double var1, double var3, long var5, boolean var7) {
      if (!var7) {
         this.T = this.J = 0.0;
         this.o = var5;
      } else {
         double var8 = this.o != 0L && var5 > this.o && var5 - this.o <= 500000000L ? (var5 - this.o) / 1.0E9 : 0.0;
         this.o = var5;
         double var10 = 1.0 - Math.exp(-Math.min(0.1, var8) / 0.16);
         this.T = this.T + (r(var1) - this.T) * var10;
         this.J = this.J + (r(var3) - this.J) * var10;
      }
   }

   private static double r(double var0) {
      return Double.isFinite(var0) ? Math.clamp(var0, -1.0, 1.0) : 0.0;
   }

   public double H() {
      return this.T;
   }

   public double U() {
      return this.J;
   }
}
