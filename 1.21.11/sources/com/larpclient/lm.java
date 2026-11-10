package com.larpclient;

import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_11785;
import net.minecraft.class_12249;
import net.minecraft.class_1921;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4608;
import net.minecraft.class_630;
import net.minecraft.class_4587.class_4665;
import net.minecraft.class_4597.class_4598;

public final class lm {
   private static final class_2960 P = class_2960.method_60655("larp-client", "dynamic/korblox_leg");
   private static final int K = 15728880;
   private static final int D = 64;
   private static int I = -1;
   private static int b = -1;

   private lm() {
   }

   public static boolean R() {
      int var0 = t_.g;
      if (I == var0) {
         return true;
      }

      if (b == var0) {
         return false;
      }

      try {
         class_1043 var1 = new class_1043(() -> "LARP Korblox leg", 64, 64, false);

         try {
            int[] var2 = t7.o();
            class_1011 var3 = var1.method_4525();

            for (int var4 = 0; var4 < var2.length; var4++) {
               var3.method_61941(var4 % 64, var4 / 64, var2[var4]);
            }

            var1.method_4524();
            class_310.method_1551().method_1531().method_4616(P, var1);
         } catch (RuntimeException var5) {
            var1.close();
            throw var5;
         }

         I = var0;
         q8.G("Korblox", "Leg ready: {} faces, generated {}x{} texture, resource generation {}", t7.v().faces(), 64, 64, var0);
         return true;
      } catch (RuntimeException var6) {
         b = var0;
         q8.u("Korblox", "Leg texture unavailable; native legs stay until resource reload", var6);
         return false;
      }
   }

   public static void F(class_4587 var0, class_11785 var1, class_630 var2, yv var3, int var4, int var5, boolean var6) {
      var0.method_22903();

      try {
         var2.method_22703(var0);
         var1.method_73483(var0, lm.x.O, (var4x, var5x) -> s(var4x, var5x, var3, var4, var5, var6, false));
         if (var3.glow() > 0.0F) {
            var1.method_73483(var0, lm.x.S, (var2x, var3x) -> s(var2x, var3x, var3, 15728880, class_4608.field_21444, var6, true));
         }
      } finally {
         var0.method_22909();
      }
   }

   public static void w(class_4587 var0, class_4598 var1, class_630 var2, yv var3, int var4, int var5, boolean var6) {
      var0.method_22903();

      try {
         var2.method_22703(var0);
         s(var0.method_23760(), var1.method_73477(lm.x.O), var3, var4, var5, var6, false);
         if (var3.glow() > 0.0F) {
            s(var0.method_23760(), var1.method_73477(lm.x.S), var3, 15728880, class_4608.field_21444, var6, true);
         }

         var1.method_22993();
      } finally {
         var0.method_22909();
      }
   }

   static void s(class_4665 var0, class_4588 var1, yv var2, int var3, int var4, boolean var5, boolean var6) {
      long var7 = c5.X();
      t7.o var9 = t7.v();
      float[] var10 = var9.uvs();
      float[] var11 = var9.gradient();
      float[] var12 = new float[6];
      int[] var13 = var9.quads();
      float var14 = var6 ? 0.5F : 0.0F;

      for (int var15 = 0; var15 < var9.faces(); var15++) {
         for (int var16 = 0; var16 < 4; var16++) {
            int var17 = var13[var15 * 4 + (var5 ? (4 - var16) % 4 : var16)];
            t7.i(var9, var17, var2.length(), var2.thickness(), var5, var12);
            int var18 = var6 ? t7.d(var2.tip(), var2.glow(), var11[var17]) : t7.d(var2.hip(), var2.tip(), var11[var17]);
            var1.method_56824(var0, var12[0] / 16.0F, var12[1] / 16.0F, var12[2] / 16.0F)
               .method_39415(var18)
               .method_22913(var10[var17 * 2] + var14, var10[var17 * 2 + 1])
               .method_22922(var4)
               .method_60803(var3)
               .method_60831(var0, var12[3], var12[4], var12[5]);
         }
      }

      c5.w("Korblox", var7);
   }

   private static final class x {
      static final class_1921 O = class_12249.method_75990(lm.P);
      static final class_1921 S = class_12249.method_76014(lm.P);
   }
}
