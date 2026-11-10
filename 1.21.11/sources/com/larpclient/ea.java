package com.larpclient;

public final class ea {
   private int G = -1;
   private int w = -1;

   public boolean B() {
      return this.w >= 0;
   }

   public boolean F(int var1) {
      return this.B() && var1 == this.w;
   }

   public void X(int var1, int var2) {
      if (var1 >= 0 && var1 <= 8 && var2 >= 0 && var2 <= 8) {
         if (!this.B()) {
            this.G = var1;
         }

         this.w = var2;
      } else {
         throw new IllegalArgumentException("Hotbar slot");
      }
   }

   public int w(int var1, boolean var2) {
      int var3 = var2 && var1 == this.w && this.G != this.w ? this.G : -1;
      this.o();
      return var3;
   }

   public void o() {
      this.G = this.w = -1;
   }
}
