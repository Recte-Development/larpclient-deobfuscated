package com.larpclient;

public final class i1<T> {
   private Object x;
   private long K;
   private int a;
   private T m;
   private boolean L;

   public void B(Object var1, long var2, int var4, T var5) {
      if (!this.m(var1, var2, var4)) {
         this.x = var1;
         this.K = var2;
         this.a = var4;
         this.m = (T)var5;
         this.L = (boolean)0;
      }
   }

   public boolean m(Object var1, long var2, int var4) {
      return this.m != null && this.x == var1 && this.K == var2 && this.a == var4;
   }

   public void n(Object var1, long var2, int var4, boolean var5) {
      if (!var5 || !this.m(var1, var2, var4)) {
         this.n();
      }
   }

   public T R() {
      return this.L ? null : this.m;
   }

   public boolean j() {
      return this.L;
   }

   public void a(T var1) {
      if (this.m != null && var1 == this.m) {
         this.L = (boolean)1;
      }
   }

   public void n() {
      this.x = null;
      this.m = null;
      this.L = (boolean)0;
   }
}
