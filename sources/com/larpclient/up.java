package com.larpclient;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class up extends b {
   private final BooleanSupplier H;
   private final Consumer<Boolean> K;

   public up(BooleanSupplier var1, Consumer<Boolean> var2) {
      this.H = var1;
      this.K = var2;
      this.W = 52;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      int var4 = Math.max(1, (this.A - 6) / 2);

      for (int var5 = 0; var5 < 2; var5++) {
         int var6 = var5 == 0 ? 1 : 0;
         int var7 = this.q + var5 * (var4 + 6);
         int var8 = var6 != 0 ? -131587 : -15724526;
         int var9 = var6 != 0 ? -1776409 : -14408664;
         pn.b(var1, var7, this.J, var4, 36, 6, var8);
         pn.D(var1, var7, this.J, var4, 36, 6, f.I);
         pn.b(var1, var7 + 5, this.J + 4, 8, 28, 2, var6 != 0 ? -723723 : -15132387);

         for (int var10 = 0; var10 < 3; var10++) {
            pn.b(var1, var7 + 16, this.J + 7 + var10 * 7, Math.max(1, var4 - 22), 4, 2, var9);
         }

         k6.T(var1, var6 != 0 ? "Light" : "Dark", var7 + 4, this.J + 40, var6 == this.H.getAsBoolean() ? f.J : f.s, k6.d.K);
      }

      if (this.l && this.R) {
         pn.y(var1, this.q, this.J, this.A, this.W, 6);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.contains(var1, var3)) {
         this.K.accept(var1 < this.q + this.A / 2.0);
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

      this.K.accept(var1 == 263 || var1 != 262 && !this.H.getAsBoolean());
      return true;
   }
}
