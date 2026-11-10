package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class jm extends jd {
   private final com.larpclient.module.l d;

   public jm(com.larpclient.module.l var1) {
      this.d = var1;
      this.J = 20;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      int var4 = Math.max(1, this.A / this.d.a().size());

      for (int var5 = 0; var5 < this.d.a().size(); var5++) {
         String var6 = this.d.a().get(var5);
         boolean var7 = this.d.O(var6);
         int var8 = this.S + var5 * var4;
         int var9 = Math.max(1, var4 - 3);
         int var10 = var7 ? lv.readableAccent(ho.Q(var8), ho.f | 0xFF000000) : ho.B | 0xFF000000;
         int var11 = ho.S() ? 7 : 5;
         if (var7) {
            wp.e(var1, var8, this.u, var9, this.J, var11, 5.0F, uh.H(var10, 0.22));
         }

         wp.I(var1, var8, this.u, var9, this.J, var11, var10);
         wp.q(var1, var8, this.u, var9, this.J, var11, var7 ? var10 : ho.c);
         bj.k(var1, bj.i(var6, var4 - 9, bj.u.M), var8 + 4, this.u + 6, lv.controlInk(var10), bj.u.M);
      }

      if (this.T && this.W) {
         wp.t(var1, this.S, this.u, this.A, this.J, ho.S() ? 7 : 5);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.contains(var1, var3)) {
         int var6 = Math.max(1, this.A / this.d.a().size());
         this.d.setValue(this.d.a().get(Math.clamp((int)(var1 - this.S) / var6, 0, this.d.a().size() - 1)));
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

      int var2 = this.d.a().indexOf(this.d.getValue());
      this.d.setValue(this.d.a().get(Math.floorMod(var2 + (var1 == 263 ? -1 : 1), this.d.a().size())));
      return true;
   }
}
