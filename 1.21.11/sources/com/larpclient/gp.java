package com.larpclient;

public final class gp {
   private long z;
   private long d;
   private long j;
   private long s = -1L;
   private long K = -1L;
   private long Y = -1L;
   private long N = -1L;

   public void p() {
      this.z++;
      this.d = this.j = 0L;
   }

   public void N() {
      this.d++;
   }

   public void Q() {
      this.j++;
   }

   public boolean p() {
      return this.s == this.z && this.Y == this.d;
   }

   public boolean Q() {
      return this.K == this.z && this.N == this.j;
   }

   public void H() {
      this.s = this.z;
      this.Y = this.d;
   }

   public void L() {
      this.K = this.z;
      this.N = this.j;
   }

   public void D() {
      this.s = this.K = -1L;
   }
}
