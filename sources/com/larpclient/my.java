package com.larpclient;

public final class my<T> {
   private Object z;
   private long H;
   private int t;
   private T a;
   private boolean R;

   public void E(Object var1, long var2, int var4, T var5) {
      if (!this.F(var1, var2, var4)) {
         this.z = var1;
         this.H = var2;
         this.t = var4;
         this.a = (T)var5;
         this.R = (boolean)0;
      }
   }

   public boolean F(Object var1, long var2, int var4) {
      return this.a != null && this.z == var1 && this.H == var2 && this.t == var4;
   }

   public void L(Object var1, long var2, int var4, boolean var5) {
      if (!var5 || !this.F(var1, var2, var4)) {
         this.v();
      }
   }

   public T P() {
      return this.R ? null : this.a;
   }

   public boolean D() {
      return this.R;
   }

   public void u(T var1) {
      if (this.a != null && var1 == this.a) {
         this.R = (boolean)1;
      }
   }

   public void v() {
      this.z = null;
      this.a = null;
      this.R = (boolean)0;
   }
}
