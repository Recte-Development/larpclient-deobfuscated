package com.larpclient;

import net.minecraft.class_3532;

public final class hl {
   private long c = Long.MIN_VALUE;
   private int p;

   public static float[] m(float var0, float var1, float var2, float var3, double var4) {
      float var6 = class_3532.method_15393(var2 - var0);
      float var7 = class_3532.method_15363(var3, -90.0F, 90.0F) - var1;
      double var8 = Math.hypot(var6, var7);
      double var10 = var8 == 0.0 ? 1.0 : Math.min(1.0, Math.max(1.0, var4) / var8);
      return new float[]{var0 + (float)(var6 * var10), class_3532.method_15363(var1 + (float)(var7 * var10), -90.0F, 90.0F)};
   }

   public void x(long var1, boolean var3) {
      if (var1 != this.c) {
         this.p = var3 ? (this.c == var1 - 1L ? Math.min(5, this.p + 1) : 1) : 0;
         this.c = var1;
      }
   }

   public boolean p(long var1, int var3) {
      return var1 == this.c && this.p >= Math.max(1, var3);
   }

   public void z() {
      this.c = Long.MIN_VALUE;
      this.p = 0;
   }
}
