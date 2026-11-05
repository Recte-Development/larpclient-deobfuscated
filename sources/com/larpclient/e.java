package com.larpclient;

final class e {
   static final int p = 32768;
   private Object A;
   private long x;
   private long D = T(0);
   private long n;
   private long W;
   private long N;
   private long I;
   private long t;
   private int B;
   private int u;

   static long T(int var0) {
      return Math.max(15000L, Math.min(60000L, 5000L + 6L * Math.max(0, var0)));
   }

   void f(int var1) {
      this.D = T(var1);
   }

   boolean O(long var1) {
      return System.nanoTime() - var1 > 4L * this.D * 1000000L;
   }

   boolean t() {
      return this.t(System.nanoTime());
   }

   boolean t(long var1) {
      return var1 - this.x > this.D * 1000000L;
   }

   void l(Object var1, int var2, long var3) {
      this.P(var1, var2, var2, var3);
   }

   void P(Object var1, int var2, int var3, long var4) {
      this.x = System.nanoTime();
      this.A = var1;
      this.B = var2;
      this.u = var3;
      this.n = var4;
      this.W = this.N = this.I = this.t = 0L;
   }

   void K(Object var1, long var2, boolean var4, int var5, boolean var6) {
      if (var1 == this.A && var2 > this.n) {
         if (var4) {
            this.W = var2;
         }

         if (var5 == this.B) {
            this.N = var2;
         }

         if (var5 == this.u) {
            this.t = var2;
         }

         if (var6) {
            this.I = var2;
         }
      }
   }

   boolean r() {
      return this.W > this.n || this.N > this.n && this.t > this.n && this.I > this.n;
   }

   boolean c() {
      return this.W > this.n;
   }

   boolean Q(boolean var1) {
      return this.r() && var1;
   }

   boolean l(boolean var1, boolean var2, boolean var3) {
      return this.Q(var1 && var2 && var3);
   }

   String c() {
      return "issued=" + this.n + " full=" + this.W + " slot=" + this.N + " other=" + this.t + " cursor=" + this.I;
   }
}
