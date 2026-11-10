package com.larpclient;

public final class q {
   public static final int L = 30;
   private q.i F = q.i.V;
   private boolean P;
   private long j;
   private long G;
   private long U = -1L;

   public q.i W() {
      return this.F;
   }

   public boolean I() {
      return this.F != q.i.V;
   }

   public boolean n() {
      if (this.I()) {
         return false;
      }

      this.P = (boolean)1;
      return true;
   }

   public boolean a() {
      boolean var1 = this.P;
      this.P = (boolean)0;
      return var1;
   }

   public void G(long var1) {
      if (this.U < 0L) {
         this.U = var1;
      }
   }

   public boolean V() {
      return this.U >= 0L;
   }

   public boolean G(long var1) {
      return this.U >= 0L && var1 - this.U > 30L;
   }

   public void e() {
      this.U = -1L;
   }

   public void r(long var1, int var3) {
      if (this.I()) {
         throw new IllegalStateException("Pearl catch already active");
      }

      this.P = (boolean)0;
      this.U = -1L;
      this.F = q.i.O;
      this.j = var1 + var3;
   }

   public void n(long var1, int var3) {
      if (this.F != q.i.O) {
         throw new IllegalStateException("No pearl throw pending");
      }

      this.F = q.i.F;
      this.G = var1;
      this.j = var1 + var3;
   }

   public void D(long var1, int var3) {
      if (this.F != q.i.F) {
         throw new IllegalStateException("No wind charge pending");
      }

      this.j = var1 + var3;
   }

   public void b() {
      if (this.F != q.i.F) {
         throw new IllegalStateException("No wind charge pending");
      }

      this.F = q.i.V;
   }

   public long R(long var1) {
      return this.F == q.i.F ? var1 - this.G : -1L;
   }

   public boolean f(long var1) {
      return this.I() && var1 - this.j >= 0L;
   }

   public void T() {
      this.F = q.i.V;
      this.P = (boolean)0;
      this.U = -1L;
   }

   public enum i {
      V,
      O,
      F;
   }
}
