package com.larpclient.hud;

import com.larpclient.render.world.ItemStackMarkers;
import com.larpclient.util.inventory.ContainerTransaction;
import com.larpclient.util.inventory.FakeLootWithdrawer;
import com.larpclient.util.inventory.HotbarSlotMapper;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.world.FakeSpawnerController;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen.ItemPickerMenu;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen.SlotWrapper;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class VisualInventory {
   private static HotbarSlotMapper slotMapping = new HotbarSlotMapper();
   private static Object lastPlayer;
   private static int[] physicalSlots;
   private static int selectedLogicalSlot;
   private static int selectedPhysicalSlot = -1;
   private static boolean selectionPinned;
   private static AbstractContainerMenu wrappedMenu;
   private static AbstractContainerMenu pickerMenu;
   private static final Map<Slot, VisualInventory.GapSlot> slotWrappers = new IdentityHashMap<>();
   private static final Map<Slot, SlotWrapper> pickerWrappers = new IdentityHashMap<>();
   private static final SimpleContainer emptyContainer = new SimpleContainer(1);
   private static boolean mappingDirty = (boolean)1;
   private static Object lastContainerMenu;
   private static long snapshotVersion;
   private static int pickedUpSlot = -1;

   public static boolean isTransferBusy() {
      return ContainerTransaction.isActive();
   }

   public static void isActive() {
      mappingDirty = (boolean)1;
   }

   public static long getSnapshotVersion() {
      return snapshotVersion;
   }

   public static void reset() {
      lastContainerMenu = null;
      lastPlayer = null;
      physicalSlots = null;
      pickerMenu = null;
      wrappedMenu = null;
      slotWrappers.clear();
      pickerWrappers.clear();
      pickedUpSlot = -1;
      selectedPhysicalSlot = -1;
      selectionPinned = (boolean)0;
      slotMapping = new HotbarSlotMapper();
      mappingDirty = (boolean)1;
   }

   public static boolean isActive() {
      Minecraft var0 = Minecraft.getInstance();
      return var0.player != null && FakeSpawnerController.getInstance().isActive();
   }

   public static void invalidate() {
      mappingDirty = (boolean)1;
      syncMapping();
   }

   private static void syncMapping() {
      Minecraft var0 = Minecraft.getInstance();
      if (!isActive()) {
         lastPlayer = null;
         physicalSlots = null;
      } else {
         if (var0.player.containerMenu.getCarried().isEmpty()) {
            pickedUpSlot = -1;
         }

         if (lastPlayer != var0.player) {
            lastPlayer = var0.player;
            slotMapping = new HotbarSlotMapper();
            physicalSlots = null;
            selectedPhysicalSlot = var0.player.getInventory().getSelectedSlot();
            selectedLogicalSlot = selectedPhysicalSlot;
            selectionPinned = (boolean)0;
            mappingDirty = (boolean)1;
            LarpLog.debug("FakeSpawner", "Visual inventory initialized; ordinary slot positions retained");
         }

         if (lastContainerMenu != var0.player.containerMenu) {
            lastContainerMenu = var0.player.containerMenu;
            mappingDirty = (boolean)1;
         }

         if (mappingDirty || physicalSlots == null) {
            if (physicalSlots == null || !isTransferBusy()) {
               long var1 = 0L;
               long var3 = 0L;

               for (int var5 = 0; var5 < 36; var5++) {
                  if (!var0.player.getInventory().getItem(var5).isEmpty()
                     || FakeLootWithdrawer.hasFakeStack(var5)
                     || FakeSpawnerController.getInstance().readPilesFrom(var5)) {
                     var1 |= 1L << var5;
                  }

                  if (FakeSpawnerController.getInstance().isReservedSlot(var5)
                     && FakeSpawnerController.getInstance().getUnreservedCount(var5, var0.player.getInventory().getItem(var5)) == 0) {
                     var3 |= 1L << var5;
                  }
               }

               mappingDirty = (boolean)0;
               int[] var6 = slotMapping.resolve(var1, var3);
               if (var6 != physicalSlots) {
                  physicalSlots = var6;
                  snapshotVersion++;
                  LarpLog.debug("FakeSpawner", "Mapping snapshot {} occupancy={} reserved={}", snapshotVersion, Long.toHexString(var1), Long.toHexString(var3));
               }
            }
         }
      }
   }

   public static int getPhysicalSlot(int var0) {
      syncMapping();
      return physicalSlots != null && var0 >= 0 && var0 < 36 ? physicalSlots[var0] : var0;
   }

   public static ItemStack getPhysicalSlot(int var0) {
      int var1 = getPhysicalSlot(var0);
      return var1 < 0 ? ItemStack.EMPTY : displayStack(var1, Minecraft.getInstance().player.getInventory().getItem(var1));
   }

   public static ItemStack displayStack(int var0, ItemStack var1) {
      return FakeSpawnerController.getInstance().isReservedSlot(var0)
         ? FakeSpawnerController.getInstance().syncState(var0, var1)
         : FakeSpawnerController.getInstance().reconcileInventory(var0, FakeLootWithdrawer.overrideSlotStack(var0, var1));
   }

   public static int getSelectedSlot(int var0) {
      syncMapping();
      if (!isActive()) {
         return var0;
      }

      if (var0 != selectedPhysicalSlot) {
         selectedLogicalSlot = var0;
         selectionPinned = (boolean)0;
      }

      selectedPhysicalSlot = var0;
      if (selectionPinned && physicalSlots != null && selectedLogicalSlot >= 0 && selectedLogicalSlot < 9 && physicalSlots[selectedLogicalSlot] < 0) {
         return selectedLogicalSlot;
      }

      selectionPinned = (boolean)0;
      if (physicalSlots != null) {
         for (int var1 = 0; var1 < 9; var1++) {
            if (physicalSlots[var1] == var0) {
               selectedLogicalSlot = var1;
               break;
            }
         }
      }

      return selectedLogicalSlot;
   }

   public static int findFreeSlot() {
      syncMapping();

      for (int var0 = 0; var0 < 36; var0++) {
         int var1 = physicalSlots == null ? var0 : physicalSlots[var0];
         if (var1 < 0
            || Minecraft.getInstance().player.getInventory().getItem(var1).isEmpty()
               && !FakeSpawnerController.getInstance().readPilesFrom(var1)
               && !FakeLootWithdrawer.hasFakeStack(var1)) {
            return var0;
         }
      }

      return -1;
   }

   public static void assignSlot(int var0, ItemStack var1, int var2) {
      if (var2 >= 0) {
         slotMapping.pinSlot(var2, var0);
      }

      invalidate();
   }

   public static void addStack(int var0, ItemStack var1) {
      assignSlot(var0, var1, findFreeSlot());
   }

   public static Slot wrapSlot(AbstractContainerMenu var0, Slot var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var0 instanceof ItemPickerMenu) {
         if (isActive() && var1 instanceof SlotWrapper var7 && !(var7.target instanceof VisualInventory.GapSlot)) {
            syncMapping(var0);
            return wrapPickerSlot(var1, wrapSlot(var2.player.inventoryMenu, var7.target));
         } else {
            return var1;
         }
      } else if (isActive()
         && !(var1 instanceof VisualInventory.GapSlot)
         && var1.container == var2.player.getInventory()
         && var1.getContainerSlot() >= 0
         && var1.getContainerSlot() < 36) {
         int var3 = getPhysicalSlot(var1.getContainerSlot());
         Slot var4 = null;
         if (var3 >= 0) {
            for (Slot var6 : var0.slots) {
               if (var6.container == var1.container && var6.getContainerSlot() == var3) {
                  var4 = var6;
                  break;
               }
            }
         }

         if (wrappedMenu != var0) {
            slotWrappers.clear();
            pickerWrappers.clear();
            wrappedMenu = var0;
         }

         VisualInventory.GapSlot var8 = slotWrappers.get(var1);
         if (var8 == null || var8.target != var4) {
            var8 = new VisualInventory.GapSlot(var1, var4);
            slotWrappers.put(var1, var8);
         }

         return var8;
      } else {
         return var1;
      }
   }

   private static Slot wrapPickerSlot(Slot var0, Slot var1) {
      SlotWrapper var2 = pickerWrappers.get(var0);
      if (var2 == null || var2.target != var1) {
         var2 = new SlotWrapper(var1, var0.getContainerSlot(), var0.x, var0.y);
         var2.index = var0.index;
         pickerWrappers.put(var0, var2);
      }

      return var2;
   }

   private static void syncMapping(AbstractContainerMenu var0) {
      if (pickerMenu != var0) {
         pickerWrappers.clear();
         pickerMenu = var0;
      }
   }

   public static Slot unwrapSlot(Slot var0) {
      return var0 instanceof SlotWrapper var1 ? var1.target : var0;
   }

   public static Slot findFreeSlot(AbstractContainerMenu var0, Slot var1, Set<Slot> var2) {
      if (isActive() && var0 instanceof ItemPickerMenu && var1 instanceof SlotWrapper var16) {
         syncMapping(var0);
         InventoryMenu var17 = Minecraft.getInstance().player.inventoryMenu;
         HashSet var18 = new HashSet();

         for (Slot var21 : var2) {
            var18.add(unwrapSlot(var21));
         }

         Slot var20 = findFreeSlot(var17, var16.target, var18);
         if (!var2.isEmpty()) {
            var2.clear();

            for (Slot var23 : var18) {
               for (Slot var25 : var0.slots) {
                  Slot var26 = wrapSlot(var0, var25);
                  if (unwrapSlot(var26) == var23) {
                     var2.add(var26);
                     break;
                  }
               }
            }
         }

         return wrapPickerSlot(var1, var20);
      } else {
         Slot var3 = wrapSlot(var0, var1);
         if (var3 instanceof VisualInventory.GapSlot var4
            && var4.target == null
            && !isTransferBusy()
            && var0.containerId >= 0
            && !var0.getCarried().isEmpty()
            && !ItemStackMarkers.isPreview(var0.getCarried())) {
            Inventory var5 = Minecraft.getInstance().player.getInventory();

            for (int var6 = 0; var6 < 2; var6++) {
               for (Slot var8 : var0.slots) {
                  int var9 = var8.getContainerSlot();
                  if (var8.container == var5
                     && var9 >= 0
                     && var9 < 36
                     && !var8.hasItem()
                     && (var6 != 0 || var9 != pickedUpSlot)
                     && (var6 != 1 || var9 == pickedUpSlot)
                     && !isReserved(var9, var8.getItem())
                     && var8.mayPlace(var0.getCarried())
                     && var8.isActive()
                     && var8.getMaxStackSize(var0.getCarried()) >= 1
                     && !var2.stream().anyMatch(var2x -> var2x.container == var5 && var2x.getContainerSlot() == var9)) {
                     slotMapping.pinSlot(var4.gapIndex, var9);
                     isActive();
                     syncMapping();
                     if (!var2.isEmpty()) {
                        ArrayList var10 = new ArrayList(var2.size());

                        for (Slot var12 : var2) {
                           Slot var13 = var12;
                           if (var12.container == var5) {
                              for (Slot var15 : var0.slots) {
                                 if (var15.container == var5
                                    && var15.getContainerSlot() >= 0
                                    && var15.getContainerSlot() < 36
                                    && getPhysicalSlot(var15.getContainerSlot()) == var12.getContainerSlot()) {
                                    var13 = wrapSlot(var0, var15);
                                    break;
                                 }
                              }
                           }

                           var10.add(var13);
                        }

                        var2.clear();
                        var2.addAll(var10);
                     }

                     LarpLog.debug("FakeSpawner", "Visual gap bound logical={} physical={} menu={}", var4.gapIndex, var9, var0.containerId);
                     return wrapSlot(var0, var1);
                  }
               }
            }

            return var3;
         } else {
            return var3;
         }
      }
   }

   public static Slot unwrapGap(AbstractContainerMenu var0, Slot var1) {
      if (var1 instanceof VisualInventory.GapSlot var2 && var2.target != null) {
         for (Slot var4 : var0.slots) {
            if (var4.container == var1.container && var4.getContainerSlot() == var1.getContainerSlot()) {
               return new VisualInventory.GapSlot(new Slot(var1.container, var2.gapIndex, var1.x, var1.y), var4);
            }
         }

         return var1;
      } else {
         return var1;
      }
   }

   public static void onSlotPickup(Slot var0) {
      if (!isTransferBusy()) {
         if (var0 instanceof VisualInventory.GapSlot var1 && var1.target != null) {
            if (Minecraft.getInstance().player.containerMenu.getCarried().isEmpty() && var1.hasItem()) {
               pickedUpSlot = var1.target.getContainerSlot();
            }

            slotMapping.pinSlot(var1.gapIndex, var1.target.getContainerSlot());
            isActive();
         }
      }
   }

   private static boolean isReserved(int var0, ItemStack var1) {
      return var0 < 0
         || FakeSpawnerController.getInstance().isReservedSlot(var0)
         || FakeLootWithdrawer.hasFakeStack(var0)
         || FakeSpawnerController.getInstance().readPilesFrom(var0);
   }

   public static boolean isVirtualGap(Slot var0) {
      return var0 instanceof VisualInventory.GapSlot var1 && (var1.target == null || isReserved(var1.target.getContainerSlot(), var1.target.getItem()));
   }

   public static void swapSlots(int var0, int var1) {
      slotMapping.pinSlot(var0, var1);
      invalidate();
   }

   public static void swapSlots(AbstractContainerMenu var0, Slot var1, int var2) {
      if (!isTransferBusy()) {
         Minecraft var3 = Minecraft.getInstance();
         if (var1 != null && !isVirtualGap(var1) && var3.gameMode != null && var0.getCarried().isEmpty()) {
            int var4 = getPhysicalSlot(var2);
            Slot var5 = null;

            for (Slot var7 : var0.slots) {
               if (var7.container == var3.player.getInventory() && var7.getContainerSlot() == var4) {
                  var5 = var7;
                  break;
               }
            }

            if (var5 != null && var5.index != var1.index && !isReserved(var4, var5.getItem())) {
               if (var1.mayPickup(var3.player)
                  && var5.mayPickup(var3.player)
                  && (var5.getItem().isEmpty() || var1.mayPlace(var5.getItem()) && var5.getItem().getCount() <= var1.getMaxStackSize(var5.getItem()))
                  && (var1.getItem().isEmpty() || var5.mayPlace(var1.getItem()))) {
                  if (var4 < 9) {
                     if (!FakeSpawnerController.getInstance()
                        .replayCreativeClicks(var0, List.of(new ContainerTransaction.Click(var1.index, var4, ContainerInput.SWAP)))) {
                        var3.gameMode.handleContainerInput(var0.containerId, var1.index, var4, ContainerInput.SWAP, var3.player);
                     }
                  } else {
                     int var10 = 0;

                     while (var1.container == var3.player.getInventory() && var1.getContainerSlot() == var10) {
                        var10++;
                     }

                     int var11 = var10;
                     Slot var8 = var5;
                     if (FakeSpawnerController.getInstance().isReservedSlot(var11)) {
                        return;
                     }

                     List var9 = List.of(
                        new ContainerTransaction.Click(var1.index, var11, ContainerInput.SWAP),
                        new ContainerTransaction.Click(var8.index, var11, ContainerInput.SWAP),
                        new ContainerTransaction.Click(var1.index, var11, ContainerInput.SWAP)
                     );
                     if (!FakeSpawnerController.getInstance().replayCreativeClicks(var0, var9)) {
                        ContainerTransaction.begin(var0, "visual number-key swap", var9, VisualInventory::isActive, VisualInventory::isActive);
                     }
                  }

                  slotMapping.pinSlot(var2, var4);
                  if (var1 instanceof VisualInventory.GapSlot) {
                     onSlotPickup(var1);
                  } else if (var1.container == var3.player.getInventory()) {
                     slotMapping.pinSlot(var1.getContainerSlot(), var1.getContainerSlot());
                  }

                  invalidate();
               }
            }
         }
      }
   }

   public static void selectHotbarSlot(int var0) {
      if (!isTransferBusy()) {
         Minecraft var1 = Minecraft.getInstance();
         if (!isActive()) {
            var1.player.getInventory().setSelectedSlot(var0);
         } else {
            int var2 = getPhysicalSlot(var0);
            if (var2 >= 0) {
               selectionPinned = (boolean)0;
               if (var2 < 9) {
                  var1.player.getInventory().setSelectedSlot(var2);
                  selectedPhysicalSlot = var2;
                  selectedLogicalSlot = var0;
               } else if (var1.gameMode != null && var1.player.containerMenu == var1.player.inventoryMenu && var1.player.inventoryMenu.getCarried().isEmpty()) {
                  for (int var6 = 0; var6 < 9; var6++) {
                     if (!FakeSpawnerController.getInstance().isReservedSlot(var6)) {
                        FakeSpawnerController var8 = FakeSpawnerController.getInstance();
                        if (var1.player.getInventory().getItem(var2).is(Items.BONE)) {
                           var8.snapshotInventory(var6);
                        }

                        if (var1.player.getInventory().getItem(var6).is(Items.BONE)) {
                           var8.snapshotInventory(var2);
                        }

                        var1.gameMode.handleContainerInput(0, var2, var6, ContainerInput.SWAP, var1.player);
                        slotMapping.swapTargets(var2, var6);
                        isActive();
                        var1.player.getInventory().setSelectedSlot(var6);
                        selectedPhysicalSlot = var6;
                        selectedLogicalSlot = var0;
                        return;
                     }
                  }

                  if (FakeSpawnerController.getInstance().restorePiles(var2, var0)) {
                     slotMapping.swapTargets(var2, var0);
                     slotMapping.pinSlot(var0, var0);
                     var1.player.getInventory().setSelectedSlot(var0);
                     selectedPhysicalSlot = var0;
                     selectedLogicalSlot = var0;
                     invalidate();
                  }
               }
            } else {
               int var3 = 0;

               int var4;
               while (true) {
                  if (var3 >= 36) {
                     var3 = var0;

                     for (int var7 = 0; var7 < 9; var7++) {
                        if (FakeSpawnerController.getInstance().isReservedSlot(var7)) {
                           var3 = var7;
                           break;
                        }
                     }

                     var1.player.getInventory().setSelectedSlot(var3);
                     selectedPhysicalSlot = var3;
                     selectedLogicalSlot = var0;
                     selectionPinned = (boolean)1;
                     return;
                  }

                  if (!isReserved(var3, var1.player.getInventory().getItem(var3)) && var1.player.getInventory().getItem(var3).isEmpty()) {
                     var4 = var3;
                     if (var3 < 9) {
                        break;
                     }

                     var4 = var0;
                     if (FakeSpawnerController.getInstance().restorePiles(var3, var4)) {
                        slotMapping.swapTargets(var3, var4);
                        slotMapping.pinSlot(var0, var4);
                        isActive();
                        break;
                     }
                  }

                  var3++;
               }

               var1.player.getInventory().setSelectedSlot(var4);
               selectedPhysicalSlot = var4;
               selectedLogicalSlot = var0;
               selectionPinned = (boolean)1;
            }
         }
      }
   }

   public static final class GapSlot extends Slot {
      private final Slot target;
      private final int gapIndex;

      GapSlot(Slot var1, Slot var2) {
         super((Container)(var2 == null ? VisualInventory.emptyContainer : var2.container), var2 == null ? 0 : var2.getContainerSlot(), var1.x, var1.y);
         this.target = var2;
         this.gapIndex = var1.getContainerSlot();
         this.index = var2 == null ? -1 : var2.index;
      }

      public ItemStack getItem() {
         return this.target == null ? ItemStack.EMPTY : VisualInventory.displayStack(this.target.getContainerSlot(), this.target.getItem());
      }

      public boolean mayPlace(ItemStack var1) {
         return this.target != null && !VisualInventory.isVirtualGap(this) && this.target.mayPlace(var1);
      }

      public boolean mayPickup(Player var1) {
         return this.target != null
            && (
               !VisualInventory.isVirtualGap(this)
                  || FakeSpawnerController.getInstance().getUnreservedCount(this.target.getContainerSlot(), this.target.getItem()) > 0
            )
            && this.target.mayPickup(var1);
      }

      public int getMaxStackSize() {
         return this.target == null ? 0 : this.target.getMaxStackSize();
      }

      public int getMaxStackSize(ItemStack var1) {
         return this.target == null ? 0 : this.target.getMaxStackSize(var1);
      }

      public boolean isActive() {
         return this.target != null && this.target.isActive();
      }

      public Identifier getNoItemIcon() {
         return this.target == null ? null : this.target.getNoItemIcon();
      }

      public boolean isFake() {
         return this.target != null && this.target.isFake();
      }
   }
}
