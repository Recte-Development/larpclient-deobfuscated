package com.larpclient;

public final class mw {
   public static final int o = 30;
   private mw.e q = mw.e.T;
   private boolean F;
   private long x;
   private long e;
   private long X = -1L;

   public mw.e q() {
      return this.q;
   }

   public boolean t() {
      return this.q != mw.e.T;
   }

   public boolean Z() {
      if (this.t()) {
         return false;
      }

      this.F = (boolean)1;
      return true;
   }

   public boolean D() {
      boolean var1 = this.F;
      this.F = (boolean)0;
      return var1;
   }

   public void s(long var1) {
      if (this.X < 0L) {
         this.X = var1;
      }
   }

   public boolean h() {
      return this.X >= 0L;
   }

   public boolean V(long var1) {
      return this.X >= 0L && var1 - this.X > 30L;
   }

   public void k() {
      this.X = -1L;
   }

   public void r(long var1, int var3) {
      if (this.t()) {
         throw new IllegalStateException("Pearl catch already active");
      }

      this.F = (boolean)0;
      this.X = -1L;
      this.q = mw.e.d;
      this.x = var1 + var3;
   }

   public void X(long var1, int var3) {
      if (this.q != mw.e.d) {
         throw new IllegalStateException("No pearl throw pending");
      }

      this.q = mw.e.J;
      this.e = var1;
      this.x = var1 + var3;
   }

   public void d(long var1, int var3) {
      if (this.q != mw.e.J) {
         throw new IllegalStateException("No wind charge pending");
      }

      this.x = var1 + var3;
   }

   public void j() {
      if (this.q != mw.e.J) {
         throw new IllegalStateException("No wind charge pending");
      }

      this.q = mw.e.T;
   }

   public long l(long var1) {
      return this.q == mw.e.J ? var1 - this.e : -1L;
   }

   public boolean J(long var1) {
      return this.t() && var1 - this.x >= 0L;
   }

   public void F() {
      this.q = mw.e.T;
      this.F = (boolean)0;
      this.X = -1L;
   }

   public enum e {
      T,
      d,
      J;
   }
}
