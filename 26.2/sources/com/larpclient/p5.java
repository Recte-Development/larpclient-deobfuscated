package com.larpclient;

public final class p5 {
   private double k = Double.NaN;
   private double B;
   private double d;

   public double E(double var1, double var3) {
      if (Double.isFinite(this.k)) {
         this.d = this.d + Math.max(0.0, var1 - this.k) * this.B * Math.PI * 2.0;
      }

      this.k = var1;
      this.B = var3;
      return this.d;
   }

   public void x() {
      this.k = Double.NaN;
      this.d = 0.0;
      this.B = 0.0;
   }
}
