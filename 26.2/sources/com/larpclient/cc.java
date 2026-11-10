package com.larpclient;

import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

@Environment(EnvType.CLIENT)
public final class cc {
   private static final int C = 3;
   private static final cc j = new cc();
   public boolean Y;
   public boolean y;
   private boolean q;
   private boolean a;
   private long m;
   private int l = -1;
   private ItemStack A = ItemStack.EMPTY;

   private cc() {
   }

   public static cc z() {
      return j;
   }

   public void q() {
      this.Y = (boolean)(!this.Y ? 1 : 0);
   }

   public void F(boolean var1) {
      this.y = (boolean)(var1 && !gj.c().F() ? 1 : 0);
   }

   public void I() {
      this.F(!this.y);
   }

   public boolean L() {
      return this.q;
   }

   public void E() {
      this.q = (boolean)1;
   }

   public void c() {
      this.q = (boolean)0;
   }

   public boolean z() {
      Minecraft var1 = Minecraft.getInstance();
      if (this.y && var1 != null && var1.player != null && var1.level != null && var1.gameMode != null && var1.getConnection() != null) {
         int var2 = this.W();
         return var2 >= 0 && var2 < 9;
      } else {
         return false;
      }
   }

   public boolean v() {
      if (!this.Y && !this.y && !this.a) {
         return false;
      }

      int var1 = this.W();
      return var1 >= 0 && var1 < 9;
   }

   public void l(int var1) {
      this.l = var1;
   }

   public int g() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1 == null || var1.player == null) {
         return 0;
      } else {
         return this.v() && this.l >= 0 && this.l < 9 ? this.l : var1.player.getInventory().getSelectedSlot();
      }
   }

   public int W() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1 != null && var1.player != null) {
         Inventory var2 = var1.player.getInventory();

         for (int var3 = 0; var3 < var2.getContainerSize(); var3++) {
            if (i(var2.getItem(var3))) {
               return var3;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   private static boolean i(ItemStack var0) {
      if (var0 != null && !var0.isEmpty() && var0.is(Items.FISHING_ROD)) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1 != null && var1.level != null) {
            Registry var2 = var1.level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            Optional var3 = var2.getOptional(Enchantments.LURE);
            return var3.isEmpty() ? false : EnchantmentHelper.getItemEnchantmentLevel(var2.wrapAsHolder((Enchantment)var3.get()), var0) >= 3;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static boolean x(ItemStack var0) {
      return var0 != null && !var0.isEmpty() && var0.is(Items.FISHING_ROD);
   }

   private static ItemStack w(ItemStack var0) {
      if (!x(var0)) {
         return var0;
      }

      ItemStack var1 = var0.copy();
      var1.remove(DataComponents.ENCHANTMENTS);
      var1.remove(DataComponents.STORED_ENCHANTMENTS);
      var1.remove(DataComponents.ITEM_NAME);
      var1.remove(DataComponents.CUSTOM_NAME);
      var1.remove(DataComponents.LORE);
      return var1;
   }

   public ItemStack u(int var1, ItemStack var2) {
      if (var1 < 0) {
         return var2;
      } else if ((this.Y || this.y || this.a) && var1 == this.W()) {
         return !this.A.isEmpty() ? this.A : ItemStack.EMPTY;
      } else {
         return var2;
      }
   }

   public ItemStack g() {
      return this.A;
   }

   public void n() {
      this.A = ItemStack.EMPTY;
   }

   public void M(ItemStack var1) {
      if (var1 != null && !var1.isEmpty()) {
         if (this.A.isEmpty()) {
            this.A = var1.copy();
         } else if (ItemStack.matches(this.A, var1)) {
            this.A.grow(var1.getCount());
            if (this.A.getCount() > this.A.getMaxStackSize()) {
               this.A.setCount(this.A.getMaxStackSize());
            }
         } else {
            this.A = var1.copy();
         }
      }
   }

   public ItemStack s(int var1, ItemStack var2) {
      return !this.Y && !this.y ? var2 : w(var2);
   }

   public boolean R() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1 != null && var1.player != null) {
         return var1.player.getInventory().getSelectedSlot() == this.W() ? false : x(var1.player.getMainHandItem());
      } else {
         return false;
      }
   }

   public void J(Minecraft var1) {
      if (this.y && var1 != null && var1.player != null && var1.gameMode != null) {
         if (var1.gui.screen() == null) {
            int var2 = this.W();
            if (var2 >= 9 && var2 < 36) {
               long var3 = System.currentTimeMillis();
               if (var3 - this.m >= 250L) {
                  this.m = var3;
                  this.y(var1, var2);
               }
            }
         }
      }
   }

   private void y(Minecraft var1, int var2) {
      Inventory var3 = var1.player.getInventory();
      int var4 = -1;

      for (int var5 = 0; var5 < 9; var5++) {
         if (var3.getItem(var5).isEmpty()) {
            var4 = var5;
            break;
         }
      }

      if (var4 < 0) {
         int var11 = var3.getSelectedSlot();
         if (var3.getItem(var11).isEmpty()) {
            var4 = var11;
         }
      }

      if (var4 >= 0) {
         int var12 = -1;

         for (Slot var7 : var1.player.containerMenu.slots) {
            if (var7.container == var3 && var7.getContainerSlot() == var2) {
               var12 = var7.index;
               break;
            }
         }

         if (var12 >= 0) {
            this.a = (boolean)1;

            try {
               var1.gameMode.handleContainerInput(var1.player.containerMenu.containerId, var12, var4, ContainerInput.SWAP, var1.player);
            } finally {
               this.a = (boolean)0;
            }
         }
      }
   }

   public String h() {
      return this.y ? "On" : "Off";
   }
}
