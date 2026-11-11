package com.larpclient.util.misc;

public final class ClickScheduler {
   private long nextClickNanos;

   public boolean isReady(long var1) {
      return var1 >= this.nextClickNanos;
   }

   public void reset() {
      this.nextClickNanos = 0L;
   }

   public void schedule(long var1, double var3, double var5, double var7) {
      this.nextClickNanos = var1 + intervalNanos(var3, var5, var7);
   }

   public static long intervalNanos(double var0, double var2, double var4) {
      double var6 = Math.clamp(var2, 1.0, 20.0);
      double var8 = Math.clamp(var0, 1.0, var6);
      double var10 = var8 + (var6 - var8) * Math.clamp(var4, 0.0, 1.0);
      return (long)Math.ceil(1.0E9 / var10);
   }
}
