package com.larpclient.module;

import java.util.Arrays;
import net.minecraft.util.Mth;

public final class e {
   private static final float[] e = new float[20];
   private static int w;
   private static long u = -1L;

   private e() {
   }

   public static void w() {
      long var0 = System.nanoTime();
      if (u != -1L) {
         float var2 = (float)(var0 - u) / 1.0E9F;
         e[w % e.length] = Mth.clamp(20.0F / var2, 0.0F, 20.0F);
         w++;
      }

      u = var0;
   }

   public static void l() {
      Arrays.fill(e, 0.0F);
      w = 0;
      u = -1L;
   }

   public static float A() {
      float var0 = 0.0F;
      float var1 = 0.0F;

      for (float var5 : e) {
         if (var5 > 0.0F) {
            var0 += var5;
            var1++;
         }
      }

      return Mth.clamp(var0 / var1, 0.0F, 20.0F);
   }

   public static float D() {
      float var0 = A();
      return Float.isFinite(var0) && var0 > 0.0F ? var0 : 20.0F;
   }
}
