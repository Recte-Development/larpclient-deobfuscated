package com.larpclient.module;

import java.util.Arrays;
import net.minecraft.class_3532;

public final class a1 {
   private static final float[] V = new float[20];
   private static int M;
   private static long v = -1L;

   private a1() {
   }

   public static void x() {
      long var0 = System.nanoTime();
      if (v != -1L) {
         float var2 = (float)(var0 - v) / 1.0E9F;
         V[M % V.length] = class_3532.method_15363(20.0F / var2, 0.0F, 20.0F);
         M++;
      }

      v = var0;
   }

   public static void z() {
      Arrays.fill(V, 0.0F);
      M = 0;
      v = -1L;
   }

   public static float S() {
      float var0 = 0.0F;
      float var1 = 0.0F;

      for (float var5 : V) {
         if (var5 > 0.0F) {
            var0 += var5;
            var1++;
         }
      }

      return class_3532.method_15363(var0 / var1, 0.0F, 20.0F);
   }

   public static float p() {
      float var0 = S();
      return Float.isFinite(var0) && var0 > 0.0F ? var0 : 20.0F;
   }
}
