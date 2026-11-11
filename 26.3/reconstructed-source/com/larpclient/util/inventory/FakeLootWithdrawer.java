package com.larpclient.util.inventory;

import com.larpclient.gui.screen.FakeSpawnerScreen;
import com.larpclient.hud.VisualInventory;
import com.larpclient.render.world.ItemStackMarkers;
import com.larpclient.social.misc.PlayerMessages;
import com.larpclient.util.entity.FarmMobType;
import com.larpclient.util.misc.DelayedTaskScheduler;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PriceTagParser;
import com.larpclient.util.world.FakeSpawnerController;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;

public final class FakeLootWithdrawer {
   private static final Map<Integer, ItemStack> fakeLoot = new LinkedHashMap<>();
   private static final Map<Item, Integer> pendingStacks = new LinkedHashMap<>();
   private static final Map<Item, Integer> purchasedCounts = new LinkedHashMap<>();
   private static Object player;
   private static Object level;
   private static BlockPos spawnerPos;
   private static AbstractContainerMenu shopMenu;
   private static boolean active;
   private static boolean awaitingShopMenu;
   private static boolean awaitingPurchase;
   private static boolean arranging;
   private static boolean reopenInventory;
   private static boolean stepScheduled;
   private static int tickCount;
   private static int deadlineTick;
   private static int suppressUntilTick;
   private static int clientTick;
   private static int lastActionTick;
   private static int countBeforePurchase;
   private static int arrangeSlot = -1;
   private static int arrangeTargetCount;
   private static Item currentItem;
   private static boolean priceUnreadable;

   public static boolean hasFakeStack(int var0) {
      return fakeLoot.containsKey(var0);
   }

   public static int countFakeItems(Item var0) {
      int var1 = 0;

      for (ItemStack var3 : fakeLoot.values()) {
         if (var3.is(var0)) {
            var1 += var3.getCount();
         }
      }

      return var1;
   }

   public static ItemStack overrideSlotStack(int var0, ItemStack var1) {
      if (!var1.isEmpty()) {
         return var1;
      }

      ItemStack var2 = fakeLoot.get(var0);
      if (var2 == null) {
         return var1;
      }

      int var3 = purchasedCounts.getOrDefault(var2.getItem(), 0);

      for (Entry var5 : fakeLoot.entrySet()) {
         if (((ItemStack)var5.getValue()).is(var2.getItem())) {
            if ((Integer)var5.getKey() == var0) {
               int var6 = var2.getCount() - Math.min(var3, var2.getCount());
               return var6 <= 0 ? var1 : ItemStackMarkers.markPreview(new ItemStack(var2.getItem(), var6));
            }

            var3 = Math.max(0, var3 - ((ItemStack)var5.getValue()).getCount());
         }
      }

      return var1;
   }

   public static int addFakeLoot(Item var0, int var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (!active
         && var1 > 0
         && FarmMobType.isDropItem(var0)
         && var2.player != null
         && var2.player.containerMenu == var2.player.inventoryMenu
         && var2.player.inventoryMenu.getCarried().isEmpty()) {
         if (player != var2.player || level != var2.level) {
            reset();
         }

         player = var2.player;
         level = var2.level;

         for (int var3 = 0; var3 < 36; var3++) {
            int var4 = VisualInventory.getPhysicalSlot(var3);
            if (var4 >= 0 && !FakeSpawnerController.getInstance().isReservedSlot(var4) && var2.player.getInventory().getItem(var4).isEmpty()) {
               ItemStack var5 = fakeLoot.get(var4);
               if (var5 == null || var5.is(var0) && var5.getCount() < 64) {
                  int var6 = Math.min(var1, 64 - (var5 == null ? 0 : var5.getCount()));
                  fakeLoot.put(var4, new ItemStack(var0, var6 + (var5 == null ? 0 : var5.getCount())));
                  VisualInventory.swapSlots(var3, var4);
                  return var6;
               }
            }
         }

         return 0;
      } else {
         return 0;
      }
   }

   public static void withdrawAll() {
      withdraw(null);
   }

   public static void withdraw(BlockPos var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (!fakeLoot.isEmpty() && !active && var1.player != null) {
         if (var1.player.containerMenu == var1.player.inventoryMenu && var1.player.inventoryMenu.getCarried().isEmpty()) {
            pendingStacks.clear();
            spawnerPos = var0 == null ? null : var0.immutable();

            for (ItemStack var3 : fakeLoot.values()) {
               pendingStacks.merge(var3.getItem(), var3.getCount(), Integer::sum);
            }

            pendingStacks.replaceAll((var0x, var1x) -> PriceTagParser.stackCount(var1x));
            awaitingShopMenu = (boolean)1;
            active = (boolean)1;
            suppressUntilTick = tickCount + 120;
            deadlineTick = tickCount + 100;
            var1.player.connection.sendCommand("shop");
         } else {
            finish("Empty your cursor before withdrawing loot.");
         }
      }
   }

   public static boolean countFakeItems(ClientboundOpenScreenPacket var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (!var1.isSameThread() || !awaitingShopMenu || var1.player == null) {
         return false;
      }

      if (var1.gui.screen() == null && var1.player == player && var1.level == level && tickCount <= deadlineTick) {
         AbstractContainerMenu var2 = var0.getType().create(var0.getContainerId(), var1.player.getInventory());
         if (!(var2 instanceof ChestMenu)) {
            finish("Unsupported shop menu.");
            return false;
         } else {
            shopMenu = var2;
            var1.player.containerMenu = var2;
            awaitingShopMenu = (boolean)0;
            deadlineTick = tickCount + 100;
            return true;
         }
      } else {
         finish("Shop purchase stopped: another screen opened or the request expired.");
         return false;
      }
   }

   public static void onClientTick() {
      if (Minecraft.getInstance().isSameThread()) {
         clientTick++;
         if (active && awaitingPurchase && !stepScheduled) {
            stepScheduled = (boolean)1;
            DelayedTaskScheduler.schedule(0L, () -> {
               stepScheduled = (boolean)0;
               if (active && awaitingPurchase) {
                  advance(Minecraft.getInstance());
               }
            });
         }
      }
   }

   public static boolean isBusy() {
      return Minecraft.getInstance().isSameThread() && tickCount < suppressUntilTick;
   }

   public static boolean filterChatMessage(Component var0, boolean var1) {
      if (!isBusy()) {
         return false;
      }

      String var2 = var0.getString().toLowerCase(Locale.ROOT);
      int var3 = !var2.contains("not enough")
            && !var2.contains("insufficient")
            && !var2.contains("inventory full")
            && !var2.contains("inventory is full")
            && !var2.contains("cannot afford")
            && !var2.contains("can't afford")
         ? 0
         : 1;
      int var4 = var3 == 0
            && !var2.contains("bought")
            && !var2.contains("purchas")
            && !var2.contains("paid")
            && !var2.contains("shop")
            && !var2.contains("received")
            && !var2.contains("successfully")
            && !var2.contains("deducted")
         ? 0
         : 1;
      if (var3 != 0 && active) {
         finish("Shop purchase failed; remaining withdrawals cancelled.");
      }

      return var1 || var4 != 0;
   }

   public static void onTick(Minecraft var0) {
      tickCount++;
      advance(var0);
   }

   private static void advance(Minecraft var0) {
      if (player == null || player == var0.player && level == var0.level && FakeSpawnerController.getInstance().isActive()) {
         if (!active) {
            if (!fakeLoot.isEmpty() && !(var0.gui.screen() instanceof FakeSpawnerScreen)) {
               reset();
            }
         } else if (var0.player != null && var0.gameMode != null) {
            if (var0.gui.screen() instanceof InventoryScreen) {
               reopenInventory = (boolean)1;
               var0.gui.setScreen(null);
            }

            if (var0.gui.screen() != null || var0.player.isDeadOrDying()) {
               finish("Shop purchase cancelled.");
            } else if (tickCount > deadlineTick) {
               finish(
                  priceUnreadable
                     ? "Shop purchase stopped: cannot read the server price (details in latest.log)."
                     : "Shop purchase timed out; no retry was sent."
               );
            } else if (!awaitingShopMenu) {
               if (arranging) {
                  arrangeLoot(var0);
               } else if (shopMenu != null && var0.player.containerMenu == shopMenu) {
                  if (awaitingPurchase) {
                     if (clientTick <= lastActionTick || onTick(currentItem) < countBeforePurchase + 64 || !shopMenu.getCarried().isEmpty()) {
                        return;
                     }

                     pendingStacks.computeIfPresent(currentItem, (var0x, var1) -> var1 - 1);
                     purchasedCounts.merge(currentItem, 64, Integer::sum);
                     suppressUntilTick = tickCount + 20;
                     awaitingPurchase = (boolean)0;
                     deadlineTick = tickCount + 100;
                  }

                  currentItem = pendingStacks.entrySet().stream().filter(var0x -> var0x.getValue() > 0).map(Entry::getKey).findFirst().orElse(null);
                  if (currentItem == null) {
                     var0.player.closeContainer();
                     shopMenu = null;
                     arranging = (boolean)1;
                     deadlineTick = tickCount + 100;
                  } else {
                     for (Slot var2 : shopMenu.slots) {
                        if (var2.container != var0.player.getInventory() && var2.getItem().is(currentItem)) {
                           ItemLore var3 = (ItemLore)var2.getItem().get(DataComponents.LORE);
                           List var4 = var3 == null ? List.of() : var3.lines().stream().map(Component::getString).toList();
                           PriceTagParser.PriceVerdict var5 = PriceTagParser.classifyPrice(var4);
                           if (var5 == PriceTagParser.PriceVerdict.PLAUSIBLE) {
                              if (shopMenu.getCarried().isEmpty() && onClientTick(currentItem) >= 64) {
                                 priceUnreadable = (boolean)0;
                                 countBeforePurchase = onTick(currentItem);
                                 lastActionTick = clientTick;
                                 awaitingPurchase = (boolean)1;
                                 suppressUntilTick = tickCount + 120;
                                 deadlineTick = tickCount + 100;
                                 var0.gameMode.handleContainerInput(shopMenu.containerId, var2.index, 0, ContainerInput.PICKUP, var0.player);
                                 return;
                              }

                              finish("Shop purchase stopped: inventory or cursor is full.");
                              return;
                           }

                           if (var5 == PriceTagParser.PriceVerdict.UNCLEAR && !priceUnreadable) {
                              LarpLog.warn("FakeSpawner", "Waiting for shop price for {}. Server lore: {}", BuiltInRegistries.ITEM.getKey(currentItem), var4);
                           }

                           if (var5 != PriceTagParser.PriceVerdict.UNCLEAR) {
                              LarpLog.warn(
                                 "FakeSpawner", "Purchase blocked for {}: {}. Server lore: {}", BuiltInRegistries.ITEM.getKey(currentItem), var5, var4
                              );
                              finish("Shop purchase blocked: server listing is above $3M.");
                              return;
                           }

                           priceUnreadable = (boolean)1;
                        }
                     }
                  }
               } else {
                  finish("Shop closed before purchases completed.");
               }
            }
         } else {
            reset();
         }
      } else {
         finish(null);
         suppressUntilTick = 0;
      }
   }

   private static int onTick(Item var0) {
      Inventory var1 = Minecraft.getInstance().player.getInventory();
      int var2 = 0;

      for (int var3 = 0; var3 < 36; var3++) {
         if (var1.getItem(var3).is(var0)) {
            var2 += var1.getItem(var3).getCount();
         }
      }

      return var2;
   }

   private static int onClientTick(Item var0) {
      Inventory var1 = Minecraft.getInstance().player.getInventory();
      int var2 = 0;

      for (int var3 = 0; var3 < 36; var3++) {
         if (!FakeSpawnerController.getInstance().isReservedSlot(var3)) {
            ItemStack var4 = var1.getItem(var3);
            if (var4.isEmpty()) {
               var2 += 64;
            } else if (var4.is(var0)) {
               var2 += Math.max(0, var4.getMaxStackSize() - var4.getCount());
            }
         }
      }

      return var2;
   }

   private static int hotbarToMenuSlot(int var0) {
      return var0 < 9 ? var0 + 36 : var0;
   }

   private static void arrangeLoot(Minecraft var0) {
      if (var0.player.containerMenu != var0.player.inventoryMenu) {
         finish("Inventory changed; purchased loot kept.");
      } else if (!ContainerTransaction.isActive()) {
         if (arrangeSlot >= 0) {
            if (var0.player.getInventory().getItem(arrangeSlot).getCount() < arrangeTargetCount || !var0.player.inventoryMenu.getCarried().isEmpty()) {
               return;
            }

            arrangeSlot = -1;
         }

         if (var0.player.inventoryMenu.getCarried().isEmpty()) {
            for (Entry var2 : fakeLoot.entrySet()) {
               int var3 = (Integer)var2.getKey();
               ItemStack var4 = (ItemStack)var2.getValue();
               ItemStack var5 = var0.player.getInventory().getItem(var3);
               if (var5.isEmpty() || var5.is(var4.getItem())) {
                  int var6 = var4.getCount() - var5.getCount();
                  if (var6 > 0) {
                     for (int var7 = 0; var7 < 36; var7++) {
                        if (var7 != var3) {
                           ItemStack var8 = var0.player.getInventory().getItem(var7);
                           if (var8.is(var4.getItem())) {
                              int var9 = fakeLoot.containsKey(var7) && fakeLoot.get(var7).is(var8.getItem()) ? fakeLoot.get(var7).getCount() : 0;
                              int var10 = Math.min(var6, var8.getCount() - var9);
                              if (var10 > 0) {
                                 arrangeSlot = var3;
                                 arrangeTargetCount = var5.getCount() + var10;
                                 lastActionTick = clientTick;
                                 deadlineTick = tickCount + 100;
                                 int var11 = var8.getCount();
                                 ContainerTransaction.beginInInventory(
                                    "arrange purchased loot",
                                    ContainerTransaction.begin(var7, var3, var10, var11),
                                    VisualInventory::isActive,
                                    () -> finish("Inventory corrected; purchased loot kept in its real slots.")
                                 );
                                 return;
                              }
                           }
                        }
                     }
                  }
               }
            }

            finish(null);
         }
      }
   }

   private static void finish(String var0) {
      Minecraft var1 = Minecraft.getInstance();
      int var2 = reopenInventory && var1.player == player ? 1 : 0;
      if (spawnerPos != null && var1.player == player && var1.level == level) {
         LinkedHashMap var3 = new LinkedHashMap();

         for (ItemStack var5 : fakeLoot.values()) {
            var3.merge(var5.getItem(), var5.getCount(), Integer::sum);
         }

         var3.forEach(
            (var0x, var1x) -> FakeSpawnerController.getInstance().savePiles(spawnerPos, var0x, Math.min(var1x, purchasedCounts.getOrDefault(var0x, 0)))
         );
      }

      if (var1.player == player && shopMenu != null && var1.player.containerMenu == shopMenu) {
         var1.player.closeContainer();
      }

      reset();
      suppressUntilTick = tickCount + 20;
      if (var0 != null && var1.player != null) {
         PlayerMessages.sendMessage(Component.literal(var0), false);
      }

      if (var2 != 0 && var1.player != null) {
         var1.gui.setScreen(new InventoryScreen(var1.player));
      }
   }

   public static void finish() {
      finish(null);
      suppressUntilTick = 0;
   }

   private static void reset() {
      fakeLoot.clear();
      pendingStacks.clear();
      purchasedCounts.clear();
      shopMenu = null;
      spawnerPos = null;
      level = null;
      player = null;
      stepScheduled = (boolean)0;
      reopenInventory = (boolean)0;
      priceUnreadable = (boolean)0;
      arranging = (boolean)0;
      awaitingPurchase = (boolean)0;
      awaitingShopMenu = (boolean)0;
      active = (boolean)0;
      arrangeSlot = -1;
      VisualInventory.isActive();
   }
}
