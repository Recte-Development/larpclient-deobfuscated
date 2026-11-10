package com.larpclient;

import net.minecraft.class_332;

public final class l_ extends lc {
   private final String e;
   private final String G;

   public l_(String var1, String var2) {
      this.e = var1;
      this.G = var2;
      this.N = (boolean)0;
   }

   @Override
   public void render(class_332 var1, int var2, int var3) {
      if (ez.d()) {
         p.N(var1, this.D, this.g, this.d, this.w, ez.U, ez.M, 0.6);
      } else {
         p.W(var1, this.D, this.g, this.d, this.w, 6, ez.M, 0.3);
      }

      uw.X(var1, uw.X(this.e, this.d - 20, uw.c.e), this.D + 10, this.g + 8, ez.H, uw.c.e);
      uw.X(var1, uw.X(this.G, this.d - 20, uw.c.M), this.D + 10, this.g + 21, ez.PH, uw.c.M);
   }
}
