package com.larpclient;

import java.util.function.IntConsumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class u9 extends b {
   private static final int[] y = new int[]{-15198181, -1096636, -429290, -1395960, -14498466, -16337196, -12877066, -5745161, -1292135};
   private final IntConsumer M;
   private int C;

   public u9(IntConsumer var1) {
      this.M = var1;
      this.W = 18;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      int var4 = Math.max(1, this.A / y.length);
      int var5 = Math.min(12, var4 - 1);

      for (int var6 = 0; var6 < y.length; var6++) {
         int var7 = this.q + var6 * var4;
         pn.b(var1, var7, this.J + 2, var5, var5, 4, y[var6]);
         tl.V(var1, var7, this.J + 2, var5, var5, 4.0F, _3.U(f.H, 1.6), _3.U(f.g, 2.0));
         if (f.C == y[var6]) {
            tl.M(var1, var7 - 2, this.J, var5 + 4, var5 + 4, 6.0F, _3.A(f.J, 0.8));
         }

         if (this.l && this.R && this.C == var6) {
            pn.y(var1, var7 - 2, this.J, var5 + 4, var5 + 4, 6);
         }
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.contains(var1, var3)) {
         this.C = Math.clamp((int)(var1 - this.q) / Math.max(1, this.A / y.length), 0, y.length - 1);
         this.M.accept(y[this.C]);
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

      this.C = Math.floorMod(this.C + (var1 == 263 ? -1 : (var1 == 262 ? 1 : 0)), y.length);
      this.M.accept(y[this.C]);
      return true;
   }
}
