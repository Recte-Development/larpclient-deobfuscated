package com.larpclient;

public final class kt {
   public static final int k = 240;
   private long m = -1L;
   private double Y;
   private boolean t;
   private boolean Z;
   private mb.d R = mb.d.REST;

   public mb.d V() {
      return this.R;
   }

   public double v() {
      return this.Y;
   }

   public boolean n() {
      return this.Z;
   }

   public mb.d h(long var1, boolean var3, boolean var4) {
      if (this.Z) {
         return this.R;
      }

      if (!var4) {
         this.O();
         return this.R;
      }

      if (var3 && this.t && this.m >= 0L && var1 >= this.m && var1 - this.m <= 500000000L) {
         this.Y = this.Y + (var1 - this.m) / 1000000.0;
      }

      this.m = var1;
      this.t = var3;
      if (this.Y >= 240.0) {
         this.O();
         return this.R;
      }

      if (var3) {
         double var5 = jp.m.A.s(this.Y / 240.0);
         this.R = new mb.d(0.965 + 0.035 * var5, 7.0 * (1.0 - var5));
      }

      return this.R;
   }

   public void O() {
      this.Z = (boolean)1;
      this.Y = 240.0;
      this.R = mb.d.REST;
   }
}
