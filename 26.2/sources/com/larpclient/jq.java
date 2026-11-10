package com.larpclient;

import java.util.Objects;
import net.minecraft.world.item.ItemStack;

public final class jq {
   private ItemStack V;
   private z2.e M;
   private Object s;

   public void o() {
      this.V = null;
      this.M = null;
      this.s = null;
   }

   public Object g(ItemStack var1, z2.e var2) {
      if (this.V == null || !ItemStack.isSameItemSameComponents(this.V, var1) || !Objects.equals(this.M, var2)) {
         this.V = var1.copy();
         this.M = var2;
         this.s = new Object();
      }

      return this.s;
   }
}
