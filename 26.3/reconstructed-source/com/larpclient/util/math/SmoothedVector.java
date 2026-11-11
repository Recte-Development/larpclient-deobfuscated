package com.larpclient.util.math;

public final class SmoothedVector {
   private double x;
   private double y;
   private long lastUpdateNanos;

   public void update(double var1, double var3, long var5, boolean var7) {
      if (!var7) {
         this.x = this.y = 0.0;
         this.lastUpdateNanos = var5;
      } else {
         double var8 = this.lastUpdateNanos != 0L && var5 > this.lastUpdateNanos && var5 - this.lastUpdateNanos <= 500000000L
            ? (var5 - this.lastUpdateNanos) / 1.0E9
            : 0.0;
         this.lastUpdateNanos = var5;
         double var10 = 1.0 - Math.exp(-Math.min(0.1, var8) / 0.16);
         this.x = this.x + (sanitize(var1) - this.x) * var10;
         this.y = this.y + (sanitize(var3) - this.y) * var10;
      }
   }

   private static double sanitize(double var0) {
      return Double.isFinite(var0) ? Math.clamp(var0, -1.0, 1.0) : 0.0;
   }

   public double getX() {
      return this.x;
   }

   public double getY() {
      return this.y;
   }
}
