package com.larpclient.util.inventory;

import com.larpclient.render.world.ItemStackMarkers;
import com.larpclient.util.misc.AuctionUtil;
import com.larpclient.util.misc.JsonFiles;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.world.FakeSpawnerController;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class AuctionStackTracker {
   private static Map<Integer, ItemProvenance.Stack> stackRecords = new HashMap<>();
   private static String serverKey;
   private static long graceUntil;
   private static Path savePath;
   private static final Map<String, AuctionStackTracker.PendingPurchase> pendingPurchases = new HashMap<>();
   private static Map<Integer, ItemStack> lastSnapshot = Map.of();
   private static Object lastMenu;
   private static boolean lastAuctionOpen;

   public static void recordPurchase(ItemStack var0, int var1) {
      String var2 = ItemStackMarkers.fingerprint(var0);
      AuctionStackTracker.PendingPurchase var3 = pendingPurchases.get(var2);
      pendingPurchases.put(
         var2, new AuctionStackTracker.PendingPurchase(Math.max(0, var1) + (var3 == null ? 0 : var3.count()), System.currentTimeMillis() + 10000L)
      );
   }

   public static void update() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0.isSameThread()) {
         if (!ContainerTransaction.isActive()) {
            if (var0.player != null && var0.level != null) {
               String var1 = var0.getCurrentServer() == null ? "local" : var0.getCurrentServer().ip;
               int var2 = !var1.equals(serverKey) ? 1 : 0;
               if (var2 != 0) {
                  serverKey = var1;
                  graceUntil = 0L;
                  stackRecords.clear();
                  savePath = FabricLoader.getInstance()
                     .getConfigDir()
                     .resolve("larp-client/fake-spawner-real-stacks-v2-" + ItemStackMarkers.sha256(serverKey) + ".txt");

                  try {
                     if (Files.exists(savePath)) {
                        for (String var4 : Files.readAllLines(savePath)) {
                           String[] var5 = var4.split("\t");
                           if (var5.length == 4) {
                              stackRecords.put(
                                 Integer.parseInt(var5[0]), new ItemProvenance.Stack(var5[1], Integer.parseInt(var5[2]), Integer.parseInt(var5[3]))
                              );
                           }
                        }
                     }
                  } catch (IOException | NumberFormatException var18) {
                     LarpLog.warn("FakeSpawner", "Could not load auction stack exemptions", var18);
                  }
               }

               long var19 = System.currentTimeMillis();
               boolean var20 = AuctionUtil.isAuctionScreen();
               if (var20) {
                  graceUntil = var19 + 10000L;
               } else if (var0.player.containerMenu != var0.player.inventoryMenu) {
                  graceUntil = 0L;
               }

               HashMap var6 = new HashMap();
               Inventory var7 = var0.player.getInventory();

               for (int var8 = 0; var8 < var7.getContainerSize(); var8++) {
                  collectTrackedStacks(var6, var8, var7.getItem(var8));
               }

               collectTrackedStacks(var6, -1, var0.player.containerMenu.getCarried());
               if (!var20) {
                  for (Slot var9 : var0.player.containerMenu.slots) {
                     if (var9.container != var7) {
                        collectTrackedStacks(var6, 1000 + var9.index, var9.getItem());
                     }
                  }
               }

               pendingPurchases.values().removeIf(var2x -> var2x.until() < var19);
               int var22 = var2 == 0
                     && lastMenu == var0.player.containerMenu
                     && lastAuctionOpen == var20
                     && var6.size() == lastSnapshot.size()
                     && var6.entrySet()
                        .stream()
                        .allMatch(var0x -> ItemStack.matches((ItemStack)var0x.getValue(), lastSnapshot.getOrDefault(var0x.getKey(), ItemStack.EMPTY)))
                  ? 1
                  : 0;
               if (var22 == 0) {
                  lastSnapshot = new HashMap<>();
                  var6.forEach((var0x, var1x) -> lastSnapshot.put(var0x, var1x.copy()));
                  lastMenu = var0.player.containerMenu;
                  lastAuctionOpen = var20;
                  HashMap var23 = new HashMap();
                  var6.forEach((var1x, var2x) -> {
                     String var3 = ItemStackMarkers.fingerprint(var2x);
                     ItemProvenance.Stack var4x = stackRecords.get(var1x);
                     int var5x = ItemStackMarkers.isRealPurchase(var2x) ? var2x.getCount() : 0;
                     if (var5x > 0 && var4x != null && var4x.item().equals(var3)) {
                        var5x = Math.min(var5x, var4x.purchased());
                     }

                     var23.put(var1x, new ItemProvenance.Stack(var3, var2x.getCount(), var5x));
                  });
                  if (var2 != 0) {
                     stackRecords.entrySet()
                        .removeIf(
                           var1x -> !var23.containsKey(var1x.getKey())
                              || !((ItemProvenance.Stack)var23.get(var1x.getKey())).item().equals(var1x.getValue().item())
                              || ((ItemProvenance.Stack)var23.get(var1x.getKey())).count() != var1x.getValue().count()
                        );
                  }

                  Map var10 = ItemProvenance.reconcile(stackRecords, var23, var2 == 0 && var19 < graceUntil);
                  pendingPurchases.values().removeIf(var2x -> var2x.until() < var19);

                  for (Entry var12 : new TreeMap(var10).entrySet()) {
                     ItemProvenance.Stack var13 = (ItemProvenance.Stack)var12.getValue();
                     AuctionStackTracker.PendingPurchase var14 = pendingPurchases.get(var13.item());
                     if (var14 != null) {
                        ItemProvenance.Stack var15 = stackRecords.get(var12.getKey());
                        int var16 = var13.count() - (var15 != null && var15.item().equals(var13.item()) ? var15.count() : 0);
                        int var17 = Math.min(Math.max(0, var16), Math.min(var14.count(), var13.count() - var13.purchased()));
                        if (var17 > 0) {
                           var10.put((Integer)var12.getKey(), new ItemProvenance.Stack(var13.item(), var13.count(), var13.purchased() + var17));
                           if (var17 == var14.count()) {
                              pendingPurchases.remove(var13.item());
                           } else {
                              pendingPurchases.put(var13.item(), new AuctionStackTracker.PendingPurchase(var14.count() - var17, var14.until()));
                           }
                        }
                     }
                  }

                  var6.forEach(
                     (var1x, var2x) -> ItemStackMarkers.setRealPurchase(var2x, ((ItemProvenance.Stack)var10.get(var1x)).purchased() == var2x.getCount())
                  );
                  int var24 = !var10.equals(stackRecords) ? 1 : 0;
                  stackRecords = var10;
                  boolean var25 = separatePurchasedStacks(var0);
                  if (var24 != 0 || var25) {
                     save();
                  }
               }
            } else {
               serverKey = null;
               stackRecords.clear();
               pendingPurchases.clear();
               graceUntil = 0L;
               lastSnapshot = Map.of();
               lastMenu = null;
            }
         }
      }
   }

   private static void save() {
      try {
         Files.createDirectories(savePath.getParent());
         List var0 = stackRecords.entrySet()
            .stream()
            .filter(var0x -> var0x.getKey() < 1000 && var0x.getValue().purchased() > 0)
            .map(var0x -> var0x.getKey() + "\t" + var0x.getValue().item() + "\t" + var0x.getValue().count() + "\t" + var0x.getValue().purchased())
            .sorted()
            .toList();
         JsonFiles.writeAtomic(savePath, String.join("\n", var0));
      } catch (IOException var1) {
         LarpLog.warn("FakeSpawner", "Could not save auction stack exemptions", var1);
      }
   }

   private static boolean separatePurchasedStacks(Minecraft var0) {
      if (var0.gameMode != null && var0.player.containerMenu == var0.player.inventoryMenu && var0.player.inventoryMenu.getCarried().isEmpty()) {
         for (Entry var2 : new TreeMap<>(stackRecords).entrySet()) {
            int var3 = (Integer)var2.getKey();
            ItemProvenance.Stack var4 = (ItemProvenance.Stack)var2.getValue();
            if (var3 >= 0 && var3 < 36 && var4.purchased() > 0 && var4.purchased() < var4.count()) {
               int var5 = -1;

               for (int var6 = 0; var6 < 36; var6++) {
                  if (var0.player.getInventory().getItem(var6).isEmpty()) {
                     var5 = var6;
                     break;
                  }
               }

               if (var5 < 0) {
                  return false;
               }

               if (var3 < 9) {
                  int var10000 = var3 + 36;
               }

               if (var5 < 9) {
                  int var9 = var5 + 36;
               }

               if (!FakeSpawnerController.getInstance().isReservedSlot(var3)) {
                  int var8 = var5;
                  return ContainerTransaction.beginInInventory(
                     "separate purchased backing", ContainerTransaction.begin(var3, var8, var4.purchased(), var4.count()), () -> {
                        ItemStackMarkers.setRealPurchase(var0.player.getInventory().getItem(var8), true);
                        stackRecords.put(var3, new ItemProvenance.Stack(var4.item(), var4.count() - var4.purchased(), 0));
                        stackRecords.put(var8, new ItemProvenance.Stack(var4.item(), var4.purchased(), var4.purchased()));
                        lastSnapshot = Map.of();
                        save();
                     }, () -> {
                        lastSnapshot = Map.of();
                        graceUntil = 0L;
                     }
                  );
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private static void collectTrackedStacks(Map<Integer, ItemStack> var0, int var1, ItemStack var2) {
      if (!var2.isEmpty()) {
         String var3 = var2.getItem().builtInRegistryHolder().key().identifier().toString();
         if (FakeSpawnerController.getInstance().writePilesTo(var2.getItem()) != null) {
            var0.put(var1, var2);
         }
      }
   }

   private record PendingPurchase(int count, long until) {
   }
}
