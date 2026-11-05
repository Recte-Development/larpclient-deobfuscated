package com.larpclient;

public final class kw {
   private long s;
   private long z;
   private long E;
   private long N = -1L;
   private long f = -1L;
   private long Z = -1L;
   private long n = -1L;

   public void y() {
      this.s++;
      this.z = this.E = 0L;
   }

   public void F() {
      this.z++;
   }

   public void S() {
      this.E++;
   }

   public boolean F() {
      return this.N == this.s && this.Z == this.z;
   }

   public boolean A() {
      return this.f == this.s && this.n == this.E;
   }

   public void j() {
      this.N = this.s;
      this.Z = this.z;
   }

   public void N() {
      this.f = this.s;
      this.n = this.E;
   }

   public void V() {
      this.N = this.f = -1L;
   }
}
