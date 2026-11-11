package com.larpclient.util.data;

import java.util.IdentityHashMap;
import java.util.Map.Entry;
import java.util.function.Predicate;

public final class RecentEventTracker {
   public static final int mAX_ENTRIES = 64;
   public static final double mAX_AGE_SECONDS = 3.0;
   private final IdentityHashMap<Object, Double> pending = new IdentityHashMap<>();
   private final IdentityHashMap<Object, Double> consumed = new IdentityHashMap<>();

   public void markPending(Object var1, boolean var2, double var3) {
      this.expire(var3, var0 -> true);
      if (!var2) {
         this.pending.remove(var1);
      } else {
         if (this.pending.size() >= 64 && !this.pending.containsKey(var1)) {
            evictOldest(this.pending);
         }

         this.pending.put(var1, var3);
      }
   }

   public boolean consume(Object var1, double var2) {
      this.expire(var2, var0 -> true);
      Double var4 = this.pending.remove(var1);
      if (this.consumed.containsKey(var1)) {
         return false;
      }

      if (this.consumed.size() >= 64) {
         evictOldest(this.consumed);
      }

      this.consumed.put(var1, var2);
      return var4 != null && var2 >= var4 && var2 - var4 <= 3.0;
   }

   public void clearPending(Object var1) {
      this.pending.remove(var1);
   }

   public void expire(double var1, Predicate<Object> var3) {
      this.pending.entrySet().removeIf(var3x -> var1 < var3x.getValue() || var1 - var3x.getValue() > 3.0 || !var3.test(var3x.getKey()));
      this.consumed.entrySet().removeIf(var3x -> var1 < var3x.getValue() || var1 - var3x.getValue() > 12.0 || !var3.test(var3x.getKey()));
   }

   public int pendingCount() {
      return this.pending.size();
   }

   public int consumedCount() {
      return this.consumed.size();
   }

   public void clear() {
      this.pending.clear();
      this.consumed.clear();
   }

   private static void evictOldest(IdentityHashMap<Object, Double> var0) {
      Object var1 = null;
      double var2 = Double.POSITIVE_INFINITY;

      for (Entry var5 : var0.entrySet()) {
         if ((Double)var5.getValue() < var2) {
            var1 = var5.getKey();
            var2 = (Double)var5.getValue();
         }
      }

      var0.remove(var1);
   }
}
