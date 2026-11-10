package com.larpclient;

import com.mojang.blaze3d.platform.InputConstants.Type;

public final class p4 {
   public static final int M = -1;
   private static final int K = 1;
   private static final int n = 8;

   public static boolean h(int var0) {
      return var0 >= 1 && var0 < 9;
   }

   public static boolean z(int var0) {
      return var0 >= 32 && var0 <= 348;
   }

   public static boolean g(int var0) {
      return h(var0) || z(var0);
   }

   public static int f(int var0) {
      return var0 >= 0 && var0 < 8 ? 1 + var0 : -1;
   }

   public static int y(int var0) {
      return h(var0) ? var0 - 1 : -1;
   }

   public static boolean P(int var0) {
      return var0 >= 2 && var0 < 8;
   }

   public static boolean Y(long var0, int var2) {
      return h(var2) ? n2.L(var0, y(var2)) == 1 : z(var2) && n2.R(var0, var2) == 1;
   }

   public static String Y(int var0) {
      return switch (y(var0)) {
         case 0 -> "Left Mouse";
         case 1 -> "Right Mouse";
         case 2 -> "Middle Mouse";
         default -> "Mouse " + (y(var0) + 1);
      };
   }

   public static String n(int var0) {
      if (h(var0)) {
         return Y(var0);
      } else {
         return !z(var0) ? "Unbound" : Type.KEYBOARD.getOrCreate(j2.X(var0)).getDisplayName().getString();
      }
   }

   private p4() {
   }
}
