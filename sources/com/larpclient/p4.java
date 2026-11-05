package com.larpclient;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class p4 extends oq {
   private final com.larpclient.module.lp X;

   public p4(com.larpclient.module.lp var1) {
      this.X = var1;
      this.O = 20;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      int var4 = Math.max(1, this.v / this.X.X().size());

      for (int var5 = 0; var5 < this.X.X().size(); var5++) {
         String var6 = this.X.X().get(var5);
         boolean var7 = this.X.i(var6);
         int var8 = this.B + var5 * var4;
         int var9 = Math.max(1, var4 - 3);
         int var10 = var7 ? jd.readableAccent(rv.m(var8), rv.H | 0xFF000000) : rv.z | 0xFF000000;
         int var11 = rv.u() ? 7 : 5;
         if (var7) {
            ll.S(var1, var8, this.L, var9, this.O, var11, 5.0F, oh.i(var10, 0.22));
         }

         ll.N(var1, var8, this.L, var9, this.O, var11, var10);
         ll.u(var1, var8, this.L, var9, this.O, var11, var7 ? var10 : rv.f);
         j_.L(var1, j_.d(var6, var4 - 9, j_.m.U), var8 + 4, this.L + 6, jd.controlInk(var10), j_.m.U);
      }

      if (this.A && this.h) {
         ll.w(var1, this.B, this.L, this.v, this.O, rv.u() ? 7 : 5);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.contains(var1, var3)) {
         int var6 = Math.max(1, this.v / this.X.X().size());
         this.X.setValue(this.X.X().get(Math.clamp((int)(var1 - this.B) / var6, 0, this.X.X().size() - 1)));
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

      int var2 = this.X.X().indexOf(this.X.getValue());
      this.X.setValue(this.X.X().get(Math.floorMod(var2 + (var1 == 263 ? -1 : 1), this.X.X().size())));
      return true;
   }
}
