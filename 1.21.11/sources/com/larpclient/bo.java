package com.larpclient;

final class bo {
   private int C = -1;
   private int N = -1;
   private int B = 1;
   private boolean r;

   boolean I(int var1) {
      return !this.r && var1 >= this.B && (this.C < 0 || this.d());
   }

   void L(int var1, int var2) {
      this.C = var1;
      this.B = var2 + 1;
   }

   void L(int var1) {
      this.N = Math.max(this.N, var1);
   }

   void v() {
      this.r = (boolean)1;
   }

   boolean V() {
      return this.C >= 0;
   }

   boolean d() {
      return this.V() && (this.r || this.N >= this.C);
   }

   String E() {
      return "sequence=" + this.C + " acknowledged=" + this.N;
   }
}
