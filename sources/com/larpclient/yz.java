package com.larpclient;

public final class yz {
   private Object P;
   private long o;
   private long U;
   private long a = Long.MIN_VALUE;

   public boolean e(Object var1, long var2, int var4) {
      if (var1 == null) {
         this.K();
         return false;
      }

      if (var1 != this.P || var2 < this.U) {
         this.P = var1;
         this.o = var2;
         this.a = Long.MIN_VALUE;
      }

      this.U = var2;
      return var2 != this.a && var2 - this.o >= Math.max(0, var4);
   }

   public void P(Object var1, long var2) {
      if (var1 == this.P) {
         this.o = this.U = this.a = var2;
      }
   }

   public void K() {
      this.P = null;
      this.o = this.U = 0L;
      this.a = Long.MIN_VALUE;
   }
}
