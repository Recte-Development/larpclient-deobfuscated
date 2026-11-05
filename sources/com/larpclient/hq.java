package com.larpclient;

import net.minecraft.util.Mth;

public final class hq {
   private long c = Long.MIN_VALUE;
   private int O;

   public static float[] A(float var0, float var1, float var2, float var3, double var4) {
      float var6 = Mth.wrapDegrees(var2 - var0);
      float var7 = Mth.clamp(var3, -90.0F, 90.0F) - var1;
      double var8 = Math.hypot(var6, var7);
      double var10 = var8 == 0.0 ? 1.0 : Math.min(1.0, Math.max(1.0, var4) / var8);
      return new float[]{var0 + (float)(var6 * var10), Mth.clamp(var1 + (float)(var7 * var10), -90.0F, 90.0F)};
   }

   public void h(long var1, boolean var3) {
      if (var1 != this.c) {
         this.O = var3 ? (this.c == var1 - 1L ? Math.min(5, this.O + 1) : 1) : 0;
         this.c = var1;
      }
   }

   public boolean E(long var1, int var3) {
      return var1 == this.c && this.O >= Math.max(1, var3);
   }

   public void f() {
      this.c = Long.MIN_VALUE;
      this.O = 0;
   }
}
