package com.larpclient;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

final class sf {
   private sf() {
   }

   static ItemStack q(Item var0, ItemStack var1) {
      int var2 = var1 != null && !var1.isEmpty() ? Math.max(1, var1.getCount()) : 1;
      ItemStack var3 = new ItemStack(var0, var2);
      Component var4 = var1 == null ? null : (Component)var1.get(DataComponents.CUSTOM_NAME);
      if (var4 != null) {
         var3.set(DataComponents.CUSTOM_NAME, var4);
      }

      return var3;
   }

   static boolean K(ItemStack var0) {
      String var1 = var0.getHoverName().getString().toLowerCase();
      if (!var1.contains("[host]") && !var1.contains("[viewer]")) {
         String var2 = var1.replaceAll("[^0-9]", "");
         if (var2.isEmpty()) {
            return false;
         }

         try {
            int var3 = Integer.parseInt(var2);
            return var3 >= 1 && var3 <= 10 || var3 == 11;
         } catch (NumberFormatException var4) {
            return false;
         }
      } else {
         return true;
      }
   }

   static boolean b(ItemStack var0, Item var1, Item var2) {
      return !var0.isEmpty() && var0.is(var1) && !K(var0) && var2 != var1;
   }
}
