package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class uq extends b {
   private final String Y;
   private final String k;

   public uq(String var1, String var2) {
      this.Y = var1;
      this.k = var2;
      this.X = (boolean)0;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (f.Y()) {
         pn.d(var1, this.q, this.J, this.A, this.W, f.D, f.i, 0.6);
      } else {
         pn.g(var1, this.q, this.J, this.A, this.W, 6, f.i, 0.3);
      }

      k6.T(var1, k6.o(this.Y, this.A - 20, k6.d.a), this.q + 10, this.J + 8, f.J, k6.d.a);
      k6.T(var1, k6.o(this.k, this.A - 20, k6.d.K), this.q + 10, this.J + 21, f.s, k6.d.K);
   }
}
