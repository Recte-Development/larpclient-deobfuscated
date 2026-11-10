package com.larpclient;

final class t0 {
   private int J = -1;
   private int u = -1;
   private int I = 1;
   private boolean H;

   boolean g(int var1) {
      return !this.H && var1 >= this.I && (this.J < 0 || this.S());
   }

   void p(int var1, int var2) {
      this.J = var1;
      this.I = var2 + 1;
   }

   void P(int var1) {
      this.u = Math.max(this.u, var1);
   }

   void I() {
      this.H = (boolean)1;
   }

   boolean x() {
      return this.J >= 0;
   }

   boolean S() {
      return this.x() && (this.H || this.u >= this.J);
   }

   String U() {
      return "sequence=" + this.J + " acknowledged=" + this.u;
   }
}
