package com.larpclient;

public final class ff {
   private long U;
   private long Y;
   private long F;
   private long n = -1L;
   private long i = -1L;
   private long W = -1L;
   private long v = -1L;

   public void p() {
      this.U++;
      this.Y = this.F = 0L;
   }

   public void u() {
      this.Y++;
   }

   public void H() {
      this.F++;
   }

   public boolean K() {
      return this.n == this.U && this.W == this.Y;
   }

   public boolean a() {
      return this.i == this.U && this.v == this.F;
   }

   public void J() {
      this.n = this.U;
      this.W = this.Y;
   }

   public void W() {
      this.i = this.U;
      this.v = this.F;
   }

   public void r() {
      this.n = this.i = -1L;
   }
}
