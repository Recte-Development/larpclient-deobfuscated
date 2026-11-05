package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class py extends oq {
   private final String y;
   private final String m;

   public py(String var1, String var2) {
      this.y = var1;
      this.m = var2;
      this.x = (boolean)0;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (rv.u()) {
         ll.w(var1, this.B, this.L, this.v, this.O, rv.S, rv.H, 0.6);
      } else {
         ll.Y(var1, this.B, this.L, this.v, this.O, 6, rv.H, 0.3);
      }

      j_.L(var1, j_.d(this.y, this.v - 20, j_.m.d), this.B + 10, this.L + 8, rv.P, j_.m.d);
      j_.L(var1, j_.d(this.m, this.v - 20, j_.m.U), this.B + 10, this.L + 21, rv.G0, j_.m.U);
   }
}
