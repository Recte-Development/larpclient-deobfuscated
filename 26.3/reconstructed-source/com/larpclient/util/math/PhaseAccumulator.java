package com.larpclient.util.math;

public final class PhaseAccumulator {
   private double lastTime = Double.NaN;
   private double frequency;
   private double phase;

   public double advance(double var1, double var3) {
      if (Double.isFinite(this.lastTime)) {
         this.phase = this.phase + Math.max(0.0, var1 - this.lastTime) * this.frequency * Math.PI * 2.0;
      }

      this.lastTime = var1;
      this.frequency = var3;
      return this.phase;
   }

   public void reset() {
      this.lastTime = Double.NaN;
      this.phase = 0.0;
      this.frequency = 0.0;
   }
}
