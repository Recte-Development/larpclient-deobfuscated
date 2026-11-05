package com.larpclient;

import com.mojang.blaze3d.platform.InputConstants.Type;

public final class cc {
   public static final int f = -1;
   private static final int b = 1;
   private static final int q = 8;

   public static boolean h(int var0) {
      return var0 >= 1 && var0 < 9;
   }

   public static boolean x(int var0) {
      return var0 >= 32 && var0 <= 348;
   }

   public static boolean N(int var0) {
      return h(var0) || x(var0);
   }

   public static int M(int var0) {
      return var0 >= 0 && var0 < 8 ? 1 + var0 : -1;
   }

   public static int T(int var0) {
      return h(var0) ? var0 - 1 : -1;
   }

   public static boolean P(int var0) {
      return var0 >= 2 && var0 < 8;
   }

   public static boolean N(long var0, int var2) {
      return h(var2) ? hy.Y(var0, T(var2)) == 1 : x(var2) && hy.z(var0, var2) == 1;
   }

   public static String U(int var0) {
      return switch (T(var0)) {
         case 0 -> "Left Mouse";
         case 1 -> "Right Mouse";
         case 2 -> "Middle Mouse";
         default -> "Mouse " + (T(var0) + 1);
      };
   }

   public static String u(int var0) {
      if (h(var0)) {
         return U(var0);
      } else {
         return !x(var0) ? "Unbound" : Type.KEYBOARD.getOrCreate(mo.f(var0)).getDisplayName().getString();
      }
   }

   private cc() {
   }
}
