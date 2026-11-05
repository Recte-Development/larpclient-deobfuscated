package com.larpclient;

public final class m3 {
   private double h;
   private double o;
   private long I;

   public void R(double var1, double var3, long var5, boolean var7) {
      if (!var7) {
         this.h = this.o = 0.0;
         this.I = var5;
      } else {
         double var8 = this.I != 0L && var5 > this.I && var5 - this.I <= 500000000L ? (var5 - this.I) / 1.0E9 : 0.0;
         this.I = var5;
         double var10 = 1.0 - Math.exp(-Math.min(0.1, var8) / 0.16);
         this.h = this.h + (x(var1) - this.h) * var10;
         this.o = this.o + (x(var3) - this.o) * var10;
      }
   }

   private static double x(double var0) {
      return Double.isFinite(var0) ? Math.clamp(var0, -1.0, 1.0) : 0.0;
   }

   public double i() {
      return this.h;
   }

   public double h() {
      return this.o;
   }
}
