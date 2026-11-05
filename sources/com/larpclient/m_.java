package com.larpclient;

public final class m_ {
   private long d;
   private long H;
   private long K;
   private long s = -1L;
   private long N = -1L;
   private long M = -1L;
   private long y = -1L;

   public void w() {
      this.d++;
      this.H = this.K = 0L;
   }

   public void F() {
      this.H++;
   }

   public void m() {
      this.K++;
   }

   public boolean Y() {
      return this.s == this.d && this.M == this.H;
   }

   public boolean N() {
      return this.N == this.d && this.y == this.K;
   }

   public void E() {
      this.s = this.d;
      this.M = this.H;
   }

   public void l() {
      this.N = this.d;
      this.y = this.K;
   }

   public void i() {
      this.s = this.N = -1L;
   }
}
