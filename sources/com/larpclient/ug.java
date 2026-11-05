package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class ug extends b {
   private final com.larpclient.module.d6 c;

   public ug(com.larpclient.module.d6 var1) {
      this.c = var1;
      this.W = 20;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      int var4 = Math.max(1, this.A / this.c.N().size());

      for (int var5 = 0; var5 < this.c.N().size(); var5++) {
         String var6 = this.c.N().get(var5);
         boolean var7 = this.c.S(var6);
         int var8 = this.q + var5 * var4;
         int var9 = Math.max(1, var4 - 3);
         int var10 = var7 ? n.readableAccent(f.z(var8), f.i | 0xFF000000) : f.S | 0xFF000000;
         int var11 = f.Y() ? 7 : 5;
         if (var7) {
            pn.Z(var1, var8, this.J, var9, this.W, var11, 5.0F, _3.A(var10, 0.22));
         }

         pn.b(var1, var8, this.J, var9, this.W, var11, var10);
         pn.D(var1, var8, this.J, var9, this.W, var11, var7 ? var10 : f.I);
         k6.T(var1, k6.o(var6, var4 - 9, k6.d.K), var8 + 4, this.J + 6, n.controlInk(var10), k6.d.K);
      }

      if (this.l && this.R) {
         pn.y(var1, this.q, this.J, this.A, this.W, f.Y() ? 7 : 5);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.contains(var1, var3)) {
         int var6 = Math.max(1, this.A / this.c.N().size());
         this.c.setValue(this.c.N().get(Math.clamp((int)(var1 - this.q) / var6, 0, this.c.N().size() - 1)));
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

      int var2 = this.c.N().indexOf(this.c.P());
      this.c.setValue(this.c.N().get(Math.floorMod(var2 + (var1 == 263 ? -1 : 1), this.c.N().size())));
      return true;
   }
}
