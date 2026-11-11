package com.larpclient.module.player;

import com.larpclient.module.misc.AutoSellModule;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.util.entity.CombatUtil;
import com.larpclient.util.misc.LarpLog;
import java.util.BitSet;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ChestStealerModule extends Module {
   private static final int startupDelay = 2;
   private static final int noClick = Integer.MAX_VALUE;
   private final NumberSetting delay = this.addSetting(new NumberSetting("Delay", "Ticks between items", 2.0, 0.0, 10.0, 1.0));
   private int waitTicks;
   private int ticksInMenu;
   private int ticksSinceClick = Integer.MAX_VALUE;
   private int clickCount;
   private int correctionCount;
   private ChestMenu menu;
   private ItemStack[] slotSnapshot;
   private final BitSet clickedSlots = new BitSet();
   private boolean waitingForCapacity;
   private boolean waitingForStop;

   public ChestStealerModule() {
      super("ChestStealer", "Automatically loot chests", Category.PLAYER, -1);
   }

   @Override
   public void onDisable() {
      this.reset();
   }

   private void reset() {
      if (this.clickCount > 0) {
         LarpLog.info("ChestStealer", "Chest finished: {} clicks, {} server corrections", this.clickCount, this.correctionCount);
      }

      this.waitTicks = 0;
      this.ticksInMenu = 0;
      this.ticksSinceClick = Integer.MAX_VALUE;
      this.clickCount = 0;
      this.correctionCount = 0;
      this.menu = null;
      this.slotSnapshot = null;
      this.clickedSlots.clear();
      this.waitingForCapacity = (boolean)0;
      this.waitingForStop = (boolean)0;
   }

   private void trackMenu(ChestMenu var1, int var2) {
      if (var1 != this.menu) {
         this.reset();
         this.menu = var1;
         this.waitTicks = 2;
      }

      if (this.syncSnapshot(var1) && this.ticksSinceClick <= var2) {
         this.waitTicks = Math.max(this.waitTicks, var2 + 1);
         this.correctionCount++;
         LarpLog.debug("ChestStealer", "Server corrected the menu; pausing {} ticks", this.waitTicks);
      }
   }

   private boolean syncSnapshot(ChestMenu var1) {
      int var2 = this.slotSnapshot != null && this.slotSnapshot.length == var1.slots.size() ? 0 : 1;
      if (var2 == 0) {
         for (int var3 = 0; var3 < this.slotSnapshot.length; var3++) {
            if (!ItemStack.matches(this.slotSnapshot[var3], var1.getSlot(var3).getItem())) {
               var2 = 1;
               break;
            }
         }
      }

      if (var2 != 0) {
         this.slotSnapshot = new ItemStack[var1.slots.size()];

         for (int var4 = 0; var4 < this.slotSnapshot.length; var4++) {
            this.slotSnapshot[var4] = var1.getSlot(var4).getItem().copy();
         }

         this.clickedSlots.clear();
         this.waitingForCapacity = (boolean)0;
      }

      return (boolean)var2;
   }

   private boolean hasRoomFor(ChestMenu var1, ItemStack var2, int var3) {
      for (int var4 = var3; var4 < var1.slots.size(); var4++) {
         Slot var5 = var1.getSlot(var4);
         ItemStack var6 = var5.getItem();
         if (var6.isEmpty()) {
            if (var5.mayPlace(var2) && var5.getMaxStackSize(var2) > 0) {
               return true;
            }
         } else if (var2.isStackable() && ItemStack.isSameItemSameComponents(var2, var6) && var6.getCount() < var5.getMaxStackSize(var6)) {
            return true;
         }
      }

      return false;
   }

   private boolean isMoving() {
      Input var1 = minecraft.player.getLastSentInput();
      return var1.forward() || var1.backward() || var1.left() || var1.right() || var1.shift() || minecraft.player.isSprinting();
   }

   @Override
   public void onTick() {
      if (!(this.isInGame() && minecraft.player.containerMenu instanceof ChestMenu var1)) {
         this.reset();
      } else if (AutoSellModule.isActiveSellMenu(var1)) {
         this.reset();
      } else {
         int var16 = CombatUtil.getPingTicks(minecraft.player);
         if (this.ticksSinceClick != Integer.MAX_VALUE) {
            byte var9 = 1;
            int var8 = this.ticksSinceClick;
            this.ticksSinceClick = var8 + var9;
         }

         byte var11 = 1;
         int var10 = this.ticksInMenu;
         this.ticksInMenu = var10 + var11;
         this.trackMenu(var1, var16);
         if (this.waitTicks > 0) {
            byte var13 = 1;
            int var12 = this.waitTicks;
            this.waitTicks = var12 - var13;
         } else if (minecraft.gameMode != null) {
            if (this.isMoving()) {
               if (!this.waitingForStop) {
                  this.waitingForStop = (boolean)1;
                  LarpLog.debug("ChestStealer", "Waiting for the player to stop moving");
               }
            } else {
               this.waitingForStop = (boolean)0;
               int var3 = var1.getRowCount() * 9;
               byte var4 = 0;

               for (int var5 = 0; var5 < var3; var5++) {
                  Slot var6 = var1.getSlot(var5);
                  ItemStack var7 = var6.getItem();
                  if (!var7.isEmpty()) {
                     var4 = 1;
                     if (!this.clickedSlots.get(var5) && var6.mayPickup(minecraft.player) && this.hasRoomFor(var1, var7, var3)) {
                        this.clickedSlots.set(var5);
                        minecraft.gameMode.handleContainerInput(var1.containerId, var5, 0, ContainerInput.QUICK_MOVE, minecraft.player);
                        this.ticksSinceClick = 0;
                        this.clickCount++;
                        this.syncSnapshot(var1);
                        this.waitTicks = this.delay.getValue().intValue();
                        return;
                     }
                  }
               }

               if (var4 == 0) {
                  int var15 = var16;
                  int var14 = this.ticksInMenu;
                  if ((-Integer.compare(var14, var15) >>> 31 ^ 1) != 0 || this.ticksSinceClick <= var16) {
                     return;
                  }

                  minecraft.player.closeContainer();
                  this.reset();
               } else if (!this.waitingForCapacity) {
                  this.waitingForCapacity = (boolean)1;
                  LarpLog.debug("ChestStealer", "Waiting for inventory capacity or container update");
               }
            }
         }
      }
   }
}
