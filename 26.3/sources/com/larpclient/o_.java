package com.larpclient;

public final class o_ {
   private final pz M;
   private int c;

   public o_(pz var1) {
      this.M = var1;
      this.c = var1.ordinal();
   }

   public pz m() {
      return pz.values()[this.c];
   }

   public pz G() {
      return this.M;
   }

   public void I(int var1) {
      this.c = Math.clamp(var1, 0, pz.values().length - 1);
   }

   public void G(int var1) {
      this.I(this.c + Integer.signum(var1));
   }

   public pz k() {
      return this.M;
   }

   public pz K() {
      return this.m();
   }
}
