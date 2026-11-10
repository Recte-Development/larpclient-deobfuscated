package com.larpclient;

public final class wd {
   static final double S = 0.5;
   static final double w = 0.5;
   private Object d;
   private float t;
   private boolean u;
   private boolean R;
   private boolean I;
   private boolean L;
   private int v;
   private double r;
   private double f;
   private double n;

   public void t() {
      this.d = null;
      this.u = this.R = this.I = this.L = (boolean)0;
   }

   public float M(Object var1, float var2, boolean var3) {
      float var4 = Float.isFinite(var2) ? Math.clamp(var2, 0.0F, 1.0F) : 0.0F;
      if (var3) {
         return var4;
      }

      if (var1 != this.d) {
         this.d = var1;
         this.t = 1.0F;
         this.u = (boolean)1;
      }

      if (!this.u) {
         return 0.0F;
      }

      this.t = Math.min(this.t, var4);
      if (this.t <= 0.01F) {
         this.u = (boolean)0;
      }

      return this.u ? this.t : 0.0F;
   }

   public void p(boolean var1, int var2, double var3) {
      int var5 = !this.R || !var1 || this.I && var2 >= this.v ? 0 : 1;
      this.R = (boolean)1;
      this.I = var1;
      this.v = var2;
      if (var5 != 0) {
         if (this.L) {
            if (this.v(var3) < 0.5) {
               return;
            }

            this.r = this.j(var3);
            this.f += 360.0;
         } else {
            this.r = 0.0;
            this.f = 360.0;
         }

         this.n = var3;
         this.L = (boolean)1;
      }
   }

   public float P(double var1) {
      if (!this.L) {
         return 0.0F;
      } else if (this.v(var1) >= 1.0) {
         this.L = (boolean)0;
         return 0.0F;
      } else {
         return (float)(this.j(var1) % 360.0);
      }
   }

   private double v(double var1) {
      return Math.clamp((var1 - this.n) / 0.5, 0.0, 1.0);
   }

   private double j(double var1) {
      double var3 = 1.0 - this.v(var1);
      return this.r + (this.f - this.r) * (1.0 - var3 * var3 * var3);
   }
}
