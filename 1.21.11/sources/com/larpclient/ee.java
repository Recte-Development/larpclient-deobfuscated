package com.larpclient;

final class ee {
   static final int t = 32768;
   private Object s;
   private long I;
   private long j = x(0);
   private long T;
   private long D;
   private long y;
   private long b;
   private long i;
   private int m;
   private int z;

   static long x(int var0) {
      return Math.max(15000L, Math.min(60000L, 5000L + 6L * Math.max(0, var0)));
   }

   void T(int var1) {
      this.j = x(var1);
   }

   boolean h(long var1) {
      return System.nanoTime() - var1 > 4L * this.j * 1000000L;
   }

   boolean m() {
      return this.g(System.nanoTime());
   }

   boolean g(long var1) {
      return var1 - this.I > this.j * 1000000L;
   }

   void Q(Object var1, int var2, long var3) {
      this.v(var1, var2, var2, var3);
   }

   void v(Object var1, int var2, int var3, long var4) {
      this.I = System.nanoTime();
      this.s = var1;
      this.m = var2;
      this.z = var3;
      this.T = var4;
      this.D = this.y = this.b = this.i = 0L;
   }

   void O(Object var1, long var2, boolean var4, int var5, boolean var6) {
      if (var1 == this.s && var2 > this.T) {
         if (var4) {
            this.D = var2;
         }

         if (var5 == this.m) {
            this.y = var2;
         }

         if (var5 == this.z) {
            this.i = var2;
         }

         if (var6) {
            this.b = var2;
         }
      }
   }

   boolean o() {
      return this.D > this.T || this.y > this.T && this.i > this.T && this.b > this.T;
   }

   boolean K() {
      return this.D > this.T;
   }

   boolean v(boolean var1) {
      return this.o() && var1;
   }

   boolean Z(boolean var1, boolean var2, boolean var3) {
      return this.v(var1 && var2 && var3);
   }

   String U() {
      return "issued=" + this.T + " full=" + this.D + " slot=" + this.y + " other=" + this.i + " cursor=" + this.b;
   }
}
