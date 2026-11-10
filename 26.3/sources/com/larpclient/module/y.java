package com.larpclient.module;

import java.util.Arrays;
import net.minecraft.util.Mth;

public final class y {
   private static final float[] a = new float[20];
   private static int b;
   private static long C = -1L;

   private y() {
   }

   public static void z() {
      long var0 = System.nanoTime();
      if (C != -1L) {
         float var2 = (float)(var0 - C) / 1.0E9F;
         a[b % a.length] = Mth.clamp(20.0F / var2, 0.0F, 20.0F);
         b++;
      }

      C = var0;
   }

   public static void E() {
      Arrays.fill(a, 0.0F);
      b = 0;
      C = -1L;
   }

   public static float w() {
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

   public static float a() {
      float var0 = w();
      return Float.isFinite(var0) && var0 > 0.0F ? var0 : 20.0F;
   }
}
