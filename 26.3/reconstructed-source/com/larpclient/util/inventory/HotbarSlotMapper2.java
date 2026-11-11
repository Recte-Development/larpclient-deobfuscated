package com.larpclient.util.inventory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.IntSupplier;

public final class HotbarSlotMapper2 {
   private final ArrayDeque<HotbarSlotMapper2.PendingSwap> pendingSwaps = new ArrayDeque<>();
   private final int[] slotMap = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
   private final List<HotbarSlotMapper2.SlotEntry> entries = new ArrayList<>();

   public List<HotbarSlotMapper2.PendingSwap> drainSwaps() {
      List var1 = List.copyOf(this.pendingSwaps);
      this.pendingSwaps.clear();
      return var1;
   }

   public int mapSlot(int var1) {
      return var1 >= 0 && var1 < 9 ? this.slotMap[var1] : var1;
   }

   public int stockSlotOf(String var1) {
      HotbarSlotMapper2.SlotEntry var2 = this.findEntry(var1);
      return var2 == null ? -1 : var2.stockSlot;
   }

   public void swapKeyToSlot(String var1, int var2) {
      int var3 = this.stockSlotOf(var1);
      if (var3 >= 0 && var2 >= 0 && var2 < 9) {
         this.swapSlots(var3, this.slotMap[var2]);
      }
   }

   public void moveEntryToSlot(int var1, int var2) {
      if (var2 >= 0 && var2 < 9) {
         for (HotbarSlotMapper2.SlotEntry var4 : this.entries) {
            if (var4.slot == var1) {
               int var5 = isStockKey(var4.key) ? Integer.parseInt(var4.key.substring(6)) : var4.stockSlot;
               if (var5 >= 0) {
                  this.swapSlots(this.slotMap[var2], var5);
               }

               return;
            }
         }
      }
   }

   private void swapSlots(int var1, int var2) {
      if (var1 >= 0 && var2 >= 0) {
         for (int var3 = 0; var3 < 9; var3++) {
            if (this.slotMap[var3] == var1) {
               this.slotMap[var3] = var2;
            } else if (this.slotMap[var3] == var2) {
               this.slotMap[var3] = var1;
            }
         }
      }
   }

   public void reset(int[] var1) {
      this.entries.clear();
      this.pendingSwaps.clear();
      int var2 = 0;

      while (var2 < 9) {
         this.slotMap[var2] = var2++;
      }

      for (int var3 = 0; var3 < var1.length; var3++) {
         if (var1[var3] > 0) {
            this.entries.add(new HotbarSlotMapper2.SlotEntry(var1[var3], "stock:" + var3));
         }
      }
   }

   private HotbarSlotMapper2.SlotEntry findEntry(String var1) {
      for (HotbarSlotMapper2.SlotEntry var3 : this.entries) {
         if (var1.equals(var3.key)) {
            return var3;
         }
      }

      return null;
   }

   private static boolean isStockKey(String var0) {
      return var0 != null && var0.startsWith("stock:");
   }

   public int findEntry(String var1, int var2) {
      HotbarSlotMapper2.SlotEntry var3 = this.findEntry(var1);
      return var3 == null ? var2 : var3.slot;
   }

   public boolean hasCustomRemap() {
      for (HotbarSlotMapper2.SlotEntry var2 : this.entries) {
         if (var2.count != var2.slot && !isStockKey(var2.key)) {
            return true;
         }
      }

      return false;
   }

   public boolean findEntry() {
      for (int var1 = 0; var1 < this.slotMap.length; var1++) {
         if (this.slotMap[var1] != var1) {
            return true;
         }
      }

      return this.entries.stream().anyMatch(var0 -> var0.count != var0.slot);
   }

   public int isStockKey() {
      int var1 = 0;

      for (HotbarSlotMapper2.SlotEntry var3 : this.entries) {
         if (var3.slot == 2) {
            var1++;
         }
      }

      return var1;
   }

   public int countCustomAtSlotTwo() {
      int var1 = 0;

      for (HotbarSlotMapper2.SlotEntry var3 : this.entries) {
         if (var3.slot == 2 && var3.key != null && !isStockKey(var3.key)) {
            var1++;
         }
      }

      return var1;
   }

   public void update(Map<String, Integer> var1, Set<String> var2, int var3, int var4, boolean var5) {
      this.updateWithSupplier(var1, var2, () -> var3, var4, var5);
   }

   public boolean isAllStock() {
      return !this.entries.isEmpty() && this.entries.stream().allMatch(var0 -> isStockKey(var0.key));
   }

   public void restoreStock(Map<String, Integer> var1) {
      List var2 = this.entries
         .stream()
         .filter(var0 -> var0.count == 1 && var0.displaced && var0.key != null && !isStockKey(var0.key))
         .sorted(Comparator.comparingInt(var0 -> var0.slot == 1 ? 0 : 1))
         .toList();
      if (!var2.isEmpty()) {
         List var3 = this.entries.stream().filter(var0 -> var0.count == 1 && isStockKey(var0.key)).toList();
         TreeSet var4 = new TreeSet();

         for (HotbarSlotMapper2.SlotEntry var6 : var2) {
            var4.add(var6.stockSlot);
         }

         for (HotbarSlotMapper2.SlotEntry var13 : var3) {
            var4.add(Integer.parseInt(var13.key.substring(6)));
         }

         List var12 = var4.stream().filter(var1x -> var1.getOrDefault("stock:" + var1x, -1) < 1).toList();
         List var14 = var4.stream().filter(var1x -> var1.getOrDefault("stock:" + var1x, -1) == 1).toList();
         if (var12.size() == var2.size() && var14.size() == var3.size()) {
            HotbarSlotMapper2.SlotEntry var7 = this.entries
               .stream()
               .filter(var0 -> var0.count == 2 && var0.slot == 1 && isStockKey(var0.key))
               .findFirst()
               .orElse(null);
            int var8 = var7 == null ? -1 : Integer.parseInt(var7.key.substring(6));

            for (HotbarSlotMapper2.SlotEntry var10 : var2) {
               if (var10.slot == 2 && var8 >= 0) {
                  this.swapSlots(var10.stockSlot, var8);
               }
            }

            for (int var15 = 0; var15 < var3.size(); var15++) {
               ((HotbarSlotMapper2.SlotEntry)var3.get(var15)).key = "stock:" + var14.get(var15);
            }

            for (int var16 = 0; var16 < var2.size(); var16++) {
               HotbarSlotMapper2.SlotEntry var17 = (HotbarSlotMapper2.SlotEntry)var2.get(var16);
               var17.stockSlot = (Integer)var12.get(var16);
               var17.displaced = (boolean)0;
               if (var17.slot == 2 && var8 >= 0) {
                  this.swapSlots(var17.stockSlot, var8);
               }
            }
         }
      }
   }

   public void updateWithSupplier(Map<String, Integer> var1, Set<String> var2, IntSupplier var3, int var4, boolean var5) {
      if (var5) {
         for (HotbarSlotMapper2.SlotEntry var7 : this.entries) {
            if (var7.key != null && !isStockKey(var7.key) && var7.stockSlot >= 0 && var7.displaced) {
               var7.displaced = (boolean)0;
               String var8 = "stock:" + var7.stockSlot;
               if (this.findEntry(var8) == null && var1.getOrDefault(var8, -1) == var7.count) {
                  for (HotbarSlotMapper2.SlotEntry var10 : this.entries) {
                     if (isStockKey(var10.key) && var10.count == var7.count && var1.getOrDefault(var10.key, -1) < 1) {
                        int var11 = Integer.parseInt(var10.key.substring(6));
                        this.swapSlots(var7.stockSlot, var11);
                        var10.key = var8;
                        var7.stockSlot = var11;
                        break;
                     }
                  }
               }
            }
         }
      }

      for (HotbarSlotMapper2.SlotEntry var18 : this.entries) {
         if (var18.key != null && (!isStockKey(var18.key) || var5) && var1.getOrDefault(var18.key, -1) != var18.count) {
            var18.wasStock = isStockKey(var18.key);
            if (var18.wasStock) {
               var18.stockSlot = Integer.parseInt(var18.key.substring(6));
               var18.displaced = (boolean)0;
            }

            var18.key = null;
         }
      }

      ArrayList var17 = new ArrayList(var1.keySet());
      var17.sort(Comparator.<String, Integer>comparing(var0 -> isStockKey(var0) ? 1 : 0).thenComparing(var0 -> (String)var0));

      for (String var20 : var17) {
         int var21 = (Integer)var1.get(var20);
         if (var21 >= 1 && this.findEntry(var20) == null) {
            HotbarSlotMapper2.SlotEntry var22 = null;

            for (HotbarSlotMapper2.SlotEntry var12 : this.entries) {
               if (var12.key == null && var12.count == var21) {
                  if (isStockKey(var20) && var20.equals("stock:" + var12.stockSlot)) {
                     var22 = var12;
                     break;
                  }

                  if (var22 == null || var22.wasStock && !var12.wasStock) {
                     var22 = var12;
                  }
               }
            }

            if (var22 == null && !var5 && !isStockKey(var20)) {
               ArrayList var24 = new ArrayList();

               for (HotbarSlotMapper2.SlotEntry var13 : this.entries) {
                  if (isStockKey(var13.key) && var13.count == var21) {
                     var24.add(var13);
                  }
               }

               if (!var24.isEmpty()) {
                  var22 = (HotbarSlotMapper2.SlotEntry)var24.get(Math.floorMod(var4, var24.size()));
                  var22.wasStock = (boolean)1;
                  var22.displaced = (boolean)1;
                  var22.stockSlot = Integer.parseInt(var22.key.substring(6));
               }
            }

            if (var22 != null) {
               int var25 = var22.wasStock && var2.contains(var20) ? 1 : 0;
               var22.key = var20;
               var22.wasStock = (boolean)0;
               int var27 = var25 != 0 ? var3.getAsInt() : var22.slot;
               if (var25 != 0 && var22.slot != var27) {
                  ArrayList var28 = new ArrayList();

                  for (HotbarSlotMapper2.SlotEntry var15 : this.entries) {
                     if (isStockKey(var15.key) && var15.slot == var27) {
                        var28.add(var15);
                     }
                  }

                  if (!var28.isEmpty()) {
                     HotbarSlotMapper2.SlotEntry var29 = (HotbarSlotMapper2.SlotEntry)var28.get(Math.floorMod(var4, var28.size()));
                     this.swapSlots(var22.stockSlot, Integer.parseInt(var29.key.substring(6)));
                     int var30 = var22.slot;
                     var22.slot = var29.slot;
                     var29.slot = var30;
                  }
               }

               if (var25 != 0) {
                  if (this.pendingSwaps.size() >= 200) {
                     this.pendingSwaps.removeFirst();
                  }

                  this.pendingSwaps.addLast(new HotbarSlotMapper2.PendingSwap(var22.count, var22.slot));
               }
            }
         }
      }
   }

   public record PendingSwap(int actual, int display) {
   }

   private static final class SlotEntry {
      final int count;
      int slot;
      String key;
      boolean wasStock;
      int stockSlot = -1;
      boolean displaced;

      SlotEntry(int var1, String var2) {
         this.count = var1;
         this.slot = var1;
         this.key = var2;
      }
   }
}
