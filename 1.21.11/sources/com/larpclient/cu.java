package com.larpclient;

public final class cu {
   private Object J;
   private Object m;
   private boolean A;

   public boolean u(Object var1, Object var2, boolean var3) {
      int var4 = this.J == var1 && this.m == var2 && this.A == var3 ? 0 : 1;
      this.J = var1;
      this.m = var2;
      this.A = var3;
      return (boolean)var4;
   }
}
