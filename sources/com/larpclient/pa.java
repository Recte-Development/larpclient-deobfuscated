package com.larpclient;

import java.util.function.IntConsumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class pa extends oq {
   private static final int[] W = new int[]{-15198181, -1096636, -429290, -1395960, -14498466, -16337196, -12877066, -5745161, -1292135};
   private final IntConsumer Z;
   private int t;

   public pa(IntConsumer var1) {
      this.Z = var1;
      this.O = 18;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      int var4 = Math.max(1, this.v / W.length);
      int var5 = Math.min(12, var4 - 1);

      for (int var6 = 0; var6 < W.length; var6++) {
         int var7 = this.B + var6 * var4;
         ll.N(var1, var7, this.L + 2, var5, var5, 4, W[var6]);
         ow.y(var1, var7, this.L + 2, var5, var5, 4.0F, oh.S(rv.h, 1.6), oh.S(rv.C, 2.0));
         if (rv.e == W[var6]) {
            ow.A(var1, var7 - 2, this.L, var5 + 4, var5 + 4, 6.0F, oh.i(rv.P, 0.8));
         }

         if (this.A && this.h && this.t == var6) {
            ll.w(var1, var7 - 2, this.L, var5 + 4, var5 + 4, 6);
         }
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.contains(var1, var3)) {
         this.t = Math.clamp((int)(var1 - this.B) / Math.max(1, this.v / W.length), 0, W.length - 1);
         this.Z.accept(W[this.t]);
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

      this.t = Math.floorMod(this.t + (var1 == 263 ? -1 : (var1 == 262 ? 1 : 0)), W.length);
      this.Z.accept(W[this.t]);
      return true;
   }
}
