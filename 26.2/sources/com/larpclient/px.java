package com.larpclient;

import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;

public final class px {
   private px() {
   }

   public static ItemStack q(ItemStack var0) {
      b8 var1 = b8.k();
      return var1.a(var0) ? ItemStack.EMPTY : var1.D(var0);
   }

   public static boolean A() {
      Minecraft var0 = Minecraft.getInstance();
      return var0 != null && var0.gui.screen() != null && var0.gui.screen().getTitle().getString().toLowerCase(Locale.ROOT).contains("auction");
   }

   public static boolean T(ItemStack var0) {
      if (gz.W(var0)) {
         return true;
      }

      if (!A()) {
         return false;
      }

      LocalPlayer var1 = Minecraft.getInstance().player;
      if (var1 == null) {
         return false;
      }

      for (Slot var3 : var1.containerMenu.slots) {
         if (var3.container == var1.getInventory()) {
            break;
         }

         if (var3.getItem() == var0) {
            return true;
         }
      }

      return false;
   }

   public static boolean C(ItemStack var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (b8.k().J() && var1.gui.screen() != null && var1.gui.screen().getTitle().getString().toLowerCase(Locale.ROOT).contains("sell")) {
         ItemLore var2 = (ItemLore)var0.get(DataComponents.LORE);
         return var2 != null && var2.lines().stream().anyMatch(var0x -> var0x.getString().toUpperCase(Locale.ROOT).contains("CLICK TO SELL ITEMS"));
      } else {
         return false;
      }
   }

   public static long I() {
      LocalPlayer var0 = Minecraft.getInstance().player;
      return var0 == null
         ? 0L
         : var0.containerMenu.slots.stream().filter(var1 -> var1.container != var0.getInventory()).mapToLong(var0x -> b8.k().x(var0x.getItem())).sum();
   }

   public static String z(long var0) {
      if (var0 >= 1000000000L) {
         return i(var0 / 1.0E9) + "B";
      } else if (var0 >= 1000000L) {
         return i(var0 / 1000000.0) + "M";
      } else {
         return var0 >= 1000L ? i(var0 / 1000.0) + "K" : Long.toString(var0);
      }
   }

   private static String i(double var0) {
      return String.format(Locale.ROOT, "%.1f", var0).replaceAll("\\.0$", "");
   }
}
