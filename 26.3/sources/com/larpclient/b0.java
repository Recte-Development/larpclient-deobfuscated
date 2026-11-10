package com.larpclient;

public final class b0 {
   private double X = Double.NaN;
   private double L;
   private double i;

   public double v(double var1, double var3) {
      if (Double.isFinite(this.X)) {
         this.i = this.i + Math.max(0.0, var1 - this.X) * this.L * Math.PI * 2.0;
      }

      this.X = var1;
      this.L = var3;
      return this.i;
   }

   public void a() {
      this.X = Double.NaN;
      this.i = 0.0;
      this.L = 0.0;
   }
}
