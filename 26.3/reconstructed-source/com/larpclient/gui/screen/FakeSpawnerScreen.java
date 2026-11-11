package com.larpclient.gui.screen;

import com.larpclient.hud.VisualInventory;
import com.larpclient.social.misc.PlayerMessages;
import com.larpclient.util.entity.FarmMobType;
import com.larpclient.util.inventory.FakeLootWithdrawer;
import com.larpclient.util.misc.KeyCodes;
import com.larpclient.util.world.FakeSpawnerController;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.ItemSlotMouseAction;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class FakeSpawnerScreen extends ContainerScreen {
   private final BlockPos spawnerPos;
   private ItemStack heldStack = ItemStack.EMPTY;
   private int heldSlot = -1;
   private long releaseAt;
   private long openMenuAt;
   private int lootCountA;
   private int lootCountB;
   private boolean openMenuPending;
   private static final SimpleContainer placeholderContainer = new SimpleContainer(1);

   private void releaseHeldItem() {
      if (!this.heldStack.isEmpty()) {
         if (this.heldSlot < 54) {
            ((ChestMenu)this.menu).getSlot(this.heldSlot).set(this.heldStack);
         }

         this.heldStack = ItemStack.EMPTY;
         this.heldSlot = -1;
      }
   }

   private void pickUpSlot(int var1) {
      this.heldSlot = var1;
      Slot var2 = ((ChestMenu)this.menu).getSlot(var1);
      this.heldStack = (var1 < 54 ? var2 : VisualInventory.wrapSlot(this.menu, var2)).getItem().copy();
      if (var1 < 54) {
         var2.set(ItemStack.EMPTY);
      }

      ((ChestMenu)this.menu).setCarried(this.heldStack);
      this.releaseAt = System.currentTimeMillis() + 200L;
   }

   private int countItem(Item var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 45; var3++) {
         if (((ChestMenu)this.menu).getSlot(var3).getItem().is(var1)) {
            var2 += ((ChestMenu)this.menu).getSlot(var3).getItem().getCount();
         }
      }

      return var2;
   }

   public FakeSpawnerScreen(ChestMenu var1, Inventory var2, Component var3) {
      this(var1, var2, var3, null);
   }

   public FakeSpawnerScreen(ChestMenu var1, Inventory var2, Component var3, BlockPos var4) {
      super(var1, var2, var3);
      this.spawnerPos = var4 == null ? null : var4.immutable();
   }

   protected void addItemSlotMouseAction(ItemSlotMouseAction var1) {
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (!this.heldStack.isEmpty() && System.currentTimeMillis() >= this.releaseAt) {
         this.releaseHeldItem();
      }

      if (this.openMenuPending && System.currentTimeMillis() >= this.openMenuAt) {
         this.openMenuPending = (boolean)0;
         this.minecraft
            .gui
            .setScreen(new ConfirmSellScreen(this.spawnerPos, FakeSpawnerController.getInstance().getKind(this.spawnerPos), this.lootCountA, this.lootCountB));
      } else {
         if (this.minecraft.player != null) {
            ((ChestMenu)this.menu).setCarried(this.heldStack.isEmpty() ? this.minecraft.player.inventoryMenu.getCarried() : this.heldStack);
         }

         super.extractRenderState(var1, var2, var3, var4);
         this.extractTooltip(var1, var2, var3);
      }
   }

   protected void extractSlot(GuiGraphicsExtractor var1, Slot var2, int var3, int var4) {
      if (this.heldSlot >= 54 && var2.index == this.heldSlot && !this.heldStack.isEmpty()) {
         Slot var5 = new Slot(placeholderContainer, 0, var2.x, var2.y);
         var5.index = var2.index;
         super.extractSlot(var1, var5, var3, var4);
      } else {
         super.extractSlot(var1, var2, var3, var4);
      }
   }

   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.heldStack.isEmpty() && !this.openMenuPending) {
         if (KeyCodes.mouseButtonOf(var1) != 0 && KeyCodes.mouseButtonOf(var1) != 1) {
            return true;
         }

         for (int var3 = 0; var3 < ((ChestMenu)this.menu).slots.size(); var3++) {
            Slot var4 = (Slot)((ChestMenu)this.menu).slots.get(var3);
            int var5 = this.leftPos + var4.x;
            int var6 = this.topPos + var4.y;
            if (!(var1.x() < var5) && !(var1.x() >= var5 + 16) && !(var1.y() < var6) && !(var1.y() >= var6 + 16)) {
               FakeSpawnerController var7 = FakeSpawnerController.getInstance();
               if (var3 < 45 && FarmMobType.isDropItem(var4.getItem().getItem())) {
                  int var11 = !this.minecraft.options.keyShift.isDown() && !this.minecraft.player.isShiftKeyDown() ? 0 : 1;
                  int var12 = var11 != 0
                     ? var7.replayCreativeClicks(var4.getItem().getItem(), var4.getItem().getCount())
                     : var7.isActive(var4.getItem().getItem(), KeyCodes.mouseButtonOf(var1) == 1 ? 1 : var4.getItem().getCount());
                  if (var12 > 0) {
                     var4.getItem().shrink(var12);
                  }
               } else if ((var3 == 48 || var3 == 50) && this.minecraft.player.inventoryMenu.getCarried().isEmpty()) {
                  FarmMobType var8 = var7.getKind(this.spawnerPos);
                  int var9 = this.countItem(var8.primaryDrop);
                  int var10 = this.countItem(var8.secondaryDrop);
                  if (var9 + var10 == 0) {
                     this.pickUpSlot(var3);
                     if (var3 == 48) {
                        PlayerMessages.sendMessage(Component.literal("Spawner has no loot").withStyle(ChatFormatting.RED), true);
                     }
                  } else if (var3 == 48) {
                     this.pickUpSlot(var3);
                     this.lootCountA = var9;
                     this.lootCountB = var10;
                     this.openMenuAt = this.releaseAt + 50L;
                     this.openMenuPending = (boolean)1;
                  }
               } else if (var3 == 49 && this.minecraft.player.inventoryMenu.getCarried().isEmpty()) {
                  this.pickUpSlot(var3);
               } else if (var3 < 54) {
                  var7.isFakeSpawnerBlock(var3);
               } else if (var3 >= 54
                  && !VisualInventory.wrapSlot(this.menu, var4).getItem().isEmpty()
                  && this.minecraft.player.inventoryMenu.getCarried().isEmpty()) {
                  this.pickUpSlot(var3);
               }

               if (this.minecraft.player != null) {
                  ((ChestMenu)this.menu).setCarried(this.heldStack.isEmpty() ? this.minecraft.player.inventoryMenu.getCarried() : this.heldStack);
               }
               break;
            }
         }

         return true;
      } else {
         return true;
      }
   }

   protected void slotClicked(Slot var1, int var2, int var3, ContainerInput var4) {
   }

   public void onClose() {
      this.minecraft.gui.setScreen(null);
      FakeLootWithdrawer.withdraw(this.spawnerPos);
   }

   public void removed() {
      this.releaseHeldItem();
   }
}
