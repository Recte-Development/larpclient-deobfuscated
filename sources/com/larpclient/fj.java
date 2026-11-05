package com.larpclient;

public final class fj {
   private long l;

   public boolean j(long var1) {
      return var1 >= this.l;
   }

   public void K() {
      this.l = 0L;
   }

   public void M(long var1, double var3, double var5, double var7) {
      this.l = var1 + E(var3, var5, var7);
   }

   public static long E(double var0, double var2, double var4) {
      double var6 = Math.clamp(var2, 1.0, 20.0);
      double var8 = Math.clamp(var0, 1.0, var6);
      double var10 = var8 + (var6 - var8) * Math.clamp(var4, 0.0, 1.0);
      return (long)Math.ceil(1.0E9 / var10);
   }
}
