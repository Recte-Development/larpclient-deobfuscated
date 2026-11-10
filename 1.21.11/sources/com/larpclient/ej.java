package com.larpclient;

import net.minecraft.class_4587;
import net.minecraft.class_572;
import net.minecraft.class_630;
import net.minecraft.class_7833;

public final class ej {
   private ej() {
   }

   public static void F(class_572<?> var0, float[] var1, float var2) {
      y(var0.field_3398, var1, cb.x.y, var2);
      y(var0.field_3391, var1, cb.x.g, var2);
      y(var0.field_3401, var1, cb.x.j, var2);
      y(var0.field_27433, var1, cb.x.m, var2);
      y(var0.field_3392, var1, cb.x.k, var2);
      y(var0.field_3397, var1, cb.x.M, var2);
   }

   public static float[] y(float[] var0, float var1) {
      cb.x[] var2 = new cb.x[]{cb.x.j, cb.x.m, cb.x.k, cb.x.M};
      float[] var3 = null;

      for (int var4 = 0; var4 < var2.length; var4++) {
         float var5 = var0[var2[var4].ordinal() * 10 + 6];
         if (!Float.isNaN(var5)) {
            if (var3 == null) {
               var3 = new float[4];
            }

            var3[var4] = var5 * var1;
         }
      }

      return var3;
   }

   public static void m(class_4587 var0, float[] var1, float var2) {
      int var3 = cb.x.D.ordinal() * 10;
      var0.method_46416(Y(var1[var3], var2), Y(var1[var3 + 1], var2) + 0.7F, Y(var1[var3 + 2], var2));
      em.W(var0, class_7833.field_40718.rotation(Y(var1[var3 + 5], var2)));
      em.W(var0, class_7833.field_40716.rotation(Y(var1[var3 + 4], var2)));
      em.W(var0, class_7833.field_40714.rotation(Y(var1[var3 + 3], var2)));
      var0.method_46416(0.0F, -0.7F, 0.0F);
   }

   private static void y(class_630 var0, float[] var1, cb.x var2, float var3) {
      int var4 = var2.ordinal() * 10;
      var0.field_3657 = l(var0.field_3657, var1[var4], var3);
      var0.field_3656 = l(var0.field_3656, var1[var4 + 1], var3);
      var0.field_3655 = l(var0.field_3655, var1[var4 + 2], var3);
      var0.field_3654 = l(var0.field_3654, var1[var4 + 3], var3);
      var0.field_3675 = l(var0.field_3675, var1[var4 + 4], var3);
      var0.field_3674 = l(var0.field_3674, var1[var4 + 5], var3);
   }

   static float l(float var0, float var1, float var2) {
      return Float.isNaN(var1) ? var0 : var0 + (var1 - var0) * var2;
   }

   private static float Y(float var0, float var1) {
      return Float.isNaN(var0) ? 0.0F : var0 * var1;
   }
}
