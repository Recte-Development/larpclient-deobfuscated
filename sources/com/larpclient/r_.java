package com.larpclient;

import java.util.Objects;
import net.minecraft.world.item.ItemStack;

public final class r_ {
   private ItemStack a;
   private ms.c M;
   private Object E;

   public void n() {
      this.a = null;
      this.M = null;
      this.E = null;
   }

   public Object Q(ItemStack var1, ms.c var2) {
      if (this.a == null || !ItemStack.isSameItemSameComponents(this.a, var1) || !Objects.equals(this.M, var2)) {
         this.a = var1.copy();
         this.M = var2;
         this.E = new Object();
      }

      return this.E;
   }
}
