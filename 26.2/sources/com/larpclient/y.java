package com.larpclient;

public final class y {
   public static wa.a m = wa.a.Y;
   private static wa.a v;
   private static wa.a V;

   public static int i(int var0) {
      if (var0 <= 0) {
         return 0;
      }

      double var1 = switch (m) {
         case Z, Y -> 0.7;
         case P -> 0.45;
         case f -> 0.9;
         case E -> 1.0;
      };
      return Math.max(1, (int)Math.ceil(var0 * var1));
   }

   public static boolean y() {
      return m.ordinal() >= wa.a.Y.ordinal();
   }

   public static void h(wa.a var0) {
      if (v != null && m != V) {
         v = m;
      }

      if (var0 == null) {
         if (v != null) {
            m = v;
            V = null;
            v = null;
         }
      } else {
         if (v == null) {
            v = m;
         }

         m = var0.ordinal() < v.ordinal() ? var0 : v;
         V = m;
      }
   }

   public static wa.a P() {
      return v != null && m == V ? v : m;
   }

   private y() {
   }
}
