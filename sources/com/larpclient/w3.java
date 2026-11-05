package com.larpclient;

final class w3 {
   static final int F = 32768;
   private Object C;
   private long U;
   private long l = l(0);
   private long r;
   private long j;
   private long V;
   private long g;
   private long o;
   private int P;
   private int d;

   static long l(int var0) {
      return Math.max(15000L, Math.min(60000L, 5000L + 6L * Math.max(0, var0)));
   }

   void z(int var1) {
      this.l = l(var1);
   }

   boolean Y(long var1) {
      return System.nanoTime() - var1 > 4L * this.l * 1000000L;
   }

   boolean g() {
      return this.S(System.nanoTime());
   }

   boolean S(long var1) {
      return var1 - this.U > this.l * 1000000L;
   }

   void O(Object var1, int var2, long var3) {
      this.L(var1, var2, var2, var3);
   }

   void L(Object var1, int var2, int var3, long var4) {
      this.U = System.nanoTime();
      this.C = var1;
      this.P = var2;
      this.d = var3;
      this.r = var4;
      this.j = this.V = this.g = this.o = 0L;
   }

   void z(Object var1, long var2, boolean var4, int var5, boolean var6) {
      if (var1 == this.C && var2 > this.r) {
         if (var4) {
            this.j = var2;
         }

         if (var5 == this.P) {
            this.V = var2;
         }

         if (var5 == this.d) {
            this.o = var2;
         }

         if (var6) {
            this.g = var2;
         }
      }
   }

   boolean N() {
      return this.j > this.r || this.V > this.r && this.o > this.r && this.g > this.r;
   }

   boolean d() {
      return this.j > this.r;
   }

   boolean M(boolean var1) {
      return this.N() && var1;
   }

   boolean p(boolean var1, boolean var2, boolean var3) {
      return this.M(var1 && var2 && var3);
   }

   String a() {
      return "issued=" + this.r + " full=" + this.j + " slot=" + this.V + " other=" + this.o + " cursor=" + this.g;
   }
}
