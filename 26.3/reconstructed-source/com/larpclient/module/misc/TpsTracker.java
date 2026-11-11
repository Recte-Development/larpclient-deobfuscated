package com.larpclient.module.misc;

import java.util.Arrays;
import net.minecraft.util.Mth;

public final class TpsTracker {
   private static final float[] samples = new float[20];
   private static int sampleIndex;
   private static long lastTickNanos = -1L;

   private TpsTracker() {
   }

   public static void onTimeUpdate() {
      long var0 = System.nanoTime();
      if (lastTickNanos != -1L) {
         float var2 = (float)(var0 - lastTickNanos) / 1.0E9F;
         samples[sampleIndex % samples.length] = Mth.clamp(20.0F / var2, 0.0F, 20.0F);
         sampleIndex++;
      }

      lastTickNanos = var0;
   }

   public static void reset() {
      Arrays.fill(samples, 0.0F);
      sampleIndex = 0;
      lastTickNanos = -1L;
   }

   public static float averageTps() {
      float var0 = 0.0F;
      float var1 = 0.0F;

      for (float var5 : samples) {
         if (var5 > 0.0F) {
            var0 += var5;
            var1++;
         }
      }

      return Mth.clamp(var0 / var1, 0.0F, 20.0F);
   }

   public static float getTps() {
      float var0 = averageTps();
      return Float.isFinite(var0) && var0 > 0.0F ? var0 : 20.0F;
   }
}
