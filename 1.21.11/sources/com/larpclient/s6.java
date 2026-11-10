package com.larpclient;

public final class s6 {
   private final tl u;
   private int O;

   public s6(tl var1) {
      this.u = var1;
      this.O = var1.ordinal();
   }

   public tl P() {
      return tl.values()[this.O];
   }

   public tl T() {
      return this.u;
   }

   public void u(int var1) {
      this.O = Math.clamp(var1, 0, tl.values().length - 1);
   }

   public void T(int var1) {
      this.u(this.O + Integer.signum(var1));
   }

   public tl q() {
      return this.u;
   }

   public tl p() {
      return this.P();
   }
}
