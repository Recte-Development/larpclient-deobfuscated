package com.larpclient;

public final class _l {
   private Object r;
   private long L;
   private long v;
   private long E = Long.MIN_VALUE;

   public boolean L(Object var1, long var2, int var4) {
      if (var1 == null) {
         this.m();
         return false;
      }

      if (var1 != this.r || var2 < this.v) {
         this.r = var1;
         this.L = var2;
         this.E = Long.MIN_VALUE;
      }

      this.v = var2;
      return var2 != this.E && var2 - this.L >= Math.max(0, var4);
   }

   public void X(Object var1, long var2) {
      if (var1 == this.r) {
         this.L = this.v = this.E = var2;
      }
   }

   public void m() {
      this.r = null;
      this.L = this.v = 0L;
      this.E = Long.MIN_VALUE;
   }
}
