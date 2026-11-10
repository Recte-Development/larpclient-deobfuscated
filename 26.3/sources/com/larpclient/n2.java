package com.larpclient;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.sdl.SDLInit;
import org.lwjgl.sdl.SDLMouse;
import org.lwjgl.sdl.SDLVideo;

public final class n2 {
   public static final int t = 0;
   public static final int q = 1;
   public static final int L = 0;
   public static final int G = 1;
   public static final int s = 2;
   public static final int v = -1;
   public static final int R = 32;
   public static final int g = 66;
   public static final int M = 67;
   public static final int E = 71;
   public static final int w = 47;
   public static final int i = 256;
   public static final int U = 257;
   public static final int k = 266;
   public static final int c = 267;
   public static final int n = 335;
   public static final int r = 340;
   public static final int S = 341;
   public static final int P = 344;
   public static final int o = 345;
   public static final int A = 347;
   public static final int e = 348;
   public static final int T = 0;
   public static final int z = 1;
   public static final int W = 135173;
   public static final int V = 131076;
   public static final int a = 139266;
   public static final int Q = 139267;
   public static final int f = 139272;
   public static final int D = 204801;
   private static final Map<Integer, Integer> Z = new HashMap<>();
   private static final Map<Long, Long> h = new HashMap<>();

   public static int R(long var0, int var2) {
      int var3 = j2.X(var2);
      return var3 > 0 && InputConstants.isKeyDown(var3) ? 1 : 0;
   }

   public static int L(long var0, int var2) {
      int var3 = j2.K(var2);
      if (var3 >= 1 && var3 <= 32) {
         int var4 = SDLMouse.SDL_GetMouseState(null, null);
         return (var4 & 1 << var3 - 1) != 0 ? 1 : 0;
      } else {
         return 0;
      }
   }

   public static boolean C() {
      return SDLInit.SDL_Init(32);
   }

   public static void t(int var0, int var1) {
      Z.put(var0, var1);
   }

   public static long B(int var0, int var1, CharSequence var2, long var3, long var5) {
      SDLVideo.SDL_GL_SetAttribute(17, Z.getOrDefault(139266, 3));
      SDLVideo.SDL_GL_SetAttribute(18, Z.getOrDefault(139267, 3));
      if (Z.getOrDefault(139272, 0) == 204801) {
         SDLVideo.SDL_GL_SetAttribute(20, 1);
      }

      if (Z.containsKey(135173)) {
         SDLVideo.SDL_GL_SetAttribute(6, Z.get(135173));
      }

      long var7 = 2L | (Z.getOrDefault(131076, 1) == 0 ? 8L : 0L);
      long var9 = SDLVideo.SDL_CreateWindow(var2, var0, var1, var7);
      if (var9 == 0L) {
         return 0L;
      } else {
         long var11 = SDLVideo.SDL_GL_CreateContext(var9);
         if (var11 == 0L) {
            SDLVideo.SDL_DestroyWindow(var9);
            return 0L;
         } else {
            h.put(var9, var11);
            return var9;
         }
      }
   }

   public static void U(long var0) {
      SDLVideo.SDL_GL_MakeCurrent(var0, var0 == 0L ? 0L : h.getOrDefault(var0, 0L));
   }

   public static void d(long var0) {
      Long var2 = h.remove(var0);
      if (var2 != null) {
         SDLVideo.SDL_GL_DestroyContext(var2);
      }

      if (var0 != 0L) {
         SDLVideo.SDL_DestroyWindow(var0);
      }
   }

   public static void B() {
      Z.clear();
      SDLInit.SDL_QuitSubSystem(32);
   }

   public static void p(long var0) {
      SDLVideo.SDL_RaiseWindow(var0);
   }

   private n2() {
   }
}
