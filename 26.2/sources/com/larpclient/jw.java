package com.larpclient;

import java.util.function.IntConsumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class jw extends jd {
   private static final int[] X = new int[]{-15198181, -1096636, -429290, -1395960, -14498466, -16337196, -12877066, -5745161, -1292135};
   private final IntConsumer Z;
   private int e;

   public jw(IntConsumer var1) {
      this.Z = var1;
      this.J = 18;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      int var4 = Math.max(1, this.A / X.length);
      int var5 = Math.min(12, var4 - 1);

      for (int var6 = 0; var6 < X.length; var6++) {
         int var7 = this.S + var6 * var4;
         wp.I(var1, var7, this.u + 2, var5, var5, 4, X[var6]);
         tb.s(var1, var7, this.u + 2, var5, var5, 4.0F, uh.F(ho.Y, 1.6), uh.F(ho.S, 2.0));
         if (ho.J == X[var6]) {
            tb.J(var1, var7 - 2, this.u, var5 + 4, var5 + 4, 6.0F, uh.H(ho.u, 0.8));
         }

         if (this.T && this.W && this.e == var6) {
            wp.t(var1, var7 - 2, this.u, var5 + 4, var5 + 4, 6);
         }
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.contains(var1, var3)) {
         this.e = Math.clamp((int)(var1 - this.S) / Math.max(1, this.A / X.length), 0, X.length - 1);
         this.Z.accept(X[this.e]);
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

      this.e = Math.floorMod(this.e + (var1 == 263 ? -1 : (var1 == 262 ? 1 : 0)), X.length);
      this.Z.accept(X[this.e]);
      return true;
   }
}
