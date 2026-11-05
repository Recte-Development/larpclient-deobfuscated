package com.larpclient;

public final class uc<T> {
   private Object G;
   private long S;
   private int E;
   private T l;
   private boolean J;

   public void u(Object var1, long var2, int var4, T var5) {
      if (!this.I(var1, var2, var4)) {
         this.G = var1;
         this.S = var2;
         this.E = var4;
         this.l = (T)var5;
         this.J = (boolean)0;
      }
   }

   public boolean I(Object var1, long var2, int var4) {
      return this.l != null && this.G == var1 && this.S == var2 && this.E == var4;
   }

   public void O(Object var1, long var2, int var4, boolean var5) {
      if (!var5 || !this.I(var1, var2, var4)) {
         this.G();
      }
   }

   public T N() {
      return this.J ? null : this.l;
   }

   public boolean W() {
      return this.J;
   }

   public void x(T var1) {
      if (this.l != null && var1 == this.l) {
         this.J = (boolean)1;
      }
   }

   public void G() {
      this.G = null;
      this.l = null;
      this.J = (boolean)0;
   }
}
