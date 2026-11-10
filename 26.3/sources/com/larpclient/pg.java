package com.larpclient;

public final class pg {
   private long T = -1L;

   public void s() {
      this.T = -1L;
   }

   public boolean z(boolean var1, boolean var2, long var3) {
      if (var1 && var2) {
         if (this.T < 0L) {
            this.T = var3;
         }

         return var3 - this.T >= 3L;
      } else {
         this.s();
         return false;
      }
   }
}
