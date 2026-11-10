package com.larpclient;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapId;

final class je {
   static boolean q(ItemStack var0, ItemStack var1) {
      return A(q(var0), var0.isEmpty() ? 0 : 1, q(var1), var1.isEmpty() ? 0 : 1, ItemStack.isSameItemSameComponents(var0, var1));
   }

   static boolean F(ItemStack var0, ItemStack var1) {
      return A(q(var0), var0.getCount(), q(var1), var1.getCount(), ItemStack.isSameItemSameComponents(var0, var1));
   }

   static boolean A(int var0, int var1, int var2, int var3, boolean var4) {
      if (var1 > 0 && var3 > 0) {
         if (var1 != var3) {
            return false;
         } else {
            return var0 >= 0 ? var0 == var2 : var2 < 0 && var4;
         }
      } else {
         return var1 <= 0 && var3 <= 0;
      }
   }

   private static int q(ItemStack var0) {
      MapId var1 = var0.is(Items.FILLED_MAP) ? (MapId)var0.get(DataComponents.MAP_ID) : null;
      return var1 == null ? -1 : var1.id();
   }
}
