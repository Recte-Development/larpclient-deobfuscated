package com.larpclient.util.math;

import net.minecraft.util.Mth;

public final class RotationLimiter {
   private long lastTick = Long.MIN_VALUE;
   private int streak;

   public static float[] stepToward(float var0, float var1, float var2, float var3, double var4) {
      float var6 = Mth.wrapDegrees(var2 - var0);
      float var7 = Mth.clamp(var3, -90.0F, 90.0F) - var1;
      double var8 = Math.hypot(var6, var7);
      double var10 = var8 == 0.0 ? 1.0 : Math.min(1.0, Math.max(1.0, var4) / var8);
      return new float[]{var0 + (float)(var6 * var10), Mth.clamp(var1 + (float)(var7 * var10), -90.0F, 90.0F)};
   }

   public void recordTick(long var1, boolean var3) {
      if (var1 != this.lastTick) {
         this.streak = var3 ? (this.lastTick == var1 - 1L ? Math.min(5, this.streak + 1) : 1) : 0;
         this.lastTick = var1;
      }
   }

   public boolean hasStreak(long var1, int var3) {
      return var1 == this.lastTick && this.streak >= Math.max(1, var3);
   }

   public void reset() {
      this.lastTick = Long.MIN_VALUE;
      this.streak = 0;
   }
}
