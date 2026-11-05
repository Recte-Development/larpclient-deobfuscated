package com.larpclient;

public final class xg {
   private Object w;
   private Object y;
   private boolean t;

   public boolean h(Object var1, Object var2, boolean var3) {
      int var4 = this.w == var1 && this.y == var2 && this.t == var3 ? 0 : 1;
      this.w = var1;
      this.y = var2;
      this.t = var3;
      return (boolean)var4;
   }
}
