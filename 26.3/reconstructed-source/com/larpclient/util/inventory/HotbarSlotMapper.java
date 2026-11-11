package com.larpclient.util.inventory;

public final class HotbarSlotMapper {
   private final int[] slotMap = new int[36];
   private final boolean[] pinned = new boolean[36];
   private long lastPresentMask = -1L;
   private long lastBlockedMask = -1L;
   private long updateCount;
   private int[] cachedResult;

   public HotbarSlotMapper() {
      int var1 = 0;

      while (var1 < 36) {
         this.slotMap[var1] = var1++;
      }
   }

   public int[] resolve(long var1, long var3) {
      if (this.cachedResult != null && var1 == this.lastPresentMask && var3 == this.lastBlockedMask) {
         return this.cachedResult;
      }

      boolean[] var5 = new boolean[36];
      boolean[] var6 = new boolean[36];

      for (int var7 = 0; var7 < 36; var7++) {
         var5[var7] = (boolean)((var1 & 1L << var7) != 0L ? 1 : 0);
         var6[var7] = (boolean)((var3 & 1L << var7) != 0L ? 1 : 0);
      }

      this.reconcile(var5, var6);
      this.lastPresentMask = var1;
      this.lastBlockedMask = var3;
      this.cachedResult = this.applyBlocked(var6);
      this.updateCount++;
      return this.cachedResult;
   }

   public long getUpdateCount() {
      return this.updateCount;
   }

   public void findFreeSlot(int[] var1) {
      if (var1.length != 36) {
         throw new IllegalArgumentException("Expected 36 slots");
      }

      System.arraycopy(var1, 0, this.slotMap, 0, 36);
      this.cachedResult = null;
   }

   public void reconcile(boolean[] var1, boolean[] var2) {
      this.cachedResult = null;
      if (var1.length == 36 && var2.length == 36) {
         boolean[] var3 = new boolean[36];

         for (int var4 = 0; var4 < 36; var4++) {
            int var5 = this.slotMap[var4];
            if (var5 >= 0 && !var2[var5] && !var3[var5]) {
               var3[var5] = (boolean)1;
            } else {
               this.slotMap[var4] = -1;
               this.pinned[var4] = (boolean)0;
            }
         }

         for (int var7 = 0; var7 < 9; var7++) {
            if (this.slotMap[var7] < 0) {
               for (int var9 = 9; var9 < 36; var9++) {
                  int var6 = this.slotMap[var9];
                  if (var6 >= 0 && var1[var6] && !this.pinned[var9]) {
                     this.slotMap[var7] = var6;
                     this.slotMap[var9] = -1;
                     break;
                  }
               }
            }
         }

         for (int var8 = 0; var8 < 36; var8++) {
            if (!var2[var8] && !var3[var8]) {
               int var10 = this.slotMap[var8] < 0 ? var8 : this.findFreeSlot();
               if (var10 < 0) {
                  break;
               }

               this.slotMap[var10] = var8;
               var3[var8] = (boolean)1;
            }
         }
      } else {
         throw new IllegalArgumentException("Expected 36 slots");
      }
   }

   private int findFreeSlot() {
      for (int var1 = 0; var1 < 36; var1++) {
         if (this.slotMap[var1] < 0) {
            return var1;
         }
      }

      return -1;
   }

   public int[] applyBlocked(boolean[] var1) {
      int[] var2 = (int[])this.slotMap.clone();

      for (int var3 = 0; var3 < 36; var3++) {
         if (var2[var3] >= 0 && var1[var2[var3]]) {
            var2[var3] = -1;
         }
      }

      return var2;
   }

   public void pinSlot(int var1, int var2) {
      if (var1 >= 0 && var1 < 36 && var2 >= 0 && var2 < 36) {
         this.cachedResult = null;
         int var3 = this.slotMap[var1];
         byte var4 = 0;

         for (int var5 = 0; var5 < 36; var5++) {
            if (var5 != var1 && this.slotMap[var5] == var2) {
               this.slotMap[var5] = var3;
               this.pinned[var5] = (boolean)0;
               var4 = 1;
               break;
            }
         }

         this.slotMap[var1] = var2;
         this.pinned[var1] = (boolean)1;
         if (var4 == 0 && var3 >= 0 && var3 != var2) {
            int var6 = this.findFreeSlot();
            if (var6 >= 0) {
               this.slotMap[var6] = var3;
               this.pinned[var6] = (boolean)0;
            }
         }
      }
   }

   public void swapTargets(int var1, int var2) {
      this.cachedResult = null;

      for (int var3 = 0; var3 < 36; var3++) {
         if (this.slotMap[var3] == var1) {
            this.slotMap[var3] = var2;
         } else if (this.slotMap[var3] == var2) {
            this.slotMap[var3] = var1;
         }
      }
   }
}
