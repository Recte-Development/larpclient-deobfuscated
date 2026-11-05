package com.larpclient;

public final class p6 {
   private final String k;

   private p6(String var1) {
      this.k = var1 == null ? "Rig" : "Rig/" + var1;
   }

   public static p6 r(String var0) {
      return new p6(var0);
   }

   public void h(String var1, Object... var2) {
      r4.g(this.k, var1, R(var2));
   }

   public void W(String var1, Object... var2) {
      r4.V(this.k, var1, R(var2));
   }

   public void L(String var1, Object... var2) {
      r4.m(this.k, var1, R(var2));
   }

   public void C(String var1, Object... var2) {
      r4.m(this.k, var1, var2);
   }

   private static Object[] R(Object[] var0) {
      return var0 == null ? new Object[0] : var0;
   }
}
