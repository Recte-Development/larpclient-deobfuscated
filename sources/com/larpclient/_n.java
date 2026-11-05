package com.larpclient;

final class _n {
   private int X = -1;
   private int O = -1;
   private int m = 1;
   private boolean V;

   boolean c(int var1) {
      return !this.V && var1 >= this.m && (this.X < 0 || this.Z());
   }

   void j(int var1, int var2) {
      this.X = var1;
      this.m = var2 + 1;
   }

   void n(int var1) {
      this.O = Math.max(this.O, var1);
   }

   void z() {
      this.V = (boolean)1;
   }

   boolean N() {
      return this.X >= 0;
   }

   boolean Z() {
      return this.N() && (this.V || this.O >= this.X);
   }

   String m() {
      return "sequence=" + this.X + " acknowledged=" + this.O;
   }
}
