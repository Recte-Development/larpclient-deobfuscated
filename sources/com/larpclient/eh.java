package com.larpclient;

public final class eh {
   private int l = -1;
   private int T = -1;

   public boolean c() {
      return this.T >= 0;
   }

   public boolean c(int var1) {
      return this.c() && var1 == this.T;
   }

   public void q(int var1, int var2) {
      if (var1 >= 0 && var1 <= 8 && var2 >= 0 && var2 <= 8) {
         if (!this.c()) {
            this.l = var1;
         }

         this.T = var2;
      } else {
         throw new IllegalArgumentException("Hotbar slot");
      }
   }

   public int L(int var1, boolean var2) {
      int var3 = var2 && var1 == this.T && this.l != this.T ? this.l : -1;
      this.C();
      return var3;
   }

   public void C() {
      this.l = this.T = -1;
   }
}
