package com.larpclient;

public final class dv {
   private double v;
   private double V;
   private long a;

   public void h(double var1, double var3, long var5, boolean var7) {
      if (!var7) {
         this.v = this.V = 0.0;
         this.a = var5;
      } else {
         double var8 = this.a != 0L && var5 > this.a && var5 - this.a <= 500000000L ? (var5 - this.a) / 1.0E9 : 0.0;
         this.a = var5;
         double var10 = 1.0 - Math.exp(-Math.min(0.1, var8) / 0.16);
         this.v = this.v + (K(var1) - this.v) * var10;
         this.V = this.V + (K(var3) - this.V) * var10;
      }
   }

   private static double K(double var0) {
      return Double.isFinite(var0) ? Math.clamp(var0, -1.0, 1.0) : 0.0;
   }

   public double N() {
      return this.v;
   }

   public double e() {
      return this.V;
   }
}
