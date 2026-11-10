package com.larpclient;

public final class jt {
   private Object Y;
   private long U;
   private long L;
   private long O = Long.MIN_VALUE;

   public boolean x(Object var1, long var2, int var4) {
      if (var1 == null) {
         this.U();
         return false;
      }

      if (var1 != this.Y || var2 < this.L) {
         this.Y = var1;
         this.U = var2;
         this.O = Long.MIN_VALUE;
      }

      this.L = var2;
      return var2 != this.O && var2 - this.U >= Math.max(0, var4);
   }

   public void b(Object var1, long var2) {
      if (var1 == this.Y) {
         this.U = this.L = this.O = var2;
      }
   }

   public void U() {
      this.Y = null;
      this.U = this.L = 0L;
      this.O = Long.MIN_VALUE;
   }
}
