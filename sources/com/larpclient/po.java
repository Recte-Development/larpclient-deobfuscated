package com.larpclient;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class po extends oq {
   private final BooleanSupplier E;
   private final Consumer<Boolean> W;

   public po(BooleanSupplier var1, Consumer<Boolean> var2) {
      this.E = var1;
      this.W = var2;
      this.O = 52;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      int var4 = Math.max(1, (this.v - 6) / 2);

      for (int var5 = 0; var5 < 2; var5++) {
         int var6 = var5 == 0 ? 1 : 0;
         int var7 = this.B + var5 * (var4 + 6);
         int var8 = var6 != 0 ? -131587 : -15724526;
         int var9 = var6 != 0 ? -1776409 : -14408664;
         ll.N(var1, var7, this.L, var4, 36, 6, var8);
         ll.u(var1, var7, this.L, var4, 36, 6, rv.f);
         ll.N(var1, var7 + 5, this.L + 4, 8, 28, 2, var6 != 0 ? -723723 : -15132387);

         for (int var10 = 0; var10 < 3; var10++) {
            ll.N(var1, var7 + 16, this.L + 7 + var10 * 7, Math.max(1, var4 - 22), 4, 2, var9);
         }

         j_.L(var1, var6 != 0 ? "Light" : "Dark", var7 + 4, this.L + 40, var6 == this.E.getAsBoolean() ? rv.P : rv.G0, j_.m.U);
      }

      if (this.A && this.h) {
         ll.w(var1, this.B, this.L, this.v, this.O, 6);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.contains(var1, var3)) {
         this.W.accept(var1 < this.B + this.v / 2.0);
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

      this.W.accept(var1 == 263 || var1 != 262 && !this.E.getAsBoolean());
      return true;
   }
}
