package com.larpclient;

public final class p8 {
   private double Q;
   private double I;
   private long Z;

   public void x(double var1, double var3, long var5, boolean var7) {
      if (!var7) {
         this.Q = this.I = 0.0;
         this.Z = var5;
      } else {
         double var8 = this.Z != 0L && var5 > this.Z && var5 - this.Z <= 500000000L ? (var5 - this.Z) / 1.0E9 : 0.0;
         this.Z = var5;
         double var10 = 1.0 - Math.exp(-Math.min(0.1, var8) / 0.16);
         this.Q = this.Q + (J(var1) - this.Q) * var10;
         this.I = this.I + (J(var3) - this.I) * var10;
      }
   }

   private static double J(double var0) {
      return Double.isFinite(var0) ? Math.clamp(var0, -1.0, 1.0) : 0.0;
   }

   public double m() {
      return this.Q;
   }

   public double f() {
      return this.I;
   }
}
