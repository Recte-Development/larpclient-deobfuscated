package com.larpclient;

import com.larpclient.module.Module;
import net.minecraft.client.gui.GuiGraphicsExtractor;

final class j1 extends j0 {
   private final String I;
   private final Module N;
   private final fq C;

   j1(String var1, Module var2) {
      this.I = var1;
      this.N = var2;
      this.O = 26;
      this.C = new fq(var2.isEnabled() ? 1.0 : 0.0);
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      String var4 = this.I.isEmpty() ? (this.N.isEnabled() ? "Enabled" : "Disabled") : this.I;
      double var5 = this.C.n(this.N.isEnabled() ? 1.0 : 0.0, 180);
      int var7 = vv.Q(c.f, w4.readableAccent(c.c(this.P), c.D), var5);
      ic.n var8 = ic.n.I;
      ic.N(var1, ic.o(var4, this.L - 38, var8), this.P + 4, this.T + (this.O - var8.T()) / 2, var7, var8);
      wc.q(var1, this.P + this.L - 28, this.T + (this.O - 13) / 2, var5);
      if (this.F && this.X) {
         wc.U(var1, this.P + this.L - 28, this.T + (this.O - 13) / 2, 24, 13, 6);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.contains(var1, var3)) {
         this.t();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (this.R && this.V && this.F && (var1 == 257 || var1 == 32)) {
         this.t();
         return true;
      } else {
         return false;
      }
   }

   private void t() {
      this.N.toggle();
      ft.h(this.N.isEnabled() ? ft.e.y : ft.e.J);
   }
}
