package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class sp extends j0 {
   private final com.larpclient.module.o c;

   public sp(com.larpclient.module.o var1) {
      this.c = var1;
      this.O = 20;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      int var4 = Math.max(1, this.L / this.c.j().size());

      for (int var5 = 0; var5 < this.c.j().size(); var5++) {
         String var6 = this.c.j().get(var5);
         boolean var7 = this.c.w(var6);
         int var8 = this.P + var5 * var4;
         int var9 = Math.max(1, var4 - 3);
         int var10 = var7 ? w4.readableAccent(com.larpclient.c.c(var8), com.larpclient.c.m | 0xFF000000) : com.larpclient.c.R | 0xFF000000;
         int var11 = com.larpclient.c.J() ? 7 : 5;
         if (var7) {
            wc.b(var1, var8, this.T, var9, this.O, var11, 5.0F, vv.N(var10, 0.22));
         }

         wc.b(var1, var8, this.T, var9, this.O, var11, var10);
         wc.h(var1, var8, this.T, var9, this.O, var11, var7 ? var10 : com.larpclient.c.n);
         ic.N(var1, ic.o(var6, var4 - 9, ic.n.I), var8 + 4, this.T + 6, w4.controlInk(var10), ic.n.I);
      }

      if (this.F && this.X) {
         wc.U(var1, this.P, this.T, this.L, this.O, com.larpclient.c.J() ? 7 : 5);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.contains(var1, var3)) {
         int var6 = Math.max(1, this.L / this.c.j().size());
         this.c.setValue(this.c.j().get(Math.clamp((int)(var1 - this.P) / var6, 0, this.c.j().size() - 1)));
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (var1 != 263 && var1 != 262 && var1 != 32 && var1 != 257) {
         return false;
      }

      int var2 = this.c.j().indexOf(this.c.getValue());
      this.c.setValue(this.c.j().get(Math.floorMod(var2 + (var1 == 263 ? -1 : 1), this.c.j().size())));
      return true;
   }
}
