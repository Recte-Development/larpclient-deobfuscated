package com.larpclient;

import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.ItemLore;

public final class o2 {
   private static final boolean F = Boolean.getBoolean("larp.menuTrace");
   private static int i;

   private o2() {
   }

   public static void N(Minecraft var0) {
      if (F && var0.player != null) {
         AbstractContainerMenu var1 = var0.player.containerMenu;
         if (var1 == var0.player.inventoryMenu) {
            i = 0;
         } else {
            Screen var2 = var0.gui.screen();
            String var3 = var2 == null ? "<no screen>" : var2.getTitle().getString();
            int var4 = Objects.hash(var1.containerId, var3);

            for (Slot var6 : var1.slots) {
               var4 = 31 * var4 + ItemStack.hashItemAndComponents(var6.getItem()) * 7 + var6.getItem().getCount();
            }

            if (var4 != i) {
               i = var4;
               StringBuilder var9 = new StringBuilder();
               var9.append("Menu '")
                  .append(var3)
                  .append("' id=")
                  .append(var1.containerId)
                  .append(" state=")
                  .append(var1.getStateId())
                  .append(" type=")
                  .append(var1.getClass().getSimpleName())
                  .append(" slots=")
                  .append(var1.slots.size());

               for (int var10 = 0; var10 < var1.slots.size(); var10++) {
                  Slot var7 = (Slot)var1.slots.get(var10);
                  if (var7.container != var0.player.getInventory()) {
                     ItemStack var8 = var7.getItem();
                     if (!var8.isEmpty()) {
                        var9.append("\n  #").append(var10).append(' ').append(F(var8));
                     }
                  }
               }

               r4.V("MenuTrace", "{}", var9);
            }
         }
      }
   }

   public static String F(ItemStack var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append(BuiltInRegistries.ITEM.getKey(var0.getItem()))
         .append(" x")
         .append(var0.getCount())
         .append(" '")
         .append(var0.getHoverName().getString())
         .append('\'');
      ItemLore var2 = (ItemLore)var0.get(DataComponents.LORE);
      if (var2 != null) {
         for (Component var4 : var2.lines()) {
            var1.append("\n      | ").append(var4.getString());
         }
      }

      ItemContainerContents var5 = (ItemContainerContents)var0.get(DataComponents.CONTAINER);
      if (var5 != null) {
         var1.append("\n      container items=").append(var5.nonEmptyItems().iterator().hasNext());
      }

      return var1.toString();
   }
}
