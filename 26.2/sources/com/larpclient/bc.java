package com.larpclient;

import com.mojang.blaze3d.platform.InputConstants.Type;
import org.lwjgl.glfw.GLFW;

public final class bc {
   public static final int j = -1;
   private static final int A = 1;
   private static final int u = 8;

   public static boolean Q(int var0) {
      return var0 >= 1 && var0 < 9;
   }

   public static boolean I(int var0) {
      return var0 >= 32 && var0 <= 348;
   }

   public static boolean a(int var0) {
      return Q(var0) || I(var0);
   }

   public static int Z(int var0) {
      return var0 >= 0 && var0 < 8 ? 1 + var0 : -1;
   }

   public static int a(int var0) {
      return Q(var0) ? var0 - 1 : -1;
   }

   public static boolean j(int var0) {
      return var0 >= 2 && var0 < 8;
   }

   public static boolean I(long var0, int var2) {
      return Q(var2) ? GLFW.glfwGetMouseButton(var0, a(var2)) == 1 : I(var2) && GLFW.glfwGetKey(var0, var2) == 1;
   }

   public static String E(int var0) {
      return switch (a(var0)) {
         case 0 -> "Left Mouse";
         case 1 -> "Right Mouse";
         case 2 -> "Middle Mouse";
         default -> "Mouse " + (a(var0) + 1);
      };
   }

   public static String G(int var0) {
      if (Q(var0)) {
         return E(var0);
      } else {
         return !I(var0) ? "Unbound" : Type.KEYSYM.getOrCreate(k2.M(var0)).getDisplayName().getString();
      }
   }

   private bc() {
   }
}
