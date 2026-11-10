package com.larpclient;

final class p5 {
   private int H = -1;
   private int K = -1;
   private int A = 1;
   private boolean F;

   boolean n(int var1) {
      return !this.F && var1 >= this.A && (this.H < 0 || this.y());
   }

   void o(int var1, int var2) {
      this.H = var1;
      this.A = var2 + 1;
   }

   void x(int var1) {
      this.K = Math.max(this.K, var1);
   }

   void D() {
      this.F = (boolean)1;
   }

   boolean I() {
      return this.H >= 0;
   }

   boolean y() {
      return this.I() && (this.F || this.K >= this.H);
   }

   String G() {
      return "sequence=" + this.H + " acknowledged=" + this.K;
   }
}
