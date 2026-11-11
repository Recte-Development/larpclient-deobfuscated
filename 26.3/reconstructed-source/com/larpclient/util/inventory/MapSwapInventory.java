package com.larpclient.util.inventory;

import com.larpclient.module.exploit.BlackjackRigger;
import com.larpclient.module.exploit.CardForce;
import com.larpclient.module.player.FishingRodSwapper;
import com.larpclient.util.misc.FiftyFiftyDispenser;
import com.larpclient.util.world.DispenserRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapId;

public final class MapSwapInventory {
   private static final MapSlotTracker swapState = new MapSlotTracker();
   private static final Map<String, Map<Integer, Integer>> stockSlotsByContainer = new HashMap<>();
   private static final Map<Integer, ItemStack> mapTemplates = new HashMap<>();
   private static final Map<MapSlotTracker.SlotRef, Entity> trackedEntities = new HashMap<>();
   private static final Map<MapSlotTracker.MapMove, MapSwapInventory.VisualStack> visualStacks = new IdentityHashMap<>();
   private static ClientLevel lastLevel;
   private static long lastGameTime = Long.MIN_VALUE;
   private static Map<Integer, StackRedistributor.Anonymous> displayedMaps = Map.of();
   private static final Map<Integer, ItemStack> displayStacks = new HashMap<>();
   private static boolean handlingClick;
   private static int virtualHotbarSlot = -1;
   private static int actualHotbarSlot = -1;
   private static boolean changingSelectedSlot;

   private static Map<Integer, StackRedistributor.Anonymous> mapIdOf() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0.player != null && !swapState.moves.isEmpty()) {
         int[] var1 = new int[36];
         int[] var2 = new int[36];

         for (int var3 = 0; var3 < 36; var3++) {
            ItemStack var4 = var0.player.getInventory().getItem(var3);
            var1[var3] = mapIdOf(var4);
            var2[var3] = var4.getCount();
         }

         ArrayList var6 = new ArrayList();

         for (MapSlotTracker.MapMove var5 : swapState.moves) {
            if (var5.location != null && var5.location.kind().equals("inventory") && var5.location.slot() < 36) {
               var6.add(new StackRedistributor.Anonymous2(var5.location.slot(), var5.destMap, var5.sourceMap, 1));
            }
         }

         Map var8 = StackRedistributor.planStacksWithPrevious(var1, var2, var6, displayedMaps);
         if (!var8.equals(displayedMaps)) {
            displayedMaps = var8;
            displayStacks.clear();
            var8.forEach((var1x, var2x) -> {
               if (var2x.count() == 0) {
                  displayStacks.put(var1x, ItemStack.EMPTY);
               } else {
                  ItemStack var3x = var0.player.getInventory().getItem(var2x.portions().getFirst().source()).copyWithCount(var2x.count());
                  var3x.set(DataComponents.MAP_ID, new MapId(var2x.shown()));
                  displayStacks.put(var1x, var3x);
               }
            });
         }

         return displayedMaps;
      } else {
         displayedMaps = Map.of();
         displayStacks.clear();
         return displayedMaps;
      }
   }

   public static int cursorLocation(int var0) {
      if (changingSelectedSlot) {
         return var0;
      }

      virtualHotbarSlot = var0;
      actualHotbarSlot = var0;
      StackRedistributor.Anonymous var1 = mapIdOf().get(var0);
      if (var1 != null && !var1.portions().isEmpty()) {
         StackRedistributor.Anonymous2 var2 = var1.portions().getFirst();
         if (var2.source() >= 9) {
            return var0;
         }

         actualHotbarSlot = var2.source();
         swapState.expectMap(inventoryLocation(var2.source()), var1.shown());
         return var2.source();
      } else {
         return var0;
      }
   }

   public static int syncSelectedSlot() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0.player == null) {
         return -1;
      }

      int var1 = var0.player.getInventory().getSelectedSlot();
      if (!CardForce.getActive().isSwapActive() && !FishingRodSwapper.getInstance().setAutoSwap()) {
         if (virtualHotbarSlot < 0 || virtualHotbarSlot >= 9 || var1 != actualHotbarSlot) {
            virtualHotbarSlot = var1;
            actualHotbarSlot = var1;
         }

         StackRedistributor.Anonymous var2 = mapIdOf().get(virtualHotbarSlot);
         int var3 = var2 != null && !var2.portions().isEmpty() ? var2.portions().getFirst().source() : virtualHotbarSlot;
         if (var3 < 9 && var3 != var1) {
            changingSelectedSlot = (boolean)1;

            try {
               var0.player.getInventory().setSelectedSlot(var3);
               actualHotbarSlot = var3;
            } finally {
               changingSelectedSlot = (boolean)0;
            }
         }

         return virtualHotbarSlot;
      } else {
         return var1;
      }
   }

   public static boolean hasSelectedItem() {
      StackRedistributor.Anonymous var0 = mapIdOf().get(syncSelectedSlot());
      return var0 == null || var0.count() > 0;
   }

   public static boolean canUseSelected(boolean var0) {
      if (!hasSelectedItem()) {
         return false;
      }

      StackRedistributor.Anonymous var1 = mapIdOf().get(syncSelectedSlot());
      Minecraft var2 = Minecraft.getInstance();
      return !var0 || var1 == null || var1.portions().size() == 1 && var1.count() == var2.player.getMainHandItem().getCount();
   }

   public static boolean isSplitAcrossSlots(int var0) {
      StackRedistributor.Anonymous var1 = mapIdOf().get(var0);
      return var1 != null && var1.portions().size() > 1;
   }

   public static void update() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0.player != null) {
         StackRedistributor.Anonymous var1 = mapIdOf().get(syncSelectedSlot());
         if (var1 != null) {
            swapState.expectMap(inventoryLocation(var0.player.getInventory().getSelectedSlot()), var1.shown());
         }
      }
   }

   private static int findMenuSlot(int var0) {
      Minecraft var1 = Minecraft.getInstance();

      for (Slot var3 : var1.player.containerMenu.slots) {
         if (var3.container == var1.player.getInventory() && var3.getContainerSlot() == var0) {
            return var3.index;
         }
      }

      return -1;
   }

   private static void sendContainerInput(int var0, int var1, ContainerInput var2) {
      Minecraft var3 = Minecraft.getInstance();
      var3.gameMode.handleContainerInput(var3.player.containerMenu.containerId, var0, var1, var2, var3.player);
      refresh();
   }

   private static void syncSelectedSlot(StackRedistributor.Anonymous2 var0, int var1) {
      Minecraft var2 = Minecraft.getInstance();
      int var3 = findMenuSlot(var0.source());
      int var4 = var2.player.getInventory().getItem(var0.source()).getCount();
      swapState.expectMap(inventoryLocation(var0.source()), var0.shown());
      sendContainerInput(var3, 0, ContainerInput.PICKUP);

      for (int var5 = var1; var5 < var4; var5++) {
         int var6 = mapIdOf(var2.player.containerMenu.getCarried());
         long var7 = swapState.moves.stream().filter(var0x -> cursorLocation().equals(var0x.location)).count();
         int var9 = var2.player.containerMenu.getCarried().getCount() > var7 ? var6 : var0.shown();
         if (var9 == var0.shown()) {
            for (MapSlotTracker.MapMove var11 : swapState.moves) {
               if (cursorLocation().equals(var11.location) && var11.sourceMap != var0.shown()) {
                  var9 = var11.sourceMap;
                  break;
               }
            }
         }

         swapState.expectMap(cursorLocation(), var9);
         sendContainerInput(var3, 1, ContainerInput.PICKUP);
      }
   }

   public static void syncSelectedSlot(Minecraft var0) {
      if (var0.player != null
         && var0.gameMode != null
         && !swapState.moves.isEmpty()
         && !handlingClick
         && !CardForce.getActive().isSwapActive()
         && !CardForce.getActive().blocksUserInteraction()
         && !FiftyFiftyDispenser.getInstance().shouldBlockInput()
         && !BlackjackRigger.getInstance().isHidingCards()
         && !CardForce.getActive().isSwapFreezingPassThrough()
         && var0.player.containerMenu.getCarried().isEmpty()) {
         refresh();

         for (MapSlotTracker.MapMove var2 : swapState.moves) {
            if (var2.location != null && var2.location.kind().equals("inventory") && var2.location.slot() < 36) {
               int var3 = var2.location.slot();
               int var4 = var2.destMap;
               int var5 = var2.sourceMap;
               ItemStack var6 = var0.player.getInventory().getItem(var3);
               if (mapIdOf(var6) == var4) {
                  int var7 = (int)swapState.moves
                     .stream()
                     .filter(var3x -> inventoryLocation(var3).equals(var3x.location) && var3x.destMap == var4 && var3x.sourceMap == var5)
                     .count();
                  if (var7 < var6.getCount()) {
                     int var8 = -1;

                     for (int var9 = 0; var9 < 36; var9++) {
                        if (var0.player.getInventory().getItem(var9).isEmpty() && findMenuSlot(var9) >= 0) {
                           var8 = var9;
                           break;
                        }
                     }

                     if (var8 >= 0 && findMenuSlot(var3) >= 0) {
                        handlingClick = (boolean)1;

                        try {
                           sendContainerInput(findMenuSlot(var3), 0, ContainerInput.PICKUP);

                           for (int var13 = 0; var13 < var7; var13++) {
                              swapState.expectMap(cursorLocation(), var5);
                              sendContainerInput(findMenuSlot(var8), 1, ContainerInput.PICKUP);
                           }

                           if (!var0.player.containerMenu.getCarried().isEmpty()) {
                              sendContainerInput(findMenuSlot(var3), 0, ContainerInput.PICKUP);
                           }
                        } finally {
                           handlingClick = (boolean)0;
                        }

                        return;
                     }

                     return;
                  }
               }
            }
         }
      }
   }

   public static boolean interceptSlotClick(int var0, int var1, int var2, ContainerInput var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (!handlingClick
         && var4.player != null
         && !swapState.moves.isEmpty()
         && !CardForce.getActive().isSwapActive()
         && !FiftyFiftyDispenser.getInstance().shouldBlockInput()
         && !BlackjackRigger.getInstance().isHidingCards()) {
         AbstractContainerMenu var5 = var4.player.containerMenu;
         if (var5.containerId == var0 && var1 >= 0 && var1 < var5.slots.size()) {
            Slot var6 = var5.getSlot(var1);
            if (var6.container == var4.player.getInventory() && var6.getContainerSlot() >= 0 && var6.getContainerSlot() < 36) {
               refresh();
               StackRedistributor.Anonymous var7 = mapIdOf().get(var6.getContainerSlot());
               if (var7 == null) {
                  return false;
               }

               if (var3 != ContainerInput.PICKUP && var3 != ContainerInput.QUICK_MOVE && var3 != ContainerInput.THROW) {
                  return true;
               }

               handlingClick = (boolean)1;

               try {
                  if (!var5.getCarried().isEmpty()) {
                     if (var3 != ContainerInput.PICKUP) {
                        return true;
                     }

                     ItemStack var19 = findMenuSlot(var5.getCarried());
                     int var21 = -1;
                     if (var7.shown() == mapIdOf(var19)) {
                        for (StackRedistributor.Anonymous2 var25 : var7.portions()) {
                           if (ItemStack.isSameItemSameComponents(var4.player.getInventory().getItem(var25.source()), var5.getCarried())
                              && var4.player.getInventory().getItem(var25.source()).getCount() < var5.getCarried().getMaxStackSize()) {
                              var21 = findMenuSlot(var25.source());
                              break;
                           }
                        }
                     }

                     if (var21 < 0 && (var7.count() == 0 || var7.shown() == mapIdOf(var19))) {
                        for (int var23 = 0; var23 < 36; var23++) {
                           if (var4.player.getInventory().getItem(var23).isEmpty()) {
                              var21 = findMenuSlot(var23);
                              break;
                           }
                        }
                     }

                     if (var21 < 0 && var7.shown() != mapIdOf(var19) && !var7.portions().isEmpty()) {
                        StackRedistributor.Anonymous2 var24 = var7.portions().getFirst();
                        ItemStack var26 = var4.player.getInventory().getItem(var24.source());
                        if (ItemStack.isSameItemSameComponents(var26, var5.getCarried())) {
                           return true;
                        }

                        if (var24.count() == var26.getCount()) {
                           var21 = findMenuSlot(var24.source());
                        }
                     }

                     if (var21 >= 0) {
                        sendContainerInput(var21, var2, var3);
                     }

                     return true;
                  } else {
                     if (var7.portions().isEmpty()) {
                        return true;
                     }

                     if (var3 == ContainerInput.QUICK_MOVE) {
                        for (StackRedistributor.Anonymous2 var9 : var7.portions()) {
                           int var10 = var4.player.getInventory().getItem(var9.source()).getCount();
                           if (var10 == var9.count()) {
                              swapState.expectMap(inventoryLocation(var9.source()), var9.shown());
                              sendContainerInput(findMenuSlot(var9.source()), 0, var3);
                           } else if (var5 instanceof DispenserMenu) {
                              int var11 = -1;
                              ItemStack var12 = var4.player.getInventory().getItem(var9.source());

                              for (int var13 = 0; var13 < 9; var13++) {
                                 ItemStack var14 = var5.getSlot(var13).getItem();
                                 if (!var14.isEmpty()
                                    && ItemStack.isSameItemSameComponents(var14, var12)
                                    && var14.getCount() + var9.count() <= var14.getMaxStackSize()) {
                                    var11 = var13;
                                    break;
                                 }
                              }

                              if (var11 < 0) {
                                 for (int var27 = 0; var27 < 9; var27++) {
                                    if (var5.getSlot(var27).getItem().isEmpty()) {
                                       var11 = var27;
                                       break;
                                    }
                                 }
                              }

                              if (var11 >= 0) {
                                 syncSelectedSlot(var9, var9.count());
                                 sendContainerInput(var11, 0, ContainerInput.PICKUP);
                                 if (!var5.getCarried().isEmpty()) {
                                    sendContainerInput(findMenuSlot(var9.source()), 0, ContainerInput.PICKUP);
                                 }
                              }
                           }
                        }
                     } else {
                        StackRedistributor.Anonymous2 var18 = var7.portions().getFirst();
                        int var20 = var3 == ContainerInput.THROW
                           ? (var2 == 0 ? 1 : var18.count())
                           : Math.min(var18.count(), var2 == 1 ? (var7.count() + 1) / 2 : var18.count());
                        syncSelectedSlot(var18, var20);
                        if (var3 == ContainerInput.THROW) {
                           sendContainerInput(-999, 0, ContainerInput.PICKUP);
                        }
                     }

                     return true;
                  }
               } finally {
                  handlingClick = (boolean)0;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static MapSlotTracker.SlotRef inventoryLocation(int var0) {
      return new MapSlotTracker.SlotRef("inventory", "", var0);
   }

   private static MapSlotTracker.SlotRef cursorLocation() {
      return new MapSlotTracker.SlotRef("cursor", "", 0);
   }

   private static MapSlotTracker.SlotRef entityLocation(Entity var0) {
      return new MapSlotTracker.SlotRef("entity", var0.getStringUUID(), 0);
   }

   private static int mapIdOf(ItemStack var0) {
      MapId var1 = var0.is(Items.FILLED_MAP) ? (MapId)var0.get(DataComponents.MAP_ID) : null;
      return var1 == null ? -1 : var1.id();
   }

   private static void findMenuSlot(Map<MapSlotTracker.SlotRef, MapSlotTracker.SlotContents> var0, MapSlotTracker.SlotRef var1, ItemStack var2) {
      var0.put(var1, new MapSlotTracker.SlotContents(mapIdOf(var2), var2.getCount()));
      if (mapIdOf(var2) >= 0) {
         mapTemplates.put(mapIdOf(var2), var2.copyWithCount(1));
      }
   }

   private static String hasSelectedItem(Minecraft var0) {
      DispenserRegistry var1 = DispenserRegistry.getInstance();
      return var0.player != null && var0.player.containerMenu instanceof DispenserMenu var2 && var1.getActiveKey() == var2.containerId
         ? var1.getActiveKey()
         : null;
   }

   private static Map<MapSlotTracker.SlotRef, MapSlotTracker.SlotContents> snapshotStacks(Minecraft var0) {
      LinkedHashMap var1 = new LinkedHashMap();
      if (var0.player != null && var0.level != null) {
         for (int var2 = 0; var2 < var0.player.getInventory().getContainerSize(); var2++) {
            findMenuSlot(var1, inventoryLocation(var2), var0.player.getInventory().getItem(var2));
         }

         findMenuSlot(var1, cursorLocation(), var0.player.containerMenu.getCarried());
         String var7 = hasSelectedItem(var0);
         if (var7 != null) {
            Map var3 = stockSlotsByContainer.computeIfAbsent(var7, var0x -> new HashMap<>());

            for (int var4 = 0; var4 < 9; var4++) {
               ItemStack var5 = var0.player.containerMenu.getSlot(var4).getItem();
               findMenuSlot(var1, new MapSlotTracker.SlotRef("stock", var7, var4), var5);
               if (mapIdOf(var5) >= 0) {
                  var3.put(mapIdOf(var5), var4);
               }
            }
         }

         for (Entity var10 : var0.level.entitiesForRendering()) {
            if (var10 instanceof ItemFrame || var10 instanceof ItemEntity) {
               MapSlotTracker.SlotRef var12 = entityLocation(var10);
               trackedEntities.put(var12, var10);
               findMenuSlot(var1, var12, var10 instanceof ItemFrame var6 ? var6.getItem() : ((ItemEntity)var10).getItem());
            }
         }

         for (Entry var11 : trackedEntities.entrySet()) {
            if (((Entity)var11.getValue()).isRemoved()) {
               findMenuSlot(var1, (MapSlotTracker.SlotRef)var11.getKey(), ItemStack.EMPTY);
            }
         }

         return var1;
      } else {
         return var1;
      }
   }

   public static void reset() {
      swapState.reset();
      stockSlotsByContainer.clear();
      mapTemplates.clear();
      trackedEntities.clear();
      visualStacks.clear();
      lastGameTime = Long.MIN_VALUE;
      displayedMaps = Map.of();
      displayStacks.clear();
      virtualHotbarSlot = -1;
      actualHotbarSlot = -1;
   }

   public static void update(Minecraft var0) {
      if (lastLevel != var0.level) {
         reset();
         lastLevel = var0.level;
      }

      if (var0.player != null && var0.level != null && var0.level.getGameTime() != lastGameTime) {
         lastGameTime = var0.level.getGameTime();
         if (!swapState.moves.isEmpty()) {
            swapState.observe(snapshotStacks(var0));
         }

         visualStacks.keySet().retainAll(swapState.moves);
         syncSelectedSlot();
      }
   }

   public static void refresh() {
      Minecraft var0 = Minecraft.getInstance();
      update(var0);
      if (var0.player != null && var0.level != null && !swapState.moves.isEmpty()) {
         swapState.observe(snapshotStacks(var0));
      }
   }

   public static boolean isPlayerMoving(Minecraft var0) {
      Input var1 = var0.player.input.keyPresses;
      return !var0.player.onGround()
         || var0.player.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6
         || var1.forward()
         || var1.backward()
         || var1.left()
         || var1.right()
         || var1.jump()
         || var0.options.keyUp.isDown()
         || var0.options.keyDown.isDown()
         || var0.options.keyLeft.isDown()
         || var0.options.keyRight.isDown()
         || var0.options.keyJump.isDown();
   }

   public static boolean swapMapIds(CardForce var0, int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      update(var3);
      String var4 = var0.getInstance();
      if (var4 != null && var1 >= 0 && var3.player != null) {
         ItemStack var5 = var3.player.getInventory().getItem(var1);
         int var6 = mapIdOf(var5);
         int var7 = var0.getTemplateIdForValue(var2);
         if (var6 >= 0 && var7 >= 0 && var6 != var7) {
            Map var8 = stockSlotsByContainer.computeIfAbsent(var4, var0x -> new HashMap<>());

            for (int var9 = 1; var9 <= 10; var9++) {
               int var10 = var0.getTemplateIdForValue(var9);
               int var11 = var0.onMapCountRise(var9);
               if (var11 < 0) {
                  var11 = var0.containerIndexForValue(var9);
               }

               if (var10 >= 0 && var11 >= 0 && var11 < 9) {
                  var8.putIfAbsent(var10, var11);
               }
            }

            ItemStack var12 = var5.copyWithCount(1);
            var12.set(DataComponents.MAP_ID, new MapId(var7));
            mapTemplates.putIfAbsent(var7, var12);
            swapState.recordMove(var4, var6, var7, inventoryLocation(var1), snapshotStacks(var3));
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static ItemStack sendContainerInput(MapSlotTracker.SlotRef var0, ItemStack var1) {
      MapSlotTracker.MapMove var2 = swapState.findMove(var0, mapIdOf(var1));
      if (var2 == null) {
         return null;
      }

      MapSwapInventory.VisualStack var3 = visualStacks.get(var2);
      if (var3 != null && ItemStack.matches(var3.actual(), var1)) {
         return var3.visual();
      }

      ItemStack var4 = var1.copy();
      var4.set(DataComponents.MAP_ID, new MapId(var2.sourceMap));
      visualStacks.put(var2, new MapSwapInventory.VisualStack(var1.copy(), var4));
      return var4;
   }

   public static ItemStack getInventoryDisplayStack(int var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var0 >= 0 && var0 < var1.player.getInventory().getContainerSize() && !swapState.moves.isEmpty()) {
         update(var1);
         if (var0 < 36) {
            mapIdOf();
            return displayStacks.get(var0);
         } else {
            return sendContainerInput(inventoryLocation(var0), var1.player.getInventory().getItem(var0));
         }
      } else {
         return null;
      }
   }

   public static ItemStack swapMapIds(String var0, ItemStack var1) {
      update(Minecraft.getInstance());

      for (MapSlotTracker.MapMove var3 : swapState.moves) {
         if (var3.location != null && !var3.isFromStock() && var3.destMap == mapIdOf(var1)) {
            Entity var4 = trackedEntities.get(var3.location);
            if (var3.location.kind().equals("inventory") && var0.equals("inventory[" + var3.location.slot() + "]")
               || var4 instanceof ItemFrame && var0.equals("item_frame[" + var4.getId() + "]")) {
               return sendContainerInput(var3.location, var1);
            }
         }
      }

      return var1;
   }

   public static ItemStack findMenuSlot(ItemStack var0) {
      update(Minecraft.getInstance());
      ItemStack var1 = sendContainerInput(cursorLocation(), var0);
      return var1 == null ? var0 : var1;
   }

   public static ItemStack getEntityDisplayStack(Entity var0, ItemStack var1) {
      if (swapState.moves.isEmpty()) {
         return var1;
      }

      update(Minecraft.getInstance());
      ItemStack var2 = sendContainerInput(entityLocation(var0), var1);
      if (var2 == null && mapIdOf(var1) >= 0) {
         refresh();
         var2 = sendContainerInput(entityLocation(var0), var1);
      }

      return var2 == null ? var1 : var2;
   }

   public static ItemStack entityLocation(Slot var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var0 != null) {
         if (var0.container == var1.player.getInventory()) {
            return getInventoryDisplayStack(var0.getContainerSlot());
         }

         String var2 = hasSelectedItem(var1);
         if (var2 != null && var0.index >= 0 && var0.index < 9 && !swapState.moves.isEmpty()) {
            update(var1);
            Map var3 = stockSlotsByContainer.getOrDefault(var2, Map.of());
            ItemStack var4 = var0.getItem();
            int var5 = mapIdOf(var4);
            if (var5 < 0) {
               for (Entry var7 : var3.entrySet()) {
                  if ((Integer)var7.getValue() == var0.index && swapState.netCount(var2, (Integer)var7.getKey()) > 0) {
                     var5 = (Integer)var7.getKey();
                     break;
                  }
               }
            }

            int var9 = var3.getOrDefault(var5, -1) == var0.index ? swapState.netCount(var2, var5) : 0;
            if (var9 == 0) {
               return null;
            }

            ItemStack var10 = var4.isEmpty() ? mapTemplates.get(var5) : var4;
            if (var10 == null) {
               return null;
            }

            int var8 = Math.max(0, var4.getCount() + var9);
            return var8 == 0 ? ItemStack.EMPTY : var10.copyWithCount(var8);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static void onStockSlotUpdated(int var0, int var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2.player != null && var2.player.containerMenu.containerId == var0 && var1 >= 0 && var1 < 9) {
         String var3 = hasSelectedItem(var2);
         if (var3 != null) {
            refresh();
            swapState.clearStockMovesFor(new MapSlotTracker.SlotRef("stock", var3, var1));
         }
      }
   }

   public static void isPlayerMoving(int var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var1.player.containerMenu.containerId == var0 && hasSelectedItem(var1) != null) {
         refresh();
         swapState.clearStockMoves();
      }
   }

   private record VisualStack(ItemStack actual, ItemStack visual) {
   }
}
