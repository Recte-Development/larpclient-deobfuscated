package com.larpclient;

import net.minecraft.class_4588;
import net.minecraft.class_5611;
import net.minecraft.class_777;
import net.minecraft.class_4587.class_4665;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public final class cx {
   public static final float l = 0.55F;
   public static final int G = 9;

   public static class_4588 r(class_4588 var0) {
      return j5.J() ? new cx.d(var0) : var0;
   }

   public static void t(
      class_4588 var0, class_4665 var1, class_777 var2, float[] var3, float var4, float var5, float var6, float var7, int[] var8, int var9, int var10
   ) {
      if (var10 >= 1 && var10 <= 3) {
         Vector3fc var11 = var2.comp_5238();
         Vector3fc var12 = var2.comp_5239();
         Vector3fc var13 = var2.comp_5241();
         int var14 = i(var11, var12) ? var10 : 1;
         int var15 = i(var11, var13) ? var10 : 1;

         for (int var16 = 0; var16 < var15; var16++) {
            for (int var17 = 0; var17 < var14; var17++) {
               float var18 = (float)var17 / var14;
               float var19 = (float)(var17 + 1) / var14;
               float var20 = (float)var16 / var15;
               float var21 = (float)(var16 + 1) / var15;
               float[] var22 = new float[]{var18, var19, var19, var18};
               float[] var23 = new float[]{var20, var20, var21, var21};
               Vector3f[] var24 = new Vector3f[4];
               long[] var25 = new long[4];
               float[] var26 = new float[4];
               int[] var27 = new int[4];

               for (int var28 = 0; var28 < 4; var28++) {
                  float var29 = var22[var28];
                  float var30 = var23[var28];
                  var24[var28] = new Vector3f(
                     W(var11.x(), var12.x(), var2.comp_5240().x(), var13.x(), var29, var30),
                     W(var11.y(), var12.y(), var2.comp_5240().y(), var13.y(), var29, var30),
                     W(var11.z(), var12.z(), var2.comp_5240().z(), var13.z(), var29, var30)
                  );
                  var25[var28] = class_5611.method_76640(w(var2, var29, var30, true), w(var2, var29, var30, false));
                  var26[var28] = W(var3[0], var3[1], var3[2], var3[3], var29, var30);
                  var27[var28] = e(var8, var29, var30);
               }

               class_777 var31 = new class_777(
                  var24[0],
                  var24[1],
                  var24[2],
                  var24[3],
                  var25[0],
                  var25[1],
                  var25[2],
                  var25[3],
                  var2.comp_3722(),
                  var2.comp_3723(),
                  var2.comp_3724(),
                  var2.comp_3725(),
                  var2.comp_3726()
               );
               var0.method_22920(var1, var31, var26, var4, var5, var6, var7, var27, var9);
            }
         }
      } else {
         throw new IllegalArgumentException("Unbounded snow subdivision");
      }
   }

   private static boolean i(Vector3fc var0, Vector3fc var1) {
      return Math.abs(var0.x() - var1.x()) + Math.abs(var0.z() - var1.z()) > 1.0E-5F;
   }

   private static float W(float var0, float var1, float var2, float var3, float var4, float var5) {
      return var4 >= var5 ? var0 + (var1 - var0) * (var4 - var5) + (var2 - var0) * var5 : var0 + (var2 - var0) * var4 + (var3 - var0) * (var5 - var4);
   }

   private static float w(class_777 var0, float var1, float var2, boolean var3) {
      return var3
         ? W(
            class_5611.method_76641(var0.comp_5242()),
            class_5611.method_76641(var0.comp_5243()),
            class_5611.method_76641(var0.comp_5244()),
            class_5611.method_76641(var0.comp_5245()),
            var1,
            var2
         )
         : W(
            class_5611.method_76642(var0.comp_5242()),
            class_5611.method_76642(var0.comp_5243()),
            class_5611.method_76642(var0.comp_5244()),
            class_5611.method_76642(var0.comp_5245()),
            var1,
            var2
         );
   }

   private static int e(int[] var0, float var1, float var2) {
      int var3 = 0;

      for (int var7 : new int[]{0, 16}) {
         var3 |= Math.clamp(
               Math.round(W(var0[0] >>> var7 & 65535, var0[1] >>> var7 & 65535, var0[2] >>> var7 & 65535, var0[3] >>> var7 & 65535, var1, var2)), 0, 240
            )
            << var7;
      }

      return var3;
   }

   private cx() {
   }

   private record d(class_4588 output) implements class_4588 {
      public void method_22920(class_4665 var1, class_777 var2, float[] var3, float var4, float var5, float var6, float var7, int[] var8, int var9) {
         int var10 = j5.P(var2, var8);
         if (var10 == 0) {
            this.output.method_22920(var1, var2, var3, var4, var5, var6, var7, var8, var9);
         } else {
            cx.t(this.output, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
         }
      }

      public class_4588 method_22912(float var1, float var2, float var3) {
         this.output.method_22912(var1, var2, var3);
         return this;
      }

      public class_4588 method_1336(int var1, int var2, int var3, int var4) {
         this.output.method_1336(var1, var2, var3, var4);
         return this;
      }

      public class_4588 method_39415(int var1) {
         this.output.method_39415(var1);
         return this;
      }

      public class_4588 method_22913(float var1, float var2) {
         this.output.method_22913(var1, var2);
         return this;
      }

      public class_4588 method_60796(int var1, int var2) {
         this.output.method_60796(var1, var2);
         return this;
      }

      public class_4588 method_22921(int var1, int var2) {
         this.output.method_22921(var1, var2);
         return this;
      }

      public class_4588 method_22914(float var1, float var2, float var3) {
         this.output.method_22914(var1, var2, var3);
         return this;
      }

      public class_4588 method_75298(float var1) {
         this.output.method_75298(var1);
         return this;
      }
   }
}
