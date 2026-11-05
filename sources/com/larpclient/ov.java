package com.larpclient;

import com.larpclient.module.Module;
import net.minecraft.client.gui.GuiGraphicsExtractor;

final class ov extends oq {
   private final String X;
   private final Module Z;
   private final u7 s;

   ov(String var1, Module var2) {
      this.X = var1;
      this.Z = var2;
      this.O = 26;
      this.s = new u7(var2.isEnabled() ? 1.0 : 0.0);
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      String var4 = this.X.isEmpty() ? (this.Z.isEnabled() ? "Enabled" : "Disabled") : this.X;
      double var5 = this.s.y(this.Z.isEnabled() ? 1.0 : 0.0, 180);
      int var7 = oh.j(rv.r, jd.readableAccent(rv.m(this.B), rv.w), var5);
      j_.m var8 = j_.m.U;
      j_.L(var1, j_.d(var4, this.v - 38, var8), this.B + 4, this.L + (this.O - var8.C()) / 2, var7, var8);
      ll.e(var1, this.B + this.v - 28, this.L + (this.O - 13) / 2, var5);
      if (this.A && this.h) {
         ll.w(var1, this.B + this.v - 28, this.L + (this.O - 13) / 2, 24, 13, 6);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.contains(var1, var3)) {
         this.s();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (this.F && this.x && this.A && (var1 == 257 || var1 == 32)) {
         this.s();
         return true;
      } else {
         return false;
      }
   }

   private void s() {
      this.Z.toggle();
      hx.w(this.Z.isEnabled() ? hx.t.N : hx.t.F);
   }
}
