package com.larpclient;

public final class a4 {
   public static nl.q h = nl.q.C;
   private static nl.q j;
   private static nl.q q;

   public static int N(int var0) {
      if (var0 <= 0) {
         return 0;
      }

      double var1 = switch (h) {
         case q, C -> 0.7;
         case l -> 0.45;
         case n -> 0.9;
         case V -> 1.0;
      };
      return Math.max(1, (int)Math.ceil(var0 * var1));
   }

   public static boolean K() {
      return h.ordinal() >= nl.q.C.ordinal();
   }

   public static void M(nl.q var0) {
      if (j != null && h != q) {
         j = h;
      }

      if (var0 == null) {
         if (j != null) {
            h = j;
            q = null;
            j = null;
         }
      } else {
         if (j == null) {
            j = h;
         }

         h = var0.ordinal() < j.ordinal() ? var0 : j;
         q = h;
      }
   }

   public static nl.q c() {
      return j != null && h == q ? j : h;
   }

   private a4() {
   }
}
