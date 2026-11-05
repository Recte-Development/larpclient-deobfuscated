package com.larpclient;

final class lh {
   private int n = -1;
   private int G = -1;
   private int F = 1;
   private boolean w;

   boolean u(int var1) {
      return !this.w && var1 >= this.F && (this.n < 0 || this.p());
   }

   void s(int var1, int var2) {
      this.n = var1;
      this.F = var2 + 1;
   }

   void w(int var1) {
      this.G = Math.max(this.G, var1);
   }

   void d() {
      this.w = (boolean)1;
   }

   boolean I() {
      return this.n >= 0;
   }

   boolean p() {
      return this.I() && (this.w || this.G >= this.n);
   }

   String E() {
      return "sequence=" + this.n + " acknowledged=" + this.G;
   }
}
