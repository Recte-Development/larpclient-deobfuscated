package com.larpclient;

import com.larpclient.module.PlayerModelModule;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.class_10055;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_10799;
import net.minecraft.class_11659;
import net.minecraft.class_12247;
import net.minecraft.class_1306;
import net.minecraft.class_1921;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3300;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4608;
import net.minecraft.class_591;
import net.minecraft.class_12247.class_4750;
import net.minecraft.class_4587.class_4665;

public final class fm {
   private static final class_2960 K = n("textures/particles/cash.png");
   private static final class_2960 s = n("dynamic/cash_fan");
   public static final int R = 4;
   private static class_1043 h;
   private static long V = -1L;
   private static long z = -1L;

   private fm() {
   }

   private static class_2960 n(String var0) {
      return class_2960.method_60655("larp-client", var0);
   }

   public static boolean Y(class_591 var0, class_10055 var1, class_4587 var2, class_11659 var3, int var4) {
      q1 var5 = (q1)var1;
      float var6 = var5.larp$emoteWeight();
      if (!(var6 <= 0.0F) && "cash_fans".equals(var5.larp$emoteProp())) {
         PlayerModelModule var7 = PlayerModelModule.h();
         if (var7 != null && var7.g(var1)) {
            return false;
         }

         float[] var8 = var5.larp$emoteSample();
         int var9 = cb.x.B.ordinal() * 10;
         double var10 = E(var8[var9 + 7], 1.0) * var6;
         if (!G()) {
            return false;
         }

         if (var10 < 0.001) {
            return true;
         }

         double var12 = E(var8[var9 + 3], 0.0);
         double var14 = E(var8[var9 + 4], 0.0);
         double var16 = E(var8[var9 + 5], 0.0);
         double var18 = E(var8[var9 + 8], 0.0);

         for (class_1306 var23 : class_1306.values()) {
            float[] var24 = us.I(var23 == class_1306.field_6183, var12, var14, var16, var18, var10, var5.larp$emoteCycle());
            var2.method_22903();

            try {
               var0.method_72859(var1, var23, var2);
               var3.method_73483(var2, fm.m.R, (var2x, var3x) -> o(var24, var2x, var3x, var4));
            } finally {
               var2.method_22909();
            }
         }

         c5.L("Cash fan bills", 14L);
         return true;
      } else {
         return false;
      }
   }

   private static double E(float var0, double var1) {
      return Float.isNaN(var0) ? var1 : var0;
   }

   private static void o(float[] var0, class_4665 var1, class_4588 var2, int var3) {
      for (byte var4 = 0; var4 < var0.length; var4 += 9) {
         int var5 = Math.round(Math.clamp(var0[var4 + 8], 0.0F, 1.0F) * 255.0F);
         var2.method_56824(var1, var0[var4], var0[var4 + 1], var0[var4 + 2])
            .method_39415(0xFF000000 | var5 * 65793)
            .method_22913(var0[var4 + 3], var0[var4 + 4])
            .method_22922(class_4608.field_21444)
            .method_60803(var3)
            .method_60831(var1, var0[var4 + 5], var0[var4 + 6], var0[var4 + 7]);
      }
   }

   private static boolean G() {
      if (z == t_.g) {
         return false;
      }

      if (h != null && V == t_.g) {
         return true;
      }

      try {
         u();
         return true;
      } catch (IOException | RuntimeException var1) {
         z = t_.g;
         h = null;
         q8.u("Emotes", "Cash fan atlas unavailable; Money Spread shows held items until resource reload", var1);
         return false;
      }
   }

   private static void u() throws IOException {
      class_3300 var0 = class_310.method_1551().method_1478();

      int[] var1;
      int var2;
      int var3;
      try (InputStream var4 = var0.getResourceOrThrow(K).method_14482()) {
         class_1011 var5 = class_1011.method_4309(var4);

         try {
            var2 = var5.method_4307() / 4;
            var3 = var5.method_4323() / 4;
            if (var2 * 4 != var5.method_4307() || var3 * 4 != var5.method_4323()) {
               throw new IOException("Cash atlas size " + var5.method_4307() + "x" + var5.method_4323() + " is not a multiple of 4");
            }

            var1 = U(var5.method_61942(), var5.method_4307(), var5.method_4323(), 4);
         } catch (Throwable var12) {
            if (var5 != null) {
               try {
                  var5.close();
               } catch (Throwable var9) {
                  var12.addSuppressed(var9);
               }
            }

            throw var12;
         }

         if (var5 != null) {
            var5.close();
         }
      }

      class_1011 var14 = new class_1011(var2, var3, false);

      class_1043 var15;
      try {
         for (int var6 = 0; var6 < var1.length; var6++) {
            var14.method_61941(var6 % var2, var6 / var2, var1[var6]);
         }

         var15 = new class_1043(() -> "LARP cash fan", var14);
      } catch (RuntimeException var11) {
         var14.close();
         throw var11;
      }

      try {
         class_310.method_1551().method_1531().method_4616(s, var15);
      } catch (RuntimeException var10) {
         var15.close();
         throw var10;
      }

      h = var15;
      V = t_.g;
      q8.G("Emotes", "Cash fan atlas ready: {}x{}, resource generation {}", var2, var3, V);
   }

   public static int[] U(int[] var0, int var1, int var2, int var3) {
      int var4 = var1 / var3;
      int var5 = var2 / var3;
      int[] var6 = new int[var4 * var5];

      for (int var7 = 0; var7 < var5; var7++) {
         for (int var8 = 0; var8 < var4; var8++) {
            long var9 = 0L;
            long var11 = 0L;
            long var13 = 0L;
            long var15 = 0L;

            for (int var17 = 0; var17 < var3; var17++) {
               for (int var18 = 0; var18 < var3; var18++) {
                  int var19 = var0[(var7 * var3 + var17) * var1 + var8 * var3 + var18];
                  int var20 = var19 >>> 24;
                  var9 += var20;
                  var11 += (var19 >> 16 & 0xFF) * var20;
                  var13 += (var19 >> 8 & 0xFF) * var20;
                  var15 += (var19 & 0xFF) * var20;
               }
            }

            if (var9 != 0L) {
               int var29 = (int)Math.round((double)var9 / (var3 * var3));
               var6[var7 * var4 + var8] = var29 << 24
                  | (int)Math.round((double)var11 / var9) << 16
                  | (int)Math.round((double)var13 / var9) << 8
                  | (int)Math.round((double)var15 / var9);
            }
         }
      }

      for (int var23 = 0; var23 < 2; var23++) {
         int[] var24 = (int[])var6.clone();

         for (int var25 = 0; var25 < var5; var25++) {
            for (int var10 = 0; var10 < var4; var10++) {
               if (var6[var25 * var4 + var10] >>> 24 == 0) {
                  long var26 = 0L;
                  long var27 = 0L;
                  long var28 = 0L;
                  int var30 = 0;

                  for (int var31 = -1; var31 <= 1; var31++) {
                     for (int var32 = -1; var32 <= 1; var32++) {
                        int var33 = var10 + var32;
                        int var21 = var25 + var31;
                        if (var33 >= 0 && var21 >= 0 && var33 < var4 && var21 < var5) {
                           int var22 = var6[var21 * var4 + var33];
                           if (var22 >>> 24 >= 128) {
                              var26 += var22 >> 16 & 0xFF;
                              var27 += var22 >> 8 & 0xFF;
                              var28 += var22 & 0xFF;
                              var30++;
                           }
                        }
                     }
                  }

                  if (var30 > 0) {
                     var24[var25 * var4 + var10] = 0xFF000000 | (int)(var26 / var30) << 16 | (int)(var27 / var30) << 8 | (int)(var28 / var30);
                  }
               }
            }
         }

         var6 = var24;
      }

      return var6;
   }

   private static final class m {
      static final class_1921 R = class_1921.method_75940(
         "larp_cash_fan",
         class_12247.method_75927(class_10799.field_56903)
            .method_76560("Sampler0", fm.s, () -> RenderSystem.getSamplerCache().method_75294(FilterMode.LINEAR))
            .method_75928()
            .method_75935()
            .method_75936()
            .method_75932(class_4750.field_21855)
            .method_75938()
      );
   }
}
