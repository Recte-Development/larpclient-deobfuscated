package com.larpclient.module;

import java.util.Arrays;
import net.minecraft.util.Mth;

public final class t3 {
   private static final float[] a = new float[20];
   private static int A;
   private static long j = -1L;

   private t3() {
   }

   public static void S() {
      long var0 = System.nanoTime();
      if (j != -1L) {
         float var2 = (float)(var0 - j) / 1.0E9F;
         a[A % a.length] = Mth.clamp(20.0F / var2, 0.0F, 20.0F);
         A++;
      }

      j = var0;
   }

   public static void g() {
      Arrays.fill(a, 0.0F);
      A = 0;
      j = -1L;
   }

   public static float e() {
      float var0 = 0.0F;
      float var1 = 0.0F;

      for (float var5 : a) {
         if (var5 > 0.0F) {
            var0 += var5;
            var1++;
         }
      }

      return Mth.clamp(var0 / var1, 0.0F, 20.0F);
   }

   public static float v() {
      float var0 = e();
      return Float.isFinite(var0) && var0 > 0.0F ? var0 : 20.0F;
   }
}
