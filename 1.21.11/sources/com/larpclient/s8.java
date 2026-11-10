package com.larpclient;

import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_3532;

public final class s8 {
   public static double n(double var0) {
      double var2 = Math.clamp(var0 - 0.5, 0.0, 1.0);
      return var2 * var2 * (3.0 - 2.0 * var2);
   }

   public static float[] V(class_238 var0, class_243 var1, class_243 var2, float var3, float var4) {
      if (var0.method_1006(var1)) {
         return new float[]{var3, var4};
      }

      float[] var5 = t9.X(var1, var2);
      if (Math.hypot(var2.field_1352 - var1.field_1352, var2.field_1350 - var1.field_1350) < 1.0E-6) {
         var5[0] = var3;
      }

      return var5;
   }

   public static float[] R(class_238 var0, class_243 var1, class_243 var2, class_238 var3, class_243 var4, float var5, float var6, double var7) {
      float[] var9 = V(var0, var1, var2, var5, var6);
      if (var0.method_1006(var1)) {
         return var9;
      }

      float var10 = var5;
      float var11 = var6;
      if (var3 != null && var4 != null && !var3.method_1006(var4) && var1.method_1025(var4) <= 1.0 && var0.method_1005().method_1025(var3.method_1005()) <= 1.0
         )
       {
         class_243 var12 = W(var3, B(var0, var2));
         float[] var13 = t9.X(var4, var12);
         float var14 = class_3532.method_15393(var9[0] - var13[0]);
         float var15 = var9[1] - var13[1];
         if (Math.abs(var14) <= 60.0F && Math.abs(var15) <= 60.0F) {
            var10 += var14;
            var11 += var15;
         }
      }

      return t9.e(var10, var11, var9[0], var9[1], var7);
   }

   public static class_243 g(class_238 var0, class_243 var1) {
      double var2 = Math.min(0.001, Math.min(var0.method_17939(), Math.min(var0.method_17940(), var0.method_17941())) / 4.0);
      return new class_243(
         Math.clamp(var1.field_1352, var0.field_1323 + var2, var0.field_1320 - var2),
         Math.clamp(var1.field_1351, var0.field_1322 + var2, var0.field_1325 - var2),
         Math.clamp(var1.field_1350, var0.field_1321 + var2, var0.field_1324 - var2)
      );
   }

   public static class_243 B(class_238 var0, class_243 var1) {
      return new class_243(
         (var1.field_1352 - var0.field_1323) / Math.max(0.001, var0.method_17939()),
         (var1.field_1351 - var0.field_1322) / Math.max(0.001, var0.method_17940()),
         (var1.field_1350 - var0.field_1321) / Math.max(0.001, var0.method_17941())
      );
   }

   public static class_243 W(class_238 var0, class_243 var1) {
      return g(
         var0,
         new class_243(
            var0.field_1323 + var1.field_1352 * var0.method_17939(),
            var0.field_1322 + var1.field_1351 * var0.method_17940(),
            var0.field_1321 + var1.field_1350 * var0.method_17941()
         )
      );
   }

   public static Optional<class_243> l(class_238 var0, class_243 var1, class_243 var2, class_243 var3, Predicate<class_243> var4) {
      class_243 var5 = g(var0, var2);
      if (var4.test(var5)) {
         return Optional.of(var5);
      }

      if (var3 != null) {
         var5 = W(var0, var3);
         if (var4.test(var5)) {
            return Optional.of(var5);
         }
      }

      var5 = g(var0, var1);
      if (var4.test(var5)) {
         return Optional.of(var5);
      }

      for (double var9 : new double[]{0.5, 0.9, 0.1}) {
         for (double[] var14 : new double[][]{{0.5, 0.5}, {0.1, 0.1}, {0.1, 0.9}, {0.9, 0.1}, {0.9, 0.9}}) {
            var5 = W(var0, new class_243(var14[0], var9, var14[1]));
            if (var4.test(var5)) {
               return Optional.of(var5);
            }
         }
      }

      return Optional.empty();
   }

   public static class_243 J(class_238 var0, class_243 var1, class_243 var2, double var3, Predicate<class_243> var5) {
      class_243 var6 = new class_243(var2.field_1352, 0.0, var2.field_1350).method_1021(var3);
      if (var6.method_1027() > 0.25) {
         var6 = var6.method_1029().method_1021(0.5);
      }

      class_243 var7 = g(var0, var1.method_1019(var6));
      return var5.test(var7) ? var7 : var1;
   }

   private s8() {
   }
}
