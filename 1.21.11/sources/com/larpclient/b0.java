package com.larpclient;

public final class b0 {
   public static t_.t r = t_.t.Q;
   private static t_.t M;
   private static t_.t s;

   public static int Y(int var0) {
      if (var0 <= 0) {
         return 0;
      }

      double var1 = switch (r) {
         case A, Q -> 0.7;
         case Y -> 0.45;
         case S -> 0.9;
         case J -> 1.0;
      };
      return Math.max(1, (int)Math.ceil(var0 * var1));
   }

   public static boolean E() {
      return r.ordinal() >= t_.t.Q.ordinal();
   }

   public static void C(t_.t var0) {
      if (M != null && r != s) {
         M = r;
      }

      if (var0 == null) {
         if (M != null) {
            r = M;
            s = null;
            M = null;
         }
      } else {
         if (M == null) {
            M = r;
         }

         r = var0.ordinal() < M.ordinal() ? var0 : M;
         s = r;
      }
   }

   public static t_.t M() {
      return M != null && r == s ? M : r;
   }

   private b0() {
   }
}
