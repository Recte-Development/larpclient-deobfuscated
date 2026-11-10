package com.larpclient;

public final class mq {
   private final t2 f;
   private int b;

   public mq(t2 var1) {
      this.f = var1;
      this.b = var1.ordinal();
   }

   public t2 u() {
      return t2.values()[this.b];
   }

   public t2 D() {
      return this.f;
   }

   public void p(int var1) {
      this.b = Math.clamp(var1, 0, t2.values().length - 1);
   }

   public void m(int var1) {
      this.p(this.b + Integer.signum(var1));
   }

   public t2 v() {
      return this.f;
   }

   public t2 s() {
      return this.u();
   }
}
