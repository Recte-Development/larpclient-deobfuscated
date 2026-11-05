package com.larpclient;

public final class y7 {
   public static final int q = 30;
   private y7.c s;
   private boolean N;
   private long y;
   private long A;
   private long k;

   public y7() {
      this.s = y7.c.s;
      this.k = -1L;
   }

   public y7.c r() {
      return this.s;
   }

   public boolean R() {
      return this.s != y7.c.s;
   }

   public boolean W() {
      if (this.R()) {
         return false;
      }

      this.N = (boolean)1;
      return true;
   }

   public boolean U() {
      boolean var1 = this.N;
      this.N = (boolean)0;
      return var1;
   }

   public void J(long var1) {
      if (this.k < 0L) {
         this.k = var1;
      }
   }

   public boolean e() {
      return this.k >= 0L;
   }

   public boolean v(long var1) {
      return this.k >= 0L && var1 - this.k > 30L;
   }

   public void h() {
      this.k = -1L;
   }

   public void W(long var1, int var3) {
      if (this.R()) {
         throw new IllegalStateException("Pearl catch already active");
      }

      this.N = (boolean)0;
      this.k = -1L;
      this.s = y7.c.M;
      this.y = var1 + var3;
   }

   public void J(long var1, int var3) {
      if (this.s != y7.c.M) {
         throw new IllegalStateException("No pearl throw pending");
      }

      this.s = y7.c.i;
      this.A = var1;
      this.y = var1 + var3;
   }

   public void R(long var1, int var3) {
      if (this.s != y7.c.i) {
         throw new IllegalStateException("No wind charge pending");
      }

      this.y = var1 + var3;
   }

   public void U() {
      if (this.s != y7.c.i) {
         throw new IllegalStateException("No wind charge pending");
      }

      this.s = y7.c.s;
   }

   public long q(long var1) {
      return this.s == y7.c.i ? var1 - this.A : -1L;
   }

   public boolean x(long var1) {
      return this.R() && var1 - this.y >= 0L;
   }

   public void O() {
      this.s = y7.c.s;
      this.N = (boolean)0;
      this.k = -1L;
   }

   public enum c {
      s,
      M,
      i;
   }
}
