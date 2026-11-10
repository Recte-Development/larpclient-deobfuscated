package com.larpclient;

public final class bz {
   private bz.q I = bz.q.B;
   private boolean l;
   private boolean Q;
   private long o;
   private long j;
   private int X;

   public bz.q w() {
      return this.I;
   }

   public boolean Z() {
      return this.I != bz.q.B;
   }

   public void k(long var1) {
      this.g();
      this.Q = (boolean)1;
      this.j = var1 + 250000000L;
   }

   public boolean R(long var1) {
      if (this.Q && var1 - this.j >= 0L) {
         this.Q = (boolean)0;
      }

      return !this.Q;
   }

   public void B(long var1, long var3, boolean var5) {
      if (this.Z()) {
         throw new IllegalStateException("Rod attempt already active");
      }

      this.l = var5;
      this.o = var1 + var3;
      this.X = 0;
      this.I = bz.q.G;
   }

   public bz.w m(long var1, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7) {
      if (!this.Z()) {
         return bz.w.Z;
      }

      if (var1 - this.o < 0L && ++this.X < 60) {
         if (this.I == bz.q.G) {
            if (!var3) {
               return bz.w.Z;
            }

            this.I = bz.q.v;
         }

         if (!var3 || var5) {
            return bz.w.k;
         }

         if (this.I == bz.q.Z) {
            return bz.w.Z;
         }

         if (var4) {
            if (!this.l) {
               return bz.w.k;
            }

            this.I = bz.q.H;
            return bz.w.c;
         } else {
            return !var6 && !var7 ? bz.w.Z : bz.w.k;
         }
      } else {
         return bz.w.k;
      }
   }

   public void h() {
      if (this.I != bz.q.H) {
         throw new IllegalStateException("No confirmed rod hook");
      }

      this.I = bz.q.Z;
   }

   public void g() {
      this.I = bz.q.B;
      this.Q = (boolean)0;
      this.X = 0;
   }

   public enum q {
      B,
      G,
      v,
      H,
      Z;
   }

   public static final class s {
      private long A;
      private boolean V;

      public boolean J(long var1) {
         return !this.V || var1 - this.A >= 0L;
      }

      public void G(long var1, long var3) {
         this.A = var1 + var3;
         this.V = (boolean)1;
      }

      public void Y(long var1, long var3) {
         this.A = var1 + var3;
         this.V = (boolean)1;
      }

      public void X() {
         this.V = (boolean)0;
      }
   }

   public enum w {
      Z,
      k,
      c;
   }
}
