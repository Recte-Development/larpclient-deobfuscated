package com.larpclient;

import net.minecraft.class_332;

public final class lt extends lc {
   private final com.larpclient.module.li J;

   public lt(com.larpclient.module.li var1) {
      this.J = var1;
      this.w = 20;
   }

   @Override
   public void render(class_332 var1, int var2, int var3) {
      int var4 = Math.max(1, this.d / this.J.e().size());

      for (int var5 = 0; var5 < this.J.e().size(); var5++) {
         String var6 = this.J.e().get(var5);
         boolean var7 = this.J.G(var6);
         int var8 = this.D + var5 * var4;
         int var9 = Math.max(1, var4 - 3);
         int var10 = var7 ? jx.readableAccent(ez.P(var8), ez.M | 0xFF000000) : ez.B | 0xFF000000;
         int var11 = ez.d() ? 7 : 5;
         if (var7) {
            p.a(var1, var8, this.g, var9, this.w, var11, 5.0F, ei.D(var10, 0.22));
         }

         p.B(var1, var8, this.g, var9, this.w, var11, var10);
         p.a(var1, var8, this.g, var9, this.w, var11, var7 ? var10 : ez.D);
         uw.X(var1, uw.X(var6, var4 - 9, uw.c.M), var8 + 4, this.g + 6, jx.controlInk(var10), uw.c.M);
      }

      if (this.M && this.q) {
         p.C(var1, this.D, this.g, this.d, this.w, ez.d() ? 7 : 5);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.contains(var1, var3)) {
         int var6 = Math.max(1, this.d / this.J.e().size());
         this.J.setValue(this.J.e().get(Math.clamp((int)(var1 - this.D) / var6, 0, this.J.e().size() - 1)));
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

      int var2 = this.J.e().indexOf(this.J.getValue());
      this.J.setValue(this.J.e().get(Math.floorMod(var2 + (var1 == 263 ? -1 : 1), this.J.e().size())));
      return true;
   }
}
