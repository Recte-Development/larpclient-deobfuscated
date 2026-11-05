package com.larpclient;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.sdl.SDLInit;
import org.lwjgl.sdl.SDLMouse;
import org.lwjgl.sdl.SDLVideo;

public final class eo {
   public static final int J = 0;
   public static final int S = 1;
   public static final int i = 0;
   public static final int M = 1;
   public static final int e = 2;
   public static final int K = -1;
   public static final int Y = 32;
   public static final int E = 66;
   public static final int o = 67;
   public static final int W = 71;
   public static final int z = 256;
   public static final int t = 257;
   public static final int g = 266;
   public static final int b = 267;
   public static final int l = 335;
   public static final int I = 341;
   public static final int D = 344;
   public static final int x = 345;
   public static final int R = 348;
   public static final int c = 0;
   public static final int O = 1;
   public static final int w = 135173;
   public static final int B = 131076;
   public static final int G = 139266;
   public static final int X = 139267;
   public static final int k = 139272;
   public static final int r = 204801;
   private static final Map<Integer, Integer> n = new HashMap<>();
   private static final Map<Long, Long> s = new HashMap<>();

   public static int F(long var0, int var2) {
      int var3 = r7.e(var2);
      return var3 > 0 && InputConstants.isKeyDown(var3) ? 1 : 0;
   }

   public static int Y(long var0, int var2) {
      int var3 = r7.y(var2);
      if (var3 >= 1 && var3 <= 32) {
         int var4 = SDLMouse.SDL_GetMouseState(null, null);
         return (var4 & 1 << var3 - 1) != 0 ? 1 : 0;
      } else {
         return 0;
      }
   }

   public static boolean f() {
      return SDLInit.SDL_Init(32);
   }

   public static void E(int var0, int var1) {
      n.put(var0, var1);
   }

   public static long o(int var0, int var1, CharSequence var2, long var3, long var5) {
      SDLVideo.SDL_GL_SetAttribute(17, n.getOrDefault(139266, 3));
      SDLVideo.SDL_GL_SetAttribute(18, n.getOrDefault(139267, 3));
      if (n.getOrDefault(139272, 0) == 204801) {
         SDLVideo.SDL_GL_SetAttribute(20, 1);
      }

      if (n.containsKey(135173)) {
         SDLVideo.SDL_GL_SetAttribute(6, n.get(135173));
      }

      long var7 = 2L | (n.getOrDefault(131076, 1) == 0 ? 8L : 0L);
      long var9 = SDLVideo.SDL_CreateWindow(var2, var0, var1, var7);
      if (var9 == 0L) {
         return 0L;
      } else {
         long var11 = SDLVideo.SDL_GL_CreateContext(var9);
         if (var11 == 0L) {
            SDLVideo.SDL_DestroyWindow(var9);
            return 0L;
         } else {
            s.put(var9, var11);
            return var9;
         }
      }
   }

   public static void G(long var0) {
      SDLVideo.SDL_GL_MakeCurrent(var0, var0 == 0L ? 0L : s.getOrDefault(var0, 0L));
   }

   public static void i(long var0) {
      Long var2 = s.remove(var0);
      if (var2 != null) {
         SDLVideo.SDL_GL_DestroyContext(var2);
      }

      if (var0 != 0L) {
         SDLVideo.SDL_DestroyWindow(var0);
      }
   }

   public static void z() {
      n.clear();
      SDLInit.SDL_QuitSubSystem(32);
   }

   public static void L(long var0) {
      SDLVideo.SDL_RaiseWindow(var0);
   }

   private eo() {
   }
}
