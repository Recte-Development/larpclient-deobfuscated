package com.larpclient;

public final class j2 {
   private Object J;
   private Object S;
   private boolean H;

   public boolean O(Object var1, Object var2, boolean var3) {
      int var4 = this.J == var1 && this.S == var2 && this.H == var3 ? 0 : 1;
      this.J = var1;
      this.S = var2;
      this.H = var3;
      return (boolean)var4;
   }
}
