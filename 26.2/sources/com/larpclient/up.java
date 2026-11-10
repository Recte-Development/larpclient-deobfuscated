package com.larpclient;

public final class up {
   private double c;
   private double x;
   private long X;

   public void h(double var1, double var3, long var5, boolean var7) {
      if (!var7) {
         this.c = this.x = 0.0;
         this.X = var5;
      } else {
         double var8 = this.X != 0L && var5 > this.X && var5 - this.X <= 500000000L ? (var5 - this.X) / 1.0E9 : 0.0;
         this.X = var5;
         double var10 = 1.0 - Math.exp(-Math.min(0.1, var8) / 0.16);
         this.c = this.c + (Q(var1) - this.c) * var10;
         this.x = this.x + (Q(var3) - this.x) * var10;
      }
   }

   private static double Q(double var0) {
      return Double.isFinite(var0) ? Math.clamp(var0, -1.0, 1.0) : 0.0;
   }

   public double i() {
      return this.c;
   }

   public double l() {
      return this.x;
   }
}
