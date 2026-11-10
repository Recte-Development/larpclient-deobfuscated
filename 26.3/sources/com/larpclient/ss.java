package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class ss extends j0 {
   private final String Z;
   private final String u;

   public ss(String var1, String var2) {
      this.Z = var1;
      this.u = var2;
      this.V = (boolean)0;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (c.J()) {
         wc.g(var1, this.P, this.T, this.L, this.O, c.k, c.m, 0.6);
      } else {
         wc.t(var1, this.P, this.T, this.L, this.O, 6, c.m, 0.3);
      }

      ic.N(var1, ic.o(this.Z, this.L - 20, ic.n.Q), this.P + 10, this.T + 8, c.H, ic.n.Q);
      ic.N(var1, ic.o(this.u, this.L - 20, ic.n.I), this.P + 10, this.T + 21, c.W, ic.n.I);
   }
}
