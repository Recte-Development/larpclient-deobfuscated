package com.larpclient;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class jn extends jd {
   private final BooleanSupplier O;
   private final Consumer<Boolean> e;

   public jn(BooleanSupplier var1, Consumer<Boolean> var2) {
      this.O = var1;
      this.e = var2;
      this.J = 52;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      int var4 = Math.max(1, (this.A - 6) / 2);

      for (int var5 = 0; var5 < 2; var5++) {
         int var6 = var5 == 0 ? 1 : 0;
         int var7 = this.S + var5 * (var4 + 6);
         int var8 = var6 != 0 ? -131587 : -15724526;
         int var9 = var6 != 0 ? -1776409 : -14408664;
         wp.I(var1, var7, this.u, var4, 36, 6, var8);
         wp.q(var1, var7, this.u, var4, 36, 6, ho.c);
         wp.I(var1, var7 + 5, this.u + 4, 8, 28, 2, var6 != 0 ? -723723 : -15132387);

         for (int var10 = 0; var10 < 3; var10++) {
            wp.I(var1, var7 + 16, this.u + 7 + var10 * 7, Math.max(1, var4 - 22), 4, 2, var9);
         }

         bj.k(var1, var6 != 0 ? "Light" : "Dark", var7 + 4, this.u + 40, var6 == this.O.getAsBoolean() ? ho.u : ho.Z, bj.u.M);
      }

      if (this.T && this.W) {
         wp.t(var1, this.S, this.u, this.A, this.J, 6);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.contains(var1, var3)) {
         this.e.accept(var1 < this.S + this.A / 2.0);
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

      this.e.accept(var1 == 263 || var1 != 262 && !this.O.getAsBoolean());
      return true;
   }
}
