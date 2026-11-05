package com.larpclient;

public final class jj {
   private int m = -1;
   private int E = -1;

   public boolean p() {
      return this.E >= 0;
   }

   public boolean T(int var1) {
      return this.p() && var1 == this.E;
   }

   public void V(int var1, int var2) {
      if (var1 >= 0 && var1 <= 8 && var2 >= 0 && var2 <= 8) {
         if (!this.p()) {
            this.m = var1;
         }

         this.E = var2;
      } else {
         throw new IllegalArgumentException("Hotbar slot");
      }
   }

   public int d(int var1, boolean var2) {
      int var3 = var2 && var1 == this.E && this.m != this.E ? this.m : -1;
      this.H();
      return var3;
   }

   public void H() {
      this.m = this.E = -1;
   }
}
