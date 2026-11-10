package com.larpclient;

public final class pd {
   public static final int J = 120;
   private final double[] M = new double[120];
   private int K;
   private int y;
   private double L;
   private double S;
   private double o;
   private Object l;
   private Object j;
   private boolean q;

   public void b() {
      this.K = this.y = 0;
      this.L = 0.0;
      this.q = (boolean)0;
      this.l = this.j = null;
   }

   public void E() {
      this.q = (boolean)0;
   }

   public void W(Object var1, Object var2, double var3, double var5, double var7) {
      if (var1 != null && var2 != null && Double.isFinite(var3) && Double.isFinite(var5) && Double.isFinite(var7) && !(var7 <= 0.0)) {
         if (this.l != var1 || this.j != var2) {
            this.b();
            this.l = var1;
            this.j = var2;
         }

         if (!this.q) {
            this.S = var3;
            this.o = var5;
            this.q = (boolean)1;
         } else {
            double var9 = Math.hypot(var3 - this.S, var5 - this.o);
            this.S = var3;
            this.o = var5;
            if (var9 > 16.0) {
               this.K = this.y = 0;
               this.L = 0.0;
            } else {
               double var11 = var9 * var7;
               if (!Double.isFinite(var11)) {
                  this.b();
               } else {
                  if (this.y == 120) {
                     this.L = this.L - this.M[this.K];
                     this.M[this.K] = var11;
                     this.K = (this.K + 1) % 120;
                  } else {
                     this.M[(this.K + this.y++) % 120] = var11;
                  }

                  this.L += var11;
               }
            }
         }
      } else {
         this.b();
      }
   }

   public int Y() {
      return this.y;
   }

   public double K(int var1) {
      if (var1 >= 0 && var1 < this.y) {
         return this.M[(this.K + var1) % 120];
      } else {
         throw new IndexOutOfBoundsException(var1);
      }
   }

   public double O() {
      return this.y == 0 ? 0.0 : this.K(this.y - 1);
   }

   public double j() {
      return this.y == 0 ? 0.0 : Math.max(0.0, this.L / this.y);
   }
}
