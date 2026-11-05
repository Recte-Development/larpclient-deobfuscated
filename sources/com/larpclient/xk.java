package com.larpclient;

public final class xk {
   private final f7 n;
   private int O;

   public xk(f7 var1) {
      this.n = var1;
      this.O = var1.ordinal();
   }

   public f7 E() {
      return f7.values()[this.O];
   }

   public f7 k() {
      return this.n;
   }

   public void e(int var1) {
      this.O = Math.clamp(var1, 0, f7.values().length - 1);
   }

   public void X(int var1) {
      this.e(this.O + Integer.signum(var1));
   }

   public f7 F() {
      return this.n;
   }

   public f7 C() {
      return this.E();
   }
}
