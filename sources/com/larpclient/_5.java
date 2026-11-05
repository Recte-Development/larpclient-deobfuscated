package com.larpclient;

public final class _5 {
   private double H = Double.NaN;
   private double e;
   private double O;

   public double p(double var1, double var3) {
      if (Double.isFinite(this.H)) {
         this.O = this.O + Math.max(0.0, var1 - this.H) * this.e * Math.PI * 2.0;
      }

      this.H = var1;
      this.e = var3;
      return this.O;
   }

   public void q() {
      this.H = Double.NaN;
      this.O = 0.0;
      this.e = 0.0;
   }
}
