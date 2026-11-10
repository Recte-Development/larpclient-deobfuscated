package com.larpclient;

import java.util.function.IntConsumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class s4 extends j0 {
   private static final int[] S = new int[]{-15198181, -1096636, -429290, -1395960, -14498466, -16337196, -12877066, -5745161, -1292135};
   private final IntConsumer B;
   private int a;

   public s4(IntConsumer var1) {
      this.B = var1;
      this.O = 18;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      int var4 = Math.max(1, this.L / S.length);
      int var5 = Math.min(12, var4 - 1);

      for (int var6 = 0; var6 < S.length; var6++) {
         int var7 = this.P + var6 * var4;
         wc.b(var1, var7, this.T + 2, var5, var5, 4, S[var6]);
         w1.T(var1, var7, this.T + 2, var5, var5, 4.0F, vv.O(c.Xn, 1.6), vv.O(c.XZ, 2.0));
         if (c.y == S[var6]) {
            w1.X(var1, var7 - 2, this.T, var5 + 4, var5 + 4, 6.0F, vv.N(c.H, 0.8));
         }

         if (this.F && this.X && this.a == var6) {
            wc.U(var1, var7 - 2, this.T, var5 + 4, var5 + 4, 6);
         }
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.contains(var1, var3)) {
         this.a = Math.clamp((int)(var1 - this.P) / Math.max(1, this.L / S.length), 0, S.length - 1);
         this.B.accept(S[this.a]);
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

      this.a = Math.floorMod(this.a + (var1 == 263 ? -1 : (var1 == 262 ? 1 : 0)), S.length);
      this.B.accept(S[this.a]);
      return true;
   }
}
