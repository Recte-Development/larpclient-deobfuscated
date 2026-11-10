package com.larpclient;

public final class i_ {
   private Object F;
   private Object Z;
   private boolean A;

   public boolean d(Object var1, Object var2, boolean var3) {
      int var4 = this.F == var1 && this.Z == var2 && this.A == var3 ? 0 : 1;
      this.F = var1;
      this.Z = var2;
      this.A = var3;
      return (boolean)var4;
   }
}
