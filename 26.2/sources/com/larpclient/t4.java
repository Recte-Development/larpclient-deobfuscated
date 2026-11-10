package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class t4 {
   private static final List<t4.u> b = new ArrayList<>();

   private t4() {
   }

   public static long B() {
      return 250L + ThreadLocalRandom.current().nextLong(500L);
   }

   public static void C(long var0, Runnable var2) {
      b.add(new t4.u(System.currentTimeMillis() + Math.max(0L, var0), var2));
   }

   public static void c() {
      b.clear();
   }

   public static void T() {
      if (!b.isEmpty()) {
         long var0 = System.currentTimeMillis();
         ArrayList var2 = new ArrayList();
         b.removeIf(var3 -> {
            if (var3.at <= var0) {
               var2.add(var3);
               return true;
            } else {
               return false;
            }
         });

         for (t4.u var4 : var2) {
            var4.action.run();
         }
      }
   }

   private record u(long at, Runnable action) {
   }
}
