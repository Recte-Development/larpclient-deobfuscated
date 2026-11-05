package com.larpclient;

import java.util.Objects;
import net.minecraft.world.item.ItemStack;

public final class w1 {
   private ItemStack n;
   private h3.r a;
   private Object p;

   public void h() {
      this.n = null;
      this.a = null;
      this.p = null;
   }

   public Object q(ItemStack var1, h3.r var2) {
      if (this.n == null || !ItemStack.isSameItemSameComponents(this.n, var1) || !Objects.equals(this.a, var2)) {
         this.n = var1.copy();
         this.a = var2;
         this.p = new Object();
      }

      return this.p;
   }
}
