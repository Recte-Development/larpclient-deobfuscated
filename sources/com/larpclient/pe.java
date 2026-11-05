package com.larpclient;

import com.mojang.blaze3d.platform.InputConstants.Type;

public final class pe {
   public static final int s = -1;
   private static final int J = 1;
   private static final int z = 8;

   public static boolean o(int var0) {
      return var0 >= 1 && var0 < 9;
   }

   public static boolean E(int var0) {
      return var0 >= 32 && var0 <= 348;
   }

   public static boolean F(int var0) {
      return o(var0) || E(var0);
   }

   public static int h(int var0) {
      return var0 >= 0 && var0 < 8 ? 1 + var0 : -1;
   }

   public static int H(int var0) {
      return o(var0) ? var0 - 1 : -1;
   }

   public static boolean Y(int var0) {
      return var0 >= 2 && var0 < 8;
   }

   public static boolean A(long var0, int var2) {
      return o(var2) ? eo.Y(var0, H(var2)) == 1 : E(var2) && eo.F(var0, var2) == 1;
   }

   public static String B(int var0) {
      return switch (H(var0)) {
         case 0 -> "Left Mouse";
         case 1 -> "Right Mouse";
         case 2 -> "Middle Mouse";
         default -> "Mouse " + (H(var0) + 1);
      };
   }

   public static String A(int var0) {
      if (o(var0)) {
         return B(var0);
      } else {
         return !E(var0) ? "Unbound" : Type.KEYBOARD.getOrCreate(r7.e(var0)).getDisplayName().getString();
      }
   }

   private pe() {
   }
}
