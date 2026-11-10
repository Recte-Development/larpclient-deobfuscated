package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class jj extends jd {
   private final String y;
   private final String G;

   public jj(String var1, String var2) {
      this.y = var1;
      this.G = var2;
      this.p = (boolean)0;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (ho.S()) {
         wp.S(var1, this.S, this.u, this.A, this.J, ho.dr, ho.f, 0.6);
      } else {
         wp.Z(var1, this.S, this.u, this.A, this.J, 6, ho.f, 0.3);
      }

      bj.k(var1, bj.i(this.y, this.A - 20, bj.u.i), this.S + 10, this.u + 8, ho.u, bj.u.i);
      bj.k(var1, bj.i(this.G, this.A - 20, bj.u.M), this.S + 10, this.u + 21, ho.Z, bj.u.M);
   }
}
