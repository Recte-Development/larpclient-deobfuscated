package com.larpclient;

public final class wz {
   private final String a;

   private wz(String var1) {
      this.a = var1 == null ? "Rig" : "Rig/" + var1;
   }

   public static wz A(String var0) {
      return new wz(var0);
   }

   public void K(String var1, Object... var2) {
      q8.v(this.a, var1, u(var2));
   }

   public void B(String var1, Object... var2) {
      q8.G(this.a, var1, u(var2));
   }

   public void S(String var1, Object... var2) {
      q8.L(this.a, var1, u(var2));
   }

   public void M(String var1, Object... var2) {
      q8.L(this.a, var1, var2);
   }

   private static Object[] u(Object[] var0) {
      return var0 == null ? new Object[0] : var0;
   }
}
