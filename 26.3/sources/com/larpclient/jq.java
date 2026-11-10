package com.larpclient;

public final class jq {
   private final String D;

   private jq(String var1) {
      this.D = var1 == null ? "Rig" : "Rig/" + var1;
   }

   public static jq d(String var0) {
      return new jq(var0);
   }

   public void N(String var1, Object... var2) {
      _q.c(this.D, var1, C(var2));
   }

   public void u(String var1, Object... var2) {
      _q.z(this.D, var1, C(var2));
   }

   public void A(String var1, Object... var2) {
      _q.R(this.D, var1, C(var2));
   }

   public void P(String var1, Object... var2) {
      _q.R(this.D, var1, var2);
   }

   private static Object[] C(Object[] var0) {
      return var0 == null ? new Object[0] : var0;
   }
}
