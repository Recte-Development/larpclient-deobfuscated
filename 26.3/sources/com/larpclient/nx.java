package com.larpclient;

import net.minecraft.util.Mth;

public final class nx {
   private long e = Long.MIN_VALUE;
   private int N;

   public static float[] M(float var0, float var1, float var2, float var3, double var4) {
      float var6 = Mth.wrapDegrees(var2 - var0);
      float var7 = Mth.clamp(var3, -90.0F, 90.0F) - var1;
      double var8 = Math.hypot(var6, var7);
      double var10 = var8 == 0.0 ? 1.0 : Math.min(1.0, Math.max(1.0, var4) / var8);
      return new float[]{var0 + (float)(var6 * var10), Mth.clamp(var1 + (float)(var7 * var10), -90.0F, 90.0F)};
   }

   public void f(long var1, boolean var3) {
      if (var1 != this.e) {
         this.N = var3 ? (this.e == var1 - 1L ? Math.min(5, this.N + 1) : 1) : 0;
         this.e = var1;
      }
   }

   public boolean C(long var1, int var3) {
      return var1 == this.e && this.N >= Math.max(1, var3);
   }

   public void J() {
      this.e = Long.MIN_VALUE;
      this.N = 0;
   }
}
