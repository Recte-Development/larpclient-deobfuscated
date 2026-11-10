package com.larpclient;

import java.util.List;
import java.util.Optional;
import net.minecraft.class_1753;
import net.minecraft.class_238;
import net.minecraft.class_243;

public final class cg {
   public static final double n = 4.0;
   public static final double v = 1.0;
   public static final double X = 0.1;

   public static double X(int var0) {
      return class_1753.method_7722(Math.max(0, var0)) * 3.0F;
   }

   public static double O(double var0, double var2) {
      return Double.isFinite(var0) && Double.isFinite(var2) ? 4.0 + 1.0 * Math.clamp(var2, 0.0, 1.0) * 1.5 * Math.min(Math.max(0.0, var0), 5.0) : 4.0;
   }

   public static double[] G(double var0) {
      return new double[]{O(var0, 0.0), O(var0, 0.5), O(var0, 1.0)};
   }

   public static double S(float[] var0) {
      return (var0[0] + 4.0F * var0[1] + var0[2]) / 6.0F;
   }

   public static boolean t(class_243 var0, class_243 var1, class_238 var2, List<class_238> var3) {
      if (var2.method_1006(var0)) {
         return false;
      }

      Optional var4 = var2.method_992(var0, var1);
      if (var4.isEmpty()) {
         return false;
      }

      double var5 = var0.method_1025((class_243)var4.get());

      for (class_238 var8 : var3) {
         if (var8.method_1006(var0)) {
            return false;
         }

         Optional var9 = var8.method_992(var0, var1);
         if (var9.isPresent() && var0.method_1025((class_243)var9.get()) <= var5) {
            return false;
         }
      }

      return true;
   }

   private cg() {
   }
}
