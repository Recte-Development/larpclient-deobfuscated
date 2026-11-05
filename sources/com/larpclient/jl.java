package com.larpclient;

import org.lwjgl.sdl.SDLProperties;
import org.lwjgl.sdl.SDLVideo;

public final class jl {
   public static long m(long var0) {
      int var2 = SDLVideo.SDL_GetWindowProperties(var0);
      return var2 == 0 ? 0L : SDLProperties.SDL_GetPointerProperty(var2, "SDL.window.win32.hwnd", 0L);
   }

   private jl() {
   }
}
