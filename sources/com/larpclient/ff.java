package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class ff {
   private static final List<ff.e> v = new ArrayList<>();

   private ff() {
   }

   public static long J() {
      return 250L + ThreadLocalRandom.current().nextLong(500L);
   }

   public static void r(long var0, Runnable var2) {
      v.add(new ff.e(System.currentTimeMillis() + Math.max(0L, var0), var2));
   }

   public static void C() {
      v.clear();
   }

   public static void y() {
      if (!v.isEmpty()) {
         long var0 = System.currentTimeMillis();
         ArrayList var2 = new ArrayList();
         v.removeIf(var3 -> {
            if (var3.at <= var0) {
               var2.add(var3);
               return true;
            } else {
               return false;
            }
         });

         for (ff.e var4 : var2) {
            var4.action.run();
         }
      }
   }

   private record e(long at, Runnable action) {
   }
}
