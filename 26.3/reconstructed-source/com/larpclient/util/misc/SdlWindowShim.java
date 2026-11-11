package com.larpclient.util.misc;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.sdl.SDLInit;
import org.lwjgl.sdl.SDLMouse;
import org.lwjgl.sdl.SDLVideo;

public final class SdlWindowShim {
   public static final int rELEASE = 0;
   public static final int pRESS = 1;
   public static final int mOUSE_BUTTON_LEFT = 0;
   public static final int mOUSE_BUTTON_RIGHT = 1;
   public static final int mOUSE_BUTTON_MIDDLE = 2;
   public static final int kEY_UNKNOWN = -1;
   public static final int sDL_INIT_VIDEO = 32;
   public static final int kEY_B = 66;
   public static final int kEY_C = 67;
   public static final int kEY_G = 71;
   public static final int kEY_SLASH = 47;
   public static final int kEY_ESCAPE = 256;
   public static final int kEY_ENTER = 257;
   public static final int kEY_PAGE_UP = 266;
   public static final int kEY_PAGE_DOWN = 267;
   public static final int kEY_KP_ENTER = 335;
   public static final int kEY_LEFT_SHIFT = 340;
   public static final int kEY_LEFT_CONTROL = 341;
   public static final int kEY_RIGHT_SHIFT = 344;
   public static final int kEY_RIGHT_CONTROL = 345;
   public static final int kEY_RIGHT_SUPER = 347;
   public static final int kEY_MENU = 348;
   public static final int fALSE = 0;
   public static final int tRUE = 1;
   public static final int hINT_DEPTH_BITS = 135173;
   public static final int hINT_VISIBLE = 131076;
   public static final int hINT_CONTEXT_VERSION_MAJOR = 139266;
   public static final int hINT_CONTEXT_VERSION_MINOR = 139267;
   public static final int hINT_OPENGL_PROFILE = 139272;
   public static final int oPENGL_CORE_PROFILE = 204801;
   private static final Map<Integer, Integer> windowHints = new HashMap<>();
   private static final Map<Long, Long> glContexts = new HashMap<>();

   public static int getKeyState(long var0, int var2) {
      int var3 = KeyCodes.toScancode(var2);
      return var3 > 0 && InputConstants.isKeyDown(var3) ? 1 : 0;
   }

   public static int getMouseButtonState(long var0, int var2) {
      int var3 = KeyCodes.toMinecraftButton(var2);
      if (var3 >= 1 && var3 <= 32) {
         int var4 = SDLMouse.SDL_GetMouseState(null, null);
         return (var4 & 1 << var3 - 1) != 0 ? 1 : 0;
      } else {
         return 0;
      }
   }

   public static boolean init() {
      return SDLInit.SDL_Init(32);
   }

   public static void setWindowHint(int var0, int var1) {
      windowHints.put(var0, var1);
   }

   public static long createWindow(int var0, int var1, CharSequence var2, long var3, long var5) {
      SDLVideo.SDL_GL_SetAttribute(17, windowHints.getOrDefault(139266, 3));
      SDLVideo.SDL_GL_SetAttribute(18, windowHints.getOrDefault(139267, 3));
      if (windowHints.getOrDefault(139272, 0) == 204801) {
         SDLVideo.SDL_GL_SetAttribute(20, 1);
      }

      if (windowHints.containsKey(135173)) {
         SDLVideo.SDL_GL_SetAttribute(6, windowHints.get(135173));
      }

      long var7 = 2L | (windowHints.getOrDefault(131076, 1) == 0 ? 8L : 0L);
      long var9 = SDLVideo.SDL_CreateWindow(var2, var0, var1, var7);
      if (var9 == 0L) {
         return 0L;
      } else {
         long var11 = SDLVideo.SDL_GL_CreateContext(var9);
         if (var11 == 0L) {
            SDLVideo.SDL_DestroyWindow(var9);
            return 0L;
         } else {
            glContexts.put(var9, var11);
            return var9;
         }
      }
   }

   public static void makeContextCurrent(long var0) {
      SDLVideo.SDL_GL_MakeCurrent(var0, var0 == 0L ? 0L : glContexts.getOrDefault(var0, 0L));
   }

   public static void destroyWindow(long var0) {
      Long var2 = glContexts.remove(var0);
      if (var2 != null) {
         SDLVideo.SDL_GL_DestroyContext(var2);
      }

      if (var0 != 0L) {
         SDLVideo.SDL_DestroyWindow(var0);
      }
   }

   public static void createWindow() {
      windowHints.clear();
      SDLInit.SDL_QuitSubSystem(32);
   }

   public static void focusWindow(long var0) {
      SDLVideo.SDL_RaiseWindow(var0);
   }

   private SdlWindowShim() {
   }
}
