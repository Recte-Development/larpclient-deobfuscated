package com.larpclient.util.inventory;

import com.larpclient.render.model.ModelAxisFinder;
import java.util.Objects;
import net.minecraft.world.item.ItemStack;

public final class ItemStackTokenCache {
   private ItemStack lastStack;
   private ModelAxisFinder.Axis lastKey;
   private Object token;

   public void clear() {
      this.lastStack = null;
      this.lastKey = null;
      this.token = null;
   }

   public Object getToken(ItemStack var1, ModelAxisFinder.Axis var2) {
      if (this.lastStack == null || !ItemStack.isSameItemSameComponents(this.lastStack, var1) || !Objects.equals(this.lastKey, var2)) {
         this.lastStack = var1.copy();
         this.lastKey = var2;
         this.token = new Object();
      }

      return this.token;
   }
}
