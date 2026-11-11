package com.larpclient.util.misc;

import com.larpclient.render.world.ItemStackMarkers;
import com.larpclient.util.world.FakeSpawnerController;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;

public final class AuctionUtil {
   private AuctionUtil() {
   }

   public static ItemStack filterStack(ItemStack var0) {
      FakeSpawnerController var1 = FakeSpawnerController.getInstance();
      return var1.isReservedHeldStack(var0) ? ItemStack.EMPTY : var1.removeSpawnerEntity(var0);
   }

   public static boolean isAuctionScreen() {
      Minecraft var0 = Minecraft.getInstance();
      return var0 != null && var0.gui.screen() != null && var0.gui.screen().getTitle().getString().toLowerCase(Locale.ROOT).contains("auction");
   }

   public static boolean isAuctionItem(ItemStack var0) {
      if (ItemStackMarkers.isRealPurchase(var0)) {
         return true;
      }

      if (!isAuctionScreen()) {
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

   public static boolean isSellPrompt(ItemStack var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (FakeSpawnerController.getInstance().isActive()
         && var1.gui.screen() != null
         && var1.gui.screen().getTitle().getString().toLowerCase(Locale.ROOT).contains("sell")) {
         ItemLore var2 = (ItemLore)var0.get(DataComponents.LORE);
         return var2 != null && var2.lines().stream().anyMatch(var0x -> var0x.getString().toUpperCase(Locale.ROOT).contains("CLICK TO SELL ITEMS"));
      } else {
         return false;
      }
   }

   public static long getContainerWorth() {
      LocalPlayer var0 = Minecraft.getInstance().player;
      return var0 == null
         ? 0L
         : var0.containerMenu
            .slots
            .stream()
            .filter(var1 -> var1.container != var0.getInventory())
            .mapToLong(var0x -> FakeSpawnerController.getInstance().loadPiles(var0x.getItem()))
            .sum();
   }

   public static String formatCompact(long var0) {
      if (var0 >= 1000000000L) {
         return formatDecimal(var0 / 1.0E9) + "B";
      } else if (var0 >= 1000000L) {
         return formatDecimal(var0 / 1000000.0) + "M";
      } else {
         return var0 >= 1000L ? formatDecimal(var0 / 1000.0) + "K" : Long.toString(var0);
      }
   }

   private static String formatDecimal(double var0) {
      return String.format(Locale.ROOT, "%.1f", var0).replaceAll("\\.0$", "");
   }
}
