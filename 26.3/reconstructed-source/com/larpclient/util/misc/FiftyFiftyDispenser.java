package com.larpclient.util.misc;

import com.larpclient.util.world.DispenserRegistry;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.DispenserScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public final class FiftyFiftyDispenser {
   private static final FiftyFiftyDispenser instance = new FiftyFiftyDispenser();
   private boolean enabled;
   private boolean awaitingMenuOpen;
   private boolean sendingOwnAction;
   private boolean menuSynced;
   private boolean wasTriggered;
   private boolean sourceSlotCleared;
   private boolean holdSlotFilled;
   public Item firstItem = Items.APPLE;
   public Item secondItem = Items.DIAMOND;
   public boolean pickFirst = (boolean)1;
   private int holdHotbarSlot = -1;
   private int removedSlot = -1;
   private int keptSlot = -1;
   private DispenserMenu menu;
   private BlockPos dispenserPos;
   private final ItemStack[] itemSnapshots = new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY};
   private final int[] itemSlots = new int[]{-1, -1};
   private ItemStack removedStack = ItemStack.EMPTY;
   private ItemStack keptStack = ItemStack.EMPTY;
   private int chosenIndex;
   private FiftyFiftyDispenser.State state = FiftyFiftyDispenser.State.IDLE;
   private long deadlineNanos;
   private long nextOpenNanos;

   public static FiftyFiftyDispenser getInstance() {
      return instance;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public boolean isActive() {
      return this.state != FiftyFiftyDispenser.State.IDLE;
   }

   public int getChosenIndex() {
      return this.pickFirst ? 1 : 2;
   }

   public ItemStack hideHeldStack(ItemStack var1) {
      Minecraft var2 = Minecraft.getInstance();
      return this.holdHotbarSlot >= 0 && this.isActive() && var2.player != null && var1 == var2.player.getInventory().getItem(this.holdHotbarSlot)
         ? ItemStack.EMPTY
         : var1;
   }

   public ItemStack getChosenIndex(Minecraft var1, AbstractContainerMenu var2, Slot var3) {
      if (this.state != FiftyFiftyDispenser.State.ABORTED && this.isActive() && var2 == this.menu && this.isMenuSynced()) {
         if (var3.container == var1.player.getInventory()) {
            return var3.getContainerSlot() == this.holdHotbarSlot ? ItemStack.EMPTY : null;
         } else {
            return var3.index == this.removedSlot && this.menu.getSlot(this.removedSlot) == var3 ? this.removedStack : null;
         }
      } else {
         return null;
      }
   }

   public String getItemName(int var1) {
      return this.itemSnapshots[var1].isEmpty() ? "ITEM " + (var1 + 1) : this.itemSnapshots[var1].getHoverName().getString();
   }

   public boolean isMenuSynced() {
      Minecraft var1 = Minecraft.getInstance();
      return this.menuSynced && this.menu != null && var1.player != null && var1.player.containerMenu == this.menu;
   }

   public void setChosenIndex(int var1) {
      if ((!this.isActive() || this.state == FiftyFiftyDispenser.State.TRIGGERED) && var1 >= 1 && var1 <= 2) {
         this.pickFirst = (boolean)(var1 == 1 ? 1 : 0);
      }
   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   private BlockPos returnHeldItem() {
      return DispenserRegistry.getInstance().onDispenserUpdate(DispenserRegistry.GameType.FIFTY_FIFTY);
   }

   public void isActive(Minecraft var1, DispenserMenu var2, BlockPos var3) {
      if (!this.isActive() && var3 != null) {
         this.menu = var2;
         this.dispenserPos = var3.immutable();
         this.menuSynced = (boolean)0;
      }
   }

   public boolean onMenuOpen(MenuType<?> var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (this.awaitingMenuOpen && var1 == MenuType.GENERIC_3x3 && var3.player != null) {
         this.menu = new DispenserMenu(var2, var3.player.getInventory());
         var3.player.containerMenu = this.menu;
         this.awaitingMenuOpen = (boolean)0;
         this.menuSynced = (boolean)0;
         return true;
      } else {
         return false;
      }
   }

   static int[] findItemSlots(List<ItemStack> var0) {
      int var1 = -1;
      int var2 = -1;

      for (int var3 = 0; var3 < 9; var3++) {
         if (!((ItemStack)var0.get(var3)).isEmpty()) {
            if (((ItemStack)var0.get(var3)).getCount() != 1) {
               return null;
            }

            if (var1 < 0) {
               var1 = var3;
            } else {
               if (var2 >= 0) {
                  return null;
               }

               var2 = var3;
            }
         }
      }

      return var2 >= 0 && !((ItemStack)var0.get(var1)).is(((ItemStack)var0.get(var2)).getItem()) ? new int[]{var1, var2} : null;
   }

   public void onContainerContent(int var1, List<ItemStack> var2, ItemStack var3) {
      if (this.menu != null && var1 == this.menu.containerId && var2.size() == this.menu.slots.size()) {
         this.menuSynced = (boolean)1;
         if (this.state == FiftyFiftyDispenser.State.IDLE) {
            int[] var4 = findItemSlots(var2);
            if (var4 != null) {
               for (int var5 = 0; var5 < 2; var5++) {
                  this.itemSlots[var5] = var4[var5];
                  this.itemSnapshots[var5] = ((ItemStack)var2.get(var4[var5])).copy();
               }

               this.firstItem = this.itemSnapshots[0].getItem();
               this.secondItem = this.itemSnapshots[1].getItem();
            }
         }

         this.onMenuOpen(var2, var3);
      }
   }

   public void onSlotUpdate(int var1, int var2, ItemStack var3) {
      if (this.menu != null && this.isActive() && this.state != FiftyFiftyDispenser.State.ABORTED) {
         if (var1 == this.menu.containerId || var1 == 0 && var2 == 36 + this.holdHotbarSlot) {
            if (this.state == FiftyFiftyDispenser.State.REMOVING || this.state == FiftyFiftyDispenser.State.RETURNING) {
               if (var2 == this.removedSlot) {
                  this.sourceSlotCleared = this.state == FiftyFiftyDispenser.State.REMOVING ? var3.isEmpty() : ItemStack.matches(var3, this.removedStack);
               }

               if (var2 == 36 + this.holdHotbarSlot) {
                  this.holdSlotFilled = this.state == FiftyFiftyDispenser.State.REMOVING ? ItemStack.matches(var3, this.removedStack) : var3.isEmpty();
               }

               if (this.sourceSlotCleared && this.holdSlotFilled && this.menu.getCarried().isEmpty()) {
                  if (this.state == FiftyFiftyDispenser.State.REMOVING) {
                     this.confirmTrigger(!ItemStack.matches(this.menu.getSlot(this.keptSlot).getItem(), this.keptStack));
                  } else {
                     this.hideHeldStack();
                  }
               }
            }

            if (this.state == FiftyFiftyDispenser.State.TRIGGERED && var2 == this.keptSlot && !ItemStack.matches(var3, this.keptStack)) {
               this.returnHeldItem();
            }
         }
      }
   }

   public void findItemSlots(int var1, ItemStack var2) {
      if (this.menu != null && var1 == this.holdHotbarSlot) {
         this.onSlotUpdate(this.menu.containerId, 36 + var1, var2);
      }
   }

   private void confirmTrigger(boolean var1) {
      this.state = FiftyFiftyDispenser.State.TRIGGERED;
      this.deadlineNanos = System.nanoTime() + 15000000000L;
      RigLog.logMessage("50/50", "Trigger swap confirmed: " + this.keptStack.getHoverName().getString());
      if (!this.enabled || this.getChosenIndex() != this.chosenIndex || var1) {
         this.returnHeldItem();
      }
   }

   private void hideHeldStack() {
      this.state = FiftyFiftyDispenser.State.IDLE;
      this.removedStack = ItemStack.EMPTY;
      this.holdHotbarSlot = -1;
      this.keptStack = ItemStack.EMPTY;
      RigLog.logMessage("50/50", "Returned held item; dispenser ready");
   }

   private void onMenuOpen(List<ItemStack> var1, ItemStack var2) {
      if (this.isActive() && this.state != FiftyFiftyDispenser.State.ABORTED && this.holdHotbarSlot >= 0 && var2.isEmpty()) {
         int var3 = 36 + this.holdHotbarSlot;
         if (this.state == FiftyFiftyDispenser.State.REMOVING
            && ((ItemStack)var1.get(this.removedSlot)).isEmpty()
            && ItemStack.matches((ItemStack)var1.get(var3), this.removedStack)) {
            this.confirmTrigger(!ItemStack.matches((ItemStack)var1.get(this.keptSlot), this.keptStack));
         } else if (this.state == FiftyFiftyDispenser.State.REMOVING && ((ItemStack)var1.get(var3)).isEmpty()) {
            this.state = FiftyFiftyDispenser.State.IDLE;
            this.holdHotbarSlot = -1;
            this.removedStack = ItemStack.EMPTY;
            this.keptStack = ItemStack.EMPTY;
            RigLog.logMessage("50/50", "Missed trigger: removal not accepted; no replay");
         } else if (this.state == FiftyFiftyDispenser.State.TRIGGERED && !ItemStack.matches((ItemStack)var1.get(this.keptSlot), this.keptStack)) {
            this.returnHeldItem();
         } else if (this.state == FiftyFiftyDispenser.State.RETURNING
            && ItemStack.matches((ItemStack)var1.get(this.removedSlot), this.removedStack)
            && ((ItemStack)var1.get(var3)).isEmpty()) {
            this.hideHeldStack();
         }
      }
   }

   private void returnHeldItem() {
      Minecraft var1 = Minecraft.getInstance();
      if (this.isMenuSynced()
         && this.menu.getSlot(this.removedSlot).getItem().isEmpty()
         && ItemStack.matches(var1.player.getInventory().getItem(this.holdHotbarSlot), this.removedStack)) {
         this.state = FiftyFiftyDispenser.State.RETURNING;
         this.deadlineNanos = System.nanoTime() + 15000000000L;
         this.swapSlots(this.removedSlot, this.holdHotbarSlot);
      } else {
         this.abort("return state changed");
      }
   }

   private void swapSlots(int var1, int var2) {
      this.sourceSlotCleared = this.holdSlotFilled = (boolean)0;
      Minecraft var3 = Minecraft.getInstance();
      this.sendingOwnAction = (boolean)1;

      try {
         var3.gameMode.handleContainerInput(this.menu.containerId, var1, var2, ContainerInput.SWAP, var3.player);
      } finally {
         this.sendingOwnAction = (boolean)0;
      }
   }

   public int overrideInputFlags(AbstractContainerMenu var1, int var2) {
      return this.sendingOwnAction && var1 == this.menu ? 32768 : var2;
   }

   public boolean shouldBlockInput() {
      return this.isActive() && !this.sendingOwnAction && this.state != FiftyFiftyDispenser.State.ABORTED;
   }

   public boolean isReturning() {
      return this.state == FiftyFiftyDispenser.State.TRIGGERED;
   }

   public boolean shouldCancelInteraction() {
      if (!this.shouldBlockInput()) {
         return false;
      }

      Minecraft var1 = Minecraft.getInstance();
      return !this.isReturning()
         || !(
            var1.hitResult instanceof BlockHitResult var2
               && var1.level.getBlockState(var2.getBlockPos()).is(Blocks.LEVER)
               && this.dispenserPos != null
               && !(var2.getBlockPos().distSqr(this.dispenserPos) > 9.0)
         );
   }

   public boolean onUseBlock(Minecraft var1, InteractionHand var2, BlockHitResult var3) {
      if (this.sendingOwnAction) {
         return false;
      }

      if (this.isActive()) {
         return this.state != FiftyFiftyDispenser.State.ABORTED;
      }

      if (this.enabled
         && this.isMenuSynced()
         && var2 == InteractionHand.MAIN_HAND
         && var1.level != null
         && var1.level.getBlockState(var3.getBlockPos()).is(Blocks.LEVER)
         && !(Boolean)var1.level.getBlockState(var3.getBlockPos()).getValue(LeverBlock.POWERED)
         && this.dispenserPos != null
         && var3.getBlockPos().distSqr(this.dispenserPos) <= 9.0) {
         this.onContainerContent(var1);
      }

      return false;
   }

   public void isActive(BlockPos var1, BlockState var2) {
      if (this.dispenserPos != null && this.dispenserPos.equals(var1)) {
         int var3 = var2.hasProperty(DispenserBlock.TRIGGERED) && var2.getValue(DispenserBlock.TRIGGERED) ? 1 : 0;
         int var4 = var3 != 0 && !this.wasTriggered ? 1 : 0;
         this.wasTriggered = (boolean)var3;
         if (var4 != 0 && this.enabled && this.isMenuSynced() && !this.isActive()) {
            this.onContainerContent(Minecraft.getInstance());
         }
      }
   }

   private void onContainerContent(Minecraft var1) {
      NonNullList var2 = this.menu.getItems();
      if (findItemSlots(var2) != null
         && this.itemSlots[0] >= 0
         && ItemStack.matches((ItemStack)var2.get(this.itemSlots[0]), this.itemSnapshots[0])
         && ItemStack.matches((ItemStack)var2.get(this.itemSlots[1]), this.itemSnapshots[1])
         && this.menu.getCarried().isEmpty()) {
         this.holdHotbarSlot = -1;

         for (int var3 = 0; var3 < 9; var3++) {
            if (var1.player.getInventory().getItem(var3).isEmpty() && var3 != var1.player.getInventory().getSelectedSlot()) {
               this.holdHotbarSlot = var3;
               break;
            }
         }

         if (this.holdHotbarSlot >= 0) {
            this.chosenIndex = this.getChosenIndex();
            this.keptSlot = this.itemSlots[this.chosenIndex - 1];
            this.removedSlot = this.itemSlots[2 - this.chosenIndex];
            this.keptStack = ((ItemStack)var2.get(this.keptSlot)).copy();
            this.removedStack = ((ItemStack)var2.get(this.removedSlot)).copy();
            this.state = FiftyFiftyDispenser.State.REMOVING;
            this.deadlineNanos = System.nanoTime() + 15000000000L;
            this.swapSlots(this.removedSlot, this.holdHotbarSlot);
         }
      }
   }

   private void abort(String var1) {
      this.state = FiftyFiftyDispenser.State.ABORTED;
      this.enabled = (boolean)0;
      this.holdHotbarSlot = -1;
      RigLog.logMessage("50/50", "Stopped: " + var1 + "; held item remains real, no replay");
      Minecraft var2 = Minecraft.getInstance();
      if (var2.player != null && var2.player.containerMenu == this.menu) {
         var2.gui.setScreen(new DispenserScreen(this.menu, var2.player.getInventory(), Component.literal("50/50 recovery")));
      }
   }

   public void tick(Minecraft var1) {
      if (var1.player == null || var1.level == null) {
         this.menu = null;
         this.state = FiftyFiftyDispenser.State.IDLE;
         this.awaitingMenuOpen = this.menuSynced = this.wasTriggered = (boolean)0;
         this.holdHotbarSlot = -1;
      } else if (this.state == FiftyFiftyDispenser.State.ABORTED) {
         if (var1.player.containerMenu != this.menu) {
            this.state = FiftyFiftyDispenser.State.IDLE;
            this.menu = null;
            this.menuSynced = (boolean)0;
         }
      } else if (this.isActive()) {
         if (var1.player.containerMenu != this.menu) {
            this.abort("container closed during transfer");
         } else {
            if (this.isReturning()) {
               if (!this.enabled || this.getChosenIndex() != this.chosenIndex || System.nanoTime() > this.deadlineNanos) {
                  this.returnHeldItem();
               }
            } else if (System.nanoTime() > this.deadlineNanos) {
               this.abort("server confirmation timed out");
            }
         }
      } else {
         if (this.menu != null && var1.player.containerMenu != this.menu) {
            this.menu = null;
            this.menuSynced = (boolean)0;
         }

         if (!this.enabled) {
            if (this.isMenuSynced() && var1.gui.screen() == null) {
               var1.player.closeContainer();
            }

            this.awaitingMenuOpen = (boolean)0;
         } else if (!this.isMenuSynced()) {
            if ((!this.awaitingMenuOpen || System.nanoTime() >= this.deadlineNanos)
               && var1.gui.screen() == null
               && var1.player.containerMenu == var1.player.inventoryMenu) {
               if (System.nanoTime() >= this.nextOpenNanos) {
                  this.dispenserPos = this.returnHeldItem();
                  if (this.dispenserPos != null && !(var1.player.distanceToSqr(Vec3.atCenterOf(this.dispenserPos)) > 16.0)) {
                     this.awaitingMenuOpen = (boolean)1;
                     this.menuSynced = (boolean)0;
                     this.deadlineNanos = System.nanoTime() + 5000000000L;
                     this.nextOpenNanos = System.nanoTime() + 5000000000L;
                     this.sendingOwnAction = (boolean)1;

                     try {
                        var1.gameMode
                           .useItemOn(
                              var1.player,
                              InteractionHand.MAIN_HAND,
                              new BlockHitResult(Vec3.atCenterOf(this.dispenserPos), Direction.UP, this.dispenserPos, false)
                           );
                     } finally {
                        this.sendingOwnAction = (boolean)0;
                     }
                  }
               }
            }
         }
      }
   }

   private enum State {
      IDLE,
      REMOVING,
      TRIGGERED,
      RETURNING,
      ABORTED;
   }
}
