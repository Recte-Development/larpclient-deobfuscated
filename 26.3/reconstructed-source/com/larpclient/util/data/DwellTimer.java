package com.larpclient.util.data;

public final class DwellTimer {
   private Object target;
   private long startTime;
   private long lastTime;
   private long firedTime = Long.MIN_VALUE;

   public boolean isReady(Object var1, long var2, int var4) {
      if (var1 == null) {
         this.reset();
         return false;
      }

      if (var1 != this.target || var2 < this.lastTime) {
         this.target = var1;
         this.startTime = var2;
         this.firedTime = Long.MIN_VALUE;
      }

      this.lastTime = var2;
      return var2 != this.firedTime && var2 - this.startTime >= Math.max(0, var4);
   }

   public void markFired(Object var1, long var2) {
      if (var1 == this.target) {
         this.startTime = this.lastTime = this.firedTime = var2;
      }
   }

   public void reset() {
      this.target = null;
      this.startTime = this.lastTime = 0L;
      this.firedTime = Long.MIN_VALUE;
   }
}
