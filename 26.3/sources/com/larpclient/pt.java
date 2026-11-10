package com.larpclient;

public final class pt {
   private pt.d V;
   private pt.m n;
   private int h;
   private int M;
   private int q;
   private boolean g;
   private boolean c;

   public boolean Y(pt.d var1) {
      return this.V == var1;
   }

   public void I(pt.d var1, pt.m var2, int var3) {
      if (this.V != null && (!this.n.current() || this.n.player() != var2.player())) {
         this.n();
      }

      if (var2.allowed() && var3 >= 0) {
         if (this.V != null && this.V != var1) {
            if (var1 != pt.d.i) {
               return;
            }

            this.n();
         }

         if (this.V != var1) {
            if (!var2.usingItem() && !var2.manualUse() && var2.usable(var3)) {
               this.V = var1;
               this.n = var2;
               this.h = var2.selected();
               this.M = var3;
               this.q = var2.tick();
               this.g = this.c = (boolean)0;
               var2.select(this.M);
               var2.holdUse(true);
            }
         } else if (!this.n.manualUse() && this.n.selected() == this.M && this.n.usable(this.M)) {
            if (this.n.usingItem()) {
               this.g = (boolean)1;
            } else {
               if (this.g || this.n.tick() - this.q >= 8) {
                  this.n();
                  return;
               }

               if (!this.c) {
                  this.c = this.n.startUnderScreen();
               }
            }

            this.n.holdUse(true);
         } else {
            this.n();
         }
      } else {
         this.Q(var1);
      }
   }

   public void Q(pt.d var1) {
      if (this.V == var1) {
         this.n();
      }
   }

   public void n() {
      pt.m var1 = this.n;
      int var2 = this.h;
      int var3 = this.M;
      this.V = null;
      this.n = null;
      this.g = (boolean)0;
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

   public enum d {
      p,
      i;
   }

   public interface m {
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
