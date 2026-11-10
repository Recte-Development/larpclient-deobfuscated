package com.larpclient;

public final class cc {
   public static final int D = 30;
   private cc.u R = cc.u.x;
   private boolean y;
   private long c;
   private long k;
   private long A = -1L;

   public cc.u a() {
      return this.R;
   }

   public boolean E() {
      return this.R != cc.u.x;
   }

   public boolean u() {
      if (this.E()) {
         return false;
      }

      this.y = (boolean)1;
      return true;
   }

   public boolean L() {
      boolean var1 = this.y;
      this.y = (boolean)0;
      return var1;
   }

   public void x(long var1) {
      if (this.A < 0L) {
         this.A = var1;
      }
   }

   public boolean C() {
      return this.A >= 0L;
   }

   public boolean A(long var1) {
      return this.A >= 0L && var1 - this.A > 30L;
   }

   public void Z() {
      this.A = -1L;
   }

   public void k(long var1, int var3) {
      if (this.E()) {
         throw new IllegalStateException("Pearl catch already active");
      }

      this.y = (boolean)0;
      this.A = -1L;
      this.R = cc.u.E;
      this.c = var1 + var3;
   }

   public void L(long var1, int var3) {
      if (this.R != cc.u.E) {
         throw new IllegalStateException("No pearl throw pending");
      }

      this.R = cc.u.v;
      this.k = var1;
      this.c = var1 + var3;
   }

   public void F(long var1, int var3) {
      if (this.R != cc.u.v) {
         throw new IllegalStateException("No wind charge pending");
      }

      this.c = var1 + var3;
   }

   public void m() {
      if (this.R != cc.u.v) {
         throw new IllegalStateException("No wind charge pending");
      }

      this.R = cc.u.x;
   }

   public long z(long var1) {
      return this.R == cc.u.v ? var1 - this.k : -1L;
   }

   public boolean y(long var1) {
      return this.E() && var1 - this.c >= 0L;
   }

   public void v() {
      this.R = cc.u.x;
      this.y = (boolean)0;
      this.A = -1L;
   }

   public enum u {
      x,
      E,
      v;
   }
}
