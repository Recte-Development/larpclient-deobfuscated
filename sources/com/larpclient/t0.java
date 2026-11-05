package com.larpclient;

public final class t0 {
   private final String n;

   private t0(String var1) {
      this.n = var1 == null ? "Rig" : "Rig/" + var1;
   }

   public static t0 R(String var0) {
      return new t0(var0);
   }

   public void a(String var1, Object... var2) {
      mt.U(this.n, var1, G(var2));
   }

   public void x(String var1, Object... var2) {
      mt.K(this.n, var1, G(var2));
   }

   public void D(String var1, Object... var2) {
      mt.T(this.n, var1, G(var2));
   }

   public void M(String var1, Object... var2) {
      mt.T(this.n, var1, var2);
   }

   private static Object[] G(Object[] var0) {
      return var0 == null ? new Object[0] : var0;
   }
}
