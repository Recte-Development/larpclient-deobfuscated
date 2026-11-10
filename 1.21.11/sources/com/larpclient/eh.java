package com.larpclient;

import net.minecraft.class_1041;
import org.joml.Matrix3x2f;

record eh(int width, int height, float scale) {
   static final int MIN_WIDTH = 640;
   static final int MIN_HEIGHT = 360;
   static final int PAIRED_WIDTH = 766;
   static final double MIN_PAIRED_UNIT = 1.5;

   static eh from(class_1041 var0) {
      return from(var0, 1.0);
   }

   static eh from(class_1041 var0, double var1) {
      return calculate(var0.method_4489(), var0.method_4506(), var0.method_4495(), var0.method_4476(2, false), var1);
   }

   static eh calculate(int var0, int var1, int var2, int var3) {
      return new eh(Math.max(1, Math.ceilDiv(var0, var3)), Math.max(1, Math.ceilDiv(var1, var3)), (float)var3 / var2);
   }

   static eh calculate(int var0, int var1, int var2, int var3, double var4) {
      if (var4 != 1.0 && Double.isFinite(var4)) {
         double var6 = Math.max(var3, Math.min(var0 / 640.0, var1 / 360.0));
         double var8 = Math.min(var3 * var4, var6);
         return new eh(Math.max(1, (int)Math.ceil(var0 / var8)), Math.max(1, (int)Math.ceil(var1 / var8)), (float)(var8 / var2));
      } else {
         return calculate(var0, var1, var2, var3);
      }
   }

   static eh paired(class_1041 var0, double var1) {
      return paired(from(var0, var1), var0.method_4489(), var0.method_4506(), var0.method_4495());
   }

   static eh paired(eh var0, int var1, int var2, int var3) {
      if (var0.width() >= 766) {
         return var0;
      }

      double var4 = var1 / 766.0;
      return !(var4 < 1.5) && !(var4 >= var0.scale() * var3) ? new eh(766, Math.max(1, (int)Math.ceil(var2 / var4)), (float)(var4 / var3)) : var0;
   }

   Matrix3x2f transform() {
      return new Matrix3x2f().scaling(this.scale);
   }
}
