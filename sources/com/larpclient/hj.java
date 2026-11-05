package com.larpclient;

public final class hj {
   private double B = Double.NaN;
   private double t;
   private double u;

   public double d(double var1, double var3) {
      if (Double.isFinite(this.B)) {
         this.u = this.u + Math.max(0.0, var1 - this.B) * this.t * Math.PI * 2.0;
      }

      this.B = var1;
      this.t = var3;
      return this.u;
   }

   public void q() {
      this.B = Double.NaN;
      this.u = 0.0;
      this.t = 0.0;
   }
}
