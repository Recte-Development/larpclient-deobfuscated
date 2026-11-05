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
public final class pj {
   private static final int I = 3;
   private static final pj h = new pj();
   public boolean Z;
   public boolean F;
   private boolean D;
   private boolean r;
   private long W;
   private int Y = -1;
   private ItemStack O = ItemStack.EMPTY;

   private pj() {
   }

   public static pj U() {
      return h;
   }

   public void g() {
      this.Z = (boolean)(!this.Z ? 1 : 0);
   }

   public void K(boolean var1) {
      this.F = (boolean)(var1 && !ep.H().O() ? 1 : 0);
   }

   public void B() {
      this.K(!this.F);
   }

   public boolean V() {
      return this.D;
   }

   public void a() {
      this.D = (boolean)1;
   }

   public void N() {
      this.D = (boolean)0;
   }

   public boolean W() {
      Minecraft var1 = Minecraft.getInstance();
      if (this.F && var1 != null && var1.player != null && var1.level != null && var1.gameMode != null && var1.getConnection() != null) {
         int var2 = this.B();
         return var2 >= 0 && var2 < 9;
      } else {
         return false;
      }
   }

   public boolean c() {
      if (!this.Z && !this.F && !this.r) {
         return false;
      }

      int var1 = this.B();
      return var1 >= 0 && var1 < 9;
   }

   public void A(int var1) {
      this.Y = var1;
   }

   public int N() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1 == null || var1.player == null) {
         return 0;
      } else {
         return this.c() && this.Y >= 0 && this.Y < 9 ? this.Y : var1.player.getInventory().getSelectedSlot();
      }
   }

   public int B() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1 != null && var1.player != null) {
         Inventory var2 = var1.player.getInventory();

         for (int var3 = 0; var3 < var2.getContainerSize(); var3++) {
            if (F(var2.getItem(var3))) {
               return var3;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   private static boolean F(ItemStack var0) {
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

   private static boolean R(ItemStack var0) {
      return var0 != null && !var0.isEmpty() && var0.is(Items.FISHING_ROD);
   }

   private static ItemStack F(ItemStack var0) {
      if (!R(var0)) {
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

   public ItemStack a(int var1, ItemStack var2) {
      if (var1 < 0) {
         return var2;
      } else if ((this.Z || this.F || this.r) && var1 == this.B()) {
         return !this.O.isEmpty() ? this.O : ItemStack.EMPTY;
      } else {
         return var2;
      }
   }

   public ItemStack I() {
      return this.O;
   }

   public void C() {
      this.O = ItemStack.EMPTY;
   }

   public void H(ItemStack var1) {
      if (var1 != null && !var1.isEmpty()) {
         if (this.O.isEmpty()) {
            this.O = var1.copy();
         } else if (ItemStack.matches(this.O, var1)) {
            this.O.grow(var1.getCount());
            if (this.O.getCount() > this.O.getMaxStackSize()) {
               this.O.setCount(this.O.getMaxStackSize());
            }
         } else {
            this.O = var1.copy();
         }
      }
   }

   public ItemStack F(int var1, ItemStack var2) {
      return !this.Z && !this.F ? var2 : F(var2);
   }

   public boolean U() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1 != null && var1.player != null) {
         return var1.player.getInventory().getSelectedSlot() == this.B() ? false : R(var1.player.getMainHandItem());
      } else {
         return false;
      }
   }

   public void k(Minecraft var1) {
      if (this.F && var1 != null && var1.player != null && var1.gameMode != null) {
         if (var1.gui.screen() == null) {
            int var2 = this.B();
            if (var2 >= 9 && var2 < 36) {
               long var3 = System.currentTimeMillis();
               if (var3 - this.W >= 250L) {
                  this.W = var3;
                  this.t(var1, var2);
               }
            }
         }
      }
   }

   private void t(Minecraft var1, int var2) {
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
            this.r = (boolean)1;

            try {
               var1.gameMode.handleContainerInput(var1.player.containerMenu.containerId, var12, var4, ContainerInput.SWAP, var1.player);
            } finally {
               this.r = (boolean)0;
            }
         }
      }
   }

   public String A() {
      return this.F ? "On" : "Off";
   }
}
