package com.larpclient;

public final class ew {
   private ew.h U = ew.h.u;
   private boolean P;
   private boolean b;
   private long d;
   private long x;
   private int R;

   public ew.h E() {
      return this.U;
   }

   public boolean C() {
      return this.U != ew.h.u;
   }

   public void g(long var1) {
      this.l();
      this.b = (boolean)1;
      this.x = var1 + 250000000L;
   }

   public boolean S(long var1) {
      if (this.b && var1 - this.x >= 0L) {
         this.b = (boolean)0;
      }

      return !this.b;
   }

   public void B(long var1, long var3, boolean var5) {
      if (this.C()) {
         throw new IllegalStateException("Rod attempt already active");
      }

      this.P = var5;
      this.d = var1 + var3;
      this.R = 0;
      this.U = ew.h.B;
   }

   public ew.j H(long var1, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7) {
      if (!this.C()) {
         return ew.j.H;
      }

      if (var1 - this.d < 0L && ++this.R < 60) {
         if (this.U == ew.h.B) {
            if (!var3) {
               return ew.j.H;
            }

            this.U = ew.h.s;
         }

         if (!var3 || var5) {
            return ew.j.r;
         }

         if (this.U == ew.h.S) {
            return ew.j.H;
         }

         if (var4) {
            if (!this.P) {
               return ew.j.r;
            }

            this.U = ew.h.E;
            return ew.j.L;
         } else {
            return !var6 && !var7 ? ew.j.H : ew.j.r;
         }
      } else {
         return ew.j.r;
      }
   }

   public void N() {
      if (this.U != ew.h.E) {
         throw new IllegalStateException("No confirmed rod hook");
      }

      this.U = ew.h.S;
   }

   public void l() {
      this.U = ew.h.u;
      this.b = (boolean)0;
      this.R = 0;
   }

   public static final class e {
      private long h;
      private boolean r;

      public boolean E(long var1) {
         return !this.r || var1 - this.h >= 0L;
      }

      public void S(long var1, long var3) {
         this.h = var1 + var3;
         this.r = (boolean)1;
      }

      public void n(long var1, long var3) {
         this.h = var1 + var3;
         this.r = (boolean)1;
      }

      public void n() {
         this.r = (boolean)0;
      }
   }

   public enum h {
      u,
      B,
      s,
      E,
      S;
   }

   public enum j {
      H,
      r,
      L;
   }
}
