package com.larpclient.module;

import net.minecraft.class_1309;
import net.minecraft.class_243;
import net.minecraft.class_3532;

public final class ap {
   private ap() {
   }

   public static class_243 f(class_1309 var0, double var1) {
      double var3 = class_3532.method_15350(var1, 0.0, 4.0);
      class_243 var5 = var0.method_73189();
      if (var3 <= 1.0E-4F) {
         return var5;
      }

      class_243 var6 = new class_243(var0.method_23317() - var0.field_6014, var0.method_23318() - var0.field_6036, var0.method_23321() - var0.field_5969);
      class_243 var7 = var6.method_1021(0.7F).method_1019(var0.method_18798().method_1021(0.3F));
      if (Double.isFinite(var7.field_1352) && Double.isFinite(var7.field_1351) && Double.isFinite(var7.field_1350) && !(var7.method_1027() < 1.0E-6F)) {
         if (var0.method_24828()) {
            var7 = new class_243(var7.field_1352, 0.0, var7.field_1350);
         }

         class_243 var8 = var7.method_1021(var3);
         double var9 = 6.0;
         if (var8.method_1027() > var9 * var9) {
            var8 = var8.method_1029().method_1021(var9);
         }

         return var5.method_1019(var8);
      } else {
         return var5;
      }
   }
}
