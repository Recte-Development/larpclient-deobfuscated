package com.larpclient;

public final class gf<T> {
   private Object E;
   private long s;
   private int z;
   private T Y;
   private boolean o;

   public void O(Object var1, long var2, int var4, T var5) {
      if (!this.F(var1, var2, var4)) {
         this.E = var1;
         this.s = var2;
         this.z = var4;
         this.Y = (T)var5;
         this.o = (boolean)0;
      }
   }

   public boolean F(Object var1, long var2, int var4) {
      return this.Y != null && this.E == var1 && this.s == var2 && this.z == var4;
   }

   public void p(Object var1, long var2, int var4, boolean var5) {
      if (!var5 || !this.F(var1, var2, var4)) {
         this.j();
      }
   }

   public T W() {
      return this.o ? null : this.Y;
   }

   public boolean H() {
      return this.o;
   }

   public void i(T var1) {
      if (this.Y != null && var1 == this.Y) {
         this.o = (boolean)1;
      }
   }

   public void j() {
      this.E = null;
      this.Y = null;
      this.o = (boolean)0;
   }
}
