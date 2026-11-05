package com.larpclient;

public final class lp {
   private long D = -1L;

   public void o() {
      this.D = -1L;
   }

   public boolean e(boolean var1, boolean var2, long var3) {
      if (var1 && var2) {
         if (this.D < 0L) {
            this.D = var3;
         }

         return var3 - this.D >= 3L;
      } else {
         this.o();
         return false;
      }
   }
}
