package com.larpclient;

import java.util.function.IntConsumer;
import net.minecraft.class_332;

public final class l3 extends lc {
   private static final int[] e = new int[]{-15198181, -1096636, -429290, -1395960, -14498466, -16337196, -12877066, -5745161, -1292135};
   private final IntConsumer O;
   private int Y;

   public l3(IntConsumer var1) {
      this.O = var1;
      this.w = 18;
   }

   @Override
   public void render(class_332 var1, int var2, int var3) {
      int var4 = Math.max(1, this.d / e.length);
      int var5 = Math.min(12, var4 - 1);

      for (int var6 = 0; var6 < e.length; var6++) {
         int var7 = this.D + var6 * var4;
         p.B(var1, var7, this.g + 2, var5, var5, 4, e[var6]);
         ws.A(var1, var7, this.g + 2, var5, var5, 4.0F, ei.R(ez.o, 1.6), ei.R(ez.J, 2.0));
         if (ez.a == e[var6]) {
            ws.X(var1, var7 - 2, this.g, var5 + 4, var5 + 4, 6.0F, ei.D(ez.H, 0.8));
         }

         if (this.M && this.q && this.Y == var6) {
            p.C(var1, var7 - 2, this.g, var5 + 4, var5 + 4, 6);
         }
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.contains(var1, var3)) {
         this.Y = Math.clamp((int)(var1 - this.D) / Math.max(1, this.d / e.length), 0, e.length - 1);
         this.O.accept(e[this.Y]);
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

      this.Y = Math.floorMod(this.Y + (var1 == 263 ? -1 : (var1 == 262 ? 1 : 0)), e.length);
      this.O.accept(e[this.Y]);
      return true;
   }
}
