package com.larpclient;

public final class b_ {
   private b_.s E;
   private b_.p r;
   private int f;
   private int k;
   private int U;
   private boolean v;
   private boolean F;

   public boolean w(b_.s var1) {
      return this.E == var1;
   }

   public void A(b_.s var1, b_.p var2, int var3) {
      if (this.E != null && (!this.r.current() || this.r.player() != var2.player())) {
         this.r();
      }

      if (var2.allowed() && var3 >= 0) {
         if (this.E != null && this.E != var1) {
            if (var1 != b_.s.t) {
               return;
            }

            this.r();
         }

         if (this.E != var1) {
            if (!var2.usingItem() && !var2.manualUse() && var2.usable(var3)) {
               this.E = var1;
               this.r = var2;
               this.f = var2.selected();
               this.k = var3;
               this.U = var2.tick();
               this.v = this.F = (boolean)0;
               var2.select(this.k);
               var2.holdUse(true);
            }
         } else if (!this.r.manualUse() && this.r.selected() == this.k && this.r.usable(this.k)) {
            if (this.r.usingItem()) {
               this.v = (boolean)1;
            } else {
               if (this.v || this.r.tick() - this.U >= 8) {
                  this.r();
                  return;
               }

               if (!this.F) {
                  this.F = this.r.startUnderScreen();
               }
            }

            this.r.holdUse(true);
         } else {
            this.r();
         }
      } else {
         this.P(var1);
      }
   }

   public void P(b_.s var1) {
      if (this.E == var1) {
         this.r();
      }
   }

   public void r() {
      b_.p var1 = this.r;
      int var2 = this.f;
      int var3 = this.k;
      this.E = null;
      this.r = null;
      this.v = (boolean)0;
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

   public interface p {
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

   public enum s {
      z,
      t;
   }
}
