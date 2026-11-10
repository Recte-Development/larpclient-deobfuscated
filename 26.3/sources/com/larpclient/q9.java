package com.larpclient;

public final class q9 {
   public static final int O = 120;
   private final double[] S = new double[120];
   private int A;
   private int B;
   private double n;
   private double Q;
   private double r;
   private Object Y;
   private Object H;
   private boolean i;

   public void Q() {
      this.A = this.B = 0;
      this.n = 0.0;
      this.i = (boolean)0;
      this.Y = this.H = null;
   }

   public void R() {
      this.i = (boolean)0;
   }

   public void U(Object var1, Object var2, double var3, double var5, double var7) {
      if (var1 != null && var2 != null && Double.isFinite(var3) && Double.isFinite(var5) && Double.isFinite(var7) && !(var7 <= 0.0)) {
         if (this.Y != var1 || this.H != var2) {
            this.Q();
            this.Y = var1;
            this.H = var2;
         }

         if (!this.i) {
            this.Q = var3;
            this.r = var5;
            this.i = (boolean)1;
         } else {
            double var9 = Math.hypot(var3 - this.Q, var5 - this.r);
            this.Q = var3;
            this.r = var5;
            if (var9 > 16.0) {
               this.A = this.B = 0;
               this.n = 0.0;
            } else {
               double var11 = var9 * var7;
               if (!Double.isFinite(var11)) {
                  this.Q();
               } else {
                  if (this.B == 120) {
                     this.n = this.n - this.S[this.A];
                     this.S[this.A] = var11;
                     this.A = (this.A + 1) % 120;
                  } else {
                     this.S[(this.A + this.B++) % 120] = var11;
                  }

                  this.n += var11;
               }
            }
         }
      } else {
         this.Q();
      }
   }

   public int W() {
      return this.B;
   }

   public double b(int var1) {
      if (var1 >= 0 && var1 < this.B) {
         return this.S[(this.A + var1) % 120];
      } else {
         throw new IndexOutOfBoundsException(var1);
      }
   }

   public double d() {
      return this.B == 0 ? 0.0 : this.b(this.B - 1);
   }

   public double x() {
      return this.B == 0 ? 0.0 : Math.max(0.0, this.n / this.B);
   }
}
