package com.larpclient;

public final class uw {
   private int l = -1;
   private int z = -1;

   public boolean J() {
      return this.z >= 0;
   }

   public boolean s(int var1) {
      return this.J() && var1 == this.z;
   }

   public void h(int var1, int var2) {
      if (var1 >= 0 && var1 <= 8 && var2 >= 0 && var2 <= 8) {
         if (!this.J()) {
            this.l = var1;
         }

         this.z = var2;
      } else {
         throw new IllegalArgumentException("Hotbar slot");
      }
   }

   public int n(int var1, boolean var2) {
      int var3 = var2 && var1 == this.z && this.l != this.z ? this.l : -1;
      this.B();
      return var3;
   }

   public void B() {
      this.l = this.z = -1;
   }
}
