package com.larpclient.module;

import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_746;

public final class a_ {
   private a_() {
   }

   public static float i() {
      class_310 var0 = class_310.method_1551();
      if (var0 != null && var0.field_1690 != null && var0.field_1690.method_42495() != null) {
         double var1 = (Double)var0.field_1690.method_42495().method_41753();
         double var3 = var1 * 0.6F + 0.2F;
         return (float)(var3 * var3 * var3 * 1.2F);
      } else {
         return 0.00405F;
      }
   }

   public static float x(float var0, float var1) {
      float var2 = var1 - var0;

      while (var2 > 180.0F) {
         var2 -= 360.0F;
      }

      while (var2 < -180.0F) {
         var2 += 360.0F;
      }

      return var2;
   }

   public static float H(float var0, float var1) {
      return var0 + x(var0, var1);
   }

   public static l4 A(class_243 var0) {
      class_746 var1 = class_310.method_1551().field_1724;
      double var2 = var0.field_1352 - var1.method_23317();
      double var4 = var0.field_1351 - (var1.method_23318() + var1.method_18381(var1.method_18376()));
      double var6 = var0.field_1350 - var1.method_23321();
      double var8 = Math.sqrt(var2 * var2 + var6 * var6);
      float var10 = (float)Math.toDegrees(Math.atan2(var6, var2)) - 90.0F;
      float var11 = (float)(-Math.toDegrees(Math.atan2(var4, var8)));
      return new l4(var10, var11);
   }

   public static l4 Z(l4 var0, l4 var1) {
      float var2 = i();
      if (!(var2 <= 1.0E-6F) && Float.isFinite(var2)) {
         float var3 = x(var0.j, var1.j);
         float var4 = var1.l - var0.l;
         int var5 = Math.round(var3 / var2);
         int var6 = Math.round(var4 / var2);
         if (var5 == 0 && Math.abs(var3) >= var2 * 0.25F && Math.abs(var3) > 0.001F) {
            var5 = (int)Math.signum(var3);
         }

         if (var6 == 0 && Math.abs(var4) >= var2 * 0.25F && Math.abs(var4) > 0.001F) {
            var6 = (int)Math.signum(var4);
         }

         return new l4(var0.j + var5 * var2, class_3532.method_15363(var0.l + var6 * var2, -90.0F, 90.0F));
      } else {
         return var1;
      }
   }

   public static float r(float var0, float var1) {
      float var2 = i();
      return !(var2 <= 1.0E-6F) && Float.isFinite(var2) ? var0 + Math.round((var1 - var0) / var2) * var2 : var1;
   }

   public static class_243 w(float var0, float var1) {
      float var2 = var1 * (float) (Math.PI / 180.0);
      float var3 = -var0 * (float) (Math.PI / 180.0);
      float var4 = class_3532.method_15362(var3);
      float var5 = class_3532.method_15374(var3);
      float var6 = class_3532.method_15362(var2);
      float var7 = class_3532.method_15374(var2);
      return new class_243(var5 * var6, -var7, var4 * var6);
   }
}
