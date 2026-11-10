package com.larpclient;

public final class d8 {
   public static final int d = 30;
   private d8.g r = d8.g.E;
   private boolean g;
   private long a;
   private long D;
   private long L = -1L;

   public d8.g A() {
      return this.r;
   }

   public boolean H() {
      return this.r != d8.g.E;
   }

   public boolean P() {
      if (this.H()) {
         return false;
      }

      this.g = (boolean)1;
      return true;
   }

   public boolean c() {
      boolean var1 = this.g;
      this.g = (boolean)0;
      return var1;
   }

   public void f(long var1) {
      if (this.L < 0L) {
         this.L = var1;
      }
   }

   public boolean Z() {
      return this.L >= 0L;
   }

   public boolean v(long var1) {
      return this.L >= 0L && var1 - this.L > 30L;
   }

   public void L() {
      this.L = -1L;
   }

   public void G(long var1, int var3) {
      if (this.H()) {
         throw new IllegalStateException("Pearl catch already active");
      }

      this.g = (boolean)0;
      this.L = -1L;
      this.r = d8.g.q;
      this.a = var1 + var3;
   }

   public void M(long var1, int var3) {
      if (this.r != d8.g.q) {
         throw new IllegalStateException("No pearl throw pending");
      }

      this.r = d8.g.w;
      this.D = var1;
      this.a = var1 + var3;
   }

   public void o(long var1, int var3) {
      if (this.r != d8.g.w) {
         throw new IllegalStateException("No wind charge pending");
      }

      this.a = var1 + var3;
   }

   public void v() {
      if (this.r != d8.g.w) {
         throw new IllegalStateException("No wind charge pending");
      }

      this.r = d8.g.E;
   }

   public long H(long var1) {
      return this.r == d8.g.w ? var1 - this.D : -1L;
   }

   public boolean n(long var1) {
      return this.H() && var1 - this.a >= 0L;
   }

   public void i() {
      this.r = d8.g.E;
      this.g = (boolean)0;
      this.L = -1L;
   }

   public enum g {
      E,
      q,
      w;
   }
}
