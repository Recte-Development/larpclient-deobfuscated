package com.larpclient;

import net.minecraft.class_3675.class_307;
import org.lwjgl.glfw.GLFW;

public final class s5 {
   public static final int X = -1;
   private static final int n = 1;
   private static final int w = 8;

   public static boolean X(int var0) {
      return var0 >= 1 && var0 < 9;
   }

   public static boolean D(int var0) {
      return var0 >= 32 && var0 <= 348;
   }

   public static boolean A(int var0) {
      return X(var0) || D(var0);
   }

   public static int K(int var0) {
      return var0 >= 0 && var0 < 8 ? 1 + var0 : -1;
   }

   public static int W(int var0) {
      return X(var0) ? var0 - 1 : -1;
   }

   public static boolean z(int var0) {
      return var0 >= 2 && var0 < 8;
   }

   public static boolean m(long var0, int var2) {
      return X(var2) ? GLFW.glfwGetMouseButton(var0, W(var2)) == 1 : D(var2) && GLFW.glfwGetKey(var0, var2) == 1;
   }

   public static String J(int var0) {
      return switch (W(var0)) {
         case 0 -> "Left Mouse";
         case 1 -> "Right Mouse";
         case 2 -> "Middle Mouse";
         default -> "Mouse " + (W(var0) + 1);
      };
   }

   public static String S(int var0) {
      if (X(var0)) {
         return J(var0);
      } else {
         return !D(var0) ? "Unbound" : class_307.field_1668.method_1447(s2.m(var0)).method_27445().getString();
      }
   }

   private s5() {
   }
}
