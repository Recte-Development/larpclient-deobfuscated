package com.larpclient;

public final class zw {
   private final String Y;

   private zw(String var1) {
      this.Y = var1 == null ? "Rig" : "Rig/" + var1;
   }

   public static zw k(String var0) {
      return new zw(var0);
   }

   public void t(String var1, Object... var2) {
      h.U(this.Y, var1, h(var2));
   }

   public void H(String var1, Object... var2) {
      h.A(this.Y, var1, h(var2));
   }

   public void z(String var1, Object... var2) {
      h.g(this.Y, var1, h(var2));
   }

   public void N(String var1, Object... var2) {
      h.g(this.Y, var1, var2);
   }

   private static Object[] h(Object[] var0) {
      return var0 == null ? new Object[0] : var0;
   }
}
