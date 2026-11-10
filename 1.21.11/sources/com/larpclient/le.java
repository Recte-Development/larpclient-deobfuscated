package com.larpclient;

import com.larpclient.module.Module;
import net.minecraft.class_332;

final class le extends lc {
   private final String G;
   private final Module x;
   private final qb r;

   le(String var1, Module var2) {
      this.G = var1;
      this.x = var2;
      this.w = 26;
      this.r = new qb(var2.isEnabled() ? 1.0 : 0.0);
   }

   @Override
   public void render(class_332 var1, int var2, int var3) {
      String var4 = this.G.isEmpty() ? (this.x.isEnabled() ? "Enabled" : "Disabled") : this.G;
      double var5 = this.r.a(this.x.isEnabled() ? 1.0 : 0.0, 180);
      int var7 = ei.W(ez.t, jx.readableAccent(ez.P(this.D), ez.d), var5);
      uw.c var8 = uw.c.M;
      uw.X(var1, uw.X(var4, this.d - 38, var8), this.D + 4, this.g + (this.w - var8.b()) / 2, var7, var8);
      p.s(var1, this.D + this.d - 28, this.g + (this.w - 13) / 2, var5);
      if (this.M && this.q) {
         p.C(var1, this.D + this.d - 28, this.g + (this.w - 13) / 2, 24, 13, 6);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.contains(var1, var3)) {
         this.o();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (this.I && this.N && this.M && (var1 == 257 || var1 == 32)) {
         this.o();
         return true;
      } else {
         return false;
      }
   }

   private void o() {
      this.x.toggle();
      c_.b(this.x.isEnabled() ? c_.d.d : c_.d.n);
   }
}
