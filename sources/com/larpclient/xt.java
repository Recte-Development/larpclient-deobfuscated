package com.larpclient;

public final class xt {
   public static final int b = 240;
   private long V = -1L;
   private double n;
   private boolean p;
   private boolean k;
   private m8.w I = m8.w.REST;

   public m8.w j() {
      return this.I;
   }

   public double w() {
      return this.n;
   }

   public boolean W() {
      return this.k;
   }

   public m8.w s(long var1, boolean var3, boolean var4) {
      if (this.k) {
         return this.I;
      }

      if (!var4) {
         this.r();
         return this.I;
      }

      if (var3 && this.p && this.V >= 0L && var1 >= this.V && var1 - this.V <= 500000000L) {
         this.n = this.n + (var1 - this.V) / 1000000.0;
      }

      this.V = var1;
      this.p = var3;
      if (this.n >= 240.0) {
         this.r();
         return this.I;
      }

      if (var3) {
         double var5 = ef.q.N.k(this.n / 240.0);
         this.I = new m8.w(0.965 + 0.035 * var5, 7.0 * (1.0 - var5));
      }

      return this.I;
   }

   public void r() {
      this.k = (boolean)1;
      this.n = 240.0;
      this.I = m8.w.REST;
   }
}
