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
public final class hh {
   private static final int l = 3;
   private static final hh p = new hh();
   public boolean x;
   public boolean s;
   private boolean L;
   private boolean O;
   private long r;
   private int a = -1;
   private ItemStack y = ItemStack.EMPTY;

   private hh() {
   }

   public static hh R() {
      return p;
   }

   public void b() {
      this.x = (boolean)(!this.x ? 1 : 0);
   }

   public void U(boolean var1) {
      this.s = (boolean)(var1 && !hk.e().q() ? 1 : 0);
   }

   public void T() {
      this.U(!this.s);
   }

   public boolean y() {
      return this.L;
   }

   public void a() {
      this.L = (boolean)1;
   }

   public void o() {
      this.L = (boolean)0;
   }

   public boolean P() {
      Minecraft var1 = Minecraft.getInstance();
      if (this.s && var1 != null && var1.player != null && var1.level != null && var1.gameMode != null && var1.getConnection() != null) {
         int var2 = this.t();
         return var2 >= 0 && var2 < 9;
      } else {
         return false;
      }
   }

   public boolean U() {
      if (!this.x && !this.s && !this.O) {
         return false;
      }

      int var1 = this.t();
      return var1 >= 0 && var1 < 9;
   }

   public void A(int var1) {
      this.a = var1;
   }

   public int j() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1 == null || var1.player == null) {
         return 0;
      } else {
         return this.U() && this.a >= 0 && this.a < 9 ? this.a : var1.player.getInventory().getSelectedSlot();
      }
   }

   public int t() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1 != null && var1.player != null) {
         Inventory var2 = var1.player.getInventory();

         for (int var3 = 0; var3 < var2.getContainerSize(); var3++) {
            if (K(var2.getItem(var3))) {
               return var3;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   private static boolean K(ItemStack var0) {
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

   private static boolean a(ItemStack var0) {
      return var0 != null && !var0.isEmpty() && var0.is(Items.FISHING_ROD);
   }

   private static ItemStack w(ItemStack var0) {
      if (!a(var0)) {
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

   public ItemStack o(int var1, ItemStack var2) {
      if (var1 < 0) {
         return var2;
      } else if ((this.x || this.s || this.O) && var1 == this.t()) {
         return !this.y.isEmpty() ? this.y : ItemStack.EMPTY;
      } else {
         return var2;
      }
   }

   public ItemStack w() {
      return this.y;
   }

   public void m() {
      this.y = ItemStack.EMPTY;
   }

   public void I(ItemStack var1) {
      if (var1 != null && !var1.isEmpty()) {
         if (this.y.isEmpty()) {
            this.y = var1.copy();
         } else if (ItemStack.matches(this.y, var1)) {
            this.y.grow(var1.getCount());
            if (this.y.getCount() > this.y.getMaxStackSize()) {
               this.y.setCount(this.y.getMaxStackSize());
            }
         } else {
            this.y = var1.copy();
         }
      }
   }

   public ItemStack P(int var1, ItemStack var2) {
      return !this.x && !this.s ? var2 : w(var2);
   }

   public boolean Y() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1 != null && var1.player != null) {
         return var1.player.getInventory().getSelectedSlot() == this.t() ? false : a(var1.player.getMainHandItem());
      } else {
         return false;
      }
   }

   public void c(Minecraft var1) {
      if (this.s && var1 != null && var1.player != null && var1.gameMode != null) {
         if (var1.gui.screen() == null) {
            int var2 = this.t();
            if (var2 >= 9 && var2 < 36) {
               long var3 = System.currentTimeMillis();
               if (var3 - this.r >= 250L) {
                  this.r = var3;
                  this.q(var1, var2);
               }
            }
         }
      }
   }

   private void q(Minecraft var1, int var2) {
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
            this.O = (boolean)1;

            try {
               var1.gameMode.handleContainerInput(var1.player.containerMenu.containerId, var12, var4, ContainerInput.SWAP, var1.player);
            } finally {
               this.O = (boolean)0;
            }
         }
      }
   }

   public String y() {
      return this.s ? "On" : "Off";
   }
}
