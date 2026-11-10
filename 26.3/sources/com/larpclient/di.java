package com.larpclient;

public final class di {
   private Object W;
   private long E;
   private long U;
   private long a = Long.MIN_VALUE;

   public boolean m(Object var1, long var2, int var4) {
      if (var1 == null) {
         this.z();
         return false;
      }

      if (var1 != this.W || var2 < this.U) {
         this.W = var1;
         this.E = var2;
         this.a = Long.MIN_VALUE;
      }

      this.U = var2;
      return var2 != this.a && var2 - this.E >= Math.max(0, var4);
   }

   public void S(Object var1, long var2) {
      if (var1 == this.W) {
         this.E = this.U = this.a = var2;
      }
   }

   public void z() {
      this.W = null;
      this.E = this.U = 0L;
      this.a = Long.MIN_VALUE;
   }
}
