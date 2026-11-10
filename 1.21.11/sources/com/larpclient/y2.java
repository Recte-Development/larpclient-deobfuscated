package com.larpclient;

public final class y2 {
   private Object z;
   private long U;
   private long l;
   private long h = Long.MIN_VALUE;

   public boolean n(Object var1, long var2, int var4) {
      if (var1 == null) {
         this.N();
         return false;
      }

      if (var1 != this.z || var2 < this.l) {
         this.z = var1;
         this.U = var2;
         this.h = Long.MIN_VALUE;
      }

      this.l = var2;
      return var2 != this.h && var2 - this.U >= Math.max(0, var4);
   }

   public void F(Object var1, long var2) {
      if (var1 == this.z) {
         this.U = this.l = this.h = var2;
      }
   }

   public void N() {
      this.z = null;
      this.U = this.l = 0L;
      this.h = Long.MIN_VALUE;
   }
}
