package com.larpclient;

import com.larpclient.module.Module;
import net.minecraft.client.gui.GuiGraphicsExtractor;

final class l extends b {
   private final String t;
   private final Module d;
   private final ef I;

   l(String var1, Module var2) {
      this.t = var1;
      this.d = var2;
      this.W = 26;
      this.I = new ef(var2.isEnabled() ? 1.0 : 0.0);
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      String var4 = this.t.isEmpty() ? (this.d.isEnabled() ? "Enabled" : "Disabled") : this.t;
      double var5 = this.I.m(this.d.isEnabled() ? 1.0 : 0.0, 180);
      int var7 = _3.D(f.G, n.readableAccent(f.z(this.q), f.u), var5);
      k6.d var8 = k6.d.K;
      k6.T(var1, k6.o(var4, this.A - 38, var8), this.q + 4, this.J + (this.W - var8.q()) / 2, var7, var8);
      pn.Y(var1, this.q + this.A - 28, this.J + (this.W - 13) / 2, var5);
      if (this.l && this.R) {
         pn.y(var1, this.q + this.A - 28, this.J + (this.W - 13) / 2, 24, 13, 6);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.contains(var1, var3)) {
         this.E();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (this.D && this.X && this.l && (var1 == 257 || var1 == 32)) {
         this.E();
         return true;
      } else {
         return false;
      }
   }

   private void E() {
      this.d.toggle();
      kb.d(this.d.isEnabled() ? kb.n.l : kb.n.O);
   }
}
