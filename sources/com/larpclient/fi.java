package com.larpclient;

public final class fi {
   public static final int a = 120;
   private final double[] w = new double[120];
   private int t;
   private int E;
   private double G;
   private double j;
   private double e;
   private Object f;
   private Object m;
   private boolean T;

   public void F() {
      this.t = this.E = 0;
      this.G = 0.0;
      this.T = (boolean)0;
      this.f = this.m = null;
   }

   public void P() {
      this.T = (boolean)0;
   }

   public void A(Object var1, Object var2, double var3, double var5, double var7) {
      if (var1 != null && var2 != null && Double.isFinite(var3) && Double.isFinite(var5) && Double.isFinite(var7) && !(var7 <= 0.0)) {
         if (this.f != var1 || this.m != var2) {
            this.F();
            this.f = var1;
            this.m = var2;
         }

         if (!this.T) {
            this.j = var3;
            this.e = var5;
            this.T = (boolean)1;
         } else {
            double var9 = Math.hypot(var3 - this.j, var5 - this.e);
            this.j = var3;
            this.e = var5;
            if (var9 > 16.0) {
               this.t = this.E = 0;
               this.G = 0.0;
            } else {
               double var11 = var9 * var7;
               if (!Double.isFinite(var11)) {
                  this.F();
               } else {
                  if (this.E == 120) {
                     this.G = this.G - this.w[this.t];
                     this.w[this.t] = var11;
                     this.t = (this.t + 1) % 120;
                  } else {
                     this.w[(this.t + this.E++) % 120] = var11;
                  }

                  this.G += var11;
               }
            }
         }
      } else {
         this.F();
      }
   }

   public int D() {
      return this.E;
   }

   public double h(int var1) {
      if (var1 >= 0 && var1 < this.E) {
         return this.w[(this.t + var1) % 120];
      } else {
         throw new IndexOutOfBoundsException(var1);
      }
   }

   public double J() {
      return this.E == 0 ? 0.0 : this.h(this.E - 1);
   }

   public double b() {
      return this.E == 0 ? 0.0 : Math.max(0.0, this.G / this.E);
   }
}
