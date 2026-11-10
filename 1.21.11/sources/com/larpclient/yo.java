package com.larpclient;

public final class yo {
   private long M = -1L;

   public void o() {
      this.M = -1L;
   }

   public boolean n(boolean var1, boolean var2, long var3) {
      if (var1 && var2) {
         if (this.M < 0L) {
            this.M = var3;
         }

         return var3 - this.M >= 3L;
      } else {
         this.o();
         return false;
      }
   }
}
