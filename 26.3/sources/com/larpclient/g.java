package com.larpclient;

import com.larp.obf.q41a5851bcf8e876a.qb75a226e201a934e.q8ecc8da09c365bfb;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.dialog.DialogScreen;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.dialog.CommonDialogData;
import net.minecraft.server.dialog.Dialog;
import net.minecraft.server.dialog.Input;
import net.minecraft.server.dialog.body.DialogBody;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.ItemLore;

public final class g {
   private static final boolean F = Boolean.getBoolean("larp.menuTrace");
   private static int B;
   private static Object s;

   private g() {
   }

   public static void T(Minecraft var0) {
      if (F && var0.player != null) {
         AbstractContainerMenu var1 = var0.player.containerMenu;
         Screen var2 = var0.gui.screen();
         M(var2);
         if (var1 == var0.player.inventoryMenu) {
            B = 0;
         } else {
            String var3 = var2 == null ? "<no screen>" : var2.getTitle().getString();
            int var4 = Objects.hash(var1.containerId, var3);

            for (Slot var6 : var1.slots) {
               var4 = 31 * var4 + ItemStack.hashItemAndComponents(var6.getItem()) * 7 + var6.getItem().getCount();
            }

            if (var4 != B) {
               B = var4;
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

               _q.z("MenuTrace", "{}", var9);
            }
         }
      }
   }

   private static void M(Screen var0) {
      if (var0 != s) {
         s = var0;
         if (var0 instanceof DialogScreen var1) {
            Dialog var2 = ((q8ecc8da09c365bfb)var1).larp$dialog();
            if (var2 == null) {
               _q.z("MenuTrace", "Dialog '{}' with no dialog data", var0.getTitle().getString());
               return;
            }

            CommonDialogData var3 = var2.common();
            StringBuilder var4 = new StringBuilder("Dialog '")
               .append(var3.title().getString())
               .append("' type=")
               .append(var2.getClass().getSimpleName())
               .append(" inputs=")
               .append(var3.inputs().size());

            for (DialogBody var6 : var3.body()) {
               var4.append("\n  body ").append(var6);
            }

            for (Input var8 : var3.inputs()) {
               var4.append("\n  input ").append(var8);
            }

            var4.append("\n  raw ").append(var2);
            _q.z("MenuTrace", "{}", var4);
         } else if (var0 instanceof AbstractSignEditScreen) {
            _q.z("MenuTrace", "Sign editor '{}' opened ({})", var0.getTitle().getString(), var0.getClass().getSimpleName());
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
