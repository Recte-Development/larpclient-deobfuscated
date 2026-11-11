package com.larpclient.util.data;

final class ActionConfirmationTracker {
   static final int maxTrackedValue = 32768;
   private Object token;
   private long startedAtNanos;
   private long timeoutMillis = timeoutFor(0);
   private long issuedAt;
   private long fullSyncAt;
   private long slotSyncAt;
   private long cursorSyncAt;
   private long otherSlotSyncAt;
   private int slot;
   private int otherSlot;

   static long timeoutFor(int var0) {
      return Math.max(15000L, Math.min(60000L, 5000L + 6L * Math.max(0, var0)));
   }

   void setLatency(int var1) {
      this.timeoutMillis = timeoutFor(var1);
   }

   boolean isStale(long var1) {
      return System.nanoTime() - var1 > 4L * this.timeoutMillis * 1000000L;
   }

   boolean isExpired() {
      return this.isExpiredAt(System.nanoTime());
   }

   boolean isExpiredAt(long var1) {
      return var1 - this.startedAtNanos > this.timeoutMillis * 1000000L;
   }

   void begin(Object var1, int var2, long var3) {
      this.beginSwap(var1, var2, var2, var3);
   }

   void beginSwap(Object var1, int var2, int var3, long var4) {
      this.startedAtNanos = System.nanoTime();
      this.token = var1;
      this.slot = var2;
      this.otherSlot = var3;
      this.issuedAt = var4;
      this.fullSyncAt = this.slotSyncAt = this.cursorSyncAt = this.otherSlotSyncAt = 0L;
   }

   void recordSync(Object var1, long var2, boolean var4, int var5, boolean var6) {
      if (var1 == this.token && var2 > this.issuedAt) {
         if (var4) {
            this.fullSyncAt = var2;
         }

         if (var5 == this.slot) {
            this.slotSyncAt = var2;
         }

         if (var5 == this.otherSlot) {
            this.otherSlotSyncAt = var2;
         }

         if (var6) {
            this.cursorSyncAt = var2;
         }
      }
   }

   boolean isConfirmed() {
      return this.fullSyncAt > this.issuedAt || this.slotSyncAt > this.issuedAt && this.otherSlotSyncAt > this.issuedAt && this.cursorSyncAt > this.issuedAt;
   }

   boolean isFullySynced() {
      return this.fullSyncAt > this.issuedAt;
   }

   boolean isConfirmedAnd(boolean var1) {
      return this.isConfirmed() && var1;
   }

   boolean isConfirmedAndAll(boolean var1, boolean var2, boolean var3) {
      return this.isConfirmedAnd(var1 && var2 && var3);
   }

   String describe() {
      return "issued="
         + this.issuedAt
         + " full="
         + this.fullSyncAt
         + " slot="
         + this.slotSyncAt
         + " other="
         + this.otherSlotSyncAt
         + " cursor="
         + this.cursorSyncAt;
   }
}
