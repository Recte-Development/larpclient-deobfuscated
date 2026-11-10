package com.larpclient;

public final class s {
   private int p = -1;
   private int E = -1;

   public boolean R() {
      return this.E >= 0;
   }

   public boolean y(int var1) {
      return this.R() && var1 == this.E;
   }

   public void s(int var1, int var2) {
      if (var1 >= 0 && var1 <= 8 && var2 >= 0 && var2 <= 8) {
         if (!this.R()) {
            this.p = var1;
         }

         this.E = var2;
      } else {
         throw new IllegalArgumentException("Hotbar slot");
      }
   }

   public int h(int var1, boolean var2) {
      int var3 = var2 && var1 == this.E && this.p != this.E ? this.p : -1;
      this.J();
      return var3;
   }

   public void J() {
      this.p = this.E = -1;
   }
}
