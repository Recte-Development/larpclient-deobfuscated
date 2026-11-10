package com.larpclient;

import java.util.Objects;
import net.minecraft.world.item.ItemStack;

public final class i {
   private ItemStack M;
   private op.r P;
   private Object G;

   public void c() {
      this.M = null;
      this.P = null;
      this.G = null;
   }

   public Object R(ItemStack var1, op.r var2) {
      if (this.M == null || !ItemStack.isSameItemSameComponents(this.M, var1) || !Objects.equals(this.P, var2)) {
         this.M = var1.copy();
         this.P = var2;
         this.G = new Object();
      }

      return this.G;
   }
}
