package com.larpclient;

public final class uk {
   private long R = -1L;

   public void x() {
      this.R = -1L;
   }

   public boolean l(boolean var1, boolean var2, long var3) {
      if (var1 && var2) {
         if (this.R < 0L) {
            this.R = var3;
         }

         return var3 - this.R >= 3L;
      } else {
         this.x();
         return false;
      }
   }
}
