package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class qw {
   private static final List<qw.k> n = new ArrayList<>();

   private qw() {
   }

   public static long a() {
      return 250L + ThreadLocalRandom.current().nextLong(500L);
   }

   public static void K(long var0, Runnable var2) {
      n.add(new qw.k(System.currentTimeMillis() + Math.max(0L, var0), var2));
   }

   public static void M() {
      n.clear();
   }

   public static void A() {
      if (!n.isEmpty()) {
         long var0 = System.currentTimeMillis();
         ArrayList var2 = new ArrayList();
         n.removeIf(var3 -> {
            if (var3.at <= var0) {
               var2.add(var3);
               return true;
            } else {
               return false;
            }
         });

         for (qw.k var4 : var2) {
            var4.action.run();
         }
      }
   }

   private record k(long at, Runnable action) {
   }
}
