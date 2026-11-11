package com.larpclient.util.inventory;

import com.larpclient.hud.VisualInventory;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.world.FakeSpawnerController;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;

public final class ContainerTransaction {
   private static AbstractContainerMenu menu;
   private static Object owner;
   private static Object level;
   private static List<ContainerTransaction.Click> clicks = List.of();
   private static List<ItemStack> expectedSlots = List.of();
   private static ItemStack expectedCursor = ItemStack.EMPTY;
   private static ItemStack finalCursor = ItemStack.EMPTY;
   private static Runnable onComplete;
   private static Runnable onCancel;
   private static String label;
   private static int step;
   private static int tickCount;
   private static boolean internalClick;

   private ContainerTransaction() {
   }

   public static boolean isActive() {
      return menu != null;
   }

   public static boolean isInternalClick() {
      return internalClick;
   }

   public static void runInternal(Runnable var0) {
      if (!isActive() || internalClick) {
         boolean var1 = internalClick;
         internalClick = (boolean)1;

         try {
            var0.run();
         } finally {
            internalClick = var1;
         }
      }
   }

   public static int toMenuSlot(int var0) {
      return var0 == 40 ? 45 : (var0 < 9 ? var0 + 36 : var0);
   }

   public static List<ContainerTransaction.Click> begin(int var0, int var1, int var2, int var3) {
      if (var0 != var1 && var2 > 0 && var2 <= var3 && var3 <= 64) {
         ArrayList var4 = new ArrayList();
         var4.add(new ContainerTransaction.Click(toMenuSlot(var0), 0, ContainerInput.PICKUP));
         if (var2 == var3) {
            var4.add(new ContainerTransaction.Click(toMenuSlot(var1), 0, ContainerInput.PICKUP));
         } else {
            for (int var5 = 0; var5 < var2; var5++) {
               var4.add(new ContainerTransaction.Click(toMenuSlot(var1), 1, ContainerInput.PICKUP));
            }

            var4.add(new ContainerTransaction.Click(toMenuSlot(var0), 0, ContainerInput.PICKUP));
         }

         return var4;
      } else {
         throw new IllegalArgumentException("Invalid backing transfer");
      }
   }

   public static boolean beginInInventory(String var0, List<ContainerTransaction.Click> var1, Runnable var2, Runnable var3) {
      Minecraft var4 = Minecraft.getInstance();
      return var4.player != null && begin(var4.player.inventoryMenu, var0, var1, var2, var3);
   }

   public static boolean begin(AbstractContainerMenu var0, String var1, List<ContainerTransaction.Click> var2, Runnable var3, Runnable var4) {
      return tick(var0, var1, var2, ItemStack.EMPTY, var3, var4);
   }

   public static boolean tick(AbstractContainerMenu var0, String var1, List<ContainerTransaction.Click> var2, ItemStack var3, Runnable var4, Runnable var5) {
      Minecraft var6 = Minecraft.getInstance();
      if (!isActive()
         && !var2.isEmpty()
         && var2.size() <= 132
         && var6.player != null
         && var6.gameMode != null
         && var6.player.containerMenu == var0
         && var0.getCarried().isEmpty()) {
         menu = var0;
         owner = var6.player;
         level = var6.level;
         clicks = List.copyOf(var2);
         tickCount = 0;
         step = 0;
         onComplete = var4;
         onCancel = var5;
         label = var1;
         finalCursor = var3.copy();
         snapshot();
         LarpLog.debug("FakeSpawner", "Transaction started: {} clicks={} container={}", var1, var2.size(), menu.containerId);
         return true;
      } else {
         return false;
      }
   }

   private static void snapshot() {
      expectedSlots = menu.slots.stream().map(var0 -> var0.getItem().copy()).toList();
      expectedCursor = menu.getCarried().copy();
   }

   static boolean matchesSnapshot(AbstractContainerMenu var0, List<ItemStack> var1, ItemStack var2) {
      if (var0.slots.size() == var1.size() && ItemStack.matches(var0.getCarried(), var2)) {
         for (int var3 = 0; var3 < var1.size(); var3++) {
            if (!ItemStack.matches(var0.getSlot(var3).getItem(), (ItemStack)var1.get(var3))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static void onInventoryCorrection() {
      if (isActive() && !matchesSnapshot(menu, expectedSlots, expectedCursor)) {
         cancel("server inventory correction");
      }

      VisualInventory.isActive();
   }

   public static void tick() {
      if (isActive()) {
         Minecraft var0 = Minecraft.getInstance();
         if (var0.player == owner
            && var0.level == level
            && var0.player.containerMenu == menu
            && var0.player.isAlive()
            && FakeSpawnerController.getInstance().isActive()) {
            if (++tickCount <= 200 && matchesSnapshot(menu, expectedSlots, expectedCursor)) {
               if (step == clicks.size()) {
                  if (!ItemStack.matches(menu.getCarried(), finalCursor)) {
                     cancel("unexpected cursor at completion");
                  } else {
                     Runnable var6 = onComplete;
                     LarpLog.debug("FakeSpawner", "Transaction predicted complete: {} clicks={}", label, step);
                     reset();
                     if (var6 != null) {
                        var6.run();
                     }

                     VisualInventory.isActive();
                  }
               } else {
                  for (int var1 = 0; var1 < 4 && step < clicks.size(); var1++) {
                     ContainerTransaction.Click var2 = clicks.get(step++);
                     internalClick = (boolean)1;

                     try {
                        var0.gameMode.handleContainerInput(menu.containerId, var2.slot, var2.button, var2.type, var0.player);
                     } finally {
                        internalClick = (boolean)0;
                     }

                     snapshot();
                     VisualInventory.isActive();
                  }
               }
            } else {
               cancel(tickCount > 200 ? "timeout" : "inventory changed");
            }
         } else {
            cancel("owner or container changed");
         }
      }
   }

   public static void cancel(String var0) {
      if (isActive()) {
         Minecraft var1 = Minecraft.getInstance();
         Runnable var2 = onCancel;
         int var3 = var1.player == owner && var1.level == level && var1.player.containerMenu == menu ? 1 : 0;
         LarpLog.debug(
            "FakeSpawner",
            "Transaction cancelled: {} step={}/{} reason={}; native cursor recovery={}",
            label,
            step,
            clicks.size(),
            var0,
            Boolean.valueOf((boolean)var3)
         );
         reset();
         if (var3 != 0) {
            var1.player.closeContainer();
         }

         if (var2 != null) {
            var2.run();
         }

         VisualInventory.isActive();
      }
   }

   private static void reset() {
      menu = null;
      level = null;
      owner = null;
      clicks = List.of();
      expectedSlots = List.of();
      expectedCursor = finalCursor = ItemStack.EMPTY;
      onCancel = null;
      onComplete = null;
      label = null;
      tickCount = 0;
      step = 0;
   }

   public record Click(int slot, int button, ContainerInput type) {
   }
}
