package com.larpclient;

public final class dc {
   public static final int U = 240;
   private long j = -1L;
   private double T;
   private boolean m;
   private boolean J;
   private fr.z t = fr.z.REST;

   public fr.z h() {
      return this.t;
   }

   public double c() {
      return this.T;
   }

   public boolean t() {
      return this.J;
   }

   public fr.z A(long var1, boolean var3, boolean var4) {
      if (this.J) {
         return this.t;
      }

      if (!var4) {
         this.C();
         return this.t;
      }

      if (var3 && this.m && this.j >= 0L && var1 >= this.j && var1 - this.j <= 500000000L) {
         this.T = this.T + (var1 - this.j) / 1000000.0;
      }

      this.j = var1;
      this.m = var3;
      if (this.T >= 240.0) {
         this.C();
         return this.t;
      }

      if (var3) {
         double var5 = fq.y.j.q(this.T / 240.0);
         this.t = new fr.z(0.965 + 0.035 * var5, 7.0 * (1.0 - var5));
      }

      return this.t;
   }

   public void C() {
      this.J = (boolean)1;
      this.T = 240.0;
      this.t = fr.z.REST;
   }
}
