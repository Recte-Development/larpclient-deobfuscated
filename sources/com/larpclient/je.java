package com.larpclient;

public final class je {
   private je.b u;
   private je.r O;
   private int g;
   private int s;
   private int k;
   private boolean N;

   public boolean Y(je.b var1) {
      return this.u == var1;
   }

   public void w(je.b var1, je.r var2, int var3) {
      if (this.u != null && (!this.O.current() || this.O.player() != var2.player())) {
         this.U();
      }

      if (var2.allowed() && var3 >= 0) {
         if (this.u != null && this.u != var1) {
            if (var1 != je.b.P) {
               return;
            }

            this.U();
         }

         if (this.u != var1) {
            if (!var2.usingItem() && !var2.manualUse() && var2.usable(var3)) {
               this.u = var1;
               this.O = var2;
               this.g = var2.selected();
               this.s = var3;
               this.k = var2.tick();
               this.N = (boolean)0;
               var2.select(this.s);
               var2.holdUse(true);
            }
         } else if (!this.O.manualUse() && this.O.selected() == this.s && this.O.usable(this.s)) {
            if (this.O.usingItem()) {
               this.N = (boolean)1;
            } else if (this.N || this.O.tick() - this.k >= 8) {
               this.U();
               return;
            }

            this.O.holdUse(true);
         } else {
            this.U();
         }
      } else {
         this.U(var1);
      }
   }

   public void U(je.b var1) {
      if (this.u == var1) {
         this.U();
      }
   }

   public void U() {
      je.r var1 = this.O;
      int var2 = this.g;
      int var3 = this.s;
      this.u = null;
      this.O = null;
      this.N = (boolean)0;
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

   public enum b {
      A,
      P;
   }

   public interface r {
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
   }
}
