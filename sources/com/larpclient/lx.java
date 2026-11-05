package com.larpclient;

public final class lx {
   private lx.m O = lx.m.j;
   private boolean C;
   private boolean T;
   private long Z;
   private long B;
   private int e;

   public lx.m P() {
      return this.O;
   }

   public boolean U() {
      return this.O != lx.m.j;
   }

   public void r(long var1) {
      this.s();
      this.T = (boolean)1;
      this.B = var1 + 250000000L;
   }

   public boolean O(long var1) {
      if (this.T && var1 - this.B >= 0L) {
         this.T = (boolean)0;
      }

      return !this.T;
   }

   public void v(long var1, long var3, boolean var5) {
      if (this.U()) {
         throw new IllegalStateException("Rod attempt already active");
      }

      this.C = var5;
      this.Z = var1 + var3;
      this.e = 0;
      this.O = lx.m.Y;
   }

   public lx.x s(long var1, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7) {
      if (!this.U()) {
         return lx.x.p;
      }

      if (var1 - this.Z < 0L && ++this.e < 60) {
         if (this.O == lx.m.Y) {
            if (!var3) {
               return lx.x.p;
            }

            this.O = lx.m.y;
         }

         if (!var3 || var5) {
            return lx.x.R;
         }

         if (this.O == lx.m.I) {
            return lx.x.p;
         }

         if (var4) {
            if (!this.C) {
               return lx.x.R;
            }

            this.O = lx.m.T;
            return lx.x.J;
         } else {
            return !var6 && !var7 ? lx.x.p : lx.x.R;
         }
      } else {
         return lx.x.R;
      }
   }

   public void E() {
      if (this.O != lx.m.T) {
         throw new IllegalStateException("No confirmed rod hook");
      }

      this.O = lx.m.I;
   }

   public void s() {
      this.O = lx.m.j;
      this.T = (boolean)0;
      this.e = 0;
   }

   public static final class a {
      private long D;
      private boolean z;

      public boolean J(long var1) {
         return !this.z || var1 - this.D >= 0L;
      }

      public void X(long var1, long var3) {
         this.D = var1 + var3;
         this.z = (boolean)1;
      }

      public void C(long var1, long var3) {
         this.D = var1 + var3;
         this.z = (boolean)1;
      }

      public void L() {
         this.z = (boolean)0;
      }
   }

   public enum m {
      j,
      Y,
      y,
      T,
      I;
   }

   public enum x {
      p,
      R,
      J;
   }
}
