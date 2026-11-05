package com.larpclient;

public final class y0 {
   public static final int V = 240;
   private long J = -1L;
   private double R;
   private boolean a;
   private boolean P;
   private r3.h k = r3.h.REST;

   public r3.h Z() {
      return this.k;
   }

   public double Y() {
      return this.R;
   }

   public boolean W() {
      return this.P;
   }

   public r3.h p(long var1, boolean var3, boolean var4) {
      if (this.P) {
         return this.k;
      }

      if (!var4) {
         this.z();
         return this.k;
      }

      if (var3 && this.a && this.J >= 0L && var1 >= this.J && var1 - this.J <= 500000000L) {
         this.R = this.R + (var1 - this.J) / 1000000.0;
      }

      this.J = var1;
      this.a = var3;
      if (this.R >= 240.0) {
         this.z();
         return this.k;
      }

      if (var3) {
         double var5 = u7.p.Y.m(this.R / 240.0);
         this.k = new r3.h(0.965 + 0.035 * var5, 7.0 * (1.0 - var5));
      }

      return this.k;
   }

   public void z() {
      this.P = (boolean)1;
      this.R = 240.0;
      this.k = r3.h.REST;
   }
}
