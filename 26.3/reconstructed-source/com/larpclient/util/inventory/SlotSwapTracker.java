package com.larpclient.util.inventory;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

public final class SlotSwapTracker {
   private int[] lastItemIds;
   private int[] lastSlotValues;
   private final Map<Integer, ArrayDeque<Integer>> pendingValuesByItem = new HashMap<>();

   public void reset() {
      this.lastItemIds = null;
      this.lastSlotValues = null;
      this.pendingValuesByItem.clear();
   }

   public int pollPending(int var1, int var2) {
      ArrayDeque var3 = this.pendingValuesByItem.get(var1);
      return var3 != null && !var3.isEmpty() ? (Integer)var3.removeFirst() : var2;
   }

   public boolean swapIfMatching(int var1, int var2) {
      if (this.lastItemIds != null
         && this.lastItemIds[var1] > 0
         && this.lastItemIds[var1] == this.lastItemIds[var2]
         && this.lastSlotValues[var1] != this.lastSlotValues[var2]) {
         int var3 = this.lastSlotValues[var1];
         this.lastSlotValues[var1] = this.lastSlotValues[var2];
         this.lastSlotValues[var2] = var3;
         return true;
      } else {
         return false;
      }
   }

   public boolean hasSnapshot() {
      return this.lastItemIds != null;
   }

   public int getSlotValue(int var1) {
      return this.lastSlotValues[var1];
   }

   public void update(int[] var1, int[] var2) {
      if (this.lastItemIds == null) {
         this.lastItemIds = (int[])var1.clone();
         this.lastSlotValues = (int[])var2.clone();
      } else {
         for (int var3 = 0; var3 < var1.length; var3++) {
            if (var1[var3] != -2 && var1[var3] != this.lastItemIds[var3] && this.lastItemIds[var3] > 0) {
               this.pendingValuesByItem.computeIfAbsent(this.lastItemIds[var3], var0 -> new ArrayDeque<>()).add(this.lastSlotValues[var3]);
            }
         }

         for (int var5 = 0; var5 < var1.length; var5++) {
            if (var1[var5] != -2 && var1[var5] != this.lastItemIds[var5]) {
               ArrayDeque var4 = this.pendingValuesByItem.get(var1[var5]);
               this.lastSlotValues[var5] = var4 != null && !var4.isEmpty() ? (Integer)var4.removeFirst() : var2[var5];
               this.lastItemIds[var5] = var1[var5];
            }
         }
      }
   }
}
