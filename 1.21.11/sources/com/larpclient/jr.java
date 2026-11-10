package com.larpclient;

public final class jr {
   public static final int W = 240;
   private long I = -1L;
   private double B;
   private boolean m;
   private boolean V;
   private ix.g u = ix.g.REST;

   public ix.g V() {
      return this.u;
   }

   public double K() {
      return this.B;
   }

   public boolean m() {
      return this.V;
   }

   public ix.g U(long var1, boolean var3, boolean var4) {
      if (this.V) {
         return this.u;
      }

      if (!var4) {
         this.w();
         return this.u;
      }

      if (var3 && this.m && this.I >= 0L && var1 >= this.I && var1 - this.I <= 500000000L) {
         this.B = this.B + (var1 - this.I) / 1000000.0;
      }

      this.I = var1;
      this.m = var3;
      if (this.B >= 240.0) {
         this.w();
         return this.u;
      }

      if (var3) {
         double var5 = qb.y.y.f(this.B / 240.0);
         this.u = new ix.g(0.965 + 0.035 * var5, 7.0 * (1.0 - var5));
      }

      return this.u;
   }

   public void w() {
      this.V = (boolean)1;
      this.B = 240.0;
      this.u = ix.g.REST;
   }
}
