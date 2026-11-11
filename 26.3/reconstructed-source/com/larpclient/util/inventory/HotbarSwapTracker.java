package com.larpclient.util.inventory;

public final class HotbarSwapTracker {
   private int originalSlot = -1;
   private int swappedSlot = -1;

   public boolean isSwapped() {
      return this.swappedSlot >= 0;
   }

   public boolean isSwappedSlot(int var1) {
      return this.isSwapped() && var1 == this.swappedSlot;
   }

   public void recordSwap(int var1, int var2) {
      if (var1 >= 0 && var1 <= 8 && var2 >= 0 && var2 <= 8) {
         if (!this.isSwapped()) {
            this.originalSlot = var1;
         }

         this.swappedSlot = var2;
      } else {
         throw new IllegalArgumentException("Hotbar slot");
      }
   }

   public int consumeRestoreSlot(int var1, boolean var2) {
      int var3 = var2 && var1 == this.swappedSlot && this.originalSlot != this.swappedSlot ? this.originalSlot : -1;
      this.reset();
      return var3;
   }

   public void reset() {
      this.originalSlot = this.swappedSlot = -1;
   }
}
