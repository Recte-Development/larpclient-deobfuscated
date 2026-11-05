package com.larpclient;

public final class h6 {
   private long A = -1L;

   public void Q() {
      this.A = -1L;
   }

   public boolean x(boolean var1, boolean var2, long var3) {
      if (var1 && var2) {
         if (this.A < 0L) {
            this.A = var3;
         }

         return var3 - this.A >= 3L;
      } else {
         this.Q();
         return false;
      }
   }
}
