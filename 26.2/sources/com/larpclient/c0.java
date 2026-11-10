package com.larpclient;

public final class c0 {
   private Object G;
   private Object v;
   private boolean x;

   public boolean c(Object var1, Object var2, boolean var3) {
      int var4 = this.G == var1 && this.v == var2 && this.x == var3 ? 0 : 1;
      this.G = var1;
      this.v = var2;
      this.x = var3;
      return (boolean)var4;
   }
}
