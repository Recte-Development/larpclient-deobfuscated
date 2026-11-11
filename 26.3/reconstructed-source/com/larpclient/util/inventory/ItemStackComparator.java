package com.larpclient.util.inventory;

import java.util.HashSet;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.PlainTextContents.LiteralContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;

final class ItemStackComparator {
   private static final Pattern mONEY_PATTERN = Pattern.compile("\\$(?:[0-9]{1,3}(?:,[0-9]{3})+|[0-9]+)(?:\\.[0-9]+)?[kKmMbB]?(?![\\w.,])");
   private static final Pattern wORTH_PATTERN = Pattern.compile("Worth: " + mONEY_PATTERN.pattern());
   private static final Pattern eSTIMATE_PATTERN = Pattern.compile("~" + mONEY_PATTERN.pattern().replace("\\$", "\\$ "));

   static boolean areMapsEquivalent(ItemStack var0, ItemStack var1) {
      return !var0.isEmpty()
            && !var1.isEmpty()
            && var0.is(Items.FILLED_MAP)
            && var1.is(Items.FILLED_MAP)
            && var0.get(DataComponents.MAP_ID) != null
            && Objects.equals(var0.get(DataComponents.MAP_ID), var1.get(DataComponents.MAP_ID))
         ? areStacksEquivalent(var0, var1)
         : false;
   }

   static boolean areStacksEquivalent(ItemStack var0, ItemStack var1) {
      if (!var0.isEmpty() && !var1.isEmpty() && var0.getItem() == var1.getItem()) {
         HashSet var2 = new HashSet(var0.getComponents().keySet());
         var2.addAll(var1.getComponents().keySet());

         for (DataComponentType var4 : var2) {
            if (var4 != DataComponents.LORE && !Objects.equals(var0.getComponents().get(var4), var1.getComponents().get(var4))) {
               return false;
            }
         }

         ItemLore var6 = (ItemLore)var0.get(DataComponents.LORE);
         ItemLore var7 = (ItemLore)var1.get(DataComponents.LORE);
         if (var6 != null
            && var7 != null
            && !var6.lines().equals(var7.lines())
            && var6.lines().size() == var7.lines().size()
            && var6.lines().size() <= 32
            && var6.styledLines().size() == var6.lines().size()
            && var7.styledLines().size() == var7.lines().size()) {
            for (int var5 = 0; var5 < var6.lines().size(); var5++) {
               if (!areLoreLinesEquivalent((Component)var6.lines().get(var5), (Component)var7.lines().get(var5))
                  || !areLoreLinesEquivalent((Component)var6.styledLines().get(var5), (Component)var7.styledLines().get(var5))) {
                  return false;
               }
            }

            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   static boolean areLoreLinesEquivalent(Component var0, Component var1) {
      if (var0.equals(var1)) {
         return true;
      }

      String var2 = var0.getString();
      String var3 = var1.getString();
      return var2.length() <= 512
         && var3.length() <= 512
         && (
            wORTH_PATTERN.matcher(var2).matches() && wORTH_PATTERN.matcher(var3).matches()
               || eSTIMATE_PATTERN.matcher(var2).matches() && eSTIMATE_PATTERN.matcher(var3).matches()
         )
         && areComponentsEquivalent(var0, var1, 0, false);
   }

   private static boolean areComponentsEquivalent(Component var0, Component var1, int var2, boolean var3) {
      if (var0.equals(var1)) {
         return true;
      }

      if (var2 <= 16 && var0.getStyle().equals(var1.getStyle()) && var0.getSiblings().size() == var1.getSiblings().size() && var0.getSiblings().size() <= 32) {
         if (!var0.getContents().equals(var1.getContents())) {
            if (!(var0.getContents() instanceof LiteralContents var4) || !(var1.getContents() instanceof LiteralContents var5)) {
               return false;
            }

            int var12 = var3 && mONEY_PATTERN.matcher("$" + var4.text()).matches() && mONEY_PATTERN.matcher("$" + var5.text()).matches() ? 1 : 0;
            if (var12 == 0 && !differOnlyInMoneyValues(var4.text(), var5.text())) {
               return false;
            }
         }

         for (int var9 = 0; var9 < var0.getSiblings().size(); var9++) {
            Component var10 = var9 == 0 ? null : (Component)var0.getSiblings().get(var9 - 1);
            int var13 = var10 != null
                  && var10.getSiblings().isEmpty()
                  && var10.getContents() instanceof LiteralContents var7
                  && (var7.text().equals("$") || var7.text().equals("$ "))
                  && var10.equals(var1.getSiblings().get(var9 - 1))
               ? 1
               : 0;
            if (!areComponentsEquivalent((Component)var0.getSiblings().get(var9), (Component)var1.getSiblings().get(var9), var2 + 1, (boolean)var13)) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private static boolean differOnlyInMoneyValues(String var0, String var1) {
      if (var0.length() <= 512 && var1.length() <= 512) {
         Matcher var2 = mONEY_PATTERN.matcher(var0);
         Matcher var3 = mONEY_PATTERN.matcher(var1);
         int var4 = 0;
         int var5 = 0;
         byte var6 = 0;

         while (var2.find()) {
            if (!var3.find() || !var0.substring(var4, var2.start()).equals(var1.substring(var5, var3.start()))) {
               return false;
            }

            if (!var2.group().equals(var3.group())) {
               var6 = 1;
            }

            var4 = var2.end();
            var5 = var3.end();
         }

         return var6 != 0 && !var3.find() && var0.substring(var4).equals(var1.substring(var5));
      } else {
         return false;
      }
   }

   static String describeLore(ItemStack var0) {
      ItemLore var1 = (ItemLore)var0.get(DataComponents.LORE);
      if (var1 == null) {
         return "absent";
      }

      StringBuilder var2 = new StringBuilder();

      for (Component var4 : var1.lines().stream().limit(8L).toList()) {
         String var5 = var4.toString().replace('\n', ' ').replace('\r', ' ');
         var2.append('[').append(var5, 0, Math.min(var5.length(), 256)).append(']');
      }

      return "lines=" + var1.lines().size() + var2;
   }
}
