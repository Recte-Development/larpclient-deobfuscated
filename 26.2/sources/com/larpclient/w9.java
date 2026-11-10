package com.larpclient;

final class w9 {
   static final int D = 32768;
   private Object q;
   private long P;
   private long i = N(0);
   private long Q;
   private long X;
   private long K;
   private long n;
   private long j;
   private int x;
   private int o;

   static long N(int var0) {
      return Math.max(15000L, Math.min(60000L, 5000L + 6L * Math.max(0, var0)));
   }

   void Y(int var1) {
      this.i = N(var1);
   }

   boolean U(long var1) {
      return System.nanoTime() - var1 > 4L * this.i * 1000000L;
   }

   boolean E() {
      return this.b(System.nanoTime());
   }

   boolean b(long var1) {
      return var1 - this.P > this.i * 1000000L;
   }

   void p(Object var1, int var2, long var3) {
      this.W(var1, var2, var2, var3);
   }

   void W(Object var1, int var2, int var3, long var4) {
      this.P = System.nanoTime();
      this.q = var1;
      this.x = var2;
      this.o = var3;
      this.Q = var4;
      this.X = this.K = this.n = this.j = 0L;
   }

   void C(Object var1, long var2, boolean var4, int var5, boolean var6) {
      if (var1 == this.q && var2 > this.Q) {
         if (var4) {
            this.X = var2;
         }

         if (var5 == this.x) {
            this.K = var2;
         }

         if (var5 == this.o) {
            this.j = var2;
         }

         if (var6) {
            this.n = var2;
         }
      }
   }

   boolean J() {
      return this.X > this.Q || this.K > this.Q && this.j > this.Q && this.n > this.Q;
   }

   boolean N() {
      return this.X > this.Q;
   }

   boolean J(boolean var1) {
      return this.J() && var1;
   }

   boolean I(boolean var1, boolean var2, boolean var3) {
      return this.J(var1 && var2 && var3);
   }

   String y() {
      return "issued=" + this.Q + " full=" + this.X + " slot=" + this.K + " other=" + this.j + " cursor=" + this.n;
   }
}
