package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class t1 {
   private static final List<t1.r> r = new ArrayList<>();

   private t1() {
   }

   public static long E() {
      return 250L + ThreadLocalRandom.current().nextLong(500L);
   }

   public static void O(long var0, Runnable var2) {
      r.add(new t1.r(System.currentTimeMillis() + Math.max(0L, var0), var2));
   }

   public static void n() {
      r.clear();
   }

   public static void z() {
      if (!r.isEmpty()) {
         long var0 = System.currentTimeMillis();
         ArrayList var2 = new ArrayList();
         r.removeIf(var3 -> {
            if (var3.at <= var0) {
               var2.add(var3);
               return true;
            } else {
               return false;
            }
         });

         for (t1.r var4 : var2) {
            var4.action.run();
         }
      }
   }

   private record r(long at, Runnable action) {
   }
}
