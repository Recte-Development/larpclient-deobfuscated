package com.larpclient;

public final class lo {
   public static jk.x q = jk.x.V;
   private static jk.x P;
   private static jk.x b;

   public static int H(int var0) {
      if (var0 <= 0) {
         return 0;
      }

      double var1 = switch (q) {
         case i, V -> 0.7;
         case L -> 0.45;
         case C -> 0.9;
         case a -> 1.0;
      };
      return Math.max(1, (int)Math.ceil(var0 * var1));
   }

   public static boolean i() {
      return q.ordinal() >= jk.x.V.ordinal();
   }

   public static void s(jk.x var0) {
      if (P != null && q != b) {
         P = q;
      }

      if (var0 == null) {
         if (P != null) {
            q = P;
            b = null;
            P = null;
         }
      } else {
         if (P == null) {
            P = q;
         }

         q = var0.ordinal() < P.ordinal() ? var0 : P;
         b = q;
      }
   }

   public static jk.x m() {
      return P != null && q == b ? P : q;
   }

   private lo() {
   }
}
