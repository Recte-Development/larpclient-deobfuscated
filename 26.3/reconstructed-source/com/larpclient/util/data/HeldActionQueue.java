package com.larpclient.util.data;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.function.BooleanSupplier;

public final class HeldActionQueue {
   private final ArrayDeque<Runnable> actions = new ArrayDeque<>();
   private final int capacity;
   private long releaseTime;
   private boolean holding;
   private boolean flushing;

   public HeldActionQueue(int var1) {
      if (var1 < 1) {
         throw new IllegalArgumentException("capacity");
      }

      this.capacity = var1;
   }

   public void beginHold(long var1, long var3) {
      if (!this.holding && !this.flushing && this.actions.isEmpty() && var3 >= 0L) {
         this.releaseTime = var1 + var3;
         this.holding = (boolean)1;
      } else {
         throw new IllegalStateException("Invalid hold start");
      }
   }

   public boolean enqueue(Runnable var1) {
      if (this.holding && !this.flushing && this.actions.size() < this.capacity) {
         this.actions.addLast(Objects.requireNonNull(var1));
         return true;
      } else {
         return false;
      }
   }

   public boolean isHoldExpired(long var1) {
      return this.holding && var1 - this.releaseTime >= 0L;
   }

   public boolean isHolding() {
      return this.holding;
   }

   public boolean isFlushing() {
      return this.flushing;
   }

   public int size() {
      return this.actions.size();
   }

   public void flush(BooleanSupplier var1) {
      if (!this.flushing) {
         this.holding = (boolean)0;
         this.flushing = (boolean)1;

         try {
            while (!this.actions.isEmpty() && var1.getAsBoolean()) {
               this.actions.removeFirst().run();
            }
         } finally {
            this.actions.clear();
            this.flushing = (boolean)0;
            this.releaseTime = 0L;
         }
      }
   }

   public void cancel() {
      this.actions.clear();
      this.holding = (boolean)0;
      this.releaseTime = 0L;
   }
}
