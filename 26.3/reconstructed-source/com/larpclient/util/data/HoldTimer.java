package com.larpclient.util.data;

public final class HoldTimer {
   private long startTime = -1L;

   public void reset() {
      this.startTime = -1L;
   }

   public boolean isHeld(boolean var1, boolean var2, long var3) {
      if (var1 && var2) {
         if (this.startTime < 0L) {
            this.startTime = var3;
         }

         return var3 - this.startTime >= 3L;
      } else {
         this.reset();
         return false;
      }
   }
}
