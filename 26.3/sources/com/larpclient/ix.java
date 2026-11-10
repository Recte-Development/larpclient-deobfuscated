package com.larpclient;

public final class ix {
   private ix.u U = ix.u.B;
   private boolean w;
   private boolean A;
   private long j;
   private long H;
   private int m;

   public ix.u N() {
      return this.U;
   }

   public boolean Q() {
      return this.U != ix.u.B;
   }

   public void Z(long var1) {
      this.Q();
      this.A = (boolean)1;
      this.H = var1 + 250000000L;
   }

   public boolean y(long var1) {
      if (this.A && var1 - this.H >= 0L) {
         this.A = (boolean)0;
      }

      return !this.A;
   }

   public void Z(long var1, long var3, boolean var5) {
      if (this.Q()) {
         throw new IllegalStateException("Rod attempt already active");
      }

      this.w = var5;
      this.j = var1 + var3;
      this.m = 0;
      this.U = ix.u.Y;
   }

   public ix.k a(long var1, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7) {
      if (!this.Q()) {
         return ix.k.e;
      }

      if (var1 - this.j < 0L && ++this.m < 60) {
         if (this.U == ix.u.Y) {
            if (!var3) {
               return ix.k.e;
            }

            this.U = ix.u.j;
         }

         if (!var3 || var5) {
            return ix.k.u;
         }

         if (this.U == ix.u.q) {
            return ix.k.e;
         }

         if (var4) {
            if (!this.w) {
               return ix.k.u;
            }

            this.U = ix.u.S;
            return ix.k.d;
         } else {
            return !var6 && !var7 ? ix.k.e : ix.k.u;
         }
      } else {
         return ix.k.u;
      }
   }

   public void u() {
      if (this.U != ix.u.S) {
         throw new IllegalStateException("No confirmed rod hook");
      }

      this.U = ix.u.q;
   }

   public void Q() {
      this.U = ix.u.B;
      this.A = (boolean)0;
      this.m = 0;
   }

   public enum k {
      e,
      u,
      d;
   }

   public static final class r {
      private long P;
      private boolean n;

      public boolean V(long var1) {
         return !this.n || var1 - this.P >= 0L;
      }

      public void e(long var1, long var3) {
         this.P = var1 + var3;
         this.n = (boolean)1;
      }

      public void V(long var1, long var3) {
         this.P = var1 + var3;
         this.n = (boolean)1;
      }

      public void c() {
         this.n = (boolean)0;
      }
   }

   public enum u {
      B,
      Y,
      j,
      S,
      q;
   }
}
