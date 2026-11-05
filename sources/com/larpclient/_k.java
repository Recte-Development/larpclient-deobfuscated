package com.larpclient;

public final class _k {
   public static m5.n t = m5.n.h;
   private static m5.n s;
   private static m5.n I;

   public static int m(int var0) {
      if (var0 <= 0) {
         return 0;
      }

      double var1 = switch (t) {
         case N, h -> 0.7;
         case T -> 0.45;
         case B -> 0.9;
         case O -> 1.0;
      };
      return Math.max(1, (int)Math.ceil(var0 * var1));
   }

   public static boolean C() {
      return t.ordinal() >= m5.n.h.ordinal();
   }

   public static void C(m5.n var0) {
      if (s != null && t != I) {
         s = t;
      }

      if (var0 == null) {
         if (s != null) {
            t = s;
            I = null;
            s = null;
         }
      } else {
         if (s == null) {
            s = t;
         }

         t = var0.ordinal() < s.ordinal() ? var0 : s;
         I = t;
      }
   }

   public static m5.n b() {
      return s != null && t == I ? s : t;
   }

   private _k() {
   }
}
