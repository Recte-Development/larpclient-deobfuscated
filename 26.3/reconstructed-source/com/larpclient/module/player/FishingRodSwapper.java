package com.larpclient.module.player;

import com.larpclient.util.world.DispenserRegistry;
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
public final class FishingRodSwapper {
   private static final int minLureLevel = 3;
   private static final FishingRodSwapper instance = new FishingRodSwapper();
   public boolean spoofEnabled;
   public boolean autoSwapEnabled;
   private boolean locked;
   private boolean swapping;
   private long lastSwapTime;
   private int overrideSlot = -1;
   private ItemStack displayStack = ItemStack.EMPTY;

   private FishingRodSwapper() {
   }

   public static FishingRodSwapper getInstance() {
      return instance;
   }

   public void toggleSpoof() {
      this.spoofEnabled = (boolean)(!this.spoofEnabled ? 1 : 0);
   }

   public void setAutoSwap(boolean var1) {
      this.autoSwapEnabled = (boolean)(var1 && !DispenserRegistry.getInstance().isLookingAtUnassigned() ? 1 : 0);
   }

   public void toggleAutoSwap() {
      this.setAutoSwap(!this.autoSwapEnabled);
   }

   public boolean isLocked() {
      return this.locked;
   }

   public void lock() {
      this.locked = (boolean)1;
   }

   public void unlock() {
      this.locked = (boolean)0;
   }

   public boolean isAutoSwapReady() {
      Minecraft var1 = Minecraft.getInstance();
      if (this.autoSwapEnabled && var1 != null && var1.player != null && var1.level != null && var1.gameMode != null && var1.getConnection() != null) {
         int var2 = this.findRodSlot();
         return var2 >= 0 && var2 < 9;
      } else {
         return false;
      }
   }

   public boolean setAutoSwap() {
      if (!this.spoofEnabled && !this.autoSwapEnabled && !this.swapping) {
         return false;
      }

      int var1 = this.findRodSlot();
      return var1 >= 0 && var1 < 9;
   }

   public void setOverrideSlot(int var1) {
      this.overrideSlot = var1;
   }

   public int getActiveSlot() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1 == null || var1.player == null) {
         return 0;
      } else {
         return this.setAutoSwap() && this.overrideSlot >= 0 && this.overrideSlot < 9 ? this.overrideSlot : var1.player.getInventory().getSelectedSlot();
      }
   }

   public int findRodSlot() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1 != null && var1.player != null) {
         Inventory var2 = var1.player.getInventory();

         for (int var3 = 0; var3 < var2.getContainerSize(); var3++) {
            if (isLureRod(var2.getItem(var3))) {
               return var3;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   private static boolean isLureRod(ItemStack var0) {
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

   private static boolean lock(ItemStack var0) {
      return var0 != null && !var0.isEmpty() && var0.is(Items.FISHING_ROD);
   }

   private static ItemStack stripRodData(ItemStack var0) {
      if (!lock(var0)) {
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

   public ItemStack unlock(int var1, ItemStack var2) {
      if (var1 < 0) {
         return var2;
      } else if ((this.spoofEnabled || this.autoSwapEnabled || this.swapping) && var1 == this.findRodSlot()) {
         return !this.displayStack.isEmpty() ? this.displayStack : ItemStack.EMPTY;
      } else {
         return var2;
      }
   }

   public ItemStack stripRodData() {
      return this.displayStack;
   }

   public void clearDisplayStack() {
      this.displayStack = ItemStack.EMPTY;
   }

   public void accumulateStack(ItemStack var1) {
      if (var1 != null && !var1.isEmpty()) {
         if (this.displayStack.isEmpty()) {
            this.displayStack = var1.copy();
         } else if (ItemStack.matches(this.displayStack, var1)) {
            this.displayStack.grow(var1.getCount());
            if (this.displayStack.getCount() > this.displayStack.getMaxStackSize()) {
               this.displayStack.setCount(this.displayStack.getMaxStackSize());
            }
         } else {
            this.displayStack = var1.copy();
         }
      }
   }

   public ItemStack isAutoSwapReady(int var1, ItemStack var2) {
      return !this.spoofEnabled && !this.autoSwapEnabled ? var2 : stripRodData(var2);
   }

   public boolean isHoldingOtherRod() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1 != null && var1.player != null) {
         return var1.player.getInventory().getSelectedSlot() == this.findRodSlot() ? false : lock(var1.player.getMainHandItem());
      } else {
         return false;
      }
   }

   public void tick(Minecraft var1) {
      if (this.autoSwapEnabled && var1 != null && var1.player != null && var1.gameMode != null) {
         if (var1.gui.screen() == null) {
            int var2 = this.findRodSlot();
            if (var2 >= 9 && var2 < 36) {
               long var3 = System.currentTimeMillis();
               if (var3 - this.lastSwapTime >= 250L) {
                  this.lastSwapTime = var3;
                  this.swapIntoHotbar(var1, var2);
               }
            }
         }
      }
   }

   private void swapIntoHotbar(Minecraft var1, int var2) {
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
            this.swapping = (boolean)1;

            try {
               var1.gameMode.handleContainerInput(var1.player.containerMenu.containerId, var12, var4, ContainerInput.SWAP, var1.player);
            } finally {
               this.swapping = (boolean)0;
            }
         }
      }
   }

   public String isLocked() {
      return this.autoSwapEnabled ? "On" : "Off";
   }
}
