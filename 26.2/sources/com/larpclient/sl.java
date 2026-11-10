package com.larpclient;

public final class sl {
   static final double Q = 0.5;
   static final double w = 0.5;
   private Object n;
   private float Y;
   private boolean V;
   private boolean P;
   private boolean X;
   private boolean Z;
   private int I;
   private double N;
   private double c;
   private double O;

   public void E() {
      this.n = null;
      this.V = this.P = this.X = this.Z = (boolean)0;
   }

   public float e(Object var1, float var2, boolean var3) {
      float var4 = Float.isFinite(var2) ? Math.clamp(var2, 0.0F, 1.0F) : 0.0F;
      if (var3) {
         return var4;
      }

      if (var1 != this.n) {
         this.n = var1;
         this.Y = 1.0F;
         this.V = (boolean)1;
      }

      if (!this.V) {
         return 0.0F;
      }

      this.Y = Math.min(this.Y, var4);
      if (this.Y <= 0.01F) {
         this.V = (boolean)0;
      }

      return this.V ? this.Y : 0.0F;
   }

   public void E(boolean var1, int var2, double var3) {
      int var5 = !this.P || !var1 || this.X && var2 >= this.I ? 0 : 1;
      this.P = (boolean)1;
      this.X = var1;
      this.I = var2;
      if (var5 != 0) {
         if (this.Z) {
            if (this.w(var3) < 0.5) {
               return;
            }

            this.N = this.O(var3);
            this.c += 360.0;
         } else {
            this.N = 0.0;
            this.c = 360.0;
         }

         this.O = var3;
         this.Z = (boolean)1;
      }
   }

   public float I(double var1) {
      if (!this.Z) {
         return 0.0F;
      } else if (this.w(var1) >= 1.0) {
         this.Z = (boolean)0;
         return 0.0F;
      } else {
         return (float)(this.O(var1) % 360.0);
      }
   }

   private double w(double var1) {
      return Math.clamp((var1 - this.O) / 0.5, 0.0, 1.0);
   }

   private double O(double var1) {
      double var3 = 1.0 - this.w(var1);
      return this.N + (this.c - this.N) * (1.0 - var3 * var3 * var3);
   }
}
