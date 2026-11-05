package com.larpclient;

public final class p6 {
   private p6.v Z = p6.v.r;
   private boolean m;
   private boolean z;
   private long W;
   private long u;
   private int K;

   public p6.v Y() {
      return this.Z;
   }

   public boolean V() {
      return this.Z != p6.v.r;
   }

   public void e(long var1) {
      this.q();
      this.z = (boolean)1;
      this.u = var1 + 250000000L;
   }

   public boolean C(long var1) {
      if (this.z && var1 - this.u >= 0L) {
         this.z = (boolean)0;
      }

      return !this.z;
   }

   public void I(long var1, long var3, boolean var5) {
      if (this.V()) {
         throw new IllegalStateException("Rod attempt already active");
      }

      this.m = var5;
      this.W = var1 + var3;
      this.K = 0;
      this.Z = p6.v.X;
   }

   public p6.n i(long var1, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7) {
      if (!this.V()) {
         return p6.n.v;
      }

      if (var1 - this.W < 0L && ++this.K < 60) {
         if (this.Z == p6.v.X) {
            if (!var3) {
               return p6.n.v;
            }

            this.Z = p6.v.u;
         }

         if (!var3 || var5) {
            return p6.n.X;
         }

         if (this.Z == p6.v.v) {
            return p6.n.v;
         }

         if (var4) {
            if (!this.m) {
               return p6.n.X;
            }

            this.Z = p6.v.E;
            return p6.n.W;
         } else {
            return !var6 && !var7 ? p6.n.v : p6.n.X;
         }
      } else {
         return p6.n.X;
      }
   }

   public void T() {
      if (this.Z != p6.v.E) {
         throw new IllegalStateException("No confirmed rod hook");
      }

      this.Z = p6.v.v;
   }

   public void q() {
      this.Z = p6.v.r;
      this.z = (boolean)0;
      this.K = 0;
   }

   public enum n {
      v,
      X,
      W;
   }

   public enum v {
      r,
      X,
      u,
      E,
      v;
   }

   public static final class w {
      private long J;
      private boolean a;

      public boolean R(long var1) {
         return !this.a || var1 - this.J >= 0L;
      }

      public void i(long var1, long var3) {
         this.J = var1 + var3;
         this.a = (boolean)1;
      }

      public void P(long var1, long var3) {
         this.J = var1 + var3;
         this.a = (boolean)1;
      }

      public void j() {
         this.a = (boolean)0;
      }
   }
}
