package com.larpclient;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.minecraft.class_332;

public final class lg extends lc {
   private final BooleanSupplier k;
   private final Consumer<Boolean> K;

   public lg(BooleanSupplier var1, Consumer<Boolean> var2) {
      this.k = var1;
      this.K = var2;
      this.w = 52;
   }

   @Override
   public void render(class_332 var1, int var2, int var3) {
      int var4 = Math.max(1, (this.d - 6) / 2);

      for (int var5 = 0; var5 < 2; var5++) {
         int var6 = var5 == 0 ? 1 : 0;
         int var7 = this.D + var5 * (var4 + 6);
         int var8 = var6 != 0 ? -131587 : -15724526;
         int var9 = var6 != 0 ? -1776409 : -14408664;
         p.B(var1, var7, this.g, var4, 36, 6, var8);
         p.a(var1, var7, this.g, var4, 36, 6, ez.D);
         p.B(var1, var7 + 5, this.g + 4, 8, 28, 2, var6 != 0 ? -723723 : -15132387);

         for (int var10 = 0; var10 < 3; var10++) {
            p.B(var1, var7 + 16, this.g + 7 + var10 * 7, Math.max(1, var4 - 22), 4, 2, var9);
         }

         uw.X(var1, var6 != 0 ? "Light" : "Dark", var7 + 4, this.g + 40, var6 == this.k.getAsBoolean() ? ez.H : ez.PH, uw.c.M);
      }

      if (this.M && this.q) {
         p.C(var1, this.D, this.g, this.d, this.w, 6);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.contains(var1, var3)) {
         this.K.accept(var1 < this.D + this.d / 2.0);
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

      this.K.accept(var1 == 263 || var1 != 262 && !this.k.getAsBoolean());
      return true;
   }
}
