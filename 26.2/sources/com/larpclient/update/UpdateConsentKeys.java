package com.larpclient.update;

import java.util.HashSet;
import java.util.Set;
import java.util.function.IntPredicate;

public final class UpdateConsentKeys {
   private static final int[] SELECTION = new int[]{32, 257, 335};
   private final Set<Integer> held = new HashSet<>();

   public void begin(IntPredicate var1) {
      this.held.clear();

      for (int var5 : SELECTION) {
         if (var1.test(var5)) {
            this.held.add(var5);
         }
      }
   }

   public boolean press(int var1) {
      for (int var5 : SELECTION) {
         if (var1 == var5) {
            return this.held.add(var1);
         }
      }

      return true;
   }

   void release(int var1) {
      this.held.remove(var1);
   }

   public void reconcile(IntPredicate var1) {
      this.held.removeIf(var1x -> !var1.test(var1x));
   }
}
