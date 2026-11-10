package com.larpclient;

import com.larpclient.module.Module;
import net.minecraft.client.gui.GuiGraphicsExtractor;

final class jg extends jd {
   private final String v;
   private final Module Z;
   private final jp y;

   jg(String var1, Module var2) {
      this.v = var1;
      this.Z = var2;
      this.J = 26;
      this.y = new jp(var2.isEnabled() ? 1.0 : 0.0);
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      String var4 = this.v.isEmpty() ? (this.Z.isEnabled() ? "Enabled" : "Disabled") : this.v;
      double var5 = this.y.k(this.Z.isEnabled() ? 1.0 : 0.0, 180);
      int var7 = uh.q(ho.j, lv.readableAccent(ho.Q(this.S), ho.A), var5);
      bj.u var8 = bj.u.M;
      bj.k(var1, bj.i(var4, this.A - 38, var8), this.S + 4, this.u + (this.J - var8.n()) / 2, var7, var8);
      wp.E(var1, this.S + this.A - 28, this.u + (this.J - 13) / 2, var5);
      if (this.T && this.W) {
         wp.t(var1, this.S + this.A - 28, this.u + (this.J - 13) / 2, 24, 13, 6);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.contains(var1, var3)) {
         this.z();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (this.i && this.p && this.T && (var1 == 257 || var1 == 32)) {
         this.z();
         return true;
      } else {
         return false;
      }
   }

   private void z() {
      this.Z.toggle();
      sr.v(this.Z.isEnabled() ? sr.e.J : sr.e.v);
   }
}
