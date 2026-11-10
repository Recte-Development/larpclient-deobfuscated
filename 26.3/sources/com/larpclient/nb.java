package com.larpclient;

import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;

public final class nb {
   private nb() {
   }

   public static ItemStack t(ItemStack var0) {
      jn var1 = jn.g();
      return var1.e(var0) ? ItemStack.EMPTY : var1.w(var0);
   }

   public static boolean C() {
      Minecraft var0 = Minecraft.getInstance();
      return var0 != null && var0.gui.screen() != null && var0.gui.screen().getTitle().getString().toLowerCase(Locale.ROOT).contains("auction");
   }

   public static boolean u(ItemStack var0) {
      if (nn.i(var0)) {
         return true;
      }

      if (!C()) {
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

   public static boolean n(ItemStack var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (jn.g().a() && var1.gui.screen() != null && var1.gui.screen().getTitle().getString().toLowerCase(Locale.ROOT).contains("sell")) {
         ItemLore var2 = (ItemLore)var0.get(DataComponents.LORE);
         return var2 != null && var2.lines().stream().anyMatch(var0x -> var0x.getString().toUpperCase(Locale.ROOT).contains("CLICK TO SELL ITEMS"));
      } else {
         return false;
      }
   }

   public static long E() {
      LocalPlayer var0 = Minecraft.getInstance().player;
      return var0 == null
         ? 0L
         : var0.containerMenu.slots.stream().filter(var1 -> var1.container != var0.getInventory()).mapToLong(var0x -> jn.g().A(var0x.getItem())).sum();
   }

   public static String b(long var0) {
      if (var0 >= 1000000000L) {
         return o(var0 / 1.0E9) + "B";
      } else if (var0 >= 1000000L) {
         return o(var0 / 1000000.0) + "M";
      } else {
         return var0 >= 1000L ? o(var0 / 1000.0) + "K" : Long.toString(var0);
      }
   }

   private static String o(double var0) {
      return String.format(Locale.ROOT, "%.1f", var0).replaceAll("\\.0$", "");
   }
}
