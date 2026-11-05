package com.larpclient;

public final class eo {
   private eo.r c;
   private eo.o U;
   private int r;
   private int z;
   private int Z;
   private boolean q;

   public boolean v(eo.r var1) {
      return this.c == var1;
   }

   public void v(eo.r var1, eo.o var2, int var3) {
      if (this.c != null && (!this.U.current() || this.U.player() != var2.player())) {
         this.x();
      }

      if (var2.allowed() && var3 >= 0) {
         if (this.c != null && this.c != var1) {
            if (var1 != eo.r.V) {
               return;
            }

            this.x();
         }

         if (this.c != var1) {
            if (!var2.usingItem() && !var2.manualUse() && var2.usable(var3)) {
               this.c = var1;
               this.U = var2;
               this.r = var2.selected();
               this.z = var3;
               this.Z = var2.tick();
               this.q = (boolean)0;
               var2.select(this.z);
               var2.holdUse(true);
            }
         } else if (!this.U.manualUse() && this.U.selected() == this.z && this.U.usable(this.z)) {
            if (this.U.usingItem()) {
               this.q = (boolean)1;
            } else if (this.q || this.U.tick() - this.Z >= 8) {
               this.x();
               return;
            }

            this.U.holdUse(true);
         } else {
            this.x();
         }
      } else {
         this.W(var1);
      }
   }

   public void W(eo.r var1) {
      if (this.c == var1) {
         this.x();
      }
   }

   public void x() {
      eo.o var1 = this.U;
      int var2 = this.r;
      int var3 = this.z;
      this.c = null;
      this.U = null;
      this.q = (boolean)0;
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

   public interface o {
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

   public enum r {
      s,
      V;
   }
}
