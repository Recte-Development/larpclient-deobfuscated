package com.larpclient;

public final class d5<T> {
   private Object r;
   private long q;
   private int Z;
   private T A;
   private boolean g;

   public void H(Object var1, long var2, int var4, T var5) {
      if (!this.A(var1, var2, var4)) {
         this.r = var1;
         this.q = var2;
         this.Z = var4;
         this.A = (T)var5;
         this.g = (boolean)0;
      }
   }

   public boolean A(Object var1, long var2, int var4) {
      return this.A != null && this.r == var1 && this.q == var2 && this.Z == var4;
   }

   public void b(Object var1, long var2, int var4, boolean var5) {
      if (!var5 || !this.A(var1, var2, var4)) {
         this.P();
      }
   }

   public T m() {
      return this.g ? null : this.A;
   }

   public boolean l() {
      return this.g;
   }

   public void W(T var1) {
      if (this.A != null && var1 == this.A) {
         this.g = (boolean)1;
      }
   }

   public void P() {
      this.r = null;
      this.A = null;
      this.g = (boolean)0;
   }
}
