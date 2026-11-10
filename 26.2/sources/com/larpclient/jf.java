package com.larpclient;

public final class jf {
   private jf.k K;
   private jf.z E;
   private int T;
   private int p;
   private int S;
   private boolean A;
   private boolean u;

   public boolean H(jf.k var1) {
      return this.K == var1;
   }

   public void d(jf.k var1, jf.z var2, int var3) {
      if (this.K != null && (!this.E.current() || this.E.player() != var2.player())) {
         this.h();
      }

      if (var2.allowed() && var3 >= 0) {
         if (this.K != null && this.K != var1) {
            if (var1 != jf.k.q) {
               return;
            }

            this.h();
         }

         if (this.K != var1) {
            if (!var2.usingItem() && !var2.manualUse() && var2.usable(var3)) {
               this.K = var1;
               this.E = var2;
               this.T = var2.selected();
               this.p = var3;
               this.S = var2.tick();
               this.A = this.u = (boolean)0;
               var2.select(this.p);
               var2.holdUse(true);
            }
         } else if (!this.E.manualUse() && this.E.selected() == this.p && this.E.usable(this.p)) {
            if (this.E.usingItem()) {
               this.A = (boolean)1;
            } else {
               if (this.A || this.E.tick() - this.S >= 8) {
                  this.h();
                  return;
               }

               if (!this.u) {
                  this.u = this.E.startUnderScreen();
               }
            }

            this.E.holdUse(true);
         } else {
            this.h();
         }
      } else {
         this.i(var1);
      }
   }

   public void i(jf.k var1) {
      if (this.K == var1) {
         this.h();
      }
   }

   public void h() {
      jf.z var1 = this.E;
      int var2 = this.T;
      int var3 = this.p;
      this.K = null;
      this.E = null;
      this.A = (boolean)0;
      if (var1 != null) {
         try {
            var1.releaseUse();
         } finally {
            if (var1.current() && !var1.manualUse() && var1.selected() == var3) {
               var1.select(var2);
            }
         }
      }
   }

   public enum k {
      W,
      q;
   }

   public interface z {
      Object player();

      int tick();

      boolean current();

      boolean allowed();

      int selected();

      void select(int var1);

      boolean usable(int var1);

      boolean usingItem();

      boolean manualUse();

      void holdUse(boolean var1);

      void releaseUse();

      boolean startUnderScreen();
   }
}
