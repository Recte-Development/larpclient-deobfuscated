package com.larpclient.module;

import java.util.Arrays;
import net.minecraft.util.Mth;

public final class lm {
   private static final float[] k = new float[20];
   private static int q;
   private static long i = -1L;

   private lm() {
   }

   public static void M() {
      long var0 = System.nanoTime();
      if (i != -1L) {
         float var2 = (float)(var0 - i) / 1.0E9F;
         k[q % k.length] = Mth.clamp(20.0F / var2, 0.0F, 20.0F);
         q++;
      }

      i = var0;
   }

   public static void N() {
      Arrays.fill(k, 0.0F);
      q = 0;
      i = -1L;
   }

   public static float a() {
      float var0 = 0.0F;
      float var1 = 0.0F;

      for (float var5 : k) {
         if (var5 > 0.0F) {
            var0 += var5;
            var1++;
         }
      }

      return Mth.clamp(var0 / var1, 0.0F, 20.0F);
   }

   public static float N() {
      float var0 = a();
      return Float.isFinite(var0) && var0 > 0.0F ? var0 : 20.0F;
   }
}
