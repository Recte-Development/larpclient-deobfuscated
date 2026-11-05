package com.larpclient;

public final class zk {
   public static final int S = 120;
   private final double[] d = new double[120];
   private int x;
   private int n;
   private double L;
   private double f;
   private double T;
   private Object G;
   private Object g;
   private boolean r;

   public void g() {
      this.x = this.n = 0;
      this.L = 0.0;
      this.r = (boolean)0;
      this.G = this.g = null;
   }

   public void c() {
      this.r = (boolean)0;
   }

   public void O(Object var1, Object var2, double var3, double var5, double var7) {
      if (var1 != null && var2 != null && Double.isFinite(var3) && Double.isFinite(var5) && Double.isFinite(var7) && !(var7 <= 0.0)) {
         if (this.G != var1 || this.g != var2) {
            this.g();
            this.G = var1;
            this.g = var2;
         }

         if (!this.r) {
            this.f = var3;
            this.T = var5;
            this.r = (boolean)1;
         } else {
            double var9 = Math.hypot(var3 - this.f, var5 - this.T);
            this.f = var3;
            this.T = var5;
            if (var9 > 16.0) {
               this.x = this.n = 0;
               this.L = 0.0;
            } else {
               double var11 = var9 * var7;
               if (!Double.isFinite(var11)) {
                  this.g();
               } else {
                  if (this.n == 120) {
                     this.L = this.L - this.d[this.x];
                     this.d[this.x] = var11;
                     this.x = (this.x + 1) % 120;
                  } else {
                     this.d[(this.x + this.n++) % 120] = var11;
                  }

                  this.L += var11;
               }
            }
         }
      } else {
         this.g();
      }
   }

   public int y() {
      return this.n;
   }

   public double D(int var1) {
      if (var1 >= 0 && var1 < this.n) {
         return this.d[(this.x + var1) % 120];
      } else {
         throw new IndexOutOfBoundsException(var1);
      }
   }

   public double U() {
      return this.n == 0 ? 0.0 : this.D(this.n - 1);
   }

   public double T() {
      return this.n == 0 ? 0.0 : Math.max(0.0, this.L / this.n);
   }
}
