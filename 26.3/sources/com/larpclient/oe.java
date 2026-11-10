package com.larpclient;

final class oe {
   static final int x = 32768;
   private Object n;
   private long r;
   private long b = V(0);
   private long p;
   private long Y;
   private long T;
   private long z;
   private long s;
   private int d;
   private int F;

   static long V(int var0) {
      return Math.max(15000L, Math.min(60000L, 5000L + 6L * Math.max(0, var0)));
   }

   void o(int var1) {
      this.b = V(var1);
   }

   boolean B(long var1) {
      return System.nanoTime() - var1 > 4L * this.b * 1000000L;
   }

   boolean y() {
      return this.n(System.nanoTime());
   }

   boolean n(long var1) {
      return var1 - this.r > this.b * 1000000L;
   }

   void U(Object var1, int var2, long var3) {
      this.c(var1, var2, var2, var3);
   }

   void c(Object var1, int var2, int var3, long var4) {
      this.r = System.nanoTime();
      this.n = var1;
      this.d = var2;
      this.F = var3;
      this.p = var4;
      this.Y = this.T = this.z = this.s = 0L;
   }

   void X(Object var1, long var2, boolean var4, int var5, boolean var6) {
      if (var1 == this.n && var2 > this.p) {
         if (var4) {
            this.Y = var2;
         }

         if (var5 == this.d) {
            this.T = var2;
         }

         if (var5 == this.F) {
            this.s = var2;
         }

         if (var6) {
            this.z = var2;
         }
      }
   }

   boolean Z() {
      return this.Y > this.p || this.T > this.p && this.s > this.p && this.z > this.p;
   }

   boolean z() {
      return this.Y > this.p;
   }

   boolean Q(boolean var1) {
      return this.Z() && var1;
   }

   boolean q(boolean var1, boolean var2, boolean var3) {
      return this.Q(var1 && var2 && var3);
   }

   String E() {
      return "issued=" + this.p + " full=" + this.Y + " slot=" + this.T + " other=" + this.s + " cursor=" + this.z;
   }
}
