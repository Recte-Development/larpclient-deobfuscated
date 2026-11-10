package com.larpclient;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class sg extends j0 {
   private final BooleanSupplier H;
   private final Consumer<Boolean> W;

   public sg(BooleanSupplier var1, Consumer<Boolean> var2) {
      this.H = var1;
      this.W = var2;
      this.O = 52;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      int var4 = Math.max(1, (this.L - 6) / 2);

      for (int var5 = 0; var5 < 2; var5++) {
         int var6 = var5 == 0 ? 1 : 0;
         int var7 = this.P + var5 * (var4 + 6);
         int var8 = var6 != 0 ? -131587 : -15724526;
         int var9 = var6 != 0 ? -1776409 : -14408664;
         wc.b(var1, var7, this.T, var4, 36, 6, var8);
         wc.h(var1, var7, this.T, var4, 36, 6, c.n);
         wc.b(var1, var7 + 5, this.T + 4, 8, 28, 2, var6 != 0 ? -723723 : -15132387);

         for (int var10 = 0; var10 < 3; var10++) {
            wc.b(var1, var7 + 16, this.T + 7 + var10 * 7, Math.max(1, var4 - 22), 4, 2, var9);
         }

         ic.N(var1, var6 != 0 ? "Light" : "Dark", var7 + 4, this.T + 40, var6 == this.H.getAsBoolean() ? c.H : c.W, ic.n.I);
      }

      if (this.F && this.X) {
         wc.U(var1, this.P, this.T, this.L, this.O, 6);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.contains(var1, var3)) {
         this.W.accept(var1 < this.P + this.L / 2.0);
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

      this.W.accept(var1 == 263 || var1 != 262 && !this.H.getAsBoolean());
      return true;
   }
}
