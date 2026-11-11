package com.larpclient.module.player;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class SellMenuLogic {
   public static Map<SellMenuLogic.ItemKey, Integer> adjustCount(Map<SellMenuLogic.ItemKey, Integer> var0, SellMenuLogic.ItemKey var1, int var2) {
      HashMap var3 = new HashMap(var0);
      int var4 = var3.getOrDefault(var1, 0) + var2;
      if (var4 < 0) {
         return Map.of();
      }

      if (var4 == 0) {
         var3.remove(var1);
      } else {
         var3.put(var1, var4);
      }

      return Map.copyOf(var3);
   }

   public static boolean isTransferApplied(
      int var0, int var1, Map<SellMenuLogic.ItemKey, Integer> var2, Map<SellMenuLogic.ItemKey, Integer> var3, Map<SellMenuLogic.ItemKey, Integer> var4
   ) {
      return var0 != var1 && var2.isEmpty() && var3.equals(var4);
   }

   public static SellMenuLogic.TransferVerdict classifyTransfer(
      boolean var0, int var1, boolean var2, Map<SellMenuLogic.ItemKey, Integer> var3, Map<SellMenuLogic.ItemKey, Integer> var4
   ) {
      if (var0 && var3.equals(var4) && var1 < 100) {
         return var2 && var1 >= 5 ? SellMenuLogic.TransferVerdict.ACCEPT_BULK : SellMenuLogic.TransferVerdict.ACCEPT;
      } else {
         return SellMenuLogic.TransferVerdict.REJECT;
      }
   }

   public static boolean classifyTransfer(boolean var0, boolean var1, boolean var2, int var3, int var4) {
      return var2 && var4 > 0 && (var0 || var1 && var3 < var4);
   }

   public static boolean isSellConfirmButton(String var0, String var1, int var2, int var3) {
      return isSellConfirmButtonWithLore(var0, var1, List.of(), var2, var3);
   }

   public static boolean isSellConfirmButtonWithLore(String var0, String var1, List<String> var2, int var3, int var4) {
      if (var3 >= var4 - 9 && var3 < var4 && Set.of("minecraft:lime_stained_glass_pane", "minecraft:green_stained_glass_pane").contains(var0)) {
         String var5 = var1.strip().toLowerCase(Locale.ROOT);
         if (!var5.equals("sell") && !var5.equals("confirm") && !var5.equals("confirm sale") && !var5.startsWith("sell ") && !var5.startsWith("confirm ")) {
            for (String var7 : var2) {
               String var8 = var7.strip().toLowerCase(Locale.ROOT);
               if (var8.equals("click to sell") || var8.startsWith("click to sell ")) {
                  return true;
               }
            }

            return false;
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public static boolean isCancelButton(String var0, String var1, int var2, int var3) {
      if (var2 >= var3 - 9 && var2 < var3) {
         String var4 = var1.strip().toLowerCase(Locale.ROOT);
         return Set.of("minecraft:black_stained_glass_pane", "minecraft:gray_stained_glass_pane").contains(var0)
            ? var4.isEmpty()
            : var0.equals("minecraft:red_stained_glass_pane")
               && (var4.equals("cancel") || var4.equals("close") || var4.equals("back") || var4.startsWith("cancel ") || var4.startsWith("close "));
      } else {
         return false;
      }
   }

   private SellMenuLogic() {
   }

   public record ItemKey(String id, Object components) {
      private final String itemId;

      public ItemKey(String id, Object components) {
         this.itemId = id;
         this.components = components;
      }
   }

   public record SellSlot(
      int revision, SellMenuLogic.ItemKey key, int sourceCount, Map<SellMenuLogic.ItemKey, Integer> input, Map<SellMenuLogic.ItemKey, Integer> inventory
   ) {
      public SellSlot(
         int revision, SellMenuLogic.ItemKey key, int sourceCount, Map<SellMenuLogic.ItemKey, Integer> input, Map<SellMenuLogic.ItemKey, Integer> inventory
      ) {
         input = Map.copyOf(input);
         inventory = Map.copyOf(inventory);
         this.revision = revision;
         this.key = key;
         this.sourceCount = sourceCount;
         this.input = input;
         this.inventory = inventory;
      }

      public int accepted(int var1, int var2, Map<SellMenuLogic.ItemKey, Integer> var3, Map<SellMenuLogic.ItemKey, Integer> var4) {
         if (this.revision != var1 && var2 >= 0 && var2 < this.sourceCount) {
            int var5 = this.sourceCount - var2;
            return SellMenuLogic.adjustCount(this.input, this.key, var5).equals(var3)
                  && SellMenuLogic.adjustCount(this.inventory, this.key, -var5).equals(var4)
               ? var5
               : 0;
         } else {
            return 0;
         }
      }
   }

   public enum TransferVerdict {
      ACCEPT,
      ACCEPT_BULK,
      REJECT;
   }
}
