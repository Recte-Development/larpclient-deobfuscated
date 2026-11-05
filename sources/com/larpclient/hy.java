package com.larpclient;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.sdl.SDLInit;
import org.lwjgl.sdl.SDLMouse;
import org.lwjgl.sdl.SDLVideo;

public final class hy {
   public static final int V = 0;
   public static final int z = 1;
   public static final int N = 0;
   public static final int y = 1;
   public static final int m = 2;
   public static final int C = -1;
   public static final int T = 32;
   public static final int j = 66;
   public static final int A = 67;
   public static final int E = 71;
   public static final int k = 256;
   public static final int l = 257;
   public static final int I = 266;
   public static final int i = 267;
   public static final int Q = 335;
   public static final int q = 341;
   public static final int S = 344;
   public static final int r = 345;
   public static final int H = 348;
   public static final int a = 0;
   public static final int e = 1;
   public static final int K = 135173;
   public static final int O = 131076;
   public static final int c = 139266;
   public static final int R = 139267;
   public static final int o = 139272;
   public static final int D = 204801;
   private static final Map<Integer, Integer> L = new HashMap<>();
   private static final Map<Long, Long> b = new HashMap<>();

   public static int z(long var0, int var2) {
      int var3 = mo.f(var2);
      return var3 > 0 && InputConstants.isKeyDown(var3) ? 1 : 0;
   }

   public static int Y(long var0, int var2) {
      int var3 = mo.A(var2);
      if (var3 >= 1 && var3 <= 32) {
         int var4 = SDLMouse.SDL_GetMouseState(null, null);
         return (var4 & 1 << var3 - 1) != 0 ? 1 : 0;
      } else {
         return 0;
      }
   }

   public static boolean k() {
      return SDLInit.SDL_Init(32);
   }

   public static void X(int var0, int var1) {
      L.put(var0, var1);
   }

   public static long r(int var0, int var1, CharSequence var2, long var3, long var5) {
      SDLVideo.SDL_GL_SetAttribute(17, L.getOrDefault(139266, 3));
      SDLVideo.SDL_GL_SetAttribute(18, L.getOrDefault(139267, 3));
      if (L.getOrDefault(139272, 0) == 204801) {
         SDLVideo.SDL_GL_SetAttribute(20, 1);
      }

      if (L.containsKey(135173)) {
         SDLVideo.SDL_GL_SetAttribute(6, L.get(135173));
      }

      long var7 = 2L | (L.getOrDefault(131076, 1) == 0 ? 8L : 0L);
      long var9 = SDLVideo.SDL_CreateWindow(var2, var0, var1, var7);
      if (var9 == 0L) {
         return 0L;
      } else {
         long var11 = SDLVideo.SDL_GL_CreateContext(var9);
         if (var11 == 0L) {
            SDLVideo.SDL_DestroyWindow(var9);
            return 0L;
         } else {
            b.put(var9, var11);
            return var9;
         }
      }
   }

   public static void Q(long var0) {
      SDLVideo.SDL_GL_MakeCurrent(var0, var0 == 0L ? 0L : b.getOrDefault(var0, 0L));
   }

   public static void w(long var0) {
      Long var2 = b.remove(var0);
      if (var2 != null) {
         SDLVideo.SDL_GL_DestroyContext(var2);
      }

      if (var0 != 0L) {
         SDLVideo.SDL_DestroyWindow(var0);
      }
   }

   public static void E() {
      L.clear();
      SDLInit.SDL_QuitSubSystem(32);
   }

   public static void e(long var0) {
      SDLVideo.SDL_RaiseWindow(var0);
   }

   private hy() {
   }
}
