package com.larpclient.util.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class DelayedTaskScheduler {
   private static final List<DelayedTaskScheduler.Task> pending = new ArrayList<>();

   private DelayedTaskScheduler() {
   }

   public static long randomDelay() {
      return 250L + ThreadLocalRandom.current().nextLong(500L);
   }

   public static void schedule(long var0, Runnable var2) {
      pending.add(new DelayedTaskScheduler.Task(System.currentTimeMillis() + Math.max(0L, var0), var2));
   }

   public static void clear() {
      pending.clear();
   }

   public static void runDue() {
      if (!pending.isEmpty()) {
         long var0 = System.currentTimeMillis();
         ArrayList var2 = new ArrayList();
         pending.removeIf(var3 -> {
            if (var3.dueAt <= var0) {
               var2.add(var3);
               return true;
            } else {
               return false;
            }
         });

         for (DelayedTaskScheduler.Task var4 : var2) {
            var4.action.run();
         }
      }
   }

   private record Task(long at, Runnable action) {
      private final long dueAt;

      private Task(long at, Runnable action) {
         this.dueAt = at;
         this.action = action;
      }
   }
}
