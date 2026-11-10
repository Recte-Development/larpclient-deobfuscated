package com.larpclient;

public final class sq {
   private long E;

   public boolean L(long var1) {
      return var1 >= this.E;
   }

   public void p() {
      this.E = 0L;
   }

   public void X(long var1, double var3, double var5, double var7) {
      this.E = var1 + s(var3, var5, var7);
   }

   public static long s(double var0, double var2, double var4) {
      double var6 = Math.clamp(var2, 1.0, 20.0);
      double var8 = Math.clamp(var0, 1.0, var6);
      double var10 = var8 + (var6 - var8) * Math.clamp(var4, 0.0, 1.0);
      return (long)Math.ceil(1.0E9 / var10);
   }
}
