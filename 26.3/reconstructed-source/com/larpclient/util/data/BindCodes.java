package com.larpclient.util.data;

import com.larpclient.util.misc.KeyCodes;
import com.larpclient.util.misc.SdlWindowShim;
import com.mojang.blaze3d.platform.InputConstants.Type;

public final class BindCodes {
   public static final int uNBOUND = -1;
   private static final int mOUSE_OFFSET = 1;
   private static final int mOUSE_BUTTON_COUNT = 8;

   public static boolean isMouseBind(int var0) {
      return var0 >= 1 && var0 < 9;
   }

   public static boolean isKeyboardBind(int var0) {
      return var0 >= 32 && var0 <= 348;
   }

   public static boolean isValidBind(int var0) {
      return isMouseBind(var0) || isKeyboardBind(var0);
   }

   public static int toMouseBind(int var0) {
      return var0 >= 0 && var0 < 8 ? 1 + var0 : -1;
   }

   public static int toMouseButton(int var0) {
      return isMouseBind(var0) ? var0 - 1 : -1;
   }

   public static boolean isExtraMouseButton(int var0) {
      return var0 >= 2 && var0 < 8;
   }

   public static boolean isPressed(long var0, int var2) {
      return isMouseBind(var2)
         ? SdlWindowShim.getMouseButtonState(var0, toMouseButton(var2)) == 1
         : isKeyboardBind(var2) && SdlWindowShim.getKeyState(var0, var2) == 1;
   }

   public static String isPressed(int var0) {
      return switch (toMouseButton(var0)) {
         case 0 -> "Left Mouse";
         case 1 -> "Right Mouse";
         case 2 -> "Middle Mouse";
         default -> "Mouse " + (toMouseButton(var0) + 1);
      };
   }

   public static String displayName(int var0) {
      if (isMouseBind(var0)) {
         return isPressed(var0);
      } else {
         return !isKeyboardBind(var0) ? "Unbound" : Type.KEYBOARD.getOrCreate(KeyCodes.toScancode(var0)).getDisplayName().getString();
      }
   }

   private BindCodes() {
   }
}
