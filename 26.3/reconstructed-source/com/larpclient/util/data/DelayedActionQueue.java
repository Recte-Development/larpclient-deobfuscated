package com.larpclient.util.data;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.function.BooleanSupplier;

public final class DelayedActionQueue {
   private final ArrayDeque<DelayedActionQueue.ScheduledAction> queue = new ArrayDeque<>();
   private final int capacity;
   private boolean draining;

   public DelayedActionQueue(int var1) {
      if (var1 < 1) {
         throw new IllegalArgumentException("capacity");
      }

      this.capacity = var1;
   }

   public boolean schedule(long var1, long var3, Runnable var5) {
      if (var3 < 0L) {
         throw new IllegalArgumentException("duration");
      } else if (!this.draining && this.queue.size() < this.capacity) {
         this.queue.addLast(new DelayedActionQueue.ScheduledAction(var1 + var3, Objects.requireNonNull(var5)));
         return true;
      } else {
         return false;
      }
   }

   public int size() {
      return this.queue.size();
   }

   public boolean isDraining() {
      return this.draining;
   }

   public void clear() {
      this.queue.clear();
   }

   public void drain(long var1, boolean var3, BooleanSupplier var4) {
      if (!this.draining) {
         this.draining = (boolean)1;

         try {
            while (!this.queue.isEmpty() && var4.getAsBoolean() && (var3 || var1 - this.queue.peekFirst().deadline() >= 0L)) {
               this.queue.removeFirst().action().run();
            }

            if (!var4.getAsBoolean()) {
               this.queue.clear();
            }
         } catch (RuntimeException | Error var9) {
            this.queue.clear();
            throw var9;
         } finally {
            this.draining = (boolean)0;
         }
      }
   }

   private record ScheduledAction(long deadline, Runnable action) {
   }
}
