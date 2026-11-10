package com.larpclient;

public final class c3 {
   private double T = Double.NaN;
   private double k;
   private double F;

   public double u(double var1, double var3) {
      if (Double.isFinite(this.T)) {
         this.F = this.F + Math.max(0.0, var1 - this.T) * this.k * Math.PI * 2.0;
      }

      this.T = var1;
      this.k = var3;
      return this.F;
   }

   public void K() {
      this.T = Double.NaN;
      this.F = 0.0;
      this.k = 0.0;
   }
}
