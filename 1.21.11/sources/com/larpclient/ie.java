package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class ie {
   private static final List<ie.t> p = new ArrayList<>();

   private ie() {
   }

   public static long f() {
      return 250L + ThreadLocalRandom.current().nextLong(500L);
   }

   public static void w(long var0, Runnable var2) {
      p.add(new ie.t(System.currentTimeMillis() + Math.max(0L, var0), var2));
   }

   public static void N() {
      p.clear();
   }

   public static void R() {
      if (!p.isEmpty()) {
         long var0 = System.currentTimeMillis();
         ArrayList var2 = new ArrayList();
         p.removeIf(var3 -> {
            if (var3.at <= var0) {
               var2.add(var3);
               return true;
            } else {
               return false;
            }
         });

         for (ie.t var4 : var2) {
            var4.action.run();
         }
      }
   }

   private record t(long at, Runnable action) {
   }
}
