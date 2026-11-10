package com.larpclient;

public final class gs {
   static final double y = 0.5;
   static final double I = 0.5;
   private Object S;
   private float e;
   private boolean N;
   private boolean x;
   private boolean Q;
   private boolean o;
   private int q;
   private double l;
   private double D;
   private double g;

   public void l() {
      this.S = null;
      this.N = this.x = this.Q = this.o = (boolean)0;
   }

   public float m(Object var1, float var2, boolean var3) {
      float var4 = Float.isFinite(var2) ? Math.clamp(var2, 0.0F, 1.0F) : 0.0F;
      if (var3) {
         return var4;
      }

      if (var1 != this.S) {
         this.S = var1;
         this.e = 1.0F;
         this.N = (boolean)1;
      }

      if (!this.N) {
         return 0.0F;
      }

      this.e = Math.min(this.e, var4);
      if (this.e <= 0.01F) {
         this.N = (boolean)0;
      }

      return this.N ? this.e : 0.0F;
   }

   public void Y(boolean var1, int var2, double var3) {
      int var5 = !this.x || !var1 || this.Q && var2 >= this.q ? 0 : 1;
      this.x = (boolean)1;
      this.Q = var1;
      this.q = var2;
      if (var5 != 0) {
         if (this.o) {
            if (this.w(var3) < 0.5) {
               return;
            }

            this.l = this.H(var3);
            this.D += 360.0;
         } else {
            this.l = 0.0;
            this.D = 360.0;
         }

         this.g = var3;
         this.o = (boolean)1;
      }
   }

   public float f(double var1) {
      if (!this.o) {
         return 0.0F;
      } else if (this.w(var1) >= 1.0) {
         this.o = (boolean)0;
         return 0.0F;
      } else {
         return (float)(this.H(var1) % 360.0);
      }
   }

   private double w(double var1) {
      return Math.clamp((var1 - this.g) / 0.5, 0.0, 1.0);
   }

   private double H(double var1) {
      double var3 = 1.0 - this.w(var1);
      return this.l + (this.D - this.l) * (1.0 - var3 * var3 * var3);
   }
}
