package com.larpclient.util.inventory;

import com.larpclient.util.misc.DropperGameSpoofer;
import com.larpclient.util.world.DispenserRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class DispenserRemap {
   private static final DispenserRemap iNSTANCE = new DispenserRemap();
   public Item tntItem = Items.TNT;
   public Item paperItem = Items.PAPER;
   public boolean enabled;
   public boolean refill;
   private final DropperGameSpoofer remapper = new DropperGameSpoofer(true, true);
   private boolean swapping;
   private boolean wasActive;

   public static DispenserRemap getInstance() {
      return iNSTANCE;
   }

   public void arm() {
      this.refill = (boolean)1;
      this.remapper.selectedRank = 2;
   }

   public boolean configure(Minecraft var1, BlockPos var2, DispenserMenu var3) {
      this.remapper.selectedRank = this.refill ? 2 : 1;
      if (var2 != null && var1 != null) {
         try {
            this.remapper.setupFromDropper(var1, var2, var3);
         } catch (IllegalArgumentException var5) {
            return false;
         }

         this.paperItem = this.remapper.getSetupStack(1).getItem();
         this.tntItem = this.remapper.getSetupStack(2).getItem();
         return true;
      } else {
         return false;
      }
   }

   private boolean isMenuTracked(Minecraft var1, DispenserMenu var2) {
      DispenserRegistry var3 = DispenserRegistry.getInstance();
      BlockPos var4 = this.remapper.hasDropper(0);
      return this.enabled
         && var4 != null
         && var1 != null
         && var1.player != null
         && var2 == var1.player.containerMenu
         && var2.containerId == var3.getActiveKey()
         && var3.getActiveKey(var1, var4).equals(var3.getActiveKey());
   }

   public void tick(Minecraft var1) {
      if (!this.swapping) {
         if (var1 != null && var1.player != null && var1.level != null) {
            if (!this.enabled) {
               if (this.wasActive) {
                  this.remapper.reset(var1);
               }

               this.wasActive = (boolean)0;
            } else {
               this.wasActive = (boolean)1;
               if (this.remapper.refreshDisplay()) {
                  this.refill = (boolean)0;
               }

               this.remapper.selectedRank = this.refill ? 2 : 1;
               this.remapper.tick(var1);
               if (this.remapper.refreshDisplay()) {
                  this.refill = (boolean)0;
               }
            }
         } else {
            if (this.wasActive && var1 != null) {
               this.remapper.reset(var1);
            }

            this.wasActive = (boolean)0;
         }
      }
   }

   public ItemStack tick(ItemStack var1) {
      return this.enabled && var1 != null && !var1.isEmpty() ? this.remapper.getDisplayStack(var1) : var1;
   }

   public ItemStack remapStack(ItemStack var1) {
      return this.tick(var1);
   }

   public ItemStack remapForPlayer(LivingEntity var1, ItemStack var2) {
      Minecraft var3 = Minecraft.getInstance();
      return var3 != null && var1 == var3.player ? this.tick(var2) : var2;
   }

   public ItemStack remapItemEntity(ItemEntity var1, ItemStack var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (this.enabled && var1 != null && var3 != null && var3.level != null) {
         ItemStack var4 = this.remapper.getItemEntityDisplay(var3, var1);
         return var4 == var1.getItem() ? var2 : var4;
      } else {
         return var2;
      }
   }

   public ItemStack remapSlotItem(Minecraft var1, Slot var2) {
      return var1 != null
            && var1.player != null
            && var2.container != var1.player.getInventory()
            && var1.player.containerMenu instanceof DispenserMenu var3
            && var2.getContainerSlot() >= 0
            && var2.getContainerSlot() < 9
            && this.isMenuTracked(var1, var3)
         ? this.remapper.getMenuSlotDisplay(var1, var3, 0, var2.getContainerSlot())
         : null;
   }

   public boolean handleSlotClick(Minecraft var1, int var2, int var3, int var4, ContainerInput var5) {
      if (this.swapping
         || var1 == null
         || var1.player == null
         || var1.gameMode == null
         || !(
            var1.player.containerMenu instanceof DispenserMenu var6
               && var6.containerId == var2
               && var3 >= 0
               && var3 < var6.slots.size()
               && this.isMenuTracked(var1, var6)
         )) {
         return false;
      }

      if (var5 == ContainerInput.QUICK_MOVE && var6.getSlot(var3).container == var1.player.getInventory()) {
         int var18 = var6.getSlot(var3).getContainerSlot();
         int var8 = this.remapper.copySpoofed(var1, var18);
         if (var8 < 0 || var8 >= 9) {
            return false;
         }

         if (var6.getCarried().isEmpty() && var6.getSlot(var8).getItem().isEmpty() && var6.getSlot(var3).getItem().getCount() == 1) {
            this.remapper.setupWithItems(var6, var18);
            this.swapping = (boolean)1;

            try {
               if (var18 < 9) {
                  var1.gameMode.handleContainerInput(var2, var8, var18, ContainerInput.SWAP, var1.player);
               } else {
                  var1.gameMode.handleContainerInput(var2, var3, 40, ContainerInput.SWAP, var1.player);
                  var1.gameMode.handleContainerInput(var2, var8, 40, ContainerInput.SWAP, var1.player);
                  var1.gameMode.handleContainerInput(var2, var3, 40, ContainerInput.SWAP, var1.player);
               }
            } finally {
               this.swapping = (boolean)0;
            }

            this.remapper.tick(var1);
            return true;
         } else {
            return true;
         }
      } else {
         if (var3 >= 9) {
            return false;
         }

         int var17 = this.remapper.trackMenu(var1, var6, 0, var3);
         if (var17 == var3) {
            return false;
         }

         this.swapping = (boolean)1;

         try {
            var1.gameMode.handleContainerInput(var2, var17, var4, var5, var1.player);
         } finally {
            this.swapping = (boolean)0;
         }

         return true;
      }
   }
}
